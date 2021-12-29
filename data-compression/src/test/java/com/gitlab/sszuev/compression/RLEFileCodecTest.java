package com.gitlab.sszuev.compression;

import com.gitlab.sszuev.compression.impl.EnhancedRLECodecImpl;
import com.gitlab.sszuev.compression.impl.SimpleRLECodecImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;

/**
 * Created by @ssz on 21.12.2021.
 */
public class RLEFileCodecTest {
    @TempDir
    static Path tempDir;

    public static byte[] decompressWithSimpleRLE(byte[] compressed) {
        int length = 0;
        for (int i = 0; i < compressed.length; i += 2) {
            length += Byte.toUnsignedInt(compressed[i]);
        }
        byte[] decompress = new byte[length];
        int j = 0;
        for (int i = 0; i < compressed.length; i += 2) {
            int s = Byte.toUnsignedInt(compressed[i]);
            byte b = compressed[i + 1];
            Arrays.fill(decompress, j, j += s, b);
        }
        return decompress;
    }

    public static byte[] decompressWithOptimisedRLE(byte[] compressed) {
        int length = 0;
        for (int i = 0; i < compressed.length; ) {
            int series = compressed[i];
            if (series > 0) {
                length += series;
                i += 2;
            } else {
                length -= series;
                i += 1 - series;
            }
        }
        byte[] decompress = new byte[length];
        int j = 0;
        for (int i = 0; i < compressed.length; ) {
            int series = compressed[i];
            if (series > 0) {
                byte b = compressed[++i];
                Arrays.fill(decompress, j, j += series, b);
                i++;
            } else {
                System.arraycopy(compressed, ++i, decompress, j, -series);
                i -= series;
                j -= series;
            }
        }
        return decompress;
    }

    public static void assertOptRLECompressedData(byte[] expected, byte[] actual) {
        long e = calcOptRLESum(expected);
        long a = calcOptRLESum(actual);
        Assertions.assertEquals(e, a);
    }

    public static long calcOptRLESum(byte[] array) {
        long res = 0;
        for (int i = 0; i < array.length; ) {
            int series = array[i];
            if (series > 0) {
                long d = array[i + 1] * series;
                res += d;
                i += 2;
            } else {
                long sum = 0;
                for (int k = 0; k < -series; k++) {
                    sum += array[i + 1 + k];
                }
                res += sum;
                i += 1 - series;
            }
        }
        return res;
    }

    private static void testEncode(byte[] compressedData,
                                   UnaryOperator<byte[]> decompressor,
                                   FileCodec codec) throws IOException {
        byte[] givenRawData = decompressor.apply(compressedData);

        Path givenRaw = Files.createTempFile(tempDir, "raw", "tmp");
        Files.write(givenRaw, givenRawData);

        Path res = Files.createTempFile(tempDir, "compressed", "tmp");
        codec.encode(givenRaw, res);
        byte[] actualData = Files.readAllBytes(res);
        Assertions.assertArrayEquals(compressedData, actualData);
    }

    private static void testDecode(byte[] compressedData,
                                   UnaryOperator<byte[]> decompressor,
                                   FileCodec codec) throws IOException {
        byte[] expectedRawData = decompressor.apply(compressedData);

        Path compressedFile = Files.createTempFile(tempDir, "compressed", "tmp");
        Files.write(compressedFile, compressedData);

        Path res = Files.createTempFile(tempDir, "restored", "tmp");
        codec.decode(compressedFile, res);
        byte[] actualDecompressedData = Files.readAllBytes(res);
        Assertions.assertArrayEquals(expectedRawData, actualDecompressedData);
    }

    private static <CODEC extends FileCodec & BinaryCodec> void testEncode(Path source,
                                                                           UnaryOperator<byte[]> decompressor,
                                                                           BiConsumer<byte[], byte[]> assertResult,
                                                                           CODEC codec) throws IOException {
        byte[] realFileData = Files.readAllBytes(source);

        byte[] expectedCompressedData = codec.encode(realFileData);
        byte[] expectedRawData = decompressor.apply(expectedCompressedData);

        Assertions.assertArrayEquals(expectedRawData, realFileData);

        Path res = Files.createTempFile(tempDir, "compressed", "tmp");

        codec.encode(source, res);
        byte[] actualCompressedData = Files.readAllBytes(res);
        assertResult.accept(expectedCompressedData, actualCompressedData);
    }

