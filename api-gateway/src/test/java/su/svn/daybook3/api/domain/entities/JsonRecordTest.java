package su.svn.daybook3.api.domain.entities;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.UUID;

class JsonRecordTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> {
            var test1 = new JsonRecord();
            var test2 = new JsonRecord(
                    null, null, null, null, null, null, null, null, null, true, false, 0
            );
            Assertions.assertEquals(test1, test2);
        });
    }

    @Test
    void testGetters() {
        var entry = JsonRecord.builder().build();
        Assertions.assertDoesNotThrow(() -> entry.id());
        Assertions.assertDoesNotThrow(() -> entry.title());
        Assertions.assertDoesNotThrow(() -> entry.values());
        Assertions.assertDoesNotThrow(() -> entry.postAt());
        Assertions.assertDoesNotThrow(() -> entry.refreshAt());
        Assertions.assertDoesNotThrow(() -> entry.baseRecord());
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
        var base = BaseRecord.builder().id(id).parentId(id).build();
        base.parent(base);
        var entry = JsonRecord.builder().build();
        Assertions.assertDoesNotThrow(() -> entry.id(id));
        Assertions.assertDoesNotThrow(() -> entry.baseRecord(base));
        Assertions.assertDoesNotThrow(() -> entry.title("test"));
        Assertions.assertDoesNotThrow(() -> entry.values(Collections.emptyMap()));
        Assertions.assertDoesNotThrow(() -> entry.postAt(OffsetDateTime.now()));
        Assertions.assertDoesNotThrow(() -> entry.refreshAt(OffsetDateTime.now()));
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
        EqualsVerifier.forClass(JsonRecord.class)
                .withIgnoredFields("baseRecord")
                .withPrefabValues(BaseRecord.class, base, new BaseRecord())
                .verify();
    }

    @Test
    void testToString() {
        var entry = JsonRecord.builder().build();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        var id = UUID.randomUUID();
        var base = BaseRecord.builder().id(id).parentId(id).build();
        base.parent(base);
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(JsonRecord.builder()
                .id(null)
                .baseRecord(base)
                .title("")
                .values(Collections.emptyMap())
                .postAt(OffsetDateTime.now())
                .refreshAt(OffsetDateTime.now())
                .userName(null)
                .createTime(null)
                .updateTime(null)
                .enabled(true)
                .visible(true)
                .flags(0)
                .build()));
    }
}