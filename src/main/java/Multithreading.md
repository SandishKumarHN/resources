##Java Multi Threading:##
    Three Ways to Implement multithreading
	-   extend Thread class
	-   implement Runnable Interface and pass its object to Thread constructor as param
	-   and Thread pool
    “implements Runnable” vs “extends Thread”:
		When you extends Thread class, after that you can’t extend any other class which you required. (As you know, Java does not allow inheriting more than one class).
		When you implements Runnable, you can save a space for your class to extend any other class in future or now.
		Java doesn't support multiple inheritance, which means you can only extend one class in Java so once you extended Thread class you lost your chance and can not extend or inherit another class in Java.
		In Object oriented programming extending a class generally means adding new functionality, modifying or improving behaviors. If we are not making any modification on Thread then use Runnable interface instead.
		Runnable interface represent a Task which can be executed by either plain Thread or Executors or any other means. so logical separation of Task as Runnable than Thread is good design decision.
		Separating task as Runnable means we can reuse the task and also has liberty to execute it from different means. since you can not restart a Thread once it completes. again Runnable vs Thread for task, Runnable is winner.
		Java designer recognizes this and that's why Executors accept Runnable as Task and they have worker thread which executes those task.
		Inheriting all Thread methods are additional overhead just for representing a Task which can be done easily with Runnable.

###Synchronization:###
### Volatile: ###
    The value of this variable will never be cached thread-locally: all reads and writes will go straight to "main memory"; 
    Access to the variable acts as though it is enclosed in a synchronized block, synchronized on itself. 
    synchronized vs volatile:
        synchronized is method level/block level access restriction modifier. It will make sure that one thread owns the lock for critical section. 
        Only the thread,which own a lock can enter synchronized block. If other threads are trying to access this critical section, 
        they have to wait till current owner releases the lock.
        volatile is variable access modifier which forces all threads to get latest value of the variable from main memory. 
        No locking is required to access volatile variables. All threads can access volatile variable value at same time.
        A good example to use volatile variable : Date variable.
        Assume that you have made Date variable volatile. All the threads, 
        which access this variable always get latest data from main memory so that all threads show real (actual) Date value.
        You don't need different threads showing different time for same variable. All threads should show right Date value.

### Synchronized: ###
    A synchronized block in Java is synchronized on some object. 
    All synchronized blocks synchronized on the same object can only have one thread executing inside them at a time. 
    All other threads attempting to enter the synchronized block are blocked until the thread inside the synchronized block exits the block.
