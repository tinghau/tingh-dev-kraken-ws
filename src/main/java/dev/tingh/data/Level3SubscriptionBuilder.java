package dev.tingh.data;

import java.util.HashMap;
import java.util.Map;

public class Level3SubscriptionBuilder {
    private final Map<String, Object> params = new HashMap<>();

    public Level3SubscriptionBuilder(String... symbols) {
        params.put("channel", "level3");
        params.put("symbol", symbols);
    }

    /**
     * Limit the number of returned order updates.
     * @param limit valid values: 1-1000, default: 100
     * @return this builder
     */
    public Level3SubscriptionBuilder withDepth(int limit) {
        if (limit < 1 || limit > 1000) {
            throw new IllegalArgumentException("Limit must be between 1 and 1000");
        }
        params.put("depth", limit);
        return this;
    }

    public Level3SubscriptionBuilder withToken(String token) {
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