package com.gitlab.sszuev.tasks.sorting;

import com.gitlab.sszuev.tasks.Algorithm;

/**
 * Created by @ssz on 19.09.2021.
 */
public class RadixSortAlgorithmTest extends SortAlgorithmTestBase {
    @Override
    public Algorithm getTaskToTest() {
        return new RadixSortAlgorithm();
    }
}
