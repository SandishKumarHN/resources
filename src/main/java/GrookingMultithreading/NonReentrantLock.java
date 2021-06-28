package GrookingMultithreading;

public class NonReentrantLock {
  boolean isLocked;

  public NonReentrantLock() {
    isLocked = false;
  }

  public synchronized void lock() {
    while (isLocked) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    isLocked = true;
  }

  public synchronized void unllock() {
    isLocked = false;
    notify();
  }

  public static void main(String[] args) {
    NonReentrantLock nonReentrantLock = new NonReentrantLock();
    nonReentrantLock.lock();
    System.out.println("Acquired first lock");

    System.out.println("trying to Acquire second lock");
    nonReentrantLock.lock();
    System.out.println("Acquired second lock");
  }
}
