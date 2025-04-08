package dev.tingh.data;

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
        private OhlcDetails ohlc;

        public String getSymbol() {
            return symbol;
        }

        public OhlcDetails getOhlc() {
            return ohlc;
        }
    }

    public static class OhlcDetails {
        private String interval;
        private List<Candle> candles;

        public String getInterval() {
            return interval;
        }

        public List<Candle> getCandles() {
            return candles;
        }
    }

    public static class Candle {
        private String timestamp;
        private String open;
        private String high;
        private String low;
        private String close;
        private String volume;
        private String trades;

        public String getTimestamp() {
            return timestamp;
        }

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

        public String getTrades() {
            return trades;
        }
    }
}