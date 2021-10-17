package com.gitlab.sszuev.graphs.impl;

import com.gitlab.sszuev.graphs.ModifiableWeightedGraph;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by @ssz on 17.10.2021.
 */
public class UndirectedWeightedGraphImpl<X> extends BaseGraphImpl<X> implements ModifiableWeightedGraph<X> {
    protected final Map<Edge<X>, Long> edges = new HashMap<>();

    @Override
    public long weight(Edge<X> edge) {
        Long res = edges.get(edge);
        if (res == null) {
            throw new IllegalArgumentException("Can't find edge");
        }
        return res;
    }

    @Override
    public Edge<X> add(X left, X right, long weight) {
        return createEdge(left, right, weight);
    }

    @Override
    public UndirectedWeightedGraphImpl<X> addEdge(X left, X right, long weight) {
        ModifiableWeightedGraph.super.addEdge(left, right, weight);
        return this;
    }

    @Override
    protected BaseEdgeImpl<X> newEdge(VertexImpl<X> left, VertexImpl<X> right) {
        return new UndirectedEdgeImpl<>(left, right);
    }

    protected BaseEdgeImpl<X> createEdge(X left, X right, long weight) {
        VertexImpl<X> a = fetchVertex(left);
        VertexImpl<X> b = fetchVertex(right);
        BaseEdgeImpl<X> res = newEdge(a, b);
        edges.put(res, weight);
        a.addEdge(res);
        b.addEdge(newEdge(b, a));
        return res;
    }

    @Override
    public Stream<Edge<X>> edges() {
        return edges.keySet().stream();
    }

}
