package com.gitlab.sszuev.arrays;

import org.junit.jupiter.api.Test;

/**
 * Created by @ssz on 22.08.2021.
 */
public class FactorDynamicArrayTest extends DynamicArrayBaseTest {

    @Test
    public void testAdd() {
        doTestAdd(data -> FactorDynamicArray.of(42, data), 42);
        doTestAdd(data -> new FactorDynamicArray<>(33, 500));
        doTestAdd(data -> new FactorDynamicArray<>(3, 5));
    }

    @Test
    public void testCreateNonEmptyWithVector4() {
        doTestCreateNonEmpty(data -> FactorDynamicArray.of(4, data));
    }

    @Test
    public void testRemoveWithFactor2() {
        doTestRemove(data -> FactorDynamicArray.of(2, data));
    }

    @Test
    public void testRemoveWithFactor5() {
        doTestRemove(data -> FactorDynamicArray.of(5, data));
    }
}
