package com.gitlab.sszuev.tasks.sorting;

/**
 * <b>Bubble Sort Algorithm (for Integers)</b>
 * It is <b>non-adaptive</b>, <b>stable</b>, <b>non-online</b>, <b>in-place</b> sorting.
 * <ul>
 * <li>Average case performance: {@code O(n^2)} (comparisons and swaps)</li>
 * <li>Average case memory: {@code O(1)}</li>
 * <li>Best case performance: {@code O(n)} comparisons and {@code O(1)} swaps</li>
 * <li>Worst case performance: {@code O(n^2)} (comparisons and swaps)</li>
 * <li>Worst-case memory: {@code O(n)}</li>
 * </ul>
 * {@inheritDoc}
 * Created by @ssz on 29.08.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Bubble_sort'>wiki: Bubble Sort</a>
 * @see <a href='https://javarevisited.blogspot.com/2014/08/bubble-sort-algorithm-in-java-with.html'>javarevisited: Bubble Sort</a>
 * @see <a href='https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html'>Visualization</a>
 */
public class BubbleSortAlgorithm extends BaseIntegerSortAlgorithm {

    public static void sortInts(int[] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = array.length - 1; j > i; j--) {
                if (array[j] < array[j - 1]) {
                    swap(array, j, j - 1);
                }
            }
        }
    }

    private static void swap(int[] array, int from, int to) {
        int temp = array[from];
        array[from] = array[to];
        array[to] = temp;
    }

    @Override
    public void sort(int[] array) {
        sortInts(array);
    }
}

