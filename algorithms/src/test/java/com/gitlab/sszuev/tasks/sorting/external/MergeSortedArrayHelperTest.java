package com.gitlab.sszuev.tasks.sorting.external;

import com.gitlab.sszuev.utils.BufferUtils;
import com.gitlab.sszuev.utils.CharsUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by @ssz on 08.09.2021.
 */
@Disabled("a technical tests (for manual running)")
public class MergeSortedArrayHelperTest {

    private static final Random RANDOM = new Random();

    public static Stream<Data> listData() {
        return Stream.of(
                randomData(420, 810, 19, 21),
                randomData(32, 10, 42, 21),
                randomData(500, 500, 42, 1),
                randomData(50, 50, 1, 42));
    }

    static Data randomData(int boundLeft, int boundRight, int sizeLeft, int sizeRight) {
        char[] left = CharsUtils.generateSortedArray(RANDOM, boundLeft, sizeLeft);
        char[] right = CharsUtils.generateSortedArray(RANDOM, boundRight, sizeRight);
        Data data = new Data();
        data.arr = CharsUtils.concat(left, right);
        data.middle = sizeLeft;
        return data;
    }

    @BeforeEach
    public void before() {
        System.out.println("====".repeat(42));
    }

    @ParameterizedTest
    @MethodSource(value = {"listData"})
    public void testSimpleMergeArrays(Data data) {
        testMergeArrays(data, MergeHalfSortedArrayHelper::mergeParts);
    }

    @ParameterizedTest
    @MethodSource(value = {"listData"})
    public void testGapMergeArrays(Data data) {
        testMergeArrays(data, (a, m) -> MergeHalfSortedArrayHelper.mergeParts(a));
    }

    private void testMergeArrays(Data data, BiFunction<char[], Integer, char[]> func) {
        System.out.println("G" + CharsUtils.toString(data.arr));
        char[] sorted = CharsUtils.toSorted(data.arr);
        System.out.println("E" + CharsUtils.toString(sorted));
        char[] actual = func.apply(data.arr, data.middle);
        System.out.println("A" + CharsUtils.toString(actual));
        Assertions.assertArrayEquals(sorted, actual);
    }

    @Test
    public void testMergeOddFile() throws IOException {
        Path file = generateHalfSortedRandomFile(getClass().getSimpleName() + "-odd-",
                42, (char) 2222, 22, (char) 43434, true);
        testMergeHalfSortedFileDirectly(file);
    }

    @Test
    public void testMergeEvenFile() throws IOException {
        Path file = generateHalfSortedRandomFile(getClass().getSimpleName() + "-even-",
                22, (char) (Character.MAX_VALUE / 2), 42, (char) 4434, false);
        testMergeHalfSortedFileDirectly(file);
    }

    @ParameterizedTest
    @ValueSource(ints = {
            42_000, // in memory O(n)
            100,    // in memory O(n*log(n))
            42      // in file
    })
    public void testMergeFile(int upperLimit) throws IOException {
        Charset charset = StandardCharsets.UTF_8;
        String firstData = "HLL";
        String lastData = "WRD";
        int leftSize = 21;
        int rightSize = 21;

        ByteBuffer middle = generateHalfSortedRandomFileContent(leftSize, (char) 100, rightSize, (char) 100, false);
        ByteBuffer start = ByteBuffer.wrap(firstData.getBytes(charset));
        ByteBuffer end = ByteBuffer.wrap(lastData.getBytes(charset));

        char[] expectedMiddleData = new char[middle.capacity() / 2];
        BufferUtils.copy(middle, CharBuffer.wrap(expectedMiddleData));
        System.out.println("G" + CharsUtils.toString(expectedMiddleData));
        Arrays.sort(expectedMiddleData);
        System.out.println("E" + CharsUtils.toString(expectedMiddleData));

        ByteBuffer res = ByteBuffer.allocate(start.capacity() + middle.capacity() + end.capacity());
        res.put(start);
        res.put(middle);
        res.put(end);

        Path file = Files.createTempFile(getClass().getSimpleName() + "-testMergeFile-", ".bin");
        Files.write(file, res.array());

        try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(file,
                StandardOpenOption.WRITE, StandardOpenOption.READ)) {
            long leftStartIndex = start.capacity();
            long rightStartIndex = start.capacity() + leftSize * 2;
            long rightEndIndex = start.capacity() + (leftSize + rightSize) * 2 - 1;
            MergeHalfSortedArrayHelper.merge(channel, upperLimit, leftStartIndex, rightStartIndex, rightEndIndex);
        }

        byte[] actual = ByteBuffer.wrap(Files.readAllBytes(file)).array();
        String actualPrefix = new String(actual, 0, firstData.length(), charset);
        Assertions.assertEquals(firstData, actualPrefix);
        String actualSuffix = new String(actual, actual.length - lastData.length(), lastData.length(), charset);
        Assertions.assertEquals(lastData, actualSuffix);

