package dev.tingh.client;

import com.google.gson.Gson;
import dev.tingh.AuthTokens;
import dev.tingh.data.handler.*;
import dev.tingh.data.model.OrderData;
import dev.tingh.data.subscription.OrderSubscriptionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;

public class KrakenDataAuthClient extends KrakenBaseClient {

    private static final Logger logger = LoggerFactory.getLogger(KrakenDataAuthClient.class);

    private final AuthTokens authTokens = new AuthTokens();
    private final Gson gson = new Gson();

    private final OrderDataHandler orderDataHandler;

    public KrakenDataAuthClient(URI serverUri, String baseDirectory) {
        super(serverUri);
        this.orderDataHandler = new OrderDataHandler(baseDirectory);
    }

    // Add message handler method
    @Override
    public void onMessage(String message) {
        logger.info("Received message: {}", message);

        if (message.contains("\"type\":") && (message.contains("\"level3\""))) {
            // Parse the JSON message to OrderData object and pass it to handler
            OrderData orderData = gson.fromJson(message, OrderData.class);
            orderDataHandler.handleOrderData(orderData);
        }
        // Handle other message types...
    }

    public void subscribeToOrder(OrderSubscriptionBuilder subscription) {
        subscription.withToken(authTokens.getToken());
        send(gson.toJson(new HashMap<>(subscription.build())));
    }
}
