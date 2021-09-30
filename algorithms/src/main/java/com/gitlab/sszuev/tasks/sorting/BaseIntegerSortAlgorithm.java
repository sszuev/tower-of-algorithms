package com.gitlab.sszuev.tasks.sorting;

import com.gitlab.sszuev.tasks.Algorithm;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by @ssz on 29.08.2021.
 */
abstract class BaseIntegerSortAlgorithm implements Algorithm, IntSort {

    @Override
    public final List<String> run(String arg, String... other) {
        int length = Integer.parseInt(arg);
        int[] array = Arrays.stream(other[0].split("\\s+")).mapToInt(Integer::parseInt).toArray();
        if (array.length != length) {
            throw new IllegalArgumentException("Wrong length: " + length);
        }
        sort(array);
        return List.of(Arrays.stream(array).mapToObj(String::valueOf).collect(Collectors.joining(" ")));
    }
}
