package com.gitlab.sszuev.maps;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Created by @ssz on 06.11.2021.
 */
abstract class BaseBSTSimpleMap<K, V> implements SimpleMap<K, V> {
    protected final Comparator<K> comparator;
    protected BiNode<K, V> root;
    protected long size;

    public BaseBSTSimpleMap(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public V get(K key) {
        BiNode<K, V> current = root;
        if (current == null) {
            return null;
        }
        while (true) {
            int res = compare(key, current.key());
            if (res == 0) {
                return current.value();
            }
            if (res < 0) {
                BiNode<K, V> left = current.left();
                if (left != null) {
                    current = left;
                } else {
                    return null;
                }
                continue;
            }
            BiNode<K, V> right = current.right();
            if (right == null) {
                return null;
            }
            current = right;
        }
    }

    protected void root(BiNode<K, V> newRoot) {
        root = newRoot;
    }

    protected int compare(K left, K right) {
        if (left == right) {
            return 0;
        }
        if (comparator != null) {
            return comparator.compare(left, right);
        }
        return asComparable(left).compareTo(right);
    }

    @SuppressWarnings("unchecked")
    private Comparable<? super K> asComparable(K key) {
        return (Comparable<? super K>) key;
    }

    protected BiNode<K, V> node(BiNode<K, V> other) {
        return node(other.key(), other.value());
    }

    protected BiNode<K, V> node(K key, V value) {
        BiNode<K, V> res = new BiNode<>(key);
        res.value(value);
        return res;
    }

    /**
     * Created by @ssz on 21.09.2021.
     */
    public static class BiNode<K, V> implements TreeNode<K> {
        private final K key;
        private BiNode<K, V> left, right;
        private V value;

        protected BiNode(K key) {
            this.key = Objects.requireNonNull(key);
        }

        protected void value(V value) {
            this.value = value;
        }

        public BiNode<K, V> left() {
            return left;
        }

        public BiNode<K, V> right() {
            return right;
        }

        protected void right(BiNode<K, V> right) {
            this.right = right;
        }

        protected void left(BiNode<K, V> left) {
            this.left = left;
        }

        @Override
        public Stream<TreeNode<K>> children() {
            return Stream.of(left, right);
        }

        @Override
        public K key() {
            return key;
        }

        public V value() {
            return value;
        }

        @Override
        public String toString() {
            if (left == null && right == null) {
                return "(" + key + ")";
            }
            return String.format("(%s)[%s, %s]", key, left, right);
        }
    }
}
