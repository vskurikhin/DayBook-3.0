package su.svn.daybook.domain.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static su.svn.daybook.domain.model.UserHasRolesTable.NONE;

class UserHasRolesTableTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new UserHasRolesTable());
        Assertions.assertDoesNotThrow(() -> new UserHasRolesTable(
                null, NONE, NONE, null, null, true, true, 0
        ));
    }
    @Test
    void testGetters(){
        var entry = new UserHasRolesTable();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::userName);
        Assertions.assertDoesNotThrow(entry::getRole);
        Assertions.assertDoesNotThrow(entry::userName);
        Assertions.assertDoesNotThrow(entry::createTime);
        Assertions.assertDoesNotThrow(entry::updateTime);
        Assertions.assertDoesNotThrow(entry::enabled);
        Assertions.assertDoesNotThrow(entry::isEnabled);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::isVisible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(UserHasRolesTable.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .withIgnoredFields("createTime", "updateTime", "hash", "hashIsZero")
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
    }

    @Test
    void testToString() {
        var entry = new UserHasRolesTable();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(UserHasRolesTable.builder()
                .id(null)
                .userName(NONE)
                .role(NONE)
                .createTime(null)
                .updateTime(null)
                .enabled(true)
                .visible(true)
                .flags(0)
                .build()));
    }
}