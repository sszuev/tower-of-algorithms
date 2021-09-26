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
        if (root == null) {
            return;
        }
        AssertionError error = new AssertionError("Three is not balanced");
        root.preOrder(node -> {
            AVLTSimpleMap.AVLBiNode<K, V> n = asAVL(node);
            int leftHeight = AVLTSimpleMap.AVLBiNode.height(n.left());
            int rightHeight = AVLTSimpleMap.AVLBiNode.height(n.right());
            if (Math.abs(rightHeight - leftHeight) > 1) {
                throw error;
            }
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
    public void testInsertFixedData() {
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

    @Test
    public void testInsertSequentialDataAndRemove() {
        Object data = "X";
        AVLTSimpleMap<Long, Object> map = new AVLTSimpleMap<>();
        for (long i = 0; i < 20; i++) {
            map.put(i, data);
        }
        assertAVLT(map);

        Assertions.assertEquals(data, map.remove(15L));
        assertAVLT(map);
        Assertions.assertEquals(19, map.size());

        Assertions.assertEquals(data, map.remove(19L));
        assertAVLT(map);
        Assertions.assertEquals(18, map.size());

        Assertions.assertEquals(data, map.remove(17L));
        assertAVLT(map);
        Assertions.assertEquals(17, map.size());

        Assertions.assertEquals(data, map.remove(2L));
        assertAVLT(map);
        Assertions.assertEquals(16, map.size());

        Assertions.assertEquals(data, map.remove(1L));
        assertAVLT(map);
        Assertions.assertEquals(15, map.size());

        Assertions.assertEquals(data, map.remove(5L));
        assertAVLT(map);
        Assertions.assertEquals(14, map.size());

        Assertions.assertEquals(data, map.remove(0L));
        assertAVLT(map);
        Assertions.assertEquals(13, map.size());

        Assertions.assertEquals(data, map.remove(11L));
        assertAVLT(map);
        Assertions.assertEquals(12, map.size());

        Assertions.assertEquals(data, map.remove(16L));
        assertAVLT(map);
        Assertions.assertEquals(11, map.size());

        Assertions.assertEquals(data, map.remove(7L));
        assertAVLT(map);
        Assertions.assertEquals(10, map.size());

        Assertions.assertEquals(data, map.remove(9L));
        assertAVLT(map);
        Assertions.assertEquals(9, map.size());

        Assertions.assertEquals(data, map.remove(13L));
        assertAVLT(map);
        Assertions.assertEquals(8, map.size());

        Assertions.assertEquals(data, map.remove(14L));
        assertAVLT(map);
        Assertions.assertEquals(7, map.size());

        Assertions.assertEquals(data, map.remove(8L));
        assertAVLT(map);
        Assertions.assertEquals(6, map.size());

        Assertions.assertEquals(data, map.remove(18L));
        assertAVLT(map);
        Assertions.assertEquals(5, map.size());

        Assertions.assertEquals(data, map.remove(12L));
        assertAVLT(map);
        Assertions.assertEquals(4, map.size());

        Assertions.assertEquals(data, map.remove(4L));
        assertAVLT(map);
        Assertions.assertEquals(3, map.size());

        Assertions.assertEquals(data, map.remove(6L));
        assertAVLT(map);
        Assertions.assertEquals(2, map.size());

        Assertions.assertEquals(data, map.remove(10L));
        assertAVLT(map);
        Assertions.assertEquals(1, map.size());

        Assertions.assertEquals(data, map.remove(3L));
        assertAVLT(map);
        Assertions.assertEquals(0, map.size());
    }
}
