package com.gitlab.sszuev.compression;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * Created by @ssz on 12.12.2021.
 */
public interface StreamCodec {

    /**
     * Compresses the given raw data which is given as a {@code ReadableByteChannel}.
     *
     * @param source       an {@link IOSupplier} providing {@link ReadableByteChannel} to read raw data
     * @param target       an {@link IOSupplier} providing {@link WritableByteChannel} to write compressed data
     * @param bufferLength positive {@code int}, buffer size
     * @throws IOException if something is wrong
     */
    void encode(IOSupplier<? extends ReadableByteChannel> source,
                IOSupplier<? extends WritableByteChannel> target,
                int bufferLength) throws IOException;

    /**
     * Decompress the given compressed data (which is given as a {@code ReadableByteChannel}) into the original content.
     *
     * @param source       an {@link IOSupplier} providing {@link ReadableByteChannel} to read compressed data
     * @param target       an {@link IOSupplier} providing {@link WritableByteChannel} to write original data
     * @param bufferLength positive {@code int}, buffer size
     * @throws IOException if something is wrong
     */
    void decode(IOSupplier<? extends ReadableByteChannel> source,
                IOSupplier<? extends WritableByteChannel> target,
                int bufferLength) throws IOException;

    /**
     * Represents an IO-supplier of results.
     *
     * @param <X> - anything
     */
    @FunctionalInterface
    interface IOSupplier<X extends Closeable> {
        X open() throws IOException;
    }
}
