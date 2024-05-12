/*
 * This file was last modified at 2024-05-14 20:43 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.models.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static su.svn.daybook3.api.gateway.models.domain.Word.NONE;

class WordTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new Word());
        Assertions.assertDoesNotThrow(() -> new Word(
                NONE, true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = new Word();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::word);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(Word.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testToString() {
        var entry = new Word();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(Word.builder()
                .id(NONE)
                .word(NONE)
                .visible(true)
                .flags(0)
                .build()));
        Assertions.assertDoesNotThrow(() -> new Word().toBuilder().build());
    }
}