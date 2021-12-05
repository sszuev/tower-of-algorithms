package com.gitlab.sszuev.tasks.strings;

/**
 * Created by @ssz on 04.11.2021.
 */
public class NaiveSubstringFindOneAlgorithm extends BaseSubstringFindOneAlgorithm {
    
    public static int indexOf(String text, String str) {
        char[] strChars = str.toCharArray();
        if (strChars.length == 0) {
            return 0;
        }
        char[] textChars = text.toCharArray();
        if (textChars.length < strChars.length) {
            return -1;
        }
        return findFullScan(textChars, strChars);
    }

    private static int findFullScan(char[] text, char[] pattern) {
        for (int t = 0; t <= text.length - pattern.length; t++) {
            int p = 0;
            while (p < pattern.length && text[t + p] == pattern[p]) {
                p++;
            }
            if (p == pattern.length) {
                return t;
            }
        }
        return -1;
    }

    @Override
    int find(String text, String str) {
        return indexOf(text, str);
    }
}
