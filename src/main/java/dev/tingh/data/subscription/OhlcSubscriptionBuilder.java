package dev.tingh.data.subscription;

import java.util.HashMap;
import java.util.Map;

public class OhlcSubscriptionBuilder {
    private final Map<String, Object> params = new HashMap<>();

    public OhlcSubscriptionBuilder(String... symbols) {
        params.put("channel", "ohlc");
        params.put("symbol", symbols);
    }

    public OhlcSubscriptionBuilder withReqId(Integer reqId) {
        params.put("req_id", reqId);
        return this;
    }

    public OhlcSubscriptionBuilder interval(int interval) {
        params.put("interval", interval);
        return this;
    }

    public Map<String, Object> build() {
        Map<String, Object> subscription = new HashMap<>();
        subscription.put("method", "subscribe");
        subscription.put("params", params);

        return subscription;
    }
}