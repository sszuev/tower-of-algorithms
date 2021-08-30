package com.gitlab.sszuev.tasks.algebraic.power;

/**
 * Created by @ssz on 15.08.2021.
 */
public class NaturalPowerFastAlgorithm extends BasePowerAlgorithm {

    public static double pow(double a, long p) {
        double an = a;
        double res = 1;
        for (long i = p; i > 0; i /= 2) {
            if (i % 2 != 0) {
                res *= an;
            }
            if (i > 1) {
                an *= an;
            }
        }
        return res;
    }

    @Override
    double calcPower(double base, long power) {
        return pow(base, power);
    }
}
