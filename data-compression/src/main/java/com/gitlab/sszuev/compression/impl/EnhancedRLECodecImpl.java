package com.gitlab.sszuev.compression.impl;

import com.gitlab.sszuev.compression.BinaryCodec;
import com.gitlab.sszuev.compression.FileCodec;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;

/**
 * Run-length encoding (RLE) with obvious improvements
 * (for max sequences of distinct contiguous elements there is no inefficient RLE-compression).
 * <p>
 * Example: {@code #########....#.###.#.#.##..#...######### => +9#+4.-2#.+3#-10.#.#.##..#+3.+9#}
 * <p>
 * Created by @ssz on 22.12.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Run-length_encoding'>Run-length encoding</a>
 */
public class EnhancedRLECodecImpl implements BinaryCodec, FileCodec {
    private static final int MAX_LENGTH_OF_REPEAT_SEQUENCE = Byte.MAX_VALUE;
    private static final int MAX_LENGTH_OF_UNIQUE_SEQUENCE = -Byte.MIN_VALUE;

    @Override
    public void encode(Path source, Path target) throws IOException {
        this.encode(source, target, DEFAULT_BUFFER_SIZE);
    }

    public void encode(Path source, Path target, int bufferLength) throws IOException {
        long sourceSize = Files.size(Objects.requireNonNull(source, "Null source"));
        Objects.requireNonNull(target, "Null target");
        int readBufferLength = calcReadBufferCapacityForCompress(bufferLength);
        if (sourceSize <= readBufferLength) {
            byte[] original = Files.readAllBytes(source);
            byte[] encoded = this.encode(original);
            Files.write(target, encoded);
            return;
        }
        this.encode(() -> FileCodec.newReadChannel(source), () -> FileCodec.newWriteChannel(target),
                readBufferLength, bufferLength - readBufferLength);
    }

    @Override
    public byte[] encode(byte[] raw) {
        byte[] res = new byte[maxLength(raw.length)];
        int resLength = encode(raw, raw.length, res);
        return res.length == resLength ? res : Arrays.copyOf(res, resLength);
    }

    @Override
    public final void encode(IOSupplier<? extends ReadableByteChannel> source,
                             IOSupplier<? extends WritableByteChannel> target,
                             int bufferLength) throws IOException {
        int readBufferCapacity = calcReadBufferCapacityForCompress(bufferLength);
        int writeBufferCapacity = bufferLength - readBufferCapacity;
        encode(source, target, readBufferCapacity, writeBufferCapacity);
    }

    protected void encode(IOSupplier<? extends ReadableByteChannel> source,
                          IOSupplier<? extends WritableByteChannel> target,
                          final int readBufferCapacity,
                          final int writeBufferCapacity) throws IOException {
        ByteBuffer readBuffer = ByteBuffer.allocate(readBufferCapacity);
        ByteBuffer writeBuffer = ByteBuffer.allocate(writeBufferCapacity);

        try (ReadableByteChannel readChannel = source.open();
             WritableByteChannel writeChannel = target.open()) {
            SeekableByteChannel seekable = writeChannel instanceof SeekableByteChannel ?
                    (SeekableByteChannel) writeChannel : null;

            int readLength;
            int writeLength;
            int writePosition;
            int tailLength = -1;

            while ((readLength = readChannel.read(readBuffer)) > 0) {
                readBuffer.rewind();
                tailLength = -1;
                writePosition = writeBuffer.position();
                writeLength = encodeWithTail(readBuffer, readLength, writeBuffer);
                if (writeLength == 0) {
                    throw new IllegalStateException();
                }
                if (seekable == null) {
                    // can't glue, set limit to the end
                    writeBuffer.limit(writeLength);
                } else if (writePosition != 0) {
                    // has tail from the previous iteration
                    glueTail(writeBuffer, writePosition);
                }
                int writeBytes = writeChannel.write(writeBuffer);
                if (writeBytes < 0) {
                    throw new IllegalStateException();
                }
                if (seekable != null) {
                    // copy the tail to the beginning of array
                    tailLength = writeLength - writeBuffer.limit();
                    if (tailLength > MAX_LENGTH_OF_UNIQUE_SEQUENCE) {
                        throw new IllegalStateException();
                    }
                    byte[] targetArray = writeBuffer.array();
                    System.arraycopy(targetArray, writeBuffer.limit(), targetArray, 0, tailLength);
                    writeBuffer.position(tailLength);
                } else {
                    // can't glue, reset to zero
                    writeBuffer.position(0);
                }
            }
            if (seekable != null && tailLength > 0) {
                // write the last remaining tail
                writeBuffer.limit(tailLength);
                writeBuffer.position(0);
                int writeBytes = writeChannel.write(writeBuffer);
                if (writeBytes != tailLength) {
                    throw new IllegalStateException();
                }
            }
        }
    }

