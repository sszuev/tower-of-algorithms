#### Algorithms

This is a collection of some algorithmic tasks and junit-based demo stand for running various algorithms.
Right now there are following algorithmic tasks:

- [Calculate String Length](src/main/java/com/gitlab/sszuev/tasks/strings/StringLengthCalculationAlgorithm.java) (a
  dummy algorithm for demonstration)
- [Happy tickets problem](src/main/java/com/gitlab/sszuev/tasks/tickets/HappyTicketsDynamicAlgorithm.java) (
  dynamic-programming approach)

###### For [OtusTeam](https://otus.ru).

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

##### Run particular test:

```
$ git clone git@gitlab.com:sszuev/2021-07-otus-algorithms-sszuev.git
$ cd 2021-07-otus-algorithms-sszuev/tasks
$ mvn -Dtest=StringLengthCalculationAlgorithmTest -q test
```
