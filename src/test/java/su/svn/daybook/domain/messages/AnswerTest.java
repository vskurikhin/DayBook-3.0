package su.svn.daybook.domain.messages;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import su.svn.daybook.DataTest;

class AnswerTest {

    @Test
    void empty() {
        Answer test = Answer.empty();
        Assertions.assertEquals(DataTest.NO_SUCH_ELEMENT, test.getMessage());
        Assertions.assertEquals(404, test.getError());
        Assertions.assertEquals(Answer.EMPTY, test.getPayload());
    }

    @Test
    void of() {
        Object o = new Object();
        Answer test = Answer.of(o);
        Assertions.assertEquals(o, test.getPayload());
        Assertions.assertEquals(Answer.DEFAULT_MESSAGE, test.getMessage());
    }

    @Test
    void create() {
        Object o = new Object();
        Answer test = Answer.create("test", o);
        Assertions.assertEquals(o, test.getPayload());
        Assertions.assertEquals("test", test.getMessage());
    }

    @Test
    void setPayload() {
        Answer test = Answer.empty();
        Object o = new Object();
        test.setPayload(o);
        Assertions.assertEquals(o, test.getPayload());
    }

    @Test
    void getPayloadClass() {
        Answer test = Answer.empty();
        Object o = new Object();
        test.setPayload(o);
        Assertions.assertEquals(o.getClass(), test.getPayloadClass());
    }

    @Test
    void testEquals() {
        Object o = new Object();
        Answer test1 = new Answer("test");
        Answer test2 = new Answer("test");
        Assertions.assertTrue(test1.equals(test2));
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