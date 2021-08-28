package com.gitlab.sszuev.arrays;

import org.junit.jupiter.api.Test;

import java.util.stream.LongStream;

/**
 * Created by @ssz on 22.08.2021.
 */
public class FactorVectorDynamicArrayTest extends DynamicArrayBaseTest {

    @Test
    public void testCreateNonEmptyWithVector4() {
        doTestCreateNonEmpty(data -> FactorVectorDynamicArray.of(4, data));
    }

    @Test
    public void testAdd() {
        doTestAdd(data -> FactorVectorDynamicArray.of(42, data), 42);
        doTestAdd(data -> new FactorVectorDynamicArray<>(33, 500));
        doTestAdd(data -> new FactorVectorDynamicArray<>(3, 5));
    }

    @Test
    public void testInsert() {
        doTestInsert(data -> FactorVectorDynamicArray.of(42, data), "5", "5", "4", "3", "2", "1");
        doTestInsert(data -> new FactorVectorDynamicArray<>(1, 300));
    }

    @Test
    public void testRandomModify() {
        doRandomModify(data -> FactorVectorDynamicArray.of(21, data), 10, LongStream.range(0, 10).boxed().toArray(Long[]::new));
        doRandomModify(data -> new FactorVectorDynamicArray<>(34, 42), 42);
    }

    @Test
    public void testRemoveWithFactor2() {
        doTestRemove(data -> FactorVectorDynamicArray.of(2, data));
    }

    @Test
    public void testRemoveWithFactor5() {
        doTestRemove(data -> FactorVectorDynamicArray.of(5, data));
    }
}
