package com.gitlab.sszuev.tasks.sorting.external;

import com.gitlab.sszuev.utils.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
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
     * Merges two sorted parts of a file using different approaches:
     * in-memory, partially in-memory, with temporary files.
     * Uses the first approach if the file parts are small enough to fit in memory twice.
     * Uses the second approach, if file parts can be read into memory wholly,
     * but there is no free memory to hold the sorted result.
     * The third way is used in any other case.
     *
     * @param channel         {@link AsynchronousFileChannel} - a file channel, not {@code null}
     * @param bufferLength    {@code long} an upper memory limit in bytes, this number is also used to choose the sorting method
     * @param leftStartIndex  {@code long}, start index in bytes
     * @param rightStartIndex {@code long}, start index of a second sorted part in bytes
     * @param rightEndIndex   {@code long}, end index in bytes
     * @throws IOException if there is some problem with I/O
     */
    public static void merge(AsynchronousFileChannel channel,
                             long bufferLength,
                             long leftStartIndex,
                             long rightStartIndex,
                             long rightEndIndex) throws IOException {
        long length = rightEndIndex - leftStartIndex + 1;
        // case 1: put everything in mem, sort using additional memory with O(N) time complexity, write everything down
        if (length < bufferLength / 2) {
            mergeWithAdditionalMemory(channel, leftStartIndex, rightStartIndex, rightEndIndex);
            return;
        }
        // case 2: put everything in mem, sort without additional memory with O(N * log(N)) complexity, write down
        if (length < bufferLength) {
            mergeWithoutAdditionalMemory(channel, leftStartIndex, rightEndIndex);
            return;
        }
        // case 3: do not use memory for sorting, but use additional temporal files instead
        mergeWithTemporaryFile(channel, bufferLength, leftStartIndex, rightStartIndex, rightEndIndex);
    }

    /**
     * Merges the two sorted halves of the given file
     * putting everything in memory and also using additional memory array
     * and algorithm {@link MergeHalfSortedArrayHelper#mergeParts(char[], int)}.
     *
     * @param channel         {@link AsynchronousFileChannel} - a file channel
     * @param leftStartIndex  {@code long}, start index in bytes
     * @param rightStartIndex {@code long}, start index of a second sorted part in bytes
     * @param rightEndIndex   {@code long}, end index in bytes
     * @throws IOException if there is some problem with I/O
     */
    public static void mergeWithAdditionalMemory(AsynchronousFileChannel channel,
                                                 long leftStartIndex,
                                                 long rightStartIndex,
                                                 long rightEndIndex) throws IOException {
        long length = checkPositiveInt(rightEndIndex - leftStartIndex + 1);
        long delimiter = checkPositiveInt((rightStartIndex - leftStartIndex) / 2);
        ByteBuffer bytes = ByteBuffer.allocate((int) length);
        read(channel, leftStartIndex, bytes);
        bytes.rewind();

        CharBuffer chars = BufferUtils.toCharBuffer(bytes);
        char[] sorted = mergeParts(chars.array(), (int) delimiter);

        BufferUtils.copy(CharBuffer.wrap(sorted), bytes);
        bytes.rewind();
        write(channel, leftStartIndex, bytes);
    }

    /**
     * Merges the two sorted halves of the given file, putting everything in memory,
     * but unlike the {@link #mergeWithAdditionalMemory(AsynchronousFileChannel, long, long, long)} algorithm,
     * this method does not require additional memory beyond that.
     * Uses the algorithm {@link MergeHalfSortedArrayHelper#mergeParts(char[])}.
     *
     * @param channel        {@link AsynchronousFileChannel} - a file channel, not {@code null}
     * @param leftStartIndex {@code long}, start index in bytes
     * @param rightEndIndex  {@code long}, end index in bytes
     * @throws IOException if there is some problem with I/O
     */
    public static void mergeWithoutAdditionalMemory(AsynchronousFileChannel channel,
                                                    long leftStartIndex,
                                                    long rightEndIndex) throws IOException {
        long length = rightEndIndex - leftStartIndex + 1;
        if (length >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        ByteBuffer bytes = ByteBuffer.allocate((int) length);
        read(channel, leftStartIndex, bytes);
        bytes.rewind();

        CharBuffer chars = BufferUtils.toCharBuffer(bytes);
        mergeParts(chars.array());

        BufferUtils.copy(chars, bytes);
        bytes.rewind();
        write(channel, leftStartIndex, bytes);
    }

    /**
     * Merges two sorted halves of a file using temporary file
     * and algorithm {@link MergeHalfSortedArrayHelper#mergeParts(char[], int)}.
     *
     * @param channel         {@link AsynchronousFileChannel} - a file channel
     * @param bufferLength    {@code long} an upper memory limit in bytes
     * @param leftStartIndex  {@code long}, start index in bytes
     * @param rightStartIndex {@code long}, start index of a second sorted part in bytes
     * @param rightEndIndex   {@code long}, end index in bytes
     * @throws IOException if there is some problem with I/O
     */
    public static void mergeWithTemporaryFile(AsynchronousFileChannel channel,
                                              long bufferLength,
                                              long leftStartIndex,
                                              long rightStartIndex,
                                              long rightEndIndex) throws IOException {
        Path res = mergeToTemporaryFile(channel,
                bufferLength, leftStartIndex, rightStartIndex, rightEndIndex);
        // copy everything back to the source channel
        copy(res, channel, leftStartIndex, (int) Math.min(Integer.MAX_VALUE, bufferLength));
        // delete temporary file
        Files.delete(res);
    }

    private static Path mergeToTemporaryFile(AsynchronousFileChannel channel,
                                             long bufferLengthInBytes,
                                             long leftStartIndexInBytes,
                                             long rightStartIndexInBytes,
                                             long rightEndIndexInBytes) throws IOException {
        long lengthInBytes = checkPositiveInt(rightEndIndexInBytes - leftStartIndexInBytes + 1);
        long readBufferSizeInChars = checkPositiveInt(bufferLengthInBytes / 2 / 2 / 2);
        CharBuffer leftBuffer = CharBuffer.allocate((int) readBufferSizeInChars);
        CharBuffer rightBuffer = CharBuffer.allocate((int) readBufferSizeInChars);

        long writeBufferSizeInChars = bufferLengthInBytes / 2 / 2;
        CharBuffer targetBuffer = CharBuffer.allocate((int) writeBufferSizeInChars);

        Path res = createTempFile(leftStartIndexInBytes, rightEndIndexInBytes);
        try (SeekableByteChannel target = Files.newByteChannel(res, StandardOpenOption.WRITE)) {
            mergeWithTemporaryFile(channel, leftBuffer, rightBuffer,
                    target, targetBuffer,
                    lengthInBytes, leftStartIndexInBytes, rightStartIndexInBytes);
        }
        return res;
    }

    private static void mergeWithTemporaryFile(AsynchronousFileChannel source,
                                               CharBuffer sourceLeftBuffer,
                                               CharBuffer sourceRightBuffer,
                                               SeekableByteChannel target,
                                               CharBuffer targetBuffer,
                                               long lengthInBytes,
                                               long leftStartIndexInBytes,
                                               long rightStartIndexInBytes) throws IOException {
        long lengthInChars = lengthInBytes / 2 + lengthInBytes % 2;
        long leftStartIndexInChars = 0;
        long rightStartIndexInChars = (rightStartIndexInBytes - leftStartIndexInBytes + lengthInBytes % 2) / 2;

        long i = leftStartIndexInChars;
        long j = rightStartIndexInChars;
        long targetCharIndex = 0;
        while (targetCharIndex < lengthInChars) {
            char item;
            char left = get(source, lengthInBytes, leftStartIndexInBytes, sourceLeftBuffer, i);
            char right = get(source, lengthInBytes, leftStartIndexInBytes, sourceRightBuffer, j);
            if (i >= rightStartIndexInChars) {
                item = right;
                j++;
            } else if (j >= lengthInChars) {
                item = left;
                i++;
            } else if (left <= right) {
                item = left;
                i++;
            } else {
                item = right;
                j++;
            }
            if (targetBuffer.position() == targetBuffer.limit()) {
                // flush to file and rewind
                write(target, lengthInBytes, targetCharIndex, targetBuffer);
            }
            targetBuffer.put(item);
            targetCharIndex++;
        }
        if (targetBuffer.position() != targetBuffer.limit()) {
            write(target, lengthInBytes, targetCharIndex, targetBuffer);
        }
    }

    private static Path createTempFile(long i, long j) throws IOException {
        return Files.createTempFile(MergeHalfSortedArrayHelper.class.getSimpleName() + "-" + i + "-" + j + "-", ".bin");
    }

    private static char get(AsynchronousFileChannel channel,
                            long lengthInBytes,
                            long leftShiftInBytes,
                            CharBuffer charBuffer,
                            long charIndex) throws IOException {
        int charBufferSize = charBuffer.limit();
        long chunkIndex = charIndex / charBufferSize;
        if (chunkIndex > Integer.MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        if (charBuffer.position() == 0) {
            // the buffer is empty -> read the next data chunk from the source
            readChunk(channel, lengthInBytes, leftShiftInBytes, chunkIndex, charBuffer);
        }
        int index = (int) charIndex % charBufferSize;
        char res = charBuffer.get(index);
        if (index == charBufferSize - 1) {
            // buffer is full -> rewind it
            charBuffer.rewind();
        }
        return res;
    }

    private static void readChunk(AsynchronousFileChannel source,
                                  long lengthInBytes,
                                  long leftShiftInBytes,
                                  long chunkIndex,
                                  CharBuffer charBuffer) throws IOException {
        int charBufferSize = charBuffer.limit();
        long charStartIndex = chunkIndex * charBufferSize;
        ByteBuffer bytes = allocate(lengthInBytes, charBufferSize, charStartIndex);

        long byteStartIndex = charIndexToByteIndex(charStartIndex, lengthInBytes) + leftShiftInBytes;
        read(source, byteStartIndex, bytes);
        BufferUtils.copy(bytes, charBuffer);
        charBuffer.rewind();
    }

    private static void write(SeekableByteChannel target,
                              long lengthInBytes,
                              long targetCharIndex,
                              CharBuffer targetBuffer) throws IOException {
        long chunkIndex = (targetCharIndex - 1) / targetBuffer.limit();
        writeChunk(target, lengthInBytes, chunkIndex, targetBuffer);
        targetBuffer.rewind();
    }

    private static void writeChunk(SeekableByteChannel target,
                                   long lengthInBytes,
                                   long chunkIndex,
                                   CharBuffer charBuffer) throws IOException {
        int charBufferSize = charBuffer.limit();
        long charStartIndex = chunkIndex * charBufferSize;
        ByteBuffer bytes = allocate(lengthInBytes, charBufferSize, charStartIndex);
        long lengthInChars = lengthInBytes / 2 + lengthInBytes % 2;
        int lengthToCopy = Math.min(charBufferSize, (int) (lengthInChars - charStartIndex));
        BufferUtils.copy(charBuffer, lengthToCopy, bytes);
        bytes.rewind();
        long byteStartIndex = charIndexToByteIndex(charStartIndex, lengthInBytes);
        write(target, bytes, byteStartIndex);
        charBuffer.rewind();
    }

    private static ByteBuffer allocate(long lengthInBytes, int charBufferSize, long charStartIndex) {
        // the first char may be represented as a single byte
        long bytesBufferSize = charBufferSize * 2L;
        if (charStartIndex == 0 && lengthInBytes % 2 != 0) {
            bytesBufferSize -= 1;
        }
        long byteStartIndex = charIndexToByteIndex(charStartIndex, lengthInBytes);
        bytesBufferSize = checkPositiveInt(Math.min(bytesBufferSize, lengthInBytes - byteStartIndex));
        return ByteBuffer.allocate((int) bytesBufferSize);
    }

    private static long charIndexToByteIndex(long charIndex, long lengthInBytes) {
        return charIndex != 0 ? charIndex * 2 - lengthInBytes % 2 : 0;
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
     * Merges two sorted halves of the specified file that is represented as a {@link AsynchronousFileChannel cannel}.
     * No additional memory or disk space is required.
     * TODO: this is an experimental functionality, not ready to use,
     *  it works very slow since there are no buffers used while I\O operations.
     *
     * @param file {@link AsynchronousFileChannel}
     * @throws IOException some I/O problem
     */
    public static void mergeParts(AsynchronousFileChannel file) throws IOException {
        mergeParts(file, 0, file.size() - 1);
    }

    /**
     * Merges two sorted halves of the specified file with no additional memory or disk space.
     * TODO: this is an experimental functionality, not ready to use,
     *  it works very slow since there are no buffers used while I\O operations.
     *
     * @param file              {@link AsynchronousFileChannel}
     * @param startIndexInBytes {@code long}, start index (position) in bytes
     * @param endIndexInBytes   {@code long}, end index (position) in bytes
     * @throws IOException some I/O problem
     */
    public static void mergeParts(AsynchronousFileChannel file,
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
        read(source, byteIndex, bytes);
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
        write(source, byteIndex, bytes);
    }

    private static void copy(Path source, AsynchronousFileChannel target, long targetPosition, int bufferSize) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
        try (SeekableByteChannel channel = Files.newByteChannel(source, StandardOpenOption.READ)) {
            long length = channel.size();
            long sourcePosition = 0;
            while (sourcePosition < length) {
                channel.position(sourcePosition);
                channel.read(buffer);
                int current = buffer.position();
                buffer.rewind();
                buffer.limit(current);
                write(target, targetPosition, buffer, current);
                buffer.rewind();
                sourcePosition += current;
                targetPosition += current;
            }
        }
    }

    private static void read(AsynchronousFileChannel channel, long position, ByteBuffer bytes) throws IOException {
        performAsync(channel.read(bytes, position), bytes.limit());
    }

    private static void write(AsynchronousFileChannel channel, long position, ByteBuffer bytes) throws IOException {
        write(channel, position, bytes, bytes.limit());
    }

    private static void write(AsynchronousFileChannel channel, long position, ByteBuffer bytes, int expectedReadBytes) throws IOException {
        performAsync(channel.write(bytes, position), expectedReadBytes);
    }

    private static void write(SeekableByteChannel channel, ByteBuffer bytes, long position) throws IOException {
        channel.position(position);
        ensureIsOk(bytes.limit(), channel.write(bytes));
    }

    private static void performAsync(Future<Integer> operation, int expected) throws IOException {
        try {
            ensureIsOk(expected, operation.get(IO_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS));
        } catch (InterruptedException | TimeoutException e) {
            throw new IllegalStateException(e);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            throw new IllegalStateException(e);
        }
    }

    private static void ensureIsOk(int expected, int actual) {
        if (actual != expected) {
            throw new IllegalStateException("Expected " + expected + ", got " + actual);
        }
    }

    private static int calcGap(int gap) {
        return gap > 1 ? gap / 2 + gap % 2 : 0;
    }

    private static long calcGap(long gap) {
        return gap > 1 ? gap / 2 + gap % 2 : 0;
    }

    private static int checkPositiveInt(long value) {
        if (value > Integer.MAX_VALUE || value <= 0) {
            throw new IllegalArgumentException("Wrong value " + value);
        }
        return (int) value;
    }
}
