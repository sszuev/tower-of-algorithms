package com.gitlab.sszuev.tasks.bitboard;

import com.gitlab.sszuev.tasks.Algorithm;

import java.util.List;

/**
 * Created by @ssz on 12.08.2021.
 */
abstract class BaseWalkAlgorithm implements Algorithm {

    abstract long calculateWalkMask(int p);

    @Override
    public final List<String> run(String arg, String... other) {
        int position = Integer.parseInt(arg);
        long mask = calculateWalkMask(position);
        int number = BitUtils.count(mask);
        return List.of(String.valueOf(number), Long.toUnsignedString(mask));
    }
}
