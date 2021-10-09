package com.gitlab.sszuev.graphs;

import java.util.Objects;

/**
 * Created by @ssz on 09.10.2021.
 */
public class DirectedGraph<X> extends BaseGraph<X> {

    @Override
    protected DirectEdgeImpl<X> createEdge(X left, X right) {
        VertexImpl<X> a = fetchVertex(left);
        VertexImpl<X> b = fetchVertex(right);
        DirectEdgeImpl<X> res = newEdge(a, b);
        a.addEdge(res);
        return res;
    }

    @Override
    protected DirectEdgeImpl<X> newEdge(VertexImpl<X> left, VertexImpl<X> right) {
        return new DirectEdgeImpl<>(left, right);
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
    }
}
