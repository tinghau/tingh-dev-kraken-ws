package dev.tingh.data.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OhlcData {
    private String channel;
    private String type;
    private List<OhlcSymbolData> data;

    public String getChannel() {
        return channel;
    }

    public String getType() {
        return type;
    }

    public List<OhlcSymbolData> getData() {
        return data;
    }

    public static class OhlcSymbolData {
        private String symbol;
        private String open;
        private String high;
        private String low;
        private String close;
        private String trades;
        private String volume;
        private String vwap;
        @SerializedName("interval_begin")
        private String intervalBegin;
        private String interval;
        private String timestamp;

        public String getSymbol() { return symbol; }
        public String getOpen() { return open; }
        public String getHigh() { return high; }
        public String getLow() { return low; }
        public String getClose() { return close; }
        public String getTrades() { return trades; }
        public String getVolume() { return volume; }
        public String getVwap() { return vwap; }
        public String getIntervalBegin() { return intervalBegin; }
        public String getInterval() { return interval; }
        public String getTimestamp() { return timestamp; }
    }

}