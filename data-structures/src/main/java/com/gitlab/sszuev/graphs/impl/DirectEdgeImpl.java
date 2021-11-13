package com.gitlab.sszuev.graphs.impl;

import java.util.Objects;

/**
 * Created by @ssz on 17.10.2021.
 */
public class DirectEdgeImpl<X> extends BaseEdgeImpl<X> {
    public DirectEdgeImpl(VertexImpl<X> left, VertexImpl<X> right) {
        super(left, right);
    }

    @Override
    protected boolean matchEdge(BaseEdgeImpl<?> other) {
        return Objects.equals(this.left, other.left) && Objects.equals(this.right, other.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.left, this.right);
    }

    @Override
    public String toString() {
        return String.format("%s => %s", left.payload(), right.payload());
    }
}
