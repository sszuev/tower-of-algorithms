package com.gitlab.sszuev.queues;

import java.util.Comparator;
import java.util.Objects;

/**
 * Created by @ssz on 28.08.2021.
 */
public class JDKPriorityQueue<E> implements PriorityQueue<E> {
    private final java.util.PriorityQueue<Item<E>> queue;
    // not thread-safe:
    private long counter;

    public JDKPriorityQueue() {
        this.queue = new java.util.PriorityQueue<>();
    }

    @Override
    public void enqueue(E item, int priority) {
        queue.add(new Item<>(item, priority, counter++));
    }

    @Override
    public E dequeue() {
        Item<E> res = queue.poll();
        return res == null ? null : res.element;
    }

    /**
     * A {@link Comparable comparable} wrapper for {@link X} with two priorities.
     * There is a user-specified {@link Item#major primary priority}, which is used in first order.
     * If two items have equal primary priority, a {@link Item#minor secondary priority} is used.
     *
     * @param <X> anything
     */
    protected static class Item<X> implements Comparable<Item<X>> {
        protected static final Comparator<Item<?>> COMPARATOR = Comparator.comparing((Item<?> x) -> x.major).reversed()
                .thenComparing(x -> x.minor);

        protected final X element;
        protected final int major;
        protected final long minor;

        protected Item(X element, int major, long minor) {
            this.element = Objects.requireNonNull(element);
            this.major = major;
            this.minor = minor;
        }

        @SuppressWarnings("NullableProblems")
        @Override
        public int compareTo(JDKPriorityQueue.Item<X> other) {
            return COMPARATOR.compare(this, other);
        }


        public static void main(String... args) {
            Item<String> a = new Item<>("a", 1, 2);
            Item<String> b = new Item<>("a", 1, 1);

        }
    }
}
