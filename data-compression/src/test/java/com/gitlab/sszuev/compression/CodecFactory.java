package com.gitlab.sszuev.compression;

import com.gitlab.sszuev.compression.impl.*;

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
    JDK_GZIP {
        @Override
        public BinaryCodec createCodec() {
            return new JDKGZipCodecImpl();
        }
    },
    APACHE_ZIO {
        @Override
        public BinaryCodec createCodec() {
            return new ApacheZipCodecImpl();
        }
    },
    SIMPLE_RLE {
        @Override
        public BinaryCodec createCodec() {
            return new SimpleRLECodecImpl();
        }
    },
    ENHANCED_RLE {
        @Override
        public BinaryCodec createCodec() {
            return new EnhancedRLECodecImpl();
        }
    }
    ;

    public abstract BinaryCodec createCodec();
}
