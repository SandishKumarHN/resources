package com.java.multithreading.leetcode;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Suppose we have a class:
 *
 * <p>public class Foo { public void first() { print("first"); } public void second() {
 * print("second"); } public void third() { print("third"); } } The same instance of Foo will be
 * passed to three different threads. Thread A will call first(), thread B will call second(), and
 * thread C will call third(). Design a mechanism and modify the program to ensure that second() is
 * executed after first(), and third() is executed after second().
 */
public class PrintInOrder {

  private AtomicInteger firstJobDone = new AtomicInteger(0);
  private AtomicInteger secondJobDone = new AtomicInteger(0);

  public PrintInOrder() {}

  public void first(Runnable printFirst) throws InterruptedException {
    printFirst.run();
    firstJobDone.incrementAndGet();
  }

  public void second(Runnable printSecond) throws InterruptedException {
    while (firstJobDone.get() != 1) {

    }

    printSecond.run();
    secondJobDone.incrementAndGet();
  }

  public void third(Runnable printThird) throws InterruptedException {
    while (secondJobDone.get() != 1) {

    }

    printThird.run();
  }

  public static void main(String args[]) {

  }
}