![Alt text](https://www.logicbig.com/tutorials/core-java-tutorial/java-multi-threading/thread-livelock/images/livelock.png)

#### Object level lock vs class level lock: ####
![Alt text](https://image.slidesharecdn.com/concurrencyinjava-160120162710/95/concurrency-in-java-5-638.jpg?cb=1453740013)

        Synchronization in Java guarantees that no two threads can execute a synchronized method, which requires same lock, simultaneously or concurrently.
        synchronized keyword can be used only with methods and code blocks. These methods or blocks can be static or non-static both.
        When ever a thread enters into Java synchronized method or block it acquires a lock and whenever it leaves synchronized method or block it releases the lock. Lock is released even if thread leaves synchronized method after completion or due to any Error or Exception.
        Java synchronized keyword is re-entrant in nature it means if a synchronized method calls another synchronized method which requires same lock then current thread which is holding lock can enter into that method without acquiring lock.
        Java synchronization will throw NullPointerException if object used in synchronized block is null. For example, in above code sample if lock is initialized as null, the “synchronized (lock)” will throw NullPointerException.
        Synchronized methods in Java put a performance cost on your application. So use synchronization when it is absolutely required. Also, consider using synchronized code blocks for synchronizing only critical section of your code.
        It’s possible that both static synchronized and non static synchronized method can run simultaneously or concurrently because they lock on different object.
        According to the Java language specification you can not use synchronized keyword with constructor. It is illegal and result in compilation error.
        Do not synchronize on non final field on synchronized block in Java. because reference of non final field may change any time and then different thread might synchronizing on different objects i.e. no synchronization at all. Best is to use String class, which is already immutable and declared final.
### Synchronized Blocks vs Methods: ###
    synchronized method acquires a lock on the whole object. This means no other thread can use any synchronized method in the whole object while the method is being run by one thread. 
    synchronized blocks acquires a lock in the object between parentheses after the synchronized keyword
    if you want to lock the whole object, use a synchronized method. If you want to keep other parts of the object accessible to other threads, use synchronized block.
    synchronized block provide granular control over lock but synchronized method lock either on current object represented by this or class level lock.
#### ThreadPools: #### 
		Java thread pool manages the pool of worker threads, it contains a queue that keeps tasks waiting to get executed
		Java thread pool manages the collection of Runnable threads and worker threads execute Runnable from the queue. java.util.concurrent.Executors provide factory and support methods for java.util.concurrent.Executor interface to create the thread pool in java.
		Executors is a utility class that also provides useful methods to work with ExecutorService, ScheduledExecutorService, ThreadFactory, and Callable classes through various factory methods.
![Alt text](https://www.baeldung.com/wp-content/uploads/2016/08/2016-08-10_10-16-52-1024x572.png)
		Java provides the Executor framework which is centered around the Executor interface, its sub-interface –ExecutorService and the class-ThreadPoolExecutor, which implements both of these interfaces. By using the executor, one only has to implement the Runnable objects and send them to the executor to execute.
		They allow you to take advantage of threading, but focus on the tasks that you want the thread to perform, instead of thread mechanics.
		To use thread pools, we first create a object of ExecutorService and pass a set of tasks to it. ThreadPoolExecutor class allows to set the core and maximum pool size.The runnables that are run by a particular thread are executed sequentially
	
#### CountDownLatch: #### 
![Alt text](https://cdn2.howtodoinjava.com/wp-content/uploads/CountdownLatch_example.png)
		CoundDownLatch enables you to make a thread wait till all other threads are done with their execution.
		CountDownLatch works by having a counter initialized with number of threads, which is decremented each time a thread complete its execution. When count reaches to zero, it means all threads have completed their execution, and thread waiting on latch resume the execution.
		Wait N threads to completes before start execution: For example an application start-up class want to ensure that all N external systems are Up and running before handling the user requests.
	
#### BlockingQueue/ArrayBlockingQueue: #### 
![Alt text](https://coderscampus.com/wp-content/uploads/2016/04/CSP-Diagram.png)
		it represents a FIFO (first in, first out) queue of values, is itself thread-safe, and allows for blocking on both producing and consuming processes.
		If you give your threads the same queue, they can pass messages between each other in a safe way.
	
#### Wait and Notify: #### 
![Alt text](http://www.ntu.edu.sg/home/ehchua/programming/java/images/Multithread_TimedWaiting.png)
		wait : Object wait methods has three variance, one which waits indefinitely for any other thread to call notify or notifyAll method on the object to wake up the current thread. 
			   Other two variances puts the current thread in wait for specific amount of time before they wake up.
		notify: notify method wakes up only one thread waiting on the object and that thread starts execution. 
				So if there are multiple threads waiting for an object, this method will wake up only one of them. The choice of the thread to wake depends on the OS implementation of thread management.
		notifyAll: notifyAll method wakes up all the threads waiting on the object, although which one will process first depends on the OS implementation.
#### ReentrantLock: #### 
		A ReentrantLock is unstructured, unlike synchronized constructs -- i.e. you don't need to use a block structure for locking and can even hold a lock across methods.
#### DeadLock: #### 
![Alt text](https://cdncontribute.geeksforgeeks.org/wp-content/uploads/22-2.png)
		Deadlock occurs when multiple threads need the same locks but obtain them in different order. A Java multithreaded program may suffer from the 
		deadlock condition because the synchronized keyword causes the executing thread to block while waiting for the lock, or monitor, associated with the specified object.
#### Semaphores: #### 
![Alt text](https://3.bp.blogspot.com/-7nB5mH-BGhQ/WH0va83GJfI/AAAAAAAAXf0/1nVX7w72J1Y7ubeRDqURYUtLXtsH7Yg7QCLcB/s1600/semaphore.png)
		A Mutex, by definition, is used to serialize access to a section of re­-entrant code that cannot be executed concurrently by more than one thread.
		A Semaphore, by definition, restricts the number of simultaneous users of a shared resource up to a maximum number
		No one owns semaphores, whereas Mutex are owned and the owner is held responsible for them. This is an important distinction from a debugging perspective.
#### Callable and Future: #### 
		Callable vs Runnable
		For implementing Runnable, the run() method needs to be implemented which does not return anything, while for a Callable, the call() method needs to be implemented which returns a result on completion. Note that a thread can’t be created with a Callable, it can only be created with a Runnable.
		Another difference is that the call() method can throw an exception whereas run() cannot.
		When the call() method completes, answer must be stored in an object known to the main thread, so that the main thread can know about the result that the thread returned. How will the program store and obtain this result later? For this, a Future object can be used
#### Yield: #### 
		sleep() causes the thread to definitely stop executing for a given amount of time; if no other thread or process needs to be run, the CPU will be idle (and probably enter a power saving mode).
		yield() basically means that the thread is not doing anything particularly important and if any other threads or processes need to be run, they should. Otherwise, the current thread will continue to run.
		
	
		
		
		
		

	
	
	