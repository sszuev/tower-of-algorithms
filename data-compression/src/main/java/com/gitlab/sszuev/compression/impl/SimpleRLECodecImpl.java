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
import java.util.function.LongConsumer;

/**
 * Run-length encoding (RLE).
 * <p>
 * Example: {@code WWWWWWWWWBBBWWWWWWWWWWWWWWWWWWWWWWWWBWWWWWWWWWWWWWW => 9W3B24W1B14W}
 * <p>
 * Created by @ssz on 12.12.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Run-length_encoding'>Run-length encoding</a>
 */
public class SimpleRLECodecImpl implements BinaryCodec, FileCodec {
    private static final int MAX_BYTES_IN_SEQUENCE = 256;

    private final LongConsumer listener;

    public SimpleRLECodecImpl() {
        this(null);
    }

    public SimpleRLECodecImpl(LongConsumer listener) {
        this.listener = listener;
    }

    @Override
    public void encode(Path source, Path target) throws IOException {
        this.encode(source, target, DEFAULT_BUFFER_SIZE);
    }

    public void encode(Path source, Path target, int bufferLength) throws IOException {
        long sourceSize = Files.size(Objects.requireNonNull(source, "Null source"));
        Objects.requireNonNull(target, "Null target");
        if (sourceSize <= bufferLength / 3) {
            byte[] original = Files.readAllBytes(source);
            byte[] encoded = this.encode(original);
            Files.write(target, encoded);
            return;
        }
        this.encode(() -> FileCodec.newReadChannel(source), () -> FileCodec.newWriteChannel(target), bufferLength);
    }

    @Override
    public byte[] encode(byte[] raw) {
        byte[] res = new byte[raw.length * 2];
        int resLength = encode(raw, raw.length, res);
        return res.length == resLength ? res : Arrays.copyOf(res, resLength);
    }

    @Override
    public void encode(IOSupplier<? extends ReadableByteChannel> source,
                       IOSupplier<? extends WritableByteChannel> target,
                       int bufferLength) throws IOException {
        // the write-buffer capacity must be twice the read-buffer capacity for the worse case
        int readBufferCapacity = bufferLength / 3;
        int writeBufferCapacity = 2 * readBufferCapacity;

        ByteBuffer readBuffer = ByteBuffer.allocate(readBufferCapacity);
        ByteBuffer writeBuffer = ByteBuffer.allocate(writeBufferCapacity);

        try (ReadableByteChannel readChannel = source.open();
             WritableByteChannel writeChannel = target.open()) {
            SeekableByteChannel seekable = writeChannel instanceof SeekableByteChannel ?
                    (SeekableByteChannel) writeChannel : null;

            // the number of bytes read, possibly zero, or -1 if the channel has reached end-of-stream
            int readLength;
            // the number of bytes written, possibly zero
            int writeLength;
            byte endSymbol = 0;
            byte endCount = 0;

            while ((readLength = readChannel.read(readBuffer)) > 0) {
                record(readLength);
                readBuffer.rewind();

                writeLength = encode(readBuffer.array(), readLength, writeBuffer.array());
                writeBuffer.limit(writeLength);
                writeBuffer.position(0);

                if (seekable != null) {
                    byte startSymbol = writeBuffer.get(1);
                    byte startCount = writeBuffer.get(0);
                    int sum;
                    if (endSymbol != 0 &&
                            startSymbol == endSymbol &&
                            (sum = unsignedIntSum(startCount, endCount)) <= MAX_BYTES_IN_SEQUENCE) {
                        writeBuffer.put(0, (byte) sum);
                        // shift two symbols back
                        seekable.position(seekable.position() - 2);
                    }
                    // remember the last two symbols
                    endSymbol = writeBuffer.get(writeLength - 1);
                    endCount = writeBuffer.get(writeLength - 2);
                }

                int writeBytes = writeChannel.write(writeBuffer);
                if (writeBytes != writeLength) {
                    throw new IllegalStateException();
                }
            }
            record(readLength);
        }
    }

    private static int unsignedIntSum(byte a, byte b) {
        return Byte.toUnsignedInt(a) + Byte.toUnsignedInt(b);
    }

