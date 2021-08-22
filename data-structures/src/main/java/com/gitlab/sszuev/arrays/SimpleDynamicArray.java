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
        resize();
        array[size() - 1] = item;
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

    private void resize() {
        Object[] newArray = new Object[array.length + 1];
        System.arraycopy(array, 0, newArray, 0, size());
        array = newArray;
    }
}
