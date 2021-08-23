package com.gitlab.sszuev.arrays;

import org.junit.jupiter.api.Test;

/**
 * Created by @ssz on 22.08.2021.
 */
public class VectorDynamicArrayTest extends DynamicArrayBaseTest {

    @Test
    public void testAdd() {
        doTestAdd(data -> VectorDynamicArray.of(42, data), 42);
        doTestAdd(data -> new VectorDynamicArray<>(33));
    }

    @Test
    public void testInsert() {
        doTestInsert(data -> VectorDynamicArray.of(2, data), "x", "y", "z");
        doTestInsert(data -> new VectorDynamicArray<>(1));
    }

    @Test
    public void testCreateNonEmptyWithVector4() {
        doTestCreateNonEmpty(data -> VectorDynamicArray.of(4, data));
    }

    @Test
    public void testRemoveWithVector2() {
        doTestRemove(data -> VectorDynamicArray.of(2, data));
    }

    @Test
    public void testRemoveWithVector100() {
        doTestRemove(data -> VectorDynamicArray.of(100, data));
    }
}
