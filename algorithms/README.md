#### Algorithms

This is a collection of some algorithmic tasks and junit-based demo stand for running various algorithms.
Right now there are following algorithmic tasks:

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
  
- Greatest common divisor algorithms (command to run: `mvn -q test -Dtest=*GCDAlgorithmTest`):
  * [Recursive Binary GCD algorithm](src/main/java/com/gitlab/sszuev/tasks/algebraic/gcd/RecursiveBinaryGCDAlgorithm.java)

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
  * [Naive full-scan algorithm to search substring](src/main/java/com/gitlab/sszuev/tasks/strings/NaiveSubstringFindOneAlgorithm.java)
  * [Boyer–Moore–Horspool algorithm to search substring](src/main/java/com/gitlab/sszuev/tasks/strings/BMHSubstringFindOneAlgorithm.java)
  * [Boyer–Moore algorithm to search substring, wiki variant](src/main/java/com/gitlab/sszuev/tasks/strings/WikiBMSubstringFindOneAlgorithm.java)
  * [Boyer–Moore algorithm to search substring, ssz variant](src/main/java/com/gitlab/sszuev/tasks/strings/MyBMSubstringFindOneAlgorithm.java)
  * [Knuth-Morris-Pratt algorithm to search substring, naive prefix function](src/main/java/com/gitlab/sszuev/tasks/strings/SimpleKMKSubstringFindAllAlgorithm.java)
  * [Knuth-Morris-Pratt algorithm to search substring, classic prefix function](src/main/java/com/gitlab/sszuev/tasks/strings/FastKMKSubstringFindAllAlgorithm.java)

- Bitboard algorithms (command to run: `mvn -q test -Dtest=*WalkAlgorithmTest`):
  * [King walk problem](src/main/java/com/gitlab/sszuev/tasks/bitboard/KingWalkAlgorithm.java)
  * [Bishop walk problem](src/main/java/com/gitlab/sszuev/tasks/bitboard/BishopWalkAlgorithm.java)
  * [Rook walk problem](src/main/java/com/gitlab/sszuev/tasks/bitboard/RookWalkAlgorithm.java)
  * [Queen walk problem](src/main/java/com/gitlab/sszuev/tasks/bitboard/QueenWalkAlgorithm.java)
  * [Knight walk problem](src/main/java/com/gitlab/sszuev/tasks/bitboard/KnightWalkAlgorithm.java)
  
- Miscellaneous recursion and dynamic programming tasks
  * [Happy tickets problem](src/main/java/com/gitlab/sszuev/tasks/dynamic/tickets/HappyTicketsDynamicAlgorithm.java) (command to run: `mvn -q test -Dtest=HappyTicketsDynamicAlgorithmTest`)
  * [Sum of fractions](src/main/java/com/gitlab/sszuev/tasks/dynamic/fractions/SumOfFractionsDynamicAlgorithm.java) (command to run: `mvn -q test -Dtest=SumOfFractionsDynamicAlgorithmTest`)
  * [Pyramid problem: searching the maximum "garland" in the "digital christmas tree"](src/main/java/com/gitlab/sszuev/tasks/dynamic/pyramid/PyramidDynamicAlgorithm.java) (command to run: `mvn -q test -Dtest=PyramidDynamicAlgorithmTest`)
  * [Counting two-digit numbers with exclusion of three consecutive digits series ("5x8 problem")](src/main/java/com/gitlab/sszuev/tasks/dynamic/misc/CountTwoDigitNumbersDynamicAlgorithm.java) (`mvn -q test -Dtest=CountTwoDigitNumbersDynamicAlgorithmTest`)
  * [Matrix islands: find all 1-digit islands from square matrix consisting of 0 and 1](src/main/java/com/gitlab/sszuev/tasks/dynamic/matrix/MatrixIslandsDynamicAlgorithm.java) (`mvn -q test -Dtest=MatrixIslandsDynamicAlgorithmTest`)
  * Calculation the maximum possible shed area on a fixed rectangular area with some obstacles (command to run: `mvn -q test -Dtest=*ShedDynamicAlgorithmTest`)
    - [A brute-force solution with O(N^4) complexity](src/main/java/com/gitlab/sszuev/tasks/dynamic/shed/SmallShedDynamicAlgorithm.java)
    - [An optimized solution with O(N^3) complexity](src/main/java/com/gitlab/sszuev/tasks/dynamic/shed/LargeShedDynamicAlgorithm.java)
    - [An optimal solution with O(N^2) complexity](src/main/java/com/gitlab/sszuev/tasks/dynamic/shed/HugeShedDynamicAlgorithm.java)

##### Requirements:

- Git
- Java **11**
- Maven **3+**

##### Run all tests:

```bash
$ git clone git@gitlab.com:sszuev/2021-07-otus-algorithms-sszuev.git
$ cd 2021-07-otus-algorithms-sszuev/algorithms
$ mvn -q test
```

##### Run a particular test:

```bash
$ git clone git@gitlab.com:sszuev/2021-07-otus-algorithms-sszuev.git
$ cd 2021-07-otus-algorithms-sszuev/algorithms
$ mvn -Dtest=StringLengthCalculationAlgorithmTest -q test
```

Use option `-Dtest-data` to specify a real dir containing test data (it works only for a particular test):

```bash
mvn -q test -Dtest=RookWalkAlgorithmTest -Dtest-data=target\test-classes\bitboard\rook
``` 