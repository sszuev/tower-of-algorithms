package com.gitlab.sszuev.tasks.strings;

import java.util.Arrays;

/**
 * This is the Boyer–Moore–Horspool algorithm to search substring (a simplified version of the Boyer–Moore algorithm).
 * It only works for searching ASCII substrings, but can be applied to text that consists of any characters.
 * <p>
 * Created by @ssz on 30.10.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Boyer%E2%80%93Moore%E2%80%93Horspool_algorithm'>wiki: Boyer–Moore–Horspool algorithm</a>
 */
public class BMHSubstringSearchAlgorithm extends BaseSubstringSearchAlgorithm {

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

    private static int indexOf(char[] text, char[] str) {
        int[] shifts = calcASCIIShiftTable(str);
        int t = 0;
        while (t <= text.length - str.length) {
            int p = str.length - 1;
            while (p >= 0 && text[t + p] == str[p]) {
                p--;
            }
            if (p < 0) {
                return t;
            }
            int index = text[t + str.length - 1];
            t += index > shifts.length ? str.length : shifts[index];
        }
        return -1;
    }

    private static int[] calcASCIIShiftTable(char[] str) {
        int[] res = new int[128]; // only ASCII !
        Arrays.fill(res, str.length);
        for (int i = 0; i < str.length - 1; i++) {
            char index = str[i];
            if (index > 127) {
                throw new IllegalArgumentException("Not a ASCII char: " + index + " (code = " + (int) index + ")");
            }
            res[index] = str.length - i - 1;
        }
        return res;
    }

    @Override
    int find(String text, String str) {
        return indexOf(text, str);
    }

}
