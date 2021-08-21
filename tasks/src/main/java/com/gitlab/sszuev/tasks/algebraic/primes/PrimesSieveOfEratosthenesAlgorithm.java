package com.gitlab.sszuev.tasks.algebraic.primes;

import com.gitlab.sszuev.tasks.Algorithm;

import java.util.List;

/**
 * Time complexity: {@code O(N*log(log(N)))}.
 * Mem complexity: {@code O(N)}.
 * <p>
 * Created by @ssz on 21.08.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes'>wiki: Sieve of Eratosthenes</a>
 */
public class PrimesSieveOfEratosthenesAlgorithm implements Algorithm {

    public static long numberOfPrimes(long n) {
        if (n <= 0 || n > Integer.MAX_VALUE - 1) {
            throw new IllegalArgumentException("Wrong n = " + n);
        }
        if (n == 1) {
            return 0;
        }
        boolean[] sieve = new boolean[(int) (n + 1)];
        int max = (int) Math.sqrt(n);
        long res = 0;
        for (int i = 2; i <= n; i++) {
            if (sieve[i]) {
                continue;
            }
            res++;
            if (i > max) {
                continue;
            }
            for (int j = i * i; j <= n; j += i) {
                sieve[j] = true;
            }
        }
        return res;
    }

    @Override
    public List<String> run(String arg, String... other) {
        long n = Long.parseLong(arg);
        long res = numberOfPrimes(n);
        return List.of(String.valueOf(res));
    }
}
