package com.gitlab.sszuev.arrays;

import java.util.Arrays;

/**
 * Created by @ssz on 22.08.2021.
 */
class ArrayUtils {

    static Object[] copy(Object[] array) {
        return array.length == 0 ? array : Arrays.copyOf(array, array.length, Object[].class);
    }

    static Object[][] split(Object[] array, int chunkSize) {
        if (chunkSize <= 0) {
            throw new IllegalArgumentException();
        }
        int rest = array.length % chunkSize;
        int chunks = array.length / chunkSize;
        Object[][] res = new Object[chunks + (rest > 0 ? 1 : 0)][];
        for (int i = 0; i < chunks; i++) {
            res[i] = Arrays.copyOfRange(array, i * chunkSize, i * chunkSize + chunkSize);
        }
        if (rest > 0) {
            res[chunks] = Arrays.copyOfRange(array, chunks * chunkSize, chunks * chunkSize + rest);
        }
        return res;
    }
}
