/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * KeyValueMapperTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.services.mappers;

import io.quarkus.test.junit.QuarkusTest;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import su.svn.daybook3.api.gateway.TestData;
import su.svn.daybook3.api.gateway.domain.model.KeyValueTable;
import su.svn.daybook3.api.gateway.models.domain.KeyValue;

import jakarta.inject.Inject;

import java.math.BigInteger;

@QuarkusTest
class KeyValueMapperTest {

    JsonObject json;

    @BeforeEach
    void setUp() {
    }

    @Inject
    KeyValueMapper mapper;

    @Test
    void convertToDomain() {
        json = new JsonObject();
        var test = KeyValue.builder()
                .id(TestData.uuid.ONE)
                .key(BigInteger.TWO)
                .value(json)
                .visible(true)
                .flags(13)
                .build();
        var expected = KeyValueTable.builder()
                .id(TestData.uuid.ONE)
                .key(BigInteger.TWO)
                .value(json)
                .visible(true)
                .flags(13)
                .build();
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(expected, mapper.convertToDomain(test)));
    }

    @Test
    void convertToModel() {
        var test = KeyValueTable.builder()
                .id(TestData.uuid.ONE)
                .key(BigInteger.TWO)
                .value(json)
                .visible(true)
                .flags(13)
                .build();
        var expected = KeyValue.builder()
                .id(TestData.uuid.ONE)
                .key(BigInteger.TWO)
                .value(json)
                .visible(true)
                .flags(13)
                .build();
        Assertions.assertDoesNotThrow(() -> Assertions.assertEquals(expected, mapper.convertToModel(test)));
    }
}