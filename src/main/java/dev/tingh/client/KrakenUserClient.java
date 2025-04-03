package dev.tingh.client;

import com.google.gson.Gson;
import dev.tingh.user.BalancesSubscriptionBuilder;
import dev.tingh.user.ExecutionsSubscriptionBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class KrakenUserClient extends KrakenBaseClient {

    private final Gson gson = new Gson();

    public KrakenUserClient(URI serverUri, String apiKey, String apiSecret) {
        super(serverUri);
        authenticate(apiKey, apiSecret);
    }

    private void authenticate(String apiKey, String apiSecret) {
        Map<String, Object> authRequest = new HashMap<>();
        authRequest.put("method", "subscribe");
        authRequest.put("channel", "auth");
        authRequest.put("params", new HashMap<String, Object>() {{
            put("name", "token");
            put("key", apiKey);
            put("secret", apiSecret);
        }});

        send(gson.toJson(authRequest));
    }

    public void subscribeToExecutions(ExecutionsSubscriptionBuilder subscription) {
        Map<String, Object> subscriptionMap = new HashMap<>();
        subscriptionMap.put("method", "subscribe");
        subscriptionMap.putAll(subscription.build());
        send(gson.toJson(subscriptionMap));
    }

    public void subscribeToBalances(BalancesSubscriptionBuilder subscription) {
        Map<String, Object> subscriptionMap = new HashMap<>();
        subscriptionMap.put("method", "subscribe");
        subscriptionMap.putAll(subscription.build());
        send(gson.toJson(subscriptionMap));
    }
}