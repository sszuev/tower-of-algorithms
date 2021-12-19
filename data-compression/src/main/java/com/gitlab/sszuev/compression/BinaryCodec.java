package com.gitlab.sszuev.compression;

/**
 * Represents a lossless data compression codec for binary arrays.
 * <p>
 * Created by @ssz on 11.12.2021.
 */
public interface BinaryCodec {

    /**
     * Compresses the given raw data which is given as a byte array.
     *
     * @param raw {@code Array} of {@code byte}s
     * @return {@code Array} of {@code byte}s
     */
    byte[] encode(byte[] raw);

    /**
     * Restores the original data from the byte array of compressed data.
     *
     * @param encoded {@code Array} of {@code byte}s
     * @return {@code Array} of {@code byte}s
     */
    byte[] decode(byte[] encoded);

}
