package com.gitlab.sszuev.arrays;

/**
 * A simple straightforward {@link DynamicArray} implementation,
 * the internal array grows by one item each time the operation {@link #add(Object)} is performed.
 *
 * @param <E> - any object
 */
public class SimpleDynamicArray<E> implements DynamicArray<E> {

    private Object[] array;

    public SimpleDynamicArray() {
        this.array = new Object[0];
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
