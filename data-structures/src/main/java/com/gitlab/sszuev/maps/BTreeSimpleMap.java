package com.gitlab.sszuev.maps;

import java.lang.reflect.Array;
import java.util.*;
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
public class BTreeSimpleMap<K, V> implements SimpleMap<K, V>, HasTreeRoot {
    protected final Comparator<K> comparator;
    protected final int degree;
    protected final int middle;

    protected long size;
    protected BNodeImpl<K, V> root;

    public BTreeSimpleMap() {
        this(3);
    }

    public BTreeSimpleMap(int degree) {
        this(degree, null);
    }

    public BTreeSimpleMap(int degree, Comparator<K> comparator) {
        if (degree < 3) {
            throw new IllegalArgumentException("Degree must be >= 3");
        }
        this.degree = degree;
        this.middle = calcMiddleIndex(degree);
        this.comparator = comparator;
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public BNodeImpl<K, V> getRoot() {
        return root;
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
            current = next(current, -res - 1);
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if (root == null) {
            root = new BNodeImpl<>(degree);
        }
        BNodeImpl<K, V> current = root;
        while (true) {
            BNodeImpl.ItemImpl<K, V>[] items = current.items();
            int res = binarySearch(items, current.lastIndex(), key);
            if (res >= 0) {
                return items[res].value(value);
            }
            int insertIndex = -res - 1;
            if (current.isLeaf()) {
                BNodeImpl.ItemImpl<K, V> item = new BNodeImpl.ItemImpl<>(key);
                current.insertItem(item, insertIndex);
                if (needRebalanceWhenAdd(current)) { // need rebalance
                    rebalanceWhenAdd(current);
                }
                size++;
                return item.value(value);
            }
            current = next(current, insertIndex);
        }
    }

    @Override
    public V remove(K key) {
        BNodeImpl<K, V> current = root;
        List<Integer> path = new ArrayList<>();
        while (current != null) {
            if (current.isEmpty()) {
                break;
            }
            BNodeImpl.ItemImpl<K, V>[] items = current.items();
            int res = binarySearch(items, current.lastIndex(), key);
            if (res >= 0) {
                V value = items[res].value();
                deleteNode(current, res, path);
                this.size--;
                if (this.size == 0) {
                    this.root = null;
                }
                return value;
            }
            int prevIndex = -res - 1;
            path.add(prevIndex);
            current = next(current, prevIndex);
        }
        return null;
    }

    protected BNodeImpl<K, V> next(BNodeImpl<K, V> current, int insertIndex) {
        if (insertIndex < 2) {
            return insertIndex == 0 ? current.left() : current.right(0);
        } else {
            return current.right(insertIndex - 1);
        }
    }

    protected boolean needRebalanceWhenAdd(BNodeImpl<K, V> node) {
        return node.lastIndex() >= degree - 1;
    }

    protected void rebalanceWhenAdd(BNodeImpl<K, V> node) {
        BNodeImpl<K, V> left = BNodeImpl.splitLeft(node, this.middle);
        BNodeImpl<K, V> right = BNodeImpl.splitRight(node, this.middle);
        BNodeImpl.ItemImpl<K, V> middle = node.items()[this.middle];

        BNodeImpl<K, V> parent = node.parent();
        int insertIndex;
        if (parent == null) { // new root
            parent = new BNodeImpl<>(degree);
            insertIndex = 0;
            this.root = parent;
        } else {
            int res = binarySearch(parent.items(), parent.lastIndex(), middle.key());
            if (res >= 0) {
                throw new IllegalStateException();
            }
            insertIndex = -res - 1;
        }
        parent.insertItem(middle, insertIndex);
        if (insertIndex == 0) {
            parent.left(left);
            BNodeImpl<K, V> prevRight = parent.right(0, right);
            right.left(prevRight);
        } else {
            parent.right(insertIndex - 1, left);
            parent.right(insertIndex, right);
        }

        if (needRebalanceWhenAdd(parent)) {
            rebalanceWhenAdd(parent);
        }
    }

    protected void deleteNode(BNodeImpl<K, V> node, int index, List<Integer> path) {
        if (node.isLeaf()) {
            if (node.isRoot() || isHalfFull(node)) { // case 1:
                node.deleteItem(index);
                return;
            }
            // case 2:
            node.deleteItem(index);
            while (true) {
                int pathIndex = path.remove(path.size() - 1);
                rebalanceWhenDelete(node, pathIndex);
                node = node.parent();
                if (node.isRoot()) {
                    if (node.isEmpty()) {
                        this.root = node.left();
                        BNodeImpl.parent(this.root, null);
                    }
                    return;
                }
                if (!needRebalanceWhenDelete(node)) {
                    return;
                }
            }
        }
        // case 3:
        // TODO:
        throw new UnsupportedOperationException("TODO");
    }

    protected boolean needRebalanceWhenDelete(BNodeImpl<K, V> node) {
        return node.length() < middle;
    }

    protected void rebalanceWhenDelete(BNodeImpl<K, V> node, int indexInParent) {
        // case 2a:
        BNodeImpl<K, V> parent = node.parent();
        BNodeImpl<K, V> left = indexInParent > 0 ? next(parent, indexInParent - 1) : null;
        if (left == node) {
            throw new IllegalStateException();
        }
        if (left != null && isHalfFull(left)) {
            BNodeImpl<K, V> toLeft = left.right(left.lastIndex());
            BNodeImpl.ItemImpl<K, V> fromParent = BNodeImpl.move(left, left.lastIndex(), parent, indexInParent - 1);
            node.insertItem(fromParent, 0);
            BNodeImpl<K, V> toRight = node.left();
            node.left(toLeft);
            node.right(0, toRight);
            return;
        }
        BNodeImpl<K, V> right = next(parent, indexInParent + 1);
        if (right != null && isHalfFull(right)) {
            BNodeImpl<K, V> toLeft = right.right(0);
            BNodeImpl<K, V> toRight = right.left();
            BNodeImpl.ItemImpl<K, V> fromParent = BNodeImpl.move(right, 0, parent, indexInParent);
            node.insertItem(fromParent, node.length());
            right.left(toLeft);
            node.right(node.lastIndex(), toRight);
            return;
        }
        // case 2b:
        if (left != null) {
            BNodeImpl.move(parent, indexInParent - 1, left, left.length());
            left.addAll(node);
            return;
        }
        if (right != null) {
            BNodeImpl.move(parent, indexInParent, node, node.length());
            node.addAll(right);
            return;
        }
        throw new IllegalStateException();
    }

    protected boolean isHalfFull(BNodeImpl<K, V> node) {
        return node.length() > middle;
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

    private static int calcMiddleIndex(int length) {
        int res = length / 2;
        if (length % 2 == 0) {
            res--;
        }
        return res;
    }

    public static class BNodeImpl<K, V> implements MultiNode<K> {
        private final ItemImpl<K, V>[] items;
        private int lastIndex;
        private BNodeImpl<K, V> parent;
        private BNodeImpl<K, V> leftLink;

        public BNodeImpl(int degree) {
            this(newItemArray(degree));
        }

        protected BNodeImpl(ItemImpl<K, V>[] items) {
            this.items = Objects.requireNonNull(items);
        }

        @SuppressWarnings("unchecked")
        private static <X, Y> ItemImpl<X, Y>[] newItemArray(int size) {
            return (ItemImpl<X, Y>[]) Array.newInstance(ItemImpl.class, size);
        }

        private static <X, Y> void parent(BNodeImpl<X, Y> child, BNodeImpl<X, Y> parent) {
            if (child != null) {
                child.parent = parent;
            }
        }

        private static <X, Y> ItemImpl<X, Y> move(BNodeImpl<X, Y> source, int sourceIndex,
                                                  BNodeImpl<X, Y> target, int targetIndex) {
            ItemImpl<X, Y> src = source.deleteItem(sourceIndex);
            return target.replaceItem(src, targetIndex);
        }

        public static <K, V> BNodeImpl<K, V> splitLeft(BNodeImpl<K, V> node, int middle) {
            ItemImpl<K, V>[] nodeArray = node.items();
            BNodeImpl<K, V> res = new BNodeImpl<>(nodeArray.length);
            ItemImpl<K, V>[] resArray = res.items();
            for (int i = 0; i < middle; i++) {
                parent(node.right(i), res);
                resArray[i] = nodeArray[i];
            }
            res.left(node.left());
            return res;
        }

        public static <K, V> BNodeImpl<K, V> splitRight(BNodeImpl<K, V> node, int middle) {
            ItemImpl<K, V>[] nodeArray = node.items();
            BNodeImpl<K, V> res = new BNodeImpl<>(nodeArray.length);
            ItemImpl<K, V>[] resArray = res.items();
            int start = middle + 1;
            for (int i = start; i < nodeArray.length; i++) {
                parent(node.right(i), res);
                resArray[i - start] = nodeArray[i];
            }
            res.left(node.right(middle));
            return res;
        }

        protected ItemImpl<K, V>[] items() {
            return items;
        }

        public boolean isEmpty() {
            return items[0] == null;
        }

        public int length() {
            return lastIndex() + 1;
        }

        protected int lastIndex() {
            return lastIndex <= 0 ? lastIndex = calcLastIndex() : lastIndex;
        }

        private int calcLastIndex() {
            int index = items.length - 1;
            for (; index >= 0; index--) {
                if (items[index] != null) {
                    break;
                }
            }
            return index;
        }

        protected void insertItem(ItemImpl<K, V> item, int index) {
            if (index == 0 && items[0] == null) {
                items[0] = item;
                this.lastIndex = 0;
                return;
            }
            int lastIndex = lastIndex();
            if (index > lastIndex) {
                items[index] = item;
                this.lastIndex = index;
                return;
            }
            System.arraycopy(items, index, items, index + 1, lastIndex + 1 - index);
            items[index] = item;
            this.lastIndex++;
        }

        protected ItemImpl<K, V> deleteItem(int index) {
            int lastIndex = lastIndex();
            if (lastIndex < index) {
                throw new IllegalStateException();
            }
            ItemImpl<K, V> res = items[index];
            if (index == lastIndex) {
                items[index] = null;
                this.lastIndex--;
                return res;
            }
            System.arraycopy(items, index + 1, items, index, lastIndex - index);
            items[lastIndex] = null;
            this.lastIndex--;
            return res;
        }

        protected void addAll(BNodeImpl<K, V> other) {
            BNodeImpl<K, V> prev = right(lastIndex(), other.left());
            if (prev != null) {
                throw new IllegalStateException();
            }
            int length = length();
            int otherLength = other.length();
            ItemImpl<K, V>[] otherItems = other.items();
            for (int i = length; i < length + otherLength; i++) {
                ItemImpl<K, V> item = otherItems[i - length];
                this.items[i] = item;
                parent(item.link, this);
            }
            this.lastIndex += otherLength;
        }

        protected ItemImpl<K, V> replaceItem(ItemImpl<K, V> item, int index) {
            BNodeImpl<K, V> resLink = right(index);
            ItemImpl<K, V> old = items[index];
            if (old != null) {
                right(index, null); // remove ref for old item
                items[index] = item;
            } else { // at the end of the node with index increasing
                insertItem(item, index);
            }
            right(index, resLink);
            return old;
        }

        @Override
        public Stream<K> keys() {
            return Arrays.stream(items, 0, length()).map(ItemImpl::key);
        }

        @Override
        public Stream<MultiNode<K>> children() {
            Stream<MultiNode<K>> res = Arrays.stream(items).flatMap(BNodeImpl::rightLinks);
            if (leftLink != null) {
                res = Stream.concat(Stream.of(leftLink), res);
            }
            return res;
        }

        private static <X> Stream<MultiNode<X>> rightLinks(ItemImpl<X, ?> e) {
            return e == null ? Stream.empty() : Stream.ofNullable(e.link);
        }

        public boolean isLeaf() {
            return leftLink == null;
        }

        public boolean isRoot() {
            return parent == null;
        }

        protected BNodeImpl<K, V> parent() {
            return parent;
        }

        public BNodeImpl<K, V> left() {
            return leftLink;
        }

        protected BNodeImpl<K, V> left(BNodeImpl<K, V> leftLink) {
            parent(leftLink, this);
            BNodeImpl<K, V> prev = this.leftLink;
            this.leftLink = leftLink;
            return prev;
        }

        public BNodeImpl<K, V> right(int index) {
            ItemImpl<K, V> item = items[index];
            return item == null ? null : item.link;
        }

        protected BNodeImpl<K, V> right(int index, BNodeImpl<K, V> rightLink) {
            ItemImpl<K, V> item = items[index];
            BNodeImpl<K, V> prev = item.link;
            item.link = rightLink;
            parent(rightLink, this);
            return prev;
        }

        @Override
        public String toString() {
            return Arrays.stream(items).filter(Objects::nonNull)
                    .map(x -> String.valueOf(x.key())).collect(Collectors.joining("|", "[", "]"));
        }

        public static class ItemImpl<K, V> {
            private final K key;
            private V value;

            private BNodeImpl<K, V> link;

            public ItemImpl(K key) {
                this.key = Objects.requireNonNull(key);
            }

            public K key() {
                return key;
            }

            public V value() {
                return value;
            }

            protected V value(V value) {
                V prev = this.value;
                this.value = value;
                return prev;
            }

            @Override
            public String toString() {
                return String.format("{%s}", key);
            }
        }
    }
}
