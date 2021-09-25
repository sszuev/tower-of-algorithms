package com.gitlab.sszuev.trees;

import java.util.Comparator;

/**
 * A Binary Search Tree with AVL-rebalance, and also an implementation of {@link SimpleMap}.
 * Created by @ssz on 25.09.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/AVL_tree'>wiki</a>
 * @see <a href='https://www.cs.usfca.edu/~galles/visualization/AVLtree.html'>visualization</a>
 */
public class AVLTSimpleMap<K, V> extends BSTSimpleMap<K, V> {

    public AVLTSimpleMap() {
        this(null);
    }

    public AVLTSimpleMap(Comparator<K> comparator) {
        super(comparator);
    }

    @Override
    protected void afterInsert(BiNode<K, V> node) {
        AVLBiNode<K, V> n = AVLBiNode.asAVL(node);
        AVLBiNode.reset(n);
        rebalance(n);
    }

    @Override
    protected void afterRemove(BiNode<K, V> node) {
        AVLBiNode<K, V> n = AVLBiNode.asAVL(node);
        AVLBiNode.reset(n);
        // TODO: run rebalance
    }

    public void rebalance(AVLBiNode<K, V> node) {
        while (node != null) {
            if (needRebalance(node)) {
                node = performRebalance(node);
                AVLBiNode.reset(node);
            }
            node = node.parent();
        }
    }

    private boolean needRebalance(AVLBiNode<K, V> node) {
        if (node == null) {
            return false;
        }
        int leftHeight = AVLBiNode.height(node.left());
        int rightHeight = AVLBiNode.height(node.right());
        return Math.abs(leftHeight - rightHeight) > 1;
    }

    private AVLBiNode<K, V> performRebalance(final AVLBiNode<K, V> a) {
        AVLBiNode<K, V> b = a.right();

        AVLBiNode<K, V> c = AVLBiNode.leftOrNull(b);
        AVLBiNode<K, V> r = AVLBiNode.rightOrNull(b);
        AVLBiNode<K, V> l = AVLBiNode.leftOrNull(a);

        if (AVLBiNode.height(b) - AVLBiNode.height(l) == 2) {
            if (AVLBiNode.height(c) <= AVLBiNode.height(r)) {
                return smallLeftRotation(a);
            } else {
                return bigLeftRotation(a);
            }
        }

        b = a.left();
        c = AVLBiNode.rightOrNull(b);
        r = AVLBiNode.rightOrNull(a);
        l = AVLBiNode.leftOrNull(b);

        if (AVLBiNode.height(b) - AVLBiNode.height(r) == 2) {
            if (AVLBiNode.height(c) <= AVLBiNode.height(l)) {
                return smallRightRotation(a);
            } else {
                return bigRightRotation(a);
            }
        }
        throw new IllegalStateException();
    }

    public AVLBiNode<K, V> smallLeftRotation(AVLBiNode<K, V> a) {
        AVLBiNode<K, V> p = a.parent();

        AVLBiNode<K, V> b = a.right();
        AVLBiNode<K, V> c = b.left();

        a.right(c);
        b.left(a);

        replace(p, a, b);
        return b;
    }

    public AVLBiNode<K, V> bigLeftRotation(AVLBiNode<K, V> a) {
        AVLBiNode<K, V> p = a.parent();

        AVLBiNode<K, V> b = a.right();
        AVLBiNode<K, V> c = b.left();
        AVLBiNode<K, V> m = c.left();
        AVLBiNode<K, V> n = c.right();

        a.right(m);
        b.left(n);
        c.left(a);
        c.right(b);

        replace(p, a, c);
        return c;
    }

    public AVLBiNode<K, V> smallRightRotation(AVLBiNode<K, V> a) {
        AVLBiNode<K, V> p = a.parent();

        AVLBiNode<K, V> b = a.left();
        AVLBiNode<K, V> c = b.right();

        b.right(a);
        a.left(c);

        replace(p, a, b);
        return b;
    }

    public AVLBiNode<K, V> bigRightRotation(AVLBiNode<K, V> a) {
        AVLBiNode<K, V> p = a.parent();

        AVLBiNode<K, V> b = a.left();
        AVLBiNode<K, V> c = b.right();
        AVLBiNode<K, V> m = c.left();
        AVLBiNode<K, V> n = c.right();

        b.right(m);
        a.left(n);
        c.right(a);
        c.left(b);

        replace(p, a, c);
        return c;
    }

    @Override
    protected void root(BiNode<K, V> newRoot) {
        AVLBiNode<K, V> root = AVLBiNode.asAVL(newRoot);
        super.root(root);
        if (root != null) {
            root.parent(null);
            root.reset();
        }
    }

    @Override
    protected AVLBiNode<K, V> node(BiNode<K, V> other) {
        return node(other.key(), other.value());
    }

    @Override
    protected AVLBiNode<K, V> node(K key, V value) {
        AVLBiNode<K, V> res = new AVLBiNode<>(key);
        res.value(value);
        return res;
    }

    public static class AVLBiNode<K, V> extends BiNode<K, V> {
        private int height;
        private AVLBiNode<K, V> parent;

        protected AVLBiNode(K key) {
            super(key);
        }

        public static int height(AVLBiNode<?, ?> node) {
            return node == null ? 0 : node.height();
        }

        protected static void reset(AVLBiNode<?, ?> node) {
            while (node != null) {
                node.height = 0;
                node = node.parent;
            }
        }

        public static <X, Y> AVLBiNode<X, Y> leftOrNull(AVLBiNode<X, Y> node) {
            return node == null ? null : node.left();
        }

        public static <X, Y> AVLBiNode<X, Y> rightOrNull(AVLBiNode<X, Y> node) {
            return node == null ? null : node.right();
        }

        private static <X, Y> AVLBiNode<X, Y> asAVL(BiNode<X, Y> node) {
            return (AVLBiNode<X, Y>) node;
        }

        public AVLBiNode<K, V> parent() {
            return parent;
        }

        @Override
        public AVLBiNode<K, V> left() {
            return asAVL(super.left());
        }

        @Override
        public AVLBiNode<K, V> right() {
            return asAVL(super.right());
        }

        @Override
        public void right(BiNode<K, V> right) {
            super.right(right);
            if (right != null) {
                asAVL(right).parent(this);
            }
            reset();
        }

        @Override
        public void left(BiNode<K, V> left) {
            super.left(left);
            if (left != null) {
                asAVL(left).parent(this);
            }
            reset();
        }

        protected void parent(AVLBiNode<K, V> parent) {
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
