package su.svn.daybook.models.pagination;

import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.impl.CodecManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class PageRequestCodecTest {

    PageRequest instance;

    PageRequestCodec codec;

    CodecManager manager;

    @BeforeEach
    void setUp() {
        instance = new PageRequest(1, (short) 2);
        manager = new CodecManager();
        codec = new PageRequestCodec();
        manager.registerDefaultCodec(PageRequest.class, codec);
        manager.serializableCheck(c -> true);
    }

    @Test
    void testTransform() {
        var expected = instance = new PageRequest(1, (short) 2);
        var test = codec.transform(instance);
        Assertions.assertEquals(expected, test);
    }

    @Test
    void testEncodeDecode() {
        var expected = instance = new PageRequest(1, (short) 2);
        var buffer = Buffer.buffer();
        codec.encodeToWire(buffer, instance);
        PageRequest test = (PageRequest) codec.decodeFromWire(0, buffer);
        Assertions.assertEquals(expected, test);
    }
}
