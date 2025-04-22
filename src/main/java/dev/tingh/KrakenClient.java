package dev.tingh;

import dev.tingh.client.KrakenDataAuthClient;
import dev.tingh.client.KrakenDataClient;
import dev.tingh.client.KrakenUserClient;
import dev.tingh.data.subscription.*;
import dev.tingh.exception.ConnectException;
import dev.tingh.user.subscription.BalanceSubscriptionBuilder;
import dev.tingh.user.subscription.ExecutionSubscriptionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class KrakenClient {
    private static final Logger logger = LoggerFactory.getLogger(KrakenClient.class);
    private static final String BASE_DIRECTORY = "data";

    public static void main(String[] args) throws URISyntaxException, InterruptedException {
        new KrakenClient().start();
    }

    private KrakenDataClient dataClient;
    private KrakenDataAuthClient dataAuthClient;
    private KrakenUserClient userClient;

    public void start() throws URISyntaxException, InterruptedException {
        dataClient = connectClient();
        dataAuthClient = connectAuthClient();
        userClient = connectUserClient();

            subscribeToTicker();
            subscribeToBook();
//        subscribeToOrders();
            subscribeToOhlc();
            subscribeToTrade();
            subscribeToInstrument();
//        subscribeToBalance();
//        subscribeToExecution();
    }

    private static KrakenDataClient connectClient() throws URISyntaxException {
        URI serverUri = new URI("wss://ws.kraken.com/v2"); // Kraken WebSocket URI
        KrakenDataClient client = new KrakenDataClient(serverUri, BASE_DIRECTORY);
        return makeConnection(client);
    }

    private static KrakenDataClient makeConnection(KrakenDataClient client) {
        try {
            client.connectBlocking(10, TimeUnit.SECONDS);
            return client;
        } catch (InterruptedException e) {
            logger.error("Failed to connect to Kraken WebSocket server", e);
            throw new ConnectException(e);
        }
    }

    private static KrakenDataAuthClient connectAuthClient() throws URISyntaxException {
        URI serverUri = new URI("wss://ws-auth.kraken.com/v2"); // Kraken WebSocket URI
        KrakenDataAuthClient client = new KrakenDataAuthClient(serverUri, BASE_DIRECTORY);
        try {
            client.connectBlocking(10, TimeUnit.SECONDS);
            return client;
        } catch (InterruptedException e) {
            logger.error("Failed to connect to Kraken WebSocket server", e);
            throw new ConnectException(e);
        }
    }

    private static KrakenUserClient connectUserClient() throws URISyntaxException {
        URI serverUri = new URI("wss://ws-auth.kraken.com/v2"); // Kraken WebSocket URI
        KrakenUserClient client = new KrakenUserClient(serverUri, BASE_DIRECTORY);
        try {
            client.connectBlocking(10, TimeUnit.SECONDS);
            return client;
        } catch (InterruptedException e) {
            logger.error("Failed to connect to Kraken WebSocket server", e);
            throw new ConnectException(e);
        }
    }

    private void subscribeToInstrument() {
        InstrumentSubscriptionBuilder instrumentSubscription = new InstrumentSubscriptionBuilder();
        dataClient.subscribeToInstrument(instrumentSubscription);
    }

    private void subscribeToTrade() {
        TradeSubscriptionBuilder tradeSubscription = new TradeSubscriptionBuilder("BTC/USD");
        dataClient.subscribeToTrade(tradeSubscription);
    }

    private void subscribeToOhlc() {
        OhlcSubscriptionBuilder ohlcSubscription = new OhlcSubscriptionBuilder("BTC/USD");
        ohlcSubscription.interval(1); // 1-second interval
        dataClient.subscribeToOhlc(ohlcSubscription);
    }

    private void subscribeToOrder() {
        OrderSubscriptionBuilder orderSubscription = new OrderSubscriptionBuilder("BTC/USD");
        dataAuthClient.subscribeToOrder(orderSubscription);
    }

    private void subscribeToBook() {
        BookSubscriptionBuilder bookSubscription = new BookSubscriptionBuilder("BTC/USD");
        dataClient.subscribeToBook(bookSubscription);
    }

    private void subscribeToTicker() {
        TickerSubscriptionBuilder tickerSubscription = new TickerSubscriptionBuilder("BTC/USD");
        dataClient.subscribeToTicker(tickerSubscription);
    }

    private void subscribeToBalances() {
        BalanceSubscriptionBuilder balanceSubscription = new BalanceSubscriptionBuilder();
        userClient.subscribeToBalances(balanceSubscription);
    }

    private void subscribeToExecution() {
        ExecutionSubscriptionBuilder executionSubscriptionBuilder = new ExecutionSubscriptionBuilder();
        userClient.subscribeToExecutions(executionSubscriptionBuilder);
    }

}