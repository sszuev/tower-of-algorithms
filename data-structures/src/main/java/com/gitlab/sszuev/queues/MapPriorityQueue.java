package com.gitlab.sszuev.queues;

import java.util.*;

/**
 * A {@link PriorityQueue} implementation which is based on {@link java.util.Map}.
 * <p>
 * Created by @ssz on 28.08.2021.
 */
public class MapPriorityQueue<E> implements PriorityQueue<E> {
    private final NavigableMap<Integer, List<E>> queue;

    public MapPriorityQueue() {
        this.queue = new TreeMap<>();
    }

    @Override
    public void enqueue(E item, int priority) {
        queue.computeIfAbsent(priority, x -> new LinkedList<>()).add(item);
    }

    @Override
    public E dequeue() {
        Map.Entry<Integer, List<E>> entry = queue.lastEntry();
        if (entry == null) {
            return null;
        }
        E res = entry.getValue().remove(0);
        if (res == null || entry.getValue().isEmpty()) {
            queue.remove(entry.getKey());
        }
        return res;
    }
}
