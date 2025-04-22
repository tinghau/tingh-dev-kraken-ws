package dev.tingh.data.handler;

import com.google.gson.Gson;
import dev.tingh.data.model.BookData;
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

public class BookDataHandler {
    private static final Logger logger = LoggerFactory.getLogger(BookDataHandler.class);
    private static final int MAX_DEPTH = 10;

    private final String baseDirectory;
    private final Lock fileLock = new ReentrantLock();
    private final Gson gson = new Gson();
    private LocalDate currentFileDate;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    // Store the latest orderbook state for each symbol
    private final Map<String, OrderbookState> orderbookStates = new ConcurrentHashMap<>();

    public BookDataHandler(String baseDirectory) {
        this.baseDirectory = baseDirectory;
        this.currentFileDate = LocalDate.now();

        try {
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory for book data", e);
        }
    }

    // Keep the old method for backward compatibility
    public void handleBookData(String jsonData) {
        try {
            BookData bookData = gson.fromJson(jsonData, BookData.class);
            handleBookData(bookData);
        } catch (Exception e) {
            logger.error("Error processing book data from JSON: {}", e.getMessage(), e);
        }
    }

    // New method that accepts BookData object
    public void handleBookData(BookData bookData) {
        try {
            if (bookData == null || bookData.getData() == null) {
                return;
            }

            String updateType = bookData.getType();

            for (BookData.BookSymbolData bookSymbolData : bookData.getData()) {
                if (bookSymbolData == null) {
                    continue;
                }
                String symbol = bookSymbolData.getSymbol();

                // Get or create orderbook state for this symbol
                OrderbookState state = orderbookStates.computeIfAbsent(symbol, s -> new OrderbookState());

                // Process snapshot (replace the entire orderbook)
                if ("snapshot".equals(updateType)) {
                    processSnapshot(bookSymbolData, state);
                }
                // Process update (merge with existing orderbook)
                else if ("update".equals(updateType)) {
                    processUpdate(bookSymbolData, state);
                }

                // Write the updated orderbook to CSV
                writeBookDataToCsv(symbol, state, updateType);
            }
        } catch (Exception e) {
            logger.error("Error processing book data: {}", e.getMessage(), e);
        }
    }

    private void processSnapshot(BookData.BookSymbolData book, OrderbookState state) {
        state.bids.clear();
        state.asks.clear();

        if (book.getBids() != null) {
            for (BookData.PriceLevel level : book.getBids()) {
                state.bids.put(level.getPrice(), level);
            }
        }

        if (book.getAsks() != null) {
            for (BookData.PriceLevel level : book.getAsks()) {
                state.asks.put(level.getPrice(), level);
            }
        }
    }

    private void processUpdate(BookData.BookSymbolData book, OrderbookState state) {
        // Process bid updates
        if (book.getBids() != null) {
            for (BookData.PriceLevel level : book.getBids()) {
                String price = level.getPrice();
                if ("0".equals(level.getQty())) {
                    // Remove the price level
                    state.bids.remove(price);
                } else {
                    // Add or update the price level
                    state.bids.put(price, level);
                }
            }
        }

        // Process ask updates
        if (book.getAsks() != null) {
            for (BookData.PriceLevel level : book.getAsks()) {
                String price = level.getPrice();
                if ("0".equals(level.getQty())) {
                    // Remove the price level
                    state.asks.remove(price);
                } else {
                    // Add or update the price level
                    state.asks.put(price, level);
                }
            }
        }
    }

    private void writeBookDataToCsv(String symbol, OrderbookState state, String updateType) {
        LocalDate today = LocalDate.now();

        try {
            fileLock.lock();

            // Check if we need to roll to a new file (new day)
            if (!today.equals(currentFileDate)) {
                currentFileDate = today;
            }

            // Create the file path with date-based naming
            String fileName = symbol.replace("/", "_") + "_book_" + currentFileDate.format(dateFormatter) + ".csv";
            Path filePath = Paths.get(baseDirectory, fileName);

            // Create headers if file doesn't exist
            boolean fileExists = Files.exists(filePath);
            if (!fileExists) {
                String headers = "timestamp,symbol,update_type," +
                        buildPriceLevelHeaders("bid", MAX_DEPTH) + "," +
                        buildPriceLevelHeaders("ask", MAX_DEPTH) + "\n";
                Files.writeString(filePath, headers, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            }

            // Get top N bids (highest to lowest)
            List<BookData.PriceLevel> topBids = getTopN(state.bids.values(), MAX_DEPTH, true);

            // Get top N asks (lowest to highest)
            List<BookData.PriceLevel> topAsks = getTopN(state.asks.values(), MAX_DEPTH, false);

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
            logger.error("Error writing book data to CSV: {}", e.getMessage(), e);
        } finally {
            fileLock.unlock();
        }
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

    private List<BookData.PriceLevel> getTopN(Collection<BookData.PriceLevel> levels, int n, boolean descending) {
        Comparator<BookData.PriceLevel> comparator = Comparator.comparing(
                level -> Double.parseDouble(level.getPrice())
        );

        if (descending) {
            comparator = comparator.reversed();
        }

        return levels.stream()
                .sorted(comparator)
                .limit(n)
                .toList();
    }

    private void appendPriceLevels(StringBuilder sb, List<BookData.PriceLevel> levels, int maxDepth) {
        for (int i = 0; i < maxDepth; i++) {
            sb.append(",");

            if (i < levels.size()) {
                BookData.PriceLevel level = levels.get(i);
                sb.append(level.getPrice()).append(",")
                        .append(level.getQty());
            } else {
                sb.append(","); // Empty price and volume for missing levels
            }
        }
    }

    private static class OrderbookState {
        // Using TreeMap for bids (price -> level) - sorted by price
        private final Map<String, BookData.PriceLevel> bids = new HashMap<>();

        // Using TreeMap for asks (price -> level) - sorted by price
        private final Map<String, BookData.PriceLevel> asks = new HashMap<>();
    }
}
