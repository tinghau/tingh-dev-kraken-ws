package dev.tingh.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class InstrumentData {
    private String type;
    private InstrumentSymbolData data;

    public static class InstrumentSymbolData {
        private List<AssetDetail> assets;
        private List<PairDetail> pairs;

        public List<AssetDetail> getAssets() { return assets; }

        public List<PairDetail> getPairs() {
            return pairs;
        }
    }

    public static class AssetDetail {
        private String id;
        private String status;
        private String precision;
        @SerializedName("precision_display")
        private String precisionDisplay;
        private String borrowable;
        @SerializedName("collateral_value")
        private String collateralValue;
        @SerializedName("margin_rate")
        private String marginRate;

        public String getId() { return id; }
        public String getStatus() { return status; }
        public String getPrecision() { return precision; }
        public String getPrecisionDisplay() { return precisionDisplay; }
        public String getBorrowable() { return borrowable; }
        public String getCollateralValue() { return collateralValue; }
        public String getMarginRate() { return marginRate; }
    }

    public static class PairDetail {
        private String symbol;
        private String base;
        private String quote;
        private String status;
        @SerializedName("qty_precision")
        private long qtyPrecision;
        @SerializedName("qty_increment")
        private double qtyIncrement;
        @SerializedName("price_precision")
        private long pricePrecision;
        @SerializedName("cost_precision")
        private long costPrecision;
        private boolean marginable;
        @SerializedName("has_index")
        private boolean hasIndex;
        @SerializedName("cost_min")
        private double costMin;
        @SerializedName("margin_initial")
        private double marginInitial;
        @SerializedName("position_limit_long")
        private long positionLimitLong;
        @SerializedName("position_limit_short")
        private long positionLimitShort;
        @SerializedName("tick_size")
        private double tickSize;
        @SerializedName("price_increment")
        private double priceIncrement;
        @SerializedName("qty_min")
        private double qtyMin;

        public String getSymbol() { return symbol; }
        public String getBase() { return base; }
        public String getQuote() { return quote; }
        public String getStatus() { return status; }
        public long getQtyPrecision() { return qtyPrecision; }
        public double getQtyIncrement() { return qtyIncrement; }
        public long getPricePrecision() { return pricePrecision; }
        public long getCostPrecision() { return costPrecision; }
        public boolean isMarginable() { return marginable; }
        public boolean hasIndex() { return hasIndex; }
        public double getCostMin() { return costMin; }
        public double getMarginInitial() { return marginInitial; }
        public long getPositionLimitLong() { return positionLimitLong; }
        public long getPositionLimitShort() { return positionLimitShort; }
        public double getTickSize() { return tickSize; }
        public double getPriceIncrement() { return priceIncrement; }
        public double getQtyMin() { return qtyMin; }
    }

    public String getType() {
        return type;
    }

    public InstrumentSymbolData getData() {
        return data;
    }
}