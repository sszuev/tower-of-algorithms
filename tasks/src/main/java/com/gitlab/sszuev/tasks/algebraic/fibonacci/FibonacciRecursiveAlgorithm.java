package com.gitlab.sszuev.tasks.algebraic.fibonacci;

import com.gitlab.sszuev.tasks.Algorithm;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by @ssz on 17.08.2021.
 */
public class FibonacciRecursiveAlgorithm implements Algorithm {

    public static BigInteger fibonacci(long n) {
        if (n <= 1) {
            return BigInteger.valueOf(n);
        }
        return fibonacci(n - 1).add(fibonacci(n - 2));
    }

    BigInteger calc(long n) {
        return fibonacci(n);
    }

    @Override
    public List<String> run(String arg, String... other) {
        long n = Long.parseLong(arg);
        String res = String.valueOf(calc(n));
        return List.of(res);
    }
}
