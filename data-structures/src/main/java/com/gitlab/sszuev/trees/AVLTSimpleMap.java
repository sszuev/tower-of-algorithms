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
        private long height;
        private AVLBiNode<K, V> parent;

        protected AVLBiNode(K key) {
            super(key);
        }

        public static long height(AVLBiNode<?, ?> node) {
            return node == null ? 0 : node.height();
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
            this.height = 0;
        }

        @Override
        public void left(BiNode<K, V> left) {
            super.left(left);
            if (left != null) {
                asAVL(left).parent(this);
            }
            this.height = 0;
        }

        protected void parent(AVLBiNode<K, V> parent) {
            this.parent = parent;
        }

        public long height() {
            return height == 0 ? height = calcHeight() : height;
        }

        private long calcHeight() {
            return Math.max(height(right()), height(left())) + 1;
        }

        private AVLBiNode<K, V> asAVL(BiNode<K, V> node) {
            return (AVLBiNode<K, V>) node;
        }
    }
}
