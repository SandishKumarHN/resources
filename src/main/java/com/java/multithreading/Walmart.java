package com.java.multithreading;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class Walmart implements DistributedCache {

  ConcurrentHashMap<String, Future<String>> map = new ConcurrentHashMap<>();
  Integer requestOutOfCache = 0;
  Integer totalRequest = 0;
  @Override
  public String getEntry(String key) throws ExecutionException, InterruptedException {
    Future<String> value = map.get(key);
    if(value == null) {
      FutureTask task = null;
      synchronized(map) {
        requestOutOfCache++;
        task= new FutureTask(new Callable() {
        @Override
        public Object call() throws Exception {
          return null;
        }
      });
        totalRequest++;
        map.put(key, task);
      }
    }
    return map.get(key).get();
  }

  @Override
  public float cacheHitRatio() {
    return totalRequest - requestOutOfCache;
  }

}


//
//  Design a distributed cache that keeps upto N cache entries for a backend data system.
//  Multiple readers can call getEntry() to retrieve entries from the cache.
//  If the cache does not have the entry for the specified key, the reader shall wait until the entry becomes available.