package com.gitlab.sszuev.compression.impl;

import com.gitlab.sszuev.compression.BinaryCodec;
import com.gitlab.sszuev.compression.FileCodec;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Created by @ssz on 28.12.2021.
 */
public class JDKGZipCodecImpl implements BinaryCodec, FileCodec {
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
             OutputStream zip = new GZIPOutputStream(Channels.newOutputStream(dst))) {
            ByteBuffer buffer = ByteBuffer.allocate(bufferLength);
            while (src.read(buffer) != -1) {
                zip.write(buffer.array(), 0, buffer.position());
                buffer.rewind();
            }
        }
    }

    @Override
    public void decode(IOSupplier<? extends ReadableByteChannel> source,
                       IOSupplier<? extends WritableByteChannel> target,
                       int bufferLength) throws IOException {
        try (ReadableByteChannel src = source.open();
             InputStream zip = new GZIPInputStream(Channels.newInputStream(src));
             WritableByteChannel dst = target.open()) {
            ByteBuffer buffer = ByteBuffer.allocate(bufferLength);
            int p1;
            while ((p1 = zip.read(buffer.array())) > 0) {
                buffer.limit(p1);
                int p2 = dst.write(buffer);
                if (p2 != p1) {
                    throw new IllegalStateException();
                }
                buffer.rewind();
            }
        }
    }
}
