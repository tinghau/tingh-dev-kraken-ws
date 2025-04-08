package dev.tingh.data.handler;

import com.google.gson.Gson;
import dev.tingh.data.OhlcData;
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

public class OhlcDataHandler {
    private static final Logger logger = LoggerFactory.getLogger(OhlcDataHandler.class);

    private final String baseDirectory;
    private final Lock fileLock = new ReentrantLock();
    private final Gson gson = new Gson();
    private LocalDate currentFileDate;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public OhlcDataHandler(String baseDirectory) {
        this.baseDirectory = baseDirectory;
        this.currentFileDate = LocalDate.now();

        // Create directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory for OHLC data", e);
        }
    }

    public void handleOhlcData(String jsonData) {
        try {
            OhlcData ohlcData = gson.fromJson(jsonData, OhlcData.class);

            if (ohlcData == null || ohlcData.getData() == null) {
                return;
            }

            // Process each symbol's data
            for (OhlcData.OhlcSymbolData symbolData : ohlcData.getData()) {
                writeOhlcDataToCsv(symbolData, ohlcData.getType());
            }
        } catch (Exception e) {
            logger.error("Error processing OHLC data: {}", e.getMessage(), e);
        }
    }

    private void writeOhlcDataToCsv(OhlcData.OhlcSymbolData symbolData, String updateType) {
        LocalDate today = LocalDate.now();
        String symbol = symbolData.getSymbol();
        OhlcData.OhlcDetails ohlc = symbolData.getOhlc();

        if (ohlc == null || ohlc.getCandles() == null || ohlc.getCandles().isEmpty()) {
            return;
        }

        try {
            fileLock.lock();

            // Check if we need to roll to a new file (new day)
            if (!today.equals(currentFileDate)) {
                currentFileDate = today;
            }

            // Create the file path with date-based naming, include interval in filename
            String interval = ohlc.getInterval();
            String fileName = symbol.replace("/", "_") + "_ohlc_" + interval + "_" + currentFileDate.format(dateFormatter) + ".csv";
            Path filePath = Paths.get(baseDirectory, fileName);

            // Create headers if file doesn't exist
            boolean fileExists = Files.exists(filePath);
            if (!fileExists) {
                String headers = "local_timestamp,symbol,update_type,interval,candle_timestamp,open,high,low,close,volume,trades\n";
                Files.writeString(filePath, headers, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            }

            // Format the data rows
            String localTimestamp = LocalDateTime.now().format(timestampFormatter);
            StringBuilder dataRows = new StringBuilder();

            for (OhlcData.Candle candle : ohlc.getCandles()) {
                // Convert candle timestamp to readable format
                String candleTimestamp = "";
                if (candle.getTimestamp() != null && !candle.getTimestamp().isEmpty()) {
                    candleTimestamp = Instant.ofEpochSecond(Long.parseLong(candle.getTimestamp()))
                            .atZone(ZoneId.systemDefault())
                            .format(timestampFormatter);
                }

                dataRows.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        localTimestamp,
                        symbol,
                        updateType,
                        interval,
                        candleTimestamp,
                        candle.getOpen(),
                        candle.getHigh(),
                        candle.getLow(),
                        candle.getClose(),
                        candle.getVolume(),
                        candle.getTrades()
                ));
            }

            // Append the data to the file
            Files.writeString(filePath, dataRows.toString(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("Error writing OHLC data to CSV: {}", e.getMessage(), e);
        } finally {
            fileLock.unlock();
        }
    }
}