package com.gitlab.sszuev.tasks.tickets;

import com.gitlab.sszuev.tasks.Algorithm;

import java.util.Collections;
import java.util.List;
import java.util.stream.LongStream;

/**
 * The Task:
 * <p>
 * A ticket with a {@code 2 * N} digit number is considered happy,
 * if the sum of the first {@code N} digits is equal to the sum of the last {@code N} digits.
 * How many lucky {@code 2 * N}-digit tickets exist?
 * <p>
 * Time+mem complexity : {@code O(n^2)}
 * <p>
 * Created by @ssz on 08.08.2021.
 */
public class HappyTicketsDynamicAlgorithm implements Algorithm {

    public static long calcNumberOfHappyTickets(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("Wrong n: " + n);
        }
        return LongStream.of(calcSums(n)).map(x -> x * x).sum();
    }

    private static long[] calcSums(int n) {
        if (n == 1) {
            return LongStream.range(0, 10).map(x -> 1).toArray();
        }
        long[] prev = calcSums(n - 1);
        int prevSize = prev.length;
        int resSize = n * 9 + 1;
        long[] res = new long[resSize];

        for (int i = 0; i < resSize; i++) {
            long sum = 0;
            int start = Math.max(prevSize - i - 1, 0);
            int end = Math.min(prevSize, prevSize - i + 9);
            for (int j = start; j < end; j++) {
                sum += prev[j];
            }
            res[i] = sum;
        }
        return res;
    }

    @Override
    public List<String> run(String arg, String... other) {
        int n = Integer.parseInt(arg);
        return Collections.singletonList(String.valueOf(calcNumberOfHappyTickets(n)));
    }
}
