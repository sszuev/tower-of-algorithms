package com.gitlab.sszuev.queues;

import org.junit.jupiter.api.Test;

/**
 * Created by @ssz on 28.08.2021.
 */
public class MapPriorityQueueTest extends PriorityQueueTestBase {

    @Test
    public void testAddRemoveWithoutPriority() {
        doTestFillAndEmptyWithDefaultPriority(MapPriorityQueue::new, 'G', 'H', 'J', 'J', 'K');
        doTestFillAndEmptyWithDefaultPriority(MapPriorityQueue::new,
                ObjWrapper.of(9),
                ObjWrapper.of(42),
                ObjWrapper.of(42));
    }

    @Test
    public void testAddRemoveAllWithPriority() {
        doTestFillAndEmptyWithDifferentPriorities(MapPriorityQueue::new,
                ObjIntWrapper.of('f', 2),
                ObjIntWrapper.of('f', 42),
                ObjIntWrapper.of('e', 1),
                ObjIntWrapper.of('v', Integer.MAX_VALUE / 2),
                ObjIntWrapper.of('x', Integer.MAX_VALUE),
                ObjIntWrapper.of('w', -42),
                ObjIntWrapper.of('g', Integer.MAX_VALUE / 2),
                ObjIntWrapper.of('x', 42));
    }

    @Test
    public void testAddRemoveWithPriority() {
        doTestAddRemoveWithPriorityAndFixedData(MapPriorityQueue::new);
    }
}
