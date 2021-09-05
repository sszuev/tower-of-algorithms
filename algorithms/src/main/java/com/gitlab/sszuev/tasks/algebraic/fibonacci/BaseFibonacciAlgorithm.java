package com.gitlab.sszuev.tasks.algebraic.fibonacci;

import com.gitlab.sszuev.tasks.Algorithm;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by @ssz on 18.08.2021.
 */
abstract class BaseFibonacciAlgorithm implements Algorithm {
    /**
     * The maximum number for which the Fibonacci number is still in the {@code long} range
     * (i.e. less than {@link Long#MAX_VALUE}).
     */
    static final int MAX_N_FOR_LONG_RETURN = 92;

    static void checkNumberIsInRange(int n) {
        if (n > MAX_N_FOR_LONG_RETURN) {
            throw new IllegalArgumentException("Not applicable for n > " + MAX_N_FOR_LONG_RETURN);
        }
    }

    abstract BigInteger calc(long n);

    @Override
    public final List<String> run(String arg, String... other) {
        long n = Long.parseLong(arg);
        String res = String.valueOf(calc(n));
        return List.of(res);
    }

}
