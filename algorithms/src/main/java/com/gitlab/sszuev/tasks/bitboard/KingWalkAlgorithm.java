package com.gitlab.sszuev.tasks.bitboard;

/**
 * The king decides to take a walk on a deserted chessboard.
 * Currently, he is in the specified chessboard cell.
 * Where can he go?
 * Need to find the number of king's possible moves
 * and the number (as a long bit-mask) with the set bits of those fields where it can step into.
 * There is no one else on the chessboard except the king,
 * the king moves one square in any direction (horizontally/vertically/diagonally).
 * <p>
 * Created by @ssz on 10.08.2021.
 *
 * @see <a href='https://gekomad.github.io/Cinnamon/BitboardCalculator/'>Bitboard Calculator</a>
 */
public class KingWalkAlgorithm extends BaseWalkAlgorithm {

    /**
     * Calculates the mask for king's position.
     *
     * @param p - the number of position (see {@code Bitboard Calculator, Layout 2})
     * @return {@code long} - bit-mask
     */
    public static long calcMask(int p) {
        long k = BitUtils.getBitMask(p);
        long ka = k & BitUtils.EVERYTHING_WITHOUT_COLUMN_A;
        long kh = k & BitUtils.EVERYTHING_WITHOUT_COLUMN_H;
        return ka << 7 | k << 8 | kh << 9 | ka >>> 1 | kh << 1 | ka >>> 9 | k >>> 8 | kh >>> 7;
    }

    @Override
    long calculateWalkMask(int p) {
        return calcMask(p);
    }
}
