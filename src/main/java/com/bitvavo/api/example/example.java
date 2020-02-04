package com.bitvavo.api.example;

import com.bitvavo.api.*;
import org.json.*;
import java.util.*;

/*
 * This is an example utilising all functions of the node Bitvavo API wrapper.
 * The APIKEY and APISECRET should be replaced by your own key and secret.
 * For public functions the APIKEY and SECRET can be removed.
 * Documentation: https://docs.bitvavo.com
 * Bitvavo: https://bitvavo.com
 * README: https://github.com/bitvavo/java-bitvavo-api
 */

class example{
  public static void main(String args[]){
    Bitvavo bitvavo = new Bitvavo(new JSONObject("{" + 
        "APIKEY: '<APIKEY>', " +
        "APISECRET: '<APISECRET>', " +
        "RESTURL: 'https://api.bitvavo.com/v2'," +
        "WSURL: 'wss://ws.bitvavo.com/v2/'," +
        "ACCESSWINDOW: 10000, " +
        "DEBUGGING: false }"));

    testREST(bitvavo);
    testWebsocket(bitvavo);
  }

  public static void testREST(Bitvavo bitvavo) {
    JSONArray response;

    int remaining = bitvavo.getRemainingLimit();
    if (remaining > 0) {
      System.out.println("remaining limit is " + remaining);
      System.out.println(bitvavo.time().toString(2));
    }

    // response = bitvavo.markets(new JSONObject());
    // for(int i = 0; i < response.length(); i ++) {
    //   System.out.println(response.getJSONObject(i).toString(2));
    // }

    // response = bitvavo.assets(new JSONObject());
    // for(int i = 0; i < response.length(); i ++) {
    //   System.out.println(response.getJSONObject(i).toString(2));
    // }

    // System.out.println(bitvavo.book("BTC-EUR", new JSONObject()).toString(2));

    // response = bitvavo.publicTrades("BTC-EUR", new JSONObject());
    // for(int i = 0; i < response.length(); i ++) {
    //   System.out.println(response.getJSONObject(i).toString(2));
    // }

    // JSONArray candles = bitvavo.candles("BTC-EUR", "1h", new JSONObject());
    // for(int i = 0; i < candles.length(); i ++) {
    //   System.out.println(candles.getJSONArray(i).toString(2));
    // }

    // response = bitvavo.tickerPrice(new JSONObject());
    // for(int i = 0; i < response.length(); i ++) {
    //   System.out.println(response.getJSONObject(i).toString(2));
    // }

    // response = bitvavo.tickerBook(new JSONObject());
    // for(int i = 0; i < response.length(); i ++) {
    //   System.out.println(response.getJSONObject(i).toString(2));
    // }

    // response = bitvavo.ticker24h(new JSONObject());
    // for(int i = 0; i < response.length(); i ++) {
    //   System.out.println(response.getJSONObject(i).toString(2));
    // }

    // System.out.println(bitvavo.placeOrder("BTC-EUR", "sell", "limit", new JSONObject("{ amount: 0.1, price: 4000 }")).toString(2));
    
    // System.out.println(bitvavo.getOrder("BTC-EUR", "afa9da1c-edb9-4245-9271-3549147845a1").toString(2));

    // System.out.println(bitvavo.updateOrder("BTC-EUR", "afa9da1c-edb9-4245-9271-3549147845a1", new JSONObject("{ amount: 0.2 }")).toString(2));

    // System.out.println(bitvavo.cancelOrder("BTC-EUR", "afa9da1c-edb9-4245-9271-3549147845a1").toString(2));
    
    // response = bitvavo.getOrders("BTC-EUR", new JSONObject());
    // for(int i = 0; i < response.length(); i ++) {
    //   System.out.println(response.getJSONObject(i).toString(2));
    // }

    // response = bitvavo.cancelOrders(new JSONObject("{ market: BTC-EUR }"));
    // for(int i = 0; i < response.length(); i ++) {
    //   System.out.println(response.getJSONObject(i).toString(2));
    // }
    
    // response = bitvavo.ordersOpen(new JSONObject("{ market: BTC-EUR }"));
    // for(int i = 0; i < response.length(); i ++) {
    //   System.out.println(response.getJSONObject(i).toString(2));
    // }
    
    // response = bitvavo.trades("BTC-EUR", new JSONObject());
    // for(int i = 0; i < response.length(); i ++) {
    //   System.out.println(response.getJSONObject(i).toString(2));
    // }

    // response = bitvavo.balance(new JSONObject());
    // for(int i = 0; i < response.length(); i ++) {
    //   System.out.println(response.getJSONObject(i).toString(2));
    // }

    // System.out.println(bitvavo.depositAssets("BTC").toString(2));

    // System.out.println(bitvavo.withdrawAssets("BTC", "1", "BitcoinAddress", new JSONObject()).toString(2));

    // response = bitvavo.depositHistory(new JSONObject());
    // for(int i = 0; i < response.length(); i ++) {
    //   System.out.println(response.getJSONObject(i).toString(2));
    // }

    // response = bitvavo.withdrawalHistory(new JSONObject());
    // for(int i = 0; i < response.length(); i ++) {
    //   System.out.println(response.getJSONObject(i).toString(2));
    // }
  }

