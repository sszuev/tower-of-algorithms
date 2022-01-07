package com.gitlab.sszuev.maps.trees;

import com.gitlab.sszuev.maps.SimpleMap;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Created by @ssz on 06.11.2021.
 */
abstract class BaseBSTSimpleMap<K, V> implements SimpleMap<K, V>, HasTreeRoot {
    protected final Comparator<K> comparator;
    protected BiNodeImpl<K, V> root;
    protected long size;

    public BaseBSTSimpleMap(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public BiNodeImpl<K, V> getRoot() {
        return root;
    }

    @Override
    public V get(K key) {
        BiNodeImpl<K, V> current = root;
        if (current == null) {
            return null;
        }
        while (true) {
            int res = compare(key, current.key());
            if (res == 0) {
                return current.value();
            }
            if (res < 0) {
                BiNodeImpl<K, V> left = current.left();
                if (left != null) {
                    current = left;
                } else {
                    return null;
                }
                continue;
            }
            BiNodeImpl<K, V> right = current.right();
            if (right == null) {
                return null;
            }
            current = right;
        }
    }

    protected void root(BiNodeImpl<K, V> newRoot) {
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

    protected BiNodeImpl<K, V> node(BiNodeImpl<K, V> other) {
        return node(other.key(), other.value());
    }

    protected BiNodeImpl<K, V> node(K key, V value) {
        BiNodeImpl<K, V> res = new BiNodeImpl<>(key);
        res.value(value);
        return res;
    }

    /**
     * Created by @ssz on 21.09.2021.
     */
    public static class BiNodeImpl<K, V> implements BiNode<K> {
        private final K key;
        private BiNodeImpl<K, V> left, right;
        private V value;

        protected BiNodeImpl(K key) {
            this.key = Objects.requireNonNull(key);
        }

        protected void value(V value) {
            this.value = value;
        }

        @Override
        public BiNodeImpl<K, V> left() {
            return left;
        }

        @Override
        public BiNodeImpl<K, V> right() {
            return right;
        }

        protected void right(BiNodeImpl<K, V> right) {
            this.right = right;
        }

        protected void left(BiNodeImpl<K, V> left) {
            this.left = left;
        }

        @Override
        public Stream<BiNodeImpl<K, V>> children() {
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
