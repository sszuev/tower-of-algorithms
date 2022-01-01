package com.gitlab.sszuev.tasks.dynamic.matrix;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.RunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 01.01.2022.
 */
public class MatrixIslandsDynamicAlgorithmTest extends RunTestEngine {
    public static Stream<Data> listData() throws Exception {
        return listData("/matrix-islands");
    }

    @Override
    public Algorithm getTaskToTest() {
        return new MatrixIslandsDynamicAlgorithm();
    }
}
