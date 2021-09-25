package com.gitlab.sszuev.trees;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by @ssz on 22.09.2021.
 */
public class SimpleMapTest {

    private static <K, V> Stream<Supplier<SimpleMap<K, V>>> maps() {
        return Stream.of(new Supplier<>() {
            @Override
            public String toString() {
                return "BST";
            }

            @Override
            public SimpleMap<K, V> get() {
                return new BSTSimpleMap<>();
            }
        }, new Supplier<>() {
            @Override
            public String toString() {
                return "AVL";
            }

            @Override
            public SimpleMap<K, V> get() {
                return new AVLTSimpleMap<>();
            }
        });
    }

    @ParameterizedTest
    @MethodSource("maps")
    public void testStringMap(Supplier<SimpleMap<String, Integer>> factory) {
        SimpleMap<String, Integer> map = factory.get();
        Assertions.assertNull(map.get("A"));
        Assertions.assertNull(map.put("A", -21));
        Assertions.assertNull(map.put("b", 25));
        Assertions.assertNull(map.put("C", 25));
        Assertions.assertNull(map.put("d", 125));
        Assertions.assertNull(map.put("42", 21));
        Assertions.assertEquals(-21, map.put("A", 42));

        Assertions.assertEquals(5, map.size());

        Assertions.assertEquals(42, map.get("A"));
        Assertions.assertEquals(125, map.get("d"));
        Assertions.assertEquals(25, map.get("b"));
        Assertions.assertEquals(25, map.get("C"));
        Assertions.assertEquals(21, map.get("42"));

        TreeMapUtils.assertBST(map);
    }

    @ParameterizedTest
    @MethodSource("maps")
    public void testIntegerMapRemoveNilItem(Supplier<SimpleMap<String, Boolean>> factory) {
        SimpleMap<String, Boolean> map = factory.get();
        Assertions.assertNull(map.remove("X"));
        map.put("X", Boolean.TRUE);
        Assertions.assertEquals(1, map.size());
        Assertions.assertNull(map.remove("Y"));
        Assertions.assertTrue(map.remove("X"));
        Assertions.assertEquals(0, map.size());
        TreeMapUtils.assertBST(map);

        map.put("f", Boolean.TRUE);
        map.put("g", Boolean.TRUE);
        map.put("h", Boolean.TRUE);
        map.put("c", Boolean.TRUE);
        map.put("d", Boolean.TRUE);

        Assertions.assertEquals(5, map.size());
        TreeMapUtils.assertBST(map);

        Assertions.assertTrue(map.remove("h"));
        Assertions.assertTrue(map.remove("d"));
        TreeMapUtils.assertBST(map);
        Assertions.assertEquals(3, map.size());


        Assertions.assertTrue(map.remove("c"));
        Assertions.assertTrue(map.remove("g"));
        TreeMapUtils.assertBST(map);
        Assertions.assertEquals(1, map.size());

        Assertions.assertTrue(map.remove("f"));
        TreeMapUtils.assertBST(map);
        Assertions.assertEquals(0, map.size());
    }

    @ParameterizedTest
    @MethodSource("maps")
    public void testLongMapRemoveItemWithSingleChild(Supplier<SimpleMap<Long, Boolean>> factory) {
        SimpleMap<Long, Boolean> map = factory.get();
        map.put(40L, Boolean.TRUE);
        map.put(50L, Boolean.TRUE);
        map.put(60L, Boolean.TRUE);
        map.put(30L, Boolean.TRUE);
        map.put(35L, Boolean.TRUE);
        map.put(65L, Boolean.TRUE);

        Assertions.assertEquals(6, map.size());
        TreeMapUtils.assertBST(map);

        Assertions.assertNull(map.remove(42L));

        Assertions.assertTrue(map.remove(60L));
        TreeMapUtils.assertBST(map);
        Assertions.assertEquals(5, map.size());

        Assertions.assertTrue(map.remove(30L));
        TreeMapUtils.assertBST(map);
        Assertions.assertEquals(4, map.size());

        Assertions.assertTrue(map.remove(35L));
        TreeMapUtils.assertBST(map);
        Assertions.assertEquals(3, map.size());

        Assertions.assertTrue(map.remove(50L));
        TreeMapUtils.assertBST(map);
        Assertions.assertEquals(2, map.size());

        Assertions.assertTrue(map.remove(40L));
        TreeMapUtils.assertBST(map);
        Assertions.assertEquals(1, map.size());
    }

    @ParameterizedTest
    @MethodSource("maps")
    public void testIntegerMapRemoveItemWithTwoChildren(Supplier<SimpleMap<Integer, Integer>> factory) {
        SimpleMap<Integer, Integer> map = factory.get();
        map.put(40, 0);
        map.put(50, 1);
        map.put(55, 2);
        map.put(60, 3);
        map.put(20, 1);
        map.put(10, 2);
        map.put(36, 2);
        map.put(45, 2);
        map.put(8, 3);
        map.put(12, 3);
        map.put(30, 3);
        map.put(16, 4);
        map.put(22, 4);
        map.put(28, 5);

        TreeMapUtils.assertBST(map);
        Assertions.assertEquals(14, map.size());

        Assertions.assertEquals(1, map.remove(20));
        TreeMapUtils.assertBST(map);
        Assertions.assertEquals(13, map.size());

        Assertions.assertEquals(0, map.remove(40));
        TreeMapUtils.assertBST(map);
        Assertions.assertEquals(12, map.size());

        Assertions.assertEquals(2, map.remove(45));
        TreeMapUtils.assertBST(map);
        Assertions.assertEquals(11, map.size());

        Assertions.assertEquals(2, map.remove(10));
        TreeMapUtils.assertBST(map);
        Assertions.assertEquals(10, map.size());

        Assertions.assertEquals(4, map.remove(22));
        TreeMapUtils.assertBST(map);
        Assertions.assertEquals(9, map.size());

        Assertions.assertEquals(5, map.remove(28));
        TreeMapUtils.assertBST(map);
        Assertions.assertEquals(8, map.size());

        Assertions.assertEquals(3, map.remove(30));
        TreeMapUtils.assertBST(map);
        Assertions.assertEquals(7, map.size());

        Assertions.assertEquals(1, map.remove(50));
        TreeMapUtils.assertBST(map);
        Assertions.assertEquals(6, map.size());

        Assertions.assertEquals(3, map.remove(12));
        TreeMapUtils.assertBST(map);
        Assertions.assertEquals(5, map.size());

        Assertions.assertEquals(2, map.remove(55));
        TreeMapUtils.assertBST(map);
        Assertions.assertEquals(4, map.size());
    }
}