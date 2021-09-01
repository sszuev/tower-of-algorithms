package com.gitlab.sszuev.arrays;

import java.util.Arrays;

/**
 * Created by @ssz on 22.08.2021.
 *
 * @see Arrays
 */
class ArrayUtils {

    static Object[] copy(Object[] array) {
        return array.length == 0 ? array : Arrays.copyOf(array, array.length, Object[].class);
    }

    static Object[] grow(Object[] array, int toAdd) {
        if (toAdd < 0) {
            throw new IllegalArgumentException();
        }
        Object[] res = new Object[array.length + toAdd];
        System.arraycopy(array, 0, res, 0, array.length);
        return res;
    }

    static Object[] truncate(Object[] array, int toRemove) {
        if (toRemove < 0) {
            throw new IllegalArgumentException();
        }
        Object[] res = new Object[toRemove];
        System.arraycopy(array, 0, res, 0, toRemove);
        return res;
    }

    static void remove(Object[] array, int index) {
        if (array.length - 1 - index < 0) {
            throw new IndexOutOfBoundsException();
        }
        System.arraycopy(array, index + 1, array, index, array.length - 1 - index);
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

    static int checkIndex(int index, int size) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("index = " + index + ", size = " + size);
        }
        return index;
    }
}
