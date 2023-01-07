package su.svn.daybook.models.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static su.svn.daybook.models.domain.UserHasRoles.NONE;

class UserHasRolesTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new UserHasRoles());
        Assertions.assertDoesNotThrow(() -> new UserHasRoles(
                null, NONE, null, true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = new UserHasRoles();
        Assertions.assertDoesNotThrow(entry::getId);
        Assertions.assertDoesNotThrow(entry::getUserName);
        Assertions.assertDoesNotThrow(entry::getRole);
        Assertions.assertDoesNotThrow(entry::getVisible);
        Assertions.assertDoesNotThrow(entry::isVisible);
        Assertions.assertDoesNotThrow(entry::getFlags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(UserHasRoles.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testToString() {
        var entry = new UserHasRoles();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(UserHasRoles.builder()
                .id(null)
                .userName(NONE)
                .role(null)
                .visible(true)
                .flags(0)
                .build()));
    }
}