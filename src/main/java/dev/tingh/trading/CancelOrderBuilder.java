package dev.tingh.trading;

import com.sun.source.doctree.SinceTree;

import java.util.HashMap;
import java.util.Map;

public class CancelOrderBuilder {
    private final Map<String, Object> params = new HashMap<>();

    public CancelOrderBuilder withToken(String token) {
        params.put("token", token);
        return this;
    }

    public CancelOrderBuilder withOrderIds(String... orderIds) {
        params.put("order_id", orderIds);
        return this;
    }

    public Map<String, Object> build() {
        Map<String, Object> cancel = new HashMap<>();
        cancel.put("method", "cancel_order");
        cancel.put("params", params);

        return cancel;
    }
}