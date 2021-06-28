package GrookingMultithreading;

public class DealLock {
  private int counter = 0;
  private Object lock1 =  new Object();
  private Object lock2 = new Object();

  Runnable incrementer = new Runnable() {
    @Override
    public void run() {
      try {
        for(int i = 0; i < 100; i++) {
          incrementCounter();
          System.out.println("Incrementing, " + i);
        }
      } catch (InterruptedException ie) {

      }
    }
  };

  Runnable derementer = new Runnable() {
    @Override
    public void run() {
      try {
        for(int i = 0; i < 100; i++) {
          derementerCounter();
          System.out.println("Incrementing, " + i);
        }
      } catch (InterruptedException ie) {

      }
    }
  };

  public void runTest() throws InterruptedException {
    Thread t1 = new Thread(incrementer);
    Thread t2 = new Thread(derementer);
     t1.start();
     Thread.sleep(100);
     t2.start();

     t1.join();
     t2.join();

     System.out.println("Done: " + counter);
  }

  public void incrementCounter() throws InterruptedException {
    synchronized (lock1) {
      System.out.println("t1 Acquired lock1");
      Thread.sleep(100);
      synchronized (lock2) {
        System.out.println("t1 Acquired lock2");
        counter++;
      }
    }
  }

  public void derementerCounter() throws InterruptedException {
    synchronized (lock2) {
      System.out.println("t2 Acquired lock2");
      Thread.sleep(100);
      synchronized (lock1) {
        System.out.println("t2 Acquired lock1");
        counter--;
      }
    }
  }

  public static void main(String[] args) {
    DealLock dealLock = new DealLock();
    try {
      dealLock.runTest();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
