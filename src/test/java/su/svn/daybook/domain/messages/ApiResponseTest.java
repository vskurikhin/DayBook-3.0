/*
 * This file was last modified at 2022.01.12 17:47 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ApiResponseTest.java
 * $Id$
 */

package su.svn.daybook.domain.messages;

import liquibase.structure.core.Data;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import su.svn.daybook.DataTest;

import java.io.Serializable;

class ApiResponseTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new ApiResponse<>(
                DataTest.ZERO_UUID, new Object()
        ));
        Assertions.assertDoesNotThrow(() -> ApiResponse.message(null));
    }

    @Test
    void testGetters(){
        var entry = new ApiResponse<>(DataTest.ZERO_UUID);
        Assertions.assertDoesNotThrow(entry::getId);
        Assertions.assertDoesNotThrow(entry::getError);
        Assertions.assertDoesNotThrow(entry::getMessage);
        Assertions.assertDoesNotThrow(entry::getPayload);
        Assertions.assertDoesNotThrow(entry::getPayloadClass);
    }

    @Test
    void testEqualsVerifier() {
        EqualsVerifier.forClass(ApiResponse.class)
                .withCachedHashCode("hash", "calculateHashCode", null)
                .suppress(Warning.NO_EXAMPLE_FOR_CACHED_HASHCODE)
                .verify();
        Assertions.assertEquals(0, (new ApiResponse<>(new ForHashCode())).hashCode());
        Assertions.assertNotEquals(0, (new ApiResponse<>(DataTest.ZERO_UUID)).hashCode());
    }

    @Test
    void testToString() {
        var entry = new ApiResponse<>(DataTest.STRING_ZERO_UUID);
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(entry.toString()));
    }

    @Test
    void testBuilder() {
        Assertions.assertDoesNotThrow(() -> Assertions.assertNotNull(ApiResponse.builder()
                .id(DataTest.ZERO_UUID)
                .error(0)
                .message(null)
                .payload(new Object())
                .build()));
    }

    static class ForHashCode implements Comparable<ForHashCode>, Serializable {

        @Override
        public int compareTo(ForHashCode o) {
            return 0;
        }

        public int hashCode() {
            return -31;
        }
    }
}