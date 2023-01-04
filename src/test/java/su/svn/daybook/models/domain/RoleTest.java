package su.svn.daybook.models.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static su.svn.daybook.models.domain.Role.NONE;

class RoleTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new Role());
        Assertions.assertDoesNotThrow(() -> new Role(
                null, NONE, null, true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = new Role();
        Assertions.assertDoesNotThrow(entry::getId);
        Assertions.assertDoesNotThrow(entry::getRole);
        Assertions.assertDoesNotThrow(entry::getDescription);
        Assertions.assertDoesNotThrow(entry::getVisible);
        Assertions.assertDoesNotThrow(entry::isVisible);
        Assertions.assertDoesNotThrow(entry::getFlags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(Role.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testToString() {
        var entry = new Role();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(Role.builder()
                .id(null)
                .role(NONE)
                .description(null)
                .visible(true)
                .flags(0)
                .build()));
    }
}