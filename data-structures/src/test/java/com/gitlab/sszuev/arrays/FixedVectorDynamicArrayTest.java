package com.gitlab.sszuev.arrays;

import org.junit.jupiter.api.Test;

import java.util.stream.LongStream;

/**
 * Created by @ssz on 22.08.2021.
 */
public class FixedVectorDynamicArrayTest extends DynamicArrayBaseTest {

    @Test
    public void testAdd() {
        doTestAdd(data -> FixedVectorDynamicArray.of(42, data), 42);
        doTestAdd(data -> new FixedVectorDynamicArray<>(33));
    }

    @Test
    public void testInsert() {
        doTestInsert(data -> FixedVectorDynamicArray.of(2, data), "x", "y", "z");
        doTestInsert(data -> new FixedVectorDynamicArray<>(1));
    }

    @Test
    public void testRandomModify() {
        doRandomModify(data -> FixedVectorDynamicArray.of(42, data), 10, LongStream.range(0, 11).boxed().toArray(Long[]::new));
        doRandomModify(data -> new FixedVectorDynamicArray<>(42), 2);
    }

    @Test
    public void testCreateNonEmptyWithVector4() {
        doTestCreateNonEmpty(data -> FixedVectorDynamicArray.of(4, data));
    }

    @Test
    public void testRemoveWithVector2() {
        doTestRemove(data -> FixedVectorDynamicArray.of(2, data));
    }

    @Test
    public void testRemoveWithVector100() {
        doTestRemove(data -> FixedVectorDynamicArray.of(100, data));
    }
}
