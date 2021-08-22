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
        if (size == array.length) {
            this.array = ArrayUtils.grow(array, this.array.length * factor / 100);
        }
        array[size] = item;
        size++;
    }

    @Override
    public E remove(int index) {
        @SuppressWarnings("unchecked") E res = (E) array[checkIndex(index)];
        int vector = this.array.length * factor / 100;
        ArrayUtils.remove(array, index);
        size--;
        if (this.array.length - size >= vector) {
            this.array = ArrayUtils.truncate(array, size);
        }
        return res;
    }

    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        return (E) array[checkIndex(index)];
    }

    @Override
    public int size() {
        return size;
    }

    private int checkIndex(int index) {
        return ArrayUtils.checkIndex(index, size);
    }
}
