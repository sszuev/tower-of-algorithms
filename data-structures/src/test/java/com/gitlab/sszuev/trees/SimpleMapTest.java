package com.gitlab.sszuev.trees;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by @ssz on 22.09.2021.
 */
public class SimpleMapTest {

    private static <K extends Comparable<K>, V> void assertBST(SimpleMap<K, V> map) {
        if (!(map instanceof BSTSimpleMap)) {
            return;
        }
        System.out.println(TreeMapUtils.print(map));
        Assertions.assertTrue(TreeMapUtils.isBST(((BSTSimpleMap<K, V>) map).root));
        Assertions.assertEquals(TreeMapUtils.size(((BSTSimpleMap<K, V>) map).root), map.size());
        System.out.println("-".repeat(42));
    }

    @Test
    public void testStringMap() {
        SimpleMap<String, Integer> map = new BSTSimpleMap<>();
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

        assertBST(map);
    }

    @Test
    public void testIntegerMapRemoveNilItem() {
        SimpleMap<String, Boolean> map = new BSTSimpleMap<>();
        Assertions.assertNull(map.remove("X"));
        map.put("X", Boolean.TRUE);
        Assertions.assertEquals(1, map.size());
        Assertions.assertNull(map.remove("Y"));
        Assertions.assertTrue(map.remove("X"));
        Assertions.assertEquals(0, map.size());
        assertBST(map);

        map.put("f", Boolean.TRUE);
        map.put("g", Boolean.TRUE);
        map.put("h", Boolean.TRUE);
        map.put("c", Boolean.TRUE);
        map.put("d", Boolean.TRUE);

        Assertions.assertEquals(5, map.size());
        assertBST(map);

        Assertions.assertTrue(map.remove("h"));
        Assertions.assertTrue(map.remove("d"));
        assertBST(map);
        Assertions.assertEquals(3, map.size());


        Assertions.assertTrue(map.remove("c"));
        Assertions.assertTrue(map.remove("g"));
        assertBST(map);
        Assertions.assertEquals(1, map.size());

        Assertions.assertTrue(map.remove("f"));
        assertBST(map);
        Assertions.assertEquals(0, map.size());
    }

    @Test
    public void testLongMapRemoveItemWithSingleChild() {
        SimpleMap<Long, Boolean> map = new BSTSimpleMap<>();
        map.put(40L, Boolean.TRUE);
        map.put(50L, Boolean.TRUE);
        map.put(60L, Boolean.TRUE);
        map.put(30L, Boolean.TRUE);
        map.put(35L, Boolean.TRUE);
        map.put(65L, Boolean.TRUE);

        Assertions.assertEquals(6, map.size());
        assertBST(map);

        Assertions.assertNull(map.remove(42L));

        Assertions.assertTrue(map.remove(60L));
        assertBST(map);
        Assertions.assertEquals(5, map.size());

        Assertions.assertTrue(map.remove(30L));
        assertBST(map);
        Assertions.assertEquals(4, map.size());

        Assertions.assertTrue(map.remove(35L));
        assertBST(map);
        Assertions.assertEquals(3, map.size());

        Assertions.assertTrue(map.remove(50L));
        assertBST(map);
        Assertions.assertEquals(2, map.size());

        Assertions.assertTrue(map.remove(40L));
        assertBST(map);
        Assertions.assertEquals(1, map.size());
    }

    @Test
    public void testIntegerMapRemoveItemWithTwoChildren() {
        SimpleMap<Integer, Integer> map = new BSTSimpleMap<>();
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

        assertBST(map);
        Assertions.assertEquals(14, map.size());

        Assertions.assertEquals(1, map.remove(20));
        assertBST(map);
        Assertions.assertEquals(13, map.size());

        Assertions.assertEquals(0, map.remove(40));
        assertBST(map);
        Assertions.assertEquals(12, map.size());

        Assertions.assertEquals(2, map.remove(45));
        assertBST(map);
        Assertions.assertEquals(11, map.size());

        Assertions.assertEquals(2, map.remove(10));
        assertBST(map);
        Assertions.assertEquals(10, map.size());

        Assertions.assertEquals(4, map.remove(22));
        assertBST(map);
        Assertions.assertEquals(9, map.size());

        Assertions.assertEquals(5, map.remove(28));
        assertBST(map);
        Assertions.assertEquals(8, map.size());

        Assertions.assertEquals(3, map.remove(30));
        assertBST(map);
        Assertions.assertEquals(7, map.size());

        Assertions.assertEquals(1, map.remove(50));
        assertBST(map);
        Assertions.assertEquals(6, map.size());

        Assertions.assertEquals(3, map.remove(12));
        assertBST(map);
        Assertions.assertEquals(5, map.size());

        Assertions.assertEquals(2, map.remove(55));
        assertBST(map);
        Assertions.assertEquals(4, map.size());
    }
}