package com.gitlab.sszuev.tasks.sorting;

import com.gitlab.sszuev.tasks.Algorithm;

/**
 * Created by @ssz on 22.01.2022.
 */
public class WithMax10000CountingSortAlgorithmTest extends SortAlgorithmTestBase {
    @Override
    public Algorithm getTaskToTest() {
        return new CountingSortAlgorithm(10_000);
    }
}
