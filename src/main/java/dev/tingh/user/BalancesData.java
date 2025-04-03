package dev.tingh.user;

import java.util.List;
import java.util.Map;

public class BalancesData {
    private String channel;
    private String type;
    private List<BalanceItem> data;

    public String getChannel() {
        return channel;
    }

    public String getType() {
        return type;
    }

    public List<BalanceItem> getData() {
        return data;
    }

    public static class BalanceItem {
        private String currency;
        private String available;
        private String hold;

        public String getCurrency() {
            return currency;
        }

        public String getAvailable() {
            return available;
        }

        public String getHold() {
            return hold;
        }
    }
}