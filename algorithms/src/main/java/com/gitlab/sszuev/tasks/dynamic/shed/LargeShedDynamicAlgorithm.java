package com.gitlab.sszuev.tasks.dynamic.shed;

import com.gitlab.sszuev.utils.BitMatrix;

/**
 * An optimized solution for
 * a problem of calculation the maximum possible shed area on a fixed rectangular area with some obstacles.
 * The complexity is {@code O(N^3)}.
 * <p>
 * The problem:
 * <p>
 * The farmer wants to build a shed as large as possible on his land,
 * but there are trees and other outbuildings on the plot that he does not want to move anywhere.
 * A farmer's land is represented as a {@link BitMatrix}, where {@code 1}-bit are obstacles.
 * <p>
 * Created by @ssz on 03.01.2022.
 */
public class LargeShedDynamicAlgorithm extends BaseShedAlgorithm {

    public static long calcSquare(BitMatrix matrix) {
        int n = matrix.getWidth();
        int m = matrix.getHeight();
        int[] heights = new int[n];
        long maxSquare = 0;
        for (int j = 0; j < m; j++) {
            calcHeights(matrix, heights, j);
            for (int i = 0; i < n; i++) {
                int s = calcSquareAt(matrix, heights, i);
                if (maxSquare < s) {
                    maxSquare = s;
                }
            }
        }
        return maxSquare;
    }

    private static int calcSquareAt(BitMatrix matrix, int[] heights, final int i) {
        int maxSquare = 0;
        int minHeight = matrix.getHeight();
        for (int x = i; x < matrix.getWidth(); x++) {
            int height = heights[x];
            if (height == 0) {
                break;
            }
            if (minHeight > height) {
                minHeight = height;
            }
            int width = x + 1 - i;
            int s = width * minHeight;
            if (maxSquare < s) {
                maxSquare = s;
            }
        }
        return maxSquare;
    }

    private static void calcHeights(BitMatrix matrix, int[] heights, int j) {
        for (int i = 0; i < heights.length; i++) {
            if (matrix.get(i, j)) {
                heights[i] = 0;
            } else {
                heights[i]++;
            }
        }
    }

    @Override
    long calculate(BitMatrix area) {
        return calcSquare(area);
    }

}
