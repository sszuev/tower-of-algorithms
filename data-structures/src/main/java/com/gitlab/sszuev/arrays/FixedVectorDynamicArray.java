package com.gitlab.sszuev.arrays;

/**
 * A simple {@link DynamicArray} implementation,
 * if there is no free space in the internal array, it grows by a fixed length (i.e. {@link #vector}) .
 *
 * @param <E> - any object
 */
public class FixedVectorDynamicArray<E> extends BaseVectorDynamicArray<E> implements DynamicArray<E> {

    private final int vector;

    public FixedVectorDynamicArray() {
        this(10);
    }

    public FixedVectorDynamicArray(int vector) {
        this(new Object[0], checkVector(vector));
    }

    protected FixedVectorDynamicArray(Object[] array, int vector) {
        super(array, array.length);
        this.vector = vector;
    }

    /**
     * Creates an array instance with the given data.
     *
     * @param vector {@code int} a factor to increase array when no more free space
     * @param array  {@code Array} with data
     * @param <X>    any object
     * @return {@link FixedVectorDynamicArray} of {@link X}
     */
    @SafeVarargs
    public static <X> FixedVectorDynamicArray<X> of(int vector, X... array) {
        return new FixedVectorDynamicArray<>(ArrayUtils.copy(array), checkVector(vector));
    }

    private static int checkVector(int vector) {
        if (vector <= 0) throw new IllegalArgumentException();
        return vector;
    }

    @Override
    protected int vector() {
        return vector;
    }
}
