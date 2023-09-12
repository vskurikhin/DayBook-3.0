package su.svn.daybook.domain.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SettingTableTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new SettingTable(
                null, SettingTable.NONE, null, 0L, 0L, null, null, null, true, true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = SettingTable.builder().build();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::variable);
        Assertions.assertDoesNotThrow(entry::value);
        Assertions.assertDoesNotThrow(entry::valueTypeId);
        Assertions.assertDoesNotThrow(entry::stanzaId);
        Assertions.assertDoesNotThrow(entry::userName);
        Assertions.assertDoesNotThrow(entry::createTime);
        Assertions.assertDoesNotThrow(entry::updateTime);
        Assertions.assertDoesNotThrow(entry::enabled);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(SettingTable.class).verify();
    }

    @Test
    void testToString() {
        var entry = SettingTable.builder().build();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(SettingTable.builder()
                .id(null)
                .variable(SettingTable.NONE)
                .value(null)
                .valueTypeId(0L)
                .stanzaId(0)
                .userName(null)
                .createTime(null)
                .updateTime(null)
                .enabled(true)
                .visible(true)
                .flags(0)
                .build()));
    }
}