package su.svn.daybook.domain.messages;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CommandTest {

    @Test
    void methodTest_createPing() {
        Command test = Command.createPing("sender_test", "recipient_test");
        Assertions.assertNull(test.getPayload());
        Assertions.assertEquals("sender_test", test.getSender());
        Assertions.assertEquals("recipient_test", test.getRecipient());
        Assertions.assertEquals(Command.PING, test.getCommand());
    }

    @Test
    void methodTest_createPongOf() {
        Command ping = Command.createPing("sender_test", "recipient_test");
        Command test = Command.createPongOf(ping);
        Assertions.assertNull(test.getPayload());
        Assertions.assertEquals("recipient_test", test.getSender());
        Assertions.assertEquals("sender_test", test.getRecipient());
        Assertions.assertEquals(Command.PONG, test.getCommand());
    }

    @Test
    void methodTest_createSend() {
        Object o = new Object();
        Command test = Command.createSend("sender_test", "recipient_test", o);
        Assertions.assertEquals(o, test.getPayload());
        Assertions.assertEquals("sender_test", test.getSender());
        Assertions.assertEquals("recipient_test", test.getRecipient());
        Assertions.assertEquals(Command.SEND, test.getCommand());
    }

    @Test
    void methodTest_create() {
        Object o = new Object();
        Command test = Command.create("sender_test", "recipient_test", "command_test", o);
        Assertions.assertEquals(o, test.getPayload());
        Assertions.assertEquals("sender_test", test.getSender());
        Assertions.assertEquals("recipient_test", test.getRecipient());
        Assertions.assertEquals("command_test", test.getCommand());
    }

    @Test
    void methodTest_setRecipient() {
        Object o = new Object();
        Command test = Command.create("sender_test", "recipient_test", "command_test", o);
        Assertions.assertEquals(o, test.getPayload());
        Assertions.assertEquals("sender_test", test.getSender());
        Assertions.assertEquals("recipient_test", test.getRecipient());
        Assertions.assertEquals("command_test", test.getCommand());
    }

    @Test
    void methodTest_setPayload() {
        Object o = new Object();
        Command test = new Command("sender_test", "recipient_test", "command_test");
        test.setPayload(o);
        Assertions.assertEquals(o, test.getPayload());
        Assertions.assertEquals("sender_test", test.getSender());
        Assertions.assertEquals("recipient_test", test.getRecipient());
        Assertions.assertEquals("command_test", test.getCommand());
    }

    @Test
    void methodTest_getPayloadClass() {
        Object o = new Object();
        Command test = Command.create("sender_test", "recipient_test", "command_test", o);
        Assertions.assertEquals(o.getClass(), test.getPayloadClass());
    }

    @Test
    void methodTest_equals() {
        Object o = new Object();
        Command test1 = Command.create("sender_test", "recipient_test", "command_test", o);
        Command test2 = Command.create("sender_test", "recipient_test", "command_test", o);
        Assertions.assertTrue(test1.equals(test2));
    }

    @Test
    void methodTest_hashCode() {
        Object o = new Object();
        Command test1 = Command.create("sender_test", "recipient_test", "command_test", o);
        Command test2 = Command.create("sender_test", "recipient_test", "command_test", o);
        Assertions.assertEquals(test1.hashCode(), test2.hashCode());
    }

    @Test
    void methodTest_toString() {
        Command test = Command.create("sender_test", "recipient_test", "command_test", null);
        Assertions.assertTrue(test.toString().length() > 0);
    }
}