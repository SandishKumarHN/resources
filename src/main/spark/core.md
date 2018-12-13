## Transformations vs Actions: ##

    Transformations: which create a new dataset from an existing one, All transformations in Spark are lazy, in that they do not compute their results right away. Instead, they just remember the transformations applied to some base dataset (e.g. a file). The transformations are only computed when an action requires a result to be returned to the driver program. 
    Actions: which return a value to the driver program after running a computation on the dataset.
    Map: Return a new distributed dataset formed by passing each element of the source through a function func. list of tuples. [[3, 9], [4, 16], [5, 25]]
    FlatMap: Similar to map, but each input item can be mapped to 0 or more output items. returns a flat list. [3, 9, 4, 16, 5, 25]
    Filter: Return a new dataset formed by selecting those elements of the source on which func returns true.
    MapPartitions: Similar to map, but runs separately on each partition (block) of the RDD, mapPartitionsWithIndex Similar to mapPartitions, but also provides func with an integer value representing the index of the partition.
    Sample: Sample a fraction fraction of the data, with or without replacement, using a given random number generator seed.
    union: Return a new dataset that contains the union of the elements in the source dataset and the argument
    Intersection: Return a new RDD that contains the intersection of elements in the source dataset and the argument.
    distinct: Return a new dataset that contains the distinct elements of the source dataset.
    groupByKey: When called on a dataset of (K, V) pairs, returns a dataset of (K, Iterable<V>) pairs. If you are grouping in order to perform an aggregation (such as a sum or average) over each key, using reduceByKey or aggregateByKey will yield much better performance.
    reduceByKey: When called on a dataset of (K, V) pairs, returns a dataset of (K, V) pairs where the values for each key are aggregated using the given reduce function func, which must be of type (V,V) => V.
    aggregateByKey(zeroValue)(seqOp, combOp, [numPartitions]): When called on a dataset of (K, V) pairs, returns a dataset of (K, U) pairs where the values for each key are aggregated using the given combine functions and a neutral "zero" value. Allows an aggregated value type that is different than the input value type, while avoiding unnecessary allocations.
    sortByKey: When called on a dataset of (K, V) pairs where K implements Ordered, returns a dataset of (K, V) pairs sorted by keys in ascending or descending order, as specified in the boolean ascending argument.
    join: When called on datasets of type (K, V) and (K, W), returns a dataset of (K, (V, W)) pairs with all pairs of elements for each key. Outer joins are supported through leftOuterJoin, rightOuterJoin, and fullOuterJoin.
    cogroup: When called on datasets of type (K, V) and (K, W), returns a dataset of (K, (Iterable<V>, Iterable<W>)) tuples. 
    cartesian: When called on datasets of types T and U, returns a dataset of (T, U) pairs (all pairs of elements).
    pipe: Pipe each partition of the RDD through a shell command,
    coalesce: Decrease the number of partitions in the RDD to numPartitions. repartition: Reshuffle the data in the RDD randomly to create either more or fewer partitions and balance it across them. This always shuffles all data over the network. 
    repartitionAndSortWithinPartitions: Repartition the RDD according to the given partitioner and, within each resulting partition, sort records by their keys. This is more efficient than calling repartition and then sorting within each partition because it can push the sorting down into the shuffle machinery.
    randomSplit: Randomly splits this RDD with the provided weights.
    Subtract: subtract(RDD<T> other) Return an RDD with the elements from this that are not in other.
    zip: Zips this RDD with another one, returning key-value pairs with the first element in each RDD, second element in each RDD,
    keyBy: Constructs two-component tuples (key-value pairs) by applying a function on each data item. The result of the function becomes the key and the original data item becomes the value of the newly created tuples.
    zipWithIndex: (assigne index to rdd start from 0) Zips the elements of the RDD with its element indexes. The indexes start from 0. If the RDD is spread across multiple partitions then a spark Job is started to perform this operation.
    zipWithUniqueId: (random uniq id, not index.), This is different from zipWithIndex since just gives a unique id to each data element but the ids may not match the index number of the data element. 
    fold: Action, The key difference between fold() and reduce() is that, reduce() throws an exception for empty collection, but fold() is defined for empty collection.
    countByKey: Action, Only available on RDDs of type (K, V). Returns a hashmap of (K, Int) pairs with the count of each key.
    reduce: Action, Aggregate the elements of the dataset using a function func.
    foreach: Action, Run a function func on each element of the dataset. This is usually done for side effects such as updating an Accumulator or interacting with external storage systems.
    more from here http://homepage.cs.latrobe.edu.au/zhe/ZhenHeSparkRDDAPIExamples.html#aggregateByKey
    
