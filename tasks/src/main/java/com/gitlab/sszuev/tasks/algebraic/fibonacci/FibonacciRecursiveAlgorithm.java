package com.gitlab.sszuev.tasks.algebraic.fibonacci;

import java.math.BigInteger;

/**
 * Created by @ssz on 17.08.2021.
 */
public class FibonacciRecursiveAlgorithm extends BaseFibonacciAlgorithm {

    public static BigInteger fibonacci(long n) {
        if (n <= 1) {
            return BigInteger.valueOf(n);
        }
        return fibonacci(n - 1).add(fibonacci(n - 2));
    }

    public static long fibonacci(int n) {
        if (n <= 1) {
            return n;
        }
        checkNumberIsInRange(n);
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    @Override
    BigInteger calc(long n) {
        return n > MAX_N_FOR_LONG_RETURN ? fibonacci(n) : BigInteger.valueOf(fibonacci((int) n));
    }

}
