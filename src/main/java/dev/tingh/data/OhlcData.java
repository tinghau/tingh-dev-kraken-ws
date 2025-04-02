package dev.tingh.data;

import java.util.List;

public class OhlcData {
    private String type;
    private List<OhlcSymbolData> data;

    public static class OhlcSymbolData {
        private String symbol;
        private OhlcDetails ohlc;

        public String getSymbol() {
            return symbol;
        }

        public OhlcDetails getOhlc() {
            return ohlc;
        }
    }

    public static class OhlcDetails {
        private String open;
        private String high;
        private String low;
        private String close;
        private String volume;
        private long timestamp;
        private String interval;

        public String getOpen() {
            return open;
        }

        public String getHigh() {
            return high;
        }

        public String getLow() {
            return low;
        }

        public String getClose() {
            return close;
        }

        public String getVolume() {
            return volume;
        }

        public long getTimestamp() {
            return timestamp;
        }

        public String getInterval() {
            return interval;
        }
    }

    public String getType() {
        return type;
    }

    public List<OhlcSymbolData> getData() {
        return data;
    }
}