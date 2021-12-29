package com.gitlab.sszuev.tasks.strings;

/**
 * A Knuth-Morris-Pratt algorithm (searching all occurrences of substring within the specified text)
 * with naive prefix function:
 * <pre>{@code
 * int[] prefixFunction(string s):
 *      int[] p = int[s.length]
 *      fill(p, 0)
 *      for i = 0 to s.length - 1
 *          for k = 0 to i - 1
 *              if s[0..k] == s[i - k..i]
 *                  p[i] = k
 *      return p
 * }</pre>
 * Created by @ssz on 05.12.2021.
 */
public class SimpleKMKSubstringFindAllAlgorithm extends BaseKMKSubstringFindAllAlgorithm {
    @Override
    public int[] prefixFunction(char[] str) {
        int[] res = new int[str.length];
        for (int i = 0; i < str.length; i++) {
            for (int j = 1; j <= i; j++) {
                if (endsMatch(str, i + 1, j)) {
                    res[i] = j;
                }
            }
        }
        return res;
    }

    private static boolean endsMatch(char[] array, int lineLength, int prefixLength) {
        for (int i = 0; i < prefixLength; i++) {
            if (array[i] != array[lineLength - prefixLength + i]) {
                return false;
            }
        }
        return true;
    }
}
