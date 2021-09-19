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
        return Stream.of(new JDKDualPivotQuickSortAlgorithm());
    }

    @ParameterizedTest
    @MethodSource({"innerSorts"})
    public void testMergeSorting(CharSort sort) throws IOException {
        Path file = IOUtils.newTempFile(ExternalSortAlgorithmTest.class.getSimpleName() + "-testExternalSorting-", ".bytes");

        new RandomFileGenerator().generate(file, 2 * 1024 * 1024); // 2 MB

        ExternalSortAlgorithm alg = new ExternalSortAlgorithm(sort, 5 * 1024 * 1024); // sort in mem for this test

        Instant s = Instant.now();
        Assertions.assertEquals(file, alg.sort(file.toString()));
        Instant e = Instant.now();

        boolean res = isSorted(file);

        String name = ((Algorithm) sort).name();
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
