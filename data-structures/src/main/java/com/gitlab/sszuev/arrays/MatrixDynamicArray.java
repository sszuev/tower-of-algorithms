package com.gitlab.sszuev.arrays;

/**
 * A {@link DynamicArray} implementation that is based on array of arrays.
 * If there is no free space when operation {@link #add(Object)} is performed, then a new array is added.
 *
 * @param <E> - any object
 */
public class MatrixDynamicArray<E> implements DynamicArray<E> {

    private final int vector;
    private final DynamicArray<DynamicArray<E>> array;
    private int size;

    public MatrixDynamicArray() {
        this(10);
    }

    public MatrixDynamicArray(int vector) {
        this.vector = vector;
        this.array = new SimpleDynamicArray<>();
        this.size = 0;
    }

    @Override
    public void add(E item) {
        if (size == array.size() * vector)
            array.add(new VectorDynamicArray<>(vector));
        array.get(size / vector).add(item);
        size++;
    }

    @Override
    public E get(int index) {
        return array.get(index / vector).get(index % vector);
    }

    @Override
    public int size() {
        return size;
    }
}
