package dev.tingh.data.subscription;

import java.util.*;

public class TickerSubscriptionBuilder {
    private final Map<String, Object> params = new HashMap<>();

    public TickerSubscriptionBuilder(String... symbols) {
        params.put("channel", "ticker");
        params.put("symbol", symbols);
    }

    public Map<String, Object> build() {
        Map<String, Object> subscription = new HashMap<>();
        subscription.put("method", "subscribe");
        subscription.put("params", params);

        return subscription;
    }
}