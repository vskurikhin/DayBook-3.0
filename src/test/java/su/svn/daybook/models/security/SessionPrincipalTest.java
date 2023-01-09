package su.svn.daybook.models.security;

import org.jose4j.jwt.JwtClaims;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;

class SessionPrincipalTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new SessionPrincipal(
                null, new JwtClaims(), null, Collections.emptySet(), null
        ));
    }

    @Test
    void testGetters() {
        var entry = new SessionPrincipal(
                null, new JwtClaims(), null, Collections.emptySet(), null
        );
        Assertions.assertDoesNotThrow(entry::getGroups);
        Assertions.assertDoesNotThrow(entry::getName);
        Assertions.assertDoesNotThrow(entry::getSessionId);
        Assertions.assertDoesNotThrow(entry::getRequestId);
        Assertions.assertDoesNotThrow(() -> entry.toString(true));
    }

    @Test
    void testToString() {
        var entry = new SessionPrincipal(
                null, new JwtClaims(), null, Collections.emptySet(), null
        );
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }
}