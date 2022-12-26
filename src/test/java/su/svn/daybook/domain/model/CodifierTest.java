/*
 * This file was last modified at 2021.12.06 19:31 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CodifierTest.java
 * $Id$
 */

package su.svn.daybook.domain.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static su.svn.daybook.domain.model.Codifier.NONE;

class CodifierTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new Codifier());
        Assertions.assertDoesNotThrow(() -> new Codifier(
                NONE, null, null, null, null, false, true, 0
        ));
    }
    @Test
    void testGetters(){
        var entry = new Codifier();
        Assertions.assertDoesNotThrow(entry::getId);
        Assertions.assertDoesNotThrow(entry::getCode);
        Assertions.assertDoesNotThrow(entry::getValue);
        Assertions.assertDoesNotThrow(entry::getUserName);
        Assertions.assertDoesNotThrow(entry::getCreateTime);
        Assertions.assertDoesNotThrow(entry::getUpdateTime);
        Assertions.assertDoesNotThrow(entry::getEnabled);
        Assertions.assertDoesNotThrow(entry::isEnabled);
        Assertions.assertDoesNotThrow(entry::getVisible);
        Assertions.assertDoesNotThrow(entry::isVisible);
        Assertions.assertDoesNotThrow(entry::getFlags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(Codifier.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withIgnoredFields("createTime")
                .withIgnoredFields("updateTime")
                .verify();
    }

    @Test
    void testToString() {
        var entry = new Codifier();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(Codifier.builder()
                .withCode(NONE)
                .withValue(null)
                .withUserName(null)
                .withCreateTime(null)
                .withUpdateTime(null)
                .withEnabled(false)
                .withVisible(true)
                .withFlags(0)
                .build()));
    }
}