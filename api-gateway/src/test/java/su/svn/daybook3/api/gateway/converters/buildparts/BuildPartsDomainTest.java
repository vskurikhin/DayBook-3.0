/*
 * This file was last modified at 2024-05-14 20:56 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * BuildPartsDomainTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.converters.buildparts;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.svn.daybook3.api.gateway.models.domain.TestModel;

import java.util.ArrayList;

class BuildPartsDomainTest {

    BuildPartsAnnotatedDomainFiled<?> buildParts;

    @BeforeEach
    void setUp() {
        Assertions.assertDoesNotThrow(() -> {
            buildParts = new BuildPartsAnnotatedDomainFiled<>(TestModel.class, TestModel::builder);
        });
    }

    @Test
    void test() {
        var expected = new ArrayList<String>() {{
            add("id");
            add("test");
        }};
        Assertions.assertDoesNotThrow(() -> {
            buildParts.forEach(entry -> Assertions.assertTrue(expected.contains(entry.getKey())));
        });
    }

    @Test
    void testGetters(){
        Assertions.assertDoesNotThrow(buildParts::getBuildParts);
        Assertions.assertDoesNotThrow(buildParts::getBuilderFactory);
        Assertions.assertDoesNotThrow(buildParts::getPClass);
    }
}