package su.svn.daybook.domain.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CodifierTableTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new CodifierTable(
                CodifierTable.NONE, null, null, null, null, true, true, 0
        ));
    }
    @Test
    void testGetters(){
        var entry = CodifierTable.builder().build();
        Assertions.assertDoesNotThrow(entry::code);
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::value);
        Assertions.assertDoesNotThrow(entry::userName);
        Assertions.assertDoesNotThrow(entry::createTime);
        Assertions.assertDoesNotThrow(entry::updateTime);
        Assertions.assertDoesNotThrow(entry::enabled);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(CodifierTable.class).verify();
    }

    @Test
    void testToString() {
        var entry = CodifierTable.builder().build();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(CodifierTable.builder()
                .code(CodifierTable.NONE)
                .value(null)
                .userName(null)
                .createTime(null)
                .updateTime(null)
                .enabled(true)
                .visible(true)
                .flags(0)
                .build()));
    }
}