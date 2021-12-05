package com.gitlab.sszuev.tasks.strings;

import java.util.Arrays;

/**
 * This is the Boyer–Moore algorithm to search substring.
 * This concrete implementation is copy-pasted from wiki.
 * <p>
 * Created by @ssz on 31.10.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Boyer%E2%80%93Moore_string-search_algorithm'>wiki:Boyer-Moore string-search algorithm</a>
 */
public class WikiBMSubstringFindOneAlgorithm extends BaseSubstringFindOneAlgorithm {

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

    public static int indexOf(char[] text, char[] str) {
        int[] charTable = makeCharTable(str);
        int[] offsetTable = makeOffsetTable(str);
        int t = str.length - 1;
        while (t < text.length) {
            int p = str.length - 1;
            while (text[t] == str[p]) {
                if (p == 0) {
                    return t;
                }
                t--;
                p--;
            }
            int shift = Math.max(offsetTable[str.length - 1 - p], charTable[text[t]]);
            t += shift;
        }
        return -1;
    }

    /**
     * Makes the jump table based on the mismatched character information.
     *
     * @param string {@code Array} of {@code char}s
     * @return a {@code Array} of {@code int}s
     */
    private static int[] makeCharTable(char[] string) {
        int[] res = new int[Character.MAX_VALUE + 1]; // 65536
        Arrays.fill(res, string.length);
        for (int i = 0; i < string.length - 1; ++i) {
            res[string[i]] = string.length - 1 - i;
        }
        return res;
    }

    /**
     * Makes the jump table based on the scan offset which mismatch occurs.
     *
     * @param string {@code Array} of {@code char}s
     * @return a {@code Array} of {@code int}s
     */
    private static int[] makeOffsetTable(char[] string) {
        int[] res = new int[string.length];
        int lastPrefixPosition = string.length;
        for (int i = string.length; i > 0; i--) {
            if (isPrefix(string, i)) {
                lastPrefixPosition = i;
            }
            res[string.length - i] = lastPrefixPosition - i + string.length;
        }
        for (int i = 0; i < string.length - 1; i++) {
            int suffix = suffixLength(string, i);
            res[suffix] = string.length - 1 - i + suffix;
        }
        return res;
    }

    private static boolean isPrefix(char[] string, int p) {
        for (int i = p, j = 0; i < string.length; i++, j++) {
            if (string[i] != string[j]) {
                return false;
            }
        }
        return true;
    }

    private static int suffixLength(char[] string, int p) {
        int len = 0;
        int i = p;
        int j = string.length - 1;
        while (i >= 0 && string[i] == string[j]) {
            ++len;
            --i;
            --j;
        }
        return len;
    }

    @Override
    int find(String text, String str) {
        return indexOf(text, str);
    }
}
