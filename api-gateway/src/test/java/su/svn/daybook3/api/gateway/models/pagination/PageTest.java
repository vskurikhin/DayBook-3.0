/*
 * This file was last modified at 2024-05-14 20:56 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * PageTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.models.pagination;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class PageTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new Page<>());
        Assertions.assertDoesNotThrow(() -> new Page<>(
                0L, 0L, (short) 0, 0, 0L, false, false, Collections.emptyList()
        ));
    }

    @Test
    void testGetters() {
        var entry = new Page<>();
        Assertions.assertDoesNotThrow(entry::getFirst);
        Assertions.assertDoesNotThrow(entry::getContent);
        Assertions.assertDoesNotThrow(entry::getPage);
        Assertions.assertDoesNotThrow(entry::getRows);
        Assertions.assertDoesNotThrow(entry::getTotalRecords);
        Assertions.assertDoesNotThrow(entry::getTotalPages);
        Assertions.assertDoesNotThrow(entry::isNextPage);
        Assertions.assertDoesNotThrow(entry::isPrevPage);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(Page.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testToString() {
        var entry = new Page<>();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(Page.builder()
                .content(Collections.emptyList())
                .first(0)
                .page(0)
                .rows((short) 0)
                .nextPage(false)
                .prevPage(false)
                .totalRecords(0)
                .totalPages(0L)
                .build()));
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(new Page<>().toBuilder().build()));
    }
}
