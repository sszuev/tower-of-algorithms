package com.gitlab.sszuev.tasks.dynamic.pyramid;

import com.gitlab.sszuev.tasks.Algorithm;

import java.util.Arrays;
import java.util.List;

/**
 * Pyramid, or "Digital Christmas tree" problem.
 * Need to find the "garland" with the maximum sum of numbers.
 * A "garland" is a sequence of numbers that can be obtained by going down from the top trees to its foot,
 * each time moving either left-down or right-down.
 * Example of pyramid:
 * <pre>
 *    1
 *   8 0
 *  6 7 3
 * 9 9 2 3
 * </pre>
 * Here, the maximum length of "garland" is 25: ({@code 1 -> 8 -> 7 -> 9})
 * <p>
 * Created by @ssz on 31.12.2021.
 */
public class PyramidDynamicAlgorithm implements Algorithm {

    public static long calcMaxSum(long[][] pyramid) {
        if (pyramid.length == 0) {
            throw new IllegalArgumentException();
        }
        long[] lastLevel = pyramid[pyramid.length - 1];
        long[] res = Arrays.copyOf(lastLevel, lastLevel.length);
        calcMaxSum(pyramid, pyramid.length - 1, res);
        return res[0];
    }

    private static void calcMaxSum(long[][] pyramid, int level, long[] res) {
        while (level > 0) {
            long[] prevLevel = pyramid[level - 1];
            for (int i = 0; i < prevLevel.length; i++) {
                long x = prevLevel[i];
                res[i] = Math.max(x + res[i], x + res[i + 1]);
            }
            --level;
        }
    }

    public static long[][] parsePyramid(String numberOfLevels, String... levels) {
        int num = Integer.parseInt(numberOfLevels);
        if (levels.length != num) {
            throw new IllegalArgumentException();
        }
        long[][] res = new long[num][];
        for (int i = 0; i < num; i++) {
            String[] digits = levels[i].split("\\s+");
            int length = i + 1;
            if (digits.length != length) {
                throw new IllegalArgumentException();
            }
            res[i] = new long[i + 1];
            for (int j = 0; j < length; j++) {
                String d = digits[j];
                if (!d.matches("\\d+")) {
                    throw new IllegalArgumentException();
                }
                res[i][j] = Long.parseLong(d);
            }
        }
        return res;
    }

    @Override
    public List<String> run(String arg, String... other) {
        long[][] pyramid = parsePyramid(arg, other);
        long res = calcMaxSum(pyramid);
        return List.of(String.valueOf(res));
    }
}
