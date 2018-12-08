# Spark Tuning: #
	
### Data Serialization: ###

    Data serialization, which is crucial for good network performance and can also reduce memory use

#### Java serialization: ###

	-   Spark serializes objects using Java’s ObjectOutputStream framework, and can work with any class you create that implements java.io.Serializable. 
        You can also control the performance of your serialization more closely by extending java.io.Externalizable. 
        Java serialization is flexible but often quite slow, and leads to large serialized formats for many classes.

#### Kryo serialization: ####

    -   Spark can also use the Kryo library (version 4) to serialize objects more quickly. 
        Kryo is significantly faster and more compact than Java serialization (often as much as 10x), 
        but does not support all Serializable types and requires you to register the classes you’ll 
        use in the program in advance for best performance. conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        Kyro serializer used for not only shuffling data between worker nodes but also when serializing RDDs to disk. 
        The only reason Kryo is not the default is because of the custom registration requirement,
        but we recommend trying it in any network-intensive application
	
## Memory Tuning: ##

    There are three considerations in tuning memory usage

	 -  the amount of memory used by your objects (you may want your entire dataset to fit in memory)
	 -  the cost of accessing those objects, 
	 -  the overhead of garbage collection (if you have high turnover in terms of objects)

    By default, Java objects are fast to access, but can easily consume a factor of 2-5x more space than the “raw” data inside their fields. 

    - Each distinct Java object has an “object header”, which is about 16 bytes and contains information such as a pointer to its class. 
      For an object with very little data in it (say one Int field), this can be bigger than the data.
    - Java Strings have about 40 bytes of overhead over the raw string data (since they store it in an array of Chars and keep extra data such as the length), 
      and store each character as two bytes due to String’s internal usage of UTF-16 encoding. 
      Thus a 10-character string can easily consume 60 bytes.
    - Common collection classes, such as HashMap and LinkedList, use linked data structures, where there is a “wrapper” object for each entry (e.g. Map.Entry). 
      This object not only has a header, but also pointers (typically 8 bytes each) to the next object in the list.
    - Collections of primitive types often store them as “boxed” objects such as java.lang.Integer
		
## Memory Management: ##

    -	Memory usage in Spark largely falls under one of two categories: execution and storage. 
	    Execution memory refers to that used for computation in shuffles, joins, sorts and aggregations, 
	    while storage memory refers to that used for caching and propagating internal data across the cluster. 
    -	Spark, execution and storage share a unified region (M). When no execution memory is used, storage can acquire all the available memory and vice versa. 
	    Execution may evict storage if necessary, but only until total storage memory usage falls under a certain threshold (R).
    -	First, applications that do not use caching can use the entire space for execution, obviating unnecessary disk spills. 
	    Second, applications that do use caching can reserve a minimum storage space (R) where their data blocks are immune to being evicted. 
	    Lastly, this approach provides reasonable out-of-the-box performance for a variety of workloads without requiring user expertise of how memory is divided internally.

