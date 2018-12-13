## Cassandra Data Model: ## 

-   A Cassandra cluster has many keyspaces. Each keyspace has many tables. A “table” is what you expect: columns of data; rows that have a cell for each column; columns have a fixed type; a primary key determinies how the data is organized.

-   The keyspace is more than just a container for some tables. A keyspace has rules about how it's replicated: can be a simple replication factor, or a description of how many replicas should be in each datacentre around the world.

-   The first part of the primary key is the partition key. It controls which node(s) store the record. Records with the same partition key will all be on the same nodes. i.e, PRIMARY KEY (i1,i2), so i1 is the partition key. Any records with the same i1 will be stored on the same nodes.

-   The primary key determines the layout of data on the nodes/​disk. Accessing data in any other way is potentially very expensive.  CQL INSERT isn't really an insert: it's “insert or update by primary key”. Since we had PRIMARY KEY (i1,i2). That implies that primary keys must be unique. Sometimes the easiest way around that is to just use a UUID.

-   Spread data evenly around the cluster: You want every node in the cluster to have roughly the same amount of data. Rows are spread around the cluster based on a hash of the partition key, which is the first element of the PRIMARY KEY. So, the key to spreading data evenly is this: pick a good primary key.

-   Minimize the number of partitions read

-   Everything is dependent on your read/write pattern No Range Scans? No need for clustering keys, Yes Range Scans? Clustering keys a must.

-   Partition Key: Cassandra is a distributed database in which data is partitioned and stored across multiple nodes within a cluster. The partition key is made up of one or more data fields and is used by the partitioner to generate a token via hashing to distribute the data uniformly across a cluster.

-   Clustering Key: A clustering key is made up of one or more fields and helps in clustering or grouping together rows with same partition key and storing them in sorted order. The combination of partition key and clustering key makes up the primary key and uniquely identifies any record in the Cassandra cluster.

#### Data Modeling Example-1: #### 

Suppose that we are storing Facebook posts of different users in Cassandra. One of the common query patterns will be fetching the top ‘N‘ posts made by a given user. Thus, we need to store all data for a particular user on a single partition as per the above guidelines. Also, using the post timestamp as the clustering key will be helpful for retrieving the top ‘N‘ posts more efficiently.

#### Data Modeling Example-2: #### 

Suppose that we are storing the details of different partner gyms across the different cities and states of many countries and we would like to fetch the gyms for a given city. Also, let’s say we need to return the results having gyms sorted by their opening date. Based on the above guidelines, we should store the gyms located in a given city of a specific state and country on a single partition and use the opening date and gym name as a clustering key.

#### Data Modeling Example-3: #### 

Let’s say we are running an e-commerce store and that we are storing the Customer and Product information within Cassandra. Let’s look at some of the common query patterns around this use case: Get Customer info Get Product info Get all Customers who like a given Product Get all Products a given Customer likes We will start by using separate tables for storing the Customer and Product information. However, we need to introduce a fair amount of denormalization to support the 3rd and 4th queries shown above. We will create two more tables to achieve this – “Customer_by_Product” and “Product_by_Customer“.
  