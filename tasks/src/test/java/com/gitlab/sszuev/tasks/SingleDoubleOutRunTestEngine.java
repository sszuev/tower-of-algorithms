package com.gitlab.sszuev.tasks;

import org.junit.jupiter.api.Assertions;

import java.util.List;

/**
 * Created by @ssz on 15.08.2021.
 */
public abstract class SingleDoubleOutRunTestEngine extends RunTestEngine {
    private static final double DELTA = .0000001;

    protected double delta() {
        return DELTA;
    }

    @Override
    protected boolean isEquals(List<String> expected, List<String> actual) {
        return Math.abs(extractDouble(expected) - extractDouble(actual)) < delta();
    }

    @Override
    protected void assertEquals(List<String> expected, List<String> actual) {
        Assertions.assertEquals(extractDouble(expected), extractDouble(actual), delta());
    }

    private double extractDouble(List<String> data) {
        if (data.size() != 1) {
            throw new IllegalArgumentException();
        }
        return Double.parseDouble(data.get(0));
    }
}
