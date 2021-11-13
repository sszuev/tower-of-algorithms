package com.gitlab.sszuev.graphs;

/**
 * Created by @ssz on 09.10.2021.
 */
public interface ModifiableGraph<X> extends Graph<X> {

    /**
     * Adds an edge to the graph.
     *
     * @param left  {@link X} - a payload for the left vertex, not {@code null}
     * @param right {@link X} - a payload for the right vertex, not {@code null}
     * @return {@link Edge}
     */
    Edge<X> add(X left, X right);

    /**
     * Builds a node with the given vertices.
     *
     * @param left  {@link X} - a payload for the left vertex (or source vertex in digraph), not {@code null}
     * @param right {@link X} - a payload for the first right vertex (or target vertex in digraph), not {@code null}
     * @param other {@code Array} of {@link X}s - other right vertexes
     * @return a <b>this</b> graph to allow cascading calls
     */
    @SuppressWarnings("unchecked")
    default ModifiableGraph<X> addNode(X left, X right, X... other) {
        add(left, right);
        for (X r : other) {
            add(left, r);
        }
        return this;
    }
}
