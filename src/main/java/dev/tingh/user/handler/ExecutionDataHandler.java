package dev.tingh.user.handler;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.tingh.user.model.ExecutionData;
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

public class ExecutionDataHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExecutionDataHandler.class);

    private final String baseDirectory;
    private final Lock fileLock = new ReentrantLock();

    private LocalDate currentFileDate;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public ExecutionDataHandler(String baseDirectory) {
        this.baseDirectory = baseDirectory;
        this.currentFileDate = LocalDate.now();

        // Create directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory for execution data", e);
        }
    }

    public void handleExecutionData(ExecutionData executionData) {
        try {
            if (executionData == null || executionData.getData() == null) {
                return;
            }

            writeExecutionDataToCsv(executionData);
        } catch (Exception e) {
            logger.error("Error processing execution data: {}", e.getMessage(), e);
        }
    }

    private void writeExecutionDataToCsv(ExecutionData executionData) {
        LocalDate today = LocalDate.now();

        try {
            fileLock.lock();

            // Check if we need to roll to a new file (new day)
            if (!today.equals(currentFileDate)) {
                currentFileDate = today;
            }

            // Create the file path with date-based naming
            String fileName = "executions_" + currentFileDate.format(dateFormatter) + ".csv";
            Path filePath = Paths.get(baseDirectory, fileName);

            // Create headers if file doesn't exist
            boolean fileExists = Files.exists(filePath);
            if (!fileExists) {
                String headers = "local_timestamp,channel,type,order_id,order_userref,symbol,order_qty,cum_cost," +
                        "time_in_force,exec_type,side,order_type,limit_price_type,limit_price,stop_price,order_status," +
                        "fee_usd_equiv,fee_ccy_pref,timestamp\n";
                Files.writeString(filePath, headers, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            }

            // Format the data rows
            String localTimestamp = LocalDateTime.now().format(timestampFormatter);
            String channel = executionData.getChannel();
            String type = executionData.getType();

            StringBuilder dataRows = new StringBuilder();

            for (ExecutionData.ExecutionItem item : executionData.getData()) {
                dataRows.append(String.format("%s,%s,%s,%s,%d,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        localTimestamp,
                        channel,
                        type,
                        item.getOrderId(),
                        item.getOrderUserref(),
                        item.getSymbol(),
                        item.getOrderQty(),
                        item.getCumCost(),
                        item.getTimeInForce(),
                        item.getExecType(),
                        item.getSide(),
                        item.getOrderType(),
                        item.getLimitPriceType(),
                        item.getLimitPrice(),
                        item.getStopPrice(),
                        item.getOrderStatus(),
                        item.getFeeUsdEquiv(),
                        item.getFeeCcyPref(),
                        item.getTimestamp()
                ));
            }

            // Append the data to the file
            Files.writeString(filePath, dataRows.toString(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("Error writing execution data to CSV: {}", e.getMessage(), e);
        } finally {
            fileLock.unlock();
        }
    }
}