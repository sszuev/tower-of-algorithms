package com.gitlab.sszuev.maps;

import com.gitlab.sszuev.misc.SimpleBloomFilter;

import java.util.Map;
import java.util.Objects;

/**
 * A {@link SimpleMap} implementation that uses {@link SimpleBloomFilter Bloom-Filter} internally
 * as optimization for operation {@link SimpleMap#get(Object)}.
 * As the following benchmarks show, this may indeed provide some profit for large data:
 * <pre>{@code
 * Benchmark                                    (factory)   Mode  Cnt    Score     Error  Units
 * BigMapBenchmark.GET                  STANDARD_HASH_MAP  thrpt    5  806,035 ? 375,992  ops/s
 * BigMapBenchmark.GET    GUAVA_BLOOM_FILTER_OPT_HASH_MAP  thrpt    5  508,903 ? 207,563  ops/s
 * BigMapBenchmark.GET  MURMUR3_BLOOM_FILTER_OPT_HASH_MAP  thrpt    5  408,421 ? 128,082  ops/s
 * BigMapBenchmark.GET   SIMPLE_BLOOM_FILTER_OPT_HASH_MAP  thrpt    5  820,163 ? 281,077  ops/s
 * BigMapBenchmark.GET                  STANDARD_TREE_MAP  thrpt    5  313,074 ?  40,370  ops/s
 * BigMapBenchmark.GET    GUAVA_BLOOM_FILTER_OPT_TREE_MAP  thrpt    5  275,226 ?  47,263  ops/s
 * BigMapBenchmark.GET  MURMUR3_BLOOM_FILTER_OPT_TREE_MAP  thrpt    5  234,258 ?  19,788  ops/s
 * BigMapBenchmark.GET   SIMPLE_BLOOM_FILTER_OPT_TREE_MAP  thrpt    5  351,889 ?  90,291  ops/s
 * }</pre>
 * Created by @ssz on 06.01.2022.
 */
public class BigSimpleMap<K, V> implements SimpleMap<K, V> {
    private final SimpleBloomFilter<K> filter;
    private final Map<K, V> map;

    public BigSimpleMap(Map<K, V> map, SimpleBloomFilter<K> filter) {
        this.filter = Objects.requireNonNull(filter);
        this.map = Objects.requireNonNull(map);
    }

    @Override
    public V get(K key) {
        if (!filter.mightContain(key)) {
            return null;
        }
        return map.get(key);
    }

    @Override
    public V put(K key, V value) {
        filter.add(key);
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
