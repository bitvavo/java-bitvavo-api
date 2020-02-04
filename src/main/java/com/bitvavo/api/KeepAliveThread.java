package com.bitvavo.api;

public class KeepAliveThread extends Thread {
  public void run(){
    boolean keepRunning = true;
    while(keepRunning) {
      try{
        Thread.sleep(200);
      }
      catch(InterruptedException ex) {
        keepRunning = false;
        System.out.println("KeepAliveThread got interrupted, terminating thread.");
      }
    }
  }
}