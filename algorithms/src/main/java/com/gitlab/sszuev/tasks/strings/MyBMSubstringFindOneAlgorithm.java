package com.gitlab.sszuev.tasks.strings;

import java.util.HashMap;
import java.util.Map;

/**
 * A Boyer–Moore algorithm to search substring.
 * <p>
 * Created by @ssz on 31.10.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Boyer%E2%80%93Moore_string-search_algorithm'>wiki:Boyer-Moore string-search algorithm</a>
 */
public class MyBMSubstringFindOneAlgorithm extends BaseSubstringFindOneAlgorithm {

    public static int indexOf(String text, String str) {
        char[] strChars = str.toCharArray();
        if (strChars.length == 0) {
            return 0;
        }
        char[] textChars = text.toCharArray();
        if (textChars.length < strChars.length) {
            return -1;
        }
        return indexOf(textChars, strChars);
    }

    protected static int indexOf(char[] text, char[] str) {
        int[] shifts = calcShiftTable(str);
        Map<Character, Integer> table = calcShiftMap(str);
        int t = 0;
        while (t <= text.length - str.length) {
            int p = str.length - 1;
            while (p >= 0 && text[t + p] == str[p]) {
                p--;
            }
            if (p < 0) {
                return t;
            }
            int shift;
            if (p == str.length - 1) {
                char ch = text[t + str.length - 1];
                shift = table.getOrDefault(ch, str.length);
            } else {
                shift = shifts[p + 1];
            }
            t += shift;
        }
        return -1;
    }

    @Override
    int find(String text, String str) {
        return indexOf(text, str);
    }

    /**
     * Makes the jump table based on the mismatched character information.
     *
     * @param chars {@code Array} of {@code char}s
     * @return a {@code Map} of {@code char}-{@code int} pairs
     */
    protected static Map<Character, Integer> calcShiftMap(char[] chars) {
        Map<Character, Integer> res = new HashMap<>(chars.length);
        for (int i = 0; i < chars.length - 1; ++i) {
            res.put(chars[i], chars.length - 1 - i);
        }
        return res;
    }

    /**
     * Makes the jump table based on the scan offset which mismatch occurs.
     * Examples:
     * <ul>
     * <li>{@code "BC.ABC.BC.C.ABC" => [13, 13, 13, 13, 13, 13, 13, 13, 13, 13, 9, 9, 9, 6, 4]}</li>
     * <li>{@code "KOLOKOL" => [4, 4, 4, 4, 4, 4, 4]}</li>
     * <li>{@code "GCAGAGAG" => [7, 7, 7, 7, 2, 2, 2, 2]}</li>
     * <li>{@code "NNAAMAN" => [6, 6, 6, 6, 6, 6, 5]}</li>
     * <li>{@code "ANAMPNAM" => [8, 8, 8, 8, 8, 4, 4, 4]}</li>
     * <li>{@code "FZZACXZACYYYACDZZZAC" => [20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 15, 11, 6, 6]}</li>
     * </ul>
     *
     * @param chars an {@code Array} of {@code char}s
     * @return an {@code Array} of {@code int}s - shift indexes
     */
    protected static int[] calcShiftTable(char[] chars) {
        int[] res = new int[chars.length];
        int j = chars.length - 1;
        for (int i = chars.length - 2; i >= 0; i--) {
            if (chars[i] == chars[j]) {
                if (!match(chars, j, chars.length, i, i + chars.length - j)) {
                    continue;
                }
                int shift = j - i;
                res[j] = shift;
                --j;
            }
        }
        int k = chars.length - 1;
        int shift = chars.length;
        for (int i = chars.length - j - 1; i >= 0; i--) {
            if (chars[k] == chars[i]) {
                if (match(chars, k, chars.length, 0, chars.length - k)) {
                    shift = k;
                    break;
                }
                --k;
            }
        }
        for (int i = j; i >= 0; i--) {
            res[i] = shift;
        }
        return res;
    }

    protected static boolean match(char[] str, int startLeft, int endLeft, int startRight, int endRight) {
        int length = endLeft - startLeft;
        if (length != endRight - startRight) {
            return false;
        }
        for (int i = 0, j = length - 1; i <= j; i++, j--) {
            if (str[i + startLeft] != str[i + startRight]) {
                return false;
            }
            if (str[j + startLeft] != str[j + startRight]) {
                return false;
            }
        }
        return true;
    }
}
