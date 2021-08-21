package com.gitlab.sszuev.tasks.algebraic.primes;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.RunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 21.08.2021.
 */
public class PrimesSieveOfEratosthenesLinearTimeAlgorithmTest extends RunTestEngine {

    public static Stream<Data> listData() throws Exception {
        // iteration #13 (N = 1_000_000_000) lasts about ~17s
        return listData("/algebraic/primes");
    }

    @Override
    public Algorithm getTaskToTest() {
        return new PrimesSieveOfEratosthenesLinearTimeAlgorithm();
    }
}
