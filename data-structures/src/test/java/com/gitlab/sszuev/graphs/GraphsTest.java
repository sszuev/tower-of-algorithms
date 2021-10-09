package com.gitlab.sszuev.graphs;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * Created by @ssz on 09.10.2021.
 */
public class GraphsTest {

    @Test
    public void testAdjacencyMatrixForUndirectedGraph() {
        ModifiableGraph<Integer> graph = new UndirectedGraph<>();
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
        String res = matrixToString(matrix);
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
        ModifiableGraph<String> graph = new DirectedGraph<>();
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
        String res = matrixToString(matrix);
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

    private static String matrixToString(byte[][] matrix) {
        return Arrays.deepToString(matrix).replace("], [", "],\n [");
    }

}
