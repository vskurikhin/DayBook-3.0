package su.svn.daybook.models.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import su.svn.daybook.TestData;

import java.util.Collections;

class SessionTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new Session());
        Assertions.assertDoesNotThrow(() -> new Session(
                null, Session.NONE,  Collections.emptySet(), TestData.time.EPOCH_TIME, true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = new Session();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::getUserName);
        Assertions.assertDoesNotThrow(entry::getRoles);
        Assertions.assertDoesNotThrow(entry::getValidTime);
        Assertions.assertDoesNotThrow(entry::getVisible);
        Assertions.assertDoesNotThrow(entry::isVisible);
        Assertions.assertDoesNotThrow(entry::getFlags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(Session.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testToString() {
        var entry = new Session();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(Session.builder()
                .id(null)
                .userName(Session.NONE)
                .roles(Collections.emptySet())
                .validTime(TestData.time.EPOCH_TIME)
                .visible(true)
                .flags(0)
                .build()));
    }
}