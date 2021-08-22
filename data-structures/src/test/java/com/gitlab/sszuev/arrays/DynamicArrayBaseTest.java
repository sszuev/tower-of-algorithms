package com.gitlab.sszuev.arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by @ssz on 22.08.2021.
 */
abstract class DynamicArrayBaseTest {

    abstract DynamicArray<Integer> createIntegerDynamicArray(Integer[] data);

    @Test
    public void testCreateNonEmpty() {
        doTestCreateIntegerDynamicArray(new Integer[]{1, 2, 3, 4, 5, 6, 6, 7, -1});
    }

    void doTestCreateIntegerDynamicArray(Integer[] data) {
        DynamicArray<Integer> res = createIntegerDynamicArray(data);
        Assertions.assertEquals(data.length, res.size());
        for (int i = 0; i < data.length; i++) {
            Assertions.assertEquals(data[i], res.get(i));
        }
    }
}
