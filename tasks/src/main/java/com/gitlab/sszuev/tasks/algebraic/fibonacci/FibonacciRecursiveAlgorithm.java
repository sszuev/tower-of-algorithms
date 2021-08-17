package com.gitlab.sszuev.tasks.algebraic.fibonacci;

import com.gitlab.sszuev.tasks.Algorithm;

import java.util.List;

/**
 * Created by @ssz on 17.08.2021.
 */
public class FibonacciRecursiveAlgorithm implements Algorithm {

    public static long calc(long n) {
        if (n <= 1) return n;
        return calc(n - 1) + calc(n - 2);
    }

    @Override
    public List<String> run(String arg, String... other) {
        long n = Long.parseLong(arg);
        long res = calc(n);
        return List.of(String.valueOf(res));
    }
}
