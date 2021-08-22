package com.gitlab.sszuev.arrays;

import java.util.Objects;

/**
 * A simple straightforward {@link DynamicArray} implementation,
 * the internal array grows by one item each time the operation {@link #add(Object)} is performed.
 *
 * @param <E> - any object
 */
public class SimpleDynamicArray<E> implements DynamicArray<E> {

    private Object[] array;

    public SimpleDynamicArray() {
        this(new Object[0]);
    }

    protected SimpleDynamicArray(Object[] array) {
        this.array = Objects.requireNonNull(array);
    }

    /**
     * Creates an array instance with the given data.
     *
     * @param array {@code Array} with data
     * @param <X>   any object
     * @return {@link SimpleDynamicArray} of {@link X}
     */
    @SafeVarargs
    public static <X> SimpleDynamicArray<X> of(X... array) {
        return new SimpleDynamicArray<>(ArrayUtils.copy(array));
    }

    @Override
    public void add(E item) {
        this.array = ArrayUtils.grow(array, 1);
        this.array[this.array.length - 1] = item;
    }

    @Override
    public E remove(int index) {
        @SuppressWarnings("unchecked") E res = (E) array[index];
        Object[] newArray = new Object[array.length - 1];
        System.arraycopy(array, 0, newArray, 0, index);
        System.arraycopy(array, index + 1, newArray, index, array.length - index - 1);
        array = newArray;
        return res;
    }

    @SuppressWarnings("unchecked")
    @Override
    public E get(int index) {
        return (E) array[index];
    }

    @Override
    public int size() {
        return array.length;
    }

}
