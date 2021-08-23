package com.gitlab.sszuev.arrays;

/**
 * A {@link DynamicArray} implementation,
 * if there is no free space in the internal array, it grows by a some length calculated dynamically.
 *
 * @param <E> any object
 */
public class FactorVectorDynamicArray<E> extends BaseVectorDynamicArray<E> implements DynamicArray<E> {

    private final int factor;

    public FactorVectorDynamicArray() {
        this(50, 10);
    }

    public FactorVectorDynamicArray(int factor, int initLength) {
        this(new Object[initLength], checkFactor(factor), 0);
    }

    protected FactorVectorDynamicArray(Object[] array, int factor, int size) {
        super(array, size);
        this.factor = factor;
    }

    /**
     * Creates an array instance with the given data.
     *
     * @param factor {@code int} a factor to control array growing
     * @param array  {@code Array} with data
     * @param <X>    any object
     * @return {@link FactorVectorDynamicArray} of {@link X}
     */
    @SafeVarargs
    public static <X> FactorVectorDynamicArray<X> of(int factor, X... array) {
        Object[] data = ArrayUtils.copy(array);
        return new FactorVectorDynamicArray<>(data, checkFactor(factor), data.length);
    }

    private static int checkFactor(int factor) {
        if (factor <= 0) throw new IllegalArgumentException();
        return factor;
    }

    @Override
    protected int vector() {
        return Math.max(this.array.length * factor / 100, 1);
    }
}
