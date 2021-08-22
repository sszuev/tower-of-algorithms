package com.gitlab.sszuev.arrays;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple wrapper for {@link List JDK List}.
 * <p>
 * Created by @ssz on 22.08.2021.
 *
 * @param <E> the type of elements in this array
 */
public class JDKListDynamicArray<E> implements DynamicArray<E> {

    private final List<E> inner;

    public JDKListDynamicArray() {
        this(new ArrayList<>());
    }

    public JDKListDynamicArray(List<E> inner) {
        this.inner = Objects.requireNonNull(inner);
    }

    @Override
    public void add(E item) {
        inner.add(item);
    }

    @Override
    public E remove(int index) {
        return inner.remove(index);
    }

    @Override
    public E get(int index) {
        return inner.get(index);
    }

    @Override
    public int size() {
        return inner.size();
    }
}
