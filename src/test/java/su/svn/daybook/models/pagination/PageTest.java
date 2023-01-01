package su.svn.daybook.models.pagination;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class PageTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new Page<>());
        Assertions.assertDoesNotThrow(() -> new Page<>(
                0L, (short) 0, 0, 0L, false, false, Collections.emptyList()
        ));
    }

    @Test
    void testGetters() {
        var entry = new Page<>();
        Assertions.assertDoesNotThrow(entry::getContent);
        Assertions.assertDoesNotThrow(entry::getPageNumber);
        Assertions.assertDoesNotThrow(entry::getPageSize);
        Assertions.assertDoesNotThrow(entry::getTotalElements);
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
                .pageNumber(0)
                .pageSize((short) 0)
                .nextPage(false)
                .prevPage(false)
                .totalElements(0)
                .totalPages(0L)
                .build()));
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(new Page<>().toBuilder().build()));
    }
}
