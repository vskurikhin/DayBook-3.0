/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * StringUtilTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.svn.daybook3.utils.StringUtil;

class StringUtilTest {

    @BeforeEach
    void setUp() {
    }

    @Test
    void test() {
        Assertions.assertDoesNotThrow(StringUtil::generateStringId);
        var test = StringUtil.generateStringId();
        Assertions.assertNotNull(test);
        Assertions.assertTrue(test.length() > 0);
    }
}