package com.gitlab.sszuev.tasks.algebraic.fibonacci;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.RunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 17.08.2021.
 */
public class FibonacciIterativeAlgorithmTest extends RunTestEngine {

    public static Stream<Data> listData() throws Exception {
        // for 12 (n = 10_000_000) calculation is too heavy, can't wait
        // for 11 it takes about 20s - still too long
        return listData("/algebraic/fibonacci", 11);
    }

    @Override
    public Algorithm getTaskToTest() {
        return new FibonacciIterativeAlgorithm();
    }
}