    @Override
    public void decode(Path source, Path target) throws IOException {
        this.decode(source, target, DEFAULT_BUFFER_SIZE);
    }

    public void decode(Path source, Path target, int bufferLength) throws IOException {
        long sourceSize = Files.size(Objects.requireNonNull(source, "Null source"));
        Objects.requireNonNull(target, "Null target");
        int readBufferLength = calcReadBufferCapacityForDecompress(bufferLength);
        if (sourceSize <= readBufferLength) {
            byte[] compressed = Files.readAllBytes(source);
            byte[] decompressed = this.decode(compressed);
            Files.write(target, decompressed);
            return;
        }
        this.decode(() -> FileCodec.newReadChannel(source), () -> FileCodec.newWriteChannel(target),
                readBufferLength, bufferLength - readBufferLength);
    }

    @Override
    public byte[] decode(byte[] encoded) {
        int length = getRawArrayLength(encoded);
        byte[] res = new byte[length];
        int index = 0;
        for (int i = 0; i < encoded.length; i++) {
            int series = encoded[i];
            if (series == 0) {
                throw new IllegalStateException();
            }
            if (series > 0) {
                Arrays.fill(res, index, index += series, encoded[i + 1]);
                i++;
                continue;
            }
            System.arraycopy(encoded, i + 1, res, index, -series);
            index -= series;
            i -= series;
        }
        return res;
    }

    @Override
    public final void decode(IOSupplier<? extends ReadableByteChannel> source,
                             IOSupplier<? extends WritableByteChannel> target,
                             int bufferLength) throws IOException {
        int readBufferCapacity = calcReadBufferCapacityForDecompress(bufferLength);
        int writeBufferCapacity = bufferLength - readBufferCapacity;
        decode(source, target, readBufferCapacity, writeBufferCapacity);
    }

