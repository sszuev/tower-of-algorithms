#### Data structures.

This project contains different data structure implementations and simple
console [JMH](https://openjdk.java.net/projects/code-tools/jmh) based application for demonstration.

Right now there are following data structures:

- [Dynamic Array](src/main/java/com/gitlab/sszuev/arrays/DynamicArray.java) (command: `java -jar target/data-structures.jar com.gitlab.sszuev.benchmarks.arrays.*`):
  * [A naive one-item-a-time-grown implementation](src/main/java/com/gitlab/sszuev/arrays/SimpleDynamicArray.java)
  * [An implementation with fixed growth-factor (so called "vector")](src/main/java/com/gitlab/sszuev/arrays/FixedVectorDynamicArray.java)
  * [An implementation with dynamic growth-factor](src/main/java/com/gitlab/sszuev/arrays/FactorVectorDynamicArray.java)
  * [A matrix-based dynamic array](src/main/java/com/gitlab/sszuev/arrays/MatrixDynamicArray.java)
  * [A wrapper for JDK `java.util.List` implementations](src/main/java/com/gitlab/sszuev/arrays/JDKListDynamicArray.java)

- [Priority Queue](src/main/java/com/gitlab/sszuev/queues/PriorityQueue.java) (command: `java -jar target/data-structures.jar com.gitlab.sszuev.benchmarks.queues.*`):
  * [A priority heap based impplementation (wrapper for `java.util.PriorityQueue`)](src/main/java/com/gitlab/sszuev/queues/HeapPriorityQueue.java)
  * [A `java.util.TreeSet`-based implementation](src/main/java/com/gitlab/sszuev/queues/TreeSetPriorityQueue.java)
  * [A `java.util.Map`-based implementation](src/main/java/com/gitlab/sszuev/queues/MapPriorityQueue.java)
  
- [Map (Key-Value Dictionary)](src/main/java/com/gitlab/sszuev/maps/SimpleMap.java) (command: `java -jar target/data-structures.jar com.gitlab.sszuev.benchmarks.maps.*`):
  * [Simple Binary Search Tree Map implementation without any rebalance](src/main/java/com/gitlab/sszuev/maps/BinarySearchTreeSimpleMap.java)
  * [AVL Binary Search Tree Map implementation](src/main/java/com/gitlab/sszuev/maps/AVLBinarySearchTreeSimpleMap.java)
  * [A hashtable separate-chaining implementation](src/main/java/com/gitlab/sszuev/maps/SeparateChainingHashtableSimpleMap.java)

##### Requirements:

- Git
- Java **11**
- Maven **3+**

##### Build and run:

```
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