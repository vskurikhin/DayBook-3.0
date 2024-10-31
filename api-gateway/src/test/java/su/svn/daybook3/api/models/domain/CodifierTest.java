/*
 * This file was last modified at 2024-05-14 20:56 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CodifierTest.java
 * $Id$
 */

package su.svn.daybook3.api.models.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CodifierTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new Codifier());
        Assertions.assertDoesNotThrow(() -> new Codifier(
                Codifier.NONE, null, true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = new Codifier();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::code);
        Assertions.assertDoesNotThrow(entry::value);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(Codifier.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testHashCode() {
        var test1 = new Codifier(Codifier.NONE, null, true, 0);
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotEquals(0, test1.hashCode()));
    }

    @Test
    void testToString() {
        var entry = new Codifier();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(Codifier.builder()
                .code(Codifier.NONE)
                .value(null)
                .visible(true)
                .flags(0)
                .build()));
    }


    @Test
    void testToBuilder() {
        var test = new Codifier();
        var expected = new Codifier();
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(expected, test.toBuilder().build()));
    }
}