package dev.tingh.data;

import java.util.List;

public class BookData {
    private String type;
    private List<BookSymbolData> data;

    public static class BookSymbolData {
        private String symbol;
        private BookDetails book;

        public String getSymbol() {
            return symbol;
        }

        public BookDetails getBook() {
            return book;
        }
    }

    public static class BookDetails {
        private List<PriceLevel> asks;
        private List<PriceLevel> bids;

        public List<PriceLevel> getAsks() {
            return asks;
        }

        public List<PriceLevel> getBids() {
            return bids;
        }
    }

    public static class PriceLevel {
        private String price;
        private String volume;
        private String timestamp;
        private String updateType; // "r" (remove), "a" (add), "u" (update)

        public String getPrice() {
            return price;
        }

        public String getVolume() {
            return volume;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public String getUpdateType() {
            return updateType;
        }
    }

    public String getType() {
        return type;
    }

    public List<BookSymbolData> getData() {
        return data;
    }
}