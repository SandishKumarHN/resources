## Java Memory Managment ##

![Alt text](https://pbs.twimg.com/media/CsLONcVW8AAVbTh.jpg)

### The Role of Stack and The Heap in JVM ###

-   Heap contains Objects (may also contain reference variables)

-   Stack contains methods, local variables and reference variables

-   When each time a new object created in Java it goes into the area of memory known as heap. 
    The primitive variables like int, long, float, double…Etc are allocated in the stack (i.e. Stack follow Last In First Out algorithm), 
    if variables are local variables it places in stack and if variables are member variables (i.e. fields of a class) then it place in heap. 
    In Java methods and its local variables are pushed into stack when a method is invoked and stack pointer is decremented when a method call is completed.

-   At the time of multi-threaded application each thread will have its own stack but will share the same heap. 
    So in this situation care should be taken in your code to avoid any concurrent access issues in the heap space.

-   The stack is thread-safe because each thread will have its own stack with say 1MB RAM allocated for each 
    thread but the heap is not thread-safe unless guarded with synchronization through your code. The stack space can be increased with the –Xss option.
    
![Alt text](https://dnhome.files.wordpress.com/2012/06/java-memory-stack-heap.jpg)


