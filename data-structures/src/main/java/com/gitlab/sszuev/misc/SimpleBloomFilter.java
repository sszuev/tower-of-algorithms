package com.gitlab.sszuev.misc;

/**
 * A simple Bloom-Filter.
 * <p>
 * Created by @ssz on 06.01.2022.
 *
 * @param <X> - anything
 * @see <a href='https://en.wikipedia.org/wiki/Bloom_filter'>wiki: Bloom Filter</a>
 * @see <a href='https://www.jasondavies.com/bloomfilter/'>Bloom Filter Visualization</a>
 * @see <a href='https://hur.st/bloomfilter/'>Bloom Filter Calculator</a>
 */
public interface SimpleBloomFilter<X> {

    /**
     * Puts an element into this {@code SimpleBloomFilter}.
     * Ensures that subsequent invocations of {@link #mightContain(Object)} with the same element will always return {@code true}.
     *
     * @param value {@link X} to add
     */
    void add(X value);

    /**
     * Returns {@code true} if the element <i>might</i> have been put in this filter,
     * {@code false} if this is <i>definitely</i> not the case.
     *
     * @param value {@link X} to test
     * @return {@code boolean}
     */
    boolean mightContain(X value);
}
