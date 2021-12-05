package com.gitlab.sszuev.tasks.strings;

/**
 * Created by @ssz on 30.10.2021.
 *
 * @see String#indexOf(String)
 */
public class JDKSubstringFindOneAlgorithm extends BaseSubstringFindOneAlgorithm {
    @Override
    int find(String text, String str) {
        return text.indexOf(str);
    }
}
