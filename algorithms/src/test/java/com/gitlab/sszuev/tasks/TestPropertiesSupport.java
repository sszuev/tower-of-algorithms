package com.gitlab.sszuev.tasks;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by @ssz on 28.08.2021.
 */
public class TestPropertiesSupport {
    /**
     * Specify VM option '-Duse.assertions=true' to throw {@link AssertionError} if testcase has wrong status.
     * To provide nice-looking console output this option is disabled by default.
     */
    public static final boolean USE_ASSERTIONS = Boolean.parseBoolean(System.getProperty("use.assertions",
            Boolean.FALSE.toString()));

    private static final String BASE = RunTestEngine.class.getPackageName();
    private static final List<String> PACKAGES = Arrays.stream(Package.getPackages())
            .map(Package::getName)
            .filter(x -> x.startsWith(BASE))
            .collect(Collectors.toUnmodifiableList());

    /**
     * Answers a test data directory if it is specified and exists in file system (e.g. `-Dtest-data=/dir`).
     * A dir must contain files
     *
     * @return a {@link Path} or {@code null}
     */
    static Path getTestDataDir() {
        String name = System.getProperty("test-data");
        if (name == null) {
            return null;
        }
        try {
            return Paths.get(name).toRealPath();
        } catch (IOException ignore) {
            return null;
        }
    }

    /**
     * Answers {@code true} if a particular test is specified in options (e.g. `-Dtest=RookWalkAlgorithmTest`).
     *
     * @return {@code boolean}
     */
    static boolean isParticularTestSpecified() {
        String name = System.getProperty("test");
        if (name == null || !name.endsWith("Test") || name.contains("*")) {
            return false;
        }
        if (name.contains(".")) { // absolute path ?
            return classExists(name);
        }
        for (String p : PACKAGES) {
            if (classExists(p + "." + name)) { // simple class name
                return true;
            }
        }
        return false;
    }

    private static boolean classExists(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException ignore) {
            return false;
        }
    }
}
