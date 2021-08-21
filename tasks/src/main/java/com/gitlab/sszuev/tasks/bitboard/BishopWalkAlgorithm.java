package com.gitlab.sszuev.tasks.bitboard;

/**
 * The chess bishop decides to take a walk on a deserted chessboard.
 * Currently, he is in the specified chessboard cell.
 * Where can he go?
 * Need to find the number of bishop's possible moves
 * and the number (as a long bit-mask) with the set bits of those fields where it can step into.
 * Except him there is no one else on the chessboard,
 * the bishop moves diagonally to any number of cells.
 * <p>
 * Created by @ssz on 12.08.2021.
 */
public class BishopWalkAlgorithm extends BaseWalkAlgorithm {

    /**
     * Calculates the mask for bishop's position.
     *
     * @param p - the number of position (see {@code Bitboard Calculator, Layout 2})
     * @return {@code long} - bit-mask
     */
    public static long calcMask(int p) {
        long k = BitUtils.getBitMask(p);
        return (calcBottomLeftToTopRightDiagonal(k) | calcTopLeftToBottomRightDiagonal(k)) ^ k;
    }

    private static long calcBottomLeftToTopRightDiagonal(long k) {
        for (int i = 1; i < 8; i++) {
            if ((BitUtils.BOTTOM_LEFT_BORDER & k) != 0)
                break;
            k |= k >>> 9; // move to the bottom left cell
        }
        for (int i = 1; i < 8; i++) {
            if ((BitUtils.TOP_RIGHT_BORDER & k) != 0)
                break;
            k |= k << 9; // move to the top right cell
        }
        return k;
    }

    private static long calcTopLeftToBottomRightDiagonal(long k) {
        for (int i = 1; i < 8; i++) {
            if ((BitUtils.BOTTOM_RIGHT_BORDER & k) != 0)
                break;
            k |= k >>> 7; // move to the bottom right cell
        }
        for (int i = 1; i < 8; i++) {
            if ((BitUtils.TOP_LEFT_BORDER & k) != 0)
                break;
            k |= k << 7; // move to the top left cell
        }
        return k;
    }

    @Override
    long calculateWalkMask(int p) {
        return calcMask(p);
    }
}
