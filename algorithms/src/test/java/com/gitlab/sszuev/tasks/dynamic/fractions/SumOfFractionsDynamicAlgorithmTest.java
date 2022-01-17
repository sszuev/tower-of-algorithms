package com.gitlab.sszuev.tasks.dynamic.fractions;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.RunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 31.12.2021.
 */
public class SumOfFractionsDynamicAlgorithmTest extends RunTestEngine {
    public static Stream<Data> listData() throws Exception {
        return listData("/fractions");
    }

    @Override
    public Algorithm getTaskToTest() {
        return new SumOfFractionsDynamicAlgorithm();
    }
}
