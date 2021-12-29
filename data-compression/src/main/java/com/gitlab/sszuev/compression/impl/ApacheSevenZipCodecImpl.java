package com.gitlab.sszuev.compression.impl;

import com.gitlab.sszuev.compression.BinaryCodec;
import com.gitlab.sszuev.compression.FileCodec;
import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.archivers.sevenz.SevenZOutputFile;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.LongConsumer;

/**
 * Created by @ssz on 29.12.2021.
 */
public class ApacheSevenZipCodecImpl implements FileCodec, BinaryCodec {
    private static final String ZIP_ENTRY_ID = ApacheSevenZipCodecImpl.class.getName();
    private final LongConsumer listener;

    public ApacheSevenZipCodecImpl() {
        this(null);
    }

    public ApacheSevenZipCodecImpl(LongConsumer listener) {
        this.listener = listener;
    }

    @Override
    public byte[] encode(byte[] raw) {
        ReadableByteChannel src = new SeekableInMemoryByteChannel(raw);
        SeekableInMemoryByteChannel dst = new SeekableInMemoryByteChannel();
        try {
            this.encode(() -> src, () -> dst, ZIP_ENTRY_ID, DEFAULT_BUFFER_SIZE);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return extractData(dst);
    }

    @Override
    public byte[] decode(byte[] encoded) {
        ReadableByteChannel src = new SeekableInMemoryByteChannel(encoded);
        SeekableInMemoryByteChannel dst = new SeekableInMemoryByteChannel();
        try {
            this.decode(() -> src, () -> dst, DEFAULT_BUFFER_SIZE);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return extractData(dst);
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
                       String entryName,
                       int bufferLength) throws IOException {
        try (ReadableByteChannel src = source.open();
             WritableByteChannel dst = target.open();
             SevenZOutputFile zip = createWriteFile(dst)) {
            SevenZArchiveEntry entry = new SevenZArchiveEntry();
            entry.setName(entryName);
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
             SevenZFile zip = createReadFile(src);
             WritableByteChannel dst = target.open()) {
            SevenZArchiveEntry entry = zip.getNextEntry();
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

    public static SevenZOutputFile createWriteFile(WritableByteChannel out) throws IOException {
        if (out instanceof SeekableByteChannel) {
            return new SevenZOutputFile((SeekableByteChannel) out);
        }
        throw new UnsupportedOperationException("Not supported out");
    }

    public static SevenZFile createReadFile(ReadableByteChannel in) throws IOException {
        if (in instanceof SeekableByteChannel) {
            return new SevenZFile((SeekableByteChannel) in);
        }
        throw new UnsupportedOperationException("Not supported in");
    }

    public static byte[] extractData(SeekableInMemoryByteChannel channel) {
        long size = channel.size();
        if (size > Integer.MAX_VALUE) {
            throw new IllegalStateException();
        }
        return Arrays.copyOf(channel.array(), (int) size);
    }

    protected void record(long readLength) {
        if (listener != null) {
            listener.accept(readLength);
        }
    }

}
