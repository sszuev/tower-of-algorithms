package com.gitlab.sszuev.maps.hashtables;

import com.gitlab.sszuev.maps.SimpleMap;

import java.util.Objects;

/**
 * A hashtable implementation that is based on Separate chaining approach.
 * This is basically a copy-paste from {@link java.util.Hashtable JDK Hashtable}, but with the following differences:
 * <ul>
 * <li>this implementation is symmetric: the operation {@link #remove(Object)} keeps track of the internal array size</li>
 * <li>{@code null}-values are allowed (but not {@code null}-keys)</li>
 * <li>there are no micro-optimizations (code readability is more important in this implementation)</li>
 * <li>different code-organizations</li>
 * <li>it is not thread safe</li>
 * </ul>
 * <p>
 * Created by @ssz on 01.10.2021.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @see java.util.Hashtable
 * @see java.util.HashMap
 * @see <a href='https://en.wikipedia.org/wiki/Hash_table#Separate_chaining'>wiki</a>
 */
public class SeparateChainingHashtableSimpleMap<K, V> implements SimpleMap<K, V> {
    private static final int MIN_ARRAY_SIZE = 8;
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - MIN_ARRAY_SIZE;

    private final float loadFactor;
    protected long size;
    private Entry<K, V>[] table;

    public SeparateChainingHashtableSimpleMap() {
        this(11, 0.75f);
    }

    public SeparateChainingHashtableSimpleMap(int initialCapacity, float loadFactor) {
        if (loadFactor <= 0 || initialCapacity <= 0) {
            throw new IllegalArgumentException();
        }
        initialCapacity = Math.max(initialCapacity, MIN_ARRAY_SIZE);
        this.loadFactor = loadFactor;
        this.table = newTable(initialCapacity);
    }

    @SuppressWarnings("unchecked")
    private static <K, V> Entry<K, V>[] newTable(int size) {
        return new Entry[size];
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public V get(K key) {
        int keyHash = Objects.requireNonNull(key).hashCode();
        int index = hash(keyHash, table.length);
        for (Entry<K, V> entry = table[index]; entry != null; entry = entry.next()) {
            if (entry.match(key, keyHash)) {
                return entry.value();
            }
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        int keyHash = Objects.requireNonNull(key).hashCode();
        int index = hash(keyHash, table.length);
        for (Entry<K, V> entry = table[index]; entry != null; entry = entry.next()) {
            if (entry.match(key, keyHash)) {
                return entry.value(value);
            }
        }
        if (size >= threshold(table.length)) {
            grow();
            index = hash(keyHash, table.length);
        }
        Entry<K, V> entry = newEntry(key, keyHash);
        entry.next(table[index]);
        entry.value(value);
        table[index] = entry;
        size++;
        return null;
    }

    @Override
    public V remove(K key) {
        int keyHash = Objects.requireNonNull(key).hashCode();
        int index = hash(keyHash, table.length);
        Entry<K, V> entry = table[index];
        for (Entry<K, V> prev = null; entry != null; prev = entry, entry = entry.next()) {
            if (!entry.match(key, keyHash)) {
                continue;
            }
            if (prev != null) {
                prev.next(entry.next());
            } else {
                table[index] = entry.next();
            }
            size--;
            if (size < threshold(prevArrayLength(table.length))) {
                shrink();
            }
            return entry.value(null);
        }
        return null;
    }

    /**
     * Calculates a table index.
     *
     * @param keyHashCode {@code int} an object hashcode
     * @param tableLength {@code int} a table length
     * @return {@code int}
     */
    protected int hash(int keyHashCode, int tableLength) {
        return (keyHashCode & 0x7FFFFFFF) % tableLength;
    }

    /**
     * Increases the capacity of and internally reorganizes this hashtable,
     * in order to accommodate and access its entries more efficiently.
     * This method is called automatically when the number of keys in the hashtable exceeds
     * this capacity and load factor.
     */
    protected void grow() {
        final int thisLength = table.length;
        int resLength = nextArrayLength(thisLength);
        if (resLength - MAX_ARRAY_SIZE > 0) {
            if (thisLength == MAX_ARRAY_SIZE) {
                // can't grow anymore
                return;
            }
            resLength = MAX_ARRAY_SIZE;
        }
        Entry<K, V>[] res = newTable(resLength);
        resettle(this.table, thisLength, res, resLength);
        this.table = res;
    }

    /**
     * Decreases the capacity of and internally reorganizes this hashtable,
     * in order to accommodate and access its entries more efficiently.
     * This is done in order to make the remove operation a symmetric add operation.
     */
    protected void shrink() {
        final int thisLength = table.length;
        if (thisLength <= MIN_ARRAY_SIZE) {
            return;
        }
        int resLength = prevArrayLength(thisLength);
        Entry<K, V>[] res = newTable(resLength);
        resettle(this.table, thisLength, res, resLength);
        this.table = res;
    }

    protected void resettle(Entry<K, V>[] source, int sourceSize, Entry<K, V>[] target, int targetSize) {
        for (int i = sourceSize - 1; i >= 0; i--) {
            for (Entry<K, V> e = source[i]; e != null; ) {
                Entry<K, V> entry = e;
                e = e.next();
                int index = hash(entry.keyHash(), targetSize);
                entry.next(target[index]);
                target[index] = entry;
            }
        }
    }

    private int threshold(int length) {
        return (int) Math.min(length * loadFactor, MAX_ARRAY_SIZE);
    }

    private int nextArrayLength(int length) {
        return length << 1 + 1;
    }

    private int prevArrayLength(int length) {
        return length >> 1 + 1;
    }

    protected Entry<K, V> newEntry(K key, int hashCode) {
        return new Entry<>(key, hashCode);
    }

    /**
     * A map entry.
     *
     * @param <K> any key
     * @param <V> any value
     */
    public static class Entry<K, V> {
        private final int keyHash;
        private final K key;

        private Entry<K, V> next;
        private V value;

        protected Entry(K key, int keyHash) {
            this.key = Objects.requireNonNull(key);
            this.keyHash = keyHash;
        }

        public Entry<K, V> next() {
            return next;
        }

        public int keyHash() {
            return keyHash;
        }

        public V value() {
            return value;
        }

        protected V value(V value) {
            V res = this.value;
            this.value = value;
            return res;
        }

        protected void next(Entry<K, V> next) {
            this.next = next;
        }

        private boolean match(K key, int code) {
            return this.keyHash == code && this.key.equals(key);
        }
    }
}
