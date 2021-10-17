package com.gitlab.sszuev.graphs;

/**
 * A graph with weights.
 * Created by @ssz on 17.10.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Glossary_of_graph_theory#weighted_graph'>wiki</a>
 */
public interface WeightedGraph<X> extends Graph<X> {

    /**
     * Returns a weight of the given edge.
     *
     * @param edge {@link Graph.Edge} - must belong to this graph
     * @return {@code long}
     */
    long weight(Edge<X> edge);
}
