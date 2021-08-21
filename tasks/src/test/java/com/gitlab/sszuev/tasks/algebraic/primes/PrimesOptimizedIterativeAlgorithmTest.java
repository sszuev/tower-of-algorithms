package com.gitlab.sszuev.tasks.algebraic.primes;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.RunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 21.08.2021.
 */
public class PrimesOptimizedIterativeAlgorithmTest extends RunTestEngine {

    public static Stream<Data> listData() throws Exception {
        // for N = 12 it takes more than 2 min
        return listData("/algebraic/primes").filter(x -> x.id() < 12);
    }

    @Override
    public Algorithm getTaskToTest() {
        return new PrimesOptimizedIterativeAlgorithm();
    }
}

