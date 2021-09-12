package com.gitlab.sszuev.tasks.sorting.external;

import com.gitlab.sszuev.utils.CharsUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by @ssz on 11.09.2021.
 */
public class MergeHalfSortedArrayHelper {

    private static final int IO_TIMEOUT_IN_SECONDS = 5;

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
     * @return sorted {@code Array} of {@code char}s
     */
    public static char[] mergeParts(char[] array) {
        int gap = calcGap(array.length);
        for (; gap > 0; gap = calcGap(gap)) {
            for (int i = 0, j = gap; j < array.length; i++, j++) {
                int left = array[i];
                int right = array[j];
                if (left > right) {
                    CharsUtils.swap(array, i, j);
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
        long lengthInBytes = file.size();
        long lengthInChars = lengthInBytes / 2 + lengthInBytes % 2;
        long fix = -lengthInBytes % 2;
        long gap = calcGap(lengthInChars);
        for (; gap > 0; gap = calcGap(gap)) {
            for (long i = 0, j = gap; j < lengthInChars; i++, j++) {
                long bi = i * 2 + fix;
                long bj = j * 2 + fix;
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
        try {
            int res = source.read(bytes, byteIndex).get(IO_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
            if (res != bytes.limit()) {
                throw new IllegalStateException();
            }
        } catch (InterruptedException | TimeoutException e) {
            throw new IllegalStateException(e);
        } catch (ExecutionException e) {
            catchIOException(e);
        }
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
        try {
            int res = source.write(bytes, byteIndex).get(IO_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
            if (res != 2) {
                throw new IllegalStateException();
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
