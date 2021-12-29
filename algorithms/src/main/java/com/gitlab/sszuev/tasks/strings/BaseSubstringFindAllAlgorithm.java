package com.gitlab.sszuev.tasks.strings;

import com.gitlab.sszuev.tasks.Algorithm;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by @ssz on 04.12.2021.
 */
abstract class BaseSubstringFindAllAlgorithm implements Algorithm {

    /**
     * Returns a {@code List} of all indices within the {@code text} of the occurrence of {@code str}.
     * No overlaps are allowed.
     * For example for text {@code 'aaaaaaaaaaaa'} and substring {@code 'aa'}
     * the method returns {@code  [0, 2, 4, 6, 8, 10]}.
     *
     * @param text the {@code String} to search in, not {@code null}
     * @param str  the substring to search for, not {@code null}
     * @return a {@code List} of {@link Integer}s
     */
    abstract List<Integer> find(String text, String str);

    @Override
    public final List<String> run(String arg, String... other) {
        return find(arg, other[0]).stream().map(String::valueOf).collect(Collectors.toList());
    }
}
