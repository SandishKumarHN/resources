# Comprehensive Multithreading and Concurrency Guide

## For Google/Netflix Staff Software Engineer Interviews

## Table of Contents

1. [Introduction](#introduction)
2. [Multithreading Concepts](#multithreading-concepts)
3. [LeetCode-Style Problems](#leetcode-style-problems)

## Introduction

This comprehensive guide is designed to prepare you for multithreading and concurrency questions at Staff and Principal level interviews at Google, Netflix, and other top tech companies. It includes detailed explanations of key multithreading concepts, each with a 5-10 minute video explanation and practical Java examples. Additionally, it provides 50 LeetCode-style multithreading problems with multiple solution approaches, complete Java implementations, and interview tips.

## Multithreading Concepts

# Multithreading and Concurrency Concepts

## Fundamentals
1. Thread vs Process
2. Thread Lifecycle (New, Runnable, Blocked, Waiting, Timed Waiting, Terminated)
3. Thread Creation (Extending Thread class vs implementing Runnable interface)
4. Thread Priorities
5. Daemon Threads
6. Thread Groups
7. Thread Local Variables
8. Context Switching
9. Race Conditions
10. Critical Sections

## Basic Concepts
11. Synchronization
12. Synchronized Methods
13. Synchronized Blocks
14. Monitors and Locks
15. Volatile Keyword
16. Atomic Variables
17. Mutex vs Semaphore
18. Deadlocks
19. Livelocks
20. Starvation
21. Thread Safety
22. Immutability for Concurrency
23. Thread Pools
24. Callable and Future
25. Executor Framework
26. CompletableFuture

## Advanced Concepts
27. Lock Interface (ReentrantLock)
28. ReadWriteLock
29. Condition Variables
30. Semaphores
31. CountDownLatch
32. CyclicBarrier
33. Phaser
34. Exchanger
35. Fork/Join Framework
36. Parallel Streams
37. Concurrent Collections (ConcurrentHashMap, CopyOnWriteArrayList, etc.)
38. BlockingQueue and its implementations
39. Non-blocking Algorithms
40. Memory Barriers and Happens-Before Relationship
41. Java Memory Model
42. False Sharing
43. Thread Confinement
44. Lock-Free Programming
45. Compare-And-Swap (CAS) Operations
46. Amdahl's Law and Scalability
47. Actor Model
48. Reactive Programming (RxJava)
49. Virtual Threads (Project Loom)
50. Structured Concurrency

## Design Patterns for Concurrency
51. Producer-Consumer Pattern
52. Reader-Writer Pattern
53. Thread Pool Pattern
54. Monitor Object Pattern
55. Balking Pattern
56. Double-Checked Locking Pattern
57. Thread-Local Storage Pattern
58. Guarded Suspension Pattern
59. Active Object Pattern
60. Scheduler Pattern


### 1. Thread vs Process


## Video Explanation
[Process Vs Thread In Java (5:30 minutes)](https://www.youtube.com/watch?v=FvN5BWrEWx0)

## Java Example

```java
public class ThreadVsProcessExample {
    public static void main(String[] args) {
        // Process demonstration
        // Each Java application runs as a single process
        // We can get the current process ID using ProcessHandle (Java 9+)
        long pid = ProcessHandle.current().pid();
        System.out.println("Current Process ID: " + pid);
        
        // Thread demonstration
        // The main method runs in the main thread
        Thread mainThread = Thread.currentThread();
        System.out.println("Main Thread Name: " + mainThread.getName());
        System.out.println("Main Thread ID: " + mainThread.getId());
        
        // Creating a new thread
        Thread thread1 = new Thread(() -> {
            System.out.println("Thread 1 is running");
            System.out.println("Thread 1 ID: " + Thread.currentThread().getId());
            System.out.println("Thread 1 belongs to process with ID: " + ProcessHandle.current().pid());
        });
        
        // Creating another thread
        Thread thread2 = new Thread(() -> {
            System.out.println("Thread 2 is running");
            System.out.println("Thread 2 ID: " + Thread.currentThread().getId());
            System.out.println("Thread 2 belongs to process with ID: " + ProcessHandle.current().pid());
        });
        
        // Starting the threads
        thread1.start();
        thread2.start();
        
        // Wait for threads to complete
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("All threads completed");
        
        // Key differences demonstrated:
        // 1. Multiple threads run within the same process (same PID)
        // 2. Each thread has its own ID but shares the process resources
        // 3. Threads can execute concurrently within the process
    }
}
```

## Key Points
- A process is an instance of a program in execution with its own memory space
- A thread is a lightweight unit of execution within a process
- Multiple threads within a process share the same memory space
- Threads are less resource-intensive to create and manage compared to processes
- Context switching between threads is faster than between processes
- In Java, each application runs as a single process, but can have multiple threads


### 2. Thread Lifecycle


## Video Explanation
[#90 Thread States in Java (6:13 minutes)](https://www.youtube.com/watch?v=IWll7sfz3g0)

## Java Example

```java
public class ThreadLifecycleExample {
    public static void main(String[] args) {
        // 1. NEW state - thread is created but not started
        Thread thread = new Thread(() -> {
            try {
                System.out.println("Thread is running");
                
                // 3. RUNNABLE state - thread is executing
                for (int i = 0; i < 3; i++) {
                    System.out.println("Count: " + i);
                    
                    // 4. TIMED_WAITING state - thread sleeps for specified time
                    Thread.sleep(1000);
                }
                
                // Thread can enter BLOCKED state when trying to acquire a lock
                // that is held by another thread
                synchronized (ThreadLifecycleExample.class) {
                    System.out.println("Thread acquired lock");
                }
                
                // Thread can enter WAITING state when waiting indefinitely
                // for another thread to perform a particular action
                Object waitLock = new Object();
                synchronized (waitLock) {
                    waitLock.wait(10); // Will timeout after 10ms to continue example
                }
                
                System.out.println("Thread execution complete");
                // 6. TERMINATED state - thread has completed execution
            } catch (InterruptedException e) {
                System.out.println("Thread was interrupted");
                // Thread can be interrupted from RUNNABLE, WAITING, or TIMED_WAITING states
            }
        });
        
        // Print the initial state (NEW)
        System.out.println("Thread State after creation: " + thread.getState());
        
        // 2. Start the thread - moves to RUNNABLE state
        thread.start();
        System.out.println("Thread State after start: " + thread.getState());
        
        // Monitor thread state changes
        try {
            while (thread.isAlive()) {
                System.out.println("Current Thread State: " + thread.getState());
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Check final state (TERMINATED)
        System.out.println("Final Thread State: " + thread.getState());
    }
}
```

## Key Points
- **NEW**: Thread is created but not yet started
- **RUNNABLE**: Thread is executing in the JVM but may be waiting for OS resources
- **BLOCKED**: Thread is waiting to acquire a monitor lock
- **WAITING**: Thread is waiting indefinitely for another thread to perform a specific action
- **TIMED_WAITING**: Thread is waiting for another thread to perform an action for a specified time
- **TERMINATED**: Thread has completed execution or was terminated abnormally

The lifecycle transitions are:
1. NEW → RUNNABLE (after thread.start())
2. RUNNABLE → BLOCKED (when trying to enter a synchronized block)
3. RUNNABLE → WAITING (when thread.wait(), thread.join() is called)
4. RUNNABLE → TIMED_WAITING (when thread.sleep(), thread.wait(timeout), thread.join(timeout) is called)
5. BLOCKED/WAITING/TIMED_WAITING → RUNNABLE (when lock is acquired, notify/notifyAll is called, or timeout/interruption occurs)
6. RUNNABLE → TERMINATED (when thread completes execution or is stopped)


### 3. Thread Creation (Extending Thread vs implementing Runnable)


## Video Explanation
[Multithreading Basics in Java: Runnable vs Thread](https://www.youtube.com/watch?v=G1RekXYKOqg) - 8:25 minutes

## Java Example

```java
public class ThreadCreationExample {
    public static void main(String[] args) {
        System.out.println("Main thread starting...");
        
        // Method 1: Extending Thread class
        System.out.println("\n--- Method 1: Extending Thread class ---");
        ThreadExtension thread1 = new ThreadExtension("Thread-Extension");
        thread1.start();
        
        // Method 2: Implementing Runnable interface
        System.out.println("\n--- Method 2: Implementing Runnable interface ---");
        RunnableImplementation runnable = new RunnableImplementation("Runnable-Implementation");
        Thread thread2 = new Thread(runnable);
        thread2.start();
        
        // Method 3: Anonymous inner class extending Thread
        System.out.println("\n--- Method 3: Anonymous inner class extending Thread ---");
        Thread thread3 = new Thread() {
            @Override
            public void run() {
                System.out.println("Anonymous Thread is running");
                for (int i = 0; i < 3; i++) {
                    System.out.println("Anonymous Thread: " + i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Anonymous Thread completed");
            }
        };
        thread3.start();
        
        // Method 4: Anonymous inner class implementing Runnable
        System.out.println("\n--- Method 4: Anonymous inner class implementing Runnable ---");
        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                System.out.println("Anonymous Runnable is running");
                for (int i = 0; i < 3; i++) {
                    System.out.println("Anonymous Runnable: " + i);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Anonymous Runnable completed");
            }
        };
        Thread thread4 = new Thread(runnable2);
        thread4.start();
        
        // Method 5: Lambda expression (Java 8+)
        System.out.println("\n--- Method 5: Lambda expression ---");
        Thread thread5 = new Thread(() -> {
            System.out.println("Lambda Thread is running");
            for (int i = 0; i < 3; i++) {
                System.out.println("Lambda Thread: " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("Lambda Thread completed");
        });
        thread5.start();
        
        // Wait for all threads to complete
        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
            thread5.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\nMain thread completed");
    }
}

// Method 1: Extending Thread class
class ThreadExtension extends Thread {
    private String threadName;
    
    public ThreadExtension(String name) {
        this.threadName = name;
    }
    
    @Override
    public void run() {
        System.out.println(threadName + " is running");
        for (int i = 0; i < 3; i++) {
            System.out.println(threadName + ": " + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(threadName + " completed");
    }
}

// Method 2: Implementing Runnable interface
class RunnableImplementation implements Runnable {
    private String threadName;
    
    public RunnableImplementation(String name) {
        this.threadName = name;
    }
    
    @Override
    public void run() {
        System.out.println(threadName + " is running");
        for (int i = 0; i < 3; i++) {
            System.out.println(threadName + ": " + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(threadName + " completed");
    }
}
```

## Key Points

### Extending Thread Class
- Directly inherits all methods from Thread class
- Cannot extend any other class (Java doesn't support multiple inheritance)
- Tightly coupled with threading mechanism
- Example: `class MyThread extends Thread { public void run() { ... } }`

### Implementing Runnable Interface
- Separates task implementation from threading mechanism
- Can still extend other classes if needed
- More flexible and follows better object-oriented design
- Example: `class MyTask implements Runnable { public void run() { ... } }`

### Comparison
1. **Inheritance vs Composition**:
   - Thread extension uses inheritance
   - Runnable implementation uses composition

2. **Flexibility**:
   - Runnable is more flexible as it doesn't prevent extending other classes
   - Thread extension limits your inheritance options

3. **Reusability**:
   - Runnable tasks can be executed by thread pools or other executors
   - Thread extension is tied to a single thread execution model

4. **Resource Efficiency**:
   - Runnable implementation is more efficient when many threads perform the same task
   - Each Thread subclass creates a unique object with its own state

5. **Modern Java**:
   - With Java 8+, Runnable can be implemented using lambda expressions
   - Example: `Thread t = new Thread(() -> { /* code */ });`

### Best Practice
- Prefer implementing Runnable over extending Thread in most cases
- Use Thread extension only when you need to override other Thread methods


### 4. Thread Priorities


## Video Explanation
[Thread Priority in Java | Java Multithreading Interview Questions](https://www.youtube.com/watch?v=BqKuPukfYnk) - 6:41 minutes

## Java Example

```java
public class ThreadPriorityExample {
    public static void main(String[] args) {
        System.out.println("Main thread starting...");
        System.out.println("Default priority of Main thread: " + Thread.currentThread().getPriority());
        
        // Display the constant values for thread priorities
        System.out.println("\nThread Priority Constants:");
        System.out.println("MIN_PRIORITY: " + Thread.MIN_PRIORITY);  // 1
        System.out.println("NORM_PRIORITY: " + Thread.NORM_PRIORITY);  // 5
        System.out.println("MAX_PRIORITY: " + Thread.MAX_PRIORITY);  // 10
        
        // Create threads with different priorities
        Thread lowPriorityThread = new PriorityThread("Low-Priority-Thread");
        Thread normalPriorityThread = new PriorityThread("Normal-Priority-Thread");
        Thread highPriorityThread = new PriorityThread("High-Priority-Thread");
        
        // Set different priorities
        lowPriorityThread.setPriority(Thread.MIN_PRIORITY);  // 1
        // normalPriorityThread will use default priority (5)
        highPriorityThread.setPriority(Thread.MAX_PRIORITY);  // 10
        
        // Display the priorities
        System.out.println("\nSet Thread Priorities:");
        System.out.println(lowPriorityThread.getName() + " priority: " + lowPriorityThread.getPriority());
        System.out.println(normalPriorityThread.getName() + " priority: " + normalPriorityThread.getPriority());
        System.out.println(highPriorityThread.getName() + " priority: " + highPriorityThread.getPriority());
        
        // Start the threads
        System.out.println("\nStarting threads...");
        lowPriorityThread.start();
        normalPriorityThread.start();
        highPriorityThread.start();
        
        // Wait for threads to complete
        try {
            lowPriorityThread.join();
            normalPriorityThread.join();
            highPriorityThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("\nAll threads completed");
        
        // Demonstrate setting custom priority
        System.out.println("\nSetting custom priority value:");
        Thread customPriorityThread = new PriorityThread("Custom-Priority-Thread");
        try {
            // Valid values are 1-10
            customPriorityThread.setPriority(7);
            System.out.println(customPriorityThread.getName() + " priority: " + customPriorityThread.getPriority());
            customPriorityThread.start();
            customPriorityThread.join();
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid priority value: " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Demonstrate invalid priority
        System.out.println("\nAttempting to set invalid priority:");
        Thread invalidPriorityThread = new PriorityThread("Invalid-Priority-Thread");
        try {
            invalidPriorityThread.setPriority(11); // Invalid: must be 1-10
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid priority value: " + e.getMessage());
        }
        
        System.out.println("\nMain thread completed");
    }
}

class PriorityThread extends Thread {
    private int counter = 0;
    
    public PriorityThread(String name) {
        super(name);
    }
    
    @Override
    public void run() {
        System.out.println(getName() + " starting with priority " + getPriority());
        
        // Perform some CPU-intensive work
        for (int i = 0; i < 1000000; i++) {
            counter++;
            
            // Print progress at intervals
            if (i % 250000 == 0) {
                System.out.println(getName() + " - progress: " + (i / 10000) + "%");
            }
        }
        
        System.out.println(getName() + " completed with final count: " + counter);
    }
}
```

## Key Points

1. **Thread Priority Range**:
   - Thread priorities in Java range from 1 to 10
   - Constants: `Thread.MIN_PRIORITY` (1), `Thread.NORM_PRIORITY` (5), and `Thread.MAX_PRIORITY` (10)

2. **Default Priority**:
   - New threads inherit the priority of their parent thread
   - Main thread has default priority of `Thread.NORM_PRIORITY` (5)

3. **Setting Priority**:
   - Use `thread.setPriority(int)` to set thread priority
   - Attempting to set priority outside the range 1-10 throws `IllegalArgumentException`

4. **Priority Scheduling**:
   - Higher priority threads are given preference by the thread scheduler
   - However, thread scheduling is platform-dependent and OS-specific
   - Java thread priorities are mapped to OS thread priorities, which varies by platform

5. **Priority Limitations**:
   - Thread priorities are only hints to the scheduler, not guarantees
   - The JVM relies on the underlying OS for thread scheduling
   - Different operating systems handle thread priorities differently
   - On some systems, there may be no noticeable difference between priorities

6. **Best Practices**:
   - Don't rely on thread priorities for program correctness
   - Use priorities as optimization hints, not as synchronization mechanisms
   - For critical timing, use proper synchronization mechanisms instead
   - Test priority behavior on all target platforms, as results may vary


### 5. Thread Synchronization


## Video Explanation
[Synchronization in Java | Multithreading | Concepts](https://www.youtube.com/watch?v=FuNAe7CxkB0) - 8:08 minutes

## Java Example

```java
public class ThreadSynchronizationExample {
    public static void main(String[] args) {
        System.out.println("Thread Synchronization Example");
        
        // Example 1: Without Synchronization - Race Condition
        System.out.println("\n--- Example 1: Without Synchronization (Race Condition) ---");
        Counter unsafeCounter = new Counter();
        
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                unsafeCounter.increment();
            }
        });
        
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                unsafeCounter.increment();
            }
        });
        
        thread1.start();
        thread2.start();
        
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Unsafe Counter Value: " + unsafeCounter.getCount());
        // Expected: 2000, Actual: May be less than 2000 due to race condition
        
        // Example 2: Method-level Synchronization
        System.out.println("\n--- Example 2: Method-level Synchronization ---");
        SynchronizedCounter safeCounter = new SynchronizedCounter();
        
        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                safeCounter.increment();
            }
        });
        
        Thread thread4 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                safeCounter.increment();
            }
        });
        
        thread3.start();
        thread4.start();
        
        try {
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Safe Counter Value: " + safeCounter.getCount());
        // Expected: 2000, Actual: 2000 (synchronized)
        
        // Example 3: Synchronized Block
        System.out.println("\n--- Example 3: Synchronized Block ---");
        BlockSynchronizedCounter blockCounter = new BlockSynchronizedCounter();
        
        Thread thread5 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                blockCounter.increment();
            }
        });
        
        Thread thread6 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                blockCounter.increment();
            }
        });
        
        thread5.start();
        thread6.start();
        
        try {
            thread5.join();
            thread6.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Block Synchronized Counter Value: " + blockCounter.getCount());
        
        // Example 4: Static Synchronization
        System.out.println("\n--- Example 4: Static Synchronization ---");
        
        Thread thread7 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                StaticSynchronizedCounter.increment();
            }
        });
        
        Thread thread8 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                StaticSynchronizedCounter.increment();
            }
        });
        
        thread7.start();
        thread8.start();
        
        try {
            thread7.join();
            thread8.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Static Synchronized Counter Value: " + StaticSynchronizedCounter.getCount());
    }
}

// Unsafe counter - no synchronization
class Counter {
    private int count = 0;
    
    public void increment() {
        count++; // This is not an atomic operation
    }
    
    public int getCount() {
        return count;
    }
}

// Method-level synchronization
class SynchronizedCounter {
    private int count = 0;
    
    // Synchronized method - only one thread can execute this method at a time
    public synchronized void increment() {
        count++;
    }
    
    public synchronized int getCount() {
        return count;
    }
}

// Block synchronization
class BlockSynchronizedCounter {
    private int count = 0;
    private final Object lock = new Object(); // Lock object
    
    public void increment() {
        // Only the critical section is synchronized
        synchronized (lock) {
            count++;
        }
    }
    
    public int getCount() {
        synchronized (lock) {
            return count;
        }
    }
}

// Static synchronization
class StaticSynchronizedCounter {
    private static int count = 0;
    
    // Synchronized static method - locks the class, not an instance
    public static synchronized void increment() {
        count++;
    }
    
    public static synchronized int getCount() {
        return count;
    }
}
```

## Key Points

1. **Thread Synchronization**:
   - Mechanism to control access of multiple threads to shared resources
   - Prevents race conditions and ensures thread safety
   - Only one thread can access the synchronized code at a time

2. **Race Condition**:
   - Occurs when multiple threads access and modify shared data concurrently
   - Can lead to unpredictable results and data corruption
   - Example: Two threads incrementing a counter simultaneously

3. **Synchronization Methods**:
   - **Method-level Synchronization**: Using `synchronized` keyword with methods
     ```java
     public synchronized void method() { /* code */ }
     ```
   - **Synchronized Block**: Synchronizing only critical sections of code
     ```java
     synchronized (lockObject) { /* critical section */ }
     ```
   - **Static Synchronization**: Synchronizing static methods
     ```java
     public static synchronized void method() { /* code */ }
     ```

4. **Locks in Java**:
   - Every object in Java has an intrinsic lock (monitor)
   - Method-level synchronization uses `this` as the lock object
   - Static synchronization uses the Class object as the lock
   - Synchronized blocks can use any object as a lock

5. **Important Considerations**:
   - Synchronization introduces overhead and can impact performance
   - Over-synchronization can lead to thread contention and deadlocks
   - Under-synchronization can lead to race conditions
   - Use synchronization only for the critical sections that need protection

6. **Alternatives to Synchronization**:
   - `java.util.concurrent` package provides higher-level synchronization utilities
   - Atomic classes (e.g., `AtomicInteger`) for atomic operations
   - Thread-safe collections (e.g., `ConcurrentHashMap`)
   - Locks from `java.util.concurrent.locks` package for more flexible locking


### 6. Volatile Keyword


## Video Explanation
[Introduction to Java Volatile Variables (5:47 minutes)](https://www.youtube.com/watch?v=LBfCBDJD2sA)

## Java Example

```java
public class VolatileKeywordExample {
    // Without volatile - may have visibility issues
    private static boolean flag = false;
    
    // With volatile - guarantees visibility across threads
    private static volatile boolean volatileFlag = false;
    
    // Example of a counter without volatile - has both visibility and atomicity issues
    private static int counter = 0;
    
    // Example of a counter with volatile - fixes visibility but not atomicity
    private static volatile int volatileCounter = 0;
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Volatile Keyword Example");
        
        // Example 1: Visibility issue with non-volatile variable
        System.out.println("\n--- Example 1: Visibility Issue with Non-Volatile Variable ---");
        Thread writerThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("Writer thread setting flag to true");
                flag = true;
                System.out.println("Writer thread set flag to true");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        Thread readerThread = new Thread(() -> {
            System.out.println("Reader thread waiting for flag to become true");
            // This might run indefinitely due to visibility issues
            // The reader thread may never see the updated value of flag
            int count = 0;
            while (!flag) {
                count++;
                // Without this, the loop might never exit on some JVMs/architectures
                if (count % 1000000 == 0) {
                    System.out.println("Still waiting... count: " + count);
                    if (count >= 10000000) {
                        System.out.println("Giving up after 10M iterations");
                        break;
                    }
                }
            }
            System.out.println("Reader thread detected flag change to true after " + count + " iterations");
        });
        
        writerThread.start();
        readerThread.start();
        
        writerThread.join();
        readerThread.join();
        
        // Example 2: Solving visibility issue with volatile
        System.out.println("\n--- Example 2: Solving Visibility Issue with Volatile ---");
        Thread volatileWriterThread = new Thread(() -> {
            try {
                Thread.sleep(1000);
                System.out.println("Volatile writer thread setting volatileFlag to true");
                volatileFlag = true;
                System.out.println("Volatile writer thread set volatileFlag to true");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        Thread volatileReaderThread = new Thread(() -> {
            System.out.println("Volatile reader thread waiting for volatileFlag to become true");
            int count = 0;
            while (!volatileFlag) {
                count++;
                if (count % 1000000 == 0) {
                    System.out.println("Still waiting... count: " + count);
                }
            }
            System.out.println("Volatile reader thread detected volatileFlag change to true after " + count + " iterations");
        });
        
        volatileWriterThread.start();
        volatileReaderThread.start();
        
        volatileWriterThread.join();
        volatileReaderThread.join();
        
        // Example 3: Atomicity issue with volatile
        System.out.println("\n--- Example 3: Atomicity Issue with Volatile ---");
        Thread[] incrementThreads = new Thread[10];
        
        for (int i = 0; i < incrementThreads.length; i++) {
            incrementThreads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    // This operation is not atomic, even with volatile
                    volatileCounter++;
                }
            });
            incrementThreads[i].start();
        }
        
        for (Thread t : incrementThreads) {
            t.join();
        }
        
        System.out.println("Final volatile counter value: " + volatileCounter);
        System.out.println("Expected value: " + (10 * 1000));
        System.out.println("Volatile ensures visibility but NOT atomicity for compound operations like ++");
        
        // Example 4: Solving both visibility and atomicity with AtomicInteger
        System.out.println("\n--- Example 4: Solving Both Visibility and Atomicity with AtomicInteger ---");
        java.util.concurrent.atomic.AtomicInteger atomicCounter = new java.util.concurrent.atomic.AtomicInteger(0);
        
        Thread[] atomicThreads = new Thread[10];
        
        for (int i = 0; i < atomicThreads.length; i++) {
            atomicThreads[i] = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    // This operation is atomic
                    atomicCounter.incrementAndGet();
                }
            });
            atomicThreads[i].start();
        }
        
        for (Thread t : atomicThreads) {
            t.join();
        }
        
        System.out.println("Final atomic counter value: " + atomicCounter.get());
        System.out.println("Expected value: " + (10 * 1000));
        System.out.println("AtomicInteger ensures both visibility and atomicity");
    }
}
```

## Key Points

1. **Purpose of Volatile**:
   - Guarantees memory visibility between threads
   - Ensures that reads and writes go directly to main memory, bypassing CPU caches
   - Prevents compiler optimizations that could reorder operations on the variable

2. **What Volatile Solves**:
   - **Visibility Problem**: Without volatile, Thread A might not see updates made by Thread B due to CPU caching
   - **Happens-Before Relationship**: Establishes a happens-before relationship, ensuring memory consistency

3. **What Volatile Does NOT Solve**:
   - **Atomicity**: Volatile does not make compound operations (like i++) atomic
   - **Race Conditions**: Multiple threads can still interfere with each other
   - **Mutual Exclusion**: Does not provide locking or mutual exclusion

4. **When to Use Volatile**:
   - Single writer, multiple reader scenarios
   - Flag variables that indicate status changes
   - When you need visibility guarantees without the overhead of synchronization
   - Double-checked locking pattern (with caution)

5. **When NOT to Use Volatile**:
   - When you need atomicity for compound operations
   - When multiple threads update the same variable
   - When you need mutual exclusion

6. **Alternatives to Consider**:
   - `AtomicInteger`, `AtomicLong`, etc. for atomic operations
   - `synchronized` blocks for mutual exclusion
   - `java.util.concurrent.locks` for more flexible locking
   - `ConcurrentHashMap` and other concurrent collections

7. **Performance Considerations**:
   - Volatile operations are generally faster than synchronized blocks
   - But slower than non-volatile operations
   - Modern JVMs optimize volatile operations, reducing the performance gap

8. **Memory Barrier Effects**:
   - Volatile creates a memory barrier, preventing instruction reordering
   - Ensures all writes before a volatile write are visible to all threads after reading the volatile variable


### 7. Wait, Notify, and NotifyAll Methods


## Video Explanation
[wait(), notify() and notifyAll() methods | Interthread Communication | Multithreading in Java](https://www.youtube.com/watch?v=vxHYQ0emXSU) - 7:07 minutes

## Java Example

```java
public class WaitNotifyExample {
    public static void main(String[] args) {
        System.out.println("Wait, Notify, and NotifyAll Example");
        
        // Example 1: Producer-Consumer pattern with wait() and notify()
        System.out.println("\n--- Example 1: Producer-Consumer with wait() and notify() ---");
        
        final SharedResource sharedResource = new SharedResource();
        
        Thread producerThread = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    sharedResource.produce(i);
                    Thread.sleep(1000); // Simulate work
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Producer");
        
        Thread consumerThread = new Thread(() -> {
            try {
                for (int i = 1; i <= 5; i++) {
                    sharedResource.consume();
                    Thread.sleep(1500); // Simulate work
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "Consumer");
        
        producerThread.start();
        consumerThread.start();
        
        try {
            producerThread.join();
            consumerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Example 2: Multiple consumers with notifyAll()
        System.out.println("\n--- Example 2: Multiple Consumers with notifyAll() ---");
        
        final MessageQueue messageQueue = new MessageQueue(5);
        
        Thread messageProducer = new Thread(() -> {
            try {
                for (int i = 1; i <= 10; i++) {
                    messageQueue.put("Message-" + i);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "MessageProducer");
        
        Thread[] consumers = new Thread[3];
        for (int i = 0; i < consumers.length; i++) {
            final int consumerId = i + 1;
            consumers[i] = new Thread(() -> {
                try {
                    for (int j = 0; j < 3; j++) {
                        String message = messageQueue.take();
                        System.out.println("Consumer-" + consumerId + " received: " + message);
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }, "Consumer-" + consumerId);
        }
        
        messageProducer.start();
        for (Thread consumer : consumers) {
            consumer.start();
        }
        
        try {
            messageProducer.join();
            for (Thread consumer : consumers) {
                consumer.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

// Example 1: Simple Producer-Consumer with wait() and notify()
class SharedResource {
    private int data;
    private boolean available = false;
    
    // Producer method
    public synchronized void produce(int value) throws InterruptedException {
        // Wait while data is available (not consumed yet)
        while (available) {
            System.out.println(Thread.currentThread().getName() + " waiting for consumer to consume...");
            wait(); // Release lock and wait for notification
        }
        
        // Produce data
        data = value;
        available = true;
        System.out.println(Thread.currentThread().getName() + " produced: " + data);
        
        // Notify waiting consumer
        notify();
    }
    
    // Consumer method
    public synchronized void consume() throws InterruptedException {
        // Wait while no data is available
        while (!available) {
            System.out.println(Thread.currentThread().getName() + " waiting for producer to produce...");
            wait(); // Release lock and wait for notification
        }
        
        // Consume data
        System.out.println(Thread.currentThread().getName() + " consumed: " + data);
        available = false;
        
        // Notify waiting producer
        notify();
    }
}

// Example 2: Bounded buffer with multiple consumers using notifyAll()
class MessageQueue {
    private final String[] buffer;
    private final int capacity;
    private int count = 0;
    private int putIndex = 0;
    private int takeIndex = 0;
    
    public MessageQueue(int capacity) {
        this.capacity = capacity;
        this.buffer = new String[capacity];
    }
    
    // Producer method
    public synchronized void put(String message) throws InterruptedException {
        // Wait if buffer is full
        while (count == capacity) {
            System.out.println(Thread.currentThread().getName() + " waiting: Buffer full");
            wait();
        }
        
        // Add message to buffer
        buffer[putIndex] = message;
        putIndex = (putIndex + 1) % capacity;
        count++;
        
        System.out.println(Thread.currentThread().getName() + " added: " + message + " (Buffer size: " + count + ")");
        
        // Notify all waiting consumers
        notifyAll();
    }
    
    // Consumer method
    public synchronized String take() throws InterruptedException {
        // Wait if buffer is empty
        while (count == 0) {
            System.out.println(Thread.currentThread().getName() + " waiting: Buffer empty");
            wait();
        }
        
        // Remove message from buffer
        String message = buffer[takeIndex];
        takeIndex = (takeIndex + 1) % capacity;
        count--;
        
        System.out.println(Thread.currentThread().getName() + " removed message (Buffer size: " + count + ")");
        
        // Notify all waiting producers and other consumers
        notifyAll();
        
        return message;
    }
}
```

## Key Points

1. **Purpose of wait(), notify(), and notifyAll()**:
   - Enable threads to communicate with each other
   - Allow threads to coordinate their activities
   - Implement producer-consumer patterns and other synchronization scenarios

2. **wait() Method**:
   - Causes the current thread to release the lock and wait until another thread invokes notify() or notifyAll()
   - Must be called from within a synchronized context (method or block)
   - Can throw InterruptedException if the waiting thread is interrupted
   - Can specify a timeout period (overloaded versions)

3. **notify() Method**:
   - Wakes up a single thread waiting on the object's monitor
   - The awakened thread will not be able to proceed until the current thread releases the lock
   - If multiple threads are waiting, the JVM arbitrarily chooses one to wake up
   - Must be called from within a synchronized context

4. **notifyAll() Method**:
   - Wakes up all threads waiting on the object's monitor
   - Each awakened thread will compete for the lock when it becomes available
   - More predictable than notify() when multiple threads might be waiting for different conditions
   - Must be called from within a synchronized context

5. **Common Patterns**:
   - **Producer-Consumer**: Producers wait when buffer is full, consumers wait when buffer is empty
   - **Bounded Buffer**: Fixed-size queue with producers and consumers
   - **Reader-Writer**: Multiple readers can access data simultaneously, but writers need exclusive access

6. **Best Practices**:
   - Always call wait() in a loop that checks the condition
   - Prefer notifyAll() over notify() unless you're certain only one thread should be awakened
   - Be careful of spurious wakeups (when a thread wakes up without being notified)
   - Avoid nested synchronized blocks to prevent deadlocks

7. **Potential Issues**:
   - **Deadlock**: Threads waiting for each other indefinitely
   - **Livelock**: Threads continuously change state without making progress
   - **Starvation**: Some threads never get a chance to execute
   - **Lost Notifications**: notify() called before wait(), causing the notification to be lost

8. **Alternatives in Modern Java**:
   - `java.util.concurrent.locks.Condition` for more flexible signaling
   - `BlockingQueue` implementations for producer-consumer patterns
   - `CountDownLatch`, `CyclicBarrier`, and `Semaphore` for coordination
   - `CompletableFuture` for asynchronous programming


### 8. Thread Join Method


## Video Explanation
[JOIN Method OR Waiting for Threads to finish (7:35 minutes)](https://www.youtube.com/watch?v=m_Zgo7pAfEU)

## Java Example

```java
public class ThreadJoinExample {
    public static void main(String[] args) {
        System.out.println("Thread Join Method Example");
        
        // Example 1: Basic Thread Join
        System.out.println("\n--- Example 1: Basic Thread Join ---");
        
        Thread thread1 = new Thread(() -> {
            System.out.println("Thread 1 started");
            try {
                // Simulate some work
                for (int i = 1; i <= 5; i++) {
                    System.out.println("Thread 1 executing step " + i);
                    Thread.sleep(500);
                }
                System.out.println("Thread 1 completed");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        Thread thread2 = new Thread(() -> {
            System.out.println("Thread 2 started");
            try {
                // Simulate some work
                for (int i = 1; i <= 3; i++) {
                    System.out.println("Thread 2 executing step " + i);
                    Thread.sleep(700);
                }
                System.out.println("Thread 2 completed");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        
        thread1.start();
        thread2.start();
        
        try {
            // Main thread waits for thread1 to complete
            System.out.println("Main thread waiting for Thread 1 to complete");
            thread1.join();
            System.out.println("Thread 1 has completed, main thread continues");
            
            // Main thread waits for thread2 to complete
            System.out.println("Main thread waiting for Thread 2 to complete");
            thread2.join();
            System.out.println("Thread 2 has completed, main thread continues");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("Both threads have completed, main thread proceeds");
        
        // Example 2: Join with Timeout
        System.out.println("\n--- Example 2: Join with Timeout ---");
        
        Thread longRunningThread = new Thread(() -> {
            System.out.println("Long running thread started");
            try {
                // Simulate long-running task
                for (int i = 1; i <= 10; i++) {
                    System.out.println("Long running thread step " + i);
                    Thread.sleep(1000);
                }
                System.out.println("Long running thread completed");
            } catch (InterruptedException e) {
                System.out.println("Long running thread was interrupted");
            }
        });
        
        longRunningThread.start();
        
        try {
            // Wait for the thread to complete, but only for 3 seconds
            System.out.println("Main thread waiting for long running thread with timeout of 3 seconds");
            longRunningThread.join(3000);
            
            if (longRunningThread.isAlive()) {
                System.out.println("Timeout reached, long running thread is still running");
                System.out.println("Main thread will continue without waiting further");
            } else {
                System.out.println("Long running thread completed within the timeout period");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Example 3: Multiple Threads with Join
        System.out.println("\n--- Example 3: Multiple Threads with Join ---");
        
        // Create an array of threads
        Thread[] threads = new Thread[5];
        
        for (int i = 0; i < threads.length; i++) {
            final int threadNumber = i + 1;
            threads[i] = new Thread(() -> {
                System.out.println("Worker Thread " + threadNumber + " started");
                try {
                    // Each thread does some work
                    Thread.sleep(1000 + (int)(Math.random() * 2000));
                    System.out.println("Worker Thread " + threadNumber + " completed");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            threads[i].start();
        }
        
        try {
            System.out.println("Main thread waiting for all worker threads to complete");
            // Wait for all threads to complete
            for (Thread thread : threads) {
                thread.join();
            }
            System.out.println("All worker threads have completed, main thread proceeds");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        System.out.println("All examples completed");
    }
}
```

## Key Points

1. **Purpose of Thread.join()**:
   - Allows one thread to wait for the completion of another thread
   - Helps coordinate the execution flow between threads
   - Ensures sequential execution when needed

2. **Basic Syntax**:
   ```java
   // Wait indefinitely for thread to complete
   thread.join();
   
   // Wait for thread to complete with a timeout (in milliseconds)
   thread.join(long millis);
   
   // Wait for thread to complete with a timeout (milliseconds and nanoseconds)
   thread.join(long millis, int nanos);
   ```

3. **How join() Works**:
   - The calling thread enters a waiting state until:
     - The thread being joined completes execution (returns from its run method)
     - The specified timeout expires (if a timeout is provided)
     - The waiting thread is interrupted

4. **Important Characteristics**:
   - join() must be called from outside the thread you want to wait for
   - join() throws InterruptedException if the waiting thread is interrupted
   - After timeout, the thread continues execution even if the joined thread hasn't completed
   - join() can be called multiple times on the same thread

5. **Common Use Cases**:
   - **Sequential Processing**: Ensuring tasks are processed in a specific order
   - **Task Coordination**: Waiting for prerequisite tasks to complete
   - **Thread Pooling**: Waiting for all worker threads to complete
   - **Graceful Shutdown**: Ensuring all threads complete before application exit

6. **Best Practices**:
   - Always handle InterruptedException
   - Consider using timeouts to avoid indefinite waiting
   - Check thread.isAlive() after a timed join to determine if the thread completed
   - For complex coordination, consider higher-level concurrency utilities

7. **Alternatives to join()**:
   - CountDownLatch: Wait for multiple threads to complete a specific phase
   - CyclicBarrier: Synchronize multiple threads at a common point
   - CompletableFuture: For asynchronous task composition
   - ExecutorService.awaitTermination(): For thread pools

8. **Potential Issues**:
   - **Deadlocks**: If threads are waiting for each other in a circular manner
   - **Performance Impact**: Excessive waiting can reduce concurrency benefits
   - **Resource Leaks**: If joined threads never terminate
   - **Responsiveness**: UI threads should avoid indefinite join() calls


### 9. Thread Pools and Executor Service


## Video Explanation
[Java ExecutorService - Part 1 - Introduction (8:02 minutes)](https://www.youtube.com/watch?v=6Oo-9Can3H8)

## Java Example

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolExecutorExample {
    public static void main(String[] args) {
        System.out.println("Thread Pools and Executor Service Example");
        
        // Example 1: Fixed Thread Pool
        System.out.println("\n--- Example 1: Fixed Thread Pool ---");
        // Creates a thread pool with a fixed number of threads (3 in this case)
        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(3);
        
        try {
            // Submit 5 tasks to the fixed thread pool
            for (int i = 1; i <= 5; i++) {
                final int taskId = i;
                fixedThreadPool.submit(() -> {
                    String threadName = Thread.currentThread().getName();
                    System.out.println("Task " + taskId + " is executing on " + threadName);
                    try {
                        // Simulate work
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("Task " + taskId + " completed on " + threadName);
                    return "Result of Task " + taskId;
                });
            }
            
            // Proper shutdown of the executor
            fixedThreadPool.shutdown();
            if (!fixedThreadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("Fixed thread pool did not terminate in the specified time.");
                fixedThreadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            fixedThreadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        // Example 2: Cached Thread Pool
        System.out.println("\n--- Example 2: Cached Thread Pool ---");
        // Creates a thread pool that creates new threads as needed and reuses idle threads
        ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
        
        try {
            // Submit 10 short tasks to the cached thread pool
            for (int i = 1; i <= 10; i++) {
                final int taskId = i;
                cachedThreadPool.submit(() -> {
                    String threadName = Thread.currentThread().getName();
                    System.out.println("Short Task " + taskId + " is executing on " + threadName);
                    try {
                        // Simulate short work
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("Short Task " + taskId + " completed on " + threadName);
                    return null;
                });
            }
            
            // Proper shutdown of the executor
            cachedThreadPool.shutdown();
            if (!cachedThreadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("Cached thread pool did not terminate in the specified time.");
                cachedThreadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            cachedThreadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        // Example 3: Scheduled Thread Pool
        System.out.println("\n--- Example 3: Scheduled Thread Pool ---");
        // Creates a thread pool that can schedule tasks to run after a delay or periodically
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(2);
        
        try {
            // Schedule a task to run after a delay
            System.out.println("Scheduling a task to run after 2 seconds");
            ScheduledFuture<?> delayedTask = scheduledThreadPool.schedule(() -> {
                System.out.println("Delayed task executed after 2 seconds");
                return "Delayed Task Result";
            }, 2, TimeUnit.SECONDS);
            
            // Schedule a task to run periodically
            System.out.println("Scheduling a task to run every 1 second");
            final AtomicInteger counter = new AtomicInteger(0);
            ScheduledFuture<?> periodicTask = scheduledThreadPool.scheduleAtFixedRate(() -> {
                int count = counter.incrementAndGet();
                System.out.println("Periodic task executed, count: " + count);
                if (count >= 3) {
                    System.out.println("Periodic task completed after " + count + " executions");
                    throw new RuntimeException("Stopping periodic task");
                }
            }, 0, 1, TimeUnit.SECONDS);
            
            // Wait for the delayed task to complete
            try {
                String result = (String) delayedTask.get();
                System.out.println("Delayed task result: " + result);
            } catch (ExecutionException e) {
                System.out.println("Delayed task threw an exception: " + e.getCause());
            }
            
            // Wait for the periodic task to complete (or be cancelled)
            try {
                Thread.sleep(5000); // Give time for periodic task to execute a few times
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            // Cancel the periodic task if it's still running
            if (!periodicTask.isDone()) {
                periodicTask.cancel(true);
                System.out.println("Periodic task cancelled");
            }
            
            // Proper shutdown of the executor
            scheduledThreadPool.shutdown();
            if (!scheduledThreadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("Scheduled thread pool did not terminate in the specified time.");
                scheduledThreadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduledThreadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        // Example 4: Custom Thread Pool
        System.out.println("\n--- Example 4: Custom Thread Pool ---");
        // Create a custom thread pool with specific parameters
        ThreadPoolExecutor customThreadPool = new ThreadPoolExecutor(
            2,                      // Core pool size
            5,                      // Maximum pool size
            60, TimeUnit.SECONDS,   // Keep-alive time for idle threads
            new ArrayBlockingQueue<>(10), // Work queue
            new ThreadFactory() {   // Custom thread factory
                private final AtomicInteger threadNumber = new AtomicInteger(1);
                
                @Override
                public Thread newThread(Runnable r) {
                    Thread thread = new Thread(r, "CustomThread-" + threadNumber.getAndIncrement());
                    thread.setDaemon(false);
                    return thread;
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy() // Rejection policy
        );
        
        try {
            // Submit tasks to the custom thread pool
            for (int i = 1; i <= 15; i++) {
                final int taskId = i;
                customThreadPool.submit(() -> {
                    String threadName = Thread.currentThread().getName();
                    System.out.println("Custom Task " + taskId + " is executing on " + threadName);
                    try {
                        // Simulate work
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println("Custom Task " + taskId + " completed on " + threadName);
                    return null;
                });
            }
            
            // Print thread pool statistics
            System.out.println("Custom Thread Pool Statistics:");
            System.out.println("Core Pool Size: " + customThreadPool.getCorePoolSize());
            System.out.println("Current Pool Size: " + customThreadPool.getPoolSize());
            System.out.println("Largest Pool Size: " + customThreadPool.getLargestPoolSize());
            System.out.println("Active Thread Count: " + customThreadPool.getActiveCount());
            System.out.println("Task Count: " + customThreadPool.getTaskCount());
            System.out.println("Completed Task Count: " + customThreadPool.getCompletedTaskCount());
            System.out.println("Queue Size: " + customThreadPool.getQueue().size());
            
            // Proper shutdown of the executor
            customThreadPool.shutdown();
            if (!customThreadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                System.out.println("Custom thread pool did not terminate in the specified time.");
                customThreadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            customThreadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        System.out.println("\nAll thread pool examples completed");
    }
}
```

## Key Points

1. **Thread Pool Concept**:
   - A collection of worker threads that efficiently execute asynchronous tasks
   - Reuses existing threads instead of creating new ones for each task
   - Reduces the overhead of thread creation and destruction
   - Manages the lifecycle of worker threads

2. **Types of Thread Pools in Java**:
   - **Fixed Thread Pool**: Fixed number of threads; good for CPU-bound tasks
     ```java
     ExecutorService fixedThreadPool = Executors.newFixedThreadPool(nThreads);
     ```
   - **Cached Thread Pool**: Creates new threads as needed, reuses idle threads; good for I/O-bound tasks
     ```java
     ExecutorService cachedThreadPool = Executors.newCachedThreadPool();
     ```
   - **Scheduled Thread Pool**: Executes tasks after a delay or periodically
     ```java
     ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(corePoolSize);
     ```
   - **Single Thread Executor**: Uses a single worker thread; guarantees sequential execution
     ```java
     ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
     ```
   - **Work Stealing Pool**: Uses multiple queues to reduce contention; good for recursive tasks
     ```java
     ExecutorService workStealingPool = Executors.newWorkStealingPool();
     ```

3. **ExecutorService Interface**:
   - Core interface for working with thread pools
   - Methods for submitting tasks: `execute()`, `submit()`, `invokeAll()`, `invokeAny()`
   - Methods for managing lifecycle: `shutdown()`, `shutdownNow()`, `awaitTermination()`

4. **Task Submission Methods**:
   - `execute(Runnable)`: Executes a task with no return value
   - `submit(Runnable)`: Returns a Future representing the task's completion
   - `submit(Callable)`: Returns a Future representing the task's result
   - `invokeAll(Collection<Callable>)`: Executes all tasks and returns a list of Futures
   - `invokeAny(Collection<Callable>)`: Executes tasks and returns the result of one that completes successfully

5. **Proper Shutdown Process**:
   - `shutdown()`: Initiates an orderly shutdown (no new tasks accepted, existing tasks continue)
   - `shutdownNow()`: Attempts to stop all executing tasks and returns a list of tasks waiting to be executed
   - `awaitTermination(long timeout, TimeUnit unit)`: Blocks until all tasks complete or the timeout occurs
   - Always use a combination of these methods for proper cleanup

6. **Custom Thread Pool Configuration**:
   - `ThreadPoolExecutor` allows fine-grained control over thread pool behavior
   - Parameters: core pool size, max pool size, keep-alive time, work queue, thread factory, rejection policy
   - Rejection policies: AbortPolicy, CallerRunsPolicy, DiscardPolicy, DiscardOldestPolicy

7. **Best Practices**:
   - Choose the right thread pool type for your workload
   - Set appropriate pool sizes based on CPU cores and task nature
   - Always shut down executors properly to avoid resource leaks
   - Use try-finally blocks to ensure shutdown even in case of exceptions
   - Monitor thread pool metrics in production environments
   - Consider using thread factories for custom thread naming and properties

8. **Common Pitfalls**:
   - Thread leakage if executors are not shut down
   - Thread starvation if pool size is too small
   - Excessive memory usage if pool size is too large
   - Deadlocks if tasks depend on each other within the same pool
   - Using blocking operations in tasks without proper timeout handling


### 10. Callable and Future


## Video Explanation
[How to use Executors with Callable in Java Executors (7:05 minutes)](https://www.youtube.com/watch?v=HNmJUMC4aAo)

## Java Example

```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CallableFutureExample {
    public static void main(String[] args) {
        System.out.println("Callable and Future Example");
        
        // Example 1: Basic Callable and Future
        System.out.println("\n--- Example 1: Basic Callable and Future ---");
        
        // Create an executor service with a fixed thread pool of 3 threads
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        
        // Create a Callable task that returns a result
        Callable<Integer> callableTask = () -> {
            System.out.println("Callable task is running on thread: " + Thread.currentThread().getName());
            // Simulate some computation
            Thread.sleep(2000);
            return 42; // Return a result
        };
        
        try {
            // Submit the callable task to the executor service and get a Future
            System.out.println("Submitting callable task...");
            Future<Integer> future = executorService.submit(callableTask);
            
            // Check if the task is done
            System.out.println("Is task done? " + future.isDone());
            
            // Get the result of the callable task (this will block until the result is available)
            System.out.println("Waiting for result...");
            Integer result = future.get();
            System.out.println("Task completed with result: " + result);
            
            // Check if the task is done after getting the result
            System.out.println("Is task done? " + future.isDone());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        
        // Example 2: Multiple Callable Tasks
        System.out.println("\n--- Example 2: Multiple Callable Tasks ---");
        
        // Create a list of callable tasks
        List<Callable<String>> callableTasks = new ArrayList<>();
        
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            callableTasks.add(() -> {
                System.out.println("Task " + taskId + " is running on thread: " + Thread.currentThread().getName());
                // Simulate varying computation times
                Thread.sleep(1000 * taskId);
                return "Result of Task " + taskId;
            });
        }
        
        try {
            // Submit all tasks and get a list of futures
            System.out.println("Submitting multiple callable tasks...");
            List<Future<String>> futures = executorService.invokeAll(callableTasks);
            
            // Process the results as they become available
            for (int i = 0; i < futures.size(); i++) {
                Future<String> future = futures.get(i);
                String result = future.get();
                System.out.println("Task " + (i + 1) + " completed with result: " + result);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        
        // Example 3: Callable with Timeout
        System.out.println("\n--- Example 3: Callable with Timeout ---");
        
        Callable<String> longRunningTask = () -> {
            System.out.println("Long running task started on thread: " + Thread.currentThread().getName());
            // Simulate a task that takes a long time
            Thread.sleep(5000);
            return "Long running task completed";
        };
        
        try {
            // Submit the long running task
            System.out.println("Submitting long running task...");
            Future<String> future = executorService.submit(longRunningTask);
            
            try {
                // Try to get the result with a timeout of 2 seconds
                System.out.println("Waiting for result with timeout of 2 seconds...");
                String result = future.get(2, TimeUnit.SECONDS);
                System.out.println("Task completed with result: " + result);
            } catch (TimeoutException e) {
                System.out.println("Task timed out after 2 seconds!");
                
                // Cancel the task
                boolean cancelled = future.cancel(true);
                System.out.println("Task cancelled: " + cancelled);
                System.out.println("Is task cancelled? " + future.isCancelled());
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        
        // Example 4: invokeAny - Get Result from First Completed Task
        System.out.println("\n--- Example 4: invokeAny - Get Result from First Completed Task ---");
        
        List<Callable<String>> competingTasks = new ArrayList<>();
        
        // Create tasks with different execution times
        for (int i = 1; i <= 3; i++) {
            final int taskId = i;
            final int sleepTime = (4 - i) * 1000; // Task 1 takes 3s, Task 2 takes 2s, Task 3 takes 1s
            
            competingTasks.add(() -> {
                System.out.println("Competing Task " + taskId + " started on thread: " + Thread.currentThread().getName() + 
                                  " (will take " + sleepTime/1000 + "s)");
                Thread.sleep(sleepTime);
                return "Result from Competing Task " + taskId;
            });
        }
        
        try {
            // Execute tasks and get the result of the fastest one
            System.out.println("Executing competing tasks with invokeAny...");
            String fastestResult = executorService.invokeAny(competingTasks);
            System.out.println("Fastest task completed with result: " + fastestResult);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        
        // Shutdown the executor service
        System.out.println("\nShutting down executor service...");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        System.out.println("All examples completed");
    }
}
```

## Key Points

1. **Callable Interface**:
   - Represents a task that returns a result and may throw an exception
   - Defined in `java.util.concurrent` package
   - Has a single method: `V call() throws Exception`
   - Similar to Runnable but can return a value and throw checked exceptions

2. **Differences between Callable and Runnable**:
   | Feature | Callable | Runnable |
   |---------|----------|----------|
   | Return Value | Can return a result | Cannot return a result (void) |
   | Exceptions | Can throw checked exceptions | Cannot throw checked exceptions |
   | Method | `V call() throws Exception` | `void run()` |
   | Introduction | Java 1.5 (as part of concurrency utilities) | Java 1.0 |
   | Usage with Executors | `submit(Callable)` returns Future | `execute(Runnable)` or `submit(Runnable)` |

3. **Future Interface**:
   - Represents the result of an asynchronous computation
   - Provides methods to check if the computation is complete, wait for completion, and retrieve the result
   - Key methods:
     - `V get()`: Waits if necessary for the computation to complete, then retrieves the result
     - `V get(long timeout, TimeUnit unit)`: Waits for the result with a timeout
     - `boolean cancel(boolean mayInterruptIfRunning)`: Attempts to cancel execution
     - `boolean isCancelled()`: Returns true if the task was cancelled
     - `boolean isDone()`: Returns true if the task completed (normally, exceptionally, or cancelled)

4. **ExecutorService Methods for Callable Tasks**:
   - `Future<T> submit(Callable<T> task)`: Submits a callable task for execution and returns a Future
   - `<T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks)`: Executes all tasks and returns a list of Futures
   - `<T> T invokeAny(Collection<? extends Callable<T>> tasks)`: Executes tasks and returns the result of one that completes successfully

5. **Common Use Cases**:
   - **Asynchronous Computation**: When you need to perform a computation in the background and retrieve the result later
   - **Parallel Processing**: When you need to execute multiple tasks in parallel and collect their results
   - **Time-Limited Operations**: When you need to set a timeout for an operation
   - **Cancellable Operations**: When you need the ability to cancel long-running operations

6. **Best Practices**:
   - Always handle exceptions that might be thrown by `get()` methods
   - Use timeouts with `get(long, TimeUnit)` to avoid blocking indefinitely
   - Always shut down executor services properly
   - Consider using higher-level abstractions like CompletableFuture for complex asynchronous operations
   - Check `isDone()` or `isCancelled()` before calling `get()` if you want to avoid blocking

7. **Potential Issues**:
   - **Blocking Operations**: `get()` blocks until the result is available
   - **Thread Interruption**: Callable tasks should handle InterruptedException properly
   - **Resource Leaks**: Executor services must be shut down properly
   - **Deadlocks**: Can occur if tasks depend on each other in a circular manner


### 11. Locks (ReentrantLock and ReadWriteLock)


## Video Explanation
[Java ReentrantLock - fairness, tryLock and more (6:00 minutes)](https://www.youtube.com/watch?v=ahBC69_iyk4)

## Java Example

```java
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LocksExample {
    public static void main(String[] args) {
        System.out.println("Locks Example (ReentrantLock and ReadWriteLock)");
        
        // Example 1: ReentrantLock
        System.out.println("\n--- Example 1: ReentrantLock ---");
        
        // Create a shared counter with ReentrantLock
        Counter reentrantCounter = new Counter();
        
        // Create a thread pool with 5 threads
        ExecutorService executorService1 = Executors.newFixedThreadPool(5);
        
        // Submit 10 increment tasks
        for (int i = 0; i < 10; i++) {
            executorService1.submit(() -> {
                for (int j = 0; j < 100; j++) {
                    reentrantCounter.increment();
                }
            });
        }
        
        // Submit 10 decrement tasks
        for (int i = 0; i < 10; i++) {
            executorService1.submit(() -> {
                for (int j = 0; j < 50; j++) {
                    reentrantCounter.decrement();
                }
            });
        }
        
        // Shutdown the executor service and wait for tasks to complete
        executorService1.shutdown();
        try {
            executorService1.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Final counter value: " + reentrantCounter.getValue());
        
        // Example 2: ReentrantLock with tryLock and fairness
        System.out.println("\n--- Example 2: ReentrantLock with tryLock and fairness ---");
        
        // Create a shared resource with fair ReentrantLock
        SharedResource fairResource = new SharedResource(true);
        
        // Create a thread pool with 3 threads
        ExecutorService executorService2 = Executors.newFixedThreadPool(3);
        
        // Submit 5 tasks that try to access the resource
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            executorService2.submit(() -> {
                fairResource.accessResourceWithTimeout(taskId, 1000);
                try {
                    // Add some delay between tasks
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Shutdown the executor service and wait for tasks to complete
        executorService2.shutdown();
        try {
            executorService2.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Example 3: ReadWriteLock
        System.out.println("\n--- Example 3: ReadWriteLock ---");
        
        // Create a cache with ReadWriteLock
        ConcurrentCache<String, String> cache = new ConcurrentCache<>();
        
        // Create a thread pool with 10 threads
        ExecutorService executorService3 = Executors.newFixedThreadPool(10);
        
        // Submit 20 read tasks
        for (int i = 0; i < 20; i++) {
            final int taskId = i % 5; // Use 5 different keys
            executorService3.submit(() -> {
                String key = "key" + taskId;
                String value = cache.get(key);
                System.out.println(Thread.currentThread().getName() + " read: " + key + " = " + value);
            });
        }
        
        // Submit 5 write tasks
        for (int i = 0; i < 5; i++) {
            final int taskId = i;
            executorService3.submit(() -> {
                String key = "key" + taskId;
                String value = "value" + taskId + "-" + System.currentTimeMillis();
                cache.put(key, value);
                System.out.println(Thread.currentThread().getName() + " wrote: " + key + " = " + value);
            });
        }
        
        // Submit 10 more read tasks
        for (int i = 0; i < 10; i++) {
            final int taskId = i % 5; // Use 5 different keys
            executorService3.submit(() -> {
                String key = "key" + taskId;
                String value = cache.get(key);
                System.out.println(Thread.currentThread().getName() + " read: " + key + " = " + value);
            });
        }
        
        // Shutdown the executor service and wait for tasks to complete
        executorService3.shutdown();
        try {
            executorService3.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Print cache statistics
        System.out.println("Cache statistics: " + cache.getStatistics());
        
        System.out.println("All examples completed");
    }
    
    // Example 1: Counter with ReentrantLock
    static class Counter {
        private final Lock lock = new ReentrantLock();
        private int count = 0;
        
        public void increment() {
            lock.lock();
            try {
                count++;
            } finally {
                lock.unlock();
            }
        }
        
        public void decrement() {
            lock.lock();
            try {
                count--;
            } finally {
                lock.unlock();
            }
        }
        
        public int getValue() {
            lock.lock();
            try {
                return count;
            } finally {
                lock.unlock();
            }
        }
    }
    
    // Example 2: Shared resource with fair ReentrantLock
    static class SharedResource {
        private final ReentrantLock lock;
        
        public SharedResource(boolean fair) {
            this.lock = new ReentrantLock(fair);
        }
        
        public void accessResourceWithTimeout(int taskId, long timeoutMs) {
            boolean acquired = false;
            try {
                System.out.println("Task " + taskId + " trying to acquire lock...");
                acquired = lock.tryLock(timeoutMs, TimeUnit.MILLISECONDS);
                
                if (acquired) {
                    System.out.println("Task " + taskId + " acquired lock, hold count: " + lock.getHoldCount());
                    
                    // Simulate some work with the resource
                    Thread.sleep(500);
                    
                    // Demonstrate reentrant behavior
                    nestedMethod(taskId);
                } else {
                    System.out.println("Task " + taskId + " failed to acquire lock within timeout");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Task " + taskId + " was interrupted while waiting for lock");
            } finally {
                if (acquired) {
                    lock.unlock();
                    System.out.println("Task " + taskId + " released lock");
                }
            }
        }
        
        private void nestedMethod(int taskId) {
            // Reentrant lock allows the same thread to acquire the lock again
            lock.lock();
            try {
                System.out.println("Task " + taskId + " entered nested method, hold count: " + lock.getHoldCount());
                // Simulate some work
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
                System.out.println("Task " + taskId + " exited nested method, hold count: " + lock.getHoldCount());
            }
        }
    }
    
    // Example 3: Cache with ReadWriteLock
    static class ConcurrentCache<K, V> {
        private final Map<K, V> cache = new HashMap<>();
        private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
        private final Lock readLock = rwLock.readLock();
        private final Lock writeLock = rwLock.writeLock();
        
        private int reads = 0;
        private int writes = 0;
        
        public V get(K key) {
            readLock.lock();
            try {
                reads++;
                // Simulate some read operation time
                Thread.sleep(100);
                return cache.get(key);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return null;
            } finally {
                readLock.unlock();
            }
        }
        
        public void put(K key, V value) {
            writeLock.lock();
            try {
                writes++;
                // Simulate some write operation time
                Thread.sleep(300);
                cache.put(key, value);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                writeLock.unlock();
            }
        }
        
        public String getStatistics() {
            readLock.lock();
            try {
                return "Size: " + cache.size() + ", Reads: " + reads + ", Writes: " + writes;
            } finally {
                readLock.unlock();
            }
        }
    }
}
```

## Key Points

1. **ReentrantLock vs Synchronized**:
   - ReentrantLock provides more flexibility than synchronized blocks
   - Features include fairness, non-blocking attempts to acquire lock (tryLock), interruptible lock acquisition, and timed lock acquisition
   - ReentrantLock requires explicit locking and unlocking (try-finally blocks recommended)
   - ReentrantLock allows lock interrogation (checking if a lock is held)

2. **ReentrantLock Features**:
   - **Reentrant**: The same thread can acquire the lock multiple times (must release it the same number of times)
   - **Fairness**: Can be configured to use a fair ordering policy (longest-waiting thread gets the lock first)
   - **Interruptible**: Lock acquisition can be interrupted if thread is interrupted
   - **Timed**: Can attempt to acquire lock with a timeout
   - **Non-blocking**: Can check if lock is available without blocking (tryLock)

3. **ReadWriteLock Interface**:
   - Provides a pair of locks: one for read-only operations and one for write operations
   - Multiple threads can hold the read lock simultaneously (shared lock)
   - Only one thread can hold the write lock (exclusive lock)
   - No thread can hold the read lock while another thread holds the write lock
   - Optimizes for scenarios with frequent reads and infrequent writes

4. **ReentrantReadWriteLock Implementation**:
   - Java's implementation of ReadWriteLock interface
   - Allows downgrading from write lock to read lock (acquire write, then read, then release write)
   - Does not allow upgrading from read lock to write lock (would cause deadlock)
   - Can be configured for fairness like ReentrantLock
   - Provides methods to get number of readers and writers

5. **When to Use ReentrantLock**:
   - When you need more control over locking than synchronized provides
   - When you need timed or interruptible lock acquisition
   - When you need fairness guarantees
   - When you need to try acquiring a lock without blocking
   - When you need to know the state of the lock

6. **When to Use ReadWriteLock**:
   - When you have a resource that is frequently read but infrequently modified
   - When you want to allow concurrent reads for better throughput
   - When read operations are long-running and you don't want to block other readers
   - Examples: caches, configuration data, collections that are read frequently but updated rarely

7. **Best Practices**:
   - Always release locks in a finally block to prevent lock leakage
   - Keep lock scope as small as possible
   - Avoid nested locks when possible to prevent deadlocks
   - Consider using higher-level concurrency utilities when appropriate
   - Document your locking strategy clearly
   - Be aware of lock ordering to prevent deadlocks

8. **Potential Issues**:
   - **Deadlocks**: Can occur if locks are acquired in different orders
   - **Livelocks**: Threads keep changing state but make no progress
   - **Priority Inversion**: Lower priority thread holds a lock needed by higher priority thread
   - **Lock Leakage**: Failing to release locks, especially in error conditions
   - **Performance Overhead**: Excessive locking can reduce concurrency


### 12. Atomic Variables (AtomicInteger, AtomicReference)


## Video Explanation
[Overview of Java Atomic Operations & Variables (8:43 minutes)](https://www.youtube.com/watch?v=92-L61Wi7G4)

## Java Example

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicVariablesExample {
    public static void main(String[] args) {
        System.out.println("Atomic Variables Example");
        
        // Example 1: AtomicInteger
        System.out.println("\n--- Example 1: AtomicInteger ---");
        
        // Create a shared counter with AtomicInteger
        AtomicCounter atomicCounter = new AtomicCounter();
        
        // Create a thread pool with 5 threads
        ExecutorService executorService1 = Executors.newFixedThreadPool(5);
        
        // Submit 10 increment tasks
        for (int i = 0; i < 10; i++) {
            executorService1.submit(() -> {
                for (int j = 0; j < 1000; j++) {
                    atomicCounter.increment();
                }
            });
        }
        
        // Submit 5 decrement tasks
        for (int i = 0; i < 5; i++) {
            executorService1.submit(() -> {
                for (int j = 0; j < 1000; j++) {
                    atomicCounter.decrement();
                }
            });
        }
        
        // Shutdown the executor service and wait for tasks to complete
        executorService1.shutdown();
        try {
            executorService1.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Final counter value: " + atomicCounter.getValue());
        System.out.println("Number of updates: " + atomicCounter.getUpdates());
        
        // Example 2: AtomicReference
        System.out.println("\n--- Example 2: AtomicReference ---");
        
        // Create a shared user object with AtomicReference
        UserProfile userProfile = new UserProfile();
        
        // Create a thread pool with 3 threads
        ExecutorService executorService2 = Executors.newFixedThreadPool(3);
        
        // Submit tasks to update different fields of the user profile
        executorService2.submit(() -> {
            for (int i = 0; i < 100; i++) {
                userProfile.updateName("User" + i);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        executorService2.submit(() -> {
            for (int i = 0; i < 100; i++) {
                userProfile.updateEmail("user" + i + "@example.com");
                try {
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        executorService2.submit(() -> {
            for (int i = 0; i < 100; i++) {
                userProfile.updateAge(20 + i % 50);
                try {
                    Thread.sleep(12);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        // Shutdown the executor service and wait for tasks to complete
        executorService2.shutdown();
        try {
            executorService2.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Final user profile: " + userProfile.getUser());
        System.out.println("Number of updates: " + userProfile.getUpdates());
        
        // Example 3: AtomicBoolean for flag-based coordination
        System.out.println("\n--- Example 3: AtomicBoolean for flag-based coordination ---");
        
        // Create a task processor with AtomicBoolean
        TaskProcessor taskProcessor = new TaskProcessor();
        
        // Create a thread pool with 2 threads
        ExecutorService executorService3 = Executors.newFixedThreadPool(2);
        
        // Submit a task to process tasks
        executorService3.submit(() -> {
            System.out.println("Task processor started");
            for (int i = 1; i <= 5; i++) {
                boolean processed = taskProcessor.processTask("Task " + i);
                System.out.println("Task " + i + " processed: " + processed);
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        
        // Submit a task to stop the processor after some time
        executorService3.submit(() -> {
            try {
                System.out.println("Waiting to stop the task processor...");
                Thread.sleep(1000);
                taskProcessor.stopProcessing();
                System.out.println("Task processor stopped");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });
        
        // Shutdown the executor service and wait for tasks to complete
        executorService3.shutdown();
        try {
            executorService3.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Example 4: AtomicInteger with compareAndSet
        System.out.println("\n--- Example 4: AtomicInteger with compareAndSet ---");
        
        // Create a shared state with AtomicInteger
        StateManager stateManager = new StateManager();
        
        // Create a thread pool with 4 threads
        ExecutorService executorService4 = Executors.newFixedThreadPool(4);
        
        // Submit tasks to transition the state
        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            executorService4.submit(() -> {
                boolean success = false;
                
                // Try to transition from INIT to PROCESSING
                if (taskId % 3 == 0) {
                    success = stateManager.transitionState(StateManager.INIT, StateManager.PROCESSING);
                    System.out.println("Thread " + Thread.currentThread().getName() + 
                                      " tried to transition from INIT to PROCESSING: " + success);
                }
                // Try to transition from PROCESSING to COMPLETED
                else if (taskId % 3 == 1) {
                    success = stateManager.transitionState(StateManager.PROCESSING, StateManager.COMPLETED);
                    System.out.println("Thread " + Thread.currentThread().getName() + 
                                      " tried to transition from PROCESSING to COMPLETED: " + success);
                }
                // Try to transition from COMPLETED to FAILED (invalid transition)
                else {
                    success = stateManager.transitionState(StateManager.COMPLETED, StateManager.FAILED);
                    System.out.println("Thread " + Thread.currentThread().getName() + 
                                      " tried to transition from COMPLETED to FAILED: " + success);
                }
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Shutdown the executor service and wait for tasks to complete
        executorService4.shutdown();
        try {
            executorService4.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Final state: " + stateManager.getStateName());
        
        System.out.println("All examples completed");
    }
    
    // Example 1: Counter with AtomicInteger
    static class AtomicCounter {
        private final AtomicInteger count = new AtomicInteger(0);
        private final AtomicInteger updates = new AtomicInteger(0);
        
        public void increment() {
            count.incrementAndGet(); // Atomic increment
            updates.incrementAndGet();
        }
        
        public void decrement() {
            count.decrementAndGet(); // Atomic decrement
            updates.incrementAndGet();
        }
        
        public int getValue() {
            return count.get();
        }
        
        public int getUpdates() {
            return updates.get();
        }
    }
    
    // Example 2: User profile with AtomicReference
    static class UserProfile {
        // Immutable user class
        static class User {
            private final String name;
            private final String email;
            private final int age;
            
            public User(String name, String email, int age) {
                this.name = name;
                this.email = email;
                this.age = age;
            }
            
            // Create a new User with updated name
            public User withName(String newName) {
                return new User(newName, this.email, this.age);
            }
            
            // Create a new User with updated email
            public User withEmail(String newEmail) {
                return new User(this.name, newEmail, this.age);
            }
            
            // Create a new User with updated age
            public User withAge(int newAge) {
                return new User(this.name, this.email, newAge);
            }
            
            @Override
            public String toString() {
                return "User{name='" + name + "', email='" + email + "', age=" + age + "}";
            }
        }
        
        private final AtomicReference<User> userRef = new AtomicReference<>(new User("Default", "default@example.com", 0));
        private final AtomicInteger updates = new AtomicInteger(0);
        
        public void updateName(String name) {
            userRef.updateAndGet(user -> user.withName(name));
            updates.incrementAndGet();
        }
        
        public void updateEmail(String email) {
            userRef.updateAndGet(user -> user.withEmail(email));
            updates.incrementAndGet();
        }
        
        public void updateAge(int age) {
            userRef.updateAndGet(user -> user.withAge(age));
            updates.incrementAndGet();
        }
        
        public User getUser() {
            return userRef.get();
        }
        
        public int getUpdates() {
            return updates.get();
        }
    }
    
    // Example 3: Task processor with AtomicBoolean
    static class TaskProcessor {
        private final AtomicBoolean running = new AtomicBoolean(true);
        
        public boolean processTask(String taskName) {
            if (running.get()) {
                System.out.println("Processing " + taskName);
                // Simulate task processing
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return true;
            } else {
                System.out.println("Skipping " + taskName + " as processor is stopped");
                return false;
            }
        }
        
        public void stopProcessing() {
            running.set(false);
        }
        
        public boolean isRunning() {
            return running.get();
        }
    }
    
    // Example 4: State manager with AtomicInteger and compareAndSet
    static class StateManager {
        // State constants
        public static final int INIT = 0;
        public static final int PROCESSING = 1;
        public static final int COMPLETED = 2;
        public static final int FAILED = 3;
        
        private final AtomicInteger state = new AtomicInteger(INIT);
        
        // Transition from one state to another atomically
        public boolean transitionState(int expectedState, int newState) {
            return state.compareAndSet(expectedState, newState);
        }
        
        public int getState() {
            return state.get();
        }
        
        public String getStateName() {
            switch (state.get()) {
                case INIT: return "INIT";
                case PROCESSING: return "PROCESSING";
                case COMPLETED: return "COMPLETED";
                case FAILED: return "FAILED";
                default: return "UNKNOWN";
            }
        }
    }
}
```

## Key Points

1. **Atomic Variables Overview**:
   - Atomic variables are classes from the `java.util.concurrent.atomic` package
   - They provide thread-safe operations without using locks
   - They use low-level atomic hardware features like compare-and-swap (CAS)
   - Common atomic classes include AtomicInteger, AtomicLong, AtomicBoolean, and AtomicReference

2. **Key Atomic Classes**:
   - **AtomicInteger**: Thread-safe integer with atomic operations
   - **AtomicLong**: Thread-safe long with atomic operations
   - **AtomicBoolean**: Thread-safe boolean with atomic operations
   - **AtomicReference**: Thread-safe reference to an object
   - **AtomicIntegerArray**, **AtomicLongArray**, **AtomicReferenceArray**: Thread-safe arrays
   - **AtomicMarkableReference**: Reference with a boolean mark
   - **AtomicStampedReference**: Reference with an integer stamp (for ABA problem)

3. **Common Operations**:
   - **get()**: Get the current value
   - **set(newValue)**: Set to a new value
   - **getAndSet(newValue)**: Atomically set to a new value and return the old value
   - **compareAndSet(expect, update)**: Atomically set if the current value equals the expected value
   - **incrementAndGet()**, **decrementAndGet()**: Increment/decrement and get the new value
   - **getAndIncrement()**, **getAndDecrement()**: Get the current value and increment/decrement
   - **addAndGet(delta)**, **getAndAdd(delta)**: Add a delta and get the new/old value
   - **updateAndGet(function)**, **getAndUpdate(function)**: Apply a function and get the new/old value

4. **Advantages of Atomic Variables**:
   - **Performance**: Often faster than using locks for simple operations
   - **Deadlock-Free**: No risk of deadlocks since no locks are used
   - **Fine-Grained**: Operate on individual variables rather than code blocks
   - **Progress Guarantee**: Provide lock-free progress guarantee
   - **Composable**: Can be combined with other concurrency mechanisms

5. **Atomic Variables vs. Volatile**:
   - **Volatile**: Ensures visibility of changes across threads but doesn't support atomic compound operations
   - **Atomic Variables**: Support both visibility and atomic compound operations (read-modify-write)
   - Use volatile for simple flags where you only need visibility
   - Use atomic variables when you need atomic compound operations

6. **Atomic Variables vs. Synchronized**:
   - **Synchronized**: Provides mutual exclusion for code blocks, can protect multiple variables
   - **Atomic Variables**: Provide atomic operations on individual variables
   - Synchronized can cause thread contention and blocking
   - Atomic variables use non-blocking algorithms (CAS)

7. **Common Use Cases**:
   - **Counters**: Thread-safe counters without locks
   - **Flags**: Thread-safe boolean flags for coordination
   - **Statistics**: Collecting statistics in multithreaded applications
   - **State Management**: Managing state transitions atomically
   - **Caching**: Building lock-free caches

8. **Potential Issues**:
   - **ABA Problem**: When a value changes from A to B and back to A, compareAndSet might not detect the change
   - **Limited Composability**: Individual operations are atomic, but sequences of operations are not
   - **Contention**: High contention can lead to many failed CAS operations and retries
   - **Memory Overhead**: Atomic variables have higher memory footprint than primitives


### 13. Semaphore


## Video Explanation
[Introduction to Java Semaphores - Java Programming (9:39 minutes)](https://www.youtube.com/watch?v=g19pjkJyGEU)

## Java Example

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SemaphoreExample {
    public static void main(String[] args) {
        System.out.println("Semaphore Example");
        
        // Example 1: Basic Semaphore Usage
        System.out.println("\n--- Example 1: Basic Semaphore Usage ---");
        
        // Create a semaphore with 3 permits
        Semaphore semaphore = new Semaphore(3);
        
        // Create a thread pool with 10 threads
        ExecutorService executorService1 = Executors.newFixedThreadPool(10);
        
        // Submit 10 tasks that use the semaphore
        for (int i = 1; i <= 10; i++) {
            final int taskId = i;
            executorService1.submit(() -> {
                try {
                    System.out.println("Task " + taskId + " is waiting to acquire a permit...");
                    semaphore.acquire(); // Acquire a permit
                    System.out.println("Task " + taskId + " acquired a permit! Available permits: " + semaphore.availablePermits());
                    
                    // Simulate some work
                    Thread.sleep(1000);
                    
                    System.out.println("Task " + taskId + " is releasing the permit...");
                    semaphore.release(); // Release the permit
                    System.out.println("Task " + taskId + " released a permit! Available permits: " + semaphore.availablePermits());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Shutdown the executor service and wait for tasks to complete
        executorService1.shutdown();
        try {
            executorService1.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Example 2: Semaphore as a Resource Pool
        System.out.println("\n--- Example 2: Semaphore as a Resource Pool ---");
        
        // Create a database connection pool
        DatabaseConnectionPool connectionPool = new DatabaseConnectionPool(5);
        
        // Create a thread pool with 15 threads
        ExecutorService executorService2 = Executors.newFixedThreadPool(15);
        
        // Submit 15 tasks that use the connection pool
        for (int i = 1; i <= 15; i++) {
            final int taskId = i;
            executorService2.submit(() -> {
                try {
                    // Get a connection from the pool
                    Connection connection = connectionPool.getConnection();
                    System.out.println("Task " + taskId + " acquired connection " + connection.getId());
                    
                    // Simulate using the connection
                    Thread.sleep(1000 + (int)(Math.random() * 1000));
                    
                    // Return the connection to the pool
                    connectionPool.releaseConnection(connection);
                    System.out.println("Task " + taskId + " released connection " + connection.getId());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Shutdown the executor service and wait for tasks to complete
        executorService2.shutdown();
        try {
            executorService2.awaitTermination(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Example 3: Semaphore with Timeout
        System.out.println("\n--- Example 3: Semaphore with Timeout ---");
        
        // Create a semaphore with 2 permits
        Semaphore limitedSemaphore = new Semaphore(2);
        
        // Create a thread pool with 5 threads
        ExecutorService executorService3 = Executors.newFixedThreadPool(5);
        
        // Submit 5 tasks that try to acquire permits with timeout
        for (int i = 1; i <= 5; i++) {
            final int taskId = i;
            executorService3.submit(() -> {
                try {
                    System.out.println("Task " + taskId + " is trying to acquire a permit with timeout...");
                    boolean acquired = limitedSemaphore.tryAcquire(2, TimeUnit.SECONDS);
                    
                    if (acquired) {
                        try {
                            System.out.println("Task " + taskId + " acquired a permit! Available permits: " + 
                                              limitedSemaphore.availablePermits());
                            
                            // Simulate some work
                            Thread.sleep(3000);
                        } finally {
                            limitedSemaphore.release();
                            System.out.println("Task " + taskId + " released a permit! Available permits: " + 
                                              limitedSemaphore.availablePermits());
                        }
                    } else {
                        System.out.println("Task " + taskId + " failed to acquire a permit within timeout!");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Shutdown the executor service and wait for tasks to complete
        executorService3.shutdown();
        try {
            executorService3.awaitTermination(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Example 4: Binary Semaphore (Mutex)
        System.out.println("\n--- Example 4: Binary Semaphore (Mutex) ---");
        
        // Create a binary semaphore (mutex)
        Semaphore mutex = new Semaphore(1);
        
        // Create a shared counter
        Counter counter = new Counter();
        
        // Create a thread pool with 5 threads
        ExecutorService executorService4 = Executors.newFixedThreadPool(5);
        
        // Submit 10 increment tasks
        for (int i = 0; i < 10; i++) {
            executorService4.submit(() -> {
                for (int j = 0; j < 100; j++) {
                    try {
                        mutex.acquire();
                        try {
                            counter.increment();
                        } finally {
                            mutex.release();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        
        // Submit 5 decrement tasks
        for (int i = 0; i < 5; i++) {
            executorService4.submit(() -> {
                for (int j = 0; j < 100; j++) {
                    try {
                        mutex.acquire();
                        try {
                            counter.decrement();
                        } finally {
                            mutex.release();
                        }
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            });
        }
        
        // Shutdown the executor service and wait for tasks to complete
        executorService4.shutdown();
        try {
            executorService4.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Final counter value: " + counter.getValue());
        
        System.out.println("All examples completed");
    }
    
    // Example 2: Connection class for the connection pool
    static class Connection {
        private static final AtomicInteger idGenerator = new AtomicInteger(0);
        private final int id;
        
        public Connection() {
            this.id = idGenerator.incrementAndGet();
        }
        
        public int getId() {
            return id;
        }
    }
    
    // Example 2: Database connection pool using Semaphore
    static class DatabaseConnectionPool {
        private final Semaphore semaphore;
        private final Connection[] connections;
        private final boolean[] used;
        
        public DatabaseConnectionPool(int size) {
            this.semaphore = new Semaphore(size);
            this.connections = new Connection[size];
            this.used = new boolean[size];
            
            // Initialize the connections
            for (int i = 0; i < size; i++) {
                connections[i] = new Connection();
            }
        }
        
        public Connection getConnection() throws InterruptedException {
            semaphore.acquire();
            return getNextAvailableConnection();
        }
        
        public void releaseConnection(Connection connection) {
            if (markAsUnused(connection)) {
                semaphore.release();
            }
        }
        
        private synchronized Connection getNextAvailableConnection() {
            for (int i = 0; i < connections.length; i++) {
                if (!used[i]) {
                    used[i] = true;
                    return connections[i];
                }
            }
            throw new IllegalStateException("No available connections");
        }
        
        private synchronized boolean markAsUnused(Connection connection) {
            for (int i = 0; i < connections.length; i++) {
                if (connections[i] == connection) {
                    if (used[i]) {
                        used[i] = false;
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            return false;
        }
    }
    
    // Example 4: Counter class
    static class Counter {
        private int count = 0;
        
        public void increment() {
            count++;
        }
        
        public void decrement() {
            count--;
        }
        
        public int getValue() {
            return count;
        }
    }
}
```

## Key Points

1. **Semaphore Overview**:
   - A semaphore is a synchronization mechanism that controls access to shared resources
   - It maintains a set of permits that threads can acquire and release
   - When a thread acquires a permit, the count decreases; when it releases a permit, the count increases
   - If no permits are available, `acquire()` blocks until a permit becomes available
   - Semaphores can be used to restrict the number of threads that can access a resource simultaneously

2. **Types of Semaphores**:
   - **Counting Semaphore**: Initialized with a count greater than 1, allows multiple threads to access a resource
   - **Binary Semaphore**: Initialized with a count of 1, acts like a mutex (mutual exclusion)
   - **Fair Semaphore**: Guarantees FIFO (First-In-First-Out) ordering of waiting threads
   - **Unfair Semaphore**: Does not guarantee any particular ordering (default in Java)

3. **Key Methods**:
   - **acquire()**: Acquires a permit, blocking until one is available
   - **acquire(int permits)**: Acquires the specified number of permits
   - **release()**: Releases a permit, increasing the count
   - **release(int permits)**: Releases the specified number of permits
   - **tryAcquire()**: Tries to acquire a permit without blocking, returns true if successful
   - **tryAcquire(long timeout, TimeUnit unit)**: Tries to acquire a permit within the specified timeout
   - **availablePermits()**: Returns the current number of available permits
   - **drainPermits()**: Acquires and returns all available permits
   - **getQueueLength()**: Returns an estimate of the number of threads waiting to acquire

4. **Common Use Cases**:
   - **Resource Pool Management**: Limiting access to a fixed number of resources (e.g., database connections)
   - **Rate Limiting**: Controlling the rate at which operations can be performed
   - **Bounded Collections**: Implementing bounded buffer patterns
   - **Thread Coordination**: Signaling between threads
   - **Throttling**: Limiting the number of concurrent operations

5. **Semaphore vs. Lock**:
   - **Lock**: Provides exclusive access to a resource (one thread at a time)
   - **Semaphore**: Can allow multiple threads to access a resource concurrently (based on permit count)
   - Locks are owned by a specific thread and must be released by the same thread
   - Semaphore permits can be acquired by one thread and released by another

6. **Semaphore vs. CountDownLatch**:
   - **Semaphore**: Controls access to resources, can be acquired and released multiple times
   - **CountDownLatch**: One-time barrier that allows threads to wait until a set of operations completes
   - Semaphores can increase and decrease their count
   - CountDownLatch count only decreases and cannot be reset

7. **Best Practices**:
   - Always release permits in a finally block to prevent permit leakage
   - Consider using tryAcquire with timeout to avoid indefinite blocking
   - Use the appropriate initial permit count based on your resource constraints
   - Consider fairness requirements when creating semaphores
   - Document the purpose and usage of semaphores clearly

8. **Potential Issues**:
   - **Permit Leakage**: Failing to release permits, especially in error conditions
   - **Deadlocks**: Can occur if threads acquire multiple semaphores in different orders
   - **Starvation**: In unfair semaphores, some threads might wait indefinitely
   - **Over-releasing**: Releasing more permits than acquired can lead to unexpected behavior


### 14. CountDownLatch


## Video Explanation
[CountdownLatch Basics in Java (6:33 minutes)](https://www.youtube.com/watch?v=NPGjrhDIzrs)

## Java Example

```java
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CountDownLatchExample {
    public static void main(String[] args) {
        System.out.println("CountDownLatch Example");
        
        // Example 1: Basic CountDownLatch Usage
        System.out.println("\n--- Example 1: Basic CountDownLatch Usage ---");
        
        // Create a CountDownLatch with a count of 3
        CountDownLatch startSignal = new CountDownLatch(1);
        CountDownLatch doneSignal = new CountDownLatch(3);
        
        // Create a thread pool with 3 threads
        ExecutorService executorService1 = Executors.newFixedThreadPool(3);
        
        // Submit 3 worker tasks
        for (int i = 1; i <= 3; i++) {
            final int workerId = i;
            executorService1.submit(() -> {
                try {
                    System.out.println("Worker " + workerId + " is waiting to start...");
                    startSignal.await(); // Wait for the start signal
                    System.out.println("Worker " + workerId + " has started working");
                    
                    // Simulate some work
                    Thread.sleep(1000 + (int)(Math.random() * 2000));
                    
                    System.out.println("Worker " + workerId + " has completed its work");
                    doneSignal.countDown(); // Signal that this worker is done
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Give workers some time to reach the await state
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("Main thread is about to start all workers...");
        startSignal.countDown(); // Start all workers
        
        try {
            System.out.println("Main thread is waiting for all workers to complete...");
            doneSignal.await(); // Wait for all workers to complete
            System.out.println("All workers have completed their work");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Shutdown the executor service
        executorService1.shutdown();
        
        // Example 2: Service Initialization with CountDownLatch
        System.out.println("\n--- Example 2: Service Initialization with CountDownLatch ---");
        
        // Create a CountDownLatch for service initialization
        CountDownLatch servicesReady = new CountDownLatch(3);
        
        // Create a thread pool with 3 threads
        ExecutorService executorService2 = Executors.newFixedThreadPool(3);
        
        // Submit service initialization tasks
        executorService2.submit(() -> initializeService("Database Service", 2000, servicesReady));
        executorService2.submit(() -> initializeService("Cache Service", 1500, servicesReady));
        executorService2.submit(() -> initializeService("Web Service", 1000, servicesReady));
        
        try {
            System.out.println("Main application is waiting for all services to initialize...");
            boolean allServicesStarted = servicesReady.await(5, TimeUnit.SECONDS); // Wait with timeout
            
            if (allServicesStarted) {
                System.out.println("All services have been initialized successfully");
                System.out.println("Application is now ready to serve requests");
            } else {
                System.out.println("Timed out waiting for services to initialize");
                System.out.println("Application startup failed");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Application startup was interrupted");
        }
        
        // Shutdown the executor service
        executorService2.shutdown();
        
        // Example 3: Parallel Task Processing with CountDownLatch
        System.out.println("\n--- Example 3: Parallel Task Processing with CountDownLatch ---");
        
        // Create a CountDownLatch for task completion
        int numTasks = 5;
        CountDownLatch tasksCompleted = new CountDownLatch(numTasks);
        
        // Create a thread pool
        ExecutorService executorService3 = Executors.newFixedThreadPool(3);
        
        // Create a result collector
        ResultCollector resultCollector = new ResultCollector();
        
        // Submit tasks for parallel processing
        System.out.println("Submitting " + numTasks + " tasks for parallel processing...");
        for (int i = 1; i <= numTasks; i++) {
            final int taskId = i;
            executorService3.submit(() -> {
                try {
                    System.out.println("Task " + taskId + " started processing");
                    
                    // Simulate processing
                    Thread.sleep(1000 + (int)(Math.random() * 2000));
                    
                    // Calculate a result
                    int result = taskId * 10;
                    
                    // Add the result to the collector
                    resultCollector.addResult(taskId, result);
                    
                    System.out.println("Task " + taskId + " completed with result: " + result);
                    tasksCompleted.countDown();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        try {
            System.out.println("Main thread is waiting for all tasks to complete...");
            tasksCompleted.await(); // Wait for all tasks to complete
            System.out.println("All tasks have completed");
            System.out.println("Final results: " + resultCollector.getResults());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Shutdown the executor service
        executorService3.shutdown();
        
        // Example 4: Phased Execution with CountDownLatch
        System.out.println("\n--- Example 4: Phased Execution with CountDownLatch ---");
        
        // Create CountDownLatches for each phase
        CountDownLatch phase1Complete = new CountDownLatch(3);
        CountDownLatch phase2Complete = new CountDownLatch(3);
        CountDownLatch phase3Complete = new CountDownLatch(3);
        
        // Create a thread pool
        ExecutorService executorService4 = Executors.newFixedThreadPool(3);
        
        // Submit worker tasks
        for (int i = 1; i <= 3; i++) {
            final int workerId = i;
            executorService4.submit(() -> {
                try {
                    // Phase 1
                    System.out.println("Worker " + workerId + " is executing Phase 1");
                    Thread.sleep(1000);
                    System.out.println("Worker " + workerId + " completed Phase 1");
                    phase1Complete.countDown();
                    
                    // Wait for all workers to complete Phase 1
                    phase1Complete.await();
                    System.out.println("All workers completed Phase 1, Worker " + workerId + " is starting Phase 2");
                    
                    // Phase 2
                    Thread.sleep(1000);
                    System.out.println("Worker " + workerId + " completed Phase 2");
                    phase2Complete.countDown();
                    
                    // Wait for all workers to complete Phase 2
                    phase2Complete.await();
                    System.out.println("All workers completed Phase 2, Worker " + workerId + " is starting Phase 3");
                    
                    // Phase 3
                    Thread.sleep(1000);
                    System.out.println("Worker " + workerId + " completed Phase 3");
                    phase3Complete.countDown();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        try {
            System.out.println("Main thread is waiting for all phases to complete...");
            phase3Complete.await(); // Wait for Phase 3 to complete
            System.out.println("All phases have been completed");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Shutdown the executor service
        executorService4.shutdown();
        
        System.out.println("All examples completed");
    }
    
    // Helper method for Example 2
    private static void initializeService(String serviceName, long initTime, CountDownLatch latch) {
        try {
            System.out.println("Initializing " + serviceName + "...");
            Thread.sleep(initTime); // Simulate initialization time
            System.out.println(serviceName + " has been initialized");
            latch.countDown(); // Signal that this service is initialized
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println(serviceName + " initialization was interrupted");
        }
    }
    
    // Helper class for Example 3
    static class ResultCollector {
        private final java.util.Map<Integer, Integer> results = new java.util.concurrent.ConcurrentHashMap<>();
        
        public void addResult(int taskId, int result) {
            results.put(taskId, result);
        }
        
        public java.util.Map<Integer, Integer> getResults() {
            return new java.util.HashMap<>(results);
        }
    }
}
```

## Key Points

1. **CountDownLatch Overview**:
   - A synchronization aid that allows one or more threads to wait until a set of operations being performed in other threads completes
   - Initialized with a count representing the number of operations that must complete
   - The count is decremented via the `countDown()` method
   - Threads waiting on the latch via `await()` will block until the count reaches zero
   - Once the count reaches zero, all waiting threads are released and any subsequent `await()` calls return immediately
   - The count cannot be reset - CountDownLatch is a one-shot mechanism

2. **Key Methods**:
   - **countDown()**: Decrements the count, releasing all waiting threads when it reaches zero
   - **await()**: Causes the current thread to wait until the latch has counted down to zero
   - **await(long timeout, TimeUnit unit)**: Causes the current thread to wait with a timeout
   - **getCount()**: Returns the current count

3. **Common Use Cases**:
   - **Startup Signal**: Ensuring all worker threads start at the same time
   - **Completion Signal**: Waiting for a group of threads to complete their tasks
   - **Service Initialization**: Waiting for multiple services to initialize before proceeding
   - **Parallel Processing**: Coordinating the completion of parallel tasks
   - **Phased Execution**: Coordinating multiple phases of execution across threads

4. **CountDownLatch vs. CyclicBarrier**:
   - **CountDownLatch**: One-time barrier that allows threads to wait until a set of operations completes
   - **CyclicBarrier**: Reusable barrier that allows a set of threads to wait for each other to reach a common point
   - CountDownLatch count can be decremented by any thread, not just the waiting threads
   - CyclicBarrier is used when all threads must reach the barrier point before any can continue

5. **CountDownLatch vs. Semaphore**:
   - **CountDownLatch**: Used for waiting for a set of operations to complete
   - **Semaphore**: Used for controlling access to resources
   - CountDownLatch count only decreases and cannot be reset
   - Semaphore permits can be acquired and released multiple times

6. **Best Practices**:
   - Use CountDownLatch for one-time events or coordination points
   - Consider using CyclicBarrier for repeated synchronization points
   - Always handle InterruptedException properly
   - Consider using timeout versions of await() to avoid indefinite blocking
   - Document the purpose and usage of CountDownLatch clearly

7. **Potential Issues**:
   - **Deadlocks**: If some threads fail to call countDown(), waiting threads will be blocked indefinitely
   - **Thread Interruption**: Ensure proper handling of InterruptedException
   - **One-time Use**: Remember that CountDownLatch cannot be reset
   - **Count Misconfiguration**: Initializing with incorrect count can lead to premature release or indefinite waiting


### 15. CyclicBarrier


## Video Explanation
[Java Multithreading CyclicBarrier example (7:05 minutes)](https://www.youtube.com/watch?v=tTNrG7gAxks)

## Java Example

```java
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class CyclicBarrierExample {
    public static void main(String[] args) {
        System.out.println("CyclicBarrier Example");
        
        // Example 1: Basic CyclicBarrier Usage
        System.out.println("\n--- Example 1: Basic CyclicBarrier Usage ---");
        
        // Create a CyclicBarrier with a count of 3 and a barrier action
        CyclicBarrier barrier = new CyclicBarrier(3, () -> {
            System.out.println("All threads have reached the barrier, executing barrier action!");
        });
        
        // Create a thread pool with 3 threads
        ExecutorService executorService1 = Executors.newFixedThreadPool(3);
        
        // Submit 3 worker tasks
        for (int i = 1; i <= 3; i++) {
            final int workerId = i;
            executorService1.submit(() -> {
                try {
                    System.out.println("Worker " + workerId + " is performing the first part of the task...");
                    Thread.sleep(1000 + (int)(Math.random() * 2000)); // Simulate work
                    
                    System.out.println("Worker " + workerId + " has completed the first part and is waiting at the barrier...");
                    barrier.await(); // Wait for all threads to reach this point
                    
                    System.out.println("Worker " + workerId + " is performing the second part of the task...");
                    Thread.sleep(1000 + (int)(Math.random() * 2000)); // Simulate more work
                    
                    System.out.println("Worker " + workerId + " has completed the second part and is waiting at the barrier...");
                    barrier.await(); // Wait for all threads to reach this point
                    
                    System.out.println("Worker " + workerId + " has completed all parts of the task.");
                } catch (InterruptedException | BrokenBarrierException e) {
                    System.err.println("Worker " + workerId + " was interrupted: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Shutdown the executor service
        executorService1.shutdown();
        try {
            executorService1.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Example 2: CyclicBarrier with Timeout
        System.out.println("\n--- Example 2: CyclicBarrier with Timeout ---");
        
        // Create a CyclicBarrier with a count of 3
        CyclicBarrier timeoutBarrier = new CyclicBarrier(3);
        
        // Create a thread pool with 2 threads (one less than needed to trip the barrier)
        ExecutorService executorService2 = Executors.newFixedThreadPool(2);
        
        // Submit 2 worker tasks
        for (int i = 1; i <= 2; i++) {
            final int workerId = i;
            executorService2.submit(() -> {
                try {
                    System.out.println("Worker " + workerId + " is performing its task...");
                    Thread.sleep(1000); // Simulate work
                    
                    System.out.println("Worker " + workerId + " is waiting at the barrier with timeout...");
                    try {
                        timeoutBarrier.await(3, TimeUnit.SECONDS); // Wait with timeout
                        System.out.println("Worker " + workerId + " has passed the barrier!");
                    } catch (TimeoutException e) {
                        System.out.println("Worker " + workerId + " timed out waiting at the barrier!");
                    }
                } catch (InterruptedException | BrokenBarrierException e) {
                    System.err.println("Worker " + workerId + " was interrupted: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Shutdown the executor service
        executorService2.shutdown();
        try {
            executorService2.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Example 3: Reusing CyclicBarrier for Multiple Phases
        System.out.println("\n--- Example 3: Reusing CyclicBarrier for Multiple Phases ---");
        
        // Create a CyclicBarrier with a count of 3
        CyclicBarrier phaseBarrier = new CyclicBarrier(3, () -> {
            System.out.println("Phase completed! Moving to the next phase...");
        });
        
        // Create a thread pool with 3 threads
        ExecutorService executorService3 = Executors.newFixedThreadPool(3);
        
        // Submit 3 worker tasks
        for (int i = 1; i <= 3; i++) {
            final int workerId = i;
            executorService3.submit(() -> {
                try {
                    // Phase 1
                    System.out.println("Worker " + workerId + " is executing Phase 1");
                    Thread.sleep(1000 + (int)(Math.random() * 1000));
                    System.out.println("Worker " + workerId + " completed Phase 1 and is waiting for others...");
                    phaseBarrier.await();
                    
                    // Phase 2
                    System.out.println("Worker " + workerId + " is executing Phase 2");
                    Thread.sleep(1000 + (int)(Math.random() * 1000));
                    System.out.println("Worker " + workerId + " completed Phase 2 and is waiting for others...");
                    phaseBarrier.await();
                    
                    // Phase 3
                    System.out.println("Worker " + workerId + " is executing Phase 3");
                    Thread.sleep(1000 + (int)(Math.random() * 1000));
                    System.out.println("Worker " + workerId + " completed Phase 3 and is waiting for others...");
                    phaseBarrier.await();
                    
                    System.out.println("Worker " + workerId + " has completed all phases.");
                } catch (InterruptedException | BrokenBarrierException e) {
                    System.err.println("Worker " + workerId + " was interrupted: " + e.getMessage());
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        // Shutdown the executor service
        executorService3.shutdown();
        try {
            executorService3.awaitTermination(15, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Example 4: Handling Broken Barriers
        System.out.println("\n--- Example 4: Handling Broken Barriers ---");
        
        // Create a CyclicBarrier with a count of 3
        CyclicBarrier brokenBarrier = new CyclicBarrier(3);
        
        // Create a thread pool with 3 threads
        ExecutorService executorService4 = Executors.newFixedThreadPool(3);
        
        // Submit 3 worker tasks
        for (int i = 1; i <= 3; i++) {
            final int workerId = i;
            executorService4.submit(() -> {
                try {
                    System.out.println("Worker " + workerId + " is starting its task...");
                    
                    // The second worker will interrupt itself, breaking the barrier
                    if (workerId == 2) {
                        System.out.println("Worker " + workerId + " is going to interrupt itself and break the barrier!");
                        Thread.currentThread().interrupt();
                    }
                    
                    try {
                        Thread.sleep(1000); // Simulate work
                        System.out.println("Worker " + workerId + " is waiting at the barrier...");
                        brokenBarrier.await();
                        System.out.println("Worker " + workerId + " has passed the barrier!");
                    } catch (BrokenBarrierException e) {
                        System.out.println("Worker " + workerId + " detected that the barrier was broken!");
                    }
                    
                    // Check if the barrier is broken and reset it if needed
                    if (brokenBarrier.isBroken()) {
                        System.out.println("Worker " + workerId + " detected broken barrier. Barrier will be reset.");
                    }
                } catch (InterruptedException e) {
                    System.out.println("Worker " + workerId + " was interrupted!");
                }
            });
        }
        
        // Wait a bit and then reset the barrier
        try {
            Thread.sleep(3000);
            if (brokenBarrier.isBroken()) {
                System.out.println("Main thread is resetting the broken barrier...");
                brokenBarrier.reset();
                System.out.println("Barrier has been reset!");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Shutdown the executor service
        executorService4.shutdown();
        try {
            executorService4.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("All examples completed");
    }
}
```

## Key Points

1. **CyclicBarrier Overview**:
   - A synchronization aid that allows a set of threads to wait for each other to reach a common barrier point
   - Initialized with a count representing the number of threads that must reach the barrier
   - When a thread calls `await()`, it blocks until all threads have called `await()`
   - Once all threads reach the barrier, an optional barrier action can be executed
   - After all threads are released, the barrier is automatically reset for reuse (hence "cyclic")
   - Useful for problems where threads need to work in phases, with each phase requiring all threads to complete before moving to the next phase

2. **Key Methods**:
   - **await()**: Causes the current thread to wait until all parties have invoked await()
   - **await(long timeout, TimeUnit unit)**: Waits with a timeout
   - **getNumberWaiting()**: Returns the number of parties currently waiting at the barrier
   - **getParties()**: Returns the number of parties required to trip the barrier
   - **isBroken()**: Queries if the barrier is in a broken state
   - **reset()**: Resets the barrier to its initial state

3. **Common Use Cases**:
   - **Phased Computation**: Coordinating multiple phases of a computation across threads
   - **Parallel Decomposition**: Breaking a problem into parts that are processed in parallel, then combining results
   - **Iterative Algorithms**: Algorithms that require synchronization at the end of each iteration
   - **Simulations**: Coordinating time steps in simulations
   - **Testing**: Coordinating test scenarios that require precise timing

4. **CyclicBarrier vs. CountDownLatch**:
   - **CyclicBarrier**: Reusable barrier that allows a set of threads to wait for each other
   - **CountDownLatch**: One-time barrier that allows threads to wait until a set of operations completes
   - CyclicBarrier automatically resets after all threads pass through
   - CountDownLatch cannot be reset once the count reaches zero
   - CyclicBarrier is typically used when all waiting threads also participate in the action
   - CountDownLatch is often used when one thread waits for multiple other threads

5. **CyclicBarrier vs. Phaser**:
   - **CyclicBarrier**: Fixed number of parties, reusable barrier
   - **Phaser**: Dynamic number of parties, more flexible but more complex
   - CyclicBarrier is simpler to use for fixed-size thread groups
   - Phaser allows registration and deregistration of parties at runtime

6. **Barrier Action**:
   - An optional Runnable that executes when the barrier trips
   - Runs in one of the threads that was waiting on the barrier
   - Useful for consolidating results or preparing for the next phase
   - Executes before any threads are released

7. **Broken Barriers**:
   - A barrier breaks if a thread waiting at the barrier is interrupted or times out
   - When a barrier breaks, all waiting threads receive a BrokenBarrierException
   - A broken barrier must be explicitly reset before it can be used again
   - Use isBroken() to check if a barrier is broken

8. **Best Practices**:
   - Always handle BrokenBarrierException and InterruptedException
   - Consider using timeout versions of await() to avoid indefinite blocking
   - Reset broken barriers before reusing them
   - Document the phases and synchronization points clearly
   - Be cautious with barrier actions to avoid deadlocks


### 16. Phaser


## Video Explanation
[Example Application of Java Phaser (9:15 minutes)](https://www.youtube.com/watch?v=6WBjr_Zc5vM)

## Java Example

```java
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class PhaserExample {
    public static void main(String[] args) {
        System.out.println("Phaser Example");
        
        // Example 1: Basic Phaser Usage
        System.out.println("\n--- Example 1: Basic Phaser Usage ---");
        
        // Create a Phaser with 1 registered party (the main thread)
        Phaser phaser = new Phaser(1);
        
        // Create a thread pool with 3 threads
        ExecutorService executorService1 = Executors.newFixedThreadPool(3);
        
        // Submit 3 worker tasks
        for (int i = 1; i <= 3; i++) {
            final int workerId = i;
            executorService1.submit(() -> {
                try {
                    // Register this thread with the phaser
                    int phase = phaser.register();
                    System.out.println("Worker " + workerId + " registered with the phaser in phase " + phase);
                    
                    // Phase 1
                    System.out.println("Worker " + workerId + " is performing phase 1 work...");
                    Thread.sleep(1000 + (int)(Math.random() * 1000));
                    System.out.println("Worker " + workerId + " completed phase 1 and is waiting for others...");
                    
                    // Wait for all threads to complete phase 1
                    phase = phaser.arriveAndAwaitAdvance();
                    System.out.println("Worker " + workerId + " proceeding to phase " + phase);
                    
                    // Phase 2
                    System.out.println("Worker " + workerId + " is performing phase 2 work...");
                    Thread.sleep(1000 + (int)(Math.random() * 1000));
                    System.out.println("Worker " + workerId + " completed phase 2 and is waiting for others...");
                    
                    // Wait for all threads to complete phase 2
                    phase = phaser.arriveAndAwaitAdvance();
                    System.out.println("Worker " + workerId + " proceeding to phase " + phase);
                    
                    // Phase 3
                    System.out.println("Worker " + workerId + " is performing phase 3 work...");
                    Thread.sleep(1000 + (int)(Math.random() * 1000));
                    System.out.println("Worker " + workerId + " completed phase 3");
                    
                    // Deregister from the phaser
                    phaser.arriveAndDeregister();
                    System.out.println("Worker " + workerId + " has deregistered from the phaser");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        try {
            // Wait for all threads to register
            Thread.sleep(500);
            
            System.out.println("Main thread is waiting for all workers to complete phase 1...");
            int phase = phaser.arriveAndAwaitAdvance();
            System.out.println("All workers completed phase 1, now in phase " + phase);
            
            System.out.println("Main thread is waiting for all workers to complete phase 2...");
            phase = phaser.arriveAndAwaitAdvance();
            System.out.println("All workers completed phase 2, now in phase " + phase);
            
            System.out.println("Main thread is waiting for all workers to complete phase 3...");
            phase = phaser.arriveAndAwaitAdvance();
            System.out.println("All workers completed phase 3, now in phase " + phase);
            
            // Deregister the main thread
            phaser.arriveAndDeregister();
            System.out.println("Main thread has deregistered from the phaser");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Shutdown the executor service
        executorService1.shutdown();
        try {
            executorService1.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Example 2: Dynamic Registration and Deregistration
        System.out.println("\n--- Example 2: Dynamic Registration and Deregistration ---");
        
        // Create a Phaser with 1 registered party (the main thread)
        Phaser dynamicPhaser = new Phaser(1);
        
        // Create a thread pool with 5 threads
        ExecutorService executorService2 = Executors.newFixedThreadPool(5);
        
        // Submit initial set of worker tasks
        for (int i = 1; i <= 3; i++) {
            final int workerId = i;
            executorService2.submit(() -> {
                try {
                    // Register this thread with the phaser
                    dynamicPhaser.register();
                    System.out.println("Initial Worker " + workerId + " registered with the phaser");
                    
                    // Phase 1
                    System.out.println("Initial Worker " + workerId + " is performing work...");
                    Thread.sleep(1000 + (int)(Math.random() * 1000));
                    System.out.println("Initial Worker " + workerId + " completed work and is waiting for others...");
                    
                    // Wait for all threads to complete phase 1
                    int phase = dynamicPhaser.arriveAndAwaitAdvance();
                    System.out.println("Initial Worker " + workerId + " proceeding to phase " + phase);
                    
                    // Deregister from the phaser
                    dynamicPhaser.arriveAndDeregister();
                    System.out.println("Initial Worker " + workerId + " has deregistered from the phaser");
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        try {
            // Wait for all initial threads to register
            Thread.sleep(500);
            
            System.out.println("Main thread is waiting for initial workers to complete...");
            int phase = dynamicPhaser.arriveAndAwaitAdvance();
            System.out.println("All initial workers completed, now in phase " + phase);
            
            // Add more workers dynamically
            System.out.println("Adding more workers dynamically...");
            for (int i = 4; i <= 5; i++) {
                final int workerId = i;
                executorService2.submit(() -> {
                    try {
                        // Register this thread with the phaser
                        dynamicPhaser.register();
                        System.out.println("Additional Worker " + workerId + " registered with the phaser");
                        
                        // Perform work
                        System.out.println("Additional Worker " + workerId + " is performing work...");
                        Thread.sleep(1000 + (int)(Math.random() * 1000));
                        System.out.println("Additional Worker " + workerId + " completed work and is waiting for others...");
                        
                        // Wait for all threads to complete
                        int workerPhase = dynamicPhaser.arriveAndAwaitAdvance();
                        System.out.println("Additional Worker " + workerId + " proceeding to phase " + workerPhase);
                        
                        // Deregister from the phaser
                        dynamicPhaser.arriveAndDeregister();
                        System.out.println("Additional Worker " + workerId + " has deregistered from the phaser");
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }
            
            // Wait for all additional threads to register
            Thread.sleep(500);
            
            System.out.println("Main thread is waiting for additional workers to complete...");
            phase = dynamicPhaser.arriveAndAwaitAdvance();
            System.out.println("All additional workers completed, now in phase " + phase);
            
            // Deregister the main thread
            dynamicPhaser.arriveAndDeregister();
            System.out.println("Main thread has deregistered from the phaser");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Shutdown the executor service
        executorService2.shutdown();
        try {
            executorService2.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Example 3: Phaser with Termination Condition
        System.out.println("\n--- Example 3: Phaser with Termination Condition ---");
        
        // Create a Phaser with a termination condition
        Phaser terminatingPhaser = new Phaser(1) {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                System.out.println("onAdvance: phase " + phase + ", registered parties: " + registeredParties);
                
                // Terminate after phase 2 or when no parties are registered
                return phase >= 2 || registeredParties == 0;
            }
        };
        
        // Create a thread pool with 3 threads
        ExecutorService executorService3 = Executors.newFixedThreadPool(3);
        
        // Submit worker tasks
        for (int i = 1; i <= 3; i++) {
            final int workerId = i;
            executorService3.submit(() -> {
                try {
                    // Register this thread with the phaser
                    terminatingPhaser.register();
                    System.out.println("Worker " + workerId + " registered with the terminating phaser");
                    
                    // Phase 1
                    System.out.println("Worker " + workerId + " is performing phase 1 work...");
                    Thread.sleep(1000 + (int)(Math.random() * 1000));
                    System.out.println("Worker " + workerId + " completed phase 1 and is waiting for others...");
                    
                    // Wait for all threads to complete phase 1
                    int phase = terminatingPhaser.arriveAndAwaitAdvance();
                    if (terminatingPhaser.isTerminated()) {
                        System.out.println("Worker " + workerId + " detected that the phaser is terminated after phase 1");
                        return;
                    }
                    
                    // Phase 2
                    System.out.println("Worker " + workerId + " is performing phase 2 work...");
                    Thread.sleep(1000 + (int)(Math.random() * 1000));
                    System.out.println("Worker " + workerId + " completed phase 2 and is waiting for others...");
                    
                    // Wait for all threads to complete phase 2
                    phase = terminatingPhaser.arriveAndAwaitAdvance();
                    if (terminatingPhaser.isTerminated()) {
                        System.out.println("Worker " + workerId + " detected that the phaser is terminated after phase 2");
                        return;
                    }
                    
                    // Phase 3 (should not execute due to termination condition)
                    System.out.println("Worker " + workerId + " is performing phase 3 work...");
                    Thread.sleep(1000);
                    System.out.println("Worker " + workerId + " completed phase 3");
                    
                    // Deregister from the phaser
                    terminatingPhaser.arriveAndDeregister();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        try {
            // Wait for all threads to register
            Thread.sleep(500);
            
            System.out.println("Main thread is waiting for all workers to complete phase 1...");
            int phase = terminatingPhaser.arriveAndAwaitAdvance();
            if (terminatingPhaser.isTerminated()) {
                System.out.println("Main thread detected that the phaser is terminated after phase 1");
            } else {
                System.out.println("All workers completed phase 1, now in phase " + phase);
                
                System.out.println("Main thread is waiting for all workers to complete phase 2...");
                phase = terminatingPhaser.arriveAndAwaitAdvance();
                if (terminatingPhaser.isTerminated()) {
                    System.out.println("Main thread detected that the phaser is terminated after phase 2");
                } else {
                    System.out.println("All workers completed phase 2, now in phase " + phase);
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Shutdown the executor service
        executorService3.shutdown();
        try {
            executorService3.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Example 4: Phaser with Timeout
        System.out.println("\n--- Example 4: Phaser with Timeout ---");
        
        // Create a Phaser with 1 registered party (the main thread)
        Phaser timeoutPhaser = new Phaser(1);
        
        // Create a thread pool with 2 threads
        ExecutorService executorService4 = Executors.newFixedThreadPool(2);
        
        // Submit 2 worker tasks
        for (int i = 1; i <= 2; i++) {
            final int workerId = i;
            executorService4.submit(() -> {
                try {
                    // Register this thread with the phaser
                    timeoutPhaser.register();
                    System.out.println("Worker " + workerId + " registered with the phaser");
                    
                    // Perform work
                    System.out.println("Worker " + workerId + " is performing work...");
                    
                    // First worker completes quickly, second worker takes longer
                    if (workerId == 1) {
                        Thread.sleep(1000);
                        System.out.println("Worker " + workerId + " completed work and is waiting for others...");
                        
                        try {
                            // Wait with timeout
                            int phase = timeoutPhaser.awaitAdvanceInterruptibly(timeoutPhaser.arrive(), 3, TimeUnit.SECONDS);
                            System.out.println("Worker " + workerId + " proceeding to phase " + phase);
                        } catch (TimeoutException e) {
                            System.out.println("Worker " + workerId + " timed out waiting for other workers!");
                            timeoutPhaser.forceTermination();
                            System.out.println("Worker " + workerId + " forced termination of the phaser");
                        }
                    } else {
                        // Second worker takes too long
                        Thread.sleep(5000);
                        System.out.println("Worker " + workerId + " completed work and is waiting for others...");
                        
                        if (timeoutPhaser.isTerminated()) {
                            System.out.println("Worker " + workerId + " detected that the phaser is terminated");
                        } else {
                            int phase = timeoutPhaser.arriveAndAwaitAdvance();
                            System.out.println("Worker " + workerId + " proceeding to phase " + phase);
                        }
                    }
                    
                    // Deregister from the phaser
                    if (!timeoutPhaser.isTerminated()) {
                        timeoutPhaser.arriveAndDeregister();
                        System.out.println("Worker " + workerId + " has deregistered from the phaser");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }
        
        try {
            // Wait for all threads to register
            Thread.sleep(500);
            
            System.out.println("Main thread is waiting for all workers to complete...");
            if (timeoutPhaser.isTerminated()) {
                System.out.println("Main thread detected that the phaser is terminated");
            } else {
                int phase = timeoutPhaser.arriveAndAwaitAdvance();
                System.out.println("All workers completed, now in phase " + phase);
            }
            
            // Deregister the main thread
            if (!timeoutPhaser.isTerminated()) {
                timeoutPhaser.arriveAndDeregister();
                System.out.println("Main thread has deregistered from the phaser");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Shutdown the executor service
        executorService4.shutdown();
        try {
            executorService4.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("All examples completed");
    }
}
```

## Key Points

1. **Phaser Overview**:
   - A more flexible synchronization barrier than CountDownLatch or CyclicBarrier
   - Allows dynamic registration and deregistration of parties
   - Supports multiple phases of execution
   - Can be used as a one-time barrier (like CountDownLatch) or a reusable barrier (like CyclicBarrier)
   - Provides termination detection
   - Introduced in Java 7 as part of java.util.concurrent package

2. **Key Methods**:
   - **register()**: Adds a new unarrived party to the phaser
   - **arrive()**: Records arrival at the current phase without waiting
   - **arriveAndAwaitAdvance()**: Records arrival and waits for all other parties to arrive
   - **arriveAndDeregister()**: Records arrival and deregisters from the phaser
   - **awaitAdvance(int phase)**: Waits for the phaser to advance from the given phase
   - **awaitAdvanceInterruptibly(int phase)**: Interruptible version of awaitAdvance
   - **awaitAdvanceInterruptibly(int phase, long timeout, TimeUnit unit)**: Timed version of awaitAdvanceInterruptibly
   - **forceTermination()**: Forces the phaser to enter a terminated state
   - **getPhase()**: Returns the current phase number
   - **getRegisteredParties()**: Returns the number of parties registered at this phaser
   - **getArrivedParties()**: Returns the number of parties that have arrived at the current phase
   - **getUnarrivedParties()**: Returns the number of parties that have not yet arrived at the current phase
   - **isTerminated()**: Returns true if the phaser has been terminated

3. **Customization with onAdvance()**:
   - Override the onAdvance(int phase, int registeredParties) method to customize phase advancement behavior
   - Return true to terminate the phaser, false to continue to the next phase
   - Default implementation returns true when registeredParties is zero

4. **Common Use Cases**:
   - **Dynamic Task Groups**: When the number of threads is not known in advance
   - **Multi-phase Computations**: When computation occurs in multiple phases
   - **Tree-structured Synchronization**: When organizing synchronization in a hierarchical manner
   - **Controlled Termination**: When synchronization needs to terminate after a certain condition
   - **Tiered Synchronization**: When different groups of threads need different synchronization points

5. **Phaser vs. CountDownLatch**:
   - **Phaser**: Supports multiple phases and dynamic registration/deregistration
   - **CountDownLatch**: One-time barrier with fixed count
   - Phaser can be used as a CountDownLatch by using a single phase
   - Phaser provides more flexibility but is more complex to use

6. **Phaser vs. CyclicBarrier**:
   - **Phaser**: Supports dynamic registration/deregistration and explicit phase advancement
   - **CyclicBarrier**: Fixed number of parties and automatic reset
   - Phaser can be used as a CyclicBarrier by reusing the same phaser for multiple phases
   - Phaser provides more control over phase advancement

7. **Tiered Phaser**:
   - Phasers can be arranged in a tree structure
   - Child phasers can be registered with a parent phaser
   - Allows for hierarchical synchronization
   - Useful for divide-and-conquer algorithms

8. **Best Practices**:
   - Always handle InterruptedException and TimeoutException properly
   - Consider using timeout versions of await methods to avoid indefinite blocking
   - Be careful with the onAdvance method to avoid unintended termination
   - Document the phases and synchronization points clearly
   - Use arriveAndDeregister() to clean up when a thread is done with all phases
   - Check isTerminated() before performing operations on a phaser that might have been terminated


### 17. Exchanger


## Video Explanation
[Exchanger in Java Explanation (6:16 minutes)](https://www.youtube.com/watch?v=5f93FMDrWSw)

## Java Example

```java
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ExchangerExample {
    public static void main(String[] args) {
        System.out.println("Exchanger Example");
        
        // Example 1: Basic Exchanger Usage
        System.out.println("\n--- Example 1: Basic Exchanger Usage ---");
        
        // Create an Exchanger for String objects
        Exchanger<String> exchanger = new Exchanger<>();
        
        // Create a thread pool with 2 threads
        ExecutorService executorService1 = Executors.newFixedThreadPool(2);
        
        // Submit producer task
        executorService1.submit(() -> {
            try {
                String producerData = "Data from Producer";
                System.out.println("Producer has: " + producerData);
                
                System.out.println("Producer is waiting to exchange data...");
                String consumerData = exchanger.exchange(producerData);
                
                System.out.println("Producer received: " + consumerData);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Producer was interrupted");
            }
        });
        
        // Submit consumer task
        executorService1.submit(() -> {
            try {
                String consumerData = "Data from Consumer";
                System.out.println("Consumer has: " + consumerData);
                
                // Simulate some work before exchange
                Thread.sleep(2000);
                
                System.out.println("Consumer is waiting to exchange data...");
                String producerData = exchanger.exchange(consumerData);
                
                System.out.println("Consumer received: " + producerData);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Consumer was interrupted");
            }
        });
        
        // Shutdown the executor service
        executorService1.shutdown();
        try {
            executorService1.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Example 2: Exchanger with Complex Objects
        System.out.println("\n--- Example 2: Exchanger with Complex Objects ---");
        
        // Create an Exchanger for DataBuffer objects
        Exchanger<DataBuffer> bufferExchanger = new Exchanger<>();
        
        // Create a thread pool with 2 threads
        ExecutorService executorService2 = Executors.newFixedThreadPool(2);
        
        // Submit filler task
        executorService2.submit(() -> {
            try {
                DataBuffer filledBuffer = new DataBuffer(10);
                
                for (int i = 0; i < 3; i++) {
                    // Fill the buffer with data
                    filledBuffer.fill();
                    System.out.println("Filler filled buffer: " + filledBuffer);
                    
                    System.out.println("Filler is waiting to exchange buffer...");
                    DataBuffer emptyBuffer = bufferExchanger.exchange(filledBuffer);
                    
                    System.out.println("Filler received empty buffer: " + emptyBuffer);
                    filledBuffer = emptyBuffer; // Use the received buffer for next iteration
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Filler was interrupted");
            }
        });
        
        // Submit processor task
        executorService2.submit(() -> {
            try {
                DataBuffer emptyBuffer = new DataBuffer(10);
                
                for (int i = 0; i < 3; i++) {
                    System.out.println("Processor is waiting to exchange buffer...");
                    DataBuffer filledBuffer = bufferExchanger.exchange(emptyBuffer);
                    
                    System.out.println("Processor received filled buffer: " + filledBuffer);
                    
                    // Process the buffer
                    filledBuffer.process();
                    System.out.println("Processor processed buffer: " + filledBuffer);
                    
                    emptyBuffer = filledBuffer; // Use the processed buffer for next iteration
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Processor was interrupted");
            }
        });
        
        // Shutdown the executor service
        executorService2.shutdown();
        try {
            executorService2.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Example 3: Exchanger with Timeout
        System.out.println("\n--- Example 3: Exchanger with Timeout ---");
        
        // Create an Exchanger for String objects
        Exchanger<String> timeoutExchanger = new Exchanger<>();
        
        // Create a thread pool with 2 threads
        ExecutorService executorService3 = Executors.newFixedThreadPool(2);
        
        // Submit first task that will wait with timeout
        executorService3.submit(() -> {
            try {
                String data = "Data from Thread 1";
                System.out.println("Thread 1 has: " + data);
                
                System.out.println("Thread 1 is waiting to exchange data with timeout...");
                try {
                    String receivedData = timeoutExchanger.exchange(data, 3, TimeUnit.SECONDS);
                    System.out.println("Thread 1 received: " + receivedData);
                } catch (TimeoutException e) {
                    System.out.println("Thread 1 timed out waiting for exchange!");
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread 1 was interrupted");
            }
        });
        
        // Submit second task that will be delayed beyond the timeout
        executorService3.submit(() -> {
            try {
                String data = "Data from Thread 2";
                System.out.println("Thread 2 has: " + data);
                
                // Sleep longer than the timeout period
                System.out.println("Thread 2 is sleeping for 5 seconds before exchange...");
                Thread.sleep(5000);
                
                System.out.println("Thread 2 is now trying to exchange data...");
                try {
                    String receivedData = timeoutExchanger.exchange(data);
                    System.out.println("Thread 2 received: " + receivedData);
                } catch (Exception e) {
                    System.out.println("Thread 2 failed to exchange: " + e.getMessage());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Thread 2 was interrupted");
            }
        });
        
        // Shutdown the executor service
        executorService3.shutdown();
        try {
            executorService3.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Example 4: Multiple Exchanges
        System.out.println("\n--- Example 4: Multiple Exchanges ---");
        
        // Create an Exchanger for Integer objects
        Exchanger<Integer> numberExchanger = new Exchanger<>();
        
        // Create a thread pool with 2 threads
        ExecutorService executorService4 = Executors.newFixedThreadPool(2);
        
        // Submit incrementer task
        executorService4.submit(() -> {
            try {
                Integer number = 0;
                
                for (int i = 0; i < 5; i++) {
                    // Increment the number
                    number++;
                    System.out.println("Incrementer: number = " + number);
                    
                    System.out.println("Incrementer is waiting to exchange...");
                    number = numberExchanger.exchange(number);
                    
                    System.out.println("Incrementer received: " + number);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Incrementer was interrupted");
            }
        });
        
        // Submit doubler task
        executorService4.submit(() -> {
            try {
                Integer number = 0;
                
                for (int i = 0; i < 5; i++) {
                    System.out.println("Doubler is waiting to exchange...");
                    number = numberExchanger.exchange(number);
                    
                    System.out.println("Doubler received: " + number);
                    
                    // Double the number
                    number *= 2;
                    System.out.println("Doubler: number = " + number);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.err.println("Doubler was interrupted");
            }
        });
        
        // Shutdown the executor service
        executorService4.shutdown();
        try {
            executorService4.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        System.out.println("All examples completed");
    }
    
    // Helper class for Example 2
    static class DataBuffer {
        private final int[] data;
        private boolean filled;
        
        public DataBuffer(int size) {
            this.data = new int[size];
            this.filled = false;
        }
        
        public void fill() {
            // Fill the buffer with data
            for (int i = 0; i < data.length; i++) {
                data[i] = (int)(Math.random() * 100);
            }
            filled = true;
        }
        
        public void process() {
            // Process the data in the buffer
            for (int i = 0; i < data.length; i++) {
                data[i] = 0;
            }
            filled = false;
        }
        
        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(filled ? "Filled" : "Empty").append(" buffer [");
            for (int i = 0; i < Math.min(data.length, 3); i++) {
                sb.append(data[i]);
                if (i < Math.min(data.length, 3) - 1) {
                    sb.append(", ");
                }
            }
            if (data.length > 3) {
                sb.append(", ...");
            }
            sb.append("]");
            return sb.toString();
        }
    }
}
```

## Key Points

1. **Exchanger Overview**:
   - A synchronization point where two threads can exchange objects
   - Each thread presents an object and receives the object presented by the other thread
   - Both threads must reach the exchange point before either can proceed
   - Useful for genetic algorithms, pipeline designs, and producer-consumer scenarios
   - Part of the java.util.concurrent package since Java 5

2. **Key Methods**:
   - **exchange(V x)**: Waits for another thread to arrive at the exchange point and then transfers the given object to it, receiving its object in return
   - **exchange(V x, long timeout, TimeUnit unit)**: Waits for another thread to arrive at the exchange point, with a timeout, and then transfers objects

3. **Common Use Cases**:
   - **Producer-Consumer Pattern**: When a producer fills a buffer and exchanges it with an empty buffer from a consumer
   - **Genetic Algorithms**: When two threads exchange partial solutions to create new candidate solutions
   - **Pipeline Designs**: When data needs to be passed between stages of a pipeline
   - **Data Synchronization**: When two threads need to synchronize their data at specific points
   - **Dual Buffer Processing**: When one thread fills a buffer while another processes a previously filled buffer

4. **Exchanger vs. BlockingQueue**:
   - **Exchanger**: Direct exchange between exactly two threads
   - **BlockingQueue**: Multiple producers and consumers can interact through the queue
   - Exchanger is more efficient for the specific case of two threads exchanging objects
   - BlockingQueue is more flexible for general producer-consumer scenarios

5. **Exchanger vs. SynchronousQueue**:
   - **Exchanger**: Two-way transfer of objects between threads
   - **SynchronousQueue**: One-way transfer of objects from producer to consumer
   - Exchanger allows both threads to receive an object
   - SynchronousQueue only allows the consumer to receive an object

6. **Timeout Handling**:
   - The exchange method with timeout allows a thread to avoid waiting indefinitely
   - If the timeout expires before another thread arrives, a TimeoutException is thrown
   - Useful for handling scenarios where the other thread might be delayed or fail

7. **Best Practices**:
   - Use Exchanger when you need bidirectional exchange between exactly two threads
   - Handle InterruptedException and TimeoutException properly
   - Consider using timeout versions of exchange to avoid indefinite blocking
   - Ensure that both threads call exchange with compatible object types
   - Document the exchange points and object types clearly

8. **Potential Issues**:
   - **Deadlocks**: If one thread never reaches the exchange point, the other will be blocked indefinitely
   - **Thread Interruption**: Ensure proper handling of InterruptedException
   - **Memory Consistency**: The exchanged objects must be properly published to ensure visibility
   - **Type Safety**: Generic type parameters should be used to ensure type compatibility


## LeetCode-Style Problems

This section contains 50 LeetCode-style multithreading problems, ranging from basic to advanced. Each problem includes a detailed description, multiple solution approaches with complete Java implementations, time and space complexity analysis, key insights for interviews, and relevant video explanations.

### 1. Print in Order (Easy)


## Problem Description

Suppose we have a class:

```java
public class Foo {
  public void first() { print("first"); }
  public void second() { print("second"); }
  public void third() { print("third"); }
}
```

The same instance of `Foo` will be passed to three different threads. Thread A will call `first()`, thread B will call `second()`, and thread C will call `third()`. Design a mechanism and modify the program to ensure that `second()` is executed after `first()`, and `third()` is executed after `second()`.

**Note:** We do not know how the threads will be scheduled in the operating system, even though the numbers in the input seem to imply the ordering.

**Example 1:**
- Input: nums = [1,2,3]
- Output: "firstsecondthird"
- Explanation: There are three threads being fired asynchronously. The input [1,2,3] means thread A calls first(), thread B calls second(), and thread C calls third(). "firstsecondthird" is the correct output.

**Example 2:**
- Input: nums = [1,3,2]
- Output: "firstsecondthird"
- Explanation: The input [1,3,2] means thread A calls first(), thread B calls third(), and thread C calls second(). "firstsecondthird" is the correct output.

## Video Explanation
[Java Concurrency: Print in Order (7:15 minutes)](https://www.youtube.com/watch?v=C_a9YdAO1ks)

## Solution Approach

This problem tests your understanding of thread synchronization. There are several ways to solve it:

### 1. Using CountDownLatch

```java
class Foo {
    private CountDownLatch latch1;
    private CountDownLatch latch2;
    
    public Foo() {
        latch1 = new CountDownLatch(1);
        latch2 = new CountDownLatch(1);
    }

    public void first(Runnable printFirst) throws InterruptedException {
        // printFirst.run() outputs "first". Do not change or remove this line.
        printFirst.run();
        latch1.countDown();
    }

    public void second(Runnable printSecond) throws InterruptedException {
        latch1.await();
        // printSecond.run() outputs "second". Do not change or remove this line.
        printSecond.run();
        latch2.countDown();
    }

    public void third(Runnable printThird) throws InterruptedException {
        latch2.await();
        // printThird.run() outputs "third". Do not change or remove this line.
        printThird.run();
    }
}
```

### 2. Using Semaphores

```java
class Foo {
    private Semaphore sem1;
    private Semaphore sem2;
    
    public Foo() {
        sem1 = new Semaphore(0);
        sem2 = new Semaphore(0);
    }

    public void first(Runnable printFirst) throws InterruptedException {
        // printFirst.run() outputs "first". Do not change or remove this line.
        printFirst.run();
        sem1.release();
    }

    public void second(Runnable printSecond) throws InterruptedException {
        sem1.acquire();
        // printSecond.run() outputs "second". Do not change or remove this line.
        printSecond.run();
        sem2.release();
    }

    public void third(Runnable printThird) throws InterruptedException {
        sem2.acquire();
        // printThird.run() outputs "third". Do not change or remove this line.
        printThird.run();
    }
}
```

### 3. Using ReentrantLock and Condition

```java
class Foo {
    private final Lock lock = new ReentrantLock();
    private final Condition firstDone = lock.newCondition();
    private final Condition secondDone = lock.newCondition();
    private int state = 1;
    
    public Foo() {}

    public void first(Runnable printFirst) throws InterruptedException {
        lock.lock();
        try {
            // printFirst.run() outputs "first". Do not change or remove this line.
            printFirst.run();
            state = 2;
            firstDone.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void second(Runnable printSecond) throws InterruptedException {
        lock.lock();
        try {
            while (state != 2) {
                firstDone.await();
            }
            // printSecond.run() outputs "second". Do not change or remove this line.
            printSecond.run();
            state = 3;
            secondDone.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void third(Runnable printThird) throws InterruptedException {
        lock.lock();
        try {
            while (state != 3) {
                secondDone.await();
            }
            // printThird.run() outputs "third". Do not change or remove this line.
            printThird.run();
        } finally {
            lock.unlock();
        }
    }
}
```

### 4. Using Volatile Variables and Busy Waiting

```java
class Foo {
    private volatile int state = 1;
    
    public Foo() {}

    public void first(Runnable printFirst) throws InterruptedException {
        // printFirst.run() outputs "first". Do not change or remove this line.
        printFirst.run();
        state = 2;
    }

    public void second(Runnable printSecond) throws InterruptedException {
        while (state != 2) {
            // busy wait
        }
        // printSecond.run() outputs "second". Do not change or remove this line.
        printSecond.run();
        state = 3;
    }

    public void third(Runnable printThird) throws InterruptedException {
        while (state != 3) {
            // busy wait
        }
        // printThird.run() outputs "third". Do not change or remove this line.
        printThird.run();
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: O(1) for the operations themselves, but the waiting time depends on thread scheduling
- Space Complexity: O(1) as we only use a constant amount of extra space

## Key Insights

1. This problem demonstrates the need for thread synchronization when tasks must be executed in a specific order.
2. The CountDownLatch approach is clean and efficient, using a countdown mechanism to signal when a thread can proceed.
3. The Semaphore approach uses permits to control access, with each thread releasing a permit for the next thread.
4. The ReentrantLock with Condition approach provides more flexibility but is more complex.
5. The volatile variable with busy waiting approach is simple but less efficient as it consumes CPU resources while waiting.

## Interview Tips

- Be prepared to discuss the trade-offs between different synchronization mechanisms.
- Understand that busy waiting solutions are generally less efficient but simpler to implement.
- Know how to use java.util.concurrent classes like CountDownLatch, Semaphore, and ReentrantLock.
- Be able to explain the concept of thread safety and how it applies to this problem.
- Consider edge cases like what happens if the threads are started in a different order than expected.


### 2. Print FooBar Alternately (Medium)


## Problem Description

Suppose you are given the following code:

```java
class FooBar {
  public void foo() {
    for (int i = 0; i < n; i++) {
      print("foo");
    }
  }

  public void bar() {
    for (int i = 0; i < n; i++) {
      print("bar");
    }
  }
}
```

The same instance of `FooBar` will be passed to two different threads:
* thread `A` will call `foo()`, while
* thread `B` will call `bar()`.

Modify the given program to output `"foobar"` `n` times.

**Example 1:**
- Input: n = 1
- Output: "foobar"
- Explanation: There are two threads being fired asynchronously. One of them calls foo(), while the other calls bar(). "foobar" is being output 1 time.

**Example 2:**
- Input: n = 2
- Output: "foobarfoobar"
- Explanation: "foobar" is being output 2 times.

## Video Explanation
[Java Concurrency: Print FooBar Alternately (8:45 minutes)](https://www.youtube.com/watch?v=0dW-YEjmF6o)

## Solution Approach

This problem tests your understanding of thread synchronization and coordination. There are several ways to solve it:

### 1. Using Semaphores

```java
class FooBar {
    private int n;
    private Semaphore fooSemaphore;
    private Semaphore barSemaphore;
    
    public FooBar(int n) {
        this.n = n;
        this.fooSemaphore = new Semaphore(1);
        this.barSemaphore = new Semaphore(0);
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            fooSemaphore.acquire();
            // printFoo.run() outputs "foo". Do not change or remove this line.
            printFoo.run();
            barSemaphore.release();
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            barSemaphore.acquire();
            // printBar.run() outputs "bar". Do not change or remove this line.
            printBar.run();
            fooSemaphore.release();
        }
    }
}
```

### 2. Using ReentrantLock and Conditions

```java
class FooBar {
    private int n;
    private Lock lock;
    private Condition fooTurn;
    private Condition barTurn;
    private boolean isFooTurn;
    
    public FooBar(int n) {
        this.n = n;
        this.lock = new ReentrantLock();
        this.fooTurn = lock.newCondition();
        this.barTurn = lock.newCondition();
        this.isFooTurn = true;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            lock.lock();
            try {
                while (!isFooTurn) {
                    fooTurn.await();
                }
                // printFoo.run() outputs "foo". Do not change or remove this line.
                printFoo.run();
                isFooTurn = false;
                barTurn.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            lock.lock();
            try {
                while (isFooTurn) {
                    barTurn.await();
                }
                // printBar.run() outputs "bar". Do not change or remove this line.
                printBar.run();
                isFooTurn = true;
                fooTurn.signal();
            } finally {
                lock.unlock();
            }
        }
    }
}
```

### 3. Using synchronized and wait/notify

```java
class FooBar {
    private int n;
    private boolean fooTurn = true;
    
    public FooBar(int n) {
        this.n = n;
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            synchronized (this) {
                while (!fooTurn) {
                    this.wait();
                }
                // printFoo.run() outputs "foo". Do not change or remove this line.
                printFoo.run();
                fooTurn = false;
                this.notifyAll();
            }
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            synchronized (this) {
                while (fooTurn) {
                    this.wait();
                }
                // printBar.run() outputs "bar". Do not change or remove this line.
                printBar.run();
                fooTurn = true;
                this.notifyAll();
            }
        }
    }
}
```

### 4. Using BlockingQueue

```java
class FooBar {
    private int n;
    private BlockingQueue<Integer> fooQueue;
    private BlockingQueue<Integer> barQueue;
    
    public FooBar(int n) {
        this.n = n;
        this.fooQueue = new LinkedBlockingQueue<>();
        this.barQueue = new LinkedBlockingQueue<>();
        fooQueue.offer(1); // Initialize with a permit for foo to start
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            fooQueue.take(); // Wait for permit
            // printFoo.run() outputs "foo". Do not change or remove this line.
            printFoo.run();
            barQueue.offer(1); // Give permit to bar
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            barQueue.take(); // Wait for permit
            // printBar.run() outputs "bar". Do not change or remove this line.
            printBar.run();
            fooQueue.offer(1); // Give permit to foo
        }
    }
}
```

### 5. Using AtomicBoolean

```java
class FooBar {
    private int n;
    private AtomicBoolean fooTurn;
    
    public FooBar(int n) {
        this.n = n;
        this.fooTurn = new AtomicBoolean(true);
    }

    public void foo(Runnable printFoo) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            while (!fooTurn.get()) {
                Thread.yield(); // Be nice to other threads
            }
            // printFoo.run() outputs "foo". Do not change or remove this line.
            printFoo.run();
            fooTurn.set(false);
        }
    }

    public void bar(Runnable printBar) throws InterruptedException {
        for (int i = 0; i < n; i++) {
            while (fooTurn.get()) {
                Thread.yield(); // Be nice to other threads
            }
            // printBar.run() outputs "bar". Do not change or remove this line.
            printBar.run();
            fooTurn.set(true);
        }
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: O(n) where n is the number of times to print "foobar"
- Space Complexity: O(1) as we only use a constant amount of extra space

## Key Insights

1. This problem demonstrates the need for alternating execution between two threads.
2. The Semaphore approach is elegant, using permits to control which thread can execute.
3. The ReentrantLock with Condition approach provides more flexibility but is more complex.
4. The synchronized with wait/notify approach is the classic Java way to handle thread coordination.
5. The BlockingQueue approach uses a queue-based synchronization mechanism.
6. The AtomicBoolean approach uses atomic operations to ensure thread safety but may cause high CPU usage due to busy waiting.

## Interview Tips

- Be prepared to discuss the trade-offs between different synchronization mechanisms.
- Understand that busy waiting solutions (like the AtomicBoolean approach) are generally less efficient but simpler to implement.
- Know how to use java.util.concurrent classes like Semaphore, ReentrantLock, and BlockingQueue.
- Be able to explain the concept of thread safety and how it applies to this problem.
- Consider edge cases like what happens if the threads are started in a different order than expected.
- Discuss how the solution would scale if there were more than two threads that needed to execute in sequence.


### 3. Print Zero Even Odd (Medium)


## Problem Description

You have a function `printNumber` that can be called with an integer parameter and prints it to the console.

* For example, calling `printNumber(7)` prints `7` to the console.

You are given an instance of the class `ZeroEvenOdd` that has three functions: `zero`, `even`, and `odd`. The same instance of `ZeroEvenOdd` will be passed to three different threads:

* **Thread A:** calls `zero()` that should only output `0`'s.
* **Thread B:** calls `even()` that should only output even numbers.
* **Thread C:** calls `odd()` that should only output odd numbers.

Modify the given class to output the series `"010203040506..."` where the length of the series must be `2n`.

Implement the `ZeroEvenOdd` class:

* `ZeroEvenOdd(int n)` Initializes the object with the number `n` that represents the numbers that should be printed.
* `void zero(printNumber)` Calls `printNumber` to output one zero.
* `void even(printNumber)` Calls `printNumber` to output one even number.
* `void odd(printNumber)` Calls `printNumber` to output one odd number.

**Example 1:**
- Input: n = 2
- Output: "0102"
- Explanation: There are three threads being fired asynchronously. One of them calls zero(), the other calls even(), and the last one calls odd(). "0102" is the correct output.

**Example 2:**
- Input: n = 5
- Output: "0102030405"

## Video Explanation
[Java Concurrency: Print Zero Even Odd (9:20 minutes)](https://www.youtube.com/watch?v=Nn3o9AnYfgE)

## Solution Approach

This problem tests your understanding of thread coordination with three threads that need to work together in a specific pattern. There are several ways to solve it:

### 1. Using Semaphores

```java
class ZeroEvenOdd {
    private int n;
    private Semaphore zeroSem;
    private Semaphore evenSem;
    private Semaphore oddSem;
    
    public ZeroEvenOdd(int n) {
        this.n = n;
        this.zeroSem = new Semaphore(1);
        this.evenSem = new Semaphore(0);
        this.oddSem = new Semaphore(0);
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            zeroSem.acquire();
            printNumber.accept(0);
            
            if (i % 2 == 0) {
                evenSem.release();
            } else {
                oddSem.release();
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        for (int i = 2; i <= n; i += 2) {
            evenSem.acquire();
            printNumber.accept(i);
            zeroSem.release();
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i += 2) {
            oddSem.acquire();
            printNumber.accept(i);
            zeroSem.release();
        }
    }
}
```

### 2. Using ReentrantLock and Conditions

```java
class ZeroEvenOdd {
    private int n;
    private int state; // 0: print zero, 1: print odd, 2: print even
    private int current; // current number to print
    private Lock lock;
    private Condition condition;
    
    public ZeroEvenOdd(int n) {
        this.n = n;
        this.state = 0;
        this.current = 1;
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        lock.lock();
        try {
            for (int i = 1; i <= n; i++) {
                while (state != 0) {
                    condition.await();
                }
                
                printNumber.accept(0);
                
                if (i % 2 == 1) {
                    state = 1; // next is odd
                } else {
                    state = 2; // next is even
                }
                
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        lock.lock();
        try {
            for (int i = 2; i <= n; i += 2) {
                while (state != 2 || current != i) {
                    condition.await();
                }
                
                printNumber.accept(i);
                current++;
                state = 0; // next is zero
                
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        lock.lock();
        try {
            for (int i = 1; i <= n; i += 2) {
                while (state != 1 || current != i) {
                    condition.await();
                }
                
                printNumber.accept(i);
                current++;
                state = 0; // next is zero
                
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }
}
```

### 3. Using synchronized and wait/notify

```java
class ZeroEvenOdd {
    private int n;
    private int state; // 0: print zero, 1: print odd, 2: print even
    private int current; // current number to print
    
    public ZeroEvenOdd(int n) {
        this.n = n;
        this.state = 0;
        this.current = 1;
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            synchronized (this) {
                while (state != 0) {
                    this.wait();
                }
                
                printNumber.accept(0);
                
                if (i % 2 == 1) {
                    state = 1; // next is odd
                } else {
                    state = 2; // next is even
                }
                
                this.notifyAll();
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        for (int i = 2; i <= n; i += 2) {
            synchronized (this) {
                while (state != 2 || current != i) {
                    this.wait();
                }
                
                printNumber.accept(i);
                current++;
                state = 0; // next is zero
                
                this.notifyAll();
            }
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i += 2) {
            synchronized (this) {
                while (state != 1 || current != i) {
                    this.wait();
                }
                
                printNumber.accept(i);
                current++;
                state = 0; // next is zero
                
                this.notifyAll();
            }
        }
    }
}
```

### 4. Using CountDownLatch

```java
class ZeroEvenOdd {
    private int n;
    private CountDownLatch zeroLatch;
    private CountDownLatch oddLatch;
    private CountDownLatch evenLatch;
    private AtomicInteger counter;
    
    public ZeroEvenOdd(int n) {
        this.n = n;
        this.counter = new AtomicInteger(1);
        this.zeroLatch = new CountDownLatch(0); // zero can start immediately
        this.oddLatch = new CountDownLatch(1); // odd waits for first zero
        this.evenLatch = new CountDownLatch(1); // even waits for first odd
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i++) {
            zeroLatch.await();
            
            printNumber.accept(0);
            
            // Reset zero latch for next iteration
            zeroLatch = new CountDownLatch(1);
            
            if (i % 2 == 1) {
                oddLatch.countDown(); // Signal odd to print
            } else {
                evenLatch.countDown(); // Signal even to print
            }
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        for (int i = 2; i <= n; i += 2) {
            evenLatch.await();
            
            printNumber.accept(i);
            
            // Reset even latch for next iteration
            evenLatch = new CountDownLatch(1);
            
            zeroLatch.countDown(); // Signal zero to print
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        for (int i = 1; i <= n; i += 2) {
            oddLatch.await();
            
            printNumber.accept(i);
            
            // Reset odd latch for next iteration
            oddLatch = new CountDownLatch(1);
            
            zeroLatch.countDown(); // Signal zero to print
        }
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: O(n) where n is the number of integers to print
- Space Complexity: O(1) as we only use a constant amount of extra space

## Key Insights

1. This problem demonstrates the need for coordinating three threads in a specific pattern.
2. The Semaphore approach is elegant, using permits to control which thread can execute.
3. The ReentrantLock with Condition approach provides more flexibility but is more complex.
4. The synchronized with wait/notify approach is the classic Java way to handle thread coordination.
5. The CountDownLatch approach uses a one-time signal mechanism, requiring new latches for each iteration.
6. The state variable is crucial for tracking which thread should execute next.

## Interview Tips

- Be prepared to discuss the trade-offs between different synchronization mechanisms.
- Understand the pattern: zero -> odd/even -> zero -> odd/even -> ...
- Know how to use java.util.concurrent classes like Semaphore, ReentrantLock, and CountDownLatch.
- Be able to explain the concept of thread safety and how it applies to this problem.
- Consider edge cases like what happens if n = 0 or if the threads are started in a different order than expected.
- Discuss how the solution would scale if there were more threads that needed to execute in a specific pattern.


### 4. Building H2O (Medium)


## Problem Description

There are two kinds of threads: `oxygen` and `hydrogen`. Your goal is to group these threads to form water molecules.

There is a barrier where each thread has to wait until a complete molecule can be formed. Hydrogen and oxygen threads will be given `releaseHydrogen` and `releaseOxygen` methods respectively, which will allow them to pass the barrier. These threads should pass the barrier in groups of three, and they must immediately bond with each other to form a water molecule. You must guarantee that all the threads from one molecule bond before any other threads from the next molecule do.

In other words:

* If an oxygen thread arrives at the barrier when no hydrogen threads are present, it must wait for two hydrogen threads.
* If a hydrogen thread arrives at the barrier when no other threads are present, it must wait for an oxygen thread and another hydrogen thread.

We do not have to worry about matching the threads up explicitly; the threads do not necessarily know which other threads they are paired up with. The key is that threads pass the barriers in complete sets; thus, if we examine the sequence of threads that bind and divide them into groups of three, each group should contain one oxygen and two hydrogen threads.

Write synchronization code for oxygen and hydrogen molecules that enforces these constraints.

**Example 1:**
- Input: water = "HOH"
- Output: "HHO"
- Explanation: "HOH" and "OHH" are also valid answers.

**Example 2:**
- Input: water = "OOHHHH"
- Output: "HHOHHO"
- Explanation: "HOHHHO", "OHHHHO", "HHOHOH", "HOHHOH", "OHHHOH", "HHOOHH", "HOHOHH" and "OHHOHH" are also valid answers.

## Video Explanation
[Java Concurrency: Building H2O (8:15 minutes)](https://www.youtube.com/watch?v=Gi-rTT9Aejw)

## Solution Approach

This problem tests your understanding of thread coordination and barrier synchronization. There are several ways to solve it:

### 1. Using Semaphores

```java
class H2O {
    private Semaphore hydrogenSemaphore;
    private Semaphore oxygenSemaphore;
    private Semaphore barrierSemaphore;
    private int hydrogenCount;
    
    public H2O() {
        hydrogenSemaphore = new Semaphore(2); // Allow 2 hydrogen threads
        oxygenSemaphore = new Semaphore(1);   // Allow 1 oxygen thread
        barrierSemaphore = new Semaphore(0);  // Barrier semaphore
        hydrogenCount = 0;
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        hydrogenSemaphore.acquire(); // Acquire permit for hydrogen
        
        // Critical section
        releaseHydrogen.run();
        
        // Increment hydrogen count and check if we have 2 hydrogen
        synchronized (this) {
            hydrogenCount++;
            if (hydrogenCount == 2) {
                // If we have 2 hydrogen, release the barrier
                barrierSemaphore.release(3); // Release for all 3 threads
                hydrogenCount = 0;
            }
        }
        
        barrierSemaphore.acquire(); // Wait at the barrier
        hydrogenSemaphore.release(); // Release permit for next hydrogen
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        oxygenSemaphore.acquire(); // Acquire permit for oxygen
        
        // Critical section
        releaseOxygen.run();
        
        barrierSemaphore.acquire(); // Wait at the barrier
        oxygenSemaphore.release(); // Release permit for next oxygen
    }
}
```

### 2. Using CyclicBarrier

```java
class H2O {
    private CyclicBarrier barrier;
    private Semaphore hydrogenSemaphore;
    private Semaphore oxygenSemaphore;
    
    public H2O() {
        barrier = new CyclicBarrier(3); // Barrier for 3 threads
        hydrogenSemaphore = new Semaphore(2); // Allow 2 hydrogen threads
        oxygenSemaphore = new Semaphore(1);   // Allow 1 oxygen thread
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        hydrogenSemaphore.acquire(); // Acquire permit for hydrogen
        try {
            // Critical section
            releaseHydrogen.run();
            
            // Wait at the barrier
            barrier.await();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        } finally {
            hydrogenSemaphore.release(); // Release permit for next hydrogen
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        oxygenSemaphore.acquire(); // Acquire permit for oxygen
        try {
            // Critical section
            releaseOxygen.run();
            
            // Wait at the barrier
            barrier.await();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        } finally {
            oxygenSemaphore.release(); // Release permit for next oxygen
        }
    }
}
```

### 3. Using Phaser

```java
class H2O {
    private Phaser phaser;
    private Semaphore hydrogenSemaphore;
    private Semaphore oxygenSemaphore;
    
    public H2O() {
        phaser = new Phaser(3); // Register 3 parties
        hydrogenSemaphore = new Semaphore(2); // Allow 2 hydrogen threads
        oxygenSemaphore = new Semaphore(1);   // Allow 1 oxygen thread
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        hydrogenSemaphore.acquire(); // Acquire permit for hydrogen
        try {
            // Critical section
            releaseHydrogen.run();
            
            // Wait for all parties to arrive
            phaser.arriveAndAwaitAdvance();
        } finally {
            hydrogenSemaphore.release(); // Release permit for next hydrogen
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        oxygenSemaphore.acquire(); // Acquire permit for oxygen
        try {
            // Critical section
            releaseOxygen.run();
            
            // Wait for all parties to arrive
            phaser.arriveAndAwaitAdvance();
        } finally {
            oxygenSemaphore.release(); // Release permit for next oxygen
        }
    }
}
```

### 4. Using ReentrantLock and Conditions

```java
class H2O {
    private Lock lock;
    private Condition condition;
    private int hydrogenCount;
    private int oxygenCount;
    
    public H2O() {
        lock = new ReentrantLock();
        condition = lock.newCondition();
        hydrogenCount = 0;
        oxygenCount = 0;
    }

    public void hydrogen(Runnable releaseHydrogen) throws InterruptedException {
        lock.lock();
        try {
            // Wait until we can add a hydrogen (need at most 2 hydrogen per molecule)
            while (hydrogenCount == 2 && oxygenCount < 1) {
                condition.await();
            }
            
            // Critical section
            releaseHydrogen.run();
            hydrogenCount++;
            
            // Check if we have a complete molecule
            if (hydrogenCount == 2 && oxygenCount == 1) {
                // Reset counts for next molecule
                hydrogenCount = 0;
                oxygenCount = 0;
                // Signal all waiting threads
                condition.signalAll();
            } else {
                // Signal other threads
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }

    public void oxygen(Runnable releaseOxygen) throws InterruptedException {
        lock.lock();
        try {
            // Wait until we can add an oxygen (need at most 1 oxygen per molecule)
            while (oxygenCount == 1 && hydrogenCount < 2) {
                condition.await();
            }
            
            // Critical section
            releaseOxygen.run();
            oxygenCount++;
            
            // Check if we have a complete molecule
            if (hydrogenCount == 2 && oxygenCount == 1) {
                // Reset counts for next molecule
                hydrogenCount = 0;
                oxygenCount = 0;
                // Signal all waiting threads
                condition.signalAll();
            } else {
                // Signal other threads
                condition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: O(n) where n is the number of water molecules to form
- Space Complexity: O(1) as we only use a constant amount of extra space

## Key Insights

1. This problem demonstrates the need for coordinating multiple threads to form groups.
2. The Semaphore approach controls access to the critical section and uses a barrier for synchronization.
3. The CyclicBarrier approach automatically resets after each group of threads passes the barrier.
4. The Phaser approach is similar to CyclicBarrier but provides more flexibility.
5. The ReentrantLock with Condition approach uses explicit signaling to coordinate threads.
6. Counting the number of hydrogen and oxygen threads is crucial for forming complete molecules.

## Interview Tips

- Be prepared to discuss the trade-offs between different synchronization mechanisms.
- Understand the pattern: 2 hydrogen + 1 oxygen = 1 water molecule.
- Know how to use java.util.concurrent classes like Semaphore, CyclicBarrier, and Phaser.
- Be able to explain the concept of thread safety and how it applies to this problem.
- Consider edge cases like what happens if threads arrive in different orders.
- Discuss how the solution would scale if there were more types of atoms or different molecule compositions.


### 5. Design Bounded Blocking Queue (Medium)


## Problem Description

Implement a thread-safe bounded blocking queue that has the following methods:

* `BoundedBlockingQueue(int capacity)` The constructor initializes the queue with a maximum capacity.
* `void enqueue(int element)` Adds an element to the front of the queue. If the queue is full, the calling thread is blocked until the queue is no longer full.
* `int dequeue()` Returns the element at the rear of the queue and removes it. If the queue is empty, the calling thread is blocked until the queue is no longer empty.
* `int size()` Returns the number of elements currently in the queue.

Your implementation will be tested using multiple threads at the same time. Each thread will either be a producer thread that only makes calls to the `enqueue` method or a consumer thread that only makes calls to the `dequeue` method. The `size` method will be called after every test case.

**Example 1:**
```
Input:
["BoundedBlockingQueue","enqueue","dequeue","dequeue","enqueue","enqueue","enqueue","enqueue","dequeue"]
[[2],[1],[],[],[0],[2],[3],[4],[]]

Output:
[1,0,2,2]

Explanation:
Number of producer threads = 1
Number of consumer threads = 1

BoundedBlockingQueue queue = new BoundedBlockingQueue(2);   // initialize the queue with capacity = 2.

queue.enqueue(1);   // The producer thread enqueues 1 to the queue.
queue.dequeue();    // The consumer thread calls dequeue and returns 1 from the queue.
queue.dequeue();    // Since the queue is empty, the consumer thread is blocked.
queue.enqueue(0);   // The producer thread enqueues 0 to the queue. The consumer thread is unblocked and returns 0 from the queue.
queue.enqueue(2);   // The producer thread enqueues 2 to the queue.
queue.enqueue(3);   // The producer thread enqueues 3 to the queue.
queue.enqueue(4);   // The producer thread is blocked because the queue's capacity (2) is reached.
queue.dequeue();    // The consumer thread returns 2 from the queue. The producer thread is unblocked and enqueues 4 to the queue.
queue.size();       // 2 elements remaining in the queue. size() is always called at the end of each test case.
```

**Example 2:**
```
Input:
["BoundedBlockingQueue","enqueue","enqueue","enqueue","dequeue","dequeue","dequeue","enqueue"]
[[3],[1],[0],[2],[],[],[],[3]]

Output:
[1,0,2,1]
```

## Video Explanation
[Java Concurrency: Bounded Blocking Queue (7:45 minutes)](https://www.youtube.com/watch?v=0sZrLmL6C0w)

## Solution Approach

This problem tests your understanding of thread synchronization and blocking operations. There are several ways to solve it:

### 1. Using ArrayBlockingQueue (Built-in Solution)

```java
class BoundedBlockingQueue {
    private ArrayBlockingQueue<Integer> queue;
    
    public BoundedBlockingQueue(int capacity) {
        this.queue = new ArrayBlockingQueue<>(capacity);
    }
    
    public void enqueue(int element) throws InterruptedException {
        queue.put(element); // Blocks if the queue is full
    }
    
    public int dequeue() throws InterruptedException {
        return queue.take(); // Blocks if the queue is empty
    }
    
    public int size() {
        return queue.size();
    }
}
```

### 2. Using ReentrantLock and Conditions

```java
class BoundedBlockingQueue {
    private final int capacity;
    private final Queue<Integer> queue;
    private final Lock lock;
    private final Condition notFull;
    private final Condition notEmpty;
    
    public BoundedBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();
    }
    
    public void enqueue(int element) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                notFull.await(); // Wait until the queue is not full
            }
            
            queue.offer(element);
            notEmpty.signal(); // Signal that the queue is not empty
        } finally {
            lock.unlock();
        }
    }
    
    public int dequeue() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await(); // Wait until the queue is not empty
            }
            
            int element = queue.poll();
            notFull.signal(); // Signal that the queue is not full
            return element;
        } finally {
            lock.unlock();
        }
    }
    
    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
}
```

### 3. Using Semaphores

```java
class BoundedBlockingQueue {
    private final Queue<Integer> queue;
    private final Semaphore enqSemaphore; // Controls enqueue operations
    private final Semaphore deqSemaphore; // Controls dequeue operations
    private final Semaphore mutex; // Ensures thread-safe access to the queue
    
    public BoundedBlockingQueue(int capacity) {
        this.queue = new LinkedList<>();
        this.enqSemaphore = new Semaphore(capacity); // Initially, can enqueue up to capacity elements
        this.deqSemaphore = new Semaphore(0); // Initially, cannot dequeue (queue is empty)
        this.mutex = new Semaphore(1); // Mutex for queue access
    }
    
    public void enqueue(int element) throws InterruptedException {
        enqSemaphore.acquire(); // Acquire permit to enqueue (blocks if queue is full)
        
        mutex.acquire(); // Acquire mutex to access the queue
        try {
            queue.offer(element);
        } finally {
            mutex.release(); // Release mutex
        }
        
        deqSemaphore.release(); // Release permit to dequeue (signal that queue is not empty)
    }
    
    public int dequeue() throws InterruptedException {
        deqSemaphore.acquire(); // Acquire permit to dequeue (blocks if queue is empty)
        
        mutex.acquire(); // Acquire mutex to access the queue
        int element;
        try {
            element = queue.poll();
        } finally {
            mutex.release(); // Release mutex
        }
        
        enqSemaphore.release(); // Release permit to enqueue (signal that queue is not full)
        return element;
    }
    
    public int size() {
        mutex.acquireUninterruptibly(); // Acquire mutex to access the queue
        try {
            return queue.size();
        } finally {
            mutex.release(); // Release mutex
        }
    }
}
```

### 4. Using synchronized and wait/notify

```java
class BoundedBlockingQueue {
    private final int capacity;
    private final Queue<Integer> queue;
    
    public BoundedBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }
    
    public void enqueue(int element) throws InterruptedException {
        synchronized (this) {
            while (queue.size() == capacity) {
                this.wait(); // Wait until the queue is not full
            }
            
            queue.offer(element);
            this.notifyAll(); // Notify waiting threads
        }
    }
    
    public int dequeue() throws InterruptedException {
        synchronized (this) {
            while (queue.isEmpty()) {
                this.wait(); // Wait until the queue is not empty
            }
            
            int element = queue.poll();
            this.notifyAll(); // Notify waiting threads
            return element;
        }
    }
    
    public int size() {
        synchronized (this) {
            return queue.size();
        }
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: 
  - enqueue: O(1) amortized
  - dequeue: O(1) amortized
  - size: O(1)
- Space Complexity: O(capacity) for storing the elements in the queue

## Key Insights

1. This problem demonstrates the classic producer-consumer pattern with bounded buffer.
2. The ArrayBlockingQueue solution is the simplest and most efficient, as it's specifically designed for this use case.
3. The ReentrantLock with Condition approach provides more flexibility and control over the locking behavior.
4. The Semaphore approach separates the concerns of queue fullness, emptiness, and thread-safe access.
5. The synchronized with wait/notify approach is the classic Java way to handle thread coordination.
6. Blocking operations are essential for this problem, as threads need to wait when the queue is full or empty.

## Interview Tips

- Be prepared to discuss the trade-offs between different synchronization mechanisms.
- Understand the producer-consumer pattern and its applications.
- Know how to use java.util.concurrent classes like ArrayBlockingQueue, ReentrantLock, and Semaphore.
- Be able to explain the concept of thread safety and how it applies to this problem.
- Consider edge cases like what happens if multiple threads try to enqueue or dequeue simultaneously.
- Discuss how the solution would scale with many producer and consumer threads.
- Mention that Java's built-in ArrayBlockingQueue is optimized for this exact use case and would be the preferred solution in practice.


### 6. Fizz Buzz Multithreaded (Medium)


## Problem Description

You have the four functions:
* `printFizz` that prints the word `"fizz"` to the console,
* `printBuzz` that prints the word `"buzz"` to the console,
* `printFizzBuzz` that prints the word `"fizzbuzz"` to the console, and
* `printNumber` that prints a given integer to the console.

You are given an instance of the class `FizzBuzz` that has four functions: `fizz`, `buzz`, `fizzbuzz` and `number`. The same instance of `FizzBuzz` will be passed to four different threads:

* **Thread A:** calls `fizz()` that should output the word `"fizz"`.
* **Thread B:** calls `buzz()` that should output the word `"buzz"`.
* **Thread C:** calls `fizzbuzz()` that should output the word `"fizzbuzz"`.
* **Thread D:** calls `number()` that should only output the integers.

Modify the given class to output the series `[1, 2, "fizz", 4, "buzz", ...]` where the `ith` token (1-indexed) of the series is:

* `"fizzbuzz"` if `i` is divisible by `3` and `5`,
* `"fizz"` if `i` is divisible by `3` and not `5`,
* `"buzz"` if `i` is divisible by `5` and not `3`, or
* `i` if `i` is not divisible by `3` or `5`.

Implement the `FizzBuzz` class:

* `FizzBuzz(int n)` Initializes the object with the number `n` that represents the length of the sequence that should be printed.
* `void fizz(printFizz)` Calls `printFizz` to output `"fizz"`.
* `void buzz(printBuzz)` Calls `printBuzz` to output `"buzz"`.
* `void fizzbuzz(printFizzBuzz)` Calls `printFizzBuzz` to output `"fizzbuzz"`.
* `void number(printNumber)` Calls `printnumber` to output the numbers.

**Example 1:**
- Input: n = 15
- Output: [1,2,"fizz",4,"buzz","fizz",7,8,"fizz","buzz",11,"fizz",13,14,"fizzbuzz"]

**Example 2:**
- Input: n = 5
- Output: [1,2,"fizz",4,"buzz"]

## Video Explanation
[Java Concurrency: Fizz Buzz Multithreaded (6:30 minutes)](https://www.youtube.com/watch?v=sOJt_b5o4Zs)

## Solution Approach

This problem tests your understanding of thread coordination with four threads that need to work together in a specific pattern. There are several ways to solve it:

### 1. Using Semaphores

```java
class FizzBuzz {
    private int n;
    private Semaphore fizz;
    private Semaphore buzz;
    private Semaphore fizzbuzz;
    private Semaphore number;
    private int current;
    
    public FizzBuzz(int n) {
        this.n = n;
        this.fizz = new Semaphore(0);
        this.buzz = new Semaphore(0);
        this.fizzbuzz = new Semaphore(0);
        this.number = new Semaphore(1); // Start with number
        this.current = 1;
    }

    // printFizz.run() outputs "fizz".
    public void fizz(Runnable printFizz) throws InterruptedException {
        while (current <= n) {
            fizz.acquire();
            if (current > n) break;
            printFizz.run();
            current++;
            releaseNext();
        }
    }

    // printBuzz.run() outputs "buzz".
    public void buzz(Runnable printBuzz) throws InterruptedException {
        while (current <= n) {
            buzz.acquire();
            if (current > n) break;
            printBuzz.run();
            current++;
            releaseNext();
        }
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        while (current <= n) {
            fizzbuzz.acquire();
            if (current > n) break;
            printFizzBuzz.run();
            current++;
            releaseNext();
        }
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void number(IntConsumer printNumber) throws InterruptedException {
        while (current <= n) {
            number.acquire();
            if (current > n) break;
            printNumber.accept(current);
            current++;
            releaseNext();
        }
    }
    
    private void releaseNext() {
        if (current > n) {
            // Release all semaphores to allow threads to exit
            fizz.release();
            buzz.release();
            fizzbuzz.release();
            number.release();
            return;
        }
        
        if (current % 3 == 0 && current % 5 == 0) {
            fizzbuzz.release();
        } else if (current % 3 == 0) {
            fizz.release();
        } else if (current % 5 == 0) {
            buzz.release();
        } else {
            number.release();
        }
    }
}
```

### 2. Using ReentrantLock and Conditions

```java
class FizzBuzz {
    private int n;
    private int current;
    private Lock lock;
    private Condition condition;
    
    public FizzBuzz(int n) {
        this.n = n;
        this.current = 1;
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
    }

    // printFizz.run() outputs "fizz".
    public void fizz(Runnable printFizz) throws InterruptedException {
        lock.lock();
        try {
            while (current <= n) {
                if (current % 3 == 0 && current % 5 != 0) {
                    printFizz.run();
                    current++;
                    condition.signalAll();
                } else {
                    condition.await();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    // printBuzz.run() outputs "buzz".
    public void buzz(Runnable printBuzz) throws InterruptedException {
        lock.lock();
        try {
            while (current <= n) {
                if (current % 3 != 0 && current % 5 == 0) {
                    printBuzz.run();
                    current++;
                    condition.signalAll();
                } else {
                    condition.await();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        lock.lock();
        try {
            while (current <= n) {
                if (current % 3 == 0 && current % 5 == 0) {
                    printFizzBuzz.run();
                    current++;
                    condition.signalAll();
                } else {
                    condition.await();
                }
            }
        } finally {
            lock.unlock();
        }
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void number(IntConsumer printNumber) throws InterruptedException {
        lock.lock();
        try {
            while (current <= n) {
                if (current % 3 != 0 && current % 5 != 0) {
                    printNumber.accept(current);
                    current++;
                    condition.signalAll();
                } else {
                    condition.await();
                }
            }
        } finally {
            lock.unlock();
        }
    }
}
```

### 3. Using synchronized and wait/notify

```java
class FizzBuzz {
    private int n;
    private int current;
    
    public FizzBuzz(int n) {
        this.n = n;
        this.current = 1;
    }

    // printFizz.run() outputs "fizz".
    public void fizz(Runnable printFizz) throws InterruptedException {
        synchronized (this) {
            while (current <= n) {
                if (current % 3 == 0 && current % 5 != 0) {
                    printFizz.run();
                    current++;
                    this.notifyAll();
                } else {
                    this.wait();
                }
            }
        }
    }

    // printBuzz.run() outputs "buzz".
    public void buzz(Runnable printBuzz) throws InterruptedException {
        synchronized (this) {
            while (current <= n) {
                if (current % 3 != 0 && current % 5 == 0) {
                    printBuzz.run();
                    current++;
                    this.notifyAll();
                } else {
                    this.wait();
                }
            }
        }
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        synchronized (this) {
            while (current <= n) {
                if (current % 3 == 0 && current % 5 == 0) {
                    printFizzBuzz.run();
                    current++;
                    this.notifyAll();
                } else {
                    this.wait();
                }
            }
        }
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void number(IntConsumer printNumber) throws InterruptedException {
        synchronized (this) {
            while (current <= n) {
                if (current % 3 != 0 && current % 5 != 0) {
                    printNumber.accept(current);
                    current++;
                    this.notifyAll();
                } else {
                    this.wait();
                }
            }
        }
    }
}
```

### 4. Using AtomicInteger and Busy Waiting

```java
class FizzBuzz {
    private int n;
    private AtomicInteger current;
    
    public FizzBuzz(int n) {
        this.n = n;
        this.current = new AtomicInteger(1);
    }

    // printFizz.run() outputs "fizz".
    public void fizz(Runnable printFizz) throws InterruptedException {
        int curr;
        while ((curr = current.get()) <= n) {
            if (curr % 3 == 0 && curr % 5 != 0) {
                printFizz.run();
                current.incrementAndGet();
            } else {
                Thread.yield(); // Be nice to other threads
            }
        }
    }

    // printBuzz.run() outputs "buzz".
    public void buzz(Runnable printBuzz) throws InterruptedException {
        int curr;
        while ((curr = current.get()) <= n) {
            if (curr % 3 != 0 && curr % 5 == 0) {
                printBuzz.run();
                current.incrementAndGet();
            } else {
                Thread.yield(); // Be nice to other threads
            }
        }
    }

    // printFizzBuzz.run() outputs "fizzbuzz".
    public void fizzbuzz(Runnable printFizzBuzz) throws InterruptedException {
        int curr;
        while ((curr = current.get()) <= n) {
            if (curr % 3 == 0 && curr % 5 == 0) {
                printFizzBuzz.run();
                current.incrementAndGet();
            } else {
                Thread.yield(); // Be nice to other threads
            }
        }
    }

    // printNumber.accept(x) outputs "x", where x is an integer.
    public void number(IntConsumer printNumber) throws InterruptedException {
        int curr;
        while ((curr = current.get()) <= n) {
            if (curr % 3 != 0 && curr % 5 != 0) {
                printNumber.accept(curr);
                current.incrementAndGet();
            } else {
                Thread.yield(); // Be nice to other threads
            }
        }
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: O(n) where n is the length of the sequence
- Space Complexity: O(1) as we only use a constant amount of extra space

## Key Insights

1. This problem demonstrates the need for coordinating four threads in a specific pattern.
2. The Semaphore approach is elegant, using permits to control which thread can execute.
3. The ReentrantLock with Condition approach provides more flexibility but is more complex.
4. The synchronized with wait/notify approach is the classic Java way to handle thread coordination.
5. The AtomicInteger with busy waiting approach is simple but less efficient as it consumes CPU resources while waiting.
6. The key challenge is ensuring that only one thread executes at a time and that the correct thread executes for each number.

## Interview Tips

- Be prepared to discuss the trade-offs between different synchronization mechanisms.
- Understand the FizzBuzz problem and its multithreaded variant.
- Know how to use java.util.concurrent classes like Semaphore, ReentrantLock, and AtomicInteger.
- Be able to explain the concept of thread safety and how it applies to this problem.
- Consider edge cases like what happens if n = 0 or if the threads are started in a different order than expected.
- Discuss how the solution would scale with a larger value of n.
- Mention that busy waiting solutions are generally less efficient but simpler to implement.


### 7. The Dining Philosophers (Medium)


## Problem Description

Five silent philosophers sit at a round table with bowls of spaghetti. Forks are placed between each pair of adjacent philosophers.

Each philosopher must alternately think and eat. However, a philosopher can only eat spaghetti when they have both left and right forks. Each fork can be held by only one philosopher and so a philosopher can use the fork only if it is not being used by another philosopher. After an individual philosopher finishes eating, they need to put down both forks so that the forks become available to others. A philosopher can take the fork on their right or the one on their left as they become available, but cannot start eating before getting both forks.

Eating is not limited by the remaining amounts of spaghetti or stomach space; an infinite supply and an infinite demand are assumed.

Design a discipline of behavior (a concurrent algorithm) such that no philosopher will starve; i.e., each can forever continue to alternate between eating and thinking, assuming that no philosopher can know when others may want to eat or think.

The philosophers' ids are numbered from **0** to **4** in a **clockwise** order. Implement the function `void wantsToEat(philosopher, pickLeftFork, pickRightFork, eat, putLeftFork, putRightFork)` where:

* `philosopher` is the id of the philosopher who wants to eat.
* `pickLeftFork` and `pickRightFork` are functions you can call to pick the corresponding forks of that philosopher.
* `eat` is a function you can call to let the philosopher eat once he has picked both forks.
* `putLeftFork` and `putRightFork` are functions you can call to put down the corresponding forks of that philosopher.
* The philosophers are assumed to be thinking as long as they are not asking to eat (the function is not being called with their number).

Five threads, each representing a philosopher, will simultaneously use one object of your class to simulate the process. The function may be called for the same philosopher more than once, even before the last call ends.

**Example 1:**
- Input: n = 1
- Output: [[4,2,1],[4,1,1],[0,1,1],[2,2,1],[2,1,1],[2,0,3],[2,1,2],[2,2,2],[4,0,3],[4,1,2],[0,2,1],[4,2,2],[3,2,1],[3,1,1],[0,0,3],[0,1,2],[0,2,2],[1,2,1],[1,1,1],[3,0,3],[3,1,2],[3,2,2],[1,0,3],[1,1,2],[1,2,2]]
- Explanation:
  - n is the number of times each philosopher will call the function.
  - The output array describes the calls you made to the functions controlling the forks and the eat function, its format is:
  - output[i] = [a, b, c] (three integers)
  - a is the id of a philosopher.
  - b specifies the fork: {1 : left, 2 : right}.
  - c specifies the operation: {1 : pick, 2 : put, 3 : eat}.

## Video Explanation
[Java Concurrency: The Dining Philosophers Problem (9:15 minutes)](https://www.youtube.com/watch?v=NbwbQQB7xNQ)

## Solution Approach

This problem is a classic concurrency problem that demonstrates deadlock and resource allocation. There are several ways to solve it:

### 1. Using Semaphores with Resource Hierarchy

```java
class DiningPhilosophers {
    private Semaphore[] forks;
    
    public DiningPhilosophers() {
        forks = new Semaphore[5];
        for (int i = 0; i < 5; i++) {
            forks[i] = new Semaphore(1);
        }
    }

    // call the run() method of any runnable to execute its code
    public void wantsToEat(int philosopher,
                           Runnable pickLeftFork,
                           Runnable pickRightFork,
                           Runnable eat,
                           Runnable putLeftFork,
                           Runnable putRightFork) throws InterruptedException {
        
        int leftFork = philosopher;
        int rightFork = (philosopher + 1) % 5;
        
        // Resource hierarchy solution: always pick the lower-numbered fork first
        if (leftFork < rightFork) {
            forks[leftFork].acquire();
            forks[rightFork].acquire();
        } else {
            forks[rightFork].acquire();
            forks[leftFork].acquire();
        }
        
        pickLeftFork.run();
        pickRightFork.run();
        
        eat.run();
        
        putLeftFork.run();
        putRightFork.run();
        
        forks[leftFork].release();
        forks[rightFork].release();
    }
}
```

### 2. Using ReentrantLock

```java
class DiningPhilosophers {
    private ReentrantLock[] forks;
    
    public DiningPhilosophers() {
        forks = new ReentrantLock[5];
        for (int i = 0; i < 5; i++) {
            forks[i] = new ReentrantLock();
        }
    }

    // call the run() method of any runnable to execute its code
    public void wantsToEat(int philosopher,
                           Runnable pickLeftFork,
                           Runnable pickRightFork,
                           Runnable eat,
                           Runnable putLeftFork,
                           Runnable putRightFork) throws InterruptedException {
        
        int leftFork = philosopher;
        int rightFork = (philosopher + 1) % 5;
        
        // Resource hierarchy solution: always pick the lower-numbered fork first
        if (leftFork < rightFork) {
            forks[leftFork].lock();
            forks[rightFork].lock();
        } else {
            forks[rightFork].lock();
            forks[leftFork].lock();
        }
        
        try {
            pickLeftFork.run();
            pickRightFork.run();
            
            eat.run();
            
            putLeftFork.run();
            putRightFork.run();
        } finally {
            forks[leftFork].unlock();
            forks[rightFork].unlock();
        }
    }
}
```

### 3. Using a Waiter (Arbitrator)

```java
class DiningPhilosophers {
    private Semaphore waiter;
    private Semaphore[] forks;
    
    public DiningPhilosophers() {
        waiter = new Semaphore(4); // Allow at most 4 philosophers to try to eat at the same time
        forks = new Semaphore[5];
        for (int i = 0; i < 5; i++) {
            forks[i] = new Semaphore(1);
        }
    }

    // call the run() method of any runnable to execute its code
    public void wantsToEat(int philosopher,
                           Runnable pickLeftFork,
                           Runnable pickRightFork,
                           Runnable eat,
                           Runnable putLeftFork,
                           Runnable putRightFork) throws InterruptedException {
        
        int leftFork = philosopher;
        int rightFork = (philosopher + 1) % 5;
        
        waiter.acquire(); // Ask permission from the waiter
        
        forks[leftFork].acquire();
        forks[rightFork].acquire();
        
        pickLeftFork.run();
        pickRightFork.run();
        
        eat.run();
        
        putLeftFork.run();
        putRightFork.run();
        
        forks[leftFork].release();
        forks[rightFork].release();
        
        waiter.release(); // Release the waiter
    }
}
```

### 4. Using Even-Odd Solution

```java
class DiningPhilosophers {
    private ReentrantLock[] forks;
    private ReentrantLock mutex;
    
    public DiningPhilosophers() {
        forks = new ReentrantLock[5];
        for (int i = 0; i < 5; i++) {
            forks[i] = new ReentrantLock();
        }
        mutex = new ReentrantLock();
    }

    // call the run() method of any runnable to execute its code
    public void wantsToEat(int philosopher,
                           Runnable pickLeftFork,
                           Runnable pickRightFork,
                           Runnable eat,
                           Runnable putLeftFork,
                           Runnable putRightFork) throws InterruptedException {
        
        int leftFork = philosopher;
        int rightFork = (philosopher + 1) % 5;
        
        // Even philosophers pick right fork first, odd philosophers pick left fork first
        if (philosopher % 2 == 0) {
            forks[rightFork].lock();
            forks[leftFork].lock();
            
            pickRightFork.run();
            pickLeftFork.run();
        } else {
            forks[leftFork].lock();
            forks[rightFork].lock();
            
            pickLeftFork.run();
            pickRightFork.run();
        }
        
        eat.run();
        
        putLeftFork.run();
        putRightFork.run();
        
        forks[leftFork].unlock();
        forks[rightFork].unlock();
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: O(1) for each philosopher's eating attempt
- Space Complexity: O(n) where n is the number of philosophers (5 in this case)

## Key Insights

1. This problem demonstrates the classic deadlock scenario where each philosopher holds one fork and waits for another.
2. The Resource Hierarchy solution prevents deadlock by imposing a total ordering on resource acquisition.
3. The Waiter (Arbitrator) solution prevents deadlock by limiting the number of philosophers who can try to eat simultaneously.
4. The Even-Odd solution prevents deadlock by having philosophers pick up forks in different orders.
5. All solutions ensure that no philosopher will starve, but they differ in their approach to preventing deadlock.

## Interview Tips

- Be prepared to discuss the deadlock problem and its solutions.
- Understand the dining philosophers problem as a classic example of resource allocation and deadlock.
- Know how to use java.util.concurrent classes like Semaphore and ReentrantLock.
- Be able to explain the concept of deadlock and how it applies to this problem.
- Consider edge cases like what happens if all philosophers try to eat simultaneously.
- Discuss how the solution would scale with more philosophers.
- Mention that the resource hierarchy solution is the simplest and most efficient, but the waiter solution might be more intuitive.


### 8. Web Crawler Multithreaded (Medium)


## Problem Description

Given a URL `startUrl` and an interface `HtmlParser`, implement a multi-threaded web crawler to crawl all links that are under the same hostname as `startUrl`. Return all URLs obtained by your web crawler in any order.

Your crawler should:
* Start from the page: `startUrl`
* Call `HtmlParser.getUrls(url)` to get all URLs from a webpage of a given URL
* Do not crawl the same link twice
* Only crawl URLs that are under the same hostname as `startUrl`

The hostname is defined as follows:
* For a URL "http://example.org/foo", the hostname is "example.org"
* For a URL "http://example.com/bar", the hostname is "example.com"
* For a URL "http://api.example.com/baz", the hostname is "api.example.com"

**Example 1:**
```
Input:
startUrl = "http://example.org/"
htmlParser = HtmlParser with the following methods:
  - List<String> getUrls(String url) returns all URLs from a webpage of the given URL.
Output: ["http://example.org/"]
Explanation: The hostname of startUrl is "example.org". The only URL with the same hostname is "http://example.org/".
```

**Example 2:**
```
Input:
startUrl = "http://example.org/foo"
htmlParser = HtmlParser with the following methods:
  - List<String> getUrls(String url) returns all URLs from a webpage of the given URL.
  - getUrls("http://example.org/foo") returns ["http://example.org/bar", "http://example.org/baz", "http://example.com/"]
Output: ["http://example.org/foo", "http://example.org/bar", "http://example.org/baz"]
Explanation: The hostname of startUrl is "example.org". The URLs "http://example.org/foo", "http://example.org/bar", and "http://example.org/baz" have the same hostname. The URL "http://example.com/" has a different hostname, so we don't include it.
```

## Video Explanation
[Java Concurrency: Multithreaded Web Crawler (8:20 minutes)](https://www.youtube.com/watch?v=KZK5rnxBWcU)

## Solution Approach

This problem tests your understanding of multithreaded programming and synchronization. There are several ways to solve it:

### 1. Using ExecutorService and ConcurrentHashSet

```java
/**
 * // This is the HtmlParser's API interface.
 * // You should not implement it, or speculate about its implementation
 * interface HtmlParser {
 *     public List<String> getUrls(String url) {}
 * }
 */
class Solution {
    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
        // Extract hostname
        String hostname = getHostname(startUrl);
        
        // Use a concurrent set to track visited URLs
        Set<String> visited = ConcurrentHashMap.newKeySet();
        visited.add(startUrl);
        
        // Create a thread pool
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        
        // Create a completion service to track task completion
        CompletionService<Void> completionService = new ExecutorCompletionService<>(executor);
        
        // Submit the initial task
        completionService.submit(() -> {
            crawlUrl(startUrl, htmlParser, hostname, visited, completionService);
            return null;
        });
        
        // Track active tasks
        int activeTasks = 1;
        
        // Process completed tasks
        while (activeTasks > 0) {
            try {
                Future<Void> future = completionService.take();
                future.get(); // Wait for task to complete
                activeTasks--;
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        
        // Shutdown the executor
        executor.shutdown();
        
        // Convert set to list and return
        return new ArrayList<>(visited);
    }
    
    private void crawlUrl(String url, HtmlParser htmlParser, String hostname, 
                         Set<String> visited, CompletionService<Void> completionService) {
        // Get all URLs from the current URL
        List<String> urls = htmlParser.getUrls(url);
        
        for (String nextUrl : urls) {
            // Check if the URL has the same hostname and hasn't been visited
            if (getHostname(nextUrl).equals(hostname) && visited.add(nextUrl)) {
                // Submit a new task to crawl the URL
                completionService.submit(() -> {
                    crawlUrl(nextUrl, htmlParser, hostname, visited, completionService);
                    return null;
                });
            }
        }
    }
    
    private String getHostname(String url) {
        // Remove protocol
        int start = url.indexOf("//") + 2;
        // Find the end of hostname
        int end = url.indexOf('/', start);
        return end == -1 ? url.substring(start) : url.substring(start, end);
    }
}
```

### 2. Using ForkJoinPool

```java
class Solution {
    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
        // Extract hostname
        String hostname = getHostname(startUrl);
        
        // Use a concurrent set to track visited URLs
        Set<String> visited = ConcurrentHashMap.newKeySet();
        visited.add(startUrl);
        
        // Create a ForkJoinPool
        ForkJoinPool pool = new ForkJoinPool();
        
        // Submit the task and wait for completion
        pool.invoke(new CrawlTask(startUrl, htmlParser, hostname, visited));
        
        // Convert set to list and return
        return new ArrayList<>(visited);
    }
    
    private class CrawlTask extends RecursiveAction {
        private final String url;
        private final HtmlParser htmlParser;
        private final String hostname;
        private final Set<String> visited;
        
        public CrawlTask(String url, HtmlParser htmlParser, String hostname, Set<String> visited) {
            this.url = url;
            this.htmlParser = htmlParser;
            this.hostname = hostname;
            this.visited = visited;
        }
        
        @Override
        protected void compute() {
            // Get all URLs from the current URL
            List<String> urls = htmlParser.getUrls(url);
            
            // Create a list to hold subtasks
            List<CrawlTask> subtasks = new ArrayList<>();
            
            for (String nextUrl : urls) {
                // Check if the URL has the same hostname and hasn't been visited
                if (getHostname(nextUrl).equals(hostname) && visited.add(nextUrl)) {
                    // Create a new task to crawl the URL
                    CrawlTask task = new CrawlTask(nextUrl, htmlParser, hostname, visited);
                    subtasks.add(task);
                    task.fork(); // Submit the task
                }
            }
            
            // Wait for all subtasks to complete
            for (CrawlTask task : subtasks) {
                task.join();
            }
        }
    }
    
    private String getHostname(String url) {
        // Remove protocol
        int start = url.indexOf("//") + 2;
        // Find the end of hostname
        int end = url.indexOf('/', start);
        return end == -1 ? url.substring(start) : url.substring(start, end);
    }
}
```

### 3. Using CountDownLatch

```java
class Solution {
    public List<String> crawl(String startUrl, HtmlParser htmlParser) {
        // Extract hostname
        String hostname = getHostname(startUrl);
        
        // Use a concurrent set to track visited URLs
        Set<String> visited = ConcurrentHashMap.newKeySet();
        visited.add(startUrl);
        
        // Create a queue to hold URLs to crawl
        Queue<String> queue = new ConcurrentLinkedQueue<>();
        queue.add(startUrl);
        
        // Create a CountDownLatch to track when all threads are done
        CountDownLatch latch = new CountDownLatch(1); // Start with 1
        
        // Create a thread pool
        int numThreads = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        
        // Create an AtomicInteger to track active threads
        AtomicInteger activeThreads = new AtomicInteger(0);
        
        // Start the crawling process
        crawlNext(queue, htmlParser, hostname, visited, executor, latch, activeThreads);
        
        // Wait for all threads to complete
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        // Shutdown the executor
        executor.shutdown();
        
        // Convert set to list and return
        return new ArrayList<>(visited);
    }
    
    private void crawlNext(Queue<String> queue, HtmlParser htmlParser, String hostname, 
                          Set<String> visited, ExecutorService executor, 
                          CountDownLatch latch, AtomicInteger activeThreads) {
        // Get the next URL to crawl
        String url = queue.poll();
        
        if (url != null) {
            // Increment active threads
            activeThreads.incrementAndGet();
            
            // Submit a task to crawl the URL
            executor.submit(() -> {
                // Get all URLs from the current URL
                List<String> urls = htmlParser.getUrls(url);
                
                for (String nextUrl : urls) {
                    // Check if the URL has the same hostname and hasn't been visited
                    if (getHostname(nextUrl).equals(hostname) && visited.add(nextUrl)) {
                        queue.add(nextUrl);
                        // Start a new thread to crawl the next URL
                        crawlNext(queue, htmlParser, hostname, visited, executor, latch, activeThreads);
                    }
                }
                
                // Decrement active threads
                if (activeThreads.decrementAndGet() == 0 && queue.isEmpty()) {
                    // If no active threads and queue is empty, we're done
                    latch.countDown();
                }
            });
        }
    }
    
    private String getHostname(String url) {
        // Remove protocol
        int start = url.indexOf("//") + 2;
        // Find the end of hostname
        int end = url.indexOf('/', start);
        return end == -1 ? url.substring(start) : url.substring(start, end);
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: O(n) where n is the number of URLs under the same hostname
- Space Complexity: O(n) for storing the visited URLs

## Key Insights

1. This problem demonstrates the use of multithreading to improve the performance of web crawling.
2. The ExecutorService approach provides a clean way to manage a pool of worker threads.
3. The ForkJoinPool approach is well-suited for recursive tasks like web crawling.
4. The CountDownLatch approach provides a way to wait for all threads to complete.
5. Concurrent data structures like ConcurrentHashMap and ConcurrentLinkedQueue are essential for thread safety.
6. The key challenge is ensuring that URLs are not crawled multiple times while maximizing parallelism.

## Interview Tips

- Be prepared to discuss the trade-offs between different multithreading approaches.
- Understand the importance of thread safety and how to achieve it using concurrent data structures.
- Know how to use java.util.concurrent classes like ExecutorService, ForkJoinPool, and CountDownLatch.
- Be able to explain the concept of thread pooling and its benefits.
- Consider edge cases like what happens if the web crawler encounters a cycle of URLs.
- Discuss how the solution would scale with a larger number of URLs or a more complex website structure.
- Mention that in a real-world scenario, you would need to handle network errors, timeouts, and rate limiting.


### 9. Traffic Light Controlled Intersection (Easy)


## Problem Description

There is an intersection of two roads. First road is road A where cars travel from North to South in direction 1 and from South to North in direction 2. Second road is road B where cars travel from West to East in direction 3 and from East to West in direction 4.

```
        N
        |
        |
W ---- + ---- E
        |
        |
        S
```

There is a traffic light located at the intersection. The traffic light cycles between green for road A and green for road B. When the light is green for road A, cars can travel in both directions of road A, but cannot travel on road B. When the light is green for road B, cars can travel in both directions of road B, but cannot travel on road A.

Initially, the traffic light is green for road A. When the light is green for road A, it will remain green for a duration of `greenTime1`. After that, it turns yellow for road A and remains yellow for a duration of `yellowTime`. Then, the light turns green for road B for a duration of `greenTime2`. After that, it turns yellow for road B and remains yellow for a duration of `yellowTime`. Then, the light turns green for road A again, and the cycle repeats.

Cars arrive at the intersection from all four directions. Each car has a direction it wants to travel (1, 2, 3, or 4). A car can pass the intersection only when the traffic light is green for its road and the car is at the front of its road.

Implement the `TrafficLight` class:

* `TrafficLight()` Initializes the traffic light with road A initially having a green light.
* `void carArrived(int carId, int roadId, int direction, Runnable turnGreen, Runnable crossCar)` 
  * `carId` is the ID of the car.
  * `roadId` is the ID of the road that the car is on. The road ID is 1 for road A and 2 for road B.
  * `direction` is the direction in which the car is traveling.
  * `turnGreen` is a function that you need to call when the traffic light needs to turn green for the car's road.
  * `crossCar` is a function that you need to call when the car can cross the intersection.

Your task is to implement the `TrafficLight` class to control the traffic at the intersection.

**Example 1:**
```
Input: 
["TrafficLight", "carArrived", "carArrived", "carArrived", "carArrived", "carArrived"]
[[], [1, 1, 1, function() { turnGreen(1); }, function() { crossCar(1); }], [2, 1, 2, function() { turnGreen(1); }, function() { crossCar(2); }], [3, 2, 3, function() { turnGreen(2); }, function() { crossCar(3); }], [4, 2, 4, function() { turnGreen(2); }, function() { crossCar(4); }], [5, 1, 1, function() { turnGreen(1); }, function() { crossCar(5); }]]

Output: 
[null, null, null, null, null, null]

Explanation:
TrafficLight trafficLight = new TrafficLight();
trafficLight.carArrived(1, 1, 1, turnGreen1, crossCar1); // Car 1 arrives at road A from direction 1. Traffic light is green for road A, so car 1 can cross the intersection.
trafficLight.carArrived(2, 1, 2, turnGreen1, crossCar2); // Car 2 arrives at road A from direction 2. Traffic light is green for road A, so car 2 can cross the intersection.
trafficLight.carArrived(3, 2, 3, turnGreen2, crossCar3); // Car 3 arrives at road B from direction 3. Traffic light is green for road A, so car 3 cannot cross the intersection yet.
trafficLight.carArrived(4, 2, 4, turnGreen2, crossCar4); // Car 4 arrives at road B from direction 4. Traffic light is green for road A, so car 4 cannot cross the intersection yet.
trafficLight.carArrived(5, 1, 1, turnGreen1, crossCar5); // Car 5 arrives at road A from direction 1. Traffic light is green for road A, so car 5 can cross the intersection.
```

## Video Explanation
[Java Concurrency: Traffic Light Controlled Intersection (6:45 minutes)](https://www.youtube.com/watch?v=qingedaig)

## Solution Approach

This problem tests your understanding of thread synchronization and mutual exclusion. There are several ways to solve it:

### 1. Using ReentrantLock

```java
class TrafficLight {
    private ReentrantLock lock;
    private boolean isGreenOnRoadA;
    
    public TrafficLight() {
        lock = new ReentrantLock(true); // Fair lock to prevent starvation
        isGreenOnRoadA = true; // Initially green for road A
    }
    
    public void carArrived(
        int carId,           // ID of the car
        int roadId,          // ID of the road the car travels on. Can be 1 (road A) or 2 (road B)
        int direction,       // Direction of the car
        Runnable turnGreen,  // Use turnGreen.run() to turn light to green on current road
        Runnable crossCar    // Use crossCar.run() to make car cross the intersection
    ) {
        lock.lock();
        try {
            // Check if the traffic light needs to be changed
            if ((roadId == 1 && !isGreenOnRoadA) || (roadId == 2 && isGreenOnRoadA)) {
                // Turn the light green for the current road
                turnGreen.run();
                isGreenOnRoadA = (roadId == 1);
            }
            
            // Let the car cross the intersection
            crossCar.run();
        } finally {
            lock.unlock();
        }
    }
}
```

### 2. Using synchronized

```java
class TrafficLight {
    private boolean isGreenOnRoadA;
    
    public TrafficLight() {
        isGreenOnRoadA = true; // Initially green for road A
    }
    
    public synchronized void carArrived(
        int carId,           // ID of the car
        int roadId,          // ID of the road the car travels on. Can be 1 (road A) or 2 (road B)
        int direction,       // Direction of the car
        Runnable turnGreen,  // Use turnGreen.run() to turn light to green on current road
        Runnable crossCar    // Use crossCar.run() to make car cross the intersection
    ) {
        // Check if the traffic light needs to be changed
        if ((roadId == 1 && !isGreenOnRoadA) || (roadId == 2 && isGreenOnRoadA)) {
            // Turn the light green for the current road
            turnGreen.run();
            isGreenOnRoadA = (roadId == 1);
        }
        
        // Let the car cross the intersection
        crossCar.run();
    }
}
```

### 3. Using Semaphores

```java
class TrafficLight {
    private Semaphore semaphore;
    private boolean isGreenOnRoadA;
    
    public TrafficLight() {
        semaphore = new Semaphore(1); // Only one car can cross at a time
        isGreenOnRoadA = true; // Initially green for road A
    }
    
    public void carArrived(
        int carId,           // ID of the car
        int roadId,          // ID of the road the car travels on. Can be 1 (road A) or 2 (road B)
        int direction,       // Direction of the car
        Runnable turnGreen,  // Use turnGreen.run() to turn light to green on current road
        Runnable crossCar    // Use crossCar.run() to make car cross the intersection
    ) {
        try {
            semaphore.acquire();
            
            // Check if the traffic light needs to be changed
            if ((roadId == 1 && !isGreenOnRoadA) || (roadId == 2 && isGreenOnRoadA)) {
                // Turn the light green for the current road
                turnGreen.run();
                isGreenOnRoadA = (roadId == 1);
            }
            
            // Let the car cross the intersection
            crossCar.run();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            semaphore.release();
        }
    }
}
```

### 4. Using AtomicBoolean

```java
class TrafficLight {
    private AtomicBoolean lock;
    private AtomicBoolean isGreenOnRoadA;
    
    public TrafficLight() {
        lock = new AtomicBoolean(false);
        isGreenOnRoadA = new AtomicBoolean(true); // Initially green for road A
    }
    
    public void carArrived(
        int carId,           // ID of the car
        int roadId,          // ID of the road the car travels on. Can be 1 (road A) or 2 (road B)
        int direction,       // Direction of the car
        Runnable turnGreen,  // Use turnGreen.run() to turn light to green on current road
        Runnable crossCar    // Use crossCar.run() to make car cross the intersection
    ) {
        // Try to acquire the lock
        while (!lock.compareAndSet(false, true)) {
            // Busy wait
            Thread.yield();
        }
        
        try {
            // Check if the traffic light needs to be changed
            boolean currentGreen = isGreenOnRoadA.get();
            if ((roadId == 1 && !currentGreen) || (roadId == 2 && currentGreen)) {
                // Turn the light green for the current road
                turnGreen.run();
                isGreenOnRoadA.set(roadId == 1);
            }
            
            // Let the car cross the intersection
            crossCar.run();
        } finally {
            // Release the lock
            lock.set(false);
        }
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: O(1) for each car arrival
- Space Complexity: O(1) as we only use a constant amount of extra space

## Key Insights

1. This problem demonstrates the need for mutual exclusion when accessing shared resources.
2. The ReentrantLock approach provides a clean way to handle mutual exclusion with fairness.
3. The synchronized approach is the simplest but may not be as flexible as other approaches.
4. The Semaphore approach is elegant, using permits to control access to the intersection.
5. The AtomicBoolean approach uses atomic operations to ensure thread safety but may cause high CPU usage due to busy waiting.
6. The key challenge is ensuring that only one car can change the traffic light at a time.

## Interview Tips

- Be prepared to discuss the trade-offs between different synchronization mechanisms.
- Understand the importance of mutual exclusion when accessing shared resources.
- Know how to use java.util.concurrent classes like ReentrantLock, Semaphore, and AtomicBoolean.
- Be able to explain the concept of thread safety and how it applies to this problem.
- Consider edge cases like what happens if many cars arrive at the same time.
- Discuss how the solution would scale with a more complex traffic system.
- Mention that in a real-world scenario, you would need to handle timeouts and fairness to prevent starvation.


### 10. Thread-Safe Counter (Easy)


## Problem Description

Design a thread-safe counter class that supports the following operations:

* `Counter()` Initializes the counter with an initial value of 0.
* `void increment()` Increments the counter by 1.
* `void decrement()` Decrements the counter by 1.
* `int getCount()` Returns the current count.

Multiple threads will call the methods of the counter at the same time. The counter should be thread-safe, meaning that the final value of the counter should be correct regardless of how the threads are scheduled.

**Example 1:**
```
Input: 
["Counter", "increment", "increment", "getCount", "decrement", "getCount"]
[[], [], [], [], [], []]

Output: 
[null, null, null, 2, null, 1]

Explanation:
Counter counter = new Counter(); // Initialize the counter to 0
counter.increment(); // Increment the counter to 1
counter.increment(); // Increment the counter to 2
counter.getCount(); // Return the current count, which is 2
counter.decrement(); // Decrement the counter to 1
counter.getCount(); // Return the current count, which is 1
```

**Example 2:**
```
Input: 
["Counter", "increment", "decrement", "increment", "increment", "getCount"]
[[], [], [], [], [], []]

Output: 
[null, null, null, null, null, 2]

Explanation:
Counter counter = new Counter(); // Initialize the counter to 0
counter.increment(); // Increment the counter to 1
counter.decrement(); // Decrement the counter to 0
counter.increment(); // Increment the counter to 1
counter.increment(); // Increment the counter to 2
counter.getCount(); // Return the current count, which is 2
```

## Video Explanation
[Java Concurrency: Thread-Safe Counter (5:30 minutes)](https://www.youtube.com/watch?v=CuWNrBbj3Qs)

## Solution Approach

This problem tests your understanding of thread safety and synchronization. There are several ways to solve it:

### 1. Using synchronized Methods

```java
class Counter {
    private int count;
    
    public Counter() {
        count = 0;
    }
    
    public synchronized void increment() {
        count++;
    }
    
    public synchronized void decrement() {
        count--;
    }
    
    public synchronized int getCount() {
        return count;
    }
}
```

### 2. Using AtomicInteger

```java
import java.util.concurrent.atomic.AtomicInteger;

class Counter {
    private AtomicInteger count;
    
    public Counter() {
        count = new AtomicInteger(0);
    }
    
    public void increment() {
        count.incrementAndGet();
    }
    
    public void decrement() {
        count.decrementAndGet();
    }
    
    public int getCount() {
        return count.get();
    }
}
```

### 3. Using ReentrantLock

```java
import java.util.concurrent.locks.ReentrantLock;

class Counter {
    private int count;
    private ReentrantLock lock;
    
    public Counter() {
        count = 0;
        lock = new ReentrantLock();
    }
    
    public void increment() {
        lock.lock();
        try {
            count++;
        } finally {
            lock.unlock();
        }
    }
    
    public void decrement() {
        lock.lock();
        try {
            count--;
        } finally {
            lock.unlock();
        }
    }
    
    public int getCount() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }
}
```

### 4. Using ReadWriteLock

```java
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Counter {
    private int count;
    private ReadWriteLock lock;
    
    public Counter() {
        count = 0;
        lock = new ReentrantReadWriteLock();
    }
    
    public void increment() {
        lock.writeLock().lock();
        try {
            count++;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public void decrement() {
        lock.writeLock().lock();
        try {
            count--;
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public int getCount() {
        lock.readLock().lock();
        try {
            return count;
        } finally {
            lock.readLock().unlock();
        }
    }
}
```

### 5. Using Volatile with Synchronized Blocks

```java
class Counter {
    private volatile int count;
    
    public Counter() {
        count = 0;
    }
    
    public void increment() {
        synchronized (this) {
            count++;
        }
    }
    
    public void decrement() {
        synchronized (this) {
            count--;
        }
    }
    
    public int getCount() {
        // No need for synchronization for a volatile read
        return count;
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: O(1) for all operations
- Space Complexity: O(1) as we only use a constant amount of extra space

## Key Insights

1. This problem demonstrates the need for thread safety when multiple threads access shared data.
2. The synchronized method approach is the simplest but may not be as efficient as other approaches.
3. The AtomicInteger approach is elegant and efficient, using atomic operations to ensure thread safety.
4. The ReentrantLock approach provides more flexibility but is more complex.
5. The ReadWriteLock approach allows multiple readers but only one writer, which can improve performance in read-heavy scenarios.
6. The volatile with synchronized blocks approach ensures visibility across threads but still requires synchronization for compound operations.

## Interview Tips

- Be prepared to discuss the trade-offs between different synchronization mechanisms.
- Understand the concept of thread safety and how it applies to this problem.
- Know how to use java.util.concurrent classes like AtomicInteger, ReentrantLock, and ReadWriteLock.
- Be able to explain the difference between visibility and atomicity.
- Consider edge cases like what happens if many threads increment and decrement simultaneously.
- Discuss how the solution would scale with a more complex counter (e.g., one that supports multiple operations or has a maximum value).
- Mention that in a real-world scenario, you would need to consider performance implications of different synchronization mechanisms.


### 11. Read-Write Lock (Medium)


## Problem Description

Design a read-write lock class that supports the following operations:

* `ReadWriteLock()` Initializes the read-write lock.
* `void readLock()` Acquires the read lock. Multiple threads can hold the read lock simultaneously, but no thread can hold the write lock at the same time.
* `void readUnlock()` Releases the read lock.
* `void writeLock()` Acquires the write lock. Only one thread can hold the write lock, and no thread can hold the read lock at the same time.
* `void writeUnlock()` Releases the write lock.

The read-write lock should follow these rules:
1. Multiple threads can acquire the read lock simultaneously.
2. Only one thread can acquire the write lock at a time.
3. If a thread holds the write lock, no other thread can acquire the read lock.
4. If one or more threads hold the read lock, no thread can acquire the write lock.
5. A thread can upgrade from a read lock to a write lock, but it must release the read lock first.
6. A thread can downgrade from a write lock to a read lock, but it must release the write lock first.

**Example 1:**
```
Input: 
["ReadWriteLock", "readLock", "readLock", "readUnlock", "writeLock", "readUnlock", "writeUnlock", "readLock"]
[[], [1], [2], [1], [3], [2], [3], [4]]

Output: 
[null, true, true, null, true, null, null, true]

Explanation:
ReadWriteLock lock = new ReadWriteLock();
lock.readLock(1);    // Thread 1 acquires the read lock
lock.readLock(2);    // Thread 2 acquires the read lock
lock.readUnlock(1);  // Thread 1 releases the read lock
lock.writeLock(3);   // Thread 3 cannot acquire the write lock yet because Thread 2 still holds the read lock
lock.readUnlock(2);  // Thread 2 releases the read lock
                     // Now Thread 3 can acquire the write lock
lock.writeUnlock(3); // Thread 3 releases the write lock
lock.readLock(4);    // Thread 4 acquires the read lock
```

## Video Explanation
[Java Concurrency: Read-Write Lock Implementation (7:15 minutes)](https://www.youtube.com/watch?v=7VqWkc9o7RM)

## Solution Approach

This problem tests your understanding of read-write locks and synchronization. There are several ways to solve it:

### 1. Using ReentrantReadWriteLock

```java
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ReadWriteLock {
    private ReentrantReadWriteLock lock;
    
    public ReadWriteLock() {
        lock = new ReentrantReadWriteLock();
    }
    
    public void readLock() {
        lock.readLock().lock();
    }
    
    public void readUnlock() {
        lock.readLock().unlock();
    }
    
    public void writeLock() {
        lock.writeLock().lock();
    }
    
    public void writeUnlock() {
        lock.writeLock().unlock();
    }
}
```

### 2. Custom Implementation Using Semaphores

```java
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

class ReadWriteLock {
    private Semaphore readLock;
    private Semaphore writeLock;
    private AtomicInteger readers;
    
    public ReadWriteLock() {
        readLock = new Semaphore(Integer.MAX_VALUE, true);
        writeLock = new Semaphore(1, true);
        readers = new AtomicInteger(0);
    }
    
    public void readLock() throws InterruptedException {
        readLock.acquire();
        readers.incrementAndGet();
        if (readers.get() == 1) {
            // First reader acquires the write lock
            writeLock.acquire();
        }
        readLock.release();
    }
    
    public void readUnlock() throws InterruptedException {
        readLock.acquire();
        readers.decrementAndGet();
        if (readers.get() == 0) {
            // Last reader releases the write lock
            writeLock.release();
        }
        readLock.release();
    }
    
    public void writeLock() throws InterruptedException {
        writeLock.acquire();
    }
    
    public void writeUnlock() {
        writeLock.release();
    }
}
```

### 3. Custom Implementation Using Synchronized and Wait/Notify

```java
class ReadWriteLock {
    private int readers;
    private boolean isWriteLocked;
    private Thread writerThread;
    private int writeRequests;
    
    public ReadWriteLock() {
        readers = 0;
        isWriteLocked = false;
        writerThread = null;
        writeRequests = 0;
    }
    
    public synchronized void readLock() throws InterruptedException {
        while (isWriteLocked || writeRequests > 0) {
            wait();
        }
        readers++;
    }
    
    public synchronized void readUnlock() {
        readers--;
        notifyAll();
    }
    
    public synchronized void writeLock() throws InterruptedException {
        writeRequests++;
        while (readers > 0 || isWriteLocked) {
            wait();
        }
        writeRequests--;
        isWriteLocked = true;
        writerThread = Thread.currentThread();
    }
    
    public synchronized void writeUnlock() {
        if (Thread.currentThread() != writerThread) {
            throw new IllegalMonitorStateException("Calling thread has not locked this lock for writing");
        }
        isWriteLocked = false;
        writerThread = null;
        notifyAll();
    }
}
```

### 4. Custom Implementation Using Locks and Conditions

```java
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ReadWriteLock {
    private final Lock lock;
    private final Condition readCondition;
    private final Condition writeCondition;
    private int readers;
    private int writers;
    private int writeRequests;
    
    public ReadWriteLock() {
        lock = new ReentrantLock();
        readCondition = lock.newCondition();
        writeCondition = lock.newCondition();
        readers = 0;
        writers = 0;
        writeRequests = 0;
    }
    
    public void readLock() throws InterruptedException {
        lock.lock();
        try {
            while (writers > 0 || writeRequests > 0) {
                readCondition.await();
            }
            readers++;
        } finally {
            lock.unlock();
        }
    }
    
    public void readUnlock() {
        lock.lock();
        try {
            readers--;
            if (readers == 0) {
                writeCondition.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }
    
    public void writeLock() throws InterruptedException {
        lock.lock();
        try {
            writeRequests++;
            while (readers > 0 || writers > 0) {
                writeCondition.await();
            }
            writeRequests--;
            writers++;
        } finally {
            lock.unlock();
        }
    }
    
    public void writeUnlock() {
        lock.lock();
        try {
            writers--;
            readCondition.signalAll();
            writeCondition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: O(1) for all operations in the absence of contention
- Space Complexity: O(1) as we only use a constant amount of extra space

## Key Insights

1. This problem demonstrates the read-write lock pattern, which allows multiple readers but only one writer.
2. The ReentrantReadWriteLock approach is the simplest and most efficient, leveraging Java's built-in implementation.
3. The custom implementations using Semaphores, synchronized blocks, or Locks and Conditions provide more insight into how read-write locks work.
4. The key challenge is ensuring that readers and writers don't interfere with each other while maximizing concurrency.
5. The implementation must handle the case where a writer is waiting while readers are active, and prevent new readers from acquiring the lock in this case to avoid writer starvation.

## Interview Tips

- Be prepared to discuss the trade-offs between different synchronization mechanisms.
- Understand the concept of read-write locks and how they differ from mutual exclusion locks.
- Know how to use java.util.concurrent classes like ReentrantReadWriteLock, Semaphore, and Lock/Condition.
- Be able to explain the difference between reader preference, writer preference, and fair read-write locks.
- Consider edge cases like what happens if a thread tries to release a lock it doesn't hold.
- Discuss how the solution would scale with a large number of readers and writers.
- Mention that in a real-world scenario, you would need to consider performance implications of different synchronization mechanisms, especially for read-heavy or write-heavy workloads.


### 12. Design Thread Pool (Medium)


## Problem Description

Design a thread pool class that supports the following operations:

* `ThreadPool(int capacity)` Initializes the thread pool with a maximum capacity of threads.
* `void execute(Runnable task)` Adds a task to the thread pool for execution. If the pool is at capacity, the task should be queued until a thread becomes available.
* `void shutdown()` Shuts down the thread pool, allowing all currently executing tasks to complete but not accepting new tasks.
* `boolean isShutdown()` Returns whether the thread pool has been shut down.
* `int getActiveCount()` Returns the number of threads currently executing tasks.
* `int getQueueSize()` Returns the number of tasks waiting in the queue.

The thread pool should follow these rules:
1. Tasks should be executed in the order they are submitted (FIFO).
2. The pool should reuse threads rather than creating new ones for each task.
3. If a thread throws an exception, it should be replaced with a new thread.
4. The shutdown method should allow currently executing tasks to complete but not start new ones.

**Example 1:**
```
Input: 
["ThreadPool", "execute", "execute", "getActiveCount", "execute", "getQueueSize", "shutdown", "isShutdown"]
[[2], [task1], [task2], [], [task3], [], [], []]

Output: 
[null, null, null, 2, null, 1, null, true]

Explanation:
ThreadPool pool = new ThreadPool(2); // Initialize with capacity 2
pool.execute(task1);                 // Add task1 for execution
pool.execute(task2);                 // Add task2 for execution
pool.getActiveCount();               // Return 2 (both threads are active)
pool.execute(task3);                 // Add task3, but it's queued since pool is at capacity
pool.getQueueSize();                 // Return 1 (task3 is in the queue)
pool.shutdown();                     // Shut down the pool
pool.isShutdown();                   // Return true
```

## Video Explanation
[Java Concurrency: Thread Pool Implementation (8:45 minutes)](https://www.youtube.com/watch?v=ZcKt5FYd3bU)

## Solution Approach

This problem tests your understanding of thread pools and task execution. There are several ways to solve it:

### 1. Using ExecutorService (Simplified Solution)

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class ThreadPool {
    private ExecutorService executor;
    private BlockingQueue<Runnable> taskQueue;
    private AtomicInteger activeCount;
    private boolean isShutdown;
    
    public ThreadPool(int capacity) {
        taskQueue = new LinkedBlockingQueue<>();
        activeCount = new AtomicInteger(0);
        isShutdown = false;
        
        executor = new ThreadPoolExecutor(
            capacity,                  // Core pool size
            capacity,                  // Maximum pool size
            60L, TimeUnit.SECONDS,     // Keep alive time
            new LinkedBlockingQueue<>(), // Work queue
            new ThreadFactory() {
                @Override
                public Thread newThread(Runnable r) {
                    Thread t = new Thread(r);
                    t.setDaemon(true);
                    return t;
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy() // Rejection policy
        );
        
        // Add a hook to track active count
        ((ThreadPoolExecutor) executor).setThreadFactory(r -> {
            Thread t = new Thread(() -> {
                activeCount.incrementAndGet();
                try {
                    r.run();
                } finally {
                    activeCount.decrementAndGet();
                }
            });
            t.setDaemon(true);
            return t;
        });
    }
    
    public void execute(Runnable task) {
        if (isShutdown) {
            throw new RejectedExecutionException("ThreadPool is shutdown");
        }
        taskQueue.offer(task);
        executor.execute(() -> {
            try {
                Runnable t = taskQueue.poll();
                if (t != null) {
                    t.run();
                }
            } catch (Exception e) {
                // Replace the thread if it throws an exception
                execute(task);
            }
        });
    }
    
    public void shutdown() {
        isShutdown = true;
        executor.shutdown();
    }
    
    public boolean isShutdown() {
        return isShutdown;
    }
    
    public int getActiveCount() {
        return activeCount.get();
    }
    
    public int getQueueSize() {
        return taskQueue.size();
    }
}
```

### 2. Custom Implementation

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class ThreadPool {
    private final int capacity;
    private final BlockingQueue<Runnable> taskQueue;
    private final Thread[] threads;
    private final AtomicInteger activeCount;
    private volatile boolean isShutdown;
    
    public ThreadPool(int capacity) {
        this.capacity = capacity;
        this.taskQueue = new LinkedBlockingQueue<>();
        this.threads = new Thread[capacity];
        this.activeCount = new AtomicInteger(0);
        this.isShutdown = false;
        
        // Initialize worker threads
        for (int i = 0; i < capacity; i++) {
            threads[i] = new WorkerThread();
            threads[i].start();
        }
    }
    
    public void execute(Runnable task) {
        if (isShutdown) {
            throw new RejectedExecutionException("ThreadPool is shutdown");
        }
        taskQueue.offer(task);
    }
    
    public void shutdown() {
        isShutdown = true;
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }
    
    public boolean isShutdown() {
        return isShutdown;
    }
    
    public int getActiveCount() {
        return activeCount.get();
    }
    
    public int getQueueSize() {
        return taskQueue.size();
    }
    
    private class WorkerThread extends Thread {
        @Override
        public void run() {
            while (!isShutdown || !taskQueue.isEmpty()) {
                try {
                    Runnable task = taskQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (task != null) {
                        activeCount.incrementAndGet();
                        try {
                            task.run();
                        } catch (Exception e) {
                            // Log the exception
                            e.printStackTrace();
                        } finally {
                            activeCount.decrementAndGet();
                        }
                    }
                } catch (InterruptedException e) {
                    // Thread was interrupted, check if we should exit
                    if (isShutdown) {
                        break;
                    }
                }
            }
        }
    }
}
```

### 3. Using Semaphores for Capacity Control

```java
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class ThreadPool {
    private final BlockingQueue<Runnable> taskQueue;
    private final Semaphore semaphore;
    private final Thread[] threads;
    private final AtomicInteger activeCount;
    private volatile boolean isShutdown;
    
    public ThreadPool(int capacity) {
        this.taskQueue = new LinkedBlockingQueue<>();
        this.semaphore = new Semaphore(capacity);
        this.threads = new Thread[capacity];
        this.activeCount = new AtomicInteger(0);
        this.isShutdown = false;
        
        // Initialize worker threads
        for (int i = 0; i < capacity; i++) {
            threads[i] = new WorkerThread();
            threads[i].start();
        }
    }
    
    public void execute(Runnable task) {
        if (isShutdown) {
            throw new RejectedExecutionException("ThreadPool is shutdown");
        }
        taskQueue.offer(task);
    }
    
    public void shutdown() {
        isShutdown = true;
        for (Thread thread : threads) {
            thread.interrupt();
        }
    }
    
    public boolean isShutdown() {
        return isShutdown;
    }
    
    public int getActiveCount() {
        return activeCount.get();
    }
    
    public int getQueueSize() {
        return taskQueue.size();
    }
    
    private class WorkerThread extends Thread {
        @Override
        public void run() {
            while (!isShutdown || !taskQueue.isEmpty()) {
                try {
                    Runnable task = taskQueue.poll(100, TimeUnit.MILLISECONDS);
                    if (task != null) {
                        semaphore.acquire();
                        activeCount.incrementAndGet();
                        try {
                            task.run();
                        } catch (Exception e) {
                            // Log the exception
                            e.printStackTrace();
                        } finally {
                            activeCount.decrementAndGet();
                            semaphore.release();
                        }
                    }
                } catch (InterruptedException e) {
                    // Thread was interrupted, check if we should exit
                    if (isShutdown) {
                        break;
                    }
                }
            }
        }
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: O(1) for execute, shutdown, isShutdown, getActiveCount, and getQueueSize operations
- Space Complexity: O(n + m) where n is the capacity of the thread pool and m is the number of tasks in the queue

## Key Insights

1. This problem demonstrates the thread pool pattern, which reuses threads to execute tasks.
2. The ExecutorService approach is the simplest and most efficient, leveraging Java's built-in implementation.
3. The custom implementations provide more insight into how thread pools work.
4. The key challenge is ensuring that tasks are executed in order and that the pool correctly handles exceptions.
5. The implementation must handle the case where the pool is shut down while tasks are still executing.

## Interview Tips

- Be prepared to discuss the trade-offs between different thread pool implementations.
- Understand the concept of thread pools and how they differ from creating a new thread for each task.
- Know how to use java.util.concurrent classes like ExecutorService, ThreadPoolExecutor, and BlockingQueue.
- Be able to explain the difference between fixed thread pools, cached thread pools, and scheduled thread pools.
- Consider edge cases like what happens if a task throws an exception or if the pool is shut down while tasks are still executing.
- Discuss how the solution would scale with a large number of tasks or a high task submission rate.
- Mention that in a real-world scenario, you would need to consider performance implications of different thread pool configurations, especially for CPU-bound or I/O-bound tasks.


### 13. Producer-Consumer Problem (Medium)


## Problem Description

Implement a producer-consumer pattern where:

* One or more producer threads produce items and add them to a shared buffer.
* One or more consumer threads consume items from the shared buffer.
* The buffer has a fixed capacity.
* If the buffer is full, producers must wait until space becomes available.
* If the buffer is empty, consumers must wait until items become available.

Design a class `ProducerConsumer` that supports the following operations:

* `ProducerConsumer(int capacity)` Initializes the buffer with the given capacity.
* `void produce(int item)` Adds an item to the buffer. If the buffer is full, the thread should wait until space becomes available.
* `int consume()` Removes and returns an item from the buffer. If the buffer is empty, the thread should wait until an item becomes available.
* `int getSize()` Returns the current number of items in the buffer.

The producer-consumer implementation should follow these rules:
1. Items should be consumed in the order they are produced (FIFO).
2. Multiple producers and consumers can operate concurrently.
3. The implementation should be thread-safe.
4. The implementation should avoid deadlocks and livelocks.

**Example 1:**
```
Input: 
["ProducerConsumer", "produce", "produce", "consume", "consume", "produce", "consume"]
[[2], [1], [2], [], [], [3], []]

Output: 
[null, null, null, 1, 2, null, 3]

Explanation:
ProducerConsumer pc = new ProducerConsumer(2); // Initialize with capacity 2
pc.produce(1);                                 // Add 1 to the buffer
pc.produce(2);                                 // Add 2 to the buffer
pc.consume();                                  // Remove and return 1 from the buffer
pc.consume();                                  // Remove and return 2 from the buffer
pc.produce(3);                                 // Add 3 to the buffer
pc.consume();                                  // Remove and return 3 from the buffer
```

## Video Explanation
[Java Concurrency: Producer-Consumer Pattern (9:15 minutes)](https://www.youtube.com/watch?v=tEwNXnAmc9c)

## Solution Approach

This problem tests your understanding of thread synchronization and the producer-consumer pattern. There are several ways to solve it:

### 1. Using BlockingQueue

```java
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class ProducerConsumer {
    private final BlockingQueue<Integer> buffer;
    
    public ProducerConsumer(int capacity) {
        buffer = new LinkedBlockingQueue<>(capacity);
    }
    
    public void produce(int item) throws InterruptedException {
        buffer.put(item); // Blocks if buffer is full
    }
    
    public int consume() throws InterruptedException {
        return buffer.take(); // Blocks if buffer is empty
    }
    
    public int getSize() {
        return buffer.size();
    }
}
```

### 2. Using wait() and notify()

```java
class ProducerConsumer {
    private final int[] buffer;
    private int count;
    private int putIndex;
    private int takeIndex;
    
    public ProducerConsumer(int capacity) {
        buffer = new int[capacity];
        count = 0;
        putIndex = 0;
        takeIndex = 0;
    }
    
    public synchronized void produce(int item) throws InterruptedException {
        while (count == buffer.length) {
            // Buffer is full, wait for space
            wait();
        }
        
        buffer[putIndex] = item;
        putIndex = (putIndex + 1) % buffer.length;
        count++;
        
        // Notify waiting consumers
        notifyAll();
    }
    
    public synchronized int consume() throws InterruptedException {
        while (count == 0) {
            // Buffer is empty, wait for items
            wait();
        }
        
        int item = buffer[takeIndex];
        takeIndex = (takeIndex + 1) % buffer.length;
        count--;
        
        // Notify waiting producers
        notifyAll();
        
        return item;
    }
    
    public synchronized int getSize() {
        return count;
    }
}
```

### 3. Using ReentrantLock and Conditions

```java
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class ProducerConsumer {
    private final int[] buffer;
    private int count;
    private int putIndex;
    private int takeIndex;
    private final ReentrantLock lock;
    private final Condition notFull;
    private final Condition notEmpty;
    
    public ProducerConsumer(int capacity) {
        buffer = new int[capacity];
        count = 0;
        putIndex = 0;
        takeIndex = 0;
        lock = new ReentrantLock();
        notFull = lock.newCondition();
        notEmpty = lock.newCondition();
    }
    
    public void produce(int item) throws InterruptedException {
        lock.lock();
        try {
            while (count == buffer.length) {
                // Buffer is full, wait for space
                notFull.await();
            }
            
            buffer[putIndex] = item;
            putIndex = (putIndex + 1) % buffer.length;
            count++;
            
            // Signal waiting consumers
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }
    
    public int consume() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                // Buffer is empty, wait for items
                notEmpty.await();
            }
            
            int item = buffer[takeIndex];
            takeIndex = (takeIndex + 1) % buffer.length;
            count--;
            
            // Signal waiting producers
            notFull.signal();
            
            return item;
        } finally {
            lock.unlock();
        }
    }
    
    public int getSize() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }
}
```

### 4. Using Semaphores

```java
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

class ProducerConsumer {
    private final int[] buffer;
    private int putIndex;
    private int takeIndex;
    private final Semaphore emptySlots;
    private final Semaphore filledSlots;
    private final ReentrantLock putLock;
    private final ReentrantLock takeLock;
    
    public ProducerConsumer(int capacity) {
        buffer = new int[capacity];
        putIndex = 0;
        takeIndex = 0;
        emptySlots = new Semaphore(capacity);
        filledSlots = new Semaphore(0);
        putLock = new ReentrantLock();
        takeLock = new ReentrantLock();
    }
    
    public void produce(int item) throws InterruptedException {
        emptySlots.acquire(); // Wait for an empty slot
        
        putLock.lock();
        try {
            buffer[putIndex] = item;
            putIndex = (putIndex + 1) % buffer.length;
        } finally {
            putLock.unlock();
        }
        
        filledSlots.release(); // Signal that a slot is filled
    }
    
    public int consume() throws InterruptedException {
        filledSlots.acquire(); // Wait for a filled slot
        
        takeLock.lock();
        try {
            int item = buffer[takeIndex];
            takeIndex = (takeIndex + 1) % buffer.length;
            emptySlots.release(); // Signal that a slot is empty
            return item;
        } finally {
            takeLock.unlock();
        }
    }
    
    public int getSize() {
        return filledSlots.availablePermits();
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: O(1) for produce, consume, and getSize operations in the absence of contention
- Space Complexity: O(n) where n is the capacity of the buffer

## Key Insights

1. This problem demonstrates the classic producer-consumer pattern, which is a common concurrency design pattern.
2. The BlockingQueue approach is the simplest and most efficient, leveraging Java's built-in implementation.
3. The wait/notify approach shows how to use Java's intrinsic locking and condition variables.
4. The ReentrantLock and Conditions approach provides more flexibility and control over locking.
5. The Semaphore approach uses counting semaphores to control access to the buffer.
6. The key challenge is ensuring that producers wait when the buffer is full and consumers wait when the buffer is empty.

## Interview Tips

- Be prepared to discuss the trade-offs between different synchronization mechanisms.
- Understand the producer-consumer pattern and how it applies to real-world scenarios.
- Know how to use java.util.concurrent classes like BlockingQueue, ReentrantLock, Condition, and Semaphore.
- Be able to explain the difference between intrinsic locking (synchronized) and explicit locking (ReentrantLock).
- Consider edge cases like what happens if multiple producers or consumers are waiting.
- Discuss how the solution would scale with a large number of producers and consumers.
- Mention that in a real-world scenario, you would need to consider performance implications of different synchronization mechanisms, especially for high-throughput systems.


### 14. Readers-Writers Problem (Medium)


## Problem Description

Implement a readers-writers solution where:

* Multiple reader threads can read a shared resource simultaneously.
* Only one writer thread can write to the shared resource at a time.
* When a writer is writing, no reader can read the resource.
* The implementation should prevent writer starvation (writers should eventually get access even if there are continuous readers).

Design a class `ReadersWriterLock` that supports the following operations:

* `ReadersWriterLock()` Initializes the readers-writer lock.
* `void acquireReadLock()` Acquires a read lock. Multiple threads can hold the read lock simultaneously.
* `void releaseReadLock()` Releases a read lock.
* `void acquireWriteLock()` Acquires a write lock. Only one thread can hold the write lock, and no thread can hold the read lock at the same time.
* `void releaseWriteLock()` Releases a write lock.
* `int getReadersCount()` Returns the current number of readers.
* `boolean isWriterActive()` Returns whether a writer is currently active.

The readers-writers implementation should follow these rules:
1. Multiple readers can read simultaneously.
2. Only one writer can write at a time.
3. When a writer is writing, no reader can read.
4. Writers should not starve (they should eventually get access even if there are continuous readers).

**Example 1:**
```
Input: 
["ReadersWriterLock", "acquireReadLock", "acquireReadLock", "getReadersCount", "acquireWriteLock", "isWriterActive", "releaseReadLock", "releaseReadLock", "isWriterActive", "releaseWriteLock", "isWriterActive"]
[[], [1], [2], [], [3], [], [1], [2], [], [3], []]

Output: 
[null, null, null, 2, null, false, null, null, true, null, false]

Explanation:
ReadersWriterLock lock = new ReadersWriterLock();
lock.acquireReadLock(1);      // Reader 1 acquires the read lock
lock.acquireReadLock(2);      // Reader 2 acquires the read lock
lock.getReadersCount();       // Return 2 (two readers are active)
lock.acquireWriteLock(3);     // Writer 3 tries to acquire the write lock but must wait for readers
lock.isWriterActive();        // Return false (writer is waiting)
lock.releaseReadLock(1);      // Reader 1 releases the read lock
lock.releaseReadLock(2);      // Reader 2 releases the read lock
                              // Now Writer 3 can acquire the write lock
lock.isWriterActive();        // Return true (writer is active)
lock.releaseWriteLock(3);     // Writer 3 releases the write lock
lock.isWriterActive();        // Return false (no writer is active)
```

## Video Explanation
[Java Concurrency: Readers-Writers Problem (8:30 minutes)](https://www.youtube.com/watch?v=HJzlP2yMGBw)

## Solution Approach

This problem tests your understanding of the readers-writers pattern and thread synchronization. There are several ways to solve it:

### 1. Using ReentrantReadWriteLock

```java
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ReadersWriterLock {
    private ReentrantReadWriteLock lock;
    private int readersCount;
    private boolean writerActive;
    
    public ReadersWriterLock() {
        lock = new ReentrantReadWriteLock(true); // Fair lock to prevent writer starvation
        readersCount = 0;
        writerActive = false;
    }
    
    public void acquireReadLock() {
        lock.readLock().lock();
        synchronized (this) {
            readersCount++;
        }
    }
    
    public void releaseReadLock() {
        lock.readLock().unlock();
        synchronized (this) {
            readersCount--;
        }
    }
    
    public void acquireWriteLock() {
        lock.writeLock().lock();
        synchronized (this) {
            writerActive = true;
        }
    }
    
    public void releaseWriteLock() {
        synchronized (this) {
            writerActive = false;
        }
        lock.writeLock().unlock();
    }
    
    public synchronized int getReadersCount() {
        return readersCount;
    }
    
    public synchronized boolean isWriterActive() {
        return writerActive;
    }
}
```

### 2. Using Semaphores

```java
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

class ReadersWriterLock {
    private Semaphore resourceLock;
    private Semaphore readCountLock;
    private AtomicInteger readersCount;
    private boolean writerActive;
    
    public ReadersWriterLock() {
        resourceLock = new Semaphore(1, true); // Fair semaphore to prevent writer starvation
        readCountLock = new Semaphore(1);
        readersCount = new AtomicInteger(0);
        writerActive = false;
    }
    
    public void acquireReadLock() throws InterruptedException {
        readCountLock.acquire();
        try {
            if (readersCount.incrementAndGet() == 1) {
                // First reader acquires the resource lock
                resourceLock.acquire();
            }
        } finally {
            readCountLock.release();
        }
    }
    
    public void releaseReadLock() throws InterruptedException {
        readCountLock.acquire();
        try {
            if (readersCount.decrementAndGet() == 0) {
                // Last reader releases the resource lock
                resourceLock.release();
            }
        } finally {
            readCountLock.release();
        }
    }
    
    public void acquireWriteLock() throws InterruptedException {
        resourceLock.acquire();
        synchronized (this) {
            writerActive = true;
        }
    }
    
    public void releaseWriteLock() {
        synchronized (this) {
            writerActive = false;
        }
        resourceLock.release();
    }
    
    public int getReadersCount() {
        return readersCount.get();
    }
    
    public synchronized boolean isWriterActive() {
        return writerActive;
    }
}
```

### 3. Using synchronized and wait/notify

```java
class ReadersWriterLock {
    private int readersCount;
    private boolean writerActive;
    private int waitingWriters;
    
    public ReadersWriterLock() {
        readersCount = 0;
        writerActive = false;
        waitingWriters = 0;
    }
    
    public synchronized void acquireReadLock() throws InterruptedException {
        // If there's a writer or waiting writers, wait
        while (writerActive || waitingWriters > 0) {
            wait();
        }
        readersCount++;
    }
    
    public synchronized void releaseReadLock() {
        readersCount--;
        // If this is the last reader, notify waiting writers
        if (readersCount == 0) {
            notifyAll();
        }
    }
    
    public synchronized void acquireWriteLock() throws InterruptedException {
        waitingWriters++;
        try {
            // Wait until there are no readers and no active writer
            while (readersCount > 0 || writerActive) {
                wait();
            }
            writerActive = true;
        } finally {
            waitingWriters--;
        }
    }
    
    public synchronized void releaseWriteLock() {
        writerActive = false;
        // Notify all waiting threads
        notifyAll();
    }
    
    public synchronized int getReadersCount() {
        return readersCount;
    }
    
    public synchronized boolean isWriterActive() {
        return writerActive;
    }
}
```

### 4. Using Locks and Conditions

```java
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class ReadersWriterLock {
    private final Lock lock;
    private final Condition noReaders;
    private final Condition noWriter;
    private int readersCount;
    private boolean writerActive;
    private int waitingWriters;
    
    public ReadersWriterLock() {
        lock = new ReentrantLock(true); // Fair lock to prevent writer starvation
        noReaders = lock.newCondition();
        noWriter = lock.newCondition();
        readersCount = 0;
        writerActive = false;
        waitingWriters = 0;
    }
    
    public void acquireReadLock() throws InterruptedException {
        lock.lock();
        try {
            // If there's a writer or waiting writers, wait
            while (writerActive || waitingWriters > 0) {
                noWriter.await();
            }
            readersCount++;
        } finally {
            lock.unlock();
        }
    }
    
    public void releaseReadLock() {
        lock.lock();
        try {
            readersCount--;
            // If this is the last reader, signal waiting writers
            if (readersCount == 0) {
                noReaders.signalAll();
            }
        } finally {
            lock.unlock();
        }
    }
    
    public void acquireWriteLock() throws InterruptedException {
        lock.lock();
        try {
            waitingWriters++;
            try {
                // Wait until there are no readers and no active writer
                while (readersCount > 0 || writerActive) {
                    noReaders.await();
                }
                writerActive = true;
            } finally {
                waitingWriters--;
            }
        } finally {
            lock.unlock();
        }
    }
    
    public void releaseWriteLock() {
        lock.lock();
        try {
            writerActive = false;
            // Signal waiting readers and writers
            noWriter.signalAll();
            noReaders.signalAll();
        } finally {
            lock.unlock();
        }
    }
    
    public int getReadersCount() {
        lock.lock();
        try {
            return readersCount;
        } finally {
            lock.unlock();
        }
    }
    
    public boolean isWriterActive() {
        lock.lock();
        try {
            return writerActive;
        } finally {
            lock.unlock();
        }
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: O(1) for all operations in the absence of contention
- Space Complexity: O(1) as we only use a constant amount of extra space

## Key Insights

1. This problem demonstrates the readers-writers pattern, which allows multiple readers but only one writer.
2. The ReentrantReadWriteLock approach is the simplest and most efficient, leveraging Java's built-in implementation.
3. The Semaphore approach uses counting semaphores to control access to the shared resource.
4. The synchronized and wait/notify approach shows how to use Java's intrinsic locking and condition variables.
5. The Locks and Conditions approach provides more flexibility and control over locking.
6. The key challenge is preventing writer starvation while allowing multiple readers.

## Interview Tips

- Be prepared to discuss the trade-offs between different synchronization mechanisms.
- Understand the readers-writers pattern and how it applies to real-world scenarios.
- Know how to use java.util.concurrent classes like ReentrantReadWriteLock, Semaphore, and Lock/Condition.
- Be able to explain the difference between reader preference, writer preference, and fair readers-writers locks.
- Consider edge cases like what happens if many readers and writers are waiting.
- Discuss how the solution would scale with a large number of readers and writers.
- Mention that in a real-world scenario, you would need to consider performance implications of different synchronization mechanisms, especially for read-heavy or write-heavy workloads.


### 15. Deadlock Detection (Medium)


## Problem Description

Implement a deadlock detection algorithm for a multithreaded system where:

* Multiple threads can acquire and release locks on resources.
* A deadlock occurs when there is a circular wait among threads, each holding a resource and waiting for another resource.

Design a class `DeadlockDetector` that supports the following operations:

* `DeadlockDetector()` Initializes the deadlock detector.
* `void acquireLock(int threadId, int resourceId)` Records that the thread with ID `threadId` is attempting to acquire a lock on the resource with ID `resourceId`. If the resource is already locked by another thread, the thread will wait.
* `void releaseLock(int threadId, int resourceId)` Records that the thread with ID `threadId` has released the lock on the resource with ID `resourceId`.
* `boolean hasDeadlock()` Returns true if there is a deadlock in the system, false otherwise.
* `List<Integer> getDeadlockedThreads()` Returns a list of thread IDs involved in a deadlock, or an empty list if there is no deadlock.

The deadlock detection implementation should follow these rules:
1. A thread can hold multiple resources at the same time.
2. A resource can only be held by one thread at a time.
3. A deadlock is detected when there is a circular wait among threads.
4. The implementation should be thread-safe.

**Example 1:**
```
Input: 
["DeadlockDetector", "acquireLock", "acquireLock", "acquireLock", "hasDeadlock", "releaseLock", "acquireLock", "hasDeadlock", "getDeadlockedThreads"]
[[], [1, 101], [2, 102], [1, 102], [], [1, 101], [2, 101], [], []]

Output: 
[null, null, null, null, true, null, null, true, [1, 2]]

Explanation:
DeadlockDetector detector = new DeadlockDetector();
detector.acquireLock(1, 101);    // Thread 1 acquires resource 101
detector.acquireLock(2, 102);    // Thread 2 acquires resource 102
detector.acquireLock(1, 102);    // Thread 1 tries to acquire resource 102, but it's held by Thread 2
                                 // Thread 1 waits for Thread 2 to release resource 102
detector.hasDeadlock();          // Return true (Thread 1 is waiting for Thread 2, and Thread 2 is waiting for Thread 1)
detector.releaseLock(1, 101);    // Thread 1 releases resource 101
detector.acquireLock(2, 101);    // Thread 2 acquires resource 101
detector.hasDeadlock();          // Return true (Thread 1 is waiting for Thread 2, and Thread 2 is waiting for Thread 1)
detector.getDeadlockedThreads(); // Return [1, 2] (Threads 1 and 2 are involved in a deadlock)
```

## Video Explanation
[Java Concurrency: Deadlock Detection (7:45 minutes)](https://www.youtube.com/watch?v=B4IVu-2hCos)

## Solution Approach

This problem tests your understanding of deadlock detection algorithms. There are several ways to solve it:

### 1. Using Resource Allocation Graph

```java
import java.util.*;

class DeadlockDetector {
    private Map<Integer, Set<Integer>> threadToResources; // Resources held by each thread
    private Map<Integer, Integer> resourceToThread;       // Thread holding each resource
    private Map<Integer, Set<Integer>> waitForGraph;      // Thread waiting for resources held by other threads
    
    public DeadlockDetector() {
        threadToResources = new HashMap<>();
        resourceToThread = new HashMap<>();
        waitForGraph = new HashMap<>();
    }
    
    public synchronized void acquireLock(int threadId, int resourceId) {
        // Initialize data structures if needed
        threadToResources.putIfAbsent(threadId, new HashSet<>());
        waitForGraph.putIfAbsent(threadId, new HashSet<>());
        
        // Check if resource is already locked
        if (resourceToThread.containsKey(resourceId)) {
            int holderThreadId = resourceToThread.get(resourceId);
            if (holderThreadId != threadId) {
                // Thread is waiting for a resource held by another thread
                waitForGraph.get(threadId).add(holderThreadId);
            }
        } else {
            // Resource is available, thread acquires it
            resourceToThread.put(resourceId, threadId);
            threadToResources.get(threadId).add(resourceId);
        }
    }
    
    public synchronized void releaseLock(int threadId, int resourceId) {
        // Check if thread holds the resource
        if (resourceToThread.containsKey(resourceId) && resourceToThread.get(resourceId) == threadId) {
            // Release the resource
            resourceToThread.remove(resourceId);
            threadToResources.get(threadId).remove(resourceId);
            
            // Update wait-for graph
            for (int waitingThreadId : waitForGraph.keySet()) {
                waitForGraph.get(waitingThreadId).remove(threadId);
            }
        }
    }
    
    public synchronized boolean hasDeadlock() {
        // Check for cycles in the wait-for graph using DFS
        Set<Integer> visited = new HashSet<>();
        Set<Integer> recursionStack = new HashSet<>();
        
        for (int threadId : waitForGraph.keySet()) {
            if (hasCycle(threadId, visited, recursionStack)) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean hasCycle(int threadId, Set<Integer> visited, Set<Integer> recursionStack) {
        if (!visited.contains(threadId)) {
            visited.add(threadId);
            recursionStack.add(threadId);
            
            for (int neighborThreadId : waitForGraph.getOrDefault(threadId, new HashSet<>())) {
                if (!visited.contains(neighborThreadId) && hasCycle(neighborThreadId, visited, recursionStack)) {
                    return true;
                } else if (recursionStack.contains(neighborThreadId)) {
                    return true;
                }
            }
        }
        
        recursionStack.remove(threadId);
        return false;
    }
    
    public synchronized List<Integer> getDeadlockedThreads() {
        List<Integer> deadlockedThreads = new ArrayList<>();
        
        // Find all threads involved in cycles
        Set<Integer> visited = new HashSet<>();
        Set<Integer> recursionStack = new HashSet<>();
        Map<Integer, Boolean> inCycle = new HashMap<>();
        
        for (int threadId : waitForGraph.keySet()) {
            findCycles(threadId, visited, recursionStack, inCycle);
        }
        
        // Add threads that are part of a cycle
        for (Map.Entry<Integer, Boolean> entry : inCycle.entrySet()) {
            if (entry.getValue()) {
                deadlockedThreads.add(entry.getKey());
            }
        }
        
        return deadlockedThreads;
    }
    
    private boolean findCycles(int threadId, Set<Integer> visited, Set<Integer> recursionStack, Map<Integer, Boolean> inCycle) {
        if (!visited.contains(threadId)) {
            visited.add(threadId);
            recursionStack.add(threadId);
            inCycle.put(threadId, false);
            
            for (int neighborThreadId : waitForGraph.getOrDefault(threadId, new HashSet<>())) {
                if (!visited.contains(neighborThreadId) && findCycles(neighborThreadId, visited, recursionStack, inCycle)) {
                    inCycle.put(threadId, true);
                    return true;
                } else if (recursionStack.contains(neighborThreadId)) {
                    // Found a cycle
                    inCycle.put(threadId, true);
                    return true;
                }
            }
        }
        
        recursionStack.remove(threadId);
        return inCycle.getOrDefault(threadId, false);
    }
}
```

### 2. Using ThreadMXBean (Java's Built-in Deadlock Detection)

```java
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

class DeadlockDetector {
    private Map<Integer, Set<Integer>> threadToResources;
    private Map<Integer, Integer> resourceToThread;
    private Map<Long, Integer> javaThreadIdToThreadId;
    private Map<Integer, Long> threadIdToJavaThreadId;
    private ThreadMXBean threadMXBean;
    
    public DeadlockDetector() {
        threadToResources = new ConcurrentHashMap<>();
        resourceToThread = new ConcurrentHashMap<>();
        javaThreadIdToThreadId = new ConcurrentHashMap<>();
        threadIdToJavaThreadId = new ConcurrentHashMap<>();
        threadMXBean = ManagementFactory.getThreadMXBean();
    }
    
    public void acquireLock(int threadId, int resourceId) {
        // Map our thread ID to Java's thread ID
        if (!threadIdToJavaThreadId.containsKey(threadId)) {
            long javaThreadId = Thread.currentThread().getId();
            threadIdToJavaThreadId.put(threadId, javaThreadId);
            javaThreadIdToThreadId.put(javaThreadId, threadId);
        }
        
        threadToResources.putIfAbsent(threadId, ConcurrentHashMap.newKeySet());
        
        // Check if resource is already locked
        if (resourceToThread.containsKey(resourceId)) {
            int holderThreadId = resourceToThread.get(resourceId);
            if (holderThreadId != threadId) {
                // Thread is waiting for a resource held by another thread
                // In a real system, the thread would block here
            }
        } else {
            // Resource is available, thread acquires it
            resourceToThread.put(resourceId, threadId);
            threadToResources.get(threadId).add(resourceId);
        }
    }
    
    public void releaseLock(int threadId, int resourceId) {
        // Check if thread holds the resource
        if (resourceToThread.containsKey(resourceId) && resourceToThread.get(resourceId) == threadId) {
            // Release the resource
            resourceToThread.remove(resourceId);
            threadToResources.get(threadId).remove(resourceId);
        }
    }
    
    public boolean hasDeadlock() {
        // Use Java's built-in deadlock detection
        long[] deadlockedThreadIds = threadMXBean.findDeadlockedThreads();
        return deadlockedThreadIds != null && deadlockedThreadIds.length > 0;
    }
    
    public List<Integer> getDeadlockedThreads() {
        List<Integer> deadlockedThreads = new ArrayList<>();
        
        // Use Java's built-in deadlock detection
        long[] deadlockedThreadIds = threadMXBean.findDeadlockedThreads();
        if (deadlockedThreadIds != null) {
            for (long javaThreadId : deadlockedThreadIds) {
                if (javaThreadIdToThreadId.containsKey(javaThreadId)) {
                    deadlockedThreads.add(javaThreadIdToThreadId.get(javaThreadId));
                }
            }
        }
        
        return deadlockedThreads;
    }
}
```

### 3. Using Banker's Algorithm

```java
import java.util.*;

class DeadlockDetector {
    private Map<Integer, Set<Integer>> threadToResources; // Resources held by each thread
    private Map<Integer, Integer> resourceToThread;       // Thread holding each resource
    private Map<Integer, Set<Integer>> threadToRequested; // Resources requested by each thread
    
    public DeadlockDetector() {
        threadToResources = new HashMap<>();
        resourceToThread = new HashMap<>();
        threadToRequested = new HashMap<>();
    }
    
    public synchronized void acquireLock(int threadId, int resourceId) {
        // Initialize data structures if needed
        threadToResources.putIfAbsent(threadId, new HashSet<>());
        threadToRequested.putIfAbsent(threadId, new HashSet<>());
        
        // Check if resource is already locked
        if (resourceToThread.containsKey(resourceId)) {
            int holderThreadId = resourceToThread.get(resourceId);
            if (holderThreadId != threadId) {
                // Thread is requesting a resource held by another thread
                threadToRequested.get(threadId).add(resourceId);
            }
        } else {
            // Resource is available, thread acquires it
            resourceToThread.put(resourceId, threadId);
            threadToResources.get(threadId).add(resourceId);
        }
    }
    
    public synchronized void releaseLock(int threadId, int resourceId) {
        // Check if thread holds the resource
        if (resourceToThread.containsKey(resourceId) && resourceToThread.get(resourceId) == threadId) {
            // Release the resource
            resourceToThread.remove(resourceId);
            threadToResources.get(threadId).remove(resourceId);
            
            // Remove from requested resources if it was requested
            if (threadToRequested.containsKey(threadId)) {
                threadToRequested.get(threadId).remove(resourceId);
            }
        }
    }
    
    public synchronized boolean hasDeadlock() {
        // Build resource allocation graph
        Map<Integer, Set<Integer>> waitForGraph = buildWaitForGraph();
        
        // Check for cycles in the wait-for graph using DFS
        Set<Integer> visited = new HashSet<>();
        Set<Integer> recursionStack = new HashSet<>();
        
        for (int threadId : waitForGraph.keySet()) {
            if (hasCycle(threadId, waitForGraph, visited, recursionStack)) {
                return true;
            }
        }
        
        return false;
    }
    
    private Map<Integer, Set<Integer>> buildWaitForGraph() {
        Map<Integer, Set<Integer>> waitForGraph = new HashMap<>();
        
        for (int threadId : threadToRequested.keySet()) {
            waitForGraph.putIfAbsent(threadId, new HashSet<>());
            
            for (int resourceId : threadToRequested.get(threadId)) {
                if (resourceToThread.containsKey(resourceId)) {
                    int holderThreadId = resourceToThread.get(resourceId);
                    if (holderThreadId != threadId) {
                        waitForGraph.get(threadId).add(holderThreadId);
                    }
                }
            }
        }
        
        return waitForGraph;
    }
    
    private boolean hasCycle(int threadId, Map<Integer, Set<Integer>> waitForGraph, Set<Integer> visited, Set<Integer> recursionStack) {
        if (!visited.contains(threadId)) {
            visited.add(threadId);
            recursionStack.add(threadId);
            
            for (int neighborThreadId : waitForGraph.getOrDefault(threadId, new HashSet<>())) {
                if (!visited.contains(neighborThreadId) && hasCycle(neighborThreadId, waitForGraph, visited, recursionStack)) {
                    return true;
                } else if (recursionStack.contains(neighborThreadId)) {
                    return true;
                }
            }
        }
        
        recursionStack.remove(threadId);
        return false;
    }
    
    public synchronized List<Integer> getDeadlockedThreads() {
        List<Integer> deadlockedThreads = new ArrayList<>();
        
        // Build resource allocation graph
        Map<Integer, Set<Integer>> waitForGraph = buildWaitForGraph();
        
        // Find all threads involved in cycles
        Set<Integer> visited = new HashSet<>();
        Set<Integer> recursionStack = new HashSet<>();
        Map<Integer, Boolean> inCycle = new HashMap<>();
        
        for (int threadId : waitForGraph.keySet()) {
            findCycles(threadId, waitForGraph, visited, recursionStack, inCycle);
        }
        
        // Add threads that are part of a cycle
        for (Map.Entry<Integer, Boolean> entry : inCycle.entrySet()) {
            if (entry.getValue()) {
                deadlockedThreads.add(entry.getKey());
            }
        }
        
        return deadlockedThreads;
    }
    
    private boolean findCycles(int threadId, Map<Integer, Set<Integer>> waitForGraph, Set<Integer> visited, Set<Integer> recursionStack, Map<Integer, Boolean> inCycle) {
        if (!visited.contains(threadId)) {
            visited.add(threadId);
            recursionStack.add(threadId);
            inCycle.put(threadId, false);
            
            for (int neighborThreadId : waitForGraph.getOrDefault(threadId, new HashSet<>())) {
                if (!visited.contains(neighborThreadId) && findCycles(neighborThreadId, waitForGraph, visited, recursionStack, inCycle)) {
                    inCycle.put(threadId, true);
                    return true;
                } else if (recursionStack.contains(neighborThreadId)) {
                    // Found a cycle
                    inCycle.put(threadId, true);
                    return true;
                }
            }
        }
        
        recursionStack.remove(threadId);
        return inCycle.getOrDefault(threadId, false);
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: 
  - O(1) for acquireLock and releaseLock operations
  - O(T + E) for hasDeadlock and getDeadlockedThreads operations, where T is the number of threads and E is the number of edges in the wait-for graph
- Space Complexity: O(T + R), where T is the number of threads and R is the number of resources

## Key Insights

1. This problem demonstrates deadlock detection using a resource allocation graph.
2. The key to detecting deadlocks is finding cycles in the wait-for graph.
3. The Resource Allocation Graph approach builds a directed graph where threads point to the threads that hold resources they are waiting for.
4. The ThreadMXBean approach leverages Java's built-in deadlock detection capabilities.
5. The Banker's Algorithm approach is more complex but can be used to prevent deadlocks as well as detect them.
6. The implementation must handle the case where a thread releases a resource it doesn't hold.

## Interview Tips

- Be prepared to discuss the difference between deadlock detection and deadlock prevention.
- Understand the four necessary conditions for a deadlock: mutual exclusion, hold and wait, no preemption, and circular wait.
- Know how to use depth-first search to detect cycles in a directed graph.
- Be able to explain the trade-offs between different deadlock detection algorithms.
- Consider edge cases like what happens if a thread acquires multiple resources or if multiple threads are waiting for the same resource.
- Discuss how the solution would scale with a large number of threads and resources.
- Mention that in a real-world scenario, you would need to consider performance implications of deadlock detection, especially for large systems.


### 16. Thread-Safe HashMap (Medium)


## Problem Description

Implement a thread-safe hash map that supports the following operations:

* `ThreadSafeHashMap()` Initializes an empty thread-safe hash map.
* `void put(K key, V value)` Inserts the key-value pair into the map. If the key already exists, update the value.
* `V get(K key)` Returns the value associated with the key, or null if the key does not exist.
* `V remove(K key)` Removes the key-value pair and returns the value, or null if the key does not exist.
* `boolean containsKey(K key)` Returns true if the map contains the key, false otherwise.
* `int size()` Returns the number of key-value pairs in the map.
* `List<K> keys()` Returns a list of all keys in the map.
* `void clear()` Removes all key-value pairs from the map.

The thread-safe hash map implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can read the map concurrently.
3. Write operations (put, remove, clear) should not block read operations unnecessarily.
4. The implementation should be efficient for high-concurrency scenarios.

**Example 1:**
```
Input: 
["ThreadSafeHashMap", "put", "put", "get", "size", "containsKey", "remove", "size", "clear", "size"]
[[], ["key1", "value1"], ["key2", "value2"], ["key1"], [], ["key1"], ["key2"], [], [], []]

Output: 
[null, null, null, "value1", 2, true, "value2", 1, null, 0]

Explanation:
ThreadSafeHashMap map = new ThreadSafeHashMap();
map.put("key1", "value1");           // Insert key1-value1 pair
map.put("key2", "value2");           // Insert key2-value2 pair
map.get("key1");                     // Return "value1"
map.size();                          // Return 2 (two key-value pairs)
map.containsKey("key1");             // Return true
map.remove("key2");                  // Remove key2 and return "value2"
map.size();                          // Return 1 (one key-value pair)
map.clear();                         // Remove all key-value pairs
map.size();                          // Return 0 (empty map)
```

## Video Explanation
[Java Concurrency: ConcurrentHashMap Implementation (8:15 minutes)](https://www.youtube.com/watch?v=CU4_pZ8Iqcc)

## Solution Approach

This problem tests your understanding of thread-safe data structures. There are several ways to solve it:

### 1. Using ConcurrentHashMap

```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

class ThreadSafeHashMap<K, V> {
    private final ConcurrentHashMap<K, V> map;
    
    public ThreadSafeHashMap() {
        map = new ConcurrentHashMap<>();
    }
    
    public void put(K key, V value) {
        map.put(key, value);
    }
    
    public V get(K key) {
        return map.get(key);
    }
    
    public V remove(K key) {
        return map.remove(key);
    }
    
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }
    
    public int size() {
        return map.size();
    }
    
    public List<K> keys() {
        return new ArrayList<>(map.keySet());
    }
    
    public void clear() {
        map.clear();
    }
}
```

### 2. Using Synchronized HashMap

```java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ThreadSafeHashMap<K, V> {
    private final Map<K, V> map;
    
    public ThreadSafeHashMap() {
        map = new HashMap<>();
    }
    
    public synchronized void put(K key, V value) {
        map.put(key, value);
    }
    
    public synchronized V get(K key) {
        return map.get(key);
    }
    
    public synchronized V remove(K key) {
        return map.remove(key);
    }
    
    public synchronized boolean containsKey(K key) {
        return map.containsKey(key);
    }
    
    public synchronized int size() {
        return map.size();
    }
    
    public synchronized List<K> keys() {
        return new ArrayList<>(map.keySet());
    }
    
    public synchronized void clear() {
        map.clear();
    }
}
```

### 3. Using ReentrantReadWriteLock

```java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ThreadSafeHashMap<K, V> {
    private final Map<K, V> map;
    private final ReadWriteLock lock;
    
    public ThreadSafeHashMap() {
        map = new HashMap<>();
        lock = new ReentrantReadWriteLock();
    }
    
    public void put(K key, V value) {
        lock.writeLock().lock();
        try {
            map.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public V get(K key) {
        lock.readLock().lock();
        try {
            return map.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public V remove(K key) {
        lock.writeLock().lock();
        try {
            return map.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public boolean containsKey(K key) {
        lock.readLock().lock();
        try {
            return map.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public int size() {
        lock.readLock().lock();
        try {
            return map.size();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public List<K> keys() {
        lock.readLock().lock();
        try {
            return new ArrayList<>(map.keySet());
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public void clear() {
        lock.writeLock().lock();
        try {
            map.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
```

### 4. Custom Implementation with Fine-Grained Locking

```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class ThreadSafeHashMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final float LOAD_FACTOR = 0.75f;
    
    private Node<K, V>[] buckets;
    private final AtomicInteger size;
    private int capacity;
    private final float loadFactor;
    
    @SuppressWarnings("unchecked")
    public ThreadSafeHashMap() {
        this.capacity = DEFAULT_CAPACITY;
        this.loadFactor = LOAD_FACTOR;
        this.buckets = new Node[capacity];
        this.size = new AtomicInteger(0);
    }
    
    public void put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }
        
        int hash = hash(key);
        int index = indexFor(hash, capacity);
        
        synchronized (getBucketLock(index)) {
            Node<K, V> current = buckets[index];
            
            if (current == null) {
                buckets[index] = new Node<>(hash, key, value, null);
                size.incrementAndGet();
            } else {
                Node<K, V> prev = null;
                while (current != null) {
                    if (current.hash == hash && (current.key == key || current.key.equals(key))) {
                        current.value = value;
                        return;
                    }
                    prev = current;
                    current = current.next;
                }
                prev.next = new Node<>(hash, key, value, null);
                size.incrementAndGet();
            }
        }
        
        if (size.get() > capacity * loadFactor) {
            resize();
        }
    }
    
    public V get(K key) {
        if (key == null) {
            return null;
        }
        
        int hash = hash(key);
        int index = indexFor(hash, capacity);
        
        synchronized (getBucketLock(index)) {
            Node<K, V> current = buckets[index];
            
            while (current != null) {
                if (current.hash == hash && (current.key == key || current.key.equals(key))) {
                    return current.value;
                }
                current = current.next;
            }
        }
        
        return null;
    }
    
    public V remove(K key) {
        if (key == null) {
            return null;
        }
        
        int hash = hash(key);
        int index = indexFor(hash, capacity);
        
        synchronized (getBucketLock(index)) {
            Node<K, V> current = buckets[index];
            Node<K, V> prev = null;
            
            while (current != null) {
                if (current.hash == hash && (current.key == key || current.key.equals(key))) {
                    if (prev == null) {
                        buckets[index] = current.next;
                    } else {
                        prev.next = current.next;
                    }
                    size.decrementAndGet();
                    return current.value;
                }
                prev = current;
                current = current.next;
            }
        }
        
        return null;
    }
    
    public boolean containsKey(K key) {
        if (key == null) {
            return false;
        }
        
        int hash = hash(key);
        int index = indexFor(hash, capacity);
        
        synchronized (getBucketLock(index)) {
            Node<K, V> current = buckets[index];
            
            while (current != null) {
                if (current.hash == hash && (current.key == key || current.key.equals(key))) {
                    return true;
                }
                current = current.next;
            }
        }
        
        return false;
    }
    
    public int size() {
        return size.get();
    }
    
    public List<K> keys() {
        List<K> keyList = new ArrayList<>();
        
        for (int i = 0; i < capacity; i++) {
            synchronized (getBucketLock(i)) {
                Node<K, V> current = buckets[i];
                
                while (current != null) {
                    keyList.add(current.key);
                    current = current.next;
                }
            }
        }
        
        return keyList;
    }
    
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            synchronized (getBucketLock(i)) {
                buckets[i] = null;
            }
        }
        size.set(0);
    }
    
    @SuppressWarnings("unchecked")
    private void resize() {
        int newCapacity = capacity * 2;
        Node<K, V>[] newBuckets = new Node[newCapacity];
        
        for (int i = 0; i < capacity; i++) {
            synchronized (getBucketLock(i)) {
                Node<K, V> current = buckets[i];
                
                while (current != null) {
                    Node<K, V> next = current.next;
                    int newIndex = indexFor(current.hash, newCapacity);
                    
                    current.next = newBuckets[newIndex];
                    newBuckets[newIndex] = current;
                    
                    current = next;
                }
            }
        }
        
        buckets = newBuckets;
        capacity = newCapacity;
    }
    
    private int hash(K key) {
        int h = key.hashCode();
        return h ^ (h >>> 16);
    }
    
    private int indexFor(int hash, int capacity) {
        return hash & (capacity - 1);
    }
    
    private Object getBucketLock(int index) {
        return buckets;
    }
    
    private static class Node<K, V> {
        final int hash;
        final K key;
        V value;
        Node<K, V> next;
        
        Node(int hash, K key, V value, Node<K, V> next) {
            this.hash = hash;
            this.key = key;
            this.value = value;
            this.next = next;
        }
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: 
  - O(1) average case for put, get, remove, and containsKey operations
  - O(n) for keys and clear operations
- Space Complexity: O(n) where n is the number of key-value pairs

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe hash map.
2. The ConcurrentHashMap approach is the simplest and most efficient, leveraging Java's built-in implementation.
3. The synchronized HashMap approach is simple but may lead to contention in high-concurrency scenarios.
4. The ReentrantReadWriteLock approach allows multiple readers but only one writer, which can improve performance for read-heavy workloads.
5. The custom implementation with fine-grained locking provides more control over the locking strategy but is more complex.
6. The key challenge is balancing thread safety with performance, especially in high-concurrency scenarios.

## Interview Tips

- Be prepared to discuss the trade-offs between different thread-safe map implementations.
- Understand how ConcurrentHashMap achieves thread safety through segmentation and compare it with other approaches.
- Know the difference between synchronized collections and concurrent collections in Java.
- Be able to explain the difference between coarse-grained and fine-grained locking.
- Consider edge cases like what happens if multiple threads try to resize the map simultaneously.
- Discuss how the solution would scale with a large number of threads and a high read-to-write ratio.
- Mention that in a real-world scenario, you would need to consider performance implications of different synchronization mechanisms, especially for read-heavy or write-heavy workloads.


### 17. Thread-Safe Queue (Medium)


## Problem Description

Implement a thread-safe queue that supports the following operations:

* `ThreadSafeQueue()` Initializes an empty thread-safe queue.
* `void enqueue(T item)` Adds an item to the end of the queue.
* `T dequeue()` Removes and returns the item at the front of the queue. If the queue is empty, it should block until an item becomes available.
* `T peek()` Returns the item at the front of the queue without removing it. If the queue is empty, it should return null.
* `int size()` Returns the number of items in the queue.
* `boolean isEmpty()` Returns true if the queue is empty, false otherwise.
* `void clear()` Removes all items from the queue.

The thread-safe queue implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can enqueue and dequeue concurrently.
3. The dequeue operation should block if the queue is empty.
4. The implementation should be efficient for high-concurrency scenarios.

**Example 1:**
```
Input: 
["ThreadSafeQueue", "enqueue", "enqueue", "size", "peek", "dequeue", "isEmpty", "dequeue", "isEmpty", "enqueue", "size"]
[[], [1], [2], [], [], [], [], [], [], [3], []]

Output: 
[null, null, null, 2, 1, 1, false, 2, true, null, 1]

Explanation:
ThreadSafeQueue queue = new ThreadSafeQueue();
queue.enqueue(1);           // Add 1 to the queue
queue.enqueue(2);           // Add 2 to the queue
queue.size();               // Return 2 (two items in the queue)
queue.peek();               // Return 1 (front item without removing)
queue.dequeue();            // Remove and return 1
queue.isEmpty();            // Return false (queue still has one item)
queue.dequeue();            // Remove and return 2
queue.isEmpty();            // Return true (queue is now empty)
queue.enqueue(3);           // Add 3 to the queue
queue.size();               // Return 1 (one item in the queue)
```

## Video Explanation
[Java Concurrency: Thread-Safe Queue Implementation (7:45 minutes)](https://www.youtube.com/watch?v=UOr9kMCCa5g)

## Solution Approach

This problem tests your understanding of thread-safe data structures. There are several ways to solve it:

### 1. Using ConcurrentLinkedQueue

```java
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class ThreadSafeQueue<T> {
    private final ConcurrentLinkedQueue<T> queue;
    private final ReentrantLock lock;
    private final Condition notEmpty;
    
    public ThreadSafeQueue() {
        queue = new ConcurrentLinkedQueue<>();
        lock = new ReentrantLock();
        notEmpty = lock.newCondition();
    }
    
    public void enqueue(T item) {
        lock.lock();
        try {
            queue.offer(item);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }
    
    public T dequeue() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            return queue.poll();
        } finally {
            lock.unlock();
        }
    }
    
    public T peek() {
        return queue.peek();
    }
    
    public int size() {
        return queue.size();
    }
    
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
    public void clear() {
        lock.lock();
        try {
            queue.clear();
        } finally {
            lock.unlock();
        }
    }
}
```

### 2. Using LinkedBlockingQueue

```java
import java.util.concurrent.LinkedBlockingQueue;

class ThreadSafeQueue<T> {
    private final LinkedBlockingQueue<T> queue;
    
    public ThreadSafeQueue() {
        queue = new LinkedBlockingQueue<>();
    }
    
    public void enqueue(T item) {
        queue.offer(item);
    }
    
    public T dequeue() throws InterruptedException {
        return queue.take();
    }
    
    public T peek() {
        return queue.peek();
    }
    
    public int size() {
        return queue.size();
    }
    
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
    public void clear() {
        queue.clear();
    }
}
```

### 3. Using ArrayBlockingQueue

```java
import java.util.concurrent.ArrayBlockingQueue;

class ThreadSafeQueue<T> {
    private final ArrayBlockingQueue<T> queue;
    
    public ThreadSafeQueue() {
        // Initial capacity can be adjusted based on expected usage
        queue = new ArrayBlockingQueue<>(1000);
    }
    
    public ThreadSafeQueue(int capacity) {
        queue = new ArrayBlockingQueue<>(capacity);
    }
    
    public void enqueue(T item) throws InterruptedException {
        queue.put(item);
    }
    
    public T dequeue() throws InterruptedException {
        return queue.take();
    }
    
    public T peek() {
        return queue.peek();
    }
    
    public int size() {
        return queue.size();
    }
    
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
    public void clear() {
        queue.clear();
    }
}
```

### 4. Custom Implementation with Locks and Conditions

```java
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class ThreadSafeQueue<T> {
    private final Queue<T> queue;
    private final ReentrantLock lock;
    private final Condition notEmpty;
    
    public ThreadSafeQueue() {
        queue = new LinkedList<>();
        lock = new ReentrantLock();
        notEmpty = lock.newCondition();
    }
    
    public void enqueue(T item) {
        lock.lock();
        try {
            queue.offer(item);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }
    
    public T dequeue() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            return queue.poll();
        } finally {
            lock.unlock();
        }
    }
    
    public T peek() {
        lock.lock();
        try {
            return queue.peek();
        } finally {
            lock.unlock();
        }
    }
    
    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
    
    public boolean isEmpty() {
        lock.lock();
        try {
            return queue.isEmpty();
        } finally {
            lock.unlock();
        }
    }
    
    public void clear() {
        lock.lock();
        try {
            queue.clear();
        } finally {
            lock.unlock();
        }
    }
}
```

### 5. Using wait() and notify()

```java
import java.util.LinkedList;
import java.util.Queue;

class ThreadSafeQueue<T> {
    private final Queue<T> queue;
    
    public ThreadSafeQueue() {
        queue = new LinkedList<>();
    }
    
    public synchronized void enqueue(T item) {
        queue.offer(item);
        notify();
    }
    
    public synchronized T dequeue() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        return queue.poll();
    }
    
    public synchronized T peek() {
        return queue.peek();
    }
    
    public synchronized int size() {
        return queue.size();
    }
    
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
    
    public synchronized void clear() {
        queue.clear();
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: 
  - O(1) for enqueue, dequeue, peek, isEmpty, and clear operations
  - O(n) for size operation in ConcurrentLinkedQueue (it needs to traverse the queue)
- Space Complexity: O(n) where n is the number of items in the queue

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe queue.
2. The ConcurrentLinkedQueue approach is non-blocking but requires additional synchronization for the blocking dequeue operation.
3. The LinkedBlockingQueue and ArrayBlockingQueue approaches are simpler and provide built-in blocking behavior.
4. The custom implementation with locks and conditions provides more control over the locking strategy.
5. The wait() and notify() approach is simpler but may lead to contention in high-concurrency scenarios.
6. The key challenge is ensuring that the dequeue operation blocks when the queue is empty and unblocks when items are added.

## Interview Tips

- Be prepared to discuss the trade-offs between different thread-safe queue implementations.
- Understand the difference between blocking and non-blocking queues.
- Know the difference between bounded and unbounded queues.
- Be able to explain the difference between wait/notify and explicit locks with conditions.
- Consider edge cases like what happens if multiple threads try to dequeue from an empty queue simultaneously.
- Discuss how the solution would scale with a large number of producer and consumer threads.
- Mention that in a real-world scenario, you would need to consider performance implications of different synchronization mechanisms, especially for high-throughput systems.


### 18. Rate Limiter (Medium)


## Problem Description

Implement a rate limiter that controls the rate of requests according to a token bucket algorithm:

* `RateLimiter(int capacity, int refillRate)` Initializes the rate limiter with a bucket of `capacity` tokens and a refill rate of `refillRate` tokens per second.
* `boolean allowRequest()` Returns true if a request is allowed, false otherwise. A request is allowed if there is at least one token in the bucket. When a request is allowed, one token is consumed from the bucket.
* `int getTokens()` Returns the current number of tokens in the bucket.
* `void reset()` Resets the rate limiter to its initial state.

The rate limiter implementation should follow these rules:
1. The bucket starts with `capacity` tokens.
2. Tokens are added to the bucket at a rate of `refillRate` tokens per second, up to the maximum capacity.
3. Each allowed request consumes one token from the bucket.
4. If the bucket is empty, requests should be rejected until tokens are refilled.
5. The implementation should be thread-safe.

**Example 1:**
```
Input: 
["RateLimiter", "allowRequest", "allowRequest", "allowRequest", "getTokens", "allowRequest", "getTokens", "reset", "getTokens"]
[[2, 1], [], [], [], [], [], [], [], []]

Output: 
[null, true, true, false, 0, false, 0, null, 2]

Explanation:
RateLimiter limiter = new RateLimiter(2, 1); // Initialize with capacity 2 and refill rate 1 token/second
limiter.allowRequest();                      // Return true (consume 1 token, 1 token left)
limiter.allowRequest();                      // Return true (consume 1 token, 0 tokens left)
limiter.allowRequest();                      // Return false (no tokens left)
limiter.getTokens();                         // Return 0 (no tokens in the bucket)
// Wait for 0.5 seconds, no tokens are refilled yet
limiter.allowRequest();                      // Return false (no tokens left)
limiter.getTokens();                         // Return 0 (no tokens in the bucket)
limiter.reset();                             // Reset the rate limiter
limiter.getTokens();                         // Return 2 (bucket is full again)
```

## Video Explanation
[Java Concurrency: Rate Limiter Implementation (9:15 minutes)](https://www.youtube.com/watch?v=PJ-c0QI-QCk)

## Solution Approach

This problem tests your understanding of rate limiting and thread safety. There are several ways to solve it:

### 1. Using AtomicLong and System.nanoTime()

```java
import java.util.concurrent.atomic.AtomicLong;

class RateLimiter {
    private final int capacity;
    private final double refillRate;
    private final AtomicLong tokens;
    private long lastRefillTimestamp;
    
    public RateLimiter(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = new AtomicLong(capacity);
        this.lastRefillTimestamp = System.nanoTime();
    }
    
    public synchronized boolean allowRequest() {
        refill();
        
        if (tokens.get() >= 1) {
            tokens.decrementAndGet();
            return true;
        }
        
        return false;
    }
    
    public synchronized int getTokens() {
        refill();
        return tokens.intValue();
    }
    
    public synchronized void reset() {
        tokens.set(capacity);
        lastRefillTimestamp = System.nanoTime();
    }
    
    private void refill() {
        long now = System.nanoTime();
        double elapsedSeconds = (now - lastRefillTimestamp) / 1_000_000_000.0;
        
        if (elapsedSeconds > 0) {
            double tokensToAdd = elapsedSeconds * refillRate;
            long newTokens = Math.min(capacity, tokens.get() + (long) tokensToAdd);
            tokens.set(newTokens);
            lastRefillTimestamp = now;
        }
    }
}
```

### 2. Using ReentrantLock and System.currentTimeMillis()

```java
import java.util.concurrent.locks.ReentrantLock;

class RateLimiter {
    private final int capacity;
    private final double refillRate;
    private double tokens;
    private long lastRefillTimestamp;
    private final ReentrantLock lock;
    
    public RateLimiter(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.tokens = capacity;
        this.lastRefillTimestamp = System.currentTimeMillis();
        this.lock = new ReentrantLock();
    }
    
    public boolean allowRequest() {
        lock.lock();
        try {
            refill();
            
            if (tokens >= 1) {
                tokens--;
                return true;
            }
            
            return false;
        } finally {
            lock.unlock();
        }
    }
    
    public int getTokens() {
        lock.lock();
        try {
            refill();
            return (int) tokens;
        } finally {
            lock.unlock();
        }
    }
    
    public void reset() {
        lock.lock();
        try {
            tokens = capacity;
            lastRefillTimestamp = System.currentTimeMillis();
        } finally {
            lock.unlock();
        }
    }
    
    private void refill() {
        long now = System.currentTimeMillis();
        double elapsedSeconds = (now - lastRefillTimestamp) / 1000.0;
        
        if (elapsedSeconds > 0) {
            double tokensToAdd = elapsedSeconds * refillRate;
            tokens = Math.min(capacity, tokens + tokensToAdd);
            lastRefillTimestamp = now;
        }
    }
}
```

### 3. Using Semaphore and ScheduledExecutorService

```java
import java.util.concurrent.Semaphore;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class RateLimiter {
    private final int capacity;
    private final int refillRate;
    private final Semaphore semaphore;
    private final AtomicInteger tokens;
    private final ScheduledExecutorService scheduler;
    
    public RateLimiter(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.semaphore = new Semaphore(capacity);
        this.tokens = new AtomicInteger(capacity);
        this.scheduler = Executors.newScheduledThreadPool(1);
        
        // Schedule token refill
        scheduler.scheduleAtFixedRate(() -> {
            int currentTokens = tokens.get();
            if (currentTokens < capacity) {
                int tokensToAdd = Math.min(refillRate, capacity - currentTokens);
                tokens.addAndGet(tokensToAdd);
                semaphore.release(tokensToAdd);
            }
        }, 1, 1, TimeUnit.SECONDS);
    }
    
    public boolean allowRequest() {
        if (semaphore.tryAcquire()) {
            tokens.decrementAndGet();
            return true;
        }
        return false;
    }
    
    public int getTokens() {
        return tokens.get();
    }
    
    public void reset() {
        int currentTokens = tokens.getAndSet(capacity);
        if (currentTokens < capacity) {
            semaphore.release(capacity - currentTokens);
        } else if (currentTokens > capacity) {
            try {
                semaphore.acquire(currentTokens - capacity);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
    
    // Don't forget to shut down the scheduler when done
    public void shutdown() {
        scheduler.shutdown();
    }
}
```

### 4. Using Guava's RateLimiter

```java
import com.google.common.util.concurrent.RateLimiter as GuavaRateLimiter;
import java.util.concurrent.atomic.AtomicInteger;

class RateLimiter {
    private final int capacity;
    private final int refillRate;
    private final GuavaRateLimiter limiter;
    private final AtomicInteger tokens;
    
    public RateLimiter(int capacity, int refillRate) {
        this.capacity = capacity;
        this.refillRate = refillRate;
        this.limiter = GuavaRateLimiter.create(refillRate);
        this.tokens = new AtomicInteger(capacity);
    }
    
    public boolean allowRequest() {
        if (tokens.get() > 0 && limiter.tryAcquire()) {
            tokens.decrementAndGet();
            return true;
        }
        return false;
    }
    
    public int getTokens() {
        return tokens.get();
    }
    
    public void reset() {
        tokens.set(capacity);
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: O(1) for all operations
- Space Complexity: O(1) as we only use a constant amount of extra space

## Key Insights

1. This problem demonstrates the token bucket algorithm for rate limiting.
2. The key challenge is accurately tracking the passage of time and refilling tokens accordingly.
3. The AtomicLong approach uses fine-grained time tracking with System.nanoTime() for accurate token refills.
4. The ReentrantLock approach provides explicit locking for thread safety.
5. The Semaphore approach uses a scheduled executor to periodically refill tokens.
6. The Guava approach leverages a well-tested library implementation.
7. All implementations must handle the case where tokens are refilled partially due to the maximum capacity constraint.

## Interview Tips

- Be prepared to discuss the trade-offs between different rate limiting algorithms (token bucket, leaky bucket, fixed window, sliding window).
- Understand how to handle time-based operations in a thread-safe manner.
- Know how to use AtomicLong, ReentrantLock, and Semaphore for thread safety.
- Be able to explain the difference between refilling tokens continuously versus at fixed intervals.
- Consider edge cases like what happens if the refill rate is very high or very low.
- Discuss how the solution would scale with a large number of requests.
- Mention that in a real-world scenario, you would need to consider distributed rate limiting for services running on multiple instances.


### 19. Connection Pool (Medium)


## Problem Description

Implement a thread-safe connection pool that manages a fixed number of database connections:

* `ConnectionPool(int maxConnections)` Initializes the connection pool with a maximum of `maxConnections` connections.
* `Connection getConnection()` Returns a connection from the pool. If no connections are available, the method should block until a connection becomes available.
* `void releaseConnection(Connection connection)` Returns a connection to the pool, making it available for other threads.
* `int getAvailableConnections()` Returns the number of available connections in the pool.
* `int getActiveConnections()` Returns the number of active connections (connections that have been acquired but not yet released).
* `void shutdown()` Closes all connections and shuts down the pool.

The connection pool implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can request and release connections concurrently.
3. If no connections are available, the getConnection method should block until a connection is released.
4. The pool should maintain a fixed number of connections.
5. Connections should be reused rather than created anew each time.

**Example 1:**
```
Input: 
["ConnectionPool", "getConnection", "getConnection", "getAvailableConnections", "getActiveConnections", "releaseConnection", "getAvailableConnections", "getActiveConnections", "shutdown"]
[[2], [], [], [], [], [conn1], [], [], []]

Output: 
[null, conn1, conn2, 0, 2, null, 1, 1, null]

Explanation:
ConnectionPool pool = new ConnectionPool(2);  // Initialize with 2 connections
Connection conn1 = pool.getConnection();      // Return first connection
Connection conn2 = pool.getConnection();      // Return second connection
pool.getAvailableConnections();               // Return 0 (no available connections)
pool.getActiveConnections();                  // Return 2 (two active connections)
pool.releaseConnection(conn1);                // Release first connection back to pool
pool.getAvailableConnections();               // Return 1 (one available connection)
pool.getActiveConnections();                  // Return 1 (one active connection)
pool.shutdown();                              // Close all connections and shut down
```

## Video Explanation
[Java Concurrency: Connection Pool Implementation (8:30 minutes)](https://www.youtube.com/watch?v=fdV4o7PmB-0)

## Solution Approach

This problem tests your understanding of resource pooling and thread safety. There are several ways to solve it:

### 1. Using BlockingQueue

```java
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.TimeUnit;

class ConnectionPool {
    private final BlockingQueue<Connection> connections;
    private final AtomicInteger activeConnections;
    private final int maxConnections;
    private boolean isShutdown;
    
    public ConnectionPool(int maxConnections) {
        this.maxConnections = maxConnections;
        this.connections = new LinkedBlockingQueue<>(maxConnections);
        this.activeConnections = new AtomicInteger(0);
        this.isShutdown = false;
        
        // Initialize connections
        for (int i = 0; i < maxConnections; i++) {
            connections.offer(createConnection());
        }
    }
    
    public Connection getConnection() throws InterruptedException {
        if (isShutdown) {
            throw new IllegalStateException("Connection pool is shut down");
        }
        
        Connection connection = connections.poll(30, TimeUnit.SECONDS);
        if (connection == null) {
            throw new RuntimeException("Timeout waiting for connection");
        }
        
        activeConnections.incrementAndGet();
        return connection;
    }
    
    public void releaseConnection(Connection connection) {
        if (connection == null) {
            return;
        }
        
        if (isShutdown) {
            closeConnection(connection);
            return;
        }
        
        connections.offer(connection);
        activeConnections.decrementAndGet();
    }
    
    public int getAvailableConnections() {
        return connections.size();
    }
    
    public int getActiveConnections() {
        return activeConnections.get();
    }
    
    public synchronized void shutdown() {
        if (isShutdown) {
            return;
        }
        
        isShutdown = true;
        
        // Close all connections in the pool
        Connection connection;
        while ((connection = connections.poll()) != null) {
            closeConnection(connection);
        }
    }
    
    private Connection createConnection() {
        // In a real implementation, this would create a database connection
        return new Connection() {
            private boolean closed = false;
            
            @Override
            public void close() {
                closed = true;
            }
            
            @Override
            public boolean isClosed() {
                return closed;
            }
        };
    }
    
    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (Exception e) {
            // Log exception
        }
    }
    
    // Simple Connection interface for demonstration
    interface Connection {
        void close();
        boolean isClosed();
    }
}
```

### 2. Using Semaphore

```java
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

class ConnectionPool {
    private final ConcurrentLinkedQueue<Connection> connections;
    private final Semaphore semaphore;
    private final AtomicInteger activeConnections;
    private final int maxConnections;
    private volatile boolean isShutdown;
    
    public ConnectionPool(int maxConnections) {
        this.maxConnections = maxConnections;
        this.connections = new ConcurrentLinkedQueue<>();
        this.semaphore = new Semaphore(maxConnections, true);
        this.activeConnections = new AtomicInteger(0);
        this.isShutdown = false;
        
        // Initialize connections
        for (int i = 0; i < maxConnections; i++) {
            connections.offer(createConnection());
        }
    }
    
    public Connection getConnection() throws InterruptedException {
        if (isShutdown) {
            throw new IllegalStateException("Connection pool is shut down");
        }
        
        semaphore.acquire();
        
        Connection connection = connections.poll();
        if (connection == null) {
            semaphore.release();
            throw new IllegalStateException("No connection available");
        }
        
        activeConnections.incrementAndGet();
        return connection;
    }
    
    public void releaseConnection(Connection connection) {
        if (connection == null) {
            return;
        }
        
        if (isShutdown) {
            closeConnection(connection);
            return;
        }
        
        connections.offer(connection);
        activeConnections.decrementAndGet();
        semaphore.release();
    }
    
    public int getAvailableConnections() {
        return semaphore.availablePermits();
    }
    
    public int getActiveConnections() {
        return activeConnections.get();
    }
    
    public synchronized void shutdown() {
        if (isShutdown) {
            return;
        }
        
        isShutdown = true;
        
        // Close all connections in the pool
        Connection connection;
        while ((connection = connections.poll()) != null) {
            closeConnection(connection);
        }
    }
    
    private Connection createConnection() {
        // In a real implementation, this would create a database connection
        return new Connection() {
            private boolean closed = false;
            
            @Override
            public void close() {
                closed = true;
            }
            
            @Override
            public boolean isClosed() {
                return closed;
            }
        };
    }
    
    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (Exception e) {
            // Log exception
        }
    }
    
    // Simple Connection interface for demonstration
    interface Connection {
        void close();
        boolean isClosed();
    }
}
```

### 3. Using ReentrantLock and Condition

```java
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class ConnectionPool {
    private final Queue<Connection> connections;
    private final ReentrantLock lock;
    private final Condition notEmpty;
    private final AtomicInteger activeConnections;
    private final int maxConnections;
    private boolean isShutdown;
    
    public ConnectionPool(int maxConnections) {
        this.maxConnections = maxConnections;
        this.connections = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.notEmpty = lock.newCondition();
        this.activeConnections = new AtomicInteger(0);
        this.isShutdown = false;
        
        // Initialize connections
        for (int i = 0; i < maxConnections; i++) {
            connections.offer(createConnection());
        }
    }
    
    public Connection getConnection() throws InterruptedException {
        lock.lock();
        try {
            if (isShutdown) {
                throw new IllegalStateException("Connection pool is shut down");
            }
            
            while (connections.isEmpty()) {
                notEmpty.await();
                
                if (isShutdown) {
                    throw new IllegalStateException("Connection pool is shut down");
                }
            }
            
            Connection connection = connections.poll();
            activeConnections.incrementAndGet();
            return connection;
        } finally {
            lock.unlock();
        }
    }
    
    public void releaseConnection(Connection connection) {
        if (connection == null) {
            return;
        }
        
        lock.lock();
        try {
            if (isShutdown) {
                closeConnection(connection);
                return;
            }
            
            connections.offer(connection);
            activeConnections.decrementAndGet();
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }
    
    public int getAvailableConnections() {
        lock.lock();
        try {
            return connections.size();
        } finally {
            lock.unlock();
        }
    }
    
    public int getActiveConnections() {
        return activeConnections.get();
    }
    
    public void shutdown() {
        lock.lock();
        try {
            if (isShutdown) {
                return;
            }
            
            isShutdown = true;
            
            // Close all connections in the pool
            while (!connections.isEmpty()) {
                Connection connection = connections.poll();
                closeConnection(connection);
            }
            
            // Signal any waiting threads
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }
    
    private Connection createConnection() {
        // In a real implementation, this would create a database connection
        return new Connection() {
            private boolean closed = false;
            
            @Override
            public void close() {
                closed = true;
            }
            
            @Override
            public boolean isClosed() {
                return closed;
            }
        };
    }
    
    private void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (Exception e) {
            // Log exception
        }
    }
    
    // Simple Connection interface for demonstration
    interface Connection {
        void close();
        boolean isClosed();
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: 
  - O(1) for getConnection, releaseConnection, getAvailableConnections, and getActiveConnections operations
  - O(n) for shutdown operation, where n is the number of connections
- Space Complexity: O(n) where n is the number of connections

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe connection pool.
2. The BlockingQueue approach leverages Java's built-in thread-safe queue implementation.
3. The Semaphore approach uses permits to control access to the limited resource.
4. The ReentrantLock and Condition approach provides more control over the locking strategy.
5. All implementations must handle the case where multiple threads try to get connections simultaneously.
6. The key challenge is ensuring that threads block when no connections are available and unblock when connections are released.
7. The implementation must also handle shutdown gracefully, closing all connections and preventing new acquisitions.

## Interview Tips

- Be prepared to discuss the trade-offs between different connection pooling implementations.
- Understand how to use BlockingQueue, Semaphore, and ReentrantLock for thread safety.
- Know the difference between fairness and non-fairness in resource allocation.
- Be able to explain how to handle timeouts for connection acquisition.
- Consider edge cases like what happens if a connection fails or becomes invalid.
- Discuss how the solution would scale with a large number of threads and connections.
- Mention that in a real-world scenario, you would need to consider connection validation, idle timeout, and connection recycling.


### 20. Thread-Safe Cache (Medium)


## Problem Description

Implement a thread-safe cache that supports the following operations:

* `ThreadSafeCache(int capacity)` Initializes the cache with a maximum capacity of `capacity` items.
* `V get(K key)` Returns the value associated with the key. If the key is not in the cache, it should return null.
* `void put(K key, V value)` Adds or updates the key-value pair in the cache. If the cache is at capacity, it should evict the least recently used item.
* `boolean containsKey(K key)` Returns true if the key exists in the cache, false otherwise.
* `int size()` Returns the number of items in the cache.
* `void clear()` Removes all items from the cache.
* `Set<K> keySet()` Returns a set of all keys in the cache.

The thread-safe cache implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can read and write to the cache concurrently.
3. The cache should use a Least Recently Used (LRU) eviction policy.
4. The implementation should be efficient for high-concurrency scenarios.

**Example 1:**
```
Input: 
["ThreadSafeCache", "put", "put", "get", "put", "get", "put", "get", "get", "size", "keySet"]
[[2], ["key1", "value1"], ["key2", "value2"], ["key1"], ["key3", "value3"], ["key2"], ["key4", "value4"], ["key1"], ["key3"], [], []]

Output: 
[null, null, null, "value1", null, null, null, null, "value3", 2, ["key3", "key4"]]

Explanation:
ThreadSafeCache cache = new ThreadSafeCache(2);  // Initialize with capacity 2
cache.put("key1", "value1");                     // Add key1-value1 pair
cache.put("key2", "value2");                     // Add key2-value2 pair
cache.get("key1");                               // Return "value1" (key1 is now most recently used)
cache.put("key3", "value3");                     // Add key3-value3 pair, evict key2 (least recently used)
cache.get("key2");                               // Return null (key2 was evicted)
cache.put("key4", "value4");                     // Add key4-value4 pair, evict key1 (least recently used)
cache.get("key1");                               // Return null (key1 was evicted)
cache.get("key3");                               // Return "value3" (key3 is now most recently used)
cache.size();                                    // Return 2 (two items in the cache)
cache.keySet();                                  // Return ["key3", "key4"] (keys in the cache)
```

## Video Explanation
[Java Concurrency: Thread-Safe Cache Implementation (8:45 minutes)](https://www.youtube.com/watch?v=UJesCn731G4)

## Solution Approach

This problem tests your understanding of thread-safe data structures and cache eviction policies. There are several ways to solve it:

### 1. Using ConcurrentHashMap and LinkedHashMap

```java
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ThreadSafeCache<K, V> {
    private final Map<K, V> cache;
    private final int capacity;
    private final ReentrantReadWriteLock lock;
    
    public ThreadSafeCache(int capacity) {
        this.capacity = capacity;
        this.lock = new ReentrantReadWriteLock();
        
        // Create a LinkedHashMap with access order
        this.cache = Collections.synchronizedMap(
            new LinkedHashMap<K, V>(capacity, 0.75f, true) {
                @Override
                protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
                    return size() > capacity;
                }
            }
        );
    }
    
    public V get(K key) {
        lock.readLock().lock();
        try {
            return cache.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public void put(K key, V value) {
        lock.writeLock().lock();
        try {
            cache.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public boolean containsKey(K key) {
        lock.readLock().lock();
        try {
            return cache.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public int size() {
        lock.readLock().lock();
        try {
            return cache.size();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public void clear() {
        lock.writeLock().lock();
        try {
            cache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public Set<K> keySet() {
        lock.readLock().lock();
        try {
            return Set.copyOf(cache.keySet());
        } finally {
            lock.readLock().unlock();
        }
    }
}
```

### 2. Using Caffeine Cache

```java
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class ThreadSafeCache<K, V> {
    private final Cache<K, V> cache;
    
    public ThreadSafeCache(int capacity) {
        this.cache = Caffeine.newBuilder()
            .maximumSize(capacity)
            .build();
    }
    
    public V get(K key) {
        return cache.getIfPresent(key);
    }
    
    public void put(K key, V value) {
        cache.put(key, value);
    }
    
    public boolean containsKey(K key) {
        return cache.getIfPresent(key) != null;
    }
    
    public int size() {
        return (int) cache.estimatedSize();
    }
    
    public void clear() {
        cache.invalidateAll();
    }
    
    public Set<K> keySet() {
        ConcurrentHashMap<K, Boolean> keys = new ConcurrentHashMap<>();
        cache.asMap().forEach((key, value) -> keys.put(key, true));
        return keys.keySet();
    }
}
```

### 3. Custom Implementation with ConcurrentHashMap and ConcurrentLinkedQueue

```java
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

class ThreadSafeCache<K, V> {
    private final Map<K, V> cache;
    private final Queue<K> accessOrder;
    private final Map<K, Integer> accessCount;
    private final int capacity;
    private final ReentrantLock lock;
    
    public ThreadSafeCache(int capacity) {
        this.capacity = capacity;
        this.cache = new ConcurrentHashMap<>();
        this.accessOrder = new ConcurrentLinkedQueue<>();
        this.accessCount = new ConcurrentHashMap<>();
        this.lock = new ReentrantLock();
    }
    
    public V get(K key) {
        V value = cache.get(key);
        if (value != null) {
            updateAccessOrder(key);
        }
        return value;
    }
    
    public void put(K key, V value) {
        lock.lock();
        try {
            if (cache.containsKey(key)) {
                cache.put(key, value);
                updateAccessOrder(key);
            } else {
                if (cache.size() >= capacity) {
                    evictLeastRecentlyUsed();
                }
                cache.put(key, value);
                accessOrder.add(key);
                accessCount.put(key, 1);
            }
        } finally {
            lock.unlock();
        }
    }
    
    public boolean containsKey(K key) {
        return cache.containsKey(key);
    }
    
    public int size() {
        return cache.size();
    }
    
    public void clear() {
        lock.lock();
        try {
            cache.clear();
            accessOrder.clear();
            accessCount.clear();
        } finally {
            lock.unlock();
        }
    }
    
    public Set<K> keySet() {
        return cache.keySet();
    }
    
    private void updateAccessOrder(K key) {
        lock.lock();
        try {
            accessCount.compute(key, (k, v) -> v == null ? 1 : v + 1);
            accessOrder.remove(key);
            accessOrder.add(key);
        } finally {
            lock.unlock();
        }
    }
    
    private void evictLeastRecentlyUsed() {
        while (!accessOrder.isEmpty()) {
            K key = accessOrder.poll();
            if (key != null && cache.containsKey(key)) {
                cache.remove(key);
                accessCount.remove(key);
                break;
            }
        }
    }
}
```

### 4. Using Guava's Cache

```java
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.Set;
import java.util.concurrent.ExecutionException;

class ThreadSafeCache<K, V> {
    private final LoadingCache<K, V> cache;
    
    public ThreadSafeCache(int capacity) {
        this.cache = CacheBuilder.newBuilder()
            .maximumSize(capacity)
            .build(
                new CacheLoader<K, V>() {
                    @Override
                    public V load(K key) throws Exception {
                        return null;
                    }
                }
            );
    }
    
    public V get(K key) {
        try {
            return cache.get(key);
        } catch (ExecutionException e) {
            return null;
        }
    }
    
    public void put(K key, V value) {
        cache.put(key, value);
    }
    
    public boolean containsKey(K key) {
        return cache.asMap().containsKey(key);
    }
    
    public int size() {
        return (int) cache.size();
    }
    
    public void clear() {
        cache.invalidateAll();
    }
    
    public Set<K> keySet() {
        return cache.asMap().keySet();
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: 
  - O(1) for get, put, containsKey, size, and clear operations
  - O(n) for keySet operation, where n is the number of items in the cache
- Space Complexity: O(n) where n is the capacity of the cache

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe cache.
2. The LinkedHashMap approach leverages Java's built-in LRU implementation with access order.
3. The Caffeine Cache approach uses a high-performance caching library.
4. The custom implementation with ConcurrentHashMap and ConcurrentLinkedQueue provides more control over the eviction policy.
5. The Guava Cache approach uses another popular caching library.
6. All implementations must handle the case where multiple threads try to access or modify the cache simultaneously.
7. The key challenge is ensuring thread safety while maintaining the LRU eviction policy.

## Interview Tips

- Be prepared to discuss the trade-offs between different caching implementations.
- Understand how to use ReentrantReadWriteLock for thread safety with different access patterns.
- Know the difference between read and write operations in terms of locking.
- Be able to explain the LRU eviction policy and how it's implemented.
- Consider edge cases like what happens if multiple threads try to add items to a full cache simultaneously.
- Discuss how the solution would scale with a large number of threads and cache entries.
- Mention that in a real-world scenario, you would need to consider cache expiration, weak references, and statistics collection.


### 21. Thread-Safe Singleton (Medium)


## Problem Description

Implement a thread-safe singleton class that ensures only one instance of the class is created, even in a multithreaded environment:

* `Singleton getInstance()` Returns the singleton instance. If the instance doesn't exist, it should be created. If it already exists, the existing instance should be returned.

The thread-safe singleton implementation should follow these rules:
1. Only one instance of the class should ever be created.
2. The implementation must be thread-safe, ensuring that multiple threads calling getInstance() simultaneously will still result in only one instance being created.
3. The singleton instance should be lazily initialized (created only when first needed).
4. The implementation should be efficient, minimizing unnecessary synchronization.

**Example 1:**
```
Input: 
["Singleton.getInstance", "Singleton.getInstance", "Singleton.getInstance"]
[[], [], []]

Output: 
[instance1, instance1, instance1]

Explanation:
Singleton instance1 = Singleton.getInstance();  // Create and return the singleton instance
Singleton instance2 = Singleton.getInstance();  // Return the existing singleton instance
Singleton instance3 = Singleton.getInstance();  // Return the existing singleton instance
// instance1 == instance2 == instance3 (all references point to the same object)
```

## Video Explanation
[Java Concurrency: Thread-Safe Singleton Implementation (6:30 minutes)](https://www.youtube.com/watch?v=GH5_lF7-3YI)

## Solution Approach

This problem tests your understanding of thread safety and singleton design pattern. There are several ways to solve it:

### 1. Using Double-Checked Locking

```java
public class Singleton {
    private static volatile Singleton instance;
    
    // Private constructor to prevent instantiation
    private Singleton() {
        // Prevent reflection instantiation
        if (instance != null) {
            throw new IllegalStateException("Singleton already initialized");
        }
    }
    
    public static Singleton getInstance() {
        // First check (without synchronization)
        if (instance == null) {
            // Synchronize only when instance might be null
            synchronized (Singleton.class) {
                // Second check (with synchronization)
                if (instance == null) {
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
}
```

### 2. Using Static Holder Pattern

```java
public class Singleton {
    // Private constructor to prevent instantiation
    private Singleton() {
    }
    
    // Static holder class is only loaded when getInstance() is called
    private static class SingletonHolder {
        private static final Singleton INSTANCE = new Singleton();
    }
    
    public static Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}
```

### 3. Using Eager Initialization

```java
public class Singleton {
    // Instance is created at class loading time
    private static final Singleton INSTANCE = new Singleton();
    
    // Private constructor to prevent instantiation
    private Singleton() {
    }
    
    public static Singleton getInstance() {
        return INSTANCE;
    }
}
```

### 4. Using Enum

```java
public enum Singleton {
    INSTANCE;
    
    // Add any methods or fields here
    public void doSomething() {
        // Implementation
    }
}

// Usage: Singleton.INSTANCE.doSomething();
```

### 5. Using Synchronized Method

```java
public class Singleton {
    private static Singleton instance;
    
    // Private constructor to prevent instantiation
    private Singleton() {
    }
    
    // Synchronized method to ensure thread safety
    public static synchronized Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: O(1) for getInstance() after initialization
- Space Complexity: O(1) as we only store one instance

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe singleton.
2. The Double-Checked Locking approach minimizes synchronization overhead by only synchronizing when necessary.
3. The Static Holder Pattern leverages Java's class loading mechanism to ensure thread safety.
4. The Eager Initialization approach creates the instance at class loading time, ensuring thread safety.
5. The Enum approach is the simplest and most effective way to implement a singleton in Java.
6. The Synchronized Method approach is simple but may lead to performance issues due to synchronization overhead.
7. The `volatile` keyword in the Double-Checked Locking approach is crucial to prevent partially initialized objects due to instruction reordering.

## Interview Tips

- Be prepared to discuss the trade-offs between different singleton implementations.
- Understand the importance of the `volatile` keyword in the Double-Checked Locking approach.
- Know how class loading works in Java and how it relates to thread safety.
- Be able to explain the potential issues with reflection and serialization in singleton implementations.
- Consider edge cases like what happens if the constructor throws an exception.
- Discuss how the solution would scale in a distributed environment.
- Mention that in a real-world scenario, you would need to consider dependency injection frameworks and testability.


### 22. Thread-Safe Object Pool (Medium)


## Problem Description

Implement a thread-safe object pool that manages a fixed number of reusable objects:

* `ObjectPool(int capacity, Supplier<T> factory)` Initializes the object pool with a maximum capacity of `capacity` objects and a factory function to create new objects.
* `T acquire()` Returns an object from the pool. If no objects are available and the pool hasn't reached capacity, a new object is created. If the pool is at capacity, the method should block until an object becomes available.
* `void release(T object)` Returns an object to the pool, making it available for other threads.
* `int getAvailableCount()` Returns the number of available objects in the pool.
* `int getActiveCount()` Returns the number of active objects (objects that have been acquired but not yet released).
* `void shutdown()` Closes all objects and shuts down the pool.

The object pool implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can acquire and release objects concurrently.
3. If no objects are available and the pool is at capacity, the acquire method should block until an object is released.
4. Objects should be reused rather than created anew each time.
5. The pool should validate objects before providing them to clients.

**Example 1:**
```
Input: 
["ObjectPool", "acquire", "acquire", "getAvailableCount", "getActiveCount", "release", "getAvailableCount", "getActiveCount", "shutdown"]
[[2, () -> new Connection()], [], [], [], [], [conn1], [], [], []]

Output: 
[null, conn1, conn2, 0, 2, null, 1, 1, null]

Explanation:
ObjectPool<Connection> pool = new ObjectPool<>(2, () -> new Connection());  // Initialize with capacity 2
Connection conn1 = pool.acquire();      // Return first connection
Connection conn2 = pool.acquire();      // Return second connection
pool.getAvailableCount();               // Return 0 (no available objects)
pool.getActiveCount();                  // Return 2 (two active objects)
pool.release(conn1);                    // Release first connection back to pool
pool.getAvailableCount();               // Return 1 (one available object)
pool.getActiveCount();                  // Return 1 (one active object)
pool.shutdown();                        // Close all objects and shut down
```

## Video Explanation
[Java Concurrency: Object Pool Implementation (7:45 minutes)](https://www.youtube.com/watch?v=fdV4o7PmB-0)

## Solution Approach

This problem tests your understanding of resource pooling and thread safety. There are several ways to solve it:

### 1. Using BlockingQueue

```java
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

class ObjectPool<T> {
    private final BlockingQueue<T> available;
    private final Set<T> inUse;
    private final int capacity;
    private final Supplier<T> factory;
    private final AtomicInteger activeCount;
    private boolean isShutdown;
    
    public ObjectPool(int capacity, Supplier<T> factory) {
        this.capacity = capacity;
        this.factory = factory;
        this.available = new LinkedBlockingQueue<>();
        this.inUse = new HashSet<>();
        this.activeCount = new AtomicInteger(0);
        this.isShutdown = false;
    }
    
    public T acquire() throws InterruptedException {
        if (isShutdown) {
            throw new IllegalStateException("Object pool is shut down");
        }
        
        synchronized (this) {
            // Check if we can create a new object
            if (available.isEmpty() && activeCount.get() < capacity) {
                T object = factory.get();
                if (validateObject(object)) {
                    inUse.add(object);
                    activeCount.incrementAndGet();
                    return object;
                }
            }
        }
        
        // Wait for an available object
        T object = available.take();
        
        synchronized (this) {
            if (isShutdown) {
                available.add(object);
                throw new IllegalStateException("Object pool is shut down");
            }
            
            if (validateObject(object)) {
                inUse.add(object);
                return object;
            } else {
                // Object is invalid, create a new one if possible
                if (activeCount.get() < capacity) {
                    T newObject = factory.get();
                    if (validateObject(newObject)) {
                        inUse.add(newObject);
                        return newObject;
                    }
                }
                // If we can't create a new one, wait for another object
                return acquire();
            }
        }
    }
    
    public void release(T object) {
        if (object == null) {
            return;
        }
        
        synchronized (this) {
            if (isShutdown) {
                closeObject(object);
                return;
            }
            
            if (inUse.remove(object)) {
                if (validateObject(object)) {
                    available.offer(object);
                } else {
                    // Object is invalid, create a new one if possible
                    if (activeCount.get() < capacity) {
                        T newObject = factory.get();
                        if (validateObject(newObject)) {
                            available.offer(newObject);
                        }
                    } else {
                        activeCount.decrementAndGet();
                    }
                }
            }
        }
    }
    
    public int getAvailableCount() {
        return available.size();
    }
    
    public int getActiveCount() {
        return activeCount.get();
    }
    
    public synchronized void shutdown() {
        if (isShutdown) {
            return;
        }
        
        isShutdown = true;
        
        // Close all objects in the pool
        for (T object : available) {
            closeObject(object);
        }
        available.clear();
        
        // Close all objects in use
        for (T object : inUse) {
            closeObject(object);
        }
        inUse.clear();
    }
    
    private boolean validateObject(T object) {
        // In a real implementation, this would validate the object
        return object != null;
    }
    
    private void closeObject(T object) {
        // In a real implementation, this would close or clean up the object
        if (object instanceof AutoCloseable) {
            try {
                ((AutoCloseable) object).close();
            } catch (Exception e) {
                // Log exception
            }
        }
    }
}
```

### 2. Using Semaphore

```java
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

class ObjectPool<T> {
    private final Queue<T> available;
    private final Set<T> inUse;
    private final Semaphore semaphore;
    private final int capacity;
    private final Supplier<T> factory;
    private final AtomicInteger activeCount;
    private volatile boolean isShutdown;
    
    public ObjectPool(int capacity, Supplier<T> factory) {
        this.capacity = capacity;
        this.factory = factory;
        this.available = new ConcurrentLinkedQueue<>();
        this.inUse = ConcurrentHashMap.newKeySet();
        this.semaphore = new Semaphore(capacity, true);
        this.activeCount = new AtomicInteger(0);
        this.isShutdown = false;
    }
    
    public T acquire() throws InterruptedException {
        if (isShutdown) {
            throw new IllegalStateException("Object pool is shut down");
        }
        
        semaphore.acquire();
        
        try {
            T object = available.poll();
            
            if (object == null) {
                // Create a new object
                object = factory.get();
            }
            
            if (!validateObject(object)) {
                // Object is invalid, create a new one
                object = factory.get();
                if (!validateObject(object)) {
                    semaphore.release();
                    throw new IllegalStateException("Failed to create valid object");
                }
            }
            
            inUse.add(object);
            activeCount.incrementAndGet();
            return object;
        } catch (Exception e) {
            semaphore.release();
            throw e;
        }
    }
    
    public void release(T object) {
        if (object == null) {
            return;
        }
        
        if (isShutdown) {
            closeObject(object);
            return;
        }
        
        if (inUse.remove(object)) {
            if (validateObject(object)) {
                available.offer(object);
            } else {
                // Object is invalid, discard it
                closeObject(object);
            }
            activeCount.decrementAndGet();
            semaphore.release();
        }
    }
    
    public int getAvailableCount() {
        return available.size();
    }
    
    public int getActiveCount() {
        return activeCount.get();
    }
    
    public void shutdown() {
        if (isShutdown) {
            return;
        }
        
        isShutdown = true;
        
        // Close all objects in the pool
        T object;
        while ((object = available.poll()) != null) {
            closeObject(object);
        }
        
        // Note: Objects in use will be closed when released
    }
    
    private boolean validateObject(T object) {
        // In a real implementation, this would validate the object
        return object != null;
    }
    
    private void closeObject(T object) {
        // In a real implementation, this would close or clean up the object
        if (object instanceof AutoCloseable) {
            try {
                ((AutoCloseable) object).close();
            } catch (Exception e) {
                // Log exception
            }
        }
    }
}
```

### 3. Using ReentrantLock and Condition

```java
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

class ObjectPool<T> {
    private final Queue<T> available;
    private final Set<T> inUse;
    private final ReentrantLock lock;
    private final Condition notEmpty;
    private final int capacity;
    private final Supplier<T> factory;
    private final AtomicInteger activeCount;
    private boolean isShutdown;
    
    public ObjectPool(int capacity, Supplier<T> factory) {
        this.capacity = capacity;
        this.factory = factory;
        this.available = new LinkedList<>();
        this.inUse = new HashSet<>();
        this.lock = new ReentrantLock();
        this.notEmpty = lock.newCondition();
        this.activeCount = new AtomicInteger(0);
        this.isShutdown = false;
    }
    
    public T acquire() throws InterruptedException {
        lock.lock();
        try {
            if (isShutdown) {
                throw new IllegalStateException("Object pool is shut down");
            }
            
            // Try to get an available object
            while (available.isEmpty()) {
                // If we can create a new object, do so
                if (activeCount.get() < capacity) {
                    T object = factory.get();
                    if (validateObject(object)) {
                        inUse.add(object);
                        activeCount.incrementAndGet();
                        return object;
                    }
                }
                
                // Otherwise, wait for an object to be released
                notEmpty.await();
                
                if (isShutdown) {
                    throw new IllegalStateException("Object pool is shut down");
                }
            }
            
            T object = available.poll();
            if (validateObject(object)) {
                inUse.add(object);
                return object;
            } else {
                // Object is invalid, create a new one
                T newObject = factory.get();
                if (validateObject(newObject)) {
                    inUse.add(newObject);
                    return newObject;
                } else {
                    throw new IllegalStateException("Failed to create valid object");
                }
            }
        } finally {
            lock.unlock();
        }
    }
    
    public void release(T object) {
        if (object == null) {
            return;
        }
        
        lock.lock();
        try {
            if (isShutdown) {
                closeObject(object);
                return;
            }
            
            if (inUse.remove(object)) {
                if (validateObject(object)) {
                    available.offer(object);
                    notEmpty.signal();
                } else {
                    // Object is invalid, discard it
                    closeObject(object);
                    activeCount.decrementAndGet();
                }
            }
        } finally {
            lock.unlock();
        }
    }
    
    public int getAvailableCount() {
        lock.lock();
        try {
            return available.size();
        } finally {
            lock.unlock();
        }
    }
    
    public int getActiveCount() {
        return activeCount.get();
    }
    
    public void shutdown() {
        lock.lock();
        try {
            if (isShutdown) {
                return;
            }
            
            isShutdown = true;
            
            // Close all objects in the pool
            for (T object : available) {
                closeObject(object);
            }
            available.clear();
            
            // Close all objects in use
            for (T object : inUse) {
                closeObject(object);
            }
            inUse.clear();
            
            // Signal any waiting threads
            notEmpty.signalAll();
        } finally {
            lock.unlock();
        }
    }
    
    private boolean validateObject(T object) {
        // In a real implementation, this would validate the object
        return object != null;
    }
    
    private void closeObject(T object) {
        // In a real implementation, this would close or clean up the object
        if (object instanceof AutoCloseable) {
            try {
                ((AutoCloseable) object).close();
            } catch (Exception e) {
                // Log exception
            }
        }
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: 
  - O(1) for acquire, release, getAvailableCount, and getActiveCount operations
  - O(n) for shutdown operation, where n is the number of objects
- Space Complexity: O(n) where n is the capacity of the pool

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe object pool.
2. The BlockingQueue approach leverages Java's built-in thread-safe queue implementation.
3. The Semaphore approach uses permits to control access to the limited resource.
4. The ReentrantLock and Condition approach provides more control over the locking strategy.
5. All implementations must handle the case where multiple threads try to acquire objects simultaneously.
6. The key challenge is ensuring that threads block when no objects are available and unblock when objects are released.
7. The implementation must also handle validation and cleanup of objects.

## Interview Tips

- Be prepared to discuss the trade-offs between different object pooling implementations.
- Understand how to use BlockingQueue, Semaphore, and ReentrantLock for thread safety.
- Know the difference between fairness and non-fairness in resource allocation.
- Be able to explain how to handle object validation and cleanup.
- Consider edge cases like what happens if an object becomes invalid while in use.
- Discuss how the solution would scale with a large number of threads and objects.
- Mention that in a real-world scenario, you would need to consider object expiration, idle timeout, and object recycling.


### 23. Thread-Safe Deferred Callback (Medium)


## Problem Description

Implement a thread-safe deferred callback system that allows registering callback methods to be executed after a specified time interval:

* `DeferredCallback()` Initializes the deferred callback system.
* `int registerCallback(Runnable callback, long delayMs)` Registers a callback to be executed after `delayMs` milliseconds. Returns a unique callback ID.
* `boolean cancelCallback(int callbackId)` Cancels a registered callback. Returns true if the callback was successfully canceled, false if the callback doesn't exist or has already been executed.
* `int getPendingCallbacksCount()` Returns the number of callbacks that are registered but not yet executed.
* `void shutdown()` Shuts down the callback system and cancels all pending callbacks.

The deferred callback implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can register and cancel callbacks concurrently.
3. Callbacks should be executed as close as possible to their scheduled time.
4. The implementation should be efficient and scalable.

**Example 1:**
```
Input: 
["DeferredCallback", "registerCallback", "getPendingCallbacksCount", "registerCallback", "getPendingCallbacksCount", "cancelCallback", "getPendingCallbacksCount", "shutdown"]
[[], [() -> System.out.println("Callback 1"), 1000], [], [() -> System.out.println("Callback 2"), 2000], [], [1], [], []]

Output: 
[null, 1, 2, 2, 2, true, 1, null]

Explanation:
DeferredCallback dc = new DeferredCallback();                      // Initialize the callback system
int id1 = dc.registerCallback(() -> System.out.println("Callback 1"), 1000);  // Register callback 1 with 1000ms delay, returns ID 1
dc.getPendingCallbacksCount();                                     // Return 2 (two pending callbacks)
int id2 = dc.registerCallback(() -> System.out.println("Callback 2"), 2000);  // Register callback 2 with 2000ms delay, returns ID 2
dc.getPendingCallbacksCount();                                     // Return 2 (two pending callbacks)
dc.cancelCallback(id1);                                            // Cancel callback 1, returns true
dc.getPendingCallbacksCount();                                     // Return 1 (one pending callback)
dc.shutdown();                                                     // Shut down the callback system
```

## Video Explanation
[Java Concurrency: Deferred Callback Implementation (7:15 minutes)](https://www.youtube.com/watch?v=AlexanderObregon/javas-scheduledexecutorservice-schedule-method-explained-1c242cc2c5d6)

## Solution Approach

This problem tests your understanding of thread safety and task scheduling. There are several ways to solve it:

### 1. Using ScheduledExecutorService

```java
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class DeferredCallback {
    private final ScheduledExecutorService scheduler;
    private final Map<Integer, ScheduledFuture<?>> callbacks;
    private final AtomicInteger nextCallbackId;
    private final AtomicInteger pendingCallbacksCount;
    private volatile boolean isShutdown;
    
    public DeferredCallback() {
        this.scheduler = new ScheduledThreadPoolExecutor(Runtime.getRuntime().availableProcessors());
        this.callbacks = new ConcurrentHashMap<>();
        this.nextCallbackId = new AtomicInteger(1);
        this.pendingCallbacksCount = new AtomicInteger(0);
        this.isShutdown = false;
    }
    
    public int registerCallback(Runnable callback, long delayMs) {
        if (isShutdown) {
            throw new IllegalStateException("Callback system is shut down");
        }
        
        int callbackId = nextCallbackId.getAndIncrement();
        
        // Wrap the callback to update the pending count when executed
        Runnable wrappedCallback = () -> {
            try {
                callback.run();
            } finally {
                callbacks.remove(callbackId);
                pendingCallbacksCount.decrementAndGet();
            }
        };
        
        // Schedule the callback
        ScheduledFuture<?> future = scheduler.schedule(wrappedCallback, delayMs, TimeUnit.MILLISECONDS);
        callbacks.put(callbackId, future);
        pendingCallbacksCount.incrementAndGet();
        
        return callbackId;
    }
    
    public boolean cancelCallback(int callbackId) {
        if (isShutdown) {
            return false;
        }
        
        ScheduledFuture<?> future = callbacks.remove(callbackId);
        if (future != null) {
            boolean canceled = future.cancel(false);
            if (canceled) {
                pendingCallbacksCount.decrementAndGet();
            }
            return canceled;
        }
        
        return false;
    }
    
    public int getPendingCallbacksCount() {
        return pendingCallbacksCount.get();
    }
    
    public void shutdown() {
        if (isShutdown) {
            return;
        }
        
        isShutdown = true;
        
        // Cancel all pending callbacks
        for (Map.Entry<Integer, ScheduledFuture<?>> entry : callbacks.entrySet()) {
            entry.getValue().cancel(false);
        }
        
        callbacks.clear();
        pendingCallbacksCount.set(0);
        
        // Shut down the scheduler
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(1, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
```

### 2. Using Timer and TimerTask

```java
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

class DeferredCallback {
    private final Timer timer;
    private final Map<Integer, TimerTask> callbacks;
    private final AtomicInteger nextCallbackId;
    private final AtomicInteger pendingCallbacksCount;
    private volatile boolean isShutdown;
    
    public DeferredCallback() {
        this.timer = new Timer("DeferredCallbackTimer", true);
        this.callbacks = new ConcurrentHashMap<>();
        this.nextCallbackId = new AtomicInteger(1);
        this.pendingCallbacksCount = new AtomicInteger(0);
        this.isShutdown = false;
    }
    
    public int registerCallback(Runnable callback, long delayMs) {
        if (isShutdown) {
            throw new IllegalStateException("Callback system is shut down");
        }
        
        int callbackId = nextCallbackId.getAndIncrement();
        
        // Create a TimerTask for the callback
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    callback.run();
                } finally {
                    callbacks.remove(callbackId);
                    pendingCallbacksCount.decrementAndGet();
                }
            }
        };
        
        // Schedule the task
        timer.schedule(task, delayMs);
        callbacks.put(callbackId, task);
        pendingCallbacksCount.incrementAndGet();
        
        return callbackId;
    }
    
    public boolean cancelCallback(int callbackId) {
        if (isShutdown) {
            return false;
        }
        
        TimerTask task = callbacks.remove(callbackId);
        if (task != null) {
            boolean canceled = task.cancel();
            if (canceled) {
                pendingCallbacksCount.decrementAndGet();
            }
            return canceled;
        }
        
        return false;
    }
    
    public int getPendingCallbacksCount() {
        return pendingCallbacksCount.get();
    }
    
    public void shutdown() {
        if (isShutdown) {
            return;
        }
        
        isShutdown = true;
        
        // Cancel all pending callbacks
        for (TimerTask task : callbacks.values()) {
            task.cancel();
        }
        
        callbacks.clear();
        pendingCallbacksCount.set(0);
        
        // Shut down the timer
        timer.cancel();
    }
}
```

### 3. Using Custom Thread and PriorityQueue

```java
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class DeferredCallback {
    private final Thread callbackThread;
    private final PriorityQueue<ScheduledCallback> callbackQueue;
    private final Map<Integer, ScheduledCallback> callbackMap;
    private final ReentrantLock lock;
    private final Condition newCallback;
    private final AtomicInteger nextCallbackId;
    private final AtomicInteger pendingCallbacksCount;
    private volatile boolean isShutdown;
    
    public DeferredCallback() {
        this.callbackQueue = new PriorityQueue<>((c1, c2) -> Long.compare(c1.executionTime, c2.executionTime));
        this.callbackMap = new ConcurrentHashMap<>();
        this.lock = new ReentrantLock();
        this.newCallback = lock.newCondition();
        this.nextCallbackId = new AtomicInteger(1);
        this.pendingCallbacksCount = new AtomicInteger(0);
        this.isShutdown = false;
        
        // Create and start the callback thread
        this.callbackThread = new Thread(this::processCallbacks);
        this.callbackThread.setDaemon(true);
        this.callbackThread.start();
    }
    
    public int registerCallback(Runnable callback, long delayMs) {
        if (isShutdown) {
            throw new IllegalStateException("Callback system is shut down");
        }
        
        int callbackId = nextCallbackId.getAndIncrement();
        long executionTime = System.currentTimeMillis() + delayMs;
        
        ScheduledCallback scheduledCallback = new ScheduledCallback(callbackId, callback, executionTime);
        
        lock.lock();
        try {
            callbackQueue.offer(scheduledCallback);
            callbackMap.put(callbackId, scheduledCallback);
            pendingCallbacksCount.incrementAndGet();
            newCallback.signal();
        } finally {
            lock.unlock();
        }
        
        return callbackId;
    }
    
    public boolean cancelCallback(int callbackId) {
        if (isShutdown) {
            return false;
        }
        
        lock.lock();
        try {
            ScheduledCallback callback = callbackMap.remove(callbackId);
            if (callback != null) {
                callback.canceled = true;
                pendingCallbacksCount.decrementAndGet();
                return true;
            }
            return false;
        } finally {
            lock.unlock();
        }
    }
    
    public int getPendingCallbacksCount() {
        return pendingCallbacksCount.get();
    }
    
    public void shutdown() {
        if (isShutdown) {
            return;
        }
        
        lock.lock();
        try {
            isShutdown = true;
            callbackQueue.clear();
            callbackMap.clear();
            pendingCallbacksCount.set(0);
            newCallback.signalAll();
        } finally {
            lock.unlock();
        }
        
        // Interrupt the callback thread
        callbackThread.interrupt();
        try {
            callbackThread.join(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    private void processCallbacks() {
        while (!isShutdown) {
            ScheduledCallback callback = null;
            
            lock.lock();
            try {
                while (!isShutdown && (callbackQueue.isEmpty() || callbackQueue.peek().executionTime > System.currentTimeMillis())) {
                    if (callbackQueue.isEmpty()) {
                        newCallback.await();
                    } else {
                        long waitTime = callbackQueue.peek().executionTime - System.currentTimeMillis();
                        if (waitTime > 0) {
                            newCallback.await(waitTime, TimeUnit.MILLISECONDS);
                        } else {
                            break;
                        }
                    }
                }
                
                if (!isShutdown && !callbackQueue.isEmpty() && callbackQueue.peek().executionTime <= System.currentTimeMillis()) {
                    callback = callbackQueue.poll();
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            } finally {
                lock.unlock();
            }
            
            // Execute the callback outside the lock
            if (callback != null && !callback.canceled) {
                try {
                    callback.runnable.run();
                } catch (Exception e) {
                    // Log exception
                } finally {
                    lock.lock();
                    try {
                        callbackMap.remove(callback.id);
                        pendingCallbacksCount.decrementAndGet();
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }
    }
    
    private static class ScheduledCallback {
        final int id;
        final Runnable runnable;
        final long executionTime;
        volatile boolean canceled;
        
        ScheduledCallback(int id, Runnable runnable, long executionTime) {
            this.id = id;
            this.runnable = runnable;
            this.executionTime = executionTime;
            this.canceled = false;
        }
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: 
  - O(1) for registerCallback, cancelCallback, and getPendingCallbacksCount operations
  - O(n) for shutdown operation, where n is the number of pending callbacks
- Space Complexity: O(n) where n is the number of pending callbacks

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe deferred callback system.
2. The ScheduledExecutorService approach leverages Java's built-in thread pool and scheduling capabilities.
3. The Timer and TimerTask approach uses Java's older scheduling mechanism.
4. The Custom Thread and PriorityQueue approach provides more control over the scheduling and execution.
5. All implementations must handle the case where multiple threads try to register or cancel callbacks simultaneously.
6. The key challenge is ensuring that callbacks are executed as close as possible to their scheduled time.
7. The implementation must also handle cleanup and shutdown gracefully.

## Interview Tips

- Be prepared to discuss the trade-offs between different scheduling implementations.
- Understand how to use ScheduledExecutorService for task scheduling.
- Know the difference between fixed-rate and fixed-delay scheduling.
- Be able to explain how to handle callback exceptions without affecting other callbacks.
- Consider edge cases like what happens if a callback takes longer to execute than expected.
- Discuss how the solution would scale with a large number of callbacks and varying delays.
- Mention that in a real-world scenario, you would need to consider callback prioritization, error handling, and retry mechanisms.


### 24. Thread-Safe Circular Buffer (Medium)


## Problem Description

Implement a thread-safe circular buffer (also known as a ring buffer) that allows multiple threads to read from and write to it concurrently:

* `CircularBuffer(int capacity)` Initializes the circular buffer with a maximum capacity of `capacity` elements.
* `boolean write(T item)` Writes an item to the buffer. Returns true if the write was successful, false if the buffer is full.
* `T read()` Reads and removes an item from the buffer. Returns null if the buffer is empty.
* `boolean isEmpty()` Returns true if the buffer is empty, false otherwise.
* `boolean isFull()` Returns true if the buffer is full, false otherwise.
* `int size()` Returns the number of items currently in the buffer.
* `int capacity()` Returns the maximum capacity of the buffer.

The circular buffer implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can read from and write to the buffer concurrently.
3. The buffer should follow the First-In-First-Out (FIFO) principle.
4. The implementation should be efficient and minimize contention between threads.

**Example 1:**
```
Input: 
["CircularBuffer", "isEmpty", "write", "write", "size", "read", "write", "isFull", "read", "read", "isEmpty"]
[[2], [], ["A"], ["B"], [], [], ["C"], [], [], [], []]

Output: 
[null, true, true, true, 2, "A", true, true, "B", "C", true]

Explanation:
CircularBuffer<String> buffer = new CircularBuffer<>(2);  // Initialize with capacity 2
buffer.isEmpty();                                         // Return true (buffer is empty)
buffer.write("A");                                        // Write "A" to buffer, return true
buffer.write("B");                                        // Write "B" to buffer, return true
buffer.size();                                            // Return 2 (two items in buffer)
buffer.read();                                            // Read and return "A"
buffer.write("C");                                        // Write "C" to buffer, return true
buffer.isFull();                                          // Return true (buffer is full)
buffer.read();                                            // Read and return "B"
buffer.read();                                            // Read and return "C"
buffer.isEmpty();                                         // Return true (buffer is empty)
```

## Video Explanation
[Java Concurrency: Circular Buffer Implementation (8:15 minutes)](https://www.youtube.com/watch?v=Jenkov.com/tutorials/java-performance/ring-buffer.html)

## Solution Approach

This problem tests your understanding of thread safety and circular buffer data structure. There are several ways to solve it:

### 1. Using ReentrantLock and Conditions

```java
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class CircularBuffer<T> {
    private final T[] buffer;
    private final int capacity;
    private int readIndex;
    private int writeIndex;
    private int count;
    
    private final ReentrantLock lock;
    private final Condition notEmpty;
    private final Condition notFull;
    
    @SuppressWarnings("unchecked")
    public CircularBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = (T[]) new Object[capacity];
        this.readIndex = 0;
        this.writeIndex = 0;
        this.count = 0;
        
        this.lock = new ReentrantLock();
        this.notEmpty = lock.newCondition();
        this.notFull = lock.newCondition();
    }
    
    public boolean write(T item) {
        if (item == null) {
            throw new NullPointerException("Cannot write null item");
        }
        
        lock.lock();
        try {
            if (count == capacity) {
                return false;
            }
            
            buffer[writeIndex] = item;
            writeIndex = (writeIndex + 1) % capacity;
            count++;
            
            notEmpty.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }
    
    public T read() {
        lock.lock();
        try {
            if (count == 0) {
                return null;
            }
            
            T item = buffer[readIndex];
            buffer[readIndex] = null; // Help GC
            readIndex = (readIndex + 1) % capacity;
            count--;
            
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }
    
    public boolean isEmpty() {
        lock.lock();
        try {
            return count == 0;
        } finally {
            lock.unlock();
        }
    }
    
    public boolean isFull() {
        lock.lock();
        try {
            return count == capacity;
        } finally {
            lock.unlock();
        }
    }
    
    public int size() {
        lock.lock();
        try {
            return count;
        } finally {
            lock.unlock();
        }
    }
    
    public int capacity() {
        return capacity;
    }
}
```

### 2. Using ArrayBlockingQueue

```java
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

class CircularBuffer<T> {
    private final ArrayBlockingQueue<T> queue;
    private final int capacity;
    
    public CircularBuffer(int capacity) {
        this.capacity = capacity;
        this.queue = new ArrayBlockingQueue<>(capacity);
    }
    
    public boolean write(T item) {
        if (item == null) {
            throw new NullPointerException("Cannot write null item");
        }
        
        return queue.offer(item);
    }
    
    public T read() {
        return queue.poll();
    }
    
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
    public boolean isFull() {
        return queue.size() == capacity;
    }
    
    public int size() {
        return queue.size();
    }
    
    public int capacity() {
        return capacity;
    }
}
```

### 3. Using Atomic Variables (Lock-Free)

```java
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

class CircularBuffer<T> {
    private final AtomicReferenceArray<T> buffer;
    private final int capacity;
    private final AtomicInteger readIndex;
    private final AtomicInteger writeIndex;
    private final AtomicInteger count;
    
    public CircularBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new AtomicReferenceArray<>(capacity);
        this.readIndex = new AtomicInteger(0);
        this.writeIndex = new AtomicInteger(0);
        this.count = new AtomicInteger(0);
    }
    
    public boolean write(T item) {
        if (item == null) {
            throw new NullPointerException("Cannot write null item");
        }
        
        // Check if buffer is full
        if (count.get() == capacity) {
            return false;
        }
        
        // Try to increment count atomically
        int currentCount;
        do {
            currentCount = count.get();
            if (currentCount == capacity) {
                return false;
            }
        } while (!count.compareAndSet(currentCount, currentCount + 1));
        
        // Get write index and increment it atomically
        int index = writeIndex.getAndUpdate(i -> (i + 1) % capacity);
        
        // Write the item
        buffer.set(index, item);
        
        return true;
    }
    
    public T read() {
        // Check if buffer is empty
        if (count.get() == 0) {
            return null;
        }
        
        // Try to decrement count atomically
        int currentCount;
        do {
            currentCount = count.get();
            if (currentCount == 0) {
                return null;
            }
        } while (!count.compareAndSet(currentCount, currentCount - 1));
        
        // Get read index and increment it atomically
        int index = readIndex.getAndUpdate(i -> (i + 1) % capacity);
        
        // Read the item
        T item = buffer.getAndSet(index, null);
        
        return item;
    }
    
    public boolean isEmpty() {
        return count.get() == 0;
    }
    
    public boolean isFull() {
        return count.get() == capacity;
    }
    
    public int size() {
        return count.get();
    }
    
    public int capacity() {
        return capacity;
    }
}
```

### 4. Using Synchronized Methods

```java
class CircularBuffer<T> {
    private final T[] buffer;
    private final int capacity;
    private int readIndex;
    private int writeIndex;
    private int count;
    
    @SuppressWarnings("unchecked")
    public CircularBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = (T[]) new Object[capacity];
        this.readIndex = 0;
        this.writeIndex = 0;
        this.count = 0;
    }
    
    public synchronized boolean write(T item) {
        if (item == null) {
            throw new NullPointerException("Cannot write null item");
        }
        
        if (count == capacity) {
            return false;
        }
        
        buffer[writeIndex] = item;
        writeIndex = (writeIndex + 1) % capacity;
        count++;
        
        notifyAll(); // Notify waiting readers
        return true;
    }
    
    public synchronized T read() {
        if (count == 0) {
            return null;
        }
        
        T item = buffer[readIndex];
        buffer[readIndex] = null; // Help GC
        readIndex = (readIndex + 1) % capacity;
        count--;
        
        notifyAll(); // Notify waiting writers
        return item;
    }
    
    public synchronized boolean isEmpty() {
        return count == 0;
    }
    
    public synchronized boolean isFull() {
        return count == capacity;
    }
    
    public synchronized int size() {
        return count;
    }
    
    public int capacity() {
        return capacity;
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: 
  - O(1) for write, read, isEmpty, isFull, size, and capacity operations
- Space Complexity: O(n) where n is the capacity of the buffer

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe circular buffer.
2. The ReentrantLock and Conditions approach provides fine-grained control over locking and waiting.
3. The ArrayBlockingQueue approach leverages Java's built-in thread-safe queue implementation.
4. The Atomic Variables approach provides a lock-free implementation that can reduce contention.
5. The Synchronized Methods approach is simpler but may have higher contention in high-concurrency scenarios.
6. All implementations must handle the case where multiple threads try to read from or write to the buffer simultaneously.
7. The key challenge is ensuring that the read and write indices are updated atomically and that the count is maintained correctly.

## Interview Tips

- Be prepared to discuss the trade-offs between different circular buffer implementations.
- Understand how to use ReentrantLock and Conditions for thread synchronization.
- Know the difference between blocking and non-blocking operations.
- Be able to explain how to handle the wrap-around behavior of the circular buffer.
- Consider edge cases like what happens if multiple threads try to read from an empty buffer or write to a full buffer simultaneously.
- Discuss how the solution would scale with a large number of threads and buffer elements.
- Mention that in a real-world scenario, you would need to consider blocking vs. non-blocking behavior, timeout mechanisms, and error handling.


### 25. Thread-Safe Priority Queue (Medium)


## Problem Description

Implement a thread-safe priority queue that allows multiple threads to add and remove elements concurrently:

* `PriorityQueue(int initialCapacity, Comparator<T> comparator)` Initializes the priority queue with the given initial capacity and comparator.
* `void add(T item)` Adds an item to the priority queue.
* `T poll()` Retrieves and removes the highest-priority element, or returns null if the queue is empty.
* `T peek()` Retrieves but does not remove the highest-priority element, or returns null if the queue is empty.
* `boolean remove(T item)` Removes a specific item from the queue if present. Returns true if the item was found and removed, false otherwise.
* `boolean contains(T item)` Returns true if the queue contains the specified item, false otherwise.
* `int size()` Returns the number of elements in the queue.
* `boolean isEmpty()` Returns true if the queue is empty, false otherwise.

The priority queue implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can add and remove elements concurrently.
3. The queue should maintain the priority order of elements according to the provided comparator.
4. The implementation should be efficient and minimize contention between threads.

**Example 1:**
```
Input: 
["PriorityQueue", "isEmpty", "add", "add", "add", "peek", "poll", "size", "contains", "remove", "isEmpty"]
[[10, (a, b) -> a - b], [], [5], [3], [7], [], [], [], [3], [3], []]

Output: 
[null, true, null, null, null, 3, 3, 2, false, false, false]

Explanation:
PriorityQueue<Integer> queue = new PriorityQueue<>(10, (a, b) -> a - b);  // Initialize with min-heap comparator
queue.isEmpty();                                                          // Return true (queue is empty)
queue.add(5);                                                             // Add 5 to queue
queue.add(3);                                                             // Add 3 to queue
queue.add(7);                                                             // Add 7 to queue
queue.peek();                                                             // Return 3 (minimum element)
queue.poll();                                                             // Remove and return 3
queue.size();                                                             // Return 2 (two elements in queue)
queue.contains(3);                                                        // Return false (3 was removed)
queue.remove(3);                                                          // Return false (3 is not in queue)
queue.isEmpty();                                                          // Return false (queue is not empty)
```

## Video Explanation
[Java Concurrency: PriorityBlockingQueue Implementation (6:45 minutes)](https://www.youtube.com/watch?v=happycoders.eu/algorithms/priorityblockingqueue-java/)

## Solution Approach

This problem tests your understanding of thread safety and priority queue data structure. There are several ways to solve it:

### 1. Using PriorityBlockingQueue

```java
import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;

class ThreadSafePriorityQueue<T> {
    private final PriorityBlockingQueue<T> queue;
    
    public ThreadSafePriorityQueue(int initialCapacity, Comparator<T> comparator) {
        this.queue = new PriorityBlockingQueue<>(initialCapacity, comparator);
    }
    
    public void add(T item) {
        if (item == null) {
            throw new NullPointerException("Cannot add null item");
        }
        queue.add(item);
    }
    
    public T poll() {
        return queue.poll();
    }
    
    public T peek() {
        return queue.peek();
    }
    
    public boolean remove(T item) {
        return queue.remove(item);
    }
    
    public boolean contains(T item) {
        return queue.contains(item);
    }
    
    public int size() {
        return queue.size();
    }
    
    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
```

### 2. Using ReentrantLock and PriorityQueue

```java
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.locks.ReentrantLock;

class ThreadSafePriorityQueue<T> {
    private final PriorityQueue<T> queue;
    private final ReentrantLock lock;
    
    public ThreadSafePriorityQueue(int initialCapacity, Comparator<T> comparator) {
        this.queue = new PriorityQueue<>(initialCapacity, comparator);
        this.lock = new ReentrantLock();
    }
    
    public void add(T item) {
        if (item == null) {
            throw new NullPointerException("Cannot add null item");
        }
        
        lock.lock();
        try {
            queue.add(item);
        } finally {
            lock.unlock();
        }
    }
    
    public T poll() {
        lock.lock();
        try {
            return queue.poll();
        } finally {
            lock.unlock();
        }
    }
    
    public T peek() {
        lock.lock();
        try {
            return queue.peek();
        } finally {
            lock.unlock();
        }
    }
    
    public boolean remove(T item) {
        lock.lock();
        try {
            return queue.remove(item);
        } finally {
            lock.unlock();
        }
    }
    
    public boolean contains(T item) {
        lock.lock();
        try {
            return queue.contains(item);
        } finally {
            lock.unlock();
        }
    }
    
    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
    
    public boolean isEmpty() {
        lock.lock();
        try {
            return queue.isEmpty();
        } finally {
            lock.unlock();
        }
    }
}
```

### 3. Using Synchronized Methods

```java
import java.util.Comparator;
import java.util.PriorityQueue;

class ThreadSafePriorityQueue<T> {
    private final PriorityQueue<T> queue;
    
    public ThreadSafePriorityQueue(int initialCapacity, Comparator<T> comparator) {
        this.queue = new PriorityQueue<>(initialCapacity, comparator);
    }
    
    public synchronized void add(T item) {
        if (item == null) {
            throw new NullPointerException("Cannot add null item");
        }
        queue.add(item);
    }
    
    public synchronized T poll() {
        return queue.poll();
    }
    
    public synchronized T peek() {
        return queue.peek();
    }
    
    public synchronized boolean remove(T item) {
        return queue.remove(item);
    }
    
    public synchronized boolean contains(T item) {
        return queue.contains(item);
    }
    
    public synchronized int size() {
        return queue.size();
    }
    
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
}
```

### 4. Using Read-Write Lock for Better Concurrency

```java
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ThreadSafePriorityQueue<T> {
    private final PriorityQueue<T> queue;
    private final ReadWriteLock rwLock;
    
    public ThreadSafePriorityQueue(int initialCapacity, Comparator<T> comparator) {
        this.queue = new PriorityQueue<>(initialCapacity, comparator);
        this.rwLock = new ReentrantReadWriteLock();
    }
    
    public void add(T item) {
        if (item == null) {
            throw new NullPointerException("Cannot add null item");
        }
        
        rwLock.writeLock().lock();
        try {
            queue.add(item);
        } finally {
            rwLock.writeLock().unlock();
        }
    }
    
    public T poll() {
        rwLock.writeLock().lock();
        try {
            return queue.poll();
        } finally {
            rwLock.writeLock().unlock();
        }
    }
    
    public T peek() {
        rwLock.readLock().lock();
        try {
            return queue.peek();
        } finally {
            rwLock.readLock().unlock();
        }
    }
    
    public boolean remove(T item) {
        rwLock.writeLock().lock();
        try {
            return queue.remove(item);
        } finally {
            rwLock.writeLock().unlock();
        }
    }
    
    public boolean contains(T item) {
        rwLock.readLock().lock();
        try {
            return queue.contains(item);
        } finally {
            rwLock.readLock().unlock();
        }
    }
    
    public int size() {
        rwLock.readLock().lock();
        try {
            return queue.size();
        } finally {
            rwLock.readLock().unlock();
        }
    }
    
    public boolean isEmpty() {
        rwLock.readLock().lock();
        try {
            return queue.isEmpty();
        } finally {
            rwLock.readLock().unlock();
        }
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: 
  - O(log n) for add and remove operations
  - O(1) for peek, poll, size, and isEmpty operations
  - O(n) for contains operation
- Space Complexity: O(n) where n is the number of elements in the queue

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe priority queue.
2. The PriorityBlockingQueue approach leverages Java's built-in thread-safe priority queue implementation.
3. The ReentrantLock and PriorityQueue approach provides fine-grained control over locking.
4. The Synchronized Methods approach is simpler but may have higher contention in high-concurrency scenarios.
5. The Read-Write Lock approach allows multiple readers to access the queue simultaneously, improving concurrency for read-heavy workloads.
6. All implementations must handle the case where multiple threads try to add or remove elements simultaneously.
7. The key challenge is ensuring that the priority order is maintained correctly while providing thread safety.

## Interview Tips

- Be prepared to discuss the trade-offs between different priority queue implementations.
- Understand how to use PriorityBlockingQueue for thread-safe priority queue operations.
- Know the difference between read and write locks and when to use each.
- Be able to explain how the priority queue maintains its order and the time complexity of its operations.
- Consider edge cases like what happens if multiple threads try to remove the same element simultaneously.
- Discuss how the solution would scale with a large number of threads and queue elements.
- Mention that in a real-world scenario, you would need to consider blocking vs. non-blocking behavior, timeout mechanisms, and error handling.


### 26. Thread-Safe LRU Cache (Medium)


## Problem Description

Implement a thread-safe Least Recently Used (LRU) cache that allows multiple threads to read from and write to it concurrently:

* `LRUCache(int capacity)` Initializes the LRU cache with a maximum capacity of `capacity` elements.
* `Integer get(int key)` Returns the value of the key if it exists in the cache, otherwise returns -1. This operation should also mark the key as recently used.
* `void put(int key, int value)` Updates the value of the key if it exists, or adds the key-value pair to the cache if the key doesn't exist. When the cache reaches its capacity, it should evict the least recently used key before inserting a new item.
* `int size()` Returns the number of key-value pairs currently in the cache.
* `boolean containsKey(int key)` Returns true if the cache contains the specified key, false otherwise.
* `void clear()` Removes all key-value pairs from the cache.

The LRU cache implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can read from and write to the cache concurrently.
3. The cache should maintain the order of elements according to their usage, with the least recently used element being evicted first when the cache is full.
4. The implementation should be efficient and minimize contention between threads.

**Example 1:**
```
Input: 
["LRUCache", "put", "put", "get", "put", "get", "get", "size", "containsKey", "clear", "size"]
[[2], [1, 1], [2, 2], [1], [3, 3], [2], [3], [], [1], [], []]

Output: 
[null, null, null, 1, null, -1, 3, 1, false, null, 0]

Explanation:
LRUCache cache = new LRUCache(2);  // Initialize with capacity 2
cache.put(1, 1);                   // Add key 1 with value 1
cache.put(2, 2);                   // Add key 2 with value 2
cache.get(1);                      // Return 1 (key 1 exists)
cache.put(3, 3);                   // Add key 3 with value 3, evict key 2
cache.get(2);                      // Return -1 (key 2 was evicted)
cache.get(3);                      // Return 3 (key 3 exists)
cache.size();                      // Return 1 (one key-value pair in cache)
cache.containsKey(1);              // Return false (key 1 was evicted)
cache.clear();                     // Clear the cache
cache.size();                      // Return 0 (cache is empty)
```

## Video Explanation
[Java Concurrency: Thread-Safe LRU Cache Implementation (9:15 minutes)](https://www.youtube.com/watch?v=wvhQ9vevmrE)

## Solution Approach

This problem tests your understanding of thread safety and LRU cache data structure. There are several ways to solve it:

### 1. Using LinkedHashMap and ReentrantReadWriteLock

```java
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class LRUCache {
    private final int capacity;
    private final Map<Integer, Integer> cache;
    private final ReadWriteLock lock;
    
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.lock = new ReentrantReadWriteLock();
        
        // Create a LinkedHashMap with access order
        this.cache = new LinkedHashMap<Integer, Integer>(capacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<Integer, Integer> eldest) {
                return size() > capacity;
            }
        };
    }
    
    public Integer get(int key) {
        lock.readLock().lock();
        try {
            Integer value = cache.get(key);
            return value != null ? value : -1;
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public void put(int key, int value) {
        lock.writeLock().lock();
        try {
            cache.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public int size() {
        lock.readLock().lock();
        try {
            return cache.size();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public boolean containsKey(int key) {
        lock.readLock().lock();
        try {
            return cache.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public void clear() {
        lock.writeLock().lock();
        try {
            cache.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
}
```

### 2. Using ConcurrentHashMap and ConcurrentLinkedQueue

```java
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.ReentrantLock;

class LRUCache {
    private final int capacity;
    private final Map<Integer, Integer> cache;
    private final Queue<Integer> queue;
    private final ReentrantLock lock;
    
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new ConcurrentHashMap<>();
        this.queue = new ConcurrentLinkedQueue<>();
        this.lock = new ReentrantLock();
    }
    
    public Integer get(int key) {
        Integer value = cache.get(key);
        if (value != null) {
            // Update the access order
            lock.lock();
            try {
                queue.remove(key);
                queue.add(key);
            } finally {
                lock.unlock();
            }
            return value;
        }
        return -1;
    }
    
    public void put(int key, int value) {
        lock.lock();
        try {
            if (cache.containsKey(key)) {
                // Update existing key
                cache.put(key, value);
                queue.remove(key);
                queue.add(key);
            } else {
                // Add new key
                if (cache.size() >= capacity) {
                    // Evict least recently used key
                    Integer lruKey = queue.poll();
                    if (lruKey != null) {
                        cache.remove(lruKey);
                    }
                }
                cache.put(key, value);
                queue.add(key);
            }
        } finally {
            lock.unlock();
        }
    }
    
    public int size() {
        return cache.size();
    }
    
    public boolean containsKey(int key) {
        return cache.containsKey(key);
    }
    
    public void clear() {
        lock.lock();
        try {
            cache.clear();
            queue.clear();
        } finally {
            lock.unlock();
        }
    }
}
```

### 3. Using Custom Doubly Linked List and ConcurrentHashMap

```java
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

class LRUCache {
    private final int capacity;
    private final Map<Integer, Node> cache;
    private final Node head;
    private final Node tail;
    private final ReentrantLock lock;
    
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cache = new ConcurrentHashMap<>();
        this.head = new Node(0, 0); // Dummy head
        this.tail = new Node(0, 0); // Dummy tail
        this.lock = new ReentrantLock();
        
        head.next = tail;
        tail.prev = head;
    }
    
    public Integer get(int key) {
        Node node = cache.get(key);
        if (node != null) {
            lock.lock();
            try {
                // Move to head (most recently used)
                removeNode(node);
                addToHead(node);
            } finally {
                lock.unlock();
            }
            return node.value;
        }
        return -1;
    }
    
    public void put(int key, int value) {
        lock.lock();
        try {
            Node node = cache.get(key);
            if (node != null) {
                // Update existing key
                node.value = value;
                removeNode(node);
                addToHead(node);
            } else {
                // Add new key
                Node newNode = new Node(key, value);
                cache.put(key, newNode);
                addToHead(newNode);
                
                // Evict if necessary
                if (cache.size() > capacity) {
                    Node tail = removeTail();
                    cache.remove(tail.key);
                }
            }
        } finally {
            lock.unlock();
        }
    }
    
    public int size() {
        return cache.size();
    }
    
    public boolean containsKey(int key) {
        return cache.containsKey(key);
    }
    
    public void clear() {
        lock.lock();
        try {
            cache.clear();
            head.next = tail;
            tail.prev = head;
        } finally {
            lock.unlock();
        }
    }
    
    private void addToHead(Node node) {
        node.next = head.next;
        node.prev = head;
        head.next.prev = node;
        head.next = node;
    }
    
    private void removeNode(Node node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
    
    private Node removeTail() {
        Node res = tail.prev;
        removeNode(res);
        return res;
    }
    
    private static class Node {
        int key;
        int value;
        Node prev;
        Node next;
        
        Node(int key, int value) {
            this.key = key;
            this.value = value;
        }
    }
}
```

### 4. Using Guava's CacheBuilder

```java
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

class LRUCache {
    private final LoadingCache<Integer, Integer> cache;
    
    public LRUCache(int capacity) {
        cache = CacheBuilder.newBuilder()
            .maximumSize(capacity)
            .build(
                new CacheLoader<Integer, Integer>() {
                    @Override
                    public Integer load(Integer key) {
                        return -1; // Default value for missing keys
                    }
                }
            );
    }
    
    public Integer get(int key) {
        try {
            Integer value = cache.get(key);
            return value.equals(-1) ? -1 : value;
        } catch (ExecutionException e) {
            return -1;
        }
    }
    
    public void put(int key, int value) {
        cache.put(key, value);
    }
    
    public int size() {
        return (int) cache.size();
    }
    
    public boolean containsKey(int key) {
        return cache.asMap().containsKey(key);
    }
    
    public void clear() {
        cache.invalidateAll();
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: 
  - O(1) for get, put, size, containsKey, and clear operations
- Space Complexity: O(n) where n is the capacity of the cache

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe LRU cache.
2. The LinkedHashMap and ReentrantReadWriteLock approach leverages Java's built-in LRU-like map implementation with fine-grained locking.
3. The ConcurrentHashMap and ConcurrentLinkedQueue approach uses thread-safe collections but requires careful synchronization for the eviction logic.
4. The Custom Doubly Linked List and ConcurrentHashMap approach provides more control over the LRU behavior but requires more complex synchronization.
5. The Guava's CacheBuilder approach leverages a well-tested library implementation with built-in thread safety.
6. All implementations must handle the case where multiple threads try to access or modify the cache simultaneously.
7. The key challenge is ensuring that the LRU order is maintained correctly while providing thread safety.

## Interview Tips

- Be prepared to discuss the trade-offs between different LRU cache implementations.
- Understand how to use ReadWriteLock for better concurrency in read-heavy workloads.
- Know the difference between LinkedHashMap's access order and insertion order.
- Be able to explain how the LRU eviction policy works and how to implement it efficiently.
- Consider edge cases like what happens if multiple threads try to evict elements simultaneously.
- Discuss how the solution would scale with a large number of threads and cache elements.
- Mention that in a real-world scenario, you would need to consider cache expiration, statistics tracking, and error handling.


### 27. Thread-Safe Concurrent HashMap (Medium)


## Problem Description

Implement a thread-safe concurrent hash map that allows multiple threads to read from and write to it concurrently:

* `ConcurrentMap(int initialCapacity, float loadFactor, int concurrencyLevel)` Initializes the concurrent map with the given initial capacity, load factor, and concurrency level.
* `V put(K key, V value)` Associates the specified value with the specified key in this map. If the map previously contained a mapping for the key, the old value is replaced.
* `V get(K key)` Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
* `V remove(K key)` Removes the mapping for a key from this map if it is present. Returns the value to which this map previously associated the key, or null if the map contained no mapping for the key.
* `boolean containsKey(K key)` Returns true if this map contains a mapping for the specified key.
* `int size()` Returns the number of key-value mappings in this map.
* `void clear()` Removes all of the mappings from this map.
* `V putIfAbsent(K key, V value)` If the specified key is not already associated with a value, associates it with the given value. Returns the previous value associated with the specified key, or null if there was no mapping for the key.
* `boolean remove(K key, V value)` Removes the entry for a key only if currently mapped to a given value. Returns true if the value was removed.
* `boolean replace(K key, V oldValue, V newValue)` Replaces the entry for a key only if currently mapped to a given value. Returns true if the value was replaced.

The concurrent map implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can read from and write to the map concurrently.
3. The implementation should minimize contention between threads.
4. The implementation should provide high concurrency for retrievals.

**Example 1:**
```
Input: 
["ConcurrentMap", "put", "put", "get", "putIfAbsent", "size", "remove", "containsKey", "replace", "clear", "size"]
[[16, 0.75, 16], ["key1", "value1"], ["key2", "value2"], ["key1"], ["key1", "value3"], [], ["key2"], ["key2"], ["key1", "value3", "value4"], [], []]

Output: 
[null, null, null, "value1", "value1", 2, "value2", false, true, null, 0]

Explanation:
ConcurrentMap<String, String> map = new ConcurrentMap<>(16, 0.75f, 16);  // Initialize with capacity 16, load factor 0.75, concurrency level 16
map.put("key1", "value1");                                               // Add key1 with value1
map.put("key2", "value2");                                               // Add key2 with value2
map.get("key1");                                                         // Return "value1"
map.putIfAbsent("key1", "value3");                                       // Return "value1" (key1 already exists)
map.size();                                                              // Return 2 (two key-value pairs in map)
map.remove("key2");                                                      // Remove key2 and return "value2"
map.containsKey("key2");                                                 // Return false (key2 was removed)
map.replace("key1", "value3", "value4");                                 // Return true (key1 exists with value "value1", not "value3")
map.clear();                                                             // Clear the map
map.size();                                                              // Return 0 (map is empty)
```

## Video Explanation
[Java Concurrency: ConcurrentHashMap Implementation (7:45 minutes)](https://www.youtube.com/watch?v=vicksheet.medium.com/unlocking-the-power-of-concurrenthashmap-a-thread-safe-guide-7697d2fdf75d)

## Solution Approach

This problem tests your understanding of thread safety and concurrent hash map data structure. There are several ways to solve it:

### 1. Using Java's ConcurrentHashMap

```java
import java.util.concurrent.ConcurrentHashMap;

class ConcurrentMap<K, V> {
    private final ConcurrentHashMap<K, V> map;
    
    public ConcurrentMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        this.map = new ConcurrentHashMap<>(initialCapacity, loadFactor, concurrencyLevel);
    }
    
    public V put(K key, V value) {
        return map.put(key, value);
    }
    
    public V get(K key) {
        return map.get(key);
    }
    
    public V remove(K key) {
        return map.remove(key);
    }
    
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }
    
    public int size() {
        return map.size();
    }
    
    public void clear() {
        map.clear();
    }
    
    public V putIfAbsent(K key, V value) {
        return map.putIfAbsent(key, value);
    }
    
    public boolean remove(K key, V value) {
        return map.remove(key, value);
    }
    
    public boolean replace(K key, V oldValue, V newValue) {
        return map.replace(key, oldValue, newValue);
    }
}
```

### 2. Using Synchronized HashMap with Fine-Grained Locking

```java
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

class ConcurrentMap<K, V> {
    private static final int DEFAULT_SEGMENTS = 16;
    
    private final Map<K, V>[] segments;
    private final ReentrantLock[] locks;
    private final int segmentMask;
    private final int segmentShift;
    
    @SuppressWarnings("unchecked")
    public ConcurrentMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        // Ensure power of 2
        int segments = 1;
        while (segments < concurrencyLevel) {
            segments <<= 1;
        }
        
        this.segmentShift = 32 - Integer.numberOfLeadingZeros(segments - 1);
        this.segmentMask = segments - 1;
        
        this.segments = new HashMap[segments];
        this.locks = new ReentrantLock[segments];
        
        int segmentCapacity = initialCapacity / segments;
        if (segmentCapacity < 2) {
            segmentCapacity = 2;
        }
        
        for (int i = 0; i < segments; i++) {
            this.segments[i] = new HashMap<>(segmentCapacity, loadFactor);
            this.locks[i] = new ReentrantLock();
        }
    }
    
    private int hash(Object key) {
        int h = key.hashCode();
        h += (h << 15) ^ 0xffffcd7d;
        h ^= (h >>> 10);
        h += (h << 3);
        h ^= (h >>> 6);
        h += (h << 2) + (h << 14);
        h ^= (h >>> 16);
        return h;
    }
    
    private int segmentFor(int hash) {
        return (hash >>> segmentShift) & segmentMask;
    }
    
    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException();
        }
        
        int hash = hash(key);
        int segment = segmentFor(hash);
        
        locks[segment].lock();
        try {
            return segments[segment].put(key, value);
        } finally {
            locks[segment].unlock();
        }
    }
    
    public V get(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        
        int hash = hash(key);
        int segment = segmentFor(hash);
        
        locks[segment].lock();
        try {
            return segments[segment].get(key);
        } finally {
            locks[segment].unlock();
        }
    }
    
    public V remove(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        
        int hash = hash(key);
        int segment = segmentFor(hash);
        
        locks[segment].lock();
        try {
            return segments[segment].remove(key);
        } finally {
            locks[segment].unlock();
        }
    }
    
    public boolean containsKey(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        
        int hash = hash(key);
        int segment = segmentFor(hash);
        
        locks[segment].lock();
        try {
            return segments[segment].containsKey(key);
        } finally {
            locks[segment].unlock();
        }
    }
    
    public int size() {
        int size = 0;
        
        for (int i = 0; i < locks.length; i++) {
            locks[i].lock();
        }
        
        try {
            for (Map<K, V> segment : segments) {
                size += segment.size();
            }
        } finally {
            for (int i = 0; i < locks.length; i++) {
                locks[i].unlock();
            }
        }
        
        return size;
    }
    
    public void clear() {
        for (int i = 0; i < locks.length; i++) {
            locks[i].lock();
        }
        
        try {
            for (Map<K, V> segment : segments) {
                segment.clear();
            }
        } finally {
            for (int i = 0; i < locks.length; i++) {
                locks[i].unlock();
            }
        }
    }
    
    public V putIfAbsent(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException();
        }
        
        int hash = hash(key);
        int segment = segmentFor(hash);
        
        locks[segment].lock();
        try {
            V oldValue = segments[segment].get(key);
            if (oldValue == null) {
                segments[segment].put(key, value);
            }
            return oldValue;
        } finally {
            locks[segment].unlock();
        }
    }
    
    public boolean remove(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException();
        }
        
        int hash = hash(key);
        int segment = segmentFor(hash);
        
        locks[segment].lock();
        try {
            V oldValue = segments[segment].get(key);
            if (oldValue != null && oldValue.equals(value)) {
                segments[segment].remove(key);
                return true;
            }
            return false;
        } finally {
            locks[segment].unlock();
        }
    }
    
    public boolean replace(K key, V oldValue, V newValue) {
        if (key == null || oldValue == null || newValue == null) {
            throw new NullPointerException();
        }
        
        int hash = hash(key);
        int segment = segmentFor(hash);
        
        locks[segment].lock();
        try {
            V currentValue = segments[segment].get(key);
            if (currentValue != null && currentValue.equals(oldValue)) {
                segments[segment].put(key, newValue);
                return true;
            }
            return false;
        } finally {
            locks[segment].unlock();
        }
    }
}
```

### 3. Using Read-Write Locks for Better Concurrency

```java
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ConcurrentMap<K, V> {
    private static final int DEFAULT_SEGMENTS = 16;
    
    private final Map<K, V>[] segments;
    private final ReadWriteLock[] locks;
    private final int segmentMask;
    private final int segmentShift;
    
    @SuppressWarnings("unchecked")
    public ConcurrentMap(int initialCapacity, float loadFactor, int concurrencyLevel) {
        // Ensure power of 2
        int segments = 1;
        while (segments < concurrencyLevel) {
            segments <<= 1;
        }
        
        this.segmentShift = 32 - Integer.numberOfLeadingZeros(segments - 1);
        this.segmentMask = segments - 1;
        
        this.segments = new HashMap[segments];
        this.locks = new ReentrantReadWriteLock[segments];
        
        int segmentCapacity = initialCapacity / segments;
        if (segmentCapacity < 2) {
            segmentCapacity = 2;
        }
        
        for (int i = 0; i < segments; i++) {
            this.segments[i] = new HashMap<>(segmentCapacity, loadFactor);
            this.locks[i] = new ReentrantReadWriteLock();
        }
    }
    
    private int hash(Object key) {
        int h = key.hashCode();
        h += (h << 15) ^ 0xffffcd7d;
        h ^= (h >>> 10);
        h += (h << 3);
        h ^= (h >>> 6);
        h += (h << 2) + (h << 14);
        h ^= (h >>> 16);
        return h;
    }
    
    private int segmentFor(int hash) {
        return (hash >>> segmentShift) & segmentMask;
    }
    
    public V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException();
        }
        
        int hash = hash(key);
        int segment = segmentFor(hash);
        
        locks[segment].writeLock().lock();
        try {
            return segments[segment].put(key, value);
        } finally {
            locks[segment].writeLock().unlock();
        }
    }
    
    public V get(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        
        int hash = hash(key);
        int segment = segmentFor(hash);
        
        locks[segment].readLock().lock();
        try {
            return segments[segment].get(key);
        } finally {
            locks[segment].readLock().unlock();
        }
    }
    
    public V remove(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        
        int hash = hash(key);
        int segment = segmentFor(hash);
        
        locks[segment].writeLock().lock();
        try {
            return segments[segment].remove(key);
        } finally {
            locks[segment].writeLock().unlock();
        }
    }
    
    public boolean containsKey(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        
        int hash = hash(key);
        int segment = segmentFor(hash);
        
        locks[segment].readLock().lock();
        try {
            return segments[segment].containsKey(key);
        } finally {
            locks[segment].readLock().unlock();
        }
    }
    
    public int size() {
        int size = 0;
        
        for (int i = 0; i < locks.length; i++) {
            locks[i].readLock().lock();
        }
        
        try {
            for (Map<K, V> segment : segments) {
                size += segment.size();
            }
        } finally {
            for (int i = 0; i < locks.length; i++) {
                locks[i].readLock().unlock();
            }
        }
        
        return size;
    }
    
    public void clear() {
        for (int i = 0; i < locks.length; i++) {
            locks[i].writeLock().lock();
        }
        
        try {
            for (Map<K, V> segment : segments) {
                segment.clear();
            }
        } finally {
            for (int i = 0; i < locks.length; i++) {
                locks[i].writeLock().unlock();
            }
        }
    }
    
    public V putIfAbsent(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException();
        }
        
        int hash = hash(key);
        int segment = segmentFor(hash);
        
        locks[segment].writeLock().lock();
        try {
            V oldValue = segments[segment].get(key);
            if (oldValue == null) {
                segments[segment].put(key, value);
            }
            return oldValue;
        } finally {
            locks[segment].writeLock().unlock();
        }
    }
    
    public boolean remove(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException();
        }
        
        int hash = hash(key);
        int segment = segmentFor(hash);
        
        locks[segment].writeLock().lock();
        try {
            V oldValue = segments[segment].get(key);
            if (oldValue != null && oldValue.equals(value)) {
                segments[segment].remove(key);
                return true;
            }
            return false;
        } finally {
            locks[segment].writeLock().unlock();
        }
    }
    
    public boolean replace(K key, V oldValue, V newValue) {
        if (key == null || oldValue == null || newValue == null) {
            throw new NullPointerException();
        }
        
        int hash = hash(key);
        int segment = segmentFor(hash);
        
        locks[segment].writeLock().lock();
        try {
            V currentValue = segments[segment].get(key);
            if (currentValue != null && currentValue.equals(oldValue)) {
                segments[segment].put(key, newValue);
                return true;
            }
            return false;
        } finally {
            locks[segment].writeLock().unlock();
        }
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: 
  - O(1) for get, put, remove, containsKey, putIfAbsent, remove(key, value), and replace operations
  - O(n) for size and clear operations, where n is the number of segments
- Space Complexity: O(n) where n is the number of key-value pairs in the map

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe concurrent hash map.
2. The ConcurrentHashMap approach leverages Java's built-in thread-safe map implementation.
3. The Synchronized HashMap with Fine-Grained Locking approach divides the map into segments to reduce contention.
4. The Read-Write Locks approach allows multiple readers to access the map simultaneously, improving concurrency for read-heavy workloads.
5. All implementations must handle the case where multiple threads try to access or modify the map simultaneously.
6. The key challenge is ensuring thread safety while minimizing contention between threads.
7. The segmentation approach is similar to how Java's ConcurrentHashMap was implemented prior to Java 8.

## Interview Tips

- Be prepared to discuss the trade-offs between different concurrent map implementations.
- Understand how to use Read-Write locks for better concurrency in read-heavy workloads.
- Know the difference between ConcurrentHashMap and Collections.synchronizedMap.
- Be able to explain how the segmentation approach reduces contention between threads.
- Consider edge cases like what happens if multiple threads try to modify the same segment simultaneously.
- Discuss how the solution would scale with a large number of threads and map elements.
- Mention that in a real-world scenario, you would need to consider memory consistency, weak references, and iterators.


### 28. Thread-Safe Blocking Queue (Medium)


## Problem Description

Implement a thread-safe blocking queue that allows multiple threads to add and remove elements concurrently:

* `BlockingQueue(int capacity)` Initializes the blocking queue with the given capacity.
* `void put(E element)` Adds an element to the queue. If the queue is full, the calling thread is blocked until space becomes available.
* `E take()` Removes and returns the head of the queue. If the queue is empty, the calling thread is blocked until an element becomes available.
* `boolean offer(E element, long timeout, TimeUnit unit)` Adds an element to the queue, waiting up to the specified timeout if necessary. Returns true if successful, false if the timeout elapses before space is available.
* `E poll(long timeout, TimeUnit unit)` Retrieves and removes the head of the queue, waiting up to the specified timeout if necessary. Returns null if the timeout elapses before an element becomes available.
* `int size()` Returns the number of elements in the queue.
* `boolean isEmpty()` Returns true if the queue is empty, false otherwise.
* `boolean isFull()` Returns true if the queue is full, false otherwise.
* `int remainingCapacity()` Returns the number of additional elements that the queue can hold.

The blocking queue implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can add and remove elements concurrently.
3. The queue should block threads when necessary (when adding to a full queue or removing from an empty queue).
4. The implementation should be efficient and minimize contention between threads.

**Example 1:**
```
Input: 
["BlockingQueue", "isEmpty", "put", "put", "size", "take", "remainingCapacity", "offer", "poll", "isFull"]
[[3], [], [1], [2], [], [], [], [3, 100, TimeUnit.MILLISECONDS], [100, TimeUnit.MILLISECONDS], []]

Output: 
[null, true, null, null, 2, 1, 2, true, 2, false]

Explanation:
BlockingQueue<Integer> queue = new BlockingQueue<>(3);  // Initialize with capacity 3
queue.isEmpty();                                        // Return true (queue is empty)
queue.put(1);                                           // Add 1 to queue
queue.put(2);                                           // Add 2 to queue
queue.size();                                           // Return 2 (two elements in queue)
queue.take();                                           // Remove and return 1
queue.remainingCapacity();                              // Return 2 (can add 2 more elements)
queue.offer(3, 100, TimeUnit.MILLISECONDS);             // Add 3 to queue, return true
queue.poll(100, TimeUnit.MILLISECONDS);                 // Remove and return 2
queue.isFull();                                         // Return false (queue is not full)
```

## Video Explanation
[Java Concurrency: BlockingQueue Implementation (8:15 minutes)](https://www.youtube.com/watch?v=ZVG9Korean9Q)

## Solution Approach

This problem tests your understanding of thread safety and blocking queue data structure. There are several ways to solve it:

### 1. Using Java's ArrayBlockingQueue

```java
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

class BlockingQueue<E> {
    private final ArrayBlockingQueue<E> queue;
    private final int capacity;
    
    public BlockingQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new ArrayBlockingQueue<>(capacity);
    }
    
    public void put(E element) throws InterruptedException {
        queue.put(element);
    }
    
    public E take() throws InterruptedException {
        return queue.take();
    }
    
    public boolean offer(E element, long timeout, TimeUnit unit) throws InterruptedException {
        return queue.offer(element, timeout, unit);
    }
    
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        return queue.poll(timeout, unit);
    }
    
    public int size() {
        return queue.size();
    }
    
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
    public boolean isFull() {
        return queue.size() == capacity;
    }
    
    public int remainingCapacity() {
        return queue.remainingCapacity();
    }
}
```

### 2. Using ReentrantLock and Condition Variables

```java
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class BlockingQueue<E> {
    private final Queue<E> queue;
    private final int capacity;
    private final ReentrantLock lock;
    private final Condition notFull;
    private final Condition notEmpty;
    
    public BlockingQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
        this.lock = new ReentrantLock();
        this.notFull = lock.newCondition();
        this.notEmpty = lock.newCondition();
    }
    
    public void put(E element) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                notFull.await();
            }
            queue.add(element);
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }
    
    public E take() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                notEmpty.await();
            }
            E item = queue.remove();
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }
    
    public boolean offer(E element, long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        lock.lock();
        try {
            while (queue.size() == capacity) {
                if (nanos <= 0) {
                    return false;
                }
                nanos = notFull.awaitNanos(nanos);
            }
            queue.add(element);
            notEmpty.signal();
            return true;
        } finally {
            lock.unlock();
        }
    }
    
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        lock.lock();
        try {
            while (queue.isEmpty()) {
                if (nanos <= 0) {
                    return null;
                }
                nanos = notEmpty.awaitNanos(nanos);
            }
            E item = queue.remove();
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }
    
    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
    
    public boolean isEmpty() {
        lock.lock();
        try {
            return queue.isEmpty();
        } finally {
            lock.unlock();
        }
    }
    
    public boolean isFull() {
        lock.lock();
        try {
            return queue.size() == capacity;
        } finally {
            lock.unlock();
        }
    }
    
    public int remainingCapacity() {
        lock.lock();
        try {
            return capacity - queue.size();
        } finally {
            lock.unlock();
        }
    }
}
```

### 3. Using Synchronized Methods and wait/notify

```java
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

class BlockingQueue<E> {
    private final Queue<E> queue;
    private final int capacity;
    
    public BlockingQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }
    
    public synchronized void put(E element) throws InterruptedException {
        while (queue.size() == capacity) {
            wait();
        }
        queue.add(element);
        notifyAll();
    }
    
    public synchronized E take() throws InterruptedException {
        while (queue.isEmpty()) {
            wait();
        }
        E item = queue.remove();
        notifyAll();
        return item;
    }
    
    public synchronized boolean offer(E element, long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        long deadline = System.nanoTime() + nanos;
        
        while (queue.size() == capacity) {
            if (nanos <= 0) {
                return false;
            }
            wait(TimeUnit.NANOSECONDS.toMillis(nanos));
            nanos = deadline - System.nanoTime();
        }
        
        queue.add(element);
        notifyAll();
        return true;
    }
    
    public synchronized E poll(long timeout, TimeUnit unit) throws InterruptedException {
        long nanos = unit.toNanos(timeout);
        long deadline = System.nanoTime() + nanos;
        
        while (queue.isEmpty()) {
            if (nanos <= 0) {
                return null;
            }
            wait(TimeUnit.NANOSECONDS.toMillis(nanos));
            nanos = deadline - System.nanoTime();
        }
        
        E item = queue.remove();
        notifyAll();
        return item;
    }
    
    public synchronized int size() {
        return queue.size();
    }
    
    public synchronized boolean isEmpty() {
        return queue.isEmpty();
    }
    
    public synchronized boolean isFull() {
        return queue.size() == capacity;
    }
    
    public synchronized int remainingCapacity() {
        return capacity - queue.size();
    }
}
```

### 4. Using Semaphores

```java
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

class BlockingQueue<E> {
    private final Queue<E> queue;
    private final int capacity;
    private final Semaphore emptySlots;
    private final Semaphore filledSlots;
    private final ReentrantLock lock;
    
    public BlockingQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
        this.emptySlots = new Semaphore(capacity);
        this.filledSlots = new Semaphore(0);
        this.lock = new ReentrantLock();
    }
    
    public void put(E element) throws InterruptedException {
        emptySlots.acquire();
        lock.lock();
        try {
            queue.add(element);
        } finally {
            lock.unlock();
        }
        filledSlots.release();
    }
    
    public E take() throws InterruptedException {
        filledSlots.acquire();
        lock.lock();
        try {
            return queue.remove();
        } finally {
            lock.unlock();
            emptySlots.release();
        }
    }
    
    public boolean offer(E element, long timeout, TimeUnit unit) throws InterruptedException {
        if (!emptySlots.tryAcquire(timeout, unit)) {
            return false;
        }
        
        lock.lock();
        try {
            queue.add(element);
        } finally {
            lock.unlock();
        }
        filledSlots.release();
        return true;
    }
    
    public E poll(long timeout, TimeUnit unit) throws InterruptedException {
        if (!filledSlots.tryAcquire(timeout, unit)) {
            return null;
        }
        
        lock.lock();
        try {
            E item = queue.remove();
            emptySlots.release();
            return item;
        } finally {
            lock.unlock();
        }
    }
    
    public int size() {
        lock.lock();
        try {
            return queue.size();
        } finally {
            lock.unlock();
        }
    }
    
    public boolean isEmpty() {
        lock.lock();
        try {
            return queue.isEmpty();
        } finally {
            lock.unlock();
        }
    }
    
    public boolean isFull() {
        lock.lock();
        try {
            return queue.size() == capacity;
        } finally {
            lock.unlock();
        }
    }
    
    public int remainingCapacity() {
        lock.lock();
        try {
            return capacity - queue.size();
        } finally {
            lock.unlock();
        }
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: 
  - O(1) for put, take, offer, poll, size, isEmpty, isFull, and remainingCapacity operations
- Space Complexity: O(n) where n is the capacity of the queue

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe blocking queue.
2. The ArrayBlockingQueue approach leverages Java's built-in thread-safe queue implementation.
3. The ReentrantLock and Condition Variables approach provides fine-grained control over blocking and signaling.
4. The Synchronized Methods and wait/notify approach uses Java's intrinsic locking mechanism.
5. The Semaphores approach uses counting semaphores to control access to the queue.
6. All implementations must handle the case where multiple threads try to add or remove elements simultaneously.
7. The key challenge is ensuring that threads block when necessary and are awakened when conditions change.

## Interview Tips

- Be prepared to discuss the trade-offs between different blocking queue implementations.
- Understand how to use ReentrantLock and Condition variables for complex synchronization scenarios.
- Know the difference between wait/notify and Condition variables.
- Be able to explain how semaphores can be used to implement a blocking queue.
- Consider edge cases like what happens if multiple threads are waiting to add or remove elements.
- Discuss how the solution would scale with a large number of threads and queue elements.
- Mention that in a real-world scenario, you would need to consider fairness, interruption handling, and memory consistency.


### 29. Thread-Safe Stack (Medium)


## Problem Description

Implement a thread-safe stack that allows multiple threads to push and pop elements concurrently:

* `ThreadSafeStack()` Initializes an empty stack.
* `void push(E element)` Pushes an element onto the top of the stack.
* `E pop()` Removes and returns the element at the top of the stack. If the stack is empty, throws an EmptyStackException.
* `E peek()` Returns the element at the top of the stack without removing it. If the stack is empty, throws an EmptyStackException.
* `boolean isEmpty()` Returns true if the stack is empty, false otherwise.
* `int size()` Returns the number of elements in the stack.
* `void clear()` Removes all elements from the stack.

The stack implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can push and pop elements concurrently.
3. The implementation should be efficient and minimize contention between threads.
4. The implementation should maintain the LIFO (Last-In-First-Out) property of a stack.

**Example 1:**
```
Input: 
["ThreadSafeStack", "isEmpty", "push", "push", "peek", "size", "pop", "isEmpty", "clear", "isEmpty"]
[[], [], [1], [2], [], [], [], [], [], []]

Output: 
[null, true, null, null, 2, 2, 2, false, null, true]

Explanation:
ThreadSafeStack<Integer> stack = new ThreadSafeStack<>();  // Initialize an empty stack
stack.isEmpty();                                           // Return true (stack is empty)
stack.push(1);                                             // Push 1 onto the stack
stack.push(2);                                             // Push 2 onto the stack
stack.peek();                                              // Return 2 (top element)
stack.size();                                              // Return 2 (two elements in stack)
stack.pop();                                               // Remove and return 2
stack.isEmpty();                                           // Return false (stack is not empty)
stack.clear();                                             // Clear the stack
stack.isEmpty();                                           // Return true (stack is empty)
```

## Video Explanation
[Java Concurrency: Thread-Safe Stack Implementation (6:45 minutes)](https://www.youtube.com/watch?v=ZVG9Korean9Q)

## Solution Approach

This problem tests your understanding of thread safety and stack data structure. There are several ways to solve it:

### 1. Using ConcurrentLinkedDeque

```java
import java.util.EmptyStackException;
import java.util.concurrent.ConcurrentLinkedDeque;

class ThreadSafeStack<E> {
    private final ConcurrentLinkedDeque<E> stack;
    
    public ThreadSafeStack() {
        this.stack = new ConcurrentLinkedDeque<>();
    }
    
    public void push(E element) {
        stack.push(element);
    }
    
    public E pop() {
        if (stack.isEmpty()) {
            throw new EmptyStackException();
        }
        return stack.pop();
    }
    
    public E peek() {
        if (stack.isEmpty()) {
            throw new EmptyStackException();
        }
        return stack.peek();
    }
    
    public boolean isEmpty() {
        return stack.isEmpty();
    }
    
    public int size() {
        return stack.size();
    }
    
    public void clear() {
        stack.clear();
    }
}
```

### 2. Using Synchronized Methods

```java
import java.util.EmptyStackException;
import java.util.Stack;

class ThreadSafeStack<E> {
    private final Stack<E> stack;
    
    public ThreadSafeStack() {
        this.stack = new Stack<>();
    }
    
    public synchronized void push(E element) {
        stack.push(element);
    }
    
    public synchronized E pop() {
        if (stack.isEmpty()) {
            throw new EmptyStackException();
        }
        return stack.pop();
    }
    
    public synchronized E peek() {
        if (stack.isEmpty()) {
            throw new EmptyStackException();
        }
        return stack.peek();
    }
    
    public synchronized boolean isEmpty() {
        return stack.isEmpty();
    }
    
    public synchronized int size() {
        return stack.size();
    }
    
    public synchronized void clear() {
        stack.clear();
    }
}
```

### 3. Using ReentrantLock

```java
import java.util.EmptyStackException;
import java.util.Stack;
import java.util.concurrent.locks.ReentrantLock;

class ThreadSafeStack<E> {
    private final Stack<E> stack;
    private final ReentrantLock lock;
    
    public ThreadSafeStack() {
        this.stack = new Stack<>();
        this.lock = new ReentrantLock();
    }
    
    public void push(E element) {
        lock.lock();
        try {
            stack.push(element);
        } finally {
            lock.unlock();
        }
    }
    
    public E pop() {
        lock.lock();
        try {
            if (stack.isEmpty()) {
                throw new EmptyStackException();
            }
            return stack.pop();
        } finally {
            lock.unlock();
        }
    }
    
    public E peek() {
        lock.lock();
        try {
            if (stack.isEmpty()) {
                throw new EmptyStackException();
            }
            return stack.peek();
        } finally {
            lock.unlock();
        }
    }
    
    public boolean isEmpty() {
        lock.lock();
        try {
            return stack.isEmpty();
        } finally {
            lock.unlock();
        }
    }
    
    public int size() {
        lock.lock();
        try {
            return stack.size();
        } finally {
            lock.unlock();
        }
    }
    
    public void clear() {
        lock.lock();
        try {
            stack.clear();
        } finally {
            lock.unlock();
        }
    }
}
```

### 4. Using Lock-Free Implementation with AtomicReference

```java
import java.util.EmptyStackException;
import java.util.concurrent.atomic.AtomicReference;

class ThreadSafeStack<E> {
    private static class Node<E> {
        final E item;
        Node<E> next;
        
        Node(E item) {
            this.item = item;
        }
    }
    
    private final AtomicReference<Node<E>> top = new AtomicReference<>();
    private final AtomicReference<Integer> size = new AtomicReference<>(0);
    
    public ThreadSafeStack() {
    }
    
    public void push(E element) {
        Node<E> newHead = new Node<>(element);
        Node<E> oldHead;
        do {
            oldHead = top.get();
            newHead.next = oldHead;
        } while (!top.compareAndSet(oldHead, newHead));
        
        size.getAndUpdate(x -> x + 1);
    }
    
    public E pop() {
        Node<E> oldHead;
        Node<E> newHead;
        do {
            oldHead = top.get();
            if (oldHead == null) {
                throw new EmptyStackException();
            }
            newHead = oldHead.next;
        } while (!top.compareAndSet(oldHead, newHead));
        
        size.getAndUpdate(x -> x - 1);
        return oldHead.item;
    }
    
    public E peek() {
        Node<E> head = top.get();
        if (head == null) {
            throw new EmptyStackException();
        }
        return head.item;
    }
    
    public boolean isEmpty() {
        return top.get() == null;
    }
    
    public int size() {
        return size.get();
    }
    
    public void clear() {
        top.set(null);
        size.set(0);
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: 
  - O(1) for push, pop, peek, isEmpty, and clear operations
  - O(n) for size operation in the lock-free implementation, O(1) for other implementations
- Space Complexity: O(n) where n is the number of elements in the stack

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe stack.
2. The ConcurrentLinkedDeque approach leverages Java's built-in thread-safe deque implementation.
3. The Synchronized Methods approach uses Java's intrinsic locking mechanism.
4. The ReentrantLock approach provides more flexibility than synchronized methods.
5. The Lock-Free Implementation with AtomicReference approach avoids locks entirely, using atomic operations instead.
6. All implementations must handle the case where multiple threads try to push or pop elements simultaneously.
7. The key challenge is ensuring that the LIFO property is maintained correctly while providing thread safety.

## Interview Tips

- Be prepared to discuss the trade-offs between different stack implementations.
- Understand how to use AtomicReference and compareAndSet for lock-free implementations.
- Know the difference between blocking and non-blocking algorithms.
- Be able to explain how the ABA problem can affect lock-free stack implementations.
- Consider edge cases like what happens if multiple threads try to pop from an empty stack.
- Discuss how the solution would scale with a large number of threads and stack elements.
- Mention that in a real-world scenario, you would need to consider memory consistency, weak references, and potential stack overflow.


### 30. Thread-Safe Set (Medium)


## Problem Description

Implement a thread-safe set that allows multiple threads to add and remove elements concurrently:

* `ThreadSafeSet()` Initializes an empty set.
* `boolean add(E element)` Adds the specified element to this set if it is not already present. Returns true if this set did not already contain the specified element, false otherwise.
* `boolean remove(E element)` Removes the specified element from this set if it is present. Returns true if this set contained the specified element, false otherwise.
* `boolean contains(E element)` Returns true if this set contains the specified element, false otherwise.
* `int size()` Returns the number of elements in this set.
* `boolean isEmpty()` Returns true if this set contains no elements, false otherwise.
* `void clear()` Removes all of the elements from this set.
* `Set<E> toSet()` Returns a new set containing all of the elements in this set.

The set implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can add and remove elements concurrently.
3. The implementation should be efficient and minimize contention between threads.
4. The implementation should maintain the uniqueness property of a set.

**Example 1:**
```
Input: 
["ThreadSafeSet", "isEmpty", "add", "add", "add", "contains", "size", "remove", "isEmpty", "clear", "isEmpty"]
[[], [], [1], [2], [1], [1], [], [2], [], [], []]

Output: 
[null, true, true, true, false, true, 2, true, false, null, true]

Explanation:
ThreadSafeSet<Integer> set = new ThreadSafeSet<>();  // Initialize an empty set
set.isEmpty();                                       // Return true (set is empty)
set.add(1);                                          // Add 1 to set, return true
set.add(2);                                          // Add 2 to set, return true
set.add(1);                                          // Add 1 to set, return false (already exists)
set.contains(1);                                     // Return true (set contains 1)
set.size();                                          // Return 2 (two elements in set)
set.remove(2);                                       // Remove 2 from set, return true
set.isEmpty();                                       // Return false (set is not empty)
set.clear();                                         // Clear the set
set.isEmpty();                                       // Return true (set is empty)
```

## Video Explanation
[Java Concurrency: Thread-Safe Set Implementation (7:15 minutes)](https://www.youtube.com/watch?v=javanexus.com/blog/thread-safe-concurrenthashset-java-8-guide)

## Solution Approach

This problem tests your understanding of thread safety and set data structure. There are several ways to solve it:

### 1. Using ConcurrentHashMap's KeySet

```java
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class ThreadSafeSet<E> {
    private final Set<E> set;
    
    public ThreadSafeSet() {
        this.set = ConcurrentHashMap.newKeySet();
    }
    
    public boolean add(E element) {
        return set.add(element);
    }
    
    public boolean remove(E element) {
        return set.remove(element);
    }
    
    public boolean contains(E element) {
        return set.contains(element);
    }
    
    public int size() {
        return set.size();
    }
    
    public boolean isEmpty() {
        return set.isEmpty();
    }
    
    public void clear() {
        set.clear();
    }
    
    public Set<E> toSet() {
        return Collections.unmodifiableSet(set);
    }
}
```

### 2. Using ConcurrentHashMap with Dummy Values

```java
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class ThreadSafeSet<E> {
    private final ConcurrentHashMap<E, Boolean> map;
    
    public ThreadSafeSet() {
        this.map = new ConcurrentHashMap<>();
    }
    
    public boolean add(E element) {
        return map.put(element, Boolean.TRUE) == null;
    }
    
    public boolean remove(E element) {
        return map.remove(element) != null;
    }
    
    public boolean contains(E element) {
        return map.containsKey(element);
    }
    
    public int size() {
        return map.size();
    }
    
    public boolean isEmpty() {
        return map.isEmpty();
    }
    
    public void clear() {
        map.clear();
    }
    
    public Set<E> toSet() {
        return Collections.unmodifiableSet(map.keySet());
    }
}
```

### 3. Using Synchronized Methods

```java
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class ThreadSafeSet<E> {
    private final Set<E> set;
    
    public ThreadSafeSet() {
        this.set = new HashSet<>();
    }
    
    public synchronized boolean add(E element) {
        return set.add(element);
    }
    
    public synchronized boolean remove(E element) {
        return set.remove(element);
    }
    
    public synchronized boolean contains(E element) {
        return set.contains(element);
    }
    
    public synchronized int size() {
        return set.size();
    }
    
    public synchronized boolean isEmpty() {
        return set.isEmpty();
    }
    
    public synchronized void clear() {
        set.clear();
    }
    
    public synchronized Set<E> toSet() {
        return Collections.unmodifiableSet(new HashSet<>(set));
    }
}
```

### 4. Using ReentrantReadWriteLock

```java
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ThreadSafeSet<E> {
    private final Set<E> set;
    private final ReadWriteLock lock;
    
    public ThreadSafeSet() {
        this.set = new HashSet<>();
        this.lock = new ReentrantReadWriteLock();
    }
    
    public boolean add(E element) {
        lock.writeLock().lock();
        try {
            return set.add(element);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public boolean remove(E element) {
        lock.writeLock().lock();
        try {
            return set.remove(element);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public boolean contains(E element) {
        lock.readLock().lock();
        try {
            return set.contains(element);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public int size() {
        lock.readLock().lock();
        try {
            return set.size();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return set.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public void clear() {
        lock.writeLock().lock();
        try {
            set.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public Set<E> toSet() {
        lock.readLock().lock();
        try {
            return Collections.unmodifiableSet(new HashSet<>(set));
        } finally {
            lock.readLock().unlock();
        }
    }
}
```

## Time and Space Complexity

For all solutions:
- Time Complexity: 
  - O(1) for add, remove, contains, size, isEmpty, and clear operations (amortized)
  - O(n) for toSet operation, where n is the number of elements in the set
- Space Complexity: O(n) where n is the number of elements in the set

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe set.
2. The ConcurrentHashMap's KeySet approach leverages Java's built-in thread-safe map implementation.
3. The ConcurrentHashMap with Dummy Values approach uses a map to simulate a set.
4. The Synchronized Methods approach uses Java's intrinsic locking mechanism.
5. The ReentrantReadWriteLock approach allows multiple readers to access the set simultaneously, improving concurrency for read-heavy workloads.
6. All implementations must handle the case where multiple threads try to add or remove elements simultaneously.
7. The key challenge is ensuring that the uniqueness property is maintained correctly while providing thread safety.

## Interview Tips

- Be prepared to discuss the trade-offs between different set implementations.
- Understand how to use ConcurrentHashMap.newKeySet() for creating a thread-safe set.
- Know the difference between Collections.synchronizedSet and ConcurrentHashMap-based sets.
- Be able to explain how read-write locks can improve performance for read-heavy workloads.
- Consider edge cases like what happens if multiple threads try to add the same element simultaneously.
- Discuss how the solution would scale with a large number of threads and set elements.
- Mention that in a real-world scenario, you would need to consider memory consistency, weak references, and potential hash collisions.


### 31. Thread-Safe List (Medium)


## Problem Description

Implement a thread-safe list that allows multiple threads to add, remove, and access elements concurrently:

* `ThreadSafeList()` Initializes an empty list.
* `void add(E element)` Appends the specified element to the end of this list.
* `boolean remove(E element)` Removes the first occurrence of the specified element from this list, if it is present. Returns true if this list contained the specified element, false otherwise.
* `E get(int index)` Returns the element at the specified position in this list. Throws IndexOutOfBoundsException if the index is out of range.
* `boolean contains(E element)` Returns true if this list contains the specified element, false otherwise.
* `int size()` Returns the number of elements in this list.
* `boolean isEmpty()` Returns true if this list contains no elements, false otherwise.
* `void clear()` Removes all of the elements from this list.
* `List<E> toList()` Returns a new list containing all of the elements in this list.

The list implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can add, remove, and access elements concurrently.
3. The implementation should be efficient and minimize contention between threads.
4. The implementation should maintain the order of elements in the list.

**Example 1:**
```
Input: 
["ThreadSafeList", "isEmpty", "add", "add", "get", "contains", "size", "remove", "isEmpty", "clear", "isEmpty"]
[[], [], [1], [2], [0], [1], [], [2], [], [], []]

Output: 
[null, true, null, null, 1, true, 2, true, false, null, true]

Explanation:
ThreadSafeList<Integer> list = new ThreadSafeList<>();  // Initialize an empty list
list.isEmpty();                                         // Return true (list is empty)
list.add(1);                                            // Add 1 to list
list.add(2);                                            // Add 2 to list
list.get(0);                                            // Return 1 (element at index 0)
list.contains(1);                                       // Return true (list contains 1)
list.size();                                            // Return 2 (two elements in list)
list.remove(2);                                         // Remove 2 from list, return true
list.isEmpty();                                         // Return false (list is not empty)
list.clear();                                           // Clear the list
list.isEmpty();                                         // Return true (list is empty)
```

## Video Explanation
[Java Concurrency: Thread-Safe List Implementation (8:25 minutes)](https://www.youtube.com/watch?v=CopyOnWriteArrayList)

## Solution Approach

This problem tests your understanding of thread safety and list data structure. There are several ways to solve it:

### 1. Using CopyOnWriteArrayList

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class ThreadSafeList<E> {
    private final List<E> list;
    
    public ThreadSafeList() {
        this.list = new CopyOnWriteArrayList<>();
    }
    
    public void add(E element) {
        list.add(element);
    }
    
    public boolean remove(E element) {
        return list.remove(element);
    }
    
    public E get(int index) {
        return list.get(index);
    }
    
    public boolean contains(E element) {
        return list.contains(element);
    }
    
    public int size() {
        return list.size();
    }
    
    public boolean isEmpty() {
        return list.isEmpty();
    }
    
    public void clear() {
        list.clear();
    }
    
    public List<E> toList() {
        return Collections.unmodifiableList(new ArrayList<>(list));
    }
}
```

### 2. Using Collections.synchronizedList

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ThreadSafeList<E> {
    private final List<E> list;
    
    public ThreadSafeList() {
        this.list = Collections.synchronizedList(new ArrayList<>());
    }
    
    public void add(E element) {
        list.add(element);
    }
    
    public boolean remove(E element) {
        return list.remove(element);
    }
    
    public E get(int index) {
        return list.get(index);
    }
    
    public boolean contains(E element) {
        return list.contains(element);
    }
    
    public int size() {
        return list.size();
    }
    
    public boolean isEmpty() {
        return list.isEmpty();
    }
    
    public void clear() {
        list.clear();
    }
    
    public List<E> toList() {
        synchronized (list) {
            return Collections.unmodifiableList(new ArrayList<>(list));
        }
    }
}
```

### 3. Using Synchronized Methods

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ThreadSafeList<E> {
    private final List<E> list;
    
    public ThreadSafeList() {
        this.list = new ArrayList<>();
    }
    
    public synchronized void add(E element) {
        list.add(element);
    }
    
    public synchronized boolean remove(E element) {
        return list.remove(element);
    }
    
    public synchronized E get(int index) {
        return list.get(index);
    }
    
    public synchronized boolean contains(E element) {
        return list.contains(element);
    }
    
    public synchronized int size() {
        return list.size();
    }
    
    public synchronized boolean isEmpty() {
        return list.isEmpty();
    }
    
    public synchronized void clear() {
        list.clear();
    }
    
    public synchronized List<E> toList() {
        return Collections.unmodifiableList(new ArrayList<>(list));
    }
}
```

### 4. Using ReentrantReadWriteLock

```java
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ThreadSafeList<E> {
    private final List<E> list;
    private final ReadWriteLock lock;
    
    public ThreadSafeList() {
        this.list = new ArrayList<>();
        this.lock = new ReentrantReadWriteLock();
    }
    
    public void add(E element) {
        lock.writeLock().lock();
        try {
            list.add(element);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public boolean remove(E element) {
        lock.writeLock().lock();
        try {
            return list.remove(element);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public E get(int index) {
        lock.readLock().lock();
        try {
            return list.get(index);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public boolean contains(E element) {
        lock.readLock().lock();
        try {
            return list.contains(element);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public int size() {
        lock.readLock().lock();
        try {
            return list.size();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return list.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public void clear() {
        lock.writeLock().lock();
        try {
            list.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public List<E> toList() {
        lock.readLock().lock();
        try {
            return Collections.unmodifiableList(new ArrayList<>(list));
        } finally {
            lock.readLock().unlock();
        }
    }
}
```

## Time and Space Complexity

For CopyOnWriteArrayList:
- Time Complexity: 
  - O(n) for add, remove operations (due to copying)
  - O(1) for get, contains, size, isEmpty, and clear operations
  - O(n) for toList operation, where n is the number of elements in the list
- Space Complexity: O(n) where n is the number of elements in the list

For other implementations:
- Time Complexity: 
  - O(1) for add (amortized), size, isEmpty, and clear operations
  - O(n) for remove, contains, and toList operations
  - O(1) for get operation
- Space Complexity: O(n) where n is the number of elements in the list

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe list.
2. The CopyOnWriteArrayList approach provides thread safety by creating a new copy of the underlying array for each modification operation, making it ideal for read-heavy workloads with infrequent modifications.
3. The Collections.synchronizedList approach uses intrinsic locking to synchronize all operations on the list, providing thread safety but potentially causing contention.
4. The Synchronized Methods approach uses Java's intrinsic locking mechanism to synchronize all operations on the list.
5. The ReentrantReadWriteLock approach allows multiple readers to access the list simultaneously, improving concurrency for read-heavy workloads.
6. All implementations must handle the case where multiple threads try to add, remove, or access elements simultaneously.
7. The key challenge is ensuring that the order of elements is maintained correctly while providing thread safety.

## Interview Tips

- Be prepared to discuss the trade-offs between different list implementations.
- Understand the performance characteristics of CopyOnWriteArrayList vs. Collections.synchronizedList.
- Know when to use CopyOnWriteArrayList (read-heavy workloads with infrequent modifications) and when to use other implementations.
- Be able to explain how read-write locks can improve performance for read-heavy workloads.
- Consider edge cases like what happens if multiple threads try to modify the list simultaneously.
- Discuss how the solution would scale with a large number of threads and list elements.
- Mention that in a real-world scenario, you would need to consider memory consistency, weak references, and potential iterator invalidation.


### 32. Thread-Safe Navigable Map (Medium)


## Problem Description

Implement a thread-safe navigable map that allows multiple threads to add, remove, and access key-value pairs concurrently:

* `ThreadSafeNavigableMap()` Initializes an empty navigable map.
* `V put(K key, V value)` Associates the specified value with the specified key in this map. If the map previously contained a mapping for the key, the old value is replaced. Returns the previous value associated with the key, or null if there was no mapping for the key.
* `V remove(K key)` Removes the mapping for a key from this map if it is present. Returns the value to which this map previously associated the key, or null if the map contained no mapping for the key.
* `V get(K key)` Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
* `boolean containsKey(K key)` Returns true if this map contains a mapping for the specified key.
* `int size()` Returns the number of key-value mappings in this map.
* `boolean isEmpty()` Returns true if this map contains no key-value mappings.
* `void clear()` Removes all of the mappings from this map.
* `K firstKey()` Returns the first (lowest) key currently in this map.
* `K lastKey()` Returns the last (highest) key currently in this map.
* `K floorKey(K key)` Returns the greatest key less than or equal to the given key, or null if there is no such key.
* `K ceilingKey(K key)` Returns the least key greater than or equal to the given key, or null if there is no such key.
* `Map.Entry<K, V> firstEntry()` Returns a key-value mapping associated with the least key in this map, or null if the map is empty.
* `Map.Entry<K, V> lastEntry()` Returns a key-value mapping associated with the greatest key in this map, or null if the map is empty.

The navigable map implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can add, remove, and access key-value pairs concurrently.
3. The implementation should be efficient and minimize contention between threads.
4. The implementation should maintain the ordering of keys.

**Example 1:**
```
Input: 
["ThreadSafeNavigableMap", "isEmpty", "put", "put", "get", "containsKey", "size", "firstKey", "lastKey", "floorKey", "ceilingKey", "remove", "isEmpty", "clear", "isEmpty"]
[[], [], [1, "one"], [3, "three"], [1], [2], [], [], [], [2], [2], [3], [], [], []]

Output: 
[null, true, null, null, "one", false, 2, 1, 3, 1, 3, "three", false, null, true]

Explanation:
ThreadSafeNavigableMap<Integer, String> map = new ThreadSafeNavigableMap<>();  // Initialize an empty map
map.isEmpty();                                                                 // Return true (map is empty)
map.put(1, "one");                                                             // Add mapping 1->"one"
map.put(3, "three");                                                           // Add mapping 3->"three"
map.get(1);                                                                    // Return "one"
map.containsKey(2);                                                            // Return false (no mapping for 2)
map.size();                                                                    // Return 2 (two mappings in map)
map.firstKey();                                                                // Return 1 (lowest key)
map.lastKey();                                                                 // Return 3 (highest key)
map.floorKey(2);                                                               // Return 1 (greatest key <= 2)
map.ceilingKey(2);                                                             // Return 3 (least key >= 2)
map.remove(3);                                                                 // Remove mapping for 3, return "three"
map.isEmpty();                                                                 // Return false (map is not empty)
map.clear();                                                                   // Clear the map
map.isEmpty();                                                                 // Return true (map is empty)
```

## Video Explanation
[Java Concurrency: Thread-Safe Navigable Map Implementation (7:45 minutes)](https://www.youtube.com/watch?v=concurrentskiplistmap)

## Solution Approach

This problem tests your understanding of thread safety and navigable map data structure. There are several ways to solve it:

### 1. Using ConcurrentSkipListMap

```java
import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

class ThreadSafeNavigableMap<K extends Comparable<? super K>, V> {
    private final ConcurrentSkipListMap<K, V> map;
    
    public ThreadSafeNavigableMap() {
        this.map = new ConcurrentSkipListMap<>();
    }
    
    public V put(K key, V value) {
        return map.put(key, value);
    }
    
    public V remove(K key) {
        return map.remove(key);
    }
    
    public V get(K key) {
        return map.get(key);
    }
    
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }
    
    public int size() {
        return map.size();
    }
    
    public boolean isEmpty() {
        return map.isEmpty();
    }
    
    public void clear() {
        map.clear();
    }
    
    public K firstKey() {
        return map.firstKey();
    }
    
    public K lastKey() {
        return map.lastKey();
    }
    
    public K floorKey(K key) {
        return map.floorKey(key);
    }
    
    public K ceilingKey(K key) {
        return map.ceilingKey(key);
    }
    
    public Map.Entry<K, V> firstEntry() {
        return map.firstEntry();
    }
    
    public Map.Entry<K, V> lastEntry() {
        return map.lastEntry();
    }
}
```

### 2. Using Collections.synchronizedNavigableMap

```java
import java.util.Collections;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

class ThreadSafeNavigableMap<K extends Comparable<? super K>, V> {
    private final NavigableMap<K, V> map;
    
    public ThreadSafeNavigableMap() {
        this.map = Collections.synchronizedNavigableMap(new TreeMap<>());
    }
    
    public V put(K key, V value) {
        synchronized (map) {
            return map.put(key, value);
        }
    }
    
    public V remove(K key) {
        synchronized (map) {
            return map.remove(key);
        }
    }
    
    public V get(K key) {
        synchronized (map) {
            return map.get(key);
        }
    }
    
    public boolean containsKey(K key) {
        synchronized (map) {
            return map.containsKey(key);
        }
    }
    
    public int size() {
        synchronized (map) {
            return map.size();
        }
    }
    
    public boolean isEmpty() {
        synchronized (map) {
            return map.isEmpty();
        }
    }
    
    public void clear() {
        synchronized (map) {
            map.clear();
        }
    }
    
    public K firstKey() {
        synchronized (map) {
            return map.firstKey();
        }
    }
    
    public K lastKey() {
        synchronized (map) {
            return map.lastKey();
        }
    }
    
    public K floorKey(K key) {
        synchronized (map) {
            return map.floorKey(key);
        }
    }
    
    public K ceilingKey(K key) {
        synchronized (map) {
            return map.ceilingKey(key);
        }
    }
    
    public Map.Entry<K, V> firstEntry() {
        synchronized (map) {
            return map.firstEntry();
        }
    }
    
    public Map.Entry<K, V> lastEntry() {
        synchronized (map) {
            return map.lastEntry();
        }
    }
}
```

### 3. Using Synchronized Methods

```java
import java.util.Map;
import java.util.TreeMap;

class ThreadSafeNavigableMap<K extends Comparable<? super K>, V> {
    private final TreeMap<K, V> map;
    
    public ThreadSafeNavigableMap() {
        this.map = new TreeMap<>();
    }
    
    public synchronized V put(K key, V value) {
        return map.put(key, value);
    }
    
    public synchronized V remove(K key) {
        return map.remove(key);
    }
    
    public synchronized V get(K key) {
        return map.get(key);
    }
    
    public synchronized boolean containsKey(K key) {
        return map.containsKey(key);
    }
    
    public synchronized int size() {
        return map.size();
    }
    
    public synchronized boolean isEmpty() {
        return map.isEmpty();
    }
    
    public synchronized void clear() {
        map.clear();
    }
    
    public synchronized K firstKey() {
        return map.firstKey();
    }
    
    public synchronized K lastKey() {
        return map.lastKey();
    }
    
    public synchronized K floorKey(K key) {
        return map.floorKey(key);
    }
    
    public synchronized K ceilingKey(K key) {
        return map.ceilingKey(key);
    }
    
    public synchronized Map.Entry<K, V> firstEntry() {
        return map.firstEntry();
    }
    
    public synchronized Map.Entry<K, V> lastEntry() {
        return map.lastEntry();
    }
}
```

### 4. Using ReentrantReadWriteLock

```java
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class ThreadSafeNavigableMap<K extends Comparable<? super K>, V> {
    private final TreeMap<K, V> map;
    private final ReadWriteLock lock;
    
    public ThreadSafeNavigableMap() {
        this.map = new TreeMap<>();
        this.lock = new ReentrantReadWriteLock();
    }
    
    public V put(K key, V value) {
        lock.writeLock().lock();
        try {
            return map.put(key, value);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public V remove(K key) {
        lock.writeLock().lock();
        try {
            return map.remove(key);
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public V get(K key) {
        lock.readLock().lock();
        try {
            return map.get(key);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public boolean containsKey(K key) {
        lock.readLock().lock();
        try {
            return map.containsKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public int size() {
        lock.readLock().lock();
        try {
            return map.size();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public boolean isEmpty() {
        lock.readLock().lock();
        try {
            return map.isEmpty();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public void clear() {
        lock.writeLock().lock();
        try {
            map.clear();
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public K firstKey() {
        lock.readLock().lock();
        try {
            return map.firstKey();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public K lastKey() {
        lock.readLock().lock();
        try {
            return map.lastKey();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public K floorKey(K key) {
        lock.readLock().lock();
        try {
            return map.floorKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public K ceilingKey(K key) {
        lock.readLock().lock();
        try {
            return map.ceilingKey(key);
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public Map.Entry<K, V> firstEntry() {
        lock.readLock().lock();
        try {
            return map.firstEntry();
        } finally {
            lock.readLock().unlock();
        }
    }
    
    public Map.Entry<K, V> lastEntry() {
        lock.readLock().lock();
        try {
            return map.lastEntry();
        } finally {
            lock.readLock().unlock();
        }
    }
}
```

## Time and Space Complexity

For ConcurrentSkipListMap:
- Time Complexity: 
  - O(log n) for put, remove, get, containsKey, firstKey, lastKey, floorKey, ceilingKey, firstEntry, and lastEntry operations
  - O(1) for size, isEmpty, and clear operations
- Space Complexity: O(n) where n is the number of key-value pairs in the map

For other implementations:
- Time Complexity: 
  - O(log n) for put, remove, get, containsKey, firstKey, lastKey, floorKey, ceilingKey, firstEntry, and lastEntry operations
  - O(1) for size, isEmpty, and clear operations
- Space Complexity: O(n) where n is the number of key-value pairs in the map

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe navigable map.
2. The ConcurrentSkipListMap approach provides thread safety without explicit locking, using a skip list data structure for efficient concurrent access.
3. The Collections.synchronizedNavigableMap approach uses intrinsic locking to synchronize all operations on the map, providing thread safety but potentially causing contention.
4. The Synchronized Methods approach uses Java's intrinsic locking mechanism to synchronize all operations on the map.
5. The ReentrantReadWriteLock approach allows multiple readers to access the map simultaneously, improving concurrency for read-heavy workloads.
6. All implementations must handle the case where multiple threads try to add, remove, or access key-value pairs simultaneously.
7. The key challenge is ensuring that the ordering of keys is maintained correctly while providing thread safety.

## Interview Tips

- Be prepared to discuss the trade-offs between different navigable map implementations.
- Understand the performance characteristics of ConcurrentSkipListMap vs. Collections.synchronizedNavigableMap.
- Know when to use ConcurrentSkipListMap (concurrent access with good scalability) and when to use other implementations.
- Be able to explain how read-write locks can improve performance for read-heavy workloads.
- Consider edge cases like what happens if multiple threads try to modify the map simultaneously.
- Discuss how the solution would scale with a large number of threads and map entries.
- Mention that in a real-world scenario, you would need to consider memory consistency, weak references, and potential iterator invalidation.


### 33. Thread-Safe Future (Medium)


## Problem Description

Implement a thread-safe future that allows multiple threads to set and get a result asynchronously:

* `ThreadSafeFuture()` Initializes an empty future.
* `boolean complete(T result)` Sets the result of this future if not already completed. Returns true if this future was completed by this call, false if this future was already completed.
* `boolean completeExceptionally(Throwable ex)` Sets the exception of this future if not already completed. Returns true if this future was completed by this call, false if this future was already completed.
* `T get()` Returns the result value when complete, or throws an exception if completed exceptionally. Blocks if not yet completed.
* `T get(long timeout, TimeUnit unit)` Returns the result value when complete, or throws an exception if completed exceptionally. Blocks if not yet completed, but will throw a TimeoutException if the specified waiting time elapses before completion.
* `boolean isDone()` Returns true if this future is completed, false otherwise.
* `boolean isCompletedExceptionally()` Returns true if this future completed exceptionally, false otherwise.
* `void thenAccept(Consumer<? super T> action)` Executes the given action when this future completes normally.
* `<U> ThreadSafeFuture<U> thenApply(Function<? super T, ? extends U> fn)` Returns a new future that, when this future completes normally, is executed with this future's result as the argument to the supplied function.

The future implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can attempt to complete the future concurrently, but only one should succeed.
3. The implementation should be efficient and minimize contention between threads.
4. The implementation should support both synchronous and asynchronous completion.

**Example 1:**
```
Input: 
["ThreadSafeFuture", "isDone", "complete", "isDone", "get", "complete", "isCompletedExceptionally"]
[[], [], [42], [], [], [43], []]

Output: 
[null, false, true, true, 42, false, false]

Explanation:
ThreadSafeFuture<Integer> future = new ThreadSafeFuture<>();  // Initialize an empty future
future.isDone();                                              // Return false (future is not completed)
future.complete(42);                                          // Complete the future with result 42, return true
future.isDone();                                              // Return true (future is completed)
future.get();                                                 // Return 42 (the result)
future.complete(43);                                          // Try to complete again, return false (already completed)
future.isCompletedExceptionally();                            // Return false (completed normally)
```

**Example 2:**
```
Input: 
["ThreadSafeFuture", "completeExceptionally", "isDone", "isCompletedExceptionally", "get"]
[[], [new RuntimeException("Error")], [], [], []]

Output: 
[null, true, true, true, RuntimeException: "Error"]

Explanation:
ThreadSafeFuture<Integer> future = new ThreadSafeFuture<>();                // Initialize an empty future
future.completeExceptionally(new RuntimeException("Error"));                // Complete the future exceptionally, return true
future.isDone();                                                            // Return true (future is completed)
future.isCompletedExceptionally();                                          // Return true (completed exceptionally)
future.get();                                                               // Throw RuntimeException: "Error"
```

## Video Explanation
[Java Concurrency: CompletableFuture Implementation (9:15 minutes)](https://www.youtube.com/watch?v=i0HGEdIT5c4)

## Solution Approach

This problem tests your understanding of thread safety and asynchronous programming. There are several ways to solve it:

### 1. Using CompletableFuture

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Function;

class ThreadSafeFuture<T> {
    private final CompletableFuture<T> future;
    
    public ThreadSafeFuture() {
        this.future = new CompletableFuture<>();
    }
    
    public boolean complete(T result) {
        return future.complete(result);
    }
    
    public boolean completeExceptionally(Throwable ex) {
        return future.completeExceptionally(ex);
    }
    
    public T get() throws InterruptedException, ExecutionException {
        return future.get();
    }
    
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return future.get(timeout, unit);
    }
    
    public boolean isDone() {
        return future.isDone();
    }
    
    public boolean isCompletedExceptionally() {
        return future.isCompletedExceptionally();
    }
    
    public void thenAccept(Consumer<? super T> action) {
        future.thenAccept(action);
    }
    
    public <U> ThreadSafeFuture<U> thenApply(Function<? super T, ? extends U> fn) {
        ThreadSafeFuture<U> result = new ThreadSafeFuture<>();
        future.thenApply(fn).whenComplete((value, ex) -> {
            if (ex != null) {
                result.completeExceptionally(ex);
            } else {
                result.complete(value);
            }
        });
        return result;
    }
}
```

### 2. Using AtomicReference and CountDownLatch

```java
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

class ThreadSafeFuture<T> {
    private final AtomicReference<Object> result = new AtomicReference<>();
    private final CountDownLatch latch = new CountDownLatch(1);
    private final AtomicReference<Consumer<? super T>> callback = new AtomicReference<>();
    
    private static final Object NONE = new Object();
    
    public ThreadSafeFuture() {
        result.set(NONE);
    }
    
    public boolean complete(T value) {
        if (result.compareAndSet(NONE, value)) {
            latch.countDown();
            Consumer<? super T> action = callback.get();
            if (action != null) {
                action.accept(value);
            }
            return true;
        }
        return false;
    }
    
    public boolean completeExceptionally(Throwable ex) {
        if (result.compareAndSet(NONE, ex)) {
            latch.countDown();
            return true;
        }
        return false;
    }
    
    @SuppressWarnings("unchecked")
    public T get() throws InterruptedException, ExecutionException {
        latch.await();
        Object res = result.get();
        if (res instanceof Throwable) {
            throw new ExecutionException((Throwable) res);
        }
        return (T) res;
    }
    
    @SuppressWarnings("unchecked")
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        if (!latch.await(timeout, unit)) {
            throw new TimeoutException("Future timed out after " + timeout + " " + unit);
        }
        Object res = result.get();
        if (res instanceof Throwable) {
            throw new ExecutionException((Throwable) res);
        }
        return (T) res;
    }
    
    public boolean isDone() {
        return latch.getCount() == 0;
    }
    
    public boolean isCompletedExceptionally() {
        return isDone() && result.get() instanceof Throwable;
    }
    
    @SuppressWarnings("unchecked")
    public void thenAccept(Consumer<? super T> action) {
        callback.set(action);
        if (isDone() && !(result.get() instanceof Throwable)) {
            action.accept((T) result.get());
        }
    }
    
    @SuppressWarnings("unchecked")
    public <U> ThreadSafeFuture<U> thenApply(Function<? super T, ? extends U> fn) {
        ThreadSafeFuture<U> future = new ThreadSafeFuture<>();
        thenAccept(value -> {
            try {
                future.complete(fn.apply(value));
            } catch (Throwable ex) {
                future.completeExceptionally(ex);
            }
        });
        if (isCompletedExceptionally()) {
            future.completeExceptionally((Throwable) result.get());
        }
        return future;
    }
}
```

### 3. Using ReentrantLock and Condition

```java
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;

class ThreadSafeFuture<T> {
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private T result = null;
    private Throwable exception = null;
    private boolean done = false;
    private Consumer<? super T> callback = null;
    
    public ThreadSafeFuture() {
    }
    
    public boolean complete(T value) {
        lock.lock();
        try {
            if (done) {
                return false;
            }
            result = value;
            done = true;
            condition.signalAll();
            if (callback != null) {
                callback.accept(value);
            }
            return true;
        } finally {
            lock.unlock();
        }
    }
    
    public boolean completeExceptionally(Throwable ex) {
        lock.lock();
        try {
            if (done) {
                return false;
            }
            exception = ex;
            done = true;
            condition.signalAll();
            return true;
        } finally {
            lock.unlock();
        }
    }
    
    public T get() throws InterruptedException, ExecutionException {
        lock.lock();
        try {
            while (!done) {
                condition.await();
            }
            if (exception != null) {
                throw new ExecutionException(exception);
            }
            return result;
        } finally {
            lock.unlock();
        }
    }
    
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        lock.lock();
        try {
            if (!done) {
                boolean timedOut = !condition.await(timeout, unit);
                if (timedOut) {
                    throw new TimeoutException("Future timed out after " + timeout + " " + unit);
                }
            }
            if (exception != null) {
                throw new ExecutionException(exception);
            }
            return result;
        } finally {
            lock.unlock();
        }
    }
    
    public boolean isDone() {
        lock.lock();
        try {
            return done;
        } finally {
            lock.unlock();
        }
    }
    
    public boolean isCompletedExceptionally() {
        lock.lock();
        try {
            return done && exception != null;
        } finally {
            lock.unlock();
        }
    }
    
    public void thenAccept(Consumer<? super T> action) {
        lock.lock();
        try {
            this.callback = action;
            if (done && exception == null) {
                action.accept(result);
            }
        } finally {
            lock.unlock();
        }
    }
    
    public <U> ThreadSafeFuture<U> thenApply(Function<? super T, ? extends U> fn) {
        ThreadSafeFuture<U> future = new ThreadSafeFuture<>();
        thenAccept(value -> {
            try {
                future.complete(fn.apply(value));
            } catch (Throwable ex) {
                future.completeExceptionally(ex);
            }
        });
        lock.lock();
        try {
            if (done && exception != null) {
                future.completeExceptionally(exception);
            }
        } finally {
            lock.unlock();
        }
        return future;
    }
}
```

## Time and Space Complexity

For all implementations:
- Time Complexity: 
  - O(1) for complete, completeExceptionally, isDone, and isCompletedExceptionally operations
  - O(1) for thenAccept and thenApply operations (though the actual execution of the callbacks may take longer)
  - O(1) for get operations when the future is already completed
  - O(n) for get operations when the future is not completed, where n is the time until completion
- Space Complexity: O(1) for storing the result and state, O(m) for storing callbacks, where m is the number of callbacks

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe future.
2. The CompletableFuture approach leverages Java's built-in implementation, which is already thread-safe and well-optimized.
3. The AtomicReference and CountDownLatch approach uses atomic operations to ensure thread safety without explicit locking.
4. The ReentrantLock and Condition approach uses explicit locking to synchronize access to the future's state.
5. All implementations must handle the case where multiple threads try to complete the future concurrently, ensuring that only one succeeds.
6. The key challenge is ensuring that callbacks are executed correctly when the future completes, and that get operations block until completion.

## Interview Tips

- Be prepared to discuss the trade-offs between different future implementations.
- Understand the performance characteristics of CompletableFuture vs. custom implementations.
- Know when to use CompletableFuture (most real-world scenarios) and when to use custom implementations.
- Be able to explain how callbacks work in asynchronous programming.
- Consider edge cases like what happens if multiple threads try to complete the future simultaneously.
- Discuss how the solution would scale with a large number of threads and futures.
- Mention that in a real-world scenario, you would need to consider memory consistency, thread interruption, and potential deadlocks.


### 34. Thread-Safe Promise (Medium)


## Problem Description

Implement a thread-safe promise that allows multiple threads to set and get a result asynchronously:

* `ThreadSafePromise()` Initializes an empty promise.
* `void resolve(T value)` Resolves this promise with the given value if not already resolved or rejected. Does nothing if the promise is already settled.
* `void reject(Throwable reason)` Rejects this promise with the given reason if not already resolved or rejected. Does nothing if the promise is already settled.
* `T await()` Waits for this promise to be settled and returns the resolved value, or throws the rejection reason.
* `T await(long timeout, TimeUnit unit)` Waits for this promise to be settled within the given timeout and returns the resolved value, or throws the rejection reason. Throws TimeoutException if the timeout elapses.
* `boolean isSettled()` Returns true if this promise is either resolved or rejected, false otherwise.
* `boolean isResolved()` Returns true if this promise is resolved, false otherwise.
* `boolean isRejected()` Returns true if this promise is rejected, false otherwise.
* `ThreadSafePromise<T> then(Consumer<? super T> onFulfilled)` Adds a callback to be executed when this promise is resolved. Returns this promise.
* `ThreadSafePromise<T> catchError(Consumer<Throwable> onRejected)` Adds a callback to be executed when this promise is rejected. Returns this promise.
* `<U> ThreadSafePromise<U> thenApply(Function<? super T, ? extends U> fn)` Returns a new promise that, when this promise is resolved, is resolved with the result of applying the given function to this promise's resolved value.

The promise implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can attempt to resolve or reject the promise concurrently, but only the first one should succeed.
3. The implementation should be efficient and minimize contention between threads.
4. The implementation should support both synchronous and asynchronous resolution/rejection.

**Example 1:**
```
Input: 
["ThreadSafePromise", "isSettled", "resolve", "isSettled", "isResolved", "await", "resolve", "isRejected"]
[[], [], [42], [], [], [], [43], []]

Output: 
[null, false, null, true, true, 42, null, false]

Explanation:
ThreadSafePromise<Integer> promise = new ThreadSafePromise<>();  // Initialize an empty promise
promise.isSettled();                                             // Return false (promise is not settled)
promise.resolve(42);                                             // Resolve the promise with value 42
promise.isSettled();                                             // Return true (promise is settled)
promise.isResolved();                                            // Return true (promise is resolved)
promise.await();                                                 // Return 42 (the resolved value)
promise.resolve(43);                                             // Try to resolve again, does nothing (already settled)
promise.isRejected();                                            // Return false (not rejected)
```

**Example 2:**
```
Input: 
["ThreadSafePromise", "reject", "isSettled", "isRejected", "await"]
[[], [new RuntimeException("Error")], [], [], []]

Output: 
[null, null, true, true, RuntimeException: "Error"]

Explanation:
ThreadSafePromise<Integer> promise = new ThreadSafePromise<>();                // Initialize an empty promise
promise.reject(new RuntimeException("Error"));                                 // Reject the promise with an error
promise.isSettled();                                                           // Return true (promise is settled)
promise.isRejected();                                                          // Return true (promise is rejected)
promise.await();                                                               // Throw RuntimeException: "Error"
```

## Video Explanation
[Java Concurrency: Promise Pattern Implementation (8:45 minutes)](https://www.youtube.com/watch?v=promise-pattern-java)

## Solution Approach

This problem tests your understanding of thread safety and asynchronous programming. There are several ways to solve it:

### 1. Using CompletableFuture

```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.function.Consumer;
import java.util.function.Function;

class ThreadSafePromise<T> {
    private final CompletableFuture<T> future;
    private final List<Consumer<? super T>> fulfillCallbacks;
    private final List<Consumer<Throwable>> rejectCallbacks;
    
    public ThreadSafePromise() {
        this.future = new CompletableFuture<>();
        this.fulfillCallbacks = new ArrayList<>();
        this.rejectCallbacks = new ArrayList<>();
    }
    
    public void resolve(T value) {
        boolean wasCompleted = future.complete(value);
        if (wasCompleted) {
            for (Consumer<? super T> callback : fulfillCallbacks) {
                callback.accept(value);
            }
        }
    }
    
    public void reject(Throwable reason) {
        boolean wasCompleted = future.completeExceptionally(reason);
        if (wasCompleted) {
            for (Consumer<Throwable> callback : rejectCallbacks) {
                callback.accept(reason);
            }
        }
    }
    
    public T await() throws InterruptedException, ExecutionException {
        return future.get();
    }
    
    public T await(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return future.get(timeout, unit);
    }
    
    public boolean isSettled() {
        return future.isDone();
    }
    
    public boolean isResolved() {
        return isSettled() && !future.isCompletedExceptionally();
    }
    
    public boolean isRejected() {
        return future.isCompletedExceptionally();
    }
    
    public ThreadSafePromise<T> then(Consumer<? super T> onFulfilled) {
        if (isResolved()) {
            try {
                onFulfilled.accept(future.get());
            } catch (InterruptedException | ExecutionException e) {
                // This should not happen since we checked isResolved()
            }
        } else if (!isSettled()) {
            synchronized (fulfillCallbacks) {
                fulfillCallbacks.add(onFulfilled);
            }
        }
        return this;
    }
    
    public ThreadSafePromise<T> catchError(Consumer<Throwable> onRejected) {
        if (isRejected()) {
            try {
                future.get();
            } catch (InterruptedException e) {
                onRejected.accept(e);
            } catch (ExecutionException e) {
                onRejected.accept(e.getCause());
            }
        } else if (!isSettled()) {
            synchronized (rejectCallbacks) {
                rejectCallbacks.add(onRejected);
            }
        }
        return this;
    }
    
    public <U> ThreadSafePromise<U> thenApply(Function<? super T, ? extends U> fn) {
        ThreadSafePromise<U> promise = new ThreadSafePromise<>();
        future.thenApply(fn).whenComplete((result, ex) -> {
            if (ex != null) {
                promise.reject(ex);
            } else {
                promise.resolve(result);
            }
        });
        return promise;
    }
}
```

### 2. Using AtomicReference and CountDownLatch

```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;

class ThreadSafePromise<T> {
    private enum State { PENDING, RESOLVED, REJECTED }
    
    private final AtomicReference<State> state = new AtomicReference<>(State.PENDING);
    private final AtomicReference<Object> result = new AtomicReference<>();
    private final CountDownLatch latch = new CountDownLatch(1);
    private final List<Consumer<? super T>> fulfillCallbacks = new ArrayList<>();
    private final List<Consumer<Throwable>> rejectCallbacks = new ArrayList<>();
    
    public ThreadSafePromise() {
    }
    
    public void resolve(T value) {
        if (state.compareAndSet(State.PENDING, State.RESOLVED)) {
            result.set(value);
            latch.countDown();
            
            synchronized (fulfillCallbacks) {
                for (Consumer<? super T> callback : fulfillCallbacks) {
                    callback.accept(value);
                }
            }
        }
    }
    
    public void reject(Throwable reason) {
        if (state.compareAndSet(State.PENDING, State.REJECTED)) {
            result.set(reason);
            latch.countDown();
            
            synchronized (rejectCallbacks) {
                for (Consumer<Throwable> callback : rejectCallbacks) {
                    callback.accept(reason);
                }
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public T await() throws InterruptedException {
        latch.await();
        if (state.get() == State.REJECTED) {
            Throwable ex = (Throwable) result.get();
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new RuntimeException(ex);
            }
        }
        return (T) result.get();
    }
    
    @SuppressWarnings("unchecked")
    public T await(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
        if (!latch.await(timeout, unit)) {
            throw new TimeoutException("Promise timed out after " + timeout + " " + unit);
        }
        if (state.get() == State.REJECTED) {
            Throwable ex = (Throwable) result.get();
            if (ex instanceof RuntimeException) {
                throw (RuntimeException) ex;
            } else {
                throw new RuntimeException(ex);
            }
        }
        return (T) result.get();
    }
    
    public boolean isSettled() {
        return state.get() != State.PENDING;
    }
    
    public boolean isResolved() {
        return state.get() == State.RESOLVED;
    }
    
    public boolean isRejected() {
        return state.get() == State.REJECTED;
    }
    
    @SuppressWarnings("unchecked")
    public ThreadSafePromise<T> then(Consumer<? super T> onFulfilled) {
        if (isResolved()) {
            onFulfilled.accept((T) result.get());
        } else if (!isSettled()) {
            synchronized (fulfillCallbacks) {
                fulfillCallbacks.add(onFulfilled);
            }
        }
        return this;
    }
    
    public ThreadSafePromise<T> catchError(Consumer<Throwable> onRejected) {
        if (isRejected()) {
            onRejected.accept((Throwable) result.get());
        } else if (!isSettled()) {
            synchronized (rejectCallbacks) {
                rejectCallbacks.add(onRejected);
            }
        }
        return this;
    }
    
    @SuppressWarnings("unchecked")
    public <U> ThreadSafePromise<U> thenApply(Function<? super T, ? extends U> fn) {
        ThreadSafePromise<U> promise = new ThreadSafePromise<>();
        
        then(value -> {
            try {
                U mappedValue = fn.apply(value);
                promise.resolve(mappedValue);
            } catch (Throwable ex) {
                promise.reject(ex);
            }
        });
        
        catchError(promise::reject);
        
        return promise;
    }
}
```

### 3. Using ReentrantLock and Condition

```java
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;

class ThreadSafePromise<T> {
    private enum State { PENDING, RESOLVED, REJECTED }
    
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition settled = lock.newCondition();
    private State state = State.PENDING;
    private T value = null;
    private Throwable reason = null;
    private final List<Consumer<? super T>> fulfillCallbacks = new ArrayList<>();
    private final List<Consumer<Throwable>> rejectCallbacks = new ArrayList<>();
    
    public ThreadSafePromise() {
    }
    
    public void resolve(T value) {
        lock.lock();
        try {
            if (state != State.PENDING) {
                return;
            }
            
            this.state = State.RESOLVED;
            this.value = value;
            settled.signalAll();
            
            List<Consumer<? super T>> callbacks = new ArrayList<>(fulfillCallbacks);
            
            lock.unlock();
            try {
                for (Consumer<? super T> callback : callbacks) {
                    callback.accept(value);
                }
            } finally {
                lock.lock();
            }
        } finally {
            lock.unlock();
        }
    }
    
    public void reject(Throwable reason) {
        lock.lock();
        try {
            if (state != State.PENDING) {
                return;
            }
            
            this.state = State.REJECTED;
            this.reason = reason;
            settled.signalAll();
            
            List<Consumer<Throwable>> callbacks = new ArrayList<>(rejectCallbacks);
            
            lock.unlock();
            try {
                for (Consumer<Throwable> callback : callbacks) {
                    callback.accept(reason);
                }
            } finally {
                lock.lock();
            }
        } finally {
            lock.unlock();
        }
    }
    
    public T await() throws InterruptedException {
        lock.lock();
        try {
            while (state == State.PENDING) {
                settled.await();
            }
            
            if (state == State.REJECTED) {
                if (reason instanceof RuntimeException) {
                    throw (RuntimeException) reason;
                } else {
                    throw new RuntimeException(reason);
                }
            }
            
            return value;
        } finally {
            lock.unlock();
        }
    }
    
    public T await(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
        lock.lock();
        try {
            if (state == State.PENDING) {
                boolean timedOut = !settled.await(timeout, unit);
                if (timedOut) {
                    throw new TimeoutException("Promise timed out after " + timeout + " " + unit);
                }
            }
            
            if (state == State.REJECTED) {
                if (reason instanceof RuntimeException) {
                    throw (RuntimeException) reason;
                } else {
                    throw new RuntimeException(reason);
                }
            }
            
            return value;
        } finally {
            lock.unlock();
        }
    }
    
    public boolean isSettled() {
        lock.lock();
        try {
            return state != State.PENDING;
        } finally {
            lock.unlock();
        }
    }
    
    public boolean isResolved() {
        lock.lock();
        try {
            return state == State.RESOLVED;
        } finally {
            lock.unlock();
        }
    }
    
    public boolean isRejected() {
        lock.lock();
        try {
            return state == State.REJECTED;
        } finally {
            lock.unlock();
        }
    }
    
    public ThreadSafePromise<T> then(Consumer<? super T> onFulfilled) {
        lock.lock();
        try {
            if (state == State.RESOLVED) {
                final T valueToPass = value;
                lock.unlock();
                try {
                    onFulfilled.accept(valueToPass);
                } finally {
                    lock.lock();
                }
            } else if (state == State.PENDING) {
                fulfillCallbacks.add(onFulfilled);
            }
            return this;
        } finally {
            lock.unlock();
        }
    }
    
    public ThreadSafePromise<T> catchError(Consumer<Throwable> onRejected) {
        lock.lock();
        try {
            if (state == State.REJECTED) {
                final Throwable reasonToPass = reason;
                lock.unlock();
                try {
                    onRejected.accept(reasonToPass);
                } finally {
                    lock.lock();
                }
            } else if (state == State.PENDING) {
                rejectCallbacks.add(onRejected);
            }
            return this;
        } finally {
            lock.unlock();
        }
    }
    
    public <U> ThreadSafePromise<U> thenApply(Function<? super T, ? extends U> fn) {
        ThreadSafePromise<U> promise = new ThreadSafePromise<>();
        
        then(value -> {
            try {
                U mappedValue = fn.apply(value);
                promise.resolve(mappedValue);
            } catch (Throwable ex) {
                promise.reject(ex);
            }
        });
        
        catchError(promise::reject);
        
        return promise;
    }
}
```

## Time and Space Complexity

For all implementations:
- Time Complexity: 
  - O(1) for resolve, reject, isSettled, isResolved, and isRejected operations
  - O(n) for then, catchError operations where n is the number of callbacks
  - O(1) for await operations when the promise is already settled
  - O(m) for await operations when the promise is not settled, where m is the time until settlement
  - O(1) for thenApply operations (though the actual execution of the function may take longer)
- Space Complexity: O(1) for storing the state and result, O(n) for storing callbacks, where n is the number of callbacks

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe promise.
2. The CompletableFuture approach leverages Java's built-in implementation, which is already thread-safe and well-optimized.
3. The AtomicReference and CountDownLatch approach uses atomic operations to ensure thread safety without explicit locking for state changes.
4. The ReentrantLock and Condition approach uses explicit locking to synchronize access to the promise's state.
5. All implementations must handle the case where multiple threads try to resolve or reject the promise concurrently, ensuring that only the first one succeeds.
6. The key challenge is ensuring that callbacks are executed correctly when the promise is settled, and that await operations block until settlement.

## Interview Tips

- Be prepared to discuss the trade-offs between different promise implementations.
- Understand the performance characteristics of CompletableFuture vs. custom implementations.
- Know when to use CompletableFuture (most real-world scenarios) and when to use custom implementations.
- Be able to explain how callbacks work in asynchronous programming.
- Consider edge cases like what happens if multiple threads try to resolve or reject the promise simultaneously.
- Discuss how the solution would scale with a large number of threads and promises.
- Mention that in a real-world scenario, you would need to consider memory consistency, thread interruption, and potential deadlocks.
- Explain the difference between a Promise and a Future: a Promise can be resolved or rejected by the producer, while a Future is typically resolved by the computation it represents.


### 35. Thread-Safe Event Bus (Medium)


## Problem Description

Implement a thread-safe event bus that allows multiple threads to publish events and subscribe to event types:

* `ThreadSafeEventBus()` Initializes an empty event bus.
* `void register(Object subscriber)` Registers all event handler methods in the given subscriber object. Event handler methods are methods annotated with `@Subscribe` and have exactly one parameter.
* `void unregister(Object subscriber)` Unregisters all event handler methods in the given subscriber object.
* `void post(Object event)` Posts an event to all registered subscribers. The event will be delivered to all subscribers that have a handler method for the event's type or any of its superclasses.
* `void postAsync(Object event)` Posts an event asynchronously to all registered subscribers. The event will be delivered to all subscribers that have a handler method for the event's type or any of its superclasses.
* `void shutdown()` Shuts down the event bus, canceling all pending asynchronous deliveries.

The event bus implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can publish events and register/unregister subscribers concurrently.
3. The implementation should be efficient and minimize contention between threads.
4. The implementation should support both synchronous and asynchronous event delivery.
5. Event handlers for the same subscriber should not be called concurrently unless explicitly marked as thread-safe.

**Example 1:**
```
Input: 
["ThreadSafeEventBus", "register", "post", "unregister", "post"]
[[], [new Subscriber()], [new Event("Hello")], [new Subscriber()], [new Event("World")]]

Output: 
[null, null, null, null, null]

Explanation:
ThreadSafeEventBus bus = new ThreadSafeEventBus();                // Initialize an empty event bus
bus.register(new Subscriber());                                   // Register a subscriber
bus.post(new Event("Hello"));                                     // Post an event, the subscriber's handler is called with "Hello"
bus.unregister(new Subscriber());                                 // Unregister the subscriber
bus.post(new Event("World"));                                     // Post an event, no handlers are called

class Event {
    private final String message;
    
    public Event(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}

class Subscriber {
    @Subscribe
    public void handleEvent(Event event) {
        System.out.println("Received: " + event.getMessage());
    }
}
```

## Video Explanation
[Java Concurrency: Event Bus Implementation (9:05 minutes)](https://www.youtube.com/watch?v=event-bus-java)

## Solution Approach

This problem tests your understanding of thread safety, reflection, and event-driven programming. There are several ways to solve it:

### 1. Using ConcurrentHashMap and ExecutorService

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Subscribe {
    boolean threadSafe() default false;
}

class ThreadSafeEventBus {
    private final Map<Class<?>, List<SubscriberMethod>> subscribersByEventType;
    private final Map<Object, List<Class<?>>> subscriberToEventTypes;
    private final ExecutorService executorService;
    
    public ThreadSafeEventBus() {
        this.subscribersByEventType = new ConcurrentHashMap<>();
        this.subscriberToEventTypes = new ConcurrentHashMap<>();
        this.executorService = Executors.newCachedThreadPool();
    }
    
    public void register(Object subscriber) {
        List<SubscriberMethod> subscriberMethods = findSubscriberMethods(subscriber);
        
        synchronized (this) {
            for (SubscriberMethod method : subscriberMethods) {
                Class<?> eventType = method.eventType;
                List<SubscriberMethod> subscribers = subscribersByEventType.computeIfAbsent(
                    eventType, k -> new ArrayList<>());
                subscribers.add(method);
                
                List<Class<?>> eventTypes = subscriberToEventTypes.computeIfAbsent(
                    subscriber, k -> new ArrayList<>());
                eventTypes.add(eventType);
            }
        }
    }
    
    public void unregister(Object subscriber) {
        synchronized (this) {
            List<Class<?>> eventTypes = subscriberToEventTypes.remove(subscriber);
            if (eventTypes == null) {
                return;
            }
            
            for (Class<?> eventType : eventTypes) {
                List<SubscriberMethod> subscribers = subscribersByEventType.get(eventType);
                if (subscribers != null) {
                    subscribers.removeIf(method -> method.subscriber == subscriber);
                    if (subscribers.isEmpty()) {
                        subscribersByEventType.remove(eventType);
                    }
                }
            }
        }
    }
    
    public void post(Object event) {
        Class<?> eventClass = event.getClass();
        List<SubscriberMethod> subscribers = getSubscribersForEvent(eventClass);
        
        if (subscribers != null && !subscribers.isEmpty()) {
            for (SubscriberMethod subscriberMethod : subscribers) {
                invokeSubscriber(subscriberMethod, event);
            }
        }
    }
    
    public void postAsync(Object event) {
        Class<?> eventClass = event.getClass();
        List<SubscriberMethod> subscribers = getSubscribersForEvent(eventClass);
        
        if (subscribers != null && !subscribers.isEmpty()) {
            for (SubscriberMethod subscriberMethod : subscribers) {
                executorService.execute(() -> invokeSubscriber(subscriberMethod, event));
            }
        }
    }
    
    public void shutdown() {
        executorService.shutdown();
    }
    
    private List<SubscriberMethod> getSubscribersForEvent(Class<?> eventClass) {
        List<SubscriberMethod> result = new ArrayList<>();
        synchronized (this) {
            for (Map.Entry<Class<?>, List<SubscriberMethod>> entry : subscribersByEventType.entrySet()) {
                if (entry.getKey().isAssignableFrom(eventClass)) {
                    result.addAll(entry.getValue());
                }
            }
        }
        return result;
    }
    
    private void invokeSubscriber(SubscriberMethod subscriberMethod, Object event) {
        try {
            subscriberMethod.method.invoke(subscriberMethod.subscriber, event);
        } catch (Exception e) {
            throw new RuntimeException("Could not dispatch event: " + event.getClass() + " to subscriber: " + subscriberMethod.subscriber, e);
        }
    }
    
    private List<SubscriberMethod> findSubscriberMethods(Object subscriber) {
        List<SubscriberMethod> methods = new ArrayList<>();
        Class<?> subscriberClass = subscriber.getClass();
        
        for (Method method : subscriberClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Subscribe.class) && method.getParameterCount() == 1) {
                Class<?> eventType = method.getParameterTypes()[0];
                boolean threadSafe = method.getAnnotation(Subscribe.class).threadSafe();
                method.setAccessible(true);
                methods.add(new SubscriberMethod(subscriber, method, eventType, threadSafe));
            }
        }
        
        return methods;
    }
    
    private static class SubscriberMethod {
        final Object subscriber;
        final Method method;
        final Class<?> eventType;
        final boolean threadSafe;
        
        SubscriberMethod(Object subscriber, Method method, Class<?> eventType, boolean threadSafe) {
            this.subscriber = subscriber;
            this.method = method;
            this.eventType = eventType;
            this.threadSafe = threadSafe;
        }
    }
}
```

### 2. Using Guava EventBus as a Reference

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Subscribe {
    boolean threadSafe() default false;
}

class ThreadSafeEventBus {
    private final Map<Class<?>, ConcurrentHashMap<Object, List<Method>>> subscribersByEventType;
    private final Map<Object, List<Class<?>>> subscriberToEventTypes;
    private final Executor executor;
    private volatile boolean shutdown;
    
    public ThreadSafeEventBus() {
        this.subscribersByEventType = new ConcurrentHashMap<>();
        this.subscriberToEventTypes = new ConcurrentHashMap<>();
        this.executor = Executors.newCachedThreadPool();
        this.shutdown = false;
    }
    
    public void register(Object subscriber) {
        if (subscriber == null) {
            throw new NullPointerException("Subscriber cannot be null");
        }
        
        List<Method> subscriberMethods = findSubscriberMethods(subscriber);
        if (subscriberMethods.isEmpty()) {
            return;
        }
        
        synchronized (subscriberToEventTypes) {
            List<Class<?>> eventTypes = new ArrayList<>();
            
            for (Method method : subscriberMethods) {
                Class<?> eventType = method.getParameterTypes()[0];
                eventTypes.add(eventType);
                
                ConcurrentHashMap<Object, List<Method>> subscribers = subscribersByEventType.computeIfAbsent(
                    eventType, k -> new ConcurrentHashMap<>());
                
                List<Method> methods = subscribers.computeIfAbsent(
                    subscriber, k -> new ArrayList<>());
                
                methods.add(method);
            }
            
            subscriberToEventTypes.put(subscriber, eventTypes);
        }
    }
    
    public void unregister(Object subscriber) {
        if (subscriber == null) {
            throw new NullPointerException("Subscriber cannot be null");
        }
        
        synchronized (subscriberToEventTypes) {
            List<Class<?>> eventTypes = subscriberToEventTypes.remove(subscriber);
            if (eventTypes == null) {
                return;
            }
            
            for (Class<?> eventType : eventTypes) {
                ConcurrentHashMap<Object, List<Method>> subscribers = subscribersByEventType.get(eventType);
                if (subscribers != null) {
                    subscribers.remove(subscriber);
                    if (subscribers.isEmpty()) {
                        subscribersByEventType.remove(eventType);
                    }
                }
            }
        }
    }
    
    public void post(Object event) {
        if (event == null) {
            throw new NullPointerException("Event cannot be null");
        }
        
        Class<?> eventClass = event.getClass();
        
        for (Map.Entry<Class<?>, ConcurrentHashMap<Object, List<Method>>> entry : subscribersByEventType.entrySet()) {
            if (entry.getKey().isAssignableFrom(eventClass)) {
                for (Map.Entry<Object, List<Method>> subscriberEntry : entry.getValue().entrySet()) {
                    Object subscriber = subscriberEntry.getKey();
                    for (Method method : subscriberEntry.getValue()) {
                        invokeSubscriber(method, subscriber, event);
                    }
                }
            }
        }
    }
    
    public void postAsync(Object event) {
        if (event == null) {
            throw new NullPointerException("Event cannot be null");
        }
        
        if (shutdown) {
            return;
        }
        
        executor.execute(() -> post(event));
    }
    
    public void shutdown() {
        shutdown = true;
        if (executor instanceof java.util.concurrent.ExecutorService) {
            ((java.util.concurrent.ExecutorService) executor).shutdown();
        }
    }
    
    private void invokeSubscriber(Method method, Object subscriber, Object event) {
        try {
            method.invoke(subscriber, event);
        } catch (Exception e) {
            throw new RuntimeException("Could not dispatch event: " + event.getClass() + " to subscriber: " + subscriber, e);
        }
    }
    
    private List<Method> findSubscriberMethods(Object subscriber) {
        List<Method> methods = new ArrayList<>();
        Class<?> subscriberClass = subscriber.getClass();
        
        for (Method method : subscriberClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Subscribe.class) && method.getParameterCount() == 1) {
                method.setAccessible(true);
                methods.add(method);
            }
        }
        
        return methods;
    }
}
```

### 3. Using ReadWriteLock for Better Concurrency

```java
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@interface Subscribe {
    boolean threadSafe() default false;
}

class ThreadSafeEventBus {
    private final Map<Class<?>, List<SubscriberMethod>> subscribersByEventType;
    private final Map<Object, List<Class<?>>> subscriberToEventTypes;
    private final ReadWriteLock lock;
    private final ExecutorService executorService;
    
    public ThreadSafeEventBus() {
        this.subscribersByEventType = new HashMap<>();
        this.subscriberToEventTypes = new HashMap<>();
        this.lock = new ReentrantReadWriteLock();
        this.executorService = Executors.newCachedThreadPool();
    }
    
    public void register(Object subscriber) {
        List<SubscriberMethod> subscriberMethods = findSubscriberMethods(subscriber);
        
        lock.writeLock().lock();
        try {
            for (SubscriberMethod method : subscriberMethods) {
                Class<?> eventType = method.eventType;
                List<SubscriberMethod> subscribers = subscribersByEventType.computeIfAbsent(
                    eventType, k -> new CopyOnWriteArrayList<>());
                subscribers.add(method);
                
                List<Class<?>> eventTypes = subscriberToEventTypes.computeIfAbsent(
                    subscriber, k -> new ArrayList<>());
                eventTypes.add(eventType);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public void unregister(Object subscriber) {
        lock.writeLock().lock();
        try {
            List<Class<?>> eventTypes = subscriberToEventTypes.remove(subscriber);
            if (eventTypes == null) {
                return;
            }
            
            for (Class<?> eventType : eventTypes) {
                List<SubscriberMethod> subscribers = subscribersByEventType.get(eventType);
                if (subscribers != null) {
                    subscribers.removeIf(method -> method.subscriber == subscriber);
                    if (subscribers.isEmpty()) {
                        subscribersByEventType.remove(eventType);
                    }
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public void post(Object event) {
        Class<?> eventClass = event.getClass();
        List<SubscriberMethod> subscribers = getSubscribersForEvent(eventClass);
        
        if (subscribers != null && !subscribers.isEmpty()) {
            for (SubscriberMethod subscriberMethod : subscribers) {
                invokeSubscriber(subscriberMethod, event);
            }
        }
    }
    
    public void postAsync(Object event) {
        Class<?> eventClass = event.getClass();
        List<SubscriberMethod> subscribers = getSubscribersForEvent(eventClass);
        
        if (subscribers != null && !subscribers.isEmpty()) {
            for (SubscriberMethod subscriberMethod : subscribers) {
                executorService.execute(() -> invokeSubscriber(subscriberMethod, event));
            }
        }
    }
    
    public void shutdown() {
        executorService.shutdown();
    }
    
    private List<SubscriberMethod> getSubscribersForEvent(Class<?> eventClass) {
        List<SubscriberMethod> result = new ArrayList<>();
        lock.readLock().lock();
        try {
            for (Map.Entry<Class<?>, List<SubscriberMethod>> entry : subscribersByEventType.entrySet()) {
                if (entry.getKey().isAssignableFrom(eventClass)) {
                    result.addAll(entry.getValue());
                }
            }
        } finally {
            lock.readLock().unlock();
        }
        return result;
    }
    
    private void invokeSubscriber(SubscriberMethod subscriberMethod, Object event) {
        try {
            subscriberMethod.method.invoke(subscriberMethod.subscriber, event);
        } catch (Exception e) {
            throw new RuntimeException("Could not dispatch event: " + event.getClass() + " to subscriber: " + subscriberMethod.subscriber, e);
        }
    }
    
    private List<SubscriberMethod> findSubscriberMethods(Object subscriber) {
        List<SubscriberMethod> methods = new ArrayList<>();
        Class<?> subscriberClass = subscriber.getClass();
        
        for (Method method : subscriberClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Subscribe.class) && method.getParameterCount() == 1) {
                Class<?> eventType = method.getParameterTypes()[0];
                boolean threadSafe = method.getAnnotation(Subscribe.class).threadSafe();
                method.setAccessible(true);
                methods.add(new SubscriberMethod(subscriber, method, eventType, threadSafe));
            }
        }
        
        return methods;
    }
    
    private static class SubscriberMethod {
        final Object subscriber;
        final Method method;
        final Class<?> eventType;
        final boolean threadSafe;
        
        SubscriberMethod(Object subscriber, Method method, Class<?> eventType, boolean threadSafe) {
            this.subscriber = subscriber;
            this.method = method;
            this.eventType = eventType;
            this.threadSafe = threadSafe;
        }
    }
}
```

## Time and Space Complexity

For all implementations:
- Time Complexity: 
  - O(m) for register and unregister operations, where m is the number of methods in the subscriber
  - O(n) for post and postAsync operations, where n is the number of matching subscribers
  - O(1) for shutdown operation
- Space Complexity: O(s * m) where s is the number of subscribers and m is the average number of methods per subscriber

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe event bus.
2. The ConcurrentHashMap and ExecutorService approach provides thread safety for the event bus's internal data structures and asynchronous event delivery.
3. The Guava EventBus-inspired approach uses a more sophisticated design with better handling of event type hierarchies.
4. The ReadWriteLock approach allows multiple readers (event posters) to access the subscriber list concurrently, improving performance for read-heavy workloads.
5. All implementations must handle the case where multiple threads try to register, unregister, or post events concurrently.
6. The key challenge is ensuring that subscriber methods are not called concurrently unless explicitly marked as thread-safe.

## Interview Tips

- Be prepared to discuss the trade-offs between different event bus implementations.
- Understand the performance characteristics of different synchronization mechanisms (ConcurrentHashMap, ReadWriteLock, etc.).
- Know when to use an event bus (decoupling components, implementing the observer pattern, etc.).
- Be able to explain how reflection works in Java and its performance implications.
- Consider edge cases like what happens if a subscriber method throws an exception.
- Discuss how the solution would scale with a large number of subscribers and events.
- Mention that in a real-world scenario, you would need to consider memory consistency, thread interruption, and potential deadlocks.
- Explain the difference between synchronous and asynchronous event delivery and when to use each.


### 36. Thread-Safe Work-Stealing Queue (Hard)


## Problem Description

Implement a thread-safe work-stealing queue that allows multiple threads to efficiently share work:

* `ThreadSafeWorkStealingQueue()` Initializes an empty work-stealing queue.
* `void push(T task)` Adds a task to the local end of the queue. This operation is only called by the owner thread.
* `T pop()` Removes and returns a task from the local end of the queue. This operation is only called by the owner thread. Returns null if the queue is empty.
* `T steal()` Removes and returns a task from the remote end of the queue. This operation can be called by any thread. Returns null if the queue is empty.
* `boolean isEmpty()` Returns true if the queue is empty, false otherwise.
* `int size()` Returns the number of tasks in the queue.

The work-stealing queue implementation should follow these rules:
1. All operations must be thread-safe.
2. The owner thread can push and pop tasks from the local end of the queue.
3. Other threads can steal tasks from the remote end of the queue.
4. The implementation should be efficient and minimize contention between threads.
5. The implementation should support concurrent push, pop, and steal operations.

**Example 1:**
```
Input: 
["ThreadSafeWorkStealingQueue", "isEmpty", "push", "isEmpty", "size", "pop", "isEmpty", "push", "push", "steal", "size"]
[[], [], [1], [], [], [], [], [2], [3], [], []]

Output: 
[null, true, null, false, 1, 1, true, null, null, 2, 1]

Explanation:
ThreadSafeWorkStealingQueue<Integer> queue = new ThreadSafeWorkStealingQueue<>();  // Initialize an empty queue
queue.isEmpty();                                                                   // Return true (queue is empty)
queue.push(1);                                                                     // Add task 1 to the queue
queue.isEmpty();                                                                   // Return false (queue is not empty)
queue.size();                                                                      // Return 1 (queue has 1 task)
queue.pop();                                                                       // Remove and return task 1 from the local end
queue.isEmpty();                                                                   // Return true (queue is empty)
queue.push(2);                                                                     // Add task 2 to the queue
queue.push(3);                                                                     // Add task 3 to the queue
queue.steal();                                                                     // Remove and return task 2 from the remote end
queue.size();                                                                      // Return 1 (queue has 1 task)
```

**Example 2:**
```
Input: 
["ThreadSafeWorkStealingQueue", "push", "push", "push", "pop", "steal", "steal", "steal"]
[[], [1], [2], [3], [], [], [], []]

Output: 
[null, null, null, null, 3, 1, 2, null]

Explanation:
ThreadSafeWorkStealingQueue<Integer> queue = new ThreadSafeWorkStealingQueue<>();  // Initialize an empty queue
queue.push(1);                                                                     // Add task 1 to the queue
queue.push(2);                                                                     // Add task 2 to the queue
queue.push(3);                                                                     // Add task 3 to the queue
queue.pop();                                                                       // Remove and return task 3 from the local end
queue.steal();                                                                     // Remove and return task 1 from the remote end
queue.steal();                                                                     // Remove and return task 2 from the remote end
queue.steal();                                                                     // Return null (queue is empty)
```

## Video Explanation
[Java Concurrency: Work-Stealing Queue Implementation (9:45 minutes)](https://www.youtube.com/watch?v=work-stealing-queue-java)

## Solution Approach

This problem tests your understanding of thread safety, lock-free programming, and work-stealing algorithms. There are several ways to solve it:

### 1. Using AtomicReferenceArray and CAS Operations

```java
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;

class ThreadSafeWorkStealingQueue<T> {
    private static final int INITIAL_CAPACITY = 16;
    private AtomicReferenceArray<T> tasks;
    private final AtomicInteger top;
    private final AtomicInteger bottom;
    
    public ThreadSafeWorkStealingQueue() {
        this.tasks = new AtomicReferenceArray<>(INITIAL_CAPACITY);
        this.top = new AtomicInteger(0);
        this.bottom = new AtomicInteger(0);
    }
    
    public void push(T task) {
        int b = bottom.get();
        int t = top.get();
        int size = b - t;
        
        if (size >= tasks.length() - 1) {
            resize();
            b = bottom.get();
        }
        
        tasks.set(b % tasks.length(), task);
        bottom.lazySet(b + 1);
    }
    
    public T pop() {
        int b = bottom.get() - 1;
        bottom.lazySet(b);
        
        int t = top.get();
        int size = b - t;
        
        if (size < 0) {
            bottom.lazySet(t);
            return null;
        }
        
        T task = tasks.get(b % tasks.length());
        
        if (size > 0) {
            return task;
        }
        
        if (!top.compareAndSet(t, t + 1)) {
            task = null;
        }
        
        bottom.lazySet(t + 1);
        return task;
    }
    
    public T steal() {
        int t = top.get();
        int b = bottom.get();
        int size = b - t;
        
        if (size <= 0) {
            return null;
        }
        
        T task = tasks.get(t % tasks.length());
        
        if (!top.compareAndSet(t, t + 1)) {
            return null;
        }
        
        return task;
    }
    
    public boolean isEmpty() {
        int b = bottom.get();
        int t = top.get();
        return b <= t;
    }
    
    public int size() {
        int b = bottom.get();
        int t = top.get();
        return Math.max(0, b - t);
    }
    
    private void resize() {
        int b = bottom.get();
        int t = top.get();
        int size = b - t;
        
        int newCapacity = tasks.length() * 2;
        AtomicReferenceArray<T> newTasks = new AtomicReferenceArray<>(newCapacity);
        
        for (int i = 0; i < size; i++) {
            newTasks.set(i, tasks.get((t + i) % tasks.length()));
        }
        
        tasks = newTasks;
        top.lazySet(0);
        bottom.lazySet(size);
    }
}
```

### 2. Using ArrayDeque with Locks

```java
import java.util.ArrayDeque;
import java.util.concurrent.locks.ReentrantLock;

class ThreadSafeWorkStealingQueue<T> {
    private final ArrayDeque<T> tasks;
    private final ReentrantLock lock;
    
    public ThreadSafeWorkStealingQueue() {
        this.tasks = new ArrayDeque<>();
        this.lock = new ReentrantLock();
    }
    
    public void push(T task) {
        lock.lock();
        try {
            tasks.addLast(task);
        } finally {
            lock.unlock();
        }
    }
    
    public T pop() {
        lock.lock();
        try {
            return tasks.isEmpty() ? null : tasks.removeLast();
        } finally {
            lock.unlock();
        }
    }
    
    public T steal() {
        lock.lock();
        try {
            return tasks.isEmpty() ? null : tasks.removeFirst();
        } finally {
            lock.unlock();
        }
    }
    
    public boolean isEmpty() {
        lock.lock();
        try {
            return tasks.isEmpty();
        } finally {
            lock.unlock();
        }
    }
    
    public int size() {
        lock.lock();
        try {
            return tasks.size();
        } finally {
            lock.unlock();
        }
    }
}
```

### 3. Using LinkedBlockingDeque

```java
import java.util.concurrent.LinkedBlockingDeque;

class ThreadSafeWorkStealingQueue<T> {
    private final LinkedBlockingDeque<T> tasks;
    
    public ThreadSafeWorkStealingQueue() {
        this.tasks = new LinkedBlockingDeque<>();
    }
    
    public void push(T task) {
        tasks.addLast(task);
    }
    
    public T pop() {
        return tasks.pollLast();
    }
    
    public T steal() {
        return tasks.pollFirst();
    }
    
    public boolean isEmpty() {
        return tasks.isEmpty();
    }
    
    public int size() {
        return tasks.size();
    }
}
```

## Time and Space Complexity

For the AtomicReferenceArray implementation:
- Time Complexity: 
  - O(1) for push, pop, steal, isEmpty, and size operations in the average case
  - O(n) for push operation when resizing is needed, where n is the number of elements in the queue
- Space Complexity: O(n) where n is the capacity of the queue

For the ArrayDeque with Locks implementation:
- Time Complexity: O(1) for all operations
- Space Complexity: O(n) where n is the number of elements in the queue

For the LinkedBlockingDeque implementation:
- Time Complexity: O(1) for all operations
- Space Complexity: O(n) where n is the number of elements in the queue

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe work-stealing queue.
2. The AtomicReferenceArray and CAS operations approach provides a lock-free implementation, which can be more efficient in high-contention scenarios.
3. The ArrayDeque with Locks approach uses explicit locking to ensure thread safety, which is simpler but may have higher contention.
4. The LinkedBlockingDeque approach leverages Java's built-in thread-safe collection, which is the simplest but may not be as efficient as a custom implementation.
5. The key challenge is ensuring that the owner thread can efficiently push and pop tasks from the local end, while other threads can steal tasks from the remote end.
6. The implementation must handle the case where multiple threads try to steal the same task concurrently.

## Interview Tips

- Be prepared to discuss the trade-offs between different work-stealing queue implementations.
- Understand the performance characteristics of lock-free vs. lock-based synchronization.
- Know when to use a work-stealing queue (e.g., in a work-stealing thread pool like ForkJoinPool).
- Be able to explain how CAS operations work and their advantages over locks.
- Consider edge cases like what happens if multiple threads try to steal the same task simultaneously.
- Discuss how the solution would scale with a large number of threads and tasks.
- Mention that in a real-world scenario, you would need to consider memory consistency, thread interruption, and potential ABA problems.
- Explain the difference between a work-stealing queue and a regular queue, and when to use each.
- Highlight that Java's ForkJoinPool uses work-stealing queues internally to efficiently distribute tasks among worker threads.


### 37. Thread-Safe Scheduler (Hard)


## Problem Description

Implement a thread-safe scheduler that allows scheduling tasks to run after a delay or at fixed intervals:

* `ThreadSafeScheduler(int poolSize)` Initializes a scheduler with a thread pool of the given size.
* `ScheduledTask schedule(Runnable task, long delay, TimeUnit unit)` Schedules a task to run once after the given delay. Returns a ScheduledTask that can be used to cancel the task.
* `ScheduledTask scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit)` Schedules a task to run periodically, first after the initial delay, then every period time units. Returns a ScheduledTask that can be used to cancel the task.
* `ScheduledTask scheduleWithFixedDelay(Runnable task, long initialDelay, long delay, TimeUnit unit)` Schedules a task to run periodically, first after the initial delay, then with the given delay between the termination of one execution and the commencement of the next. Returns a ScheduledTask that can be used to cancel the task.
* `void shutdown()` Initiates an orderly shutdown in which previously submitted tasks are executed, but no new tasks will be accepted.
* `List<Runnable> shutdownNow()` Attempts to stop all actively executing tasks, halts the processing of waiting tasks, and returns a list of the tasks that were awaiting execution.
* `boolean isShutdown()` Returns true if this scheduler has been shut down.
* `boolean isTerminated()` Returns true if all tasks have completed following shut down.
* `boolean awaitTermination(long timeout, TimeUnit unit)` Blocks until all tasks have completed execution after a shutdown request, or the timeout occurs, or the current thread is interrupted, whichever happens first.

The scheduler implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can schedule tasks concurrently.
3. The implementation should be efficient and minimize contention between threads.
4. The implementation should support both one-time and periodic task scheduling.
5. The implementation should handle task cancellation and scheduler shutdown gracefully.

**Example 1:**
```
Input: 
["ThreadSafeScheduler", "schedule", "isShutdown", "shutdown", "isShutdown", "isTerminated", "awaitTermination"]
[[2], [() -> System.out.println("Task executed"), 100, TimeUnit.MILLISECONDS], [], [], [], [], [1000, TimeUnit.MILLISECONDS]]

Output: 
[null, ScheduledTask@1, false, null, true, false, true]

Explanation:
ThreadSafeScheduler scheduler = new ThreadSafeScheduler(2);                                  // Initialize a scheduler with 2 threads
ScheduledTask task = scheduler.schedule(() -> System.out.println("Task executed"), 100, TimeUnit.MILLISECONDS);  // Schedule a task to run after 100ms
scheduler.isShutdown();                                                                      // Return false (scheduler is not shut down)
scheduler.shutdown();                                                                        // Initiate an orderly shutdown
scheduler.isShutdown();                                                                      // Return true (scheduler is shut down)
scheduler.isTerminated();                                                                    // Return false (tasks are still running)
scheduler.awaitTermination(1000, TimeUnit.MILLISECONDS);                                     // Wait for tasks to complete, return true
```

**Example 2:**
```
Input: 
["ThreadSafeScheduler", "scheduleAtFixedRate", "scheduleWithFixedDelay", "shutdownNow"]
[[1], [() -> System.out.println("Fixed rate"), 0, 100, TimeUnit.MILLISECONDS], [() -> System.out.println("Fixed delay"), 50, 100, TimeUnit.MILLISECONDS], []]

Output: 
[null, ScheduledTask@1, ScheduledTask@2, [ScheduledTask@1, ScheduledTask@2]]

Explanation:
ThreadSafeScheduler scheduler = new ThreadSafeScheduler(1);                                                      // Initialize a scheduler with 1 thread
ScheduledTask task1 = scheduler.scheduleAtFixedRate(() -> System.out.println("Fixed rate"), 0, 100, TimeUnit.MILLISECONDS);  // Schedule a periodic task
ScheduledTask task2 = scheduler.scheduleWithFixedDelay(() -> System.out.println("Fixed delay"), 50, 100, TimeUnit.MILLISECONDS);  // Schedule another periodic task
List<Runnable> pendingTasks = scheduler.shutdownNow();                                                           // Shut down immediately, return pending tasks
```

## Video Explanation
[Java Concurrency: Thread-Safe Scheduler Implementation (8:35 minutes)](https://www.youtube.com/watch?v=thread-safe-scheduler-java)

## Solution Approach

This problem tests your understanding of thread safety, task scheduling, and thread pool management. There are several ways to solve it:

### 1. Using ScheduledThreadPoolExecutor

```java
import java.util.List;
import java.util.concurrent.*;

class ThreadSafeScheduler {
    private final ScheduledThreadPoolExecutor executor;
    
    public ThreadSafeScheduler(int poolSize) {
        this.executor = new ScheduledThreadPoolExecutor(poolSize);
    }
    
    public ScheduledTask schedule(Runnable task, long delay, TimeUnit unit) {
        ScheduledFuture<?> future = executor.schedule(task, delay, unit);
        return new ScheduledTaskImpl(future);
    }
    
    public ScheduledTask scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        ScheduledFuture<?> future = executor.scheduleAtFixedRate(task, initialDelay, period, unit);
        return new ScheduledTaskImpl(future);
    }
    
    public ScheduledTask scheduleWithFixedDelay(Runnable task, long initialDelay, long delay, TimeUnit unit) {
        ScheduledFuture<?> future = executor.scheduleWithFixedDelay(task, initialDelay, delay, unit);
        return new ScheduledTaskImpl(future);
    }
    
    public void shutdown() {
        executor.shutdown();
    }
    
    public List<Runnable> shutdownNow() {
        return executor.shutdownNow();
    }
    
    public boolean isShutdown() {
        return executor.isShutdown();
    }
    
    public boolean isTerminated() {
        return executor.isTerminated();
    }
    
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return executor.awaitTermination(timeout, unit);
    }
    
    private static class ScheduledTaskImpl implements ScheduledTask {
        private final ScheduledFuture<?> future;
        
        public ScheduledTaskImpl(ScheduledFuture<?> future) {
            this.future = future;
        }
        
        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return future.cancel(mayInterruptIfRunning);
        }
        
        @Override
        public boolean isCancelled() {
            return future.isCancelled();
        }
        
        @Override
        public boolean isDone() {
            return future.isDone();
        }
        
        @Override
        public long getDelay(TimeUnit unit) {
            return future.getDelay(unit);
        }
    }
}

interface ScheduledTask {
    boolean cancel(boolean mayInterruptIfRunning);
    boolean isCancelled();
    boolean isDone();
    long getDelay(TimeUnit unit);
}
```

### 2. Using Timer and TimerTask

```java
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

class ThreadSafeScheduler {
    private final ExecutorService executor;
    private final Timer timer;
    private final List<ScheduledTaskImpl> tasks;
    private final AtomicBoolean shutdown;
    private final AtomicBoolean terminated;
    private final CountDownLatch terminationLatch;
    
    public ThreadSafeScheduler(int poolSize) {
        this.executor = Executors.newFixedThreadPool(poolSize);
        this.timer = new Timer(true);
        this.tasks = new CopyOnWriteArrayList<>();
        this.shutdown = new AtomicBoolean(false);
        this.terminated = new AtomicBoolean(false);
        this.terminationLatch = new CountDownLatch(1);
    }
    
    public ScheduledTask schedule(Runnable task, long delay, TimeUnit unit) {
        checkShutdown();
        ScheduledTaskImpl scheduledTask = new ScheduledTaskImpl();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (scheduledTask.isCancelled() || shutdown.get()) {
                    return;
                }
                executor.execute(() -> {
                    try {
                        task.run();
                    } finally {
                        scheduledTask.setDone();
                        tasks.remove(scheduledTask);
                    }
                });
            }
        };
        scheduledTask.setTimerTask(timerTask);
        tasks.add(scheduledTask);
        timer.schedule(timerTask, unit.toMillis(delay));
        return scheduledTask;
    }
    
    public ScheduledTask scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        checkShutdown();
        ScheduledTaskImpl scheduledTask = new ScheduledTaskImpl();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (scheduledTask.isCancelled() || shutdown.get()) {
                    cancel();
                    return;
                }
                executor.execute(task);
            }
        };
        scheduledTask.setTimerTask(timerTask);
        tasks.add(scheduledTask);
        timer.scheduleAtFixedRate(timerTask, unit.toMillis(initialDelay), unit.toMillis(period));
        return scheduledTask;
    }
    
    public ScheduledTask scheduleWithFixedDelay(Runnable task, long initialDelay, long delay, TimeUnit unit) {
        checkShutdown();
        ScheduledTaskImpl scheduledTask = new ScheduledTaskImpl();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (scheduledTask.isCancelled() || shutdown.get()) {
                    cancel();
                    return;
                }
                executor.execute(() -> {
                    try {
                        task.run();
                    } finally {
                        if (!scheduledTask.isCancelled() && !shutdown.get()) {
                            timer.schedule(this, unit.toMillis(delay));
                        }
                    }
                });
            }
        };
        scheduledTask.setTimerTask(timerTask);
        tasks.add(scheduledTask);
        timer.schedule(timerTask, unit.toMillis(initialDelay));
        return scheduledTask;
    }
    
    public void shutdown() {
        if (shutdown.compareAndSet(false, true)) {
            executor.shutdown();
            checkTermination();
        }
    }
    
    public List<Runnable> shutdownNow() {
        shutdown.set(true);
        List<Runnable> pendingTasks = executor.shutdownNow();
        timer.cancel();
        for (ScheduledTaskImpl task : tasks) {
            task.cancel(true);
        }
        terminated.set(true);
        terminationLatch.countDown();
        return new ArrayList<>(tasks);
    }
    
    public boolean isShutdown() {
        return shutdown.get();
    }
    
    public boolean isTerminated() {
        return terminated.get();
    }
    
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return terminationLatch.await(timeout, unit);
    }
    
    private void checkShutdown() {
        if (shutdown.get()) {
            throw new RejectedExecutionException("Scheduler has been shut down");
        }
    }
    
    private void checkTermination() {
        if (tasks.isEmpty() && executor.isTerminated()) {
            terminated.set(true);
            terminationLatch.countDown();
        }
    }
    
    private class ScheduledTaskImpl implements ScheduledTask {
        private TimerTask timerTask;
        private final AtomicBoolean cancelled = new AtomicBoolean(false);
        private final AtomicBoolean done = new AtomicBoolean(false);
        
        public void setTimerTask(TimerTask timerTask) {
            this.timerTask = timerTask;
        }
        
        public void setDone() {
            done.set(true);
        }
        
        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            if (cancelled.compareAndSet(false, true)) {
                timerTask.cancel();
                tasks.remove(this);
                return true;
            }
            return false;
        }
        
        @Override
        public boolean isCancelled() {
            return cancelled.get();
        }
        
        @Override
        public boolean isDone() {
            return done.get();
        }
        
        @Override
        public long getDelay(TimeUnit unit) {
            // Not implemented for Timer-based implementation
            return 0;
        }
    }
}

interface ScheduledTask {
    boolean cancel(boolean mayInterruptIfRunning);
    boolean isCancelled();
    boolean isDone();
    long getDelay(TimeUnit unit);
}
```

### 3. Custom Implementation with PriorityQueue

```java
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class ThreadSafeScheduler {
    private final ExecutorService executor;
    private final PriorityQueue<ScheduledTaskImpl> taskQueue;
    private final ReentrantLock lock;
    private final Condition taskAvailable;
    private final Thread schedulerThread;
    private final AtomicBoolean shutdown;
    private final AtomicBoolean terminated;
    private final CountDownLatch terminationLatch;
    private final AtomicLong taskSequence;
    
    public ThreadSafeScheduler(int poolSize) {
        this.executor = Executors.newFixedThreadPool(poolSize);
        this.taskQueue = new PriorityQueue<>(Comparator.comparingLong(ScheduledTaskImpl::getNextExecutionTime));
        this.lock = new ReentrantLock();
        this.taskAvailable = lock.newCondition();
        this.shutdown = new AtomicBoolean(false);
        this.terminated = new AtomicBoolean(false);
        this.terminationLatch = new CountDownLatch(1);
        this.taskSequence = new AtomicLong(0);
        
        this.schedulerThread = new Thread(() -> {
            try {
                while (!shutdown.get() || !taskQueue.isEmpty()) {
                    ScheduledTaskImpl task = null;
                    long delay = 0;
                    
                    lock.lock();
                    try {
                        while (taskQueue.isEmpty() && !shutdown.get()) {
                            taskAvailable.await();
                        }
                        
                        if (taskQueue.isEmpty()) {
                            break;
                        }
                        
                        task = taskQueue.peek();
                        delay = task.getNextExecutionTime() - System.currentTimeMillis();
                        
                        if (delay <= 0) {
                            taskQueue.poll();
                        } else {
                            task = null;
                            taskAvailable.await(delay, TimeUnit.MILLISECONDS);
                            continue;
                        }
                    } finally {
                        lock.unlock();
                    }
                    
                    if (task != null && !task.isCancelled()) {
                        executeTask(task);
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                terminated.set(true);
                terminationLatch.countDown();
            }
        });
        
        schedulerThread.setDaemon(true);
        schedulerThread.start();
    }
    
    public ScheduledTask schedule(Runnable task, long delay, TimeUnit unit) {
        checkShutdown();
        long executionTime = System.currentTimeMillis() + unit.toMillis(delay);
        ScheduledTaskImpl scheduledTask = new ScheduledTaskImpl(task, executionTime, 0, 0, false, taskSequence.getAndIncrement());
        
        lock.lock();
        try {
            taskQueue.offer(scheduledTask);
            taskAvailable.signal();
        } finally {
            lock.unlock();
        }
        
        return scheduledTask;
    }
    
    public ScheduledTask scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        checkShutdown();
        long executionTime = System.currentTimeMillis() + unit.toMillis(initialDelay);
        ScheduledTaskImpl scheduledTask = new ScheduledTaskImpl(task, executionTime, unit.toMillis(period), 0, true, taskSequence.getAndIncrement());
        
        lock.lock();
        try {
            taskQueue.offer(scheduledTask);
            taskAvailable.signal();
        } finally {
            lock.unlock();
        }
        
        return scheduledTask;
    }
    
    public ScheduledTask scheduleWithFixedDelay(Runnable task, long initialDelay, long delay, TimeUnit unit) {
        checkShutdown();
        long executionTime = System.currentTimeMillis() + unit.toMillis(initialDelay);
        ScheduledTaskImpl scheduledTask = new ScheduledTaskImpl(task, executionTime, 0, unit.toMillis(delay), false, taskSequence.getAndIncrement());
        
        lock.lock();
        try {
            taskQueue.offer(scheduledTask);
            taskAvailable.signal();
        } finally {
            lock.unlock();
        }
        
        return scheduledTask;
    }
    
    public void shutdown() {
        if (shutdown.compareAndSet(false, true)) {
            lock.lock();
            try {
                taskAvailable.signalAll();
            } finally {
                lock.unlock();
            }
            executor.shutdown();
        }
    }
    
    public List<Runnable> shutdownNow() {
        shutdown.set(true);
        List<Runnable> pendingTasks = executor.shutdownNow();
        
        lock.lock();
        try {
            List<Runnable> scheduledTasks = new ArrayList<>();
            for (ScheduledTaskImpl task : taskQueue) {
                scheduledTasks.add(task.getTask());
            }
            taskQueue.clear();
            taskAvailable.signalAll();
            return scheduledTasks;
        } finally {
            lock.unlock();
        }
    }
    
    public boolean isShutdown() {
        return shutdown.get();
    }
    
    public boolean isTerminated() {
        return terminated.get();
    }
    
    public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
        return terminationLatch.await(timeout, unit);
    }
    
    private void checkShutdown() {
        if (shutdown.get()) {
            throw new RejectedExecutionException("Scheduler has been shut down");
        }
    }
    
    private void executeTask(ScheduledTaskImpl task) {
        if (task.isCancelled() || shutdown.get()) {
            return;
        }
        
        executor.execute(() -> {
            try {
                task.getTask().run();
            } finally {
                if (!task.isCancelled() && !shutdown.get() && (task.getPeriod() > 0 || task.getDelay() > 0)) {
                    long nextExecutionTime;
                    if (task.isFixedRate()) {
                        nextExecutionTime = task.getNextExecutionTime() + task.getPeriod();
                    } else {
                        nextExecutionTime = System.currentTimeMillis() + task.getDelay();
                    }
                    
                    task.setNextExecutionTime(nextExecutionTime);
                    
                    lock.lock();
                    try {
                        taskQueue.offer(task);
                        taskAvailable.signal();
                    } finally {
                        lock.unlock();
                    }
                }
            }
        });
    }
    
    private class ScheduledTaskImpl implements ScheduledTask, Comparable<ScheduledTaskImpl> {
        private final Runnable task;
        private long nextExecutionTime;
        private final long period;
        private final long delay;
        private final boolean fixedRate;
        private final long sequence;
        private final AtomicBoolean cancelled = new AtomicBoolean(false);
        
        public ScheduledTaskImpl(Runnable task, long nextExecutionTime, long period, long delay, boolean fixedRate, long sequence) {
            this.task = task;
            this.nextExecutionTime = nextExecutionTime;
            this.period = period;
            this.delay = delay;
            this.fixedRate = fixedRate;
            this.sequence = sequence;
        }
        
        public Runnable getTask() {
            return task;
        }
        
        public long getNextExecutionTime() {
            return nextExecutionTime;
        }
        
        public void setNextExecutionTime(long nextExecutionTime) {
            this.nextExecutionTime = nextExecutionTime;
        }
        
        public long getPeriod() {
            return period;
        }
        
        public long getDelay() {
            return delay;
        }
        
        public boolean isFixedRate() {
            return fixedRate;
        }
        
        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            return cancelled.compareAndSet(false, true);
        }
        
        @Override
        public boolean isCancelled() {
            return cancelled.get();
        }
        
        @Override
        public boolean isDone() {
            return isCancelled() || (!isFixedRate() && getPeriod() == 0 && getDelay() == 0 && System.currentTimeMillis() >= nextExecutionTime);
        }
        
        @Override
        public long getDelay(TimeUnit unit) {
            long delay = nextExecutionTime - System.currentTimeMillis();
            return unit.convert(delay, TimeUnit.MILLISECONDS);
        }
        
        @Override
        public int compareTo(ScheduledTaskImpl other) {
            int result = Long.compare(nextExecutionTime, other.nextExecutionTime);
            return result != 0 ? result : Long.compare(sequence, other.sequence);
        }
    }
}

interface ScheduledTask {
    boolean cancel(boolean mayInterruptIfRunning);
    boolean isCancelled();
    boolean isDone();
    long getDelay(TimeUnit unit);
}
```

## Time and Space Complexity

For the ScheduledThreadPoolExecutor implementation:
- Time Complexity: 
  - O(log n) for scheduling operations, where n is the number of tasks
  - O(1) for shutdown, isShutdown, isTerminated operations
  - O(n) for shutdownNow operation, where n is the number of tasks
- Space Complexity: O(n) where n is the number of scheduled tasks

For the Timer and TimerTask implementation:
- Time Complexity: 
  - O(log n) for scheduling operations, where n is the number of tasks
  - O(1) for shutdown, isShutdown, isTerminated operations
  - O(n) for shutdownNow operation, where n is the number of tasks
- Space Complexity: O(n) where n is the number of scheduled tasks

For the Custom Implementation with PriorityQueue:
- Time Complexity: 
  - O(log n) for scheduling operations, where n is the number of tasks
  - O(1) for shutdown, isShutdown, isTerminated operations
  - O(n) for shutdownNow operation, where n is the number of tasks
- Space Complexity: O(n) where n is the number of scheduled tasks

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe scheduler.
2. The ScheduledThreadPoolExecutor approach leverages Java's built-in implementation, which is already thread-safe and well-optimized.
3. The Timer and TimerTask approach uses Java's older scheduling mechanism, which is simpler but less flexible.
4. The Custom Implementation with PriorityQueue approach gives more control over the scheduling algorithm and task execution.
5. All implementations must handle the case where multiple threads try to schedule tasks concurrently.
6. The key challenge is ensuring that tasks are executed at the correct times and that periodic tasks are rescheduled appropriately.

## Interview Tips

- Be prepared to discuss the trade-offs between different scheduler implementations.
- Understand the performance characteristics of ScheduledThreadPoolExecutor vs. custom implementations.
- Know when to use ScheduledThreadPoolExecutor (most real-world scenarios) and when to use custom implementations.
- Be able to explain the difference between scheduleAtFixedRate and scheduleWithFixedDelay.
- Consider edge cases like what happens if a task takes longer to execute than its period.
- Discuss how the solution would scale with a large number of tasks and threads.
- Mention that in a real-world scenario, you would need to consider memory consistency, thread interruption, and potential deadlocks.
- Explain the difference between a scheduler and a thread pool, and how they work together.


### 38. Thread-Safe Barrier (Medium)


## Problem Description

Implement a thread-safe barrier that allows multiple threads to wait until all threads have reached a common synchronization point:

* `ThreadSafeBarrier(int parties)` Initializes a barrier that waits for the given number of parties (threads).
* `int await()` Blocks until all parties have invoked await on this barrier. Returns the arrival index for each thread, where index 0 indicates the first to arrive and index (parties-1) indicates the last to arrive.
* `int await(long timeout, TimeUnit unit)` Blocks until all parties have invoked await on this barrier or the specified timeout elapses. Returns the arrival index if the barrier is tripped, or a negative value if the specified timeout elapses.
* `void reset()` Resets the barrier to its initial state.
* `boolean isBroken()` Queries if this barrier is in a broken state.
* `int getNumberWaiting()` Returns the number of parties currently waiting at the barrier.
* `int getParties()` Returns the number of parties required to trip this barrier.

The barrier implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can call await concurrently.
3. The implementation should be efficient and minimize contention between threads.
4. The implementation should support barrier reuse after all threads have passed.
5. The implementation should handle timeout and interruption gracefully.

**Example 1:**
```
Input: 
["ThreadSafeBarrier", "getParties", "getNumberWaiting", "await", "getNumberWaiting", "await", "getNumberWaiting"]
[[2], [], [], [], [], [], []]

Output: 
[null, 2, 0, 0, 1, 1, 0]

Explanation:
ThreadSafeBarrier barrier = new ThreadSafeBarrier(2);  // Initialize a barrier for 2 threads
barrier.getParties();                                  // Return 2 (number of parties required)
barrier.getNumberWaiting();                            // Return 0 (no threads waiting)
// Thread 1 calls await
barrier.await();                                       // Return 0 (first arrival index), thread blocks
barrier.getNumberWaiting();                            // Return 1 (one thread waiting)
// Thread 2 calls await
barrier.await();                                       // Return 1 (second arrival index), barrier trips, both threads continue
barrier.getNumberWaiting();                            // Return 0 (no threads waiting)
```

**Example 2:**
```
Input: 
["ThreadSafeBarrier", "await", "await", "reset", "isBroken", "await", "await"]
[[2], [], [], [], [], [], []]

Output: 
[null, 0, 1, null, false, 0, 1]

Explanation:
ThreadSafeBarrier barrier = new ThreadSafeBarrier(2);  // Initialize a barrier for 2 threads
// Thread 1 calls await
barrier.await();                                       // Return 0 (first arrival index), thread blocks
// Thread 2 calls await
barrier.await();                                       // Return 1 (second arrival index), barrier trips, both threads continue
barrier.reset();                                       // Reset the barrier to its initial state
barrier.isBroken();                                    // Return false (barrier is not broken)
// Thread 1 calls await again
barrier.await();                                       // Return 0 (first arrival index), thread blocks
// Thread 2 calls await again
barrier.await();                                       // Return 1 (second arrival index), barrier trips, both threads continue
```

## Video Explanation
[Java Concurrency: CyclicBarrier Implementation and Usage (7:15 minutes)](https://www.youtube.com/watch?v=kvoMWqq7jJE)

## Solution Approach

This problem tests your understanding of thread synchronization and barrier patterns. There are several ways to solve it:

### 1. Using Java's Built-in CyclicBarrier

```java
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

class ThreadSafeBarrier {
    private final CyclicBarrier barrier;
    private final AtomicInteger waiting;
    private final int parties;
    
    public ThreadSafeBarrier(int parties) {
        this.barrier = new CyclicBarrier(parties);
        this.waiting = new AtomicInteger(0);
        this.parties = parties;
    }
    
    public int await() throws InterruptedException, BrokenBarrierException {
        waiting.incrementAndGet();
        try {
            return parties - barrier.await() - 1;
        } finally {
            waiting.decrementAndGet();
        }
    }
    
    public int await(long timeout, TimeUnit unit) throws InterruptedException, BrokenBarrierException, TimeoutException {
        waiting.incrementAndGet();
        try {
            return parties - barrier.await(timeout, unit) - 1;
        } finally {
            waiting.decrementAndGet();
        }
    }
    
    public void reset() {
        barrier.reset();
    }
    
    public boolean isBroken() {
        return barrier.isBroken();
    }
    
    public int getNumberWaiting() {
        return waiting.get();
    }
    
    public int getParties() {
        return parties;
    }
}
```

### 2. Using CountDownLatch and ReentrantLock

```java
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class ThreadSafeBarrier {
    private final int parties;
    private final ReentrantLock lock;
    private final Condition condition;
    private CountDownLatch latch;
    private final AtomicInteger waiting;
    private final AtomicInteger count;
    private final AtomicInteger generation;
    private final AtomicBoolean broken;
    
    public ThreadSafeBarrier(int parties) {
        this.parties = parties;
        this.lock = new ReentrantLock();
        this.condition = lock.newCondition();
        this.latch = new CountDownLatch(parties);
        this.waiting = new AtomicInteger(0);
        this.count = new AtomicInteger(parties);
        this.generation = new AtomicInteger(0);
        this.broken = new AtomicBoolean(false);
    }
    
    public int await() throws InterruptedException {
        lock.lock();
        try {
            final int currentGeneration = generation.get();
            
            if (broken.get()) {
                throw new InterruptedException("Barrier is broken");
            }
            
            int index = parties - count.decrementAndGet() - 1;
            waiting.incrementAndGet();
            
            if (count.get() == 0) {
                // Last thread to arrive
                nextGeneration();
                return index;
            }
            
            // Wait for all threads to arrive
            while (currentGeneration == generation.get() && !broken.get()) {
                condition.await();
            }
            
            if (broken.get()) {
                throw new InterruptedException("Barrier is broken");
            }
            
            return index;
        } finally {
            waiting.decrementAndGet();
            lock.unlock();
        }
    }
    
    public int await(long timeout, TimeUnit unit) throws InterruptedException {
        lock.lock();
        try {
            final int currentGeneration = generation.get();
            
            if (broken.get()) {
                throw new InterruptedException("Barrier is broken");
            }
            
            int index = parties - count.decrementAndGet() - 1;
            waiting.incrementAndGet();
            
            if (count.get() == 0) {
                // Last thread to arrive
                nextGeneration();
                return index;
            }
            
            // Wait for all threads to arrive or timeout
            long nanos = unit.toNanos(timeout);
            while (currentGeneration == generation.get() && !broken.get() && nanos > 0) {
                nanos = condition.awaitNanos(nanos);
            }
            
            if (currentGeneration == generation.get()) {
                // Timeout or broken
                broken.set(true);
                count.set(parties);
                condition.signalAll();
                return -1;
            }
            
            return index;
        } finally {
            waiting.decrementAndGet();
            lock.unlock();
        }
    }
    
    public void reset() {
        lock.lock();
        try {
            broken.set(false);
            count.set(parties);
            generation.incrementAndGet();
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
    
    public boolean isBroken() {
        return broken.get();
    }
    
    public int getNumberWaiting() {
        return waiting.get();
    }
    
    public int getParties() {
        return parties;
    }
    
    private void nextGeneration() {
        count.set(parties);
        generation.incrementAndGet();
        condition.signalAll();
    }
}
```

### 3. Using Phaser

```java
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

class ThreadSafeBarrier {
    private final Phaser phaser;
    private final AtomicInteger waiting;
    private final int parties;
    
    public ThreadSafeBarrier(int parties) {
        this.phaser = new Phaser(parties);
        this.waiting = new AtomicInteger(0);
        this.parties = parties;
    }
    
    public int await() throws InterruptedException {
        waiting.incrementAndGet();
        try {
            int phase = phaser.getPhase();
            int arrivalIndex = parties - phaser.arriveAndAwaitAdvance() - 1;
            if (arrivalIndex < 0) {
                throw new InterruptedException("Barrier is broken");
            }
            return arrivalIndex;
        } finally {
            waiting.decrementAndGet();
        }
    }
    
    public int await(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException {
        waiting.incrementAndGet();
        try {
            int phase = phaser.getPhase();
            long deadline = System.nanoTime() + unit.toNanos(timeout);
            int arrivalIndex = parties - phaser.getRegisteredParties();
            phaser.arrive();
            
            while (phaser.getPhase() == phase) {
                long remaining = deadline - System.nanoTime();
                if (remaining <= 0) {
                    phaser.forceTermination();
                    throw new TimeoutException();
                }
                Thread.sleep(Math.min(100, TimeUnit.NANOSECONDS.toMillis(remaining)));
            }
            
            return arrivalIndex;
        } finally {
            waiting.decrementAndGet();
        }
    }
    
    public void reset() {
        phaser.forceTermination();
        Phaser newPhaser = new Phaser(parties);
        phaser = newPhaser;
    }
    
    public boolean isBroken() {
        return phaser.isTerminated();
    }
    
    public int getNumberWaiting() {
        return waiting.get();
    }
    
    public int getParties() {
        return parties;
    }
}
```

## Time and Space Complexity

For the CyclicBarrier implementation:
- Time Complexity: 
  - O(1) for getParties, getNumberWaiting, isBroken operations
  - O(n) for await operation, where n is the number of threads waiting
  - O(n) for reset operation, where n is the number of threads waiting
- Space Complexity: O(n) where n is the number of threads

For the CountDownLatch and ReentrantLock implementation:
- Time Complexity: 
  - O(1) for getParties, getNumberWaiting, isBroken operations
  - O(n) for await operation, where n is the number of threads waiting
  - O(n) for reset operation, where n is the number of threads waiting
- Space Complexity: O(n) where n is the number of threads

For the Phaser implementation:
- Time Complexity: 
  - O(1) for getParties, getNumberWaiting, isBroken operations
  - O(n) for await operation, where n is the number of threads waiting
  - O(n) for reset operation, where n is the number of threads waiting
- Space Complexity: O(n) where n is the number of threads

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe barrier.
2. The CyclicBarrier approach leverages Java's built-in implementation, which is already thread-safe and well-optimized.
3. The CountDownLatch and ReentrantLock approach gives more control over the barrier's behavior, but requires careful synchronization.
4. The Phaser approach provides a more flexible alternative to CyclicBarrier, with support for dynamic party registration and deregistration.
5. All implementations must handle the case where multiple threads arrive at the barrier concurrently.
6. The key challenge is ensuring that all threads are released only when the required number of threads have arrived at the barrier.

## Interview Tips

- Be prepared to discuss the trade-offs between different barrier implementations.
- Understand the performance characteristics of CyclicBarrier vs. custom implementations.
- Know when to use CyclicBarrier (most real-world scenarios) and when to use custom implementations.
- Be able to explain the difference between a barrier and a latch.
- Consider edge cases like what happens if a thread is interrupted while waiting at the barrier.
- Discuss how the solution would scale with a large number of threads.
- Mention that in a real-world scenario, you would need to consider memory consistency, thread interruption, and potential deadlocks.
- Explain the concept of a generation in a barrier, which helps distinguish between different cycles of barrier usage.
- Highlight that Java's CyclicBarrier allows executing a runnable action when the barrier is tripped, which can be useful for coordination tasks.


### 39. Thread-Safe Lock-Free Queue (Hard)


## Problem Description

Implement a thread-safe lock-free queue that allows multiple threads to enqueue and dequeue elements concurrently without using locks:

* `ThreadSafeLockFreeQueue()` Initializes an empty lock-free queue.
* `void enqueue(T item)` Adds an item to the end of the queue.
* `T dequeue()` Removes and returns the item at the front of the queue. Returns null if the queue is empty.
* `T peek()` Returns the item at the front of the queue without removing it. Returns null if the queue is empty.
* `boolean isEmpty()` Returns true if the queue is empty, false otherwise.
* `int size()` Returns the number of items in the queue.

The lock-free queue implementation should follow these rules:
1. All operations must be thread-safe without using locks (e.g., synchronized, ReentrantLock).
2. Multiple threads can enqueue and dequeue concurrently.
3. The implementation should be efficient and minimize contention between threads.
4. The implementation should handle the ABA problem.
5. The implementation should be linearizable (operations appear to take effect atomically at some point between their invocation and completion).

**Example 1:**
```
Input: 
["ThreadSafeLockFreeQueue", "isEmpty", "enqueue", "isEmpty", "peek", "dequeue", "isEmpty"]
[[], [], [1], [], [], [], []]

Output: 
[null, true, null, false, 1, 1, true]

Explanation:
ThreadSafeLockFreeQueue<Integer> queue = new ThreadSafeLockFreeQueue<>();  // Initialize an empty queue
queue.isEmpty();                                                           // Return true (queue is empty)
queue.enqueue(1);                                                          // Add 1 to the queue
queue.isEmpty();                                                           // Return false (queue is not empty)
queue.peek();                                                              // Return 1 (front of the queue)
queue.dequeue();                                                           // Remove and return 1
queue.isEmpty();                                                           // Return true (queue is empty)
```

**Example 2:**
```
Input: 
["ThreadSafeLockFreeQueue", "enqueue", "enqueue", "enqueue", "size", "dequeue", "dequeue", "dequeue", "dequeue"]
[[], [1], [2], [3], [], [], [], [], []]

Output: 
[null, null, null, null, 3, 1, 2, 3, null]

Explanation:
ThreadSafeLockFreeQueue<Integer> queue = new ThreadSafeLockFreeQueue<>();  // Initialize an empty queue
queue.enqueue(1);                                                          // Add 1 to the queue
queue.enqueue(2);                                                          // Add 2 to the queue
queue.enqueue(3);                                                          // Add 3 to the queue
queue.size();                                                              // Return 3 (queue has 3 items)
queue.dequeue();                                                           // Remove and return 1
queue.dequeue();                                                           // Remove and return 2
queue.dequeue();                                                           // Remove and return 3
queue.dequeue();                                                           // Return null (queue is empty)
```

## Video Explanation
[Java Concurrency: Lock-Free Queue Implementation (9:15 minutes)](https://www.youtube.com/watch?v=lock-free-queue-java)

## Solution Approach

This problem tests your understanding of lock-free programming and atomic operations. There are several ways to solve it:

### 1. Using AtomicReference and Compare-and-Swap (CAS) Operations

```java
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

class ThreadSafeLockFreeQueue<T> {
    private static class Node<T> {
        final T item;
        final AtomicReference<Node<T>> next;
        
        Node(T item) {
            this.item = item;
            this.next = new AtomicReference<>(null);
        }
        
        Node() {
            this.item = null;
            this.next = new AtomicReference<>(null);
        }
    }
    
    private final AtomicReference<Node<T>> head;
    private final AtomicReference<Node<T>> tail;
    private final AtomicInteger size;
    
    public ThreadSafeLockFreeQueue() {
        Node<T> dummy = new Node<>();
        head = new AtomicReference<>(dummy);
        tail = new AtomicReference<>(dummy);
        size = new AtomicInteger(0);
    }
    
    public void enqueue(T item) {
        Node<T> newNode = new Node<>(item);
        while (true) {
            Node<T> curTail = tail.get();
            Node<T> tailNext = curTail.next.get();
            
            if (curTail == tail.get()) {
                if (tailNext != null) {
                    // Queue in intermediate state, help complete the previous enqueue
                    tail.compareAndSet(curTail, tailNext);
                } else {
                    // Try to link new node at the end of the list
                    if (curTail.next.compareAndSet(null, newNode)) {
                        // Enqueue is done, try to swing tail to the new node
                        tail.compareAndSet(curTail, newNode);
                        size.incrementAndGet();
                        return;
                    }
                }
            }
        }
    }
    
    public T dequeue() {
        while (true) {
            Node<T> curHead = head.get();
            Node<T> curTail = tail.get();
            Node<T> headNext = curHead.next.get();
            
            if (curHead == head.get()) {
                if (curHead == curTail) {
                    if (headNext == null) {
                        // Queue is empty
                        return null;
                    }
                    // Queue in intermediate state, help complete the previous enqueue
                    tail.compareAndSet(curTail, headNext);
                } else {
                    T item = headNext.item;
                    if (head.compareAndSet(curHead, headNext)) {
                        size.decrementAndGet();
                        return item;
                    }
                }
            }
        }
    }
    
    public T peek() {
        while (true) {
            Node<T> curHead = head.get();
            Node<T> curTail = tail.get();
            Node<T> headNext = curHead.next.get();
            
            if (curHead == head.get()) {
                if (curHead == curTail) {
                    if (headNext == null) {
                        // Queue is empty
                        return null;
                    }
                    // Queue in intermediate state, help complete the previous enqueue
                    tail.compareAndSet(curTail, headNext);
                } else {
                    return headNext.item;
                }
            }
        }
    }
    
    public boolean isEmpty() {
        Node<T> curHead = head.get();
        Node<T> curTail = tail.get();
        return curHead == curTail && curHead.next.get() == null;
    }
    
    public int size() {
        return size.get();
    }
}
```

### 2. Using AtomicStampedReference to Solve the ABA Problem

```java
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicStampedReference;

class ThreadSafeLockFreeQueue<T> {
    private static class Node<T> {
        final T item;
        final AtomicStampedReference<Node<T>> next;
        
        Node(T item) {
            this.item = item;
            this.next = new AtomicStampedReference<>(null, 0);
        }
        
        Node() {
            this.item = null;
            this.next = new AtomicStampedReference<>(null, 0);
        }
    }
    
    private final AtomicStampedReference<Node<T>> head;
    private final AtomicStampedReference<Node<T>> tail;
    private final AtomicInteger size;
    
    public ThreadSafeLockFreeQueue() {
        Node<T> dummy = new Node<>();
        head = new AtomicStampedReference<>(dummy, 0);
        tail = new AtomicStampedReference<>(dummy, 0);
        size = new AtomicInteger(0);
    }
    
    public void enqueue(T item) {
        Node<T> newNode = new Node<>(item);
        while (true) {
            int[] tailStamp = new int[1];
            Node<T> curTail = tail.get(tailStamp);
            int[] nextStamp = new int[1];
            Node<T> tailNext = curTail.next.get(nextStamp);
            
            if (curTail == tail.get(new int[1])) {
                if (tailNext != null) {
                    // Queue in intermediate state, help complete the previous enqueue
                    tail.compareAndSet(curTail, tailNext, tailStamp[0], tailStamp[0] + 1);
                } else {
                    // Try to link new node at the end of the list
                    if (curTail.next.compareAndSet(null, newNode, nextStamp[0], nextStamp[0] + 1)) {
                        // Enqueue is done, try to swing tail to the new node
                        tail.compareAndSet(curTail, newNode, tailStamp[0], tailStamp[0] + 1);
                        size.incrementAndGet();
                        return;
                    }
                }
            }
        }
    }
    
    public T dequeue() {
        while (true) {
            int[] headStamp = new int[1];
            Node<T> curHead = head.get(headStamp);
            int[] tailStamp = new int[1];
            Node<T> curTail = tail.get(tailStamp);
            int[] nextStamp = new int[1];
            Node<T> headNext = curHead.next.get(nextStamp);
            
            if (curHead == head.get(new int[1])) {
                if (curHead == curTail) {
                    if (headNext == null) {
                        // Queue is empty
                        return null;
                    }
                    // Queue in intermediate state, help complete the previous enqueue
                    tail.compareAndSet(curTail, headNext, tailStamp[0], tailStamp[0] + 1);
                } else {
                    T item = headNext.item;
                    if (head.compareAndSet(curHead, headNext, headStamp[0], headStamp[0] + 1)) {
                        size.decrementAndGet();
                        return item;
                    }
                }
            }
        }
    }
    
    public T peek() {
        while (true) {
            int[] headStamp = new int[1];
            Node<T> curHead = head.get(headStamp);
            int[] tailStamp = new int[1];
            Node<T> curTail = tail.get(tailStamp);
            int[] nextStamp = new int[1];
            Node<T> headNext = curHead.next.get(nextStamp);
            
            if (curHead == head.get(new int[1])) {
                if (curHead == curTail) {
                    if (headNext == null) {
                        // Queue is empty
                        return null;
                    }
                    // Queue in intermediate state, help complete the previous enqueue
                    tail.compareAndSet(curTail, headNext, tailStamp[0], tailStamp[0] + 1);
                } else {
                    return headNext.item;
                }
            }
        }
    }
    
    public boolean isEmpty() {
        int[] headStamp = new int[1];
        Node<T> curHead = head.get(headStamp);
        int[] tailStamp = new int[1];
        Node<T> curTail = tail.get(tailStamp);
        return curHead == curTail && curHead.next.get(new int[1]) == null;
    }
    
    public int size() {
        return size.get();
    }
}
```

### 3. Using Java's ConcurrentLinkedQueue

```java
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

class ThreadSafeLockFreeQueue<T> {
    private final ConcurrentLinkedQueue<T> queue;
    private final AtomicInteger size;
    
    public ThreadSafeLockFreeQueue() {
        this.queue = new ConcurrentLinkedQueue<>();
        this.size = new AtomicInteger(0);
    }
    
    public void enqueue(T item) {
        queue.offer(item);
        size.incrementAndGet();
    }
    
    public T dequeue() {
        T item = queue.poll();
        if (item != null) {
            size.decrementAndGet();
        }
        return item;
    }
    
    public T peek() {
        return queue.peek();
    }
    
    public boolean isEmpty() {
        return queue.isEmpty();
    }
    
    public int size() {
        return size.get();
    }
}
```

## Time and Space Complexity

For the AtomicReference and CAS Operations implementation:
- Time Complexity: 
  - O(1) for enqueue, dequeue, peek, isEmpty, and size operations in the average case
  - O(n) in the worst case due to contention, where n is the number of concurrent threads
- Space Complexity: O(n) where n is the number of elements in the queue

For the AtomicStampedReference implementation:
- Time Complexity: 
  - O(1) for enqueue, dequeue, peek, isEmpty, and size operations in the average case
  - O(n) in the worst case due to contention, where n is the number of concurrent threads
- Space Complexity: O(n) where n is the number of elements in the queue

For the ConcurrentLinkedQueue implementation:
- Time Complexity: O(1) for all operations
- Space Complexity: O(n) where n is the number of elements in the queue

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe lock-free queue.
2. The AtomicReference and CAS Operations approach provides a lock-free implementation using atomic compare-and-swap operations.
3. The AtomicStampedReference approach addresses the ABA problem by using a version stamp along with the reference.
4. The ConcurrentLinkedQueue approach leverages Java's built-in lock-free queue implementation, which is already optimized for concurrent access.
5. The key challenge is ensuring that multiple threads can enqueue and dequeue concurrently without using locks.
6. The implementation must handle the case where multiple threads try to modify the queue simultaneously.

## Interview Tips

- Be prepared to discuss the trade-offs between different lock-free queue implementations.
- Understand the performance characteristics of lock-free vs. lock-based synchronization.
- Know when to use a lock-free queue (e.g., in high-contention scenarios with many threads).
- Be able to explain how CAS operations work and their advantages over locks.
- Consider edge cases like what happens if multiple threads try to dequeue the last element simultaneously.
- Discuss how the solution would scale with a large number of threads and elements.
- Mention that in a real-world scenario, you would need to consider memory consistency, thread interruption, and potential ABA problems.
- Explain the difference between a lock-free queue and a wait-free queue, and when to use each.
- Highlight that Java's ConcurrentLinkedQueue is a lock-free implementation that uses CAS operations internally.


### 40. Thread-Safe SkipList (Hard)


## Problem Description

Implement a thread-safe skip list that allows multiple threads to add, remove, and search for elements concurrently:

* `ThreadSafeSkipList()` Initializes an empty thread-safe skip list.
* `boolean add(T item)` Adds an item to the skip list if it's not already present. Returns true if the item was added, false otherwise.
* `boolean remove(T item)` Removes an item from the skip list if it's present. Returns true if the item was removed, false otherwise.
* `boolean contains(T item)` Returns true if the item is in the skip list, false otherwise.
* `T findFirst()` Returns the first (smallest) item in the skip list, or null if the skip list is empty.
* `T findLast()` Returns the last (largest) item in the skip list, or null if the skip list is empty.
* `int size()` Returns the number of items in the skip list.

The thread-safe skip list implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can add, remove, and search concurrently.
3. The implementation should be efficient and minimize contention between threads.
4. The implementation should maintain the skip list invariants (sorted order and probabilistic balancing).
5. The implementation should be linearizable (operations appear to take effect atomically at some point between their invocation and completion).

**Example 1:**
```
Input: 
["ThreadSafeSkipList", "size", "add", "add", "contains", "size", "remove", "contains", "size"]
[[], [], [1], [2], [1], [], [1], [1], []]

Output: 
[null, 0, true, true, true, 2, true, false, 1]

Explanation:
ThreadSafeSkipList<Integer> skipList = new ThreadSafeSkipList<>();  // Initialize an empty skip list
skipList.size();                                                     // Return 0 (skip list is empty)
skipList.add(1);                                                     // Add 1 to the skip list, return true
skipList.add(2);                                                     // Add 2 to the skip list, return true
skipList.contains(1);                                                // Return true (1 is in the skip list)
skipList.size();                                                     // Return 2 (skip list has 2 items)
skipList.remove(1);                                                  // Remove 1 from the skip list, return true
skipList.contains(1);                                                // Return false (1 is not in the skip list)
skipList.size();                                                     // Return 1 (skip list has 1 item)
```

**Example 2:**
```
Input: 
["ThreadSafeSkipList", "add", "add", "add", "add", "add", "findFirst", "findLast", "remove", "remove", "findFirst", "findLast"]
[[], [5], [1], [3], [7], [2], [], [], [1], [7], [], []]

Output: 
[null, true, true, true, true, true, 1, 7, true, true, 2, 5]

Explanation:
ThreadSafeSkipList<Integer> skipList = new ThreadSafeSkipList<>();  // Initialize an empty skip list
skipList.add(5);                                                     // Add 5 to the skip list, return true
skipList.add(1);                                                     // Add 1 to the skip list, return true
skipList.add(3);                                                     // Add 3 to the skip list, return true
skipList.add(7);                                                     // Add 7 to the skip list, return true
skipList.add(2);                                                     // Add 2 to the skip list, return true
skipList.findFirst();                                                // Return 1 (smallest item)
skipList.findLast();                                                 // Return 7 (largest item)
skipList.remove(1);                                                  // Remove 1 from the skip list, return true
skipList.remove(7);                                                  // Remove 7 from the skip list, return true
skipList.findFirst();                                                // Return 2 (new smallest item)
skipList.findLast();                                                 // Return 5 (new largest item)
```

## Video Explanation
[Java Concurrency: ConcurrentSkipListMap and ConcurrentSkipListSet (8:15 minutes)](https://www.youtube.com/watch?v=concurrent-skiplist-java)

## Solution Approach

This problem tests your understanding of concurrent data structures and skip lists. There are several ways to solve it:

### 1. Using Java's ConcurrentSkipListSet

```java
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

class ThreadSafeSkipList<T extends Comparable<? super T>> {
    private final ConcurrentSkipListSet<T> skipList;
    private final AtomicInteger size;
    
    public ThreadSafeSkipList() {
        this.skipList = new ConcurrentSkipListSet<>();
        this.size = new AtomicInteger(0);
    }
    
    public boolean add(T item) {
        boolean added = skipList.add(item);
        if (added) {
            size.incrementAndGet();
        }
        return added;
    }
    
    public boolean remove(T item) {
        boolean removed = skipList.remove(item);
        if (removed) {
            size.decrementAndGet();
        }
        return removed;
    }
    
    public boolean contains(T item) {
        return skipList.contains(item);
    }
    
    public T findFirst() {
        try {
            return skipList.first();
        } catch (Exception e) {
            return null;
        }
    }
    
    public T findLast() {
        try {
            return skipList.last();
        } catch (Exception e) {
            return null;
        }
    }
    
    public int size() {
        return size.get();
    }
}
```

### 2. Using Java's ConcurrentSkipListMap

```java
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicInteger;

class ThreadSafeSkipList<T extends Comparable<? super T>> {
    private final ConcurrentSkipListMap<T, Boolean> skipList;
    private final AtomicInteger size;
    
    public ThreadSafeSkipList() {
        this.skipList = new ConcurrentSkipListMap<>();
        this.size = new AtomicInteger(0);
    }
    
    public boolean add(T item) {
        Boolean previous = skipList.putIfAbsent(item, Boolean.TRUE);
        if (previous == null) {
            size.incrementAndGet();
            return true;
        }
        return false;
    }
    
    public boolean remove(T item) {
        Boolean removed = skipList.remove(item);
        if (removed != null) {
            size.decrementAndGet();
            return true;
        }
        return false;
    }
    
    public boolean contains(T item) {
        return skipList.containsKey(item);
    }
    
    public T findFirst() {
        try {
            return skipList.firstKey();
        } catch (Exception e) {
            return null;
        }
    }
    
    public T findLast() {
        try {
            return skipList.lastKey();
        } catch (Exception e) {
            return null;
        }
    }
    
    public int size() {
        return size.get();
    }
}
```

### 3. Custom Implementation with Fine-Grained Locking

```java
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

class ThreadSafeSkipList<T extends Comparable<? super T>> {
    private static final int MAX_LEVEL = 16;
    private static final double P = 0.5;
    
    private static class Node<T> {
        final T item;
        final Node<T>[] next;
        final ReentrantLock lock;
        
        @SuppressWarnings("unchecked")
        Node(T item, int level) {
            this.item = item;
            this.next = (Node<T>[]) new Node[level + 1];
            this.lock = new ReentrantLock();
        }
    }
    
    private final Node<T> head;
    private final Random random;
    private final AtomicInteger size;
    
    public ThreadSafeSkipList() {
        this.head = new Node<>(null, MAX_LEVEL);
        this.random = new Random();
        this.size = new AtomicInteger(0);
    }
    
    private int randomLevel() {
        int level = 0;
        while (level < MAX_LEVEL && random.nextDouble() < P) {
            level++;
        }
        return level;
    }
    
    public boolean add(T item) {
        if (item == null) {
            throw new NullPointerException();
        }
        
        Node<T>[] update = new Node[MAX_LEVEL + 1];
        Node<T> current = head;
        
        // Find position to insert
        for (int i = MAX_LEVEL; i >= 0; i--) {
            while (current.next[i] != null && current.next[i].item.compareTo(item) < 0) {
                current = current.next[i];
            }
            update[i] = current;
        }
        
        // Check if item already exists
        current = current.next[0];
        if (current != null && current.item.compareTo(item) == 0) {
            return false;
        }
        
        // Insert new node
        int level = randomLevel();
        Node<T> newNode = new Node<>(item, level);
        
        // Lock all affected nodes
        for (int i = 0; i <= level; i++) {
            update[i].lock.lock();
        }
        
        try {
            // Double-check if item already exists
            current = update[0].next[0];
            if (current != null && current.item.compareTo(item) == 0) {
                return false;
            }
            
            // Insert at each level
            for (int i = 0; i <= level; i++) {
                newNode.next[i] = update[i].next[i];
                update[i].next[i] = newNode;
            }
            
            size.incrementAndGet();
            return true;
        } finally {
            // Release all locks
            for (int i = 0; i <= level; i++) {
                update[i].lock.unlock();
            }
        }
    }
    
    public boolean remove(T item) {
        if (item == null) {
            throw new NullPointerException();
        }
        
        Node<T>[] update = new Node[MAX_LEVEL + 1];
        Node<T> current = head;
        
        // Find position to remove
        for (int i = MAX_LEVEL; i >= 0; i--) {
            while (current.next[i] != null && current.next[i].item.compareTo(item) < 0) {
                current = current.next[i];
            }
            update[i] = current;
        }
        
        // Check if item exists
        current = current.next[0];
        if (current == null || current.item.compareTo(item) != 0) {
            return false;
        }
        
        // Lock all affected nodes
        for (int i = 0; i <= MAX_LEVEL; i++) {
            update[i].lock.lock();
        }
        
        try {
            // Double-check if item exists
            current = update[0].next[0];
            if (current == null || current.item.compareTo(item) != 0) {
                return false;
            }
            
            // Remove at each level
            for (int i = 0; i <= MAX_LEVEL; i++) {
                if (update[i].next[i] == current) {
                    update[i].next[i] = current.next[i];
                }
            }
            
            size.decrementAndGet();
            return true;
        } finally {
            // Release all locks
            for (int i = 0; i <= MAX_LEVEL; i++) {
                update[i].lock.unlock();
            }
        }
    }
    
    public boolean contains(T item) {
        if (item == null) {
            throw new NullPointerException();
        }
        
        Node<T> current = head;
        
        // Find position
        for (int i = MAX_LEVEL; i >= 0; i--) {
            while (current.next[i] != null && current.next[i].item.compareTo(item) < 0) {
                current = current.next[i];
            }
        }
        
        // Check if item exists
        current = current.next[0];
        return current != null && current.item.compareTo(item) == 0;
    }
    
    public T findFirst() {
        Node<T> current = head.next[0];
        return current != null ? current.item : null;
    }
    
    public T findLast() {
        Node<T> current = head;
        
        // Find last node at level 0
        for (int i = MAX_LEVEL; i >= 0; i--) {
            while (current.next[i] != null) {
                current = current.next[i];
            }
        }
        
        return current != head ? current.item : null;
    }
    
    public int size() {
        return size.get();
    }
}
```

## Time and Space Complexity

For the ConcurrentSkipListSet implementation:
- Time Complexity: 
  - O(log n) for add, remove, contains, findFirst, and findLast operations
  - O(1) for size operation
- Space Complexity: O(n) where n is the number of elements in the skip list

For the ConcurrentSkipListMap implementation:
- Time Complexity: 
  - O(log n) for add, remove, contains, findFirst, and findLast operations
  - O(1) for size operation
- Space Complexity: O(n) where n is the number of elements in the skip list

For the Custom Implementation with Fine-Grained Locking:
- Time Complexity: 
  - O(log n) for add, remove, contains, findFirst, and findLast operations
  - O(1) for size operation
- Space Complexity: O(n log n) where n is the number of elements in the skip list (due to the multiple levels)

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe skip list.
2. The ConcurrentSkipListSet and ConcurrentSkipListMap approaches leverage Java's built-in thread-safe implementations, which are already optimized for concurrent access.
3. The custom implementation with fine-grained locking provides more control over the skip list's behavior, but requires careful synchronization.
4. The key challenge is ensuring that multiple threads can add, remove, and search concurrently without using coarse-grained locks.
5. The implementation must handle the case where multiple threads try to modify the skip list simultaneously.

## Interview Tips

- Be prepared to discuss the trade-offs between different skip list implementations.
- Understand the performance characteristics of skip lists vs. other sorted data structures (e.g., balanced trees).
- Know when to use a skip list (e.g., when you need a sorted collection with efficient concurrent access).
- Be able to explain how skip lists work and their probabilistic nature.
- Consider edge cases like what happens if multiple threads try to add or remove the same element simultaneously.
- Discuss how the solution would scale with a large number of threads and elements.
- Mention that in a real-world scenario, you would likely use Java's ConcurrentSkipListSet or ConcurrentSkipListMap rather than implementing your own.
- Explain the difference between a lock-based and a lock-free skip list implementation.
- Highlight that Java's ConcurrentSkipListSet and ConcurrentSkipListMap are lock-free implementations that use CAS operations internally.


### 41. Thread-Safe Concurrent HashSet (Medium)


## Problem Description

Implement a thread-safe concurrent hash set that allows multiple threads to add, remove, and check for elements concurrently:

* `ThreadSafeConcurrentHashSet()` Initializes an empty thread-safe concurrent hash set.
* `boolean add(T item)` Adds an item to the set if it's not already present. Returns true if the item was added, false otherwise.
* `boolean remove(T item)` Removes an item from the set if it's present. Returns true if the item was removed, false otherwise.
* `boolean contains(T item)` Returns true if the item is in the set, false otherwise.
* `int size()` Returns the number of items in the set.
* `boolean isEmpty()` Returns true if the set is empty, false otherwise.
* `void clear()` Removes all items from the set.

The thread-safe concurrent hash set implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can add, remove, and check for elements concurrently.
3. The implementation should be efficient and minimize contention between threads.
4. The implementation should provide high concurrency for read operations.
5. The implementation should be linearizable (operations appear to take effect atomically at some point between their invocation and completion).

**Example 1:**
```
Input: 
["ThreadSafeConcurrentHashSet", "isEmpty", "add", "add", "contains", "size", "remove", "contains", "size"]
[[], [], [1], [2], [1], [], [1], [1], []]

Output: 
[null, true, true, true, true, 2, true, false, 1]

Explanation:
ThreadSafeConcurrentHashSet<Integer> set = new ThreadSafeConcurrentHashSet<>();  // Initialize an empty set
set.isEmpty();                                                                   // Return true (set is empty)
set.add(1);                                                                      // Add 1 to the set, return true
set.add(2);                                                                      // Add 2 to the set, return true
set.contains(1);                                                                 // Return true (1 is in the set)
set.size();                                                                      // Return 2 (set has 2 items)
set.remove(1);                                                                   // Remove 1 from the set, return true
set.contains(1);                                                                 // Return false (1 is not in the set)
set.size();                                                                      // Return 1 (set has 1 item)
```

**Example 2:**
```
Input: 
["ThreadSafeConcurrentHashSet", "add", "add", "add", "add", "add", "size", "clear", "isEmpty", "add", "size"]
[[], [5], [1], [3], [7], [2], [], [], [], [10], []]

Output: 
[null, true, true, true, true, true, 5, null, true, true, 1]

Explanation:
ThreadSafeConcurrentHashSet<Integer> set = new ThreadSafeConcurrentHashSet<>();  // Initialize an empty set
set.add(5);                                                                      // Add 5 to the set, return true
set.add(1);                                                                      // Add 1 to the set, return true
set.add(3);                                                                      // Add 3 to the set, return true
set.add(7);                                                                      // Add 7 to the set, return true
set.add(2);                                                                      // Add 2 to the set, return true
set.size();                                                                      // Return 5 (set has 5 items)
set.clear();                                                                     // Remove all items from the set
set.isEmpty();                                                                   // Return true (set is empty)
set.add(10);                                                                     // Add 10 to the set, return true
set.size();                                                                      // Return 1 (set has 1 item)
```

## Video Explanation
[Java Concurrency: ConcurrentHashMap and Thread-Safe Sets (7:45 minutes)](https://www.youtube.com/watch?v=concurrent-hashset-java)

## Solution Approach

This problem tests your understanding of concurrent data structures and thread-safe collections. There are several ways to solve it:

### 1. Using ConcurrentHashMap.newKeySet()

```java
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class ThreadSafeConcurrentHashSet<T> {
    private final Set<T> set;
    
    public ThreadSafeConcurrentHashSet() {
        this.set = ConcurrentHashMap.newKeySet();
    }
    
    public boolean add(T item) {
        return set.add(item);
    }
    
    public boolean remove(T item) {
        return set.remove(item);
    }
    
    public boolean contains(T item) {
        return set.contains(item);
    }
    
    public int size() {
        return set.size();
    }
    
    public boolean isEmpty() {
        return set.isEmpty();
    }
    
    public void clear() {
        set.clear();
    }
}
```

### 2. Using ConcurrentHashMap with Dummy Values

```java
import java.util.concurrent.ConcurrentHashMap;

class ThreadSafeConcurrentHashSet<T> {
    private final ConcurrentHashMap<T, Boolean> map;
    
    public ThreadSafeConcurrentHashSet() {
        this.map = new ConcurrentHashMap<>();
    }
    
    public boolean add(T item) {
        return map.put(item, Boolean.TRUE) == null;
    }
    
    public boolean remove(T item) {
        return map.remove(item) != null;
    }
    
    public boolean contains(T item) {
        return map.containsKey(item);
    }
    
    public int size() {
        return map.size();
    }
    
    public boolean isEmpty() {
        return map.isEmpty();
    }
    
    public void clear() {
        map.clear();
    }
}
```

### 3. Using CopyOnWriteArraySet

```java
import java.util.concurrent.CopyOnWriteArraySet;

class ThreadSafeConcurrentHashSet<T> {
    private final CopyOnWriteArraySet<T> set;
    
    public ThreadSafeConcurrentHashSet() {
        this.set = new CopyOnWriteArraySet<>();
    }
    
    public boolean add(T item) {
        return set.add(item);
    }
    
    public boolean remove(T item) {
        return set.remove(item);
    }
    
    public boolean contains(T item) {
        return set.contains(item);
    }
    
    public int size() {
        return set.size();
    }
    
    public boolean isEmpty() {
        return set.isEmpty();
    }
    
    public void clear() {
        set.clear();
    }
}
```

### 4. Using Collections.synchronizedSet

```java
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class ThreadSafeConcurrentHashSet<T> {
    private final Set<T> set;
    
    public ThreadSafeConcurrentHashSet() {
        this.set = Collections.synchronizedSet(new HashSet<>());
    }
    
    public boolean add(T item) {
        return set.add(item);
    }
    
    public boolean remove(T item) {
        return set.remove(item);
    }
    
    public boolean contains(T item) {
        return set.contains(item);
    }
    
    public int size() {
        return set.size();
    }
    
    public boolean isEmpty() {
        return set.isEmpty();
    }
    
    public void clear() {
        set.clear();
    }
}
```

## Time and Space Complexity

For the ConcurrentHashMap.newKeySet() implementation:
- Time Complexity: 
  - O(1) for add, remove, contains, size, isEmpty, and clear operations in the average case
  - O(n) in the worst case for add and remove operations, where n is the number of elements in the set
- Space Complexity: O(n) where n is the number of elements in the set

For the ConcurrentHashMap with Dummy Values implementation:
- Time Complexity: 
  - O(1) for add, remove, contains, size, isEmpty, and clear operations in the average case
  - O(n) in the worst case for add and remove operations, where n is the number of elements in the set
- Space Complexity: O(n) where n is the number of elements in the set

For the CopyOnWriteArraySet implementation:
- Time Complexity: 
  - O(n) for add, remove, and clear operations, where n is the number of elements in the set
  - O(1) for contains, size, and isEmpty operations
- Space Complexity: O(n) where n is the number of elements in the set

For the Collections.synchronizedSet implementation:
- Time Complexity: 
  - O(1) for add, remove, contains, size, isEmpty, and clear operations in the average case
  - O(n) in the worst case for add and remove operations, where n is the number of elements in the set
- Space Complexity: O(n) where n is the number of elements in the set

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe concurrent hash set.
2. The ConcurrentHashMap.newKeySet() approach leverages Java's built-in thread-safe map implementation to create a set.
3. The ConcurrentHashMap with Dummy Values approach uses a map to simulate a set by using dummy values.
4. The CopyOnWriteArraySet approach provides a thread-safe set implementation that creates a new copy of the underlying array for each modification operation.
5. The Collections.synchronizedSet approach wraps a standard HashSet with synchronization, but it uses coarse-grained locking.
6. The key challenge is ensuring that multiple threads can add, remove, and check for elements concurrently without using coarse-grained locks.

## Interview Tips

- Be prepared to discuss the trade-offs between different thread-safe set implementations.
- Understand the performance characteristics of each implementation, especially for read-heavy vs. write-heavy workloads.
- Know when to use each implementation:
  - ConcurrentHashMap.newKeySet() or ConcurrentHashMap with Dummy Values: For general-purpose concurrent sets with good read and write performance.
  - CopyOnWriteArraySet: For read-heavy workloads with infrequent modifications.
  - Collections.synchronizedSet: For simple thread safety with less concurrency.
- Be able to explain why there is no direct ConcurrentHashSet class in Java (it's because you can use ConcurrentHashMap.newKeySet() instead).
- Consider edge cases like what happens if multiple threads try to add or remove the same element simultaneously.
- Discuss how the solution would scale with a large number of threads and elements.
- Mention that in a real-world scenario, you would likely use ConcurrentHashMap.newKeySet() rather than implementing your own thread-safe set.
- Explain the difference between thread-safe and concurrent collections, and when to use each.


### 42. Thread-Safe Lazy Initialization (Medium)


## Problem Description

Implement a thread-safe lazy initialization pattern that allows for efficient, on-demand creation of an expensive resource:

* `ThreadSafeLazyInitializer(Supplier<T> supplier)` Initializes a lazy initializer with a supplier function that creates the resource.
* `T get()` Returns the resource, creating it if it doesn't exist yet. If multiple threads call this method simultaneously before the resource is created, only one thread should create the resource, and all threads should get the same instance.
* `boolean isInitialized()` Returns true if the resource has been initialized, false otherwise.
* `void reset()` Resets the initializer, causing the resource to be recreated on the next call to get().

The thread-safe lazy initialization implementation should follow these rules:
1. All operations must be thread-safe.
2. The resource should only be created once, even if multiple threads call get() simultaneously.
3. The implementation should be efficient and minimize contention between threads.
4. The implementation should avoid unnecessary synchronization after the resource is created.
5. The implementation should handle exceptions thrown by the supplier function.

**Example 1:**
```
Input: 
["ThreadSafeLazyInitializer", "isInitialized", "get", "isInitialized", "get", "reset", "isInitialized", "get"]
[[() -> new ExpensiveResource()], [], [], [], [], [], [], []]

Output: 
[null, false, ExpensiveResource@1, true, ExpensiveResource@1, null, false, ExpensiveResource@2]

Explanation:
ThreadSafeLazyInitializer<ExpensiveResource> initializer = new ThreadSafeLazyInitializer<>(() -> new ExpensiveResource());  // Initialize with a supplier
initializer.isInitialized();                                                                                               // Return false (resource not created yet)
initializer.get();                                                                                                         // Create and return the resource
initializer.isInitialized();                                                                                               // Return true (resource is created)
initializer.get();                                                                                                         // Return the existing resource (same instance)
initializer.reset();                                                                                                       // Reset the initializer
initializer.isInitialized();                                                                                               // Return false (resource will be recreated)
initializer.get();                                                                                                         // Create and return a new resource
```

**Example 2:**
```
Input: 
["ThreadSafeLazyInitializer", "isInitialized", "get", "isInitialized", "reset", "isInitialized", "get"]
[[() -> { throw new RuntimeException("Creation failed"); }], [], [], [], [], [], []]

Output: 
[null, false, RuntimeException("Creation failed"), false, null, false, RuntimeException("Creation failed")]

Explanation:
ThreadSafeLazyInitializer<Object> initializer = new ThreadSafeLazyInitializer<>(() -> { throw new RuntimeException("Creation failed"); });  // Initialize with a failing supplier
initializer.isInitialized();                                                                                                                // Return false (resource not created yet)
initializer.get();                                                                                                                          // Throw RuntimeException("Creation failed")
initializer.isInitialized();                                                                                                                // Return false (resource creation failed)
initializer.reset();                                                                                                                        // Reset the initializer
initializer.isInitialized();                                                                                                                // Return false (resource will be recreated)
initializer.get();                                                                                                                          // Throw RuntimeException("Creation failed") again
```

## Video Explanation
[Java Concurrency: Thread-Safe Lazy Initialization Patterns (6:45 minutes)](https://www.youtube.com/watch?v=lazy-initialization-java)

## Solution Approach

This problem tests your understanding of thread-safe lazy initialization patterns. There are several ways to solve it:

### 1. Using Double-Checked Locking

```java
import java.util.function.Supplier;

class ThreadSafeLazyInitializer<T> {
    private volatile T resource;
    private final Supplier<T> supplier;
    
    public ThreadSafeLazyInitializer(Supplier<T> supplier) {
        this.supplier = supplier;
    }
    
    public T get() {
        T result = resource;
        if (result == null) {
            synchronized (this) {
                result = resource;
                if (result == null) {
                    resource = result = supplier.get();
                }
            }
        }
        return result;
    }
    
    public boolean isInitialized() {
        return resource != null;
    }
    
    public void reset() {
        synchronized (this) {
            resource = null;
        }
    }
}
```

### 2. Using Holder Class Pattern (for static fields)

```java
import java.util.function.Supplier;

class ThreadSafeLazyInitializer<T> {
    private final Supplier<T> supplier;
    private volatile Holder<T> holder;
    
    private static class Holder<T> {
        private final T value;
        
        public Holder(T value) {
            this.value = value;
        }
        
        public T getValue() {
            return value;
        }
    }
    
    public ThreadSafeLazyInitializer(Supplier<T> supplier) {
        this.supplier = supplier;
    }
    
    public T get() {
        Holder<T> result = holder;
        if (result == null) {
            synchronized (this) {
                result = holder;
                if (result == null) {
                    T value = supplier.get();
                    holder = result = new Holder<>(value);
                }
            }
        }
        return result.getValue();
    }
    
    public boolean isInitialized() {
        return holder != null;
    }
    
    public void reset() {
        synchronized (this) {
            holder = null;
        }
    }
}
```

### 3. Using AtomicReference

```java
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

class ThreadSafeLazyInitializer<T> {
    private final AtomicReference<T> resource = new AtomicReference<>();
    private final Supplier<T> supplier;
    
    public ThreadSafeLazyInitializer(Supplier<T> supplier) {
        this.supplier = supplier;
    }
    
    public T get() {
        T result = resource.get();
        if (result == null) {
            T newValue = supplier.get();
            if (resource.compareAndSet(null, newValue)) {
                result = newValue;
            } else {
                result = resource.get();
            }
        }
        return result;
    }
    
    public boolean isInitialized() {
        return resource.get() != null;
    }
    
    public void reset() {
        resource.set(null);
    }
}
```

### 4. Using Synchronized Method

```java
import java.util.function.Supplier;

class ThreadSafeLazyInitializer<T> {
    private T resource;
    private final Supplier<T> supplier;
    
    public ThreadSafeLazyInitializer(Supplier<T> supplier) {
        this.supplier = supplier;
    }
    
    public synchronized T get() {
        if (resource == null) {
            resource = supplier.get();
        }
        return resource;
    }
    
    public synchronized boolean isInitialized() {
        return resource != null;
    }
    
    public synchronized void reset() {
        resource = null;
    }
}
```

## Time and Space Complexity

For the Double-Checked Locking implementation:
- Time Complexity: 
  - O(1) for get() after initialization
  - O(n) for the first call to get(), where n is the time complexity of the supplier function
  - O(1) for isInitialized() and reset()
- Space Complexity: O(1) plus the space required by the resource

For the Holder Class Pattern implementation:
- Time Complexity: 
  - O(1) for get() after initialization
  - O(n) for the first call to get(), where n is the time complexity of the supplier function
  - O(1) for isInitialized() and reset()
- Space Complexity: O(1) plus the space required by the resource

For the AtomicReference implementation:
- Time Complexity: 
  - O(1) for get() after initialization
  - O(n) for the first call to get(), where n is the time complexity of the supplier function
  - O(1) for isInitialized() and reset()
- Space Complexity: O(1) plus the space required by the resource

For the Synchronized Method implementation:
- Time Complexity: 
  - O(1) for get() after initialization, but with synchronization overhead
  - O(n) for the first call to get(), where n is the time complexity of the supplier function
  - O(1) for isInitialized() and reset()
- Space Complexity: O(1) plus the space required by the resource

## Key Insights

1. This problem demonstrates different approaches to implementing thread-safe lazy initialization.
2. The Double-Checked Locking pattern minimizes synchronization overhead by only synchronizing when necessary.
3. The Holder Class Pattern leverages class initialization semantics to ensure thread safety.
4. The AtomicReference implementation uses atomic operations to avoid explicit synchronization.
5. The Synchronized Method implementation is the simplest but has the highest synchronization overhead.
6. The key challenge is ensuring that the resource is only created once, even if multiple threads call get() simultaneously.
7. The volatile keyword is crucial for the Double-Checked Locking pattern to work correctly in Java.

## Interview Tips

- Be prepared to discuss the trade-offs between different lazy initialization patterns.
- Understand the performance characteristics of each implementation, especially in high-concurrency scenarios.
- Know when to use each pattern:
  - Double-Checked Locking: For non-static fields with high contention.
  - Holder Class Pattern: For static fields.
  - AtomicReference: For lock-free implementations.
  - Synchronized Method: For simplicity when performance is not critical.
- Be able to explain why the volatile keyword is necessary for Double-Checked Locking.
- Consider edge cases like what happens if the supplier function throws an exception.
- Discuss how the solution would scale with a large number of threads.
- Mention that in a real-world scenario, you might use Java's built-in Supplier interface or a library like Guava's Suppliers.memoize().
- Explain the difference between eager initialization and lazy initialization, and when to use each.
- Highlight that Java 8 introduced the Supplier interface, which makes implementing lazy initialization patterns easier.


### 43. Thread-Safe Concurrent Deque (Medium)


## Problem Description

Implement a thread-safe concurrent deque that allows multiple threads to add, remove, and access elements from both ends concurrently:

* `ThreadSafeConcurrentDeque()` Initializes an empty thread-safe concurrent deque.
* `void addFirst(T item)` Adds an item to the front of the deque.
* `void addLast(T item)` Adds an item to the end of the deque.
* `T removeFirst()` Removes and returns the first item from the deque. Throws NoSuchElementException if the deque is empty.
* `T removeLast()` Removes and returns the last item from the deque. Throws NoSuchElementException if the deque is empty.
* `T peekFirst()` Returns the first item from the deque without removing it. Returns null if the deque is empty.
* `T peekLast()` Returns the last item from the deque without removing it. Returns null if the deque is empty.
* `int size()` Returns the number of items in the deque.
* `boolean isEmpty()` Returns true if the deque is empty, false otherwise.

The thread-safe concurrent deque implementation should follow these rules:
1. All operations must be thread-safe.
2. Multiple threads can add, remove, and access elements concurrently.
3. The implementation should be efficient and minimize contention between threads.
4. The implementation should provide high concurrency for operations at both ends of the deque.
5. The implementation should be linearizable (operations appear to take effect atomically at some point between their invocation and completion).

**Example 1:**
```
Input: 
["ThreadSafeConcurrentDeque", "isEmpty", "addFirst", "addLast", "peekFirst", "peekLast", "size", "removeFirst", "removeLast", "isEmpty"]
[[], [], [1], [2], [], [], [], [], [], []]

Output: 
[null, true, null, null, 1, 2, 2, 1, 2, true]

Explanation:
ThreadSafeConcurrentDeque<Integer> deque = new ThreadSafeConcurrentDeque<>();  // Initialize an empty deque
deque.isEmpty();                                                               // Return true (deque is empty)
deque.addFirst(1);                                                             // Add 1 to the front of the deque
deque.addLast(2);                                                              // Add 2 to the end of the deque
deque.peekFirst();                                                             // Return 1 (first item in the deque)
deque.peekLast();                                                              // Return 2 (last item in the deque)
deque.size();                                                                  // Return 2 (deque has 2 items)
deque.removeFirst();                                                           // Remove and return 1 from the front of the deque
deque.removeLast();                                                            // Remove and return 2 from the end of the deque
deque.isEmpty();                                                               // Return true (deque is empty)
```

**Example 2:**
```
Input: 
["ThreadSafeConcurrentDeque", "addLast", "addLast", "addFirst", "addFirst", "peekFirst", "peekLast", "size", "removeFirst", "removeFirst", "removeLast", "removeLast", "isEmpty"]
[[], [3], [4], [2], [1], [], [], [], [], [], [], [], []]

Output: 
[null, null, null, null, null, 1, 4, 4, 1, 2, 4, 3, true]

Explanation:
ThreadSafeConcurrentDeque<Integer> deque = new ThreadSafeConcurrentDeque<>();  // Initialize an empty deque
deque.addLast(3);                                                              // Add 3 to the end of the deque
deque.addLast(4);                                                              // Add 4 to the end of the deque
deque.addFirst(2);                                                             // Add 2 to the front of the deque
deque.addFirst(1);                                                             // Add 1 to the front of the deque
deque.peekFirst();                                                             // Return 1 (first item in the deque)
deque.peekLast();                                                              // Return 4 (last item in the deque)
deque.size();                                                                  // Return 4 (deque has 4 items)
deque.removeFirst();                                                           // Remove and return 1 from the front of the deque
deque.removeFirst();                                                           // Remove and return 2 from the front of the deque
deque.removeLast();                                                            // Remove and return 4 from the end of the deque
deque.removeLast();                                                            // Remove and return 3 from the end of the deque
deque.isEmpty();                                                               // Return true (deque is empty)
```

## Video Explanation
[Java Concurrency: ConcurrentLinkedDeque and LinkedBlockingDeque (7:30 minutes)](https://www.youtube.com/watch?v=concurrent-deque-java)

## Solution Approach

This problem tests your understanding of concurrent data structures and thread-safe collections. There are several ways to solve it:

### 1. Using ConcurrentLinkedDeque

```java
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentLinkedDeque;

class ThreadSafeConcurrentDeque<T> {
    private final ConcurrentLinkedDeque<T> deque;
    
    public ThreadSafeConcurrentDeque() {
        this.deque = new ConcurrentLinkedDeque<>();
    }
    
    public void addFirst(T item) {
        deque.addFirst(item);
    }
    
    public void addLast(T item) {
        deque.addLast(item);
    }
    
    public T removeFirst() {
        if (deque.isEmpty()) {
            throw new NoSuchElementException();
        }
        return deque.removeFirst();
    }
    
    public T removeLast() {
        if (deque.isEmpty()) {
            throw new NoSuchElementException();
        }
        return deque.removeLast();
    }
    
    public T peekFirst() {
        return deque.peekFirst();
    }
    
    public T peekLast() {
        return deque.peekLast();
    }
    
    public int size() {
        return deque.size();
    }
    
    public boolean isEmpty() {
        return deque.isEmpty();
    }
}
```

### 2. Using LinkedBlockingDeque

```java
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingDeque;

class ThreadSafeConcurrentDeque<T> {
    private final LinkedBlockingDeque<T> deque;
    
    public ThreadSafeConcurrentDeque() {
        this.deque = new LinkedBlockingDeque<>();
    }
    
    public void addFirst(T item) {
        deque.addFirst(item);
    }
    
    public void addLast(T item) {
        deque.addLast(item);
    }
    
    public T removeFirst() {
        if (deque.isEmpty()) {
            throw new NoSuchElementException();
        }
        return deque.removeFirst();
    }
    
    public T removeLast() {
        if (deque.isEmpty()) {
            throw new NoSuchElementException();
        }
        return deque.removeLast();
    }
    
    public T peekFirst() {
        return deque.peekFirst();
    }
    
    public T peekLast() {
        return deque.peekLast();
    }
    
    public int size() {
        return deque.size();
    }
    
    public boolean isEmpty() {
        return deque.isEmpty();
    }
}
```

### 3. Custom Implementation with Fine-Grained Locking

```java
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

class ThreadSafeConcurrentDeque<T> {
    private static class Node<T> {
        final T item;
        Node<T> prev;
        Node<T> next;
        
        Node(T item) {
            this.item = item;
        }
    }
    
    private final Node<T> head;
    private final Node<T> tail;
    private final ReentrantLock headLock;
    private final ReentrantLock tailLock;
    private final AtomicInteger size;
    
    public ThreadSafeConcurrentDeque() {
        this.head = new Node<>(null);
        this.tail = new Node<>(null);
        head.next = tail;
        tail.prev = head;
        this.headLock = new ReentrantLock();
        this.tailLock = new ReentrantLock();
        this.size = new AtomicInteger(0);
    }
    
    public void addFirst(T item) {
        if (item == null) {
            throw new NullPointerException();
        }
        
        Node<T> newNode = new Node<>(item);
        headLock.lock();
        try {
            newNode.next = head.next;
            newNode.prev = head;
            head.next.prev = newNode;
            head.next = newNode;
            size.incrementAndGet();
        } finally {
            headLock.unlock();
        }
    }
    
    public void addLast(T item) {
        if (item == null) {
            throw new NullPointerException();
        }
        
        Node<T> newNode = new Node<>(item);
        tailLock.lock();
        try {
            newNode.next = tail;
            newNode.prev = tail.prev;
            tail.prev.next = newNode;
            tail.prev = newNode;
            size.incrementAndGet();
        } finally {
            tailLock.unlock();
        }
    }
    
    public T removeFirst() {
        headLock.lock();
        try {
            if (head.next == tail) {
                throw new NoSuchElementException();
            }
            
            Node<T> first = head.next;
            head.next = first.next;
            first.next.prev = head;
            size.decrementAndGet();
            return first.item;
        } finally {
            headLock.unlock();
        }
    }
    
    public T removeLast() {
        tailLock.lock();
        try {
            if (tail.prev == head) {
                throw new NoSuchElementException();
            }
            
            Node<T> last = tail.prev;
            tail.prev = last.prev;
            last.prev.next = tail;
            size.decrementAndGet();
            return last.item;
        } finally {
            tailLock.unlock();
        }
    }
    
    public T peekFirst() {
        headLock.lock();
        try {
            return head.next == tail ? null : head.next.item;
        } finally {
            headLock.unlock();
        }
    }
    
    public T peekLast() {
        tailLock.lock();
        try {
            return tail.prev == head ? null : tail.prev.item;
        } finally {
            tailLock.unlock();
        }
    }
    
    public int size() {
        return size.get();
    }
    
    public boolean isEmpty() {
        return size.get() == 0;
    }
}
```

## Time and Space Complexity

For the ConcurrentLinkedDeque implementation:
- Time Complexity: 
  - O(1) for addFirst, addLast, removeFirst, removeLast, peekFirst, peekLast, and isEmpty operations
  - O(n) for size operation, where n is the number of elements in the deque
- Space Complexity: O(n) where n is the number of elements in the deque

For the LinkedBlockingDeque implementation:
- Time Complexity: 
  - O(1) for addFirst, addLast, removeFirst, removeLast, peekFirst, peekLast, size, and isEmpty operations
- Space Complexity: O(n) where n is the number of elements in the deque

For the Custom Implementation with Fine-Grained Locking:
- Time Complexity: 
  - O(1) for addFirst, addLast, removeFirst, removeLast, peekFirst, peekLast, size, and isEmpty operations
- Space Complexity: O(n) where n is the number of elements in the deque

## Key Insights

1. This problem demonstrates different approaches to implementing a thread-safe concurrent deque.
2. The ConcurrentLinkedDeque approach leverages Java's built-in thread-safe deque implementation, which is optimized for concurrent access.
3. The LinkedBlockingDeque approach uses a blocking deque implementation, which can be useful when you need to block threads until elements are available.
4. The custom implementation with fine-grained locking provides more control over the deque's behavior, but requires careful synchronization.
5. The key challenge is ensuring that multiple threads can add, remove, and access elements from both ends concurrently without using coarse-grained locks.

## Interview Tips

- Be prepared to discuss the trade-offs between different thread-safe deque implementations.
- Understand the performance characteristics of each implementation, especially for operations at both ends of the deque.
- Know when to use each implementation:
  - ConcurrentLinkedDeque: For general-purpose concurrent deques with good performance for both ends.
  - LinkedBlockingDeque: When you need blocking operations (e.g., waiting for elements to be available).
  - Custom Implementation: When you need fine-grained control over the deque's behavior.
- Be able to explain the difference between ConcurrentLinkedDeque and LinkedBlockingDeque.
- Consider edge cases like what happens if multiple threads try to remove the last element simultaneously.
- Discuss how the solution would scale with a large number of threads and elements.
- Mention that in a real-world scenario, you would likely use Java's ConcurrentLinkedDeque or LinkedBlockingDeque rather than implementing your own thread-safe deque.
- Explain the difference between thread-safe and concurrent collections, and when to use each.
- Highlight that ConcurrentLinkedDeque is a lock-free implementation that uses CAS operations internally, while LinkedBlockingDeque uses locks.


### 44. Thread-Safe Lock-Free Stack (Hard)


## Problem Description

Implement a thread-safe lock-free stack that allows multiple threads to push and pop elements concurrently without using locks:

* `LockFreeStack()` Initializes an empty lock-free stack.
* `void push(T item)` Adds an item to the top of the stack.
* `T pop()` Removes and returns the top item from the stack. Returns null if the stack is empty.
* `T peek()` Returns the top item from the stack without removing it. Returns null if the stack is empty.
* `boolean isEmpty()` Returns true if the stack is empty, false otherwise.
* `int size()` Returns the number of items in the stack.

The lock-free stack implementation should follow these rules:
1. All operations must be thread-safe.
2. The implementation must not use any locks (e.g., synchronized, ReentrantLock).
3. The implementation should use atomic operations to ensure thread safety.
4. The implementation should be linearizable (operations appear to take effect atomically at some point between their invocation and completion).
5. The implementation should handle the ABA problem.

**Example 1:**
```
Input: 
["LockFreeStack", "isEmpty", "push", "push", "peek", "size", "pop", "pop", "isEmpty"]
[[], [], [1], [2], [], [], [], [], []]

Output: 
[null, true, null, null, 2, 2, 2, 1, true]

Explanation:
LockFreeStack<Integer> stack = new LockFreeStack<>();  // Initialize an empty stack
stack.isEmpty();                                        // Return true (stack is empty)
stack.push(1);                                          // Add 1 to the stack
stack.push(2);                                          // Add 2 to the stack
stack.peek();                                           // Return 2 (top item in the stack)
stack.size();                                           // Return 2 (stack has 2 items)
stack.pop();                                            // Remove and return 2 from the stack
stack.pop();                                            // Remove and return 1 from the stack
stack.isEmpty();                                        // Return true (stack is empty)
```

**Example 2:**
```
Input: 
["LockFreeStack", "push", "push", "push", "pop", "peek", "size", "pop", "pop", "pop"]
[[], [3], [4], [5], [], [], [], [], [], []]

Output: 
[null, null, null, null, 5, 4, 2, 4, 3, null]

Explanation:
LockFreeStack<Integer> stack = new LockFreeStack<>();  // Initialize an empty stack
stack.push(3);                                          // Add 3 to the stack
stack.push(4);                                          // Add 4 to the stack
stack.push(5);                                          // Add 5 to the stack
stack.pop();                                            // Remove and return 5 from the stack
stack.peek();                                           // Return 4 (top item in the stack)
stack.size();                                           // Return 2 (stack has 2 items)
stack.pop();                                            // Remove and return 4 from the stack
stack.pop();                                            // Remove and return 3 from the stack
stack.pop();                                            // Return null (stack is empty)
```

## Video Explanation
[Java Concurrency: Lock-Free Stack Implementation (8:15 minutes)](https://www.youtube.com/watch?v=lock-free-stack-java)

## Solution Approach

This problem tests your understanding of lock-free data structures and atomic operations. There are several ways to solve it:

### 1. Using AtomicReference

```java
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicInteger;

class LockFreeStack<T> {
    private static class Node<T> {
        final T item;
        Node<T> next;
        
        Node(T item, Node<T> next) {
            this.item = item;
            this.next = next;
        }
    }
    
    private final AtomicReference<Node<T>> head = new AtomicReference<>(null);
    private final AtomicInteger size = new AtomicInteger(0);
    
    public void push(T item) {
        Node<T> newHead = new Node<>(item, null);
        Node<T> oldHead;
        do {
            oldHead = head.get();
            newHead.next = oldHead;
        } while (!head.compareAndSet(oldHead, newHead));
        size.incrementAndGet();
    }
    
    public T pop() {
        Node<T> oldHead;
        Node<T> newHead;
        do {
            oldHead = head.get();
            if (oldHead == null) {
                return null;
            }
            newHead = oldHead.next;
        } while (!head.compareAndSet(oldHead, newHead));
        size.decrementAndGet();
        return oldHead.item;
    }
    
    public T peek() {
        Node<T> currentHead = head.get();
        return currentHead == null ? null : currentHead.item;
    }
    
    public boolean isEmpty() {
        return head.get() == null;
    }
    
    public int size() {
        return size.get();
    }
}
```

### 2. Using AtomicReference with ABA Problem Handling

```java
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicInteger;

class LockFreeStack<T> {
    private static class Node<T> {
        final T item;
        Node<T> next;
        
        Node(T item, Node<T> next) {
            this.item = item;
            this.next = next;
        }
    }
    
    private static class AtomicStampedNode<T> {
        final AtomicReference<Node<T>> ref;
        final AtomicInteger stamp;
        
        AtomicStampedNode(Node<T> node) {
            this.ref = new AtomicReference<>(node);
            this.stamp = new AtomicInteger(0);
        }
    }
    
    private final AtomicStampedNode<T> head = new AtomicStampedNode<>(null);
    private final AtomicInteger size = new AtomicInteger(0);
    
    public void push(T item) {
        Node<T> newNode = new Node<>(item, null);
        Node<T> oldHead;
        int stamp;
        do {
            stamp = head.stamp.get();
            oldHead = head.ref.get();
            newNode.next = oldHead;
        } while (!head.ref.compareAndSet(oldHead, newNode) || !head.stamp.compareAndSet(stamp, stamp + 1));
        size.incrementAndGet();
    }
    
    public T pop() {
        Node<T> oldHead;
        Node<T> newHead;
        int stamp;
        do {
            stamp = head.stamp.get();
            oldHead = head.ref.get();
            if (oldHead == null) {
                return null;
            }
            newHead = oldHead.next;
        } while (!head.ref.compareAndSet(oldHead, newHead) || !head.stamp.compareAndSet(stamp, stamp + 1));
        size.decrementAndGet();
        return oldHead.item;
    }
    
    public T peek() {
        Node<T> currentHead = head.ref.get();
        return currentHead == null ? null : currentHead.item;
    }
    
    public boolean isEmpty() {
        return head.ref.get() == null;
    }
    
    public int size() {
        return size.get();
    }
}
```

### 3. Using AtomicStampedReference

```java
import java.util.concurrent.atomic.AtomicStampedReference;
import java.util.concurrent.atomic.AtomicInteger;

class LockFreeStack<T> {
    private static class Node<T> {
        final T item;
        Node<T> next;
        
        Node(T item, Node<T> next) {
            this.item = item;
            this.next = next;
        }
    }
    
    private final AtomicStampedReference<Node<T>> head = new AtomicStampedReference<>(null, 0);
    private final AtomicInteger size = new AtomicInteger(0);
    
    public void push(T item) {
        Node<T> newHead = new Node<>(item, null);
        Node<T> oldHead;
        int stamp;
        do {
            stamp = head.getStamp();
            oldHead = head.getReference();
            newHead.next = oldHead;
        } while (!head.compareAndSet(oldHead, newHead, stamp, stamp + 1));
        size.incrementAndGet();
    }
    
    public T pop() {
        Node<T> oldHead;
        Node<T> newHead;
        int stamp;
        do {
            stamp = head.getStamp();
            oldHead = head.getReference();
            if (oldHead == null) {
                return null;
            }
            newHead = oldHead.next;
        } while (!head.compareAndSet(oldHead, newHead, stamp, stamp + 1));
        size.decrementAndGet();
        return oldHead.item;
    }
    
    public T peek() {
        Node<T> currentHead = head.getReference();
        return currentHead == null ? null : currentHead.item;
    }
    
    public boolean isEmpty() {
        return head.getReference() == null;
    }
    
    public int size() {
        return size.get();
    }
}
```

## Time and Space Complexity

For the AtomicReference implementation:
- Time Complexity: 
  - O(1) for push, pop, peek, isEmpty, and size operations in the absence of contention
  - Under contention, the time complexity can be higher due to retries in the CAS operations
- Space Complexity: O(n) where n is the number of elements in the stack

For the AtomicReference with ABA Problem Handling implementation:
- Time Complexity: 
  - O(1) for push, pop, peek, isEmpty, and size operations in the absence of contention
  - Under contention, the time complexity can be higher due to retries in the CAS operations
- Space Complexity: O(n) where n is the number of elements in the stack

For the AtomicStampedReference implementation:
- Time Complexity: 
  - O(1) for push, pop, peek, isEmpty, and size operations in the absence of contention
  - Under contention, the time complexity can be higher due to retries in the CAS operations
- Space Complexity: O(n) where n is the number of elements in the stack

## Key Insights

1. This problem demonstrates how to implement a lock-free data structure using atomic operations.
2. The AtomicReference implementation is the simplest but is susceptible to the ABA problem.
3. The ABA problem occurs when a thread reads a value A, another thread changes it to B and then back to A, and the first thread incorrectly assumes that the value has not changed.
4. The AtomicReference with ABA Problem Handling implementation uses a stamp to detect ABA problems.
5. The AtomicStampedReference implementation uses Java's built-in support for stamped references to handle the ABA problem.
6. The key challenge is ensuring that multiple threads can push and pop elements concurrently without using locks.

## Interview Tips

- Be prepared to discuss the trade-offs between lock-based and lock-free implementations.
- Understand the performance characteristics of each implementation, especially under high contention.
- Know when to use each implementation:
  - AtomicReference: For simplicity when the ABA problem is not a concern.
  - AtomicReference with ABA Problem Handling: For custom handling of the ABA problem.
  - AtomicStampedReference: For built-in handling of the ABA problem.
- Be able to explain the ABA problem and how to solve it.
- Consider edge cases like what happens if multiple threads try to pop the last element simultaneously.
- Discuss how the solution would scale with a large number of threads and elements.
- Mention that in a real-world scenario, you might use Java's ConcurrentLinkedDeque or a library like JCTools for high-performance lock-free data structures.
- Explain the difference between lock-free, wait-free, and obstruction-free algorithms.
- Highlight that lock-free algorithms guarantee system-wide progress, but not thread-specific progress.
- Discuss the memory model implications of lock-free programming in Java.


### 45. Thread-Safe Concurrent SkipList Map (Hard)


## Problem Description

Implement a thread-safe concurrent sorted map based on a skip list data structure:

* `ConcurrentSkipListMap()` Initializes an empty concurrent skip list map.
* `V put(K key, V value)` Associates the specified value with the specified key in this map. If the map previously contained a mapping for the key, the old value is replaced. Returns the previous value associated with the key, or null if there was no mapping.
* `V get(K key)` Returns the value to which the specified key is mapped, or null if this map contains no mapping for the key.
* `V remove(K key)` Removes the mapping for a key from this map if it is present. Returns the value to which this map previously associated the key, or null if the map contained no mapping for the key.
* `boolean containsKey(K key)` Returns true if this map contains a mapping for the specified key.
* `int size()` Returns the number of key-value mappings in this map.
* `boolean isEmpty()` Returns true if this map contains no key-value mappings.
* `K firstKey()` Returns the first (lowest) key currently in this map. Throws NoSuchElementException if the map is empty.
* `K lastKey()` Returns the last (highest) key currently in this map. Throws NoSuchElementException if the map is empty.
* `K floorKey(K key)` Returns the greatest key less than or equal to the given key, or null if there is no such key.
* `K ceilingKey(K key)` Returns the least key greater than or equal to the given key, or null if there is no such key.

The thread-safe concurrent skip list map implementation should follow these rules:
1. All operations must be thread-safe.
2. The map must maintain keys in sorted order.
3. The implementation should be efficient and minimize contention between threads.
4. The implementation should provide high concurrency for read and write operations.
5. The implementation should be linearizable (operations appear to take effect atomically at some point between their invocation and completion).

**Example 1:**
```
Input: 
["ConcurrentSkipListMap", "isEmpty", "put", "put", "get", "containsKey", "size", "remove", "isEmpty"]
[[], [], [1, "one"], [2, "two"], [1], [2], [], [1], []]

Output: 
[null, true, null, null, "one", true, 2, "one", false]

Explanation:
ConcurrentSkipListMap<Integer, String> map = new ConcurrentSkipListMap<>();  // Initialize an empty map
map.isEmpty();                                                               // Return true (map is empty)
map.put(1, "one");                                                           // Add mapping 1 -> "one"
map.put(2, "two");                                                           // Add mapping 2 -> "two"
map.get(1);                                                                  // Return "one" (value for key 1)
map.containsKey(2);                                                          // Return true (map contains key 2)
map.size();                                                                  // Return 2 (map has 2 entries)
map.remove(1);                                                               // Remove and return "one" (value for key 1)
map.isEmpty();                                                               // Return false (map still has one entry)
```

**Example 2:**
```
Input: 
["ConcurrentSkipListMap", "put", "put", "put", "firstKey", "lastKey", "floorKey", "ceilingKey", "remove", "size"]
[[], [3, "three"], [1, "one"], [2, "two"], [], [], [2], [2], [2], []]

Output: 
[null, null, null, null, 1, 3, 2, 2, "two", 2]

Explanation:
ConcurrentSkipListMap<Integer, String> map = new ConcurrentSkipListMap<>();  // Initialize an empty map
map.put(3, "three");                                                          // Add mapping 3 -> "three"
map.put(1, "one");                                                            // Add mapping 1 -> "one"
map.put(2, "two");                                                            // Add mapping 2 -> "two"
map.firstKey();                                                               // Return 1 (lowest key in the map)
map.lastKey();                                                                // Return 3 (highest key in the map)
map.floorKey(2);                                                              // Return 2 (greatest key <= 2)
map.ceilingKey(2);                                                            // Return 2 (least key >= 2)
map.remove(2);                                                                // Remove and return "two" (value for key 2)
map.size();                                                                   // Return 2 (map has 2 entries)
```

## Video Explanation
[Java Concurrency: ConcurrentSkipListMap Implementation (9:15 minutes)](https://www.youtube.com/watch?v=concurrent-skiplist-map-java)

## Solution Approach

This problem tests your understanding of concurrent sorted map implementations. There are several ways to solve it:

### 1. Using Java's ConcurrentSkipListMap

```java
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentSkipListMap;

class ThreadSafeConcurrentSkipListMap<K extends Comparable<? super K>, V> {
    private final ConcurrentSkipListMap<K, V> map;
    
    public ThreadSafeConcurrentSkipListMap() {
        this.map = new ConcurrentSkipListMap<>();
    }
    
    public V put(K key, V value) {
        return map.put(key, value);
    }
    
    public V get(K key) {
        return map.get(key);
    }
    
    public V remove(K key) {
        return map.remove(key);
    }
    
    public boolean containsKey(K key) {
        return map.containsKey(key);
    }
    
    public int size() {
        return map.size();
    }
    
    public boolean isEmpty() {
        return map.isEmpty();
    }
    
    public K firstKey() {
        if (map.isEmpty()) {
            throw new NoSuchElementException();
        }
        return map.firstKey();
    }
    
    public K lastKey() {
        if (map.isEmpty()) {
            throw new NoSuchElementException();
        }
        return map.lastKey();
    }
    
    public K floorKey(K key) {
        return map.floorKey(key);
    }
    
    public K ceilingKey(K key) {
        return map.ceilingKey(key);
    }
}
```

### 2. Custom Implementation of ConcurrentSkipListMap

```java
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

class ThreadSafeConcurrentSkipListMap<K extends Comparable<? super K>, V> {
    private static final int MAX_LEVEL = 16;
    private static final float PROBABILITY = 0.5f;
    
    private static class Node<K, V> {
        final K key;
        volatile V value;
        final AtomicReference<Node<K, V>>[] next;
        
        @SuppressWarnings("unchecked")
        Node(K key, V value, int level) {
            this.key = key;
            this.value = value;
            this.next = new AtomicReference[level + 1];
            for (int i = 0; i <= level; i++) {
                this.next[i] = new AtomicReference<>(null);
            }
        }
    }
    
    private final Node<K, V> head;
    private final AtomicInteger size;
    private final Random random;
    
    @SuppressWarnings("unchecked")
    public ThreadSafeConcurrentSkipListMap() {
        this.head = new Node<>(null, null, MAX_LEVEL);
        this.size = new AtomicInteger(0);
        this.random = new Random();
    }
    
    private int randomLevel() {
        int level = 0;
        while (level < MAX_LEVEL && random.nextFloat() < PROBABILITY) {
            level++;
        }
        return level;
    }
    
    public V put(K key, V value) {
        if (key == null) {
            throw new NullPointerException();
        }
        
        Node<K, V>[] update = new Node[MAX_LEVEL + 1];
        Node<K, V> current = head;
        
        for (int i = MAX_LEVEL; i >= 0; i--) {
            while (current.next[i].get() != null && 
                   current.next[i].get().key != null && 
                   current.next[i].get().key.compareTo(key) < 0) {
                current = current.next[i].get();
            }
            update[i] = current;
        }
        
        current = current.next[0].get();
        
        if (current != null && current.key != null && current.key.compareTo(key) == 0) {
            V oldValue = current.value;
            current.value = value;
            return oldValue;
        }
        
        int level = randomLevel();
        Node<K, V> newNode = new Node<>(key, value, level);
        
        for (int i = 0; i <= level; i++) {
            Node<K, V> next = update[i].next[i].get();
            newNode.next[i].set(next);
            update[i].next[i].set(newNode);
        }
        
        size.incrementAndGet();
        return null;
    }
    
    public V get(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        
        Node<K, V> current = head;
        
        for (int i = MAX_LEVEL; i >= 0; i--) {
            while (current.next[i].get() != null && 
                   current.next[i].get().key != null && 
                   current.next[i].get().key.compareTo(key) < 0) {
                current = current.next[i].get();
            }
        }
        
        current = current.next[0].get();
        
        if (current != null && current.key != null && current.key.compareTo(key) == 0) {
            return current.value;
        }
        
        return null;
    }
    
    public V remove(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        
        Node<K, V>[] update = new Node[MAX_LEVEL + 1];
        Node<K, V> current = head;
        
        for (int i = MAX_LEVEL; i >= 0; i--) {
            while (current.next[i].get() != null && 
                   current.next[i].get().key != null && 
                   current.next[i].get().key.compareTo(key) < 0) {
                current = current.next[i].get();
            }
            update[i] = current;
        }
        
        current = current.next[0].get();
        
        if (current != null && current.key != null && current.key.compareTo(key) == 0) {
            for (int i = 0; i <= MAX_LEVEL; i++) {
                if (update[i].next[i].get() != current) {
                    break;
                }
                update[i].next[i].set(current.next[i].get());
            }
            
            size.decrementAndGet();
            return current.value;
        }
        
        return null;
    }
    
    public boolean containsKey(K key) {
        return get(key) != null;
    }
    
    public int size() {
        return size.get();
    }
    
    public boolean isEmpty() {
        return size.get() == 0;
    }
    
    public K firstKey() {
        Node<K, V> first = head.next[0].get();
        if (first == null || first.key == null) {
            throw new NoSuchElementException();
        }
        return first.key;
    }
    
    public K lastKey() {
        Node<K, V> current = head;
        
        for (int i = MAX_LEVEL; i >= 0; i--) {
            while (current.next[i].get() != null && current.next[i].get().key != null) {
                current = current.next[i].get();
            }
        }
        
        if (current == head) {
            throw new NoSuchElementException();
        }
        
        return current.key;
    }
    
    public K floorKey(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        
        Node<K, V> current = head;
        Node<K, V> candidate = null;
        
        for (int i = MAX_LEVEL; i >= 0; i--) {
            while (current.next[i].get() != null && 
                   current.next[i].get().key != null && 
                   current.next[i].get().key.compareTo(key) <= 0) {
                current = current.next[i].get();
                if (current.key != null && current.key.compareTo(key) <= 0) {
                    candidate = current;
                }
            }
        }
        
        return candidate == null ? null : candidate.key;
    }
    
    public K ceilingKey(K key) {
        if (key == null) {
            throw new NullPointerException();
        }
        
        Node<K, V> current = head;
        
        for (int i = MAX_LEVEL; i >= 0; i--) {
            while (current.next[i].get() != null && 
                   current.next[i].get().key != null && 
                   current.next[i].get().key.compareTo(key) < 0) {
                current = current.next[i].get();
            }
        }
        
        current = current.next[0].get();
        
        if (current != null && current.key != null) {
            return current.key;
        }
        
        return null;
    }
}
```

## Time and Space Complexity

For the Java's ConcurrentSkipListMap implementation:
- Time Complexity: 
  - O(log n) for put, get, remove, containsKey, firstKey, lastKey, floorKey, and ceilingKey operations
  - O(1) for size and isEmpty operations
- Space Complexity: O(n) where n is the number of key-value pairs in the map

For the Custom Implementation:
- Time Complexity: 
  - O(log n) for put, get, remove, containsKey, firstKey, lastKey, floorKey, and ceilingKey operations
  - O(1) for size and isEmpty operations
- Space Complexity: O(n log n) where n is the number of key-value pairs in the map (due to the skip list structure)

## Key Insights

1. This problem demonstrates how to implement a thread-safe concurrent sorted map using a skip list data structure.
2. The Java's ConcurrentSkipListMap implementation leverages the built-in thread-safe sorted map implementation, which is optimized for concurrent access.
3. The custom implementation shows how to build a skip list from scratch, using atomic references to ensure thread safety.
4. The key challenge is ensuring that multiple threads can add, remove, and access elements concurrently while maintaining the sorted order of keys.
5. Skip lists provide O(log n) average time complexity for most operations, making them efficient for sorted maps.

## Interview Tips

- Be prepared to discuss the trade-offs between different thread-safe sorted map implementations.
- Understand the performance characteristics of skip lists, especially for operations that require maintaining sorted order.
- Know when to use ConcurrentSkipListMap:
  - When you need a thread-safe sorted map with good concurrent performance.
  - When you need to perform range queries or navigate the map in sorted order.
  - When you need ceiling, floor, or other navigational operations.
- Be able to explain how skip lists work and why they are efficient for concurrent access.
- Consider edge cases like what happens if multiple threads try to modify the same key simultaneously.
- Discuss how the solution would scale with a large number of threads and elements.
- Mention that in a real-world scenario, you would likely use Java's ConcurrentSkipListMap rather than implementing your own.
- Explain the difference between ConcurrentHashMap and ConcurrentSkipListMap, and when to use each.
- Highlight that ConcurrentSkipListMap is a lock-free implementation that uses CAS operations internally.
- Discuss the memory overhead of skip lists compared to other data structures.


### Thread-Safe Non-Blocking Algorithms


## Problem Description

Design and implement a thread-safe non-blocking counter that supports increment, decrement, and get operations without using locks or other blocking synchronization mechanisms. The counter should be able to handle high contention scenarios efficiently.

Non-blocking algorithms use atomic operations instead of locks to ensure thread safety. They provide better scalability and eliminate issues like deadlocks, priority inversion, and convoying.

## Requirements

1. Implement a `NonBlockingCounter` class with the following methods:
   - `increment()`: Atomically increases the counter value by 1
   - `decrement()`: Atomically decreases the counter value by 1
   - `get()`: Returns the current counter value

2. The implementation must be:
   - Thread-safe: Operations from multiple threads must not corrupt the counter state
   - Non-blocking: No locks, synchronized blocks, or other blocking mechanisms should be used
   - Progress guaranteeing: At least one thread must always be able to make progress

## Solution Approaches

### Approach 1: Using AtomicInteger

The simplest approach is to use Java's `AtomicInteger` class, which provides atomic operations through Compare-And-Swap (CAS) instructions.

```java
import java.util.concurrent.atomic.AtomicInteger;

public class NonBlockingCounter {
    private final AtomicInteger value = new AtomicInteger(0);
    
    public void increment() {
        value.incrementAndGet();
    }
    
    public void decrement() {
        value.decrementAndGet();
    }
    
    public int get() {
        return value.get();
    }
}
```

**Time Complexity**: O(1) amortized for all operations, though under high contention, CAS operations might need multiple attempts.
**Space Complexity**: O(1)

### Approach 2: Custom Implementation with CAS

To understand the underlying mechanism, we can implement our own non-blocking counter using `AtomicReference` and explicit CAS operations.

```java
import java.util.concurrent.atomic.AtomicReference;

public class CustomNonBlockingCounter {
    private static class IntegerHolder {
        final int value;
        
        IntegerHolder(int value) {
            this.value = value;
        }
    }
    
    private final AtomicReference<IntegerHolder> valueHolder = 
        new AtomicReference<>(new IntegerHolder(0));
    
    public void increment() {
        while (true) {
            IntegerHolder current = valueHolder.get();
            IntegerHolder next = new IntegerHolder(current.value + 1);
            if (valueHolder.compareAndSet(current, next)) {
                return;
            }
            // If CAS fails, retry with the new current value
        }
    }
    
    public void decrement() {
        while (true) {
            IntegerHolder current = valueHolder.get();
            IntegerHolder next = new IntegerHolder(current.value - 1);
            if (valueHolder.compareAndSet(current, next)) {
                return;
            }
            // If CAS fails, retry with the new current value
        }
    }
    
    public int get() {
        return valueHolder.get().value;
    }
}
```

**Time Complexity**: O(1) amortized, but potentially O(n) in high contention scenarios where n is the number of concurrent threads.
**Space Complexity**: O(1) for the counter itself, but creates temporary objects during operations.

### Approach 3: Using LongAdder for High Contention

For scenarios with high contention, Java provides `LongAdder` which reduces contention by maintaining multiple counters that are combined when the final value is needed.

```java
import java.util.concurrent.atomic.LongAdder;

public class HighContentionCounter {
    private final LongAdder adder = new LongAdder();
    
    public void increment() {
        adder.increment();
    }
    
    public void decrement() {
        adder.decrement();
    }
    
    public long get() {
        return adder.sum();
    }
}
```

**Time Complexity**: O(1) for increment/decrement, O(n) for get where n is the number of internal counters.
**Space Complexity**: O(p) where p is the number of threads actively updating the counter.

## Advanced Implementation: Lock-Free Stack

Let's extend the problem to implement a more complex non-blocking data structure - a lock-free stack.

```java
import java.util.concurrent.atomic.AtomicReference;
import java.util.EmptyStackException;

public class LockFreeStack<T> {
    private static class Node<T> {
        final T value;
        Node<T> next;
        
        Node(T value) {
            this.value = value;
        }
    }
    
    private final AtomicReference<Node<T>> head = new AtomicReference<>(null);
    
    public void push(T value) {
        Node<T> newHead = new Node<>(value);
        Node<T> oldHead;
        do {
            oldHead = head.get();
            newHead.next = oldHead;
        } while (!head.compareAndSet(oldHead, newHead));
    }
    
    public T pop() {
        Node<T> oldHead;
        Node<T> newHead;
        do {
            oldHead = head.get();
            if (oldHead == null) {
                throw new EmptyStackException();
            }
            newHead = oldHead.next;
        } while (!head.compareAndSet(oldHead, newHead));
        return oldHead.value;
    }
    
    public boolean isEmpty() {
        return head.get() == null;
    }
}
```

## Testing the Implementation

```java
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NonBlockingCounterTest {
    public static void main(String[] args) throws InterruptedException {
        final int numThreads = 10;
        final int iterations = 100000;
        
        NonBlockingCounter counter = new NonBlockingCounter();
        CountDownLatch latch = new CountDownLatch(numThreads * 2);
        ExecutorService executor = Executors.newFixedThreadPool(numThreads * 2);
        
        // Create incrementing threads
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < iterations; j++) {
                    counter.increment();
                }
                latch.countDown();
            });
        }
        
        // Create decrementing threads
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                for (int j = 0; j < iterations; j++) {
                    counter.decrement();
                }
                latch.countDown();
            });
        }
        
        latch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        
        System.out.println("Final counter value: " + counter.get());
        // Expected: 0 (since we have equal increments and decrements)
    }
}
```

## Key Insights for Interviews

1. **Compare-And-Swap (CAS)**: Understand how CAS operations work as the foundation of non-blocking algorithms. CAS atomically updates a value only if it matches an expected value.

2. **ABA Problem**: Be aware of the ABA problem in non-blocking algorithms, where a value changes from A to B and back to A, potentially causing incorrect behavior. Solutions include version counters or hazard pointers.

3. **Progress Guarantees**:
   - **Wait-freedom**: All operations complete in a bounded number of steps
   - **Lock-freedom**: At least one thread makes progress in a bounded number of steps
   - **Obstruction-freedom**: A thread makes progress if it runs in isolation

4. **Memory Barriers**: Non-blocking algorithms often require careful consideration of memory ordering to ensure visibility of changes across threads.

5. **Performance Characteristics**: Non-blocking algorithms typically perform better under high contention but may have higher overhead for uncontended operations.

## Real-world Applications

1. High-frequency trading systems where latency is critical
2. Real-time systems where thread scheduling delays are unacceptable
3. Lock-free data structures in concurrent collections
4. Operating system kernels and device drivers

## Video Explanation

[Non-Blocking Algorithms and Data Structures](https://www.youtube.com/watch?v=9XAx279s7gs) - 8:42

## Interview Tips

- Demonstrate understanding of the fundamental differences between blocking and non-blocking synchronization
- Explain the trade-offs between different non-blocking approaches
- Be prepared to discuss the ABA problem and potential solutions
- Understand the memory model implications of non-blocking algorithms
- Practice implementing basic non-blocking data structures like counters, stacks, and queues


### Memory Models and Happens-Before Relationships


## Problem Description

Design a thread-safe data structure that correctly handles memory visibility issues across multiple threads without excessive synchronization. You must demonstrate understanding of the Java Memory Model (JMM) and happens-before relationships to ensure correct behavior.

The Java Memory Model defines the rules for how changes made by one thread become visible to other threads. Understanding these rules is critical for writing correct concurrent code, especially when optimizing performance by reducing synchronization.

## Requirements

1. Implement a `SharedState` class that maintains a collection of values that can be updated and read by multiple threads.
2. The implementation must:
   - Ensure proper visibility of updates across threads
   - Minimize synchronization overhead
   - Demonstrate at least three different ways to establish happens-before relationships
   - Include documentation explaining the memory visibility guarantees

## Solution Approaches

### Approach 1: Using Volatile Variables

Volatile variables provide visibility guarantees without the overhead of full synchronization. A write to a volatile variable establishes a happens-before relationship with subsequent reads of that variable.

```java
public class VolatileSharedState {
    private volatile boolean initialized = false;
    private int[] data;
    
    public void initialize() {
        // All writes before the volatile write are visible after the volatile read
        data = new int[10];
        for (int i = 0; i < data.length; i++) {
            data[i] = i;
        }
        // This volatile write establishes a happens-before relationship
        initialized = true;
    }
    
    public int[] readData() {
        // This volatile read ensures visibility of the writes in initialize()
        if (!initialized) {
            return null;
        }
        // Because of the happens-before relationship, data is guaranteed to be visible
        return data.clone();
    }
}
```

**Memory Visibility Guarantee**: The volatile write to `initialized` ensures that all writes to `data` are visible to any thread that reads `initialized` as true.

### Approach 2: Using Synchronized Methods

Synchronized methods establish happens-before relationships between all synchronized accesses to the same object.

```java
public class SynchronizedSharedState {
    private boolean initialized = false;
    private int[] data;
    
    public synchronized void initialize() {
        if (!initialized) {
            data = new int[10];
            for (int i = 0; i < data.length; i++) {
                data[i] = i;
            }
            initialized = true;
        }
    }
    
    public synchronized int[] readData() {
        if (!initialized) {
            return null;
        }
        return data.clone();
    }
}
```

**Memory Visibility Guarantee**: The JMM ensures that when a thread exits a synchronized block, all its writes are visible to any thread that subsequently enters a synchronized block on the same object.

### Approach 3: Using java.util.concurrent.atomic Package

Atomic variables provide happens-before guarantees similar to volatile variables but with additional atomic operations.

```java
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class AtomicSharedState {
    private final AtomicBoolean initialized = new AtomicBoolean(false);
    private final AtomicReferenceArray<Integer> data = new AtomicReferenceArray<>(10);
    
    public void initialize() {
        for (int i = 0; i < data.length(); i++) {
            data.set(i, i);
        }
        // This atomic operation establishes a happens-before relationship
        initialized.set(true);
    }
    
    public Integer[] readData() {
        if (!initialized.get()) {
            return null;
        }
        
        Integer[] result = new Integer[data.length()];
        for (int i = 0; i < data.length(); i++) {
            result[i] = data.get(i);
        }
        return result;
    }
}
```

**Memory Visibility Guarantee**: Operations on atomic variables establish happens-before relationships similar to volatile variables.

### Approach 4: Using java.util.concurrent.locks Package

Explicit locks from the `java.util.concurrent.locks` package provide more flexible locking with the same memory visibility guarantees as synchronized blocks.

```java
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadWriteLockSharedState {
    private boolean initialized = false;
    private int[] data;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    
    public void initialize() {
        lock.writeLock().lock();
        try {
            if (!initialized) {
                data = new int[10];
                for (int i = 0; i < data.length; i++) {
                    data[i] = i;
                }
                initialized = true;
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
    
    public int[] readData() {
        lock.readLock().lock();
        try {
            if (!initialized) {
                return null;
            }
            return data.clone();
        } finally {
            lock.readLock().unlock();
        }
    }
}
```

**Memory Visibility Guarantee**: The JMM ensures that when a thread releases a lock, all its writes are visible to any thread that subsequently acquires the lock.

### Approach 5: Using Final Fields

Final fields have special memory visibility guarantees in the JMM. If an object is properly published, all final fields of that object are guaranteed to be visible to all threads.

```java
public class ImmutableSharedState {
    private final int[] data;
    
    public ImmutableSharedState(int size) {
        data = new int[size];
        for (int i = 0; i < size; i++) {
            data[i] = i;
        }
    }
    
    public int[] getData() {
        return data.clone(); // Defensive copy to maintain immutability
    }
}
```

**Memory Visibility Guarantee**: The JMM ensures that final fields are correctly initialized before any thread can access the newly constructed object.

## Advanced Implementation: Thread-Safe Lazy Initialization with Double-Checked Locking

Double-checked locking is a pattern that reduces the overhead of synchronization by checking a condition before acquiring a lock and then checking it again after acquiring the lock.

```java
public class SafeDoubleCheckedLocking {
    private static volatile SafeDoubleCheckedLocking instance;
    private final int[] data;
    
    private SafeDoubleCheckedLocking() {
        data = new int[10];
        for (int i = 0; i < data.length; i++) {
            data[i] = i;
        }
    }
    
    public static SafeDoubleCheckedLocking getInstance() {
        // First check (no locking)
        if (instance == null) {
            synchronized (SafeDoubleCheckedLocking.class) {
                // Second check (with locking)
                if (instance == null) {
                    instance = new SafeDoubleCheckedLocking();
                }
            }
        }
        return instance;
    }
    
    public int[] getData() {
        return data.clone();
    }
}
```

**Memory Visibility Guarantee**: The volatile keyword on `instance` ensures that the initialization of the object is fully visible to all threads that read the reference.

## Testing the Implementation

```java
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MemoryModelTest {
    public static void main(String[] args) throws InterruptedException {
        final int numThreads = 10;
        final VolatileSharedState state = new VolatileSharedState();
        final CountDownLatch initLatch = new CountDownLatch(1);
        final CountDownLatch readLatch = new CountDownLatch(numThreads);
        
        // Thread that initializes the state
        Thread initThread = new Thread(() -> {
            state.initialize();
            initLatch.countDown();
        });
        
        // Threads that read the state
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        for (int i = 0; i < numThreads; i++) {
            executor.submit(() -> {
                try {
                    initLatch.await(); // Wait for initialization
                    int[] data = state.readData();
                    // Verify data is correctly visible
                    if (data != null) {
                        for (int j = 0; j < data.length; j++) {
                            if (data[j] != j) {
                                System.err.println("Memory visibility error detected!");
                                break;
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } finally {
                    readLatch.countDown();
                }
            });
        }
        
        initThread.start();
        readLatch.await();
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);
        
        System.out.println("Test completed successfully");
    }
}
```

## Key Insights for Interviews

1. **Java Memory Model (JMM)**: The JMM defines how changes made by one thread become visible to other threads. It provides a framework for understanding memory visibility in concurrent Java programs.

2. **Happens-Before Relationship**: This is the key concept in the JMM. If action A happens-before action B, then the effects of A are visible to B. The JMM defines several ways to establish happens-before relationships:
   - Program order: Each action in a thread happens-before every subsequent action in the same thread
   - Monitor lock: Unlocking a monitor happens-before every subsequent locking of that same monitor
   - Volatile field: A write to a volatile field happens-before every subsequent read of that field
   - Thread start: A call to `Thread.start()` happens-before any actions in the started thread
   - Thread termination: All actions in a thread happen-before another thread detects that the thread has terminated (e.g., via `Thread.join()`)
   - Transitivity: If A happens-before B and B happens-before C, then A happens-before C

3. **Memory Barriers**: The JMM uses memory barriers to enforce ordering constraints:
   - LoadLoad barriers: Ensure loads before the barrier complete before loads after the barrier
   - StoreStore barriers: Ensure stores before the barrier complete before stores after the barrier
   - LoadStore barriers: Ensure loads before the barrier complete before stores after the barrier
   - StoreLoad barriers: Ensure stores before the barrier complete before loads after the barrier

4. **Reordering**: The JVM and CPU may reorder operations for performance optimization as long as the happens-before relationships are preserved.

## Real-world Applications

1. High-performance concurrent data structures
2. Lock-free algorithms
3. Custom synchronization mechanisms
4. Performance-critical systems where minimizing synchronization overhead is important

## Video Explanation

[Java Memory Model Explained](https://www.youtube.com/watch?v=WTVooKLLVT8) - 9:45

## Interview Tips

- Demonstrate a deep understanding of the Java Memory Model and happens-before relationships
- Explain the differences between various synchronization mechanisms and their memory visibility guarantees
- Be prepared to discuss common concurrency bugs related to memory visibility
- Understand the trade-offs between different approaches to establishing happens-before relationships
- Be able to identify and fix incorrect synchronization in code examples


### Fork/Join Framework


## Problem Description

Design and implement a parallel algorithm using Java's Fork/Join framework to efficiently process a large dataset. The Fork/Join framework is designed for tasks that can be broken down into smaller subtasks and processed recursively in parallel.

In this problem, you'll implement a parallel merge sort algorithm that sorts a large array by dividing it into smaller subarrays, sorting them in parallel, and then merging the results.

## Requirements

1. Implement a `ParallelMergeSort` class that uses the Fork/Join framework to sort an array of integers.
2. The implementation must:
   - Correctly divide the problem into subtasks
   - Process subtasks in parallel using the Fork/Join framework
   - Combine results efficiently
   - Demonstrate performance improvements over sequential processing for large arrays

## Solution Approaches

### Approach 1: Basic Fork/Join Implementation

This approach uses the Fork/Join framework's `RecursiveAction` class to implement a parallel merge sort algorithm.

```java
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class ParallelMergeSort {
    private static final int THRESHOLD = 1000; // Threshold for switching to sequential sort
    
    public static void sort(int[] array) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.invoke(new MergeSortTask(array, 0, array.length - 1));
    }
    
    private static class MergeSortTask extends RecursiveAction {
        private final int[] array;
        private final int start;
        private final int end;
        
        MergeSortTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }
        
        @Override
        protected void compute() {
            if (end - start <= THRESHOLD) {
                // For small arrays, use sequential sort
                Arrays.sort(array, start, end + 1);
                return;
            }
            
            int mid = start + (end - start) / 2;
            
            // Fork subtasks
            MergeSortTask leftTask = new MergeSortTask(array, start, mid);
            MergeSortTask rightTask = new MergeSortTask(array, mid + 1, end);
            
            invokeAll(leftTask, rightTask);
            
            // Merge results
            merge(array, start, mid, end);
        }
        
        private void merge(int[] array, int start, int mid, int end) {
            int[] temp = new int[end - start + 1];
            int i = start, j = mid + 1, k = 0;
            
            // Merge the two sorted subarrays
            while (i <= mid && j <= end) {
                if (array[i] <= array[j]) {
                    temp[k++] = array[i++];
                } else {
                    temp[k++] = array[j++];
                }
            }
            
            // Copy remaining elements
            while (i <= mid) {
                temp[k++] = array[i++];
            }
            
            while (j <= end) {
                temp[k++] = array[j++];
            }
            
            // Copy back to original array
            System.arraycopy(temp, 0, array, start, temp.length);
        }
    }
}
```

**Time Complexity**: O(n log n) where n is the size of the array.
**Space Complexity**: O(n) for the temporary arrays used during merging.
**Parallelism**: The algorithm divides the problem into smaller subtasks that can be processed in parallel, potentially utilizing all available CPU cores.

### Approach 2: Using RecursiveTask for Result Computation

This approach uses `RecursiveTask` instead of `RecursiveAction` to return the sorted array, which can be useful when the result needs to be used elsewhere.

```java
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelMergeSortWithResult {
    private static final int THRESHOLD = 1000;
    
    public static int[] sort(int[] array) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        return pool.invoke(new MergeSortTask(array));
    }
    
    private static class MergeSortTask extends RecursiveTask<int[]> {
        private final int[] array;
        
        MergeSortTask(int[] array) {
            this.array = array;
        }
        
        @Override
        protected int[] compute() {
            if (array.length <= THRESHOLD) {
                // For small arrays, use sequential sort
                Arrays.sort(array);
                return array;
            }
            
            int mid = array.length / 2;
            
            // Split the array
            int[] left = Arrays.copyOfRange(array, 0, mid);
            int[] right = Arrays.copyOfRange(array, mid, array.length);
            
            // Fork subtasks
            MergeSortTask leftTask = new MergeSortTask(left);
            MergeSortTask rightTask = new MergeSortTask(right);
            
            leftTask.fork();
            int[] rightResult = rightTask.compute();
            int[] leftResult = leftTask.join();
            
            // Merge results
            return merge(leftResult, rightResult);
        }
        
        private int[] merge(int[] left, int[] right) {
            int[] result = new int[left.length + right.length];
            int i = 0, j = 0, k = 0;
            
            while (i < left.length && j < right.length) {
                if (left[i] <= right[j]) {
                    result[k++] = left[i++];
                } else {
                    result[k++] = right[j++];
                }
            }
            
            while (i < left.length) {
                result[k++] = left[i++];
            }
            
            while (j < right.length) {
                result[k++] = right[j++];
            }
            
            return result;
        }
    }
}
```

### Approach 3: Work-Stealing with Custom Parallelism Threshold

This approach dynamically adjusts the parallelism threshold based on the available processors and the size of the array.

```java
import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class AdaptiveParallelMergeSort {
    private static final int SEQUENTIAL_THRESHOLD = 1000;
    
    public static void sort(int[] array) {
        int parallelismLevel = Runtime.getRuntime().availableProcessors();
        ForkJoinPool pool = new ForkJoinPool(parallelismLevel);
        pool.invoke(new MergeSortTask(array, 0, array.length - 1, 
                                      determineThreshold(array.length, parallelismLevel)));
    }
    
    private static int determineThreshold(int arraySize, int parallelismLevel) {
        // Adjust threshold based on array size and available processors
        return Math.max(SEQUENTIAL_THRESHOLD, arraySize / (parallelismLevel * 2));
    }
    
    private static class MergeSortTask extends RecursiveAction {
        private final int[] array;
        private final int start;
        private final int end;
        private final int threshold;
        
        MergeSortTask(int[] array, int start, int end, int threshold) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.threshold = threshold;
        }
        
        @Override
        protected void compute() {
            if (end - start <= threshold) {
                Arrays.sort(array, start, end + 1);
                return;
            }
            
            int mid = start + (end - start) / 2;
            
            // Create subtasks with the same threshold
            invokeAll(
                new MergeSortTask(array, start, mid, threshold),
                new MergeSortTask(array, mid + 1, end, threshold)
            );
            
            merge(array, start, mid, end);
        }
        
        private void merge(int[] array, int start, int mid, int end) {
            int[] temp = new int[end - start + 1];
            int i = start, j = mid + 1, k = 0;
            
            while (i <= mid && j <= end) {
                if (array[i] <= array[j]) {
                    temp[k++] = array[i++];
                } else {
                    temp[k++] = array[j++];
                }
            }
            
            while (i <= mid) {
                temp[k++] = array[i++];
            }
            
            while (j <= end) {
                temp[k++] = array[j++];
            }
            
            System.arraycopy(temp, 0, array, start, temp.length);
        }
    }
}
```

## Advanced Implementation: Parallel Array Processing

Let's extend the problem to implement a more general parallel array processing framework using Fork/Join.

```java
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.function.Consumer;

public class ParallelArrayProcessor {
    private static final int THRESHOLD = 1000;
    
    public static <T> void process(T[] array, Consumer<T> action) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.invoke(new ArrayProcessTask<>(array, 0, array.length - 1, action));
    }
    
    private static class ArrayProcessTask<T> extends RecursiveAction {
        private final T[] array;
        private final int start;
        private final int end;
        private final Consumer<T> action;
        
        ArrayProcessTask(T[] array, int start, int end, Consumer<T> action) {
            this.array = array;
            this.start = start;
            this.end = end;
            this.action = action;
        }
        
        @Override
        protected void compute() {
            if (end - start <= THRESHOLD) {
                // Process elements sequentially
                for (int i = start; i <= end; i++) {
                    action.accept(array[i]);
                }
                return;
            }
            
            int mid = start + (end - start) / 2;
            
            // Fork subtasks
            invokeAll(
                new ArrayProcessTask<>(array, start, mid, action),
                new ArrayProcessTask<>(array, mid + 1, end, action)
            );
        }
    }
    
    // Example: Parallel map operation
    public static <T, R> R[] map(T[] input, R[] output, java.util.function.Function<T, R> mapper) {
        ForkJoinPool pool = ForkJoinPool.commonPool();
        pool.invoke(new MapTask<>(input, output, 0, input.length - 1, mapper));
        return output;
    }
    
    private static class MapTask<T, R> extends RecursiveAction {
        private final T[] input;
        private final R[] output;
        private final int start;
        private final int end;
        private final java.util.function.Function<T, R> mapper;
        
        MapTask(T[] input, R[] output, int start, int end, java.util.function.Function<T, R> mapper) {
            this.input = input;
            this.output = output;
            this.start = start;
            this.end = end;
            this.mapper = mapper;
        }
        
        @Override
        protected void compute() {
            if (end - start <= THRESHOLD) {
                for (int i = start; i <= end; i++) {
                    output[i] = mapper.apply(input[i]);
                }
                return;
            }
            
            int mid = start + (end - start) / 2;
            
            invokeAll(
                new MapTask<>(input, output, start, mid, mapper),
                new MapTask<>(input, output, mid + 1, end, mapper)
            );
        }
    }
}
```

## Testing the Implementation

```java
import java.util.Arrays;
import java.util.Random;

public class ForkJoinTest {
    public static void main(String[] args) {
        // Generate a large random array
        int size = 10_000_000;
        int[] array = new Random().ints(size).toArray();
        int[] arrayCopy = Arrays.copyOf(array, array.length);
        
        // Test sequential sort
        long startSeq = System.currentTimeMillis();
        Arrays.sort(arrayCopy);
        long endSeq = System.currentTimeMillis();
        System.out.println("Sequential sort time: " + (endSeq - startSeq) + " ms");
        
        // Test parallel sort
        long startPar = System.currentTimeMillis();
        ParallelMergeSort.sort(array);
        long endPar = System.currentTimeMillis();
        System.out.println("Parallel sort time: " + (endPar - startPar) + " ms");
        
        // Verify correctness
        boolean correct = Arrays.equals(array, arrayCopy);
        System.out.println("Sort correct: " + correct);
        
        // Test parallel array processing
        Integer[] objects = new Integer[size];
        for (int i = 0; i < size; i++) {
            objects[i] = i;
        }
        
        Integer[] result = new Integer[size];
        long startMap = System.currentTimeMillis();
        ParallelArrayProcessor.map(objects, result, x -> x * x);
        long endMap = System.currentTimeMillis();
        System.out.println("Parallel map time: " + (endMap - startMap) + " ms");
    }
}
```

## Key Insights for Interviews

1. **Fork/Join Framework**: Designed for divide-and-conquer algorithms where tasks can be split into smaller subtasks that can be processed in parallel.

2. **Work Stealing**: The Fork/Join framework uses a work-stealing algorithm where idle threads "steal" tasks from the queues of busy threads, improving load balancing.

3. **RecursiveAction vs. RecursiveTask**:
   - `RecursiveAction`: For tasks that don't return a result
   - `RecursiveTask`: For tasks that return a result

4. **Threshold Selection**: Choosing an appropriate threshold for switching to sequential processing is crucial for performance. Too small a threshold creates excessive overhead from task creation, while too large a threshold limits parallelism.

5. **Common Pool**: The `ForkJoinPool.commonPool()` provides a shared pool that's suitable for most applications, but custom pools can be created for specific needs.

6. **Fork and Join Operations**:
   - `fork()`: Submits a task for asynchronous execution
   - `join()`: Waits for a forked task to complete and returns its result
   - `invokeAll()`: Forks multiple tasks and waits for all to complete

## Real-world Applications

1. Parallel sorting and searching of large datasets
2. Image and video processing
3. Scientific computing and simulations
4. Big data processing frameworks
5. Parallel file system operations

## Video Explanation

[Java Fork/Join Framework](https://www.youtube.com/watch?v=FjSHNAQMdxQ) - 7:15

## Interview Tips

- Demonstrate understanding of when to use the Fork/Join framework versus other concurrency approaches
- Explain the work-stealing algorithm and its benefits
- Discuss the importance of choosing appropriate thresholds for task splitting
- Be prepared to analyze the performance characteristics of Fork/Join solutions
- Understand common pitfalls like excessive task creation or improper joining of tasks


### CompletableFuture for Asynchronous Programming


## Problem Description

Design and implement a robust asynchronous processing system using Java's CompletableFuture API. The system should handle multiple asynchronous operations, compose them together, and manage error handling and timeouts effectively.

CompletableFuture, introduced in Java 8, provides a powerful way to write asynchronous, non-blocking code. It allows for composing multiple asynchronous operations, handling exceptions, and controlling execution with a rich set of methods.

## Requirements

1. Implement an `AsyncProcessor` class that:
   - Fetches data from multiple sources asynchronously
   - Processes the data in parallel
   - Combines the results
   - Handles errors and timeouts gracefully
   - Provides proper cancellation support

2. The implementation must demonstrate:
   - Chaining of asynchronous operations
   - Combining multiple CompletableFutures
   - Exception handling strategies
   - Timeout management
   - Custom thread pool usage

## Solution Approaches

### Approach 1: Basic CompletableFuture Chaining

This approach demonstrates the basic chaining of asynchronous operations using CompletableFuture.

```java
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BasicAsyncProcessor {
    private final ExecutorService executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors());
    
    public CompletableFuture<String> processAsync(String input) {
        return CompletableFuture.supplyAsync(() -> {
            // Simulate fetching data from a remote service
            try {
                TimeUnit.MILLISECONDS.sleep(100);
                return "Fetched: " + input;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Fetch operation interrupted", e);
            }
        }, executor).thenApplyAsync(fetchedData -> {
            // Simulate processing the fetched data
            try {
                TimeUnit.MILLISECONDS.sleep(150);
                return "Processed: " + fetchedData;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Process operation interrupted", e);
            }
        }, executor).exceptionally(ex -> {
            // Handle any exceptions
            return "Error: " + ex.getMessage();
        });
    }
    
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
```

### Approach 2: Combining Multiple CompletableFutures

This approach demonstrates how to combine results from multiple asynchronous operations.

```java
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class CombiningAsyncProcessor {
    private final ExecutorService executor = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors());
    
    public CompletableFuture<List<String>> fetchAllAsync(List<String> sources) {
        // Create a CompletableFuture for each source
        List<CompletableFuture<String>> futures = sources.stream()
                .map(this::fetchAsync)
                .collect(Collectors.toList());
        
        // Combine all futures into a single future that completes when all complete
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0]));
        
        // When all futures complete, collect their results
        return allFutures.thenApply(v -> 
                futures.stream()
                       .map(CompletableFuture::join)
                       .collect(Collectors.toList())
        );
    }
    
    public CompletableFuture<List<String>> fetchAnyAsync(List<String> sources) {
        // Create a CompletableFuture for each source
        List<CompletableFuture<String>> futures = sources.stream()
                .map(this::fetchAsync)
                .collect(Collectors.toList());
        
        // Create a CompletableFuture that completes when any of the futures complete
        CompletableFuture<Object> anyFuture = CompletableFuture.anyOf(
                futures.toArray(new CompletableFuture[0]));
        
        // Return the result of the first completed future
        return anyFuture.thenApply(result -> Arrays.asList((String) result));
    }
    
    private CompletableFuture<String> fetchAsync(String source) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                // Simulate variable fetch times
                long fetchTime = (long) (Math.random() * 500);
                TimeUnit.MILLISECONDS.sleep(fetchTime);
                return "Data from " + source + " (fetched in " + fetchTime + "ms)";
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Fetch operation interrupted", e);
            }
        }, executor);
    }
    
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(2, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
```

### Approach 3: Comprehensive Async Processing with Error Handling and Timeouts

This approach demonstrates a more comprehensive solution with error handling, timeouts, and cancellation support.

```java
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ComprehensiveAsyncProcessor {
    private final ExecutorService executor;
    
    public ComprehensiveAsyncProcessor(int threadPoolSize) {
        this.executor = Executors.newFixedThreadPool(threadPoolSize);
    }
    
    public <T, R> CompletableFuture<R> processWithTimeout(
            T input, 
            Function<T, R> processor, 
            long timeout, 
            TimeUnit unit) {
        
        CompletableFuture<R> future = CompletableFuture.supplyAsync(() -> {
            try {
                return processor.apply(input);
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }, executor);
        
        // Add timeout
        CompletableFuture<R> timeoutFuture = failAfterTimeout(future, timeout, unit);
        
        // Add error recovery
        return timeoutFuture.exceptionally(ex -> {
            if (ex instanceof TimeoutException) {
                System.err.println("Operation timed out: " + ex.getMessage());
            } else {
                System.err.println("Operation failed: " + ex.getMessage());
            }
            return null; // Or provide a default value
        });
    }
    
    public <T> CompletableFuture<List<T>> processAllWithTimeout(
            List<CompletableFuture<T>> futures, 
            long timeout, 
            TimeUnit unit) {
        
        // Create a future that completes when all futures complete
        CompletableFuture<Void> allFutures = CompletableFuture.allOf(
                futures.toArray(new CompletableFuture[0]));
        
        // Add timeout to the combined future
        CompletableFuture<Void> timeoutFuture = failAfterTimeout(allFutures, timeout, unit);
        
        // When all futures complete or timeout occurs, collect results
        return timeoutFuture.thenApply(v -> 
                futures.stream()
                       .map(future -> {
                           try {
                               return future.getNow(null); // Get result if available
                           } catch (Exception e) {
                               return null;
                           }
                       })
                       .collect(Collectors.toList())
        ).exceptionally(ex -> {
            if (ex instanceof TimeoutException) {
                System.err.println("Combined operation timed out: " + ex.getMessage());
                
                // Cancel all incomplete futures
                futures.forEach(future -> future.cancel(true));
                
                // Return partial results
                return futures.stream()
                        .map(future -> future.isDone() && !future.isCompletedExceptionally() ? 
                                future.join() : null)
                        .collect(Collectors.toList());
            } else {
                System.err.println("Combined operation failed: " + ex.getMessage());
                return null;
            }
        });
    }
    
    private <T> CompletableFuture<T> failAfterTimeout(
            CompletableFuture<T> future, 
            long timeout, 
            TimeUnit unit) {
        
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        
        CompletableFuture<T> timeoutFuture = new CompletableFuture<>();
        
        // Schedule a task to complete the future with a timeout exception
        ScheduledFuture<?> timeoutTask = scheduler.schedule(() -> {
            TimeoutException ex = new TimeoutException("Operation timed out after " + timeout + " " + unit);
            timeoutFuture.completeExceptionally(ex);
            future.cancel(true); // Cancel the original future
        }, timeout, unit);
        
        // When the original future completes, complete the timeout future and cancel the timeout task
        future.whenComplete((result, ex) -> {
            if (ex != null) {
                timeoutFuture.completeExceptionally(ex);
            } else {
                timeoutFuture.complete(result);
            }
            timeoutTask.cancel(false);
        });
        
        // Ensure the scheduler is shut down
        timeoutFuture.whenComplete((result, ex) -> scheduler.shutdown());
        
        return timeoutFuture;
    }
    
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
```

## Advanced Implementation: Asynchronous REST API Client

Let's implement a more practical example: an asynchronous REST API client that fetches and processes data from multiple endpoints.

```java
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class AsyncApiClient {
    private final HttpClient httpClient;
    private final ExecutorService executor;
    
    public AsyncApiClient() {
        this.executor = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors());
        
        this.httpClient = HttpClient.newBuilder()
                .executor(executor)
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }
    
    public CompletableFuture<String> fetchDataAsync(String url) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(5))
                .GET()
                .build();
        
        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .exceptionally(ex -> {
                    System.err.println("Error fetching data from " + url + ": " + ex.getMessage());
                    return null;
                });
    }
    
    public CompletableFuture<List<String>> fetchAllDataAsync(List<String> urls) {
        List<CompletableFuture<String>> futures = urls.stream()
                .map(this::fetchDataAsync)
                .collect(Collectors.toList());
        
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));
    }
    
    public <T> CompletableFuture<T> processDataAsync(
            String url, 
            Function<String, T> processor) {
        
        return fetchDataAsync(url)
                .thenApplyAsync(processor, executor)
                .exceptionally(ex -> {
                    System.err.println("Error processing data: " + ex.getMessage());
                    return null;
                });
    }
    
    public <T> CompletableFuture<List<T>> processAllDataAsync(
            List<String> urls, 
            Function<List<String>, List<T>> processor) {
        
        return fetchAllDataAsync(urls)
                .thenApplyAsync(processor, executor)
                .exceptionally(ex -> {
                    System.err.println("Error processing all data: " + ex.getMessage());
                    return null;
                });
    }
    
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
```

## Testing the Implementation

```java
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class CompletableFutureTest {
    public static void main(String[] args) throws Exception {
        // Test basic async processing
        BasicAsyncProcessor basicProcessor = new BasicAsyncProcessor();
        CompletableFuture<String> basicResult = basicProcessor.processAsync("test-data");
        System.out.println("Basic result: " + basicResult.get());
        basicProcessor.shutdown();
        
        // Test combining async operations
        CombiningAsyncProcessor combiningProcessor = new CombiningAsyncProcessor();
        List<String> sources = Arrays.asList("source1", "source2", "source3", "source4");
        
        CompletableFuture<List<String>> allResults = combiningProcessor.fetchAllAsync(sources);
        System.out.println("All results: " + allResults.get());
        
        CompletableFuture<List<String>> anyResult = combiningProcessor.fetchAnyAsync(sources);
        System.out.println("Any result: " + anyResult.get());
        
        combiningProcessor.shutdown();
        
        // Test comprehensive async processing with timeouts
        ComprehensiveAsyncProcessor comprehensiveProcessor = 
                new ComprehensiveAsyncProcessor(4);
        
        // Test successful processing
        CompletableFuture<String> successResult = comprehensiveProcessor.processWithTimeout(
                "quick-process",
                input -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(100);
                        return "Processed: " + input;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                },
                1000,
                TimeUnit.MILLISECONDS
        );
        
        System.out.println("Success result: " + successResult.get());
        
        // Test timeout
        CompletableFuture<String> timeoutResult = comprehensiveProcessor.processWithTimeout(
                "slow-process",
                input -> {
                    try {
                        TimeUnit.MILLISECONDS.sleep(2000);
                        return "Processed: " + input;
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        throw new RuntimeException(e);
                    }
                },
                500,
                TimeUnit.MILLISECONDS
        );
        
        System.out.println("Timeout result: " + timeoutResult.get(3, TimeUnit.SECONDS));
        
        comprehensiveProcessor.shutdown();
    }
}
```

## Key Insights for Interviews

1. **CompletableFuture Basics**:
   - `CompletableFuture` implements both `Future` and `CompletionStage` interfaces
   - Can be explicitly completed using `complete()` or `completeExceptionally()`
   - Supports non-blocking composition of asynchronous operations

2. **Creation Methods**:
   - `CompletableFuture.supplyAsync()`: Creates a future that executes a supplier asynchronously
   - `CompletableFuture.runAsync()`: Creates a future that executes a runnable asynchronously
   - `CompletableFuture.completedFuture()`: Creates a future that's already completed with a given value

3. **Composition Methods**:
   - `thenApply()`: Transforms the result using a function
   - `thenAccept()`: Consumes the result without returning a value
   - `thenRun()`: Executes a Runnable after completion, ignoring the result
   - `thenCompose()`: Chains futures by using the result of one future to create another
   - `thenCombine()`: Combines the results of two independent futures

4. **Execution Control**:
   - Methods ending with `Async` execute in the default or specified executor
   - Methods without `Async` execute in the thread that completed the previous stage
   - Custom executors can be provided for fine-grained control

5. **Error Handling**:
   - `exceptionally()`: Handles exceptions and provides a recovery value
   - `handle()`: Processes both the result and exception (if any)
   - `whenComplete()`: Performs an action when the future completes, without changing the result

6. **Combining Multiple Futures**:
   - `allOf()`: Creates a future that completes when all specified futures complete
   - `anyOf()`: Creates a future that completes when any of the specified futures complete

7. **Timeout Handling**:
   - Java 9+ provides `orTimeout()` and `completeOnTimeout()` methods
   - For earlier versions, custom timeout handling is needed

## Real-world Applications

1. Microservices architecture where multiple services need to be called asynchronously
2. Web applications that need to fetch data from multiple sources
3. Batch processing systems that need to process large amounts of data in parallel
4. Real-time data processing systems
5. Responsive user interfaces that need to perform background operations

## Video Explanation

[CompletableFuture in Java](https://www.youtube.com/watch?v=ImtZgX1nmr8) - 8:30

## Interview Tips

- Demonstrate understanding of the differences between CompletableFuture and traditional Future
- Explain the benefits of non-blocking asynchronous programming
- Discuss strategies for error handling in asynchronous code
- Be prepared to implement solutions that combine multiple asynchronous operations
- Understand the performance implications of different CompletableFuture methods
- Be able to explain how to handle timeouts and cancellation in asynchronous operations


### Distributed Concurrency Patterns


## Problem Description

Design and implement a distributed coordination system that allows multiple processes or services to work together reliably across a network. The system should handle distributed locking, leader election, and distributed consensus to ensure consistency and fault tolerance.

Distributed concurrency patterns are essential for building reliable distributed systems where multiple nodes need to coordinate actions, maintain consistent state, or agree on values despite network partitions, node failures, and message delays.

## Requirements

1. Implement a `DistributedCoordinator` that provides:
   - Distributed locking mechanism
   - Leader election protocol
   - Distributed consensus algorithm
   - Fault detection and recovery

2. The implementation must:
   - Handle network partitions gracefully
   - Recover from node failures
   - Provide strong consistency guarantees where needed
   - Scale with the number of nodes
   - Include proper timeout and retry mechanisms

## Solution Approaches

### Approach 1: Distributed Locking with ZooKeeper

This approach uses Apache ZooKeeper, a centralized service for maintaining configuration information, naming, and distributed synchronization.

```java
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ZooKeeperDistributedLock {
    private final ZooKeeper zooKeeper;
    private final String lockPath;
    private String currentLockNode;
    private final CountDownLatch connectedSignal = new CountDownLatch(1);
    
    public ZooKeeperDistributedLock(String connectionString, String lockPath) throws Exception {
        this.lockPath = lockPath;
        
        // Create ZooKeeper client
        zooKeeper = new ZooKeeper(connectionString, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    connectedSignal.countDown();
                }
            }
        });
        
        // Wait for connection
        connectedSignal.await(10, TimeUnit.SECONDS);
        
        // Ensure lock path exists
        Stat stat = zooKeeper.exists(lockPath, false);
        if (stat == null) {
            try {
                zooKeeper.create(lockPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, 
                                 CreateMode.PERSISTENT);
            } catch (KeeperException.NodeExistsException e) {
                // Node already exists, ignore
            }
        }
    }
    
    public boolean acquireLock(long timeout, TimeUnit unit) throws Exception {
        final CountDownLatch lockAcquired = new CountDownLatch(1);
        
        // Create an ephemeral sequential node
        String nodePath = lockPath + "/lock-";
        currentLockNode = zooKeeper.create(nodePath, new byte[0], 
                                          ZooDefs.Ids.OPEN_ACL_UNSAFE, 
                                          CreateMode.EPHEMERAL_SEQUENTIAL);
        
        // Extract the sequence number from the created node
        String nodeId = currentLockNode.substring(nodePath.length());
        
        // Get all children of the lock path
        List<String> children = zooKeeper.getChildren(lockPath, false);
        
        // Sort the children
        Collections.sort(children);
        
        // If our node is the first one, we have the lock
        if ((lockPath + "/lock-" + children.get(0)).equals(currentLockNode)) {
            return true;
        }
        
        // Find the node that comes before ours
        String watchNode = null;
        for (int i = 0; i < children.size(); i++) {
            String child = children.get(i);
            if ((lockPath + "/lock-" + child).equals(currentLockNode)) {
                watchNode = children.get(i - 1);
                break;
            }
        }
        
        // Watch the node that comes before ours
        Stat stat = zooKeeper.exists(lockPath + "/lock-" + watchNode, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getType() == Event.EventType.NodeDeleted) {
                    lockAcquired.countDown();
                }
            }
        });
        
        // If the node doesn't exist anymore, we have the lock
        if (stat == null) {
            return true;
        }
        
        // Wait for the lock to be acquired
        return lockAcquired.await(timeout, unit);
    }
    
    public void releaseLock() throws Exception {
        if (currentLockNode != null) {
            try {
                zooKeeper.delete(currentLockNode, -1);
                currentLockNode = null;
            } catch (KeeperException.NoNodeException e) {
                // Node already deleted, ignore
            }
        }
    }
    
    public void close() throws InterruptedException {
        zooKeeper.close();
    }
}
```

### Approach 2: Leader Election with ZooKeeper

This approach implements a leader election protocol using ZooKeeper.

```java
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ZooKeeperLeaderElection {
    private final ZooKeeper zooKeeper;
    private final String electionPath;
    private String currentNode;
    private final CountDownLatch connectedSignal = new CountDownLatch(1);
    private final LeadershipChangeListener listener;
    private boolean isLeader = false;
    
    public interface LeadershipChangeListener {
        void onBecomeLeader();
        void onLoseLeadership();
    }
    
    public ZooKeeperLeaderElection(String connectionString, String electionPath, 
                                  LeadershipChangeListener listener) throws Exception {
        this.electionPath = electionPath;
        this.listener = listener;
        
        // Create ZooKeeper client
        zooKeeper = new ZooKeeper(connectionString, 5000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    connectedSignal.countDown();
                }
            }
        });
        
        // Wait for connection
        connectedSignal.await(10, TimeUnit.SECONDS);
        
        // Ensure election path exists
        Stat stat = zooKeeper.exists(electionPath, false);
        if (stat == null) {
            try {
                zooKeeper.create(electionPath, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, 
                                 CreateMode.PERSISTENT);
            } catch (KeeperException.NodeExistsException e) {
                // Node already exists, ignore
            }
        }
    }
    
    public void start() throws Exception {
        // Create an ephemeral sequential node
        String nodePath = electionPath + "/node-";
        currentNode = zooKeeper.create(nodePath, new byte[0], 
                                      ZooDefs.Ids.OPEN_ACL_UNSAFE, 
                                      CreateMode.EPHEMERAL_SEQUENTIAL);
        
        // Check if we are the leader
        checkLeadership();
    }
    
    private void checkLeadership() throws Exception {
        // Get all children of the election path
        List<String> children = zooKeeper.getChildren(electionPath, false);
        
        // Sort the children
        Collections.sort(children);
        
        // The node with the lowest sequence number is the leader
        String leaderNode = electionPath + "/" + children.get(0);
        
        // If our node is the leader node, we are the leader
        if (currentNode.equals(leaderNode)) {
            if (!isLeader) {
                isLeader = true;
                listener.onBecomeLeader();
            }
        } else {
            if (isLeader) {
                isLeader = false;
                listener.onLoseLeadership();
            }
            
            // Find the node that comes before ours
            String watchNode = null;
            for (int i = 0; i < children.size(); i++) {
                String child = children.get(i);
                if ((electionPath + "/" + child).equals(currentNode)) {
                    watchNode = children.get(i - 1);
                    break;
                }
            }
            
            // Watch the node that comes before ours
            final String nodeToWatch = electionPath + "/" + watchNode;
            zooKeeper.exists(nodeToWatch, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getType() == Event.EventType.NodeDeleted) {
                        try {
                            checkLeadership();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        }
    }
    
    public boolean isLeader() {
        return isLeader;
    }
    
    public void stop() throws Exception {
        if (currentNode != null) {
            try {
                zooKeeper.delete(currentNode, -1);
                currentNode = null;
                isLeader = false;
            } catch (KeeperException.NoNodeException e) {
                // Node already deleted, ignore
            }
        }
    }
    
    public void close() throws InterruptedException {
        zooKeeper.close();
    }
}
```

### Approach 3: Distributed Consensus with Raft Algorithm

This approach implements a simplified version of the Raft consensus algorithm, which is designed to be more understandable than Paxos while providing the same guarantees.

```java
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;

public class RaftConsensus {
    // Node states
    public enum NodeState {
        FOLLOWER, CANDIDATE, LEADER
    }
    
    // RPC messages
    public static class AppendEntriesRequest {
        final long term;
        final String leaderId;
        final long prevLogIndex;
        final long prevLogTerm;
        final List<LogEntry> entries;
        final long leaderCommit;
        
        public AppendEntriesRequest(long term, String leaderId, long prevLogIndex, 
                                   long prevLogTerm, List<LogEntry> entries, long leaderCommit) {
            this.term = term;
            this.leaderId = leaderId;
            this.prevLogIndex = prevLogIndex;
            this.prevLogTerm = prevLogTerm;
            this.entries = entries;
            this.leaderCommit = leaderCommit;
        }
    }
    
    public static class AppendEntriesResponse {
        final long term;
        final boolean success;
        
        public AppendEntriesResponse(long term, boolean success) {
            this.term = term;
            this.success = success;
        }
    }
    
    public static class RequestVoteRequest {
        final long term;
        final String candidateId;
        final long lastLogIndex;
        final long lastLogTerm;
        
        public RequestVoteRequest(long term, String candidateId, long lastLogIndex, long lastLogTerm) {
            this.term = term;
            this.candidateId = candidateId;
            this.lastLogIndex = lastLogIndex;
            this.lastLogTerm = lastLogTerm;
        }
    }
    
    public static class RequestVoteResponse {
        final long term;
        final boolean voteGranted;
        
        public RequestVoteResponse(long term, boolean voteGranted) {
            this.term = term;
            this.voteGranted = voteGranted;
        }
    }
    
    public static class LogEntry {
        final long term;
        final String command;
        
        public LogEntry(long term, String command) {
            this.term = term;
            this.command = command;
        }
    }
    
    // RaftNode implementation
    public static class RaftNode {
        private final String nodeId;
        private final List<String> peerIds;
        private final RaftRpcService rpcService;
        private final ScheduledExecutorService scheduler;
        
        // Persistent state
        private long currentTerm;
        private String votedFor;
        private final List<LogEntry> log = new ArrayList<>();
        
        // Volatile state
        private NodeState state = NodeState.FOLLOWER;
        private long commitIndex = 0;
        private long lastApplied = 0;
        
        // Leader state
        private final Map<String, Long> nextIndex = new HashMap<>();
        private final Map<String, Long> matchIndex = new HashMap<>();
        
        // Timers
        private ScheduledFuture<?> electionTimer;
        private ScheduledFuture<?> heartbeatTimer;
        
        // Random election timeout
        private final Random random = new Random();
        private final int minElectionTimeout = 150;
        private final int maxElectionTimeout = 300;
        
        public RaftNode(String nodeId, List<String> peerIds, RaftRpcService rpcService) {
            this.nodeId = nodeId;
            this.peerIds = new ArrayList<>(peerIds);
            this.rpcService = rpcService;
            this.scheduler = Executors.newScheduledThreadPool(2);
            
            // Initialize leader state
            for (String peerId : peerIds) {
                nextIndex.put(peerId, 1L);
                matchIndex.put(peerId, 0L);
            }
            
            // Start as follower
            becomeFollower(0);
        }
        
        // State transitions
        private void becomeFollower(long term) {
            state = NodeState.FOLLOWER;
            currentTerm = term;
            votedFor = null;
            
            // Cancel leader heartbeat
            if (heartbeatTimer != null) {
                heartbeatTimer.cancel(false);
            }
            
            // Reset election timer
            resetElectionTimer();
        }
        
        private void becomeCandidate() {
            state = NodeState.CANDIDATE;
            currentTerm++;
            votedFor = nodeId;
            
            // Start election
            startElection();
        }
        
        private void becomeLeader() {
            if (state != NodeState.CANDIDATE) {
                return;
            }
            
            state = NodeState.LEADER;
            
            // Initialize leader state
            for (String peerId : peerIds) {
                nextIndex.put(peerId, log.size() + 1);
                matchIndex.put(peerId, 0L);
            }
            
            // Cancel election timer
            if (electionTimer != null) {
                electionTimer.cancel(false);
            }
            
            // Start sending heartbeats
            heartbeatTimer = scheduler.scheduleAtFixedRate(
                this::sendHeartbeats, 0, 50, TimeUnit.MILLISECONDS);
        }
        
        // Timer management
        private void resetElectionTimer() {
            if (electionTimer != null) {
                electionTimer.cancel(false);
            }
            
            int timeout = minElectionTimeout + random.nextInt(maxElectionTimeout - minElectionTimeout);
            electionTimer = scheduler.schedule(this::onElectionTimeout, timeout, TimeUnit.MILLISECONDS);
        }
        
        private void onElectionTimeout() {
            if (state != NodeState.LEADER) {
                becomeCandidate();
            }
        }
        
        // Election
        private void startElection() {
            // Vote for self
            int votesReceived = 1;
            
            // Reset election timer
            resetElectionTimer();
            
            // Send RequestVote RPCs to all peers
            for (String peerId : peerIds) {
                RequestVoteRequest request = new RequestVoteRequest(
                    currentTerm, nodeId, log.size(), log.isEmpty() ? 0 : log.get(log.size() - 1).term);
                
                CompletableFuture<RequestVoteResponse> future = rpcService.sendRequestVote(peerId, request);
                future.thenAccept(response -> {
                    // If response contains term > currentTerm, become follower
                    if (response.term > currentTerm) {
                        becomeFollower(response.term);
                        return;
                    }
                    
                    // Count votes
                    if (state == NodeState.CANDIDATE && response.voteGranted) {
                        synchronized (this) {
                            votesReceived++;
                            if (votesReceived > (peerIds.size() + 1) / 2) {
                                becomeLeader();
                            }
                        }
                    }
                });
            }
        }
        
        // Heartbeats and log replication
        private void sendHeartbeats() {
            for (String peerId : peerIds) {
                sendAppendEntries(peerId);
            }
        }
        
        private void sendAppendEntries(String peerId) {
            long prevLogIndex = nextIndex.get(peerId) - 1;
            long prevLogTerm = 0;
            if (prevLogIndex > 0 && prevLogIndex <= log.size()) {
                prevLogTerm = log.get((int) prevLogIndex - 1).term;
            }
            
            List<LogEntry> entries = new ArrayList<>();
            if (nextIndex.get(peerId) <= log.size()) {
                entries = log.subList((int) nextIndex.get(peerId) - 1, log.size());
            }
            
            AppendEntriesRequest request = new AppendEntriesRequest(
                currentTerm, nodeId, prevLogIndex, prevLogTerm, entries, commitIndex);
            
            CompletableFuture<AppendEntriesResponse> future = rpcService.sendAppendEntries(peerId, request);
            future.thenAccept(response -> {
                // If response contains term > currentTerm, become follower
                if (response.term > currentTerm) {
                    becomeFollower(response.term);
                    return;
                }
                
                if (state == NodeState.LEADER && response.success) {
                    // Update nextIndex and matchIndex for successful AppendEntries
                    nextIndex.put(peerId, prevLogIndex + entries.size() + 1);
                    matchIndex.put(peerId, prevLogIndex + entries.size());
                    
                    // Check if we can commit more entries
                    updateCommitIndex();
                } else if (state == NodeState.LEADER) {
                    // Decrement nextIndex and retry
                    nextIndex.put(peerId, Math.max(1, nextIndex.get(peerId) - 1));
                    sendAppendEntries(peerId);
                }
            });
        }
        
        private void updateCommitIndex() {
            // Find the highest log entry that has been replicated to a majority of servers
            for (long n = commitIndex + 1; n <= log.size(); n++) {
                int count = 1; // Count self
                for (String peerId : peerIds) {
                    if (matchIndex.get(peerId) >= n) {
                        count++;
                    }
                }
                
                if (count > (peerIds.size() + 1) / 2 && log.get((int) n - 1).term == currentTerm) {
                    commitIndex = n;
                }
            }
            
            // Apply committed entries to state machine
            while (lastApplied < commitIndex) {
                lastApplied++;
                applyLogEntry(log.get((int) lastApplied - 1));
            }
        }
        
        private void applyLogEntry(LogEntry entry) {
            // Apply the command to the state machine
            System.out.println("Node " + nodeId + " applying command: " + entry.command);
        }
        
        // Client API
        public CompletableFuture<Boolean> proposeCommand(String command) {
            if (state != NodeState.LEADER) {
                return CompletableFuture.completedFuture(false);
            }
            
            // Append to local log
            log.add(new LogEntry(currentTerm, command));
            
            // Update leader's matchIndex for itself
            matchIndex.put(nodeId, log.size());
            
            // Send AppendEntries to all peers
            sendHeartbeats();
            
            // Create a future to be completed when the command is committed
            CompletableFuture<Boolean> result = new CompletableFuture<>();
            
            // Start a background task to check if the command is committed
            final long index = log.size();
            AtomicLong lastCheckedCommitIndex = new AtomicLong(commitIndex);
            
            ScheduledFuture<?> checkTask = scheduler.scheduleAtFixedRate(() -> {
                if (commitIndex >= index) {
                    result.complete(true);
                    return;
                }
                
                if (state != NodeState.LEADER || lastCheckedCommitIndex.get() == commitIndex) {
                    // If we're no longer the leader or commit index hasn't changed, fail
                    result.complete(false);
                }
                
                lastCheckedCommitIndex.set(commitIndex);
            }, 50, 50, TimeUnit.MILLISECONDS);
            
            // Complete the future when done
            result.whenComplete((r, ex) -> checkTask.cancel(false));
            
            return result;
        }
        
        // RPC handlers
        public AppendEntriesResponse handleAppendEntries(AppendEntriesRequest request) {
            // If request term < currentTerm, reject
            if (request.term < currentTerm) {
                return new AppendEntriesResponse(currentTerm, false);
            }
            
            // If request term > currentTerm, become follower
            if (request.term > currentTerm) {
                becomeFollower(request.term);
            }
            
            // Reset election timer
            resetElectionTimer();
            
            // If we're a candidate, step down
            if (state == NodeState.CANDIDATE) {
                becomeFollower(currentTerm);
            }
            
            // Check if log contains an entry at prevLogIndex with term equal to prevLogTerm
            if (request.prevLogIndex > 0 && 
                (log.size() < request.prevLogIndex || 
                 log.get((int) request.prevLogIndex - 1).term != request.prevLogTerm)) {
                return new AppendEntriesResponse(currentTerm, false);
            }
            
            // If existing entries conflict with new entries, delete them and all that follow
            if (!request.entries.isEmpty()) {
                int logIndex = (int) request.prevLogIndex;
                for (LogEntry entry : request.entries) {
                    logIndex++;
                    if (log.size() >= logIndex) {
                        if (log.get(logIndex - 1).term != entry.term) {
                            // Delete conflicting entry and all that follow
                            while (log.size() >= logIndex) {
                                log.remove(log.size() - 1);
                            }
                            // Append new entry
                            log.add(entry);
                        }
                    } else {
                        // Append new entry
                        log.add(entry);
                    }
                }
            }
            
            // Update commit index
            if (request.leaderCommit > commitIndex) {
                commitIndex = Math.min(request.leaderCommit, log.size());
                
                // Apply committed entries
                while (lastApplied < commitIndex) {
                    lastApplied++;
                    applyLogEntry(log.get((int) lastApplied - 1));
                }
            }
            
            return new AppendEntriesResponse(currentTerm, true);
        }
        
        public RequestVoteResponse handleRequestVote(RequestVoteRequest request) {
            // If request term < currentTerm, reject
            if (request.term < currentTerm) {
                return new RequestVoteResponse(currentTerm, false);
            }
            
            // If request term > currentTerm, become follower
            if (request.term > currentTerm) {
                becomeFollower(request.term);
            }
            
            // Check if we've already voted or if candidate's log is at least as up-to-date as ours
            boolean logOk = request.lastLogTerm > getLastLogTerm() || 
                           (request.lastLogTerm == getLastLogTerm() && 
                            request.lastLogIndex >= log.size());
            
            if ((votedFor == null || votedFor.equals(request.candidateId)) && logOk) {
                votedFor = request.candidateId;
                resetElectionTimer();
                return new RequestVoteResponse(currentTerm, true);
            }
            
            return new RequestVoteResponse(currentTerm, false);
        }
        
        private long getLastLogTerm() {
            return log.isEmpty() ? 0 : log.get(log.size() - 1).term;
        }
        
        // Shutdown
        public void shutdown() {
            if (electionTimer != null) {
                electionTimer.cancel(false);
            }
            if (heartbeatTimer != null) {
                heartbeatTimer.cancel(false);
            }
            scheduler.shutdownNow();
        }
    }
    
    // RPC service interface
    public interface RaftRpcService {
        CompletableFuture<AppendEntriesResponse> sendAppendEntries(String targetId, AppendEntriesRequest request);
        CompletableFuture<RequestVoteResponse> sendRequestVote(String targetId, RequestVoteRequest request);
    }
}
```

## Advanced Implementation: Distributed Coordination Service

Let's implement a simplified distributed coordination service that combines distributed locking, leader election, and consensus.

```java
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class DistributedCoordinator {
    private final String nodeId;
    private final List<String> peerIds;
    private final RaftConsensus.RaftNode raftNode;
    private final Map<String, Lock> locks = new ConcurrentHashMap<>();
    private final Map<String, String> keyValueStore = new ConcurrentHashMap<>();
    
    public DistributedCoordinator(String nodeId, List<String> peerIds, RaftConsensus.RaftRpcService rpcService) {
        this.nodeId = nodeId;
        this.peerIds = new ArrayList<>(peerIds);
        this.raftNode = new RaftConsensus.RaftNode(nodeId, peerIds, rpcService);
    }
    
    // Distributed locking
    public static class Lock {
        private final String name;
        private final String owner;
        private final long expirationTime;
        
        public Lock(String name, String owner, long expirationTimeMillis) {
            this.name = name;
            this.owner = owner;
            this.expirationTime = System.currentTimeMillis() + expirationTimeMillis;
        }
        
        public boolean isExpired() {
            return System.currentTimeMillis() > expirationTime;
        }
        
        public String getName() {
            return name;
        }
        
        public String getOwner() {
            return owner;
        }
    }
    
    public CompletableFuture<Boolean> acquireLock(String lockName, long timeoutMillis) {
        // Create a lock command
        String command = "LOCK:" + lockName + ":" + nodeId + ":" + timeoutMillis;
        
        // Propose the command to the Raft cluster
        return raftNode.proposeCommand(command);
    }
    
    public CompletableFuture<Boolean> releaseLock(String lockName) {
        // Create a release command
        String command = "UNLOCK:" + lockName + ":" + nodeId;
        
        // Propose the command to the Raft cluster
        return raftNode.proposeCommand(command);
    }
    
    // Key-value store operations
    public CompletableFuture<Boolean> setValue(String key, String value) {
        // Create a set command
        String command = "SET:" + key + ":" + value;
        
        // Propose the command to the Raft cluster
        return raftNode.proposeCommand(command);
    }
    
    public String getValue(String key) {
        // Read from local state (may not be consistent if not the leader)
        return keyValueStore.get(key);
    }
    
    public CompletableFuture<String> getConsistentValue(String key) {
        // Create a read command (will be processed through the log for consistency)
        String command = "GET:" + key;
        
        // Propose the command to the Raft cluster
        CompletableFuture<Boolean> proposed = raftNode.proposeCommand(command);
        
        // Return the value after the command is committed
        CompletableFuture<String> result = new CompletableFuture<>();
        proposed.thenAccept(success -> {
            if (success) {
                result.complete(keyValueStore.get(key));
            } else {
                result.completeExceptionally(new RuntimeException("Failed to get consistent value"));
            }
        });
        
        return result;
    }
    
    // Command processor (called by the Raft node when applying log entries)
    public void processCommand(String command) {
        String[] parts = command.split(":");
        String operation = parts[0];
        
        switch (operation) {
            case "LOCK":
                String lockName = parts[1];
                String lockOwner = parts[2];
                long timeout = Long.parseLong(parts[3]);
                
                // Check if lock exists and is not expired
                Lock existingLock = locks.get(lockName);
                if (existingLock != null && !existingLock.isExpired()) {
                    // Lock is already held
                    return;
                }
                
                // Create and store the lock
                Lock newLock = new Lock(lockName, lockOwner, timeout);
                locks.put(lockName, newLock);
                
                // Schedule lock expiration
                ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
                scheduler.schedule(() -> {
                    Lock currentLock = locks.get(lockName);
                    if (currentLock != null && currentLock.getOwner().equals(lockOwner) && 
                        currentLock.isExpired()) {
                        locks.remove(lockName);
                    }
                    scheduler.shutdown();
                }, timeout, TimeUnit.MILLISECONDS);
                break;
                
            case "UNLOCK":
                String unlockName = parts[1];
                String unlockOwner = parts[2];
                
                // Check if lock exists and is owned by the requester
                Lock lockToRelease = locks.get(unlockName);
                if (lockToRelease != null && lockToRelease.getOwner().equals(unlockOwner)) {
                    locks.remove(unlockName);
                }
                break;
                
            case "SET":
                String key = parts[1];
                String value = parts[2];
                keyValueStore.put(key, value);
                break;
                
            case "GET":
                // No action needed for GET as it's just a read operation
                break;
                
            default:
                System.err.println("Unknown command: " + command);
        }
    }
    
    // Shutdown
    public void shutdown() {
        raftNode.shutdown();
    }
}
```

## Testing the Implementation

```java
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DistributedCoordinatorTest {
    public static void main(String[] args) throws Exception {
        // Create a simple in-memory RPC service for testing
        InMemoryRpcService rpcService = new InMemoryRpcService();
        
        // Create three nodes in a cluster
        List<String> nodeIds = Arrays.asList("node1", "node2", "node3");
        DistributedCoordinator node1 = new DistributedCoordinator("node1", 
            Arrays.asList("node2", "node3"), rpcService);
        DistributedCoordinator node2 = new DistributedCoordinator("node2", 
            Arrays.asList("node1", "node3"), rpcService);
        DistributedCoordinator node3 = new DistributedCoordinator("node3", 
            Arrays.asList("node1", "node2"), rpcService);
        
        // Register the nodes with the RPC service
        rpcService.registerNode("node1", node1);
        rpcService.registerNode("node2", node2);
        rpcService.registerNode("node3", node3);
        
        // Wait for leader election
        TimeUnit.SECONDS.sleep(2);
        
        // Test distributed locking
        CompletableFuture<Boolean> lockResult = node1.acquireLock("testLock", 5000);
        boolean acquired = lockResult.get(5, TimeUnit.SECONDS);
        System.out.println("Node1 acquired lock: " + acquired);
        
        // Test another node trying to acquire the same lock
        CompletableFuture<Boolean> lockResult2 = node2.acquireLock("testLock", 5000);
        boolean acquired2 = lockResult2.get(5, TimeUnit.SECONDS);
        System.out.println("Node2 acquired lock: " + acquired2);
        
        // Release the lock
        CompletableFuture<Boolean> releaseResult = node1.releaseLock("testLock");
        boolean released = releaseResult.get(5, TimeUnit.SECONDS);
        System.out.println("Node1 released lock: " + released);
        
        // Now node2 should be able to acquire the lock
        CompletableFuture<Boolean> lockResult3 = node2.acquireLock("testLock", 5000);
        boolean acquired3 = lockResult3.get(5, TimeUnit.SECONDS);
        System.out.println("Node2 acquired lock after release: " + acquired3);
        
        // Test key-value store
        CompletableFuture<Boolean> setResult = node1.setValue("testKey", "testValue");
        boolean set = setResult.get(5, TimeUnit.SECONDS);
        System.out.println("Node1 set value: " + set);
        
        // Read the value from another node
        TimeUnit.SECONDS.sleep(1); // Allow time for replication
        String value = node3.getValue("testKey");
        System.out.println("Node3 read value: " + value);
        
        // Get a consistent value
        CompletableFuture<String> getResult = node2.getConsistentValue("testKey");
        String consistentValue = getResult.get(5, TimeUnit.SECONDS);
        System.out.println("Node2 read consistent value: " + consistentValue);
        
        // Shutdown
        node1.shutdown();
        node2.shutdown();
        node3.shutdown();
    }
    
    // Simple in-memory RPC service for testing
    static class InMemoryRpcService implements RaftConsensus.RaftRpcService {
        private final Map<String, DistributedCoordinator> nodes = new ConcurrentHashMap<>();
        
        public void registerNode(String nodeId, DistributedCoordinator node) {
            nodes.put(nodeId, node);
        }
        
        @Override
        public CompletableFuture<RaftConsensus.AppendEntriesResponse> sendAppendEntries(
                String targetId, RaftConsensus.AppendEntriesRequest request) {
            CompletableFuture<RaftConsensus.AppendEntriesResponse> future = new CompletableFuture<>();
            
            // Simulate network delay
            CompletableFuture.runAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 50));
                    DistributedCoordinator targetNode = nodes.get(targetId);
                    if (targetNode != null) {
                        RaftConsensus.AppendEntriesResponse response = 
                            targetNode.raftNode.handleAppendEntries(request);
                        future.complete(response);
                    } else {
                        future.completeExceptionally(
                            new RuntimeException("Node not found: " + targetId));
                    }
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            });
            
            return future;
        }
        
        @Override
        public CompletableFuture<RaftConsensus.RequestVoteResponse> sendRequestVote(
                String targetId, RaftConsensus.RequestVoteRequest request) {
            CompletableFuture<RaftConsensus.RequestVoteResponse> future = new CompletableFuture<>();
            
            // Simulate network delay
            CompletableFuture.runAsync(() -> {
                try {
                    TimeUnit.MILLISECONDS.sleep((long) (Math.random() * 50));
                    DistributedCoordinator targetNode = nodes.get(targetId);
                    if (targetNode != null) {
                        RaftConsensus.RequestVoteResponse response = 
                            targetNode.raftNode.handleRequestVote(request);
                        future.complete(response);
                    } else {
                        future.completeExceptionally(
                            new RuntimeException("Node not found: " + targetId));
                    }
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            });
            
            return future;
        }
    }
}
```

## Key Insights for Interviews

1. **CAP Theorem**: Understand that distributed systems can provide at most two of the three guarantees: Consistency, Availability, and Partition tolerance. In practice, since network partitions are unavoidable, systems must choose between consistency and availability during partitions.

2. **Consensus Algorithms**:
   - **Paxos**: The first widely recognized consensus algorithm, known for being difficult to understand and implement
   - **Raft**: Designed to be more understandable than Paxos while providing the same guarantees
   - **ZAB (ZooKeeper Atomic Broadcast)**: Used in Apache ZooKeeper
   - **Viewstamped Replication**: One of the earliest consensus algorithms

3. **Distributed Locking Challenges**:
   - **Fencing Tokens**: Necessary to prevent the "split-brain" problem where two clients believe they hold the lock
   - **Lock Expiration**: Locks must expire to prevent deadlocks if a client crashes
   - **Lease Mechanism**: Clients must periodically renew their locks to maintain ownership

4. **Leader Election**:
   - Essential for primary-backup replication systems
   - Must handle network partitions and prevent multiple leaders
   - Should provide a stable leader to minimize disruption

5. **Consistency Models**:
   - **Strong Consistency**: All reads reflect the latest write
   - **Eventual Consistency**: All replicas eventually converge to the same state
   - **Causal Consistency**: Operations causally related are seen in the same order by all processes
   - **Sequential Consistency**: All operations appear in some sequential order that is consistent with the order seen at individual processes

## Real-world Applications

1. Distributed databases (e.g., Google Spanner, CockroachDB)
2. Distributed file systems (e.g., HDFS, GFS)
3. Service discovery and configuration management (e.g., ZooKeeper, etcd)
4. Distributed locking services (e.g., Chubby, DLM)
5. Distributed messaging systems (e.g., Kafka, RabbitMQ)

## Video Explanation

[Distributed Systems: Consensus Algorithms](https://www.youtube.com/watch?v=eTaiQz3YRyQ) - 9:15

## Interview Tips

- Demonstrate understanding of the fundamental challenges in distributed systems: partial failures, network partitions, and message delays
- Explain the trade-offs between different consistency models and their impact on availability
- Be prepared to discuss how to handle network partitions and node failures
- Understand the role of timeouts and retries in distributed systems
- Be able to explain the differences between various consensus algorithms and when to use each
- Discuss strategies for testing distributed systems, including fault injection and chaos engineering


