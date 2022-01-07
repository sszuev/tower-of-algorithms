package com.gitlab.sszuev.maps.trees;

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
    protected final TreapNodeImpl<K, V> node(K key, V value) {
        return node(key, value, priorityFunction.applyAsDouble(key, value));
    }

    @Override
    protected final TreapNodeImpl<K, V> node(BiNodeImpl<K, V> other) {
        return node(other.key(), other.value(), ((TreapNodeImpl<K, V>) other).priority());
    }

    protected TreapNodeImpl<K, V> node(K key, V value, double priority) {
        TreapNodeImpl<K, V> res = new TreapNodeImpl<>(key, priority);
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
        Object split = split(key, (TreapNodeImpl<K, V>) root, TreapSimpleMap.super::compare, false, this::node);
        if (split instanceof TreapSimpleMap.TreapNodeImpl) {
            TreapNodeImpl<K, V> node = (TreapNodeImpl<K, V>) split;
            V prev = node.value();
            node.value(value);
            return prev;
        }
        TreapNodeImpl<K, V> left = ((TreapNodeImpl[]) split)[0];
        TreapNodeImpl<K, V> right = ((TreapNodeImpl[]) split)[1];

        TreapNodeImpl<K, V> newLeft = merge(left, node(key, value), this::node);
        TreapNodeImpl<K, V> newRoot = merge(newLeft, right, this::node);
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
        TreapNodeImpl<K, V>[] first = (TreapNodeImpl[]) split(key, (TreapNodeImpl<K, V>) root,
                (a, b) -> TreapSimpleMap.super.compare(a, b) + 1, true, this::node);
        TreapNodeImpl<K, V> firstLeft = first[0];
        TreapNodeImpl<K, V> firstRight = first[1];
        if (firstRight == null) {
            return null;
        }
        TreapNodeImpl<K, V>[] second = (TreapNodeImpl[]) split(key, firstRight,
                TreapSimpleMap.super::compare, true, this::node);
        TreapNodeImpl<K, V> secondLeft = second[0];
        TreapNodeImpl<K, V> secondRight = second[1];
        if (secondLeft == null) {
            return null;
        }
        if (!Objects.equals(secondLeft.key(), key)) {
            throw new IllegalStateException();
        }
        TreapNodeImpl<K, V> newRoot = merge(firstLeft, secondRight, this::node);
        root(newRoot);
        size--;
        return secondLeft.value();
    }

    protected static <X, Y> TreapNodeImpl<X, Y> merge(TreapNodeImpl<X, Y> left,
                                                      TreapNodeImpl<X, Y> right,
                                                      UnaryOperator<TreapNodeImpl<X, Y>> factory) {
        if (left == null) {
            return right;
        }
        if (right == null) {
            return left;
        }
        if (left.priority() > right.priority()) {
            TreapNodeImpl<X, Y> res = factory.apply(left);
            res.left(left.left());
            res.right(merge(left.right(), right, factory));
            return res;
        }
        TreapNodeImpl<X, Y> res = factory.apply(right);
        res.right(right.right());
        res.left(merge(left, right.left(), factory));
        return res;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    protected static <X, Y> Object split(X key,
                                         TreapNodeImpl<X, Y> current,
                                         Comparator<X> comparator,
                                         boolean splitOnEquals,
                                         UnaryOperator<TreapNodeImpl<X, Y>> factory) {
        int res = comparator.compare(current.key(), key);
        if (!splitOnEquals && res == 0) {
            return current;
        }
        TreapNodeImpl<X, Y> right, left;
        TreapNodeImpl<X, Y> newTree = null;
        if (res <= 0) {
            if (current.right() == null) {
                right = null;
            } else {
                Object split = split(key, current.right(), comparator, splitOnEquals, factory);
                if (!(split instanceof TreapNodeImpl[])) {
                    return split;
                }
                newTree = ((TreapNodeImpl[]) split)[0];
                right = ((TreapNodeImpl[]) split)[1];
            }
            left = factory.apply(current);
            left.left(current.left());
            left.right(newTree);
        } else {
            if (current.left() == null) {
                left = null;
            } else {
                Object split = split(key, current.left(), comparator, splitOnEquals, factory);
                if (!(split instanceof TreapNodeImpl[])) {
                    return split;
                }
                left = ((TreapNodeImpl[]) split)[0];
                newTree = ((TreapNodeImpl[]) split)[1];
            }
            right = factory.apply(current);
            right.left(newTree);
            right.right(current.right());
        }
        return new TreapNodeImpl[]{left, right};
    }

    public static class TreapNodeImpl<K, V> extends BiNodeImpl<K, V> {
        private final double priority;

        protected TreapNodeImpl(K key, double priority) {
            super(key);
            this.priority = priority;
        }

        protected double priority() {
            return priority;
        }

        @Override
        public TreapNodeImpl<K, V> left() {
            return (TreapNodeImpl<K, V>) super.left();
        }

        @Override
        public TreapNodeImpl<K, V> right() {
            return (TreapNodeImpl<K, V>) super.right();
        }

        @Override
        public String toString() {
            return String.format("[%s; %s]", key(), priority());
        }
    }

}
