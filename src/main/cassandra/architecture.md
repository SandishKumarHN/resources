

## Cassandra ##

-   Distributed and decentralized: no node is in control, so no single point of failure. Non-relational: some concepts carry over from SQL, but not all. Specifically, no JOIN, GROUP BY. Fault tolerant: data is replicated, so failure can be handled gracefully.
    The usual Cassandra setup is to have n nodes each acting as part of the cluster. They coordinate with each other and decide how to partition the data so it's evenly distributed and redundant. A client can connect to any of them to make queries.

### Architecture: ### 

### Key structures: ### 

-   Node: Where you store your data. It is the basic infrastructure component of Cassandra.

-   Datacenter A collection of related nodes. A datacenter can be a physical datacenter or virtual datacenter. Different workloads should use separate datacenters, either physical or virtual. Replication is set by datacenter. Using separate datacenters prevents Cassandra transactions from being impacted by other workloads and keeps requests close to each other for lower latency. Depending on the replication factor, data can be written to multiple datacenters. datacenters must never span physical locations.

-   Cluster: A cluster contains one or more datacenters. It can span physical locations.

-   SSTABLE, COMMITLOG, CQLTABLE.

### Key components for configuring Cassandra: ### 

-   Gossip: A peer-to-peer communication protocol to discover and share location and state information about the other nodes in a Cassandra cluster. Gossip information is also persisted locally by each node to use immediately when a node restarts.

-   Partitioner: A partitioner determines which node will receive the first replica of a piece of data, and how to distribute other replicas across other nodes in the cluster. Each row of data is uniquely identified by a primary key, which may be the same as its partition key, but which may also include other clustering columns. A partitioner is a hash function that derives a token from the primary key of a row. The partitioner uses the token value to determine which nodes in the cluster receive the replicas of that row.

-   Replication factor, Replica placement strategy

-   Snitch: A snitch defines groups of machines into datacenters and racks (the topology) that the replication strategy uses to place replicas.

-   Consistent hashing allows distribution of data across a cluster to minimize reorganization when nodes are added or removed. Consistent hashing partitions data based on the partition key. 

-   Cassandra places the data on each node according to the value of the partition key and the range that the node is responsible for.

###  Replication: ### 

-   SimpleStrategy: Use only for a single datacenter and one rack. SimpleStrategy places the first replica on a node determined by the partitioner. Additional replicas are placed on the next nodes clockwise in the ring without considering topology (rack or datacenter location).

-   NetworkTopologyStrategy:places replicas in the same datacenter by walking the ring clockwise until reaching the first node in another rack. NetworkTopologyStrategy attempts to place replicas on distinct racks because nodes in the same rack (or similar physical grouping) often fail at the same time due to power, cooling, or network issues. 

###  Partitions: ### 

-   Murmur3Partitioner (default): uniformly distributes data across the cluster based on MurmurHash hash values.

-   RandomPartitioner: uniformly distributes data across the cluster based on MD5 hash values.

-   ByteOrderedPartitioner: keeps an ordered distribution of data lexically by key bytes.

### Snitches: ### 
-   Dynamic snitching Monitors the performance of reads from the various replicas and chooses the best replica based on this history. SimpleSnitch The SimpleSnitch is used only for single-datacenter deployments. RackInferringSnitch Determines the location of nodes by rack and datacenter corresponding to the IP addresses. PropertyFileSnitch Determines the location of nodes by rack and datacenter. GossipingPropertyFileSnitch Automatically updates all nodes using gossip when adding new nodes and is recommended for production. Ec2Snitch Use the Ec2Snitch with Amazon EC2 in a single region. Ec2MultiRegionSnitch Use the Ec2MultiRegionSnitch for deployments on Amazon EC2 where the cluster spans multiple regions. GoogleCloudSnitch Use the GoogleCloudSnitch for Cassandra deployments on Google Cloud Platform across one or more regions. CloudstackSnitch Use the CloudstackSnitch for Apache Cloudstack environments.

###  Cassandra tools: ### 

-   The nodetool utility: A list of the available commands for managing a cluster. i.e cleanup, enalbleback, takesnapshot, repaire etc.

-   The cassandra utility: You can start Cassandra 3.0 and 3.1 by adding them to the cassandra-env.sh file (package or tarball installations) or entering them at the command line in tarball installations. 

-   The cassandra-stress tool: A Java-based stress testing utility for basic benchmarking and load testing a Cassandra cluster. 

-   SSTable utilities Tools: for using, upgrading, and changing Cassandra SSTables. i.e sstabledump, sstablereset etc
          
