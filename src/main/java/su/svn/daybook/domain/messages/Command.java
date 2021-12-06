/*
 * This file was last modified at 2021.12.06 18:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Command.java
 * $Id$
 */

package su.svn.daybook.domain.messages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;

public class Command implements Serializable {

    private static final long serialVersionUID = 8774569874565187574L;

    public static final String PING = "PING";

    public static final String PONG = "PONG";

    public static final String SEND = "SEND";

    public static final String DEFAULT_COMMAND = SEND;

    private final String command;

    private String recipient;

    private final String sender;

    private Object payload;

    private Class<?> payloadClass;

    public Command(@Nonnull String sender, @Nonnull String recipient, @Nonnull String command) {
        this.command = command;
        this.recipient = recipient;
        this.sender = sender;
    }

    private Command(String sender, String recipient, String command, Object payload, Class<?> payloadClass) {
        this.command = command;
        this.recipient = recipient;
        this.payload = payload;
        this.payloadClass = payloadClass;
        this.sender = sender;
    }

    public static Command createPing(@Nonnull String sender, @Nonnull String recipient) {
        return new Command(sender, recipient, PING);
    }

    public static Command createPongOf(@Nonnull Command ping) {
        Command command = new Command(ping.getRecipient(), ping.getSender(), PONG);
        command.setRecipient(ping.getSender());
        return command;
    }

    public static <T> Command createSend(@Nonnull String sender, @Nonnull String recipient, @Nonnull T o) {
        return create(sender, recipient, DEFAULT_COMMAND, o);
    }

    /** @noinspection unchecked*/
    public static <T> Command create(@Nonnull String sender, @Nonnull String recipient, @Nonnull String command, T o) {
        Class<T> tClass = (o != null) ? (Class<T>) o.getClass() : null;
        return new Command(sender, recipient, command, o, tClass);
    }

    @Nonnull
    public String getCommand() {
        return command;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(@Nonnull String recipient) {
        this.recipient = recipient;
    }

    @Nonnull
    public String getSender() {
        return sender;
    }

    @Nullable
    public Object getPayload() {
        return payload;
    }

    public <T> void setPayload(T o) {
        //noinspection unchecked
        Class<T> tClass = (Class<T>) o.getClass();
        this.payload = o;
        this.payloadClass = tClass;
    }

    public Class<?> getPayloadClass() {
        return payloadClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Command command1 = (Command) o;

        if (!command.equals(command1.command)) return false;
        if (recipient != null ? !recipient.equals(command1.recipient) : command1.recipient != null) return false;
        if (!sender.equals(command1.sender)) return false;
        if (payload != null ? !payload.equals(command1.payload) : command1.payload != null) return false;
        return payloadClass != null ? payloadClass.equals(command1.payloadClass) : command1.payloadClass == null;
    }

    @Override
    public int hashCode() {
        int result = command.hashCode();
        result = 31 * result + (recipient != null ? recipient.hashCode() : 0);
        result = 31 * result + sender.hashCode();
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        result = 31 * result + (payloadClass != null ? payloadClass.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Command{" +
                "command='" + command + '\'' +
                ", recipient='" + recipient + '\'' +
                ", sender='" + sender + '\'' +
                ", payload=" + payload +
                ", payloadClass=" + ((payloadClass != null) ? payloadClass.getCanonicalName() : "null") +
                '}';
    }
}