package dev.tingh.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TickerSubscriptionBuilder {
    private final Map<String, Object> params = new HashMap<>();

    public TickerSubscriptionBuilder() {
        params.put("name", "ticker");
    }

    public TickerSubscriptionBuilder token(String token) {
        params.put("token", token);
        return this;
    }

    public TickerSubscriptionBuilder reqid(Integer reqid) {
        params.put("reqid", reqid);
        return this;
    }

    public TickerSubscriptionBuilder symbols(List<String> symbols) {
        params.put("symbols", symbols);
        return this;
    }

    public TickerSubscriptionBuilder symbol(String symbol) {
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

    public Map<String, Object> build() {
        return new HashMap<>(params);
    }
}