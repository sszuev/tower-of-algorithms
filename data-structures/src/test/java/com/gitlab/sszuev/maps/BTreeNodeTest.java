package com.gitlab.sszuev.maps;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * To test {@link BTreeSimpleMap}.
 * <p>
 * Created by @ssz on 13.11.2021.
 */
public class BTreeNodeTest {

    @Test
    public void testPutDegree3() {
        BTreeSimpleMap<Integer, String> map = new BTreeSimpleMap<>();
        long count = 0;
        String res = null;
        for (int k : new int[]{10, 20, 30, 40, 5, 50, 60, 70, 45, 55, 15, 25, 26, 16}) {
            System.out.println("==".repeat(42) + "::k=" + k);
            Assertions.assertNull(map.put(k, "V-" + k));
            res = TreeMapUtils.print(map);
            System.out.println(res);
            assertBTree(map);
            Assertions.assertEquals(++count, map.size());
            Assertions.assertEquals(count, res.split("[]|]").length - 1);
        }
        Assertions.assertEquals(3, res.split("\\|").length - 1);
        Assertions.assertTrue(res.contains("[20|40]") && res.contains("[15|16]") && res.contains("[50|60]"));
        Assertions.assertEquals(3, res.split("\n").length);
        Assertions.assertEquals("[20|40]", map.root.toString());

        for (int k : new int[]{18, 6, 8}) {
            System.out.println("==".repeat(42) + "::k=" + k);
            Assertions.assertNull(map.put(k, "V-" + k));
            res = TreeMapUtils.print(map);
            System.out.println(res);
            assertBTree(map);
            Assertions.assertEquals(++count, map.size());
            Assertions.assertEquals(count, res.split("[]|]").length - 1);
        }
        Assertions.assertEquals(1, res.split("\\|").length - 1);
        Assertions.assertTrue(res.contains("[50|60]"));
        Assertions.assertEquals(4, res.split("\n").length);
        Assertions.assertEquals("[20]", map.root.toString());
    }

    @Test
    public void testPutDegree3_catchBug() {
        BTreeSimpleMap<Integer, String> map = new BTreeSimpleMap<>();
        long count = 0;
        String res = null;
        for (int k : new int[]{49, 191, 118, 210, 120, 123, 284, 190, 411, 213, 151, 165, 228}) {
            System.out.println("==".repeat(42) + "::k=" + k);
            Assertions.assertNull(map.put(k, "V-" + k));
            res = TreeMapUtils.print(map);
            System.out.println(res);
            assertBTree(map);
            Assertions.assertEquals(++count, map.size());
            Assertions.assertEquals(count, res.split("[]|]").length - 1);
        }
        Assertions.assertEquals(2, res.split("\\|").length - 1);
        Assertions.assertTrue(res.contains("[213|284]"));
        Assertions.assertEquals(3, res.split("\n").length);
        Assertions.assertEquals("[123|191]", map.root.toString());
    }

    @Test
    public void testPutDegree4() {
        BTreeSimpleMap<Integer, String> map = new BTreeSimpleMap<>(4);
        long count = 0;
        String res = null;
        for (int k : new int[]{60, 40, 50, 100, 10, 80, 70, 90, 30, 20}) {
            System.out.println("==".repeat(42) + "::k=" + k);
            Assertions.assertNull(map.put(k, "V-" + k));
            res = TreeMapUtils.print(map);
            System.out.println(res);
            assertBTree(map);
            Assertions.assertEquals(++count, map.size());
            Assertions.assertEquals(count, res.split("[]|]").length - 1);
        }
        Assertions.assertEquals(5, res.split("\\|").length - 1);
        Assertions.assertEquals(2, res.split("\n").length);
        Assertions.assertEquals("[20|50|70]", map.root.toString());
        Assertions.assertTrue(res.contains("[80|90|100]") && res.contains("[30|40]"));
    }

    @Test
    public void testGet() {
        BTreeSimpleMap<Integer, String> map = createTestMap();
        System.out.println(TreeMapUtils.print(map));
        for (int k : new int[]{1, 2, 5, 6, 7, 10, 11, 12, 14, 21, 33, 34, 55, 56, 67, 122}) {
            System.out.println("Test key=" + k);
            Assertions.assertEquals("v=" + k, map.get(k), "Wrong value for key " + k);
        }
        for (int k : new int[]{4, 0, 42, 142}) {
            System.out.println("Test key=" + k);
            Assertions.assertNull(map.get(k), "There is a value for key " + k);
        }
    }

    @Test
    public void testDelCase1() {
        BTreeSimpleMap<Integer, String> map = new BTreeSimpleMap<>(3);
        for (int k : new int[]{5, 8, 1, 2, 7, 9, 10}) {
            Assertions.assertNull(map.put(k, "V-" + k));
        }
        String res = TreeMapUtils.print(map);
        System.out.println(res);
        Assertions.assertEquals(7, map.size());
        System.out.println("=".repeat(42));

        Assertions.assertEquals("V-2", map.remove(2));
        res = TreeMapUtils.print(map);
        System.out.println(res);
        assertBTree(map);
        Assertions.assertEquals(6, map.size());
        System.out.println("=".repeat(42));

        Assertions.assertEquals("V-9", map.remove(9));
        res = TreeMapUtils.print(map);
        System.out.println(res);
        assertBTree(map);
        Assertions.assertEquals(5, map.size());
    }

