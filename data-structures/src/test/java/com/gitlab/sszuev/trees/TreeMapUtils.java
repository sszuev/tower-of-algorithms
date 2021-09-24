package com.gitlab.sszuev.trees;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * A helper to work with {@link TreeNode}.
 * Created by @ssz on 22.09.2021.
 */
public class TreeMapUtils {

    private static final TreeNode<?> EMPTY = new TreeNode<>() {
        @Override
        public Stream<TreeNode<Object>> children() {
            return Stream.empty();
        }

        @Override
        public Object key() {
            return null;
        }
    };

    @SuppressWarnings("unchecked")
    public static <X> TreeNode<X> createEmpty() {
        return (TreeNode<X>) EMPTY;
    }

    public static String print(SimpleMap<?, ?> map) {
        if (map instanceof BSTSimpleMap) {
            return print(((BSTSimpleMap<?, ?>) map).root);
        }
        return map.toString();
    }

    public static String print(TreeNode<?> root) {
        return root == null ? "(empty)" : doPrint(root).toString();
    }

    public static long size(BSTSimpleMap.BiNode<?, ?> node) {
        return node == null ? 0 : node.size();
    }

    public static <X extends Comparable<X>, V> boolean isBST(BSTSimpleMap.BiNode<X, V> node) {
        return isBST(node, null, null);
    }

    private static <X extends Comparable<X>, V> boolean isBST(BSTSimpleMap.BiNode<X, V> root,
                                                              BSTSimpleMap.BiNode<X, V> min,
                                                              BSTSimpleMap.BiNode<X, V> max) {
        if (root == null)
            return true;
        X data = root.key();
        if ((min != null && data.compareTo(min.key()) <= 0) || (max != null && data.compareTo(max.key()) > 0)) {
            return false;
        }
        return isBST(root.left(), min, root) && isBST(root.right(), root, max);
    }

    private static <X> StringBlock doPrint(TreeNode<X> root) {
        StringBlock res = new StringBlock();
        List<TreeNode<X>> children = root.children().collect(Collectors.toList());
        if (!children.stream().allMatch(Objects::isNull))
            children.stream().map(x -> x == null ? createEmpty() : x).forEach(x -> res.addBlock(doPrint(x)));
        res.addLine(Optional.ofNullable(root.key()).map(Object::toString).orElse(null));
        return res;
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
