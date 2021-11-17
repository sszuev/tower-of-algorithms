package com.gitlab.sszuev.maps;

import java.util.Comparator;

/**
 * A Binary Search Tree with AVL-rebalance, and also an implementation of {@link SimpleMap}.
 * Created by @ssz on 25.09.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/AVL_tree'>wiki</a>
 * @see <a href='https://www.cs.usfca.edu/~galles/visualization/AVLtree.html'>visualization</a>
 */
public class AVLBinarySearchTreeSimpleMap<K, V> extends BinarySearchTreeSimpleMap<K, V> {

    public AVLBinarySearchTreeSimpleMap() {
        this(null);
    }

    public AVLBinarySearchTreeSimpleMap(Comparator<K> comparator) {
        super(comparator);
    }

    @Override
    protected void afterInsert(BiNodeImpl<K, V> node) {
        AVLBiNodeImpl<K, V> n = AVLBiNodeImpl.asAVL(node);
        AVLBiNodeImpl.reset(n);
        rebalance(n);
    }

    @Override
    protected void afterRemove(BiNodeImpl<K, V> node) {
        AVLBiNodeImpl<K, V> n = AVLBiNodeImpl.asAVL(node);
        AVLBiNodeImpl.reset(n);
        rebalance(n);
    }

    public void rebalance(AVLBiNodeImpl<K, V> node) {
        while (node != null) {
            if (needRebalance(node)) {
                node = performRebalance(node);
                AVLBiNodeImpl.reset(node);
            }
            node = node.parent();
        }
    }

    protected boolean needRebalance(AVLBiNodeImpl<K, V> node) {
        if (node == null) {
            return false;
        }
        int leftHeight = AVLBiNodeImpl.height(node.left());
        int rightHeight = AVLBiNodeImpl.height(node.right());
        return Math.abs(leftHeight - rightHeight) > 1;
    }

    protected AVLBiNodeImpl<K, V> performRebalance(final AVLBiNodeImpl<K, V> a) {
        AVLBiNodeImpl<K, V> b = a.right();

        AVLBiNodeImpl<K, V> c = AVLBiNodeImpl.leftOrNull(b);
        AVLBiNodeImpl<K, V> r = AVLBiNodeImpl.rightOrNull(b);
        AVLBiNodeImpl<K, V> l = AVLBiNodeImpl.leftOrNull(a);

        if (AVLBiNodeImpl.height(b) - AVLBiNodeImpl.height(l) == 2) {
            if (AVLBiNodeImpl.height(c) <= AVLBiNodeImpl.height(r)) {
                return smallLeftRotation(a);
            } else {
                return bigLeftRotation(a);
            }
        }

        b = a.left();
        c = AVLBiNodeImpl.rightOrNull(b);
        r = AVLBiNodeImpl.rightOrNull(a);
        l = AVLBiNodeImpl.leftOrNull(b);

        if (AVLBiNodeImpl.height(b) - AVLBiNodeImpl.height(r) == 2) {
            if (AVLBiNodeImpl.height(c) <= AVLBiNodeImpl.height(l)) {
                return smallRightRotation(a);
            } else {
                return bigRightRotation(a);
            }
        }
        throw new IllegalStateException();
    }

    public AVLBiNodeImpl<K, V> smallLeftRotation(AVLBiNodeImpl<K, V> a) {
        AVLBiNodeImpl<K, V> p = a.parent();

        AVLBiNodeImpl<K, V> b = a.right();
        AVLBiNodeImpl<K, V> c = b.left();

        a.right(c);
        b.left(a);

        replace(p, a, b);
        return b;
    }

    public AVLBiNodeImpl<K, V> bigLeftRotation(AVLBiNodeImpl<K, V> a) {
        AVLBiNodeImpl<K, V> p = a.parent();

        AVLBiNodeImpl<K, V> b = a.right();
        AVLBiNodeImpl<K, V> c = b.left();
        AVLBiNodeImpl<K, V> m = c.left();
        AVLBiNodeImpl<K, V> n = c.right();

        a.right(m);
        b.left(n);
        c.left(a);
        c.right(b);

        replace(p, a, c);
        return c;
    }

