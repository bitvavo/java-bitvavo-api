package com.bitvavo.api;

public class KeepAliveThread extends Thread {
  @Override
  public void run() {
    while (true) {
      try {
        Thread.sleep(200);
      } catch (InterruptedException ex) {
        System.out.println("KeepAliveThread got interrupted, terminating thread.");
        break;
      }
    }
  }
}