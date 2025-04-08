package dev.tingh.data.handler;

import com.google.gson.Gson;
import dev.tingh.data.TradeData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    public void handleTradeData(String jsonData) {
        try {
            TradeData tradeData = gson.fromJson(jsonData, TradeData.class);

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
                String headers = "local_timestamp,symbol,update_type,trade_timestamp,price,volume,side,order_type,trade_id\n";
                Files.writeString(filePath, headers, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            }

            // Format the data row
            String localTimestamp = LocalDateTime.now().format(timestampFormatter);

            // Convert trade timestamp to readable format
            String tradeTimestamp = "";
            if (trade.getTimestamp() != null && !trade.getTimestamp().isEmpty()) {
                tradeTimestamp = Instant.ofEpochSecond(Long.parseLong(trade.getTimestamp()))
                        .atZone(ZoneId.systemDefault())
                        .format(timestampFormatter);
            }

            String dataRow = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                    localTimestamp,
                    symbol,
                    updateType,
                    tradeTimestamp,
                    trade.getPrice(),
                    trade.getVolume(),
                    trade.getSide(),
                    trade.getOrderType(),
                    trade.getTradeId()
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