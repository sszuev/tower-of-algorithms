package com.gitlab.sszuev.maps.hashtables;

import com.gitlab.sszuev.maps.SimpleMap;

import java.util.Objects;

/**
 * A hashtable implementation that is based on Open addressing approach.
 * Created by @ssz on 02.10.2021.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @see <a href='https://en.wikipedia.org/wiki/Hash_table#Open_addressing'>wiki: open addressing</a>
 * @see <a href='https://en.wikipedia.org/wiki/Quadratic_probing'>wiki: quadratic probing</a>
 */
public class OpenAddressingHashtableSimpleMap<K, V> implements SimpleMap<K, V> {
    private static final int MIN_ARRAY_SIZE = 8;
    private static final int MAX_ARRAY_SIZE = Integer.MAX_VALUE - MIN_ARRAY_SIZE;

    private final float loadFactor;
    private final HashFunction probing;
    protected long size;
    private Entry<K, V>[] table;

    /**
     * Constructs and instance of map with probing function {@code (k + i ^ 2) % n}.
     */
    public OpenAddressingHashtableSimpleMap() {
        this((keyHashCode, tableLength, probe) -> (keyHashCode + probe * probe) % tableLength);
    }

    public OpenAddressingHashtableSimpleMap(HashFunction probing) {
        this(probing, 11, 0.75f);
    }

    public OpenAddressingHashtableSimpleMap(HashFunction probing, int initialCapacity, float loadFactor) {
        this.probing = Objects.requireNonNull(probing);
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
        int keyHash = keyHashCode(key);
        int num = table.length;
        for (int i = 0; i < num; i++) {
            int index = hash(keyHash, num, i);
            Entry<K, V> entry = table[index];
            if (entry == null) {
                return null;
            }
            if (!entry.match(key, keyHash)) {
                continue;
            }
            if (entry.deleted()) {
                return null;
            }
            return entry.value();
        }
        return null;
    }

    @Override
    public V put(K key, V value) {
        if (size >= threshold(table.length)) {
            grow();
        }
        int keyHash = keyHashCode(key);
        int num = table.length;
        for (int i = 0; i < num; i++) {
            int index = hash(keyHash, num, i);
            Entry<K, V> entry = table[index];
            if (entry == null) {
                entry = newEntry(key, keyHash);
                entry.value(value);
                table[index] = entry;
                size++;
                return null;
            }
            if (!entry.match(key, keyHash)) {
                continue;
            }
            if (entry.deleted()) {
                entry.deleted(false);
                size++;
            }
            return entry.value(value);
        }
        return null;
    }

    @Override
    public V remove(K key) {
        int keyHash = keyHashCode(key);
        int num = table.length;
        for (int i = 0; i < num; i++) {
            int index = hash(keyHash, num, i);
            Entry<K, V> entry = table[index];
            if (entry == null) {
                return null;
            }
            if (!entry.match(key, keyHash)) {
                continue;
            }
            if (entry.deleted()) {
                return null;
            }
            entry.deleted(true);
            size--;
            if (size < threshold(prevArrayLength(table.length))) {
                shrink();
            }
            return entry.value();
        }
        return null;
    }

    protected int hash(int keyHashCode, int tableLength, int probe) {
        return probing.hash(keyHashCode, tableLength, probe);
    }

    protected int keyHashCode(K key) {
        return Objects.requireNonNull(key).hashCode() & 0x7FFFFFFF;
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
        if (resLength <= size) { // just in case
            return;
        }
        Entry<K, V>[] res = newTable(resLength);
        resettle(this.table, thisLength, res, resLength);
        this.table = res;
    }

    protected void resettle(Entry<K, V>[] source, int sourceSize, Entry<K, V>[] target, int targetSize) {
        for (int i = 0; i < sourceSize; i++) {
            Entry<K, V> entry = source[i];
            if (entry == null || entry.deleted()) {
                continue;
            }
            insert(entry, Math.max(targetSize, sourceSize), target, targetSize);
        }
    }

    private void insert(Entry<K, V> entry, int probings, Entry<K, V>[] array, int length) {
        for (int j = 0; j < probings; j++) {
            int index = hash(entry.keyHash(), length, j);
            if (array[index] == null) {
                array[index] = entry;
                return;
            }
        }
        throw new IllegalStateException("Can't insert " + entry);
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
        private boolean deleted;

        private V value;

        protected Entry(K key, int keyHash) {
            this.key = Objects.requireNonNull(key);
            this.keyHash = keyHash;
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

        protected boolean deleted() {
            return deleted;
        }

        protected void deleted(boolean deleted) {
            this.deleted = deleted;
        }

        @SuppressWarnings("BooleanMethodIsAlwaysInverted")
        private boolean match(K key, int code) {
            return this.keyHash == code && this.key.equals(key);
        }

        @Override
        public String toString() {
            return String.format("Entry{key=%s, hash=%d, deleted=%s, value=%s}", key, keyHash, deleted, value);
        }
    }

    public interface HashFunction {
        /**
         * Calculates a table index.
         *
         * @param keyHashCode {@code int} an object hashcode
         * @param tableLength {@code int} a table length
         * @param probe       {@code int} a number of probe
         * @return {@code int}
         */
        int hash(int keyHashCode, int tableLength, int probe);
    }
}
