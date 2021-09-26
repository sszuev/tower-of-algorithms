package com.gitlab.sszuev.benchmarks.trees;

import com.gitlab.sszuev.trees.SimpleMap;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Created by @ssz on 26.09.2021.
 */
@SuppressWarnings("unused")
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 2, time = 1)
@Fork(1)
@State(Scope.Group)
public class SimpleMapBenchmark {
    private final static Object TEST_ITEM = new Object();

    private static final Random RANDOM = new Random();
    private static final int TEST_CONTENT_LENGTH_BIG = 10_000;
    private static final int TEST_CONTENT_LENGTH_SMALL = TEST_CONTENT_LENGTH_BIG / 10;
    private static final int[] TEST_CONTENT_RANDOM_BIG = IntStream.generate(RANDOM::nextInt)
            .limit(TEST_CONTENT_LENGTH_BIG).toArray();
    private static final int[] TEST_CONTENT_RANDOM_SMALL = select(TEST_CONTENT_RANDOM_BIG,
            TEST_CONTENT_LENGTH_SMALL, RANDOM).toArray();
    private static final int[] TEST_CONTENT_SEQUENTIAL_BIG = IntStream.range(0, TEST_CONTENT_LENGTH_BIG).toArray();
    private static final int[] TEST_CONTENT_SEQUENTIAL_SMALL = select(TEST_CONTENT_SEQUENTIAL_BIG,
            TEST_CONTENT_LENGTH_SMALL, RANDOM).sorted().toArray();

    @Param
    private SimpleMapFactory factory;
    private SimpleMap<Integer, Object> map;

    @SuppressWarnings("SameParameterValue")
    private static IntStream select(int[] source, int size, Random random) {
        Set<Integer> res = new HashSet<>();
        while (res.size() < size) {
            res.add(source[random.nextInt(source.length)]);
        }
        return res.stream().mapToInt(x -> x);
    }

    @Setup(Level.Invocation)
    public void doSetup() {
        map = factory.createEmptyMap();
    }

    @TearDown(Level.Invocation)
    public void doTearDown(Blackhole blackhole) {
        blackhole.consume(map);
    }

    @Benchmark
    @Group("ADD_SEQUENTIAL_10000_AND_GET")
    public void testAddSequentialAndGet(Blackhole blackhole) {
        testAdd(TEST_CONTENT_SEQUENTIAL_BIG);
        testGet(TEST_CONTENT_SEQUENTIAL_SMALL, blackhole);
    }

    @Benchmark
    @Group("ADD_RANDOM_10000_AND_GET")
    public void testAddRandomAndGet(Blackhole blackhole) {
        testAdd(TEST_CONTENT_RANDOM_BIG);
        testGet(TEST_CONTENT_RANDOM_SMALL, blackhole);
    }

    @Benchmark
    @Group("ADD_SEQUENTIAL_10000_AND_REMOVE")
    public void testAddSequentialAndRemove(Blackhole blackhole) {
        testAdd(TEST_CONTENT_SEQUENTIAL_BIG);
        testRemove(TEST_CONTENT_SEQUENTIAL_SMALL, blackhole);
    }

    @Benchmark
    @Group("ADD_RANDOM_10000_AND_REMOVE")
    public void testAddRandomAndRemove(Blackhole blackhole) {
        testAdd(TEST_CONTENT_RANDOM_BIG);
        testRemove(TEST_CONTENT_RANDOM_SMALL, blackhole);
    }

    private void testAdd(int[] keys) {
        for (int key : keys) {
            map.put(key, TEST_ITEM);
        }
    }

    private void testGet(int[] keys, Blackhole blackhole) {
        for (int key : keys) {
            blackhole.consume(map.get(key));
        }
    }

    private void testRemove(int[] keys, Blackhole blackhole) {
        for (int key : keys) {
            blackhole.consume(map.remove(key));
        }
    }
}
