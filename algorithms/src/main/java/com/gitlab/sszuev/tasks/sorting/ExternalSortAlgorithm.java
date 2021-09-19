package com.gitlab.sszuev.tasks.sorting;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.sorting.external.CharMergeSortEngine;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by @ssz on 19.09.2021.
 */
public class ExternalSortAlgorithm implements Algorithm {
    private final CharMergeSortEngine engine;

    public ExternalSortAlgorithm(CharSort sort, long buffer) {
        engine = new CharMergeSortEngine(sort, buffer, 8);
    }

    @Override
    public List<String> run(String arg, String... other) {
        return List.of(sort(arg).toString());
    }

    public Path sort(String file) {
        Path res;
        try {
            res = Paths.get(file).toRealPath();
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        try {
            engine.sort(res);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return res;
    }
}
