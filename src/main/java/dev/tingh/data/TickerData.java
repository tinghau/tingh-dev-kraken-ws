package dev.tingh.data;

import java.util.List;

public class TickerData {
    private String type;
    private List<TickerSymbolData> data;

    public static class TickerSymbolData {
        private String symbol;
        private String askPrice;
        private String askVolume;
        private String bidPrice;
        private String bidVolume;
        private String closePrice;
        private String highPrice;
        private String lowPrice;
        private String openPrice;
        private String vwap;
        private String numberOfTrades;
        private String volume;

        public String getSymbol() {
            return symbol;
        }
        public String getAskPrice() { return askPrice; }
        public String getAskVolume() { return askVolume; }
        public String getBidPrice() { return bidPrice; }
        public String getBidVolume() { return bidVolume; }
        public String getClosePrice() { return closePrice; }
        public String getHighPrice() { return highPrice; }
        public String getLowPrice() { return lowPrice; }
        public String getOpenPrice() { return openPrice; }
        public String getVwap() { return vwap; }
        public String getNumberOfTrades() { return numberOfTrades; }
        public String getVolume() { return volume; }
    }

    public String getType() {
        return type;
    }

    public List<TickerSymbolData> getData() {
        return data;
    }
}