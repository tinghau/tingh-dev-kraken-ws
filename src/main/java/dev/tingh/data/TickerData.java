package dev.tingh.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TickerData {

    private String type;
    private List<TickerSymbolData> data;

    public static class TickerSymbolData {
        private String symbol;
        private String bid;
        @SerializedName("bid_qty")
        private String bidQty;
        private String ask;
        @SerializedName("ask_qty")
        private String askQty;
        private String last;
        private String volume;
        private String vwap;
        private String low;
        private String high;
        private String change;
        @SerializedName("change_pct")
        private String changePct;

        public String getSymbol() { return symbol; }
        public String getBid() { return bid; }
        public String getBidQty() { return bidQty; }
        public String getAsk() { return ask; }
        public String getAskQty() { return askQty; }
        public String getLast() { return last; }
        public String getVolume() { return volume; }
        public String getVwap() { return vwap; }
        public String getLow() { return low; }
        public String getHigh() { return high; }
        public String getChange() { return change; }
        public String getChangePct() { return changePct; }
    }

    public String getType() {
        return type;
    }

    public List<TickerSymbolData> getData() {
        return data;
    }
}