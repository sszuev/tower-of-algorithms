package com.gitlab.sszuev.graphs;

import java.util.List;
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
     * Lists all the edges that connect all this graph vertices.
     *
     * @return a {@code Stream} of {@link Edge}
     */
    default Stream<Edge<X>> edges() {
        return vertexes().flatMap(Vertex::edges);
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
     * @param <P> anything
     */
    interface Vertex<P> {
        /**
         * Returns an encapsulated payload.
         *
         * @return {@link P}
         */
        P payload();

        /**
         * Lists all edges that connect this vertex with others.
         *
         * @return a {@code Stream} of {@link Edge}s, where {@link Edge#left()} is this vertex
         */
        Stream<Edge<P>> edges();

        /**
         * Lists all adjacent vertices.
         *
         * @return a {@code Stream} of adjacent {@link Vertex}s
         */
        default Stream<Vertex<P>> adjacent() {
            return edges().map(Edge::right);
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
