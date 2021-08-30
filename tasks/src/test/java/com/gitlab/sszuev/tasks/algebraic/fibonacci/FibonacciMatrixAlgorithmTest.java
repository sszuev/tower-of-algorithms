package com.gitlab.sszuev.tasks.algebraic.fibonacci;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.RunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 21.08.2021.
 */
public class FibonacciMatrixAlgorithmTest extends RunTestEngine {

    public static Stream<Data> listData() throws Exception {
        return listData("/algebraic/fibonacci");
    }

    @Override
    public Algorithm getTaskToTest() {
        return new FibonacciMatrixAlgorithm();
    }
}
