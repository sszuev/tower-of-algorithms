package com.gitlab.sszuev.tasks.algebraic.fibonacci;

import com.gitlab.sszuev.tasks.Algorithm;
import com.gitlab.sszuev.tasks.RunTestEngine;
import org.junit.jupiter.api.Assertions;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Created by @ssz on 18.08.2021.
 */
public class FibonacciGoldenRationAlgorithmTest extends RunTestEngine {
    private static final int LENGTH_TO_COMPARE = 10;

    public static Stream<Data> listData() throws Exception {
        // for N = 12 (n = 10_000_000) calculation is too heavy, can't wait
        // for N = 11 it takes about 30s - still too long for demonstration
        return listData("/algebraic/fibonacci", 11);
    }

    @Override
    public Algorithm getTaskToTest() {
        return new FibonacciGoldenRationAlgorithm();
    }

    @Override
    protected boolean isEquals(List<String> expected, List<String> actual) {
        String e = expected.get(0);
        String a = actual.get(0);
        if (e.length() <= LENGTH_TO_COMPARE || a.length() <= LENGTH_TO_COMPARE) {
            return Objects.equals(e, a);
        }
        return e.length() == a.length() &&
                Objects.equals(e.substring(0, LENGTH_TO_COMPARE), a.substring(0, LENGTH_TO_COMPARE));
    }

    @Override
    protected void assertEquals(List<String> expected, List<String> actual) {
        String e = expected.get(0);
        String a = actual.get(0);
        if (e.length() <= LENGTH_TO_COMPARE || a.length() <= LENGTH_TO_COMPARE) {
            Assertions.assertEquals(e, a);
            return;
        }
        Assertions.assertEquals(e.length(), a.length());
        Assertions.assertEquals(e.substring(0, LENGTH_TO_COMPARE), a.substring(0, LENGTH_TO_COMPARE));
    }

}
