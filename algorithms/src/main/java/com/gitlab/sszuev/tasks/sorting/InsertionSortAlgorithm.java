package com.gitlab.sszuev.tasks.sorting;

/**
 * <b>Insertion Sort (for Integers).</b>
 * It is <b>adaptive</b>, <b>stable</b>, <b>in-place</b>, <b>online</b> sorting.
 * <ul>
 * <li>Worst case performance, {@code O(n^2)} (comparisons and swaps)</li>
 * <li>Best case performance, {@code O(n)} comparisons and {@code O(1)} swaps</li>
 * <li>Average case performance, {@code O(n^2)} (comparisons and swaps)</li>
 * <li>Worst-case space complexity: {@code O(n)} total, {@code O(1)} auxiliary</li>
 * </ul>
 * Created by @ssz on 29.08.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Insertion_sort'>wiki: Insertion sort</a>
 * @see <a href='https://www.java67.com/2014/09/insertion-sort-in-java-with-example.html'>java67: Insertion Sort</a>
 * @see <a href='https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html'>Sorting Algorithms Visualization</a>
 */
public class InsertionSortAlgorithm extends BaseIntegerSortAlgorithm {

    public static void sort(int[] array) {
        for (int i = 1; i < array.length; i++) {
            int item = array[i];
            int index = i;
            while (index > 0 && array[index - 1] > item) {
                array[index] = array[index - 1];
                index--;
            }
            array[index] = item;
        }
    }

    @Override
    void performSorting(int[] array) {
        sort(array);
    }
}
