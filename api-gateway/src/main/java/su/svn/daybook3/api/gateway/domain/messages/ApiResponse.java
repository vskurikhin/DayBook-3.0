/*
 * This file was last modified at 2024-05-14 21:25 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ApiResponse.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.messages;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

public final class ApiResponse<I extends Comparable<? extends Serializable>> {

    public static final int BAD_REQUEST = 400;
    @JsonProperty
    private final I id;

    @JsonProperty
    private final Integer code;

    @JsonProperty
    private final Integer error;

    @JsonProperty
    private final String message;

    @JsonProperty
    private final Object payload;

    @JsonIgnore
    private transient final Class<?> payloadClass;

    @JsonIgnore
    private transient int hash;

    @JsonIgnore
    private transient boolean hashIsZero;

    ApiResponse() {
        this(null, null, null, null);
    }

    public ApiResponse(I id) {
        this(id, null, null, null);
    }

    public ApiResponse(I id, int error) {
        this(id, null, error, null);
    }

    public ApiResponse(I id, Object payload) {
        this(id, null, null, payload);
    }

    public ApiResponse(I id, int error, Object payload) {
        this(id, null, error, payload);
    }

    private ApiResponse(I id, String message, Integer error, Object payload) {
        this.id = id;
        this.message = message;
        if (error != null && error < BAD_REQUEST) {
            this.code = error;
            this.error = null;
        } else {
            this.code = null;
            this.error = error;
        }
        this.payload = payload;
        this.payloadClass = payload != null ? payload.getClass() : null;
    }

    public static ApiResponse<String> auth(Object payload) {
        return new ApiResponse<>(null, null, 202, payload);
    }

    public static <T extends Comparable<? extends Serializable>> ApiResponse.Builder<T> builder() {
        return new ApiResponse.Builder<>();
    }

    public I id() {
        return id;
    }

    public Integer code() {
        return code;
    }

    public Integer error() {
        return error;
    }

    public String message() {
        return message;
    }

    public Object payload() {
        return payload;
    }

    public Class<?> payloadClass() {
        return payloadClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ApiResponse<?> that = (ApiResponse<?>) o;
        return Objects.equals(id, that.id)
                && Objects.equals(code, that.code)
                && Objects.equals(error, that.error)
                && Objects.equals(message, that.message)
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
        return Objects.hash(id, code, error, message, payload);
    }

    @Override
    public String toString() {
        return "ApiResponse{" +
                "id=" + id +
                ", code=" + code +
                ", error=" + error +
                ", message='" + message + '\'' +
                ", payload=" + payload +
                ", payloadClass=" + ((payloadClass != null) ? payloadClass.getCanonicalName() : "null") +
                '}';
    }

    public static final class Builder<J extends Comparable<? extends Serializable>> {
        private J id;
        private Integer code;
        private Integer error;
        private String message;
        private Object payload;

        private Builder() {
        }

        public Builder<J> id(J id) {
            this.id = id;
            return this;
        }

        public Builder<J> code(Integer code) {
            if (code != null && code < BAD_REQUEST) {
                this.code = code;
            }
            return this;
        }

        public Builder<J> error(Integer error) {
            this.error = error;
            return this;
        }

        public Builder<J> message(String message) {
            this.message = message;
            return this;
        }

        public Builder<J> payload(Object payload) {
            this.payload = payload;
            return this;
        }

        public ApiResponse<J> build() {
            return new ApiResponse<>(id, message, code != null ? code : error, payload);
        }
    }
}
