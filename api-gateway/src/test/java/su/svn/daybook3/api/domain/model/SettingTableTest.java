/*
 * This file was last modified at 2024-05-14 20:56 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SettingTableTest.java
 * $Id$
 */

package su.svn.daybook3.api.domain.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SettingTableTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new SettingTable(
                null, SettingTable.NONE, null, 0L, 0L, null, null, null, true, true, true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = SettingTable.builder().build();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::variable);
        Assertions.assertDoesNotThrow(entry::value);
        Assertions.assertDoesNotThrow(entry::valueTypeId);
        Assertions.assertDoesNotThrow(entry::stanzaId);
        Assertions.assertDoesNotThrow(entry::userName);
        Assertions.assertDoesNotThrow(entry::createTime);
        Assertions.assertDoesNotThrow(entry::updateTime);
        Assertions.assertDoesNotThrow(entry::enabled);
        Assertions.assertDoesNotThrow(entry::localChange);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(SettingTable.class).verify();
    }

    @Test
    void testToString() {
        var entry = SettingTable.builder().build();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(SettingTable.builder()
                .id(null)
                .variable(SettingTable.NONE)
                .value(null)
                .valueTypeId(0L)
                .stanzaId(0)
                .userName(null)
                .createTime(null)
                .updateTime(null)
                .enabled(true)
                .visible(true)
                .flags(0)
                .build()));
    }
}