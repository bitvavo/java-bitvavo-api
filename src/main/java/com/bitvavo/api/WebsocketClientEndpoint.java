package com.bitvavo.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ClientEndpoint
public class WebsocketClientEndpoint {
    int reconnectTimer;
    Bitvavo bitvavo;
    Session userSession = null;
    boolean restartWebsocket = true;
    boolean keepBookCopy;
    Map<String, Map<String, MessageHandler>> subscriptionCandlesHandlerMap;
    Map<String, MessageHandler> subscriptionAccountHandlerMap;
    Map<String, MessageHandler> subscriptionTickerHandlerMap;
    Map<String, MessageHandler> subscriptionTicker24hHandlerMap;
    Map<String, MessageHandler> subscriptionTradesHandlerMap;
    Map<String, MessageHandler> subscriptionBookUpdateHandlerMap;
    Map<String, BookHandler> subscriptionBookHandlerMap;
    private MessageHandler messageHandler;
    private MessageHandler timeHandler;
    private MessageHandler marketsHandler;
    private MessageHandler assetsHandler;
    private MessageHandler bookHandler;
    private MessageHandler tradesHandler;
    private MessageHandler candlesHandler;
    private MessageHandler ticker24hHandler;
    private MessageHandler tickerPriceHandler;
    private MessageHandler tickerBookHandler;
    private MessageHandler createOrderHandler;
    private MessageHandler getOrderHandler;
    private MessageHandler updateOrderHandler;
    private MessageHandler cancelOrderHandler;
    private MessageHandler cancelOrdersHandler;
    private MessageHandler getOrdersHandler;
    private MessageHandler getOrdersOpenHandler;
    private MessageHandler getTradesHandler;
    private MessageHandler getAccountHandler;
    private MessageHandler balanceHandler;
    private MessageHandler depositAssetsHandler;
    private MessageHandler withdrawAssetsHandler;
    private MessageHandler depositHistoryHandler;
    private MessageHandler withdrawalHistoryHandler;
    private MessageHandler authenticateHandler;
    private MessageHandler errorHandler;

