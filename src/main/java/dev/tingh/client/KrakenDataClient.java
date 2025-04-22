package dev.tingh.client;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.tingh.data.handler.*;
import dev.tingh.data.model.*;
import dev.tingh.data.subscription.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.HashMap;

public class KrakenDataClient extends KrakenBaseClient {

    private static final Logger logger = LoggerFactory.getLogger(KrakenDataClient.class);

    private final Gson gson = new Gson();

    private final BookDataHandler bookDataHandler;
    private final InstrumentDataHandler instrumentDataHandler;
    private final OhlcDataHandler ohlcDataHandler;
    private final TickerDataHandler tickerDataHandler;
    private final TradeDataHandler tradeDataHandler;

    public KrakenDataClient(URI serverUri, String baseDirectory) {
        super(serverUri);
        this.bookDataHandler = new BookDataHandler(baseDirectory);
        this.instrumentDataHandler = new InstrumentDataHandler(baseDirectory);
        this.ohlcDataHandler = new OhlcDataHandler(baseDirectory);
        this.tickerDataHandler = new TickerDataHandler(baseDirectory);
        this.tradeDataHandler = new TradeDataHandler(baseDirectory);
    }

    // Add message handler method
    @Override
    public void onMessage(String message) {
        logger.info("Received message: {}", message);
        
        try {
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            if (!jsonObject.has("channel")) {
                logger.warn("Message does not contain type field: {}", message);
                return;
            }
            
            String channel = jsonObject.get("channel").getAsString();
            
            switch (channel) {
                case "ticker":
                    tickerDataHandler.handleTickerData(gson.fromJson(message, TickerData.class));
                    break;
                case "book":
                    bookDataHandler.handleBookData(gson.fromJson(message, BookData.class));
                    break;
                case "ohlc":
                    ohlcDataHandler.handleOhlcData(gson.fromJson(message, OhlcData.class));
                    break;
                case "trade":
                    tradeDataHandler.handleTradeData(gson.fromJson(message, TradeData.class));
                    break;
                case "instrument":
                    instrumentDataHandler.handleInstrumentData(gson.fromJson(message, InstrumentData.class));
                    break;
                default:
                    logger.warn("Unknown message type: {}", channel);
                    break;
            }
        } catch (Exception e) {
            logger.error("Error processing message: {}", e.getMessage(), e);
        }
    }

    public void subscribeToTicker(TickerSubscriptionBuilder subscription) {
        send(gson.toJson(new HashMap<>(subscription.build())));
    }

    public void subscribeToBook(BookSubscriptionBuilder subscription) {
        send(gson.toJson(new HashMap<>(subscription.build())));
    }

    public void subscribeToOhlc(OhlcSubscriptionBuilder subscription) {
        send(gson.toJson(new HashMap<>(subscription.build())));
    }

    public void subscribeToTrade(TradeSubscriptionBuilder subscription) {
        send(gson.toJson(new HashMap<>(subscription.build())));
    }

    public void subscribeToInstrument(InstrumentSubscriptionBuilder subscription) {
        send(gson.toJson(new HashMap<>(subscription.build())));
    }
}
