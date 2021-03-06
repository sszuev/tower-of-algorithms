package com.gitlab.sszuev.graphs;

import com.gitlab.sszuev.graphs.impl.DirectedGraphImpl;
import com.gitlab.sszuev.graphs.impl.UndirectedGraphImpl;
import com.gitlab.sszuev.graphs.impl.UndirectedWeightedGraphImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by @ssz on 09.10.2021.
 */
public class GraphsTest {

    @Test
    public void testAdjacencyMatrixForUndirectedGraph() {
        ModifiableGraph<Integer> graph = new UndirectedGraphImpl<>();
        graph.add(1, 1);
        graph.add(1, 2);
        graph.add(1, 5);

        graph.add(2, 1);
        graph.add(2, 5);
        graph.add(2, 3);

        graph.add(3, 4);
        graph.add(3, 2);

        graph.add(4, 3);
        graph.add(4, 5);
        graph.add(4, 6);

        graph.add(5, 1);
        graph.add(5, 2);
        graph.add(5, 4);

        graph.add(6, 4);

        byte[][] matrix = Graphs.toAdjacencyMatrix(graph);
        String res = print(matrix);
        System.out.println(res);

        byte[][] expected = new byte[][]{
                {2, 1, 0, 0, 1, 0},
                {1, 0, 1, 0, 1, 0},
                {0, 1, 0, 1, 0, 0},
                {0, 0, 1, 0, 1, 1},
                {1, 1, 0, 1, 0, 0},
                {0, 0, 0, 1, 0, 0}};

        Assertions.assertArrayEquals(expected, matrix);
    }

