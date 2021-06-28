package GrookingMultithreading;

import java.util.Random;

/**
 * The below program spawns two threads. One thread prints the value of a shared variable whenever
 * the shared variable is divisible by 5. A race condition happens when the printer thread executes
 * a test-then-act if clause, which checks if the shared variable is divisible by 5 but before the
 * thread can print the variable out, its value is changed by the modifier thread. Some of the
 * printed values aren't divisible by 5 which verifies the existence of a race condition in the
 * code.
 */
public class RaceCondition {

  public static void main(String args[]) throws InterruptedException {
    RaceCondition rc = new RaceCondition();
    rc.runTest();
  }

  int randInt;
  Random random = new Random(System.currentTimeMillis());

  void printer() {
      int i = 1000000;
      while ( i != 0) {
      synchronized (this) {
        if (randInt % 5 == 0) {
          if (randInt % 5 != 0) {
            System.out.println(randInt);
          }
        }
        i--;
        }
      }
  }

  void modifier() {
    int i = 1000000;
    while (i != 0) {
      synchronized (this) {
        randInt = random.nextInt(1000);
        i--;
      }
    }
  }

  public static void runTest() throws InterruptedException {

    RaceCondition rc = new RaceCondition();
    Thread t1 = new Thread(new Runnable() {
      @Override
      public void run() {
        rc.printer();
      }
    });

    Thread t2 = new Thread(new Runnable() {
      @Override
      public void run() {
        rc.modifier();
      }
    });

    t1.start();
    t2.start();

    t1.join();
    t2.join();
  }

}
