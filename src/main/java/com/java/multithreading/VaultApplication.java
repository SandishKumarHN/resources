package com.java.multithreading;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VaultApplication {

  public static void main(String args[]) {

    Random random = new Random();
    Vault vault = new Vault(random.nextInt(MAX_PASSWORD));

    List<Thread> threads = new ArrayList<>();

    threads.add(new AscendingHackerThread(vault));
    threads.add(new DescendingHackerThread(vault));
    threads.add(new PoliceThread());

    for(Thread thread : threads) {
      thread.start();
    }
  }

  private static final int MAX_PASSWORD = 9999;

  public static class Vault {
    private int password;
    public Vault(int password){
      this.password = password;
    }

    public boolean isCorrecypassword(int guess) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return this.password == guess;
    }
  }

  public static abstract class HackerThread extends Thread {
    public static Vault vault;
    public HackerThread(Vault vault) {
      this.vault = vault;
      this.setName(this.getClass().getSimpleName());
      this.setPriority(Thread.MAX_PRIORITY);
    }

    @Override
    public void start() {
      System.out.println("Starting the hackerThread");
      super.start();
    }
  }

  public static class AscendingHackerThread extends HackerThread{

    public AscendingHackerThread(Vault vault) {
      super(vault);
    }

    @Override
    public void run() {
      for(int guess = 0; guess < MAX_PASSWORD; guess++) {
        if(vault.isCorrecypassword(guess)) {
          System.out.println(this.getName() + "Guess the password " + guess);
        }
      }
    }
  }

  public static class DescendingHackerThread extends HackerThread {
    public DescendingHackerThread(Vault vault) {
      super(vault);
    }

    @Override
    public void run() {
      for(int guess = MAX_PASSWORD; guess > 0; guess--) {
        if(vault.isCorrecypassword(guess)) {
          System.out.println(this.getName() + " Guessed the password " + guess);
        }
      }
    }
  }

  public static class PoliceThread extends  Thread {

    @Override
    public void run() {
      for(int i = 10; i > 0 ; i--) {
        try {
          Thread.sleep(1000);
        } catch (InterruptedException  e) {

        }
        System.out.println(i);
      }
      System.out.println("Game over for you hackers");
      System.exit(0);
    }
  }
}
