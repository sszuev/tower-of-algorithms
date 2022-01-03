package com.gitlab.sszuev.tasks.dynamic.shed;

import com.gitlab.sszuev.tasks.RunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 03.01.2022.
 */
abstract class ShedAlgorithmTestBase extends RunTestEngine {
    public static Stream<Data> listData() throws Exception {
        return listData("/shed");
    }

    @Override
    public abstract BaseShedAlgorithm getTaskToTest();
}
