package com.gitlab.sszuev.arrays;

/**
 * Created by @ssz on 22.08.2021.
 */
public class SimpleDynamicArrayTest extends DynamicArrayBaseTest {
    @Override
    DynamicArray<Integer> createIntegerDynamicArray(Integer[] data) {
        return SimpleDynamicArray.of(data);
    }

    @Override
    DynamicArray<String> createStringDynamicArray(String[] data) {
        return SimpleDynamicArray.of(data);
    }
}
