### Introduction:

Benefits of threads:
  - Higher throughput, though in some pathetic scenarios it is possible to have the overhead of context switching among threads steal away any throughput gains and result in worse performance than a single-threaded scenario. However such cases are unlikely and an exception, rather than the norm.
  - Responsive applications that give the illusion of multi-tasking.
  - Efficient utilization of resources. Note that thread creation is light-weight in comparison to spawning a brand new process. Web servers that use threads instead of creating new processes when fielding web requests, consume far fewer resources.

Problems with Threads
  - Usually very hard to find bugs, some that may only rear head in production environments
  - Higher cost of code maintenance since the code inherently becomes harder to reason about
  - Increased utilization of system resources. Creation of each thread consumes additional memory, CPU cycles for book-keeping and waste of time in context switches.
  - Programs may experience slowdown as coordination amongst threads comes at a price. Acquiring and releasing locks adds to program execution time. Threads fighting over acquiring locks cause lock contention.

Program vs Process vs Thread:

  - A program is a set of instructions and associated data that resides on the disk and is loaded by the operating system 
    to perform some task. An executable file or a python script file are examples of programs. In order to run a program, 
    the operating system's kernel is first asked to create a new process, which is an environment in which a program executes.
  
  - A process is a program in execution. A process is an execution environment that consists of instructions, user-data, and system-data segments, 
    as well as lots of other resources such as CPU, memory, address-space, disk and network I/O acquired at runtime. 
    A program can have several copies of it running at the same time but a process necessarily belongs to only one program.
    
  - Thread is the smallest unit of execution in a process. A thread simply executes instructions serially. A process can have multiple threads running as part of it.
    Usually, there would be some state associated with the process that is shared among all the threads and in turn each thread would have some state private to itself. 
    The globally shared state amongst the threads of a process is visible and accessible to all the threads, and special attention needs to be paid when any thread tries to read or write to this global shared state.
    There are several constructs offered by various programming languages to guard and discipline the access to this global state, which we will go into further detail in upcoming lessons.
    
Concurrency vs Parallelism:
  - A concurrent program is one that can be decomposed into constituent parts and each part can be executed out of order or in partial order without affecting the final outcome. 
    A system capable of running several distinct programs or more than one independent unit of the same program in overlapping time intervals is called a concurrent system.
    In concurrent systems, the goal is to maximize throughput and minimize latency.
  - a concurrent juggler is one who can juggle several balls at the same time. However, at any one point in time, he can only have a single ball in his hand while the rest are in flight. 
    Each ball gets a time slice during which it lands in the juggler's hand and then is thrown back up. A concurrent system is in a similar sense juggling several processes at the same time.
  - A parallel system is one which necessarily has the ability to execute multiple programs at the same time. Usually, this capability is aided by hardware in the form of multicore processors on 
    individual machines or as computing clusters where several machines are hooked up to solve independent pieces of a problem simultaneously.
  - Concurrency is about dealing with lots of things at once. Parallelism is about doing lots of things at once. Last but not the least, 
    you'll find literature describing concurrency as a property of a program or a system whereas parallelism as a runtime behaviour of executing multiple tasks.
 
Cooperative Multitasking vs Preemptive Multitasking:
  - In preemptive multitasking, the operating system preempts a program to allow another waiting task to run on the CPU. 
    Programs or threads can't decide how long for or when they can use the CPU. The operating system’s scheduler decides which thread or program gets to use the CPU next and for how much time.
  - Cooperative Multitasking involves well-behaved programs to voluntarily give up control back to the scheduler so that another program can run. A program or thread may give up control after a period of time has expired or if it becomes idle or logically blocked.

Synchronous vs Asynchronous:
  - Synchronous execution refers to line-by-line execution of code. If a function is invoked, the program execution waits until the function call is completed. 
    Synchronous execution blocks at each method call before proceeding to the next line of code. A program executes in the same sequence as the code in the source code file. Synchronous execution is synonymous to serial execution.
  - Asynchronous (or async) execution refers to execution that doesn't block when invoking subroutines. Asynchronous programming is a means of parallel programming in which a unit of work runs separately from the main application thread and notifies the calling thread of its completion, failure or progress. 
    An asynchronous program doesn’t wait for a task to complete and can move on to the next task.
  - Async execution can invoke a method and move onto the next line of code without waiting for the invoked function to complete or receive its result. 
    Usually, such methods return an entity sometimes called a future or promise that is a representation of an in-progress computation.
  - The program can query for the status of the computation via the returned future or promise and retrieve the result once completed. 
    Another pattern is to pass a callback function to the asynchronous function call which is invoked with the results when the asynchronous function is done processing.

