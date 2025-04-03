package dev.tingh.user;

import java.util.List;

public class ExecutionsData {
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
        private String executionId;
        private String clientOrderId;
        private String orderId;
        private String symbol;
        private String side;
        private String orderType;
        private String orderStatus;
        private String price;
        private String quantity;
        private String timestamp;
        private String timeInForce;
        private String tradeId;
        private String execType;
        private String tradePrice;
        private String tradeQuantity;
        private String tradeFee;
        private String tradeFeeAsset;

        public String getExecutionId() {
            return executionId;
        }

        public String getClientOrderId() {
            return clientOrderId;
        }

        public String getOrderId() {
            return orderId;
        }

        public String getSymbol() {
            return symbol;
        }

        public String getSide() {
            return side;
        }

        public String getOrderType() {
            return orderType;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public String getPrice() {
            return price;
        }

        public String getQuantity() {
            return quantity;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getTimeInForce() {
            return timeInForce;
        }

        public String getTradeId() {
            return tradeId;
        }

        public String getExecType() {
            return execType;
        }

        public String getTradePrice() {
            return tradePrice;
        }

        public String getTradeQuantity() {
            return tradeQuantity;
        }

        public String getTradeFee() {
            return tradeFee;
        }

        public String getTradeFeeAsset() {
            return tradeFeeAsset;
        }
    }
}