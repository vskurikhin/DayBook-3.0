package su.svn.daybook.domain.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class @Name@TableTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new @Name@Table());
        Assertions.assertDoesNotThrow(() -> new @Name@Table(
                null, @KType@.ZERO, null, null, null, null, true, true, 0
        ));
    }
    @Test
    void testGetters(){
        var entry = new @Name@Table();
        Assertions.assertDoesNotThrow(entry::getId);
        Assertions.assertDoesNotThrow(entry::get@Key@);
        Assertions.assertDoesNotThrow(entry::get@Value@);
        Assertions.assertDoesNotThrow(entry::getUserName);
        Assertions.assertDoesNotThrow(entry::getCreateTime);
        Assertions.assertDoesNotThrow(entry::getUpdateTime);
        Assertions.assertDoesNotThrow(entry::getEnabled);
        Assertions.assertDoesNotThrow(entry::isEnabled);
        Assertions.assertDoesNotThrow(entry::getVisible);
        Assertions.assertDoesNotThrow(entry::isVisible);
        Assertions.assertDoesNotThrow(entry::getFlags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(@Name@Table.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("createTime", "updateTime", "hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testToString() {
        var entry = new @Name@Table();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(@Name@Table.builder()
                .id(null)
                .@key@(@KType@.ZERO)
                .@value@(null)
                .userName(null)
                .createTime(null)
                .updateTime(null)
                .enabled(true)
                .visible(true)
                .flags(0)
                .build()));
    }
}