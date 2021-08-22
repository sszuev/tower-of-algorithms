package com.gitlab.sszuev.arrays;

import java.util.Objects;

/**
 * A simple {@link DynamicArray} implementation,
 * if there is no free space in the internal array, it grows by a fixed length (i.e. {@link #vector}) .
 *
 * @param <E> - any object
 */
public class VectorDynamicArray<E> implements DynamicArray<E> {

    private final int vector;
    private Object[] array;
    private int size;

    public VectorDynamicArray() {
        this(10);
    }

    public VectorDynamicArray(int vector) {
        this(new Object[0], vector);
    }

    protected VectorDynamicArray(Object[] array, int vector) {
        if (vector <= 0) throw new IllegalArgumentException();
        this.vector = vector;
        this.array = Objects.requireNonNull(array);
        this.size = array.length;
    }

    /**
     * Creates an array instance with the given data.
     *
     * @param vector {@code int} a factor to increase array when no more free space
     * @param array  {@code Array} with data
     * @param <X>    any object
     * @return {@link VectorDynamicArray} of {@link X}
     */
    @SafeVarargs
    public static <X> VectorDynamicArray<X> of(int vector, X... array) {
        return new VectorDynamicArray<>(ArrayUtils.copy(array), vector);
    }

    @Override
    public void add(E item) {
        if (size == array.length) {
            this.array = ArrayUtils.grow(array, vector);
        }
        array[size] = item;
        size++;
    }

    @Override
    public E remove(int index) {
        @SuppressWarnings("unchecked") E res = (E) array[checkIndex(index)];
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
