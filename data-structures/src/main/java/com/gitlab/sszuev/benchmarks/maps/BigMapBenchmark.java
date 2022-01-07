package com.gitlab.sszuev.benchmarks.maps;

import com.gitlab.sszuev.maps.SimpleMap;
import org.apache.commons.lang3.RandomStringUtils;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Created by @ssz on 06.01.2022.
 */
@SuppressWarnings("unused")
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 5, time = 1)
@Fork(1)
@State(Scope.Group)
public class BigMapBenchmark {
    private static final Random RANDOM = new Random();

    private final static Object TEST_CONTENT_ITEM = new Object();
    private static final int TEST_ITERATION_NUM = 1000;
    private static final int TEST_ITEMS_LENGTH = 3_000_000;
    private static final int TEST_MIN_STRING_LENGTH = 42;
    private static final int TEST_MAX_STRING_LENGTH = 420;

    private static final String[] TEST_ITEMS_EXISTING =
            createRandomStrings(RANDOM, TEST_ITEMS_LENGTH, TEST_MIN_STRING_LENGTH, TEST_MAX_STRING_LENGTH);
    private static final String[] TEST_ITEMS_NON_EXISTING =
            createRandomStrings(RANDOM, TEST_ITEMS_LENGTH, TEST_MIN_STRING_LENGTH, TEST_MAX_STRING_LENGTH);

    @SuppressWarnings("unused")
    @Param
    private BigMapFactory factory;
    private SimpleMap<String, Object> map;

    @SuppressWarnings("SameParameterValue")
    private static String[] createRandomStrings(Random random, int arrayLength, int minStrLength, int maxStrLength) {
        return IntStream.range(0, arrayLength)
                .mapToObj(x -> createRandomString(random, minStrLength, maxStrLength))
                .toArray(String[]::new);
    }

    @SuppressWarnings("SameParameterValue")
    private static String createRandomString(Random random, int minLength, int maxLength) {
        int length = random.nextInt(maxLength - minLength) + minLength;
        return RandomStringUtils.random(length, 0, 0, true, false, null, random);
    }

    @Setup(Level.Invocation)
    public void doSetup() {
        map = factory.createStringMap(x -> TEST_CONTENT_ITEM, TEST_ITEMS_EXISTING);
    }

    @TearDown(Level.Invocation)
    public void doTearDown(Blackhole blackhole) {
        blackhole.consume(map);
    }

    @Benchmark
    @Group("GET")
    public void testGet(Blackhole blackhole) {
        testGet(blackhole, TEST_ITERATION_NUM, TEST_ITEMS_EXISTING);
        for (int i = 0; i < TEST_ITERATION_NUM; i++) {
            String key = nextRandomKey();
            blackhole.consume(map.get(key));
            blackhole.consume(key);
            blackhole.consume(i);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void testGet(Blackhole blackhole, int n, String[] data) {
        for (int i = 0; i < n; i++) {
            String key = data[RANDOM.nextInt(TEST_ITEMS_LENGTH)];
            blackhole.consume(map.get(key));
            blackhole.consume(key);
            blackhole.consume(i);
        }
    }

    private static String nextRandomKey() {
        int index = RANDOM.nextInt(TEST_ITEMS_LENGTH);
        return index % 2 == 0 ? TEST_ITEMS_EXISTING[index] : TEST_ITEMS_NON_EXISTING[index];
    }
}
