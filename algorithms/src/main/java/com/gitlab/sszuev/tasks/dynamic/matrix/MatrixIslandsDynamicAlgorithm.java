package com.gitlab.sszuev.tasks.dynamic.matrix;

import com.gitlab.sszuev.tasks.Algorithm;

import java.util.Arrays;
import java.util.List;

/**
 * Each element of the square matrix of dimension {@code N x N} is equal to {@code 0} or {@code 1}.
 * Need to find the number of "islands" formed by {@code 1}.
 * <p>
 * An "island" is understood as a group of {@code 1}s (can be one {@code 1}),
 * surrounded on all sides by {@code 0}s (or by the edges of the matrix).
 * A cell with {@code 1} is considered to belong to one "island"
 * if you can move to another "island" only by stepping on {@code 1}s located in neighboring cells.
 * <p>
 * Examples:
 * This matrix has 3 islands:
 * <pre>{@code
 * 1 1 1 1
 * 0 1 0 1
 * 0 0 0 0
 * 1 0 1 1
 * }</pre>
 * And this one has 5 islands:
 * <pre>{@code
 * 1 0 1
 * 0 1 0
 * 1 0 1
 * }</pre>
 * Created by @ssz on 01.01.2022.
 */
public class MatrixIslandsDynamicAlgorithm implements Algorithm {
    private static final byte ONE = 1;
    private static final byte ZERO = 0;

    public static long countIslands(byte[][] matrix) {
        final int n = matrix.length;
        final int m = matrix[0].length;

        byte[][] map = copy(matrix);
        long res = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (map[i][j] == ONE) {
                    res++;
                    clear(map, n, m, i, j);
                }
            }
        }
        return res;
    }

    private static void clear(byte[][] matrix, int n, int m, int i, int j) {
        if (i < 0 || i >= n) {
            return;
        }
        if (j < 0 || j >= m) {
            return;
        }
        if (matrix[i][j] == ZERO) {
            return;
        }
        matrix[i][j] = ZERO;
        clear(matrix, n, m, i - 1, j);
        clear(matrix, n, m, i + 1, j);
        clear(matrix, n, m, i, j - 1);
        clear(matrix, n, m, i, j + 1);
    }

    public static byte[][] parseMatrix(String dimension, String... rows) {
        int n = Integer.parseInt(dimension);
        if (rows.length != n) {
            throw new IllegalArgumentException();
        }
        byte[][] res = new byte[n][n];
        for (int i = 0; i < n; i++) {
            String[] tmp = rows[i].split("\\s+");
            if (tmp.length != n) {
                throw new IllegalArgumentException();
            }
            res[i] = new byte[n];
            for (int j = 0; j < n; j++) {
                res[i][j] = "1".equals(tmp[j]) ? ONE : ZERO;
            }
        }
        return res;
    }

    public static byte[][] copy(byte[][] orig) {
        byte[][] res = new byte[orig.length][];
        for (int i = 0; i < orig.length; i++) {
            byte[] row = orig[i];
            res[i] = Arrays.copyOf(row, row.length);
        }
        return res;
    }

    @Override
    public List<String> run(String arg, String... other) {
        byte[][] matrix = parseMatrix(arg, other);
        return List.of(String.valueOf(countIslands(matrix)));
    }
}
