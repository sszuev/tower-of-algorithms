package com.gitlab.sszuev.tasks.sorting;

/**
 * <b>Bubble Sort Algorithm (for Integers)</b>
 * <p>
 * Bubble sort Worst case performance, {@code O(n^2)}
 * Bubble sort Best case performance, {@code O(n)}
 * Bubble sort Average case performance, {@code O(n^2)}
 * <p>
 * Created by @ssz on 29.08.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Bubble_sort'>wiki: Bubble Sort</a>
 * @see <a href='https://javarevisited.blogspot.com/2014/08/bubble-sort-algorithm-in-java-with.html'>javarevisited: Bubble Sort</a>
 * @see <a href='https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html'>Sorting Algorithms Visualization</a>
 */
public class BubbleSortAlgorithm extends BaseIntegerSortAlgorithm {

    public static void sort(int[] array) {
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
    void performSorting(int[] array) {
        sort(array);
    }
}

