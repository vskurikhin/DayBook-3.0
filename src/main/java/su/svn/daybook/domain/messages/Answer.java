/*
 * This file was last modified at 2021.12.06 18:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Answer.java
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
public final class Answer implements Serializable {

    @Serial
    private static final long serialVersionUID = 4530969986917184578L;

    public static final String DEFAULT_MESSAGE = "ANSWER";

    public static final String EMPTY = "EMPTY";

    public static final String NO_SUCH_ELEMENT = "no such element";

    private final int error;

    private final String message;

    private Object payload;

    @JsonIgnore
    private transient Class<?> payloadClass;

    @JsonIgnore
    private transient volatile int hash;

    @JsonIgnore
    private transient volatile boolean hashIsZero;

    Answer() {
        this(DEFAULT_MESSAGE, 0, EMPTY);
    }

    public Answer(@Nonnull String message) {
        this(message, 0, null);
    }

    public Answer(@Nonnull String message, int error) {
        this(message, error, null);
    }

    private Answer(String message, int error, Object payload) {
        this.error = error;
        this.message = message;
        this.payload = payload;
        this.payloadClass = payload != null ? payload.getClass() : null;
    }

    public static Answer empty() {
        return new Answer(NO_SUCH_ELEMENT, 404, EMPTY);
    }

    public static Answer noNumber(String exceptionMessage) {
        return new Answer("string does not have the number format", 404, exceptionMessage);
    }

    public static <T> Answer of(@Nonnull T o) {
        return create(DEFAULT_MESSAGE, o);
    }

    public static Answer create(@Nonnull String message, @Nonnull Object o) {
        return new Answer(message, 200, o);
    }

    @Nonnull
    public String getMessage() {
        return message;
    }

    public int getError() {
        return error;
    }

    @Nullable
    public Object getPayload() {
        return payload;
    }

    public void setPayload(@Nonnull Object o) {
        this.payload = o;
        this.payloadClass = o.getClass();
    }

    public Class<?> getPayloadClass() {
        return payloadClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Answer answer = (Answer) o;
        return error == answer.error
                && Objects.equals(message, answer.message)
                && Objects.equals(payload, answer.payload)
                && Objects.equals(payloadClass, answer.payloadClass);
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
        return Objects.hash(message, error, payload, payloadClass);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "message='" + message + '\'' +
                ", error=" + error +
                ", payload=" + payload +
                ", payloadClass=" + ((payloadClass != null) ? payloadClass.getCanonicalName() : "null") +
                '}';
    }

    public static Builder builder() {
        return new Answer.Builder();
    }

    public static final class Builder {
        private String message;
        private int error;
        private Object payload;

        private Builder() {
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder error(int error) {
            this.error = error;
            return this;
        }

        public Builder payload(@Nonnull Object payload) {
            this.payload = payload;
            return this;
        }

        public Builder but() {
            return Answer.builder()
                    .message(message)
                    .error(error)
                    .payload(payload);
        }

        public Answer build() {
            Answer answer = new Answer(message != null ? message : EMPTY, error);
            if (payload != null) {
                answer.setPayload(payload);
            }
            return answer;
        }
    }
}
