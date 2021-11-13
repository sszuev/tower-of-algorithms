package com.gitlab.sszuev.graphs;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The generic graph interface.
 * <p>
 * Created by @ssz on 09.10.2021.
 *
 * @param <X> anything
 * @see <a href='https://en.wikipedia.org/wiki/Graph_(discrete_mathematics)'>wiki: Graph</a>
 */
public interface Graph<X> {

    /**
     * Lists all the vertices that make up this graph.
     *
     * @return a {@code Stream} of {@link Vertex}es
     */
    Stream<Vertex<X>> vertexes();

    /**
     * Finds vertex by its content.
     *
     * @param key {@link X}, not {@code null}
     * @return an {@code Optional} of {@link Vertex}
     */
    default Optional<Vertex<X>> vertex(X key) {
        Objects.requireNonNull(key);
        return vertexes().filter(v -> Objects.equals(key, v.payload())).findFirst();
    }

    /**
     * Returns a total number of vertexes in the graph.
     *
     * @return {@code long}
     */
    default long size() {
        return vertexes().count();
    }

    /**
     * Lists all the edges that connect all this graph vertices.
     *
     * @return a {@code Stream} of {@link Edge}s
     */
    default Stream<Edge<X>> edges() {
        return vertexes().flatMap(Vertex::edges);
    }

    /**
     * Returns an edge by the specified vertexes.
     *
     * @param left  {@link X} - a payload for the left vertex, not {@code null}
     * @param right {@link X} - a payload for the right vertex, not {@code null}
     * @return {@link Edge}
     * @throws NoSuchElementException if no edge is present
     */
    default Edge<X> getEdge(X left, X right) {
        return vertex(left).orElseThrow().edge(right).orElseThrow();
    }

    /**
     * Gets the vertex by its content.
     *
     * @param key {@link X} a vertex payload
     * @return {@link Vertex}, never {@code null}
     * @throws NoSuchElementException if no vertex is found
     */
    default Vertex<X> getVertex(X key) {
        Graph.Vertex<X> res = vertex(key).orElse(null);
        if (res == null) {
            throw new NoSuchElementException("Can't find vertex = " + key);
        }
        return res;
    }

    /**
     * Answers a {@code List} of {@link Vertex vertecses} that make up this graph.
     * The order of vertices in the list is determined by the internal mechanisms:
     * usually it is the natural order if the {@link X} is {@link Comparable comparable}
     * or can be the order of insertion.
     *
     * @return an unmodifiable {@code List} of {@link Vertex}es
     */
    default List<Vertex<X>> getVector() {
        return vertexes().collect(Collectors.toUnmodifiableList());
    }

    /**
     * Represents a vertex.
     *
     * @param <K> anything
     */
    interface Vertex<K> {
        /**
         * Returns an encapsulated payload.
         *
         * @return {@link K}
         */
        K payload();

        /**
         * Lists all edges that connect this vertex with others.
         *
         * @return a {@code Stream} of {@link Edge}s, where {@link Edge#left()} is this vertex
         */
        Stream<Edge<K>> edges();

        /**
         * Lists all adjacent vertices.
         *
         * @return a {@code Stream} of adjacent {@link Vertex}s
         */
        default Stream<Vertex<K>> adjacent() {
            return edges().map(Edge::right);
        }

        /**
         * Finds an edge by the destination vertex payload.
         *
         * @param other {@link K} - the right vertex payload, not {@code null}
         * @return an {@code Optional}
         */
        default Optional<Edge<K>> edge(K other) {
            Objects.requireNonNull(other);
            return edges().filter(v -> Objects.equals(other, v.right().payload())).findFirst();
        }
    }

    /**
     * Represents a vertex.
     *
     * @param <V> anything
     */
    interface Edge<V> {
        /**
         * Returns the left {@link Vertex}.
         * For directed graph it is a source vertex (the arrow base);
         * for undirected graph this is one arbitrary vertex from a vertex pair that make up this edge,
         * but for every call it is the same.
         *
         * @return {@link Vertex}, not {@code null}
         */
        Vertex<V> left();

        /**
         * Returns the right {@link Vertex}.
         * For directed graph it is a target vertex (the arrowhead);
         * for undirected graph this is one arbitrary vertex from a vertex pair that make up this edge,
         * but for every call it is the same.
         *
         * @return {@link Vertex}, not {@code null}
         */
        Vertex<V> right();
    }
}
