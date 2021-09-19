package com.gitlab.sszuev.tasks.sorting;

import java.util.Deque;
import java.util.LinkedList;

/**
 * <b>Quick Sort</b>
 * The quicksort algorithm is one of the important sorting algorithms.
 * Similar to merge sort, quicksort also uses divide-and-conquer.
 *
 * @see <a href='https://javarevisited.blogspot.com/2016/09/iterative-quicksort-example-in-java-without-recursion.html'>Iterative QuickSort (without Recursion)</a>
 * <p>
 * Created by @ssz on 19.09.2021.
 */
public class IterativeQuickSortAlgorithm extends BaseIntegerSortAlgorithm implements IntSort, CharSort {

    public static void sortInts(int[] array) {
        Deque<Integer> stack = new LinkedList<>();
        stack.push(0);
        stack.push(array.length);

        while (!stack.isEmpty()) {
            int end = stack.pop();
            int start = stack.pop();
            if (end - start < 2) {
                continue;
            }
            int p = (start + end) / 2;
            p = partition(array, p, start, end);

            stack.push(p + 1);
            stack.push(end);

            stack.push(start);
            stack.push(p);
        }
    }

    public static void sortChars(char[] array) {
        Deque<Integer> stack = new LinkedList<>();
        stack.push(0);
        stack.push(array.length);

        while (!stack.isEmpty()) {
            int end = stack.pop();
            int start = stack.pop();
            if (end - start < 2) {
                continue;
            }
            int p = (start + end) / 2;
            p = partition(array, p, start, end);

            stack.push(p + 1);
            stack.push(end);

            stack.push(start);
            stack.push(p);
        }
    }

    private static int partition(int[] input, int position, int start, int end) {
        int l = start;
        int h = end - 2;
        int piv = input[position];
        swap(input, position, end - 1);

        while (l < h) {
            if (input[l] < piv) {
                l++;
            } else if (input[h] >= piv) {
                h--;
            } else {
                swap(input, l, h);
            }
        }
        int idx = h;
        if (input[h] < piv) {
            idx++;
        }
        swap(input, end - 1, idx);
        return idx;
    }

    private static int partition(char[] input, int position, int start, int end) {
        int l = start;
        int h = end - 2;
        int piv = input[position];
        swap(input, position, end - 1);

        while (l < h) {
            if (input[l] < piv) {
                l++;
            } else if (input[h] >= piv) {
                h--;
            } else {
                swap(input, l, h);
            }
        }
        int idx = h;
        if (input[h] < piv) {
            idx++;
        }
        swap(input, end - 1, idx);
        return idx;
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    private static void swap(char[] arr, int i, int j) {
        char temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
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
