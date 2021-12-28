package com.gitlab.sszuev.compression;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Created by @ssz on 18.12.2021.
 */
@SuppressWarnings("unused")
public enum DataProvider {
    TEXT_HTML("/file-01.html"),
    ARCHIVE_ZIP("/file-02.zip"),
    MEDIA_FLAC("/file-03.flac"),
    PIC_JPEG("/file-04.jpg"),
    MEDIA_WAV("/file-05.wav"),
    TEXT_PDF("/file-06.pdf"),
    PIC_PNG("/file-07.png"),
    TEXT_JS("/file-08.js"),
    TEXT_TXT("/file-09.txt"),
    ARCHIVE_JAR("/file-10.jar"),
    ;
    private final Path path;

    DataProvider(String name) {
        this.path = of(name);
    }

    private static Path of(String file) {
        try {
            return Path.of(Objects.requireNonNull(DataProvider.class.getResource(file)).toURI()).toRealPath();
        } catch (URISyntaxException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    public Path path() {
        return path;
    }
}
