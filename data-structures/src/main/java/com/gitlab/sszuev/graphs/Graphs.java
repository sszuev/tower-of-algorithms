package com.gitlab.sszuev.graphs;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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

    /**
     * Performs a topological sort on the specified graph using Demucron's algorithm.
     * The graph must be acyclic and directed.
     * The method returns a list of levels,
     * each level contains vertices with the same length of path (shortest for first, longest for last).
     *
     * @param graph {@link DirectedGraph} acyclic digraph, not {@code null}
     * @param <X>   anything
     * @return a {@code List} of {@code List} of {@link Graph.Vertex vertexes}
     * @see <a href='https://en.wikipedia.org/wiki/Topological_sorting'>wiki: topological sorting</a>
     */
    public static <X> List<List<Graph.Vertex<X>>> topologicalSort(DirectedGraph<X> graph) {
        List<Graph.Vertex<X>> vertices = graph.getVector();
        byte[][] matrix = toAdjacencyMatrix(vertices);
        List<List<Integer>> levels = demucronTopologicalSort(matrix);
        List<List<Graph.Vertex<X>>> res = new ArrayList<>();
        for (List<Integer> indexes : levels) {
            res.add(indexes.stream().map(vertices::get).collect(Collectors.toList()));
        }
        return res;
    }

    /**
     * Runs a Demucron's algorithm (optimized Kahn's algorithm) for the given adjacency matrix.
     * The result is an array of levels which contain vertex indexes.
     *
     * @param matrix {@code byte[][]} - a square adjacency matrix
     * @return a {@code List} of {@code List} of {@code int}-indexes
     * @see <a href='https://en.wikipedia.org/wiki/Topological_sorting#Kahn's_algorithm'>wiki: Kahn's algorithm</a>
     */
    public static List<List<Integer>> demucronTopologicalSort(byte[][] matrix) {
        if (matrix.length == 0) {
            return Collections.emptyList();
        }
        if (Arrays.stream(matrix).anyMatch(row -> row.length != matrix.length)) {
            throw new IllegalArgumentException();
        }

        int[] sum = new int[matrix.length];
        IntStream.range(0, sum.length)
                .forEach(index -> sum[index] = Arrays.stream(matrix).mapToInt(row -> row[index]).sum());

        List<List<Integer>> res = new ArrayList<>();
        List<Integer> level;
        Set<Integer> processed = new HashSet<>();
        while (true) {
            level = IntStream.range(0, sum.length).filter(x -> !processed.contains(x))
                    .filter(i -> sum[i] == 0).boxed().collect(Collectors.toList());
            if (level.isEmpty()) {
                break;
            }
            res.add(level);
            processed.addAll(level);
            List<Integer> prev = level;
            IntStream.range(0, sum.length)
                    .forEach(index -> sum[index] -= prev.stream().map(i -> matrix[i]).mapToInt(row -> row[index]).sum());
        }
        return res;
    }

    /**
     * Searches for strongly connected components in the given graph using Kosaraju's algorithm.
     * Returns a list of found components as a list of vertices.
     *
     * @param graph {@link DirectedGraph} a digraph, not {@code null}
     * @param <X>   anything
     * @return a {@code List} of {@code List} of {@link Graph.Vertex vertexes}
     * @see <a href='https://en.wikipedia.org/wiki/Kosaraju%27s_algorithm'>wiki: Kosaraju's algorithm</a>
     */
    public static <X> List<List<Graph.Vertex<X>>> findStronglyConnectedComponents(DirectedGraph<X> graph) {
        LinkedList<X> ordered = new LinkedList<>();
        Set<Graph.Vertex<X>> invert = graph.invert().vertexes().collect(Collectors.toSet());
        Set<Graph.Vertex<X>> seen = new HashSet<>();
        while (!invert.isEmpty()) {
            Graph.Vertex<X> v = invert.iterator().next();
            dfs(v, seen, u -> {
                invert.remove(u);
                ordered.addFirst(u.payload());
            });
        }
        List<List<Graph.Vertex<X>>> res = new ArrayList<>();
        seen.clear();
        while (!ordered.isEmpty()) {
            Graph.Vertex<X> v = graph.vertex(ordered.get(0)).orElseThrow();
            List<Graph.Vertex<X>> component = new ArrayList<>();
            res.add(component);
            dfs(v, seen, u -> {
                ordered.remove(u.payload());
                component.add(u);
            });
        }
        return res;
    }

    /**
     * Performs depth-first search on the given graph.
     *
     * @param graph  {@link Graph}
     * @param action {@link Consumer} - an action to perform on every vertex
     * @param <X>    anything
     * @see <a href='https://en.wikipedia.org/wiki/Depth-first_search'>wiki: DFS</a>
     */
    public static <X> void dfs(Graph<X> graph, Consumer<Graph.Vertex<X>> action) {
        Graph.Vertex<X> v = graph.vertexes().findFirst().orElseThrow();
        dfs(v, new HashSet<>(), action);
    }

    private static <X> void dfs(Graph.Vertex<X> vertex,
                                Set<Graph.Vertex<X>> seen,
                                Consumer<Graph.Vertex<X>> action) {
        if (!seen.add(vertex)) {
            return;
        }
        vertex.adjacent().forEach(u -> dfs(u, seen, action));
        action.accept(vertex);
    }
}
