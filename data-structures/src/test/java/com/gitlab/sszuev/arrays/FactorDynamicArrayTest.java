package com.gitlab.sszuev.arrays;

import org.junit.jupiter.api.Test;

/**
 * Created by @ssz on 22.08.2021.
 */
public class FactorDynamicArrayTest extends DynamicArrayBaseTest {
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