I/O Bound vs CPU Bound:
  - Depending on what a program does, it can require heavier use of one or more resources. For instance, a program that loads gigabytes of data from storage into main memory would hog the main memory of the machine it runs on. 
    Another can be writing several gigabytes to permanent storage, requiring abnormally high disk i/o.
  - Programs which are compute-intensive i.e. program execution requires very high utilization of the CPU (close to 100%) are called CPU bound programs.
    Such programs primarily depend on improving CPU speed to decrease program completion time.
  - I/O bound programs are the opposite of CPU bound programs. Such programs spend most of their time waiting for input or output operations to complete while the CPU sits idle.
    I/O operations can consist of operations that write or read from main memory or network interfaces.
  - Because the CPU and main memory are physically separate a data bus exists between the two to transfer bits to and fro. Similarly, data needs to be moved between network interfaces and CPU/memory.
    Even though the physical distances are tiny, the time taken to move the data across is big enough for several thousand CPU cycles to go waste.
  - Java provides full-blown multithreading and Python is sort of multithreaded as it can only have a single thread in running state because of its global interpreter lock (GIL) limitation

Throughput vs Latency:
  - Throughput is defined as the rate of doing work or how much work gets done per unit of time. If you are an Instagram user, you could define throughput as the number of images your phone or browser downloads per unit of time.
  - Latency is defined as the time required to complete a task or produce a result. Latency is also referred to as response time. The time it takes for a web browser to download Instagram images from the internet is the latency for downloading the images.

Critical Sections & Race Conditions:
  - Critical section is any piece of code that has the possibility of being executed concurrently by more than one thread of the application and exposes any shared data or resources used by the application for access.
  - Race conditions happen when threads run through critical sections without thread synchronization. The threads "race" through the critical section to write or read shared resources and depending on the order in which threads finish the "race", the program output changes.

Deadlocks, Liveness & Reentrant Locks:
  - Deadlocks occur when two or more threads aren't able to make any progress because the resource required by the first thread is held by the second and the resource required by the second thread is held by the first.
  - Ability of a program or an application to execute in a timely manner is called liveness. If a program experiences a deadlock then it's not exhibiting liveness.
  - A live-lock occurs when two threads continuously react in response to the actions by the other thread without making any real progress.
    The best analogy is to think of two persons trying to cross each other in a hallway. John moves to the left to let Arun pass, and Arun moves to his right to let John pass. Both block each other now.
    John sees he's blocking Arun again and moves to his right and Arun moves to his left seeing he's blocking John. They never cross each other and keep blocking each other. This scenario is an example of a livelock.
  - an application thread can also experience starvation, when it never gets CPU time or access to shared resources.Other greedy threads continuously hog shared system resources not letting the starving thread make any progress.
  - Re-entrant locks allow for re-locking or re-entering of a synchronization lock. This can be best explained with an example. Consider the NonReentrant class below.

Mutex vs Semaphore:
  - Mutex as the name hints implies mutual exclusion. A mutex is used to guard shared data such as a linked-list, an array or any primitive type. A mutex allows only a single thread to access a resource or critical section.
  - Once a thread acquires a mutex, all other threads attempting to acquire the same mutex are blocked until the first thread releases the mutex. Once released, most implementations arbitrarily chose one of the waiting threads to acquire the mutex and make progress.
  - Semaphore, on the other hand, is used for limiting access to a collection of resources. Think of semaphore as having a limited number of permits to give out. If a semaphore has given out all the permits it has, then any new thread that comes along requesting for a permit will be blocked,
    till an earlier thread with a permit returns it to the semaphore. A typical example would be a pool of database connections that can be handed out to requesting threads. Say there are ten available connections but 50 requesting threads. In such a scenario, 
    a semaphore can only give out ten permits or connections at any given point in time.
  - A semaphore with a single permit is called a binary semaphore and is often thought of as an equivalent of a mutex, which isn't completely correct as we'll shortly explain. Semaphores can also be used for signaling among threads.
    This is an important distinction as it allows threads to cooperatively work towards completing a task. A mutex, on the other hand, is strictly limited to serializing access to shared state among competing threads.
  - the most important difference between the two is that in case of a mutex the same thread must call acquire and subsequent release on the mutex whereas in case of a binary sempahore, different threads can call acquire and release on the semaphore.
  - A mutex is owned by the thread acquiring it till the point the owning-thread releases it, whereas for a semaphore there's no notion of ownership.
  - semaphores can be used for signaling amongst threads, for example in case of the classical producer/consumer problem the producer thread can signal the consumer thread by incrementing the semaphore count to indicate to the consumer thread to consume the freshly produced item.
    A mutex in contrast only guards access to shared data among competing threads by forcing threads to serialize their access to critical sections and shared data-structures.

Mutex vs Monitor:
  - Concisely, a monitor is a mutex and then some. Monitors are generally language level constructs whereas mutex and semaphore are lower-level or OS provided constructs.
  - 