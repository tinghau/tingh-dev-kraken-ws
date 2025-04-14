package dev.tingh;

import dev.tingh.client.KrakenDataClient;
import dev.tingh.data.*;

import java.net.URI;

public class Main {
    public static void main(String[] args) {
        try {
            // Initialize KrakenDataClient
            String baseDirectory = "data"; // Directory for storing CSV files
            URI serverUri = new URI("wss://ws.kraken.com"); // Kraken WebSocket URI
            KrakenDataClient client = new KrakenDataClient(serverUri, baseDirectory);

            // Subscribe to Ticker data
            TickerSubscriptionBuilder tickerSubscription = new TickerSubscriptionBuilder("BTC/USD");
            client.subscribeToTicker(tickerSubscription);

            // Subscribe to Book data
            BookSubscriptionBuilder bookSubscription = new BookSubscriptionBuilder()
                    .symbol("BTC/USD");
            client.subscribeToBook(bookSubscription);

            // Subscribe to Level3 data
            Level3SubscriptionBuilder level3Subscription = new Level3SubscriptionBuilder()
                    .withSymbol("BTC/USD");
            client.subscribeToLevel3(level3Subscription);

            // Subscribe to OHLC data
            OhlcSubscriptionBuilder ohlcSubscription = new OhlcSubscriptionBuilder()
                    .symbol("BTC/USD")
                    .interval(1); // 1-minute interval
            client.subscribeToOhlc(ohlcSubscription);

            // Subscribe to Trade data
            TradeSubscriptionBuilder tradeSubscription = new TradeSubscriptionBuilder()
                    .symbol("BTC/USD");
            client.subscribeToTrade(tradeSubscription);

            // Subscribe to Instrument data
            InstrumentSubscriptionBuilder instrumentSubscription = new InstrumentSubscriptionBuilder()
                    .symbol("BTC/USD");
            client.subscribeToInstrument(instrumentSubscription);

            // Start the WebSocket client
            client.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}