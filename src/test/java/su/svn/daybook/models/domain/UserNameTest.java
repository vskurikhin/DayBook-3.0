package su.svn.daybook.models.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class UserNameTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new UserName());
        Assertions.assertDoesNotThrow(() -> new UserName(
                UUID.randomUUID(), "guest", "password", true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = new UserName();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::getUserName);
        Assertions.assertDoesNotThrow(entry::getPassword);
        Assertions.assertDoesNotThrow(entry::getVisible);
        Assertions.assertDoesNotThrow(entry::isVisible);
        Assertions.assertDoesNotThrow(entry::getFlags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(UserName.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testToString() {
        var entry = new UserName();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(UserName.builder()
                .id(UUID.randomUUID())
                .userName("root")
                .password("password")
                .visible(true)
                .flags(0)
                .build()));
    }
}