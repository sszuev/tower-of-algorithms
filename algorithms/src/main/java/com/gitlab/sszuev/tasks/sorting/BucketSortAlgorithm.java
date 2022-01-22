package com.gitlab.sszuev.tasks.sorting;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <b>Bucket Sort (ints)</b>
 * It is <b>non-adaptive</b>, <b>stable</b>, <b>online</b>, <b>non-in-place</b> sorting.
 * <ul>
 * <li>Average case performance: {@code O(n)}</li>
 * <li>Average case memory: {@code O(n)}</li>
 * <li>Best case performance: {@code O(n)}</li>
 * <li>Worst case performance: {@code O(n^2)}</li>
 * <li>Worst-case memory: {@code O(n)}</li>
 * </ul>
 * <p>
 * Bucket Sort is also known as bin sort because you create bins or buckets to sort inputs.
 * <p>
 * The time complexity of bucket sort in the {@code O(n)} in the best and average case and {@code O(n^2)} in the worst case.
 * The space complexity of the bucket sort algorithm is {@code O(n)} because even in the worst of the good cases
 * (sequential values, but no repetition) the additional space needed is as big as the original array.
 * <p>
 * For bucket sort to work at its super fast speed, there are multiple prerequisites.
 * <ul>
 * <li>First, the hash function that is used to partition the elements must be very good and must produce ordered hash:
 * if the {@code i < j} then {@code hash(i) < hash(j)}</li>
 * <li>Second, the elements to be sorted must be uniformly distributed</li>
 * </ul>
 * Bucket Sort is <b>not</b> stable and <b>not</b> in-place algorithm.
 * {@inheritDoc}
 * Created by @ssz on 20.09.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Bucket_sort'>wiki: Bucket Sort</a>
 * @see <a href='https://javarevisited.blogspot.com/2017/01/bucket-sort-in-java-with-example.html'>Bucket Sort</a>
 */
public class BucketSortAlgorithm extends BaseIntegerSortAlgorithm {

    public static void sortInts(int[] array) {
        // get hash codes
        int[] codes = hash(array);

        // create and initialize buckets to ArrayList: O(n)
        @SuppressWarnings("unchecked") List<Integer>[] buckets = new List[codes[1]];
        for (int i = 0; i < codes[1]; i++) {
            buckets[i] = new ArrayList<>();
        }

        // distribute data into buckets: O(n)
        for (int i : array) {
            buckets[hash(i, codes)].add(i);
        }

        // sort each bucket O(n)
        for (List<Integer> bucket : buckets) {
            Collections.sort(bucket);
        }

        int index = 0;
        // merge the buckets: O(n)
        for (List<Integer> bucket : buckets) {
            for (int v : bucket) {
                array[index++] = v;
            }
        }
    }

    private static int[] hash(int[] input) {
        int m = input[0];
        for (int i = 1; i < input.length; i++) {
            if (m < input[i]) {
                m = input[i];
            }
        }
        // an array containing hash of input
        return new int[]{m, (int) Math.sqrt(input.length)};
    }

    private static int hash(int i, int[] codes) {
        return (int) ((double) i / codes[0] * (codes[1] - 1));
    }

    @Override
    public void sort(int[] array) {
        sortInts(array);
    }
}
