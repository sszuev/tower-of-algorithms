package com.gitlab.sszuev.tasks.sorting;

/**
 * A generic interface that describes a sorting algorithm applicable for {@code char}-arrays.
 * <p>
 * Created by @ssz on 19.09.2021.
 */
public interface CharSort {

    /**
     * Sorts the specified array of {@code char}-primitives.
     *
     * @param array an {@code Array} of {@code char}s
     */
    void sort(char[] array);
}
