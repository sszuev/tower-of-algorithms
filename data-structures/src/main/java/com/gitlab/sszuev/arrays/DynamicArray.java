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
     * @param item {@link E} element to be appended to this array
     */
    void add(E item);

    /**
     * Inserts the specified element at the specified position in this array.
     * Shifts the element currently at that position (if any) and
     * any subsequent elements to the right (adds one to their indices).
     *
     * @param index {@code int} index at which the specified element is to be inserted
     * @param item  {@link E}  element to be inserted
     */
    void add(int index, E item);

    /**
     * Removes the element at the specified position in this array.
     * Returns the element that was removed from the array.
     *
     * @param index the index of the element to be removed
     * @return {@link E} the element previously at the specified position
     */
    E remove(int index);

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

    /**
     * Answers {@code true} if this array contains no elements.
     *
     * @return {@code true} if it is empty array
     */
    default boolean isEmpty() {
        return size() == 0;
    }
}
