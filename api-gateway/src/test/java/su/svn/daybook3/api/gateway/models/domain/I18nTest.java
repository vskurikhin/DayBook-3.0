/*
 * This file was last modified at 2024-05-14 20:56 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18nTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.models.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class I18nTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new I18n());
        Assertions.assertDoesNotThrow(() -> new I18n(
                null, Language.NONE, I18n.NONE, null, true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = new I18n();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::language);
        Assertions.assertDoesNotThrow(entry::message);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(I18n.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testHashCode() {
        var test1 = new I18n(null, Language.NONE, I18n.NONE, null, true, 0);
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotEquals(0, test1.hashCode()));
    }

    @Test
    void testToString() {
        var entry = new I18n();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(I18n.builder()
                .id(null)
                .language(Language.NONE)
                .message(I18n.NONE)
                .translation(null)
                .visible(true)
                .flags(0)
                .build()));
    }

    @Test
    void testToBuilder() {
        var test = new I18n();
        var expected = new I18n();
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(expected, test.toBuilder().build()));
    }
}