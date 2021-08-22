package com.gitlab.sszuev.arrays;

/**
 * Created by @ssz on 22.08.2021.
 */
public class FactorDynamicArrayTest extends DynamicArrayBaseTest {
    @Override
    DynamicArray<Integer> createIntegerDynamicArray(Integer[] data) {
        return FactorDynamicArray.of(10, data);
    }
}
