package dev.tingh.data.handler;

import com.google.gson.Gson;
import dev.tingh.data.model.InstrumentData;
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

public class InstrumentDataHandler {
    private static final Logger logger = LoggerFactory.getLogger(InstrumentDataHandler.class);

    private final String baseDirectory;
    private final Lock fileLock = new ReentrantLock();
    private final Gson gson = new Gson();
    private LocalDate currentFileDate;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public InstrumentDataHandler(String baseDirectory) {
        this.baseDirectory = baseDirectory;
        this.currentFileDate = LocalDate.now();

        // Create directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory for instrument data", e);
        }
    }

    // Keep the old method for backward compatibility
    public void handleInstrumentData(String jsonData) {
        try {
            InstrumentData instrumentData = gson.fromJson(jsonData, InstrumentData.class);
            handleInstrumentData(instrumentData);
        } catch (Exception e) {
            logger.error("Error processing instrument data from JSON: {}", e.getMessage(), e);
        }
    }

    // New method that accepts InstrumentData object
    public void handleInstrumentData(InstrumentData instrumentData) {
        try {
            if (instrumentData == null || instrumentData.getData() == null) {
                return;
            }

            InstrumentData.InstrumentSymbolData symbolData = instrumentData.getData();

            // Process assets if available
            if (symbolData.getAssets() != null && !symbolData.getAssets().isEmpty()) {
                for (InstrumentData.AssetDetail asset : symbolData.getAssets()) {
                    writeAssetDataToCsv(asset, instrumentData.getType());
                }
            }

            // Process pairs if available
            if (symbolData.getPairs() != null && !symbolData.getPairs().isEmpty()) {
                for (InstrumentData.PairDetail pair : symbolData.getPairs()) {
                    writePairDataToCsv(pair, instrumentData.getType());
                }
            }
        } catch (Exception e) {
            logger.error("Error processing instrument data: {}", e.getMessage(), e);
        }
    }

    private void writeAssetDataToCsv(InstrumentData.AssetDetail asset, String updateType) {
        LocalDate today = LocalDate.now();

        if (asset.getId() == null || asset.getId().isEmpty()) {
            logger.warn("Asset data missing id information");
            return;
        }

        try {
            fileLock.lock();

            // Check if we need to roll to a new file (new day)
            if (!today.equals(currentFileDate)) {
                currentFileDate = today;
            }

            // Create the file path with date-based naming
            String fileName = "instrument_assets_" + currentFileDate.format(dateFormatter) + ".csv";
            Path filePath = Paths.get(baseDirectory, fileName);

            // Create headers if file doesn't exist
            boolean fileExists = Files.exists(filePath);
            if (!fileExists) {
                String headers = "local_timestamp,update_type,asset_id,status," +
                        "precision,precision_display,borrowable,collateral_value,margin_rate\n";
                Files.writeString(filePath, headers, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            }

            // Format the data row
            String localTimestamp = LocalDateTime.now().format(timestampFormatter);

            String dataRow = String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                    localTimestamp,
                    updateType,
                    asset.getId(),
                    asset.getStatus(),
                    asset.getPrecision(),
                    asset.getPrecisionDisplay(),
                    asset.getBorrowable(),
                    asset.getCollateralValue(),
                    asset.getMarginRate()
            );

            // Append the data to the file
            Files.writeString(filePath, dataRow, StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("Error writing asset data to CSV: {}", e.getMessage(), e);
        } finally {
            fileLock.unlock();
        }
    }

    private void writePairDataToCsv(InstrumentData.PairDetail pair, String updateType) {
        LocalDate today = LocalDate.now();

        if (pair.getSymbol() == null || pair.getSymbol().isEmpty()) {
            logger.warn("Pair data missing symbol information");
            return;
        }

        try {
            fileLock.lock();

            // Check if we need to roll to a new file (new day)
            if (!today.equals(currentFileDate)) {
                currentFileDate = today;
            }

            // Create the file path with date-based naming
            String fileName = "instrument_pairs_" + currentFileDate.format(dateFormatter) + ".csv";
            Path filePath = Paths.get(baseDirectory, fileName);

            // Create headers if file doesn't exist
            boolean fileExists = Files.exists(filePath);
            if (!fileExists) {
                String headers = "local_timestamp,update_type,symbol,base,quote,status," +
                        "qty_precision,qty_increment,price_precision,cost_precision," +
                        "marginable,has_index,cost_min,margin_initial," +
                        "position_limit_long,position_limit_short," +
                        "tick_size,price_increment,qty_min\n";
                Files.writeString(filePath, headers, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            }

            // Format the data row
            String localTimestamp = LocalDateTime.now().format(timestampFormatter);

            String dataRow = String.format("%s,%s,%s,%s,%s,%s,%d,%.8f,%d,%d,%b,%b,%.5f,%.2f,%d,%d,%.8f,%.8f,%.8f\n",
                    localTimestamp,
                    updateType,
                    pair.getSymbol(),
                    pair.getBase(),
                    pair.getQuote(),
                    pair.getStatus(),
                    pair.getQtyPrecision(),
                    pair.getQtyIncrement(),
                    pair.getPricePrecision(),
                    pair.getCostPrecision(),
                    pair.isMarginable(),
                    pair.hasIndex(),
                    pair.getCostMin(),
                    pair.getMarginInitial(),
                    pair.getPositionLimitLong(),
                    pair.getPositionLimitShort(),
                    pair.getTickSize(),
                    pair.getPriceIncrement(),
                    pair.getQtyMin()
            );

            // Append the data to the file
            Files.writeString(filePath, dataRow, StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("Error writing pair data to CSV: {}", e.getMessage(), e);
        } finally {
            fileLock.unlock();
        }
    }
}
