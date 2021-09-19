package com.gitlab.sszuev.tasks.sorting.external;

import com.gitlab.sszuev.tasks.sorting.JDKDualPivotQuickSortAlgorithm;
import com.gitlab.sszuev.utils.IOUtils;
import com.gitlab.sszuev.utils.RandomFileGenerator;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

/**
 * Just demo.
 */
public class MergeSortDemo {

    private static final Logger LOGGER = Logger.getInstance(System.out);

    public static final long UPPER_LIMIT_CHUNK_SIZE_IN_BYTES = 50 * 1024 * 1024;

    public static void main(String... xxx) throws IOException {
        Instant s = Instant.now();

        Path file = IOUtils.newTempFile("BIG-FILE-", ".bytes");
        LOGGER.log(file);

        // 1. generate
        exec("generate", () -> generate(file, 2 * 1_000_000_000)); // 1e9 16-bit numbers

        // 2. validate is not sorted
        exec("validate-before", () -> validate(file, false));

        // 3. sort chunks
        exec("sort", () -> sort(file));

        // 4. merge chunks
        exec("merge", () -> merge(file));

        // 5. validate is sorted
        exec("validate-after", () -> validate(file, true));

        LOGGER.log("[TOTAL]::%s", Duration.between(s, Instant.now()));
    }

    private static void exec(String msg, Operation op) {
        msg = msg.toUpperCase();
        LOGGER.log("[" + msg + "]::START");
        Instant s = Instant.now();
        RuntimeException ex = null;
        try {
            op.run();
        } catch (Exception e) {
            ex = new RuntimeException(e);
        }
        Instant e = Instant.now();
        LOGGER.log("[" + msg + "]::" + Duration.between(s, e));
        if (ex != null) throw ex;
    }

    public static void generate(Path file, long sizeInBytes) throws IOException {
        new RandomFileGenerator().generate(file, sizeInBytes);
    }

    public static void sort(Path file) throws IOException {
        CharMergeSortEngine.sortChunks(file,
                new JDKDualPivotQuickSortAlgorithm(),
                UPPER_LIMIT_CHUNK_SIZE_IN_BYTES
        );
    }

    public static void merge(Path file) throws IOException {
        CharMergeSortEngine.mergeChunks(file, UPPER_LIMIT_CHUNK_SIZE_IN_BYTES, 8);
    }

    public static void validate(Path file, boolean expectSorted) throws IOException {
        boolean actual = CharMergeSortEngine.isSorted(file, UPPER_LIMIT_CHUNK_SIZE_IN_BYTES);
        if (expectSorted != actual) {
            throw new IllegalStateException("Expected " + (expectSorted ? "SORTED" : "UNSORTED"));
        }
    }

    interface Operation {
        void run() throws Exception;
    }


}
