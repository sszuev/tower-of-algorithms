package com.gitlab.sszuev.tasks.algebraic.fibonacci;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Created by @ssz on 18.08.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Fibonacci_number#Computation_by_rounding'>Fibonacci number, Computation by rounding</a>
 */
public class FibonacciGoldenRationAlgorithm extends BaseFibonacciAlgorithm {
    public static final double SQRT_5 = Math.sqrt(5);
    public static final double PHI = (1 + SQRT_5) / 2;

    private static final BigDecimal BIG_SQRT_5 = BigDecimal.valueOf(1 / SQRT_5);
    private static final BigDecimal BIG_PHI = BigDecimal.valueOf(PHI);
    private static final BigDecimal BIG_0_5 = BigDecimal.valueOf(0.5);

    public static long fibonacci(int n) {
        if (n <= 1) return n;
        checkNumberIsInRange(n);
        return (long) (Math.pow(PHI, n) / SQRT_5 + 0.5);
    }

    public static BigInteger fibonacci(long n) {
        if (n <= 1) return BigInteger.valueOf(n);
        if (n >= 999999999) {
            throw new IllegalArgumentException("Not applicable for n >= " + 999999999);
        }
        return BIG_PHI.pow((int) n).multiply(BIG_SQRT_5).add(BIG_0_5).toBigInteger();
    }

    @Override
    BigInteger calc(long n) {
        return n > MAX_N_FOR_LONG_RETURN ? fibonacci(n) : BigInteger.valueOf(fibonacci((int) n));
    }
}
