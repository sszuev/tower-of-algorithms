package com.gitlab.sszuev.maps;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

/**
 * Created by @ssz on 22.09.2021.
 */
public class SimpleMapTest {

    @ParameterizedTest
    @EnumSource(TestType.class)
    public void testStringMapInsert(TestType type) {
        SimpleMap<String, Integer> map = type.create();
        Assertions.assertNull(map.get("A"));

        Assertions.assertNull(map.put("A", -21));
        type.assertTree(map);

        Assertions.assertNull(map.put("b", 25));
        type.assertTree(map);

        Assertions.assertNull(map.put("C", 25));
        type.assertTree(map);

        Assertions.assertNull(map.put("d", 125));
        type.assertTree(map);

        Assertions.assertNull(map.put("42", 21));
        type.assertTree(map);

        Assertions.assertEquals(-21, map.put("A", 42));
        type.assertTree(map);

        Assertions.assertEquals(5, map.size());

        Assertions.assertEquals(42, map.get("A"));
        Assertions.assertEquals(125, map.get("d"));
        Assertions.assertEquals(25, map.get("b"));
        Assertions.assertEquals(25, map.get("C"));
        Assertions.assertEquals(21, map.get("42"));
    }

    @ParameterizedTest
    @EnumSource(TestType.class)
    public void testStringMapInsertAndRemoveNilItem(TestType type) {
        SimpleMap<String, Boolean> map = type.create();
        Assertions.assertNull(map.remove("X"));
        map.put("X", Boolean.TRUE);
        type.assertTree(map);
        Assertions.assertEquals(1, map.size());

        Assertions.assertNull(map.remove("Y"));
        Assertions.assertTrue(map.remove("X"));
        type.assertTree(map);
        Assertions.assertEquals(0, map.size());
        type.assertTree(map);

        map.put("f", Boolean.TRUE);
        map.put("g", Boolean.TRUE);
        map.put("h", Boolean.TRUE);
        map.put("c", Boolean.TRUE);
        map.put("d", Boolean.TRUE);

        Assertions.assertEquals(5, map.size());
        type.assertTree(map);

        Assertions.assertTrue(map.remove("h"));
        Assertions.assertTrue(map.remove("d"));
        type.assertTree(map);
        Assertions.assertEquals(3, map.size());


        Assertions.assertTrue(map.remove("c"));
        Assertions.assertTrue(map.remove("g"));
        type.assertTree(map);
        Assertions.assertEquals(1, map.size());

        Assertions.assertTrue(map.remove("f"));
        type.assertTree(map);
        Assertions.assertEquals(0, map.size());
    }

    @ParameterizedTest
    @EnumSource(TestType.class)
    public void testLongMapRemoveItemWithSingleChild(TestType type) {
        SimpleMap<Long, Boolean> map = type.create();
        map.put(40L, Boolean.TRUE);
        map.put(50L, Boolean.TRUE);
        map.put(60L, Boolean.TRUE);
        map.put(30L, Boolean.TRUE);
        map.put(35L, Boolean.TRUE);
        map.put(65L, Boolean.TRUE);

        Assertions.assertEquals(6, map.size());
        type.assertTree(map);

        Assertions.assertNull(map.remove(42L));

        Assertions.assertTrue(map.remove(60L));
        type.assertTree(map);
        Assertions.assertEquals(5, map.size());

        Assertions.assertTrue(map.remove(30L));
        type.assertTree(map);
        Assertions.assertEquals(4, map.size());

        Assertions.assertTrue(map.remove(35L));
        type.assertTree(map);
        Assertions.assertEquals(3, map.size());

        Assertions.assertTrue(map.remove(50L));
        type.assertTree(map);
        Assertions.assertEquals(2, map.size());

        Assertions.assertTrue(map.remove(40L));
        type.assertTree(map);
        Assertions.assertEquals(1, map.size());
    }

