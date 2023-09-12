package su.svn.daybook.models.domain;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import su.svn.daybook.domain.model.StanzaTable;

import java.util.Collections;

class                                                                                                                           StanzaTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> Stanza.ROOT);
        Assertions.assertDoesNotThrow(() -> new Stanza(
                null, StanzaTable.NONE, null, Stanza.ROOT, Collections.emptySet(), true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = new Stanza();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::name);
        Assertions.assertDoesNotThrow(entry::description);
        Assertions.assertDoesNotThrow(entry::parent);
        Assertions.assertDoesNotThrow(entry::settings);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        /** TODO fix it
        EqualsVerifier.forClass(Stanza.class)
                .withResetCaches()
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("parent", "hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
         */
    }

    @Test
    void testHashCode() {
        var test1 = new Stanza(null, Stanza.NONE, null, Stanza.ROOT, Collections.emptySet(), true, 0);
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotEquals(0, test1.hashCode()));
    }

    @Test
    void testToString() {
        var entry = new Stanza();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(Stanza.builder()
                .id(null)
                .name(Stanza.NONE)
                .description(null)
                .parent(Stanza.ROOT)
                .settings(Collections.emptySet())
                .visible(true)
                .flags(0)
                .build()));
    }

    @Test
    void testToBuilder() {
        var test = new Stanza();
        var expected = new Stanza();
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(expected, test.toBuilder().build()));
    }
}