package dev.tingh.client;

import com.google.gson.Gson;
import dev.tingh.data.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class KrakenDataClient extends KrakenBaseClient {

    private final Gson gson = new Gson();

    public KrakenDataClient(URI serverUri) {
        super(serverUri);
    }

    public void subscribeToTicker(TickerSubscriptionBuilder subscription) {
        Map<String, Object> subscriptionMap = new HashMap<>();
        subscriptionMap.put("method", "subscribe");
        subscriptionMap.putAll(subscription.build());
        send(gson.toJson(subscriptionMap));
    }

    public void subscribeToBook(BookSubscriptionBuilder subscription) {
        Map<String, Object> subscriptionMap = new HashMap<>();
        subscriptionMap.put("method", "subscribe");
        subscriptionMap.putAll(subscription.build());
        send(gson.toJson(subscriptionMap));
    }

    public void subscribeToLevel3(Level3SubscriptionBuilder subscription) {
        Map<String, Object> subscriptionMap = new HashMap<>();
        subscriptionMap.put("method", "subscribe");
        subscriptionMap.putAll(subscription.build());
        send(gson.toJson(subscriptionMap));
    }

    public void subscribeToOhlc(OhlcSubscriptionBuilder subscription) {
        Map<String, Object> subscriptionMap = new HashMap<>();
        subscriptionMap.put("method", "subscribe");
        subscriptionMap.putAll(subscription.build());
        send(gson.toJson(subscriptionMap));
    }

    public void subscribeToTrade(TradeSubscriptionBuilder subscription) {
        Map<String, Object> subscriptionMap = new HashMap<>();
        subscriptionMap.put("method", "subscribe");
        subscriptionMap.putAll(subscription.build());
        send(gson.toJson(subscriptionMap));
    }

    public void subscribeToInstrument(InstrumentSubscriptionBuilder subscription) {
        Map<String, Object> subscriptionMap = new HashMap<>();
        subscriptionMap.put("method", "subscribe");
        subscriptionMap.putAll(subscription.build());
        send(gson.toJson(subscriptionMap));
    }
}
