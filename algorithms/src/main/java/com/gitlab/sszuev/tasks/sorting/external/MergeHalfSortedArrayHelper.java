package com.gitlab.sszuev.tasks.sorting.external;

import com.gitlab.sszuev.utils.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * A helper that provides functionality for merging adjacent sorted {@code char}-arrays stored in a single file.
 * <p>
 * Created by @ssz on 11.09.2021.
 */
public class MergeHalfSortedArrayHelper {

    private static final int IO_TIMEOUT_IN_SECONDS = 5;

    /**
     * Merges two sorted parts of a file using different approaches: in-memory, partially in-memory, directly in file.
     * Uses first approach if the file parts are small enough to fit in memory twice.
     * Uses the second approach, if file parts can be read into memory wholly,
     * but there is no additional memory to hold sorted result.
     * The third way is used in any other case.
     *
     * @param channel           {@link AsynchronousFileChannel} - a file channel, not {@code null}
     * @param upperLimitInBytes {@code long} an upper memory limit, this number is used to select the sorting method
     * @param leftStartIndex    {@code long}, start index in bytes
     * @param rightStartIndex   {@code long}, start index of a second sorted part in bytes
     * @param rightEndIndex     {@code long}, end index in bytes
     */
    public static void merge(AsynchronousFileChannel channel,
                             long upperLimitInBytes,
                             long leftStartIndex,
                             long rightStartIndex,
                             long rightEndIndex) throws IOException {
        long length = rightEndIndex - leftStartIndex + 1;
        // case 1: put everything in mem, sort using additional memory with O(N) time complexity, write everything down
        if (length < upperLimitInBytes / 2) {
            mergeWithAdditionalMemory(channel, leftStartIndex, rightStartIndex, rightEndIndex);
            return;
        }
        // case 2: put everything in mem, sort without additional memory with O(N * log(N)) complexity, write down
        if (length < upperLimitInBytes) {
            mergeWithoutAdditionalMemory(channel, leftStartIndex, rightEndIndex);
            return;
        }
        // case 3: do not use memory for sorting, read and write directly from file
        mergeDirectly(channel, leftStartIndex, rightEndIndex);
    }

