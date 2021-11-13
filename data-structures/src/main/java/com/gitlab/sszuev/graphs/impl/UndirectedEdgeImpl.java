package com.gitlab.sszuev.graphs.impl;

/**
 * Created by @ssz on 17.10.2021.
 */
public class UndirectedEdgeImpl<X> extends BaseEdgeImpl<X> {

    public UndirectedEdgeImpl(VertexImpl<X> left, VertexImpl<X> right) {
        super(left, right);
    }

    @Override
    protected boolean matchEdge(BaseEdgeImpl<?> other) {
        return matchVertex(other.left) && matchVertex(other.right);
    }

    protected boolean matchVertex(VertexImpl<?> v) {
        if (v == null) return false;
        return left.equals(v) || right.equals(v);
    }

    @Override
    public int hashCode() {
        return left.hashCode() + right.hashCode();
    }

    @Override
    public String toString() {
        return String.format("%s <=> %s", left.payload(), right.payload());
    }
}
