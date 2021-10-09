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
}
