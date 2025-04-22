package dev.tingh.data.subscription;

import java.util.HashMap;
import java.util.Map;

public class TradeSubscriptionBuilder {
    private final Map<String, Object> params = new HashMap<>();

    public TradeSubscriptionBuilder(String... symbols) {
        params.put("channel", "trade");
        params.put("symbol", symbols);
    }

    public TradeSubscriptionBuilder withReqId(Integer reqId) {
        params.put("req_id", reqId);
        return this;
    }

    public Map<String, Object> build() {
        Map<String, Object> subscription = new HashMap<>();
        subscription.put("method", "subscribe");
        subscription.put("params", params);

        return subscription;
    }
}