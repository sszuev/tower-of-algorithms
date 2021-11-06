package com.gitlab.sszuev.maps;

import java.util.Comparator;
import java.util.Objects;
import java.util.Random;
import java.util.function.ToDoubleBiFunction;
import java.util.function.UnaryOperator;

/**
 * A Treap (a Cartesian Randomized Binary Search Tree).
 * Created by @ssz on 05.11.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Treap'>wiki: Treap</a>
 */
public class TreapSimpleMap<K, V> extends BaseBSTSimpleMap<K, V> {
    private static final Random RANDOM = new Random();
    private final ToDoubleBiFunction<K, V> priorityFunction;

    public TreapSimpleMap() {
        this(null, (k, v) -> RANDOM.nextDouble());
    }

    public TreapSimpleMap(Comparator<K> comparator, ToDoubleBiFunction<K, V> priorityFunction) {
        super(comparator);
        this.priorityFunction = Objects.requireNonNull(priorityFunction);
    }

    @Override
    protected final TreapNode<K, V> node(K key, V value) {
        return node(key, value, priorityFunction.applyAsDouble(key, value));
    }

    @Override
    protected final TreapNode<K, V> node(BiNode<K, V> other) {
        return node(other.key(), other.value(), ((TreapNode<K, V>) other).priority());
    }

    protected TreapNode<K, V> node(K key, V value, double priority) {
        TreapNode<K, V> res = new TreapNode<>(key, priority);
        res.value(value);
        return res;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public V put(K key, V value) {
        if (root == null) {
            root(node(key, value));
            size++;
            return null;
        }
        Object split = split(key, (TreapNode<K, V>) root, TreapSimpleMap.super::compare, false, this::node);
        if (split instanceof TreapNode) {
            TreapNode<K, V> node = (TreapNode<K, V>) split;
            V prev = node.value();
            node.value(value);
            return prev;
        }
        TreapNode<K, V> left = ((TreapNode[]) split)[0];
        TreapNode<K, V> right = ((TreapNode[]) split)[1];

        TreapNode<K, V> newLeft = merge(left, node(key, value), this::node);
        TreapNode<K, V> newRoot = merge(newLeft, right, this::node);
        root(newRoot);
        size++;
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public V remove(K key) {
        if (root == null) {
            return null;
        }
        TreapNode<K, V>[] first = (TreapNode[]) split(key, (TreapNode<K, V>) root,
                (a, b) -> TreapSimpleMap.super.compare(a, b) + 1, true, this::node);
        TreapNode<K, V> firstLeft = first[0];
        TreapNode<K, V> firstRight = first[1];
        if (firstRight == null) {
            return null;
        }
        TreapNode<K, V>[] second = (TreapNode[]) split(key, firstRight,
                TreapSimpleMap.super::compare, true, this::node);
        TreapNode<K, V> secondLeft = second[0];
        TreapNode<K, V> secondRight = second[1];
        if (secondLeft == null) {
            return null;
        }
        if (!Objects.equals(secondLeft.key(), key)) {
            throw new IllegalStateException();
        }
        TreapNode<K, V> newRoot = merge(firstLeft, secondRight, this::node);
        root(newRoot);
        size--;
        return secondLeft.value();
    }

    protected static <X, Y> TreapNode<X, Y> merge(TreapNode<X, Y> left,
                                                  TreapNode<X, Y> right,
                                                  UnaryOperator<TreapNode<X, Y>> factory) {
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }
        if (left.priority() > right.priority()) {
            TreapNode<X, Y> res = factory.apply(left);
            res.left(left.left());
            res.right(merge(left.right(), right, factory));
            return res;
        }
        TreapNode<X, Y> res = factory.apply(right);
        res.right(right.right());
        res.left(merge(left, right.left(), factory));
        return res;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected static <X, Y> Object split(X key,
                                         TreapNode<X, Y> current,
                                         Comparator<X> comparator,
                                         boolean splitOnEquals,
                                         UnaryOperator<TreapNode<X, Y>> factory) {
        int res = comparator.compare(current.key(), key);
        if (!splitOnEquals && res == 0) {
            return current;
        }
        TreapNode<X, Y> right, left;
        TreapNode<X, Y> newTree = null;
        if (res <= 0) {
            if (current.right() == null) {
                right = null;
            } else {
                Object split = split(key, current.right(), comparator, splitOnEquals, factory);
                if (!(split instanceof TreapNode[])) {
                    return split;
                }
                newTree = ((TreapNode[]) split)[0];
                right = ((TreapNode[]) split)[1];
            }
            left = factory.apply(current);
            left.left(current.left());
            left.right(newTree);
        } else {
            if (current.left() == null) {
                left = null;
            } else {
                Object split = split(key, current.left(), comparator, splitOnEquals, factory);
                if (!(split instanceof TreapNode[])) {
                    return split;
                }
                left = ((TreapNode[]) split)[0];
                newTree = ((TreapNode[]) split)[1];
            }
            right = factory.apply(current);
            right.left(newTree);
            right.right(current.right());
        }
        return new TreapNode[]{left, right};
    }

    public static class TreapNode<K, V> extends BiNode<K, V> {
        private final double priority;

        protected TreapNode(K key, double priority) {
            super(key);
            this.priority = priority;
        }

        protected double priority() {
            return priority;
        }

        @Override
        public TreapNode<K, V> left() {
            return (TreapNode<K, V>) super.left();
        }

        @Override
        public TreapNode<K, V> right() {
            return (TreapNode<K, V>) super.right();
        }

        @Override
        public String toString() {
            return String.format("[%s; %s]", key(), priority());
        }
    }

}
