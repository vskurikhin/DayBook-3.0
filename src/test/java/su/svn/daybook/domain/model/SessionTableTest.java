package su.svn.daybook.domain.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import su.svn.daybook.TestData;

import java.util.Collections;

class SessionTableTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new SessionTable());
        Assertions.assertDoesNotThrow(() -> new SessionTable(
                null, SessionTable.NONE, Collections.emptySet(), TestData.time.EPOCH_TIME, null, null, true, true, 0
        ));
    }
    @Test
    void testGetters(){
        var entry = new SessionTable();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::userName);
        Assertions.assertDoesNotThrow(entry::getRoles);
        Assertions.assertDoesNotThrow(entry::getValidTime);
        Assertions.assertDoesNotThrow(entry::createTime);
        Assertions.assertDoesNotThrow(entry::updateTime);
        Assertions.assertDoesNotThrow(entry::enabled);
        Assertions.assertDoesNotThrow(entry::isEnabled);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::isVisible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(SessionTable.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("createTime", "updateTime", "hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testToString() {
        var entry = new SessionTable();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(SessionTable.builder()
                .id(null)
                .userName(SessionTable.NONE)
                .roles(Collections.emptySet())
                .validTime(TestData.time.EPOCH_TIME)
                .createTime(null)
                .updateTime(null)
                .enabled(true)
                .visible(true)
                .flags(0)
                .build()));
    }
}