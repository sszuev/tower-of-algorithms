package com.gitlab.sszuev.compression;

import com.gitlab.sszuev.compression.impl.JDKZipCodecImpl;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Created by @ssz on 11.12.2021.
 */
public class BinaryCodecTest {

    @Test
    public void testCompressDecompress() {
        testCompressDecompress(RandomStringUtils.randomAlphabetic(42_000));
    }

    private void testCompressDecompress(String str) {
        byte[] orig = str.getBytes(StandardCharsets.UTF_8);
        System.out.println("Given: " + orig.length);
        JDKZipCodecImpl codec = new JDKZipCodecImpl();
        byte[] res = codec.encode(orig);
        System.out.println("Encoded: " + res.length);
        Assertions.assertFalse(Arrays.equals(res, orig));

        byte[] actual = codec.decode(res);
        Assertions.assertArrayEquals(orig, actual);
    }
}
