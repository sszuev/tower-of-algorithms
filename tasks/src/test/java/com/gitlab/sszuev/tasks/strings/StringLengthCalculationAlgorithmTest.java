package com.gitlab.sszuev.tasks.strings;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.RunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 07.08.2021.
 */
public class StringLengthCalculationAlgorithmTest extends RunTestEngine {

    public static Stream<Data> listData() throws Exception {
        return listData("/strings");
    }

    @Override
    public Algorithm getTaskToTest() {
        return new StringLengthCalculationAlgorithm();
    }

}
