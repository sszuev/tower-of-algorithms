package com.gitlab.sszuev.graphs.impl;

import com.gitlab.sszuev.graphs.ModifiableGraph;

import java.util.stream.Stream;

/**
 * Created by @ssz on 09.10.2021.
 */
public class UndirectedGraphImpl<X> extends BaseGraphImpl<X> implements ModifiableGraph<X> {

    @Override
    public Edge<X> add(X left, X right) {
        return createEdge(left, right);
    }

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

    @SafeVarargs
    @Override
    public final UndirectedGraphImpl<X> addNode(X left, X right, X... other) {
        ModifiableGraph.super.addNode(left, right, other);
        return this;
    }

    @Override
    public Stream<Edge<X>> edges() {
        return super.edges().distinct();
    }
}
