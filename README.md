# Algorithms and Data Structures

This project is a java-codebase containing various algorithms, 
data-structures and other things that are somehow related to the general topic. 
The project has educational and training purposes.

##### Chapters:

- [classic algorithms](algorithms) - a collection of well-known algorithms and several algorithmic puzzles made in form of junit5-based demonstration system
  <details>
    <summary>content</summary>

  - Algorithms for raising a real number to a natural power :
    * [Simple iterative algorithm](algorithms/src/main/java/com/gitlab/sszuev/tasks/algebraic/power/NaturalPowerSimpleIterativeAlgorithm.java)
    * [Iterative algorithm with optimization](algorithms/src/main/java/com/gitlab/sszuev/tasks/algebraic/power/NaturalPowerOptimizedIterativeAlgorithm.java)
    * [Fast algorithm](algorithms/src/main/java/com/gitlab/sszuev/tasks/algebraic/power/NaturalPowerFastAlgorithm.java)

  - Fibonacci number calculation:
    * [Simple recursive algorithm](algorithms/src/main/java/com/gitlab/sszuev/tasks/algebraic/fibonacci/FibonacciRecursiveAlgorithm.java)
    * [Simple iterative algorithm](algorithms/src/main/java/com/gitlab/sszuev/tasks/algebraic/fibonacci/FibonacciIterativeAlgorithm.java)
    * [Using golden ration](algorithms/src/main/java/com/gitlab/sszuev/tasks/algebraic/fibonacci/FibonacciGoldenRationAlgorithm.java)
    * [Matrix solution](algorithms/src/main/java/com/gitlab/sszuev/tasks/algebraic/fibonacci/FibonacciMatrixAlgorithm.java)

  - Primes number calculation:
    * [Iterative algorithm with some optimizations](algorithms/src/main/java/com/gitlab/sszuev/tasks/algebraic/primes/PrimesOptimizedIterativeAlgorithm.java)
    * [Sieve of Eratosthenes algorithm](algorithms/src/main/java/com/gitlab/sszuev/tasks/algebraic/primes/PrimesSieveOfEratosthenesAlgorithm.java)
    * [Sieve of Eratosthenes With Linear Complexity algorithm](algorithms/src/main/java/com/gitlab/sszuev/tasks/algebraic/primes/PrimesSieveOfEratosthenesLinearTimeAlgorithm.java)

  - Greatest common divisor algorithms:
    * [Recursive Binary GCD algorithm](algorithms/src/main/java/com/gitlab/sszuev/tasks/algebraic/gcd/RecursiveBinaryGCDAlgorithm.java)

  - Sorting algorithms:
    * [Bubble sort](algorithms/src/main/java/com/gitlab/sszuev/tasks/sorting/BubbleSortAlgorithm.java)
    * [Insertion sort](algorithms/src/main/java/com/gitlab/sszuev/tasks/sorting/InsertionSortAlgorithm.java)
    * [Selection sort](algorithms/src/main/java/com/gitlab/sszuev/tasks/sorting/SelectionSortAlgorithm.java)
    * [Shell sort](algorithms/src/main/java/com/gitlab/sszuev/tasks/sorting/ShellSortAlgorithm.java)
    * [Heap sort](algorithms/src/main/java/com/gitlab/sszuev/tasks/sorting/HeapSortAlgorithm.java)
    * [Iterative quick sort](algorithms/src/main/java/com/gitlab/sszuev/tasks/sorting/IterativeQuickSortAlgorithm.java)
    * [Plain (in-memory) merge sort](algorithms/src/main/java/com/gitlab/sszuev/tasks/sorting/MergeSortAlgorithm.java)
    * [Radix sort](algorithms/src/main/java/com/gitlab/sszuev/tasks/sorting/RadixSortAlgorithm.java)
    * [Bucket sort](algorithms/src/main/java/com/gitlab/sszuev/tasks/sorting/BucketSortAlgorithm.java)
    * [External (merge) sort](algorithms/src/main/java/com/gitlab/sszuev/tasks/sorting/ExternalSortAlgorithm.java)

  - String algorithms:
    * [Naive full-scan algorithm to search substring](algorithms/src/main/java/com/gitlab/sszuev/tasks/strings/NaiveSubstringFindOneAlgorithm.java)
    * [Boyer–Moore–Horspool algorithm to search substring](algorithms/src/main/java/com/gitlab/sszuev/tasks/strings/BMHSubstringFindOneAlgorithm.java)
    * [Boyer–Moore algorithm to search substring, wiki variant](algorithms/src/main/java/com/gitlab/sszuev/tasks/strings/WikiBMSubstringFindOneAlgorithm.java)
    * [Boyer–Moore algorithm to search substring, ssz variant](algorithms/src/main/java/com/gitlab/sszuev/tasks/strings/MyBMSubstringFindOneAlgorithm.java)
    * [Knuth-Morris-Pratt algorithm to search substring, naive prefix function](algorithms/src/main/java/com/gitlab/sszuev/tasks/strings/SimpleKMKSubstringFindAllAlgorithm.java)
    * [Knuth-Morris-Pratt algorithm to search substring, classic prefix function](algorithms/src/main/java/com/gitlab/sszuev/tasks/strings/FastKMKSubstringFindAllAlgorithm.java)

  - Bitboard algorithms:
    * [King walk problem](algorithms/src/main/java/com/gitlab/sszuev/tasks/bitboard/KingWalkAlgorithm.java)
    * [Bishop walk problem](algorithms/src/main/java/com/gitlab/sszuev/tasks/bitboard/BishopWalkAlgorithm.java)
    * [Rook walk problem](algorithms/src/main/java/com/gitlab/sszuev/tasks/bitboard/RookWalkAlgorithm.java)
    * [Queen walk problem](algorithms/src/main/java/com/gitlab/sszuev/tasks/bitboard/QueenWalkAlgorithm.java)
    * [Knight walk problem](algorithms/src/main/java/com/gitlab/sszuev/tasks/bitboard/KnightWalkAlgorithm.java)

  - Miscellaneous recursion and dynamic programming tasks
    * [Happy tickets problem](algorithms/src/main/java/com/gitlab/sszuev/tasks/dynamic/tickets/HappyTicketsDynamicAlgorithm.java)
    * [Sum of fractions](algorithms/src/main/java/com/gitlab/sszuev/tasks/dynamic/fractions/SumOfFractionsDynamicAlgorithm.java)
    * [Pyramid problem: searching the maximum "garland" in the "digital christmas tree"](algorithms/src/main/java/com/gitlab/sszuev/tasks/dynamic/pyramid/PyramidDynamicAlgorithm.java)
    * [Counting two-digit numbers with exclusion of three consecutive digits series ("5x8 problem")](algorithms/src/main/java/com/gitlab/sszuev/tasks/dynamic/misc/CountTwoDigitNumbersDynamicAlgorithm.java)
    * [Matrix islands: find all 1-digit islands from square matrix consisting of 0 and 1](algorithms/src/main/java/com/gitlab/sszuev/tasks/dynamic/matrix/MatrixIslandsDynamicAlgorithm.java)
    * Calculation the maximum possible shed area on a fixed rectangular area with some obstacles
      - [A brute-force solution with O(N^4) complexity](algorithms/src/main/java/com/gitlab/sszuev/tasks/dynamic/shed/SmallShedDynamicAlgorithm.java)
      - [An optimized solution with O(N^3) complexity](algorithms/src/main/java/com/gitlab/sszuev/tasks/dynamic/shed/LargeShedDynamicAlgorithm.java)
      - [An optimal solution with O(N^2) complexity](algorithms/src/main/java/com/gitlab/sszuev/tasks/dynamic/shed/HugeShedDynamicAlgorithm.java)

  </details>
  

