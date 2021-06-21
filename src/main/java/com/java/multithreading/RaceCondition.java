package com.java.multithreading;

public class RaceCondition {

  public static void main(String[] args) {
    SharedClass sharedClass = new SharedClass();

    Thread thread1 = new Thread(new Runnable() {
      @Override
      public void run() {
        for(int i = 0; i < Integer.MAX_VALUE; i++){
          sharedClass.increment();
        }
      }
    });

    Thread thread2 = new Thread(new Runnable() {
      @Override
      public void run() {
        for(int i = 0; i < Integer.MAX_VALUE; i++){
          sharedClass.checkForRacCondition();
        }
      }
    });

    thread1.start();
    thread2.start();
  }

  private static class SharedClass {
    private volatile int x = 0;
    private volatile int y = 0;

    public void increment(){
      x++;
      y++;
    }

    public void checkForRacCondition() {
      if(y > x) {
        System.out.println("y can not be > x, RTace Condition Error");
      }
    }
  }

}
