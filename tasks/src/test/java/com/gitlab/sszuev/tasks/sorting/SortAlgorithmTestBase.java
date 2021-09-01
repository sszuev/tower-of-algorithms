package com.gitlab.sszuev.tasks.sorting;

import com.gitlab.sszuev.tasks.RunTestEngine;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Created by @ssz on 29.08.2021.
 */
abstract class SortAlgorithmTestBase extends RunTestEngine {

    public static Stream<Data> listData() throws Exception {
        Path res = externalDir();
        if (res != null) {
            return loadTestData(res).stream();
        }
        return Stream.of("digits", "random", "revers", "sorted").flatMap(SortAlgorithmTestBase::listFor);
    }

    private static Stream<Data> listFor(String dir) {
        try {
            return listData("/sorting/" + dir).map(x -> x.withName(dir));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
