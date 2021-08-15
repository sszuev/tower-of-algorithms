package com.gitlab.sszuev.tasks.algebraic.power;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.SingleDoubleOutRunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 15.08.2021.
 */
public class NaturalPowerOptimizedIterativeAlgorithmTest extends SingleDoubleOutRunTestEngine {

    public static Stream<Data> listData() throws Exception {
        return listData("/algebraic/power");
    }

    @Override
    public Algorithm getTaskToTest() {
        return new NaturalPowerOptimizedIterativeAlgorithm();
    }
}