- [data structures](data-structures) - a collection of different data-structures with JMH-based demonstration.
  <details>
    <summary>content</summary>

  - [Dynamic Arrays](data-structures/src/main/java/com/gitlab/sszuev/arrays/DynamicArray.java):
    * [A naive one-item-a-time-grown implementation](data-structures/src/main/java/com/gitlab/sszuev/arrays/SimpleDynamicArray.java)
    * [An implementation with fixed growth-factor (so called "vector")](data-structures/src/main/java/com/gitlab/sszuev/arrays/FixedVectorDynamicArray.java)
    * [An implementation with dynamic growth-factor](data-structures/src/main/java/com/gitlab/sszuev/arrays/FactorVectorDynamicArray.java)
    * [A matrix-based dynamic array](data-structures/src/main/java/com/gitlab/sszuev/arrays/MatrixDynamicArray.java)
    * [A wrapper for JDK `java.util.List` implementations](data-structures/src/main/java/com/gitlab/sszuev/arrays/JDKListDynamicArray.java)

  - [Priority Queues](data-structures/src/main/java/com/gitlab/sszuev/queues/PriorityQueue.java):
    * [A priority heap based implementation (wrapper for `java.util.PriorityQueue`)](data-structures/src/main/java/com/gitlab/sszuev/queues/HeapPriorityQueue.java)
    * [A `java.util.TreeSet`-based implementation](data-structures/src/main/java/com/gitlab/sszuev/queues/TreeSetPriorityQueue.java)
    * [A `java.util.Map`-based implementation](data-structures/src/main/java/com/gitlab/sszuev/queues/MapPriorityQueue.java)

  - [Maps (Key-Value Dictionaries)](data-structures/src/main/java/com/gitlab/sszuev/maps/SimpleMap.java):
    * Trees:
      - [Simple Binary Search Tree Map implementation without any rebalance](data-structures/src/main/java/com/gitlab/sszuev/maps/trees/BinarySearchTreeSimpleMap.java)
      - [AVL Binary Search Tree Map implementation](data-structures/src/main/java/com/gitlab/sszuev/maps/trees/AVLBinarySearchTreeSimpleMap.java)
      - [Treap - a Cartesian Binary Search Tree Map implementation](data-structures/src/main/java/com/gitlab/sszuev/maps/trees/TreapSimpleMap.java)
      - [B-Tree - a self-balancing multi-node Search Tree Map implementation](data-structures/src/main/java/com/gitlab/sszuev/maps/trees/BTreeSimpleMap.java)
    * Hash-tables:
      - [A hashtable separate-chaining implementation](data-structures/src/main/java/com/gitlab/sszuev/maps/hashtables/SeparateChainingHashtableSimpleMap.java)
      - [A hashtable open-addressing implementation](data-structures/src/main/java/com/gitlab/sszuev/maps/hashtables/OpenAddressingHashtableSimpleMap.java)
    * Wrappers to compare:
      - [Simple wrapper for JDK `java.util.Map` implementations](data-structures/src/main/java/com/gitlab/sszuev/maps/JDKMapWrapperSimpleMap.java)
      - [A wrapper for `java.util.Map` with Bloom-Filter optimization](data-structures/src/main/java/com/gitlab/sszuev/maps/BigSimpleMap.java)

  - Misc data-structures: [Bloom Filter](data-structures/src/main/java/com/gitlab/sszuev/misc/SimpleBloomFilter.java):
    * [Simplest Bloom Filter impl](data-structures/src/main/java/com/gitlab/sszuev/misc/MyBloomFilter.java)
    * [Guava-based wrapper](data-structures/src/main/java/com/gitlab/sszuev/misc/GuavaBloomFilter.java)

  - [Graph algorithms](data-structures/src/main/java/com/gitlab/sszuev/graphs/Graph.java)
    * Demucron's algorithm (topological sorting)
    * Kosaraju's algorithm (strongly connected component searching)
    * Borůvka's algorithm (optimized Kruskal's algorithm, a searching of minimum spanning tree)
    * Dijkstra's algorithm (an algorithm for finding the shortest paths in weighted graph) 

  </details>


