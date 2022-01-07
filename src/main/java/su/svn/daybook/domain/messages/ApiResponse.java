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
public class ApiResponse {

    private Long id;

    private String message;

    private Integer error;

    private Object payload;

    private Class<?> payloadClass;

    public ApiResponse() {}

    public ApiResponse(Long id) {
        this.id = id;
    }

    public ApiResponse(String message) {
        this.message = message;
    }

    public ApiResponse(Long id, String message) {
        this.id = id;
        this.message = message;
    }

    public <T> ApiResponse(Long id, String message, Integer error, T payload) {
        this.id = id;
        this.message = message;
        this.error = error;
        this.payload = payload;
        this.payloadClass = payload.getClass();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Object payload) {
        this.payload = payload;
        this.payloadClass = payload.getClass();
    }

    public Class<?> getPayloadClass() {
        return payloadClass;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApiResponse that = (ApiResponse) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (message != null ? !message.equals(that.message) : that.message != null) return false;
        if (error != null ? !error.equals(that.error) : that.error != null) return false;
        if (payload != null ? !payload.equals(that.payload) : that.payload != null) return false;
        return payloadClass != null ? payloadClass.equals(that.payloadClass) : that.payloadClass == null;
    }

    @Override
    public int hashCode() {
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

    public static Builder builder() {
        return new ApiResponse.Builder();
    }

    public static final class Builder {
        private Long id;
        private String message;
        private Integer error;
        private Object payload;

        private Builder() {
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withError(Integer error) {
            this.error = error;
            return this;
        }

        public Builder withPayload(Object payload) {
            this.payload = payload;
            return this;
        }

        public Builder but() {
            return builder().withId(id)
                    .withMessage(message)
                    .withError(error)
                    .withPayload(payload);
        }

        public ApiResponse build() {
            ApiResponse apiResponse = new ApiResponse();
            apiResponse.setId(id);
            apiResponse.setMessage(message);
            apiResponse.setError(error);
            apiResponse.setPayload(payload);
            return apiResponse;
        }
    }
}