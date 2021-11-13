package com.gitlab.sszuev.graphs.impl;

import com.gitlab.sszuev.graphs.Graph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Created by @ssz on 17.10.2021.
 */
public class VertexImpl<X> implements Graph.Vertex<X> {
    protected final X key;
    protected final Collection<BaseEdgeImpl<X>> edges = new HashSet<>();

    public VertexImpl(X key) {
        this.key = Objects.requireNonNull(key);
    }

    @Override
    public X payload() {
        return key;
    }

    public Stream<BaseEdgeImpl<X>> listEdges() {
        return edges.stream();
    }

    @Override
    public final Stream<Graph.Edge<X>> edges() {
        return listEdges().map(x -> x);
    }

    protected void addEdge(BaseEdgeImpl<X> edge) {
        edges.add(edge);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VertexImpl<?> vertex = (VertexImpl<?>) o;
        return Objects.equals(key, vertex.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        return String.format("VertexImpl{key=%s, edges=%s}", key, edges.size());
    }
}
