package com.gitlab.sszuev.tasks.sorting;

import com.gitlab.sszuev.tasks.Algorithm;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @see <a href='https://en.wikipedia.org/wiki/Sorting_algorithm#Comparison_of_algorithms'>wiki: comparison and non-comparison sorts</a>
 * @see <a href='https://en.wikipedia.org/wiki/Sorting_algorithm#Stability'>wiki: Stable sorting</a>
 * @see <a href='https://en.wikipedia.org/wiki/Adaptive_sort'>wiki: Adaptive sort</a>
 * @see <a href='https://en.wikipedia.org/wiki/Online_algorithm'>wiki: Ontline algorithm</a>
 * @see <a href='https://en.wikipedia.org/wiki/In-place_algorithm'>wiki: In-place algorithms</a>
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
