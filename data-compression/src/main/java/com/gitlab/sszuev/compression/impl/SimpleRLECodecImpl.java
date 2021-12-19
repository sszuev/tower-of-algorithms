package com.gitlab.sszuev.compression.impl;

import com.gitlab.sszuev.compression.BinaryCodec;

import java.util.Arrays;

/**
 * Run-length encoding (RLE).
 * <p>
 * Created by @ssz on 12.12.2021.
 *
 * @see <a href='https://en.wikipedia.org/wiki/Run-length_encoding'>Run-length encoding</a>
 */
public class SimpleRLECodecImpl implements BinaryCodec {
    private static final int MAX_BYTES_IN_SEQUENCE = 256;

    @Override
    public byte[] encode(byte[] raw) {
        byte[] res = new byte[raw.length * 2];
        int length = 0;
        int series = 1;
        for (int i = 1; ; i++) {
            if (i >= raw.length) {
                res[length++] = (byte) series;
                res[length++] = raw[i - 1];
                break;
            }
            if (series < MAX_BYTES_IN_SEQUENCE - 1 && raw[i] == raw[i - 1]) {
                series++;
            } else {
                res[length++] = (byte) series;
                res[length++] = raw[i - 1];
                series = 1;
            }
        }
        return res.length == length ? res : Arrays.copyOf(res, length);
    }

    @Override
    public byte[] decode(byte[] encoded) {
        if (encoded.length == 0 || encoded.length % 2 != 0) {
            throw new IllegalArgumentException("Wrong array specified");
        }
        int length = 0;
        for (int i = 0; i < encoded.length; i += 2) {
            length += Byte.toUnsignedInt(encoded[i]);
        }
        byte[] res = new byte[length];
        int j = 0;
        for (int i = 0; i < encoded.length; i += 2) {
            int s = Byte.toUnsignedInt(encoded[i]);
            Arrays.fill(res, j, j += s, encoded[i + 1]);
        }
        return res;
    }
}
