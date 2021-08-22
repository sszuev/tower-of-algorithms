package com.gitlab.sszuev.benchmarks;

import com.gitlab.sszuev.arrays.DynamicArray;
import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Created by @ssz on 22.08.2021.
 */
@SuppressWarnings("unused")
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 2, time = 1)
@Fork(1)
@State(Scope.Group)
public class NonEmptyDynamicArrayBenchmark {
    private final static int TEST_ITEM = 42;
    private static final Random RANDOM = new Random();
    private static final int TEST_CONTENT_LENGTH = 1_000;
    private static final Object[] TEST_CONTENT = IntStream.generate(RANDOM::nextInt)
            .limit(TEST_CONTENT_LENGTH).boxed().toArray();

    @Param
    private DynamicArrayFactory factory;
    private DynamicArray<Object> array;

    @Setup(Level.Invocation)
    public void doSetup() {
        array = factory.createListOf(TEST_CONTENT);
    }

    @Benchmark
    @Group("ADD_999")
    public void testAdd10000() {
        for (int i = 0; i < 999; i++) {
            array.add(TEST_ITEM);
        }
    }

}
