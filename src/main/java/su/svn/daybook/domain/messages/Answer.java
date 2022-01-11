/*
 * This file was last modified at 2021.12.06 18:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Answer.java
 * $Id$
 */

package su.svn.daybook.domain.messages;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.Serializable;

public class Answer implements Serializable {

    private static final long serialVersionUID = 4530969986917184578L;

    public static final String DEFAULT_MESSAGE = "ANSWER";

    public static final String EMPTY = "EMPTY";

    private final String message;

    private final int error;

    private Object payload;

    private Class<?> payloadClass;

    public Answer(@Nonnull String message) {
        this.message = message;
        this.error = 0;
    }

    public Answer(@Nonnull String message, int error) {
        this.message = message;
        this.error = error;
    }

    private Answer(String message, int error, Object payload, Class<?> payloadClass) {
        this.message = message;
        this.error = error;
        this.payload = payload;
        this.payloadClass = payloadClass;
    }

    public static Answer empty() {
        return new Answer(EMPTY, 404);
    }

    public static <T> Answer of(@Nonnull T o) {
        return create(DEFAULT_MESSAGE, o);
    }

    public static Answer create(@Nonnull String message, @Nonnull Object o) {
        return new Answer(message, 0, o, o.getClass());
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

        if (error != answer.error) return false;
        if (!message.equals(answer.message)) return false;
        if (payload != null ? !payload.equals(answer.payload) : answer.payload != null) return false;
        return payloadClass != null ? payloadClass.equals(answer.payloadClass) : answer.payloadClass == null;
    }

    @Override
    public int hashCode() {
        int result = message.hashCode();
        result = 31 * result + error;
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        result = 31 * result + (payloadClass != null ? payloadClass.hashCode() : 0);
        return result;
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

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withError(int error) {
            this.error = error;
            return this;
        }

        public Builder withPayload(@Nonnull Object payload) {
            this.payload = payload;
            return this;
        }

        public Builder but() {
            return Answer.builder()
                    .withMessage(message)
                    .withError(error)
                    .withPayload(payload);
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
