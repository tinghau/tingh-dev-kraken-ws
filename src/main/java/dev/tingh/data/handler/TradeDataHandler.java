package dev.tingh.data.handler;

import com.google.gson.Gson;
import dev.tingh.data.model.TradeData;
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
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TradeDataHandler {
    private static final Logger logger = LoggerFactory.getLogger(TradeDataHandler.class);

    private final String baseDirectory;
    private final Lock fileLock = new ReentrantLock();
    private final Gson gson = new Gson();
    private LocalDate currentFileDate;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public TradeDataHandler(String baseDirectory) {
        this.baseDirectory = baseDirectory;
        this.currentFileDate = LocalDate.now();

        // Create directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory for trade data", e);
        }
    }

    // Keep the old method for backward compatibility
    public void handleTradeData(String jsonData) {
        try {
            TradeData tradeData = gson.fromJson(jsonData, TradeData.class);
            handleTradeData(tradeData);
        } catch (Exception e) {
            logger.error("Error processing trade data from JSON: {}", e.getMessage(), e);
        }
    }

    // New method that accepts TradeData object
    public void handleTradeData(TradeData tradeData) {
        try {
            if (tradeData == null || tradeData.getTrades() == null) {
                return;
            }

            // Process each trade
            for (TradeData.Trade trade : tradeData.getTrades()) {
                writeTradeDataToCsv(trade, tradeData.getType());
            }
        } catch (Exception e) {
            logger.error("Error processing trade data: {}", e.getMessage(), e);
        }
    }

    private void writeTradeDataToCsv(TradeData.Trade trade, String updateType) {
        LocalDate today = LocalDate.now();
        String symbol = trade.getSymbol();

        if (symbol == null || symbol.isEmpty()) {
            logger.warn("Trade data missing symbol information");
            return;
        }

        try {
            fileLock.lock();

            // Check if we need to roll to a new file (new day)
            if (!today.equals(currentFileDate)) {
                currentFileDate = today;
            }

            // Create the file path with date-based naming
            String fileName = symbol.replace("/", "_") + "_trade_" + currentFileDate.format(dateFormatter) + ".csv";
            Path filePath = Paths.get(baseDirectory, fileName);

            // Create headers if file doesn't exist
            boolean fileExists = Files.exists(filePath);
            if (!fileExists) {
                String headers = "local_timestamp,symbol,update_type,side,price,qty,ord_type,trade_id,trade_timestamp\n";
                Files.writeString(filePath, headers, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            }

            // Format the data row
            String localTimestamp = LocalDateTime.now().format(timestampFormatter);

            String dataRow = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                    localTimestamp,
                    symbol,
                    updateType,
                    trade.getSide(),
                    trade.getPrice(),
                    trade.getQty(),
                    trade.getOrdType(),
                    trade.getTradeId(),
                    trade.getTimestamp()
            );

            // Append the data to the file
            Files.writeString(filePath, dataRow, StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("Error writing trade data to CSV: {}", e.getMessage(), e);
        } finally {
            fileLock.unlock();
        }
    }
}
