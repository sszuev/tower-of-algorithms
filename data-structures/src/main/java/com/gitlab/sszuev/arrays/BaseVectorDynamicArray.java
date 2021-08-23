package com.gitlab.sszuev.arrays;

import java.util.Objects;

/**
 * Created by @ssz on 23.08.2021.
 */
abstract class BaseVectorDynamicArray<E> implements DynamicArray<E> {
    protected Object[] array;
    protected int size;

    protected BaseVectorDynamicArray(Object[] array, int size) {
        this.array = Objects.requireNonNull(array);
        this.size = size;
    }

    /**
     * Answers a parameter that determines how to increase or decrease
     * the internal array when performing {@code add}/{@code remove} operations.
     *
     * @return {@code int}
     */
    protected abstract int vector();

    @Override
    public void add(E item) {
        if (size == array.length) {
            this.array = ArrayUtils.grow(array, vector());
        }
        array[size] = item;
        size++;
    }

    @Override
    public void add(int index, E item) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("index = " + index + ", size = " + size);
        }
        Object[] res;
        if (size == array.length) {
            res = new Object[array.length + vector()];
            System.arraycopy(array, 0, res, 0, index);
        } else {
            res = array;
        }
        System.arraycopy(array, index, res, index + 1, size - index);
        res[index] = item;
        array = res;
        size++;
    }

    @Override
    public E remove(int index) {
        @SuppressWarnings("unchecked") E res = (E) array[checkIndex(index)];
        int vector = vector();
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

    protected int checkIndex(int index) {
        return ArrayUtils.checkIndex(index, size);
    }
}
