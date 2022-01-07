package com.gitlab.sszuev.misc;

import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnel;
import com.google.common.hash.Funnels;

import java.nio.charset.StandardCharsets;

/**
 * A wrapper for {@link BloomFilter Guava Bloom-Filter}.
 * Created by @ssz on 06.01.2022.
 *
 * @param <X> - anything
 */
@SuppressWarnings("UnstableApiUsage")
public class GuavaBloomFilter<X> implements SimpleBloomFilter<X> {
    private final BloomFilter<X> bloomFilter;

    protected GuavaBloomFilter(BloomFilter<X> bloomFilter) {
        this.bloomFilter = bloomFilter;
    }

    @Override
    public void add(X value) {
        bloomFilter.put(value);
    }

    @Override
    public boolean mightContain(X value) {
        return bloomFilter.mightContain(value);
    }

    @SuppressWarnings("SameParameterValue")
    public static <X> GuavaBloomFilter<X> createBloomFilter(Class<X> type, int capacity, double fpp) {
        return new GuavaBloomFilter<>(BloomFilter.create(createFunnel(type), capacity, fpp));
    }

    @SuppressWarnings("unchecked")
    static <X> Funnel<X> createFunnel(Class<X> type) {
        Funnel<?> res;
        if (type == String.class) {
            res = Funnels.stringFunnel(StandardCharsets.UTF_8);
        } else if (type == Integer.class) {
            res = Funnels.integerFunnel();
        } else if (type == Long.class) {
            res = Funnels.longFunnel();
        } else {
            throw new IllegalArgumentException("Unsupported type: " + type);
        }
        return (Funnel<X>) res;
    }
}
