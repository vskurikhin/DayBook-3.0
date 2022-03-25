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
public final class DictionaryResponse<I> {

    private final I id;

    private final String code;

    private final String key;

    private final String word;

    private final String message;

    private final Integer error;

    private final Object payload;

    private final Class<?> payloadClass;

    private final int hashCode;

    private DictionaryResponse() {
        this(null, null, null, null, null, null, null);
    }

    public DictionaryResponse(I id, String code, String key, String word, String message, Integer error, Object payload) {
        this.id = id;
        this.code = code;
        this.key = key;
        this.word = word;
        this.message = message;
        this.error = error;
        this.payload = payload;
        this.payloadClass = payload != null ? payload.getClass() : null;
        this.hashCode = hashCodeInt();
    }

    public static DictionaryResponse<Object> code(String code) {
        return new DictionaryResponse<>(null, code, null, null, null, null, null);
    }

    public static <T> DictionaryResponse<Object> code(String code, T payload) {
        return new DictionaryResponse<>(null, code, null, null, null, null, payload);
    }

    public static DictionaryResponse<Object> code(String code, String message, Integer error) {
        return new DictionaryResponse<>(null, code, null, null, message, error, null);
    }

    public static <T> DictionaryResponse<Object> code(String code, String message, Integer error, T payload) {
        return new DictionaryResponse<>(null, code, null, null, message, error, payload);
    }

    public static DictionaryResponse<Object> key(String key) {
        return new DictionaryResponse<>(null, null, key, null, null, null, null);
    }

    public static <T> DictionaryResponse<Object> key(String key, T payload) {
        return new DictionaryResponse<>(null, null, key, null, null, null, payload);
    }

    public static DictionaryResponse<Object> key(String key, String message, Integer error) {
        return new DictionaryResponse<>(null, null, key, null, message, error, null);
    }

    public static <T> DictionaryResponse<Object> key(String key, String message, Integer error, T payload) {
        return new DictionaryResponse<>(null, null, key, null, message, error, payload);
    }

    public static DictionaryResponse<Object> word(String word) {
        return new DictionaryResponse<>(null, null, null, word, null, null, null);
    }

    public static <T> DictionaryResponse<Object> word(String word, T payload) {
        return new DictionaryResponse<>(null, null, null, word, null, null, payload);
    }

    public static DictionaryResponse<Object> word(String word, String message, Integer error) {
        return new DictionaryResponse<>(null, null, null, word, message, error, null);
    }

    public static <T> DictionaryResponse<Object> word(String word, String message, Integer error, T payload) {
        return new DictionaryResponse<>(null, null, null, word, message, error, payload);
    }

    public static DictionaryResponse<Object> message(String message) {
        return new DictionaryResponse<>(null, null, null, null, message, null, null);
    }

    public I getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getKey() {
        return key;
    }

    public String getWord() {
        return word;
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

        DictionaryResponse<?> that = (DictionaryResponse<?>) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (code != null ? !code.equals(that.code) : that.code != null) return false;
        if (key != null ? !key.equals(that.key) : that.key != null) return false;
        if (word != null ? !word.equals(that.word) : that.word != null) return false;
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
        result = 31 * result + (code != null ? code.hashCode() : 0);
        result = 31 * result + (key != null ? key.hashCode() : 0);
        result = 31 * result + (word != null ? word.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (error != null ? error.hashCode() : 0);
        result = 31 * result + (payload != null ? payload.hashCode() : 0);
        result = 31 * result + (payloadClass != null ? payloadClass.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DictionaryResponse{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", key='" + key + '\'' +
                ", word='" + word + '\'' +
                ", message='" + message + '\'' +
                ", error=" + error +
                ", payload=" + payload +
                ", payloadClass=" + payloadClass +
                '}';
    }

    public static <T> DictionaryResponse.Builder<T> builder() {
        return new DictionaryResponse.Builder<>();
    }

    public static final class Builder<J> {
        private J id;
        private String code;
        private String key;
        private String word;
        private String message;
        private Integer error;
        private Object payload;

        private Builder() {
        }

        public Builder<J> withId(J id) {
            this.id = id;
            return this;
        }

        public Builder<J> withCode(String code) {
            this.code = code;
            return this;
        }

        public Builder<J> withKey(String key) {
            this.key = key;
            return this;
        }

        public Builder<J> withWord(String word) {
            this.word = word;
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

        public DictionaryResponse<J> build() {
            return new DictionaryResponse<>(id, code, key, word, message, error, payload);
        }
    }
}
