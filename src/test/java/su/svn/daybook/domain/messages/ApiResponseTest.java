/*
 * This file was last modified at 2021.12.21 15:19 by Victor N. Skurikhin.
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
        ApiResponse test1 = new ApiResponse();
        ApiResponse test2 = new ApiResponse((String)null);
        Assertions.assertEquals(test1, test2);

        ApiResponse test3 = new ApiResponse(0L);
        ApiResponse test4 = new ApiResponse(0L, null);
        Assertions.assertEquals(test3, test4);
    }

    @Test
    void setters() {
        Object o = new Object();

        ApiResponse test1 = new ApiResponse(0L, "");
        ApiResponse test2 = new ApiResponse(0L, "", null, o);
        test1.setPayload(o);
        Assertions.assertEquals(test1, test2);

        ApiResponse test3 = new ApiResponse(0L, "");
        ApiResponse test4 = new ApiResponse(1L, "test", 1, o);
        test3.setId(1L);
        test3.setMessage("test");
        test3.setError(1);
        test3.setPayload(o);
        Assertions.assertEquals(test3, test4);
        Assertions.assertEquals(test3.hashCode(), test4.hashCode());
    }

    @Test
    void builder() {
        Object o = new Object();

        ApiResponse.Builder builder = ApiResponse.builder()
                .withId(1L)
                .withError(1)
                .withMessage("test")
                .withPayload(o)
                .but();
        ApiResponse test1 = builder.build();
        ApiResponse test2 = new ApiResponse(1L, "test", 1, o);
        Assertions.assertEquals(test1, test2);
    }
}