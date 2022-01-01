package com.gitlab.sszuev.tasks.dynamic.misc;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.RunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 01.01.2022.
 */
public class CountTwoDigitNumbersDynamicAlgorithmTest extends RunTestEngine {
    public static Stream<Data> listData() throws Exception {
        return listData("/two-digit-numbers");
    }

    @Override
    public Algorithm getTaskToTest() {
        return new CountTwoDigitNumbersDynamicAlgorithm();
    }
}
