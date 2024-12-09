/*
 * This file was last modified at 2024-10-30 14:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * PageRequestCodec.java
 * $Id$
 */

package su.svn.daybook3.auth.models.pagination;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.impl.SerializableUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public final class PageRequestCodec implements MessageCodec<PageRequest, PageRequest> {

    private final Class<PageRequest> aClass = PageRequest.class;

    @Override
    public void encodeToWire(Buffer buffer, PageRequest pageRequest) {
        byte[] bytes = SerializableUtils.toBytes(pageRequest);
        buffer.appendInt(bytes.length);
        buffer.appendBytes(bytes);
    }

    @Override
    public PageRequest decodeFromWire(int pos, Buffer buffer) {
        int length = buffer.getInt(pos);
        pos += 4;
        byte[] bytes = buffer.getBytes(pos, pos + length);
        return (PageRequest) SerializableUtils.fromBytes(bytes, CheckedClassNameObjectInputStream::new);
    }

    @Override
    public PageRequest transform(PageRequest pageRequest) {
        return pageRequest;
    }

    @Override
    public String name() {
        return aClass.getName() + "Codec";
    }

    @Override
    public byte systemCodecID() {
        return -1;
    }

    private class CheckedClassNameObjectInputStream extends ObjectInputStream {
        CheckedClassNameObjectInputStream(InputStream in) throws IOException {
            super(in);
        }

        @Override
        protected Class<?> resolveClass(ObjectStreamClass desc) {
            return aClass;
        }
    }
}
