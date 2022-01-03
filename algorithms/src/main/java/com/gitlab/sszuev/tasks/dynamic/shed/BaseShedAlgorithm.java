package com.gitlab.sszuev.tasks.dynamic.shed;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.utils.BitMatrix;

import java.util.List;

/**
 * Created by @ssz on 03.01.2022.
 */
abstract class BaseShedAlgorithm implements Algorithm {

    abstract long calculate(BitMatrix area);

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
        long square = calculate(matrix);
        return List.of(String.valueOf(square));
    }
}
