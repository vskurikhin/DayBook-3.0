package su.svn.daybook.models.pagination;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageCodec;

public final class PageRequestCodec implements MessageCodec<PageRequest, PageRequest> {

    private final Class<PageRequest> aClass = PageRequest.class;

    @Override
    public void encodeToWire(Buffer buffer, PageRequest pageRequest) {

    }

    @Override
    public PageRequest decodeFromWire(int pos, Buffer buffer) {
        return null;
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
}
