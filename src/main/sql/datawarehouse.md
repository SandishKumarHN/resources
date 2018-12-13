## Schemas: ##
### Star Schema: ###

-   Modifying records is generally known as online transaction processing (OLTP). 
-   Data retrieval is referred to as online analytical processing (OLAP).


-   Normalization: Entity relation schemas are highly normalized. This means that data redundancy is eliminated by separating the data into multiple tables. 
    The process of normalization results in a complex schema with many tables and join paths.
    
-   When database records are retrieved, the most important requirements are query performance and schema simplicity. 
    These needs are best addressed by the dimensional model. Another name for the dimensional model is the star schema
    
![Alt text](http://gkmc.utah.edu/ebis_class/2003s/Oracle/DMB26/A73318/schemasa.gif)

-   A fact table usually contains numeric measurements, and is the only type of table with multiple joins to other tables. 
    Surrounding the fact table are dimension tables, which are related to the fact table by a single join. 
    Dimension tables contain data that describe the different characteristics, or dimensions, of a business. 
    Data warehouses and data marts are usually based on a star schema.
    
-   In a star schema, subjects are either facts or dimensions. You define and organize subjects according to how they are measured and whether or not they change over time. 
    Facts change regularly, and dimensions do not change, or change very slowly.
    
-   Separating facts and dimensions yields a subject-oriented design where data is stored according to logical relationships, not according to how the data was entered. 
    This structure is easier for both users and applications to understand and navigate.
    
Start Schema: A star schema is composed of one or more central fact tables, a set of dimension tables, and the joins that relate the dimension tables to the fact tables.

#### Fact Tables: ####

-   A fact table contains data columns for the numeric measurements of a business. It also includes a set of columns that form a concatenated, or composite key. 
    Each column of the concatenated key is a foreign key drawn from a dimensional table primary key. Fact tables usually have few columns and many rows, which result in relatively long and narrowly shaped tables.
    
-   From Diagram,  the measurements in the fact table are daily totals of sales in dollars, sales in units, and cost in dollars of each product sold. 
    The level of detail of a single record in a fact table is called the granularity of the fact table.
    
-   Each record in the fact table represents the total sales of a specific product in a retail store on one day. 
    Each new combination of product, store, or day generates a different record in the fact table.
    
#### Dimension Tables: ####

-   Dimension tables store descriptions of the characteristics of a business. A dimension is usually descriptive information that qualifies a fact. 
    For example, each record in a product dimension represents a specific product. In the star schema shown at the beginning of this chapter, the product, 
    customer, promotion, and time dimensions describe the measurements in the fact table. Dimensions do not change, or change slowly over time.
    
-   The shape of dimension tables is typically wide and short because they contain few records and many columns. 
    The columns of a dimension table are also called attributes of the dimension table. 
    Each dimension table in a star schema database has a single-part primary key joined to the fact table.
    
-   An important design characteristic of a star schema database is that you can quickly browse a single dimension table.
    This is possible because dimension tables are flat and denormalized. You can browse a single dimension table to determine the constraints and row headers to use when you query the fact table.
    
-   Most star schemas include a time dimension. A time dimension table makes it possible to analyze historic data without using complex SQL calculations. 
    For example, you can analyze your data by workdays as opposed to holidays, by weekdays as opposed to weekends, by fiscal periods, or by special events. 
    If the granularity of the fact table is daily sales, each record in the time dimension table represents a day.
    
#### Star Schema Key Structure: ####

-   The join constraints in a star schema define the relationships between a fact table and its dimension tables. the product key is the primary key in the product dimension table. 
    This means that each row in the product dimension table has a unique product key. The product key in the fact table is a foreign key drawn from the product dimension table.

-   Each row in a fact table must contain a primary key value from each dimension table. This rule is called referential integrity and is an important requirement in decision-support databases. 
    The reference from the foreign key to the primary key is the mechanism for verifying key values between the two tables. Referential integrity must be maintained to ensure valid query results.
    
-   The primary key of a fact table is a combination of its foreign keys. This is called a concatenated key. The join cardinality of dimension tables to fact tables is one-to-many, 
    because each record in a dimension table can describe many records in the fact table.
    
#### Designing a schema for a decision-support database: ####

-   Choose a business process to model in order to identify the fact tables.
    -   This is a major operational process in the organization that generates raw data. Users store and retrieve data in an existing system, such as a legacy system or company-wide data warehouse.
-   Choose the granularity of each fact table.
    -   This is the most detailed level of data to include in the fact table for the business process. The finer the granularity of each dimension, the more precisely a query can get through the database.
        The granularity of a fact table is usually the individual transaction, the individual line item, a daily snapshot, or a monthly snapshot.
-   Choose the dimensions for each fact table and their respective granularity.
    -   Examples of typical dimensions are time, product, customer, promotion, transaction type, and status. For each dimension, identify all the distinct attributes that describe the dimension. 
        The most useful attributes are textual and discrete. A dimension can include numeric attributes, such as package size, if the attribute acts more like a description than a measurement.
-   Choose the measured facts.
    -   These are the measurements that make up each fact table record. Typical measured facts are numeric additive quantities, such as sales in dollars or sales in units.

#### Star Schema Advantages ####
-   Query performance:
        Because a star schema database has a small number of tables and clear join paths, queries run faster than they do against an OLTP system. 
        Small single-table queries, usually of dimension tables, are almost instantaneous. Large join queries that involve multiple tables take only seconds or minutes to run.
-   Load performance and administration:
        Structural simplicity also reduces the time required to load large batches of data into a star schema database. By defining facts and dimensions and separating them into different tables, the impact of a load operation is reduced. 
        Dimension tables can be populated once and occasionally refreshed. You can add new facts regularly and selectively by appending records to a fact table.
-   Built-in referential integrity:
        each record in a dimension table has a unique primary key, and all keys in the fact tables are legitimate foreign keys drawn from the dimension tables. A record in the fact table that is not related correctly to a dimension cannot be given the correct key value to be retrieved.
-   Easily understood:
         A star schema is easy to understand and navigate, with dimensions joined only through the fact table. These joins are more significant to the end user, because they represent the fundamental relationship between parts of the underlying business. 
         Users can also browse dimension table attributes before constructing a query.