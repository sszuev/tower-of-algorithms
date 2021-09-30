package com.gitlab.sszuev.maps;

import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * A simple wrapper for {@link java.util.TreeMap JDK TreeMap}, which implements red-black-tree mechanism.
 * Created by @ssz on 26.09.2021.
 *
 * @see java.util.TreeMap
 * @see <a href='https://en.wikipedia.org/wiki/Red%E2%80%93black_tree'>red-black-tree: wiki</a>
 * @see <a href='https://www.cs.usfca.edu/~galles/visualization/RedBlack.html'>red-black-tree: visualization</a>
 */
public class JDKTreeSimpleMap<K, V> implements SimpleMap<K, V> {

    private final Map<K, V> map;

    @SuppressWarnings("SortedCollectionWithNonComparableKeys")
    public JDKTreeSimpleMap() {
        this(new TreeMap<>());
    }

    public JDKTreeSimpleMap(Comparator<K> comparator) {
        this(new TreeMap<>(comparator));
    }

    protected JDKTreeSimpleMap(TreeMap<K, V> map) {
        this.map = Objects.requireNonNull(map);
    }

    @Override
    public V get(K key) {
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        return map.put(key, value);
    }

    @Override
    public V remove(K key) {
        return map.remove(key);
    }

    @Override
    public long size() {
        return map.size();
    }
}
