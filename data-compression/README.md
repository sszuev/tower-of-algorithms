#### Data compression.

This project contains different data compression algorithms.

Right now there are following data compression algorithms:

- [Standard JDK's Zip compressor](/src/main/java/com/gitlab/sszuev/compression/impl/JDKZipCodecImpl.java)
- [Standard JDK's GZip compressor](/src/main/java/com/gitlab/sszuev/compression/impl/JDKGZipCodecImpl.java)
- [Apache Commons Zip compressor](/src/main/java/com/gitlab/sszuev/compression/impl/ApacheZipCodecImpl.java)
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

##### Usage:
```text
usage: -e|-d -s <source> -c {0|1|2|3|4} [[-o] -t <target>] [-b <buffer>]
 -b,--buffer <arg>   Buffer in bytes.
                     Optional: by default it is 8192
 -c,--codec <arg>    The codec, choose one of these: 0(STANDARD_ZIP), 1(COMMONS_ZIP), 2(STANDARD_GZIP), 3(NAIVE_RLE), 4(OPTIMISED_RLE)
 -d,--decode         Mandatory mode: perform decoding
 -e,--encode         Mandatory mode: perform encoding
 -h,--help           Display usage
 -o,--overwrite      Overwrites the target file if it is exist.
                     Optional: by default error in that case.
 -s,--source <arg>   The source file path (absolute or relative)
 -t,--target <arg>   The target file path (absolute or relative).
                     Optional: by default the filename is inferred from the source filename, the codec and direction mode
```