package com.gitlab.sszuev.graphs.impl;

import com.gitlab.sszuev.graphs.Graph;

import java.util.Objects;

/**
 * Created by @ssz on 17.10.2021.
 */
public abstract class BaseEdgeImpl<X> implements Graph.Edge<X> {
    protected final VertexImpl<X> left, right;

    public BaseEdgeImpl(VertexImpl<X> left, VertexImpl<X> right) {
        this.left = Objects.requireNonNull(left);
        this.right = Objects.requireNonNull(right);
    }

    @Override
    public VertexImpl<X> left() {
        return left;
    }

    @Override
    public VertexImpl<X> right() {
        return right;
    }

    @Override
    public final boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        return matchEdge((BaseEdgeImpl<?>) other);
    }

    protected abstract boolean matchEdge(BaseEdgeImpl<?> other);

    @Override
    public abstract int hashCode();

}
