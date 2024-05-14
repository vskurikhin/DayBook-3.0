/*
 * This file was last modified at 2024-05-14 20:43 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * AnswerTest.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.messages;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import su.svn.daybook3.api.gateway.TestData;

class AnswerTest {

    @Test
    void testConstructors() {
        Assertions.assertDoesNotThrow(() -> new Answer());
    }

    @Test
    void empty() {
        Answer test = Answer.empty();
        Assertions.assertEquals(TestData.NO_SUCH_ELEMENT, test.message());
        Assertions.assertEquals(404, test.error());
        Assertions.assertEquals(Answer.EMPTY, test.payload());
    }

    @Test
    void of() {
        Object o = new Object();
        Answer test = Answer.of(o);
        Assertions.assertEquals(o, test.payload());
        Assertions.assertEquals(Answer.DEFAULT_MESSAGE, test.message());
    }

    @Test
    void create() {
        Object o = new Object();
        Answer test = Answer.create("test", o);
        Assertions.assertEquals(o, test.payload());
        Assertions.assertEquals("test", test.message());
    }

    @Test
    void setPayload() {
        Answer test = Answer.empty();
        Object o = new Object();
        test.payload(o);
        Assertions.assertEquals(o, test.payload());
    }

    @Test
    void getPayloadClass() {
        Answer test = Answer.empty();
        Object o = new Object();
        test.payload(o);
        Assertions.assertEquals(o.getClass(), test.payloadClass());
    }

    @Test
    void testEquals() {
        Object o = new Object();
        Answer test1 = new Answer("test");
        Answer test2 = new Answer("test");
        Assertions.assertEquals(test1, test2);
    }

    @Test
    void testHashCode() {
        Object o = new Object();
        Answer test1 = Answer.create("test", o);
        Answer test2 = Answer.create("test", o);
        Assertions.assertEquals(test1.hashCode(), test2.hashCode());
    }

    @Test
    void testToString() {
        Object o = new Object();
        Answer test1 = Answer.create("test", o);
        Assertions.assertTrue(test1.toString().length() > 0);
    }

    @Test
    void builder() {
        Object o = new Object();

        Answer.Builder builder = Answer.builder()
                .error(200)
                .message("test")
                .payload(o)
                .but();
        Answer test1 = builder.build();
        Answer test2 = Answer.create("test", o);
        Assertions.assertEquals(test1, test2);
    }
}