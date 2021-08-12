package com.gitlab.sszuev.tasks.bitboard;

import com.gitlab.sszuev.tasks.Algorithm;

import java.util.List;

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
public class BishopWalkAlgorithm implements Algorithm {

    final static long LEFT_BORDER = 0x101010101010101L;
    final static long RIGHT_BORDER = 0x8080808080808080L;
    final static long TOP_BORDER = 0xff00000000000000L;
    final static long BOTTOM_BORDER = 0xffL;

    final static long TOP_RIGHT_BORDER = TOP_BORDER | RIGHT_BORDER;
    final static long BOTTOM_LEFT_BORDER = BOTTOM_BORDER | LEFT_BORDER;
    final static long TOP_LEFT_BORDER = TOP_BORDER | LEFT_BORDER;
    final static long BOTTOM_RIGHT_BORDER = BOTTOM_BORDER | RIGHT_BORDER;

    /**
     * Calculates the mask for bishop's position.
     *
     * @param p - the number of position (see {@code Bitboard Calculator, Layout 2})
     * @return {@code long} - bit-mask
     */
    public static long calcMask(int p) {
        if (p < 0 || p > 63) {
            throw new IllegalArgumentException("Wrong position: " + p);
        }
        long k = 1L << p;
        return (calcBottomLeftToTopRightDiagonal(k) | calcTopLeftToBottomRightDiagonal(k)) ^ k;
    }

    private static long calcBottomLeftToTopRightDiagonal(long k) {
        for (int i = 1; i < 8; i++) {
            if ((BOTTOM_LEFT_BORDER & k) != 0)
                break;
            k |= k >>> 9; // move to the bottom left cell
        }
        for (int i = 1; i < 8; i++) {
            if ((TOP_RIGHT_BORDER & k) != 0)
                break;
            k |= k << 9; // move to the top right cell
        }
        return k;
    }

    private static long calcTopLeftToBottomRightDiagonal(long k) { // TODO:
        for (int i = 1; i < 8; i++) {
            if ((BOTTOM_RIGHT_BORDER & k) != 0)
                break;
            k |= k >>> 7; // move to the bottom right cell
        }
        for (int i = 1; i < 8; i++) {
            if ((TOP_LEFT_BORDER & k) != 0)
                break;
            k |= k << 7; // move to the top left cell
        }
        return k;
    }

    @Override
    public List<String> run(String arg, String... other) {
        int position = Integer.parseInt(arg);
        long mask = calcMask(position);
        int number = BitUtils.count(mask);
        return List.of(String.valueOf(number), Long.toUnsignedString(mask));
    }
}
