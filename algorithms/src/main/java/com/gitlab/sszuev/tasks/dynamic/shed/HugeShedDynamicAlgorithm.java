package com.gitlab.sszuev.tasks.dynamic.shed;

import com.gitlab.sszuev.utils.BitMatrix;

/**
 * An optimal solution for
 * a problem of calculation the maximum possible shed area on a fixed rectangular area with some obstacles.
 * The complexity is {@code O(N^2)}.
 * There are two kind of optimizations:
 * optimization for calculation height (same as in {@link LargeShedDynamicAlgorithm})
 * plus optimization for calculation weights using dynamic programming approach.
 * <p>
 * The problem:
 * <p>
 * The farmer wants to build a shed as large as possible on his land,
 * but there are trees and other outbuildings on the plot that he does not want to move anywhere.
 * A farmer's land is represented as a {@link BitMatrix}, where {@code 1}-bit are obstacles.
 * <p>
 * Created by @ssz on 03.01.2022.
 */
public class HugeShedDynamicAlgorithm extends BaseShedAlgorithm {

    public static long calcSquare(BitMatrix matrix) {
        int n = matrix.getWidth();
        int m = matrix.getHeight();
        int[] heights = new int[n];
        int[] rights = new int[n];
        int[] lefts = new int[n];
        int[] stack = new int[n];
        long maxSquare = 0;
        for (int j = 0; j < m; j++) {
            calcHeights(matrix, heights, j);
            calcRights(rights, stack, heights);
            calcLefts(lefts, stack, heights);

            for (int i = 0; i < n; i++) {
                int height = heights[i];
                int width = rights[i] - lefts[i] + 1;
                int s = height * width;
                if (maxSquare < s) {
                    maxSquare = s;
                }
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

    private static void calcRights(int[] rights, int[] stack, int[] heights) {
        int n = heights.length;
        int stackSize = 0;
        for (int x = 0; x < heights.length; x++) {
            while (stackSize > 0) {
                if (heights[stack[stackSize - 1]] > heights[x]) {
                    rights[stack[--stackSize]] = x - 1;
                } else {
                    break;
                }
            }
            stack[stackSize++] = x;
        }
        while (stackSize > 0) {
            rights[stack[--stackSize]] = n - 1;
        }
    }

    private static void calcLefts(int[] lefts, int[] stack, int[] heights) {
        int n = heights.length;
        int stackSize = 0;
        for (int x = n - 1; x >= 0; x--) {
            while (stackSize > 0) {
                if (heights[stack[stackSize - 1]] > heights[x]) {
                    lefts[stack[--stackSize]] = x + 1;
                } else {
                    break;
                }
            }
            stack[stackSize++] = x;
        }
        while (stackSize > 0) {
            lefts[stack[--stackSize]] = 0;
        }
    }

    @Override
    long calculate(BitMatrix area) {
        return calcSquare(area);
    }

}
