package com.gitlab.sszuev.tasks.strings;

import com.gitlab.sszuev.tasks.IterativeRunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 04.11.2021.
 */
abstract class SubstringSearchAlgorithmTestBase extends IterativeRunTestEngine  {

    public static Stream<Data> listData() throws Exception {
        return listData("/substring-search");
    }

    @Override
    public long getIterationNumber() {
        return 100_000;
    }
}
