package com.gitlab.sszuev.tasks.bitboard;

/**
 * Created by @ssz on 11.08.2021.
 */
public class BitUtils {
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
}
