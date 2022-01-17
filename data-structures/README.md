#### Data structures.

This project contains different data structure implementations and simple
console [JMH](https://openjdk.java.net/projects/code-tools/jmh) based application for demonstration.
_Note: some benchmarks may take a long time_

Right now there are following data structures:

- [Dynamic Arrays](src/main/java/com/gitlab/sszuev/arrays/DynamicArray.java) (command: `java -jar target/data-structures.jar com.gitlab.sszuev.benchmarks.arrays.*`):
  * [A naive one-item-a-time-grown implementation](src/main/java/com/gitlab/sszuev/arrays/SimpleDynamicArray.java)
  * [An implementation with fixed growth-factor (so called "vector")](src/main/java/com/gitlab/sszuev/arrays/FixedVectorDynamicArray.java)
  * [An implementation with dynamic growth-factor](src/main/java/com/gitlab/sszuev/arrays/FactorVectorDynamicArray.java)
  * [A matrix-based dynamic array](src/main/java/com/gitlab/sszuev/arrays/MatrixDynamicArray.java)
  * [A wrapper for JDK `java.util.List` implementations](src/main/java/com/gitlab/sszuev/arrays/JDKListDynamicArray.java)

- [Priority Queues](src/main/java/com/gitlab/sszuev/queues/PriorityQueue.java) (benchmark command: `java -jar target/data-structures.jar com.gitlab.sszuev.benchmarks.queues.*`):
  * [A priority heap based implementation (wrapper for `java.util.PriorityQueue`)](src/main/java/com/gitlab/sszuev/queues/HeapPriorityQueue.java)
  * [A `java.util.TreeSet`-based implementation](src/main/java/com/gitlab/sszuev/queues/TreeSetPriorityQueue.java)
  * [A `java.util.Map`-based implementation](src/main/java/com/gitlab/sszuev/queues/MapPriorityQueue.java)
  
- [Maps (Key-Value Dictionaries)](src/main/java/com/gitlab/sszuev/maps/SimpleMap.java) (benchmark command: `java -jar target/data-structures.jar com.gitlab.sszuev.benchmarks.maps.SimpleMapBenchmark`):
  * Trees:
    - [Simple Binary Search Tree Map implementation without any rebalance](src/main/java/com/gitlab/sszuev/maps/trees/BinarySearchTreeSimpleMap.java)
    - [AVL Binary Search Tree Map implementation](src/main/java/com/gitlab/sszuev/maps/trees/AVLBinarySearchTreeSimpleMap.java)
    - [Treap - a Cartesian Binary Search Tree Map implementation](src/main/java/com/gitlab/sszuev/maps/trees/TreapSimpleMap.java)
    - [B-Tree - a self-balancing multi-node Search Tree Map implementation](src/main/java/com/gitlab/sszuev/maps/trees/BTreeSimpleMap.java)
  * Hash-tables:
    - [A hashtable separate-chaining implementation](src/main/java/com/gitlab/sszuev/maps/hashtables/SeparateChainingHashtableSimpleMap.java)
    - [A hashtable open-addressing implementation](src/main/java/com/gitlab/sszuev/maps/hashtables/OpenAddressingHashtableSimpleMap.java)
  * Wrappers to compare: 
    - [Simple wrapper for JDK `java.util.Map` implementations](src/main/java/com/gitlab/sszuev/maps/JDKMapWrapperSimpleMap.java)
    - [A wrapper for `java.util.Map` with Bloom-Filter optimization](src/main/java/com/gitlab/sszuev/maps/BigSimpleMap.java)
    
- Misc data-structures: [Bloom Filter](src/main/java/com/gitlab/sszuev/misc/SimpleBloomFilter.java) (benchmark command: `java -jar target/data-structures.jar com.gitlab.sszuev.benchmarks.maps.BigMapBenchmark` ~ 10m): 
  * [Simplest Bloom Filter impl](src/main/java/com/gitlab/sszuev/misc/MyBloomFilter.java)
  * [Guava-based wrapper](src/main/java/com/gitlab/sszuev/misc/GuavaBloomFilter.java)

- [Graph algorithms](src/main/java/com/gitlab/sszuev/graphs/Graph.java) 
  * [Demucron's algorithm (topological sorting)](src/main/java/com/gitlab/sszuev/graphs/Graphs.java#L66)
  * [Kosaraju's algorithm (strongly connected component searching)](src/main/java/com/gitlab/sszuev/graphs/Graphs.java#L124)
  * [Borůvka's algorithm (optimized Kruskal's algorithm, a searching of minimum spanning tree)](src/main/java/com/gitlab/sszuev/graphs/Graphs.java#L183)
  * [Dijkstra's algorithm (an algorithm for finding the shortest paths in weighted graph)](src/main/java/com/gitlab/sszuev/graphs/Graphs.java#L234)

##### Requirements:

- Git
- Java **11**
- Maven **3+**

##### Build and run:

```bash
$ git clone git@gitlab.com:sszuev/2021-07-otus-algorithms-sszuev.git
$ cd 2021-07-otus-algorithms-sszuev/data-structures
$ mvn clean package
$ java -jar target/data-structures.jar
```

##### Additional notes:

- Use `-h` for help
- To see all benchmarks use `-l`
- Example of running particular benchmark: `$ java -jar target/data-structures.jar com.gitlab.sszuev.benchmarks.arrays.NonEmptyDynamicArrayBenchmark.REMOVE_9999`
- Example of running all benchmarks for queues: `$ java -jar target/data-structures.jar com.gitlab.sszuev.benchmarks.queues.*`