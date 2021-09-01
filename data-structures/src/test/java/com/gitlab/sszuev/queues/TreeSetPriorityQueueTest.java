package com.gitlab.sszuev.queues;

import org.junit.jupiter.api.Test;

/**
 * Created by @ssz on 28.08.2021.
 */
public class TreeSetPriorityQueueTest extends PriorityQueueTestBase {
    @Test
    public void testAddRemoveWithoutPriority() {
        doTestFillAndEmptyWithDefaultPriority(TreeSetPriorityQueue::new, 1, 1, 2, 2, 3, 3, 4, 4, 42, 1, 2, 3);
        doTestFillAndEmptyWithDefaultPriority(HeapPriorityQueue::new,
                ObjWrapper.of(42),
                ObjWrapper.of("x"),
                ObjWrapper.of("y"));
    }

    @Test
    public void testAddRemoveAllWithPriority() {
        doTestFillAndEmptyWithDifferentPriorities(TreeSetPriorityQueue::new,
                ObjIntWrapper.of("b", 1),
                ObjIntWrapper.of("a", 3),
                ObjIntWrapper.of("g", 1),
                ObjIntWrapper.of("c", 2),
                ObjIntWrapper.of("j", Integer.MIN_VALUE),
                ObjIntWrapper.of("w", 0),
                ObjIntWrapper.of("q", 1),
                ObjIntWrapper.of("k", Integer.MAX_VALUE),
                ObjIntWrapper.of("l", Integer.MIN_VALUE),
                ObjIntWrapper.of("s", 42),
                ObjIntWrapper.of("z", 42));
    }

    @Test
    public void testAddRemoveWithPriority() {
        doTestAddRemoveWithPriorityAndFixedData(TreeSetPriorityQueue::new);
    }
}