    @ParameterizedTest
    @EnumSource(TestType.class)
    public void testIntegerMapRemoveItemWithTwoChildren(TestType type) {
        SimpleMap<Integer, Integer> map = type.create();
        map.put(40, 0);
        type.assertTree(map);
        map.put(50, 1);
        type.assertTree(map);
        map.put(55, 2);
        type.assertTree(map);
        map.put(60, 3);
        type.assertTree(map);
        map.put(20, 1);
        type.assertTree(map);
        map.put(10, 2);
        type.assertTree(map);
        map.put(36, 2);
        type.assertTree(map);
        map.put(45, 2);
        type.assertTree(map);
        map.put(8, 3);
        type.assertTree(map);
        map.put(12, 3);
        type.assertTree(map);
        map.put(30, 3);
        type.assertTree(map);
        map.put(16, 4);
        type.assertTree(map);
        map.put(22, 4);
        type.assertTree(map);
        map.put(28, 5);
        type.assertTree(map);

        type.assertTree(map);
        Assertions.assertEquals(14, map.size());

        Assertions.assertEquals(1, map.remove(20));
        type.assertTree(map);
        Assertions.assertEquals(13, map.size());

        Assertions.assertEquals(0, map.remove(40));
        type.assertTree(map);
        Assertions.assertEquals(12, map.size());

        Assertions.assertEquals(2, map.remove(45));
        type.assertTree(map);
        Assertions.assertEquals(11, map.size());

        Assertions.assertEquals(2, map.remove(10));
        type.assertTree(map);
        Assertions.assertEquals(10, map.size());

        Assertions.assertEquals(4, map.remove(22));
        type.assertTree(map);
        Assertions.assertEquals(9, map.size());

        Assertions.assertEquals(5, map.remove(28));
        type.assertTree(map);
        Assertions.assertEquals(8, map.size());

        Assertions.assertEquals(3, map.remove(30));
        type.assertTree(map);
        Assertions.assertEquals(7, map.size());

        Assertions.assertEquals(1, map.remove(50));
        type.assertTree(map);
        Assertions.assertEquals(6, map.size());

        Assertions.assertEquals(3, map.remove(12));
        type.assertTree(map);
        Assertions.assertEquals(5, map.size());

        Assertions.assertEquals(2, map.remove(55));
        type.assertTree(map);
        Assertions.assertEquals(4, map.size());
    }

    @ParameterizedTest
    @EnumSource(TestType.class)
    public void testLongMapInsertSequentialDataAndRemove(TestType type) {
        Object value = "X";
        SimpleMap<Long, Object> map = type.create();
        for (long i = 0; i < 20; i++) {
            map.put(i, value);
        }
        type.assertTree(map);

        Assertions.assertEquals(value, map.remove(15L));
        type.assertTree(map);
        Assertions.assertEquals(19, map.size());

        Assertions.assertEquals(value, map.remove(19L));
        type.assertTree(map);
        Assertions.assertEquals(18, map.size());

        Assertions.assertEquals(value, map.remove(17L));
        type.assertTree(map);
        Assertions.assertEquals(17, map.size());

        Assertions.assertEquals(value, map.remove(2L));
        type.assertTree(map);
        Assertions.assertEquals(16, map.size());

        Assertions.assertEquals(value, map.remove(1L));
        type.assertTree(map);
        Assertions.assertEquals(15, map.size());

        Assertions.assertEquals(value, map.remove(5L));
        type.assertTree(map);
        Assertions.assertEquals(14, map.size());

        Assertions.assertEquals(value, map.remove(0L));
        type.assertTree(map);
        Assertions.assertEquals(13, map.size());

        Assertions.assertEquals(value, map.remove(11L));
        type.assertTree(map);
        Assertions.assertEquals(12, map.size());

        Assertions.assertEquals(value, map.remove(16L));
        type.assertTree(map);
        Assertions.assertEquals(11, map.size());

        Assertions.assertEquals(value, map.remove(7L));
        type.assertTree(map);
        Assertions.assertEquals(10, map.size());

        Assertions.assertEquals(value, map.remove(9L));
        type.assertTree(map);
        Assertions.assertEquals(9, map.size());

        Assertions.assertEquals(value, map.remove(13L));
        type.assertTree(map);
        Assertions.assertEquals(8, map.size());

        Assertions.assertEquals(value, map.remove(14L));
        type.assertTree(map);
        Assertions.assertEquals(7, map.size());

        Assertions.assertEquals(value, map.remove(8L));
        type.assertTree(map);
        Assertions.assertEquals(6, map.size());

        Assertions.assertEquals(value, map.remove(18L));
        type.assertTree(map);
        Assertions.assertEquals(5, map.size());

        Assertions.assertEquals(value, map.remove(12L));
        type.assertTree(map);
        Assertions.assertEquals(4, map.size());

        Assertions.assertEquals(value, map.remove(4L));
        type.assertTree(map);
        Assertions.assertEquals(3, map.size());

        Assertions.assertEquals(value, map.remove(6L));
        type.assertTree(map);
        Assertions.assertEquals(2, map.size());

        Assertions.assertEquals(value, map.remove(10L));
        type.assertTree(map);
        Assertions.assertEquals(1, map.size());

        Assertions.assertEquals(value, map.remove(3L));
        type.assertTree(map);
        Assertions.assertEquals(0, map.size());
    }

