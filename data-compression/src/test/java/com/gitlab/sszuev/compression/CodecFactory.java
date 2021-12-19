package com.gitlab.sszuev.compression;

import com.gitlab.sszuev.compression.impl.JDKZipCodecImpl;
import com.gitlab.sszuev.compression.impl.SimpleRLECodecImpl;

/**
 * Created by @ssz on 12.12.2021.
 */
enum CodecFactory {
    JDK_ZIP {
        @Override
        public BinaryCodec createCodec() {
            return new JDKZipCodecImpl();
        }
    },
    SIMPLE_RLE {
        @Override
        public BinaryCodec createCodec() {
            return new SimpleRLECodecImpl();
        }
    },
    ;

    public abstract BinaryCodec createCodec();
}