## Shuffle Operations: ##
![Alt text](http://image.slidesharecdn.com/sparkshuffleintroduction-141228034437-conversion-gate01/95/spark-shuffle-introduction-16-638.jpg)
    
    -   The shuffle is Spark’s mechanism for re-distributing data so that it’s grouped differently across partitions. 
        This typically involves copying data across executors and machines, making the shuffle a complex and costly operation.
    -   single reduceByKey reduce task to execute, Spark needs to perform an all-to-all operation. It must read from all partitions to find all the values for all keys, and 
        then bring together values across partitions to compute the final result for each key - this is called the shuffle.
            mapPartitions to sort each partition using, for example, .sorted
            repartitionAndSortWithinPartitions to efficiently sort partitions while simultaneously repartitioning
            sortBy to make a globally ordered RDD
    -   Operations which can cause a shuffle include repartition operations like repartition and coalesce, 
        ‘ByKey operations (except for counting) like groupByKey and reduceByKey, and join operations like cogroup and join.
    -   The Shuffle is an expensive operation since it involves disk I/O, data serialization, and network I/O. 
        To organize data for the shuffle, Spark generates sets of tasks - map tasks to organize the data, and a set of reduce tasks to aggregate it.
    -   Internally, results from individual map tasks are kept in memory until they can’t fit. 
        Then, these are sorted based on the target partition and written to a single file. On the reduce side, tasks read the relevant sorted blocks.   
    -   Certain shuffle operations can consume significant amounts of heap memory since they employ in-memory data structures to organize records before or after transferring them. 
        Specifically, reduceByKey and aggregateByKey create these structures on the map side, and 'ByKey operations generate these on the reduce side. 
        When data does not fit in memory Spark will spill these tables to disk, incurring the additional overhead of disk I/O and increased garbage collection.

## RDD Persistence ##
![Alt text](https://slideplayer.com/slide/4499936/14/images/37/Persistence+Persistence+levels+from+org.apache.spark.storage.StorageLevel+%2F+py+spark.StorageLevel.+Level..jpg)

    -   

## Caching ##


## Shared Variable ##
#### Broadcasting: #### 
![Alt text](https://jaceklaskowski.gitbooks.io/mastering-apache-spark/images/sparkcontext-broadcast-executors.png)

        -   Broadcast variables allow the programmer to keep a read-only variable cached on each machine rather than shipping a copy of it with tasks. 
            They can be used, for example, to give every node a copy of a large input dataset in an efficient manner. Spark also attempts to distribute 
            broadcast variables using efficient broadcast algorithms to reduce communication cost.
        
#### Accumulators: ####
    -   Accumulators are variables that are only “added” to through an associative and commutative operation and can therefore be efficiently supported in parallel. 
        They can be used to implement counters (as in MapReduce) or sums. Spark natively supports accumulators of numeric types, and programmers can add support for new types.   

#### Checkpoint ####
    For this to be possible, Spark Streaming needs to checkpoint enough information to a fault- tolerant storage system such that it can recover from failures.
    Metadata checkpointing : 
                    Saving of the information defining the streaming computation to fault-tolerant storage like HDFS. 
                        Configuration - The configuration that was used to create the streaming application
                        DStream operations - The set of DStream operations that define the streaming application.
                        Incomplete batches - Batches whose jobs are queued but have not completed yet.
    Data checkpointing : Saving of the generated RDDs to reliable storage. This is necessary in some stateful transformations that combine data across multiple batches. 
    To summarize, metadata checkpointing is primarily needed for recovery from driver failures, whereas data or RDD checkpointing is necessary even for basic functioning if stateful transformations are used.
    If either updateStateByKey or reduceByKeyAndWindow (with inverse function) is used in the application, then the checkpoint directory must be provided to allow for periodic RDD checkpointing. streamingContext.checkpoint(checkpointDirectory).

#### Cache vs Checkpoint. ####

    -   Cache materializes the RDD and keeps it in memory and/or disk（memory）. But the lineage（computing chain） of RDD (that is, seq of operations that generated the RDD) will be remembered, 
        so that if there are node failures and parts of the cached RDDs are lost, they can be regenerated. However, checkpoint saves the RDD to an HDFS file and actually forgets the lineage completely. 
        This is allows long lineages to be truncated and the data to be saved reliably in HDFS (which is naturally fault tolerant by replication).
## ReduceBy vs GroupBy vs aggregateByKey: ##

    -   groupByKey() is just to group your dataset based on a key. 
        It will result in data shuffling when RDD is not already partitioned.
    -   reduceByKey() is something like grouping + aggregation. 
        We can say reduceBykey() equvelent to dataset.group(...).reduce(...). 
        It will shuffle less data unlike groupByKey().
    -   aggregateByKey() is logically same as reduceByKey() but it lets you return result in 
        different type. In another words, it lets you have a input as type x and aggregate result 
        as type y. For example (1,2),(1,4) as input and (1,"six") as output. 
        It also takes zero-value that will be applied at the beginning of each key.

![Alt text](http://2.bp.blogspot.com/-AkXz3JDeRQA/VdXvOvRmNBI/AAAAAAAAAoE/HVyF8mn3t8A/s1600/pic1.jpg) vs ![Alt text](http://3.bp.blogspot.com/-ZCgP18Cz7bY/VdXvernORRI/AAAAAAAAAoM/yCOMYGnFsBI/s1600/pic2.jpg)

## RDD vs DataFrame vs Dataset: ##
![Alt text](http://image.slidesharecdn.com/sparkrddvsdataframevsdataset-171122065627/95/spark-rdd-vs-data-frame-vs-dataset-2-638.jpg) ![Alt text](http://image.slidesharecdn.com/sparkrddvsdataframevsdataset-171122065627/95/spark-rdd-vs-data-frame-vs-dataset-3-638.jpg)

## Parquet vs Avro Vs Orc: ##

    Avro:
        -   Row-based, offers a compact and fast binary format
        -   Schema is encoded on the file so the data can be untagged
        -   Files support block compression and are splittable
        -   AVRO, being a row based file format, is best fit for write intensive operation
        -   supports serialization and compression formats
        -   Avro is best suitable for Real Time Processing and Spar Processing
    Parquet:
        -   Column-oriented binary file format
        -   Efficient in terms of disk I/O when specific columns need to be queried
        -   Parquet is column based file format hence support indexing because of which it is suitable for 
            read intensive, complex or analytical querying, low latency data.
        -   supports serialization and compression formats
        -   Parquet is best fit for (have MPP engine) as it is responsible for complex/interactive querying and low latency outputs.
    Orc:
        -   Column oriented storage format.
        -   Schema is with the data, but as a part of footer.
        -   Data is stored as row groups and stripes
## partitionBy vs Distribute By vs Cluster By ##
    partitionBy: creates a directory structure as described in the Partition Discovery section. Thus, it has limited applicability to columns with high cardinality. In contrast bucketBy distributes data across a fixed number of buckets and can be used when a number of unique values is unbounded.
                Partition divides large amount of data into multiple slices based on value of a table column(s). Bucketing decomposes data into more manageable or equal parts.
                The difference is bucketing divides the files by Column Name, and partitioning divides the files under By a particular value inside table. In bucketing due to equal volumes of data in each partition, joins at Map side will be quicker.
    Distribute By: Repartitions a DataFrame by the given expressions. The number of partitions is equal to spark.sql.shuffle.partitions. 
    Cluster By: This is just a shortcut for using distribute by and sort by together on the same set of expressions.
    sort by: to sort the data based on the data type of the column to be used for sorting per reducer i.e. overall sorting of output is not maintained. e.g. if column is of numeric type the data will be sorted per reducer in numeric order.
    ORDER BY of SQL. The overall sorting is maintained in case of order by and output is produced in single reducer. Hence, we need to use limit clause so that reducer is not overloaded.
    Distribute By to distribute the rows among reducers. All rows with the same Distribute By columns will go to the same reducer. However,Distribute By does not guarantee clustering or sorting properties on the distributed keys.
    Cluster By is a short-cut for both Distribute By and Sort By.


