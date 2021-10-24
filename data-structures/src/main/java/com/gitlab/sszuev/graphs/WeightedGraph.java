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
     * @return {@code double}
     * @throws java.util.NoSuchElementException if the edge does not belong to the graph
     */
    double weight(Edge<X> edge);

    /**
     * Answers {@code true} if there is negative weights in the graph.
     *
     * @return {@code boolean}
     */
    default boolean hasNegativeWeights() {
        return edges().anyMatch(e -> weight(e) < 0);
    }
}