    @Test
    public void testDelCase2aDegree3RightFullHalfNode() {
        BTreeSimpleMap<Integer, String> map = new BTreeSimpleMap<>(3);
        for (int k : new int[]{5, 8, 1, 7, 9, 10}) {
            Assertions.assertNull(map.put(k, "V-" + k));
        }
        String res = TreeMapUtils.print(map);
        System.out.println(res);
        Assertions.assertEquals(6, map.size());
        System.out.println("=".repeat(42));

        Assertions.assertEquals("V-7", map.remove(7));
        res = TreeMapUtils.print(map);
        System.out.println(res);
        assertBTree(map);
        Assertions.assertEquals(5, map.size());
        Assertions.assertEquals("[5|9]", map.root.toString());
        Assertions.assertEquals("[1][8][10]",
                map.getRoot().children().map(String::valueOf).collect(Collectors.joining("")));
    }

    @Test
    public void testDelCase2aDegree3LeftFullHalfNode() {
        BTreeSimpleMap<Integer, String> map = new BTreeSimpleMap<>(3);
        for (int k : new int[]{23, 90, 4, 13, 64, 93}) {
            Assertions.assertNull(map.put(k, "V-" + k));
        }
        String res = TreeMapUtils.print(map);
        System.out.println(res);
        Assertions.assertEquals(6, map.size());
        System.out.println("=".repeat(42));

        Assertions.assertEquals("V-64", map.remove(64));
        res = TreeMapUtils.print(map);
        System.out.println(res);
        assertBTree(map);
        Assertions.assertEquals(5, map.size());
        Assertions.assertEquals("[13|90]", map.root.toString());
        Assertions.assertEquals("[4][23][93]",
                map.getRoot().children().map(String::valueOf).collect(Collectors.joining("")));
    }

    @Test
    public void testDelCase2aDegree6() {
        BTreeSimpleMap<Integer, String> map = new BTreeSimpleMap<>(6);
        for (int k : new int[]{44, 78, 233, 623, 12, 23, 34, 45, 67, 82, 130, 324, 456, 829, 888, 3443}) {
            Assertions.assertNull(map.put(k, "V-" + k));
        }
        String res = TreeMapUtils.print(map);
        System.out.println(res);
        Assertions.assertEquals(16, map.size());

        for (int k : new int[]{67, 324}) {
            System.out.println("=".repeat(42));
            Assertions.assertEquals("V-" + k, map.remove(k));
            res = TreeMapUtils.print(map);
            System.out.println(res);
            assertBTree(map);
            System.out.println("=".repeat(42));
        }

        Assertions.assertEquals(14, map.size());
        Assertions.assertEquals("[34|78|233|829]", map.root.toString());
        Assertions.assertEquals("[12|23][44|45][82|130][456|623][888|3443]",
                map.getRoot().children().map(String::valueOf).collect(Collectors.joining("")));
    }

    public static BTreeSimpleMap<Integer, String> createTestMap() {
        BTreeSimpleMap<Integer, String> res = new BTreeSimpleMap<>();
        res.root = createTestTree();
        return res;
    }

    private static <X> void assertBTree(BTreeSimpleMap<X, ?> map) {
        BTreeSimpleMap.BNodeImpl<X, ?> root = map.getRoot();
        TreeMapUtils.assertBNode(map);
        TreeMapUtils.assertParents(root);
        Assertions.assertEquals(TreeMapUtils.size(root), map.size(), "Wrong size");
    }

    private static BTreeSimpleMap.BNodeImpl<Integer, String> createTestTree() {
        BTreeSimpleMap.BNodeImpl<Integer, String> a3 = createTestBNode(1);
        BTreeSimpleMap.BNodeImpl<Integer, String> b3 = createTestBNode(5);
        BTreeSimpleMap.BNodeImpl<Integer, String> c3 = createTestBNode(7);
        BTreeSimpleMap.BNodeImpl<Integer, String> d3 = createTestBNode(11);
        BTreeSimpleMap.BNodeImpl<Integer, String> e3 = createTestBNode(14, 21);
        BTreeSimpleMap.BNodeImpl<Integer, String> f3 = createTestBNode(34);
        BTreeSimpleMap.BNodeImpl<Integer, String> g3 = createTestBNode(56);
        BTreeSimpleMap.BNodeImpl<Integer, String> h3 = createTestBNode(122);

        BTreeSimpleMap.BNodeImpl<Integer, String> a2 = createTestBNode(2);
        BTreeSimpleMap.BNodeImpl<Integer, String> b2 = createTestBNode(10, 12);
        BTreeSimpleMap.BNodeImpl<Integer, String> c2 = createTestBNode(55, 67);

        BTreeSimpleMap.BNodeImpl<Integer, String> a1 = createTestBNode(6, 33);

        a1.left(a2);
        a1.right(0, b2);
        a1.right(1, c2);

        a2.left(a3);
        a2.right(0, b3);
        b2.left(c3);
        b2.right(0, d3);
        b2.right(1, e3);
        c2.left(f3);
        c2.right(0, g3);
        c2.right(1, h3);

        return a1;
    }

    @SuppressWarnings("unchecked")
    private static BTreeSimpleMap.BNodeImpl<Integer, String> createTestBNode(int... keys) {
        BTreeSimpleMap.BNodeImpl.ItemImpl<Integer, String>[] items = Arrays.stream(keys)
                .mapToObj(BTreeNodeTest::createTestItem).toArray(BTreeSimpleMap.BNodeImpl.ItemImpl[]::new);
        return new BTreeSimpleMap.BNodeImpl<>(items);
    }

    private static BTreeSimpleMap.BNodeImpl.ItemImpl<Integer, String> createTestItem(int key) {
        BTreeSimpleMap.BNodeImpl.ItemImpl<Integer, String> res = new BTreeSimpleMap.BNodeImpl.ItemImpl<>(key);
        res.value("v=" + key);
        return res;
    }


}
