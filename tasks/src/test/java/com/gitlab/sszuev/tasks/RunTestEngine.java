package com.gitlab.sszuev.tasks;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A test base.
 * Created by @ssz on 07.08.2021.
 */
public abstract class RunTestEngine {

    public abstract Algorithm getTaskToTest();

    @BeforeAll
    public static void beforeClass() {
        System.out.println("==".repeat(42));
    }

    /**
     * A dummy-method to load test data.
     * It is "overridden" in runtime by concrete method-loader.
     *
     * @return a {@code Stream}
     * @throws Exception something is wrong
     */
    @SuppressWarnings("RedundantThrows")
    public static Stream<Data> listData() throws Exception {
        throw new UnsupportedOperationException();
    }

    @ParameterizedTest
    @MethodSource("listData")
    public void testRunTask(Data data) {
        Algorithm task = getTaskToTest();

        Instant start = Instant.now();
        List<String> actual = task.run(data.given);
        Duration duration = Duration.between(start, Instant.now());

        String msg = formatMessage(task.name(), data.id, isEquals(data.expected, actual), duration);
        System.out.println(msg);
        if (TestPropertiesSupport.USE_ASSERTIONS) {
            assertEquals(data.expected, actual);
        }
    }

    protected boolean isEquals(List<String> expected, List<String> actual) {
        return expected.equals(actual);
    }

    protected void assertEquals(List<String> expected, List<String> actual) {
        Assertions.assertEquals(expected, actual);
    }

    private static String formatMessage(String name, long id, boolean status, Duration duration) {
        return String.format("%s\t#%d:\t\t%s\t%s",
                StringUtils.rightPad(name, 42), id, status ? "  OK" : "FAIL", formatDuration(duration));
    }

    private static String formatDuration(Duration duration) {
        double timeInMs = duration.getSeconds() * 1000. + duration.getNano() / 1_000_000.;
        return timeInMs + "ms";
    }

    /**
     * Loads some data from the resource directory with a limit:
     * all data with {@link Data#id} greater or equal the specified {@code maxDataIdLimit} will be skipped.
     * Note: the method loads all data regardless arguments
     * if there are vm-options {@code -Dtest=TestClassName} and {@code -Dtest-data=fileSystemDirPath}.
     *
     * @param dir            {@code String} name of resource dir
     * @param maxDataIdLimit {@code int} max id exclusively
     * @return a {@code Stream} of {@link Data}s
     * @throws Exception if something is wrong
     */
    public static Stream<Data> listData(String dir, int maxDataIdLimit) throws Exception {
        return listData(dir).filter(x -> x.id < maxDataIdLimit);
    }

    /**
     * Loads data from the directory.
     * Note: the method loads all data regardless arguments
     * if there are vm-options {@code -Dtest=TestClassName} and {@code -Dtest-data=FileSystemDirPath}.
     *
     * @param dir {@code String} name of resource dir
     * @return a {@code Stream} of {@link Data}s
     * @throws Exception if something is wrong
     */
    public static Stream<Data> listData(String dir) throws Exception {
        return listData(dir, x -> true);
    }

    /**
     * Loads data from a directory.
     * It is a generic method.
     * By default, it is the specified subfolder in the test resource directory.
     * But for a particular test (i.e. when there is a property {@code -Dtest=TestClassName} in the vm options),
     * a directory can also be specified in VM options, for this use {@code -Dtest-data=fileSystemDirPath} property.
     * Note that in the latter case, all method parameters are ignored.
     *
     * @param dir    {@code String} name of resource dir
     * @param filter a {@link Predicate} to filter data
     * @return a {@code Stream} of {@link Data}s
     * @throws Exception if something is wrong
     */
    public static Stream<Data> listData(String dir, Predicate<Data> filter) throws Exception {
        Path res = externalDir();
        if (res != null) {
            return loadTestData(res).stream();
        }
        return loadTestData(resourceDir(dir)).stream().filter(filter);
    }

    private static Path resourceDir(String dir) throws URISyntaxException {
        URL url = Objects.requireNonNull(RunTestEngine.class.getResource(dir));
        return Paths.get(url.toURI());
    }

    private static Path externalDir() {
        if (TestPropertiesSupport.isParticularTestSpecified()) {
            return TestPropertiesSupport.getTestDataDir();
        }
        return null;
    }

    /**
     * Loads data from specified path.
     *
     * @param dir {@link Path}
     * @return a {@code List} of {@link Data}
     * @throws IOException if something is wrong
     */
    public static List<Data> loadTestData(Path dir) throws IOException {
        List<Data> res = new ArrayList<>();
        List<Path> files;
        try (Stream<Path> list = Files.list(dir)) {
            files = list.collect(Collectors.toList());
        }
        while (!files.isEmpty()) {
            res.add(extractData(files));
        }
        return res.stream().sorted(Comparator.comparing(x -> x.id)).collect(Collectors.toUnmodifiableList());
    }

    private static Data extractData(List<Path> files) throws IOException {
        Path in = null;
        Path out = null;
        Path first = files.remove(0);
        String firstFilename = first.getFileName().toString();
        if (!firstFilename.matches("^\\w+\\.\\d+\\.\\w+$")) {
            throw new IllegalStateException("Unexpected file " + first);
        }
        String firstExtension = firstFilename.replaceFirst("^\\w+\\.\\d+\\.(\\w+)$", "$1");
        Long id = Long.parseLong(firstFilename.replaceFirst("^\\w+\\.(\\d+)\\.\\w+$", "$1"));
        String secondExtension;
        if ("out".equals(firstExtension)) {
            secondExtension = "in";
            out = first;
        } else if ("in".equals(firstExtension)) {
            secondExtension = "out";
            in = first;
        } else {
            throw new IllegalStateException("Wrong extension: " + first);
        }
        Path second = null;
        for (int i = 0; i < files.size(); i++) {
            Path p = files.get(i);
            String filename = p.getFileName().toString();
            if (filename.endsWith(id + "." + secondExtension)) {
                second = files.remove(i);
                break;
            }
        }
        if (second == null) {
            throw new IllegalStateException("Can't find file-companion for " + first);
        }
        if (out == null) {
            out = second;
        } else {
            in = second;
        }
        List<String> given = readContentAsList(in);
        List<String> expected = readContentAsList(out);
        return new Data(id, given, expected);
    }

    private static List<String> readContentAsList(Path file) throws IOException {
        try (Stream<String> lines = Files.lines(file)) {
            List<String> res = lines.map(String::trim).collect(Collectors.toUnmodifiableList());
            if (res.isEmpty()) {
                throw new IllegalStateException("Wrong content in " + file + ".");
            }
            return res;
        }
    }

    /**
     * A wrapper for test data.
     * It contains data from file {@code *.n.in} (given) and data from file {@code *.n.out} (expected), also test id.
     */
    public static class Data {
        private final long id;
        private final List<String> given;
        private final List<String> expected;

        protected Data(long id, List<String> given, List<String> expected) {
            this.given = given;
            this.expected = expected;
            this.id = id;
        }

        @Override
        public String toString() {
            return String.format("%d::{given='%s', expected='%s'}", id, given, expected);
        }
    }
}
