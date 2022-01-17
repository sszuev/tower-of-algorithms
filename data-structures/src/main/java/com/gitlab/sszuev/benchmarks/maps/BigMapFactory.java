package com.gitlab.sszuev.benchmarks.maps;

import com.gitlab.sszuev.maps.BigSimpleMap;
import com.gitlab.sszuev.maps.JDKMapWrapperSimpleMap;
import com.gitlab.sszuev.maps.SimpleMap;
import com.gitlab.sszuev.misc.GuavaBloomFilter;
import com.gitlab.sszuev.misc.MyBloomFilter;
import com.gitlab.sszuev.misc.SimpleBloomFilter;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.IntFunction;

/**
 * Created by @ssz on 06.01.2022.
 */
@SuppressWarnings("unused")
public enum BigMapFactory {
    /*
     * HashMap:
     */
    STANDARD_HASH_MAP {
        @Override
        <V> SimpleMap<Integer, V> createIntegerMap(Function<Integer, V> getValue, Integer... keys) {
            return create(getValue, keys);
        }

        @Override
        <V> SimpleMap<String, V> createStringMap(Function<String, V> getValue, String... keys) {
            return create(getValue, keys);
        }

        @SafeVarargs
        private <K, V> SimpleMap<K, V> create(Function<K, V> getValue, K... keys) {
            return BigMapFactory.createJDKBigMap(HashMap::new, getValue, keys);
        }
    },
    GUAVA_BLOOM_FILTER_OPT_HASH_MAP {
        @Override
        <V> SimpleMap<Integer, V> createIntegerMap(Function<Integer, V> getValue, Integer... keys) {
            return create(Integer.class, getValue, keys);
        }

        @Override
        <V> SimpleMap<String, V> createStringMap(Function<String, V> getValue, String... keys) {
            return create(String.class, getValue, keys);
        }

        @SafeVarargs
        private <K, V> SimpleMap<K, V> create(Class<K> type, Function<K, V> getValue, K... keys) {
            return BigMapFactory.createBigMapWithFilter(HashMap::new, GuavaBloomFilter::createBloomFilter, type, getValue, keys);
        }
    },
    MURMUR3_BLOOM_FILTER_OPT_HASH_MAP {
        @Override
        <V> SimpleMap<Integer, V> createIntegerMap(Function<Integer, V> getValue, Integer... keys) {
            return create(Integer.class, getValue, keys);
        }

        @Override
        <V> SimpleMap<String, V> createStringMap(Function<String, V> getValue, String... keys) {
            return create(String.class, getValue, keys);
        }

        @SafeVarargs
        private <K, V> SimpleMap<K, V> create(Class<K> type, Function<K, V> getValue, K... keys) {
            return BigMapFactory.createBigMapWithFilter(HashMap::new, MyBloomFilter::createBloomFilterWithMurmur3Hash, type, getValue, keys);
        }
    },
    SIMPLE_BLOOM_FILTER_OPT_HASH_MAP {
        @Override
        <V> SimpleMap<Integer, V> createIntegerMap(Function<Integer, V> getValue, Integer... keys) {
            return create(Integer.class, getValue, keys);
        }

        @Override
        <V> SimpleMap<String, V> createStringMap(Function<String, V> getValue, String... keys) {
            return create(String.class, getValue, keys);
        }

        @SafeVarargs
        private <K, V> SimpleMap<K, V> create(Class<K> type, Function<K, V> getValue, K... keys) {
            return BigMapFactory.createBigMapWithFilter(HashMap::new,
                    (t, n, p) -> MyBloomFilter.createBloomFilterWithJavaHash(n, p), type, getValue, keys);
        }
    },

