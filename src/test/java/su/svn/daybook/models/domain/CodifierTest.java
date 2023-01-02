package su.svn.daybook.models.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static su.svn.daybook.models.domain.Codifier.NONE;

class CodifierTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new Codifier());
        Assertions.assertDoesNotThrow(() -> new Codifier(
                NONE, null, true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = new Codifier();
        Assertions.assertDoesNotThrow(entry::getId);
        Assertions.assertDoesNotThrow(entry::getCode);
        Assertions.assertDoesNotThrow(entry::getValue);
        Assertions.assertDoesNotThrow(entry::getVisible);
        Assertions.assertDoesNotThrow(entry::isVisible);
        Assertions.assertDoesNotThrow(entry::getFlags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(Codifier.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
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
                .id(NONE)
                .code(NONE)
                .value(null)
                .visible(true)
                .flags(0)
                .build()));
    }
}