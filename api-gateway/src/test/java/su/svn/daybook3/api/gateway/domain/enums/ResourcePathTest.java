/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ResourcePathTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.enums;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ResourcePathTest {

    @Test
    void testMethodGetValue() {
        Assertions.assertNull(ResourcePath.Null.getValue());
    }

    @Test
    void testMethodStringEquals() {
        Assertions.assertTrue(ResourcePath.Null.stringEquals(null));
        Assertions.assertTrue(ResourcePath.KeyValue.stringEquals("/api/v1/key-value"));
    }

    @Test
    void testMethodLookup() {
        Assertions.assertEquals(ResourcePath.Null, ResourcePath.lookup(null));
        Assertions.assertEquals(ResourcePath.KeyValue, ResourcePath.lookup("/api/v1/key-value"));
        Assertions.assertNull(ResourcePath.lookup("VmeVXS0318/Zb/O8Xots9Qr3VCWIrsH3YNGeTpdrdGKnZhEz91SW3rFQqCCupzpWb"));
    }
}