    /*
     * TreeMAp:
     */
    STANDARD_TREE_MAP {
        @Override
        <V> SimpleMap<Integer, V> createIntegerMap(Function<Integer, V> getValue, Integer... keys) {
            return create(getValue, keys);
        }

        @Override
        <V> SimpleMap<String, V> createStringMap(Function<String, V> getValue, String... keys) {
            return create(getValue, keys);
        }

        @SafeVarargs
        private <K, V> SimpleMap<K, V> create(Function<K, V> getValue, K... keys) {
            return BigMapFactory.createJDKBigMap(x -> new TreeMap<>(), getValue, keys);
        }
    },
    GUAVA_BLOOM_FILTER_OPT_TREE_MAP {
        @Override
        <V> SimpleMap<Integer, V> createIntegerMap(Function<Integer, V> getValue, Integer... keys) {
            return create(Integer.class, getValue, keys);
        }

        @Override
        <V> SimpleMap<String, V> createStringMap(Function<String, V> getValue, String... keys) {
            return create(String.class, getValue, keys);
        }

        @SafeVarargs
        private <K, V> SimpleMap<K, V> create(Class<K> type, Function<K, V> getValue, K... keys) {
            return BigMapFactory.createBigMapWithFilter(x -> new TreeMap<>(), GuavaBloomFilter::createBloomFilter, type, getValue, keys);
        }
    },
    MURMUR3_BLOOM_FILTER_OPT_TREE_MAP {
        @Override
        <V> SimpleMap<Integer, V> createIntegerMap(Function<Integer, V> getValue, Integer... keys) {
            return create(Integer.class, getValue, keys);
        }

        @Override
        <V> SimpleMap<String, V> createStringMap(Function<String, V> getValue, String... keys) {
            return create(String.class, getValue, keys);
        }

        @SafeVarargs
        private <K, V> SimpleMap<K, V> create(Class<K> type, Function<K, V> getValue, K... keys) {
            return BigMapFactory.createBigMapWithFilter(x -> new TreeMap<>(), MyBloomFilter::createBloomFilterWithMurmur3Hash, type, getValue, keys);
        }
    },
    SIMPLE_BLOOM_FILTER_OPT_TREE_MAP {
        @Override
        <V> SimpleMap<Integer, V> createIntegerMap(Function<Integer, V> getValue, Integer... keys) {
            return create(Integer.class, getValue, keys);
        }

        @Override
        <V> SimpleMap<String, V> createStringMap(Function<String, V> getValue, String... keys) {
            return create(String.class, getValue, keys);
        }

        @SafeVarargs
        private <K, V> SimpleMap<K, V> create(Class<K> type, Function<K, V> getValue, K... keys) {
            return BigMapFactory.createBigMapWithFilter(x -> new TreeMap<>(),
                    (t, n, p) -> MyBloomFilter.createBloomFilterWithJavaHash(n, p), type, getValue, keys);
        }
    },
    ;

    abstract <V> SimpleMap<Integer, V> createIntegerMap(Function<Integer, V> getValue, Integer... keys);

    abstract <V> SimpleMap<String, V> createStringMap(Function<String, V> getValue, String... keys);

    @SafeVarargs
    private static <K, V> SimpleMap<K, V> createBigMapWithFilter(IntFunction<Map<K, V>> innerMapFactory,
                                                                 SimpleBloomFilterProvider<K> filterFactory,
                                                                 Class<K> type,
                                                                 Function<K, V> getValue,
                                                                 K... keys) {
        SimpleBloomFilter<K> filter = filterFactory.get(type, keys.length, 0.01);
        SimpleMap<K, V> map = new BigSimpleMap<>(innerMapFactory.apply(keys.length), filter);
        for (K k : keys) {
            map.put(k, getValue.apply(k));
        }
        return map;
    }

    @SafeVarargs
    private static <K, V> SimpleMap<K, V> createJDKBigMap(IntFunction<Map<K, V>> innerMapFactory,
                                                          Function<K, V> getValue,
                                                          K... keys) {
        Map<K, V> res = innerMapFactory.apply(keys.length);
        for (K k : keys) {
            res.put(k, getValue.apply(k));
        }
        return new JDKMapWrapperSimpleMap<>(res);
    }

    interface SimpleBloomFilterProvider<X> {
        SimpleBloomFilter<X> get(Class<X> type, int items, double fpp);
    }
}
