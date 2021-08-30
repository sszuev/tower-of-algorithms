package com.gitlab.sszuev.tasks.algebraic.power;

/**
 * Created by @ssz on 15.08.2021.
 */
public class NaturalPowerOptimizedIterativeAlgorithm extends BasePowerAlgorithm {

    public static double pow(final double a, final long p) {
        if (p == 0) {
            return 1;
        }
        if (p == 1) {
            return a;
        }
        long minPower = findNearestPowerOfTwoThatLessThanGiven(p);
        double res = a;

        long t = minPower;
        do {
            res *= res;
        } while ((t = t >> 1) != 1);

        for (long i = minPower; i < p; i++) {
            res *= a;
        }
        return res;
    }

    private static long findNearestPowerOfTwoThatLessThanGiven(long p) {
        p |= p >> 1; // first two digits are 1
        p |= p >> 2; // first 4 digits are 1
        p |= p >> 4; // first 8 digits are 1
        p |= p >> 8;  // first 16 digits are 1
        p |= p >> 16; // first 32 digits are 1
        p |= p >> 32; // all digits are 1
        return p ^ (p >> 1); // remove all but leading digit
    }

    @Override
    double calcPower(double base, long power) {
        return pow(base, power);
    }
}
