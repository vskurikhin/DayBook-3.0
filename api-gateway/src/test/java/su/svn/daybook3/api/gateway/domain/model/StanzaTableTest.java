/*
 * This file was last modified at 2024-05-14 20:56 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * StanzaTableTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StanzaTableTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new StanzaTable(
                null, StanzaTable.NONE, null, 0L, null, null, null, true, true, 0
        ));
    }
    @Test
    void testGetters(){
        var entry = StanzaTable.builder().build();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::name);
        Assertions.assertDoesNotThrow(entry::description);
        Assertions.assertDoesNotThrow(entry::parentId);
        Assertions.assertDoesNotThrow(entry::userName);
        Assertions.assertDoesNotThrow(entry::createTime);
        Assertions.assertDoesNotThrow(entry::updateTime);
        Assertions.assertDoesNotThrow(entry::enabled);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(StanzaTable.class).verify();
    }

    @Test
    void testToString() {
        var entry = StanzaTable.builder().build();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> {
            var test1 = StanzaTable.builder()
                    .id(null)
                    .name(StanzaTable.NONE)
                    .description(null)
                    .parentId(0L)
                    .userName(null)
                    .createTime(null)
                    .updateTime(null)
                    .enabled(true)
                    .visible(true)
                    .flags(0)
                    .build();
            Assertions.assertNotNull(test1);
            var test2 = test1.toBuilder().build();
            Assertions.assertNotNull(test2);
        });
    }
}