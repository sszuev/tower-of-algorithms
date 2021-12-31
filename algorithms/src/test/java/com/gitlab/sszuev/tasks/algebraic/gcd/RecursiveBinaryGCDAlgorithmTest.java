package com.gitlab.sszuev.tasks.algebraic.gcd;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.RunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 31.12.2021.
 */
public class RecursiveBinaryGCDAlgorithmTest extends RunTestEngine {

    public static Stream<Data> listData() throws Exception {
        return listData("/algebraic/gcd");
    }

    @Override
    public Algorithm getTaskToTest() {
        return new RecursiveBinaryGCDAlgorithm();
    }
}
