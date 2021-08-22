#### A project-collection of data structures and demo-benchmarks.

This project contains different data structures (such
as [DynamicArray](src/main/java/com/gitlab/sszuev/arrays/DynamicArray.java))
and simple console [JMH](https://openjdk.java.net/projects/code-tools/jmh) based application for demonstration.

###### For [OtusTeam](https://otus.ru).

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