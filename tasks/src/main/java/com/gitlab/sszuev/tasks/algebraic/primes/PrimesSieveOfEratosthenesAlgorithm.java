package com.gitlab.sszuev.tasks.algebraic.primes;

import com.gitlab.sszuev.tasks.Algorithm;

import java.util.BitSet;
import java.util.List;

/**
 * Sieve of Eratosthenes with bit-set.
 * <p>
 * Time complexity: {@code O(N*log(log(N)))}.
 * Mem complexity: {@code O(N)} (~ N * 8 byte).
 * <p>
 * Created by @ssz on 21.08.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes'>wiki: Sieve of Eratosthenes</a>
 */
public class PrimesSieveOfEratosthenesAlgorithm implements Algorithm {

    public static int numberOfPrimes(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Wrong n = " + n);
        }
        if (n == 1) {
            return 0;
        }
        BitSet sieve = new BitSet(n);
        int max = (int) Math.sqrt(n);
        int res = 0;
        for (int i = 2; i <= n; i++) {
            if (sieve.get(i)) {
                continue;
            }
            res++;
            if (i > max) {
                continue;
            }
            for (int j = i * i; j <= n; j += i) {
                sieve.set(j);
            }
        }
        return res;
    }

    @Override
    public List<String> run(String arg, String... other) {
        int n = Integer.parseInt(arg);
        int res = numberOfPrimes(n);
        return List.of(String.valueOf(res));
    }
}
