package dev.tingh.data.subscription;

import java.util.HashMap;
import java.util.Map;

public class OrderSubscriptionBuilder {
    private final Map<String, Object> params = new HashMap<>();

    public OrderSubscriptionBuilder(String... symbols) {
        params.put("channel", "level3");
        params.put("symbol", symbols);
    }

    /**
     * Limit the number of returned order updates.
     * @param limit valid values: 1-1000, default: 100
     * @return this builder
     */
    public OrderSubscriptionBuilder withDepth(int limit) {
        if (limit < 1 || limit > 1000) {
            throw new IllegalArgumentException("Limit must be between 1 and 1000");
        }
        params.put("depth", limit);
        return this;
    }

    public OrderSubscriptionBuilder withToken(String token) {
        params.put("token", token);
        return this;
    }

    public Map<String, Object> build() {
        Map<String, Object> subscription = new HashMap<>();
        subscription.put("method", "subscribe");
        subscription.put("params", params);

        return subscription;
    }
}