package com.gitlab.sszuev.graphs;

/**
 * Created by @ssz on 09.10.2021.
 */
public class UndirectedGraphImpl<X> extends BaseGraphImpl<X> {

    @Override
    protected UndirectedEdgeImpl<X> createEdge(X left, X right) {
        VertexImpl<X> a = fetchVertex(left);
        VertexImpl<X> b = fetchVertex(right);
        UndirectedEdgeImpl<X> res = newEdge(a, b);
        a.addEdge(res);
        b.addEdge(newEdge(b, a));
        return res;
    }

    @Override
    protected UndirectedEdgeImpl<X> newEdge(VertexImpl<X> left, VertexImpl<X> right) {
        return new UndirectedEdgeImpl<>(left, right);
    }

    protected static class UndirectedEdgeImpl<X> extends EdgeImpl<X> {

        public UndirectedEdgeImpl(VertexImpl<X> left, VertexImpl<X> right) {
            super(left, right);
        }

        @Override
        protected boolean matchEdge(EdgeImpl<?> other) {
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
    }
}
