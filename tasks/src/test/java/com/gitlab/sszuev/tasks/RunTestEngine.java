package com.gitlab.sszuev.tasks;

import com.gitlab.sszuev.tasks.strings.StringLengthCalculationAlgorithmTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
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

    @SuppressWarnings("RedundantThrows")
    public static Stream<Data> listData() throws Exception {
        throw new UnsupportedOperationException();
    }

    @ParameterizedTest
    @MethodSource("listData")
    public void testRunTask(Data data) {
        Algorithm task = getTaskToTest();
        String actual = null;
        Instant start = Instant.now();
        try {
            actual = task.run(data.given);
        } finally {
            Duration duration = Duration.between(start, Instant.now());
            String msg = formatMessage(task.name(), data.id, data.expected.equals(actual), duration);
            System.out.println(msg);
        }
    }

    private static String formatMessage(String name, long id, boolean status, Duration duration) {
        return String.format("%s\t#%d:\t%s\t%s",
                name, id, status ? "  OK" : "FAIL", formatDuration(duration));
    }

    private static String formatDuration(Duration duration) {
        double timeInMs = duration.getSeconds() * 1000. + duration.getNano() / 1_000_000.;
        return timeInMs + "ms";
    }

    public static Stream<Data> listData(String dir) throws Exception {
        URL url = Objects.requireNonNull(StringLengthCalculationAlgorithmTest.class.getResource(dir));
        return loadTestData(Paths.get(url.toURI())).stream();
    }

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
        if (expected.size() != 1) {
            throw new IllegalStateException("Wrong content in " + out);
        }
        return new Data(id, given, expected.get(0));
    }

    private static List<String> readContentAsList(Path file) throws IOException {
        try (Stream<String> lines = Files.lines(file)) {
            return lines.map(String::trim).collect(Collectors.toUnmodifiableList());
        }
    }

    public static class Data {
        private final long id;
        private final List<String> given;
        private final String expected;

        public Data(long id, List<String> given, String expected) {
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
