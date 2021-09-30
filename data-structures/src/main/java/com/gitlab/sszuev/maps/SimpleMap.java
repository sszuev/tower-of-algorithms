package com.gitlab.sszuev.maps;

/**
 * Created by @ssz on 22.09.2021.
 *
 * @param <K> the type of keys maintained by this map
 * @param <V> the type of mapped values
 * @see java.util.Map
 */
public interface SimpleMap<K, V> {

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     *
     * @param key {@link K} the key whose associated value is to be returned
     * @return {@link V} the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for {@code key}
     */
    V get(K key);

    /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old value is replaced by the specified value.
     *
     * @param key   {@link K} key with which the specified value is to be associated
     * @param value {@link V} value to be associated with the specified key
     * @return {@link V} the previous value associated with {@code key},
     * or {@code null} if there was no mapping for {@code key}
     */
    V put(K key, V value);

    /**
     * Removes the mapping for a key from this map if it is present.
     * Returns the value to which this map previously associated the key,
     * or {@code null} if the map contained no mapping for the key.
     * The map will not contain a mapping for the specified key once the call returns.
     *
     * @param key {@link K} key whose mapping is to be removed from the map
     * @return {@link V} the previous value associated with {@code key},
     * or {@code null} if there was no mapping for {@code key}
     */
    V remove(K key);

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return {@code long} non-negative
     */
    long size();
}