### Fault Tolerance: ### 

-   The replication factor in the keyspace lets Cassandra handle failures. 

### How is data written? ### 
    
-   Logging data in the commit log, Writing data to the memtable, Flushing data from the memtable, Storing data on disk in SSTables.
    When a write occurs, Cassandra stores the data in a memory structure called memtable, and to provide configurable durability, it also appends writes to the commit log on disk.  The memtable stores writes in sorted order until reaching a configurable limit, and then is flushed.
    To flush the data, Cassandra writes the data to disk, in the memtable-sorted order.. A partition index is also created on the disk that maps the tokens to a location on disk. When the memtable content exceeds the configurable threshold or the commitlog space exceeds the commitlog_total_space_in_mb, the memtable is put in a queue that is flushed to disk. The queue can be configured with the memtable_heap_space_in_mb or memtable_offheap_space_in_mb setting in the cassandra.yaml file. If the data to be flushed exceeds the memtable_cleanup_threshold, Cassandra blocks writes until the next flush succeeds. You can manually flush a table using nodetool flushor nodetool drain.
    Data in the commit log is purged after its corresponding data in the memtable is flushed to an SSTable on disk.
    Memtables and SSTables are maintained per table. The commit log is shared among tables. SSTables are immutable, not written to again after the memtable is flushed. Consequently, a partition is typically stored across multiple SSTable files. 
    Data (Data.db) The SSTable data Primary Index (Index.db) Index of the row keys with pointers to their positions in the data file Bloom filter (Filter.db) A structure stored in memory that checks if row data exists in the memtable before accessing SSTables on disk. etc like compression, CRC, SSTABLE index summary, SSTBALE table of contents, Secondary index.
    The SSTables are files stored on disk. For each keyspace, a directory within the data directory stores each table. For example, /data/data/ks1/cf1-5be396077b811e3a3ab9dc4b9ac088d/la-1-big-Data.db represents a data file. ks1 represents the keyspace name to distinguish the keyspace for streaming or bulk loading data. A hexadecimal string, 5be396077b811e3a3ab9dc4b9ac088d in this example, is appended to table names to represent unique table IDs.

###  How is data maintained? ### 
    
-   Cassandra writes new timestamped versions of the inserted or updated data in new SSTables. Cassandra does not perform deletes by removing the deleted data: instead, Cassandra marks it with tombstones(A marker in a row that indicates a column was deleted. During compaction, marked columns are deleted.)
    To keep the database healthy, Cassandra periodically merges SSTables and discards old data. This process is called compaction.
    compaction collects all versions of each unique row and assembles one complete row, using the most up-to-date version (by timestamp) of each of the row's columns. The merge process is performant, because rows are sorted by partition key within each SSTable, and the merge process does not use random I/O. The new versions of each row is written to a new SSTable. The old versions, along with any rows that are ready for deletion, are left in the old SSTables, and are deleted as soon as pending reads are completed.
    SizeTieredCompactionStrategy (STCS) Recommended for write-intensive workloads. The SizeTieredCompactionStrategy (STCS) initiates compaction when Cassandra has accumulated a set number (default: 4) of similar-sized SSTables.
    LeveledCompactionStrategy (LCS) Recommended for read-intensive workloads. data in memtables is flushed to SSTables in the first level (L0). LCS compaction merges these first SSTables with larger SSTables in level L1.
    TimeWindowCompactionStrategy (TWCS) Recommended for time series and expiring TTL workloads.

###  How is data updated? ### 
    
-   Cassandra treats each new row as an upsert: if the new row has the same primary key as that of an existing row, Cassandra processes it as an update to the existing row.
    Periodically, the rows stored in memory are streamed to disk into structures called SSTables. At certain intervals, Cassandra compacts smaller SSTables into larger SSTables. If Cassandra encounters two or more versions of the same row during this process, Cassandra only writes the most recent version to the new SSTable. After compaction, Cassandra drops the original SSTables, deleting the outdated rows
    Most Cassandra installations store replicas of each row on two or more nodes. Each node performs compaction independently. This means that even though out-of-date versions of a row have been dropped from one node, they may still exist on another node.
    This is why Cassandra performs another round of comparisons during a read process. When a client requests data with a particular primary key, Cassandra retrieves many versions of the row from one or more replicas. The version with the most recent timestamp is the only one returned to the client ("last-write-wins").

###  How is data deleted? ### 
    
