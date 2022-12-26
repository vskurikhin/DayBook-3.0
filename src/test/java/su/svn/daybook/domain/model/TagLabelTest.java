package su.svn.daybook.domain.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TagLabelTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new TagLabel());
        Assertions.assertDoesNotThrow(() -> new TagLabel(
                null, null, null, null, null, false, true, 0
        ));
    }

    @Test
    void testGetters(){
        var entry = new TagLabel();
        Assertions.assertDoesNotThrow(entry::getId);
        Assertions.assertDoesNotThrow(entry::getLabel);
        Assertions.assertDoesNotThrow(entry::getUserName);
        Assertions.assertDoesNotThrow(entry::getCreateTime);
        Assertions.assertDoesNotThrow(entry::getUpdateTime);
        Assertions.assertDoesNotThrow(entry::isEnabled);
        Assertions.assertDoesNotThrow(entry::getEnabled);
        Assertions.assertDoesNotThrow(entry::getVisible);
        Assertions.assertDoesNotThrow(entry::isVisible);
        Assertions.assertDoesNotThrow(entry::getFlags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(TagLabel.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withIgnoredFields("createTime")
                .withIgnoredFields("updateTime")
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
                .id(null)
                .label(null)
                .userName(null)
                .createTime(null)
                .updateTime(null)
                .enabled(false)
                .visible(true)
                .flags(0)
                .build()));
    }
}