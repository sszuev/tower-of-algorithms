package com.gitlab.sszuev.tasks.sorting;

/**
 * <b>Shell Sort (for Integers).</b>
 * It is <b>adaptive</b>(somehow), <b>non-stable</b>, <b>non-online</b>, <b>in-place</b> sorting.
 * <ul>
 * <li>Average case performance: {@code O(n^(4/3))} (approximate)</li>
 * <li>Average case memory: {@code O(1)}</li>
 * <li>Best case performance: {@code O(n*log(n))} (most gap sequences),{@code O(n * (log(n))^2)} (best known worst-case gap sequence</li>
 * <li>Worst case performance: {@code O(n^2)} (worst known worst case gap sequence) {@code O(n*(log(n))^2)} (best known worst case gap sequence)</li>
 * <li>Worst-case memory: {@code O(n)}</li>
 * </ul>
 * {@inheritDoc}
 * Created by @ssz on 29.08.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Shellsort'>wiki: Shell sort</a>
 * @see <a href='https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html'>Visualization</a>
 */
public class ShellSortAlgorithm extends BaseIntegerSortAlgorithm {

    private final int factor;

    public ShellSortAlgorithm(int factor) {
        if (factor <= 1) {
            throw new IllegalArgumentException();
        }
        this.factor = factor;
    }

    public static void sortInts(int factor, int[] array) {
        for (int gap = Math.max(1, array.length / factor); gap > 0; gap /= 2) {
            for (int i = gap; i < array.length; i++) {
                for (int j = i - gap; j >= 0 && array[j] > array[j + gap]; j -= gap) {
                    if (array[j] > array[j + gap]) {
                        swap(array, j, j + gap);
                    }
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
    public void sort(int[] array) {
        sortInts(factor, array);
    }

    @Override
    public String name() {
        return String.format("%s-factor=%d", getClass().getSimpleName(), factor);
    }
}
