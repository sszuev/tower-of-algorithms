package com.gitlab.sszuev.graphs.impl;

import com.gitlab.sszuev.graphs.ModifiableWeightedGraph;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

/**
 * Created by @ssz on 17.10.2021.
 */
public class UndirectedWeightedGraphImpl<X> extends BaseGraphImpl<X> implements ModifiableWeightedGraph<X> {
    protected final Map<Edge<X>, Double> edges = new HashMap<>();

    @Override
    public double weight(Edge<X> edge) {
        Double res = edges.get(edge);
        if (res == null) {
            throw new NoSuchElementException("Edge " + edge + " does not belong to the graph");
        }
        return res;
    }

    @Override
    public Edge<X> add(X left, X right, double weight) {
        return createEdge(left, right, weight);
    }

    @Override
    public UndirectedWeightedGraphImpl<X> addEdge(X left, X right, double weight) {
        ModifiableWeightedGraph.super.addEdge(left, right, weight);
        return this;
    }

    @Override
    protected BaseEdgeImpl<X> newEdge(VertexImpl<X> left, VertexImpl<X> right) {
        return new UndirectedEdgeImpl<>(left, right);
    }

    protected BaseEdgeImpl<X> createEdge(X left, X right, double weight) {
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
