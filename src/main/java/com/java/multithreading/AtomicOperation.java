package com.java.multithreading;

import java.util.Random;

public class AtomicOperation {
  public static void main(String[] args) {
    Metrics metrics = new Metrics();

    BussinessLogic bussinessLogic1 = new BussinessLogic(metrics);
    BussinessLogic bussinessLogic2 = new BussinessLogic(metrics);

    PrinterClass printerClass = new PrinterClass(metrics);
    bussinessLogic1.start();
    bussinessLogic2.start();

    printerClass.start();

  }

  public static class PrinterClass extends Thread {
    private Metrics metrics;

    public PrinterClass(Metrics metrics) {
      this.metrics = metrics;
    }

    @Override
    public void run() {
      while (true) {

        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {

        }
        double currentAverage = metrics.getAverage();

        System.out.println("Current Avergae is: " + currentAverage);
      }
    }
  }

  private static class BussinessLogic extends Thread {
    public Metrics metrics;
    public Random random = new Random();

    public BussinessLogic(Metrics metrics) {
      this.metrics = metrics;
    }

    @Override
    public void run() {
      while (true) {
        long start = System.currentTimeMillis();

        try {
          Thread.sleep(random.nextInt(10));
        } catch (InterruptedException e) {

        }
        long end = System.currentTimeMillis();
        metrics.addSample(end - start);
      }
    }
  }
  private static class Metrics {
    private long count = 0;
    private volatile double average = 0.0;

    public synchronized void addSample(long sample) {
      double currentSum = average * count;
      count++;
      average = (currentSum + sample) / count;
    }

    public double getAverage() {
      return average;
    }

  }
}
