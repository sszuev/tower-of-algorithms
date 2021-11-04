package com.gitlab.sszuev.tasks.strings;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.RunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 04.11.2021.
 */
public class NaiveSubstringSearchAlgorithmTest extends RunTestEngine {

    public static Stream<Data> listData() throws Exception {
        return listData("/substring-search");
    }

    @Override
    public Algorithm getTaskToTest() {
        return new NaiveSubstringSearchAlgorithm();
    }
}
