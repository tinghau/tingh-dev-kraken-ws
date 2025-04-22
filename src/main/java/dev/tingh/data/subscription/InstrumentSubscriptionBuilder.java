package dev.tingh.data.subscription;

import java.util.HashMap;
import java.util.Map;

public class InstrumentSubscriptionBuilder {
    private final Map<String, Object> params = new HashMap<>();

    public InstrumentSubscriptionBuilder() {
        params.put("channel", "instrument");
    }

    public Map<String, Object> build() {
        Map<String, Object> subscription = new HashMap<>();
        subscription.put("method", "subscribe");
        subscription.put("params", params);

        return subscription;
    }
}