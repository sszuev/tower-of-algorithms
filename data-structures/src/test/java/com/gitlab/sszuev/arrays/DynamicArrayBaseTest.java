package com.gitlab.sszuev.arrays;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by @ssz on 22.08.2021.
 */
abstract class DynamicArrayBaseTest {

    abstract DynamicArray<Integer> createIntegerDynamicArray(Integer[] data);

    abstract DynamicArray<String> createStringDynamicArray(String[] data);

    @Test
    public void testCreateNonEmpty() {
        Integer[] data = new Integer[]{1, 2, 3, 4, 5, 6, 6, 7, -1};
        DynamicArray<Integer> res = createIntegerDynamicArray(data);
        assertArray(res, data);
    }

    @Test
    public void testRemove() {
        String[] data = new String[]{"a", "b", "c", "d", "e"};
        DynamicArray<String> res = createStringDynamicArray(data);
        Assertions.assertEquals("c", res.remove(2));
        assertArray(res, "a", "b", "d", "e");

        Assertions.assertEquals("d", res.remove(2));
        assertArray(res, "a", "b", "e");

        Assertions.assertEquals("a", res.remove(0));
        assertArray(res, "b", "e");

        Assertions.assertEquals("e", res.remove(1));
        assertArray(res, "b");

        Assertions.assertEquals("b", res.remove(0));
        assertArray(res);
    }

    @SuppressWarnings("unchecked")
    private <X> void assertArray(DynamicArray<X> actual, X... expected) {
        Assertions.assertEquals(expected.length, actual.size());
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertEquals(expected[i], actual.get(i));
        }
    }
}
