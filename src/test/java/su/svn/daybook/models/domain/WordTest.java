package su.svn.daybook.models.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static su.svn.daybook.models.domain.Word.NONE;

class WordTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new Word());
        Assertions.assertDoesNotThrow(() -> new Word(
                NONE, true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = new Word();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::word);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(Word.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testToString() {
        var entry = new Word();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(Word.builder()
                .id(NONE)
                .word(NONE)
                .visible(true)
                .flags(0)
                .build()));
        Assertions.assertDoesNotThrow(() -> new Word().toBuilder().build());
    }
}