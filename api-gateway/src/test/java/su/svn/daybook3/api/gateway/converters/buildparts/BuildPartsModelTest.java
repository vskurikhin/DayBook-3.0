/*
 * This file was last modified at 2024-05-14 20:43 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * BuildPartsModelTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.converters.buildparts;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.svn.daybook3.api.gateway.domain.model.TestTable;

import java.util.ArrayList;

class BuildPartsModelTest {

    BuildPartsAnnotatedModelFiled<?> buildParts;

    @BeforeEach
    void setUp() {
        Assertions.assertDoesNotThrow(() -> {
            buildParts = new BuildPartsAnnotatedModelFiled<>(TestTable.class, TestTable::builder);
        });
    }

    @Test
    void test() {
        var expected = new ArrayList<String>() {{
            add("id");
            add("test");
            add("flag");
            add("prefix");
        }};
        Assertions.assertDoesNotThrow(() -> {
            buildParts.forEach(entry -> Assertions.assertTrue(expected.contains(entry.getKey())));
        });
    }

    @Test
    void testGetters() {
        Assertions.assertDoesNotThrow(buildParts::getBuildParts);
        Assertions.assertDoesNotThrow(buildParts::getBuilderFactory);
        Assertions.assertDoesNotThrow(buildParts::getPClass);
    }
}