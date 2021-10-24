package com.gitlab.sszuev.graphs.impl;

import com.gitlab.sszuev.graphs.Graph;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by @ssz on 09.10.2021.
 */
abstract class BaseGraphImpl<X> implements Graph<X> {
    protected final Map<X, VertexImpl<X>> vertexes = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    private static <X> Comparable<X> asComparable(X value) {
        return (Comparable<X>) value;
    }

    @Override
    public Stream<Vertex<X>> vertexes() {
        return vertexes.values().stream().map(x -> x);
    }

    @Override
    public long size() {
        return vertexes.size();
    }

    @Override
    public Optional<Vertex<X>> vertex(X key) {
        return Optional.ofNullable(vertexes.get(Objects.requireNonNull(key)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Vertex<X>> getVector() {
        List<?> raw = toList();
        return (List<Vertex<X>>) raw;
    }

    public List<VertexImpl<X>> toList() {
        Collection<VertexImpl<X>> res = vertexes.values();
        if (res.isEmpty()) {
            return Collections.emptyList();
        }
        X first = res.iterator().next().payload();
        Stream<VertexImpl<X>> stream = res.stream();
        if (first instanceof Comparable) {
            Comparator<VertexImpl<X>> comp = (o1, o2) -> asComparable(o1.payload()).compareTo(o2.payload());
            stream = stream.sorted(comp);
        }
        return stream.collect(Collectors.toUnmodifiableList());
    }

    protected VertexImpl<X> fetchVertex(X key) {
        return vertexes.computeIfAbsent(key, this::newVertex);
    }

    protected VertexImpl<X> newVertex(X k) {
        return new VertexImpl<>(k);
    }

    protected abstract BaseEdgeImpl<X> newEdge(VertexImpl<X> left, VertexImpl<X> right);

}
