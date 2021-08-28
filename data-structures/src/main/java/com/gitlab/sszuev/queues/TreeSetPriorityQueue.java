package com.gitlab.sszuev.queues;

import java.util.Comparator;
import java.util.NavigableSet;
import java.util.Objects;
import java.util.TreeSet;

/**
 * A {@link PriorityQueue} implementation which is based on {@link TreeSet}.
 * Created by @ssz on 28.08.2021.
 */
public class TreeSetPriorityQueue<E> implements PriorityQueue<E> {
    private final NavigableSet<Item<E>> queue;
    // not thread-safe:
    private long counter;

    public TreeSetPriorityQueue() {
        this.queue = new TreeSet<>();
    }

    @Override
    public void enqueue(E item, int priority) {
        queue.add(new Item<>(item, priority, counter++));
    }

    @Override
    public E dequeue() {
        Item<E> res = queue.first();
        queue.remove(res);
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
        public int compareTo(Item<X> other) {
            return COMPARATOR.compare(this, other);
        }
    }
}
