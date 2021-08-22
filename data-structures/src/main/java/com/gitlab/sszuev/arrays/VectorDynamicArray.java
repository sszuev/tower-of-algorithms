package com.gitlab.sszuev.arrays;

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
        this.vector = vector;
        this.array = new Object[0];
        this.size = 0;
    }

    @Override
    public void add(E item) {
        if (size == array.length) {
            resize();
        }
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
        Object[] newArray = new Object[array.length + vector];
        System.arraycopy(array, 0, newArray, 0, array.length);
        this.array = newArray;
    }
}
