package com.gitlab.sszuev.arrays;

import org.junit.jupiter.api.Test;

/**
 * Created by @ssz on 22.08.2021.
 */
public class SimpleDynamicArrayTest extends DynamicArrayBaseTest {

    @Test
    public void testCreateNonEmpty() {
        doTestCreateNonEmpty(SimpleDynamicArray::of);
    }

    @Test
    public void testRemove() {
        doTestRemove(SimpleDynamicArray::of);
    }
}
