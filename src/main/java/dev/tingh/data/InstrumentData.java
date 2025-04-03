package dev.tingh.data;

import java.util.List;

public class InstrumentData {
    private String type;
    private List<InstrumentSymbolData> data;

    public static class InstrumentSymbolData {
        private String symbol;
        private InstrumentDetails instrument;

        public String getSymbol() {
            return symbol;
        }

        public InstrumentDetails getInstrument() {
            return instrument;
        }
    }

    public static class InstrumentDetails {
        private String baseCurrency;
        private String quoteCurrency;
        private String volumeCurrency;
        private String status;
        private String tradableNext;
        private String pricePrecision;
        private String minOrderSize;
        private String maxOrderSize;
        private String sizeIncrement;
        private String priceIncrement;
        private List<String> orderTypes;
        private List<String> timeInForce;

        public String getBaseCurrency() {
            return baseCurrency;
        }

        public String getQuoteCurrency() {
            return quoteCurrency;
        }

        public String getVolumeCurrency() {
            return volumeCurrency;
        }

        public String getStatus() {
            return status;
        }

        public String getTradableNext() {
            return tradableNext;
        }

        public String getPricePrecision() {
            return pricePrecision;
        }

        public String getMinOrderSize() {
            return minOrderSize;
        }

        public String getMaxOrderSize() {
            return maxOrderSize;
        }

        public String getSizeIncrement() {
            return sizeIncrement;
        }

        public String getPriceIncrement() {
            return priceIncrement;
        }

        public List<String> getOrderTypes() {
            return orderTypes;
        }

        public List<String> getTimeInForce() {
            return timeInForce;
        }
    }

    public String getType() {
        return type;
    }

    public List<InstrumentSymbolData> getData() {
        return data;
    }
}