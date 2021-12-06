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

    public static final String DEFAULT_MESSAGE = "COMMAND";

    public static final String EMPTY = "EMPTY";

    private final String message;

    private Object payload;

    private Class<?> payloadClass;

    public Command(@Nonnull String message) {
        this.message = message;
    }

    private Command(String message, Object payload, Class<?> payloadClass) {
        this.message = message;
        this.payload = payload;
        this.payloadClass = payloadClass;
    }

    public static Command empty() {
        return new Command(EMPTY);
    }

    public static <T> Command of(@Nonnull T o) {
        return create(DEFAULT_MESSAGE, o);
    }

    /** @noinspection unchecked*/
    public static <T> Command create(@Nonnull String message, T o) {
        Class<T> tClass = (Class<T>) o.getClass();
        return new Command(message, o, tClass);
    }

    @Nonnull
    public String getMessage() {
        return message;
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
        if (!(o instanceof Command)) return false;

        Command answer = (Command) o;

        if (!message.equals(answer.message)) return false;
        if (payload != null ? !payload.equals(answer.payload) : answer.payload != null) return false;
        return payloadClass != null ? payloadClass.equals(answer.payloadClass) : answer.payloadClass == null;
    }

    @Override
    public int hashCode() {
        int result = message.hashCode();
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        result = 31 * result + (payloadClass != null ? payloadClass.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Command{" +
                "message='" + message + '\'' +
                ", payload=" + payload +
                ", payloadClass=" + ((payloadClass != null) ? payloadClass.getCanonicalName() : "null") +
                '}';
    }
}