    protected void decode(IOSupplier<? extends ReadableByteChannel> source,
                          IOSupplier<? extends WritableByteChannel> target,
                          final int readBufferCapacity,
                          final int writeBufferCapacity) throws IOException {
        ByteBuffer readBuffer = ByteBuffer.allocate(readBufferCapacity);
        ByteBuffer writeBuffer = ByteBuffer.allocate(writeBufferCapacity);

        try (ReadableByteChannel readChannel = source.open();
             WritableByteChannel writeChannel = target.open()) {

            int readLength;
            byte[] readArray = readBuffer.array();
            byte[] writeArray = writeBuffer.array();

            while ((readLength = readChannel.read(readBuffer)) >= 0 || readBuffer.position() > 0) {

                int ws = 0;
                int we;
                int copyLength;
                final int readEnd = readBuffer.position();
                readBuffer.position(0);
                for (int ri = 0; ri < readEnd; ) {
                    final byte series = readArray[ri];
                    if (series > 0) {
                        if (readLength >= 0 && ri + 1 >= readEnd) {
                            // read overflow -> go to next iteration
                            readArray[0] = series;
                            readBuffer.position(1);
                            break;
                        }
                        byte data = readArray[++ri];
                        we = ws + series;
                        while (we > writeBufferCapacity) {
                            Arrays.fill(writeArray, ws, writeBufferCapacity, data);
                            ws = 0;
                            we -= writeBufferCapacity;

                            writeBuffer.limit(writeBufferCapacity);
                            writeChannel.write(writeBuffer);
                            writeBuffer.position(0);
                        }
                        Arrays.fill(writeArray, ws, we, data);
                        writeBuffer.limit(we);
                        ws = we;
                        ri++;
                    } else {
                        ++ri;
                        if (readLength >= 0 && ri - series >= readEnd) {
                            // read overflow -> go to next iteration
                            readArray[0] = series;
                            copyLength = readEnd - ri;
                            System.arraycopy(readArray, ri, readArray, 1, copyLength);
                            readBuffer.position(copyLength + 1);
                            break;
                        }
                        we = ws - series;
                        while (we > writeBufferCapacity) {
                            copyLength = writeBufferCapacity - ws;
                            System.arraycopy(readArray, ri, writeArray, ws, copyLength);
                            ws = 0;
                            we -= writeBufferCapacity;
                            ri += copyLength;

                            writeBuffer.limit(writeBufferCapacity);
                            writeChannel.write(writeBuffer);
                            writeBuffer.position(0);
                        }
                        copyLength = we - ws;
                        System.arraycopy(readArray, ri, writeArray, ws, copyLength);
                        writeBuffer.limit(we);
                        ws = we;
                        ri += copyLength;
                    }
                }
                writeChannel.write(writeBuffer);
                writeBuffer.position(0);
                if (readLength < 0) { // no next iteration
                    break;
                }
            }
        }
    }

    /**
     * Calculates the read-buffer capacity for encoding operation.
     * <p>
     * The formula is:
     * <pre>{@code
     * 2 * readBufferCapacity + (readBufferCapacity / MAX_LENGTH_OF_UNIQUE_SEQUENCE) + 1 = bufferLength
     * }</pre>
     * This formula is derived from the formula {@link #maxLength(int)}:
     * <pre>{@code
     * readBufferCapacity % MAX_LENGTH_OF_UNIQUE_SEQUENCE + 1
     * + (readBufferCapacity / MAX_LENGTH_OF_UNIQUE_SEQUENCE) * (MAX_LENGTH_OF_UNIQUE_SEQUENCE + 1)
     * }</pre>, which calculates the maximum possible length of buffer for a case when all symbols are unique,
     * also, it takes into account the need for an additional free-space of {@code MAX_LENGTH_OF_UNIQUE_SEQUENCE} bytes
     * for gluing with the tail with the next write-buffer during encoding.
     *
     * @param bufferLength {@code int} - total buffer length: {@code bufferLength = readBufferCapacity + writeBufferCapacity}
     * @return {@code int} the read buffer capacity for {@link #encode(IOSupplier, IOSupplier, int)} operation
     */
    protected int calcReadBufferCapacityForCompress(int bufferLength) {
        if (bufferLength < MAX_LENGTH_OF_UNIQUE_SEQUENCE + 3) {
            throw new IllegalArgumentException("The specified buffer is too small: " + bufferLength);
        }
        int res = (bufferLength - MAX_LENGTH_OF_UNIQUE_SEQUENCE - 1) / 2;
        if (res < MAX_LENGTH_OF_UNIQUE_SEQUENCE) {
            return res;
        }
        int tmp = bufferLength + 1;
        while (tmp > bufferLength) {
            res--;
            tmp = 2 * res + (res / MAX_LENGTH_OF_UNIQUE_SEQUENCE) + MAX_LENGTH_OF_UNIQUE_SEQUENCE + 1;
        }
        return res;
    }

    /**
     * Calculates the read-buffer capacity for decoding operation.
     *
     * @param bufferLength {@code int} - total buffer length: {@code bufferLength = readBufferCapacity + writeBufferCapacity}
     * @return {@code int} the read buffer capacity for {@link #decode(IOSupplier, IOSupplier, int)} operation
     */
    protected int calcReadBufferCapacityForDecompress(int bufferLength) {
        if (bufferLength < MAX_LENGTH_OF_UNIQUE_SEQUENCE + 3) {
            throw new IllegalArgumentException("The specified buffer is too small: " + bufferLength);
        }
        return MAX_LENGTH_OF_UNIQUE_SEQUENCE + 1 + (bufferLength - MAX_LENGTH_OF_UNIQUE_SEQUENCE - 1) / 2;
    }

