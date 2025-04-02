package dev.tingh.data;

import java.util.List;

public class TickerData {
    private String type;
    private List<TickerSymbolData> data;

    public static class TickerSymbolData {
        private String symbol;
        private TickerDetails ticker;

        public String getSymbol() {
            return symbol;
        }

        public TickerDetails getTicker() {
            return ticker;
        }
    }

    public static class TickerDetails {
        private String a;    // Ask price
        private String ap;   // Ask price (deprecated)
        private String av;   // Ask volume
        private String b;    // Bid price
        private String bp;   // Bid price (deprecated)
        private String bv;   // Bid volume
        private String c;    // Close price
        private String h;    // High price
        private String l;    // Low price
        private String o;    // Open price
        private String p;    // Volume weighted average price
        private String t;    // Number of trades
        private String v;    // Volume

        // Getters
        public String getAskPrice() { return a; }
        public String getAskVolume() { return av; }
        public String getBidPrice() { return b; }
        public String getBidVolume() { return bv; }
        public String getClosePrice() { return c; }
        public String getHighPrice() { return h; }
        public String getLowPrice() { return l; }
        public String getOpenPrice() { return o; }
        public String getVwap() { return p; }
        public String getNumberOfTrades() { return t; }
        public String getVolume() { return v; }
    }

    public String getType() {
        return type;
    }

    public List<TickerSymbolData> getData() {
        return data;
    }
}