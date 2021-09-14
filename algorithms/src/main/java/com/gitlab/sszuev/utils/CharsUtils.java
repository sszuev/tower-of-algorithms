package com.gitlab.sszuev.utils;

import java.util.Arrays;
import java.util.PrimitiveIterator;
import java.util.Random;
import java.util.stream.IntStream;

/**
 * Created by @ssz on 11.09.2021.
 */
public class CharsUtils {

    public static char[] toSorted(char[] array) {
        char[] res = Arrays.copyOf(array, array.length);
        Arrays.sort(res);
        return res;
    }

    public static char[] generateSortedArray(Random random, int bound, int length) {
        checkAndCast(bound);
        return toCharArray(IntStream.generate(() -> random.nextInt(bound)).limit(length).sorted(), length);
    }

    public static char[] toCharArray(IntStream stream, int length) {
        char[] res = new char[length];
        PrimitiveIterator.OfInt it = stream.iterator();
        int index = 0;
        while (it.hasNext() && index < length) {
            res[index++] = checkAndCast(it.nextInt());
        }
        return res;
    }

    public static char[] concat(char[] left, char[] right) {
        char[] res = new char[left.length + right.length];
        System.arraycopy(left, 0, res, 0, left.length);
        System.arraycopy(right, 0, res, left.length, right.length);
        return res;
    }

    public static char checkAndCast(int value) {
        if (value > Character.MAX_VALUE) {
            throw new IllegalArgumentException();
        }
        return (char) value;
    }

    public static String toString(char[] array) {
        if (array == null) {
            return "null";
        }
        int max = array.length - 1;
        if (max == -1) {
            return "[]";
        }
        StringBuilder res = new StringBuilder();
        res.append('[');
        for (int i = 0; ; i++) {
            res.append((int) array[i]);
            if (i == max)
                return res.append(']').toString();
            res.append(", ");
        }
    }
}
