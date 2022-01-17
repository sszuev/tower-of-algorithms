package com.gitlab.sszuev.misc;

import com.google.common.primitives.Ints;
import org.apache.commons.codec.digest.MurmurHash3;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;
import java.util.function.ToIntFunction;

/**
 * Created by @ssz on 06.01.2022.
 *
 * @param <K> anything
 * @see <a href='https://richardstartin.github.io/posts/building-a-bloom-filter-from-scratch'>Richard Startin's Blog: Building a Bloom Filter from Scratch</a>
 */
public class MyBloomFilter<K> implements SimpleBloomFilter<K> {
    private static final Random RANDOM = new Random();

    private final long[] bits;
    private final int numBits;
    private final ToIntFunction<K>[] functions;

    protected MyBloomFilter(long[] bits, int numBits, ToIntFunction<K>[] functions) {
        this.bits = bits;
        this.numBits = numBits;
        this.functions = functions;
    }

    /**
     * Creates the simplest bloom-filter with hash-functions based on {@code MurmurHash3_x86_32} algorithm.
     *
     * @param type               a {@code Class}-type of {@link X}
     * @param expectedInsertions the number of expected insertions to the constructed {@link MyBloomFilter}; must be positive
     * @param fpp                the desired false positive probability (must be positive and less than 1.0)
     * @param <X>                anything - a key type
     * @return a {@link MyBloomFilter} of {@link X}
     */
    public static <X> MyBloomFilter<X> createBloomFilterWithMurmur3Hash(Class<X> type,
                                                                        int expectedInsertions,
                                                                        double fpp) {
        Objects.requireNonNull(type, "Null key-type");
        checkProbability(fpp);
        checkExpectedItems(expectedInsertions);
        int bitsSize = optimalBitsSize(expectedInsertions, fpp);
        int optimalNumHashFunctions = optimalNumOfHashFunctions(expectedInsertions, bitsSize);
        long[] data = new long[bitsSize >>> 6];
        return new MyBloomFilter<>(data, bitsSize, createMurmur3HashFunctions(type, optimalNumHashFunctions));
    }

    /**
     * Creates the simplest bloom-filter with hash-functions directly based on java-hashcode.
     *
     * @param expectedInsertions the number of expected insertions to the constructed {@link MyBloomFilter}; must be positive
     * @param fpp                the desired false positive probability (must be positive and less than 1.0)
     * @param <X>                anything - a key type
     * @return a {@link MyBloomFilter} of {@link X}
     */
    public static <X> MyBloomFilter<X> createBloomFilterWithJavaHash(int expectedInsertions, double fpp) {
        checkProbability(fpp);
        checkExpectedItems(expectedInsertions);
        int bitsSize = optimalBitsSize(expectedInsertions, fpp);
        int optimalNumHashFunctions = optimalNumOfHashFunctions(expectedInsertions, bitsSize);
        long[] data = new long[bitsSize >>> 6];
        return new MyBloomFilter<>(data, bitsSize, createSimpleHashFunctions(optimalNumHashFunctions));
    }

    @SuppressWarnings("unchecked")
    private static <X> ToIntFunction<X>[] createSimpleHashFunctions(int numberOfHashFunctions) {
        ToIntFunction<X>[] functions = new ToIntFunction[numberOfHashFunctions];
        for (int i = 0; i < numberOfHashFunctions; i++) {
            int seed = RANDOM.nextInt();
            functions[i] = value -> {
                int h = value.hashCode() + seed;
                return h ^ (h >>> 16);
            };
        }
        return functions;
    }

    @SuppressWarnings("unchecked")
    private static <X> ToIntFunction<X>[] createMurmur3HashFunctions(Class<X> type, int numberOfHashFunctions) {
        ToIntFunction<X>[] functions = new ToIntFunction[numberOfHashFunctions];
        for (int i = 0; i < numberOfHashFunctions; i++) {
            int seed = RANDOM.nextInt();
            functions[i] = value -> {
                byte[] bytes = toByteArray(type, value);
                return MurmurHash3.hash32x86(bytes, 0, bytes.length, seed);
            };
        }
        return functions;
    }

