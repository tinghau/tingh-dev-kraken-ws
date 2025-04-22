package dev.tingh.trading;

import java.util.HashMap;
import java.util.Map;

public class AmendOrderBuilder {
    private final Map<String, Object> params = new HashMap<>();

    public AmendOrderBuilder withToken(String token) {
        params.put("token", token);
        return this;
    }

    public AmendOrderBuilder withClOrdId(String clOrdId) {
        params.put("cl_ord_id", clOrdId);
        return this;
    }

    public AmendOrderBuilder withLimitPrice(String limitPrice) {
        params.put("limit_price", limitPrice);
        return this;
    }

    public AmendOrderBuilder withOrderQty(String orderQty) {
        params.put("order_qty", orderQty);
        return this;
    }

    public AmendOrderBuilder withOrderId(String orderId) {
        params.put("order_id", orderId);
        return this;
    }

    public AmendOrderBuilder withDeadline(String deadline) {
        params.put("deadline", deadline);
        return this;
    }

    public AmendOrderBuilder withPostOnly(boolean postOnly) {
        params.put("post_only", postOnly);
        return this;
    }

    public Map<String, Object> build() {
        Map<String, Object> order = new HashMap<>();
        order.put("method", "amend_order");
        order.put("params", params);

        return order;
    }
}