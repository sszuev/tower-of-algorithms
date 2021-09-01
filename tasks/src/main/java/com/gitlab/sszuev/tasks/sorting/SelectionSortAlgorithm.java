package com.gitlab.sszuev.tasks.sorting;

/**
 * <b>Bubble Sort Algorithm (for Integers)</b>
 * It is <b>non-adaptive</b>, <b>stable</b>, <b>in-place</b>, <b>non-online</b> sorting.
 * <ul>
 * <li>Worst case performance, {@code O(n^2)} (comparisons and swaps)</li>
 * <li>Best case performance, {@code O(n)} comparisons and {@code O(1)} swaps</li>
 * <li>Average case performance, {@code O(n^2)} (comparisons and swaps)</li>
 * <li>Worst-case space complexity: {@code O(n)} total, {@code O(1)} auxiliary</li>
 * </ul>
 * Created by @ssz on 29.08.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Selection_sort'>wiki: Selection Sort</a>
 * @see <a href='https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html'>Sorting Algorithms Visualization</a>
 */
public class SelectionSortAlgorithm extends BaseIntegerSortAlgorithm {

    public static void sort(int[] array) {
        for (int i = array.length - 1; i >= 0; i--) {
            int m = i;
            for (int j = 0; j < i; j++) {
                if (array[j] > array[m]) {
                    m = j;
                }
            }
            swap(array, m, i);
        }
    }

    private static void swap(int[] array, int from, int to) {
        int temp = array[from];
        array[from] = array[to];
        array[to] = temp;
    }

    @Override
    void performSorting(int[] array) {
        sort(array);
    }
}