package dev.tingh.data.handler;

import com.google.gson.Gson;
import dev.tingh.data.model.OhlcData;
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

    // Keep the old method for backward compatibility
    public void handleOhlcData(String jsonData) {
        try {
            OhlcData ohlcData = gson.fromJson(jsonData, OhlcData.class);
            handleOhlcData(ohlcData);
        } catch (Exception e) {
            logger.error("Error processing OHLC data from JSON: {}", e.getMessage(), e);
        }
    }

    // New method that accepts OhlcData object
    public void handleOhlcData(OhlcData ohlcData) {
        try {
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

    private void writeOhlcDataToCsv(OhlcData.OhlcSymbolData symbolData, String updateType) throws IOException {
        LocalDate today = LocalDate.now();

        try {
            fileLock.lock();

            // Check if we need to roll to a new file (new day)
            if (!today.equals(currentFileDate)) {
                currentFileDate = today;
            }

            // Create the file path with date-based naming, include interval in filename
            String interval = symbolData.getInterval();
            String fileName = symbolData.getSymbol().replace("/", "_") + "_ohlc_" + interval + "_" + currentFileDate.format(dateFormatter) + ".csv";
            Path filePath = Paths.get(baseDirectory, fileName);

            // Create headers if file doesn't exist
            boolean fileExists = Files.exists(filePath);
            if (!fileExists) {
                String headers = "local_timestamp,symbol,update_type,open,high,low,close,trades,volume,vwap,interval_begin,interval,timestamp\n";
                Files.writeString(filePath, headers, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            }

            // Format the data rows
            String localTimestamp = LocalDateTime.now().format(timestampFormatter);
            StringBuilder dataRows = new StringBuilder();

            dataRows.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                    localTimestamp,
                    symbolData.getSymbol(),
                    updateType,
                    symbolData.getOpen(),
                    symbolData.getHigh(),
                    symbolData.getLow(),
                    symbolData.getClose(),
                    symbolData.getTrades(),
                    symbolData.getVolume(),
                    symbolData.getVwap(),
                    symbolData.getIntervalBegin(),
                    symbolData.getInterval(),
                    symbolData.getTimestamp()
            ));
            // Append the data to the file
            Files.writeString(filePath, dataRows.toString(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("Error writing OHLC data to CSV: {}", e.getMessage(), e);
        } finally {
            fileLock.unlock();
        }
    }
}
