package com.gitlab.sszuev.tasks.strings;

import java.util.List;

/**
 * Created by @ssz on 04.11.2021.
 */
abstract class SubstringFindOneAlgorithmTestBase extends SubstringFindAlgorithmTestBase {
    @Override
    protected boolean isEquals(List<String> expected, List<String> actual) {
        return super.isEquals(expected.subList(0, 1), actual);
    }

    @Override
    protected void assertEquals(List<String> expected, List<String> actual) {
        super.assertEquals(expected.subList(0, 1), actual);
    }
}
