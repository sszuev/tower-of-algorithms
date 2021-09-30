package com.gitlab.sszuev.utils;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;

/**
 * Created by @ssz on 11.09.2021.
 */
public class BufferUtils {

    public static CharBuffer toCharBuffer(ByteBuffer bytes) {
        int limit = bytes.limit();
        int length = (limit % 2 != 0 ? limit + 1 : limit) / 2;
        CharBuffer res = CharBuffer.allocate(length);
        copy(bytes, res);
        res.rewind();
        return res;
    }

    public static void copy(ByteBuffer source, CharBuffer target) {
        source = ByteBuffer.wrap(source.array());
        int limit = source.limit();
        int length = (limit % 2 != 0 ? limit + 1 : limit) / 2;
        int i = 0;
        if (limit % 2 != 0) {
            // treat the first odd one byte item as a char-int:
            target.put(0, (char) Byte.toUnsignedInt(source.get()));
            i++;
        }
        CharBuffer chars = source.asCharBuffer();
        for (; i < length; i++) {
            target.put(i, chars.get());
        }
    }

    public static void copy(CharBuffer source, ByteBuffer target) {
        copy(source, source.limit(), target);
    }

    public static void copy(CharBuffer source, int length, ByteBuffer target) {
        int limit = target.limit();
        int i = 0;
        if (limit % 2 != 0) {
            target.put((byte) source.get(0));
            i++;
        }
        for (; i < length; i++) {
            target.putChar(source.get(i));
        }
    }
}
