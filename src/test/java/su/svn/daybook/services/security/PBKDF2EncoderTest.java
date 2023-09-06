/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * PBKDF2EncoderTest.java
 * $Id$
 */

package su.svn.daybook.services.security;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import jakarta.inject.Inject;

@QuarkusTest
class PBKDF2EncoderTest {

    @Inject
    PBKDF2Encoder pbkdf2Encoder;

    @Test
    void test() {
        var encoded = pbkdf2Encoder.encode("user");
        System.out.println("encoded = " + encoded);
    }
}