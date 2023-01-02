package su.svn.daybook.models.domain;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import su.svn.daybook.TestData;

class TagLabelTest {


    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new TagLabel());
        Assertions.assertDoesNotThrow(() -> new TagLabel(
                TestData.TAG_LABEL.ID, TagLabel.NONE, true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = new TagLabel();
        Assertions.assertDoesNotThrow(entry::getId);
        Assertions.assertDoesNotThrow(entry::getLabel);
        Assertions.assertDoesNotThrow(entry::getVisible);
        Assertions.assertDoesNotThrow(entry::isVisible);
        Assertions.assertDoesNotThrow(entry::getFlags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(TagLabel.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testToString() {
        var entry = new TagLabel();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(TagLabel.builder()
                .id(TestData.TAG_LABEL.ID)
                .label(TagLabel.NONE)
                .visible(true)
                .flags(0)
                .build()));
    }
}