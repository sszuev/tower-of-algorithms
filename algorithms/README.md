#### Algorithms

This is a collection of some algorithmic tasks and junit-based demo stand for running various algorithms.
Right now there are following algorithmic tasks:

- [Happy tickets problem](src/main/java/com/gitlab/sszuev/tasks/tickets/HappyTicketsDynamicAlgorithm.java) (command to
  run: `mvn -q test -Dtest=HappyTicketsDynamicAlgorithmTest`)
- Bitboard algorithms (command to run: `mvn -q test -Dtest=*WalkAlgorithmTest`):
  * [King walk problem](src/main/java/com/gitlab/sszuev/tasks/bitboard/KingWalkAlgorithm.java)
  * [Bishop walk problem](src/main/java/com/gitlab/sszuev/tasks/bitboard/BishopWalkAlgorithm.java)
  * [Rook walk problem](src/main/java/com/gitlab/sszuev/tasks/bitboard/RookWalkAlgorithm.java)
  * [Queen walk problem](src/main/java/com/gitlab/sszuev/tasks/bitboard/QueenWalkAlgorithm.java)
  * [Knight walk problem](src/main/java/com/gitlab/sszuev/tasks/bitboard/KnightWalkAlgorithm.java)

- Algorithms for raising a real number to a natural power (command to
  run `mvn -q test -Dtest=NaturalPower*AlgorithmTest`):
  * [Simple iterative algorithm](src/main/java/com/gitlab/sszuev/tasks/algebraic/power/NaturalPowerSimpleIterativeAlgorithm.java)
  * [Iterative algorithm with optimization](src/main/java/com/gitlab/sszuev/tasks/algebraic/power/NaturalPowerOptimizedIterativeAlgorithm.java)
  * [Fast algorithm](src/main/java/com/gitlab/sszuev/tasks/algebraic/power/NaturalPowerFastAlgorithm.java)

- Fibonacci number calculation (command to run `mvn -q test -Dtest=Fibonacci*AlgorithmTest`):
  * [Simple recursive algorithm](src/main/java/com/gitlab/sszuev/tasks/algebraic/fibonacci/FibonacciRecursiveAlgorithm.java)
  * [Simple iterative algorithm](src/main/java/com/gitlab/sszuev/tasks/algebraic/fibonacci/FibonacciIterativeAlgorithm.java)
  * [Using golden ration](src/main/java/com/gitlab/sszuev/tasks/algebraic/fibonacci/FibonacciGoldenRationAlgorithm.java)
  * [Matrix solution](src/main/java/com/gitlab/sszuev/tasks/algebraic/fibonacci/FibonacciMatrixAlgorithm.java)

- Primes number calculation (command to run: `mvn -q test -Dtest=Primes*AlgorithmTest`):
  * [Iterative algorithm with some optimizations](src/main/java/com/gitlab/sszuev/tasks/algebraic/primes/PrimesOptimizedIterativeAlgorithm.java)
  * [Sieve of Eratosthenes algorithm](src/main/java/com/gitlab/sszuev/tasks/algebraic/primes/PrimesSieveOfEratosthenesAlgorithm.java)
  * [Sieve of Eratosthenes With Linear Complexity algorithm](src/main/java/com/gitlab/sszuev/tasks/algebraic/primes/PrimesSieveOfEratosthenesLinearTimeAlgorithm.java) 

- Sorting algorithms (command to run: `mvn -q test -Dtest=*SortlgorithmTest`):
  * [Bubble sort](src/main/java/com/gitlab/sszuev/tasks/sorting/BubbleSortAlgorithm.java)
  * [Insertion sort](src/main/java/com/gitlab/sszuev/tasks/sorting/InsertionSortAlgorithm.java)
  * [Selection sort](src/main/java/com/gitlab/sszuev/tasks/sorting/SelectionSortAlgorithm.java)
  * [Shell sort](src/main/java/com/gitlab/sszuev/tasks/sorting/ShellSortAlgorithm.java)
  * [Heap sort](src/main/java/com/gitlab/sszuev/tasks/sorting/HeapSortAlgorithm.java)
  * [Iterative quick sort](src/main/java/com/gitlab/sszuev/tasks/sorting/IterativeQuickSortAlgorithm.java)
  * [Plain (in-memory) merge sort](src/main/java/com/gitlab/sszuev/tasks/sorting/MergeSortAlgorithm.java)
  * [Radix sort](src/main/java/com/gitlab/sszuev/tasks/sorting/RadixSortAlgorithm.java)
  * [Bucket sort](src/main/java/com/gitlab/sszuev/tasks/sorting/BucketSortAlgorithm.java)
  * [External (merge) sort](src/main/java/com/gitlab/sszuev/tasks/sorting/ExternalSortAlgorithm.java)

- String algorithms (command to run: `mvn -q test -Dtest=strings/*AlgorithmTest`):
  * [Boyer–Moore–Horspool algorithm to search substring](src/main/java/com/gitlab/sszuev/tasks/strings/BMHSubstringSearchAlgorithm.java)
  * [Boyer–Moore algorithm to search substring, wiki variant](src/main/java/com/gitlab/sszuev/tasks/strings/WikiBMSubstringSearchAlgorithm.java)
  * [Boyer–Moore algorithm to search substring, ssz variant](src/main/java/com/gitlab/sszuev/tasks/strings/MyBMSubstringSearchAlgorithm.java)

##### Requirements:

- Git
- Java **11**
- Maven **3+**

##### Run all tests:

```
$ git clone git@gitlab.com:sszuev/2021-07-otus-algorithms-sszuev.git
$ cd 2021-07-otus-algorithms-sszuev/algorithms
$ mvn -q test
```

##### Run a particular test:

```
$ git clone git@gitlab.com:sszuev/2021-07-otus-algorithms-sszuev.git
$ cd 2021-07-otus-algorithms-sszuev/algorithms
$ mvn -Dtest=StringLengthCalculationAlgorithmTest -q test
```

Use option `-Dtest-data` to specify a real dir containing test data (it works only for a particular test):

```
mvn -q test -Dtest=RookWalkAlgorithmTest -Dtest-data=target\test-classes\bitboard\rook
``` 