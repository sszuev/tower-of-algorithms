package com.gitlab.sszuev.tasks.bitboard;

/**
 * The chess rook decides to take a walk on a deserted chessboard.
 * Currently, he is in the specified chessboard cell. Where can he go?
 * Need to find the number of rook's possible moves
 * and the number (as a long bit-mask) with the set bits of those fields where it can step into.
 * Except him there is no one else on the chessboard,
 * the rook moves horizontally or vertically to any number of cells.
 * <p>
 * Created by @ssz on 14.08.2021.
 */
public class RookWalkAlgorithm extends BaseWalkAlgorithm {

    /**
     * Calculates the mask for rock's position.
     *
     * @param p - the number of position (see {@code Bitboard Calculator, Layout 2})
     * @return {@code long} - bit-mask
     */
    public static long calcMask(int p) {
        long k = BitUtils.getBitMask(p);
        return (calcVertical(k) | calcHorizontal(k)) ^ k;
    }

    private static long calcVertical(long k) {
        for (int i = 0; i < 8; i++) {
            if ((BitUtils.TOP_BORDER & k) != 0) {
                break;
            }
            k |= k << 8;
        }
        for (int i = 0; i < 8; i++) {
            if ((BitUtils.BOTTOM_BORDER & k) != 0) {
                break;
            }
            k |= k >>> 8;
        }
        return k;
    }

    private static long calcHorizontal(long k) {
        for (int i = 0; i < 8; i++) {
            if ((BitUtils.RIGHT_BORDER & k) != 0) {
                break;
            }
            k |= k << 1;
        }
        for (int i = 0; i < 8; i++) {
            if ((BitUtils.LEFT_BORDER & k) != 0) {
                break;
            }
            k |= k >>> 1;
        }
        return k;
    }

    @Override
    long calculateWalkMask(int p) {
        return calcMask(p);
    }
}
