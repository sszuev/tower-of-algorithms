package com.gitlab.sszuev.graphs.impl;

import com.gitlab.sszuev.graphs.DirectedGraph;

import java.util.Objects;

/**
 * Created by @ssz on 09.10.2021.
 */
public class DirectedGraphImpl<X> extends BaseGraphImpl<X> implements DirectedGraph<X> {

    @Override
    protected DirectEdgeImpl<X> createEdge(X left, X right) {
        VertexImpl<X> a = fetchVertex(left);
        VertexImpl<X> b = fetchVertex(right);
        DirectEdgeImpl<X> res = newEdge(a, b);
        a.addEdge(res);
        return res;
    }

    @Override
    protected DirectVertexImpl<X> newVertex(X k) {
        return new DirectVertexImpl<>(k);
    }

    @Override
    protected DirectEdgeImpl<X> newEdge(VertexImpl<X> left, VertexImpl<X> right) {
        return new DirectEdgeImpl<>(left, right);
    }

    @Override
    public DirectedGraph<X> invert() {
        DirectedGraphImpl<X> res = new DirectedGraphImpl<>();
        edges().forEach(e -> res.add(e.right().payload(), e.left().payload()));
        return res;
    }

    @SafeVarargs
    @Override
    public final DirectedGraphImpl<X> addNode(X left, X right, X... other) {
        return (DirectedGraphImpl<X>) super.addNode(left, right, other);
    }

    protected static class DirectVertexImpl<X> extends VertexImpl<X> {

        public DirectVertexImpl(X key) {
            super(key);
        }
    }

    protected static class DirectEdgeImpl<X> extends EdgeImpl<X> {
        public DirectEdgeImpl(VertexImpl<X> left, VertexImpl<X> right) {
            super(left, right);
        }

        @Override
        protected boolean matchEdge(EdgeImpl<?> other) {
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
}
