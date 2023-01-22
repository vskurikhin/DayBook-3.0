package su.svn.daybook.models.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.@KType@;

class @Name@Test {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new @Name@());
        Assertions.assertDoesNotThrow(() -> new @Name@(
                null, @KType@.ZERO, null, true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = new @Name@();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::@key@);
        Assertions.assertDoesNotThrow(entry::@value@);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(@Name@.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testHashCode() {
        var test0 = new @Name@(null, @KType@.ZERO, null, true, -28667312);
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(0, test0.hashCode()));
        var test1 = new @Name@(null, @KType@.ZERO, null, true, 0);
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotEquals(0, test1.hashCode()));
    }

    @Test
    void testToString() {
        var entry = new @Name@();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(@Name@.builder()
                .id(null)
                .@key@(@KType@.ZERO)
                .@value@(null)
                .visible(true)
                .flags(0)
                .build()));
    }

    @Test
    void testToBuilder() {
        var test = new @Name@();
        var expected = new @Name@();
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(expected, test.toBuilder().build()));
    }
}