package su.svn.daybook.domain.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.UUID;

class UserViewTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new UserView(
                UUID.randomUUID(), "guest", "password", Collections.emptySet(), null, null, false, true, 0
        ));
    }

    @Test
    void testGetters() {
        var entry = UserView.builder().build();
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::userName);
        Assertions.assertDoesNotThrow(entry::password);
        Assertions.assertDoesNotThrow(entry::roles);
        Assertions.assertDoesNotThrow(entry::createTime);
        Assertions.assertDoesNotThrow(entry::updateTime);
        Assertions.assertDoesNotThrow(entry::enabled);
        Assertions.assertDoesNotThrow(entry::visible);
        Assertions.assertDoesNotThrow(entry::flags);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(UserView.class).verify();
    }

    @Test
    void testToString() {
        var entry = UserView.builder().build();
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(UserView.builder()
                .id(UUID.randomUUID())
                .userName("root")
                .password("password")
                .roles(Collections.emptySet())
                .createTime(null)
                .updateTime(null)
                .enabled(false)
                .visible(true)
                .flags(0)
                .build()));
    }
}