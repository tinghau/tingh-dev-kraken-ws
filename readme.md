# Kraken WebSocket API Client

A simple Java client for the Kraken WebSocket API that handles real-time market data and user data. Currently, it 
writes market data to files and would support order management.


## Features

- Connect to Kraken WebSocket API v2
- Subscribe to market data channels:
    - Ticker
    - Book (Order Book)
    - Trades
    - OHLC (Candles)
    - Instruments (Assets and Pairs)
- Subscribe to authenticated channels:
    - Orders
    - Balances
    - Executions
- Add, amend and cancel orders
- Write received data to CSV files
- Configurable data storage

## Setup

### Dependencies

Add the following dependencies to your project:

```gradle
dependencies {
    implementation 'org.java-websocket:Java-WebSocket:1.5.3'
    implementation 'com.google.code.gson:gson:2.10.1'
    implementation 'org.slf4j:slf4j-api:2.0.9'
    implementation 'ch.qos.logback:logback-classic:1.4.14'
}
```

### Configuration
Create a base directory for data storage:

```java
private static final String BASE_DIRECTORY;
```

Add the Kraken private and public API keys to AuthTokens:
```java
    private static final String PUBLIC_KEY;
    private static final String PRIVATE_KEY;
```

### Run the Application

```commandLine
./gradlew build run
```