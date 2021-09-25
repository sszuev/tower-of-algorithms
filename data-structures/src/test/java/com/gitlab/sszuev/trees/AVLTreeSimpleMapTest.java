package com.gitlab.sszuev.trees;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by @ssz on 25.09.2021.
 */
public class AVLTreeSimpleMapTest {

    @Test
    public void testHeight() {
        AVLTSimpleMap<Integer, String> map = new AVLTSimpleMap<>();
        map.put(40, "a");
        map.put(50, "b");
        map.put(55, "c");
        map.put(60, "d");
        map.put(20, "b");
        map.put(10, "c");
        map.put(36, "c");
        map.put(45, "c");
        map.put(8, "d");
        map.put(12, "d");
        map.put(30, "d");
        map.put(16, "e");
        map.put(22, "e");
        map.put(28, "f");

        TreeMapUtils.assertBST(map);

        AVLTSimpleMap.AVLBiNode<Integer, String> root = getRoot(map);
        Assertions.assertEquals(6, root.height());
        Assertions.assertNull(root.parent());

        Assertions.assertEquals(5, root.left().height());
        Assertions.assertEquals(3, root.right().height());
        Assertions.assertEquals(40, root.left().parent().key());
        Assertions.assertEquals(40, root.right().parent().key());

        Assertions.assertEquals(4, root.left().right().height());
        Assertions.assertEquals(3, root.left().left().height());
        Assertions.assertEquals(20, root.left().right().parent().key());
        Assertions.assertEquals(20, root.left().left().parent().key());

        Assertions.assertEquals(2, root.right().right().height());
        Assertions.assertEquals(1, root.right().left().height());
        Assertions.assertEquals(50, root.right().right().parent().key());
        Assertions.assertEquals(50, root.right().left().parent().key());
    }

    private static <K, V> AVLTSimpleMap.AVLBiNode<K, V> getRoot(AVLTSimpleMap<K, V> map) {
        return (AVLTSimpleMap.AVLBiNode<K, V>) map.root;
    }
}
