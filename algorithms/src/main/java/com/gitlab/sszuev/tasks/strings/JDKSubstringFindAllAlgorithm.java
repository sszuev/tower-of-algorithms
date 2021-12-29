package com.gitlab.sszuev.tasks.strings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by @ssz on 05.12.2021.
 */
public class JDKSubstringFindAllAlgorithm extends BaseSubstringFindAllAlgorithm {

    @Override
    List<Integer> find(String text, String str) {
        return findAll(text, str);
    }

    public static List<Integer> findAll(String text, String str) {
        int textSize = text.length();
        int strSize = str.length();
        if (strSize == 0 || textSize < strSize) {
            return List.of();
        }
        ArrayList<Integer> res = new ArrayList<>(toSize(textSize, strSize));
        for (int i = 0; i < textSize; ) {
            int j = text.indexOf(str, i);
            if (j == -1) {
                break;
            }
            res.add(j);
            i = j + strSize;
        }
        res.trimToSize();
        return res;
    }

    private static int toSize(int x, int y) {
        return x / y + x % y;
    }
}
