package dev.tingh.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TradeData {
    private String channel;
    private String type;
    private List<Trade> data;

    public String getChannel() {
        return channel;
    }

    public String getType() {
        return type;
    }

    public List<Trade> getTrades() {
        return data;
    }

    public static class Trade {
        private String symbol;
        private String side;
        private String price;
        private String qty;
        @SerializedName("ord_type")
        private String ordType;
        @SerializedName("trade_id")
        private String tradeId;
        private String timestamp;

        public String getSymbol() { return symbol; }
        public String getSide() { return side; }
        public String getPrice() { return price; }
        public String getQty() { return qty; }
        public String getOrdType() { return ordType; }
        public String getTradeId() { return tradeId; }
        public String getTimestamp() { return timestamp; }
    }
}