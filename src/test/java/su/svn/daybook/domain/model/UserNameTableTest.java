package su.svn.daybook.domain.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class UserNameTableTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new UserNameTable());
        Assertions.assertDoesNotThrow(() -> new UserNameTable(
                UUID.randomUUID(), "guest", "password",null, null, false, true, 0
        ));
    }
    @Test
    void testGetters(){
        var entry = new UserNameTable();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::userName);
        Assertions.assertDoesNotThrow(entry::getPassword);
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
        EqualsVerifier.forClass(UserNameTable.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("createTime", "updateTime", "hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testToString() {
        var entry = new UserNameTable();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(UserNameTable.builder()
                .id(UUID.randomUUID())
                .userName("root")
                .password("password")
                .createTime(null)
                .updateTime(null)
                .enabled(false)
                .visible(true)
                .flags(0)
                .build()));
    }
}