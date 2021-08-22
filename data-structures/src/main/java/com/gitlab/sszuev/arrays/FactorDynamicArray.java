package com.gitlab.sszuev.arrays;

import java.util.Objects;

/**
 * A {@link DynamicArray} implementation,
 * if there is no free space in the internal array, it grows by a some length calculated dynamically.
 *
 * @param <E> any object
 */
public class FactorDynamicArray<E> implements DynamicArray<E> {

    private final int factor;
    private Object[] array;
    private int size;

    public FactorDynamicArray() {
        this(50, 10);
    }

    public FactorDynamicArray(int factor, int initLength) {
        this(new Object[initLength], factor, 0);
    }

    protected FactorDynamicArray(Object[] array, int factor, int size) {
        if (factor <= 0) throw new IllegalArgumentException();
        this.factor = factor;
        this.array = Objects.requireNonNull(array);
        this.size = size;
    }

    /**
     * Creates an array instance with the given data.
     *
     * @param factor {@code int} a factor to control array growing
     * @param array  {@code Array} with data
     * @param <X>    any object
     * @return {@link FactorDynamicArray} of {@link X}
     */
    @SafeVarargs
    public static <X> FactorDynamicArray<X> of(int factor, X... array) {
        Object[] data = ArrayUtils.copy(array);
        return new FactorDynamicArray<>(data, factor, data.length);
    }

    @Override
    public void add(E item) {
        if (size == array.length)
            resize();
        array[size] = item;
        size++;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        return (E) array[index];
    }

    @Override
    public int size() {
        return size;
    }

    private void resize() {
        Object[] newArray = new Object[array.length + array.length * factor / 100];
        System.arraycopy(array, 0, newArray, 0, array.length);
        array = newArray;
    }
}
