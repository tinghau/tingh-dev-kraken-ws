package dev.tingh.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Level3SubscriptionBuilder {
    private final List<String> symbols;
    private final Map<String, Object> params = new HashMap<>();

    public Level3SubscriptionBuilder(String... symbols) {
        this.symbols = Arrays.asList(symbols);
    }

    /**
     * Return order feed for updates beyond the given OrderID sequence number.
     * @param sequence An orderID sequence number
     * @return this builder
     */
    public Level3SubscriptionBuilder withSince(long sequence) {
        params.put("since", sequence);
        return this;
    }

    /**
     * Limit the number of returned order updates.
     * @param limit valid values: 1-1000, default: 100
     * @return this builder
     */
    public Level3SubscriptionBuilder withLimit(int limit) {
        if (limit < 1 || limit > 1000) {
            throw new IllegalArgumentException("Limit must be between 1 and 1000");
        }
        params.put("limit", limit);
        return this;
    }

    public Map<String, Object> build() {
        Map<String, Object> subscription = new HashMap<>();
        subscription.put("channel", "level3");
        subscription.put("symbol", symbols);

        if (!params.isEmpty()) {
            subscription.put("params", params);
        }

        return subscription;
    }
}