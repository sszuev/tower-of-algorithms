#### Data structures.

This project contains different data structure implementations and simple
console [JMH](https://openjdk.java.net/projects/code-tools/jmh) based application for demonstration.

Right now there are following data structures:

- [Dynamic Array](src/main/java/com/gitlab/sszuev/arrays/DynamicArray.java):
    * [A naive one-item-a-time-grown implementation](src/main/java/com/gitlab/sszuev/arrays/SimpleDynamicArray.java)
    * [An implementation with fixed growth-factor (so called "vector")](src/main/java/com/gitlab/sszuev/arrays/FixedVectorDynamicArray.java)
    * [An implementation with dynamic growth-factor](src/main/java/com/gitlab/sszuev/arrays/FactorVectorDynamicArray.java)
    * [A matrix-based dynamic array](src/main/java/com/gitlab/sszuev/arrays/MatrixDynamicArray.java)
    * [A wrapper for JDK `java.util.List` implementations](src/main/java/com/gitlab/sszuev/arrays/JDKListDynamicArray.java)

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

- For help use `-h`
- To see all benchmarks use `-l`
- Example of run particular
  benchmark: `$ java -jar target/data-structures.jar -h com.gitlab.sszuev.benchmarks.arrays.NonEmptyDynamicArrayBenchmark.REMOVE_9999`