    private static int optimalBitsSize(int expectedInsertions, double fpp) {
        long optimalNumOfBits = optimalNumOfBits(expectedInsertions, fpp);
        int numBits = Ints.checkedCast(Long.highestOneBit(optimalNumOfBits));
        if (numBits < 64) {
            numBits = 64;
        }
        return numBits;
    }

    private static void checkProbability(double fpp) {
        if (fpp > 0.0 && fpp < 1.0) {
            return;
        }
        throw new IllegalArgumentException("The probability must be in bounds (0, 1): " + fpp);
    }

    private static void checkExpectedItems(int expectedInsertions) {
        if (expectedInsertions <= 0) {
            throw new IllegalArgumentException("Non-positive insertions count: " + expectedInsertions);
        }
    }

    public static <X> byte[] toByteArray(Class<X> type, X value) {
        if (type == String.class) {
            return ((String) value).getBytes(StandardCharsets.UTF_8);
        }
        if (type == Integer.class) {
            int v = (Integer) value;
            return new byte[]{(byte) (v >>> 24), (byte) (v >>> 16), (byte) (v >>> 8), (byte) v};
        }
        if (type == Long.class) {
            long v = (Long) value;
            return new byte[]{
                    (byte) (v >>> 56), (byte) (v >>> 48), (byte) (v >>> 40), (byte) (v >> 32),
                    (byte) (v >>> 24), (byte) (v >>> 16), (byte) (v >>> 8), (byte) v};
        }
        throw new IllegalArgumentException("Unsupported type: " + type);
    }

    /**
     * Computes {@code m} (total bits of Bloom-filter) which is expected to achieve,
     * for the specified expected insertions, the required false positive probability.
     * FYI, for 3%, we always get 5 hash functions.
     * Copy-pasted from guava.
     *
     * @param n expected insertions (must be positive)
     * @param p false positive rate (must be 0 < p < 1)
     * @see <a href='http://en.wikipedia.org/wiki/Bloom_filter#Probability_of_false_positives'>wiki: Probability of false positives</a>
     * @see <a href='https://hur.st/bloomfilter/'>Bloom Filter Calculator</a>
     */
    public static long optimalNumOfBits(long n, double p) {
        if (p == 0) {
            p = Double.MIN_VALUE;
        }
        return (long) (-n * Math.log(p) / (Math.log(2) * Math.log(2)));
    }

    /**
     * Computes the optimal {@code k} (number of hashes per element inserted in Bloom-filter),
     * given the expected insertions and total number of bits in the Bloom filter.
     * Copy-pasted from guava.
     *
     * @param n expected insertions (must be positive)
     * @param m total number of bits in Bloom filter (must be positive)
     * @see <a href='http://en.wikipedia.org/wiki/File:Bloom_filter_fp_probability.svg'>Bloom_filter_fp_probability</a>
     * @see <a href='https://hur.st/bloomfilter/'>Bloom Filter Calculator</a>
     */
    public static int optimalNumOfHashFunctions(long n, long m) {
        // (m / n) * log(2), but avoid truncation due to division!
        return Math.max(1, (int) Math.round((double) m / n * Math.log(2)));
    }

    @Override
    public void add(K value) {
        for (ToIntFunction<K> function : functions) {
            int hash = mapHash(function.applyAsInt(value));
            bits[hash >>> 6] |= 1L << hash;
        }
    }

    @Override
    public boolean mightContain(K value) {
        for (ToIntFunction<K> function : functions) {
            int hash = mapHash(function.applyAsInt(value));
            if ((bits[hash >>> 6] & (1L << hash)) == 0) {
                return false;
            }
        }
        return true;
    }

    protected int mapHash(int hash) {
        return hash & (numBits - 1);
    }
}
