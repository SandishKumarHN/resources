Java Multithreading Concurrency Performance Optimization
---

### Motivation:

- Why Multithreading, Responsiveness and Performance.
- 1 core, multiple tasks, concurrency=multithreading. 
- OS loaded into Memory, OS provides an abstraction for developers.

Process:
  - a process is complete isolation from any other process. each process can have 1 or more threads.
  - a process contains of, PID(process id), Files, Data(heap), Code, 
    main thread(Stack, Instruction Pointer). 
  - in multi threaded application, each thread comes with its own stack and Instruction pointer,
    all other components are shared by all threads. 
   
  - Stack, Region in memory, where local variables are stored. and passed to functions.
  - Instructor Pointer, address of the next instruction to execute.
  
![Alt text](src/images/MultiThreadedApplicationProcess.png)

Context Switch, 
- stop thread1, Schedule thread1 out, Schedule Thread2 in and start thread2. Context Switch is not cheap.
- Too many threads causes, thrashing, where OS spends more time in managing threads than doing productive work.
- Threads consumes less resources than processes. Context switching between threads in same processes is cheaper.

Thread Scheduling,
- Epochs, 
- Decision on how to allocate a time for thread is based on, Dynamic Priority = Static Priority + Bonus (Bonus can be negative)
- Static priority is set by developer programmatically. and the Bonus is adjusted by OS in every epoch in each thread.
- OS will give preference to threads that did not complete in last epochs, or did not get enough time to run - Prevent starvation.


#### Threads termination and Deamon Thread:
- Threads consume resources, memory and kernal resources, CPU cycles and cache memory
- if a thread finished its work, but the application is still running, we want to clean up the thread resources.
- if a thread is misbehaving, we want to stop it. if we want to stop or close the application.
- Deamon threads are thread which run in background that do not prevent the application from the existing main thread terminates. i.e, background tasks.
