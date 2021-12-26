package com.gitlab.sszuev.compression.impl;

import com.gitlab.sszuev.compression.BinaryCodec;

import java.util.Arrays;

/**
 * Run-length encoding (RLE) with obvious improvements
 * (for max sequences of distinct contiguous elements there is no inefficient RLE-compression).
 * <p>
 * Example: {@code #########....#.###.#.#.##..#...######### => +9#+4.-2#.+3#-10.#.#.##..#+3.+9#}
 * <p>
 * Created by @ssz on 22.12.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Run-length_encoding'>Run-length encoding</a>
 */
public class EnhancedRLECodecImpl implements BinaryCodec {
    private static final int MAX_LENGTH_OF_REPEAT_SEQUENCE = Byte.MAX_VALUE;
    private static final int MAX_LENGTH_OF_UNIQUE_SEQUENCE = -Byte.MIN_VALUE;

    private static int maxLength(int length) { // all symbols are unique
        return length % MAX_LENGTH_OF_UNIQUE_SEQUENCE + 1 +
                (length / MAX_LENGTH_OF_UNIQUE_SEQUENCE) * (MAX_LENGTH_OF_UNIQUE_SEQUENCE + 1);
    }

    private static int halve(int length, int max) {
        int n = length / max + 1;
        return length / n;
    }

    @Override
    public byte[] encode(byte[] raw) {
        byte[] res = new byte[maxLength(raw.length)];
        int resLength = encode(raw, raw.length, res);
        return res.length == resLength ? res : Arrays.copyOf(res, resLength);
    }

    /**
     * Encodes the data using advanced RLE algorithm (no compression for sequences of distinct contiguous elements).
     *
     * @param source   the array with original data
     * @param sourceTo the final index of the source array to end encoding,
     *                 exclusive (this index may lie outside the array)
     * @param target   the array with result (encoded) data
     * @return the final index of the target array, exclusive
     */
    protected int encode(byte[] source, int sourceTo, byte[] target) {
        int length = 0;
        int series;
        for (int i = 0; i < sourceTo; i++) {
            int us = uniqueSequence(source, i, sourceTo);
            if (us != i) {
                series = us - i;
                if (series > MAX_LENGTH_OF_UNIQUE_SEQUENCE) {
                    series = halve(series, MAX_LENGTH_OF_UNIQUE_SEQUENCE);
                    us = i + series;
                }
                target[length++] = (byte) (-series);
                System.arraycopy(source, i, target, length, series);
                length += series;
                i = us - 1;
                continue;
            }
            int rs = repeatSequence(source, i, sourceTo);
            if (rs == i) {
                throw new IllegalStateException();
            }
            series = rs - i;
            if (series > MAX_LENGTH_OF_REPEAT_SEQUENCE) {
                series = halve(series, MAX_LENGTH_OF_REPEAT_SEQUENCE);
                rs = i + series;
            }
            target[length++] = (byte) series;
            target[length++] = source[i];
            i = rs - 1;
        }
        return length;
    }

    public static int uniqueSequence(byte[] source, int start, int end) {
        int i = start;
        for (; i < end; i++) {
            if (i > end - 3) {
                i = end;
                break;
            }
            if (source[i] == source[i + 1]) {
                if (i < end - 2 && source[i + 1] != source[i + 2]) {
                    i = i + 1;
                    continue;
                }
                break;
            }
        }
        return i;
    }

    public static int repeatSequence(byte[] source, int start, int end) {
        int i = start;
        for (; i < end; i++) {
            if (i > end - 2) {
                i = end;
                break;
            }
            if (source[i] != source[i + 1]) {
                i = i + 1;
                break;
            }
        }
        if (i == start + 1) {
            return start;
        }
        return i;
    }

    @Override
    public byte[] decode(byte[] encoded) {
        int length = calcDecompressedLength(encoded);
        byte[] res = new byte[length];
        int index = 0;
        for (int i = 0; i < encoded.length; i++) {
            int series = encoded[i];
            if (series == 0) {
                throw new IllegalStateException();
            }
            if (series > 0) {
                Arrays.fill(res, index, index += series, encoded[i + 1]);
                i++;
                continue;
            }
            System.arraycopy(encoded, i + 1, res, index, -series);
            index -= series;
            i -= series;
        }
        return res;
    }

    protected int calcDecompressedLength(byte[] encoded) {
        int length = 0;
        for (int i = 0; i < encoded.length; i++) {
            byte series = encoded[i];
            if (series == 0) {
                throw new IllegalArgumentException("Wrong array specified");
            }
            if (series > 0) {
                length += encoded[i];
                i++;
                continue;
            }
            length -= series;
            i -= series;
        }
        return length;
    }

}
