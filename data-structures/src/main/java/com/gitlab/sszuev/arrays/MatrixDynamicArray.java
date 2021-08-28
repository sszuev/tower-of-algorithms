package com.gitlab.sszuev.arrays;

/**
 * A {@link DynamicArray} implementation that is based on array of arrays.
 * If there is no free space when operation {@link #add(Object)} is performed, then a new array is added.
 *
 * @param <E> - any object
 * @see FixedVectorDynamicArray
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
            chunks[i] = new FixedVectorDynamicArray<>(data[i], vector);
        }
        return new MatrixDynamicArray<>(new SimpleDynamicArray<>(chunks), vector, array.length);
    }

    @Override
    public void add(E item) {
        if (size == array.size() * vector) {
            array.add(new FixedVectorDynamicArray<>(vector));
        }
        array.get(size / vector).add(item);
        size++;
    }

    @Override
    public void add(int index, E item) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("index = " + index + ", size = " + size);
        }
        if (array.isEmpty()) {
            array.add(new FixedVectorDynamicArray<>(vector));
        }
        if (index == 0) {
            array.get(0).add(0, item);
        } else {
            Position<E> pos = findPosition(index);
            pos.chunk.add(pos.innerIndex, item);
        }
        size++;
    }

    @Override
    public E remove(int index) {
        Position<E> pos = findPosition(checkIndex(index));
        E res = pos.chunk.remove(pos.innerIndex);
        if (pos.chunk.isEmpty()) {
            array.remove(pos.chunkIndex);
        }
        size--;
        return res;
    }

    @Override
    public E get(int index) {
        Position<E> pos = findPosition(checkIndex(index));
        return pos.chunk.get(pos.innerIndex);
    }

    private Position<E> findPosition(int index) {
        return 2 * index > this.size ? findFromEnd(index) : findFromBeginning(index);
    }

    private Position<E> findFromBeginning(int index) {
        int count = 0;
        for (int i = 0; i < array.size(); i++) {
            DynamicArray<E> chunk = array.get(i);
            int chunkSize = chunk.size();
            count += chunkSize;
            if (index < count) {
                return new Position<>(chunk, i, chunkSize - count + index);
            }
        }
        throw new IllegalStateException();
    }

    private Position<E> findFromEnd(int index) {
        int count = size;
        for (int i = array.size() - 1; i >= 0; i--) {
            DynamicArray<E> chunk = array.get(i);
            int chunkSize = chunk.size();
            count -= chunkSize;
            if (index >= count) {
                return new Position<>(chunk, i, index - count);
            }
        }
        throw new IllegalStateException();
    }

    @Override
    public int size() {
        return size;
    }

    private int checkIndex(int index) {
        return ArrayUtils.checkIndex(index, size);
    }

    private static class Position<X> {
        private final DynamicArray<X> chunk;
        private final int chunkIndex;
        private final int innerIndex;

        private Position(DynamicArray<X> chunk, int chunkIndex, int innerIndex) {
            this.chunk = chunk;
            this.chunkIndex = chunkIndex;
            this.innerIndex = innerIndex;
        }
    }
}
