/*
 * This file was last modified at 2024-05-14 23:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ApiResponseTest.java
 * $Id$
 */

package su.svn.daybook3.domain.messages;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import su.svn.daybook3.TestData;

import jakarta.annotation.Nonnull;

import java.io.Serializable;

class ApiResponseTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new ApiResponse<>());
        Assertions.assertDoesNotThrow(() -> new ApiResponse<>(
                TestData.uuid.ZERO, new Object()
        ));
    }

    @Test
    void testGetters(){
        var entry = new ApiResponse<>(TestData.uuid.ZERO);
        Assertions.assertDoesNotThrow(entry::id);
        Assertions.assertDoesNotThrow(entry::code);
        Assertions.assertDoesNotThrow(entry::error);
        Assertions.assertDoesNotThrow(entry::message);
        Assertions.assertDoesNotThrow(entry::payload);
        Assertions.assertDoesNotThrow(entry::payloadClass);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(ApiResponse.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
        Assertions.assertEquals(0, (new ApiResponse<>(new ForHashCode())).hashCode());
        Assertions.assertNotEquals(0, (new ApiResponse<>(TestData.uuid.ZERO)).hashCode());
    }

    @Test
    void testToString() {
        var entry = new ApiResponse<>(TestData.uuid.STRING_ZERO);
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(ApiResponse.builder()
                .id(TestData.uuid.ZERO)
                .code(0)
                .error(0)
                .message(null)
                .payload(new Object())
                .build()));
    }

    static class ForHashCode implements Comparable<ForHashCode>, Serializable {

        @Override
        public int compareTo(@Nonnull ForHashCode o) {
            return 0;
        }

        public int hashCode() {
            return -31;
        }
    }
}