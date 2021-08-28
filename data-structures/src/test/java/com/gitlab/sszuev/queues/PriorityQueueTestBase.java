package com.gitlab.sszuev.queues;

import org.junit.jupiter.api.Assertions;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Created by @ssz on 28.08.2021.
 */
abstract class PriorityQueueTestBase {

    @SuppressWarnings("unchecked")
    final <X> void doTestFillAndEmptyWithDefaultPriority(Supplier<PriorityQueue<X>> factory, X... data) {
        List<X> given = Arrays.stream(data).collect(Collectors.toList());
        PriorityQueue<X> queue = factory.get();
        given.forEach(queue::enqueue);
        while (!given.isEmpty()) {
            Assertions.assertEquals(given.remove(0), queue.dequeue());
        }
    }

    @SafeVarargs
    final <X> void doTestFillAndEmptyWithDifferentPriorities(Supplier<PriorityQueue<X>> factory, ObjIntWrapper<X>... data) {
        PriorityQueue<X> queue = factory.get();
        for (ObjIntWrapper<X> d : data) {
            queue.enqueue(d.value, d.priority);
        }

        NavigableMap<Integer, List<X>> given = ObjIntWrapper.toTree(data);
        while (!given.isEmpty()) {
            List<X> expected = given.remove(given.lastEntry().getKey());
            while (!expected.isEmpty()) {
                X e = expected.remove(0);
                X a = queue.dequeue();
                Assertions.assertEquals(e, a);
            }
        }
    }

    void doTestAddRemoveWithPriorityAndFixedData(Supplier<PriorityQueue<Object>> factory) {
        PriorityQueue<Object> queue = factory.get();
        queue.enqueue("x", 2);
        queue.enqueue(1L, 2);
        queue.enqueue(5L, 1);
        Assertions.assertEquals("x", queue.dequeue());
        queue.enqueue(4L, 1);
        queue.enqueue(4L, 0);
        Assertions.assertEquals(1L, queue.dequeue());
        queue.enqueue("42", 42);
        queue.enqueue(1L, 42);
        Assertions.assertEquals("42", queue.dequeue());
        Assertions.assertEquals(1L, queue.dequeue());
        Assertions.assertEquals(5L, queue.dequeue());
        Assertions.assertEquals(4L, queue.dequeue());
        Assertions.assertEquals(4L, queue.dequeue());
    }

    static class ObjWrapper {
        private final Object id;

        ObjWrapper(Object id) {
            this.id = id;
        }

        static ObjWrapper of(String x) {
            return new ObjWrapper(x);
        }

        @Override
        public String toString() {
            return String.format("TestObj{id='%s'}", id);
        }
    }

    static class ObjIntWrapper<X> {
        private final X value;
        private final int priority;

        ObjIntWrapper(X value, int priority) {
            this.value = value;
            this.priority = priority;
        }

        static <X> ObjIntWrapper<X> of(X x, int p) {
            return new ObjIntWrapper<>(x, p);
        }

        @SafeVarargs
        static <X> NavigableMap<Integer, List<X>> toTree(ObjIntWrapper<X>... data) {
            return Arrays.stream(data)
                    .collect(Collectors.toMap(x -> x.priority, x -> new ArrayList<>(List.of(x.value)), (left, right) -> {
                        List<X> res = new ArrayList<>();
                        res.addAll(left);
                        res.addAll(right);
                        return res;
                    }, TreeMap::new));
        }

        @Override
        public String toString() {
            return String.format("WithPriority{value=%s, priority=%d}", value, priority);
        }
    }
}
