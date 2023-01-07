/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * KeyValue.java
 * $Id$
 */

package su.svn.daybook.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.vertx.core.json.JsonObject;
import su.svn.daybook.annotations.DomainField;
import su.svn.daybook.models.UUIDIdentification;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class KeyValue implements UUIDIdentification, Serializable {

    public static final String NONE = "d94d93d9-d44c-403c-97b1-d071b6974d80";
    public static final String ID = "id";
    @Serial
    private static final long serialVersionUID = 3421670798382710094L;
    @DomainField
    private final UUID id;
    @DomainField(nullable = false)
    private final BigInteger key;
    @DomainField
    private final JsonObject value;
    @DomainField
    private final boolean visible;
    @DomainField
    private final int flags;

    @JsonIgnore
    private transient volatile int hash;

    @JsonIgnore
    private transient volatile boolean hashIsZero;

    public KeyValue() {
        this.id = null;
        this.key = BigInteger.ZERO;
        this.value = null;
        this.visible = true;
        this.flags = 0;
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

    public static KeyValue.Builder builder() {
        return new KeyValue.Builder();
    }

    public UUID getId() {
        return id;
    }

    public BigInteger getKey() {
        return key;
    }

    public JsonObject getValue() {
        return value;
    }

    public boolean getVisible() {
        return visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getFlags() {
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
        private BigInteger key;
        private JsonObject value;
        private boolean visible;
        private int flags;

        private Builder() {
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
