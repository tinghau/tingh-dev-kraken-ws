package dev.tingh.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeSubscriptionBuilder {
    private final List<String> symbols;
    private final Map<String, Object> params = new HashMap<>();

    public TradeSubscriptionBuilder(String... symbols) {
        this.symbols = Arrays.asList(symbols);
    }

    /**
     * Limit the number of returned trade updates.
     * @param limit valid values: 1-1000, default: 100
     * @return this builder
     */
    public TradeSubscriptionBuilder withLimit(int limit) {
        if (limit < 1 || limit > 1000) {
            throw new IllegalArgumentException("Limit must be between 1 and 1000");
        }
        params.put("limit", limit);
        return this;
    }

    public Map<String, Object> build() {
        Map<String, Object> subscription = new HashMap<>();
        subscription.put("channel", "trade");
        subscription.put("symbol", symbols);

        if (!params.isEmpty()) {
            subscription.put("params", params);
        }

        return subscription;
    }
}