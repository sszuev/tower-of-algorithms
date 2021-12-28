#### Data compression.

This project contains different data compression algorithms.

Right now there are following data compression algorithms:

- [Standard JDK's Zip compressor](/src/main/java/com/gitlab/sszuev/compression/impl/JDKZipCodecImpl.java)
- [Standard JDK's GZip compressor](/src/main/java/com/gitlab/sszuev/compression/impl/JDKGZipCodecImpl.java)
- [Naive RLE compressor](/src/main/java/com/gitlab/sszuev/compression/impl/SimpleRLECodecImpl.java)
- [Optimized RLE compressor](/src/main/java/com/gitlab/sszuev/compression/impl/EnhancedRLECodecImpl.java)

##### Requirements:

- Git
- Java **11**
- Maven **3+**

##### Build and run:

```bash
$ git clone git@gitlab.com:sszuev/2021-07-otus-algorithms-sszuev.git
$ cd 2021-07-otus-algorithms-sszuev/data-compression
$ mvn clean package
$ java -jar target/data-compression.jar
```

##### To run demonstration tests use command `mvn -q test -Dtest=FileCodecTest`