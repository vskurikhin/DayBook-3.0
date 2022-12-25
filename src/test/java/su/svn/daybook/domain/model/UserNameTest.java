package su.svn.daybook.domain.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.UUID;

class UserNameTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new UserName());
        Assertions.assertDoesNotThrow(() -> new UserName(
                UUID.randomUUID(), "root", "password",null, null, false, true, 0
        ));
    }
    @Test
    void testGetters(){
        var entry = new UserName();
        Assertions.assertDoesNotThrow(entry::getId);
        Assertions.assertDoesNotThrow(entry::getUserName);
        Assertions.assertDoesNotThrow(entry::getPassword);
        Assertions.assertDoesNotThrow(entry::getCreateTime);
        Assertions.assertDoesNotThrow(entry::getUpdateTime);
        Assertions.assertDoesNotThrow(entry::getEnabled);
        Assertions.assertDoesNotThrow(entry::getVisible);
        Assertions.assertDoesNotThrow(entry::getFlags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(UserName.class)
                .suppress(Warning.NONFINAL_FIELDS)
                .withIgnoredFields("createTime")
                .withIgnoredFields("updateTime")
                .verify();
    }

    @Test
    void testToString() {
        var entry = new UserName();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(UserName.builder()
                .withId(UUID.randomUUID())
                .withUserName("root")
                .withPassword("password")
                .withCreateTime(null)
                .withUpdateTime(null)
                .withEnabled(false)
                .withVisible(true)
                .withFlags(0)
                .build()));
    }
}