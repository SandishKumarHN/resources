
## Topic: ## 

-   Messages in Kafka are categorized into topics. Topics are additionally broken down into a number of partitions. Going back to the “commit log” description, 
    a partition is a sin‐ gle log. Messages are written to it in an append-only fashion, and are read in order 
    from beginning to end. Note that as a topic typically has multiple partitions, there is no guarantee of message 
    time-ordering across the entire topic, just within a single partition. Partitions are also the way that Kafka provides redundancy and scalability.
    Each partition can be hosted on a different server, which means that a single topic can be scaled horizontally across multiple servers
    to provide performance far beyond the ability of a single server.
    
![Alt text](https://www.oreilly.com/library/view/kafka-the-definitive/9781491936153/assets/ktdg_0105.png)

-   

-   

## Brokers and Clusters ##

-   A single Kafka server is called a broker. The broker receives messages from producers, assigns offsets to them, 
    and commits the messages to storage on disk. It also services consumers, responding to fetch requests for partitions and responding with the messages that have been committed to disk. 
    Depending on the specific hardware and its performance characteristics, a single broker can easily handle thousands of partitions and millions of messages per second.

-   Kafka brokers are designed to operate as part of a cluster. Within a cluster of brokers, one broker will also function as the cluster controller (elected automatically from the live members of the cluster).
    The controller is responsible for administrative operations, including assigning partitions to brokers and monitoring for broker failures. 
    A partition is owned by a single broker in the cluster, and that broker is called the leader of the partition. 
    A partition may be assigned to multiple brokers, which will result in the partition being replicated (as seen in Figure). 
    This provides redundancy of messages in the partition, such that another broker can take over leadership if there is a broker failure. 
    However, all consumers and producers operating on that partition must connect to the leader.
    
![Alt text](https://www.oreilly.com/library/view/kafka-the-definitive/9781491936153/assets/ktdg_0107.png)

-   Data Retention:

    -   Durable storage of messages for some period of time. Kafka brokers are configured with a default reten‐ tion setting for topics, 
        either retaining messages for some period of time (e.g., 7 days) or until the topic reaches a certain size in bytes (e.g., 1 GB). 
        Once these limits are reached, messages are expired and deleted so that the retention configuration is a minimum amount of data available at any time.
        
    -   Individual topics can also be configured with their own retention settings so that messages are stored for only as long as they are useful.

## Partition: ## 

## Log: ## 

## Producer: ## 

-   A message will be produced to a specific topic. By default, the producer does not care what partition a specific message is written to and 
    will balance messages over all partitions of a topic evenly
    
-   The pro‐ ducer will direct messages to specific partitions. This is typically done using the mes‐ sage key 
    and a partitioner that will generate a hash of the key and map it to a specific partition. This assures that all messages produced with a given key will get written to the same partition. 
    The producer could also use a custom partitioner that follows other business rules for mapping messages to partitions.

![Alt text](https://i.stack.imgur.com/qhGRl.png)

-   We start producing messages to Kafka by creating a ProducerRecord, which must include the topic we want to send the record to and a value. 
    Optionally, we can also specify a key and/or a partition. Once we send the ProducerRecord, the first thing the producer will do is serialize the 
    key and value objects to ByteArrays so they can be sent over the network.

-   Next, the data is sent to a partitioner. If we specified a partition in the ProducerRecord, the partitioner doesn’t do anything and simply returns the partition we specified.
    If we didn’t, the partitioner will choose a partition for us, usually based on the ProducerRecord key. 
    Once a partition is selected, the producer knows which topic and partition the record will go to. 
    It then adds the record to a batch of records that will also be sent to the same topic and partition. 
    A separate thread is responsible for sending those batches of records to the appropriate Kafka brokers

-   When the broker receives the messages, it sends back a response. If the messages were successfully written to Kafka, 
    it will return a RecordMetadata object with the topic, partition, and the offset of the record within the partition. 
    If the broker failed to write the messages, it will return an error. When the producer receives an error,
    it may retry sending the message a few more times before giving up and returning an error.
    
#### Acks: ####

-   The acks parameter controls how many partition replicas must receive the record before the producer can consider the write successful.
    This option has a significant impact on how likely messages are to be lost. There are three allowed values for the acks parameter:
    
    -   acks=0, the producer will not wait for a reply from the broker before assuming the message was sent successfully. 
        This means that if something went wrong and the broker did not receive the message, the producer will not know about it and the message will be lost.
        However, because the producer is not waiting for any response from the server, it can send messages as fast as the network will support, 
        so this setting can be used to achieve very high throughput.
     
    -   If acks=1, the producer will receive a success response from the broker the moment the leader replica received the message. 
        If the message can’t be written to the leader (e.g., if the leader crashed and a new leader was not elected yet),
        the producer will receive an error response and can retry sending the message, avoiding potential loss of data.
    
    -   acks=all, the producer will receive a success response from the broker once all in-sync replicas received the message. 
        This is the safest mode since you can make sure more than one broker has the message and that the message will survive even in the case of crash
        
#### Serializer and DeSerializer ####
![Alt text](http://www.dengshenyu.com/assets/kafka-producer/avro.png)


       
## Consumer and Consumer Group: ## 

-   Consumers work as part of a consumer group, which is one or more consumers that work together to consume a topic. 
    The group assures that each partition is only con‐ sumed by one member. 
    
-   Consumers can horizontally scale to consume topics with a large number of messages. Additionally, if a single consumer fails, 
    the remaining members of the group will rebalance the partitions being consumed to take over for the missing member. 

![Alt text](https://www.oreilly.com/library/view/kafka-the-definitive/9781491936153/assets/ktdg_0106.png)
  
-   Kafka consumers are typically part of a consumer group. When multiple consumers are subscribed to a topic and belong to the same consumer group, 
    each consumer in the group will receive messages from a different subset of the partitions in the topic.

![Alt text](https://www.oreilly.com/library/view/kafka-the-definitive/9781491936153/assets/ktdg_04in01.png)
![Alt text](https://i.stack.imgur.com/PeZlZ.png)
![Alt text](https://www.oreilly.com/library/view/kafka-the-definitive/9781491936153/assets/ktdg_04in04.png)
![Alt text](https://www.oreilly.com/library/view/kafka-the-definitive/9781491936153/assets/ktdg_04in05.png)

-   As we saw in the previous section, consumers in a consumer group share ownership of the partitions in the topics they subscribe to. When we add a new consumer to the group, 
    it starts consuming messages from partitions previously consumed by another consumer. The same thing happens when a consumer shuts down or crashes;
    it leaves the group, and the partitions it used to consume will be consumed by one of the remaining consumers. 
    Reassignment of partitions to consumers also happen when the topics the consumer group is consuming are modified 
     
-   Moving partition ownership from one consumer to another is called a rebalance. 
    Rebalances are important because they provide the consumer group with high availa‐ bility and scalability
    
-   During a rebalance, con‐ sumers can’t consume messages, so a rebalance is basically a short window of unavail‐ ability of the entire consumer group. 
    In addition, when partitions are moved from one consumer to another, the consumer loses its current state; if it was caching any data, 
    it will need to refresh its caches—slowing down the application until the con‐ sumer sets up its state again. 
    
-   The way consumers maintain membership in a consumer group and ownership of the partitions assigned to them is by sending heartbeats to a Kafka broker designated as the group coordinator 
    As long as the consumer is sending heartbeats at regular intervals, it is assumed to be alive, well, and processing messages from its partitions. Heartbeats are sent when the consumer polls and when it commits records it has consumed.
    
-   If the consumer stops sending heartbeats for long enough, its session will time out and the group coordinator will consider it dead and trigger a rebalance.
    
#### Commits and Offsets ####
-   Whenever we call poll(), it returns records written to Kafka that consumers in our group have not read yet. 
    This means that we have a way of tracking which records were read by a consumer of the group. As discussed before, 
    one of Kafka’s unique characteristics is that it does not track acknowledgments from consumers the way many JMS queues do. 
    Instead, it allows consumers to use Kafka to track their posi‐ tion (offset) in each partition.
    
-   How does a consumer commit an offset? It produces a message to Kafka, to a special __consumer_offsets topic, with the committed offset for each partition. 
    As long as all your consumers are up, running, and churning away, this will have no impact. How‐ ever, if a consumer crashes or a new consumer joins the consumer group, 
    this will trigger a rebalance. After a rebalance, each consumer may be assigned a new set of partitions than the one it processed before. In order to know where to pick up the work, 
    the consumer will read the latest committed offset of each partition and con‐ tinue from there.
    
-   If the committed offset is larger than the offset of the last message the client actually processed, all messages between the last processed offset and the committed offset will be missed by the consumer group. 

![Alt text](https://www.oreilly.com/library/view/kafka-the-definitive/9781491936153/assets/ktdg_04in06.png)
![Alt text](https://www.oreilly.com/library/view/kafka-the-definitive/9781491936153/assets/ktdg_04in07.png)

-   The easiest way to commit offsets is to allow the consumer to do it for you. If you configure enable.auto.commit=true, then every five seconds the consumer will commit the largest offset your client received from poll(). The five-second interval is the default

## Zookeeper: ## 

## MirrorMaker ##

-   MirrorMaker is simply a Kafka consumer and producer, linked together with a queue. Messages are consumed from one Kafka cluster and produced for another.

-   MirrorMaker, aggregating messages from two local clusters into an aggregate cluster, and then copying that cluster to other datacenters.

![Alt text](https://www.oreilly.com/library/view/kafka-the-definitive/9781491936153/assets/ktdg_0108.png)