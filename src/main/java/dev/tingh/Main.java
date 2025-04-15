package dev.tingh;

import dev.tingh.client.KrakenDataClient;
import dev.tingh.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

import static java.lang.Thread.sleep;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);
    private static final String BASE_DIRECTORY = "data";

    public static void main(String[] args) {
        try {
            URI serverUri = new URI("wss://ws.kraken.com/v2"); // Kraken WebSocket URI
            KrakenDataClient client = new KrakenDataClient(serverUri, BASE_DIRECTORY);
            client.connect();

            sleep(10000);

            subscribeToTicker(client);
//            subscribeToBook(client);
//            subscribeToLevel3(client);
//            subscribeToOhlc(client);
//            subscribeToTrade(client);
//            subscribeToInstrument(client);

        } catch (Exception e) {
            logger.error("An error occurred in the main method: {}", e.getMessage(), e);
        }
    }

    private static void subscribeToInstrument(KrakenDataClient client) {
        InstrumentSubscriptionBuilder instrumentSubscription = new InstrumentSubscriptionBuilder("BTC/USD");
        client.subscribeToInstrument(instrumentSubscription);
    }

    private static void subscribeToTrade(KrakenDataClient client) {
        TradeSubscriptionBuilder tradeSubscription = new TradeSubscriptionBuilder("BTC/USD");
        client.subscribeToTrade(tradeSubscription);
    }

    private static void subscribeToOhlc(KrakenDataClient client) {
        OhlcSubscriptionBuilder ohlcSubscription = new OhlcSubscriptionBuilder("BTC/USD");
        ohlcSubscription.interval(1); // 1-minute interval
        client.subscribeToOhlc(ohlcSubscription);
    }

    private static void subscribeToLevel3(KrakenDataClient client) {
        Level3SubscriptionBuilder level3Subscription = new Level3SubscriptionBuilder("BTC/USD");
        client.subscribeToLevel3(level3Subscription);
    }

    private static void subscribeToBook(KrakenDataClient client) {
        BookSubscriptionBuilder bookSubscription = new BookSubscriptionBuilder("BTC/USD");
        client.subscribeToBook(bookSubscription);
    }

    private static void subscribeToTicker(KrakenDataClient client) {
        TickerSubscriptionBuilder tickerSubscription = new TickerSubscriptionBuilder("BTC/USD");
        client.subscribeToTicker(tickerSubscription);
    }
}