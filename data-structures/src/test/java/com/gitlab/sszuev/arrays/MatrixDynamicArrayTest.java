package com.gitlab.sszuev.arrays;

/**
 * Created by @ssz on 22.08.2021.
 */
public class MatrixDynamicArrayTest extends DynamicArrayBaseTest {
    @Override
    DynamicArray<Integer> createIntegerDynamicArray(Integer[] data) {
        return MatrixDynamicArray.of(2, data);
    }

    @Override
    DynamicArray<String> createStringDynamicArray(String[] data) {
        return MatrixDynamicArray.of(3, data);
    }
}
