package com.gitlab.sszuev.queues;

/**
 * An unbounded priority queue generic interface.
 * <p>
 * Created by @ssz on 28.08.2021.
 *
 * @param <E> the type of elements in this array.
 */
public interface PriorityQueue<E> {

    /**
     * Adds the specified element to the end of the queue taking care the priority.
     *
     * @param item     {@link E}, not {@code null}
     * @param priority a non-negative {@code int}
     */
    void enqueue(E item, int priority);

    /**
     * Retrieves and removes the head of this queue, or returns {@code null} if this queue is empty.
     * The priority for elements
     * which have been added by the method {@link #enqueue(Object, int)} with a larger second argument.
     *
     * @return {@link E}
     */
    E dequeue();

    /**
     * Adds an element to this queue with default priority.
     *
     * @param item {@link E}
     */
    default void enqueue(E item) {
        enqueue(item, 0);
    }
}
