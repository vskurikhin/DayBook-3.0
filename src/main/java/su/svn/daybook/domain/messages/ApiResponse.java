/*
 * This file was last modified at 2021.12.20 15:14 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ApiResponse.java
 * $Id$
 */

package su.svn.daybook.domain.messages;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ApiResponse<I> {

    private final I id;

    private final String message;

    private final Integer error;

    private final Object payload;

    private final Class<?> payloadClass;

    private final int hashCode;

    private ApiResponse() {
        this(null, null, null, null);
    }

    public ApiResponse(I id) {
        this(id, null, null, null);
    }

    private ApiResponse(I id, String message, Integer error, Object payload) {
        this.id = id;
        this.message = message;
        this.error = error;
        this.payload = payload;
        this.payloadClass = payload != null ? payload.getClass() : null;
        this.hashCode = hashCodeInt();
    }

    public static ApiResponse<Object> message(String message) {
        return new ApiResponse<>(null, message, null, null);
    }

    public I getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public Integer getError() {
        return error;
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

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (error != null ? !error.equals(that.error) : that.error != null) return false;
        if (payload != null ? !payload.equals(that.payload) : that.payload != null) return false;
        return payloadClass != null ? payloadClass.equals(that.payloadClass) : that.payloadClass == null;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }


    private int hashCodeInt() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        result = 31 * result + (payloadClass != null ? payloadClass.hashCode() : 0);
        return result;
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

    public static <T> ApiResponse.Builder<T> builder() {
        return new ApiResponse.Builder<>();
    }

    public static final class Builder<J> {
        private J id;
        private String message;
        private Integer error;
        private Object payload;

        private Builder() {
        }

        public Builder<J> withId(J id) {
            this.id = id;
            return this;
        }

        public Builder<J> withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder<J> withError(Integer error) {
            this.error = error;
            return this;
        }

        public Builder<J> withPayload(Object payload) {
            this.payload = payload;
            return this;
        }

        public ApiResponse.Builder<J> but() {
            return ApiResponse.<J>builder()
                    .withId(id)
                    .withMessage(message)
                    .withError(error)
                    .withPayload(payload);
        }

        public ApiResponse<J> build() {
            return new ApiResponse<>(id, message, error, payload);
        }
    }
}
