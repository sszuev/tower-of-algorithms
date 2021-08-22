package com.gitlab.sszuev.arrays;

/**
 * A {@link DynamicArray} implementation that is based on array of arrays.
 * If there is no free space when operation {@link #add(Object)} is performed, then a new array is added.
 *
 * @param <E> - any object
 * @see VectorDynamicArray
 * @see SimpleDynamicArray
 */
public class MatrixDynamicArray<E> implements DynamicArray<E> {

    private final int vector;
    private final DynamicArray<DynamicArray<E>> array;
    private int size;

    public MatrixDynamicArray() {
        this(10);
    }

    public MatrixDynamicArray(int vector) {
        this(new SimpleDynamicArray<>(), vector, 0);
    }

    protected MatrixDynamicArray(DynamicArray<DynamicArray<E>> array, int vector, int size) {
        if (vector <= 0) throw new IllegalArgumentException();
        this.vector = vector;
        this.array = array;
        this.size = size;
    }

    /**
     * Creates an array instance with the given data.
     *
     * @param vector {@code int} a vector for chunk arrays
     * @param array  {@code Array} with data
     * @param <X>    any object
     * @return {@link MatrixDynamicArray} of {@link X}
     */
    @SafeVarargs
    public static <X> MatrixDynamicArray<X> of(int vector, X... array) {
        Object[][] data = ArrayUtils.split(array, vector);
        Object[] chunks = new Object[data.length];
        for (int i = 0; i < chunks.length; i++) {
            chunks[i] = new VectorDynamicArray<>(data[i], vector);
        }
        return new MatrixDynamicArray<>(new SimpleDynamicArray<>(chunks), vector, array.length);
    }

    @Override
    public void add(E item) {
        if (size == array.size() * vector) {
            array.add(new VectorDynamicArray<>(vector));
        }
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

    public static void main(String... args) {
    }
}
