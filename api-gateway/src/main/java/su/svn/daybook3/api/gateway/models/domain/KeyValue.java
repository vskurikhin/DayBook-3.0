/*
 * This file was last modified at 2024-05-14 21:36 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * KeyValue.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.vertx.core.json.JsonObject;
import su.svn.daybook3.api.gateway.annotations.DomainField;
import su.svn.daybook3.api.gateway.domain.model.KeyValueTable;
import su.svn.daybook3.api.gateway.models.UUIDIdentification;

import jakarta.annotation.Nonnull;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;
import java.util.UUID;

public final class KeyValue implements UUIDIdentification, Serializable {

    public static final String NONE = KeyValueTable.NONE;
    @Serial
    private static final long serialVersionUID = 3421670798382710094L;
    @JsonProperty
    @DomainField
    private final UUID id;
    @JsonProperty
    @DomainField(nullable = false)
    private final BigInteger key;
    @JsonProperty
    @DomainField
    private final JsonObject value;
    @JsonProperty
    @DomainField
    private final boolean visible;
    @JsonProperty
    @DomainField
    private final int flags;

    @JsonIgnore
    private transient int hash;

    @JsonIgnore
    private transient boolean hashIsZero;

    public KeyValue() {
        this(null, BigInteger.ZERO, null, true, 0);
    }

    public KeyValue(
            UUID id,
            @Nonnull BigInteger key,
            JsonObject value,
            boolean visible,
            int flags) {
        this.id = id;
        this.key = key;
        this.value = value;
        this.visible = visible;
        this.flags = flags;
    }

    public static Builder builder() {
        return new KeyValue.Builder();
    }

    public Builder toBuilder() {
        return builder()
                .id(this.id)
                .key(this.key)
                .value(this.value)
                .visible(this.visible)
                .flags(this.flags);
    }

    public UUID id() {
        return id;
    }

    public BigInteger key() {
        return key;
    }

    public JsonObject value() {
        return value;
    }

    public boolean visible() {
        return visible;
    }

    public int flags() {
        return flags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (KeyValue) o;
        return visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(key, that.key)
                && Objects.equals(value, that.value);
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
        return Objects.hash(id, key, value, visible, flags);
    }

    @Override
    public String toString() {
        return "KeyValue{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private UUID id;
        private @Nonnull BigInteger key;
        private JsonObject value;
        private boolean visible;
        private int flags;

        private Builder() {
            this.key = BigInteger.ZERO;
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder key(@Nonnull BigInteger key) {
            this.key = key;
            return this;
        }

        public Builder value(JsonObject value) {
            this.value = value;
            return this;
        }

        public Builder visible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public Builder flags(int flags) {
            this.flags = flags;
            return this;
        }

        public KeyValue build() {
            return new KeyValue(id, key, value, visible, flags);
        }
    }
}
