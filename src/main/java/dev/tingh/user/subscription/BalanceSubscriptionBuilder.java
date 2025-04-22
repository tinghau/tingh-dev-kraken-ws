package dev.tingh.user.subscription;

import java.util.HashMap;
import java.util.Map;

public class BalanceSubscriptionBuilder {
    private final Map<String, Object> params = new HashMap<>();

    public BalanceSubscriptionBuilder() {
        params.put("channel", "balances");
    }

    public BalanceSubscriptionBuilder withToken(String token) {
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