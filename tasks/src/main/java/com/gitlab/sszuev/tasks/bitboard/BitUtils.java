package com.gitlab.sszuev.tasks.bitboard;

/**
 * Created by @ssz on 11.08.2021.
 */
public class BitUtils {

    final static long LEFT_BORDER = 0x101010101010101L;
    final static long RIGHT_BORDER = 0x8080808080808080L;
    final static long TOP_BORDER = 0xff00000000000000L;
    final static long TOP_LEFT_BORDER = TOP_BORDER | LEFT_BORDER;
    final static long TOP_RIGHT_BORDER = TOP_BORDER | RIGHT_BORDER;
    final static long BOTTOM_BORDER = 0xffL;
    final static long BOTTOM_RIGHT_BORDER = BOTTOM_BORDER | RIGHT_BORDER;
    final static long BOTTOM_LEFT_BORDER = BOTTOM_BORDER | LEFT_BORDER;
    final static long EVERYTHING_WITHOUT_COLUMN_A = 0xfefefefefefefefeL;
    final static long EVERYTHING_WITHOUT_COLUMN_H = 0x7f7f7f7f7f7f7f7fL;

    /**
     * Counts number of {@code 1} in bit-representation of the given number.
     *
     * @param bits {@code long}
     * @return {@code int}
     */
    public static int count(long bits) {
        int res = 0;
        while (bits != 0) {
            res++;
            bits &= bits - 1;
        }
        return res;
    }

    /**
     * Gets bit mask for the given position.
     *
     * @param p - the number of position (see {@code Bitboard Calculator, Layout 2})
     * @return a mask as {@code long}
     * @see <a href='https://gekomad.github.io/Cinnamon/BitboardCalculator/'>Bitboard Calculator</a>
     */
    public static long getBitMask(int p) {
        return 1L << checkPosition(p);
    }

    private static int checkPosition(int p) {
        if (p < 0 || p > 63) {
            throw new IllegalArgumentException("Wrong position: " + p);
        }
        return p;
    }
}
