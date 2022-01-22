package com.gitlab.sszuev.tasks.sorting;

/**
 * <b>Merge Sort (ints).</b>
 * It is <b>non-adaptive</b>, <b>stable</b>, <b>online</b>(somehow) sorting.
 * <ul>
 * <li>Average case performance: {@code O(n*log(n))}</li>
 * <li>Average case memory: {@code O(n)} (possible {@code O(1)}) with linked lists</li>
 * <li>Best case performance: {@code O(n*log(n))}</li>
 * <li>Worst case performance: {@code O(n*log(n))}</li>
 * <li>Worst-case memory: {@code O(n)}</li>
 * </ul>
 * {@inheritDoc}
 * Created by @ssz on 19.09.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Merge_sort'>wiki: Merge sort</a>
 * @see <a href='https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html'>Visualization</a>
 * @see <a href='https://www.java67.com/2018/03/mergesort-in-java-algorithm-example-and.html'>Merge Sort - Algorithm Example</a>
 */
public class MergeSortAlgorithm extends BaseIntegerSortAlgorithm implements IntSort, CharSort {

    public static void sortInts(int[] array) {
        mergeSort(array, 0, array.length - 1);
    }

    public static void sortChars(char[] array) {
        mergeSort(array, 0, array.length - 1);
    }

    private static void mergeSort(int[] input, int start, int end) {
        int mid = (start + end) / 2;
        if (start < end) {
            mergeSort(input, start, mid);
            mergeSort(input, mid + 1, end);
        }

        int i = 0, first = start, last = mid + 1;
        int[] tmp = new int[end - start + 1];

        while (first <= mid && last <= end) {
            tmp[i++] = input[first] < input[last] ? input[first++] : input[last++];
        }
        while (first <= mid) {
            tmp[i++] = input[first++];
        }
        while (last <= end) {
            tmp[i++] = input[last++];
        }
        i = 0;
        while (start <= end) {
            input[start++] = tmp[i++];
        }
    }

    private static void mergeSort(char[] input, int start, int end) {
        int mid = (start + end) / 2;
        if (start < end) {
            mergeSort(input, start, mid);
            mergeSort(input, mid + 1, end);
        }

        int i = 0, first = start, last = mid + 1;
        char[] tmp = new char[end - start + 1];

        while (first <= mid && last <= end) {
            tmp[i++] = input[first] < input[last] ? input[first++] : input[last++];
        }
        while (first <= mid) {
            tmp[i++] = input[first++];
        }
        while (last <= end) {
            tmp[i++] = input[last++];
        }
        i = 0;
        while (start <= end) {
            input[start++] = tmp[i++];
        }
    }


    @Override
    public void sort(int[] array) {
        sortInts(array);
    }

    @Override
    public void sort(char[] array) {
        sortChars(array);
    }
}
