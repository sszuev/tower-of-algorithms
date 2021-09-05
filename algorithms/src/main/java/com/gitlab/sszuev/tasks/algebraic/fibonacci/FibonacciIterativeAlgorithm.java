package com.gitlab.sszuev.tasks.algebraic.fibonacci;

import java.math.BigInteger;

/**
 * Created by @ssz on 17.08.2021.
 */
public class FibonacciIterativeAlgorithm extends BaseFibonacciAlgorithm {

    public static long fibonacci(int n) {
        if (n <= 1) return n;
        checkNumberIsInRange(n);
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

    @Override
    BigInteger calc(long n) {
        return n > MAX_N_FOR_LONG_RETURN ? fibonacci(n) : BigInteger.valueOf(fibonacci((int) n));
    }

}
