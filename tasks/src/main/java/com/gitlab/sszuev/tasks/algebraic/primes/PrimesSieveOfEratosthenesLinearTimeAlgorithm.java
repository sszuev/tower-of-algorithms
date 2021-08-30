package com.gitlab.sszuev.tasks.algebraic.primes;

import com.gitlab.sszuev.tasks.Algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * Sieve of Eratosthenes with {@code O(N)} time-complexity algorithm:
 * <pre>{@code
 * 1: for i := 2, 3, 4, ..., until n:
 * 2:   if  lp[i] = 0:
 * 3:       lp[i] := i
 * 4:       pr[] += {i}
 * 5:   for p in pr while p ≤ lp[i] and p * i ≤ n:
 * 6:       lp[p * i] := p
 * Result:
 *  lp - an array with minimum prime divisors for each number up to n
 *  pr - an array with all primes up to n
 *  }</pre>
 * Created by @ssz on 21.08.2021.
 *
 * @see <a href='https://ecommons.cornell.edu/bitstream/handle/1813/6407/77-313.pdf'>A linear sieve algorithm for finding prime numbers</a>
 */
public class PrimesSieveOfEratosthenesLinearTimeAlgorithm implements Algorithm {

    public static int numberOfPrimes(int n) {
        return SieveOfEratosthenesEngine.calculateFor(n).pr.size();
    }

    @Override
    public List<String> run(String arg, String... other) {
        int n = Integer.parseInt(arg);
        int res = numberOfPrimes(n);
        return List.of(String.valueOf(res));
    }

    /**
     * A Sieve of Eratosthenes calculator.
     */
    public static class SieveOfEratosthenesEngine {
        final int n;
        final List<Integer> pr;
        final int[] lp;

        private SieveOfEratosthenesEngine(int n) {
            if (n <= 0) {
                throw new IllegalArgumentException("Wrong n = " + n);
            }
            this.n = n;
            this.pr = new ArrayList<>();
            this.lp = new int[n + 1];
        }

        public static SieveOfEratosthenesEngine calculateFor(int n) {
            SieveOfEratosthenesEngine res = new SieveOfEratosthenesEngine(n);
            res.fill();
            return res;
        }

        private void fill() {
            for (int i = 2; i <= n; i++) {
                if (lp[i] == 0) {
                    lp[i] = i;
                    pr.add(i);
                }
                int j = 0;
                int p;
                while (j < pr.size() && (p = pr.get(j++)) <= lp[i] && p * i <= n) {
                    lp[p * i] = p;
                }
            }
        }
    }
}
