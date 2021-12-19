package com.gitlab.sszuev.compression;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

/**
 * Created by @ssz on 12.12.2021.
 */
public interface FileCodec extends StreamCodec {
    int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * Creates a {@code target}-file,
     * and fills it with compressed data from the given {@code source}-file (a file with raw data).
     *
     * @param source {@link Path} - a fs-link to source file (raw data)
     * @param target {@link Path} - a fs-link to target file (compressed data)
     * @throws IOException if something is wrong
     */
    default void encode(Path source, Path target) throws IOException {
        Objects.requireNonNull(source, "Null source");
        Objects.requireNonNull(target, "Null target");
        encode(() -> newReadChannel(source), () -> newWriteChannel(target), DEFAULT_BUFFER_SIZE);
    }

    /**
     * Creates a {@code target}-file,
     * and fills it with decompressed data from the given {@code source}-file (an archive file).
     *
     * @param source {@link Path} - a fs-link to source file (compressed data)
     * @param target {@link Path} - a fs-link to target file (original data)
     * @throws IOException if something is wrong
     */
    default void decode(Path source, Path target) throws IOException {
        Objects.requireNonNull(source, "Null source");
        Objects.requireNonNull(target, "Null target");
        decode(() -> newReadChannel(source), () -> newWriteChannel(target), DEFAULT_BUFFER_SIZE);
    }

    static ReadableByteChannel newReadChannel(Path file) throws IOException {
        return Files.newByteChannel(file, StandardOpenOption.READ);
    }

    static WritableByteChannel newWriteChannel(Path file) throws IOException {
        return Files.newByteChannel(file, StandardOpenOption.WRITE);
    }
}
