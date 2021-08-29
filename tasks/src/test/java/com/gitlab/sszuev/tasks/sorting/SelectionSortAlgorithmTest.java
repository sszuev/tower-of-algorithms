package com.gitlab.sszuev.tasks.sorting;

import com.gitlab.sszuev.tasks.Algorithm;

/**
 * <b>Selection Sort (for Integers).</b>
 * It is is <b>non-adaptive</b>, <b>non-stable</b>, <b>in-place</b>, <b>non-online</b> sorting.
 * <ul>
 * <li>Worst case performance, {@code O(n^2)} comparisons and {@code O(n)} swaps</li>
 * <li>Best case performance, {@code O(n^2)} comparisons and {@code O(1)} swaps</li>
 * <li>Average case performance, {@code O(n^2)} comparisons and {@code O(n)} swaps</li>
 * <li>Worst-case space complexity: {@code O(1)} auxiliary</li>
 * </ul>
 * Created by @ssz on 29.08.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Selection_sort'>wiki: Selection sort</a>
 * @see <a href='https://www.cs.usfca.edu/~galles/visualization/ComparisonSort.html'>Sorting Algorithms Visualization</a>
 * Created by @ssz on 29.08.2021.
 */
public class SelectionSortAlgorithmTest extends SortAlgorithmTestBase {
    @Override
    public Algorithm getTaskToTest() {
        return new SelectionSortAlgorithm();
    }
}