    private static void mergeWithAdditionalMemory(AsynchronousFileChannel channel,
                                                  long leftStartIndex,
                                                  long rightStartIndex,
                                                  long rightEndIndex) throws IOException {
        long length = rightEndIndex - leftStartIndex + 1;
        if (length >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        long delimiter = (rightStartIndex - leftStartIndex) / 2;
        if (delimiter >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        ByteBuffer bytes = ByteBuffer.allocate((int) length);
        read(channel, bytes, leftStartIndex);
        bytes.rewind();

        CharBuffer chars = BufferUtils.toCharBuffer(bytes);
        char[] sorted = mergeParts(chars.array(), (int) delimiter);

        BufferUtils.copy(CharBuffer.wrap(sorted), bytes);
        bytes.rewind();
        write(channel, bytes, leftStartIndex);
    }

    private static void mergeWithoutAdditionalMemory(AsynchronousFileChannel channel,
                                                     long leftStartIndex,
                                                     long rightEndIndex) throws IOException {
        long length = rightEndIndex - leftStartIndex + 1;
        if (length >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        ByteBuffer bytes = ByteBuffer.allocate((int) length);
        read(channel, bytes, leftStartIndex);
        bytes.rewind();

        CharBuffer chars = BufferUtils.toCharBuffer(bytes);
        mergeParts(chars.array());

        BufferUtils.copy(chars, bytes);
        bytes.rewind();
        write(channel, bytes, leftStartIndex);
    }

    /**
     * Merges two sorted halves of array into a new array.
     * Time complexity {@code O(n)}, memory complexity {@code O(n)}.
     *
     * @param array     an {@code Array} of {@code char}s
     * @param delimiter a first index of the second part
     * @return sorted {@code Array} of {@code char}s
     */
    public static char[] mergeParts(char[] array, int delimiter) {
        char[] res = new char[array.length];
        int i = 0;
        int j = delimiter;
        int index = 0;
        while (index < array.length) {
            char item;
            if (i >= delimiter) {
                item = array[j++];
            } else if (j >= array.length) {
                item = array[i++];
            } else if (array[i] <= array[j]) {
                item = array[i++];
            } else {
                item = array[j++];
            }
            res[index++] = item;
        }
        return res;
    }

    /**
     * Merges two sorted halves of array into a new array with no additional memory.
     * Time complexity {@code O(n * log(n))}, memory complexity {@code O(1)}.
     * See <a href='https://www.geeksforgeeks.org/efficiently-merging-two-sorted-arrays-with-o1-extra-space/'>algorithm description</a>
     *
     * @param array an {@code Array} of {@code char}s
     * @return sorted {@code Array} of {@code char}s, the same
     */
    public static char[] mergeParts(char[] array) {
        int gap = calcGap(array.length);
        for (; gap > 0; gap = calcGap(gap)) {
            for (int i = 0, j = gap; j < array.length; i++, j++) {
                char left = array[i];
                char right = array[j];
                if (left > right) {
                    array[j] = left;
                    array[i] = right;
                }
            }
        }
        return array;
    }

    /**
     * Merges two sorted halves of a file.
     *
     * @param file {@link AsynchronousFileChannel}
     * @throws IOException some I/O problem
     */
    public static void mergeParts(AsynchronousFileChannel file) throws IOException {
        mergeDirectly(file, 0, file.size() - 1);
    }

    private static void mergeDirectly(AsynchronousFileChannel file,
                                      long startIndexInBytes,
                                      long endIndexInBytes) throws IOException {
        long lengthInBytes = endIndexInBytes - startIndexInBytes + 1;
        long lengthInChars = lengthInBytes / 2 + lengthInBytes % 2;
        long start = -lengthInBytes % 2 + startIndexInBytes;
        long gap = calcGap(lengthInChars);
        for (; gap > 0; gap = calcGap(gap)) {
            for (long i = 0, j = gap; j < lengthInChars; i++, j++) {
                long bi = i * 2 + start;
                long bj = j * 2 + start;
                char left = read(file, bi);
                char right = read(file, bj);
                if (left > right) {
                    write(file, bj, left);
                    write(file, bi, right);
                }
            }
        }
    }

    private static char read(AsynchronousFileChannel source, long byteIndex) throws IOException {
        ByteBuffer bytes;
        if (byteIndex == -1) {
            byteIndex = 0;
            bytes = ByteBuffer.allocate(1);
        } else {
            bytes = ByteBuffer.allocate(2);
        }
        read(source, bytes, byteIndex);
        bytes.rewind();
        return bytes.limit() == 1 ? (char) bytes.get() : bytes.getChar();
    }

    private static void write(AsynchronousFileChannel source, long byteIndex, char value) throws IOException {
        ByteBuffer bytes = ByteBuffer.allocate(2);
        if (byteIndex == -1) {
            byteIndex = 0;
            bytes.put(0, (byte) value);
        } else {
            bytes.putChar(value);
        }
        bytes.rewind();
        write(source, bytes, byteIndex);
    }

    private static void read(AsynchronousFileChannel channel, ByteBuffer bytes, long position) throws IOException {
        try {
            int res = channel.read(bytes, position).get(IO_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
            int expected = bytes.limit();
            if (res != expected) {
                throw new IllegalStateException("Expected " + expected + ", got " + res);
            }
        } catch (InterruptedException | TimeoutException e) {
            throw new IllegalStateException(e);
        } catch (ExecutionException e) {
            catchIOException(e);
        }
    }

    private static void write(AsynchronousFileChannel channel, ByteBuffer bytes, long position) throws IOException {
        try {
            int res = channel.write(bytes, position).get(IO_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
            int expected = bytes.limit();
            if (res != expected) {
                throw new IllegalStateException("Expected " + expected + ", got " + res);
            }
        } catch (InterruptedException | TimeoutException e) {
            throw new IllegalStateException(e);
        } catch (ExecutionException e) {
            catchIOException(e);
        }
    }

    private static void catchIOException(ExecutionException e) throws IOException {
        Throwable cause = e.getCause();
        if (cause instanceof IOException) {
            throw (IOException) cause;
        }
        throw new IllegalStateException(e);
    }

    private static int calcGap(int gap) {
        return gap > 1 ? gap / 2 + gap % 2 : 0;
    }

    private static long calcGap(long gap) {
        return gap > 1 ? gap / 2 + gap % 2 : 0;
    }

}