    @ParameterizedTest
    @EnumSource(TestType.class)
    public void testIntegerMapFixedDelete1(TestType type) {
        SimpleMap<Integer, Object> map = type.create();
        map.put(326, 0);

        map.put(170, 1);
        map.put(346, 1);

        map.put(65, 2);
        map.put(192, 2);
        map.put(380, 2);

        map.put(310, 3);
        map.put(348, 3);
        map.put(399, 3);

        map.put(253, 4);

        type.assertTree(map);

        for (int i : new int[]{326, 170, 380, 253, 65, 192, 346, 310, 348, 399}) {
            System.out.printf("REMOVE [%d]%n", i);
            Assertions.assertNotNull(map.remove(i));
            type.assertTree(map);
        }
        Assertions.assertEquals(0, map.size());
    }

    @ParameterizedTest
    @EnumSource(TestType.class)
    public void testIntegerMapFixedDelete2(TestType type) {
        SimpleMap<Integer, Object> map = type.create();
        map.put(245, 0);
        map.put(91, 1);
        map.put(316, 1);

        map.put(71, 2);
        map.put(210, 2);
        map.put(290, 2);
        map.put(346, 2);

        map.put(44, 3);
        map.put(192, 3);
        map.put(218, 3);
        map.put(280, 3);
        map.put(304, 3);
        map.put(397, 3);

        map.put(121, 4);
        map.put(297, 4);
        map.put(305, 4);

        type.assertTree(map);

        for (int i : new int[]{245, 280, 121, 44, 71, 397, 290, 91, 210, 297, 346, 305, 316, 304, 192, 218}) {
            System.out.printf("REMOVE [%d]%n", i);
            Assertions.assertNotNull(map.remove(i));
            type.assertTree(map);
        }
        Assertions.assertEquals(0, map.size());
    }

    @ParameterizedTest
    @EnumSource(TestType.class)
    public void testLongMapFixedDelete3(TestType type) {
        Object value = "XXX";
        SimpleMap<Long, Object> map = type.create();
        LongStream.of(
                299L, 302L, 190L, 295L, 369L, 156L, 326L, 194L, 236L, 296L, 239L, 310L, 292L, 148L,
                161L, 182L, 137L, 371L, 353L, 126L, 387L, 132L, 128L, 192L, 149L, 28L, 93L, 77L,
                350L, 196L, 274L, 318L, 248L, 117L, 234L, 341L, 312L, 46L, 84L, 277L, 342L, 315L
        ).forEach(k -> map.put(k, value));

        type.assertTree(map);

        LongStream.of(
                369L, 156L, 194L, 296L, 310L, 148L, 182L, 137L, 371L, 128L, 192L, 28L, 93L, 350L,
                46, 77, 84, 117, 126, 132, 149, 161, 190, 196, 234, 236, 239, 248, 274, 277, 292,
                295, 299, 302, 312, 315, 318, 326, 341, 342, 353, 387
        ).forEach(i -> {
            System.out.printf("REMOVE [%d]%n", i);
            Assertions.assertNotNull(map.remove(i));
            type.assertTree(map);
        });
        System.out.println(map);
        Assertions.assertEquals(0, map.size());
    }

