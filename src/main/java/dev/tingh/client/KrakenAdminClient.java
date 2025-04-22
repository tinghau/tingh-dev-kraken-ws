package dev.tingh.client;

import com.google.gson.Gson;
import dev.tingh.admin.subscription.PingSubscriptionBuilder;
import dev.tingh.admin.model.PongData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class KrakenAdminClient extends KrakenBaseClient {

    private static final Logger logger = LoggerFactory.getLogger(KrakenAdminClient.class);
    private final Gson gson = new Gson();

    public KrakenAdminClient(URI serverUri, String apiKey, String apiSecret) {
        super(serverUri);
    }

    public CompletableFuture<PongData> ping(PingSubscriptionBuilder pingSubscription) {
        CompletableFuture<PongData> pongFuture = new CompletableFuture<>();
        Map<String, Object> pingMap = pingSubscription.build();
        String reqid = (String) pingMap.getOrDefault("reqid", "");

        addMessageHandler(message -> {
            PongData pongData = gson.fromJson(message, PongData.class);
            if ("pong".equals(pongData.getMethod()) &&
                    (reqid.isEmpty() || reqid.equals(pongData.getReqid()))) {
                logger.info("Received pong response: " + message);
                pongFuture.complete(pongData);
                return true; // Remove this handler after processing
            }
            return false; // Keep this handler for future messages
        });

        send(gson.toJson(pingMap));
        return pongFuture;
    }

    /**
     * Add a message handler that processes incoming messages
     * @param handler function that returns true if handler should be removed after processing
     */
    private void addMessageHandler(MessageHandler handler) {
        // This method would be implemented in KrakenBaseClient
        // For now, we'll assume it handles adding message processing callbacks
    }

    @FunctionalInterface
    interface MessageHandler {
        boolean handleMessage(String message);
    }
}

