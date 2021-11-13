package com.gitlab.sszuev.maps;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * Created by @ssz on 13.11.2021.
 */
public class TmpBNodeTest {

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

    public static BTreeSimpleMap<Integer, String> createTestMap() {
        BTreeSimpleMap<Integer, String> res = new BTreeSimpleMap<>();
        res.root = createTestTree();
        return res;
    }

    public static BTreeSimpleMap.BNodeImpl<Integer, String> createTestTree() {
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

        a1.item(0).left(a2);
        a1.item(0).right(b2);
        a1.item(1).right(c2);

        a2.item(0).left(a3);
        a2.item(0).right(b3);
        b2.item(0).left(c3);
        b2.item(0).right(d3);
        b2.item(1).right(e3);
        c2.item(0).left(f3);
        c2.item(0).right(g3);
        c2.item(1).right(h3);

        return a1;
    }

    @SuppressWarnings("unchecked")
    private static BTreeSimpleMap.BNodeImpl<Integer, String> createTestBNode(int... keys) {
        BTreeSimpleMap.BNodeImpl.ItemImpl<Integer, String>[] items = Arrays.stream(keys)
                .mapToObj(TmpBNodeTest::createTestItem).toArray(BTreeSimpleMap.BNodeImpl.ItemImpl[]::new);
        return new BTreeSimpleMap.BNodeImpl<>(items);
    }

    private static BTreeSimpleMap.BNodeImpl.ItemImpl<Integer, String> createTestItem(int key) {
        BTreeSimpleMap.BNodeImpl.ItemImpl<Integer, String> res = new BTreeSimpleMap.BNodeImpl.ItemImpl<>(key);
        res.value("v=" + key);
        return res;
    }
}