    @ParameterizedTest
    @EnumSource(TestType.class)
    public void testIntegerMapInsertRemoveRandomData(TestType type) {
        Object value = "X";
        int maxKey = 420;
        int addSize = 42;
        int removeSize = addSize / 2;

        Random r = new Random();
        List<Integer> add = IntStream.generate(() -> r.nextInt(maxKey))
                .distinct().limit(addSize).boxed()
                .collect(Collectors.toList());

        List<Integer> remove = new ArrayList<>(add);
        while (remove.size() > removeSize) {
            remove.remove(r.nextInt(remove.size()));
        }

        SimpleMap<Integer, Object> map = type.create();
        add.forEach(key -> {
            System.out.printf("ADD [%d]%n", key);
            map.put(key, value);
            type.assertTree(map);
        });

        remove.forEach(key -> {
            System.out.printf("REMOVE [%d]%n", key);
            Assertions.assertEquals(value, map.remove(key));
            type.assertTree(map);
        });
    }

    enum TestType {
        SIMPLE_BST {
            @Override
            public <K, V> SimpleMap<K, V> create() {
                return new BinarySearchTreeSimpleMap<>();
            }

            @Override
            public <K extends Comparable<K>, V> void assertTree(SimpleMap<K, V> map) {
                Assertions.assertEquals(BinarySearchTreeSimpleMap.class, map.getClass());
                TreeMapUtils.assertBST(map);
            }
        },
        AVL_BST {
            @Override
            public <K, V> SimpleMap<K, V> create() {
                return new AVLBinarySearchTreeSimpleMap<>();
            }

            @Override
            public <K extends Comparable<K>, V> void assertTree(SimpleMap<K, V> map) {
                Assertions.assertEquals(AVLBinarySearchTreeSimpleMap.class, map.getClass());
                TreeMapUtils.assertBST(map);
                AVLBinarySearchTreeSimpleMap.AVLBiNode<K, V> root = getRoot(((AVLBinarySearchTreeSimpleMap<K, V>) map));
                if (root == null) {
                    return;
                }
                AssertionError error = new AssertionError("Three is not balanced");
                root.preOrder(node -> {
                    AVLBinarySearchTreeSimpleMap.AVLBiNode<K, V> n = asAVL(node);
                    int leftHeight = AVLBinarySearchTreeSimpleMap.AVLBiNode.height(n.left());
                    int rightHeight = AVLBinarySearchTreeSimpleMap.AVLBiNode.height(n.right());
                    if (Math.abs(rightHeight - leftHeight) > 1) {
                        throw error;
                    }
                });
            }

            private <K, V> AVLBinarySearchTreeSimpleMap.AVLBiNode<K, V> getRoot(AVLBinarySearchTreeSimpleMap<K, V> map) {
                return asAVL(map.root);
            }

            @SuppressWarnings("unchecked")
            private <K, V> AVLBinarySearchTreeSimpleMap.AVLBiNode<K, V> asAVL(TreeNode<?> node) {
                return (AVLBinarySearchTreeSimpleMap.AVLBiNode<K, V>) node;
            }
        },
        JDK_TREE_MAP {
            @SuppressWarnings("SortedCollectionWithNonComparableKeys")
            @Override
            public <K, V> SimpleMap<K, V> create() {
                return new JDKMapWrapperSimpleMap<>(new TreeMap<>());
            }
        },

        SC_HASHTABLE {
            @Override
            public <K, V> SimpleMap<K, V> create() {
                return new SeparateChainingHashtableSimpleMap<>();
            }
        };

        public abstract <K, V> SimpleMap<K, V> create();

        public <K extends Comparable<K>, V> void assertTree(SimpleMap<K, V> map) {
        }
    }
}