    /**
     * Encodes the data using RLE algorithm.
     *
     * @param source   the array with original data
     * @param sourceTo the final index of the source array to end encoding,
     *                 exclusive (this index may lie outside the array)
     * @param target   the array with result (encoded) data
     * @return the final index of the target array, exclusive
     */
    protected int encode(byte[] source, int sourceTo, byte[] target) {
        int length = 0;
        int series = 1;
        for (int i = 1; ; i++) {
            if (i >= sourceTo) {
                target[length++] = (byte) series;
                target[length++] = source[i - 1];
                break;
            }
            if (series < MAX_BYTES_IN_SEQUENCE - 1 && source[i] == source[i - 1]) {
                series++;
            } else {
                target[length++] = (byte) series;
                target[length++] = source[i - 1];
                series = 1;
            }
        }
        return length;
    }

    @Override
    public void decode(Path source, Path target) throws IOException {
        this.decode(source, target, DEFAULT_BUFFER_SIZE);
    }

    public void decode(Path source, Path target, int bufferLength) throws IOException {
        long sourceSize = Files.size(Objects.requireNonNull(source, "Null source"));
        if (sourceSize % 2 != 0) {
            throw new IllegalArgumentException("Wrong array specified");
        }
        Objects.requireNonNull(target, "Null target");
        if (sourceSize <= 2L * bufferLength / 3) {
            byte[] compressed = Files.readAllBytes(source);
            byte[] decompressed = this.decode(compressed);
            Files.write(target, decompressed);
            return;
        }
        this.decode(() -> FileCodec.newReadChannel(source), () -> FileCodec.newWriteChannel(target), bufferLength);
    }

    @Override
    public byte[] decode(byte[] encoded) {
        if (encoded.length == 0 || encoded.length % 2 != 0) {
            throw new IllegalArgumentException("Wrong array specified");
        }
        int length = 0;
        for (int i = 0; i < encoded.length; i += 2) {
            length += Byte.toUnsignedInt(encoded[i]);
        }
        byte[] res = new byte[length];
        int j = 0;
        for (int i = 0; i < encoded.length; i += 2) {
            int s = Byte.toUnsignedInt(encoded[i]);
            Arrays.fill(res, j, j += s, encoded[i + 1]);
        }
        return res;
    }

    @Override
    public void decode(IOSupplier<? extends ReadableByteChannel> source,
                       IOSupplier<? extends WritableByteChannel> target,
                       int bufferLength) throws IOException {
        int readBufferCapacity = bufferLength / 2 + bufferLength % 2;
        int writeBufferCapacity = bufferLength - readBufferCapacity;

        ByteBuffer readBuffer = ByteBuffer.allocate(readBufferCapacity);
        ByteBuffer writeBuffer = ByteBuffer.allocate(writeBufferCapacity);

        try (ReadableByteChannel readChannel = source.open();
             WritableByteChannel writeChannel = target.open()) {

            int readLength;
            while ((readLength = readChannel.read(readBuffer)) > 0) {
                record(readLength);
                int length = readBuffer.position();
                if (length % 2 != 0) {
                    throw new IllegalStateException();
                }
                readBuffer.rewind();
                int start = 0;
                for (int i = 0; i < length; i += 2) {
                    int count = Byte.toUnsignedInt(readBuffer.get(i));
                    byte data = readBuffer.get(i + 1);

                    int end = start + count;
                    while (end > writeBufferCapacity) {
                        Arrays.fill(writeBuffer.array(), start, writeBufferCapacity, data);
                        start = 0;
                        end -= writeBufferCapacity;

                        writeBuffer.limit(writeBufferCapacity);
                        writeChannel.write(writeBuffer);
                        writeBuffer.position(0);
                    }

                    Arrays.fill(writeBuffer.array(), start, end, data);
                    writeBuffer.limit(end);
                    start = end;
                }
                writeChannel.write(writeBuffer);
                writeBuffer.position(0);
            }
            record(readLength);
        }
    }

    protected void record(long readLength) {
        if (listener != null) {
            listener.accept(readLength);
        }
    }
}
