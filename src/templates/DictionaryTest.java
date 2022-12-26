package su.svn.daybook.domain.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static su.svn.daybook.domain.model.@Name@.NONE;

class @Name@Test {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new @Name@());
        Assertions.assertDoesNotThrow(() -> new @Name@(
                null, NONE, null, null, null, null, false, true, 0
        ));
    }
    @Test
    void testGetters(){
        var entry = new @Name@();
        Assertions.assertDoesNotThrow(entry::getId);
        Assertions.assertDoesNotThrow(entry::get@Key@);
        Assertions.assertDoesNotThrow(entry::get@Value@);
        Assertions.assertDoesNotThrow(entry::getUserName);
        Assertions.assertDoesNotThrow(entry::getCreateTime);
        Assertions.assertDoesNotThrow(entry::getUpdateTime);
        Assertions.assertDoesNotThrow(entry::getEnabled);
        Assertions.assertDoesNotThrow(entry::isEnabled);
        Assertions.assertDoesNotThrow(entry::getVisible);
        Assertions.assertDoesNotThrow(entry::isVisible);
        Assertions.assertDoesNotThrow(entry::getFlags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(@Name@.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withIgnoredFields("createTime")
                .withIgnoredFields("updateTime")
                .verify();
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
                .@key@(NONE)
                .@value@(null)
                .userName(null)
                .createTime(null)
                .updateTime(null)
                .enabled(false)
                .visible(true)
                .flags(0)
                .build()));
    }
}