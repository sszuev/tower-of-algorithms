package com.gitlab.sszuev.tasks.strings;

import com.gitlab.sszuev.tasks.Algorithm;

import java.util.List;

/**
 * Created by @ssz on 30.10.2021.
 */
abstract class BaseSubstringFindOneAlgorithm implements Algorithm {

    /**
     * Returns the index within the {@code text} of the first occurrence of the specified substring.
     *
     * <p>The returned index is the smallest value {@code k} for which:
     * <pre>{@code
     * text.startsWith(str, k)
     * }</pre>
     * If no such value of {@code k} exists, then {@code -1} is returned.
     *
     * @param text the {@code String} to search in, not {@code null}
     * @param str  the substring to search for, not {@code null}
     * @return the index of the first occurrence of the specified substring,
     * or {@code -1} if there is no such occurrence
     */
    abstract int find(String text, String str);

    @Override
    public final List<String> run(String arg, String... other) {
        return List.of(String.valueOf(find(arg, other[0])));
    }
}
