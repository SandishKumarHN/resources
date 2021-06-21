package com.java.multithreading;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Joiningthreads {
  public static void main(String[] args) throws InterruptedException {
    //

    List<Long> inputNUmnbers = Arrays.asList(0L, 335L, 3004L, 2800L, 982309830923L);

    List<FactorialThread> threads = new ArrayList<>();
    for(long inputNumber: inputNUmnbers) {
      threads.add(new FactorialThread(inputNumber));
    }

    for(Thread thread: threads) {
      thread.start();
    }

    for(Thread thread: threads) {
      thread.join(2000);
    }

    for(int i = 0; i < inputNUmnbers.size(); i++) {
      FactorialThread thread = threads.get(i);
      if(thread.isFinished) {
        System.out.println("Factorial of: " + inputNUmnbers.get(i) + " is " + thread.getResult());
      } else {
        System.out.println("Factorial calculation is still porcessing.... for " + inputNUmnbers.get(i));
      }
    }


  }

  public static class FactorialThread extends Thread {

    private long inputNumber;
    private BigInteger result = BigInteger.ZERO;
    private boolean isFinished = false;

    public FactorialThread(long inputNumber) {
      this.inputNumber = inputNumber;
    }

    @Override
    public void run() {
      this.result = factorial(inputNumber);
      this.isFinished = true;
    }

    public BigInteger factorial(long inputNumber) {
      BigInteger tempResult = BigInteger.ONE;
      for(long i = inputNumber; i > 0; i--) {
        tempResult = tempResult.multiply(new BigInteger(Long.toString(i)));
      }

      return tempResult;
    }

    public boolean isFinished(){
      return isFinished;
    }

    public BigInteger getResult(){
      return result;
    }

  }
}
