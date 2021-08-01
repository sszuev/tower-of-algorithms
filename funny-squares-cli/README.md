#### This is a hw-app 'Funny squares'

A simple console application for plotting a function graph using a given formula.

###### For [OtusTeam](https://otus.ru).

##### Requirements:

- Git
- Java **11**
- Maven **3+**

##### Build and run:

```
$ git clone git@gitlab.com:sszuev/2021-07-otus-algorithms-sszuev.git
$ cd 2021-07-otus-algorithms-sszuev/funny-squares-cli
$ mvn clean package
$ java -jar target/funny-squares-cli.jar -s 25 -f "Math.sin(y - 1) > Math.cos(x + 1)"
```