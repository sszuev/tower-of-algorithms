package com.gitlab.sszuev.compression;

import com.gitlab.sszuev.compression.impl.*;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by @ssz on 12.12.2021.
 */
public class FileCodecTest {

    private static final Set<Class<? extends FileCodec>> groups = new HashSet<>();

    @TempDir
    static Path tempDir;

    @AfterAll
    public static void afterAll() {
        groups.clear();
        System.out.println(formatLineMessage());
    }

    @EnumSource(value = DataProvider.class)
    @ParameterizedTest
    public void testZipCodec(DataProvider data) throws IOException {
        testCompressDecompress(new JDKZipCodecImpl(), "zip", data);
    }

    @EnumSource(value = DataProvider.class)
    @ParameterizedTest
    public void testGZipCodec(DataProvider data) throws IOException {
        testCompressDecompress(new JDKGZipCodecImpl(), "gzip", data);
    }

    @EnumSource(value = DataProvider.class)
    @ParameterizedTest
    public void testApacheCommonsZipCodec(DataProvider data) throws IOException {
        testCompressDecompress(new ApacheZipCodecImpl(), "azip", data);
    }

    @EnumSource(value = DataProvider.class)
    @ParameterizedTest
    public void testApacheCommonsSevenZipCodec(DataProvider data) throws IOException {
        testCompressDecompress(new ApacheSevenZipCodecImpl(), "7z", data);
    }

    @EnumSource(value = DataProvider.class)
    @ParameterizedTest
    public void testNaiveRLECodec(DataProvider data) throws IOException {
        testCompressDecompress(new SimpleRLECodecImpl(), "srle", data);
    }

    @EnumSource(value = DataProvider.class)
    @ParameterizedTest
    public void testOptimizedRLECodec(DataProvider data) throws IOException {
        testCompressDecompress(new EnhancedRLECodecImpl(), "erle", data);
    }

    private void testCompressDecompress(FileCodec codec, String name, DataProvider data) throws IOException {
        if (groups.add(codec.getClass())) {
            System.out.println(formatLineMessage());
        }
        Path src = data.path();
        long sourceSize = Files.size(src);
        Instant start = Instant.now();
        String ext = getExtension(src.getFileName().toString());
        String prefix = data.name() + "-";
        Path zip = Files.createTempFile(tempDir, prefix, "." + name);
        Path res = Files.createTempFile(tempDir, prefix, ext == null ? "" : "." + ext);

        codec.encode(src, zip);
        Assertions.assertFalse(isSame(src, zip));
        codec.decode(zip, res);
        Assertions.assertTrue(isSame(src, res));
        Instant end = Instant.now();
        long zipSize = Files.size(zip);

        System.out.println(formatMessage(codec, data, sourceSize, zipSize, start, end));
    }

    private static boolean isSame(Path left, Path right) throws IOException {
        if (Files.size(left) != Files.size(right)) {
            return false;
        }
        return Arrays.equals(Files.readAllBytes(left), Files.readAllBytes(right));
    }

    private static String getExtension(String file) {
        String res = file.replaceFirst(".+\\.([^.]+)$", "$1");
        return file.equals(res) ? null : res;
    }

    private static String formatLineMessage() {
        return "===".repeat(42);
    }

    private static String formatMessage(FileCodec codec,
                                        DataProvider data,
                                        long sourceSize,
                                        long zipSize,
                                        Instant start,
                                        Instant end) {
        double ration = ((double) sourceSize) / zipSize;
        Duration duration = Duration.between(start, end);
        return formatMessage(codec.getClass().getSimpleName(), data.name(), sourceSize, zipSize, ration, duration);
    }

    private static String formatMessage(String className,
                                        String fileType,
                                        long rawSizeBytes,
                                        long zipSizeBytes,
                                        double ratio,
                                        Duration duration) {
        return String.format("%s%s%s%s%s%s%s",
                StringUtils.rightPad(className, 42),
                StringUtils.rightPad(fileType, 20),
                StringUtils.rightPad(formatSize(rawSizeBytes), 12),
                StringUtils.rightPad("=>", 5),
                StringUtils.rightPad(formatSize(zipSizeBytes), 12),
                StringUtils.rightPad(String.format("(%f)", ratio), 12),
                StringUtils.leftPad(formatDuration(duration), 12));
    }

    private static String formatDuration(Duration duration) {
        double timeInMs = duration.getSeconds() * 1000. + duration.getNano() / 1_000_000.;
        return timeInMs + "ms";
    }

    private static String formatSize(long bytes) {
        return bytes / 1024 + "kB";
    }

}
