package com.java.multithreading.leetcode;

import java.util.LinkedList;
import java.util.Queue;

public class BoundedBlockingQueue {
  private Queue<Integer> queue = new LinkedList<>();
  private int limit = 0;

  public BoundedBlockingQueue(int capacity) {
    limit = capacity;
  }

  public synchronized void enqueue(int element) throws InterruptedException {
    while (queue.size() == limit) {
      wait();
    }

    queue.offer(element);
    notifyAll();
  }

  public synchronized int dequeue() throws InterruptedException {
    while (queue.size() == 0) {
      wait();
    }

    int val = queue.poll();
    notifyAll();
    return val;
  }

  public int size() {
    return queue.size();
  }
}