    /**
     * Calculates the maximum possible length of the output byte-array for the given number of input bytes
     * taking an assumption that all input bytes are unique in optimised-RLE sense.
     *
     * @param length {@code int}
     * @return {@code int}
     */
    protected int maxLength(int length) { // all symbols are unique
        return length % MAX_LENGTH_OF_UNIQUE_SEQUENCE + 1 +
                (length / MAX_LENGTH_OF_UNIQUE_SEQUENCE) * (MAX_LENGTH_OF_UNIQUE_SEQUENCE + 1);
    }

    /**
     * Encodes the data using advanced RLE algorithm (no compression for sequences of distinct contiguous elements).
     *
     * @param source    {@code Array}, the array with original data
     * @param sourceEnd {@code int}, the final index of the source array to end encoding,
     *                  exclusive (this index may lie outside the source array)
     * @param target    {@code Array}, the array with result (encoded) data
     * @return the final index of the target array, exclusive
     */
    protected int encode(byte[] source, int sourceEnd, byte[] target) {
        int length = 0;
        int series;
        for (int i = 0; i < sourceEnd; i++) {
            int us = uniqueSequence(source, i, sourceEnd);
            if (us != i) {
                series = us - i;
                if (series > MAX_LENGTH_OF_UNIQUE_SEQUENCE) {
                    series = MAX_LENGTH_OF_UNIQUE_SEQUENCE;
                    us = i + series;
                }
                target[length++] = (byte) (-series);
                System.arraycopy(source, i, target, length, series);
                length += series;
                i = us - 1;
                continue;
            }
            int rs = repeatSequence(source, i, sourceEnd);
            if (rs == i) {
                throw new IllegalStateException();
            }
            series = rs - i;
            if (series > MAX_LENGTH_OF_REPEAT_SEQUENCE) {
                series = MAX_LENGTH_OF_REPEAT_SEQUENCE;
                rs = i + series;
            }
            target[length++] = (byte) series;
            target[length++] = source[i];
            i = rs - 1;
        }
        return length;
    }

    /**
     * Encodes the data using optimised RLE algorithm,
     * remembering the last sequence using {@link ByteBuffer#limit() limit}
     * to process that tail on {@link #glueTail(ByteBuffer, int) glue} operation.
     *
     * @param sourceBuffer {@link ByteBuffer}, the buffer with the original data
     * @param sourceEnd    {@code int}, the final position of the source buffer, exclusive
     * @param targetBuffer {@link ByteBuffer}, the buffer with result (encoded) data
     * @return the final index of the target array, exclusive
     * @see #encode(byte[], int, byte[])
     */
    protected int encodeWithTail(ByteBuffer sourceBuffer, int sourceEnd, ByteBuffer targetBuffer) {
        int series;
        byte[] source = sourceBuffer.array();
        byte[] target = targetBuffer.array();
        int position = targetBuffer.position();
        for (int i = 0; i < sourceEnd; i++) {
            int us = uniqueSequence(source, i, sourceEnd);
            if (us != i) {
                series = us - i;
                if (series > MAX_LENGTH_OF_UNIQUE_SEQUENCE) {
                    series = MAX_LENGTH_OF_UNIQUE_SEQUENCE;
                    us = i + series;
                }
                targetBuffer.limit(position); // limit to the previous series, leaving tail
                target[position++] = (byte) (-series);
                System.arraycopy(source, i, target, position, series);
                position += series;
                i = us - 1;
                continue;
            }
            int rs = repeatSequence(source, i, sourceEnd);
            if (rs == i) {
                throw new IllegalStateException();
            }
            series = rs - i;
            if (series > MAX_LENGTH_OF_REPEAT_SEQUENCE) {
                series = MAX_LENGTH_OF_REPEAT_SEQUENCE;
                rs = i + series;
            }
            targetBuffer.limit(position); // limit to previous series
            target[position++] = (byte) series;
            target[position++] = source[i];
            i = rs - 1;
        }
        if (position - targetBuffer.limit() > MAX_LENGTH_OF_UNIQUE_SEQUENCE) {
            // no need in tail
            targetBuffer.limit(position);
        }
        return position;
    }

