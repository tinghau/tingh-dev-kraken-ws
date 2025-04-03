package dev.tingh.data;

import java.util.List;

public class TradeData {
    private String channel;
    private String type;
    private List<TradeItem> data;

    public String getChannel() {
        return channel;
    }

    public String getType() {
        return type;
    }

    public List<TradeItem> getData() {
        return data;
    }

    public static class TradeItem {
        private String price;
        private String volume;
        private String timestamp;
        private String side;
        private String orderType;
        private String misc;
        private String symbol;

        public String getPrice() {
            return price;
        }

        public String getVolume() {
            return volume;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getSide() {
            return side;
        }

        public String getOrderType() {
            return orderType;
        }

        public String getMisc() {
            return misc;
        }

        public String getSymbol() {
            return symbol;
        }
    }
}