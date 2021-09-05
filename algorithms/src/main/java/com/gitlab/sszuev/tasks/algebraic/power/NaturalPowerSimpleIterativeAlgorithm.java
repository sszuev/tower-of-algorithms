package com.gitlab.sszuev.tasks.algebraic.power;

/**
 * Created by @ssz on 14.08.2021.
 */
public class NaturalPowerSimpleIterativeAlgorithm extends BasePowerAlgorithm {

    public static double pow(double a, long p) {
        double res = 1;
        for (long i = 0; i < p; i++) {
            res *= a;
        }
        return res;
    }

    @Override
    double calcPower(double base, long power) {
        return pow(base, power);
    }

}