    public AVLBiNodeImpl<K, V> smallRightRotation(AVLBiNodeImpl<K, V> a) {
        AVLBiNodeImpl<K, V> p = a.parent();

        AVLBiNodeImpl<K, V> b = a.left();
        AVLBiNodeImpl<K, V> c = b.right();

        b.right(a);
        a.left(c);

        replace(p, a, b);
        return b;
    }

    public AVLBiNodeImpl<K, V> bigRightRotation(AVLBiNodeImpl<K, V> a) {
        AVLBiNodeImpl<K, V> p = a.parent();

        AVLBiNodeImpl<K, V> b = a.left();
        AVLBiNodeImpl<K, V> c = b.right();
        AVLBiNodeImpl<K, V> m = c.left();
        AVLBiNodeImpl<K, V> n = c.right();

        b.right(m);
        a.left(n);
        c.right(a);
        c.left(b);

        replace(p, a, c);
        return c;
    }

    @Override
    protected void root(BiNodeImpl<K, V> newRoot) {
        AVLBiNodeImpl<K, V> root = AVLBiNodeImpl.asAVL(newRoot);
        super.root(root);
        if (root != null) {
            root.parent(null);
            root.reset();
        }
    }

    @Override
    protected AVLBiNodeImpl<K, V> node(BiNodeImpl<K, V> other) {
        return node(other.key(), other.value());
    }

    @Override
    protected AVLBiNodeImpl<K, V> node(K key, V value) {
        AVLBiNodeImpl<K, V> res = new AVLBiNodeImpl<>(key);
        res.value(value);
        return res;
    }

    public static class AVLBiNodeImpl<K, V> extends BiNodeImpl<K, V> {
        private int height;
        private AVLBiNodeImpl<K, V> parent;

        protected AVLBiNodeImpl(K key) {
            super(key);
        }

        public static int height(AVLBiNodeImpl<?, ?> node) {
            return node == null ? 0 : node.height();
        }

        protected static void reset(AVLBiNodeImpl<?, ?> node) {
            while (node != null) {
                node.reset();
                node = node.parent;
            }
        }

        public static <X, Y> AVLBiNodeImpl<X, Y> leftOrNull(AVLBiNodeImpl<X, Y> node) {
            return node == null ? null : node.left();
        }

        public static <X, Y> AVLBiNodeImpl<X, Y> rightOrNull(AVLBiNodeImpl<X, Y> node) {
            return node == null ? null : node.right();
        }

        private static <X, Y> AVLBiNodeImpl<X, Y> asAVL(BiNodeImpl<X, Y> node) {
            return (AVLBiNodeImpl<X, Y>) node;
        }

        public AVLBiNodeImpl<K, V> parent() {
            return parent;
        }

        @Override
        public AVLBiNodeImpl<K, V> left() {
            return asAVL(super.left());
        }

        @Override
        public AVLBiNodeImpl<K, V> right() {
            return asAVL(super.right());
        }

        @Override
        public void right(BiNodeImpl<K, V> right) {
            super.right(right);
            if (right != null) {
                asAVL(right).parent(this);
            }
            reset();
        }

        @Override
        public void left(BiNodeImpl<K, V> left) {
            super.left(left);
            if (left != null) {
                asAVL(left).parent(this);
            }
            reset();
        }

        protected void parent(AVLBiNodeImpl<K, V> parent) {
            this.parent = parent;
        }

        public int height() {
            return height == 0 ? height = calcHeight() : height;
        }

        private int calcHeight() {
            return Math.max(height(right()), height(left())) + 1;
        }

        protected void reset() {
            this.height = 0;
        }
    }
}
