/*
 * This file was last modified at 2024-05-14 20:56 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserNameTest.java
 * $Id$
 */

package su.svn.daybook3.auth.models.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class UserNameTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new UserName(
                UUID.randomUUID(), "guest", "password", true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = new UserName();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::userName);
        Assertions.assertDoesNotThrow(entry::password);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
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
        Assertions.assertDoesNotThrow(() -> new UserName().toBuilder().build());
    }
}