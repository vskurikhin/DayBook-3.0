/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * EventAddressTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.enums;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EventAddressTest {

    @Test
    void testMethod_getValue() {
        Assertions.assertNull(EventAddress.Null.getValue());
    }

    @Test
    void testMethod_stringEquals() {
        Assertions.assertTrue(EventAddress.Null.stringEquals(null));
        Assertions.assertTrue(EventAddress.CodifierAdd.stringEquals("code_add"));
    }

    @Test
    void testMethod_lookup() {
        Assertions.assertEquals(EventAddress.Null, EventAddress.lookup(null));
        Assertions.assertEquals(EventAddress.CodifierAdd, EventAddress.lookup("code_add"));
        Assertions.assertNull(EventAddress.lookup("0MxliRx4k+YSk/V4dJFSmqqZgl+L65nL+hoxJb4Cl1Y97dLrIUJR9jBBRytbK7Qzd"));
    }
}