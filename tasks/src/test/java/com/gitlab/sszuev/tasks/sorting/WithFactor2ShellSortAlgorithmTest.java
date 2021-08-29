package com.gitlab.sszuev.tasks.sorting;

import com.gitlab.sszuev.tasks.Algorithm;

/**
 * Created by @ssz on 29.08.2021.
 */
public class WithFactor2ShellSortAlgorithmTest extends SortAlgorithmTestBase {
    @Override
    public Algorithm getTaskToTest() {
        return new ShellSortAlgorithm(2);
    }
}
