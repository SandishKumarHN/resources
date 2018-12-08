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

    
## Caching ##
## Shared Variable ##
    Broadcasting:
    Accumulators:       
## RDD STORAGE: ##

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






