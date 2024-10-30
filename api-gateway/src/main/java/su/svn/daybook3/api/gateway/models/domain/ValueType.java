/*
 * This file was last modified at 2024-10-29 23:50 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ValueType.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import su.svn.daybook3.annotations.DomainField;
import su.svn.daybook3.api.gateway.domain.model.ValueTypeTable;
import su.svn.daybook3.models.LongIdentification;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public final class ValueType implements LongIdentification, Serializable {

    public static final String NONE = ValueTypeTable.NONE;
    @Serial
    private static final long serialVersionUID = 7637967203652690721L;
    @JsonProperty
    @DomainField
    private final Long id;
    @JsonProperty
    @DomainField(nullable = false)
    private final String valueType;
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

    public ValueType() {
        this(null, NONE, true, 0);
    }

    public ValueType(
            Long id,
            @Nonnull String valueType,
            boolean visible,
            int flags) {
        this.id = id;
        this.valueType = valueType;
        this.visible = visible;
        this.flags = flags;
    }

    public static Builder builder() {
        return new ValueType.Builder();
    }

    public Builder toBuilder() {
        return builder()
                .id(this.id)
                .valueType(this.valueType)
                .visible(this.visible)
                .flags(this.flags);
    }

    public Long id() {
        return id;
    }

    public String valueType() {
        return valueType;
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
        var that = (ValueType) o;
        return visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(valueType, that.valueType);
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
        return Objects.hash(id, valueType, visible, flags);
    }

    @Override
    public String toString() {
        return "ValueType{" +
                "id=" + id +
                ", valueType='" + valueType + '\'' +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private Long id;
        private @Nonnull String valueType;
        private boolean visible;
        private int flags;

        private Builder() {
            this.valueType = NONE;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder valueType(@Nonnull String valueType) {
            this.valueType = valueType;
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

        public ValueType build() {
            return new ValueType(id, valueType, visible, flags);
        }
    }
}
