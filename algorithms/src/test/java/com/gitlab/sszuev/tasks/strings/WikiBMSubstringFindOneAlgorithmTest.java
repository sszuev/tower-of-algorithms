package com.gitlab.sszuev.tasks.strings;

import com.gitlab.sszuev.tasks.Algorithm;

/**
 * Created by @ssz on 31.10.2021.
 */
public class WikiBMSubstringFindOneAlgorithmTest extends SubstringFindOneAlgorithmTestBase {

    @Override
    public Algorithm getTaskToTest() {
        return new WikiBMSubstringFindOneAlgorithm();
    }
}
