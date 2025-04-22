package dev.tingh.data.handler;

import com.google.gson.Gson;
import dev.tingh.data.model.OrderData;
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
import java.util.concurrent.locks.ReentrantLock;

public class OrderDataHandler {
    private static final Logger logger = LoggerFactory.getLogger(OrderDataHandler.class);
    private static final int MAX_ORDERS_TO_WRITE = 10;
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    private final String baseDirectory;
    private final ReentrantLock fileLock = new ReentrantLock();
    private final Gson gson = new Gson();

    private LocalDate currentFileDate = LocalDate.now();

    // Use maps to store active orders
    private final Map<String, Map<String, OrderData.OrderUpdate>> symbolBidOrders = new HashMap<>();
    private final Map<String, Map<String, OrderData.OrderUpdate>> symbolAskOrders = new HashMap<>();

    public OrderDataHandler(String baseDirectory) {
        this.baseDirectory = baseDirectory;
        // Create base directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            logger.error("Failed to create base directory: {}", e.getMessage(), e);
        }
    }

    public void handleOrderData(OrderData orderData) {
        if (orderData == null || orderData.getData() == null) {
            return;
        }

        try {
            for (OrderData.OrderSymbolData symbolData : orderData.getData()) {
                if (symbolData == null) {
                    continue;
                }

                String symbol = symbolData.getSymbol();

                // Process bid updates
                if (symbolData.getBids() != null) {
                    processOrders(symbol, symbolData.getBids(), true);
                }

                // Process ask updates
                if (symbolData.getAsks() != null) {
                    processOrders(symbol, symbolData.getAsks(), false);
                }

                // Write updated state to CSV
                writeOrderBookToCsv(symbol);
            }
        } catch (Exception e) {
            logger.error("Error processing order data: {}", e.getMessage(), e);
        }
    }
    
    // For backward compatibility, keep a JSON string handler that parses the JSON
    public void handleOrderData(String jsonData) {
        OrderData orderData = gson.fromJson(jsonData, OrderData.class);
        handleOrderData(orderData);
    }

    private void processOrders(String symbol, List<OrderData.OrderUpdate> updates, boolean isBid) {
        Map<String, Map<String, OrderData.OrderUpdate>> orderMap = isBid ? symbolBidOrders : symbolAskOrders;

        // Get or create order map for this symbol
        Map<String, OrderData.OrderUpdate> orders = orderMap.computeIfAbsent(symbol, k -> new HashMap<>());

        for (OrderData.OrderUpdate update : updates) {
            String orderId = update.getOrderId();
            String event = update.getEvent();

            if ("add".equals(event) || "modify".equals(event) || event == null) {
                // Add or update the order
                orders.put(orderId, update);
            } else if ("delete".equals(event)) {
                // Remove the order
                orders.remove(orderId);
            }
        }
    }

    private void writeOrderBookToCsv(String symbol) {
        LocalDate today = LocalDate.now();

        try {
            fileLock.lock();

            // Check if we need to roll to a new file (new day)
            if (!today.equals(currentFileDate)) {
                currentFileDate = today;
            }

            // Create the file path with date-based naming
            String fileName = symbol.replace("/", "_") + "_order_" + currentFileDate.format(dateFormatter) + ".csv";
            Path filePath = Paths.get(baseDirectory, fileName);

            // Create headers if file doesn't exist
            boolean fileExists = Files.exists(filePath);
            if (!fileExists) {
                String headers = "timestamp,symbol," +
                        "bid_order_count,ask_order_count," +
                        buildOrderHeaders("bid", MAX_ORDERS_TO_WRITE) + "," +
                        buildOrderHeaders("ask", MAX_ORDERS_TO_WRITE) + "\n";
                Files.writeString(filePath, headers, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            }

            // Get maps for this symbol
            Map<String, OrderData.OrderUpdate> bidOrders = symbolBidOrders.getOrDefault(symbol, Collections.emptyMap());
            Map<String, OrderData.OrderUpdate> askOrders = symbolAskOrders.getOrDefault(symbol, Collections.emptyMap());

            // Sort bids (highest to lowest)
            List<OrderData.OrderUpdate> sortedBids = sortOrders(bidOrders.values(), true);

            // Sort asks (lowest to highest)
            List<OrderData.OrderUpdate> sortedAsks = sortOrders(askOrders.values(), false);

            // Format the data row
            String timestamp = LocalDateTime.now().format(timestampFormatter);
            StringBuilder dataRow = new StringBuilder();
            dataRow.append(timestamp).append(",")
                    .append(symbol).append(",")
                    .append(bidOrders.size()).append(",")
                    .append(askOrders.size());

            // Add bid orders
            appendOrders(dataRow, sortedBids, MAX_ORDERS_TO_WRITE);

            // Add ask orders
            appendOrders(dataRow, sortedAsks, MAX_ORDERS_TO_WRITE);

            dataRow.append("\n");

            // Append the data to the file
            Files.writeString(filePath, dataRow.toString(), StandardOpenOption.APPEND);

        } catch (IOException e) {
            logger.error("Error writing order data to CSV: {}", e.getMessage(), e);
        } finally {
            fileLock.unlock();
        }
    }

    private String buildOrderHeaders(String side, int count) {
        StringBuilder headers = new StringBuilder();
        for (int i = 0; i < count; i++) {
            if (i > 0) headers.append(",");
            headers.append(side).append(i+1).append("_id,")
                    .append(side).append(i+1).append("_price,")
                    .append(side).append(i+1).append("_qty,");
        }
        return headers.toString();
    }

    private List<OrderData.OrderUpdate> sortOrders(Collection<OrderData.OrderUpdate> orders, boolean descending) {
        Comparator<OrderData.OrderUpdate> comparator = Comparator.comparing(
                order -> Double.parseDouble(order.getLimitPrice())
        );

        if (descending) {
            comparator = comparator.reversed();
        }

        return orders.stream()
                .sorted(comparator)
                .limit(MAX_ORDERS_TO_WRITE)
                .toList();
    }

    private void appendOrders(StringBuilder sb, List<OrderData.OrderUpdate> orders, int maxOrders) {
        for (int i = 0; i < maxOrders; i++) {
            sb.append(",");

            if (i < orders.size()) {
                OrderData.OrderUpdate order = orders.get(i);
                sb.append(order.getOrderId()).append(",")
                        .append(order.getLimitPrice()).append(",")
                        .append(order.getOrderQty());
            } else {
                sb.append(",,,"); // Empty id, price, qty and event for missing orders
            }
        }
    }
}
