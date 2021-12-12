#### Data compression.

This project contains different data compression algorithms.

Right now there are following data compression algorithms:

- [Standard JDK's Zip compressor](/src/main/java/com/gitlab/sszuev/compression/impl/JDKZipCodecImpl.java)
- [Simples RLE compressor](/src/main/java/com/gitlab/sszuev/compression/impl/SimpleRLECodecImpl.java)

##### Requirements:

- Git
- Java **11**
- Maven **3+**

##### Build and run:

```
$ git clone git@gitlab.com:sszuev/2021-07-otus-algorithms-sszuev.git
$ cd 2021-07-otus-algorithms-sszuev/compression
$ mvn clean package
$ java -jar target/compression.jar