        byte[] middleDataBytes = new byte[actual.length - lastData.length() - firstData.length()];
        System.arraycopy(actual, firstData.length(), middleDataBytes, 0, middleDataBytes.length);
        char[] actualMiddleData = new char[middleDataBytes.length / 2];
        BufferUtils.copy(ByteBuffer.wrap(middleDataBytes), CharBuffer.wrap(actualMiddleData));
        System.out.println("A" + CharsUtils.toString(actualMiddleData));
        Assertions.assertArrayEquals(expectedMiddleData, actualMiddleData);
    }

    @Test
    public void testMergeWithTemporaryFile() throws IOException {
        long bufferSize = 32;
        char[] left = new char[]{5, 11, 13, 18, 19, 21, 24, 25, 27, 28, 36, 39, 39, 83, 98}; // 15
        char[] right = new char[]{4, 6, 11, 15, 27, 35, 46, 53, 65, 72, 82, 82, 91, 93}; // 14
        char[] given = new char[left.length + right.length]; // 29
        System.arraycopy(left, 0, given, 0, left.length);
        System.arraycopy(right, 0, given, left.length, right.length);
        ByteBuffer res = ByteBuffer.allocate((left.length + right.length) * 2 - 1);
        BufferUtils.copy(CharBuffer.wrap(given), res);
        System.out.println("R" + Arrays.toString(res.array()));
        System.out.println("G" + CharsUtils.toString(given));

        Path file = Files.createTempFile(getClass().getSimpleName() + "-testMergeWithTemporaryFile-", ".bin");
        Files.write(file, res.array());

        try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(file,
                StandardOpenOption.WRITE, StandardOpenOption.READ)) {
            long leftStartIndex = 0;
            long rightStartIndex = res.limit() - right.length * 2;
            long rightEndIndex = res.limit() - 1;
            MergeHalfSortedArrayHelper.mergeWithTemporaryFile(channel, bufferSize, leftStartIndex, rightStartIndex, rightEndIndex);
        }

        char[] expected = Arrays.copyOf(given, given.length);
        Arrays.sort(expected);
        System.out.println("E" + CharsUtils.toString(expected));

        byte[] actualBytes = ByteBuffer.wrap(Files.readAllBytes(file)).array();
        char[] actualChars = BufferUtils.toCharBuffer(ByteBuffer.wrap(actualBytes)).array();
        System.out.println("A" + CharsUtils.toString(actualChars));
        Assertions.assertArrayEquals(expected, actualChars);
    }

    private void testMergeHalfSortedFileDirectly(Path file) throws IOException {
        char[] content = BufferUtils.toCharBuffer(ByteBuffer.wrap(Files.readAllBytes(file))).array();
        System.out.println("G" + CharsUtils.toString(content));
        char[] sorted = CharsUtils.toSorted(content);
        System.out.println("E" + CharsUtils.toString(sorted));

        try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(file,
                StandardOpenOption.WRITE, StandardOpenOption.READ)) {
            MergeHalfSortedArrayHelper.mergeParts(channel);
        }
        char[] actual = BufferUtils.toCharBuffer(ByteBuffer.wrap(Files.readAllBytes(file))).array();
        System.out.println("A" + CharsUtils.toString(actual));
        Assertions.assertArrayEquals(sorted, actual);
    }

    private static Path generateHalfSortedRandomFile(String prefix,
                                                     int leftSize, char leftBound,
                                                     int rightSize, char rightBound,
                                                     boolean odd) throws IOException {
        ByteBuffer res = generateHalfSortedRandomFileContent(leftSize, leftBound, rightSize, rightBound, odd);
        Path file = Files.createTempFile(prefix, ".bin");
        Files.write(file, res.array());
        return file;
    }

    private static ByteBuffer generateHalfSortedRandomFileContent(int leftSize, char leftBound,
                                                                  int rightSize, char rightBound,
                                                                  boolean odd) {
        char[] left = odd ? CharsUtils.toCharArray(IntStream.concat(IntStream.of(42),
                IntStream.generate(() -> RANDOM.nextInt(leftBound)).limit(leftSize - 1)).sorted(), leftSize)
                : CharsUtils.generateSortedArray(RANDOM, leftBound, leftSize);
        char[] right = CharsUtils.generateSortedArray(RANDOM, rightBound, rightSize);
        ByteBuffer res = ByteBuffer.allocate((left.length + right.length) * 2 - (odd ? 1 : 0));
        if (odd) {
            res.put((byte) left[0]);
        }
        for (int i = odd ? 1 : 0; i < leftSize; i++) {
            res.putChar(left[i]);
        }
        for (char c : right) {
            res.putChar(c);
        }
        res.rewind();
        return res;
    }

    static class Data {
        char[] arr;
        int middle;

        @Override
        public String toString() {
            return CharsUtils.toString(arr);
        }
    }

}
