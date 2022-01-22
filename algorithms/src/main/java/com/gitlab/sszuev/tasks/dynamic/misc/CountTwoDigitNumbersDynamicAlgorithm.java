package com.gitlab.sszuev.tasks.dynamic.misc;

import com.gitlab.sszuev.tasks.Algorithm;

import java.util.List;

/**
 * The problem:
 * Given a number {@code N}.
 * Find out how many {@code N}-digit numbers can be made, using the numbers {@code 5} and {@code 8},
 * in which three of the same number do not stand next to each other?
 * For example, if {@code N = 3}, the answer is {@code 6}.
 * These six numbers are: {@code 558, 585, 588, 855, 858, 885}
 * <p>
 * Created by @ssz on 01.01.2022.
 *
 * @see <a href='https://www.youtube.com/watch?v=NOq7yuAwAFY'>ru-youtoube: 5x8</a>
 */
public class CountTwoDigitNumbersDynamicAlgorithm implements Algorithm {

    public static long count(long n) {
        long x5 = 1;
        long x55 = 0;
        long x8 = 1;
        long x88 = 0;

        long _x5, _x55, _x8, _x88;
        for (long i = 2; i <= n; i++) {
            _x5 = x8 + x88;
            _x8 = x5 + x55;
            _x55 = x5;
            _x88 = x8;
            x5 = _x5;
            x8 = _x8;
            x55 = _x55;
            x88 = _x88;
        }
        return x5 + x55 + x8 + x88;
    }

    @Override
    public List<String> run(String arg, String... other) {
        return List.of(String.valueOf(count(Long.parseLong(arg))));
    }
}
