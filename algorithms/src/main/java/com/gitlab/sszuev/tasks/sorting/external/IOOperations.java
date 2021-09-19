package com.gitlab.sszuev.tasks.sorting.external;

import java.io.IOException;
import java.nio.ByteBuffer;
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
 * Created by @ssz on 19.09.2021.
 */
public class IOOperations {
    private static final int IO_TIMEOUT_IN_SECONDS = 5;

    public static char readChar(AsynchronousFileChannel source, long byteIndex) throws IOException {
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

    public static void writeChar(AsynchronousFileChannel source, long byteIndex, char value) throws IOException {
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

    public static void copy(Path source, AsynchronousFileChannel target, long targetPosition, int bufferSize) throws IOException {
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

    public static void read(AsynchronousFileChannel channel, long position, ByteBuffer bytes) throws IOException {
        performAsync(channel.read(bytes, position), bytes.limit());
    }

    public static void write(AsynchronousFileChannel channel, long position, ByteBuffer bytes) throws IOException {
        write(channel, position, bytes, bytes.limit());
    }

    public static void write(AsynchronousFileChannel channel,
                             long position,
                             ByteBuffer bytes,
                             int expectedReadBytes) throws IOException {
        performAsync(channel.write(bytes, position), expectedReadBytes);
    }

    public static void write(SeekableByteChannel channel, ByteBuffer bytes, long position) throws IOException {
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
}
