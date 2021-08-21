package com.gitlab.sszuev.tasks.algebraic.primes;

import com.gitlab.sszuev.tasks.Algorithm;

import java.util.List;

/**
 * Created by @ssz on 21.08.2021.
 */
public class PrimesOptimizedIterativeAlgorithm implements Algorithm {

    public static long numberOfPrimes(long n) {
        if (n <= 0) throw new IllegalArgumentException();
        if (n == 1) return 0;
        long res = 0;
        for (long i = 2; i <= n; i++) {
            if (isPrime(i)) res++;
        }
        return res;
    }

    public static boolean isPrime(long n) {
        if (n == 2 || n == 3) {
            return true;
        }
        if (n % 2 == 0 || n % 3 == 0) {
            return false;
        }
        long max = (int) Math.sqrt(n);
        for (long i = 5; i <= max; i += 6) {
            if (n % i == 0 || n % (i + 2) == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public List<String> run(String arg, String... other) {
        long n = Long.parseLong(arg);
        long res = numberOfPrimes(n);
        return List.of(String.valueOf(res));
    }
}
