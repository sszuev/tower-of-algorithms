package com.gitlab.sszuev.tasks.algebraic.fibonacci;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.RunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 17.08.2021.
 */
public class FibonacciRecursiveAlgorithmTest extends RunTestEngine {

    public static Stream<Data> listData() throws Exception {
        // limit for with 7 tests: too long calculation for n ~> 100
        return listData("/algebraic/fibonacci").filter(x -> x.id() < 7);
    }

    @Override
    public Algorithm getTaskToTest() {
        return new FibonacciRecursiveAlgorithm();
    }
}
