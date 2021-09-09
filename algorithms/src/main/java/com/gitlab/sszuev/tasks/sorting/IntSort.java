package com.gitlab.sszuev.tasks.sorting;

/**
 * A generic interface that describes a sorting algorithm applicable for {@code int}-arrays.
 * <p>
 * Created by @ssz on 05.09.2021.
 */
public interface IntSort {
    /**
     * Sorts the specified array of {@code int}-primitives.
     *
     * @param array an {@code Array} of {@code int}s
     */
    void sort(int[] array);
}
