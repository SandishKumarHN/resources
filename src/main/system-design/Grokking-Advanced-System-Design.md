### Overview: ###
#### System design case studies: ####
#### Dynamo: ####
  - Dynamo is a highly available key-value store, Dynamo provides a flexible design to let applications choose their desired level of availability and consistency.
  - Dynamo falls within the category of AP systems (i.e., available and partition tolerant) and is designed for high availability and partition tolerance at the expense of strong consistency
  - Amazon DynamoDB is "built on the principles of Dynamo" and is a hosted service within the AWS infrastructure. However, while Dynamo is based on leaderless replication, DynamoDB uses single-leader replication.
  
  Design goals:
  
    - Scalable: The system should be highly scalable. We should be able to throw a machine into the system to see proportional improvement.
    - Decentralized: To avoid single points of failure and performance bottlenecks, there should not be any central/leader process.
    - Eventually Consistent: Data can be optimistically replicated to become eventually consistent. 
      This means that instead of incurring write-time costs to ensure data correctness throughout the system (i.e., strong consistency), inconsistencies can be resolved at some other time (e.g., during reads). 
      Eventual consistency is used to achieve high availability
  
  Dynamoc Use Cases:
  
    - By default, Dynamo is an eventually consistent database. Therefore, any application where strong consistency is not a concern can utilize Dynamo. 
  
  System APIs:
  
    - Dynamo treats both the object and the key as an arbitrary array of bytes (typically less than 1 MB). It applies the MD5 hashing algorithm on the key to generate a 128-bit identifier which is used to determine the storage nodes that are responsible for serving the key.
    - get(key): The get operation finds the nodes where the object associated with the given key is located and returns either a single object or a list of objects with conflicting versions along with a context. The context contains encoded metadata about the object that is meaningless to the caller and includes information such as the version of the object
    - put(key, context, object): The put operation finds the nodes where the object associated with the given key should be stored and writes the given object to the disk. The context is a value that is returned with a get operation and then sent back with the put operation. The context is always stored along with the object and is used like a cookie to verify the validity of the object supplied in the put request.
  
  Dynamo’s architecture:
  
    Data distribution:
      - Dynamo uses Consistent Hashing to distribute its data among nodes. Consistent hashing also makes it easy to add or remove nodes from a Dynamo cluster.
      - A naive approach will be to use a suitable hash function that maps the data key to a number. Then, find the server by applying modulo on this number and the total number of servers.
      - Dynamo uses consistent hashing to solve these problems. The consistent hashing algorithm helps Dynamo map rows to physical nodes and also ensures that only a small set of keys move when servers are added or removed.
      - Consistent Hashing:
            - he ring is divided into smaller predefined ranges. Each node is assigned one of these ranges. In Dynamo’s terminology, the start of the range is called a token. This means that each node will be assigned one token.
            - Whenever Dynamo is serving a put() or a get() request, the first step it performs is to apply the MD5 hashing algorithm to the key. The output of this hashing algorithm determines within which range the data lies and hence, on which node the data will be stored.
            - when a node is removed, the next node becomes responsible for all of the keys stored on the outgoing node. However, this scheme can result in non-uniform data and load distribution. Dynamo solves these issues with the help of Virtual nodes.
      - Virtual nodes:
            - isusues without vnodes: dding or removing nodes: Adding or removing nodes will result in recomputing the tokens causing a significant administrative overhead for a large cluster. 
              Hotspots: Since each node is assigned one large range, if the data is not evenly distributed, some nodes can become hotspots. 
              Node rebuilding: Since each node’s data is replicated on a fixed number of nodes (discussed later), when we need to rebuild a node, only its replica nodes can provide the data. This puts a lot of pressure on the replica nodes and can lead to service degradation.
            - Vnodes are randomly distributed across the cluster and are generally non-contiguous so that no two neighboring Vnodes are assigned to the same physical node. Furthermore, nodes do carry replicas of other nodes for fault-tolerance. 
              Also, since there can be heterogeneous machines in the clusters, some servers might hold more Vnodes than others. The figure below shows how physical nodes A, B, C, D, & E are using Vnodes of the Consistent Hash ring. Each physical node is assigned a set of Vnodes and each Vnode is replicated once.
            - Vnodes help spread the load more evenly across the physical nodes on the cluster by dividing the hash ranges into smaller subranges.
              This speeds up the rebalancing process after adding or removing nodes. When a new node is added, it receives many Vnodes from the existing nodes to maintain a balanced cluster. Similarly, when a node needs to be rebuilt, instead of getting data from a fixed number of replicas, many nodes participate in the rebuild process.
            - Vnodes make it easier to maintain a cluster containing heterogeneous machines. This means, with Vnodes, we can assign a high number of ranges to a powerful server and a lower number of ranges to a less powerful server.
            - Since Vnodes help assign smaller ranges to each physical node, the probability of hotspots is much less than the basic Consistent Hashing scheme which uses one big range per node
        
    Data replication and consistency:
      - Data is replicated optimistically, i.e., Dynamo provides eventual consistency.
      - Dynamo replicates each data item on multiple NN nodes in the system where the value NN is equivalent to the replication factor and is configurable per instance of Dynamo. 
        Each key is assigned to a coordinator node (the node that falls first in the hash range), which first stores the data locally and then replicates it to N-1N−1 clockwise successor nodes on the ring. 
        This results in each node owning the region on the ring between it and its NthNth predecessor. 
      - This replication is done asynchronously (in the background), and Dynamo provides an eventually consistent model. This replication technique is called optimistic replication, which means that replicas are not guaranteed to be identical at all times.
      - Each node in Dynamo serves as a replica for a different range of data. As Dynamo stores NN copies of data spread across different nodes, if one node is down, other replicas can respond to queries for that range of data. 
        If a client cannot contact the coordinator node, it sends the request to a node holding a replica.
      - Preference List:
          - The list of nodes responsible for storing a particular key is called the preference list. Dynamo is designed so that every node in the system can determine which nodes should be in this list for any specific key 
            list only contains distinct physical nodes(no Vnodes)
      - Sloppy quorum and handling of temporary failures:
          - To increase the availability, Dynamo does not enforce strict quorum requirements, and instead uses something called sloppy quorum.
            all read/write operations are performed on the first NN healthy nodes from the preference list, which may not always be the first NN nodes encountered while moving clockwise on the consistent hashing ring.
          - Hinted handoff: when a node is unreachable, another node can accept writes on its behalf. The write is then kept in a local buffer and sent out once the destination node is reachable again
          - it is possible for two concurrent writes to the same key to be accepted by non-overlapping sets of nodes. This means that multiple conflicting values against the same key can exist in the system, and we can get stale or conflicting data while reading. Dynamo allows this and resolves these conflicts using Vector Clocks.
    Handling temporary failures:  
      - To handle temporary failures, Dynamo replicates data to a sloppy quorum of other nodes in the system instead of a strict majority quorum.
    Inter-node communication and failure detection:
      - Dynamo’s nodes use gossip protocol to keep track of the cluster state.
      - Gossip Protocol:
        - Dynamo cluster, since we do not have any central node that keeps track of all nodes to know if a node is down or not, how does a node know every other node’s current state? The simplest way to do this is to have every node maintain heartbeats with every other node. 
          When a node goes down, it will stop sending out heartbeats, and everyone else will find out immediately.
        - Dynamo uses gossip protocol that enables each node to keep track of state information about the other nodes in the cluster, like which nodes are reachable, what key ranges they are responsible for, and so on
        - Gossip protocol is a peer-to-peer communication mechanism in which nodes periodically exchange state information about themselves and other nodes they know about.
        - Seed nodes are fully functional nodes and can be obtained either from a static configuration or a configuration service. 
          This way, all nodes are aware of seed nodes. Each node communicates with seed nodes through gossip protocol to reconcile membership changes; therefore, logical partitions are highly unlikely.
    High availability:
      - Dynamo makes the system “always writeable” (or highly available) by using hinted handoff.
    Conflict resolution and handling permanent failures:
      - Use vector clocks to keep track of value history and reconcile divergent histories at read time
      - In the background, dynamo uses an anti-entropy mechanism like Merkle trees to handle permanent failures.
      - Vector Clocks:
        - clock skew, i.e., different clocks tend to run at different rates, so we cannot assume that time t on node a happened before time t + 1 on node b. 
          The most practical techniques that help with synchronizing clocks, like NTP, still do not guarantee that every clock in a distributed system is synchronized at all times. So, without special hardware like GPS units and atomic clocks, just using wall clock timestamps is not enough.
        - Dynamo uses something called vector clock in order to capture causality between different versions of the same object. A vector clock is effectively a (node, counter) pair. One vector clock is associated with every version of every object stored in Dynamo. 
          One can determine whether two versions of an object are on parallel branches or have a causal ordering by examining their vector clocks. If the counters on the first object’s clock are less-than-or-equal to all of the nodes in the second clock, then the first is an ancestor of the second and can be forgotten.
          Otherwise, the two changes are considered to be in conflict and require reconciliation
        - Dynamo truncates vector clocks (oldest first) when they grow too large. If Dynamo ends up deleting older vector clocks that are required to reconcile an object’s state, Dynamo would not be able to achieve eventual consistency. 
          Dynamo’s authors note that this is a potential problem but do not specify how this may be addressed. They do mention that this problem has not yet surfaced in any of their production systems.
      - Conflict-free replicated data types:
        - The idea that any two nodes that have received the same set of updates will see the same end result is called strong eventual consistency
      - Last-write-wins:
        - Instead of vector clocks, Dynamo also offers ways to resolve the conflicts automatically on the server-side. Dynamo (and Apache Cassandra) often uses a simple conflict resolution policy: last-write-wins (LWW), based on the wall-clock timestamp. LWW can easily end up losing data.
    
    The Life of Dynamo’s put() & get() Operations:
        - Clients can route their requests through a generic load balancer. Clients can use a partition-aware client library that routes the requests to the appropriate coordinator nodes with lower latency.
        - In the first strategy, the client is unaware of the Dynamo ring, which helps scalability and makes Dynamo’s architecture loosely coupled.
          it is possible that the node it selects is not part of the preference list. This will result in an extra hop, as the request will then be forwarded to one of the nodes in the preference list by the intermediate node.
        - The second strategy helps in achieving lower latency, as in this case, the client maintains a copy of the ring and forwards the request to an appropriate node from the preference list. Because of this option, Dynamo is also called a zero-hop DHT, as the client can directly contact the node that holds the required data. 
          However, in this case, Dynamo does not have much control over the load distribution and request handling.
        Consistency protocol:
            - ‘put()’ process:
              - The coordinator generates a new data version and vector clock component.
                Saves new data locally.
                Sends the write request to N-1N−1 highest-ranked healthy nodes from the preference list.
                The put() operation is considered successful after receiving W-1W−1 confirmation.
            - 'get()’ process:
                The coordinator requests the data version from N-1N−1 highest-ranked healthy nodes from the preference list.
                Waits until R-1R−1 replies.
                Coordinator handles causal data versions through a vector clock.
                Returns all relevant data versions to the caller.
            - Request handling through state machine:
                Send read requests to the nodes.
                Wait for the minimum number of required responses.
                If too few replies were received within a given time limit, fail the request.
                Otherwise, gather all the data versions and determine the ones to be returned.
                If versioning is enabled, perform syntactic reconciliation and generate an opaque write context that contains the vector clock that subsumes all the remaining versions.
    
    Anti-entropy Through Merkle Trees:
        - Dynamo uses Merkle trees to compare replicas of a range. A Merkle tree is a binary tree of hashes, where each internal node is the hash of its two children, and each leaf node is a hash of a portion of the original data.
        - Comparing Merkle trees is conceptually simple: Compare the root hashes of both trees. If they are equal, stop. Recurse on the left and right children.
        - The principal advantage of using a Merkle tree is that each branch of the tree can be checked independently without requiring nodes to download the entire tree or the whole data set. 
          Hence, Merkle trees minimize the amount of data that needs to be transferred for synchronization and reduce the number of disk reads performed during the anti-entropy process.
        - The disadvantage of using Merkle trees is that many key ranges can change when a node joins or leaves, and as a result, the trees need to be recalculated.

#### Cassandra ####
  - Goal : Design a distributed and scalable system that can store a huge amount of structured data, which is indexed by a row key where each row can have an unbounded number of columns.
  - assandra combines the distributed nature of Amazon’s Dynamo which is a key-value store and the data model of Google’s BigTable which is a column-based data store.
  

    
      
    
  
