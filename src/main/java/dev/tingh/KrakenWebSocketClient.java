package dev.tingh;

import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class KrakenWebSocketClient extends WebSocketClient {
    private final Gson gson = new Gson();

    public KrakenWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Connected to Kraken WebSocket API");
    }

    @Override
    public void onMessage(String message) {
        System.out.println("Received message: " + message);
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Connection closed: " + reason);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
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
}