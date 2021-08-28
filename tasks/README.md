#### Algorithms

This is a collection of some algorithmic tasks and junit-based demo stand for running various algorithms.
Right now there are following algorithmic tasks:

- [Happy tickets problem](src/main/java/com/gitlab/sszuev/tasks/tickets/HappyTicketsDynamicAlgorithm.java) (
  dynamic-programming approach)
- Bitboard algorithms (command to run: `mvn -q test -Dtest=*WalkAlgorithmTest`):
  * [King walk problem](src/main/java/com/gitlab/sszuev/tasks/bitboard/KingWalkAlgorithm.java)
  * [Bishop walk problem](src/main/java/com/gitlab/sszuev/tasks/bitboard/BishopWalkAlgorithm.java)
  * [Rook walk problem](src/main/java/com/gitlab/sszuev/tasks/bitboard/RookWalkAlgorithm.java)
  * [Queen walk problem](src/main/java/com/gitlab/sszuev/tasks/bitboard/QueenWalkAlgorithm.java)
  * [Knight walk problem](src/main/java/com/gitlab/sszuev/tasks/bitboard/KnightWalkAlgorithm.java)

##### Requirements:

- Git
- Java **11**
- Maven **3+**

##### Run all tests:

```
$ git clone git@gitlab.com:sszuev/2021-07-otus-algorithms-sszuev.git
$ cd 2021-07-otus-algorithms-sszuev/tasks
$ mvn -q test
```

##### Run a particular test:

```
$ git clone git@gitlab.com:sszuev/2021-07-otus-algorithms-sszuev.git
$ cd 2021-07-otus-algorithms-sszuev/tasks
$ mvn -Dtest=StringLengthCalculationAlgorithmTest -q test
```

Use option `-Dtest-data` to specify a real dir containing test data (it works only for a particular test):

```
mvn -q test -Dtest=RookWalkAlgorithmTest -Dtest-data=target\test-classes\bitboard\rook
``` 