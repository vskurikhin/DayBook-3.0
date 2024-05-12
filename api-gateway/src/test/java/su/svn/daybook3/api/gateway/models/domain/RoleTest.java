/*
 * This file was last modified at 2024-05-14 20:43 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * RoleTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.models.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static su.svn.daybook3.api.gateway.models.domain.Role.NONE;

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
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::role);
        Assertions.assertDoesNotThrow(entry::description);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
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
        Assertions.assertDoesNotThrow(() -> new Role().toBuilder().build());
    }
}