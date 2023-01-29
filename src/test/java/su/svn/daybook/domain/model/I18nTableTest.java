package su.svn.daybook.domain.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class I18nTableTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new I18nTable(
                null, 0L, I18nTable.NONE, null, null, null, null, true, true, 0
        ));
    }

    @Test
    void testGetters(){
        var entry = I18nTable.builder().build();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::languageId);
        Assertions.assertDoesNotThrow(entry::message);
        Assertions.assertDoesNotThrow(entry::translation);
        Assertions.assertDoesNotThrow(entry::userName);
        Assertions.assertDoesNotThrow(entry::createTime);
        Assertions.assertDoesNotThrow(entry::updateTime);
        Assertions.assertDoesNotThrow(entry::enabled);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(I18nTable.class).verify();
    }

    @Test
    void testToString() {
        var entry = I18nTable.builder().build();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(I18nTable.builder()
                .id(null)
                .languageId(0L)
                .message(null)
                .translation(null)
                .userName(null)
                .createTime(null)
                .updateTime(null)
                .enabled(true)
                .visible(true)
                .flags(0)
                .build()));
    }
}