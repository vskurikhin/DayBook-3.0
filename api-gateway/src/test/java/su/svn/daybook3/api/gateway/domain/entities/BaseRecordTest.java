package su.svn.daybook3.api.gateway.domain.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

class BaseRecordTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new BaseRecord());
        Assertions.assertDoesNotThrow(() -> new BaseRecord(
                null, null, null, null, null, true, true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = BaseRecord.builder().build();
        Assertions.assertDoesNotThrow(() -> entry.id());
        Assertions.assertDoesNotThrow(() -> entry.type());
        Assertions.assertDoesNotThrow(() -> entry.userName());
        Assertions.assertDoesNotThrow(() -> entry.createTime());
        Assertions.assertDoesNotThrow(() -> entry.updateTime());
        Assertions.assertDoesNotThrow(() -> entry.enabled());
        Assertions.assertDoesNotThrow(() -> entry.visible());
        Assertions.assertDoesNotThrow(() -> entry.flags());
    }

    @Test
    void testSetters() {
        var entry = BaseRecord.builder().build();
        Assertions.assertDoesNotThrow(() -> entry.id(UUID.randomUUID()));
        Assertions.assertDoesNotThrow(() -> entry.type(RecordType.Base));
        Assertions.assertDoesNotThrow(() -> entry.userName("test"));
        Assertions.assertDoesNotThrow(() -> entry.createTime(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(() -> entry.updateTime(LocalDateTime.now()));
        Assertions.assertDoesNotThrow(() -> entry.enabled(true));
        Assertions.assertDoesNotThrow(() -> entry.visible(true));
        Assertions.assertDoesNotThrow(() -> entry.flags(0));
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(BaseRecord.class).verify();
    }

    @Test
    void testToString() {
        var entry = BaseRecord.builder().build();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(BaseRecord.builder()
                .id(null)
                .type(RecordType.Base)
                .userName(null)
                .createTime(null)
                .updateTime(null)
                .enabled(true)
                .visible(true)
                .flags(0)
                .build()));
    }
}