package com.gitlab.sszuev.tasks.dynamic.shed;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.RunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 03.01.2022.
 */
public class SmallShedDynamicAlgorithmTest extends RunTestEngine {
    public static Stream<Data> listData() throws Exception {
        return listData("/shed");
    }
    @Override
    public Algorithm getTaskToTest() {
        return new SmallShedDynamicAlgorithm();
    }
}
