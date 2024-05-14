/*
 * This file was last modified at 2024-05-14 20:56 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * GettersAnnotatedDomainFiledTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.converters.getters;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.svn.daybook3.api.gateway.models.domain.TestModel;

import java.util.ArrayList;

class GettersAnnotatedDomainFiledTest {

    GettersAnnotatedDomainFiled<?> getters;

    @BeforeEach
    void setUp() {
        Assertions.assertDoesNotThrow(() -> {
            getters = new GettersAnnotatedDomainFiled<>(TestModel.class);
        });
    }

    @Test
    void test() {
        var expected = new ArrayList<String>() {{
            add("id");
            add("test");
        }};
        Assertions.assertDoesNotThrow(() -> {
            getters.forEach((name, f) -> Assertions.assertTrue(expected.contains(name)));
        });
    }

    @Test
    void testGetters(){
        Assertions.assertDoesNotThrow(getters::getGetters);
        Assertions.assertDoesNotThrow(getters::getPClass);
    }
}