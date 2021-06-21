package com.java.multithreading;

import java.math.BigInteger;
import scala.math.BigInt;

public class ThreadTerminationAndDeamonThreads {


  public static void main(String args[]) {

    Thread thread = new  Thread(new BlockingThread());
    thread.start();
    thread.interrupt();

    Thread thread1 = new Thread(new LongComputationThread(new BigInteger("10000000"), new BigInteger("20000000")));
    // thread1.setDaemon(true);
    thread1.start();
    thread1.interrupt();


  }

  public static class BlockingThread implements Runnable {

    @Override
    public void run() {
      try {
        Thread.sleep(10000000);
      } catch (InterruptedException e) {
        System.out.println("Exiting the Blocking thread");
      }
    }
  }


  public static class LongComputationThread implements Runnable {

    private BigInteger base;
    private BigInteger power;


    public LongComputationThread(BigInteger power, BigInteger base) {
      this.base = base;
      this.power = power;
    }

    @Override
    public void run() {

      System.out.println(base + "^" + power + "= " + pow(base, power));

    }

    private BigInteger pow(BigInteger base, BigInteger power) {
      BigInteger result = BigInteger.ONE;

      for(BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE) ) {
        // comment if condition for daemon functionality.
        if(Thread.currentThread().isInterrupted()) {
          System.out.println("Thread got Interupted prematurly");
          return BigInteger.ZERO;
        }
        result = result.multiply(base);
      }

      return result;
    }
  }
}
