package su.svn.daybook.models.pagination;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class PageRequestRequestTest {

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