    /**
     * Glues the tail that is written at the beginning of the buffer with the rest of the buffer,
     * making it valid to write.
     *
     * @param writeBuffer   {@link  ByteBuffer}
     * @param writePosition position where the rest part begins (the end of tail is {@code writePosition - 1})
     */
    protected void glueTail(ByteBuffer writeBuffer, int writePosition) {
        if (writePosition > MAX_LENGTH_OF_UNIQUE_SEQUENCE) {
            throw new IllegalStateException();
        }
        int series = writeBuffer.get(writePosition);
        int prevSeries = writeBuffer.get(0);
        if (series > 0 && prevSeries > 0) {
            if (writePosition != 2) {
                throw new IllegalStateException();
            }
            if (writeBuffer.get(1) != writeBuffer.get(writePosition + 1)) { // different sequences
                writeBuffer.position(0);
                return;
            }
            series += prevSeries;
            if (series > MAX_LENGTH_OF_REPEAT_SEQUENCE) { // overflow: can't glue
                writeBuffer.position(0);
                return;
            }
            writeBuffer.put(writePosition, (byte) series);
            // don't change position
            return;
        }
        if (series < 0 && prevSeries < 0) {
            series += prevSeries;
            if (-series > MAX_LENGTH_OF_UNIQUE_SEQUENCE) { // overflow: can't glue
                writeBuffer.position(0);
                return;
            }
            byte[] array = writeBuffer.array();
            array[0] = (byte) series;
            System.arraycopy(array, 0, array, 1, writePosition);
            writeBuffer.position(1);
            return;
        }
        writeBuffer.position(0);
    }

    /**
     * Returns the index after the end of the RLE unique-sequence is found in the given {@code source}
     * or the given start index, if no such sequence found.
     *
     * @param source {@code Array}
     * @param start  {@code int}, inclusive
     * @param end    {@code int}, exclusive
     * @return {@code int}
     */
    public static int uniqueSequence(byte[] source, int start, int end) {
        int i = start;
        for (; i < end; i++) {
            if (i > end - 3) {
                i = end;
                break;
            }
            if (source[i] == source[i + 1]) {
                if (i < end - 2 && source[i + 1] != source[i + 2]) {
                    i = i + 1;
                    continue;
                }
                break;
            }
        }
        return i;
    }

    /**
     * Returns the index after the end of the RLE repeat-sequence
     * or the given start index, if no such sequence found.
     *
     * @param source {@code Array}
     * @param start  {@code int}, inclusive
     * @param end    {@code int}, exclusive
     * @return {@code int}
     */
    public static int repeatSequence(byte[] source, int start, int end) {
        if (start > end - 3) {
            return start;
        }
        int i = start;
        for (; i < end; i++) {
            if (i > end - 2) {
                i = end;
                break;
            }
            if (source[i] != source[i + 1]) {
                i = i + 1;
                break;
            }
        }
        return i;
    }

    /**
     * Returns a length of original (raw, decompressed) array of bytes for the specified compressed array.
     *
     * @param encoded {@code Array}
     * @return {@code int}
     */
    public int getRawArrayLength(byte[] encoded) {
        int length = 0;
        for (int i = 0; i < encoded.length; i++) {
            byte series = encoded[i];
            if (series == 0) {
                throw new IllegalArgumentException("Wrong array specified");
            }
            if (series > 0) {
                length += encoded[i];
                i++;
                continue;
            }
            length -= series;
            i -= series;
        }
        return length;
    }
}
