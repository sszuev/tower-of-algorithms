package com.gitlab.sszuev.tasks.strings;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.RunTestEngine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Map;
import java.util.stream.Stream;

/**
 * Created by @ssz on 04.11.2021.
 */
public class MyBMSubstringSearchAlgorithmTest extends RunTestEngine {

    public static Stream<Data> listData() throws Exception {
        return listData("/substring-search");
    }

    @Override
    public Algorithm getTaskToTest() {
        return new MyBMSubstringSearchAlgorithm();
    }

    public static Stream<Map.Entry<String, int[]>> calcShiftTableData() {
        return Map.of(
                "BC.ABC.BC.C.ABC", new int[]{13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 9, 9, 9, 6, 4},
                "KOLOKOL", new int[]{4, 4, 4, 4, 4, 4, 4},
                "GCAGAGAG", new int[]{7, 7, 7, 7, 2, 2, 2, 2},
                "NNAAMAN", new int[]{6, 6, 6, 6, 6, 6, 5},
                "ANAMPNAM", new int[]{8, 8, 8, 8, 8, 4, 4, 4},
                "FZZACXZACYYYACDZZZAC", new int[]{20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 15, 11, 6, 6}
        ).entrySet().stream();
    }

    @Disabled("a technical tests (for manual running)")
    @ParameterizedTest
    @MethodSource("calcShiftTableData")
    public void testCalcShiftTableData(Map.Entry<String, int[]> data) {
        int[] actual = MyBMSubstringSearchAlgorithm.calcShiftTable(data.getKey().toCharArray());
        Assertions.assertArrayEquals(data.getValue(), actual);
    }
}
