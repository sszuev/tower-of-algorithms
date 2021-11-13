package com.gitlab.sszuev.graphs;

/**
 * Created by @ssz on 10.10.2021.
 */
public interface DirectedGraph<X> extends Graph<X> {

    /**
     * Makes a copy with inverted arcs.
     *
     * @return {@link DirectedGraph}
     */
    DirectedGraph<X> invert();
}
