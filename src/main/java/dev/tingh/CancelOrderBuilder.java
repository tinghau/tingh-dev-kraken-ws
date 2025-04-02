package dev.tingh;

import java.util.HashMap;
import java.util.Map;

public class CancelOrderBuilder {
    private final Map<String, Object> params = new HashMap<>();

    public CancelOrderBuilder(String token) {
        params.put("token", token);
    }

    public CancelOrderBuilder orderid(String orderId) {
        params.put("orderid", orderId);
        return this;
    }

    public CancelOrderBuilder pair(String pair) {
        params.put("pair", pair);
        return this;
    }

    public CancelOrderBuilder userref(Integer userref) {
        params.put("userref", userref);
        return this;
    }

    Map<String, Object> build() {
        return new HashMap<>(params);
    }
}