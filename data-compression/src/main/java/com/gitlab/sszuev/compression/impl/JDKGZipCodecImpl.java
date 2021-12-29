package com.gitlab.sszuev.compression.impl;

import com.gitlab.sszuev.compression.BinaryCodec;
import com.gitlab.sszuev.compression.FileCodec;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.function.LongConsumer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by @ssz on 28.12.2021.
 */
public class JDKGZipCodecImpl implements BinaryCodec, FileCodec {
    private final LongConsumer listener;

    public JDKGZipCodecImpl() {
        this(null);
    }

    public JDKGZipCodecImpl(LongConsumer listener) {
        this.listener = listener;
    }

    @Override
    public byte[] encode(byte[] raw) {
        ByteArrayOutputStream res = new ByteArrayOutputStream(raw.length / 2);
        try (OutputStream out = new GZIPOutputStream(res)) {
            out.write(raw);
        } catch (IOException e) {
            throw new UncheckedIOException("Can't compress", e);
        }
        return res.toByteArray();
    }

    @Override
    public byte[] decode(byte[] encoded) {
        ByteArrayInputStream res = new ByteArrayInputStream(encoded);
        try (InputStream in = new GZIPInputStream(res)) {
            return in.readAllBytes();
        } catch (IOException e) {
            throw new UncheckedIOException("Can't decompress", e);
        }
    }

    @Override
    public void encode(IOSupplier<? extends ReadableByteChannel> source,
                       IOSupplier<? extends WritableByteChannel> target,
                       int bufferLength) throws IOException {
        try (ReadableByteChannel src = source.open();
             WritableByteChannel dst = target.open();
             OutputStream zip = new GZIPOutputStream(Channels.newOutputStream(dst), bufferLength)) {
            ByteBuffer buffer = ByteBuffer.allocate(bufferLength);
            int readLength;
            while ((readLength = src.read(buffer)) > 0) {
                record(readLength);
                zip.write(buffer.array(), 0, buffer.position());
                buffer.rewind();
            }
            record(readLength);
        }
    }

    @Override
    public void decode(IOSupplier<? extends ReadableByteChannel> source,
                       IOSupplier<? extends WritableByteChannel> target,
                       int bufferLength) throws IOException {
        try (ReadableByteChannel src = source.open();
             InputStream zip = new GZIPInputStream(Channels.newInputStream(src), bufferLength);
             WritableByteChannel dst = target.open()) {
            ByteBuffer readBuffer = ByteBuffer.allocate(bufferLength);
            int readLength;
            int writeLength;
            while ((readLength = zip.read(readBuffer.array())) > 0) {
                record(readLength);
                readBuffer.limit(readLength);
                writeLength = dst.write(readBuffer);
                if (writeLength != readLength) {
                    throw new IllegalStateException();
                }
                readBuffer.rewind();
            }
            record(readLength);
        }
    }

    protected void record(long readLength) {
        if (listener != null) {
            listener.accept(readLength);
        }
    }
}
