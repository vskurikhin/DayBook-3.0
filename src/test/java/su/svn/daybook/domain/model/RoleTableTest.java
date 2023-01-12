package su.svn.daybook.domain.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static su.svn.daybook.domain.model.RoleTable.NONE;

class RoleTableTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new RoleTable(
                null, NONE, null, null, null, null, true, true, 0
        ));
    }
    @Test
    void testGetters(){
        var entry = new RoleTable(
                null, NONE, null, null, null, null, true, true, 0
        );
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::role);
        Assertions.assertDoesNotThrow(entry::description);
        Assertions.assertDoesNotThrow(entry::userName);
        Assertions.assertDoesNotThrow(entry::createTime);
        Assertions.assertDoesNotThrow(entry::updateTime);
        Assertions.assertDoesNotThrow(entry::enabled);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(RoleTable.class)
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testToString() {
        var entry = new RoleTable(
                null, NONE, null, null, null, null, true, true, 0
        );
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(RoleTable.builder()
                .id(null)
                .role(NONE)
                .description(null)
                .userName(null)
                .createTime(null)
                .updateTime(null)
                .enabled(true)
                .visible(true)
                .flags(0)
                .build()));
    }
}