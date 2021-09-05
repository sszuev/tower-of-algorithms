package com.gitlab.sszuev.tasks.bitboard;

/**
 * The chess queen decides to take a walk on a deserted chessboard.
 * Currently, she is in the specified chessboard cell. Where can she go?
 * Need to find the number of queen's possible moves
 * and the number (as a long bit-mask) with the set bits of those fields where she can step into.
 * Except her there is no one else on the chessboard,
 * the queen moves horizontally or vertically or diagonally to any number of cells.
 * <p>
 * Created by @ssz on 14.08.2021.
 */
public class QueenWalkAlgorithm extends BaseWalkAlgorithm {

    /**
     * Calculates the mask for queen's position.
     *
     * @param p - the number of position (see {@code Bitboard Calculator, Layout 2})
     * @return {@code long} - bit-mask
     */
    public static long calcMask(int p) {
        return BishopWalkAlgorithm.calcMask(p) | RookWalkAlgorithm.calcMask(p);
    }

    @Override
    long calculateWalkMask(int p) {
        return calcMask(p);
    }
}
