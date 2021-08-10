package com.gitlab.sszuev.tasks;

import java.util.List;

/**
 * Describes an algorithm abstraction.
 * Created by @ssz on 07.08.2021.
 */
public interface Algorithm {

    /**
     * Runs this algorithm.
     * @param arg {@code String} first argument
     * @param other {@code Array} of rest arguments
     * @return {@code String} result of algorithm calculation
     */
    String run(String arg, String... other);

    default String run(List<String> args) {
        return run(args.get(0), args.subList(1, args.size()).toArray(String[]::new));
    }

    default String name() {
        return getClass().getSimpleName();
    }
}
