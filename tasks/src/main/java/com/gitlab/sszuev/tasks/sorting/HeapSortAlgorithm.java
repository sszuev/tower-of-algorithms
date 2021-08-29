package com.gitlab.sszuev.tasks.sorting;

/**
 * <b>Heap Sort Algorithm (for Integers)</b>
 * It is <b>non-adaptive</b>, <b>non-stable</b>, <b>in-place</b>, <b>non-online</b> sorting.
 * <ul>
 * <li>Worst case performance: {@code O(n * log(n))}</li>
 * <li>Best case performance: {@code O(n * log(n))} (distinct keys) and {@code O(n)} (equal keys)</li>
 * <li>Average case performance: {@code O(n * log(n))}</li>
 * <li>Worst-case space complexity: {@code O(n)} total, {@code O(1)} auxiliary</li>
 * </ul>
 * Created by @ssz on 29.08.2021.
 * @see <a href='https://en.wikipedia.org/wiki/Heapsort'>Heap Sort</a>
 */
public class HeapSortAlgorithm extends BaseIntegerSortAlgorithm {

    public static void sort(int[] array) {
        for (int root = array.length / 2 - 1; root >= 0; root--) {
            heapify(array, root, array.length);
        }
        for (int j = array.length - 1; j >= 0; j--) {
            swap(array, 0, j);
            heapify(array, 0, j);
        }
    }

    private static void heapify(int[] array, int root, int size) {
        int left = 2 * root + 1;
        int right = 2 * root + 2;
        int x = root;
        if (left < size && array[left] > array[x]) {
            x = left;
        }
        if (right < size && array[right] > array[x]) {
            x = right;
        }
        if (x == root) {
            return;
        }
        swap(array, x, root);
        heapify(array, x, size);
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
