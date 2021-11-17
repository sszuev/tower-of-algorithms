package com.gitlab.sszuev.maps;

import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
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

    public static <X extends Comparable<X>, V> boolean isBST(BaseBSTSimpleMap.BiNodeImpl<X, V> node) {
        return isBST(node, null, null);
    }

    private static <X extends Comparable<X>> boolean isBST(BiNode<X> root, BiNode<X> min, BiNode<X> max) {
        if (root == null)
            return true;
        X data = root.key();
        if ((min != null && data.compareTo(min.key()) <= 0) || (max != null && data.compareTo(max.key()) > 0)) {
            return false;
        }
        return isBST(root.left(), min, root) && isBST(root.right(), root, max);
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

    public static <K extends Comparable<K>, V> void assertBST(SimpleMap<K, V> map) {
        if (!(map instanceof BaseBSTSimpleMap)) {
            return;
        }
        BaseBSTSimpleMap.BiNodeImpl<K, V> root = ((BaseBSTSimpleMap<K, V>) map).getRoot();
        System.out.println(print(root));
        Assertions.assertTrue(isBST(root), "Not a BST");
        Assertions.assertEquals(size(root), map.size(), "Wrong size");
        System.out.println("-".repeat(42));
    }

    public static <K extends Comparable<K>, V> void assertBTree(SimpleMap<K, V> map) {
        if (!(map instanceof BTreeSimpleMap)) {
            return;
        }
        BTreeSimpleMap.BNodeImpl<K, V> root = ((BTreeSimpleMap<K, V>) map).getRoot();
        System.out.println(print(root));
        Assertions.assertEquals(size(root), map.size(), "Wrong size");
        System.out.println("-".repeat(42));
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