- [data compression algorithms](data-compression) - a simple command-line utility to encode/decode files using different approaches.
  <details>
    <summary>content</summary>

  - [Standard JDK's Zip compressor](data-compression/src/main/java/com/gitlab/sszuev/compression/impl/JDKZipCodecImpl.java)
  - [Standard JDK's GZip compressor](data-compression/src/main/java/com/gitlab/sszuev/compression/impl/JDKGZipCodecImpl.java)
  - [Apache Commons Zip compressor](data-compression/src/main/java/com/gitlab/sszuev/compression/impl/ApacheZipCodecImpl.java)
  - [Apache Commons SevenZip compressor](data-compression/src/main/java/com/gitlab/sszuev/compression/impl/ApacheSevenZipCodecImpl.java)
  - [Naive RLE compressor](data-compression/src/main/java/com/gitlab/sszuev/compression/impl/SimpleRLECodecImpl.java)
  - [Optimized RLE compressor](data-compression/src/main/java/com/gitlab/sszuev/compression/impl/EnhancedRLECodecImpl.java)
  
  </details>
 
  
- [algorithmic puzzles](https://gitlab.com/sszuev/algorithms) - another attempt to systematize puzzles for coding interviews


##### Requirements:

- Git
- Java **11+**
- Maven **3+**