package com.gitlab.sszuev.tasks.algebraic.power;

import com.gitlab.sszuev.tasks.Algorithm;

import java.util.List;

/**
 * Created by @ssz on 15.08.2021.
 */
abstract class BasePowerAlgorithm implements Algorithm {

    abstract double calcPower(double base, long power);

    @Override
    public final List<String> run(String arg, String... other) {
        double base = Double.parseDouble(arg);
        long power = Long.parseLong(other[0]);
        double res = calcPower(base, power);
        return List.of(String.valueOf(res));
    }
}
