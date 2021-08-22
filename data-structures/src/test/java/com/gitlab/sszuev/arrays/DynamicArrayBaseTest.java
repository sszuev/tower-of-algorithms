package com.gitlab.sszuev.arrays;

import org.junit.jupiter.api.Assertions;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by @ssz on 22.08.2021.
 */
abstract class DynamicArrayBaseTest {

    void doTestCreateNonEmpty(Function<Integer[], DynamicArray<Integer>> factory) {
        Integer[] data = new Integer[]{1, 2, 3, 4, 5, 6, 6, 7, -1};
        DynamicArray<Integer> res = factory.apply(data);
        assertArray(res, data);
    }

    void doTestAdd(Function<Integer[], DynamicArray<Integer>> factory, Integer... data) {
        List<Integer> list = Arrays.stream(data).collect(Collectors.toList());
        DynamicArray<Integer> res = factory.apply(data);
        list.add(42);
        res.add(42);
        assertArray(res, list);

        list.add(42);
        res.add(42);
        assertArray(res, list);
    }

    void doTestRemove(Function<String[], DynamicArray<String>> factory) {
        String[] data = new String[]{"a", "b", "c", "d", "e"};
        DynamicArray<String> res = factory.apply(data);
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
    <X> void assertArray(DynamicArray<X> actual, X... expected) {
        assertArray(actual, List.of(expected));
    }

    <X> void assertArray(DynamicArray<X> actual, List<X> expected) {
        Assertions.assertEquals(expected.size(), actual.size());
        IntStream.range(0, expected.size()).forEach(i -> Assertions.assertEquals(expected.get(i), actual.get(i)));
    }
}
