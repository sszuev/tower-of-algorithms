package com.gitlab.sszuev.tasks.strings;

import com.gitlab.sszuev.tasks.Algorithm;

/**
 * Created by @ssz on 30.10.2021.
 */
public class JDKSubstringFindOneAlgorithmTest extends SubstringFindOneAlgorithmTestBase {

    @Override
    public Algorithm getTaskToTest() {
        return new JDKSubstringFindOneAlgorithm();
    }
}
