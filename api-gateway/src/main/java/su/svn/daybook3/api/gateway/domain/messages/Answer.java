/*
 * This file was last modified at 2024-05-14 21:36 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Answer.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

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

    @JsonProperty
    private final int error;

    @JsonProperty
    private final String message;

    @JsonProperty
    private Object payload;

    @JsonIgnore
    private transient Class<?> payloadClass;

    @JsonIgnore
    private transient int hash;

    @JsonIgnore
    private transient boolean hashIsZero;

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

    public static <T> Answer of(@Nonnull T o) {
        return create(DEFAULT_MESSAGE, o);
    }

    public static <T> Answer from(@Nonnull T o, int error) {
        return new Answer(DEFAULT_MESSAGE, error, o);
    }

    public static Answer create(@Nonnull String message, @Nonnull Object o) {
        return new Answer(message, 200, o);
    }

    @Nonnull
    public String message() {
        return message;
    }

    public int error() {
        return error;
    }

    @Nullable
    public Object payload() {
        return payload;
    }

    public void payload(@Nonnull Object o) {
        this.payload = o;
        this.payloadClass = o.getClass();
    }

    public Class<?> payloadClass() {
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
        return Objects.hash(error, message, payload, payloadClass);
    }

    @Override
    public String toString() {
        return "Answer{" +
                "error=" + error +
                ", message='" + message + '\'' +
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
                answer.payload(payload);
            }
            return answer;
        }
    }
}
