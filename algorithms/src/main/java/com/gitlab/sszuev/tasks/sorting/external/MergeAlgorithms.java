package com.gitlab.sszuev.tasks.sorting.external;

/**
 * Created by @ssz on 19.09.2021.
 */
public class MergeAlgorithms {
    /**
     * Merges two sorted halves of array into a new array.
     * Time complexity {@code O(n)}, memory complexity {@code O(n)}.
     *
     * @param array     an {@code Array} of {@code char}s
     * @param delimiter a first index of the second part
     * @return sorted {@code Array} of {@code char}s
     */
    public static char[] merge(char[] array, int delimiter) {
        char[] res = new char[array.length];
        int i = 0;
        int j = delimiter;
        int index = 0;
        while (index < array.length) {
            char item;
            if (i >= delimiter) {
                item = array[j++];
            } else if (j >= array.length) {
                item = array[i++];
            } else if (array[i] <= array[j]) {
                item = array[i++];
            } else {
                item = array[j++];
            }
            res[index++] = item;
        }
        return res;
    }

    /**
     * Merges two sorted halves of array into a new array with no additional memory.
     * Time complexity {@code O(n * log(n))}, memory complexity {@code O(1)}.
     * See <a href='https://www.geeksforgeeks.org/efficiently-merging-two-sorted-arrays-with-o1-extra-space/'>algorithm description</a>
     *
     * @param array an {@code Array} of {@code char}s
     * @return sorted {@code Array} of {@code char}s, the same
     */
    public static char[] merge(char[] array) {
        int gap = calcGap(array.length);
        for (; gap > 0; gap = calcGap(gap)) {
            for (int i = 0, j = gap; j < array.length; i++, j++) {
                char left = array[i];
                char right = array[j];
                if (left > right) {
                    array[j] = left;
                    array[i] = right;
                }
            }
        }
        return array;
    }

    private static int calcGap(int gap) {
        return gap > 1 ? gap / 2 + gap % 2 : 0;
    }
}
