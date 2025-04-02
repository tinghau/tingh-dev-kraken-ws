package dev.tingh.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BookSubscriptionBuilder {
    private final Map<String, Object> params = new HashMap<>();

    public BookSubscriptionBuilder() {
        params.put("name", "book");
    }

    public BookSubscriptionBuilder token(String token) {
        params.put("token", token);
        return this;
    }

    public BookSubscriptionBuilder reqid(Integer reqid) {
        params.put("reqid", reqid);
        return this;
    }

    public BookSubscriptionBuilder symbols(List<String> symbols) {
        params.put("symbols", symbols);
        return this;
    }

    public BookSubscriptionBuilder symbol(String symbol) {
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

    public BookSubscriptionBuilder depth(int depth) {
        params.put("depth", depth);
        return this;
    }

    public Map<String, Object> build() {
        return new HashMap<>(params);
    }
}