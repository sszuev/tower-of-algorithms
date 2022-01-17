package com.gitlab.sszuev.tasks.algebraic.gcd;

import com.gitlab.sszuev.tasks.Algorithm;

import java.util.List;

/**
 * Binary Greatest Common Division algorithm (recursive impl).
 * <p>
 * Created by @ssz on 31.12.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Binary_GCD_algorithm'>wiki: Binary GCD algorithm</a>
 */
public class RecursiveBinaryGCDAlgorithm implements Algorithm {

    public static long bgc(long a, long b) {
        if (a == 0) {
            return b;
        }
        if (b == 0 || a == b) {
            return a;
        }
        if (isEven(a)) {
            if (isEven(b)) {
                return bgc(a >> 1, b >> 1) << 1;
            } else {
                return bgc(a >> 1, b);
            }
        } else {
            if (isEven(b)) {
                return bgc(a, b >> 1);
            }
        }
        return a > b ? bgc((a - b) >> 1, b) : bgc(a, (b - a) >> 1);
    }

    public static boolean isEven(long n) {
        return (n & 1) == 0;
    }

    @Override
    public List<String> run(String arg, String... other) {
        long a = Long.parseLong(arg);
        long b = Long.parseLong(other[0]);
        return List.of(String.valueOf(bgc(a, b)));
    }
}
