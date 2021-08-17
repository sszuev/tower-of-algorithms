package com.gitlab.sszuev.tasks.algebraic.fibonacci;

import com.gitlab.sszuev.tasks.Algorithm;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by @ssz on 17.08.2021.
 */
public class FibonacciIterativeAlgorithm implements Algorithm {

    public static long fibonacci(int n) {
        if (n <= 1) return n;
        if (n > 6) {
            throw new IllegalArgumentException("Not applicable for n > 6");
        }
        long f0 = 0;
        long f1 = 1;
        long res = -1;
        for (int i = 2; i <= n; i++, f0 = f1, f1 = res) {
            res = f0 + f1;
        }
        return res;
    }

    public static BigInteger fibonacci(long n) {
        if (n <= 1) return BigInteger.valueOf(n);
        BigInteger f0 = BigInteger.ZERO;
        BigInteger f1 = BigInteger.ONE;
        BigInteger res = null;
        for (int i = 2; i <= n; i++, f0 = f1, f1 = res) {
            res = f0.add(f1);
        }
        return res;
    }

    BigInteger calc(long n) {
        return n < 7 ? BigInteger.valueOf(fibonacci((int) n)) : fibonacci(n);
    }

    @Override
    public List<String> run(String arg, String... other) {
        long n = Long.parseLong(arg);
        String res = String.valueOf(calc(n));
        return List.of(res);
    }
}
