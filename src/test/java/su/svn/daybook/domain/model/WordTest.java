package su.svn.daybook.domain.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static su.svn.daybook.domain.model.Word.NONE;

class WordTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new Word());
        Assertions.assertDoesNotThrow(() -> new Word(
                NONE, null, null, null, false, true, 0
        ));
    }
    @Test
    void testGetters(){
        var entry = new Word();
        Assertions.assertDoesNotThrow(entry::getId);
        Assertions.assertDoesNotThrow(entry::getWord);
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
        EqualsVerifier.forClass(Word.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withIgnoredFields("createTime")
                .withIgnoredFields("updateTime")
                .verify();
    }

    @Test
    void testToString() {
        var entry = new Word();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(Word.builder()
                .withWord(NONE)
                .withUserName(null)
                .withCreateTime(null)
                .withUpdateTime(null)
                .withEnabled(false)
                .withVisible(true)
                .withFlags(0)
                .build()));
    }
}