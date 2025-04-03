package dev.tingh.admin;

import java.util.HashMap;
import java.util.Map;

public class PingSubscriptionBuilder {
    private final Map<String, Object> params = new HashMap<>();

    /**
     * Set a custom value to be echoed back in the pong response.
     * @param reqid custom request identifier
     * @return this builder
     */
    public PingSubscriptionBuilder withReqid(String reqid) {
        params.put("reqid", reqid);
        return this;
    }

    public Map<String, Object> build() {
        Map<String, Object> subscription = new HashMap<>();
        subscription.put("method", "ping");

        if (!params.isEmpty()) {
            // For ping, we don't use "params" as a nested object
            subscription.putAll(params);
        }

        return subscription;
    }
}