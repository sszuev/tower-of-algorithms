package com.gitlab.sszuev.tasks.strings;

import com.gitlab.sszuev.tasks.Algorithm;

/**
 * Created by @ssz on 05.12.2021.
 */
public class JDKSubstringFindAllAlgorithmTest extends SubstringFindAllAlgorithmTestBase {

    @Override
    public Algorithm getTaskToTest() {
        return new JDKSubstringFindAllAlgorithm();
    }
}
