/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ValueType.java
 * $Id$
 */

package su.svn.daybook.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import su.svn.daybook.domain.model.LongIdentification;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ValueType implements LongIdentification, Serializable {

    public static final String NONE = "8af24446-2ca5-4ed3-8d80-f2681feb0ecc";
    public static final String ID = "id";
    @Serial
    private static final long serialVersionUID = -1768184188117328599L;
    private final Long id;
    private final String valueType;
    private final boolean visible;
    private final int flags;

    @JsonIgnore
    private transient volatile int hash;

    @JsonIgnore
    private transient volatile boolean hashIsZero;

    public ValueType() {
        this.id = null;
        this.valueType = NONE;
        this.visible = true;
        this.flags = 0;
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

    public static ValueType.Builder builder() {
        return new ValueType.Builder();
    }

    public Long getId() {
        return id;
    }

    public String getValueType() {
        return valueType;
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
        private String valueType;
        private boolean visible;
        private int flags;

        private Builder() {
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
