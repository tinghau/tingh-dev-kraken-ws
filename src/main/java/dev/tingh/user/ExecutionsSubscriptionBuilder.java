package dev.tingh.user;

import java.util.HashMap;
import java.util.Map;

public class ExecutionsSubscriptionBuilder {
    private final Map<String, Object> params = new HashMap<>();

    /**
     * Enable snapshot mode. If enabled, the client will receive a snapshot of recent executions.
     * @param snapshot true to enable snapshot mode, false otherwise
     * @return this builder
     */
    public ExecutionsSubscriptionBuilder withSnapshot(boolean snapshot) {
        params.put("snapshot", snapshot);
        return this;
    }

    /**
     * Limit the number of returned execution updates.
     * @param limit valid values: 1-1000, default: 100
     * @return this builder
     */
    public ExecutionsSubscriptionBuilder withLimit(int limit) {
        if (limit < 1 || limit > 1000) {
            throw new IllegalArgumentException("Limit must be between 1 and 1000");
        }
        params.put("limit", limit);
        return this;
    }

    public Map<String, Object> build() {
        Map<String, Object> subscription = new HashMap<>();
        subscription.put("channel", "executions");

        if (!params.isEmpty()) {
            subscription.put("params", params);
        }

        return subscription;
    }
}