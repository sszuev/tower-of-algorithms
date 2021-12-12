package com.gitlab.sszuev.compression.impl;

import com.gitlab.sszuev.compression.BinaryCodec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * Created by @ssz on 11.12.2021.
 */
public class JDKZipCodecImpl implements BinaryCodec {
    private static final String ZIP_ENTRY_ID = JDKZipCodecImpl.class.getName();

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
            in.getNextEntry();
            return in.readAllBytes();
        } catch (IOException e) {
            throw new UncheckedIOException("Can't decompress", e);
        }
    }
}
