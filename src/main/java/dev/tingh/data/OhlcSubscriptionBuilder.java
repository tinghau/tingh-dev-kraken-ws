package dev.tingh.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OhlcSubscriptionBuilder {
    private final Map<String, Object> params = new HashMap<>();

    public OhlcSubscriptionBuilder() {
        params.put("name", "ohlc");
    }

    public OhlcSubscriptionBuilder token(String token) {
        params.put("token", token);
        return this;
    }

    public OhlcSubscriptionBuilder reqid(Integer reqid) {
        params.put("reqid", reqid);
        return this;
    }

    public OhlcSubscriptionBuilder symbols(List<String> symbols) {
        params.put("symbols", symbols);
        return this;
    }

    public OhlcSubscriptionBuilder symbol(String symbol) {
        List<String> symbols;
        if (params.containsKey("symbols")) {
            symbols = (List<String>) params.get("symbols");
        } else {
            symbols = new ArrayList<>();
            params.put("symbols", symbols);
        }
        symbols.add(symbol);
        return this;
    }

    public OhlcSubscriptionBuilder interval(int interval) {
        params.put("interval", interval);
        return this;
    }

    public Map<String, Object> build() {
        return new HashMap<>(params);
    }
}