/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LongUtilTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LongUtilTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void test() {
        Assertions.assertDoesNotThrow(
                () -> Assertions.assertTrue(LongUtil.generateLongId() > LongUtil.MILLION)
        );
    }
}