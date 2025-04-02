package dev.tingh.data;

import java.util.List;

public class Level3Data {
    private String type;
    private List<Level3SymbolData> data;

    public static class Level3SymbolData {
        private String symbol;
        private Level3Details level3;

        public String getSymbol() {
            return symbol;
        }

        public Level3Details getLevel3() {
            return level3;
        }
    }

    public static class Level3Details {
        private List<OrderUpdate> updates;

        public List<OrderUpdate> getUpdates() {
            return updates;
        }
    }

    public static class OrderUpdate {
        private String orderId;
        private String side;      // "buy" or "sell"
        private String price;
        private String volume;
        private String timestamp;
        private String updateType; // "add", "update", "remove", "trade"

        public String getOrderId() {
            return orderId;
        }

        public String getSide() {
            return side;
        }

        public String getPrice() {
            return price;
        }

        public String getVolume() {
            return volume;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getUpdateType() {
            return updateType;
        }
    }

    public String getType() {
        return type;
    }

    public List<Level3SymbolData> getData() {
        return data;
    }
}