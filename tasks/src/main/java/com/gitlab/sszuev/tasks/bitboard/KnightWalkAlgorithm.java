package com.gitlab.sszuev.tasks.bitboard;

import com.gitlab.sszuev.tasks.Algorithm;

import java.util.List;

/**
 * The chess knight decides to take a walk on a deserted chessboard.
 * Currently, he is in the specified chessboard cell. Where can he go?
 * Need to find the number of knight's possible moves
 * and the number (as a long bit-mask) with the set bits of those fields where it can step into.
 * There is no one else on the chessboard except the knight,
 * the knight moves one square horizontally or vertically,
 * and then two squares perpendicular to the original direction.
 * <p>
 * Created by @ssz on 12.08.2021.
 */
public class KnightWalkAlgorithm implements Algorithm {

    /**
     * Calculates the mask for knight walk position.
     *
     * @param p - the number of position (see {@code Bitboard Calculator, Layout 2})
     * @return {@code long} - bit-mask
     */
    public static long calcMask(int p) {
        long k = BitUtils.getBitMask(p);
        long kh1 = k & BitUtils.EVERYTHING_WITHOUT_COLUMN_H;
        long kh2 = k & BitUtils.EVERYTHING_WITHOUT_TWO_LAST_COLUMNS;

        long ka1 = k & BitUtils.EVERYTHING_WITHOUT_COLUMN_A;
        long ka2 = k & BitUtils.EVERYTHING_WITHOUT_TWO_FIRST_COLUMNS;
        return kh2 << 10 | kh1 << 17 | kh2 >>> 6 | kh1 >>> 15 | ka2 >>> 10 | ka1 >>> 17 | ka2 << 6 | ka1 << 15;
    }

    @Override
    public List<String> run(String arg, String... other) {
        int position = Integer.parseInt(arg);
        long mask = calcMask(position);
        int number = BitUtils.count(mask);
        return List.of(String.valueOf(number), Long.toUnsignedString(mask));
    }
}
