package com.gitlab.sszuev.maps;

import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A helper to work with {@link TreeNode}.
 * Created by @ssz on 22.09.2021.
 */
public class TreeMapUtils {

    private static final TreeNode EMPTY = Stream::empty;

    /**
     * Represents a {@code SimpleMap} as a {@code String}.
     *
     * @param map {@link SimpleMap}
     * @return {@code String}
     */
    public static String print(SimpleMap<?, ?> map) {
        if (map instanceof HasTreeRoot) {
            return print(((HasTreeRoot) map).getRoot());
        }
        return map.toString();
    }

    public static String print(TreeNode root) {
        return root == null ? "(empty)" : doPrint(root).toString();
    }

    public static long size(BiNode<?> node) {
        return node == null ? 0 : node.size();
    }

    public static long size(MultiNode<?> node) {
        return node == null ? 0 : node.size();
    }

    public static <X> boolean isBST(BiNode<X> node, Comparator<X> comp) {
        return isBST(node, null, null, comp);
    }

    private static <X> boolean isBST(BiNode<X> root, BiNode<X> min, BiNode<X> max, Comparator<X> comp) {
        if (root == null) {
            return true;
        }
        X data = root.key();
        if ((min != null && comp.compare(data, min.key()) <= 0) || (max != null && comp.compare(data, max.key()) > 0)) {
            return false;
        }
        return isBST(root.left(), min, root, comp) && isBST(root.right(), root, max, comp);
    }

    static <X> boolean hasSortedKeys(MultiNode<X> node, Comparator<X> comp) {
        if (node == null) {
            return true;
        }
        List<X> keys = node.keys().collect(Collectors.toList());
        if (keys.isEmpty()) {
            return false;
        }
        X prev = null;
        for (X current : keys) {
            if (prev != null) {
                if (comp.compare(prev, current) > 0) {
                    return false;
                }
            }
            prev = current;
        }
        return node.children().allMatch(it -> hasSortedKeys(it, comp));
    }

    private static StringBlock doPrint(TreeNode root) {
        StringBlock res = new StringBlock();
        List<TreeNode> children = root.children().collect(Collectors.toList());
        if (!children.stream().allMatch(Objects::isNull))
            children.stream().map(x -> x == null ? EMPTY : x).forEach(x -> res.addBlock(doPrint(x)));
        res.addLine(toStringOrNull(root));
        return res;
    }

    private static String toStringOrNull(TreeNode node) {
        if (EMPTY == node) {
            return null;
        }
        return node instanceof BiNode ? String.valueOf(((BiNode<?>) node).key()) : node.toString();
    }

    private static String withSpaces(String space, int length) {
        return IntStream.rangeClosed(0, length).mapToObj(x -> "").collect(Collectors.joining(space));
    }

    private static String withSpaces(String str, @SuppressWarnings("SameParameterValue") String space, int length) {
        if (str == null) {
            return withSpaces(space, length);
        }
        if (str.length() == length) {
            return str;
        }
        if (str.length() > length) {
            throw new IllegalArgumentException();
        }
        int left = (length - str.length()) / 2;
        int right = length - left - str.length();
        return withSpaces(space, left) + str + withSpaces(space, right);
    }

    public static <K, V> void assertBST(SimpleMap<K, V> map) {
        if (!(map instanceof BaseBSTSimpleMap)) {
            return;
        }
        BaseBSTSimpleMap<K, V> bstSimpleMap = (BaseBSTSimpleMap<K, V>) map;
        BiNode<K> root = bstSimpleMap.getRoot();
        System.out.println(print(root));
        Assertions.assertTrue(isBST(root, getComparator(bstSimpleMap)), "Not a BST");
        Assertions.assertEquals(size(root), map.size(), "Wrong size");
        System.out.println("-".repeat(42));
    }

    public static <K, V> void assertBTree(SimpleMap<K, V> map) {
        if (!(map instanceof BTreeSimpleMap)) {
            return;
        }
        BTreeSimpleMap<K, V> bTreeSimpleMap = (BTreeSimpleMap<K, V>) map;
        BTreeSimpleMap.BNodeImpl<K, V> root = bTreeSimpleMap.getRoot();
        System.out.println(print(root));
        assertBNode(bTreeSimpleMap);
        assertParents(root);
        Assertions.assertEquals(size(root), map.size(), "Wrong size");
        System.out.println("-".repeat(42));
    }

