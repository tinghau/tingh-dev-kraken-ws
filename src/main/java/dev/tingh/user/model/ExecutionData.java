package dev.tingh.user.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ExecutionData {
    private String channel;
    private String type;
    private List<ExecutionItem> data;

    public String getChannel() {
        return channel;
    }

    public String getType() {
        return type;
    }

    public List<ExecutionItem> getData() {
        return data;
    }

    public static class ExecutionItem {
        @SerializedName("order_id")
        private String orderId; // "order_id"
        @SerializedName("order_userref")
        private Integer orderUserref; // "order_userref"
        @SerializedName("exec_id")
        private String execId; // "exec_id"
        @SerializedName("exec_type")
        private String execType; // "exec_type"
        @SerializedName("trade_id")
        private String tradeId; // "trade_id"
        private String symbol; // "symbol"
        private String side; // "side"
        private String lastQty;
        @SerializedName("last_price")
        private String lastPrice; // "last_price"
        @SerializedName("liquidity_ind")
        private String liquidityInd; // "liquidity_ind"
        private String cost;
        @SerializedName("order_qty")
        private String orderQty; // "order_qty"
        @SerializedName("cum_cost")
        private String cumCost; // "cum_cost"
        @SerializedName("cum_qty")
        private String cumQty; // "cum_qty"
        @SerializedName("time_in_force")
        private String timeInForce; // "time_in_force"
        @SerializedName("order_type")
        private String orderType; // "order_type"
        @SerializedName("limit_price_type")
        private String limitPriceType; // "limit_price_type"
        @SerializedName("limit_price")
        private String limitPrice; // "limit_price"
        @SerializedName("stop_price")
        private String stopPrice; // "stop_price"
        @SerializedName("avg_price")
        private String averagePrice; // "avg_price"
        @SerializedName("order_status")
        private String orderStatus; // "order_status"
        @SerializedName("fee_usd_equiv")
        private String feeUsdEquiv; // "fee_usd_equiv"
        @SerializedName("fee_ccy_pref")
        private String feeCcyPref; // "fee_ccy_pref"
        private String timestamp; // "timestamp"
        private List<Fee> fees;

        public String getOrderId() { return orderId; }
        public Integer getOrderUserref() { return orderUserref; }
        public String getSymbol() { return symbol; }
        public String getOrderQty() { return orderQty; }
        public String getCumCost() { return cumCost; }
        public String getTimeInForce() { return timeInForce; }
        public String getExecType() { return execType; }
        public String getSide() { return side; }
        public String getOrderType() { return orderType; }
        public String getLimitPriceType() { return limitPriceType; }
        public String getLimitPrice() { return limitPrice; }
        public String getStopPrice() { return stopPrice; }
        public String getOrderStatus() { return orderStatus; }
        public String getFeeUsdEquiv() { return feeUsdEquiv; }
        public String getFeeCcyPref() { return feeCcyPref; }
        public String getTimestamp() { return timestamp; }
        public List<Fee> getFees() { return fees; }
    }

    public static class Fee {
        private String asset;
        private String qty;

        public String getAsset() {
            return asset;
        }

        public String getQty() {
            return qty;
        }
    }
}