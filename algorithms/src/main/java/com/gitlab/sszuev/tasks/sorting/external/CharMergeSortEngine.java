package com.gitlab.sszuev.tasks.sorting.external;

import com.gitlab.sszuev.tasks.sorting.CharSort;
import com.gitlab.sszuev.utils.BufferUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by @ssz on 19.09.2021.
 */
public class CharMergeSortEngine {

    private static final Logger LOGGER = Logger.getInstance();

    private final CharSort method;
    private final long bufferSizeInBytes;
    private final int mergeThreadsPoolSize;

    public CharMergeSortEngine(CharSort method, long bufferSizeInBytes, int numberOfMergeThreads) {
        this.bufferSizeInBytes = requirePositive(bufferSizeInBytes);
        this.mergeThreadsPoolSize = requirePositive(numberOfMergeThreads);
        this.method = Objects.requireNonNull(method);
    }

    public void sort(Path file) throws IOException {
        sortChunks(file, method, bufferSizeInBytes);
        mergeChunks(file, bufferSizeInBytes, mergeThreadsPoolSize);
    }

    public static void sortChunks(Path file, CharSort method, long maxMemoryLimit) throws IOException {
        requirePositive(maxMemoryLimit);
        LOGGER.log("run chunk sorting");
        try (SeekableByteChannel channel = Files.newByteChannel(file, StandardOpenOption.READ, StandardOpenOption.WRITE)) {
            FileSplitInfo info = FileSplitInfo.collect(maxMemoryLimit, channel.size());
            for (int i = 0; i < info.size(); i++) {
                readSortWrite(channel, method, info.start(i), info.end(i));
            }
        }
        LOGGER.log("chunk sorting completed");
    }

    private static void readSortWrite(SeekableByteChannel channel,
                                      CharSort method,
                                      long startIndex,
                                      long endIndex) throws IOException {
        long length = endIndex - startIndex + 1;
        if (length >= Integer.MAX_VALUE) {
            throw new IllegalStateException();
        }
        ByteBuffer bytes = ByteBuffer.allocate((int) length);
        channel.position(startIndex);
        channel.read(bytes);
        bytes.rewind();

        CharBuffer chars = BufferUtils.toCharBuffer(bytes);
        method.sort(chars.array());
        BufferUtils.copy(chars, bytes);

        channel.position(startIndex);
        channel.write(ByteBuffer.wrap(bytes.array()));
    }

    public static void mergeChunks(Path file, long maxBuffetSize, int mergeThreadsPoolSize) throws IOException {
        requirePositive(maxBuffetSize);
        requirePositive(mergeThreadsPoolSize);
        LOGGER.log("run chunk merging");
        try (AsynchronousFileChannel channel = AsynchronousFileChannel.open(file, StandardOpenOption.WRITE, StandardOpenOption.READ)) {
            FileSplitInfo info = FileSplitInfo.collect(maxBuffetSize, channel.size());
            int chunks = info.size();

            if (chunks == 1) {
                // nothing to merge
                return;
            }
            int step = 1;
            while (step < chunks) {
                step <<= 1;
                ExecutorService service = Executors.newFixedThreadPool(mergeThreadsPoolSize);
                List<Future<Boolean>> futures = new ArrayList<>();
                long bufferLength = maxBuffetSize * step / chunks;
                for (int i = 0; i < chunks; i += step) {
                    int j = i + step / 2;
                    int k = i + step;

                    final long leftStart = info.start(i);
                    final long rightStart = info.start(j);
                    final long rightEnd = k == chunks ? info.end(chunks - 1) : info.start(k) - 1;

                    futures.add(service.submit(() -> {
                        MergeHalfSortedArrayHelper.merge(channel, bufferLength, leftStart, rightStart, rightEnd);
                        return Boolean.TRUE;
                    }));
                }
                service.shutdown();
                LOGGER.log("run %d merge tasks", chunks / step);
                for (Future<Boolean> f : futures) {
                    process(f);
                }
            }
        }
        LOGGER.log("chunk merging completed");
    }

