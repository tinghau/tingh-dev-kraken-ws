package dev.tingh.client;

import com.google.gson.Gson;
import dev.tingh.data.*;
import dev.tingh.data.handler.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class KrakenDataClient extends KrakenBaseClient {

    private final Gson gson = new Gson();

    private final BookDataHandler bookDataHandler;
    private final Level3DataHandler level3DataHandler;
    private final OhlcDataHandler ohlcDataHandler;
    private final TickerDataHandler tickerDataHandler;
    private final TradeDataHandler tradeDataHandler;

    public KrakenDataClient(URI serverUri, String baseDirectory) {
        super(serverUri);
        this.bookDataHandler = new BookDataHandler(baseDirectory);
        this.level3DataHandler = new Level3DataHandler(baseDirectory);
        this.ohlcDataHandler = new OhlcDataHandler(baseDirectory);
        this.tickerDataHandler = new TickerDataHandler(baseDirectory);
        this.tradeDataHandler = new TradeDataHandler(baseDirectory);
    }

    // Add message handler method
    @Override
    public void onMessage(String message) {
        if (message.contains("\"type\":") &&
                (message.contains("\"ticker\"") || message.contains("\"ticker_snapshot\""))) {
            tickerDataHandler.handleTickerData(message);
        } else if (message.contains("\"type\":") &&
                (message.contains("\"book\"") || message.contains("\"book_snapshot\""))) {
            bookDataHandler.handleBookData(message);
        } else if (message.contains("\"type\":") &&
                (message.contains("\"level3\"") || message.contains("\"level3_snapshot\""))) {
            level3DataHandler.handleLevel3Data(message);
        } else if (message.contains("\"type\":") &&
                (message.contains("\"ohlc\"") || message.contains("\"ohlc_snapshot\""))) {
            ohlcDataHandler.handleOhlcData(message);
        } else if (message.contains("\"type\":") &&
                (message.contains("\"trade\"") || message.contains("\"trade_snapshot\""))) {
            tradeDataHandler.handleTradeData(message);
        }
        // Handle other message types...
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
