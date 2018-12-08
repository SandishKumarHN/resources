## Scheduling: ##

## Scheduler: ##
### FAIR: ### 
### FIFO: ### 
### Capacity Scheduler: ### 
### Dynamic Allocation: ### 
### Pools: ### 

    
## Yarn: ##
![Alt text](https://2xbbhjxc6wk3v21p62t8n4d4-wpengine.netdna-ssl.com/wp-content/uploads/2012/08/yarnflow1.png)

## Resource Manager: ##
    ![Alt text](https://2xbbhjxc6wk3v21p62t8n4d4-wpengine.netdna-ssl.com/wp-content/uploads/2012/08/resource_manager.png)
    
    -   YARN Resource Manager Service (RM) is the central controlling authority for resource management and 
        makes allocation decisions ResourceManager has two main components: Scheduler and ApplicationsManager. 
        The Scheduler API is specifically designed to negotiate resources and not schedule tasks
    -   Scheduler: The scheduler is responsible for allocating the resources to the running application. 
        The scheduler is pure scheduler it means that it performs no monitoring no tracking for the application and 
        even doesn’t guarantees about restarting failed tasks either due to application failure or hardware failures.
        -   The Scheduler is responsible for allocating resources to the various running applications subject to familiar constraints of capacities, queues etc. 
            The Scheduler is pure scheduler in the sense that it performs no monitoring or tracking of status for the application. 
            Also, it offers no guarantees about restarting failed tasks either due to application failure or hardware failures. 
            The Scheduler performs its scheduling function based on the resource requirements of the applications; 
            it does so based on the abstract notion of a resource Container which incorporates elements such as memory, cpu, disk, network etc.
        -   The Scheduler has a pluggable policy which is responsible for partitioning the cluster resources among the various queues, 
            applications etc. The current schedulers such as the CapacityScheduler and the FairScheduler would be some examples of plug-ins.
    -   Application Manager : It manages running Application Masters in the cluster, i.e., 
        it is responsible for starting application masters and for monitoring and restarting them on different nodes in case of failures
 
## Node Manager: ##
    ![Alt text](https://2xbbhjxc6wk3v21p62t8n4d4-wpengine.netdna-ssl.com/wp-content/uploads/2012/09/Node-Manager-Diagram.png)
    
    -   It is the slave daemon of Yarn. NM is responsible for containers monitoring their resource usage and 
        reporting the same to the ResourceManager. Manage the user process on that machine. 
        Yarn NodeManager also tracks the health of the node on which it is running. 
        The design also allows plugging long-running auxiliary services to the NM; these are application-specific 
        services, specified as part of the configurations and loaded by the NM during startup.
        A shuffle is a typical auxiliary service by the NMs for MapReduce applications on YARN
        
## Application Master: ##

    -   One application master runs per application. It negotiates resources from the resource manager and 
        works with the node manager. It Manages the application life cycle.
    -   The AM acquires containers from the RM’s Scheduler before contacting the corresponding NMs to start the application’s individual tasks.
         
## Resource Planning for Spark Application: ##
![Alt text](https://cdn-images-1.medium.com/max/2000/1*kKitFswq56j1CTgMQ0gumQ.png)

    -   application performance is heavily dependent on resources such as executors, cores, and memory allocated. 
        The resources for the application depends on the application characteristics such as storage and computation.
    -   The most granular (smallest sized executors) level of resource allocation reduces application performance due to the inability 
        to use the power of running multiple tasks inside single executor. To perform computation faster, 
        multiple tasks within the executor share the cached data.
    -   The least granular (biggest executors) level of resource allocation influences application performance due to the 
        overuse of resources and not considering memory overhead of OS and other daemons.
    -   The balanced resources (executors, cores, and memory) with memory overhead improve the performance of the Spark application,
        especially when running Spark application on YARN