    private static void process(Future<Boolean> future) throws IOException {
        try {
            if (future.get() != Boolean.TRUE) {
                throw new IllegalStateException();
            }
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof IOException) {
                throw (IOException) cause;
            }
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            throw new IllegalStateException(e);
        }
    }

    private static long requirePositive(long v) {
        if (v <= 0) {
            throw new IllegalArgumentException();
        }
        return v;
    }

    private static int requirePositive(int v) {
        if (v <= 0) {
            throw new IllegalArgumentException();
        }
        return v;
    }

    public static boolean isSorted(Path file, long maxMemoryLimit) throws IOException {
        int min = -1;
        try (SeekableByteChannel channel = Files.newByteChannel(file, StandardOpenOption.READ)) {
            FileSplitInfo info = FileSplitInfo.collect(maxMemoryLimit, channel.size());
            for (int i = 0; i < info.size(); i++) {
                ByteBuffer bytes = ByteBuffer.allocate(info.size(i));
                channel.position(info.start(i));
                channel.read(bytes);
                bytes.rewind();
                char[] chars = BufferUtils.toCharBuffer(bytes).array();
                if (chars[0] < min) {
                    return false;
                }
                if (!isSorted(chars)) {
                    return false;
                }
                min = chars[chars.length - 1];
            }
        }
        return true;
    }

    public static boolean isSorted(char[] chars) {
        for (int j = 0; j < chars.length; j++) {
            if (j == 0) {
                continue;
            }
            if (chars[j] < chars[j - 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * A holder for the file split information.
     */
    public static class FileSplitInfo {

        private final long maxLimit;
        // last indexes here
        private final List<Long> indexes;

        private FileSplitInfo(long upperLimit, List<Long> indexes) {
            this.maxLimit = upperLimit;
            this.indexes = indexes;
        }

        /**
         * Collects a file split info.
         * A file consists of chunks, the number of chunks must be power of two.
         * The first chunk can be odd, all other should have even length.
         *
         * @param maxLimit {@code long} upper limit
         * @param length   {@code long} a file size
         * @return {@link FileSplitInfo}
         */
        public static FileSplitInfo collect(final long maxLimit, final long length) {
            if (length < maxLimit) {
                return new FileSplitInfo(maxLimit, List.of(length - 1));
            }
            float chs = length;
            long num = 1;
            while (chs > maxLimit) {
                chs /= 2;
                num <<= 1;
            }
            if (num > Integer.MAX_VALUE) {
                throw new IllegalArgumentException();
            }
            int chunk = ((int) Math.ceil(chs) >> 1) << 1;
            long first = length - chunk * (num - 1);
            List<Integer> chunks = new ArrayList<>((int) num);
            chunks.add((int) first);
            for (int i = 1; i < num; i++) {
                chunks.add(chunk);
            }
            if (chunk < maxLimit - 2 && first > maxLimit) {
                int index = 1;
                while (chunks.get(0) > maxLimit && index < num) {
                    chunks.set(0, chunks.get(0) - 2);
                    chunks.set(index, chunks.get(index) + 2);
                    index++;
                }
            }
            List<Long> indexes = new ArrayList<>((int) num);
            for (int i = 0; i < num; i++) {
                indexes.add(chunks.get(i) - 1 + chunks.subList(0, i).stream().mapToLong(x -> x).sum());
            }
            return new FileSplitInfo(maxLimit, Collections.unmodifiableList(indexes));
        }

        public long start(int i) {
            if (index(i) == 0) {
                return 0;
            }
            return indexes.get(i - 1) + 1;
        }

        public long end(int i) {
            return indexes.get(index(i));
        }

        public int size(int i) {
            return (int) (end(i) - start(i) + 1);
        }

        public long length() {
            return end(indexes.size() - 1) + 1;
        }

        public int size() {
            return indexes.size();
        }

        private int index(int i) {
            if (i < 0 || i >= indexes.size()) {
                throw new IndexOutOfBoundsException("i=" + i + ", chunks=" + indexes.size());
            }
            return i;
        }

        @Override
        public String toString() {
            int chunks = indexes.size();
            long length = indexes.get(chunks - 1) + 1;
            long firstChunkSize = indexes.get(0) + 1;
            long lastChunkSize = chunks > 1 ? length - indexes.get(indexes.size() - 2) - 1 : -1;
            return String.format("FileSplitInfo{first=%d ... last=%d, max=%d, chunks=%d, length=%d}",
                    firstChunkSize, lastChunkSize, maxLimit, chunks, length);
        }
    }
}
