package dev.tingh.data.handler;

import com.google.gson.Gson;
import dev.tingh.client.KrakenAdminClient;
import dev.tingh.data.TickerData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TickerDataHandler {
    private static final Logger logger = LoggerFactory.getLogger(TickerDataHandler.class);

    private final String baseDirectory;
    private final Lock fileLock = new ReentrantLock();
    private final Gson gson = new Gson();
    private LocalDate currentFileDate;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public TickerDataHandler(String baseDirectory) {
        this.baseDirectory = baseDirectory;
        this.currentFileDate = LocalDate.now();

        // Create directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory for ticker data", e);
        }
    }

    public void handleTickerData(String jsonData) {
        try {
            TickerData tickerData = gson.fromJson(jsonData, TickerData.class);

            if (tickerData == null || tickerData.getData() == null) {
                return;
            }

            // Process each symbol's data
            for (TickerData.TickerSymbolData symbolData : tickerData.getData()) {
                writeTickerDataToCsv(symbolData, tickerData.getType());
            }
        } catch (Exception e) {
            logger.error("Error processing ticker data: {}", e.getMessage(), e);
        }
    }

    private void writeTickerDataToCsv(TickerData.TickerSymbolData symbolData, String updateType) {
        LocalDate today = LocalDate.now();
        String symbol = symbolData.getSymbol();
        TickerData.TickerDetails ticker = symbolData.getTicker();

        if (ticker == null) {
            return;
        }

        try {
            fileLock.lock();

            // Check if we need to roll to a new file (new day)
            if (!today.equals(currentFileDate)) {
                currentFileDate = today;
            }

            // Create the file path with date-based naming
            String fileName = symbol.replace("/", "_") + "_ticker_" + currentFileDate.format(dateFormatter) + ".csv";
            Path filePath = Paths.get(baseDirectory, fileName);

            // Create headers if file doesn't exist
            boolean fileExists = Files.exists(filePath);
            if (!fileExists) {
                String headers = "timestamp,symbol,update_type,ask_price,ask_volume,bid_price,bid_volume," +
                        "close_price,high_price,low_price,open_price,vwap,num_trades,volume\n";
                Files.writeString(filePath, headers, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            }

            // Format the data row
            String timestamp = LocalDateTime.now().format(timestampFormatter);
            String dataRow = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                    timestamp,
                    symbol,
                    updateType,
                    ticker.getAskPrice(),
                    ticker.getAskVolume(),
                    ticker.getBidPrice(),
                    ticker.getBidVolume(),
                    ticker.getClosePrice(),
                    ticker.getHighPrice(),
                    ticker.getLowPrice(),
                    ticker.getOpenPrice(),
                    ticker.getVwap(),
                    ticker.getNumberOfTrades(),
                    ticker.getVolume()
            );

            // Append the data to the file
            Files.writeString(filePath, dataRow, StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("Error writing ticker data to CSV: {}", e.getMessage(), e);
        } finally {
            fileLock.unlock();
        }
    }
}