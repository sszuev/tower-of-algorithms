package com.gitlab.sszuev.tasks.bitboard;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.RunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 14.08.2021.
 */
public class RookWalkAlgorithmTest extends RunTestEngine {

    public static Stream<RunTestEngine.Data> listData() throws Exception {
        return listData("/bitboard/rook");
    }

    @Override
    public Algorithm getTaskToTest() {
        return new RookWalkAlgorithm();
    }
}
