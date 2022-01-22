package com.gitlab.sszuev.tasks.sorting;

import com.gitlab.sszuev.tasks.Algorithm;

import java.nio.file.Path;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Created by @ssz on 22.01.2022.
 */
public class WithMax10CountingSortAlgorithmTest extends SortAlgorithmTestBase {

    public static Stream<Data> listData() throws Exception {
        Path res = externalDir();
        if (res != null) {
            return loadTestData(res).stream();
        }
        return Stream.of(listFor("digits"), listFor("random", 2), listFor("revers", 2), listFor("sorted", 2))
                .flatMap(Function.identity());
    }

    @Override
    public Algorithm getTaskToTest() {
        return new CountingSortAlgorithm(10);
    }
}
