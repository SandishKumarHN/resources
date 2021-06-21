package com.java.multithreading;

import java.util.concurrent.ExecutionException;

public interface DistributedCache {
  String getEntry(String key) throws ExecutionException, InterruptedException;
  float  cacheHitRatio();
}

