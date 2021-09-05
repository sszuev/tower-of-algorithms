package com.gitlab.sszuev.tasks.bitboard;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.RunTestEngine;

import java.util.stream.Stream;

/**
 * Created by @ssz on 12.08.2021.
 */
public class KnightWalkAlgorithmTest extends RunTestEngine {

    public static Stream<Data> listData() throws Exception {
        return listData("/bitboard/knight");
    }

    @Override
    public Algorithm getTaskToTest() {
        return new KnightWalkAlgorithm();
    }
}
