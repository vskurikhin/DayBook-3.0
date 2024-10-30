package su.svn.daybook3.api.domain.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

class BaseRecordTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> {
            var test1 = new BaseRecord();
            var test2 = new BaseRecord(
                    null, null, null, RecordType.Base, null, null, null, true, false, 0
            );
            Assertions.assertEquals(test1, test2);
        });
    }

    @Test
    void testGetters() {
        var entry = BaseRecord.builder().build();
        Assertions.assertDoesNotThrow(() -> entry.id());
        Assertions.assertDoesNotThrow(() -> entry.parentId());
        Assertions.assertDoesNotThrow(() -> entry.parent());
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
        var id = UUID.randomUUID();
        var entry = BaseRecord.builder().build();
        Assertions.assertDoesNotThrow(() -> entry.id(id));
        Assertions.assertDoesNotThrow(() -> entry.parentId(id));
        Assertions.assertDoesNotThrow(() -> entry.parent(entry));
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
        var base = new BaseRecord();
        base.parent(base);
        base.flags(13);
        EqualsVerifier.forClass(BaseRecord.class)
                .withIgnoredFields("parent")
                .withPrefabValues(BaseRecord.class, base, new BaseRecord())
                .verify();
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
                .parentId(null)
                .parent(null)
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