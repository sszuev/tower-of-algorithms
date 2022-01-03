package com.gitlab.sszuev.tasks.dynamic.shed;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.utils.BitMatrix;

import java.util.List;

/**
 * A problem of calculation the maximum possible shed area on a fixed rectangular area with some obstacles.
 * <p>
 * The farmer wants to build a shed as large as possible on his land,
 * but there are trees and other outbuildings on the plot that he does not want to move anywhere.
 * A farmer's land is represented as a {@link BitMatrix}, where {@code 1}-bit are obstacles.
 * <p>
 * Created by @ssz on 02.01.2022.
 */
public class SmallShedDynamicAlgorithm implements Algorithm {

    public static long calcSquare(BitMatrix matrix) {
        int n = matrix.getWidth();
        int m = matrix.getHeight();
        long maxSquare = 0;
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                int s = calcSquareAt(matrix, i, j);
                if (maxSquare < s) {
                    maxSquare = s;
                }
            }
        }
        return maxSquare;
    }

    private static int calcSquareAt(BitMatrix matrix, final int i, final int j) {
        int maxSquare = 0;
        int minHeight = matrix.getHeight();
        for (int k = i; k < matrix.getWidth(); k++) {
            int height = calcHeightAt(minHeight, matrix, k, j);
            if (height == 0) {
                break;
            }
            if (minHeight > height) {
                minHeight = height;
            }
            int width = k + 1 - i;
            int s = width * minHeight;
            if (maxSquare < s) {
                maxSquare = s;
            }
        }
        return maxSquare;
    }

    private static int calcHeightAt(int minHeight, BitMatrix matrix, int i, int j) {
        int height = 0;
        while (height < minHeight && j - height >= 0 && !matrix.get(i, j - height)) {
            height++;
        }
        return height;
    }

    public static BitMatrix parse(String widthHeight, String... obstacles) {
        checkTwoNumbersInLine(widthHeight);
        if (obstacles.length < 1) {
            throw new IllegalArgumentException();
        }
        int numberOfObstacles = Integer.parseInt(obstacles[0].trim());
        if (numberOfObstacles != obstacles.length - 1) {
            throw new IllegalArgumentException();
        }
        int width = parseFirstNumber(widthHeight);
        int height = parseSecondNumber(widthHeight);
        BitMatrix res = new BitMatrix(width, height);
        for (int i = 1; i <= numberOfObstacles; i++) {
            String line = obstacles[i];
            int x = parseFirstNumber(checkTwoNumbersInLine(line));
            int y = parseSecondNumber(line);
            res.set(x, y);
        }
        return res;
    }

    private static String checkTwoNumbersInLine(String line) {
        if (!line.matches("^\\s*\\d+\\s+\\d+\\s*$")) {
            throw new IllegalArgumentException("Wrong line: " + line);
        }
        return line;
    }

    private static int parseFirstNumber(String line) {
        return Integer.parseInt(line.replaceFirst("^\\s*(\\d+)\\s+\\d+\\s*$", "$1"));
    }

    private static int parseSecondNumber(String line) {
        return Integer.parseInt(line.replaceFirst("^\\s*\\d+\\s+(\\d+)\\s*$", "$1"));
    }

    @Override
    public List<String> run(String arg, String... other) {
        BitMatrix matrix = parse(arg, other);
        long square = calcSquare(matrix);
        return List.of(String.valueOf(square));
    }

}
