package dev.tingh.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderData {
    private String type;
    private List<OrderSymbolData> data;

    public static class OrderSymbolData {
        private String symbol;
        private List<OrderUpdate> bids;
        private List<OrderUpdate> asks;

        public String getSymbol() {
            return symbol;
        }

        public List<OrderUpdate> getBids() {
            return bids;
        }

        public List<OrderUpdate> getAsks() {
            return asks;
        }
    }

    public static class OrderUpdate {
        private String event;
        @SerializedName("order_id")
        private String orderId;
        @SerializedName("limit_price")
        private String limitPrice;
        @SerializedName("order_qty")
        private String orderQty;
        private String timestamp;

        public String getEvent() {
            return event;
        }

        public String getOrderId() {
            return orderId;
        }

        public String getLimitPrice() {
            return limitPrice;
        }

        public String getOrderQty() {
            return orderQty;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }

    public String getType() {
        return type;
    }

    public List<OrderSymbolData> getData() {
        return data;
    }
}