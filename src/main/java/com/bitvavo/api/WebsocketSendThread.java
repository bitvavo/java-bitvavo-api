package com.bitvavo.api;

import java.util.concurrent.TimeUnit;
import org.json.*;
import java.util.*;

public class WebsocketSendThread extends Thread {
  JSONObject options;
  Bitvavo bitvavo;
  WebsocketClientEndpoint ws;

  public WebsocketSendThread(JSONObject options, Bitvavo bitv, WebsocketClientEndpoint ws) {
    this.options = options;
    this.bitvavo = bitv;
    this.ws = ws;
  }

  public void sendPrivate(JSONObject options) {
    if(this.bitvavo.authenticated) {
      Iterator<String> markets = options.keys();
      while(markets.hasNext()) {
        String market = markets.next();
        this.ws.sendMessage(options.get(market).toString());
      }
    } else {
      try {
        TimeUnit.MILLISECONDS.sleep(500);
        sendPrivate(options);
      }
      catch (Exception e) {
        System.out.println("something went wrong while sending private request.");
      }
    }
  }

  public void run(){
    sendPrivate(this.options);
  }
}