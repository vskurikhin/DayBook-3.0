/*
 * This file was last modified at 2021.12.06 18:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Command.java
 * $Id$
 */

package su.svn.daybook.domain.messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Command implements Serializable {

    public static final String BROADCAST = "*";
    public static final String PING = "PING";
    public static final String PONG = "PONG";
    public static final String SEND = "SEND";
    public static final String DEFAULT_COMMAND = SEND;
    @Serial
    private static final long serialVersionUID = 8774569874565187574L;
    private final String command;

    private final String recipient;

    private final String sender;

    private final Object payload;

    private final Class<?> payloadClass;

    @JsonIgnore
    private transient volatile int hash;

    @JsonIgnore
    private transient volatile boolean hashIsZero;

    Command() {
        this(BROADCAST, BROADCAST, DEFAULT_COMMAND);
    }

    public Command(@Nonnull String sender, @Nonnull String recipient, @Nonnull String command) {
        this(sender, recipient, command, null);
    }

    private Command(String sender, String recipient, String command, Object payload) {
        this.command = command;
        this.recipient = recipient;
        this.payload = payload;
        this.payloadClass = payload != null ? payload.getClass() : null;
        this.sender = sender;
    }

    public static Command createPing(@Nonnull String sender, @Nonnull String recipient) {
        return new Command(sender, recipient, PING);
    }

    public static Command createPongOf(@Nonnull Command ping) {
        return Command.builder()
                .command(PONG)
                .recipient(ping.getSender())
                .sender(ping.getRecipient())
                .build();
    }

    public static <T> Command createSend(@Nonnull String sender, @Nonnull String recipient, @Nonnull T o) {
        return create(sender, recipient, DEFAULT_COMMAND, o);
    }

    public static <T> Command create(@Nonnull String sender, @Nonnull String recipient, @Nonnull String command, T o) {
        return new Command(sender, recipient, command, o);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Nonnull
    public String getCommand() {
        return command;
    }

    public String getRecipient() {
        return recipient;
    }

    @Nonnull
    public String getSender() {
        return sender;
    }

    @Nullable
    public Object getPayload() {
        return payload;
    }

    public Class<?> getPayloadClass() {
        return payloadClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Command command1 = (Command) o;
        return Objects.equals(command, command1.command)
                && Objects.equals(recipient, command1.recipient)
                && Objects.equals(sender, command1.sender)
                && Objects.equals(payload, command1.payload);
    }

    @Override
    public int hashCode() {
        int h = hash;
        if (h == 0 && !hashIsZero) {
            h = calculateHashCode();
            if (h == 0) {
                hashIsZero = true;
            } else {
                hash = h;
            }
        }
        return h;
    }

    private int calculateHashCode() {
        return Objects.hash(command, recipient, sender, payload);
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

    public static final class Builder {
        private String command;
        private String recipient;
        private String sender;
        private Object payload;

        private Builder() {
        }

        public Builder command(@Nonnull String command) {
            this.command = command;
            return this;
        }

        public Builder recipient(@Nonnull String recipient) {
            this.recipient = recipient;
            return this;
        }

        public Builder sender(@Nonnull String sender) {
            this.sender = sender;
            return this;
        }

        public Builder payload(Object payload) {
            this.payload = payload;
            return this;
        }

        public Command build() {
            return new Command(sender, recipient, command, payload);
        }
    }
}