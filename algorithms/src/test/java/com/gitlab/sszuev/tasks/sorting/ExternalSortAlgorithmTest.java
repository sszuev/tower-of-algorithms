package com.gitlab.sszuev.tasks.sorting;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.RunTestEngine;
import com.gitlab.sszuev.tasks.TestPropertiesSupport;
import com.gitlab.sszuev.tasks.sorting.external.CharMergeSortEngine;
import com.gitlab.sszuev.utils.BufferUtils;
import com.gitlab.sszuev.utils.IOUtils;
import com.gitlab.sszuev.utils.RandomFileGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Stream;

/**
 * Created by @ssz on 19.09.2021.
 */
public class ExternalSortAlgorithmTest {

    @BeforeAll
    public static void beforeClass() {
        System.out.println("==".repeat(42));
    }

    public static Stream<CharSort> innerSorts() {
        return Stream.of(new JDKDualPivotQuickSortAlgorithm(), new IterativeQuickSortAlgorithm(), new MergeSortAlgorithm());
    }

    @ParameterizedTest
    @MethodSource({"innerSorts"})
    public void testMergeSortingInMem(CharSort sort) throws IOException {
        testMergeSorting(sort, 2 * 1024 * 1024, 5 * 1024 * 1024);
    }

    @ParameterizedTest
    @MethodSource({"innerSorts"})
    public void testMergeSortingExternal(CharSort sort) throws IOException {
        testMergeSorting(sort, 4 * 1024 * 1024, 1024 * 1024);
    }

    private void testMergeSorting(CharSort sort, long fileSize, long bufferSize) throws IOException {
        Path file = IOUtils.newTempFile(ExternalSortAlgorithmTest.class.getSimpleName() + "-testExternalSorting-", ".bytes");

        new RandomFileGenerator().generate(file, fileSize); // 2 MB

        ExternalSortAlgorithm alg = new ExternalSortAlgorithm(sort, bufferSize); // sort in mem for this test

        Instant s = Instant.now();
        Assertions.assertEquals(file, alg.sort(file.toString()));
        Instant e = Instant.now();

        boolean res = isSorted(file);

        String name = ((Algorithm) sort).name() + "-" + (fileSize / 1024 / 1024) + "KB";
        String msg = RunTestEngine.formatMessage(ExternalSortAlgorithm.class.getSimpleName(), name, res, Duration.between(s, e));
        System.out.println(msg);

        if (TestPropertiesSupport.USE_ASSERTIONS) {
            Assertions.assertTrue(res);
        }
    }

    private boolean isSorted(Path file) throws IOException {
        byte[] bytes = Files.readAllBytes(file);
        char[] chars = BufferUtils.toCharBuffer(ByteBuffer.wrap(bytes)).array();
        return CharMergeSortEngine.isSorted(chars);
    }

}
