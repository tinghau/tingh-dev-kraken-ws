package dev.tingh.trading;

import java.util.HashMap;
import java.util.Map;

public class CancelAfterBuilder {
    private final Map<String, Object> params = new HashMap<>();

    public CancelAfterBuilder withToken(String token) {
        params.put("token", token);
        return this;
    }

    public CancelAfterBuilder withTimeout(String timeout) {
        params.put("timeout", timeout);
        return this;
    }

    public Map<String, Object> build() {
        Map<String, Object> cancel = new HashMap<>();
        cancel.put("method", "cancel_all_orders_after");
        cancel.put("params", params);

        return cancel;
    }
}