    public WebsocketClientEndpoint(URI endpointURI, Bitvavo bitv) {
        try {
            bitvavo = bitv;
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (Exception e) {
            bitvavo.errorToConsole("Caught exception in instantiating websocket." + e);
            this.reconnectTimer = 100;
            retryConnecting(endpointURI);
        }
    }

    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> returnMap = new HashMap<>();

        if (json != JSONObject.NULL) {
            returnMap = toMap(json);
        }
        return returnMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    public void retryConnecting(URI endpointURI) {
        bitvavo.debugToConsole("Trying to reconnect.");
        try {
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            container.connectToServer(this, endpointURI);
        } catch (javax.websocket.DeploymentException e) {
            try {
                TimeUnit.MILLISECONDS.sleep(this.reconnectTimer);
                this.reconnectTimer = this.reconnectTimer * 2;
                bitvavo.debugToConsole("We waited for " + this.reconnectTimer / 1000.0 + " seconds");
                retryConnecting(endpointURI);
            } catch (InterruptedException ex) {}
        } catch (Exception exception) {
            bitvavo.errorToConsole("unexpected exception caught");
            throw new RuntimeException(exception);
        }
    }

    public void closeSocket() {
        try {
            if (this.userSession != null) {
                this.restartWebsocket = false;
                this.userSession.close();
                System.out.println(bitvavo.keepAliveThread);
                bitvavo.keepAliveThread.interrupt();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnOpen
    public void onOpen(Session userSession) {
        bitvavo.debugToConsole("opening websocket");
        this.userSession = userSession;
        try {
            TimeUnit.MILLISECONDS.sleep(500);
        } catch (InterruptedException ex) {
            bitvavo.errorToConsole(ex.toString());
        }
        if (!bitvavo.getApiKey().isEmpty()) {
            long timestamp = System.currentTimeMillis();
            JSONObject authenticate = new JSONObject();
            authenticate.put("action", "authenticate");
            authenticate.put("key", bitvavo.getApiKey());
            authenticate.put("signature", bitvavo.createSignature(timestamp, "GET", "/websocket", new JSONObject()));
            authenticate.put("timestamp", timestamp);
            authenticate.put("window", Integer.toString(bitvavo.window));
            this.sendMessage(authenticate.toString());
        }
        if (bitvavo.optionsSubscriptionTicker != null) {
            bitvavo.optionsSubscriptionTicker.keySet()
                    .forEach(market -> this.sendMessage(bitvavo.optionsSubscriptionTicker.get(market).toString()));
        }
        if (bitvavo.optionsSubscriptionTicker24h != null) {
            bitvavo.optionsSubscriptionTicker.keySet()
                    .forEach(market -> this.sendMessage(bitvavo.optionsSubscriptionTicker24h.get(market).toString()));
        }
        // Account uses a threaded function, since we need a response on authenticate before we can send.
        if (bitvavo.optionsSubscriptionAccount != null) {
            WebsocketSendThread websocketSendThread = new WebsocketSendThread(bitvavo.optionsSubscriptionAccount, bitvavo, this);
            websocketSendThread.start();
        }
        if (bitvavo.optionsSubscriptionCandles != null) {
            bitvavo.optionsSubscriptionCandles.keySet()
                    .forEach(market -> {
                        JSONObject intervalObject = bitvavo.optionsSubscriptionCandles.getJSONObject(market);
                        for (String interval : intervalObject.keySet()) {
                            this.sendMessage(bitvavo.optionsSubscriptionCandles.getJSONObject(market).get(interval).toString());
                        }
                    });
        }
        if (bitvavo.optionsSubscriptionTrades != null) {
            bitvavo.optionsSubscriptionTrades.keySet()
                    .forEach(market -> this.sendMessage(bitvavo.optionsSubscriptionTrades.get(market).toString()));
        }
        if (bitvavo.optionsSubscriptionBookUpdate != null) {
            bitvavo.optionsSubscriptionBookUpdate.keySet()
                    .forEach(market -> this.sendMessage(bitvavo.optionsSubscriptionBookUpdate.get(market).toString()));
        }
        if (bitvavo.optionsSubscriptionBookFirst != null) {
            for (String market : bitvavo.optionsSubscriptionBookFirst.keySet()) {
                this.sendMessage(bitvavo.optionsSubscriptionBookFirst.get(market).toString());
                this.sendMessage(bitvavo.optionsSubscriptionBookSecond.get(market).toString());
            }
        }
        bitvavo.debugToConsole("We completed onOpen");
    }

    @OnError
    public void onError(Session userSession, Throwable error) {
        bitvavo.debugToConsole("We encountered an error: " + error);
        error.printStackTrace();

    }

    private void copyHandlers(WebsocketClientEndpoint oldCE, WebsocketClientEndpoint newCE) {
        if (oldCE.keepBookCopy) {
            newCE.keepBookCopy = true;
        }
        newCE.copySubscriptionTickerHandler(oldCE.subscriptionTickerHandlerMap);
        newCE.copySubscriptionTicker24hHandler(oldCE.subscriptionTicker24hHandlerMap);
        newCE.copySubscriptionAccountHandler(oldCE.subscriptionAccountHandlerMap);
        newCE.copySubscriptionBookUpdateHandler(oldCE.subscriptionBookUpdateHandlerMap);
        newCE.copySubscriptionCandlesHandler(oldCE.subscriptionCandlesHandlerMap);
        newCE.copySubscriptionTradesHandler(oldCE.subscriptionTradesHandlerMap);
        newCE.copySubscriptionBookHandler(oldCE.subscriptionBookHandlerMap);
    }

    @OnClose
    public void onClose(Session userSession, CloseReason reason) {
        bitvavo.debugToConsole("closing websocket " + reason);
        this.userSession = null;
        if (bitvavo.getRemainingLimit() > 0 && this.restartWebsocket) {
            try {
                bitvavo.authenticated = false;
                WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI(bitvavo.wsUrl), bitvavo);
                copyHandlers(bitvavo.ws, clientEndPoint);
                clientEndPoint.addAuthenticateHandler(response -> {
                    if (response.has("authenticated")) {
                        bitvavo.authenticated = true;
                        bitvavo.debugToConsole("We registered authenticated as true again");
                    }
                });
                clientEndPoint.addMessageHandler(response -> bitvavo.errorToConsole("Unexpected message: " + response));
                bitvavo.ws = clientEndPoint;
            } catch (Exception ex) {
                bitvavo.errorToConsole("We caught exception in reconnecting!" + ex);
            }
        } else {
            if (this.restartWebsocket) {
                bitvavo.debugToConsole("The websocket has been closed because your rate limit was reached, please wait till the ban is lifted and try again.");
            } else {
                bitvavo.debugToConsole("The websocket has been closed by the user.");
            }
        }
    }

    private List<List<String>> sortAndInsert(List<List<String>> update, List<List<String>> book, boolean asksCompare) {
        for (int i = 0; i < update.size(); i++) {
            boolean updateSet = false;
            List<String> updateEntry = update.get(i);
            for (int j = 0; j < book.size(); j++) {
                List<String> bookItem = book.get(j);
                if (asksCompare) {
                    if (Float.parseFloat(updateEntry.get(0)) < Float.parseFloat(bookItem.get(0))) {
                        book.add(j, updateEntry);
                        updateSet = true;
                        break;
                    }
                } else {
                    if (Float.parseFloat(updateEntry.get(0)) > Float.parseFloat(bookItem.get(0))) {
                        book.add(j, updateEntry);
                        updateSet = true;
                        break;
                    }
                }
                if (Float.parseFloat(bookItem.get(0)) == Float.parseFloat(updateEntry.get(0))) {
                    if (Float.parseFloat(updateEntry.get(1)) > 0.0) {
                        book.set(j, updateEntry);
                        updateSet = true;
                        break;
                    } else {
                        book.remove(j);
                        updateSet = true;
                        break;
                    }
                }
            }
            if (!updateSet) {
                book.add(updateEntry);
            }
        }
        return book;
    }

    @SuppressWarnings("unchecked")
    @OnMessage
    public void onMessage(String message) {
        String market;
        JSONObject response = new JSONObject(message);
        bitvavo.debugToConsole("FULLRESPONSE: " + response);
        if (response.has("error")) {
            bitvavo.errorRateLimit(response);
            if (this.errorHandler != null) {
                this.errorHandler.handleMessage(response);
                return;
            }
        }
        if (response.has("event")) {
            switch (response.getString("event")) {
                case "subscribed":
                    JSONObject channel = response.getJSONObject("subscriptions");
                    String channels = String.join(", ", channel.keySet());
                    bitvavo.debugToConsole("We are now subscribed to the following channels: " + channels);
                    break;
                case "authenticate":
                    if (this.authenticateHandler != null)
                        this.authenticateHandler.handleMessage(response);
                    break;
                case "trade":
                    market = response.getString("market");
                    if (this.subscriptionTradesHandlerMap != null && this.subscriptionTradesHandlerMap.get(market) != null)
                        this.subscriptionTradesHandlerMap.get(market).handleMessage(response);
                    break;
                case "fill":
                case "order":
                    market = response.getString("market");
                    if (this.subscriptionAccountHandlerMap != null && this.subscriptionAccountHandlerMap.get(market) != null)
                        this.subscriptionAccountHandlerMap.get(market).handleMessage(response);
                    break;
                case "ticker":
                    market = response.getString("market");
                    if (this.subscriptionTickerHandlerMap != null && this.subscriptionTickerHandlerMap.get(market) != null)
                        this.subscriptionTickerHandlerMap.get(market).handleMessage(response);
                    break;
                case "ticker24h":
                    JSONArray data = response.getJSONArray("data");
                    if (this.subscriptionTicker24hHandlerMap != null) {
                        for (JSONObject ticker : IntStream.of(data.length())
                                .mapToObj(data::getJSONObject).collect(Collectors.toList())) {
                            market = ticker.getString("market");
                            if (this.subscriptionTicker24hHandlerMap.get(market) != null)
                                this.subscriptionTicker24hHandlerMap.get(market).handleMessage(ticker);
                        }
                    }
                    break;
                case "candle":
                    market = response.getString("market");
                    String interval = response.getString("interval");
                    if (this.subscriptionCandlesHandlerMap.get(market) != null
                            && this.subscriptionCandlesHandlerMap.get(market).get(interval) != null)
                        this.subscriptionCandlesHandlerMap.get(market).get(interval).handleMessage(response);
                    break;
                case "book":
                    market = response.getString("market");
                    if (this.subscriptionBookUpdateHandlerMap != null && this.subscriptionBookUpdateHandlerMap.get(market) != null) {
                        this.subscriptionBookUpdateHandlerMap.get(market).handleMessage(response);
                    }
                    if (keepBookCopy && this.subscriptionBookHandlerMap != null
                            && this.subscriptionBookHandlerMap.get(market) != null) {
                        Map<String, Object> responseMap = jsonToMap(response);
                        market = (String) responseMap.get("market");

                        Map<String, Object> bidsAsks = (Map<String, Object>) bitvavo.book.get(market);

                        List<List<String>> bidsInput = (List<List<String>>) responseMap.get("bids");
                        List<List<String>> asksInput = (List<List<String>>) responseMap.get("asks");

                        if ((int) responseMap.get("nonce") != Integer.parseInt((String) bidsAsks.get("nonce")) + 1) {
                            bitvavo.websocketObject.subscriptionBook(market, this.subscriptionBookHandlerMap.get(market));
                        }else{
                            List<List<String>> bids = (List<List<String>>) bidsAsks.get("bids");
                            List<List<String>> asks = (List<List<String>>) bidsAsks.get("asks");

                            bids = sortAndInsert(bidsInput, bids, false);
                            asks = sortAndInsert(asksInput, asks, true);
                            bidsAsks.put("bids", bids);
                            bidsAsks.put("asks", asks);
                            bidsAsks.put("nonce", Integer.toString((int) responseMap.get("nonce")));
                            bitvavo.book.put(market, bidsAsks);

                            this.subscriptionBookHandlerMap.get(market).handleBook((Map<String, Object>) bitvavo.book.get(market));
                        }
                    }
                    break;
            }
        } else if (response.has("action")) {
            if (response.getString("action").equals("getTime") && this.timeHandler != null) {
                this.timeHandler.handleMessage(response);
            } else if (response.getString("action").equals("getMarkets") && this.marketsHandler != null) {
                this.marketsHandler.handleMessage(response);
            } else if (response.getString("action").equals("getAssets") && this.assetsHandler != null) {
                this.assetsHandler.handleMessage(response);
            } else if (response.getString("action").equals("getBook")) {
                if (this.bookHandler != null) {
                    this.bookHandler.handleMessage(response);
                }
                if (keepBookCopy && this.subscriptionBookHandlerMap != null) {
                    market = response.getJSONObject("response").getString("market");
                    if (this.subscriptionBookHandlerMap.get(market) != null) {
                        Map<String, Object> bidsAsks = (Map<String, Object>) bitvavo.book.get(market);
                        List<List<String>> bids = (List<List<String>>) bidsAsks.get("bids");
                        List<List<String>> asks = (List<List<String>>) bidsAsks.get("asks");

                        Map<String, Object> bookentry = jsonToMap(response.getJSONObject("response"));

                        List<List<String>> bidsInput = (List<List<String>>) bookentry.get("bids");
                        List<List<String>> asksInput = (List<List<String>>) bookentry.get("asks");

                        bidsAsks.put("bids", bidsInput);
                        bidsAsks.put("asks", asksInput);
                        bidsAsks.put("nonce", Integer.toString(response.getJSONObject("response").getInt("nonce")));
                        bitvavo.book.put(market, bidsAsks);

                        this.subscriptionBookHandlerMap.get(market).handleBook((Map<String, Object>) bitvavo.book.get(market));
                    }
                }
            } else if (response.getString("action").equals("getTrades") && this.tradesHandler != null) {
                this.tradesHandler.handleMessage(response);
            } else if (response.getString("action").equals("getCandles") && this.candlesHandler != null) {
                this.candlesHandler.handleMessage(response);
            } else if (response.getString("action").equals("getTicker24h") && this.ticker24hHandler != null) {
                this.ticker24hHandler.handleMessage(response);
            } else if (response.getString("action").equals("getTickerPrice") && this.tickerPriceHandler != null) {
                this.tickerPriceHandler.handleMessage(response);
            } else if (response.getString("action").equals("getTickerBook") && this.tickerBookHandler != null) {
                this.tickerBookHandler.handleMessage(response);
            } else if (response.getString("action").equals("privateCreateOrder") && this.createOrderHandler != null) {
                this.createOrderHandler.handleMessage(response);
            } else if (response.getString("action").equals("privateGetOrder") && this.getOrderHandler != null) {
                this.getOrderHandler.handleMessage(response);
            } else if (response.getString("action").equals("privateUpdateOrder") && this.updateOrderHandler != null) {
                this.updateOrderHandler.handleMessage(response);
            } else if (response.getString("action").equals("privateCancelOrder") && this.cancelOrderHandler != null) {
                this.cancelOrderHandler.handleMessage(response);
            } else if (response.getString("action").equals("privateGetOrders") && this.getOrdersHandler != null) {
                this.getOrdersHandler.handleMessage(response);
            } else if (response.getString("action").equals("privateCancelOrders") && this.cancelOrdersHandler != null) {
                this.cancelOrdersHandler.handleMessage(response);
            } else if (response.getString("action").equals("privateGetOrdersOpen") && this.getOrdersOpenHandler != null) {
                this.getOrdersOpenHandler.handleMessage(response);
            } else if (response.getString("action").equals("privateGetTrades") && this.getTradesHandler != null) {
                this.getTradesHandler.handleMessage(response);
            } else if (response.getString("action").equals("privateGetAccount") && this.getAccountHandler != null) {
                this.getAccountHandler.handleMessage(response);
            } else if (response.getString("action").equals("privateGetBalance") && this.balanceHandler != null) {
                this.balanceHandler.handleMessage(response);
            } else if (response.getString("action").equals("privateDepositAssets") && this.depositAssetsHandler != null) {
                this.depositAssetsHandler.handleMessage(response);
            } else if (response.getString("action").equals("privateWithdrawAssets") && this.withdrawAssetsHandler != null) {
                this.withdrawAssetsHandler.handleMessage(response);
            } else if (response.getString("action").equals("privateGetDepositHistory") && this.depositHistoryHandler != null) {
                this.depositHistoryHandler.handleMessage(response);
            } else if (response.getString("action").equals("privateGetWithdrawalHistory") && this.withdrawalHistoryHandler != null) {
                this.withdrawalHistoryHandler.handleMessage(response);
            }
        } else if (this.messageHandler != null) {
            this.messageHandler.handleMessage(response);
        }
    }

    public void addMessageHandler(MessageHandler msgHandler) {
        this.messageHandler = msgHandler;
    }

    public void addErrorHandler(MessageHandler msgHandler) {
        this.errorHandler = msgHandler;
    }

    public void addTimeHandler(MessageHandler msgHandler) {
        this.timeHandler = msgHandler;
    }

    public void addMarketsHandler(MessageHandler msgHandler) {
        this.marketsHandler = msgHandler;
    }

    public void addAssetsHandler(MessageHandler msgHandler) {
        this.assetsHandler = msgHandler;
    }

    public void addBookHandler(MessageHandler msgHandler) {
        this.bookHandler = msgHandler;
    }

    public void addTradesHandler(MessageHandler msgHandler) {
        this.tradesHandler = msgHandler;
    }

    public void addCandlesHandler(MessageHandler msgHandler) {
        this.candlesHandler = msgHandler;
    }

    public void addTicker24hHandler(MessageHandler msgHandler) {
        this.ticker24hHandler = msgHandler;
    }

    public void addTickerPriceHandler(MessageHandler msgHandler) {
        this.tickerPriceHandler = msgHandler;
    }

    public void addTickerBookHandler(MessageHandler msgHandler) {
        this.tickerBookHandler = msgHandler;
    }

    public void addPlaceOrderHandler(MessageHandler msgHandler) {
        this.createOrderHandler = msgHandler;
    }

    public void addGetOrderHandler(MessageHandler msgHandler) {
        this.getOrderHandler = msgHandler;
    }

    public void addUpdateOrderHandler(MessageHandler msgHandler) {
        this.updateOrderHandler = msgHandler;
    }

    public void addCancelOrderHandler(MessageHandler msgHandler) {
        this.cancelOrderHandler = msgHandler;
    }

    public void addGetOrdersHandler(MessageHandler msgHandler) {
        this.getOrdersHandler = msgHandler;
    }

    public void addCancelOrdersHandler(MessageHandler msgHandler) {
        this.cancelOrdersHandler = msgHandler;
    }

    public void addGetOrdersOpenHandler(MessageHandler msgHandler) {
        this.getOrdersOpenHandler = msgHandler;
    }

    public void addGetTradesHandler(MessageHandler msgHandler) {
        this.getTradesHandler = msgHandler;
    }

    public void addAccountHandler(MessageHandler msgHandler) {
        this.getAccountHandler = msgHandler;
    }

    public void addBalanceHandler(MessageHandler msgHandler) {
        this.balanceHandler = msgHandler;
    }

    public void addDepositAssetsHandler(MessageHandler msgHandler) {
        this.depositAssetsHandler = msgHandler;
    }

    public void addWithdrawAssetsHandler(MessageHandler msgHandler) {
        this.withdrawAssetsHandler = msgHandler;
    }

    public void addDepositHistoryHandler(MessageHandler msgHandler) {
        this.depositHistoryHandler = msgHandler;
    }

    public void addWithdrawalHistoryHandler(MessageHandler msgHandler) {
        this.withdrawalHistoryHandler = msgHandler;
    }

    public void addSubscriptionTickerHandler(String market, MessageHandler msgHandler) {
        if (this.subscriptionTickerHandlerMap == null) {
            this.subscriptionTickerHandlerMap = new HashMap<>();
        }
        this.subscriptionTickerHandlerMap.put(market, msgHandler);
    }

    public void copySubscriptionTickerHandler(Map<String, MessageHandler> map) {
        this.subscriptionTickerHandlerMap = map;
    }

    public void addSubscriptionTicker24hHandler(String market, MessageHandler msgHandler) {
        if (this.subscriptionTicker24hHandlerMap == null) {
            this.subscriptionTicker24hHandlerMap = new HashMap<>();
        }
        this.subscriptionTicker24hHandlerMap.put(market, msgHandler);
    }

    public void copySubscriptionTicker24hHandler(Map<String, MessageHandler> map) {
        this.subscriptionTicker24hHandlerMap = map;
    }

    public void addSubscriptionAccountHandler(String market, MessageHandler msgHandler) {
        if (this.subscriptionAccountHandlerMap == null) {
            this.subscriptionAccountHandlerMap = new HashMap<>();
        }
        this.subscriptionAccountHandlerMap.put(market, msgHandler);
    }

    public void copySubscriptionAccountHandler(Map<String, MessageHandler> map) {
        this.subscriptionAccountHandlerMap = map;
    }

    public void addSubscriptionCandlesHandler(String market, String interval, MessageHandler msgHandler) {
        if (this.subscriptionCandlesHandlerMap == null) {
            this.subscriptionCandlesHandlerMap = new HashMap<>();
        }
        if (this.subscriptionCandlesHandlerMap.get(market) != null) {
            this.subscriptionCandlesHandlerMap.get(market).put(interval, msgHandler);
        } else {
            this.subscriptionCandlesHandlerMap.put(market, new HashMap<>());
            this.subscriptionCandlesHandlerMap.get(market).put(interval, msgHandler);
        }
    }

    public void copySubscriptionCandlesHandler(Map<String, Map<String, MessageHandler>> map) {
        this.subscriptionCandlesHandlerMap = map;
    }

    public void addSubscriptionTradesHandler(String market, MessageHandler msgHandler) {
        if (this.subscriptionTradesHandlerMap == null) {
            this.subscriptionTradesHandlerMap = new HashMap<>();
        }
        this.subscriptionTradesHandlerMap.put(market, msgHandler);
    }

    public void copySubscriptionTradesHandler(Map<String, MessageHandler> map) {
        this.subscriptionTradesHandlerMap = map;
    }

    public void addSubscriptionBookUpdateHandler(String market, MessageHandler msgHandler) {
        if (this.subscriptionBookUpdateHandlerMap == null) {
            this.subscriptionBookUpdateHandlerMap = new HashMap<>();
        }
        this.subscriptionBookUpdateHandlerMap.put(market, msgHandler);
    }

    public void copySubscriptionBookUpdateHandler(Map<String, MessageHandler> map) {
        this.subscriptionBookUpdateHandlerMap = map;
    }

    public void addAuthenticateHandler(MessageHandler msgHandler) {
        this.authenticateHandler = msgHandler;
    }

    public void addSubscriptionBookHandler(String market, BookHandler msgHandler) {
        if (this.subscriptionBookHandlerMap == null) {
            this.subscriptionBookHandlerMap = new HashMap<>();
        }
        this.subscriptionBookHandlerMap.put(market, msgHandler);
    }

    public void copySubscriptionBookHandler(Map<String, BookHandler> map) {
        this.subscriptionBookHandlerMap = map;
    }

    public void sendMessage(String message) {
        bitvavo.debugToConsole("Sending message " + message);
        this.userSession.getAsyncRemote().sendText(message);
    }

    public static interface MessageHandler {
        public void handleMessage(JSONObject response);
    }

    public static interface BookHandler {
        public void handleBook(Map<String, Object> book);
    }
}