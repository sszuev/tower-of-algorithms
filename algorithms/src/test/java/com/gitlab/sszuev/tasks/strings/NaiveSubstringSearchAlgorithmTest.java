package com.gitlab.sszuev.tasks.strings;

import com.gitlab.sszuev.tasks.Algorithm;

/**
 * Created by @ssz on 04.11.2021.
 */
public class NaiveSubstringSearchAlgorithmTest extends SubstringFindOneAlgorithmTestBase {

    @Override
    public Algorithm getTaskToTest() {
        return new NaiveSubstringSearchAlgorithm();
    }
}
