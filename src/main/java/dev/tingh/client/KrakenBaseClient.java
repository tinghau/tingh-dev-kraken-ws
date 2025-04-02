package dev.tingh.client;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;

public abstract class KrakenBaseClient extends WebSocketClient {

    public KrakenBaseClient(URI serverUri) {
        super(serverUri);
    }

    // Common connection/auth methods

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
}