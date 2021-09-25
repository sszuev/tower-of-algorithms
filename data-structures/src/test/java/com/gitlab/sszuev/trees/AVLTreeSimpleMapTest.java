package com.gitlab.sszuev.trees;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Created by @ssz on 25.09.2021.
 */
public class AVLTreeSimpleMapTest {

    public static <K extends Comparable<K>, V> void assertAVLT(SimpleMap<K, V> map) {
        if (!(map instanceof AVLTSimpleMap)) {
            return;
        }
        TreeMapUtils.assertBST(map);
        AVLTSimpleMap.AVLBiNode<K, V> root = getRoot(((AVLTSimpleMap<K, V>) map));
        root.preOrder(node -> {
            AVLTSimpleMap.AVLBiNode<K, V> n = asAVL(node);
            int leftHeight = AVLTSimpleMap.AVLBiNode.height(n.left());
            int rightHeight = AVLTSimpleMap.AVLBiNode.height(n.right());
            Assertions.assertFalse(Math.abs(rightHeight - leftHeight) > 1, "Not balanced");
        });
    }

    private static <K, V> AVLTSimpleMap.AVLBiNode<K, V> getRoot(AVLTSimpleMap<K, V> map) {
        return asAVL(map.root);
    }

    @SuppressWarnings("unchecked")
    private static <K, V> AVLTSimpleMap.AVLBiNode<K, V> asAVL(TreeNode<?> node) {
        return (AVLTSimpleMap.AVLBiNode<K, V>) node;
    }

    @Test
    public void testInsert() {
        AVLTSimpleMap<Integer, String> map = new AVLTSimpleMap<>();
        map.put(40, "a");
        assertAVLT(map);
        map.put(50, "b");
        assertAVLT(map);
        map.put(55, "c");
        assertAVLT(map);
        map.put(60, "d");
        assertAVLT(map);
        map.put(20, "b");
        assertAVLT(map);
        map.put(10, "c");
        assertAVLT(map);
        map.put(36, "c");
        assertAVLT(map);
        map.put(45, "c");
        assertAVLT(map);
        map.put(8, "d");
        assertAVLT(map);
        map.put(12, "d");
        assertAVLT(map);
        map.put(30, "d");
        assertAVLT(map);
        map.put(16, "e");
        assertAVLT(map);
        map.put(22, "e");
        assertAVLT(map);
        map.put(28, "f");
        assertAVLT(map);

        AVLTSimpleMap.AVLBiNode<Integer, String> root = getRoot(map);
        Assertions.assertEquals(5, root.height());
        Assertions.assertNull(root.parent());

        Assertions.assertEquals(4, root.left().height());
        Assertions.assertEquals(3, root.right().height());
        Assertions.assertEquals(40, root.left().parent().key());
        Assertions.assertEquals(40, root.right().parent().key());

        Assertions.assertEquals(3, root.left().right().height());
        Assertions.assertEquals(3, root.left().left().height());
        Assertions.assertEquals(20, root.left().right().parent().key());
        Assertions.assertEquals(20, root.left().left().parent().key());

        Assertions.assertEquals(2, root.right().right().height());
        Assertions.assertEquals(1, root.right().left().height());
        Assertions.assertEquals(50, root.right().right().parent().key());
        Assertions.assertEquals(50, root.right().left().parent().key());
    }
}
