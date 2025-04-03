package dev.tingh.user;

import java.util.HashMap;
import java.util.Map;

public class BalancesSubscriptionBuilder {
    private final Map<String, Object> params = new HashMap<>();

    /**
     * Enable snapshot mode. If enabled, the client will receive a snapshot of current balances.
     * @param snapshot true to enable snapshot mode, false otherwise
     * @return this builder
     */
    public BalancesSubscriptionBuilder withSnapshot(boolean snapshot) {
        params.put("snapshot", snapshot);
        return this;
    }

    public Map<String, Object> build() {
        Map<String, Object> subscription = new HashMap<>();
        subscription.put("channel", "balances");

        if (!params.isEmpty()) {
            subscription.put("params", params);
        }

        return subscription;
    }
}