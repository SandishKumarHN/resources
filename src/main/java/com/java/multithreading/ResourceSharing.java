package com.java.multithreading;

public class ResourceSharing {
  public static void main(String[] args) throws InterruptedException {
    InventoryCounter inventoryCounter = new InventoryCounter();
    IncrementCounter incrementCounter = new IncrementCounter(inventoryCounter);
    DecreamentCounter decreamentCounter = new DecreamentCounter(inventoryCounter);

    incrementCounter.start();
    decreamentCounter.start();

    incrementCounter.join();
    decreamentCounter.join();

    System.out.println(inventoryCounter.getItems());
  }


  public static class DecreamentCounter extends Thread {

    private InventoryCounter inventoryCounter;

    public DecreamentCounter(InventoryCounter in) {
      this.inventoryCounter = in;
    }

    @Override
    public void run() {
      for (int i = 0; i < 10000; i++) {
        inventoryCounter.decreament();
      }
    }
  }

  public static class IncrementCounter extends Thread {

    private InventoryCounter inventoryCounter;

    public IncrementCounter(InventoryCounter in) {
      this.inventoryCounter = in;
    }

    @Override
    public void run() {
      for (int i = 0; i < 10000; i++) {
        inventoryCounter.increament();
      }
    }
  }

  private static class InventoryCounter {
    private int items = 0;

    public synchronized void increament() {
      items++;
    }

    public synchronized void decreament() {
      items--;
    }

    public int getItems() {
      return items;
    }
  }
}
