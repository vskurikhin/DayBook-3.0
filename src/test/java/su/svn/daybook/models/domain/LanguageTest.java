package su.svn.daybook.models.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LanguageTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new Language());
        Assertions.assertDoesNotThrow(() -> new Language(
                null, Language.NONE, true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = new Language();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::language);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(Language.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testHashCode() {
        var test1 = new Language(null, Language.NONE, true, 0);
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotEquals(0, test1.hashCode()));
    }

    @Test
    void testToString() {
        var entry = new Language();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(Language.builder()
                .id(null)
                .language(Language.NONE)
                .visible(true)
                .flags(0)
                .build()));
    }

    @Test
    void testToBuilder() {
        var test = new Language();
        var expected = new Language();
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(expected, test.toBuilder().build()));
    }
}