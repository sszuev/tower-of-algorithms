package com.gitlab.sszuev.tasks.bitboard;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.RunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 11.08.2021.
 */
public class KingWalkAlgorithmTest extends RunTestEngine {

    public static Stream<Data> listData() throws Exception {
        return listData("/bitboard/king");
    }

    @Override
    public Algorithm getTaskToTest() {
        return new KingWalkAlgorithm();
    }
}
