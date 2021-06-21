package com.java.multithreading;

import java.lang.Thread.UncaughtExceptionHandler;

public class Fundamentals {

  public static void main(String args[]) throws InterruptedException {
    threadExample();
    threadUnCaughtExample();
  }

  public static void threadUnCaughtExample() {
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        throw new RuntimeException("Thread error for uncaughtException");
      }
    });

    thread.setName("Worker Thread fro uncaught Exception");
    thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
      @Override
      public void uncaughtException(Thread t, Throwable e) {
        System.out.print("uncaught exception happend in thread: " + t.getName() +
            " the error is " + e.getMessage());
      }
    });
    thread.start();
  }

  public static void threadExample() {
    Thread thread = new Thread(new Runnable() {
      @Override
      public void run() {
        System.out.println("Inside the thread: " + Thread.currentThread().getName());
        System.out.println("Thread Priority: " + Thread.currentThread().getPriority());
      }
    });

    thread.setName("New Worker Thread");
    thread.setPriority(Thread.MAX_PRIORITY);
    System.out.println("before starting the thread: " + Thread.currentThread().getName());
    thread.start();
    System.out.println("after starting the thread: " + Thread.currentThread().getName());
  }
}