    static <X> void assertBNode(BTreeSimpleMap<X, ?> map) {
        Comparator<X> comp = getComparator(map);
        Assertions.assertTrue(hasSortedKeys(map.getRoot(), comp));
        assertBNode(map.getRoot(), comp);
    }

    static <X> void assertBNode(BTreeSimpleMap.BNodeImpl<X, ?> node, Comparator<X> comp) {
        if (node == null) {
            return;
        }
        List<BTreeSimpleMap.BNodeImpl<X, ?>> children = new ArrayList<>();
        BTreeSimpleMap.BNodeImpl<X, ?> left = node.left();
        if (left != null) {
            children.add(left);
            X leftKey = left.items()[left.lastIndex()].key();
            Assertions.assertTrue(comp.compare(leftKey, node.items()[0].key()) < 0);
        }
        for (int i = 0; i <= node.lastIndex(); i++) {
            BTreeSimpleMap.BNodeImpl<X, ?> right = node.right(i);
            if (left == null) {
                Assertions.assertNull(right);
                continue;
            }
            Assertions.assertNotNull(right);
            children.add(right);
            X rightKey = right.items()[0].key();
            Assertions.assertTrue(comp.compare(rightKey, node.items()[i].key()) > 0);
        }
        children.forEach(child -> {
            Assertions.assertNotSame(child, node);
            Assertions.assertSame(node, child.parent(), "Wrong parend for " + child);
            assertBNode(child, comp);
        });
    }

    static <X> Comparator<X> getComparator(BTreeSimpleMap<X, ?> map) {
        Comparator<X> comp = map.comparator;
        if (comp == null) {
            comp = naturalComparator();
        }
        return comp;
    }

    static <X> Comparator<X> getComparator(BaseBSTSimpleMap<X, ?> map) {
        Comparator<X> comp = map.comparator;
        if (comp == null) {
            comp = naturalComparator();
        }
        return comp;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static <X> Comparator<X> naturalComparator() {
        return (o1, o2) -> ((Comparable) o1).compareTo(o2);
    }

    static void assertParents(BTreeSimpleMap.BNodeImpl<?, ?> root) {
        if (root == null) {
            return;
        }
        Assertions.assertNull(root.parent());
        Assertions.assertTrue(root.children()
                .map(x -> (BTreeSimpleMap.BNodeImpl<?, ?>) x).allMatch(TreeMapUtils::hasParent));
    }

    private static boolean hasParent(BTreeSimpleMap.BNodeImpl<?, ?> node) {
        if (node.parent() == null) {
            return false;
        }
        return node.children().map(x -> (BTreeSimpleMap.BNodeImpl<?, ?>) x).allMatch(TreeMapUtils::hasParent);
    }

    private static class StringBlock {
        private static final String SPACE = " ";
        private static final String TAB = SPACE + SPACE;
        private static final String NIL = "x";
        private final List<String> lines = new ArrayList<>();

        void addLine(String v) {
            String s;
            if (lines.isEmpty()) {
                s = SPACE + (v == null ? NIL : v) + SPACE;
            } else {
                s = withSpaces(v, SPACE, length());
            }
            lines.add(0, s);
        }

        void addBlock(StringBlock other) {
            int l2 = other.length();
            int s = Math.max(lines.size(), other.lines.size());
            other.formatBlock(s, l2);
            if (lines.isEmpty()) {
                this.lines.addAll(other.lines);
                return;
            }
            int l1 = length();
            formatBlock(s, l1);
            for (int i = 0; i < s; i++) {
                lines.set(i, lines.get(i) + TAB + other.lines.get(i));
            }
        }

        private void formatBlock(int size, int length) {
            int s = lines.size();
            for (int i = 0; i < s; i++) {
                String t = lines.get(i);
                lines.set(i, withSpaces(t, SPACE, length));
            }
            for (int i = 0; i < size - s; i++) {
                lines.add(withSpaces(SPACE, length));
            }
        }

        private int length() {
            return lines.stream().mapToInt(String::length).max().orElse(0);
        }

        @Override
        public String toString() {
            return String.join("\n", lines);
        }
    }
}
