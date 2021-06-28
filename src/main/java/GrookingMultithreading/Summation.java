package GrookingMultithreading;

public class Summation {
  public static void main(String args[]) throws InterruptedException {
    SumRange.runTest();

  }
}

class SumRange {
  long startRange;
  long endRange;
  final static  long  max = Integer.MAX_VALUE;
  long count;

  public SumRange(long startRange, long endRange) {
    this.startRange = startRange;
    this.endRange = endRange;
    count = 0;
  }


  public void add() {
    for(long i = startRange; i < endRange; i++) {
      count += i;
    }
  }

  public static void twoThreads() throws InterruptedException {
      long startTime = System.currentTimeMillis();
      SumRange sumRange = new SumRange(1, max / 2);
      SumRange sumRange1 = new SumRange((max / 2) + 1, max);
      Thread t1 = new Thread(() -> {
        sumRange.add();
      });
      Thread t2 = new Thread(() -> {
        sumRange1.add();
      });
      t1.start();
      t2.start();

      t1.join();
      t2.join();

      long finalCount = sumRange.count + sumRange1.count;
      long endTime = System.currentTimeMillis();

      System.out.println("Two threads final count is " + finalCount + "time it took " + (endTime - startTime));
  }

  public static void oneThread() {
    long startTime = System.currentTimeMillis();
    SumRange sumRange = new SumRange(1, max);
    sumRange.add();
    long endTIme = System.currentTimeMillis();
    System.out.println("Single Thread final count= " + sumRange.count + " took " + (endTIme - startTime));
  }
  public static void runTest() throws InterruptedException {
    oneThread();
    twoThreads();
  }
}
