package com.gitlab.sszuev.tasks.strings;

import java.util.ArrayList;
import java.util.List;

/**
 * Knuth-Morris-Pratt algorithm for search all occurrences of substring within the specified text.
 * <pre>{@code
 * function KMP(T, S)
 *   k ← 0
 *   A ← ø
 *   π ← Prefix_Function(S)
 *   for i = 1 to |T| do
 *     while k > 0 and T[i] ≠ S[k + 1] do
 *       k ←  π[k]
 *     end while
 *     if T[i] = S[k + 1] then
 *       k ← k + 1
 *     end if
 *     if k = |S| then
 *       A ← A ⋃ {i - |S| + 1}
 *       A ← A ⋃ {i}
 *       k ← π[k]
 *     end if
 *   end for
 *   return A
 * end function
 * }</pre>
 * <p>
 * Created by @ssz on 05.12.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Knuth%E2%80%93Morris%E2%80%93Pratt_algorithm'>Knuth-Morris-Pratt algorithm</a>
 */
public abstract class BaseKMKSubstringFindAllAlgorithm extends BaseSubstringFindAllAlgorithm {

    @Override
    public final List<Integer> find(String text, String str) {
        return find(text.toCharArray(), str.toCharArray());
    }

    public List<Integer> find(char[] text, char[] str) {
        if (str.length == 0 || text.length < str.length) {
            return List.of();
        }

        ArrayList<Integer> res = new ArrayList<>(toSize(text.length, str.length));
        int[] prefixes = prefixFunction(text);
        int k = 0;
        for (int i = 0; i < text.length; i++) {
            while (k > 0 && str[k] != text[i]) {
                k = prefixes[k - 1];
            }
            if (str[k] == text[i]) {
                k++;
            }
            if (k == str.length) {
                int j = i - str.length + 1;
                if (res.isEmpty() || j >= res.get(res.size() - 1) + str.length) { // to avoid overlaps
                    res.add(j);
                }
                k = prefixes[k - 1];
            }
        }
        res.trimToSize();
        return res;
    }

    protected abstract int[] prefixFunction(char[] str);

    private static int toSize(int x, int y) {
        return x / y + x % y;
    }
}
