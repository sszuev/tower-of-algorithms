package com.gitlab.sszuev.benchmarks.arrays;

import com.gitlab.sszuev.arrays.DynamicArray;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

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
public class EmptyDynamicArrayBenchmark {

    private final static Object TEST_ITEM = new Object();

    @Param
    private DynamicArrayFactory factory;
    private DynamicArray<Object> array;

    @Setup(Level.Invocation)
    public void doSetup() {
        array = factory.createEmptyList();
    }

    @TearDown(Level.Invocation)
    public void doTearDown(Blackhole blackhole) {
        blackhole.consume(array);
    }

    @Benchmark
    @Group("ADD_10_000")
    public void testAdd10000() {
        for (int i = 0; i < 10_000; i++) {
            array.add(TEST_ITEM);
        }
    }

    @Benchmark
    @Group("ADD_100_000")
    public void testAdd100000() {
        for (int i = 0; i < 100_000; i++) {
            array.add(TEST_ITEM);
        }
    }

}
