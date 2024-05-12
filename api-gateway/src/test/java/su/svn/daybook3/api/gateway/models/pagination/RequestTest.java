/*
 * This file was last modified at 2024-05-14 20:43 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * RequestTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.models.pagination;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class PageRequestTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new PageRequest());
        Assertions.assertDoesNotThrow(() -> new PageRequest(0L, (short) 0));
    }

    @Test
    void testGetters() {
        var entry = new PageRequest();
        Assertions.assertDoesNotThrow(entry::getLimit);
        Assertions.assertDoesNotThrow(entry::getPageNumber);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(PageRequest.class)
                .verify();
    }

    @Test
    void testToString() {
        var entry = new PageRequest();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testCalculateTotalPages() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, new PageRequest(0, (short) 0).calculateTotalPages(0)));
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(3, new PageRequest(0, (short) 2).calculateTotalPages(5)));
    }
}