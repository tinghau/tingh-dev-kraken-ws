package dev.tingh.data;

import java.util.HashMap;
import java.util.Map;

public class BookSubscriptionBuilder {
    private final Map<String, Object> params = new HashMap<>();

    public BookSubscriptionBuilder(String... symbols) {
        params.put("channel", "book");
        params.put("symbol", symbols);
    }

    public BookSubscriptionBuilder withDepth(int depth) {
        params.put("depth", depth);
        return this;
    }

    public Map<String, Object> build() {
        Map<String, Object> subscription = new HashMap<>();
        subscription.put("method", "subscribe");
        subscription.put("params", params);

        return subscription;
    }
}