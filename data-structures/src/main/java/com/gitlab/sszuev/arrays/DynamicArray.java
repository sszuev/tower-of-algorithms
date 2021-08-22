package com.gitlab.sszuev.arrays;

/**
 * An array that can grow on demand.
 *
 * @param <E> the type of elements in this array.
 */
public interface DynamicArray<E> {

    /**
     * Appends the specified element to the end of this array.
     *
     * @param item {@link E} element to be appended to this list
     */
    void add(E item);

    /**
     * Returns the element at the specified position in this array.
     *
     * @param index non-negative {@code int}, index of the element to return
     * @return {@link E}, the element at the specified position in this array
     */
    E get(int index);

    /**
     * Returns the number of elements in this array.
     *
     * @return {@code int}, the number of elements
     */
    int size();
}
