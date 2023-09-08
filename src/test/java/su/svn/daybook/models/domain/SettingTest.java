package su.svn.daybook.models.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SettingTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new Setting());
        Assertions.assertDoesNotThrow(() -> new Setting(
                null, Setting.NONE, Setting.NONE, Setting.DEFAULT_TYPE, true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = new Setting();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::variable);
        Assertions.assertDoesNotThrow(entry::value);
        Assertions.assertDoesNotThrow(entry::valueType);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(Setting.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testHashCode() {
        var test1 = new Setting(null, Setting.NONE, Setting.NONE, Setting.DEFAULT_TYPE, true, 0);
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotEquals(0, test1.hashCode()));
    }

    @Test
    void testToString() {
        var entry = new Setting();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(Setting.builder()
                .id(null)
                .variable(Setting.NONE)
                .value(Setting.NONE)
                .valueType(Setting.DEFAULT_TYPE)
                .visible(true)
                .flags(0)
                .build()));
    }

    @Test
    void testToBuilder() {
        var test = new Setting();
        var expected = new Setting();
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(expected, test.toBuilder().build()));
    }
}