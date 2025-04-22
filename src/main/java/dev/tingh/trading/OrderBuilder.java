package dev.tingh.trading;

import java.util.HashMap;
import java.util.Map;

public class OrderBuilder {
    private final Map<String, Object> params = new HashMap<>();

    public OrderBuilder(String orderType, String side, String orderQty, String symbol) {
        params.put("order_type", orderType);
        params.put("side", side);
        params.put("order_qty", orderQty);
        params.put("symbol", symbol);
    }

    public OrderBuilder withLimitPrice(String limitPrice) {
        params.put("limit_price", limitPrice);
        return this;
    }

    public OrderBuilder withOrderUserref(String orderUserref) {
        params.put("order_userref", orderUserref);
        return this;
    }

    public OrderBuilder withTriggers(Map<String, String> triggers) {
        params.put("triggers", triggers);
        return this;
    }

    public OrderBuilder withConditional(Map<String, String> conditional) {
        params.put("conditional", conditional);
        return this;
    }

    public OrderBuilder withToken(String token) {
        params.put("token", token);
        return this;
    }

    public Map<String, Object> build() {
        Map<String, Object> order = new HashMap<>();
        order.put("method", "add_order");
        order.put("params", params);

        return order;
    }
}