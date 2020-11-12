<p align="center">
  <br>
  <a href="https://bitvavo.com"><img src="https://bitvavo.com/assets/static/ext/logo-shape.svg" width="100" title="Bitvavo Logo"></a>
</p>

# Java Bitvavo Api
This is the Java wrapper for the Bitvavo API. This project can be used to build your own projects which interact with the Bitvavo platform. Every function available on the API can be called through a REST request or over websockets. For info on the specifics of every parameter consult the [Bitvavo API documentation](https://docs.bitvavo.com/)

* Getting started       [REST](https://github.com/bitvavo/java-bitvavo-api#getting-started) [Websocket](https://github.com/bitvavo/java-bitvavo-api#getting-started-1)
* General
  * Time                [REST](https://github.com/bitvavo/java-bitvavo-api#get-time) [Websocket](https://github.com/bitvavo/java-bitvavo-api#get-time-1)
  * Markets             [REST](https://github.com/bitvavo/java-bitvavo-api#get-markets) [Websocket](https://github.com/bitvavo/java-bitvavo-api#get-markets-1)
  * Assets              [REST](https://github.com/bitvavo/java-bitvavo-api#get-assets) [Websocket](https://github.com/bitvavo/java-bitvavo-api#get-assets-1)
* Market Data
  * Book                [REST](https://github.com/bitvavo/java-bitvavo-api#get-book-per-market) [Websocket](https://github.com/bitvavo/java-bitvavo-api#get-book-per-market-1)
  * Public Trades       [REST](https://github.com/bitvavo/java-bitvavo-api#get-trades-per-market) [Websocket](https://github.com/bitvavo/java-bitvavo-api#get-trades-per-market-1)
  * Candles             [REST](https://github.com/bitvavo/java-bitvavo-api#get-candles-per-market) [Websocket](https://github.com/bitvavo/java-bitvavo-api#get-candles-per-market-1)
  * Price Ticker        [REST](https://github.com/bitvavo/java-bitvavo-api#get-price-ticker) [Websocket](https://github.com/bitvavo/java-bitvavo-api#get-price-ticker-1)
  * Book Ticker         [REST](https://github.com/bitvavo/java-bitvavo-api#get-book-ticker) [Websocket](https://github.com/bitvavo/java-bitvavo-api#get-book-ticker-1)
  * 24 Hour Ticker      [REST](https://github.com/bitvavo/java-bitvavo-api#get-24-hour-ticker) [Websocket](https://github.com/bitvavo/java-bitvavo-api#get-24-hour-ticker-1)
* Private 
  * Place Order         [REST](https://github.com/bitvavo/java-bitvavo-api#place-order) [Websocket](https://github.com/bitvavo/java-bitvavo-api#place-order-1)
  * Update Order        [REST](https://github.com/bitvavo/java-bitvavo-api#update-order) [Websocket](https://github.com/bitvavo/java-bitvavo-api#update-order-1)
  * Get Order           [REST](https://github.com/bitvavo/java-bitvavo-api#get-order) [Websocket](https://github.com/bitvavo/java-bitvavo-api#get-order-1)
  * Cancel Order        [REST](https://github.com/bitvavo/java-bitvavo-api#cancel-order) [Websocket](https://github.com/bitvavo/java-bitvavo-api#cancel-order-1)
  * Get Orders          [REST](https://github.com/bitvavo/java-bitvavo-api#get-orders) [Websocket](https://github.com/bitvavo/java-bitvavo-api#get-orders-1)
  * Cancel Orders       [REST](https://github.com/bitvavo/java-bitvavo-api#cancel-orders) [Websocket](https://github.com/bitvavo/java-bitvavo-api#cancel-orders-1)
  * Orders Open         [REST](https://github.com/bitvavo/java-bitvavo-api#get-orders-open) [Websocket](https://github.com/bitvavo/java-bitvavo-api#get-orders-open-1)
  * Trades              [REST](https://github.com/bitvavo/java-bitvavo-api#get-trades) [Websocket](https://github.com/bitvavo/java-bitvavo-api#get-trades-1)
  * Account             [REST](https://github.com/bitvavo/java-bitvavo-api#get-account) [Websocket](https://github.com/bitvavo/java-bitvavo-api#get-account-1)
  * Balance             [REST](https://github.com/bitvavo/java-bitvavo-api#get-balance) [Websocket](https://github.com/bitvavo/java-bitvavo-api#get-balance-1)
  * Deposit Assets     [REST](https://github.com/bitvavo/java-bitvavo-api#deposit-assets) [Websocket](https://github.com/bitvavo/java-bitvavo-api#deposit-assets-1)
  * Withdraw Assets   [REST](https://github.com/bitvavo/java-bitvavo-api#withdraw-assets) [Websocket](https://github.com/bitvavo/java-bitvavo-api#withdraw-assets-1)
  * Deposit History     [REST](https://github.com/bitvavo/java-bitvavo-api#get-deposit-history) [Websocket](https://github.com/bitvavo/java-bitvavo-api#get-deposit-history-1)
  * Withdrawal History  [REST](https://github.com/bitvavo/java-bitvavo-api#get-withdrawal-history) [Websocket](https://github.com/bitvavo/java-bitvavo-api#get-withdrawal-history-1)
* [Subscriptions](https://github.com/bitvavo/java-bitvavo-api#subscriptions)
  * [Ticker Subscription](https://github.com/bitvavo/java-bitvavo-api#ticker-subscription)
  * [Ticker 24 Hour Subscription](https://github.com/bitvavo/java-bitvavo-api#ticker-24-hour-subscription)
  * [Account Subscription](https://github.com/bitvavo/java-bitvavo-api#account-subscription)
  * [Candles Subscription](https://github.com/bitvavo/java-bitvavo-api#candles-subscription)
  * [Trades Subscription](https://github.com/bitvavo/java-bitvavo-api#trades-subscription)
  * [Book Subscription](https://github.com/bitvavo/java-bitvavo-api#book-subscription)
  * [Book Subscription With Local Copy](https://github.com/bitvavo/java-bitvavo-api#book-subscription-with-local-copy)

## Installation

The java development kit 8 is required for websockets to function.

Install with maven:

`mvn clean install`

Place dependency in your project to start using the SDK:

```
<dependency>
  <groupId>com.bitvavo.api</groupId>
  <artifactId>api</artifactId>
  <version>1.0</version>
</dependency> 
```

Or you can use the example as starting point for your own project. Create a new project with the above dependency and place the example.java file in the src/main/java folder. Install through `mvn clean install` and run the example:

`
mvn exec:java "-Dexec.mainClass=com.bitvavo.api.example.example"
`

## Rate Limiting

Bitvavo uses a weight based rate limiting system, with an allowed limit of 1000 per IP or API key each minute. Please inspect each endpoint in the [Bitvavo API documentation](https://docs.bitvavo.com/) to see the weight. Failure to respect the rate limit will result in an IP or API key ban.
Since the remaining limit is returned in the header on each REST request, the remaining limit is tracked locally and can be requested through:
```
int remaining = bitvavo.getRemainingLimit();
```
The websocket functions however do not return a remaining limit, therefore the limit is only updated locally once a ban has been issued.

## REST requests

The general convention used in all functions (both REST and websockets), is that all optional parameters are passed as an JSON object, while required parameters are passed as separate values. Only when [placing orders](https://github.com/bitvavo/java-bitvavo-api#place-order) some of the optional parameters are required, since a limit order requires more information than a market order. The returned responses are all converted to either a JSONObject or a JSONArray, depending on whether the function returns a single object or a list of objects. This means that when an array is supplied, the objects can be retrieved through: `JSONObject object = array.getJSONObject(<index>);` after which separate values can be retrieved through `String <value> = object.getString(<key>);`, `Int <value> = object.getInt(<key>);` and `boolean <value> = object.getBoolean(<key>);`. For information on which type a returned value has, consult the [documentation](https://docs.bitvavo.com/)

### Getting started

The API key and secret are required for private calls and optional for public calls. The access window and debugging parameter are optional for all calls. The access window is used to determine whether the request arrived within time, the value is specified in milliseconds. You can use the [time](https://github.com/bitvavo/java-bitvavo-api#get-time) function to synchronize your time to our server time if errors arise. REST url and WS url can be used to set a different endpoint (for testing purposes). Debugging should be set to true when you want to log additional information and full responses. Any parameter can be omitted, private functions will return an error when the api key and secret have not been set.

```java
Bitvavo bitvavo = new Bitvavo(new JSONObject("{" + 
        "APIKEY: '<APIKEY>', " +
        "APISECRET: '<APISECRET>', " +
        "RESTURL: 'https://api.bitvavo.com/v2'," +
        "WSURL: 'wss://ws.bitvavo.com/v2/'," +
        "ACCESSWINDOW: 10000, " +
        "DEBUGGING: false }"));
```

### General

#### Get time

```java
System.out.println(bitvavo.time());
```
<details>
 <summary>View Response</summary>

```java
{"time": 1548690634433}
```
</details>

#### Get markets
```java
// options: market
JSONArray response = bitvavo.markets(new JSONObject());
for(int i = 0; i < response.length(); i ++) {
  System.out.println(response.getJSONObject(i).toString(2));
}
```
<details>
 <summary>View Response</summary>

```java
{
  "market": "ADA-BTC",
  "pricePrecision": 5,
  "quote": "BTC",
  "orderTypes": [
    "market",
    "limit"
  ],
  "minOrderInQuoteAsset": "0.001",
  "minOrderInBaseAsset": "100",
  "status": "trading",
  "base": "ADA"
}
{
  "market": "ADA-EUR",
  "pricePrecision": 5,
  "quote": "EUR",
  "orderTypes": [
    "market",
    "limit"
  ],
  "minOrderInQuoteAsset": "10",
  "minOrderInBaseAsset": "100",
  "status": "trading",
  "base": "ADA"
}
{
  "market": "AE-BTC",
  "pricePrecision": 5,
  "quote": "BTC",
  "orderTypes": [
    "market",
    "limit"
  ],
  "minOrderInQuoteAsset": "0.001",
  "minOrderInBaseAsset": "10",
  "status": "trading",
  "base": "AE"
}
{
  "market": "AE-EUR",
  "pricePrecision": 5,
  "quote": "EUR",
  "orderTypes": [
    "market",
    "limit"
  ],
  "minOrderInQuoteAsset": "10",
  "minOrderInBaseAsset": "10",
  "status": "trading",
  "base": "AE"
}
...
```
</details>

#### Get assets
```java
// options: symbol
JSONArray response = bitvavo.assets(new JSONObject());
for(int i = 0; i < response.length(); i ++) {
  System.out.println(response.getJSONObject(i).toString(2));
}
```
<details>
 <summary>View Response</summary>

```java
{
  "symbol": "ADA",
  "depositConfirmations": 20,
  "withdrawalFee": "0.2",
  "withdrawalMinAmount": "0.2",
  "decimals": 6,
  "name": "Cardano",
  "depositStatus": "OK",
  "depositFee": "0",
  "withdrawalStatus": "OK",
  "networks": [
    "Mainnet"
  ],
  "message": ""
}
{
  "symbol": "AE",
  "depositConfirmations": 30,
  "withdrawalFee": "2",
  "withdrawalMinAmount": "2",
  "decimals": 8,
  "name": "Aeternity",
  "depositStatus": "OK",
  "depositFee": "0",
  "withdrawalStatus": "OK",
  "networks": [
    "Mainnet"
  ],
  "message": ""
}
{
  "symbol": "AION",
  "depositConfirmations": 0,
  "withdrawalFee": "3",
  "withdrawalMinAmount": "3",
  "decimals": 8,
  "name": "Aion",
  "depositStatus": "",
  "depositFee": "0",
  "withdrawalStatus": "",
  "networks": [
    "Mainnet"
  ],
  "message": ""
}
{
  "symbol": "ANT",
  "depositConfirmations": 30,
  "withdrawalFee": "2",
  "withdrawalMinAmount": "2",
  "decimals": 8,
  "name": "Aragon",
  "depositStatus": "OK",
  "depositFee": "0",
  "withdrawalStatus": "OK",
  "networks": [
    "Mainnet"
  ],
  "message": ""
}
...
```
</details>

### Market Data

#### Get book per market
```java
// options: depth
System.out.println(bitvavo.book("BTC-EUR", new JSONObject()).toString(2));
```
<details>
 <summary>View Response</summary>

```java
{
  "market": "BTC-EUR",
  "bids": [
    [
      "2992.2",
      "0.0033437"
    ],
    [
      "2991.7",
      "0.00334426"
    ],
    [
      "2991.2",
      "0.00334482"
    ],
    [
      "2990.7",
      "0.00334538"
    ],
    [
      "2989.3",
      "2.04653335"
    ],
    ...
  ],
  "asks": [
    [
      "2992.4",
      "0.00334348"
    ],
    [
      "2992.8",
      "0.00334358"
    ],
    [
      "2992.9",
      "0.00334403"
    ],
    [
      "2993.5",
      "0.00334403"
    ],
    [
      "2997.1",
      "2.85889573"
    ],
    ...
  ],
  "nonce": 29160
}
```
</details>

#### Get trades per market
```java
// options: limit, start, end, tradeIdFrom, tradeIdTo
JSONArray response = bitvavo.publicTrades("BTC-EUR", new JSONObject());
for(int i = 0; i < response.length(); i ++) {
  System.out.println(response.getJSONObject(i).toString(2));
}
```
<details>
 <summary>View Response</summary>

```java
{
  "amount": "0.00334359",
  "side": "buy",
  "price": "2992.3",
  "id": "dcab442d-8bbf-4c8b-894a-7b1245a19411",
  "timestamp": 1548690917034
}
{
  "amount": "2.48611494",
  "side": "buy",
  "price": "3006.8",
  "id": "e731f5c2-b11c-418f-bacc-827e96662cdf",
  "timestamp": 1548690809613
}
{
  "amount": "2.1972697",
  "side": "buy",
  "price": "3005.3",
  "id": "fa9a1bf2-c39b-4874-8f88-30d7534142b1",
  "timestamp": 1548690809606
}
{
  "amount": "1.35389599",
  "side": "buy",
  "price": "3004.8",
  "id": "ca15ec29-5b82-40a8-8fd9-889ce6b35331",
  "timestamp": 1548690809600
}
{
  "amount": "3.22488139",
  "side": "buy",
  "price": "3004.2",
  "id": "5e9423b8-b9a4-4502-ba13-dd0c615d86f5",
  "timestamp": 1548690809594
}
{
  "amount": "0.80435742",
  "side": "buy",
  "price": "3000.6",
  "id": "71748b07-7412-4e5a-b112-6f6a57388209",
  "timestamp": 1548690809589
}
...
```
</details>

#### Get candles per market
```java
// options: limit, start, end
JSONArray candles = bitvavo.candles("BTC-EUR", "1h", new JSONObject());
for(int i = 0; i < candles.length(); i ++) {
  System.out.println(candles.getJSONArray(i).toString(2));
}
```
<details>
 <summary>View Response</summary>

```java
[
  1548687600000,
  "2998.2",
  "3006.8",
  "2969.8",
  "2998.2",
  "39.78074133"
]
[
  1548684000000,
  "2993.7",
  "2996.9",
  "2992.5",
  "2993.7",
  "9"
]
[
  1548676800000,
  "2999.3",
  "3002.6",
  "2989.2",
  "2999.3",
  "63.00046504"
]
[
  1548669600000,
  "3012.9",
  "3015.8",
  "3000",
  "3012.9",
  "8"
]
...
```
</details>

#### Get price ticker
```java
// options: market
JSONArray response = bitvavo.tickerPrice(new JSONObject());
for(int i = 0; i < response.length(); i ++) {
  System.out.println(response.getJSONObject(i).toString(2));
}
```
<details>
 <summary>View Response</summary>

```java
{
  "market": "EOS-EUR",
  "price": "2.0142"
}
{
  "market": "XRP-EUR",
  "price": "0.25193"
}
{
  "market": "ETH-EUR",
  "price": "91.1"
}
{
  "market": "IOST-EUR",
  "price": "0.005941"
}
{
  "market": "BCH-EUR",
  "price": "106.57"
}
{
  "market": "BTC-EUR",
  "price": "2992.3"
}
{
  "market": "STORM-EUR",
  "price": "0.0025672"
}
{
  "market": "EOS-BTC",
  "price": "0.00066289"
}
{
  "market": "BSV-EUR",
  "price": "57.6"
}
...
```
</details>

#### Get book ticker
```java
// options: market
JSONArray response = bitvavo.tickerBook(new JSONObject());
for(int i = 0; i < response.length(); i ++) {
  System.out.println(response.getJSONObject(i).toString(2));
}
```
<details>
 <summary>View Response</summary>

```java
{
  "market": "XTZ-EUR",
  "bidSize": "174.027597",
  "ask": "1.1215",
  "bid": "1.1153",
  "askSize": "137.777992"
}
{
  "market": "XVG-BTC",
  "bidSize": "26051.54759452",
  "ask": "0.00000045",
  "bid": "0.00000044",
  "askSize": "20695.30980296"
}
{
  "market": "XVG-EUR",
  "bidSize": "481927.71084337",
  "ask": "0.0042146",
  "bid": "0.00415",
  "askSize": "1636185.01588026"
}
{
  "market": "ZIL-BTC",
  "bidSize": "170694.46012519",
  "ask": "0.00000083",
  "bid": "0.00000082",
  "askSize": "171216.58399176"
}
{
  "market": "ZIL-EUR",
  "bidSize": "320182.64363349",
  "ask": "0.0077736",
  "bid": "0.0076779",
  "askSize": "157689.53870914"
}
{
  "market": "ZRX-BTC",
  "bidSize": "629.62976831",
  "ask": "0.000016917",
  "bid": "0.000016883",
  "askSize": "1550.11187678"
}
{
  "market": "ZRX-EUR",
  "bidSize": "773.69852304",
  "ask": "0.15844",
  "bid": "0.15808",
  "askSize": "840.48655261"
}
...
```
</details>

#### Get 24 hour ticker
```java
// options: market
JSONArray response = bitvavo.ticker24h(new JSONObject());
for(int i = 0; i < response.length(); i ++) {
  System.out.println(response.getJSONObject(i).toString(2));
}
```
<details>
 <summary>View Response</summary>

```java
{
  "market": "XRP-EUR",
  "volume": "621005.595523",
  "high": "0.26643",
  "last": "0.263",
  "low": "0.26101",
  "volumeQuote": "163867.92",
  "bidSize": "4234.657481",
  "ask": "0.26345",
  "bid": "0.26331",
  "open": "0.26603",
  "askSize": "2354.543624",
  "timestamp": 1565772150602
}
{
  "market": "XTZ-EUR",
  "volume": "33595.483347",
  "high": "1.2142",
  "last": "1.1153",
  "low": "1.1102",
  "volumeQuote": "38798.23",
  "bidSize": "174.018456",
  "ask": "1.1225",
  "bid": "1.1154",
  "open": "1.2142",
  "askSize": "137.65525",
  "timestamp": 1565772150835
}
{
  "market": "XVG-EUR",
  "volume": "1145787.82971657",
  "high": "0.0044139",
  "last": "0.0041602",
  "low": "0.0040849",
  "volumeQuote": "4884.18",
  "bidSize": "481927.71084337",
  "ask": "0.0042196",
  "bid": "0.00415",
  "open": "0.0043222",
  "askSize": "1634264.73386943",
  "timestamp": 1565772150971
}
{
  "market": "ZIL-EUR",
  "volume": "895543.29280087",
  "high": "0.0084042",
  "last": "0.0076923",
  "low": "0.0076094",
  "volumeQuote": "7010.22",
  "bidSize": "319827.56419206",
  "ask": "0.0077827",
  "bid": "0.0076865",
  "open": "0.0084042",
  "askSize": "157689.53870914",
  "timestamp": 1565772150805
}
...
```
</details>

### Private

#### Place order
When placing an order, make sure that the correct optional parameters are set. For a limit order it is required to set both the amount and price. A market order is valid if either the amount or the amountQuote has been set.
```java
// optional parameters: limit:(amount, price, postOnly), market:(amount, amountQuote, disableMarketProtection),
//                      stopLoss/takeProfit:(amount, amountQuote, disableMarketProtection, triggerType, triggerReference, triggerAmount)
//                      stopLossLimit/takeProfitLimit:(amount, price, postOnly, triggerType, triggerReference, triggerAmount)
//                      all orderTypes: timeInForce, selfTradePrevention, responseRequired
System.out.println(bitvavo.placeOrder("BTC-EUR", "sell", "limit",
                                      new JSONObject("{ amount: 0.1, price: 4000 }")).toString(2));
```
<details>
 <summary>View Response</summary>

```java
{
  "selfTradePrevention": "decrementAndCancel",
  "orderType": "limit",
  "side": "sell",
  "amount": "0.1",
  "visible": true,
  "onHold": "0.1",
  "orderId": "81080b09-2415-44e3-b61c-50ffca4a0221",
  "created": 1548691279124,
  "feeCurrency": "EUR",
  "filledAmount": "0",
  "market": "BTC-EUR",
  "filledAmountQuote": "0",
  "postOnly": false,
  "price": "4000",
  "amountRemaining": "0.1",
  "feePaid": "0",
  "onHoldCurrency": "BTC",
  "updated": 1548691279124,
  "timeInForce": "GTC",
  "fills": [],
  "status": "new"
}
```
</details>

#### Update order
When updating an order make sure that at least one of the optional parameters has been set. Otherwise nothing can be updated.
```java
// Optional parameters: limit:(amount, amountRemaining, price, timeInForce, selfTradePrevention, postOnly)
//          untriggered stopLoss/takeProfit:(amount, amountQuote, disableMarketProtection, triggerType, triggerReference, triggerAmount)
//                      stopLossLimit/takeProfitLimit: (amount, price, postOnly, triggerType, triggerReference, triggerAmount)
System.out.println(bitvavo.updateOrder("BTC-EUR", "81080b09-2415-44e3-b61c-50ffca4a0221",
                                        new JSONObject("{ amount: 0.2 }")));
```
<details>
 <summary>View Response</summary>

```java
{
  "selfTradePrevention": "decrementAndCancel",
  "orderType": "limit",
  "side": "sell",
  "amount": "0.2",
  "visible": true,
  "onHold": "0.2",
  "orderId": "81080b09-2415-44e3-b61c-50ffca4a0221",
  "created": 1548691279124,
  "feeCurrency": "EUR",
  "filledAmount": "0",
  "market": "BTC-EUR",
  "filledAmountQuote": "0",
  "postOnly": false,
  "price": "4000",
  "amountRemaining": "0.2",
  "feePaid": "0",
  "onHoldCurrency": "BTC",
  "updated": 1548691427403,
  "timeInForce": "GTC",
  "fills": [],
  "status": "new"
}
```
</details>

#### Get order
```java
System.out.println(bitvavo.getOrder("BTC-EUR", "81080b09-2415-44e3-b61c-50ffca4a0221"));
```
<details>
 <summary>View Response</summary>

```java
{
  "selfTradePrevention": "decrementAndCancel",
  "orderType": "limit",
  "side": "sell",
  "amount": "0.1",
  "visible": true,
  "onHold": "0.1",
  "orderId": "81080b09-2415-44e3-b61c-50ffca4a0221",
  "created": 1548691279124,
  "feeCurrency": "EUR",
  "filledAmount": "0",
  "market": "BTC-EUR",
  "filledAmountQuote": "0",
  "postOnly": false,
  "price": "4000",
  "amountRemaining": "0.1",
  "feePaid": "0",
  "onHoldCurrency": "BTC",
  "updated": 1548691279124,
  "timeInForce": "GTC",
  "fills": [],
  "status": "new"
}
```
</details>

#### Cancel order
```java
System.out.println(bitvavo.cancelOrder("BTC-EUR", "25f9459e-785a-4587-89fc-8ebf8872a952"));
```
<details>
 <summary>View Response</summary>

```java
{"orderId":"25f9459e-785a-4587-89fc-8ebf8872a952"}
```
</details>

#### Get orders
Returns the same as get order, but can be used to return multiple orders at once.
```java
// options: limit, start, end, orderIdFrom, orderIdTo
JSONArray response = bitvavo.getOrders("BTC-EUR", new JSONObject());
for(int i = 0; i < response.length(); i ++) {
  System.out.println(response.getJSONObject(i).toString(2));
}
```
<details>
 <summary>View Response</summary>

```java
{
  "selfTradePrevention": "decrementAndCancel",
  "orderType": "limit",
  "side": "buy",
  "amount": "1",
  "visible": true,
  "onHold": "0",
  "orderId": "71cfd395-dd36-4a58-88a7-750ece07f2bb",
  "created": 1548688221990,
  "feeCurrency": "EUR",
  "filledAmount": "1",
  "market": "BTC-EUR",
  "filledAmountQuote": "2993.9",
  "postOnly": false,
  "price": "3000",
  "amountRemaining": "0",
  "feePaid": "7.49",
  "onHoldCurrency": "EUR",
  "updated": 1548688221990,
  "timeInForce": "GTC",
  "fills": [{
    "amount": "1",
    "price": "2993.9",
    "settled": true,
    "fee": "7.49",
    "feeCurrency": "EUR",
    "id": "a7035af6-d3a8-4c95-89e9-372b7b72b9e8",
    "taker": true,
    "timestamp": 1548688222016
  }],
  "status": "filled"
}
{
  "selfTradePrevention": "decrementAndCancel",
  "orderType": "limit",
  "side": "buy",
  "amount": "1",
  "visible": true,
  "onHold": "0",
  "orderId": "a7844f9d-2f63-46ae-b96f-0df1a63dc6ae",
  "created": 1548688159220,
  "feeCurrency": "EUR",
  "filledAmount": "1",
  "market": "BTC-EUR",
  "filledAmountQuote": "2993.26346668",
  "postOnly": false,
  "price": "3000",
  "amountRemaining": "0",
  "feePaid": "7.48653332",
  "onHoldCurrency": "EUR",
  "updated": 1548688159220,
  "timeInForce": "GTC",
  "fills": [
    {
      "amount": "0.63653332",
      "price": "2992.9",
      "settled": true,
      "fee": "4.769426572",
      "feeCurrency": "EUR",
      "id": "e0c5db9b-0696-4003-8909-6d5dac3855bf",
      "taker": true,
      "timestamp": 1548688159225
    },
    {
      "amount": "0.36346668",
      "price": "2993.9",
      "settled": true,
      "fee": "2.717106748",
      "feeCurrency": "EUR",
      "id": "0b921924-9ee7-4276-b63c-1681f49d016c",
      "taker": true,
      "timestamp": 1548688159231
    }
  ],
  "status": "filled"
}
...
```
</details>

#### Cancel orders
Cancels all orders in a market. If no market is specified, all orders of an account will be canceled.
```java
// options: market
JSONArray response = bitvavo.cancelOrders(new JSONObject());
for(int i = 0; i < response.length(); i ++) {
  System.out.println(response.getJSONObject(i).toString(2));
}
```
<details>
 <summary>View Response</summary>

```java
{"orderId":"a655fd83-a60d-45fc-b420-143422af62b2"}
{"orderId":"35f17a0c-3344-452c-b348-20f3424574b8"}
...
```
</details>

#### Get orders open
Returns all orders which are not filled or canceled.
```java
// options: market
JSONArray response = bitvavo.ordersOpen(new JSONObject());
for(int i = 0; i < response.length(); i ++) {
  System.out.println(response.getJSONObject(i).toString(2));
}
```
<details>
 <summary>View Response</summary>

```java
{
  "selfTradePrevention": "decrementAndCancel",
  "orderType": "limit",
  "side": "buy",
  "amount": "1.1",
  "visible": true,
  "onHold": "2205.5",
  "orderId": "97d89ffc-2339-4e8f-8032-bf7b8c9ee65b",
  "created": 1548686752319,
  "feeCurrency": "EUR",
  "filledAmount": "0",
  "market": "BTC-EUR",
  "filledAmountQuote": "0",
  "postOnly": false,
  "price": "2000",
  "amountRemaining": "1.1",
  "feePaid": "0",
  "onHoldCurrency": "EUR",
  "updated": 1548686829227,
  "timeInForce": "GTC",
  "fills": [],
  "status": "new"
}
{
  "selfTradePrevention": "decrementAndCancel",
  "orderType": "limit",
  "side": "sell",
  "amount": "0.2",
  "visible": true,
  "onHold": "0.2",
  "orderId": "81080b09-2415-44e3-b61c-50ffca4a0221",
  "created": 1548691279124,
  "feeCurrency": "EUR",
  "filledAmount": "0",
  "market": "BTC-EUR",
  "filledAmountQuote": "0",
  "postOnly": false,
  "price": "4000",
  "amountRemaining": "0.2",
  "feePaid": "0",
  "onHoldCurrency": "BTC",
  "updated": 1548691427403,
  "timeInForce": "GTC",
  "fills": [],
  "status": "new"
}
...
```
</details>

#### Get trades
Returns all trades within a market for this account.
```java
// options: limit, start, end, tradeIdFrom, tradeIdTo
JSONArray response = bitvavo.trades("BTC-EUR", new JSONObject());
for(int i = 0; i < response.length(); i ++) {
  System.out.println(response.getJSONObject(i).toString(2));
}
```
<details>
 <summary>View Response</summary>

```java
{
  "market": "BTC-EUR",
  "side": "buy",
  "amount": "2.48611494",
  "price": "3006.8",
  "settled": true,
  "fee": "18.679598408",
  "feeCurrency": "EUR",
  "id": "e731f5c2-b11c-418f-bacc-827e96662cdf",
  "taker": true,
  "timestamp": 1548690809613
}
{
  "market": "BTC-EUR",
  "side": "buy",
  "amount": "2.1972697",
  "price": "3005.3",
  "settled": true,
  "fee": "16.51537059",
  "feeCurrency": "EUR",
  "id": "fa9a1bf2-c39b-4874-8f88-30d7534142b1",
  "taker": true,
  "timestamp": 1548690809606
}
{
  "market": "BTC-EUR",
  "side": "buy",
  "amount": "1.35389599",
  "price": "3004.8",
  "settled": true,
  "fee": "10.173329248",
  "feeCurrency": "EUR",
  "id": "ca15ec29-5b82-40a8-8fd9-889ce6b35331",
  "taker": true,
  "timestamp": 1548690809600
}
{
  "market": "BTC-EUR",
  "side": "buy",
  "amount": "3.22488139",
  "price": "3004.2",
  "settled": true,
  "fee": "24.211328162",
  "feeCurrency": "EUR",
  "id": "5e9423b8-b9a4-4502-ba13-dd0c615d86f5",
  "taker": true,
  "timestamp": 1548690809594
}
...
```
</details>

#### Get account
```java
System.out.println(bitvavo.account().toString(2));
```
<details>
 <summary>View Response</summary>

```java
{
  "fees": {
    "taker": "0.0025",
    "maker": "0.0015",
    "volume": "100"
  }
}
```
</details>

#### Get balance
Returns the balance for this account.
```java
// options: symbol
JSONArray response = bitvavo.balance(new JSONObject());
for(int i = 0; i < response.length(); i ++) {
  System.out.println(response.getJSONObject(i).toString(2));
}
```
<details>
 <summary>View Response</summary>

```java
{
  "symbol": "EUR",
  "available": "2599.95",
  "inOrder": "2022.65"
}
{
  "symbol": "BTC",
  "available": "1.65437",
  "inOrder": "0.079398"
}
{
  "symbol": "ADA",
  "available": "4.8",
  "inOrder": "0"
}
{
  "symbol": "BCH",
  "available": "0.00952811",
  "inOrder": "0"
}
{
  "symbol": "BSV",
  "available": "0.00952811",
  "inOrder": "0"
}
...
```
</details>

#### Deposit assets
Returns the address which can be used to deposit funds.
```java
System.out.println(bitvavo.depositAssets("BTC").toString(2));
```
<details>
 <summary>View Response</summary>

```java
{"address":"BitcoinAddress"}
```
</details>

#### Withdraw assets
Can be used to withdraw funds from Bitvavo.
```java
// options: paymentId, internal, addWithdrawalFee
System.out.println(bitvavo.withdrawAssets("BTC", "1", "BitcoinAddress", new JSONObject()).toString(2));
```
<details>
 <summary>View Response</summary>

```java
{
  "symbol": "BTC",
  "amount": "1",
  "success": true
}
```
</details>

#### Get deposit history
Returns the deposit history of your account.
```java
// options: symbol, limit, start, end
JSONArray response = bitvavo.depositHistory(new JSONObject());
for(int i = 0; i < response.length(); i ++) {
  System.out.println(response.getJSONObject(i).toString(2));
}
```
<details>
 <summary>View Response</summary>

```java
{
  "symbol": "EUR",
  "amount": "1",
  "address": "NL12RABO324234234",
  "fee": "0",
  "timestamp": 1521550025000,
  "status": "completed"
}
{
  "symbol": "BTC",
  "amount": "0.099",
  "fee": "0",
  "txId": "0c6497e608212a516b8218674cb0ca04f65b67a00fe8bddaa1ecb03e9b029255",
  "timestamp": 1511873910000,
  "status": "completed"
}
...
```
</details>

#### Get withdrawal history
Returns the withdrawal history of an account.
```java
// options: symbol, limit, start, end
JSONArray response = bitvavo.withdrawalHistory(new JSONObject());
for(int i = 0; i < response.length(); i ++) {
  System.out.println(response.getJSONObject(i).toString(2));
} 
```
<details>
 <summary>View Response</summary>

```java
{
  "symbol": "BTC",
  "amount": "0.99994",
  "address": "1CqtG5z55x7bYD5GxsAXPx59DEyujs4bjm",
  "fee": "0.00006",
  "timestamp": 1548691806000,
  "status": "awaiting_processing"
}
{
  "symbol": "BTC",
  "amount": "0.99994",
  "address": "1CqtG5z55x7bYD5GxsAXPx59DEyujs4bjm",
  "fee": "0.00006",
  "timestamp": 1548691791000,
  "status": "awaiting_processing"
}
{
  "symbol": "BTC",
  "amount": "0.99994",
  "address": "1CqtG5z55x7bYD5GxsAXPx59DEyujs4bjm",
  "fee": "0.00006",
  "timestamp": 1548687467000,
  "status": "awaiting_processing"
}
{
  "symbol": "BTC",
  "amount": "0.99994",
  "address": "1CqtG5z55x7bYD5GxsAXPx59DEyujs4bjm",
  "fee": "0.00006",
  "timestamp": 1548682993000,
  "status": "awaiting_processing"
}
{
  "symbol": "BTC",
  "amount": "0.09994",
  "address": "1CqtG5z55x7bYD5GxsAXPx59DEyujs4bjm",
  "fee": "0.00006",
  "timestamp": 1548425559000,
  "status": "awaiting_processing"
}
{
  "symbol": "EUR",
  "amount": "50",
  "address": "NL123BIM",
  "fee": "0",
  "timestamp": 1548409721000,
  "status": "completed"
}
{
  "symbol": "BTC",
  "amount": "0.01939",
  "address": "3QpyxeA7yWWsSURXEmuBBzHpxjqn7Rbyme",
  "fee": "0.00002",
  "txId": "da2299c86fce67eb899aeaafbe1f81cf663a3850cf9f3337c92b2d87945532db",
  "timestamp": 1537803091000,
  "status": "completed"
}
...
```
</details>

## Websockets

All requests which can be done through REST requests can also be performed over websockets. Bitvavo also provides six [subscriptions](https://github.com/bitvavo/java-bitvavo-api#subscriptions). If subscribed to these, updates specific for that type/market are pushed immediately.

### Getting started

The websocket object should be initialized through the `bitvavo.newWebsocket();` function. The api key and secret are copied from the bitvavo object. Therefore if you want to use the private portion of the websockets API, you should set both the key and secret as specified in [REST requests](https://github.com/bitvavo/java-bitvavo-api#rest-requests). After this the error callback should be set through the `ws.setErrorCallback()` function. All callbacks used are MessageHandlers from the WebsocketClientEndpoint class, where every MessageHandler contains a `handleMessage()` function. Since the returned response might be of type JSONObject or type JSONArray, the response will always be encapsulated by an object with as key "response" and as value either a JSONObject or JSONArray. The same applies to callbacks of other functions, this means that the minimal setup for the time function of websockets is as follows.

```java
Bitvavo.Websocket ws = bitvavo.newWebsocket();

ws.setErrorCallback(new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject error) {
    System.out.println("Handle error here " + error);
  }
});

ws.time(new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    System.out.println("Handle your response here " + responseObject.getJSONObject("response"));
  }
});
```

If the response would be a JSONArray the appropriate setup would be as follows:
```java
Bitvavo.Websocket ws = bitvavo.newWebsocket();

ws.setErrorCallback(new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject error) {
    System.out.println("Handle error here " + error);
  }
});

ws.markets(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    JSONArray response = responseObject.getJSONArray("response");
    for (int i = 0; i < response.size(); i ++) {
      System.out.println("Handle each entry here " + response.getJSONObject(i).toString(2));
    }
  }
});
```

The only exception in callbacks is the [`ws.subscriptionBook()`](https://github.com/bitvavo/java-bitvavo-api#book-subscription-with-local-copy) function. This function returns a map of strings to objects, where the strings are "bids" and "asks" and the objects are lists of lists of values. The callback should be set in the following manner: 
```java
Bitvavo.Websocket ws = bitvavo.newWebsocket();

ws.setErrorCallback(new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject error) {
    System.out.println("Handle error here " + error);
  }
});

ws.subscriptionBook("BTC-EUR", new WebsocketClientEndpoint.BookHandler() {
  public void handleBook(Map<String, Object> book) {
    List<List<String>> bids = (List<List<String>>)book.get("bids");
    List<List<String>> asks = (List<List<String>>)book.get("asks");
    String nonce = (String)book.get("nonce");
    for (int i = 0; i < bids.size(); i++) {
      System.out.println("Handle the book here, bids: " + bids.get(i).get(0) + " amount: " + bids.get(i).get(1));
    }
    for (int j = 0; j < asks.size(); j++) {
      System.out.println("Handle the book here, asks: " + asks.get(j).get(0) + " amount: " + asks.get(j).get(1));
    }
  }
});
```

### Public

#### Get time
```java
ws.time(new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    System.out.println(responseObject.getJSONObject("response").toString(2));
  }
});
```
<details>
 <summary>View Response</summary>

```java
{"time":1543565306770}
```
</details>

#### Get markets
```java
// options: market
ws.markets(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    JSONArray response = responseObject.getJSONArray("response");
    for (int i = 0; i < response.size(); i ++) {
      System.out.println(response.getJSONObject(i).toString(2));
    }
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "market": "ADA-BTC",
  "pricePrecision": 5,
  "quote": "BTC",
  "orderTypes": [
    "market",
    "limit"
  ],
  "minOrderInQuoteAsset": "0.001",
  "minOrderInBaseAsset": "100",
  "status": "trading",
  "base": "ADA"
}
{
  "market": "ADA-EUR",
  "pricePrecision": 5,
  "quote": "EUR",
  "orderTypes": [
    "market",
    "limit"
  ],
  "minOrderInQuoteAsset": "10",
  "minOrderInBaseAsset": "100",
  "status": "trading",
  "base": "ADA"
}
{
  "market": "AE-BTC",
  "pricePrecision": 5,
  "quote": "BTC",
  "orderTypes": [
    "market",
    "limit"
  ],
  "minOrderInQuoteAsset": "0.001",
  "minOrderInBaseAsset": "10",
  "status": "trading",
  "base": "AE"
}
{
  "market": "AE-EUR",
  "pricePrecision": 5,
  "quote": "EUR",
  "orderTypes": [
    "market",
    "limit"
  ],
  "minOrderInQuoteAsset": "10",
  "minOrderInBaseAsset": "10",
  "status": "trading",
  "base": "AE"
}
...
```
</details>

#### Get assets
```java
// options: symbol
ws.assets(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    JSONArray response = responseObject.getJSONArray("response");
    for (int i = 0; i < response.length(); i ++) {
      System.out.println(response.getJSONObject(i).toString(2));
    }
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "symbol": "ADA",
  "depositConfirmations": 20,
  "withdrawalFee": "0.2",
  "withdrawalMinAmount": "0.2",
  "decimals": 6,
  "name": "Cardano",
  "depositStatus": "OK",
  "depositFee": "0",
  "withdrawalStatus": "OK",
  "networks": [
    "Mainnet"
  ],
  "message": ""
}
{
  "symbol": "AE",
  "depositConfirmations": 30,
  "withdrawalFee": "2",
  "withdrawalMinAmount": "2",
  "decimals": 8,
  "name": "Aeternity",
  "depositStatus": "OK",
  "depositFee": "0",
  "withdrawalStatus": "OK",
  "networks": [
    "Mainnet"
  ],
  "message": ""
}
{
  "symbol": "AION",
  "depositConfirmations": 0,
  "withdrawalFee": "3",
  "withdrawalMinAmount": "3",
  "decimals": 8,
  "name": "Aion",
  "depositStatus": "",
  "depositFee": "0",
  "withdrawalStatus": "",
  "networks": [
    "Mainnet"
  ],
  "message": ""
}
{
  "symbol": "ANT",
  "depositConfirmations": 30,
  "withdrawalFee": "2",
  "withdrawalMinAmount": "2",
  "decimals": 8,
  "name": "Aragon",
  "depositStatus": "OK",
  "depositFee": "0",
  "withdrawalStatus": "OK",
  "networks": [
    "Mainnet"
  ],
  "message": ""
}
...
```
</details>

#### Get book per market
```java
// options: depth
ws.book("BTC-EUR", new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    System.out.println(responseObject.getJSONObject("response").toString(2));
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "market": "BTC-EUR",
  "bids": [
    [
      "2992.2",
      "0.0033437"
    ],
    [
      "2991.7",
      "0.00334426"
    ],
    [
      "2991.2",
      "0.00334482"
    ],
    [
      "2990.7",
      "0.00334538"
    ],
    [
      "2989.3",
      "2.04653335"
    ],
    ...
  ],
  "asks": [
    [
      "2992.4",
      "0.00334348"
    ],
    [
      "2992.8",
      "0.00334358"
    ],
    [
      "2992.9",
      "0.00334403"
    ],
    [
      "2993.5",
      "0.00334403"
    ],
    [
      "2997.1",
      "2.85889573"
    ],
    ...
  ],
  "nonce": 29160
}
```
</details>

#### Get trades per market
```java
// options: limit, start, end, tradeIdFrom, tradeIdTo
ws.publicTrades("BTC-EUR", new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    JSONArray response = responseObject.getJSONArray("response");
    for (int i = 0; i < response.length(); i ++) {
      System.out.println(response.getJSONObject(i).toString(2));
    }
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "amount": "0.00334359",
  "side": "buy",
  "price": "2992.3",
  "id": "dcab442d-8bbf-4c8b-894a-7b1245a19411",
  "timestamp": 1548690917034
}
{
  "amount": "2.48611494",
  "side": "buy",
  "price": "3006.8",
  "id": "e731f5c2-b11c-418f-bacc-827e96662cdf",
  "timestamp": 1548690809613
}
{
  "amount": "2.1972697",
  "side": "buy",
  "price": "3005.3",
  "id": "fa9a1bf2-c39b-4874-8f88-30d7534142b1",
  "timestamp": 1548690809606
}
{
  "amount": "1.35389599",
  "side": "buy",
  "price": "3004.8",
  "id": "ca15ec29-5b82-40a8-8fd9-889ce6b35331",
  "timestamp": 1548690809600
}
{
  "amount": "3.22488139",
  "side": "buy",
  "price": "3004.2",
  "id": "5e9423b8-b9a4-4502-ba13-dd0c615d86f5",
  "timestamp": 1548690809594
}
{
  "amount": "0.80435742",
  "side": "buy",
  "price": "3000.6",
  "id": "71748b07-7412-4e5a-b112-6f6a57388209",
  "timestamp": 1548690809589
}
...
```
</details>

#### Get candles per market
```java
// options: limit, start, end
ws.candles("BTC-EUR", "1h", new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    JSONArray response = responseObject.getJSONArray("response");
    for (int i = 0; i < response.length(); i ++) {
      System.out.println(response.getJSONArray(i).toString(2));
    }
  }
});
```
<details>
 <summary>View Response</summary>

```java
[
  1548687600000,
  "2998.2",
  "3006.8",
  "2969.8",
  "2998.2",
  "39.78074133"
]
[
  1548684000000,
  "2993.7",
  "2996.9",
  "2992.5",
  "2993.7",
  "9"
]
[
  1548676800000,
  "2999.3",
  "3002.6",
  "2989.2",
  "2999.3",
  "63.00046504"
]
[
  1548669600000,
  "3012.9",
  "3015.8",
  "3000",
  "3012.9",
  "8"
]
...
```
</details>

#### Get price ticker
```java
// options: market
ws.tickerPrice(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    JSONArray response = responseObject.getJSONArray("response");
    for (int i = 0; i < response.length(); i ++) {
      System.out.println(response.getJSONObject(i).toString(2));
    }
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "market": "EOS-EUR",
  "price": "2.0142"
}
{
  "market": "XRP-EUR",
  "price": "0.25193"
}
{
  "market": "ETH-EUR",
  "price": "91.1"
}
{
  "market": "IOST-EUR",
  "price": "0.005941"
}
{
  "market": "BCH-EUR",
  "price": "106.57"
}
{
  "market": "BTC-EUR",
  "price": "2992.3"
}
{
  "market": "STORM-EUR",
  "price": "0.0025672"
}
{
  "market": "EOS-BTC",
  "price": "0.00066289"
}
{
  "market": "BSV-EUR",
  "price": "57.6"
}
...
```
</details>

#### Get book ticker
```java
// options: market
ws.tickerBook(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    JSONArray response = responseObject.getJSONArray("response");
    for (int i = 0; i < response.length(); i ++) {
      System.out.println(response.getJSONObject(i).toString(2));
    }
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "market": "XTZ-EUR",
  "bidSize": "174.027597",
  "ask": "1.1215",
  "bid": "1.1153",
  "askSize": "137.777992"
}
{
  "market": "XVG-BTC",
  "bidSize": "26051.54759452",
  "ask": "0.00000045",
  "bid": "0.00000044",
  "askSize": "20695.30980296"
}
{
  "market": "XVG-EUR",
  "bidSize": "481927.71084337",
  "ask": "0.0042146",
  "bid": "0.00415",
  "askSize": "1636185.01588026"
}
{
  "market": "ZIL-BTC",
  "bidSize": "170694.46012519",
  "ask": "0.00000083",
  "bid": "0.00000082",
  "askSize": "171216.58399176"
}
{
  "market": "ZIL-EUR",
  "bidSize": "320182.64363349",
  "ask": "0.0077736",
  "bid": "0.0076779",
  "askSize": "157689.53870914"
}
{
  "market": "ZRX-BTC",
  "bidSize": "629.62976831",
  "ask": "0.000016917",
  "bid": "0.000016883",
  "askSize": "1550.11187678"
}
{
  "market": "ZRX-EUR",
  "bidSize": "773.69852304",
  "ask": "0.15844",
  "bid": "0.15808",
  "askSize": "840.48655261"
}
...
```
</details>

#### Get 24 hour ticker
```java
// options: market
ws.ticker24h(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    JSONArray response = responseObject.getJSONArray("response");
    for (int i = 0; i < response.length(); i ++) {
      System.out.println(response.getJSONObject(i).toString(2));
    }
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "market": "XRP-EUR",
  "volume": "621005.595523",
  "high": "0.26643",
  "last": "0.263",
  "low": "0.26101",
  "volumeQuote": "163867.92",
  "bidSize": "4234.657481",
  "ask": "0.26345",
  "bid": "0.26331",
  "open": "0.26603",
  "askSize": "2354.543624",
  "timestamp": 1565772150602
}
{
  "market": "XTZ-EUR",
  "volume": "33595.483347",
  "high": "1.2142",
  "last": "1.1153",
  "low": "1.1102",
  "volumeQuote": "38798.23",
  "bidSize": "174.018456",
  "ask": "1.1225",
  "bid": "1.1154",
  "open": "1.2142",
  "askSize": "137.65525",
  "timestamp": 1565772150835
}
{
  "market": "XVG-EUR",
  "volume": "1145787.82971657",
  "high": "0.0044139",
  "last": "0.0041602",
  "low": "0.0040849",
  "volumeQuote": "4884.18",
  "bidSize": "481927.71084337",
  "ask": "0.0042196",
  "bid": "0.00415",
  "open": "0.0043222",
  "askSize": "1634264.73386943",
  "timestamp": 1565772150971
}
{
  "market": "ZIL-EUR",
  "volume": "895543.29280087",
  "high": "0.0084042",
  "last": "0.0076923",
  "low": "0.0076094",
  "volumeQuote": "7010.22",
  "bidSize": "319827.56419206",
  "ask": "0.0077827",
  "bid": "0.0076865",
  "open": "0.0084042",
  "askSize": "157689.53870914",
  "timestamp": 1565772150805
}
...
```
</details>

### Private

#### Place order
When placing an order, make sure that the correct optional parameters are set. For a limit order it is required to set both the amount and price. A market order is valid if either the amount or the amountQuote has been set.
```java
// optional parameters: limit:(amount, price, postOnly), market:(amount, amountQuote, disableMarketProtection),
//                      stopLoss/takeProfit:(amount, amountQuote, disableMarketProtection, triggerType, triggerReference, triggerAmount)
//                      stopLossLimit/takeProfitLimit:(amount, price, postOnly, triggerType, triggerReference, triggerAmount)
//                      all orderTypes: timeInForce, selfTradePrevention, responseRequired
ws.placeOrder("BTC-EUR", "sell", "limit", new JSONObject("{ amount: 0.1, price: 4000 }"),
  new WebsocketClientEndpoint.MessageHandler() {
    public void handleMessage(JSONObject responseObject) {
      System.out.println(responseObject.getJSONObject("response").toString(2));
    }
  });
```
<details>
 <summary>View Response</summary>

```java
{
  "selfTradePrevention": "decrementAndCancel",
  "orderType": "limit",
  "side": "sell",
  "amount": "0.1",
  "visible": true,
  "onHold": "0.1",
  "orderId": "81080b09-2415-44e3-b61c-50ffca4a0221",
  "created": 1548691279124,
  "feeCurrency": "EUR",
  "filledAmount": "0",
  "market": "BTC-EUR",
  "filledAmountQuote": "0",
  "postOnly": false,
  "price": "4000",
  "amountRemaining": "0.1",
  "feePaid": "0",
  "onHoldCurrency": "BTC",
  "updated": 1548691279124,
  "timeInForce": "GTC",
  "fills": [],
  "status": "new"
}
```
</details>

#### Update order
When updating an order make sure that at least one of the optional parameters has been set. Otherwise nothing can be updated.
```java
// Optional parameters: limit:(amount, amountRemaining, price, timeInForce, selfTradePrevention, postOnly)
//          untriggered stopLoss/takeProfit:(amount, amountQuote, disableMarketProtection, triggerType, triggerReference, triggerAmount)
//                      stopLossLimit/takeProfitLimit: (amount, price, postOnly, triggerType, triggerReference, triggerAmount)
ws.updateOrder("BTC-EUR", "81080b09-2415-44e3-b61c-50ffca4a0221", new JSONObject("{ amount: 0.2 }"),
  new WebsocketClientEndpoint.MessageHandler() {
    public void handleMessage(JSONObject responseObject) {
      System.out.println(responseObject.getJSONObject("response").toString(2));
    }
  });
```
<details>
 <summary>View Response</summary>

```java
{
  "selfTradePrevention": "decrementAndCancel",
  "orderType": "limit",
  "side": "sell",
  "amount": "0.2",
  "visible": true,
  "onHold": "0.2",
  "orderId": "81080b09-2415-44e3-b61c-50ffca4a0221",
  "created": 1548691279124,
  "feeCurrency": "EUR",
  "filledAmount": "0",
  "market": "BTC-EUR",
  "filledAmountQuote": "0",
  "postOnly": false,
  "price": "4000",
  "amountRemaining": "0.2",
  "feePaid": "0",
  "onHoldCurrency": "BTC",
  "updated": 1548691427403,
  "timeInForce": "GTC",
  "fills": [],
  "status": "new"
}
```
</details>

#### Get order
```java
ws.getOrder("BTC-EUR", "81080b09-2415-44e3-b61c-50ffca4a0221", new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    System.out.println(responseObject.getJSONObject("response").toString(2));
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "selfTradePrevention": "decrementAndCancel",
  "orderType": "limit",
  "side": "sell",
  "amount": "0.1",
  "visible": true,
  "onHold": "0.1",
  "orderId": "81080b09-2415-44e3-b61c-50ffca4a0221",
  "created": 1548691279124,
  "feeCurrency": "EUR",
  "filledAmount": "0",
  "market": "BTC-EUR",
  "filledAmountQuote": "0",
  "postOnly": false,
  "price": "4000",
  "amountRemaining": "0.1",
  "feePaid": "0",
  "onHoldCurrency": "BTC",
  "updated": 1548691279124,
  "timeInForce": "GTC",
  "fills": [],
  "status": "new"
}
```
</details>

#### Cancel order
```java
ws.cancelOrder("BTC-EUR", "afc383f8-36bb-4fa9-b511-2e35b51b1fa1", new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    System.out.println(responseObject.getJSONObject("response").toString(2));
  }
});
```
<details>
 <summary>View Response</summary>

```java
{"orderId":"25f9459e-785a-4587-89fc-8ebf8872a952"}
```
</details>

#### Get orders
Returns the same as get order, but can be used to return multiple orders at once.
```java
// options: limit, start, end, orderIdFrom, orderIdTo
ws.getOrders("BTC-EUR", new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    JSONArray response = responseObject.getJSONArray("response");
    for (int i = 0; i < response.length(); i ++) {
      System.out.println(response.getJSONObject(i).toString(2));
    }
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "selfTradePrevention": "decrementAndCancel",
  "orderType": "limit",
  "side": "buy",
  "amount": "1",
  "visible": true,
  "onHold": "0",
  "orderId": "71cfd395-dd36-4a58-88a7-750ece07f2bb",
  "created": 1548688221990,
  "feeCurrency": "EUR",
  "filledAmount": "1",
  "market": "BTC-EUR",
  "filledAmountQuote": "2993.9",
  "postOnly": false,
  "price": "3000",
  "amountRemaining": "0",
  "feePaid": "7.49",
  "onHoldCurrency": "EUR",
  "updated": 1548688221990,
  "timeInForce": "GTC",
  "fills": [{
    "amount": "1",
    "price": "2993.9",
    "settled": true,
    "fee": "7.49",
    "feeCurrency": "EUR",
    "id": "a7035af6-d3a8-4c95-89e9-372b7b72b9e8",
    "taker": true,
    "timestamp": 1548688222016
  }],
  "status": "filled"
}
{
  "selfTradePrevention": "decrementAndCancel",
  "orderType": "limit",
  "side": "buy",
  "amount": "1",
  "visible": true,
  "onHold": "0",
  "orderId": "a7844f9d-2f63-46ae-b96f-0df1a63dc6ae",
  "created": 1548688159220,
  "feeCurrency": "EUR",
  "filledAmount": "1",
  "market": "BTC-EUR",
  "filledAmountQuote": "2993.26346668",
  "postOnly": false,
  "price": "3000",
  "amountRemaining": "0",
  "feePaid": "7.48653332",
  "onHoldCurrency": "EUR",
  "updated": 1548688159220,
  "timeInForce": "GTC",
  "fills": [
    {
      "amount": "0.63653332",
      "price": "2992.9",
      "settled": true,
      "fee": "4.769426572",
      "feeCurrency": "EUR",
      "id": "e0c5db9b-0696-4003-8909-6d5dac3855bf",
      "taker": true,
      "timestamp": 1548688159225
    },
    {
      "amount": "0.36346668",
      "price": "2993.9",
      "settled": true,
      "fee": "2.717106748",
      "feeCurrency": "EUR",
      "id": "0b921924-9ee7-4276-b63c-1681f49d016c",
      "taker": true,
      "timestamp": 1548688159231
    }
  ],
  "status": "filled"
}
...
```
</details>

#### Cancel orders
Cancels all orders in a market. If no market is specified, all orders of an account will be canceled.
```java
// options: market
ws.cancelOrders(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    JSONArray response = responseObject.getJSONArray("response");
    for (int i = 0; i < response.length(); i ++) {
      System.out.println(response.getJSONObject(i).toString(2));
    }
  }
});
```
<details>
 <summary>View Response</summary>

```java
{"orderId":"a655fd83-a60d-45fc-b420-143422af62b2"}
{"orderId":"35f17a0c-3344-452c-b348-20f3424574b8"}
...
```
</details>

#### Get orders open
Returns all orders which are not filled or canceled.
```java
// options: market
ws.ordersOpen(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    JSONArray response = responseObject.getJSONArray("response");
    for (int i = 0; i < response.length(); i ++) {
      System.out.println(response.getJSONObject(i).toString(2));
    }
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "selfTradePrevention": "decrementAndCancel",
  "orderType": "limit",
  "side": "buy",
  "amount": "1.1",
  "visible": true,
  "onHold": "2205.5",
  "orderId": "97d89ffc-2339-4e8f-8032-bf7b8c9ee65b",
  "created": 1548686752319,
  "feeCurrency": "EUR",
  "filledAmount": "0",
  "market": "BTC-EUR",
  "filledAmountQuote": "0",
  "postOnly": false,
  "price": "2000",
  "amountRemaining": "1.1",
  "feePaid": "0",
  "onHoldCurrency": "EUR",
  "updated": 1548686829227,
  "timeInForce": "GTC",
  "fills": [],
  "status": "new"
}
{
  "selfTradePrevention": "decrementAndCancel",
  "orderType": "limit",
  "side": "sell",
  "amount": "0.2",
  "visible": true,
  "onHold": "0.2",
  "orderId": "81080b09-2415-44e3-b61c-50ffca4a0221",
  "created": 1548691279124,
  "feeCurrency": "EUR",
  "filledAmount": "0",
  "market": "BTC-EUR",
  "filledAmountQuote": "0",
  "postOnly": false,
  "price": "4000",
  "amountRemaining": "0.2",
  "feePaid": "0",
  "onHoldCurrency": "BTC",
  "updated": 1548691427403,
  "timeInForce": "GTC",
  "fills": [],
  "status": "new"
}
...
```
</details>

#### Get trades
Returns all trades within a market for this account.
```java
// options: limit, start, end, tradeIdFrom, tradeIdTo
ws.trades("BTC-EUR", new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    JSONArray response = responseObject.getJSONArray("response");
    for (int i = 0; i < response.length(); i ++) {
      System.out.println(response.getJSONObject(i).toString(2));
    }
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "market": "BTC-EUR",
  "side": "buy",
  "amount": "2.48611494",
  "price": "3006.8",
  "settled": true,
  "fee": "18.679598408",
  "feeCurrency": "EUR",
  "id": "e731f5c2-b11c-418f-bacc-827e96662cdf",
  "taker": true,
  "timestamp": 1548690809613
}
{
  "market": "BTC-EUR",
  "side": "buy",
  "amount": "2.1972697",
  "price": "3005.3",
  "settled": true,
  "fee": "16.51537059",
  "feeCurrency": "EUR",
  "id": "fa9a1bf2-c39b-4874-8f88-30d7534142b1",
  "taker": true,
  "timestamp": 1548690809606
}
{
  "market": "BTC-EUR",
  "side": "buy",
  "amount": "1.35389599",
  "price": "3004.8",
  "settled": true,
  "fee": "10.173329248",
  "feeCurrency": "EUR",
  "id": "ca15ec29-5b82-40a8-8fd9-889ce6b35331",
  "taker": true,
  "timestamp": 1548690809600
}
{
  "market": "BTC-EUR",
  "side": "buy",
  "amount": "3.22488139",
  "price": "3004.2",
  "settled": true,
  "fee": "24.211328162",
  "feeCurrency": "EUR",
  "id": "5e9423b8-b9a4-4502-ba13-dd0c615d86f5",
  "taker": true,
  "timestamp": 1548690809594
}
...
```
</details>

#### Get account
```java
ws.account(new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    JSONObject response = responseObject.getJSONObject("response");
    System.out.println(response.toString(2));
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "fees": {
    "taker": "0.0025",
    "maker": "0.0015",
    "volume": "100"
  }
}
```
</details>

#### Get balance
Returns the balance for this account.
```java
// options: symbol
ws.balance(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    JSONArray response = responseObject.getJSONArray("response");
    for (int i = 0; i < response.length(); i ++) {
      System.out.println(response.getJSONObject(i).toString(2));
    }
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "symbol": "EUR",
  "available": "2599.95",
  "inOrder": "2022.65"
}
{
  "symbol": "BTC",
  "available": "1.65437",
  "inOrder": "0.079398"
}
{
  "symbol": "ADA",
  "available": "4.8",
  "inOrder": "0"
}
{
  "symbol": "BCH",
  "available": "0.00952811",
  "inOrder": "0"
}
{
  "symbol": "BSV",
  "available": "0.00952811",
  "inOrder": "0"
}
...
```
</details>

#### Deposit assets
Returns the address which can be used to deposit funds.
```java
ws.depositAssets("BTC", new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    System.out.println(responseObject.getJSONObject("response").toString(2));
  }
});
```
<details>
 <summary>View Response</summary>

```java
{"address":"BitcoinAddress"}
```
</details>

#### Withdraw assets
Can be used to withdraw funds from Bitvavo.
```java
// options: paymentId, internal, addWithdrawalFee
ws.withdrawAssets("BTC", "1", "BitcoinAddress", new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    System.out.println(responseObject.getJSONObject("response").toString(2));
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "symbol": "BTC",
  "amount": "1",
  "success": true
}
```
</details>

#### Get deposit history
Returns the deposit history of your account.
```java
// options symbol, limit, start, end
ws.depositHistory(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    JSONArray response = responseObject.getJSONArray("response");
    for (int i = 0; i < response.length(); i ++) {
      System.out.println(response.getJSONObject(i).toString(2));
    }
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "symbol": "EUR",
  "amount": "1",
  "address": "NL12RABO324234234",
  "fee": "0",
  "timestamp": 1521550025000,
  "status": "completed"
}
{
  "symbol": "BTC",
  "amount": "0.099",
  "fee": "0",
  "txId": "0c6497e608212a516b8218674cb0ca04f65b67a00fe8bddaa1ecb03e9b029255",
  "timestamp": 1511873910000,
  "status": "completed"
}
...
```
</details>

#### Get withdrawal history
Returns the withdrawal history of an account.
```java
// options: symbol, limit, start, end
ws.withdrawalHistory(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject responseObject) {
    JSONArray response = responseObject.getJSONArray("response");
    for (int i = 0; i < response.length(); i ++) {
      System.out.println(response.getJSONObject(i).toString(2));
    }
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "symbol": "BTC",
  "amount": "0.99994",
  "address": "1CqtG5z55x7bYD5GxsAXPx59DEyujs4bjm",
  "fee": "0.00006",
  "timestamp": 1548691806000,
  "status": "awaiting_processing"
}
{
  "symbol": "BTC",
  "amount": "0.99994",
  "address": "1CqtG5z55x7bYD5GxsAXPx59DEyujs4bjm",
  "fee": "0.00006",
  "timestamp": 1548691791000,
  "status": "awaiting_processing"
}
{
  "symbol": "BTC",
  "amount": "0.99994",
  "address": "1CqtG5z55x7bYD5GxsAXPx59DEyujs4bjm",
  "fee": "0.00006",
  "timestamp": 1548687467000,
  "status": "awaiting_processing"
}
{
  "symbol": "BTC",
  "amount": "0.99994",
  "address": "1CqtG5z55x7bYD5GxsAXPx59DEyujs4bjm",
  "fee": "0.00006",
  "timestamp": 1548682993000,
  "status": "awaiting_processing"
}
{
  "symbol": "BTC",
  "amount": "0.09994",
  "address": "1CqtG5z55x7bYD5GxsAXPx59DEyujs4bjm",
  "fee": "0.00006",
  "timestamp": 1548425559000,
  "status": "awaiting_processing"
}
{
  "symbol": "EUR",
  "amount": "50",
  "address": "NL123BIM",
  "fee": "0",
  "timestamp": 1548409721000,
  "status": "completed"
}
{
  "symbol": "BTC",
  "amount": "0.01939",
  "address": "3QpyxeA7yWWsSURXEmuBBzHpxjqn7Rbyme",
  "fee": "0.00002",
  "txId": "da2299c86fce67eb899aeaafbe1f81cf663a3850cf9f3337c92b2d87945532db",
  "timestamp": 1537803091000,
  "status": "completed"
}
...
```
</details>

### Subscriptions

#### Ticker subscription
Sends an update every time the best bid, best ask or last price changed.
```java
ws.subscriptionTicker("BTC-EUR", new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject response) {
    System.out.println(response.toString(2));
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "market": "BTC-EUR",
  "bestAsk": "9332.9",
  "bestBidSize": "0.10654906",
  "event": "ticker",
  "bestBid": "9330.9",
  "bestAskSize": "0.10937317",
  "lastPrice": "9335"
}
```
</details>

#### Ticker 24 hour subscription
Updated ticker24h objects are sent on this channel once per second. A ticker24h object is considered updated if one of the values besides timestamp has changed.
```java
ws.subscriptionTicker24h("BTC-EUR", new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject response) {
    System.out.println(response.toString(2));
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "market": "BTC-EUR",
  "volume": "299.98297637",
  "high": "10111",
  "last": "9327.9",
  "low": "9291.5",
  "volumeQuote": "2909883.39",
  "bidSize": "0.10663934",
  "ask": "9323.1",
  "bid": "9323",
  "open": "10111",
  "askSize": "0.10948854",
  "timestamp": 1565773967750
}
```
</details>

#### Account subscription
Sends an update whenever an event happens which is related to the account. These are ‘order’ events (create, update, cancel) or ‘fill’ events (a trade occurred).
```java
ws.subscriptionAccount("BTC-EUR", new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject response) {
    System.out.println(response.toString(2));
  }
});
```
<details>
 <summary>View Response</summary>

```java
Fill:
{
  "market": "BTC-EUR",
  "side": "buy",
  "amount": "1",
  "orderId": "5586b6b2-4a65-469e-935c-43c929da562e",
  "fillId": "1cf57e8d-c3ca-4b0b-ac7a-ca7c808703bc",
  "price": "2992.9",
  "fee": "7.49",
  "feeCurrency": "EUR",
  "taker": true,
  "event": "fill",
  "timestamp": 1548692603142
}

Order:
{
  "selfTradePrevention": "decrementAndCancel",
  "orderType": "limit",
  "side": "buy",
  "amount": "1",
  "visible": true,
  "onHold": "7.11",
  "orderId": "5586b6b2-4a65-469e-935c-43c929da562e",
  "created": 1548692603124,
  "market": "BTC-EUR",
  "postOnly": false,
  "price": "3000",
  "amountRemaining": "0",
  "onHoldCurrency": "EUR",
  "event": "order",
  "updated": 1548692603124,
  "timeInForce": "GTC",
  "status": "filled"
}
```
</details>

#### Candles subscription
Sends an updated candle after each trade for the specified interval and market.
```java
ws.subscriptionCandles("BTC-EUR", "1h", new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject response) {
    System.out.println(response.toString(2));
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "market": "BTC-EUR",
  "candle": [[
    1548691200000,
    "2995.2",
    "2996.4",
    "2985.1",
    "2995.2",
    "2.42933705"
  ]],
  "interval": "1h",
  "event": "candle"
}
```
</details>

#### Trades subscription
Sends an update whenever a trade has happened on this market. For your own trades, please subscribe to account.
```java
ws.subscriptionTrades("BTC-EUR", new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject response) {
    System.out.println(response.toString(2));
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "market": "BTC-EUR",
  "amount": "1",
  "side": "buy",
  "price": "2985.1",
  "id": "7216acf7-9130-442a-bfcf-3d6ccba75891",
  "event": "trade",
  "timestamp": 1548692737974
}
```
</details>

#### Book subscription
Sends an update whenever the order book for this specific market has changed. A list of tuples ([price, amount]) are returned, where amount ‘0’ means that there are no more orders at this price. If you wish to maintain your own copy of the order book, consider using the next function.
```java
ws.subscriptionBookUpdate("BTC-EUR", new WebsocketClientEndpoint.MessageHandler() {
  public void handleMessage(JSONObject response) {
    System.out.println(response.toString(2));
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "market": "BTC-EUR",
  "bids": [[
    "2974.8",
    "1.44769424"
  ]],
  "asks": [],
  "event": "book",
  "nonce": 44941,
}
```
</details>

#### Book subscription with local copy
This is a combination of get book per market and the book subscription which maintains a local copy. On every update to the order book, the entire order book is returned to the callback, while the book subscription will only return updates to the book.
```java
ws.subscriptionBook("BTC-EUR", new WebsocketClientEndpoint.BookHandler() {
  public void handleBook(Map<String, Object> book) {
    List<List<String>> bids = (List<List<String>>)book.get("bids");
    List<List<String>> asks = (List<List<String>>)book.get("asks");
    String nonce = (String)book.get("nonce");
    for (int i = 0; i < bids.size(); i++) {
      System.out.println("Handle the book here, bid: " + bids.get(i).get(0) + " amount: " + bids.get(i).get(1));
    }
    for (int j = 0; j < asks.size(); j++) {
      System.out.println("Handle the book here, ask: " + asks.get(j).get(0) + " amount: " + asks.get(j).get(1));
    }
  }
});
```
<details>
 <summary>View Response</summary>

```java
{
  "bids": [
    [
      "3284.4",
      "0.00304622"
    ],
    [
      "3277.8",
      "0.00305236"
    ],
    [
      "3271.3",
      "0.00305842"
    ],
    [
      "3264.7",
      "0.00306461"
    ],
    [
      "3258.1",
      "0.00307081"
    ],
    ...
  ],
  "asks": [
    [
      "3308.2",
      "0.0091243"
    ],
    [
      "3310.9",
      "0.00304298"
    ],
    [
      "3317.5",
      "0.00304298"
    ],
    [
      "3324.1",
      "0.00304298"
    ],
    [
      "3330.7",
      "0.00304298"
    ],
    ...
  ],
  "nonce": 37424
}
```
</details>