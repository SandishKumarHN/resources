package GrookingMultithreading;

import java.util.Random;

public class DemoThreadSafe {
  static Random random = new Random();

  public static void main(String args[]) throws InterruptedException {
    ThreadUnsafeCounter threadUnsafeCounter = new ThreadUnsafeCounter();

    Thread thread1 = new Thread(new Runnable() {
      @Override
      public void run() {
        for(int i = 0; i < 100; i++) {
          threadUnsafeCounter.increment();
          try {
            sleepForRandom10Second();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    });


    Thread thread2 = new Thread(new Runnable() {
      @Override
      public void run() {
        for(int i = 0; i < 100; i++) {
          threadUnsafeCounter.decrement();
          try {
            sleepForRandom10Second();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
    });

    thread1.start();
    thread2.start();

    thread1.join();
    thread2.join();

    threadUnsafeCounter.printValue();
  }


  public static  void sleepForRandom10Second() throws InterruptedException {
    Thread.sleep(random.nextInt(10));
  }

}

class ThreadUnsafeCounter {
  int count = 0;

  public void increment() {
    count++;
  }

  public void decrement() {
    count--;
  }

  public void printValue() {
    System.out.println("Thread Unsafe counter final value: " + count);
  }

}
