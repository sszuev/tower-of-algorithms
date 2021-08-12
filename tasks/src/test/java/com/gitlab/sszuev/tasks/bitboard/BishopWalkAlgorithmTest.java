package com.gitlab.sszuev.tasks.bitboard;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.RunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 12.08.2021.
 */
public class BishopWalkAlgorithmTest extends RunTestEngine {

    public static Stream<Data> listData() throws Exception {
        return listData("/bitboard/bishop");
    }

    @Override
    public Algorithm getTaskToTest() {
        return new BishopWalkAlgorithm();
    }
}
