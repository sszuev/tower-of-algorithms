package com.gitlab.sszuev.graphs;

/**
 * Created by @ssz on 17.10.2021.
 */
public interface ModifiableWeightedGraph<X> extends WeightedGraph<X> {

    /**
     * Adds a weighted edge to the graph.
     *
     * @param left   {@link X} - a payload for the left vertex, not {@code null}
     * @param right  {@link X} - a payload for the right vertex, not {@code null}
     * @param weight {@code double} a weight of edge
     * @return {@link Edge}
     */
    Edge<X> add(X left, X right, double weight);

    /**
     * @param left   {@link X} - a payload for the left vertex, not {@code null}
     * @param right  {@link X} - a payload for the right vertex, not {@code null}
     * @param weight {@code double} a weight of edge
     * @return a <b>this</b> graph to allow cascading calls
     */
    default ModifiableWeightedGraph<X> addEdge(X left, X right, double weight) {
        add(left, right, weight);
        return this;
    }
}