    private static <CODEC extends FileCodec & BinaryCodec> void testDecode(Path source,
                                                                           UnaryOperator<byte[]> decompressor,
                                                                           CODEC codec) throws IOException {
        byte[] realFileData = Files.readAllBytes(source);

        byte[] compressedData = codec.encode(realFileData);
        byte[] expectedRawData = decompressor.apply(compressedData);

        Assertions.assertArrayEquals(expectedRawData, realFileData);

        Path compressed = Files.createTempFile(tempDir, "compressed", "tmp");
        Files.write(compressed, compressedData);

        Path res = Files.createTempFile(tempDir, "decompressed", "tmp");

        codec.decode(compressed, res);
        byte[] actualDecompressedData = Files.readAllBytes(res);

        Assertions.assertArrayEquals(realFileData, actualDecompressedData);
    }

    @Nested
    class SmallFileNaiveRLETest {
        @Test
        public void testCompress() throws IOException {
            // '33'x120, '42'x297, '11'x120
            byte[] data = new byte[]{120, 33, (byte) 255, 42, 42, 42, 120, 11};
            RLEFileCodecTest.testEncode(data, RLEFileCodecTest::decompressWithSimpleRLE, new SimpleRLECodecImpl());
        }

        @Test
        public void testDecompress() throws IOException {
            // '33'x120, '42'x297, '11'x120
            byte[] data = new byte[]{120, 33, (byte) 255, 42, 42, 42, 120, 11};
            RLEFileCodecTest.testDecode(data, RLEFileCodecTest::decompressWithSimpleRLE, new SimpleRLECodecImpl());
        }
    }

    @Nested
    public class RealFileNaiveRLETest {
        @Test
        public void testCompress() throws IOException {
            testEncode(DataProvider.TEXT_HTML.path(),
                    RLEFileCodecTest::decompressWithSimpleRLE,
                    Assertions::assertArrayEquals,
                    new SimpleRLECodecImpl());
        }

        @Test
        public void testDecompress() throws IOException {
            testDecode(DataProvider.TEXT_HTML.path(), RLEFileCodecTest::decompressWithSimpleRLE, new SimpleRLECodecImpl());
        }
    }

    @Nested
    class SmallFileOptimizedRLETest {
        @Test
        public void testCompress() throws IOException {
            // '33'x120, '42'x169, '11'x120, {'89','127','-128','0'}, '42'x4 = 417
            byte[] data = new byte[]{120, 33, 127, 42, 42, 42, 120, 11, -4, 89, 127, -128, 0, 4, 42};
            testEncode(data, RLEFileCodecTest::decompressWithOptimisedRLE, new EnhancedRLECodecImpl());
        }

        @Test
        public void testDecompress() throws IOException {
            // '33'x12, {'89','127','-128','0'}, '42'x84, '11'x11, '42'x25, {1, 2}, '42'x4 = 142
            byte[] data = new byte[]{12, 12, -4, 89, 127, -128, 0, 84, 42, 11, 11, 25, 42, -2, 1, 2, 4, 42};
            testDecode(data, RLEFileCodecTest::decompressWithOptimisedRLE, new EnhancedRLECodecImpl());
        }
    }

    @Nested
    class RealFileOptimizedRLETest {
        @Test
        public void testCompress() throws IOException {
            testEncode(DataProvider.TEXT_HTML.path(),
                    RLEFileCodecTest::decompressWithOptimisedRLE,
                    RLEFileCodecTest::assertOptRLECompressedData,
                    new EnhancedRLECodecImpl());
        }

        @Test
        public void testDecompress() throws IOException {
            testDecode(DataProvider.TEXT_HTML.path(),
                    RLEFileCodecTest::decompressWithOptimisedRLE,
                    new EnhancedRLECodecImpl());
        }
    }

}
