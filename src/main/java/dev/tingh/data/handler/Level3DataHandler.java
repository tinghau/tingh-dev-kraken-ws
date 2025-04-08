package dev.tingh.data.handler;

import com.google.gson.Gson;
import dev.tingh.data.Level3Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class Level3DataHandler {
    private static final Logger logger = LoggerFactory.getLogger(Level3DataHandler.class);
    private static final int MAX_DEPTH = 10;

    private final String baseDirectory;
    private final Lock fileLock = new ReentrantLock();
    private final Gson gson = new Gson();
    private LocalDate currentFileDate;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    // Map from symbol -> OrderbookState
    private final Map<String, OrderbookState> orderBooks = new ConcurrentHashMap<>();

    public Level3DataHandler(String baseDirectory) {
        this.baseDirectory = baseDirectory;
        this.currentFileDate = LocalDate.now();

        // Create directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory for level3 data", e);
        }
    }

    public void handleLevel3Data(String jsonData) {
        try {
            Level3Data level3Data = gson.fromJson(jsonData, Level3Data.class);

            if (level3Data == null || level3Data.getData() == null) {
                return;
            }

            // Process each symbol's data
            for (Level3Data.Level3SymbolData symbolData : level3Data.getData()) {
                processLevel3Data(symbolData, level3Data.getType());
                writeLevel3DataToCsv(symbolData, level3Data.getType());
            }
        } catch (Exception e) {
            logger.error("Error processing level3 data: {}", e.getMessage(), e);
        }
    }

    private void processLevel3Data(Level3Data.Level3SymbolData symbolData, String updateType) {
        String symbol = symbolData.getSymbol();
        Level3Data.Level3Details level3 = symbolData.getLevel3();

        if (level3 == null || level3.getUpdates() == null || level3.getUpdates().isEmpty()) {
            return;
        }

        // Get or create order book for this symbol
        OrderbookState orderBook = orderBooks.computeIfAbsent(symbol, s -> new OrderbookState());

        // Process each update
        for (Level3Data.OrderUpdate update : level3.getUpdates()) {
            String orderId = update.getOrderId();
            String price = update.getPrice();
            String volume = update.getVolume();
            String side = update.getSide();
            String updateType2 = update.getUpdateType();

            // Process based on update type
            switch (updateType2) {
                case "add":
                    processAddOrder(orderBook, orderId, price, volume, side);
                    break;
                case "update":
                    processUpdateOrder(orderBook, orderId, price, volume, side);
                    break;
                case "remove":
                    processRemoveOrder(orderBook, orderId, side);
                    break;
                case "trade":
                    // Trades don't directly affect the order book state
                    break;
                default:
                    logger.warn("Unknown update type: {}", updateType2);
            }
        }
    }

    private void processAddOrder(OrderbookState orderBook, String orderId, String price, String volume, String side) {
        Order order = new Order(orderId, price, volume);

        if ("buy".equalsIgnoreCase(side)) {
            orderBook.bidOrders.put(orderId, order);
            orderBook.bidsByPrice.computeIfAbsent(price, p -> new HashSet<>()).add(orderId);
        } else if ("sell".equalsIgnoreCase(side)) {
            orderBook.askOrders.put(orderId, order);
            orderBook.asksByPrice.computeIfAbsent(price, p -> new HashSet<>()).add(orderId);
        }
    }

    private void processUpdateOrder(OrderbookState orderBook, String orderId, String price, String volume, String side) {
        if ("buy".equalsIgnoreCase(side)) {
            Order existingOrder = orderBook.bidOrders.get(orderId);
            if (existingOrder != null) {
                // Remove from old price level if price changed
                if (!existingOrder.price.equals(price)) {
                    Set<String> orderIds = orderBook.bidsByPrice.get(existingOrder.price);
                    if (orderIds != null) {
                        orderIds.remove(orderId);
                        if (orderIds.isEmpty()) {
                            orderBook.bidsByPrice.remove(existingOrder.price);
                        }
                    }
                    // Add to new price level
                    orderBook.bidsByPrice.computeIfAbsent(price, p -> new HashSet<>()).add(orderId);
                }
                // Update order details
                existingOrder.price = price;
                existingOrder.volume = volume;
            }
        } else if ("sell".equalsIgnoreCase(side)) {
            Order existingOrder = orderBook.askOrders.get(orderId);
            if (existingOrder != null) {
                // Remove from old price level if price changed
                if (!existingOrder.price.equals(price)) {
                    Set<String> orderIds = orderBook.asksByPrice.get(existingOrder.price);
                    if (orderIds != null) {
                        orderIds.remove(orderId);
                        if (orderIds.isEmpty()) {
                            orderBook.asksByPrice.remove(existingOrder.price);
                        }
                    }
                    // Add to new price level
                    orderBook.asksByPrice.computeIfAbsent(price, p -> new HashSet<>()).add(orderId);
                }
                // Update order details
                existingOrder.price = price;
                existingOrder.volume = volume;
            }
        }
    }

    private void processRemoveOrder(OrderbookState orderBook, String orderId, String side) {
        if ("buy".equalsIgnoreCase(side)) {
            Order order = orderBook.bidOrders.remove(orderId);
            if (order != null) {
                Set<String> orderIds = orderBook.bidsByPrice.get(order.price);
                if (orderIds != null) {
                    orderIds.remove(orderId);
                    if (orderIds.isEmpty()) {
                        orderBook.bidsByPrice.remove(order.price);
                    }
                }
            }
        } else if ("sell".equalsIgnoreCase(side)) {
            Order order = orderBook.askOrders.remove(orderId);
            if (order != null) {
                Set<String> orderIds = orderBook.asksByPrice.get(order.price);
                if (orderIds != null) {
                    orderIds.remove(orderId);
                    if (orderIds.isEmpty()) {
                        orderBook.asksByPrice.remove(order.price);
                    }
                }
            }
        }
    }

    private void writeLevel3DataToCsv(Level3Data.Level3SymbolData symbolData, String updateType) {
        LocalDate today = LocalDate.now();
        String symbol = symbolData.getSymbol();
        Level3Data.Level3Details level3 = symbolData.getLevel3();

        if (level3 == null || level3.getUpdates() == null || level3.getUpdates().isEmpty()) {
            return;
        }

        try {
            fileLock.lock();

            // Check if we need to roll to a new file (new day)
            if (!today.equals(currentFileDate)) {
                currentFileDate = today;
            }

            // Get order book state
            OrderbookState orderBook = orderBooks.get(symbol);
            if (orderBook == null) {
                return;
            }

            // Get top N bids (highest to lowest)
            List<PriceLevel> topBids = aggregateAndGetTop(orderBook.bidOrders, orderBook.bidsByPrice, true);

            // Get top N asks (lowest to highest)
            List<PriceLevel> topAsks = aggregateAndGetTop(orderBook.askOrders, orderBook.asksByPrice, false);

            // Create the file path with date-based naming
            String fileName = symbol.replace("/", "_") + "_level3_" + currentFileDate.format(dateFormatter) + ".csv";
            Path filePath = Paths.get(baseDirectory, fileName);

            // Create headers if file doesn't exist
            boolean fileExists = Files.exists(filePath);
            if (!fileExists) {
                String headers = "timestamp,symbol,update_type," +
                        buildPriceLevelHeaders("bid", MAX_DEPTH) + "," +
                        buildPriceLevelHeaders("ask", MAX_DEPTH) + "\n";
                Files.writeString(filePath, headers, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            }

            // Format the data row
            String timestamp = LocalDateTime.now().format(timestampFormatter);
            StringBuilder dataRow = new StringBuilder();
            dataRow.append(timestamp).append(",")
                    .append(symbol).append(",")
                    .append(updateType);

            // Add bid data
            appendPriceLevels(dataRow, topBids, MAX_DEPTH);

            // Add ask data
            appendPriceLevels(dataRow, topAsks, MAX_DEPTH);

            dataRow.append("\n");

            // Append the data to the file
            Files.writeString(filePath, dataRow.toString(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("Error writing level3 data to CSV: {}", e.getMessage(), e);
        } finally {
            fileLock.unlock();
        }
    }

    private List<PriceLevel> aggregateAndGetTop(Map<String, Order> orders, Map<String, Set<String>> ordersByPrice, boolean descending) {
        // Aggregate by price
        Map<String, PriceLevel> priceLevels = new HashMap<>();

        for (Map.Entry<String, Set<String>> entry : ordersByPrice.entrySet()) {
            String price = entry.getKey();
            Set<String> orderIds = entry.getValue();

            double totalVolume = 0.0;
            for (String orderId : orderIds) {
                Order order = orders.get(orderId);
                if (order != null) {
                    totalVolume += Double.parseDouble(order.volume);
                }
            }

            priceLevels.put(price, new PriceLevel(price, String.valueOf(totalVolume)));
        }

        // Sort and get top N
        Comparator<PriceLevel> comparator = Comparator.comparing(
                level -> Double.parseDouble(level.price)
        );

        if (descending) {
            comparator = comparator.reversed();
        }

        return priceLevels.values().stream()
                .sorted(comparator)
                .limit(MAX_DEPTH)
                .collect(Collectors.toList());
    }

    private String buildPriceLevelHeaders(String side, int depth) {
        StringBuilder headers = new StringBuilder();
        for (int i = 0; i < depth; i++) {
            if (i > 0) headers.append(",");
            headers.append(side).append(i+1).append("_price,")
                    .append(side).append(i+1).append("_volume");
        }
        return headers.toString();
    }

    private void appendPriceLevels(StringBuilder sb, List<PriceLevel> levels, int maxDepth) {
        for (int i = 0; i < maxDepth; i++) {
            sb.append(",");

            if (i < levels.size()) {
                PriceLevel level = levels.get(i);
                sb.append(level.price).append(",")
                        .append(level.volume);
            } else {
                sb.append(","); // Empty price and volume for missing levels
            }
        }
    }

    private static class OrderbookState {
        // Maps order ID to order details
        private final Map<String, Order> bidOrders = new HashMap<>();
        private final Map<String, Order> askOrders = new HashMap<>();

        // Maps price to set of order IDs at that price
        private final Map<String, Set<String>> bidsByPrice = new HashMap<>();
        private final Map<String, Set<String>> asksByPrice = new HashMap<>();
    }

    private static class Order {
        private String orderId;
        private String price;
        private String volume;

        public Order(String orderId, String price, String volume) {
            this.orderId = orderId;
            this.price = price;
            this.volume = volume;
        }
    }

    private static class PriceLevel {
        private String price;
        private String volume;

        public PriceLevel(String price, String volume) {
            this.price = price;
            this.volume = volume;
        }
    }
}