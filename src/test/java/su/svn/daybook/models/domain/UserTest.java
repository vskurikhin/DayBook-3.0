package su.svn.daybook.models.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

class UserTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new User(
                UUID.randomUUID(), "guest", "password", Collections.emptySet(), true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = new User();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::userName);
        Assertions.assertDoesNotThrow(entry::password);
        Assertions.assertDoesNotThrow(entry::roles);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(User.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("hash", "hashIsZero", "password")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE, Warning.TRANSIENT_FIELDS)
                .verify();
    }

    @Test
    void testToString() {
        var entry = new User();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(User.builder()
                .id(UUID.randomUUID())
                .userName("root")
                .password("password")
                .roles(Collections.emptySet())
                .visible(true)
                .flags(0)
                .build()));
        Assertions.assertDoesNotThrow(() -> new User().toBuilder().build());
    }
}