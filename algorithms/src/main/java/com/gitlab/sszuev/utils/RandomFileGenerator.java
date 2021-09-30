package com.gitlab.sszuev.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by @ssz on 05.09.2021.
 */
public class RandomFileGenerator {
    private static final int DEFAULT_BUFFER_SIZE = 8192;
    private static final Random RANDOM = new Random();

    private final Random random;
    private final int bufferSize;

    public RandomFileGenerator() {
        this(RANDOM, DEFAULT_BUFFER_SIZE);
    }

    public RandomFileGenerator(Random random, int bufferSize) {
        this.random = Objects.requireNonNull(random);
        this.bufferSize = bufferSize;
        if (bufferSize <= 0) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Generates a random file with the given size in bytes.
     *
     * @param file        {@code Path}
     * @param sizeInBytes {@code long}
     * @throws IOException if something is wrong
     */
    public void generate(Path file, long sizeInBytes) throws IOException {
        if (sizeInBytes <= 0) {
            throw new IllegalArgumentException();
        }
        try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(file,
                StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW)) {
            try {
                writeWhole(channel, sizeInBytes);
            } catch (ExecutionException | InterruptedException | TimeoutException e) {
                throw new IOException("Can't generate file " + file, e);
            }
        }
    }

    private void writeWhole(AsynchronousFileChannel channel, long sizeInBytes)
            throws ExecutionException, InterruptedException, TimeoutException {
        List<Future<Integer>> futures = new ArrayList<>();
        for (long startIndex = 0; startIndex < sizeInBytes; startIndex += bufferSize) {
            long endIndex = startIndex + bufferSize - 1;
            if (sizeInBytes - startIndex < 3L * bufferSize / 2) {
                endIndex = sizeInBytes - 1;
                futures.add(writeSlice(channel, startIndex, endIndex));
                break;
            }
            futures.add(writeSlice(channel, startIndex, endIndex));
        }
        for (Future<Integer> future : futures) {
            future.get(1L, TimeUnit.MINUTES);
        }
    }

    private Future<Integer> writeSlice(AsynchronousFileChannel channel, long start, long end) {
        return channel.write(nextRandomBuffer(start, end), start);
    }

    protected ByteBuffer nextRandomBuffer(long start, long end) {
        long length = end - start + 1;
        if (length >= Integer.MAX_VALUE) {
            throw new IllegalStateException("buffer length is too big");
        }
        ByteBuffer res = ByteBuffer.allocate((int) length);
        random.nextBytes(res.array());
        return res;
    }

}
