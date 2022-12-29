/*
 * This file was last modified at 2021.12.20 15:14 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ApiResponse.java
 * $Id$
 */

package su.svn.daybook.domain.messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ApiResponse<I extends Comparable<? extends Serializable>> {

    private final I id;

    private final Integer error;

    private final String message;

    private final Object payload;

    private transient final Class<?> payloadClass;

    @JsonIgnore
    private transient volatile int hash;

    @JsonIgnore
    private transient volatile boolean hashIsZero;

    ApiResponse() {
        this(null, null, null, null);
    }

    public ApiResponse(I id) {
        this(id, null, null, null);
    }

    public ApiResponse(I id, Object payload) {
        this(id, null, null, payload);
    }

    private ApiResponse(I id, String message, Integer error, Object payload) {
        this.id = id;
        this.message = message;
        this.error = error;
        this.payload = payload;
        this.payloadClass = payload != null ? payload.getClass() : null;
    }

    public static ApiResponse<String> message(String message) {
        return new ApiResponse<>(null, message, null, null);
    }

    public static <T extends Comparable<? extends Serializable>> ApiResponse.Builder<T> builder() {
        return new ApiResponse.Builder<>();
    }

    public I getId() {
        return id;
    }

    public Integer getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }

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
        ApiResponse<?> that = (ApiResponse<?>) o;
        return Objects.equals(id, that.id)
                && Objects.equals(message, that.message)
                && Objects.equals(error, that.error)
                && Objects.equals(payload, that.payload);
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
        return Objects.hash(id, message, error, payload);
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", error=" + error +
                ", payload=" + payload +
                ", payloadClass=" + ((payloadClass != null) ? payloadClass.getCanonicalName() : "null") +
                '}';
    }

    public static final class Builder<J extends Comparable<? extends Serializable>> {
        private J id;
        private Integer error;
        private String message;
        private Object payload;

        private Builder() {
        }

        public Builder<J> id(J id) {
            this.id = id;
            return this;
        }

        public Builder<J> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<J> error(Integer error) {
            this.error = error;
            return this;
        }

        public Builder<J> payload(Object payload) {
            this.payload = payload;
            return this;
        }

        public ApiResponse<J> build() {
            return new ApiResponse<>(id, message, error, payload);
        }
    }
}
