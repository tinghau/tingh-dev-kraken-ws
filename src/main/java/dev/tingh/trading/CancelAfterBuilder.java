package dev.tingh.trading;

import java.util.HashMap;
import java.util.Map;

public class CancelAfterBuilder {
    private final Map<String, Object> params = new HashMap<>();

    public CancelAfterBuilder(String token, int timeout) {
        params.put("token", token);
        params.put("timeout", timeout);
    }

    public CancelAfterBuilder reqid(Integer reqid) {
        params.put("reqid", reqid);
        return this;
    }

    public Map<String, Object> build() {
        return new HashMap<>(params);
    }
}