/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * BigIntegerUtilTest.java
 * $Id$
 */

package su.svn.daybook3.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

class BigIntegerUtilTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void test() {
        Assertions.assertDoesNotThrow(
                () -> BigIntegerUtil.generateBigIntegerId(LocalDateTime.now())
        );
    }
}