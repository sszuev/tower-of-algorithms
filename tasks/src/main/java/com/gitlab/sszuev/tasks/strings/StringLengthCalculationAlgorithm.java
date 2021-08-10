package com.gitlab.sszuev.tasks.strings;

import com.gitlab.sszuev.tasks.Algorithm;

import java.util.Random;

/**
 * A dummy algorithm to test system.
 * <p>
 * Created by @ssz on 07.08.2021.
 */
public class StringLengthCalculationAlgorithm implements Algorithm {
    private static final Random RANDOM = new Random();

    @Override
    public String run(String arg, String... other) {
        delay();
        return String.valueOf(arg.length());
    }

    private void delay() {
        try {
            Thread.sleep(RANDOM.nextInt(42));
        } catch (InterruptedException ignore) {
            // ignore
        }
    }
}
