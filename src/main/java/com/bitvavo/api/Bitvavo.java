package com.bitvavo.api;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Bitvavo {
  String apiKey = "";
  String apiSecret = "";
  String restUrl = "https://api.bitvavo.com/v2";
  String wsUrl = "wss://ws.bitvavo.com/v2/";
  boolean authenticated;
  boolean debugging = false;
  int window = 10000;
  WebsocketClientEndpoint ws;
  Websocket websocketObject;
  KeepAliveThread keepAliveThread;
  Map<String, Object> book;
  JSONObject optionsSubscriptionTicker;
  JSONObject optionsSubscriptionTicker24h;
  JSONObject optionsSubscriptionAccount;
  JSONObject optionsSubscriptionCandles;
  JSONObject optionsSubscriptionTrades;
  JSONObject optionsSubscriptionBookUpdate;
  JSONObject optionsSubscriptionBookFirst;
  JSONObject optionsSubscriptionBookSecond;
  volatile int rateLimitRemaining = 1000;
  volatile long rateLimitReset = 0;
  volatile boolean rateLimitThreadStarted = false;

  public Bitvavo(JSONObject options) {
    JSONArray keys = options.names();
    for (int i = 0; i < keys.length(); ++i) {
      String key = keys.getString(i);
      if(key.equalsIgnoreCase("apikey")) {
        this.apiKey = options.getString(key);
      } else if(key.equalsIgnoreCase("apisecret")) {
        this.apiSecret = options.getString(key);
      } else if(key.equalsIgnoreCase("accesswindow")) {
        this.window = options.getInt(key);
      } else if(key.equalsIgnoreCase("debugging")) {
        this.debugging = options.getBoolean(key);
      } else if(key.equalsIgnoreCase("resturl")) {
        this.restUrl = options.getString(key);
      } else if(key.equalsIgnoreCase("wsurl")) {
        this.wsUrl = options.getString(key);
      }
    }
  }

  public String getApiKey() {
    return this.apiKey;
  }

  public String getApiSecret() {
    return this.apiSecret;
  }

  private String createUrlParams(JSONObject options) {
    String params = options.keySet().stream().map(key -> key + "=" + options.get(key).toString())
            .collect(Collectors.joining("&"));
    if(options.length() > 0) {
      params = "?" + params;
    }
    return params;
  }

  public String createSignature(long timestamp, String method, String urlEndpoint, JSONObject body) {
    if (this.apiSecret == null || this.apiKey == null) {
      errorToConsole("The API key or secret has not been set. Please pass the key and secret when instantiating the bitvavo object.");
      return "";
    }
    try {
      String result = timestamp + method + "/v2" + urlEndpoint;
      if (body.length() != 0) {
        result = result + body;
      }
      Mac sha256HMAC = Mac.getInstance("HmacSHA256");
      SecretKeySpec secretKey = new SecretKeySpec(this.apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
      sha256HMAC.init(secretKey);
      return new String(Hex.encodeHex(sha256HMAC.doFinal(result.getBytes(StandardCharsets.UTF_8))));
    } catch (Exception ex) {
      errorToConsole("Caught exception in createSignature " + ex);
      return "";
    }
  }

  public void debugToConsole(String message) {
    if(this.debugging) {
      Calendar cal = Calendar.getInstance();
      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
      System.out.println(sdf.format(cal.getTime()) + " DEBUG: " + message);
    }
  }

  public void errorToConsole(String message) {
    Calendar cal = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
    System.out.println(sdf.format(cal.getTime()) + " ERROR: " + message);
  }

  public void errorRateLimit(JSONObject response) {
    if (response.getInt("errorCode") == 105) {
      rateLimitRemaining = 0;
      String message = response.getString("error");
      String placeHolder = message.split(" at ")[1].replace(".", "");
      rateLimitReset = Long.parseLong(placeHolder);
      if (!rateLimitThreadStarted) {
        new Thread(() -> {
          try {
            long timeToWait = rateLimitReset - System.currentTimeMillis();
            rateLimitThreadStarted = true;
            debugToConsole("We are waiting for " + ((int) timeToWait / 1000) + " seconds, until the rate limit ban will be lifted.");
            Thread.sleep(timeToWait);
          } catch (InterruptedException ie) {
            errorToConsole("Got interrupted while waiting for the rate limit ban to be lifted.");
          }
          rateLimitThreadStarted = false;
          if (System.currentTimeMillis() >= rateLimitReset) {
            debugToConsole("Rate limit ban has been lifted, resetting rate limit to 1000.");
            rateLimitRemaining = 1000;
          }
        }).start();
      }
    }
  }

  public void updateRateLimit(Map<String,List<String>> response) {
    String remainingHeader = response.get("Bitvavo-Ratelimit-Remaining").get(0);
    String resetHeader = response.get("Bitvavo-Ratelimit-ResetAt").get(0);
    if(remainingHeader != null) {
      rateLimitRemaining = Integer.parseInt(remainingHeader);
    }
    if(resetHeader != null) {
      rateLimitReset = Long.parseLong(resetHeader);
      if (!rateLimitThreadStarted) {
        new Thread(() -> {
          try {
            long timeToWait = rateLimitReset - System.currentTimeMillis();
            rateLimitThreadStarted = true;
            debugToConsole("We started a thread which waits for " + ((int) timeToWait / 1000) + " seconds, until the rate limit will be reset.");
            Thread.sleep(timeToWait);
          } catch (InterruptedException ie) {
            errorToConsole("Got interrupted while waiting for the rate limit to be reset.");
          }
          rateLimitThreadStarted = false;
          if (System.currentTimeMillis() >= rateLimitReset) {
            debugToConsole("Resetting rate limit to 1000.");
            rateLimitRemaining = 1000;
          }
        }).start();
      }
    }
  }

  public int getRemainingLimit() {
    return rateLimitRemaining;
  }

  private String executePrivateRequest(String urlEndpoint, String urlParams, String method, JSONObject body) throws IOException {
    long timestamp = System.currentTimeMillis();
    String signature = createSignature(timestamp, method, (urlEndpoint + urlParams), body);
    URL url = new URL(this.restUrl + urlEndpoint + urlParams);
    HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();

    httpsCon.setRequestMethod(method);
    httpsCon.setRequestProperty("Bitvavo-Access-Key", this.apiKey);
    httpsCon.setRequestProperty("Bitvavo-Access-Signature", signature);
    httpsCon.setRequestProperty("Bitvavo-Access-Timestamp", String.valueOf(timestamp));
    httpsCon.setRequestProperty("Bitvavo-Access-Window", String.valueOf(this.window));
    httpsCon.setRequestProperty("Content-Type", "application/json");
    if (body.length() != 0) {
      httpsCon.setDoOutput(true);
      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsCon.getOutputStream());
      outputStreamWriter.write(body.toString());
      outputStreamWriter.flush();
    }

    int responseCode = httpsCon.getResponseCode();

    InputStream inputStream;
    if (responseCode == 200) {
      inputStream = httpsCon.getInputStream();
      updateRateLimit(httpsCon.getHeaderFields());
    } else {
      inputStream = httpsCon.getErrorStream();
    }
    StringWriter writer = new StringWriter();
    IOUtils.copy(inputStream, writer, StandardCharsets.UTF_8);
    return writer.toString();
  }

  public JSONObject privateRequest(String urlEndpoint, String urlParams, String method, JSONObject body) {
    try {
      String result = executePrivateRequest(urlEndpoint, urlParams, method, body);

      JSONObject response = new JSONObject(result);
      if (result.contains("errorCode")) {
        errorRateLimit(response);
      }
      return response;
    } catch (Exception ex) {
      errorToConsole("Caught exception in privateRequest " + ex);
      return new JSONObject();
    }
  }

  public JSONArray privateRequestArray(String urlEndpoint, String urlParams, String method, JSONObject body) {
    try {
      String result = executePrivateRequest(urlEndpoint, urlParams, method, body);

      if (result.contains("errorCode")) {
        errorRateLimit(new JSONObject(result));
        errorToConsole(result);
        return new JSONArray();
      }

      return new JSONArray(result);
    } catch (Exception ex) {
      errorToConsole("Caught exception in privateRequest " + ex);
      return new JSONArray();
    }
  }

  private String executePublicRequest(String urlString, String method) throws IOException {
    URL url = new URL(urlString);
    HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection();
    httpsCon.setRequestMethod(method);
    if (this.apiKey.isEmpty()) {
      long timestamp = System.currentTimeMillis();
      String signature = createSignature(timestamp, method, urlString.replace(this.restUrl, ""), new JSONObject());
      httpsCon.setRequestProperty("Bitvavo-Access-Key", this.apiKey);
      httpsCon.setRequestProperty("Bitvavo-Access-Signature", signature);
      httpsCon.setRequestProperty("Bitvavo-Access-Timestamp", String.valueOf(timestamp));
      httpsCon.setRequestProperty("Bitvavo-Access-Window", String.valueOf(this.window));
      httpsCon.setRequestProperty("Content-Type", "application/json");
    }
    int responseCode = httpsCon.getResponseCode();
    InputStream inputStream;
    if (responseCode == 200) {
      inputStream = httpsCon.getInputStream();
      updateRateLimit(httpsCon.getHeaderFields());
    } else {
      inputStream = httpsCon.getErrorStream();
    }
    StringWriter writer = new StringWriter();
    IOUtils.copy(inputStream, writer, StandardCharsets.UTF_8);
    return writer.toString();
  }

  public JSONObject publicRequest(String urlString, String method) {
    try {
      String result = executePublicRequest(urlString, method);

      JSONObject response = new JSONObject(result);
      if (result.contains("errorCode")) {
        errorRateLimit(response);
      }
      return response;
    } catch (IOException ex) {
      errorToConsole("Caught IOerror, " + ex);
    }
    return new JSONObject("{}");
  }

  public JSONArray publicRequestArray(String urlString, String method) {
    try {
      String result = executePublicRequest(urlString, method);
      if (result.contains("error")) {
        errorRateLimit(new JSONObject(result));
        return new JSONArray("[" + result + "]");
      }
      debugToConsole("FULL RESPONSE: " + result);

      return new JSONArray(result);
    } catch (MalformedURLException ex) {
      errorToConsole("Caught Malformed Url error, " + ex);
    } catch (IOException ex) {
      errorToConsole("Caught IOerror, " + ex);
    }
    return new JSONArray("[{}]");
  }

  /**
   * Returns the current time in unix time format (milliseconds since 1 jan 1970)
   * @return JSONObject response, get time through response.getLong("time")
   */
  public JSONObject time() {
    return publicRequest(this.restUrl + "/time", "GET");
  }

  /**
   * Returns the available markets
   * @param options optional parameters: market
   * @return JSONArray response, get markets by iterating over array: response.get(index)
   */
  public JSONArray markets(JSONObject options) {
    String urlParams = createUrlParams(options);
    if(options.has("market")) {
      JSONArray returnArray = new JSONArray();
      returnArray.put(publicRequest(this.restUrl + "/markets" + urlParams, "GET"));
      return returnArray;
    } else {
      return publicRequestArray(this.restUrl + "/markets" + urlParams, "GET");
    }
  }

  /**
   * Returns the available assets
   * @param options optional parameters: symbol
   * @return JSONArray response, get assets by iterating over array response.get(index)
   */
  public JSONArray assets(JSONObject options) {
    String urlParams = createUrlParams(options);
    if(options.has("symbol")) {
      JSONArray returnArray = new JSONArray();
      returnArray.put(publicRequest(this.restUrl + "/assets" + urlParams, "GET"));
      return returnArray;
    } else {
      return publicRequestArray(this.restUrl + "/assets" + urlParams, "GET");
    }
  }

  /**
   * Returns the book for a certain market
   * @param market Specifies the market for which the book should be returned.
   * @param options optional parameters: depth
   * @return JSONObject response, get bids through response.getJSONArray("bids"), asks through response.getJSONArray("asks")
   */
  public JSONObject book(String market, JSONObject options) {
    String urlParams = createUrlParams(options);
    return publicRequest(this.restUrl + "/" + market + "/book" + urlParams, "GET");
  }

  /**
   * Returns the trades for a specific market
   * @param market Specifies the market for which trades should be returned
   * @param options optional parameters: limit, start, end, tradeIdFrom, tradeIdTo
   * @return JSONArray response, iterate over array to get individual trades response.getJSONObject(index)
   */
  public JSONArray publicTrades(String market, JSONObject options) {
    String urlParams = createUrlParams(options);
    return publicRequestArray(this.restUrl + "/" + market + "/trades" + urlParams, "GET");
  }

  /**
   *  Returns the candles for a specific market
   * @param market market for which the candles should be returned
   * @param interval interval on which the candles should be returned
   * @param options optional parameters: limit, start, end
   * @return JSONArray response, get individual candles through response.getJSONArray(index)
   */
  public JSONArray candles(String market, String interval, JSONObject options) {
    options.put("interval", interval);
    String urlParams = createUrlParams(options);
    return publicRequestArray(this.restUrl + "/" + market + "/candles" + urlParams, "GET");
  }

  /**
   * Returns the ticker price
   * @param options optional parameters: market
   * @return JSONArray response, get individual prices by iterating over array: response.getJSONObject(index)
   */
  public JSONArray tickerPrice(JSONObject options) {
    String urlParams = createUrlParams(options);
    if(options.has("market")) {
      JSONArray returnArray = new JSONArray();
      returnArray.put(publicRequest(this.restUrl + "/ticker/price" + urlParams, "GET"));
      return returnArray;
    } else {
      return publicRequestArray(this.restUrl + "/ticker/price" + urlParams, "GET");
    }
  }

  /**
   * Return the book ticker
   * @param options optional parameters: market
   * @return JSONArray response, get individual books by iterating over array: response.getJSONObject(index)
   */
  public JSONArray tickerBook(JSONObject options) {
    String urlParams = createUrlParams(options);
    if(options.has("market")) {
      JSONArray returnArray = new JSONArray();
      returnArray.put(publicRequest(this.restUrl + "/ticker/book" + urlParams, "GET"));
      return returnArray;
    } else {
      return publicRequestArray(this.restUrl + "/ticker/book" + urlParams, "GET");
    }
  }

  /**
   * Return the 24 hour ticker
   * @param options optional parameters: market
   * @return JSONArray response, get individual 24 hour prices by iterating over array: response.getJSONObject(index)
   */
  public JSONArray ticker24h(JSONObject options) {
    String urlParams = createUrlParams(options);
    if(options.has("market")) {
      JSONArray returnArray = new JSONArray();
      returnArray.put(publicRequest(this.restUrl + "/ticker/24h" + urlParams, "GET"));
      return returnArray;
    } else {
      return publicRequestArray(this.restUrl + "/ticker/24h" + urlParams, "GET");
    }
  }

  /**
   * Places an order on the exchange
   * @param market The market for which the order should be created
   * @param side is this a buy or sell order
   * @param orderType is this a limit or market order
   * @param body optional body parameters: limit:(amount, price, postOnly), market:(amount, amountQuote, disableMarketProtection)
   *                                       stopLoss/takeProfit:(amount, amountQuote, disableMarketProtection, triggerType, triggerReference, triggerAmount)
   *                                       stopLossLimit/takeProfitLimit:(amount, price, postOnly, triggerType, triggerReference, triggerAmount)
   *                                       all orderTypes: timeInForce, selfTradePrevention, responseRequired
   * @return JSONObject response, get status of the order through response.getString("status")
   */
  public JSONObject placeOrder(String market, String side, String orderType, JSONObject body) {
    body.put("market", market);
    body.put("side", side);
    body.put("orderType", orderType);
    return privateRequest("/order", "", "POST", body);
  }

  /**
   * Returns a specific order
   * @param market the market the order resides on
   * @param orderId the id of the order
   * @return JSONObject response, get status of the order through response.getString("status")
   */
  public JSONObject getOrder(String market, String orderId) {
    JSONObject options = new JSONObject();
    options.put("market", market);
    options.put("orderId", orderId);
    String urlParams = createUrlParams(options);
    return privateRequest("/order", urlParams, "GET", new JSONObject());
  }

  /**
   * Updates an order
   * @param market the market the order resides on
   * @param orderId the id of the order which should be updated
   * @param body optional body parameters: limit:(amount, amountRemaining, price, timeInForce, selfTradePrevention, postOnly)
   *                           untriggered stopLoss/takeProfit:(amount, amountQuote, disableMarketProtection, triggerType, triggerReference, triggerAmount)
   *                                       stopLossLimit/takeProfitLimit: (amount, price, postOnly, triggerType, triggerReference, triggerAmount)
   * @return JSONObject response, get status of the order through response.getString("status")
   */
  public JSONObject updateOrder(String market, String orderId, JSONObject body) {
    body.put("market", market);
    body.put("orderId", orderId);
    return privateRequest("/order", "", "PUT", body);
  }

  /**
   * Cancel an order
   * @param market the market the order resides on
   * @param orderId the id of the order which should be cancelled
   * @return JSONObject response, get the id of the order which was cancelled through response.getString("orderId")
   */
  public JSONObject cancelOrder(String market, String orderId) {
    JSONObject options = new JSONObject();
    options.put("market", market);
    options.put("orderId", orderId);
    String urlParams = createUrlParams(options);
    return privateRequest("/order", urlParams, "DELETE", new JSONObject());
  }

  /**
   * Returns multiple orders for a specific market
   * @param market the market for which orders should be returned
   * @param options optional parameters: limit, start, end, orderIdFrom, orderIdTo
   * @return JSONArray response, get individual orders by iterating over array: response.getJSONObject(index)
   */
  public JSONArray getOrders(String market, JSONObject options) {
    options.put("market", market);
    String urlParams = createUrlParams(options);
    return privateRequestArray("/orders", urlParams, "GET", new JSONObject());
  }

  /**
   * Cancel multiple orders at once, if no market is specified all orders will be canceled
   * @param options optional parameters: market
   * @return JSONArray response, get individual cancelled orderId's by iterating over array: response.getJSONObject(index).getString("orderId")
   */
  public JSONArray cancelOrders(JSONObject options) {
    String urlParams = createUrlParams(options);
    return privateRequestArray("/orders", urlParams, "DELETE", new JSONObject());
  }

  /**
   * Returns all open orders for an account
   * @param options optional parameters: market
   * @return JSONArray response, get individual orders by iterating over array: response.getJSONObject(index)
   */
  public JSONArray ordersOpen(JSONObject options) {
    String urlParams = createUrlParams(options);
    return privateRequestArray("/ordersOpen", urlParams, "GET", new JSONObject());
  }

  /**
   * Returns all trades for a specific market
   * @param market the market for which trades should be returned
   * @param options optional parameters: limit, start, end, tradeIdFrom, tradeIdTo
   * @return JSONArray trades, get individual trades by iterating over array: response.getJSONObject(index)
   */
  public JSONArray trades(String market, JSONObject options) {
    options.put("market", market);
    String urlParams = createUrlParams(options);
    return privateRequestArray("/trades", urlParams, "GET", new JSONObject());
  }

  /**
   * Return the fee tier for an account
   * @return JSONObject response, get taker fee through: response.getJSONObject("fees").getString("taker")
   */
  public JSONObject account() {
    return privateRequest("/account", "", "GET", new JSONObject());
  }

  /**
   * Returns the balance for an account
   * @param options optional parameters: symbol
   * @return JSONArray response, get individual balances by iterating over array: response.getJSONObject(index)
   */
  public JSONArray balance(JSONObject options) {
    String urlParams = createUrlParams(options);
    return privateRequestArray("/balance", urlParams, "GET", new JSONObject());
  }

  /**
   * Returns the deposit address which can be used to increase the account balance
   * @param symbol the crypto currency for which the address should be returned
   * @return JSONObject response, get address through response.getString("address")
   */
  public JSONObject depositAssets(String symbol) {
    JSONObject options = new JSONObject();
    options.put("symbol", symbol);
    String urlParams = createUrlParams(options);
    return privateRequest("/deposit", urlParams, "GET", new JSONObject());
  }

  /**
   * Creates a withdrawal to another address
   * @param symbol the crypto currency for which the withdrawal should be created
   * @param amount the amount which should be withdrawn
   * @param address The address to which the crypto should get sent
   * @param body optional parameters: paymentId, internal, addWithdrawalFee
   * @return JSONObject response, get success confirmation through response.getBoolean("success")
   */
  public JSONObject withdrawAssets(String symbol, String amount, String address, JSONObject body) {
    body.put("symbol", symbol);
    body.put("amount", amount);
    body.put("address", address);
    return privateRequest("/withdrawal", "", "POST", body);
  }

  /**
   * Returns the entire deposit history for an account
   * @param options optional parameters: symbol, limit, start, end
   * @return JSONArray response, get individual deposits by iterating over the array: response.getJSONObject(index)
   */
  public JSONArray depositHistory(JSONObject options) {
    String urlParams = createUrlParams(options);
    return privateRequestArray("/depositHistory", urlParams, "GET", new JSONObject());
  }

  /**
   * Returns the entire withdrawal history for an account
   * @param options optional parameters: symbol, limit, start, end
   * @return JSONArray response, get individual withdrawals by iterating over the array: response.getJSONObject(index)
   */
  public JSONArray withdrawalHistory(JSONObject options) {
    String urlParams = createUrlParams(options);
    return privateRequestArray("/withdrawalHistory", urlParams, "GET", new JSONObject());
  }

  /**
   * Creates a websocket object
   * @return Websocket the object on which all websocket function can be called.
   */
  public Websocket newWebsocket() {
    return new Websocket();
  }

  public class Websocket {
    public Websocket() {
      try {
        final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI(Bitvavo.this.wsUrl), Bitvavo.this);
        clientEndPoint.addAuthenticateHandler(response -> {
          if (response.has("authenticated")) {
            authenticated = true;
            debugToConsole("We registered authenticated as true");
          }
        });
        clientEndPoint.addMessageHandler(response -> errorToConsole("Unexpected message: " + response));
        ws = clientEndPoint;
        book = new HashMap<>();
        Bitvavo.this.keepAliveThread = new KeepAliveThread();
        keepAliveThread.start();
      } catch (Exception ex) {
        errorToConsole("Caught exception in websocket: " + ex);
      }
    }

    void handleBook(Runnable function) {
      function.run();
    }

    public void close() {
      ws.closeSocket();
    }

    public void doSendPublic(JSONObject options) {
      ws.sendMessage(options.toString());
    }

    public void doSendPrivate(JSONObject options) {
      if (getApiKey() == null) {
        errorToConsole("You forgot to set the key and secret, both are required for this functionality.");
        return;
      }
      if (authenticated) {
        ws.sendMessage(options.toString());
      } else {
        try {
          TimeUnit.MILLISECONDS.sleep(50);
          doSendPrivate(options);
        } catch (InterruptedException ex) {
          errorToConsole("Interrupted, aborting send.");
        }
      }
    }

    /**
     * Sets the callback for errors
     * @param msgHandler callback
     */
    public void setErrorCallback(WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addErrorHandler(msgHandler);
    }

    /**
     * Returns the current time in unix timestamp (milliseconds since 1 jan 1970).
     * @param msgHandler callback
     * @return JSONObject response, get time through response.getJSONObject("response").getLong("time")
     */
    public void time(WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addTimeHandler(msgHandler);
      doSendPublic(new JSONObject("{ action: getTime }"));
    }

    /**
     * Returns available markets.
     *
     * @param options optional parameters: market
     * @param msgHandler callback
     * @return JSONObject response, get markets through response.getJSONArray("response") and iterate over array to get objects array.getJSONObject(index)
     */
    public void markets(JSONObject options, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addMarketsHandler(msgHandler);
      options.put("action", "getMarkets");
      doSendPublic(options);
    }

    /**
     * Returns available assets.
     *
     * @param options optional parameters: symbol
     * @param msgHandler callback
     * @return JSONObject response, get assets through response.getJSONArray("response") and iterate over array to get objects array.getJSONObject(index)
     */
    public void assets(JSONObject options, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addAssetsHandler(msgHandler);
      options.put("action", "getAssets");
      doSendPublic(options);
    }

    /**
     * Returns the book per market.
     *
     * @param market market for which the book should be returned.
     * @param options optional parameters: depth
     * @param msgHandler callback
     * @return JSONObject response, get book through response.getJSONObject("response") and get individual values through object.getJSONArray("bids"/"asks").get(index).get(index)
     */
    public void book(String market, JSONObject options, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addBookHandler(msgHandler);
      options.put("action", "getBook");
      options.put("market", market);
      doSendPublic(options);
    }

    /**
     * Returns the trades per market.
     *
     * @param market market for which the trades should be returned.
     * @param options optional parameters: limit, start, end, tradeIdFrom, tradeIdTo
     * @param msgHandler callback
     * @return JSONObject response, get trades through response.getJSONArray("response") and iterate over array to get objects array.getJSONObject(index)
     */
    public void publicTrades(String market, JSONObject options, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addTradesHandler(msgHandler);
      options.put("action", "getTrades");
      options.put("market", market);
      doSendPublic(options);
    }

    /**
     * Returns the candles per market.
     *
     * @param market market for which the candles should be returned.
     * @param interval interval for which the candles should be returned.
     * @param options optional parameters: limit, start, end
     * @param msgHandler callback
     * @return JSONObject response, get candles through response.getJSONArray("response") and iterate over array to get arrays containing timestamp, open, high, low, close and volume: array.getJSONArray(index) (i.e. for the open price array.getJSONArray(index).get(1))
     */
    public void candles(String market, String interval, JSONObject options, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addCandlesHandler(msgHandler);
      options.put("action", "getCandles");
      options.put("market", market);
      options.put("interval", interval);
      doSendPublic(options);
    }

    /**
     * Returns the 24 hour ticker.
     *
     * @param options optional parameters: market
     * @param msgHandler callback
     * @return JSONObject response, get array through response.getJSONArray("response") and iterate over array to get 24h ticker objects array.getJSONObject(index)
     */
    public void ticker24h(JSONObject options, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addTicker24hHandler(msgHandler);
      options.put("action", "getTicker24h");
      doSendPublic(options);
    }

    /**
     * Returns the price ticker.
     *
     * @param options optional parameters: market
     * @param msgHandler callback
     * @return JSONObject response, get array through response.getJSONArray("response") and iterate over array to get price ticker objects array.getJSONObject(index)
     */
    public void tickerPrice(JSONObject options, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addTickerPriceHandler(msgHandler);
      options.put("action", "getTickerPrice");
      doSendPublic(options);
    }

    /**
     * Returns the price ticker.
     *
     * @param options optional parameters: market
     * @param msgHandler callback
     * @return JSONObject response, get array through response.getJSONArray("response") and iterate over array to get book ticker objects array.getJSONObject(index)
     */
    public void tickerBook(JSONObject options, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addTickerBookHandler(msgHandler);
      options.put("action", "getTickerBook");
      doSendPublic(options);
    }

    /**
     * Places an order.
     *
     * @param market market on which the order should be created
     * @param side is this a sell or buy order
     * @param orderType is this a limit or market order
     * @param body optional body parameters: limit:(amount, price, postOnly), market:(amount, amountQuote, disableMarketProtection)
     *                                       stopLoss/takeProfit:(amount, amountQuote, disableMarketProtection, triggerType, triggerReference, triggerAmount)
     *                                       stopLossLimit/takeProfitLimit:(amount, price, postOnly, triggerType, triggerReference, triggerAmount)
     *                                       all orderTypes: timeInForce, selfTradePrevention, responseRequired
     * @param msgHandler callback
     * @return JSONObject response, get order object through response.getJSONObject("response")
     */
    public void placeOrder(String market, String side, String orderType, JSONObject body, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addPlaceOrderHandler(msgHandler);
      body.put("market", market);
      body.put("side", side);
      body.put("orderType", orderType);
      body.put("action", "privateCreateOrder");
      doSendPrivate(body);
    }

    /**
     * Returns an order.
     *
     * @param market market on which the order should be returned
     * @param orderId the order which should be returned
     * @param msgHandler callback
     * @return JSONObject response, get order object through response.getJSONObject("response")
     */
    public void getOrder(String market, String orderId, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addGetOrderHandler(msgHandler);
      JSONObject options = new JSONObject();
      options.put("action", "privateGetOrder");
      options.put("market", market);
      options.put("orderId", orderId);
      doSendPrivate(options);
    }

    /**
     * Updates an order.
     *
     * @param market market on which the order should be updated
     * @param orderId the order which should be updated
     * @param body optional body parameters: limit:(amount, amountRemaining, price, timeInForce, selfTradePrevention, postOnly)
     *                           untriggered stopLoss/takeProfit:(amount, amountQuote, disableMarketProtection, triggerType, triggerReference, triggerAmount)
     *                                       stopLossLimit/takeProfitLimit: (amount, price, postOnly, triggerType, triggerReference, triggerAmount)
     * @param msgHandler callback
     * @return JSONObject response, get order object through response.getJSONObject("response")
     */
    public void updateOrder(String market, String orderId, JSONObject body, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addUpdateOrderHandler(msgHandler);
      body.put("market", market);
      body.put("orderId", orderId);
      body.put("action", "privateUpdateOrder");
      doSendPrivate(body);
    }

    /**
     * Cancels an order.
     *
     * @param market market on which the order should be cancelled
     * @param orderId the order which should be cancelled
     * @param msgHandler callback
     * @return JSONObject response, get orderId through response.getJSONObject("response").getString("orderId")
     */
    public void cancelOrder(String market, String orderId, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addCancelOrderHandler(msgHandler);
      JSONObject options = new JSONObject();
      options.put("action", "privateCancelOrder");
      options.put("market", market);
      options.put("orderId", orderId);
      doSendPrivate(options);
    }

    /**
     * Returns multiple orders at once
     *
     * @param market market on which the orders should be returned
     * @param options optional parameters: limit, start, end, orderIdFrom, orderIdTo
     * @param msgHandler callback
     * @return JSONObject response, get array through response.getJSONArray("response") and iterate over the array to get order objects array.getJSONObject(index)
     */
    public void getOrders(String market, JSONObject options, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addGetOrdersHandler(msgHandler);
      options.put("action", "privateGetOrders");
      options.put("market", market);
      doSendPrivate(options);
    }

    /**
     * Cancels multiple orders at once
     *
     * @param options optional parameters: market
     * @param msgHandler callback
     * @return JSONObject response, get array through response.getJSONArray("response") and iterate over the array to get orderId's objects array.getJSONObject(index).getString("orderId")
     */
    public void cancelOrders(JSONObject options, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addCancelOrdersHandler(msgHandler);
      options.put("action", "privateCancelOrders");
      doSendPrivate(options);
    }

    /**
     * Get all open orders at once
     *
     * @param options optional parameters: market
     * @param msgHandler callback
     * @return JSONObject response, get array through response.getJSONArray("response") and iterate over the array to get open orders objects array.getJSONObject(index)
     */
    public void ordersOpen(JSONObject options, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addGetOrdersOpenHandler(msgHandler);
      options.put("action", "privateGetOrdersOpen");
      doSendPrivate(options);
    }

    /**
     * Returns all trades within a market
     *
     * @param market for which market should the trades be returned
     * @param options optional parameters: limit, start, end, tradeIdFrom, tradeIdTo
     * @param msgHandler callback
     * @return JSONObject response, get array through response.getJSONArray("response") and iterate over the array to get trades objects array.getJSONObject(index)
     */
    public void trades(String market, JSONObject options, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addGetTradesHandler(msgHandler);
      options.put("action", "privateGetTrades");
      options.put("market", market);
      doSendPrivate(options);
    }

    /**
     * Returns the fee tier for an account
     *
     * @param msgHandler callback
     * @return JSONObject response, get taker fee through response.getJSONObject("response").getJSONObject("fees").getString("taker")
     */
    public void account(WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addAccountHandler(msgHandler);
      JSONObject options = new JSONObject("{ action: privateGetAccount }");
      doSendPrivate(options);
    }

    /**
     * Returns the balance for an account
     *
     * @param options optional parameters: symbol
     * @param msgHandler callback
     * @return JSONObject response, get array through response.getJSONArray("response") and iterate over the array to get balance objects array.getJSONObject(index)
     */
    public void balance(JSONObject options, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addBalanceHandler(msgHandler);
      options.put("action", "privateGetBalance");
      doSendPrivate(options);
    }

    /**
     * Returns the deposit address which can be used to increase the account balance
     *
     * @param symbol symbol specifying the crypto for which the deposit address should be returned
     * @param msgHandler callback
     * @return JSONObject response, get address through response.getJSONObject("response").getString("address")
     */
    public void depositAssets(String symbol, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addDepositAssetsHandler(msgHandler);
      JSONObject options = new JSONObject("{ action: privateDepositAssets }");
      options.put("symbol", symbol);
      doSendPrivate(options);
    }

    /**
     * Creates a withdrawal to another address
     *
     * @param symbol symbol specifying the crypto for which the withdrawal should be created
     * @param amount string specifying the amount which should be withdrawn
     * @param address string specifying the address to which the crypto should be sent
     * @param body optional parameters: paymentId, internal, addWithdrawalFee
     * @param msgHandler callback
     * @return JSONObject response, get success confirmation through response.getJSONObject("response").getBoolean("success")
     */
    public void withdrawAssets(String symbol, String amount, String address, JSONObject body, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addWithdrawAssetsHandler(msgHandler);
      body.put("action", "privateWithdrawAssets");
      body.put("symbol", symbol);
      body.put("amount", amount);
      body.put("address", address);
      doSendPrivate(body);
    }

    /**
     * Returns the entire deposit history for an account
     *
     * @param options optional parameters: symbol, limit, start, end
     * @param msgHandler callback
     * @return JSONObject response, get array through response.getJSONArray("response") and iterate over the array to get deposit objects array.getJSONObject(index)
     */
    public void depositHistory(JSONObject options, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addDepositHistoryHandler(msgHandler);
      options.put("action", "privateGetDepositHistory");
      doSendPrivate(options);
    }

    /**
     * Returns the entire withdrawal history for an account
     *
     * @param options optional parameters: symbol, limit, start, end
     * @param msgHandler callback
     * @return JSONObject response, get array through response.getJSONArray("response") and iterate over the array to get withdrawal objects array.getJSONObject(index)
     */
    public void withdrawalHistory(JSONObject options, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addWithdrawalHistoryHandler(msgHandler);
      options.put("action", "privateGetWithdrawalHistory");
      doSendPrivate(options);
    }

    /**
     * Pushes an update every time the ticker for a market is updated
     *
     * @param market market for which tickers should be returned
     * @param msgHandler callback, will be overwritten when subscriptionTicker() is called with the same market parameter.
     * @return JSONObject response, get bestBid through response.getString("bestBid") and bestAsk through response.getString("bestAsk")
     */
    public void subscriptionTicker(String market, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addSubscriptionTickerHandler(market, msgHandler);
      JSONObject options = new JSONObject();
      JSONObject subOptions = new JSONObject();
      subOptions.put("name", "ticker");
      subOptions.put("markets", new String[] {market});
      options.put("action", "subscribe");
      options.put("channels", new JSONObject[] {subOptions});
      if(optionsSubscriptionTicker == null) {
        optionsSubscriptionTicker = new JSONObject();
      }
      optionsSubscriptionTicker.put(market, options);
      doSendPublic(options);
    }

    /**
     * Pushes an update every time the 24 hour ticker for a market is updated
     *
     * @param market market for which tickers should be returned
     * @param msgHandler callback, will be overwritten when subscriptionTicker() is called with the same market parameter.
     * @return JSONObject ticker, get individual fields through ticker.getString(<key>)
     */
    public void subscriptionTicker24h(String market, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addSubscriptionTicker24hHandler(market, msgHandler);
      JSONObject options = new JSONObject();
      JSONObject subOptions = new JSONObject();
      subOptions.put("name", "ticker24h");
      subOptions.put("markets", new String[] {market});
      options.put("action", "subscribe");
      options.put("channels", new JSONObject[] {subOptions});
      if(optionsSubscriptionTicker24h == null) {
        optionsSubscriptionTicker24h = new JSONObject();
      }
      optionsSubscriptionTicker24h.put(market, options);
      doSendPublic(options);
    }

    /**
     * Pushes an update every time an order is placed, order is canceled or trade is made for an account
     *
     * @param msgHandler callback, will be overwritten on multiple calls to subscriptionAccount
     * @return JSONObject response, get type of event through response.getString("event")
     */
    public void subscriptionAccount(String market, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addSubscriptionAccountHandler(market, msgHandler);
      JSONObject options = new JSONObject();
      JSONObject subOptions = new JSONObject();
      subOptions.put("name", "account");
      subOptions.put("markets", new String[] {market});
      options.put("action", "subscribe");
      options.put("channels", new JSONObject[] {subOptions});
      if(optionsSubscriptionAccount == null) {
        optionsSubscriptionAccount = new JSONObject();
      }
      optionsSubscriptionAccount.put(market, options);
      doSendPrivate(options);
    }

    /**
     * Pushes an update every time an candle is formed for the specified market and interval
     *
     * @param market market for which candles should be pushed.
     * @param interval interval for which the candles should be pushed
     * @param msgHandler callback, will be overwritten on multiple calls to subscriptionCandles() with the same market and interval
     * @return JSONObject response, get candle array (containing a single candle with open, high, low, close and volume) through response.getJSONObject("response").getJSONArray("candle")
     */
    public void subscriptionCandles(String market, String interval, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addSubscriptionCandlesHandler(market, interval, msgHandler);
      JSONObject options = new JSONObject();
      JSONObject subOptions = new JSONObject();
      subOptions.put("name", "candles");
      subOptions.put("interval", new String[] {interval});
      subOptions.put("markets", new String[] {market});
      options.put("action", "subscribe");
      options.put("channels", new JSONObject[] {subOptions});
      JSONObject intervalIndex = new JSONObject();
      intervalIndex.put(interval, options);
      if(optionsSubscriptionCandles == null) {
        optionsSubscriptionCandles = new JSONObject();
      }
      optionsSubscriptionCandles.put(market, intervalIndex);
      doSendPublic(options);
    }

    /**
     * Pushes an update every time a trade is made within a market.
     *
     * @param market market for which trades should be pushed.
     * @param msgHandler callback, will be overwritten on multiple calls to subscriptionTrades() with the same market
     * @return JSONObject response, get trade object through response.getJSONObject("response")
     */
    public void subscriptionTrades(String market, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addSubscriptionTradesHandler(market, msgHandler);
      JSONObject options = new JSONObject();
      JSONObject subOptions = new JSONObject();
      subOptions.put("name", "trades");
      subOptions.put("markets", new String[] {market});
      options.put("action", "subscribe");
      options.put("channels", new JSONObject[] {subOptions});
      if(optionsSubscriptionTrades == null) {
        optionsSubscriptionTrades = new JSONObject();
      }
      optionsSubscriptionTrades.put(market, options);
      doSendPublic(options);
    }

    /**
     * Pushes an update every time the book for a market is changed.
     *
     * @param market market for which book updates should be pushed.
     * @param msgHandler callback, will be overwritten on multiple calls to subscriptionBook() with the same market
     * @return JSONObject response, the object only contains updates and not the entire book, get the updates through response.getJSONObject("response").getJSONArray("bids"/"asks")
     */
    public void subscriptionBookUpdate(String market, WebsocketClientEndpoint.MessageHandler msgHandler) {
      ws.addSubscriptionBookUpdateHandler(market, msgHandler);
      JSONObject options = new JSONObject();
      JSONObject subOptions = new JSONObject();
      subOptions.put("name", "book");
      subOptions.put("markets", new String[] {market});
      options.put("action", "subscribe");
      options.put("channels", new JSONObject[] {subOptions});
      if(optionsSubscriptionBookUpdate == null) {
        optionsSubscriptionBookUpdate = new JSONObject();
      }
      optionsSubscriptionBookUpdate.put(market, options);
      doSendPublic(options);
    }

    /**
     * Pushes the entire book every time it is changed.
     *
     * @param market market for which book updates should be pushed.
     * @param msgHandler callback, will be overwritten on multiple calls to subscriptionBook() with the same market
     * @return Map<String, Object> book, this object has been converted to a Map such that List<List<String>> bids = (List<List<String>>)book.get("bids"); The entire book is contained in every callback
     */
    public void subscriptionBook(String market, WebsocketClientEndpoint.BookHandler msgHandler) {
      ws.keepBookCopy = true;
      Map<String, Object> bidsAsks = new HashMap<>();
      bidsAsks.put("bids", new ArrayList<ArrayList<Float>>());
      bidsAsks.put("asks", new ArrayList<ArrayList<Float>>());

      book.put(market, bidsAsks);
      ws.addSubscriptionBookHandler(market, msgHandler);
      JSONObject options = new JSONObject();
      options.put("action", "getBook");
      options.put("market", market);
      if(optionsSubscriptionBookFirst == null) {
        optionsSubscriptionBookFirst = new JSONObject();
      }
      optionsSubscriptionBookFirst.put(market, options);
      doSendPublic(options);

      JSONObject secondOptions = new JSONObject();
      JSONObject subOptions = new JSONObject();
      subOptions.put("name", "book");
      subOptions.put("markets", new String[] {market});
      secondOptions.put("action", "subscribe");
      secondOptions.put("channels", new JSONObject[] {subOptions});
      if(optionsSubscriptionBookSecond == null) {
        optionsSubscriptionBookSecond = new JSONObject();
      }
      optionsSubscriptionBookSecond.put(market, secondOptions);
      doSendPublic(secondOptions);
    }
  }
}
