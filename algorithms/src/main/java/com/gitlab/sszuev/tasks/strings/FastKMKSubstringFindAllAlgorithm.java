package com.gitlab.sszuev.tasks.strings;

/**
 * A Knuth-Morris-Pratt algorithm (searching all occurrences of substring within the specified text)
 * with fast prefix function:
 * <pre>{@code
 * int[] prefixFunction(string s):
 *   p[0] = 0
 *   for i = 1 to s.length - 1
 *       k = p[i - 1]
 *       while k > 0 and s[i] != s[k]
 *           k = p[k - 1]
 *       if s[i] == s[k]
 *           k++
 *       p[i] = k
 *   return p
 * }</pre>
 * Created by @ssz on 05.12.2021.
 */
public class FastKMKSubstringFindAllAlgorithm extends BaseKMKSubstringFindAllAlgorithm {
    @Override
    protected int[] prefixFunction(char[] str) {
        int[] res = new int[str.length];
        for (int i = 1; i < str.length; i++) {
            int j = res[i - 1];
            while (j > 0 && str[j] != str[i]) {
                j = res[j - 1];
            }
            if (str[j] == str[i]) {
                j++;
            }
            res[i] = j;
        }
        return res;
    }
}
