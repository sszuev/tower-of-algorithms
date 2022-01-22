package com.gitlab.sszuev.tasks.sorting;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>Radix Sort (ints)</b>
 * It is <b>non-adaptive</b>, <b>stable</b>, <b>non-online</b> sorting.
 * <ul>
 * <li>Average case performance: {@code O(k*n)}</li>
 * <li>Average case memory: {@code O(n+k)}</li>
 * </ul>
 * <p>
 * Time Complexity of radix sort in the best case, average case, and worst case is {@code O(k * n)}
 * where {@code k} is the length of the longest number, and {@code n} is the size of the input array.
 * Note: if {@code k} is greater than {@code log(n)} then a {@code n * log(n)} algorithm would be a better fit.
 * In reality, we can always change the Radix to make {@code k} less than {@code log(n)}.
 * {@inheritDoc}
 * Created by @ssz on 19.09.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Radix_sort'>wiki: Radix sort</a>
 * @see <a href='https://www.java67.com/2018/03/how-to-implement-radix-sort-in-java.html'>Radix Sort</a>
 */
public class RadixSortAlgorithm extends BaseIntegerSortAlgorithm implements IntSort, CharSort {

    public static void sortInts(int[] array) {
        final int radix = 10;
        @SuppressWarnings("unchecked") List<Integer>[] bucket = new List[radix];
        for (int i = 0; i < bucket.length; i++) {
            bucket[i] = new ArrayList<>();
        }
        boolean maxLength = false;
        int tmp;
        int placement = 1;
        while (!maxLength) {
            maxLength = true;
            for (int i : array) {
                tmp = i / placement;
                bucket[tmp % radix].add(i);
                if (maxLength && tmp > 0) {
                    maxLength = false;
                }
            }
            int a = 0;
            for (int b = 0; b < radix; b++) {
                for (Integer i : bucket[b]) {
                    array[a++] = i;
                }
                bucket[b].clear();
            }
            placement *= radix;
        }
    }

    public static void sortChars(char[] array) {
        final int radix = 10;
        @SuppressWarnings("unchecked") List<Character>[] bucket = new List[radix];
        for (int i = 0; i < bucket.length; i++) {
            bucket[i] = new ArrayList<>();
        }
        // sort
        boolean maxLength = false;
        int tmp;
        int placement = 1;
        while (!maxLength) {
            maxLength = true;
            for (char i : array) {
                tmp = i / placement;
                bucket[tmp % radix].add(i);
                if (maxLength && tmp > 0) {
                    maxLength = false;
                }
            }
            int a = 0;
            for (int b = 0; b < radix; b++) {
                for (Character i : bucket[b]) {
                    array[a++] = i;
                }
                bucket[b].clear();
            }
            placement *= radix;
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
