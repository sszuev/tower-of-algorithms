package com.gitlab.sszuev.tasks.sorting;

/**
 * <b>Counting Sort (non-negative ints with given maximum)</b>
 * It is <b>non-adaptive</b>, <b>stable</b>, <b>non-online</b>, <b>non-in-place</b> sorting.
 * <ul>
 * <li>Average case performance: {@code O(n + r)}, where k is the range of the non-negative key values</li>
 * <li>Average case memory: {@code O(n + r)}</li>
 * <li>Best case performance: {@code O(n + r)}</li>
 * <li>Worst case performance: {@code O(n + r)}</li>
 * </ul>
 * One of the few linear sorting algorithms or {@code O(n)} sorting algorithm.
 * It is an integer-based sorting algorithm.
 * Time Complexity of the Counting Sort is {@code O(n + k)} in the best case,
 * average case and worst case, where {@code n} is the size of the input array
 * and {@code k} is the maximum value in the array.
 * <p>
 * There is a key difference between {@link BucketSortAlgorithm Bucket Sort} and Counting sort,
 * for example, Bucket sort uses a hash function to distribute values;
 * Counting sort, on the other hand, creates a counter for each value hence it is called Counting Sort algorithm.
 * The counting sort is a stable sort like multiple keys with the same value
 * are placed in the sorted array in the same order that they appear in the original input array.
 * The counting sort is not a comparison based algorithm. It's actually a non-comparison sorting algorithm.
 * {@inheritDoc}
 * Created by @ssz on 22.01.2022.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Counting_sort'>wiki: Counting sort</a>
 * @see <a href='https://www.baeldung.com/java-counting-sort'>baeldung: counting sort</a>
 */
public class CountingSortAlgorithm extends BaseIntegerSortAlgorithm {
    private final int max;

    public CountingSortAlgorithm(int max) {
        if (max <= 0) throw new IllegalArgumentException();
        this.max = max;
    }

    @Override
    public void sort(int[] array) {
        // create buckets
        int[] counter = new int[max + 1];
        // fill buckets
        for (int i : array) {
            counter[i]++;
        }
        // sort array
        int index = 0;
        for (int i = 0; i < counter.length; i++) {
            while (0 < counter[i]) {
                array[index++] = i;
                counter[i]--;
            }
        }
    }

    @Override
    public String name() {
        return String.format("%s-max=%d", getClass().getSimpleName(), max);
    }
}
