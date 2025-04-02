package dev.tingh;

import java.util.HashMap;
import java.util.Map;

public class AmendOrderBuilder {
    private final Map<String, Object> params = new HashMap<>();

    public AmendOrderBuilder(String token, String orderid) {
        params.put("token", token);
        params.put("orderid", orderid);
    }

    public AmendOrderBuilder price(String price) {
        params.put("price", price);
        return this;
    }

    public AmendOrderBuilder volume(String volume) {
        params.put("volume", volume);
        return this;
    }

    public AmendOrderBuilder oflags(String oflags) {
        params.put("oflags", oflags);
        return this;
    }

    public AmendOrderBuilder deadline(String deadline) {
        params.put("deadline", deadline);
        return this;
    }

    public AmendOrderBuilder cancel_response(boolean cancelResponse) {
        params.put("cancel_response", cancelResponse);
        return this;
    }

    public AmendOrderBuilder validate(boolean validate) {
        params.put("validate", validate);
        return this;
    }

    Map<String, Object> build() {
        return new HashMap<>(params);
    }
}