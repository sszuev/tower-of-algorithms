package com.gitlab.sszuev.graphs.impl;

import com.gitlab.sszuev.graphs.DirectedGraph;
import com.gitlab.sszuev.graphs.ModifiableGraph;

/**
 * Created by @ssz on 09.10.2021.
 */
public class DirectedGraphImpl<X> extends BaseGraphImpl<X> implements DirectedGraph<X>, ModifiableGraph<X> {

    @Override
    public Edge<X> add(X left, X right) {
        return createEdge(left, right);
    }

    protected DirectEdgeImpl<X> createEdge(X left, X right) {
        VertexImpl<X> a = fetchVertex(left);
        VertexImpl<X> b = fetchVertex(right);
        DirectEdgeImpl<X> res = newEdge(a, b);
        a.addEdge(res);
        return res;
    }

    @Override
    protected VertexImpl<X> newVertex(X k) {
        return new VertexImpl<>(k);
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
        ModifiableGraph.super.addNode(left, right, other);
        return this;
    }
}
