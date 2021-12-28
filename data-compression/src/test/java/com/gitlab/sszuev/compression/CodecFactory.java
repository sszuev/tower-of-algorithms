package com.gitlab.sszuev.compression;

import com.gitlab.sszuev.compression.impl.EnhancedRLECodecImpl;
import com.gitlab.sszuev.compression.impl.JDKGZipCodecImpl;
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
    JDK_GZIP {
        @Override
        public BinaryCodec createCodec() {
            return new JDKGZipCodecImpl();
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
