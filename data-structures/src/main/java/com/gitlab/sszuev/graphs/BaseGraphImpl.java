package com.gitlab.sszuev.graphs;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by @ssz on 09.10.2021.
 */
abstract class BaseGraphImpl<X> implements ModifiableGraph<X> {
    protected final Map<X, VertexImpl<X>> vertexes = new LinkedHashMap<>();

    @Override
    public Edge<X> add(X left, X right) {
        return createEdge(left, right);
    }

    @Override
    public Stream<Vertex<X>> vertexes() {
        return vertexes.values().stream().map(x -> x);
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

    @SuppressWarnings("unchecked")
    private static <X> Comparable<X> asComparable(X value) {
        return (Comparable<X>) value;
    }

    protected VertexImpl<X> fetchVertex(X key) {
        return vertexes.computeIfAbsent(key, this::newVertex);
    }

    protected abstract EdgeImpl<X> createEdge(X left, X right);

    protected VertexImpl<X> newVertex(X k) {
        return new VertexImpl<>(k);
    }

    protected abstract EdgeImpl<X> newEdge(VertexImpl<X> left, VertexImpl<X> right);

    protected static class VertexImpl<X> implements Vertex<X> {
        protected final X key;
        protected final Collection<EdgeImpl<X>> edges = new HashSet<>();

        public VertexImpl(X key) {
            this.key = Objects.requireNonNull(key);
        }

        @Override
        public X payload() {
            return key;
        }

        public Stream<EdgeImpl<X>> listEdges() {
            return edges.stream();
        }

        @Override
        public final Stream<Edge<X>> edges() {
            return listEdges().map(x -> x);
        }

        protected void addEdge(EdgeImpl<X> edge) {
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

    protected abstract static class EdgeImpl<X> implements Edge<X> {
        protected final VertexImpl<X> left, right;

        public EdgeImpl(VertexImpl<X> left, VertexImpl<X> right) {
            this.left = Objects.requireNonNull(left);
            this.right = Objects.requireNonNull(right);
        }

        @Override
        public VertexImpl<X> left() {
            return left;
        }

        @Override
        public VertexImpl<X> right() {
            return right;
        }

        @Override
        public final boolean equals(Object other) {
            if (this == other) return true;
            if (other == null || getClass() != other.getClass()) return false;
            return matchEdge((EdgeImpl<?>) other);
        }

        protected abstract boolean matchEdge(EdgeImpl<?> other);

        @Override
        public abstract int hashCode();

    }

}
