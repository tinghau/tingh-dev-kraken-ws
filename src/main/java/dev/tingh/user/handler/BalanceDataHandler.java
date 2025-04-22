package dev.tingh.user.handler;

import com.google.gson.Gson;
import dev.tingh.user.model.BalanceData;
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

public class BalanceDataHandler {
    private static final Logger logger = LoggerFactory.getLogger(BalanceDataHandler.class);

    private final String baseDirectory;
    private final Lock fileLock = new ReentrantLock();
    private final Gson gson = new Gson();
    private LocalDate currentFileDate;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
    private final DateTimeFormatter timestampFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public BalanceDataHandler(String baseDirectory) {
        this.baseDirectory = baseDirectory;
        this.currentFileDate = LocalDate.now();

        // Create directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(baseDirectory));
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directory for balances data", e);
        }
    }

    public void handleBalancesData(BalanceData balancesData) {
        try {
            if (balancesData == null || balancesData.getData() == null) {
                return;
            }

            writeBalancesDataToCsv(balancesData);
        } catch (Exception e) {
            logger.error("Error processing balances data: {}", e.getMessage(), e);
        }
    }

    private void writeBalancesDataToCsv(BalanceData balancesData) {
        LocalDate today = LocalDate.now();

        try {
            fileLock.lock();

            // Check if we need to roll to a new file (new day)
            if (!today.equals(currentFileDate)) {
                currentFileDate = today;
            }

            // Create the file path with date-based naming
            String fileName = "balances_" + currentFileDate.format(dateFormatter) + ".csv";
            Path filePath = Paths.get(baseDirectory, fileName);

            // Create headers if file doesn't exist
            boolean fileExists = Files.exists(filePath);
            if (!fileExists) {
                String headers = "local_timestamp,channel,type,asset,asset_class,balance,wallet_type,wallet_id,wallet_balance\n";
                Files.writeString(filePath, headers, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            }

            // Format the data rows
            String localTimestamp = LocalDateTime.now().format(timestampFormatter);
            String channel = balancesData.getChannel();
            String type = balancesData.getType();

            StringBuilder dataRows = new StringBuilder();

            for (BalanceData.BalanceItem item : balancesData.getData()) {
                String asset = item.getAsset();
                String assetClass = item.getAssetClass();
                String balance = item.getBalance();

                // If the balance item has wallet details
                if (item.getWallet() != null && !item.getWallet().isEmpty()) {
                    for (BalanceData.WalletItem wallet : item.getWallet()) {
                        dataRows.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                                localTimestamp,
                                channel,
                                type,
                                asset,
                                assetClass,
                                balance,
                                wallet.getType(),
                                wallet.getId(),
                                wallet.getBalance()
                        ));
                    }
                } else {
                    // If no wallet details, just write the main balance data
                    dataRows.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                            localTimestamp,
                            channel,
                            type,
                            asset,
                            assetClass,
                            balance,
                            "",  // empty wallet type
                            "",  // empty wallet id
                            ""   // empty wallet balance
                    ));
                }
            }

            // Append the data to the file
            Files.writeString(filePath, dataRows.toString(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            logger.error("Error writing balances data to CSV: {}", e.getMessage(), e);
        } finally {
            fileLock.unlock();
        }
    }
}
