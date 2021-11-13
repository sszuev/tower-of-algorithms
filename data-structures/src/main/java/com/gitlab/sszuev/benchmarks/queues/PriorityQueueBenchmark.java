package com.gitlab.sszuev.benchmarks.queues;

import com.gitlab.sszuev.queues.PriorityQueue;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

/**
 * Created by @ssz on 28.08.2021.
 */
@SuppressWarnings("unused")
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@Warmup(iterations = 1, time = 1)
@Measurement(iterations = 2, time = 1)
@Fork(1)
@State(Scope.Group)
public class PriorityQueueBenchmark {

    private final static Object TEST_ITEM = new Object();
    private static final Random RANDOM = new Random();

    private static final int[] TEST_PRIORITIES_200_000_WITH_MAX_100 = generateTestData(200_000, 100);
    private static final int[] TEST_PRIORITIES_100_000_WITH_MAX_INT = generateTestData(100_000, -1);

    @Param
    private PriorityQueueFactory factory;
    private PriorityQueue<Object> queue;

    private static int[] generateTestData(int size, int maxPriority) {
        return IntStream.generate(() -> maxPriority > 0 ? RANDOM.nextInt(maxPriority) : RANDOM.nextInt())
                .limit(size).toArray();
    }

    @Setup(Level.Invocation)
    public void doSetup() {
        queue = factory.createPriorityQueue();
    }

    @TearDown(Level.Invocation)
    public void doTearDown(Blackhole blackhole) {
        blackhole.consume(queue);
    }

    @Benchmark
    @Group("ADD_REMOVE_100_000_WITHOUT_MAX")
    public void testAddRemove100000Unlimited(Blackhole blackhole) {
        testAddRemove(TEST_PRIORITIES_100_000_WITH_MAX_INT, blackhole);
    }

    @Benchmark
    @Group("ADD_REMOVE_200_000_WITH_MAX100")
    public void testAddRemove200000WithMax100(Blackhole blackhole) {
        testAddRemove(TEST_PRIORITIES_200_000_WITH_MAX_100, blackhole);
    }

    private void testAddRemove(int[] priorities, Blackhole blackhole) {
        for (int p : priorities) {
            queue.enqueue(TEST_ITEM, p);
        }
        for (int i = 0; i < priorities.length; i++) {
            blackhole.consume(queue.dequeue());
        }
    }

}
