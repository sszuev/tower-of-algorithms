package com.gitlab.sszuev.tasks.bitboard;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.RunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 14.08.2021.
 */
public class QueenWalkAlgorithmTest extends RunTestEngine {

    public static Stream<Data> listData() throws Exception {
        return listData("/bitboard/queen");
    }

    @Override
    public Algorithm getTaskToTest() {
        return new QueenWalkAlgorithm();
    }
}
