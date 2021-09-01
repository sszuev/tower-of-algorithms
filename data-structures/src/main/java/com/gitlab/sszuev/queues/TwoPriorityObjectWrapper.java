package com.gitlab.sszuev.queues;

import java.util.Comparator;
import java.util.Objects;

/**
 * A {@link Comparable comparable} wrapper for {@link E} with two priorities.
 * Here, there is a {@link #major primary priority},  which is used first firstly.
 * In case two objects have equal primary priority, a {@link #minor secondary priority} is used.
 *
 * @param <E> anything
 */
public class TwoPriorityObjectWrapper<E> implements Comparable<TwoPriorityObjectWrapper<E>> {
    protected static final Comparator<TwoPriorityObjectWrapper<?>> COMPARATOR =
            Comparator.comparing((TwoPriorityObjectWrapper<?> x) -> x.major).reversed().thenComparing(x -> x.minor);

    protected final E element;
    protected final int major;
    protected final long minor;

    protected TwoPriorityObjectWrapper(E element, int major, long minor) {
        this.element = Objects.requireNonNull(element);
        this.major = major;
        this.minor = minor;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(TwoPriorityObjectWrapper<E> other) {
        return COMPARATOR.compare(this, other);
    }
}
