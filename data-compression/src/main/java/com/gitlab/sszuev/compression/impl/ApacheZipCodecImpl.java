package com.gitlab.sszuev.compression.impl;

import com.gitlab.sszuev.compression.BinaryCodec;
import com.gitlab.sszuev.compression.FileCodec;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.LongConsumer;

/**
 * Created by @ssz on 29.12.2021.
 */
public class ApacheZipCodecImpl implements BinaryCodec, FileCodec {
    private static final String ZIP_ENTRY_ID = ApacheZipCodecImpl.class.getName();

    private final LongConsumer listener;

    public ApacheZipCodecImpl() {
        this(null);
    }

    public ApacheZipCodecImpl(LongConsumer listener) {
        this.listener = listener;
    }

    @Override
    public byte[] encode(byte[] raw) {
        ByteArrayOutputStream res = new ByteArrayOutputStream(raw.length / 2);
        try (ZipArchiveOutputStream out = new ZipArchiveOutputStream(res)) {
            ArchiveEntry entry = new ZipArchiveEntry(ZIP_ENTRY_ID);
            out.putArchiveEntry(entry);
            out.write(raw);
            out.closeArchiveEntry();
        } catch (IOException e) {
            throw new UncheckedIOException("Can't compress", e);
        }
        return res.toByteArray();
    }

    @Override
    public byte[] decode(byte[] encoded) {
        ByteArrayInputStream res = new ByteArrayInputStream(encoded);
        try (ZipArchiveInputStream in = new ZipArchiveInputStream(res)) {
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
        this.encode(() -> FileCodec.newReadChannel(source), () -> FileCodec.newWriteChannel(target),
                source.getFileName().toString(), DEFAULT_BUFFER_SIZE);
    }

    @Override
    public void encode(IOSupplier<? extends ReadableByteChannel> source,
                       IOSupplier<? extends WritableByteChannel> target,
                       int bufferLength) throws IOException {
        this.encode(source, target, ZIP_ENTRY_ID, bufferLength);
    }

    public void encode(IOSupplier<? extends ReadableByteChannel> source,
                       IOSupplier<? extends WritableByteChannel> target,
                       String entityName,
                       int bufferLength) throws IOException {
        try (ReadableByteChannel src = source.open();
             WritableByteChannel dst = target.open();
             ZipArchiveOutputStream zip = newOutputStream(dst)) {
            ArchiveEntry entry = new ZipArchiveEntry(entityName);
            zip.putArchiveEntry(entry);
            ByteBuffer buffer = ByteBuffer.allocate(bufferLength);
            int readLength;
            while ((readLength = src.read(buffer)) != -1) {
                record(readLength);
                zip.write(buffer.array(), 0, buffer.position());
                buffer.rewind();
            }
            record(readLength);
            zip.closeArchiveEntry();
        }
    }

    @Override
    public void decode(IOSupplier<? extends ReadableByteChannel> source,
                       IOSupplier<? extends WritableByteChannel> target,
                       int bufferLength) throws IOException {
        try (ReadableByteChannel src = source.open();
             ZipArchiveInputStream zip = newInputStream(src);
             WritableByteChannel dst = target.open()) {
            ArchiveEntry entry = zip.getNextEntry();
            if (entry == null) {
                throw new IllegalArgumentException("Wrong data specified");
            }
            ByteBuffer buffer = ByteBuffer.allocate(bufferLength);
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
        }
    }

    protected void record(long readLength) {
        if (listener != null) {
            listener.accept(readLength);
        }
    }

    private static ZipArchiveOutputStream newOutputStream(WritableByteChannel dst) throws IOException {
        return dst instanceof SeekableByteChannel ?
                new ZipArchiveOutputStream((SeekableByteChannel) dst) :
                new ZipArchiveOutputStream(Channels.newOutputStream(dst));
    }

    private static ZipArchiveInputStream newInputStream(ReadableByteChannel src) {
        return new ZipArchiveInputStream(Channels.newInputStream(src));
    }

}
