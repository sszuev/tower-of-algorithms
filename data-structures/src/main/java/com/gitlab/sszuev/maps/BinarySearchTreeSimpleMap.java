package com.gitlab.sszuev.maps;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * A Binary Search Tree, and also an implementation of {@link SimpleMap}.
 * <p>
 * Created by @ssz on 21.09.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Binary_search_tree'>wiki</a>
 * @see <a href='https://www.cs.usfca.edu/~galles/visualization/BST.html'>visualization</a>
 */
public class BinarySearchTreeSimpleMap<K, V> implements SimpleMap<K, V> {
    protected final Comparator<K> comparator;
    protected BiNode<K, V> root;
    protected long size;

    public BinarySearchTreeSimpleMap() {
        this(null);
    }

    public BinarySearchTreeSimpleMap(Comparator<K> comparator) {
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

    @Override
    public V put(K key, V value) {
        if (root == null) {
            root(node(key, value));
            size++;
            return null;
        }
        BiNode<K, V> current = root;
        while (true) {
            int res = compare(key, current.key());
            if (res == 0) {
                // replace
                V v = current.value();
                current.value(value);
                return v;
            }
            if (res < 0) {
                BiNode<K, V> left = current.left();
                if (left == null) {
                    current.left(node(key, value));
                    size++;
                    afterInsert(current.left());
                    return null;
                } else {
                    current = left;
                }
                continue;
            }
            BiNode<K, V> right = current.right();
            if (right == null) {
                current.right(node(key, value));
                size++;
                afterInsert(current.right());
                return null;
            }
            current = right;
        }
    }

    @Override
    public V remove(K key) {
        BiNode<K, V> current = root;
        if (current == null) {
            return null;
        }
        BiNode<K, V> prev = null;
        while (true) {
            int res = compare(key, current.key());
            if (res == 0) {
                V value = current.value();
                BiNode<K, V> x = remove(current, prev);
                size--;
                afterRemove(x);
                return value;
            }
            if (res < 0) {
                BiNode<K, V> left = current.left();
                if (left != null) {
                    prev = current;
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
            prev = current;
            current = right;
        }
    }

    /**
     * Removes a {@code current} node and returns a node for possible rebalance.
     *
     * @param current {@link BiNode} - a node to remove
     * @param prev    {@link BiNode} - a parent of {@code current} node
     * @return {@link BiNode} to start rebalance
     */
    protected BiNode<K, V> remove(final BiNode<K, V> current, final BiNode<K, V> prev) {
        BiNode<K, V> currentLeft = current.left();
        BiNode<K, V> currentRight = current.right();
        if (currentLeft == null && currentRight == null) {
            replace(prev, current, null);
            return prev;
        }
        if (currentRight == null || currentLeft == null) {
            BiNode<K, V> child = currentLeft == null ? currentRight : currentLeft;
            replace(prev, current, child);
            return child;
        }
        BiNode<K, V> res = null;
        BiNode<K, V> found = findPrevMinInRightBranch(current);
        BiNode<K, V> replacement;
        if (found == null) {
            replacement = currentRight;
            currentRight = replacement.right();
        } else {
            replacement = found.left();
            replace(found, replacement, replacement.right());
            res = found;
        }
        replacement = node(replacement);
        replacement.left(currentLeft);
        replacement.right(currentRight);
        replace(prev, current, replacement);
        return res == null ? replacement : res;
    }

    /**
     * Finds a parent of minimal item in the right branch, which is not a root of subtree.
     *
     * @param current a {@link BiNode} whose right child is a root of considered subtree
     * @return {@link BiNode} or {@code null} if there is no left branch in a subtree
     */
    protected BiNode<K, V> findPrevMinInRightBranch(BiNode<K, V> current) {
        BiNode<K, V> prev = current.right();
        BiNode<K, V> left = prev.left();
        if (left == null) {
            return null;
        }
        while (left.left() != null) {
            prev = left;
            left = prev.left();
        }
        return prev;
    }

    protected void replace(BiNode<K, V> parent, BiNode<K, V> oldChild, BiNode<K, V> newChild) {
        if (parent == null) {
            root(newChild);
            return;
        }
        if (parent.left() == oldChild) {
            parent.left(newChild);
        }
        if (parent.right() == oldChild) {
            parent.right(newChild);
        }
    }

    protected void root(BiNode<K, V> newRoot) {
        root = newRoot;
    }

    protected void afterInsert(BiNode<K, V> node) {
    }

    protected void afterRemove(BiNode<K, V> node) {
    }

    private int compare(K left, K right) {
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
        return node(other.key, other.value);
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
