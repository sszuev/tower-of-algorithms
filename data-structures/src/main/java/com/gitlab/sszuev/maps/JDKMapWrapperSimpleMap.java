package com.gitlab.sszuev.maps;

import java.util.Map;
import java.util.Objects;

/**
 * A simple wrapper for {@link java.util.Map JDK Map}
 * Created by @ssz on 26.09.2021.
 *
 * @see SimpleMap
 * @see java.util.Map
 */
public class JDKMapWrapperSimpleMap<K, V> implements SimpleMap<K, V> {

    private final Map<K, V> map;

    public JDKMapWrapperSimpleMap(Map<K, V> map) {
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
