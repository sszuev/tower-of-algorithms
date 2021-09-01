package com.gitlab.sszuev.queues;

/**
 * A {@link PriorityQueue} implementation
 * which is based on {@link java.util.PriorityQueue JDK PriorityQueue} (i.e. priority heap).
 * <p>
 * Created by @ssz on 28.08.2021.
 */
public class HeapPriorityQueue<E> implements PriorityQueue<E> {
    private final java.util.PriorityQueue<TwoPriorityObjectWrapper<E>> queue;
    // not thread-safe:
    private long counter;

    public HeapPriorityQueue() {
        this.queue = new java.util.PriorityQueue<>();
    }

    @Override
    public void enqueue(E item, int priority) {
        queue.add(new TwoPriorityObjectWrapper<>(item, priority, counter++));
    }

    @Override
    public E dequeue() {
        TwoPriorityObjectWrapper<E> res = queue.poll();
        return res == null ? null : res.element;
    }

}