    @Test
    public void testAdjacencyMatrixForDirectedGraph() {
        ModifiableGraph<String> graph = new DirectedGraphImpl<>();
        graph.add("v01", "v13");
        graph.add("v01", "v03");
        graph.add("v02", "v13");
        graph.add("v04", "v03");
        graph.add("v05", "v09");
        graph.add("v05", "v03");
        graph.add("v05", "v10");
        graph.add("v06", "v04");
        graph.add("v06", "v11");
        graph.add("v06", "v13");
        graph.add("v06", "v12");
        graph.add("v07", "v06");
        graph.add("v08", "v04");
        graph.add("v08", "v06");
        graph.add("v08", "v14");
        graph.add("v08", "v02");
        graph.add("v09", "v01");
        graph.add("v09", "v07");
        graph.add("v10", "v01");
        graph.add("v10", "v14");
        graph.add("v10", "v12");
        graph.add("v11", "v03");
        graph.add("v13", "v03");
        graph.add("v14", "v11");

        byte[][] matrix = Graphs.toAdjacencyMatrix(graph);
        String res = print(matrix);
        System.out.println(res);

        Assertions.assertEquals(14, graph.getVector().size());

        byte[][] expected = {
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0}
        };
        Assertions.assertArrayEquals(expected, matrix);
    }

    @Test
    public void testDemucronTopologicalSort() {
        List<List<Integer>> actual = Graphs.demucronTopologicalSort(new byte[][]{
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 1, 1, 0, 0, 0, 0},
                {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 1, 0, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 1},
                {1, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 1},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0}
        });
        System.out.println(actual);
        List<List<Integer>> expected = List.of(
                List.of(4, 7),
                List.of(1, 8, 9),
                List.of(0, 6, 13),
                List.of(5),
                List.of(3, 10, 11, 12),
                List.of(2)
        );
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testTopologicalSort() {
        DirectedGraph<String> graph = new DirectedGraphImpl<String>()
                .addNode("org.openjdk.jmh:jmh-core:jar",
                        "net.sf.jopt-simple:jopt-simple",
                        "org.apache.commons:commons-math3")
                .addNode("org.openjdk.jmh:jmh-generator-annprocess",
                        "org.openjdk.jmh:jmh-core:jar")
                .addNode("org.junit.platform:junit-platform-engine",
                        "org.apiguardian:apiguardian-api",
                        "org.opentest4j:opentest4j",
                        "org.junit.platform:junit-platform-commons")
                .addNode("org.junit.jupiter:junit-jupiter-api",
                        "org.apiguardian:apiguardian-api",
                        "org.opentest4j:opentest4j",
                        "org.junit.platform:junit-platform-commons")
                .addNode("org.junit.platform:junit-platform-commons",
                        "org.apiguardian:apiguardian-api")
                .addNode("org.junit.jupiter:junit-jupiter-engine",
                        "org.junit.platform:junit-platform-engine",
                        "org.apiguardian:apiguardian-api",
                        "org.junit.jupiter:junit-jupiter-api",
                        "org.jetbrains.kotlin:kotlin-stdlib-jdk8")
                .addNode("org.junit.jupiter:junit-jupiter-params",
                        "org.junit.jupiter:junit-jupiter-api",
                        "org.apiguardian:apiguardian-api")
                .addNode("com.gitlab.sszuev:data-structures",
                        "org.openjdk.jmh:jmh-core:jar",
                        "org.openjdk.jmh:jmh-generator-annprocess",
                        "org.junit.jupiter:junit-jupiter-engine",
                        "org.junit.jupiter:junit-jupiter-params")
                .addNode("org.jetbrains.kotlin:kotlin-stdlib-jdk7",
                        "org.jetbrains.kotlin:kotlin-stdlib")
                .addNode("org.jetbrains.kotlin:kotlin-stdlib-jdk8",
                        "org.jetbrains.kotlin:kotlin-stdlib",
                        "org.jetbrains.kotlin:kotlin-stdlib-jdk7")
                .addNode("org.jetbrains.kotlin:kotlin-stdlib",
                        "org.jetbrains.kotlin:kotlin-stdlib-common",
                        "org.jetbrains:annotations");

        List<List<Graph.Vertex<String>>> levels = Graphs.topologicalSort(graph);
        levels.forEach(x -> System.out.println(x.stream().map(Graph.Vertex::payload).collect(Collectors.joining(", "))));

        Assertions.assertEquals(6, levels.size());
        Assertions.assertEquals(List.of("com.gitlab.sszuev:data-structures"),
                levels.get(0).stream().map(Graph.Vertex::payload).collect(Collectors.toList()));
        Assertions.assertEquals(Set.of("org.jetbrains.kotlin:kotlin-stdlib-common", "org.jetbrains:annotations"),
                levels.get(levels.size() - 1).stream().map(Graph.Vertex::payload).collect(Collectors.toSet()));
    }

    @Test
    public void testInvertDirectGraph() {
        DirectedGraph<String> graph = new DirectedGraphImpl<String>()
                .addNode("A", "B")
                .addNode("B", "E", "F", "C")
                .addNode("E", "A", "F")
                .addNode("F", "G")
                .addNode("G", "F", "C", "H")
                .addNode("C", "D")
                .addNode("D", "C", "H")
                .addNode("H", "D");

        System.out.println(print(graph));

        DirectedGraph<String> invert = graph.invert();
        System.out.println(print(invert));

        Set<String> expected = graph.edges().map(e -> print(e.right(), e.left())).collect(Collectors.toSet());
        Set<String> actual = invert.edges().map(GraphsTest::print).collect(Collectors.toSet());
        Assertions.assertEquals(expected, actual);

        Assertions.assertEquals(1, graph.vertex("C").orElseThrow().edges().count());
        Assertions.assertEquals(3, invert.vertex("C").orElseThrow().edges().count());
    }

    @Test
    public void testDFS() {
        DirectedGraph<String> graph = new DirectedGraphImpl<String>()
                .addNode("F", "G", "E")
                .addNode("G", "E", "A", "H")
                .addNode("E", "D", "A")
                .addNode("H", "A")
                .addNode("A", "K", "B")
                .addNode("D", "A", "C")
                .addNode("C", "B")
                .addNode("X", "Y");

        List<String> vertexes = new ArrayList<>();
        Graphs.dfs(graph, v -> vertexes.add(v.payload()));
        System.out.println(vertexes);

        Assertions.assertEquals(9, vertexes.size());
        Assertions.assertEquals(Set.of("B", "C", "K", "A", "D", "E", "H", "G", "F"), new HashSet<>(vertexes));
    }

    @Test
    public void testFindStronglyConnectedComponents() {
        DirectedGraph<String> graph = new DirectedGraphImpl<String>()
                .addNode("G", "F", "C", "H")
                .addNode("F", "G")
                .addNode("C", "D")
                .addNode("H", "D")
                .addNode("D", "C", "H")
                .addNode("A", "B")
                .addNode("B", "C", "F", "E")
                .addNode("E", "A", "F");

        System.out.println(print(graph));

        List<List<Graph.Vertex<String>>> components = Graphs.findStronglyConnectedComponents(graph);

        Assertions.assertEquals(3, components.size());
        Assertions.assertEquals(Set.of("C", "D", "H"), data(components.get(0)).collect(Collectors.toSet()));
        Assertions.assertEquals(Set.of("F", "G"), data(components.get(1)).collect(Collectors.toSet()));
        Assertions.assertEquals(Set.of("A", "E", "B"), data(components.get(2)).collect(Collectors.toSet()));
    }

    @Test
    public void testUndirectedWeightedGraph() {
        WeightedGraph<String> graph = new UndirectedWeightedGraphImpl<String>()
                .addEdge("A", "D", 5)
                .addEdge("A", "B", 7)
                .addEdge("D", "B", 9)
                .addEdge("B", "C", 8)
                .addEdge("B", "E", 7)
                .addEdge("C", "E", 5)
                .addEdge("D", "E", 15)
                .addEdge("D", "F", 6)
                .addEdge("F", "E", 8)
                .addEdge("F", "G", 11)
                .addEdge("E", "G", 9);
        Assertions.assertEquals(11, graph.edges().count());

        Assertions.assertEquals(7, graph.weight(graph.getEdge("A", "B")));
        Assertions.assertEquals(15, graph.weight(graph.getEdge("D", "E")));
        Assertions.assertEquals(8, graph.weight(graph.getEdge("B", "C")));
        Assertions.assertEquals(11, graph.weight(graph.getEdge("F", "G")));
    }

    @Test
    public void testMST() {
        // graph from wiki: https://en.wikipedia.org/wiki/Minimum_spanning_tree#/media/File:Minimum_spanning_tree.svg
        WeightedGraph<String> graph = new UndirectedWeightedGraphImpl<String>()
                .addEdge("a", "b", 6)
                .addEdge("a", "c", 3)
                .addEdge("a", "e", 9)

                .addEdge("b", "c", 4)
                .addEdge("b", "d", 2)
                .addEdge("b", "g", 9)

                .addEdge("c", "d", 2)
                .addEdge("c", "e", 9)
                .addEdge("c", "f", 9)

                .addEdge("d", "g", 9)
                .addEdge("d", "f", 8)

                .addEdge("e", "f", 8)
                .addEdge("e", "j", 18)

                .addEdge("f", "g", 7)
                .addEdge("f", "i", 9)
                .addEdge("f", "j", 10)

                .addEdge("g", "h", 4)
                .addEdge("g", "i", 5)

                .addEdge("h", "i", 1)
                .addEdge("h", "j", 4)

                .addEdge("i", "j", 3);

        Assertions.assertEquals(21, graph.edges().count());

        List<Graph.Edge<String>> actual = Graphs.findMinimumSpanningTree(graph);
        System.out.println(actual);
        Assertions.assertEquals(9, actual.size());
        Map.of("h", "i", "b", "d", "c", "d", "a", "c", "i", "j", "g", "h", "f", "g", "d", "f", "e", "f")
                .forEach((left, right) -> Assertions.assertTrue(actual.stream()
                                .anyMatch(e -> right.equals(e.right().payload()) && left.equals(e.left().payload())),
                        "Can't find edge [" + left + " <=> " + right + "]"));
    }

    @Test
    public void testFindAllShortestPaths() {
        WeightedGraph<String> graph = new UndirectedWeightedGraphImpl<String>()
                .addEdge("A", "B", 2)
                .addEdge("A", "C", 3)
                .addEdge("A", "D", 6)

                .addEdge("B", "C", 4)
                .addEdge("B", "E", 9)

                .addEdge("C", "D", 1)
                .addEdge("C", "E", 7)
                .addEdge("C", "F", 6)

                .addEdge("D", "F", 4)

                .addEdge("E", "F", 1)
                .addEdge("E", "G", 5)

                .addEdge("F", "G", 8);

        Map<String, List<Graph.Edge<String>>> res = Graphs.findShortestPaths(graph, "A");
        Assertions.assertEquals(7, res.size());
        System.out.println(res.get("G"));

        Assertions.assertEquals(List.of(3., 1., 4., 1., 5.), getWeights(graph, res.get("G")));
        Assertions.assertEquals(List.of(), getWeights(graph, res.get("A")));
        Assertions.assertEquals(List.of(3.), getWeights(graph, res.get("C")));
        Assertions.assertEquals(List.of(3., 1., 4.), getWeights(graph, res.get("F")));
        Assertions.assertEquals(List.of(2.), getWeights(graph, res.get("B")));
        Assertions.assertEquals(List.of(3., 1., 4., 1.), getWeights(graph, res.get("E")));
    }

    @Test
    public void testFindShortestPath() {
        WeightedGraph<Integer> graph = new UndirectedWeightedGraphImpl<Integer>()
                .addEdge(0, 2, 1.)
                .addEdge(0, 4, 5.)

                .addEdge(1, 2, 5.)
                .addEdge(1, 3, 8.5)
                .addEdge(1, 5, 5.)

                .addEdge(2, 6, 1.)

                .addEdge(3, 5, 4.)
                .addEdge(3, 7, 6.)

                .addEdge(5, 6, 8.)
                .addEdge(5, 7, 2.)

                .addEdge(6, 7, 6.5);

        Assertions.assertEquals(List.of(0, 2, 6, 5), getVertexes(Graphs.findShortestPath(graph, 0, 5)));
        Assertions.assertEquals(List.of(0, 2, 6, 7), getVertexes(Graphs.findShortestPath(graph, 0, 7)));
        Assertions.assertEquals(List.of(0, 2, 6, 5, 3), getVertexes(Graphs.findShortestPath(graph, 0, 3)));
        Assertions.assertEquals(List.of(0, 2, 1), getVertexes(Graphs.findShortestPath(graph, 0, 1)));
        Assertions.assertEquals(List.of(0, 2, 6), getVertexes(Graphs.findShortestPath(graph, 0, 6)));
        Assertions.assertEquals(List.of(0, 2), getVertexes(Graphs.findShortestPath(graph, 0, 2)));
        Assertions.assertEquals(List.of(0, 4), getVertexes(Graphs.findShortestPath(graph, 0, 4)));
        Assertions.assertEquals(List.of(), getVertexes(Graphs.findShortestPath(graph, 0, 0)));
    }

    private static Stream<String> data(Collection<Graph.Vertex<String>> component) {
        return component.stream().map(Graph.Vertex::payload);
    }

    private static <T> List<Double> getWeights(WeightedGraph<T> graph, List<Graph.Edge<T>> edges) {
        return edges.stream().map(graph::weight).collect(Collectors.toUnmodifiableList());
    }

    private static <T> List<T> getVertexes(List<Graph.Edge<T>> edges) {
        LinkedList<T> res = new LinkedList<>();
        for (int i = 0; i < edges.size(); i++) {
            Graph.Edge<T> e = edges.get(i);
            T left = e.left().payload();
            T right = e.right().payload();
            if (i == 0) {
                res.add(left);
            } else if (!Objects.equals(res.getLast(), left)) {
                throw new IllegalStateException();
            }
            res.add(right);
        }
        return res;
    }

    private static <X> String print(Graph<X> graph) {
        return graph.edges().map(GraphsTest::print).collect(Collectors.joining(", "));
    }

    private static String print(Graph.Edge<?> edge) {
        return print(edge.left(), edge.right());
    }

    private static String print(Graph.Vertex<?> left, Graph.Vertex<?> right) {
        return String.format("%s => %s", print(left), print(right));
    }

    private static String print(Graph.Vertex<?> vertex) {
        return String.valueOf(vertex.payload());
    }

    private static String print(byte[][] matrix) {
        return Arrays.deepToString(matrix).replace("], [", "],\n [");
    }

}
