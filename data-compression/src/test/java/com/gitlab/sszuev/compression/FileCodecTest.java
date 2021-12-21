package com.gitlab.sszuev.compression;

import com.gitlab.sszuev.compression.impl.JDKZipCodecImpl;
import com.gitlab.sszuev.compression.impl.SimpleRLECodecImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Created by @ssz on 12.12.2021.
 */
public class FileCodecTest {

    @EnumSource(DataProvider.class)
    @ParameterizedTest
    public void testCompressDecompressWithZipCodec(DataProvider data) throws IOException {
        testCompressDecompress(new JDKZipCodecImpl(), "zip", data);
    }

    @EnumSource(DataProvider.class)
    @ParameterizedTest
    public void testCompressDecompressWithSimpleRLECodec(DataProvider data) throws IOException {
        testCompressDecompress(new SimpleRLECodecImpl(), "srle", data);
    }

    private void testCompressDecompress(FileCodec codec, String name, DataProvider data) throws IOException {
        Path src = data.path();
        String ext = getExtension(src.getFileName().toString());
        String prefix = data.name() + "-";
        Path zip = Files.createTempFile(prefix, "." + name);
        Path res = Files.createTempFile(prefix, ext == null ? "" : "." + ext);

        codec.encode(src, zip);
        debug(src, zip);
        Assertions.assertFalse(isSame(src, zip));

        codec.decode(zip, res);
        debug(zip, res);
        Assertions.assertTrue(isSame(src, res));
    }

    private static boolean isSame(Path left, Path right) throws IOException {
        if (Files.size(left) != Files.size(right)) {
            return false;
        }
        return Arrays.equals(Files.readAllBytes(left), Files.readAllBytes(right));
    }

    private static void debug(Path left, Path right) throws IOException {
        System.out.printf("[%d => %d]%s => %s%n", Files.size(left), Files.size(right), left, right);
    }

    private static String getExtension(String file) {
        String res = file.replaceFirst(".+\\.([^.]+)$", "$1");
        return file.equals(res) ? null : res;
    }

}
