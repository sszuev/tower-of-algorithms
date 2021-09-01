package com.gitlab.sszuev.queues;

import java.util.NavigableSet;
import java.util.TreeSet;

/**
 * A {@link PriorityQueue} implementation which is based on {@link TreeSet}.
 * Created by @ssz on 28.08.2021.
 */
public class TreeSetPriorityQueue<E> implements PriorityQueue<E> {
    private final NavigableSet<TwoPriorityObjectWrapper<E>> queue;
    // not thread-safe:
    private long counter;

    public TreeSetPriorityQueue() {
        this.queue = new TreeSet<>();
    }

    @Override
    public void enqueue(E item, int priority) {
        queue.add(new TwoPriorityObjectWrapper<>(item, priority, counter++));
    }

    @Override
    public E dequeue() {
        TwoPriorityObjectWrapper<E> res = queue.first();
        queue.remove(res);
        return res == null ? null : res.element;
    }
}
