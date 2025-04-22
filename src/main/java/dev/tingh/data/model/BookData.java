package dev.tingh.data.model;

import java.util.List;

public class BookData {
    private String type;
    private List<BookSymbolData> data;

    public static class BookSymbolData {
        public String symbol;
        private List<PriceLevel> asks;
        private List<PriceLevel> bids;

        public String getSymbol() {
            return symbol;
        }

        public List<PriceLevel> getAsks() {
            return asks;
        }

        public List<PriceLevel> getBids() {
            return bids;
        }
    }

    public static class PriceLevel {
        private String price;
        private String qty;

        public String getPrice() {
            return price;
        }

        public String getQty() {
            return qty;
        }
    }

    public String getType() {
        return type;
    }

    public List<BookSymbolData> getData() {
        return data;
    }
}