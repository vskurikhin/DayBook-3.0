/*
 * This file was last modified at 2024-05-14 20:56 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SessionPrincipalTest.java
 * $Id$
 */

package su.svn.daybook3.api.models.security;

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