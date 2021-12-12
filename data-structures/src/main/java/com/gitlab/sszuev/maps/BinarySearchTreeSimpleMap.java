package com.gitlab.sszuev.maps;

import java.util.Comparator;

/**
 * A straightforward Binary Search Tree {@link SimpleMap Simple Map} implementation.
 * There is no rebalance mechanism.
 * <p>
 * Created by @ssz on 21.09.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Binary_search_tree'>wiki: Binary search tree</a>
 * @see <a href='https://www.cs.usfca.edu/~galles/visualization/BST.html'>visualization: Binary search tree</a>
 */
public class BinarySearchTreeSimpleMap<K, V> extends BaseBSTSimpleMap<K, V> {

    public BinarySearchTreeSimpleMap() {
        this(null);
    }

    public BinarySearchTreeSimpleMap(Comparator<K> comparator) {
        super(comparator);
    }

    @Override
    public V put(K key, V value) {
        if (root == null) {
            root(node(key, value));
            size++;
            return null;
        }
        BiNodeImpl<K, V> current = root;
        while (true) {
            int res = compare(key, current.key());
            if (res == 0) {
                // replace
                V v = current.value();
                current.value(value);
                return v;
            }
            if (res < 0) {
                BiNodeImpl<K, V> left = current.left();
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
            BiNodeImpl<K, V> right = current.right();
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
        BiNodeImpl<K, V> current = root;
        if (current == null) {
            return null;
        }
        BiNodeImpl<K, V> prev = null;
        while (true) {
            int res = compare(key, current.key());
            if (res == 0) {
                V value = current.value();
                BiNodeImpl<K, V> x = remove(current, prev);
                size--;
                afterRemove(x);
                return value;
            }
            if (res < 0) {
                BiNodeImpl<K, V> left = current.left();
                if (left != null) {
                    prev = current;
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
            prev = current;
            current = right;
        }
    }

    /**
     * Removes a {@code current} node and returns a node for possible rebalance.
     *
     * @param current {@link BiNodeImpl} - a node to remove
     * @param prev    {@link BiNodeImpl} - a parent of {@code current} node
     * @return {@link BiNodeImpl} to start rebalance
     */
    protected BiNodeImpl<K, V> remove(final BiNodeImpl<K, V> current, final BiNodeImpl<K, V> prev) {
        BiNodeImpl<K, V> currentLeft = current.left();
        BiNodeImpl<K, V> currentRight = current.right();
        if (currentLeft == null && currentRight == null) {
            replace(prev, current, null);
            return prev;
        }
        if (currentRight == null || currentLeft == null) {
            BiNodeImpl<K, V> child = currentLeft == null ? currentRight : currentLeft;
            replace(prev, current, child);
            return child;
        }
        BiNodeImpl<K, V> res = null;
        BiNodeImpl<K, V> found = findPrevMinInRightBranch(current);
        BiNodeImpl<K, V> replacement;
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
     * @param current a {@link BiNodeImpl} whose right child is a root of considered subtree
     * @return {@link BiNodeImpl} or {@code null} if there is no left branch in a subtree
     */
    protected BiNodeImpl<K, V> findPrevMinInRightBranch(BiNodeImpl<K, V> current) {
        BiNodeImpl<K, V> prev = current.right();
        BiNodeImpl<K, V> left = prev.left();
        if (left == null) {
            return null;
        }
        while (left.left() != null) {
            prev = left;
            left = prev.left();
        }
        return prev;
    }

    protected void replace(BiNodeImpl<K, V> parent, BiNodeImpl<K, V> oldChild, BiNodeImpl<K, V> newChild) {
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

    protected void afterInsert(BiNodeImpl<K, V> node) {
    }

    protected void afterRemove(BiNodeImpl<K, V> node) {
    }
}
