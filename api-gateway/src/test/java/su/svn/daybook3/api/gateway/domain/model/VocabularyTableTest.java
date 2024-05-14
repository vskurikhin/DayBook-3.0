/*
 * This file was last modified at 2024-05-14 20:56 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * VocabularyTableTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class VocabularyTableTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new VocabularyTable(
                null, VocabularyTable.NONE, null, null, null, null, true, true, 0
        ));
    }
    @Test
    void testGetters(){
        var entry = VocabularyTable.builder().build();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::word);
        Assertions.assertDoesNotThrow(entry::value);
        Assertions.assertDoesNotThrow(entry::userName);
        Assertions.assertDoesNotThrow(entry::createTime);
        Assertions.assertDoesNotThrow(entry::updateTime);
        Assertions.assertDoesNotThrow(entry::enabled);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(VocabularyTable.class).verify();
    }

    @Test
    void testToString() {
        var entry = VocabularyTable.builder().build();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(VocabularyTable.builder()
                .id(null)
                .word(VocabularyTable.NONE)
                .value(null)
                .userName(null)
                .createTime(null)
                .updateTime(null)
                .enabled(true)
                .visible(true)
                .flags(0)
                .build()));
    }
}