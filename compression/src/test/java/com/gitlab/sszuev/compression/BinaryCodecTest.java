package com.gitlab.sszuev.compression;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by @ssz on 11.12.2021.
 */
public class BinaryCodecTest {

    @ParameterizedTest
    @EnumSource(CodecFactory.class)
    public void testCompressDecompressRandomString(CodecFactory factory) {
        String message = RandomStringUtils.randomAlphabetic(42_000);
        testCompressDecompress(factory, message);
    }

    @ParameterizedTest
    @EnumSource(CodecFactory.class)
    public void testCompressDecompressFixedString(CodecFactory factory) {
        testCompressDecompress(factory, 'a', 'b', 'c', 'd', 'd', 'd', 'e', 'e', 'e', 'e', 'e', 'f', 'f');

        testCompressDecompress(factory, 42);

        testCompressDecompress(factory, 42, 42, 42, 42, 42, 42, 42, 42, 42);

        testCompressDecompress(factory, "XXX".repeat(42) + "YYY".repeat(42_000) + "ZZZ".repeat(42));
    }

    private void testCompressDecompress(CodecFactory factory, String given) {
        testCompressDecompress(factory, given.getBytes(StandardCharsets.UTF_8));
    }

    private void testCompressDecompress(CodecFactory factory, int... given) {
        byte[] array = new byte[given.length];
        for (int i = 0; i < given.length; i++) {
            array[i] = (byte) given[i];
        }
        testCompressDecompress(factory, array);
    }

    private void testCompressDecompress(CodecFactory factory, byte[] given) {
        System.out.println("Given: " + given.length);
        BinaryCodec codec = factory.createCodec();
        byte[] res = codec.encode(given);
        System.out.println("Encoded: " + res.length);
        Assertions.assertFalse(Arrays.equals(res, given));

        byte[] actual = codec.decode(res);
        Assertions.assertArrayEquals(given, actual);
    }
}
