package com.gitlab.sszuev.arrays;

/**
 * Created by @ssz on 22.08.2021.
 */
public class VectorDynamicArrayTest extends DynamicArrayBaseTest {

    @Override
    DynamicArray<Integer> createIntegerDynamicArray(Integer[] data) {
        return VectorDynamicArray.of(2, data);
    }
}
