package com.gitlab.sszuev.utils;

import java.util.Arrays;
import java.util.BitSet;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * Created by @ssz on 03.01.2022.
 */
public class BitMatrix {
    private static final byte ONE = 1;
    private static final byte ZERO = 0;
    private final int width;
    private final int height;
    private final BitSet[] bitSets;

    public BitMatrix(int width, int height) {
        if (width < 1) {
            throw new IllegalArgumentException("Wrong width");
        }
        if (height < 1) {
            throw new IllegalArgumentException("Wrong height");
        }
        this.width = width;
        this.height = height;
        this.bitSets = new BitSet[width];
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void set(int i, int j) {
        set(i, j, true);
    }

    public void unset(int i, int j) {
        set(i, j, false);
    }

    public void set(int i, int j, boolean value) {
        checkBounds(i, j);
        BitSet set = bitSets[i];
        if (value) {
            if (set == null) {
                bitSets[i] = (set = new BitSet(height));
            }
            set.set(j);
            return;
        }
        if (set == null) {
            return;
        }
        set.clear(j);
    }

    public boolean get(int i, int j) {
        checkBounds(i, j);
        BitSet set = bitSets[i];
        if (set == null) {
            return false;
        }
        return set.get(j);
    }

    private void checkBounds(int i, int j) {
        if (i >= 0 && i < width && j >= 0 && j < height) {
            return;
        }
        throw new IllegalArgumentException("Wrong [i, j]=[" + i + ", " + j + "]; " +
                "bounds: [" + width + ", " + height + "]");
    }

    public byte[][] toByteMatrix() {
        byte[][] res = new byte[width][];
        for (int i = 0; i < width; i++) {
            res[i] = new byte[height];
            BitSet set = bitSets[i];
            if (set == null) {
                continue;
            }
            for (int j = 0; j < height; j++) {
                res[i][j] = set.get(j) ? ONE : ZERO;
            }
        }
        return res;
    }

    @Override
    public String toString() {
        return Arrays.deepToString(toByteMatrix());
    }

    public String print() {
        return Arrays.stream(toByteMatrix()).map(bytes -> print(bytes, "  ", "#", ".")).collect(Collectors.joining("\n"));
    }

    @SuppressWarnings("SameParameterValue")
    private static String print(byte[] array, String delimiter, String forTrue, String forFalse) {
        StringJoiner res = new StringJoiner(delimiter);
        for (byte b : array) {
            res.add(b == ONE ? forTrue : forFalse);
        }
        return res.toString();
    }
}
