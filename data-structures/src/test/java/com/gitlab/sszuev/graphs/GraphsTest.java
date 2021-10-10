package com.gitlab.sszuev.graphs;

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

    private static Stream<String> data(Collection<Graph.Vertex<String>> component) {
        return component.stream().map(Graph.Vertex::payload);
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
