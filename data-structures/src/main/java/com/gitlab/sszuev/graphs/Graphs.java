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
     * Performs the Depth-First Search on the given graph (recursive implementation).
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

    /**
     * Returns a Minimum Spanning Tree for the specified {@code WeightedGraph}.
     * Calculations are performed using Borůvka's algorithm, which is an optimized Kruskal's algorithm.
     *
     * @param graph a {@link WeightedGraph}, not {@code null}
     * @param <X>   anything
     * @return a {@code List} of {@link Graph.Edge}s
     * @see <a href='https://en.wikipedia.org/wiki/Minimum_spanning_tree'>wiki: MST</a>
     * @see <a href='https://en.wikipedia.org/wiki/Bor%C5%AFvka%27s_algorithm'>wiki: Borůvka's algorithm</a>
     * @see <a href='https://en.wikipedia.org/wiki/Kruskal%27s_algorithm'>wiki: Kruskal's algorithm</a>
     */
    public static <X> List<Graph.Edge<X>> findMinimumSpanningTree(WeightedGraph<X> graph) {
        List<Graph.Edge<X>> edges = graph.edges()
                .sorted(Comparator.comparing(graph::weight))
                .collect(Collectors.toList());
        long vertexes = graph.vertexes().count();
        Map<Graph.Vertex<X>, Graph.Vertex<X>> parents = new HashMap<>();
        List<Graph.Edge<X>> res = new ArrayList<>();
        while (res.size() < vertexes - 1) {
            boruvkaAddEdge(res, parents, edges.remove(0));
        }
        return res;
    }

    private static <X> void boruvkaAddEdge(List<Graph.Edge<X>> res,
                                           Map<Graph.Vertex<X>, Graph.Vertex<X>> parents,
                                           Graph.Edge<X> edge) {
        Graph.Vertex<X> p1 = findParent(parents, edge.left());
        Graph.Vertex<X> p2 = findParent(parents, edge.right());
        if (Objects.equals(p1, p2)) {
            // already connected
            return;
        }
        parents.put(p1, p2);
        res.add(edge);
    }

    private static <X> X findParent(Map<X, X> parents, X key) {
        X res = parents.get(key);
        if (res == null) {
            return key;
        }
        res = findParent(parents, res);
        parents.put(key, res);
        return res;
    }

    /**
     * Finds all shortest paths in the weighted graph for the specified source vertex.
     * The method returns a {@code Map},
     * where keys are target vertexes and values are paths from the source to that target.
     * <p>
     * Right now the method is based on the pure Dijkstra's algorithm
     * and therefore cannot be applicable to the graph with negative weights.
     *
     * @param graph  {@link WeightedGraph}
     * @param source {@link X} - a source vertex payload
     * @param <X>    - anything
     * @return a {@code Map}, key is a target {@link X}-vertex, value is a path - a {@code List} of {@link Graph.Edge}s
     * @see <a href='https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm'>wiki: Dijkstra's algorithm</a>
     * @see <a href='https://www.cs.usfca.edu/~galles/visualization/Dijkstra.html'>visualization: Dijkstra's algorithm</a>
     */
    public static <X> Map<X, List<Graph.Edge<X>>> findShortestPaths(WeightedGraph<X> graph, X source) {
        if (graph.hasNegativeWeights()) {
            throw new IllegalArgumentException("Not supported: graph contains negative weights");
        }
        Graph.Vertex<X> start = graph.getVertex(source);
        long size = graph.size();
        Set<Graph.Vertex<X>> seen = new HashSet<>();
        Comparator<Map.Entry<Graph.Vertex<X>, Cost<X>>> comp = Comparator.comparing(e -> e.getValue().value);
        Map<Graph.Vertex<X>, Cost<X>> costs = new HashMap<>();
        costs.computeIfAbsent(start, x -> new Cost<>()).put(null, 0.);

        do {
            if (calcCosts(graph, costs, comp, seen) == null) {
                break;
            }
        } while (seen.size() < size);

        return costs.keySet().stream()
                .collect(Collectors.toUnmodifiableMap(Graph.Vertex::payload, v -> extractPath(graph, costs, v)));
    }

    /**
     * Finds a shortest path in the weighted graph for the specified source and target vertexes.
     * <p>
     * Right now the method is based on the pure Dijkstra's algorithm
     * and therefore cannot be applicable to the graph with negative weights.
     *
     * @param graph  {@link WeightedGraph}
     * @param source {@link X} - a source vertex payload
     * @param target {@link X} - a target vertex payload
     * @param <X>    - anything
     * @return a {@code List} of {@link Graph.Edge}s
     * @see <a href='https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm'>wiki: Dijkstra's algorithm</a>
     * @see <a href='https://www.cs.usfca.edu/~galles/visualization/Dijkstra.html'>visualization: Dijkstra's algorithm</a>
     */
    public static <X> List<Graph.Edge<X>> findShortestPath(WeightedGraph<X> graph, X source, X target) {
        if (graph.hasNegativeWeights()) {
            throw new IllegalArgumentException("Not supported: graph contains negative weights");
        }
        Graph.Vertex<X> start = graph.getVertex(source);
        Graph.Vertex<X> end = graph.getVertex(target);
        long size = graph.size();
        Set<Graph.Vertex<X>> seen = new HashSet<>();
        Comparator<Map.Entry<Graph.Vertex<X>, Cost<X>>> comp = Comparator.comparing(e -> e.getValue().value);
        Map<Graph.Vertex<X>, Cost<X>> costs = new HashMap<>();
        costs.computeIfAbsent(start, x -> new Cost<>()).put(null, 0.);

        do {
            Graph.Vertex<X> res = calcCosts(graph, costs, comp, seen);
            if (res == null) {
                break;
            }
            if (res.equals(end)) {
                return extractPath(graph, costs, res);
            }
        } while (seen.size() < size);

        // nothing found -> empty path
        return List.of();
    }

    private static <X> Graph.Vertex<X> calcCosts(WeightedGraph<X> graph,
                                                 Map<Graph.Vertex<X>, Cost<X>> costs,
                                                 Comparator<Map.Entry<Graph.Vertex<X>, Cost<X>>> comp,
                                                 Set<Graph.Vertex<X>> seen) {
        Map.Entry<Graph.Vertex<X>, Cost<X>> min = costs.entrySet().stream()
                .filter(x -> !seen.contains(x.getKey()))
                .min(comp).orElse(null);
        if (min == null) {
            return null;
        }
        Graph.Vertex<X> vertex = min.getKey();
        Cost<X> cost = min.getValue();
        vertex.edges().forEach(e -> {
            Graph.Vertex<X> u = e.right();
            if (seen.contains(u)) {
                return;
            }
            double w = graph.weight(e) + cost.value;
            costs.computeIfAbsent(u, x -> new Cost<>()).put(vertex, w);
        });
        seen.add(vertex);
        return vertex;
    }

    private static <X> List<Graph.Edge<X>> extractPath(Graph<X> graph,
                                                       Map<Graph.Vertex<X>, Cost<X>> costs,
                                                       Graph.Vertex<X> target) {
        LinkedList<Graph.Edge<X>> res = new LinkedList<>();
        Graph.Vertex<X> to = target;
        Graph.Vertex<X> from;
        for (int i = 0; i < costs.size(); i++) {
            Cost<X> cost = costs.get(to);
            if (cost == null) {
                break;
            }
            from = cost.from;
            if (from == null) {
                break;
            }
            Graph.Edge<X> edge = graph.getEdge(from.payload(), to.payload());
            res.addFirst(edge);
            to = from;
        }
        return Collections.unmodifiableList(res);
    }

    /**
     * An auxiliary object that is used in the Dijkstra's algorithm.
     * Contains vertex-source and weight.
     *
     * @param <X> anything
     */
    private static class Cost<X> {
        Graph.Vertex<X> from;
        double value = Double.POSITIVE_INFINITY;

        private void put(Graph.Vertex<X> vertex, Double cost) {
            if (cost > this.value) {
                return;
            }
            this.from = vertex;
            this.value = cost;
        }

        @Override
        public String toString() {
            return String.format("%s-%s", value, from == null ? null : from.payload());
        }
    }


}
