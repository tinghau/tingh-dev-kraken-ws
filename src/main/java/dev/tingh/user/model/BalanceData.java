package dev.tingh.user.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BalanceData {
    private String channel;
    private String type;
    private List<BalanceItem> data;

    public String getChannel() {
        return channel;
    }

    public String getType() {
        return type;
    }

    public List<BalanceItem> getData() {
        return data;
    }

    public static class BalanceItem {
        private String asset;
        @SerializedName("asset_class")
        private String assetClass;
        private String balance;
        private List<WalletItem> wallet;

        public String getAsset() {
            return asset;
        }
        public String getAssetClass() {
            return assetClass;
        }
        public String getBalance()  { return balance; }
        public List<WalletItem> getWallet() { return wallet; }
    }

    public static class WalletItem {
        private String type;
        private String id;
        private String balance;

        public String getBalance() { return balance; }
        public String getId() { return id; }
        public String getType() { return type; }
    }

}