package com.gitlab.sszuev.tasks.dynamic.fractions;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.algebraic.gcd.RecursiveBinaryGCDAlgorithm;

import java.util.Arrays;
import java.util.List;

/**
 * Created by @ssz on 31.12.2021.
 */
public class SumOfFractionsDynamicAlgorithm implements Algorithm {

    public static long[] sum(long[] fractions) {
        if (fractions.length < 4 || fractions.length % 2 != 0) {
            throw new IllegalArgumentException();
        }
        long a = fractions[0];
        long b = fractions[1];
        long c = fractions[2];
        long d = fractions[3];
        long numerator = a * d + c * b;
        long denominator = b * d;
        long gcd = gcd(numerator, denominator);
        numerator /= gcd;
        denominator /= gcd;
        if (fractions.length == 4) {
            return new long[]{numerator, denominator};
        }
        long[] rest = Arrays.copyOfRange(fractions, 2, fractions.length);
        rest[0] = numerator;
        rest[1] = denominator;
        return sum(rest);
    }

    static long gcd(long a, long b) {
        return RecursiveBinaryGCDAlgorithm.bgc(a, b);
    }

    public static long[] parse(String sumOfFractionsAsString) {
        String[] fractions = sumOfFractionsAsString.split("\\s*\\+\\s*");
        if (fractions.length == 1) {
            throw new IllegalArgumentException("Wrong input: " + sumOfFractionsAsString);
        }
        long[] res = new long[fractions.length * 2];
        for (int i = 0; i < fractions.length; i++) {
            String fraction = fractions[i];
            if (!fraction.matches("^\\s*\\d+\\s*/\\s*\\d+\\s*$")) {
                throw new IllegalArgumentException("Wrong fraction at position " + i + ": '" + fraction + "'");
            }
            long numerator = Long.parseLong(fraction.replaceFirst("^\\s*(\\d+)\\s*/\\s*\\d+\\s*$", "$1"));
            long denominator = Long.parseLong(fraction.replaceFirst("^\\s*\\d+\\s*/\\s*(\\d+)\\s*$", "$1"));
            res[i * 2] = numerator;
            res[i * 2 + 1] = denominator;
        }
        return res;
    }

    public static String format(long numerator, long denominator) {
        return numerator + "/" + denominator;
    }

    @Override
    public List<String> run(String arg, String... other) {
        long[] parts = parse(arg);
        long[] res = sum(parts);
        return List.of(format(res[0], res[1]));
    }

}