![Alt text](https://cdn-images-1.medium.com/max/2000/1*kKitFswq56j1CTgMQ0gumQ.png)

    spark.memory.fraction:
	    -	expresses the size of M as a fraction of the (JVM heap space - 300MB) (default 0.6). 
		    The rest of the space (40%) is reserved for user data structures, internal metadata in Spark, 
		    and safeguarding against OOM errors in the case of sparse and unusually large records.
	    -	spark.memory.fraction should be set in order to fit this amount of heap space comfortably within the JVM’s old or “tenured” generation
    spark.memory.storageFraction:
	    expresses the size of R as a fraction of M (default 0.5). 
	    R is the storage space within M where cached blocks immune to being evicted by execution.
    Determining Memory Consumption:
	    SizeEstimator’s estimate method
	    val catalyst_plan = df.queryExecution.logical, val df_size_in_bytes = spark.sessionState.executePlan(catalyst_plan).optimizedPlan.stats.sizeInBytes
		
## Tuning Data Structures: ##

    -	Design your data structures to prefer arrays of objects, and primitive types, instead of the standard Java or Scala collection classes (e.g. HashMap). 
	    The fastutil library provides convenient collection classes for primitive types that are compatible with the Java standard library.
    -	Avoid nested structures with a lot of small objects and pointers when possible.
    -	Consider using numeric IDs or enumeration objects instead of strings for keys.
    -	If you have less than 32 GB of RAM, set the JVM flag -XX:+UseCompressedOops to make pointers be four bytes instead of eight.
	    You can add these options in spark-env.sh.

## Garbage Collection Tuning: ##
![Alt text](http://4.bp.blogspot.com/-zkimVwbJjsE/VjdieYsQy9I/AAAAAAAABJw/9i4WWTaNbE8/s1600/loio44a438452ba94658a8e21f998d248fa4_LowRes.png)

    -	JVM garbage collection can be a problem when you have large “churn” in terms of the RDDs stored by your program. 
        (It is usually not a problem in programs that just read an RDD once and then run many operations on it.) 
        When Java needs to evict old objects to make room for new ones, it will need to trace through all your Java objects 
        and find the unused ones. The main point to remember here is that the cost of garbage collection is proportional to the number of Java objects, 
        so using data structures with fewer objects (e.g. an array of Ints instead of a LinkedList) greatly lowers this cost.

#### Measuring the Impact of GC: ####
			
    -   The first step in GC tuning is to collect statistics on how frequently garbage collection occurs and the amount of time spent GC. 
        This can be done by adding -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps to the Java options.

    Advanced GC Tuning:
        -	Java Heap space is divided in to two regions Young and Old. 
            The Young generation is meant to hold short-lived objects while the Old generation is intended for objects with longer lifetimes.
        -	The Young generation is further divided into three regions [Eden, Survivor1, Survivor2].
        -	A simplified description of the garbage collection procedure: When Eden is full, 
            a minor GC is run on Eden and objects that are alive from Eden and Survivor1 are copied to Survivor2. 
            The Survivor regions are swapped. If an object is old enough or Survivor2 is full, it is moved to Old. 
            Finally, when Old is close to full, a full GC is invoked.
        -	The goal of GC tuning in Spark is to ensure that only long-lived RDDs are stored in the Old generation 
            and that the Young generation is sufficiently sized to store short-lived objects.
        -	If there are too many minor collections but not many major GCs, allocating more memory for Eden would help. 
            You can set the size of the Eden to be an over-estimate of how much memory each task will need. 
            If the size of Eden is determined to be E, then you can set the size of the Young generation using the option -Xmn=4/3*E.
        -	if the OldGen is close to being full, reduce the amount of memory used for caching by lowering spark.memory.fraction; 
            it is better to cache fewer objects than to slow down task execution. Alternatively, consider decreasing the size of the Young generation. 
            This means lowering -Xmn if you’ve set it as above. If not, try changing the value of the JVM’s NewRatio parameter. Many JVMs default this to 2, 
            meaning that the Old generation occupies 2/3 of the heap. It should be large enough such that this fraction exceeds spark.memory.fraction
        -	the G1GC garbage collector with -XX:+UseG1GC. It can improve performance in some situations where garbage collection is a bottleneck. Note that with large executor heap sizes, 
            it may be important to increase the G1 region size with -XX:G1HeapRegionSize

## Other Considerations: ##

#### Level of Parallelism: ####

    -   Clusters will not be fully utilized unless you set the level of parallelism for each operation high enough. 
        Spark automatically sets the number of “map” tasks to run on each file according to its size (though you can control it through optional parameters to SparkContext.textFile, etc), 
        and for distributed “reduce” operations, such as groupByKey and reduceByKey, it uses the largest parent RDD’s number of partitions. 
        You can pass the level of parallelism as a second argument (see the spark.PairRDDFunctions documentation), or set the config property spark.default.parallelism to change the default.

#### Memory Usage of Reduce Tasks: ####  

    -   you will get an OutOfMemoryError not because your RDDs don’t fit in memory, but because the working set of one of your tasks, 
        such as one of the reduce tasks in groupByKey, was too large. Spark’s shuffle operations (sortByKey, groupByKey, reduceByKey, join, etc) 
        build a hash table within each task to perform the grouping, which can often be large. 
        The simplest fix here is to increase the level of parallelism, so that each task’s input set is smaller. 
        Spark can efficiently support tasks as short as 200 ms, because it reuses one executor JVM across many tasks 
        and it has a low task launching cost, so you can safely increase the level of parallelism to more than the number of cores in your clusters.

#### Broadcasting Large Variables: ####
    -   Using the broadcast functionality available in SparkContext can greatly reduce the size of each serialized task, 
        and the cost of launching a job over a cluster. If your tasks use any large object from the driver program inside of them (e.g. a static lookup table), 
        consider turning it into a broadcast variable. Spark prints the serialized size of each task on the master, so you can look at that to decide whether your tasks are too large; 
        in general tasks larger than about 20 KB are probably worth optimizing.

#### Data Locality: ####

    Data locality is how close data is to the code processing it.
    There are several levels of locality based on the data’s current location. In order from closest to farthest
    -   PROCESS_LOCAL: data is in the same JVM as the running code. This is the best locality possible
    -   NODE_LOCAL data is on the same node. Examples might be in HDFS on the same node, or in another executor on the same node. 
        This is a little slower than PROCESS_LOCAL because the data has to travel between processes
    -   NO_PREF data is accessed equally quickly from anywhere and has no locality preference
    -   RACK_LOCAL data is on the same rack of servers. Data is on a different server on the same rack so needs to be sent over the network, typically through a single switch
    -   ANY data is elsewhere on the network and not in the same rack
        a) wait until a busy CPU frees up to start a task on data on the same server, or 
        b) immediately start a new task in a farther away place that requires moving data there.
