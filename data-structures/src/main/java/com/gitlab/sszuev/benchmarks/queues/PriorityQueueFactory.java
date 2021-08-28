package com.gitlab.sszuev.benchmarks.queues;

import com.gitlab.sszuev.queues.HeapPriorityQueue;
import com.gitlab.sszuev.queues.MapPriorityQueue;
import com.gitlab.sszuev.queues.PriorityQueue;
import com.gitlab.sszuev.queues.TreeSetPriorityQueue;

/**
 * Created by @ssz on 28.08.2021.
 */
public enum PriorityQueueFactory {
    HEAP_QUEUE {
        @Override
        public PriorityQueue<Object> createPriorityQueue() {
            return new HeapPriorityQueue<>();
        }
    },
    TREE_QUEUE {
        @Override
        public PriorityQueue<Object> createPriorityQueue() {
            return new TreeSetPriorityQueue<>();
        }
    },
    MAP_QUEUE {
        @Override
        public PriorityQueue<Object> createPriorityQueue() {
            return new MapPriorityQueue<>();
        }
    },
    ;

    public abstract PriorityQueue<Object> createPriorityQueue();
}
