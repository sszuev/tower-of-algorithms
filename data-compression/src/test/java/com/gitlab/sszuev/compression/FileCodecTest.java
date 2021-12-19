package com.gitlab.sszuev.compression;

import com.gitlab.sszuev.compression.impl.JDKZipCodecImpl;
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
    public void testCompressDecompress(DataProvider data) throws IOException {
        testCompressDecompress(new JDKZipCodecImpl(), data);
    }

    private void testCompressDecompress(FileCodec codec, DataProvider data) throws IOException {
        Path src = data.path();
        String fileName = src.getFileName().toString();
        String ext = getExtension(fileName);
        Path zip = Files.createTempFile(fileName, ".zip");
        Path res = Files.createTempFile(fileName, ext == null ? "" : "." + ext);

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
