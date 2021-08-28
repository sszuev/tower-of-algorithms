package com.gitlab.sszuev.queues;

import org.junit.jupiter.api.Test;

/**
 * Created by @ssz on 28.08.2021.
 */
public class JDKPriorityQueueTest extends PriorityQueueTestBase {

    @Test
    public void testAddRemoveWithoutPriority() {
        doTestFillAndEmptyWithDefaultPriority(JDKPriorityQueue::new, "A", "A", "B", "C", "D");
        doTestFillAndEmptyWithDefaultPriority(JDKPriorityQueue::new,
                ObjWrapper.of("a"),
                ObjWrapper.of("v"),
                ObjWrapper.of("xxx"));
    }

    @Test
    public void testAddRemoveAllWithPriority() {
        doTestFillAndEmptyWithDifferentPriorities(JDKPriorityQueue::new,
                ObjIntWrapper.of("A", 2),
                ObjIntWrapper.of("B", 1),
                ObjIntWrapper.of("S", 42),
                ObjIntWrapper.of("G", 1),
                ObjIntWrapper.of("C", 2),
                ObjIntWrapper.of("X", Integer.MAX_VALUE),
                ObjIntWrapper.of("W", 0),
                ObjIntWrapper.of("Q", 1),
                ObjIntWrapper.of("Z", 42));
    }

    @Test
    public void testAddRemoveWithPriority() {
        doTestAddRemoveWithPriorityAndFixedData(JDKPriorityQueue::new);
    }

}
