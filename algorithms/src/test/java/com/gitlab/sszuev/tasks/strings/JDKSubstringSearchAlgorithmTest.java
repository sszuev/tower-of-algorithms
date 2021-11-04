package com.gitlab.sszuev.tasks.strings;

import com.gitlab.sszuev.tasks.Algorithm;

/**
 * Created by @ssz on 30.10.2021.
 */
public class JDKSubstringSearchAlgorithmTest extends SubstringSearchAlgorithmTestBase {

    @Override
    public Algorithm getTaskToTest() {
        return new JDKSubstringSearchAlgorithm();
    }
}
