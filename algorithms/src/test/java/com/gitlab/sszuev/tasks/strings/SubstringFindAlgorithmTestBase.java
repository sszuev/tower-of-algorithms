package com.gitlab.sszuev.tasks.strings;

import com.gitlab.sszuev.tasks.IterativeRunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 05.12.2021.
 */
abstract class SubstringFindAlgorithmTestBase extends IterativeRunTestEngine {

    public static Stream<Data> listData() throws Exception {
        return listData("/substring-search");
    }
}