  public static void testWebsocket(Bitvavo bitvavo) {
    Bitvavo.Websocket ws = bitvavo.newWebsocket();

    ws.setErrorCallback(new WebsocketClientEndpoint.MessageHandler() {
      public void handleMessage(JSONObject response) {
        System.out.println("Found ERROR, own callback." + response);
      }
    });

    ws.time(new WebsocketClientEndpoint.MessageHandler() {
      public void handleMessage(JSONObject responseObject) {
        System.out.println(responseObject.getJSONObject("response").toString(2));
      }
    });

    // ws.markets(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject responseObject) {
    //     JSONArray response = responseObject.getJSONArray("response");
    //     for (int i = 0; i < response.length(); i ++) {
    //       System.out.println(response.getJSONObject(i).toString(2));
    //     }
    //   }
    // });

    // ws.assets(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject responseObject) {
    //     JSONArray response = responseObject.getJSONArray("response");
    //     for (int i = 0; i < response.length(); i ++) {
    //       System.out.println(response.getJSONObject(i).toString(2));
    //     }
    //   }
    // });

    // ws.book("BTC-EUR", new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject responseObject) {
    //     System.out.println(responseObject.getJSONObject("response").toString(2));
    //   }
    // });

    // ws.publicTrades("BTC-EUR", new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject responseObject) {
    //     JSONArray response = responseObject.getJSONArray("response");
    //     for (int i = 0; i < response.length(); i ++) {
    //       System.out.println(response.getJSONObject(i).toString(2));
    //     }
    //   }
    // });

    // ws.candles("BTC-EUR", "1h", new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject responseObject) {
    //     JSONArray response = responseObject.getJSONArray("response");
    //     for (int i = 0; i < response.length(); i ++) {
    //       System.out.println(response.getJSONArray(i).toString(2));
    //     }
    //   }
    // });

    // ws.ticker24h(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject responseObject) {
    //     JSONArray response = responseObject.getJSONArray("response");
    //     for (int i = 0; i < response.length(); i ++) {
    //       System.out.println(response.getJSONObject(i).toString(2));
    //     }
    //   }
    // });

    // ws.tickerPrice(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject responseObject) {
    //     JSONArray response = responseObject.getJSONArray("response");
    //     for (int i = 0; i < response.length(); i ++) {
    //       System.out.println(response.getJSONObject(i).toString(2));
    //     }
    //   }
    // });

    // ws.tickerBook(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject responseObject) {
    //     JSONArray response = responseObject.getJSONArray("response");
    //     for (int i = 0; i < response.length(); i ++) {
    //       System.out.println(response.getJSONObject(i).toString(2));
    //     }
    //   }
    // });

    // ws.placeOrder("BTC-EUR", "sell", "limit", new JSONObject("{ amount: 1.2, price: 6000 }"), new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject responseObject) {
    //     System.out.println(responseObject.getJSONObject("response").toString(2));
    //   }
    // });

    // ws.updateOrder("BTC-EUR", "8653b765-f6ce-44ad-b474-8cf56bd4469f", new JSONObject("{ amount: 1.4 }"), new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject responseObject) {
    //     System.out.println(responseObject.getJSONObject("response").toString(2));
    //   }
    // });

    // ws.getOrder("BTC-EUR", "8653b765-f6ce-44ad-b474-8cf56bd4469f", new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject responseObject) {
    //     System.out.println(responseObject.getJSONObject("response").toString(2));
    //   }
    // });

    // ws.cancelOrder("BTC-EUR", "8653b765-f6ce-44ad-b474-8cf56bd4469f", new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject responseObject) {
    //     System.out.println(responseObject.getJSONObject("response").toString(2));
    //   }
    // });

    // ws.getOrders("BTC-EUR", new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject responseObject) {
    //     JSONArray response = responseObject.getJSONArray("response");
    //     for (int i = 0; i < response.length(); i ++) {
    //       System.out.println(response.getJSONObject(i).toString(2));
    //     }
    //   }
    // });

    // ws.cancelOrders(new JSONObject("{ market: BTC-EUR }"), new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject responseObject) {
    //     JSONArray response = responseObject.getJSONArray("response");
    //     for (int i = 0; i < response.length(); i ++) {
    //       System.out.println(response.getJSONObject(i).toString(2));
    //     }
    //   }
    // });

    // ws.ordersOpen(new JSONObject("{ market: BTC-EUR }"), new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject responseObject) {
    //     JSONArray response = responseObject.getJSONArray("response");
    //     for (int i = 0; i < response.length(); i ++) {
    //       System.out.println(response.getJSONObject(i).toString(2));
    //     }
    //   }
    // });

    // ws.trades("BTC-EUR", new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject responseObject) {
    //     JSONArray response = responseObject.getJSONArray("response");
    //     for (int i = 0; i < response.length(); i ++) {
    //       System.out.println(response.getJSONObject(i).toString(2));
    //     }
    //   }
    // });

    // ws.balance(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject responseObject) {
    //     JSONArray response = responseObject.getJSONArray("response");
    //     for (int i = 0; i < response.length(); i ++) {
    //       System.out.println(response.getJSONObject(i).toString(2));
    //     }
    //   }
    // });

    // ws.depositAssets("BTC", new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject responseObject) {
    //     System.out.println(responseObject.getJSONObject("response").toString(2));
    //   }
    // });

    // ws.withdrawAssets("BTC", "1", "BitcoinAddress", new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject responseObject) {
    //     System.out.println(responseObject.getJSONObject("response").toString(2));
    //   }
    // });

    // ws.depositHistory(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject responseObject) {
    //     JSONArray response = responseObject.getJSONArray("response");
    //     for (int i = 0; i < response.length(); i ++) {
    //       System.out.println(response.getJSONObject(i).toString(2));
    //     }
    //   }
    // });

    // ws.withdrawalHistory(new JSONObject(), new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject responseObject) {
    //     JSONArray response = responseObject.getJSONArray("response");
    //     for (int i = 0; i < response.length(); i ++) {
    //       System.out.println(response.getJSONObject(i).toString(2));
    //     }
    //   }
    // });

    // ws.subscriptionTicker("BTC-EUR", new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject response) {
    //     System.out.println(response.toString(2));
    //   }
    // });

    // ws.subscriptionTicker24h("BTC-EUR", new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject response) {
    //     System.out.println(response.toString(2));
    //   }
    // });

    // ws.subscriptionAccount("BTC-EUR", new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject response) {
    //     System.out.println(response.toString(2));
    //   }
    // });

    // ws.subscriptionCandles("BTC-EUR", "1h", new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject response) {
    //     System.out.println(response.toString(2));
    //   }
    // });

    // ws.subscriptionTrades("BTC-EUR", new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject response) {
    //     System.out.println(response.toString(2));
    //   }
    // });

    // ws.subscriptionBookUpdate("BTC-EUR", new WebsocketClientEndpoint.MessageHandler() {
    //   public void handleMessage(JSONObject response) {
    //     System.out.println(response.toString(2));
    //   }
    // });

    // ws.subscriptionBook("BTC-EUR", new WebsocketClientEndpoint.BookHandler() {
    //   public void handleBook(Map<String, Object> book) {
    //     List<List<String>> bids = (List<List<String>>)book.get("bids");
    //     List<List<String>> asks = (List<List<String>>)book.get("asks");
    //     String nonce = (String)book.get("nonce");
    //     System.out.println(book);
    //   }
    // });

    // The following function can be used to close the socket, callbacks will no longer be called.
    // ws.close()
  }
}
