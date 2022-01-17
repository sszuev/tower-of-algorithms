package com.gitlab.sszuev.misc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;

/**
 * Created by @ssz on 07.01.2022.
 */
public class SimpleBloomFilterTest {

    @ParameterizedTest
    @EnumSource(Data.class)
    public void testStringFilter(Data factory) {
        SimpleBloomFilter<String> filter = factory.createStringFilter(10, 0.03);

        filter.add("XXX");
        Assertions.assertTrue(filter.mightContain("XXX"));
        Assertions.assertFalse(filter.mightContain("YYY"));
        Assertions.assertFalse(filter.mightContain("XXx"));

        filter.add("YYY");
        Assertions.assertTrue(filter.mightContain("XXX"));
        Assertions.assertTrue(filter.mightContain("YYY"));
        Assertions.assertFalse(filter.mightContain("XXx"));
    }

    @ParameterizedTest
    @EnumSource(Data.class)
    public void testIntegerFilter(Data factory) {
        SimpleBloomFilter<Integer> filter = factory.createIntFilter(10, 0.3);

        List<Integer> data = List.of(1, 2, 3, 4, 7, 8, 9, 10);
        for (Integer d : data) {
            Assertions.assertFalse(filter.mightContain(d));
        }
        Assertions.assertFalse(filter.mightContain(5));
        Assertions.assertFalse(filter.mightContain(6));

        for (Integer d : data) {
            filter.add(d);
            Assertions.assertTrue(filter.mightContain(d));
        }
        Assertions.assertFalse(filter.mightContain(5));
        Assertions.assertFalse(filter.mightContain(6));
    }

    enum Data {
        GUAVA_IMPL {
            @Override
            public SimpleBloomFilter<String> createStringFilter(int itemsNumber, double fpp) {
                return GuavaBloomFilter.createBloomFilter(String.class, itemsNumber, fpp);
            }

            @Override
            public SimpleBloomFilter<Integer> createIntFilter(int itemsNumber, double fpp) {
                return GuavaBloomFilter.createBloomFilter(Integer.class, itemsNumber, fpp);
            }
        },
        MURMUR3_HASH {
            @Override
            public SimpleBloomFilter<String> createStringFilter(int itemsNumber, double fpp) {
                return MyBloomFilter.createBloomFilterWithMurmur3Hash(String.class, itemsNumber, fpp);
            }

            @Override
            public SimpleBloomFilter<Integer> createIntFilter(int itemsNumber, double fpp) {
                return MyBloomFilter.createBloomFilterWithMurmur3Hash(Integer.class, itemsNumber, fpp);
            }
        },
        SIMPLE_JAVA_HASH {
            @Override
            public SimpleBloomFilter<String> createStringFilter(int itemsNumber, double fpp) {
                return MyBloomFilter.createBloomFilterWithJavaHash(itemsNumber, fpp);
            }

            @Override
            public SimpleBloomFilter<Integer> createIntFilter(int itemsNumber, double fpp) {
                return MyBloomFilter.createBloomFilterWithJavaHash(itemsNumber, fpp);
            }
        },
        ;

        public abstract SimpleBloomFilter<String> createStringFilter(int itemsNumber, double fpp);

        public abstract SimpleBloomFilter<Integer> createIntFilter(int itemsNumber, double fpp);
    }
}
