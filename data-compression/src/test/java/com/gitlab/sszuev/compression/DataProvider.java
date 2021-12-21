package com.gitlab.sszuev.compression;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.Objects;

/**
 * Created by @ssz on 18.12.2021.
 */
public enum DataProvider {
    TEXT_HTML("/file-01.html"),
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
