
## Spark Submision Breakdown: ##
![Alt text](https://jaceklaskowski.gitbooks.io/mastering-apache-spark/images/sparkstandalone-sparkcontext-taskscheduler-schedulerbackend.png)

![Alt text](https://hxquangnhat.files.wordpress.com/2015/03/scheduling.jpeg)    
### DAGScheduler: ###
        -   A stage is a set of independent tasks all computing the same function that need to run as part of a Spark job, 
            where all the tasks have the same shuffle dependencies. Each DAG of tasks run by the scheduler is split up into 
            stages at the boundaries where shuffle occurs, and then the DAGScheduler runs these stages in topological order.
        -   Each Stage can either be a shuffle map stage, in which case its tasks’ results are input for another stage, 
            or a result stage, in which case its tasks directly compute the action that initiated a job.
        
    TaskScheduler:  
        -   DAGScheduler’s just submitted the taskSet to the taskScheduler
        -   creates new TaskSet, submits it and waits for StageCompleted event
        -   When TaskScheduler submits a TaskSet, it also creates one TaskSetManager to track that taskset
        
    Worker:
        -   Spark calls worker as Executor. The backend will receive the worker list from the Cluster Manager, 
            then it will launchTask at the Executor. A BlockManager at each Executor will help it to deal with shuffle 
            data and cached RDDs. New TaskRunner is created at the Executor and it starts the threadpool to process taskset,
            each task runs on one thread.
        -   deserialize the broadcast stage from the TaskScheduler, initialize some variables for keeping track status, 
        -   MapShuffleTask: iterate on each partition of RDD and compute them, write data out through shufflewriter.
        -   ResultTask: acts the same way as MapShuffleTask, it has MapOutputTrackerWorker to know the map output information and get it.
        -   after finishing task, result will be sent back to driver or saved to disk.
    
### BlockManager: ###
    
![Alt text](https://jaceklaskowski.gitbooks.io/mastering-apache-spark/images/sparkcontext-broadcast-bittorrent.png)

        -   is a key-value store of blocks of data (block storage) identified by a block ID.
        -   acts as a local cache that runs on every node in a Spark cluster, i.e. the driver and executors.
        -   provides interface for uploading and fetching blocks both locally and remotely using various stores, i.e. memory, disk, and off-heap.
        -   is created exclusively when SparkEnv is created (for the driver and executors). While being created, BlockManager gets a DiskBlockManager, BlockInfoManager, MemoryStore and DiskStore 
            (that it immediately wires together, i.e. BlockInfoManager with MemoryStore and DiskStore with DiskBlockManager).
        -   uses a Scala ExecutionContextExecutorService to execute FIXME asynchronously (on a thread pool with block-manager-future prefix and maximum of 128 threads)
        -   it manages the storage for blocks that can represent cached RDD partitions, intermediate shuffle outputs, broadcasts, etc.
    