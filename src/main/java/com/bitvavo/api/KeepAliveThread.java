package com.bitvavo.api;

public class KeepAliveThread extends Thread {
  public void run(){
    while(true) {
      try{
        Thread.sleep(200);
      }
      catch(InterruptedException ex) {
        System.out.println("We got interrupted, terminating thread.");
      }
    }
  }
}