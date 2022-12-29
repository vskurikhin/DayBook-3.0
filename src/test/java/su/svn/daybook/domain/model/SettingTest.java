package su.svn.daybook.domain.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static su.svn.daybook.domain.model.Setting.NONE;

class SettingTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new Setting());
        Assertions.assertDoesNotThrow(() -> new Setting(
                0L, NONE, null, 0L, null, null, null, false, true, 0
        ));
    }
    @Test
    void testGetters(){
        var entry = new Setting();
        Assertions.assertDoesNotThrow(entry::getId);
        Assertions.assertDoesNotThrow(entry::getKey);
        Assertions.assertDoesNotThrow(entry::getValue);
        Assertions.assertDoesNotThrow(entry::getValueTypeId);
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
        EqualsVerifier.forClass(Setting.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("createTime", "updateTime", "hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
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
                .key(NONE)
                .value(null)
                .valueTypeId(0)
                .userName(null)
                .createTime(null)
                .updateTime(null)
                .enabled(false)
                .visible(true)
                .flags(0)
                .build()));
    }
}