-   Cassandra treats a delete as an insert or upsert. The data being added to the partition in the DELETE command is a deletion marker called a tombstone. The tombstones go through Cassandra's write path, and are written to SSTables on one or more nodes. The key difference feature of a tombstone: it has a built-in expiration date/time. At the end of its expiration period (for details see below) the tombstone is deleted as part of Cassandra's normal compaction process.
    You can also mark a Cassandra record (row or column) with a time-to-live value. After this amount of time has ended, Cassandra marks the record with a tombstone, and handles it like other tombstoned records.

###  How are indexes stored and updated? ### 
    
-   Secondary indexes are used to filter a table for data stored in non-primary key columns.
    Secondary indexes can be built for a column in a table. These indexes are stored locally on each node in a hidden table and built in a background process. If a secondary index is used in a query that is not restricted to a particular partition key, the query will have prohibitive read latency because all nodes will be queried. A query with these parameters is only allowed if the query option ALLOW FILTERING is used.

###  How is data read? ### 
    
-   Check the memtable Check row cache, if enabled Checks Bloom filter Checks partition key cache, if enabled Goes directly to the compression offset map if a partition key is found in the partition key cache, or checks the partition summary if not If the partition summary is checked, then the partition index is accessed Locates the data on disk using the compression offset map Fetches the data from the SSTable on disk.
    Cassandra consults an in-memory data structure called a Bloom filter that checks the probability of an SSTable having the needed data. The Bloom filter can tell very quickly whether the file probably has the needed data, or certainly does not have it. If answer is a tenative yes, Cassandra consults another layer of in-memory caches, then fetches the compressed data on disk. If the answer is no, Cassandra doesn't trouble with reading that SSTable at all, and moves on to the next. To satisfy a read, Cassandra must combine results from the active memtable and potentially multiple SSTables.

###  Consistency: ### 
    
-   This means that Cassandra has eventual consistency: the data will be consistent, but with some delay. Usually the delay will be short (network latency + milli­seconds). If there's a network partition, it will be longer (after communication is restored).
    Cassandra gives us a choice of how consistent we demand to be with our reads and writes. We can have different requirements for consistency for each session/​query.

-   Write Consistency Levels:

    -   ALL: A write must be written to the commit log and memtable on all replica nodes in the cluster for that partition
   
    -   EACH_QUORUM: Strong consistency. A write must be written to the commit log and memtable on a quorum of replica nodes in each datacenter.
   
    -   QUORUM: A write must be written to the commit log and memtable on a quorum of replica nodes across all datacenters.
   
    -   LOCAL_QUORUM: Strong consistency. A write must be written to the commit log and memtable on a quorum of replica nodes in the same datacenter as the coordinator. Avoids latency of inter-datacenter communication.
   
    -   ONE: A write must be written to the commit log and memtable of at least one replica node.
   
    -   TWO: A write must be written to the commit log and memtable of at least two replica nodes.
   
    -   THREE: A write must be written to the commit log and memtable of at least three replica nodes.
   
    -   LOCAL_ONE: A write must be sent to, and successfully acknowledged by, at least one replica node in the local datacenter.
   
    -   ANY: A write must be written to at least one node. If all replica nodes for the given partition key are down, the write can still succeed after a hinted handoff has been written. If all replica nodes are down at write time, an ANY write is not readable until the replica nodes for that partition have recovered.
        
-   Read Consistency Levels:

    -   ALL: Returns the record after all replicas have responded. The read operation will fail if a replica does not respond.
   
    -   EACH_QUORUM:	Not supported for read
   
    -   QUORUM: Returns the record after a quorum of replicas from all datacenters has responded.
   
    -   LOCAL_QUORUM: Returns the record after a quorum of replicas in the current datacenter as the coordinator has reported. Avoids latency of inter-datacenter communication.
   
    -   ONE: Returns a response from the closest replica, as determined by the snitch. By default, a read repair runs in the background to make the other replicas consistent.
   
    -   TWO: Returns the most recent data from two of the closest replicas.
   
    -   THREE: Returns the most recent data from three of the closest replicas.
   
    -   LOCAL_ONE: Returns a response from the closest replica in the local datacenter.
   
    -   SERIAL: Allows reading the current (and possibly uncommitted) state of data without proposing a new addition or update. If a SERIAL read finds an uncommitted transaction in progress, it will commit the transaction as part of the read. Similar to QUORUM.
   
    -   LOCAL_SERIAL: Same as SERIAL, but confined to the datacenter. Similar to LOCAL_QUORUM. 

-   How QUORUM is calculated:
   
    -   quorum = (sum_of_replication_factors / 2) + 1
   
    -   sum_of_replication_factors = datacenter1_RF + datacenter2_RF + . . . + datacentern_RF