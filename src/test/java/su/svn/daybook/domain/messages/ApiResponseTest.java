/*
 * This file was last modified at 2022.01.12 17:47 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ApiResponseTest.java
 * $Id$
 */

package su.svn.daybook.domain.messages;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ApiResponseTest {

    @Test
    void constructors() {
        ApiResponse<Object> test1 = new ApiResponse<>();
        ApiResponse<Object> test2 = ApiResponse.message(null);
        Assertions.assertEquals(test1, test2);

        ApiResponse<Long> test3 = new ApiResponse<Long>(0L);
        ApiResponse<Long> test4 = new ApiResponse<Long>(0L, null);
        Assertions.assertEquals(test3, test4);
    }

    @Test
    void setters() {
        Object o = new Object();

        ApiResponse<Long> test1 = new ApiResponse<Long>(0L, "");
        ApiResponse<Long> test2 = new ApiResponse<Long>(0L, "", null, o);
        test1.setPayload(o);
        Assertions.assertEquals(test1, test2);

        ApiResponse<Long> test3 = new ApiResponse<Long>(0L, "");
        ApiResponse<Long> test4 = new ApiResponse<Long>(1L, "test", 1, o);
        test3.setId(1L);
        test3.setMessage("test");
        test3.setError(1);
        test3.setPayload(o);
        Assertions.assertEquals(test3, test4);
        Assertions.assertEquals(test3.hashCode(), test4.hashCode());
    }


    @Test
    void testToString() {
        Object o = new Object();
        ApiResponse<Long> test1 = new ApiResponse<Long>(0L, "");
        Assertions.assertTrue(test1.toString().length() > 0);
    }

    @Test
    void builder() {
        Object o = new Object();

        ApiResponse.Builder<Long> builder = ApiResponse.<Long>builder()
                .withId(1L)
                .withError(1)
                .withMessage("test")
                .withPayload(o)
                .but();
        ApiResponse<Long> test1 = builder.build();
        ApiResponse<Long> test2 = new ApiResponse<Long>(1L, "test", 1, o);
        Assertions.assertEquals(test1, test2);
    }
}