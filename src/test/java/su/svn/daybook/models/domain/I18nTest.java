package su.svn.daybook.models.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class I18nTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new I18n());
        Assertions.assertDoesNotThrow(() -> new I18n(
                0L, 0L, null, null, true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = new I18n();
        Assertions.assertDoesNotThrow(entry::getId);
        Assertions.assertDoesNotThrow(entry::getLanguageId);
        Assertions.assertDoesNotThrow(entry::getMessage);
        Assertions.assertDoesNotThrow(entry::getTranslation);
        Assertions.assertDoesNotThrow(entry::getVisible);
        Assertions.assertDoesNotThrow(entry::isVisible);
        Assertions.assertDoesNotThrow(entry::getFlags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(I18n.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testToString() {
        var entry = new I18n();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(I18n.builder()
                .id(null)
                .languageId(0L)
                .message(null)
                .translation(null)
                .visible(true)
                .flags(0)
                .build()));
    }
}