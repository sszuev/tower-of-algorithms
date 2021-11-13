package com.gitlab.sszuev.maps;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A B-Tree {@link SimpleMap Simple Map} implementation.
 * <p>
 * Created by @ssz on 06.11.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/B-tree'>wiki: B-tree</a>
 * @see <a href='https://www.cs.usfca.edu/~galles/visualization/BTree.html'>visualization: B-tree</a>
 */
public class BTreeSimpleMap<K, V> implements SimpleMap<K, V> {
    protected final Comparator<K> comparator;
    protected long size;
    protected BNodeImpl<K, V> root;

    public BTreeSimpleMap() {
        this(null);
    }

    public BTreeSimpleMap(Comparator<K> comparator) {
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked")
    public static <X> X[] newArray(int size) {
        return (X[]) new Object[size];
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public V get(K key) {
        BNodeImpl<K, V> current = root;
        while (current != null) {
            if (current.isEmpty()) {
                break;
            }
            BNodeImpl.ItemImpl<K, V>[] items = current.items();
            int res = binarySearch(items, current.lastIndex(), key);
            if (res >= 0) {
                return items[res].value();
            }
            int insertionPoint = -res - 1;
            if (insertionPoint < 2) {
                BNodeImpl.ItemImpl<K, V> item = items[0];
                current = insertionPoint == 0 ? item.left() : item.right();
            } else {
                current = items[insertionPoint - 1].right();
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    @Override
    public V remove(K key) {
        // TODO
        throw new UnsupportedOperationException("TODO");
    }

    /**
     * Searches the specified array for the specified object using the binary search algorithm.
     *
     * @param array     the array to be searched
     * @param lastIndex {@code int} the index of the last element (inclusive) to be searched
     * @param key       {@link K} the value to be searched for
     * @return {@code int}, the index of the search key, if it is contained in the array;
     * otherwise, <code>-(<i>insertion point</i>) - 1</code>.
     * The <i>insertion point</i> is defined as the point at which the key would be inserted into the array:
     * the index of the first element greater than the key,
     * or {@code a.length} if all elements in the array are less than the specified key.
     * Note that this guarantees that the return value will be &gt;= 0 if and only if the key is found
     * @see Arrays#binarySearch(Object[], Object)
     */
    protected int binarySearch(BNodeImpl.ItemImpl<K, V>[] array, int lastIndex, K key) {
        int low = 0;
        int high = lastIndex;

        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = compare(array[mid].key(), key);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp > 0) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        // -insertion-point - 1:
        return -(low + 1);
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

    public static class BNodeImpl<K, V> implements BNode<K> {
        private final ItemImpl<K, V>[] items;
        private int lastIndex;

        public BNodeImpl(int degree) {
            this(newArray(degree));
        }

        protected BNodeImpl(ItemImpl<K, V>[] items) {
            this.items = Objects.requireNonNull(items);
        }

        private static <X> Stream<BNode<X>> nodes(ItemImpl<X, ?> e) {
            if (e == null) {
                return Stream.empty();
            }
            BNode<X> left = e.left();
            BNode<X> right = e.right();
            if (left != null && right != null) {
                return Stream.of(left, right);
            }
            if (right != null) {
                return Stream.of(right);
            }
            return Stream.ofNullable(left);
        }

        protected BNodeImpl.ItemImpl<K, V> item(int i) {
            return items[i];
        }

        protected BNodeImpl.ItemImpl<K, V>[] items() {
            return items;
        }

        public boolean isEmpty() {
            return items[0] == null;
        }

        public int lastIndex() {
            if (lastIndex > 0) {
                return lastIndex;
            }
            int index = items.length - 1;
            for (; index >= 0; index--) {
                if (items[index] != null) {
                    break;
                }
            }
            return lastIndex = index;
        }

        @Override
        public Stream<BNode<K>> children() {
            return Arrays.stream(items).flatMap(BNodeImpl::nodes);
        }

        @Override
        public String toString() {
            return Arrays.stream(items).filter(Objects::nonNull)
                    .map(x -> String.valueOf(x.key())).collect(Collectors.joining("|", "[", "]"));
        }

        public static class ItemImpl<K, V> {
            private final K key;
            private V value;
            private BNodeImpl<K, V> left;
            private BNodeImpl<K, V> right;

            public ItemImpl(K key) {
                this.key = Objects.requireNonNull(key);
            }

            public K key() {
                return key;
            }

            public V value() {
                return value;
            }

            public BNodeImpl<K, V> left() {
                return left;
            }

            public BNodeImpl<K, V> right() {
                return right;
            }

            protected V value(V value) {
                V prev = this.value;
                this.value = value;
                return prev;
            }

            protected BNodeImpl<K, V> left(BNodeImpl<K, V> child) {
                BNodeImpl<K, V> prev = this.left;
                this.left = child;
                return prev;
            }

            protected BNodeImpl<K, V> right(BNodeImpl<K, V> child) {
                BNodeImpl<K, V> prev = this.right;
                this.right = child;
                return prev;
            }
        }

    }

}
