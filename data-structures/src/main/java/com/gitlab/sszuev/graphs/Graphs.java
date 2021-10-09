package com.gitlab.sszuev.graphs;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Utilities to work with the {@link Graph graph}s.
 * <p>
 * Created by @ssz on 09.10.2021.
 */
public class Graphs {

    /**
     * Makes an adjacency matrix for the specified {@code graph}, that can be directed or undirected.
     * It is the square matrix, symmetric in case of undirected graph.
     *
     * @param graph {@link Graph}, not {@code null}
     * @param <X>   anything
     * @return an {@code Array} of {@code Array} of {@code byte}s
     * which can be {@code 0}, {@code 1} or {@code 2} for undirected graph
     * @see <a href='https://en.wikipedia.org/wiki/Adjacency_matrix'>wiki: adjacency_matrix</a>
     */
    public static <X> byte[][] toAdjacencyMatrix(Graph<X> graph) {
        return toAdjacencyMatrix(graph.getVector());
    }

    /**
     * Makes an adjacency matrix for the specified {@code vertexes}
     *
     * @param vertexes a {@code List} of {@link Graph.Vertex vertices}, not {@code null}
     * @param <X>      anything
     * @return an {@code Array} of {@code Array} of {@code byte}s
     * @see #toAdjacencyMatrix(Graph)
     */
    public static <X> byte[][] toAdjacencyMatrix(List<Graph.Vertex<X>> vertexes) {
        int size = vertexes.size();
        byte[][] res = new byte[size][size];
        for (int i = 0; i < size; i++) {
            Graph.Vertex<X> vi = vertexes.get(i);
            Set<Graph.Vertex<X>> dst = vi.edges().map(Graph.Edge::right).collect(Collectors.toSet());
            res[i] = new byte[size];
            for (int j = 0; j < size; j++) {
                Graph.Vertex<X> vj = vertexes.get(j);
                if (!dst.contains(vj)) {
                    continue;
                }
                res[i][j] = (byte) (vi.equals(vj) ? 2 : 1);
            }
        }
        return res;
    }
}
