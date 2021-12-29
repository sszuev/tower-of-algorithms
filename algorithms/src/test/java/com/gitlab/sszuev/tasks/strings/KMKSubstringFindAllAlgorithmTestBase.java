package com.gitlab.sszuev.tasks.strings;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Created by @ssz on 05.12.2021.
 */
abstract class KMKSubstringFindAllAlgorithmTestBase extends SubstringFindAllAlgorithmTestBase {

    @Override
    public abstract BaseKMKSubstringFindAllAlgorithm getTaskToTest();

    @Disabled("a technical tests (for manual running)")
    @Test
    public void testPrefixFunction() {
        doTestPrefixFunction(getTaskToTest());
    }

    static void doTestPrefixFunction(BaseKMKSubstringFindAllAlgorithm algorithm) {
        String str = "aabaabaaaabaabaaab";
        int[] expected = new int[]{0, 1, 0, 1, 2, 3, 4, 5, 2, 2, 3, 4, 5, 6, 7, 8, 9, 3};
        int[] actual = algorithm.prefixFunction(str.toCharArray());
        Assertions.assertArrayEquals(expected, actual);
    }
}
