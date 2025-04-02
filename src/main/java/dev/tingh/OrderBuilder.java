package dev.tingh;

import java.util.HashMap;
import java.util.Map;

public class OrderBuilder {
    private final Map<String, Object> params = new HashMap<>();

    public OrderBuilder(String token, String ordertype, String pair, String type, String volume) {
        params.put("token", token);
        params.put("ordertype", ordertype);
        params.put("pair", pair);
        params.put("type", type);
        params.put("volume", volume);
    }

    public OrderBuilder price(String price) {
        params.put("price", price);
        return this;
    }

    public OrderBuilder price2(String price2) {
        params.put("price2", price2);
        return this;
    }

    public OrderBuilder leverage(String leverage) {
        params.put("leverage", leverage);
        return this;
    }

    public OrderBuilder reduce_only(boolean reduceOnly) {
        params.put("reduce_only", reduceOnly);
        return this;
    }

    public OrderBuilder stptype(String stptype) {
        params.put("stptype", stptype);
        return this;
    }

    public OrderBuilder oflags(String oflags) {
        params.put("oflags", oflags);
        return this;
    }

    public OrderBuilder timeinforce(String timeinforce) {
        params.put("timeinforce", timeinforce);
        return this;
    }

    public OrderBuilder starttm(String starttm) {
        params.put("starttm", starttm);
        return this;
    }

    public OrderBuilder expiretm(String expiretm) {
        params.put("expiretm", expiretm);
        return this;
    }

    public OrderBuilder deadline(String deadline) {
        params.put("deadline", deadline);
        return this;
    }

    public OrderBuilder validate(boolean validate) {
        params.put("validate", validate);
        return this;
    }

    public OrderBuilder close_ordertype(String closeOrdertype) {
        params.put("close[ordertype]", closeOrdertype);
        return this;
    }

    public OrderBuilder close_price(String closePrice) {
        params.put("close[price]", closePrice);
        return this;
    }

    public OrderBuilder close_price2(String closePrice2) {
        params.put("close[price2]", closePrice2);
        return this;
    }

    public OrderBuilder userref(Integer userref) {
        params.put("userref", userref);
        return this;
    }

    Map<String, Object> build() {
        return new HashMap<>(params);
    }
}