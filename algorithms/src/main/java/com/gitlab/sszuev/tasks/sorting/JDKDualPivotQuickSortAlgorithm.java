package com.gitlab.sszuev.tasks.sorting;

import java.util.Arrays;

/**
 * A quick sort from JDK.
 * Created by @ssz on 05.09.2021.
 */
public class JDKDualPivotQuickSortAlgorithm extends BaseIntegerSortAlgorithm {
    @Override
    public void sort(int[] array) {
        Arrays.sort(array);
    }
}
