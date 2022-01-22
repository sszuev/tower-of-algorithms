package com.gitlab.sszuev.tasks.sorting;

/**
 * <b>Insertion Sort (for Integers).</b>
 * It is <b>adaptive</b>, <b>stable</b>, <b>online</b>, <b>in-place</b> sorting.
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
 * @see <a href='https://en.wikipedia.org/wiki/Insertion_sort'>wiki: Insertion sort</a>
 * @see <a href='https://www.java67.com/2014/09/insertion-sort-in-java-with-example.html'>java67: Insertion Sort</a>
 * @see <a href='https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html'>Visualization</a>
 */
public class InsertionSortAlgorithm extends BaseIntegerSortAlgorithm {

    public static void sortInts(int[] array) {
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
    public void sort(int[] array) {
        sortInts(array);
    }
}
