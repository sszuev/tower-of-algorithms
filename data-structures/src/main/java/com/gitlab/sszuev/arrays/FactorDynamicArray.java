package com.gitlab.sszuev.arrays;

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

    public FactorDynamicArray(int factor, int initLength) {
        this.factor = factor;
        this.array = new Object[initLength];
        this.size = 0;
    }

    public FactorDynamicArray() {
        this(50, 10);
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
