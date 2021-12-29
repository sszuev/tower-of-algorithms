package com.gitlab.sszuev.compression.impl;

import com.gitlab.sszuev.compression.BinaryCodec;
import com.gitlab.sszuev.compression.FileCodec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.LongConsumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by @ssz on 11.12.2021.
 */
public class JDKZipCodecImpl implements BinaryCodec, FileCodec {
    private static final String ZIP_ENTRY_ID = JDKZipCodecImpl.class.getName();

    private final LongConsumer listener;

    public JDKZipCodecImpl() {
        this(null);
    }

    public JDKZipCodecImpl(LongConsumer listener) {
        this.listener = listener;
    }

    @Override
    public byte[] encode(byte[] raw) {
        ByteArrayOutputStream res = new ByteArrayOutputStream(raw.length / 2);
        try (ZipOutputStream out = new ZipOutputStream(res)) {
            ZipEntry entry = new ZipEntry(ZIP_ENTRY_ID);
            out.putNextEntry(entry);
            out.write(raw);
        } catch (IOException e) {
            throw new UncheckedIOException("Can't compress", e);
        }
        return res.toByteArray();
    }

    @Override
    public byte[] decode(byte[] encoded) {
        ByteArrayInputStream res = new ByteArrayInputStream(encoded);
        try (ZipInputStream in = new ZipInputStream(res)) {
            if (in.getNextEntry() == null) {
                throw new IllegalArgumentException("Wrong data specified");
            }
            return in.readAllBytes();
        } catch (IOException e) {
            throw new UncheckedIOException("Can't decompress", e);
        }
    }

    @Override
    public void encode(Path source, Path target) throws IOException {
        Objects.requireNonNull(source, "Null source");
        Objects.requireNonNull(target, "Null target");
        encode(() -> FileCodec.newReadChannel(source), () -> FileCodec.newWriteChannel(target),
                source.getFileName().toString(), DEFAULT_BUFFER_SIZE);
    }

    @Override
    public void encode(IOSupplier<? extends ReadableByteChannel> source,
                       IOSupplier<? extends WritableByteChannel> target,
                       int bufferLength) throws IOException {
        encode(source, target, ZIP_ENTRY_ID, bufferLength);
    }

    protected void encode(IOSupplier<? extends ReadableByteChannel> source,
                          IOSupplier<? extends WritableByteChannel> target,
                          String entityName,
                          int bufferLength) throws IOException {
        try (ReadableByteChannel src = source.open();
             WritableByteChannel dst = target.open();
             ZipOutputStream zip = new ZipOutputStream(Channels.newOutputStream(dst))) {
            ZipEntry entry = new ZipEntry(entityName);
            zip.putNextEntry(entry);
            ByteBuffer buffer = ByteBuffer.allocate(bufferLength);
            int readLength;
            while ((readLength = src.read(buffer)) != -1) {
                record(readLength);
                zip.write(buffer.array(), 0, buffer.position());
                buffer.rewind();
            }
            record(readLength);
            zip.closeEntry();
        }
    }

    @Override
    public void decode(IOSupplier<? extends ReadableByteChannel> source,
                       IOSupplier<? extends WritableByteChannel> target,
                       int bufferCapacity) throws IOException {
        try (ReadableByteChannel src = source.open();
             ZipInputStream zip = new ZipInputStream(Channels.newInputStream(src));
             WritableByteChannel dst = target.open()) {
            ZipEntry entry = zip.getNextEntry();
            if (entry == null) {
                throw new IllegalArgumentException("Wrong data specified");
            }
            ByteBuffer buffer = ByteBuffer.allocate(bufferCapacity);
            int readLength;
            int writeLength;
            while ((readLength = zip.read(buffer.array())) > 0) {
                record(readLength);
                buffer.limit(readLength);
                writeLength = dst.write(buffer);
                if (writeLength != readLength) {
                    throw new IllegalStateException();
                }
                buffer.rewind();
            }
            record(readLength);
            zip.closeEntry();
        }
    }

    protected void record(long readLength) {
        if (listener != null) {
            listener.accept(readLength);
        }
    }
}
