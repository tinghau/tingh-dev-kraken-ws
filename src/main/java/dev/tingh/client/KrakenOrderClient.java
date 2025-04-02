package dev.tingh.client;

import com.google.gson.Gson;
import dev.tingh.trading.AmendOrderBuilder;
import dev.tingh.trading.CancelAfterBuilder;
import dev.tingh.trading.CancelOrderBuilder;
import dev.tingh.trading.OrderBuilder;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class KrakenOrderClient extends KrakenBaseClient {
    private final Gson gson = new Gson();

    public KrakenOrderClient(URI serverUri) {
        super(serverUri);
    }

    public void sendOrder(OrderBuilder order) {
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("event", "addOrder");
        orderMap.putAll(order.build());
        send(gson.toJson(orderMap));
    }

    public void amendOrder(AmendOrderBuilder amendOrder) {
        Map<String, Object> amendOrderMap = new HashMap<>();
        amendOrderMap.put("event", "amendOrder");
        amendOrderMap.putAll(amendOrder.build());
        send(gson.toJson(amendOrderMap));
    }

    public void cancelOrder(CancelOrderBuilder order) {
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("event", "cancelOrder");
        orderMap.putAll(order.build());
        send(gson.toJson(orderMap));
    }

    public void cancelAfter(CancelAfterBuilder cancelAfter) {
        Map<String, Object> cancelAfterMap = new HashMap<>();
        cancelAfterMap.put("event", "cancelAllOrdersAfter");
        cancelAfterMap.putAll(cancelAfter.build());
        send(gson.toJson(cancelAfterMap));
    }

}