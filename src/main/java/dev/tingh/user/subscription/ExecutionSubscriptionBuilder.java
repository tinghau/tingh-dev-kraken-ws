package dev.tingh.user.subscription;

import java.util.HashMap;
import java.util.Map;

public class ExecutionSubscriptionBuilder {
    private final Map<String, Object> params = new HashMap<>();

    public ExecutionSubscriptionBuilder() {
        params.put("channel", "executions");
    }

    public ExecutionSubscriptionBuilder withToken(String token) {
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