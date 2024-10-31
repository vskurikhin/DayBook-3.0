/*
 * This file was last modified at 2024-10-30 17:26 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Setting.java
 * $Id$
 */

package su.svn.daybook3.api.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import su.svn.daybook3.annotations.DomainField;
import su.svn.daybook3.api.domain.model.SettingTable;
import su.svn.daybook3.models.LongIdentification;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public final class Setting implements LongIdentification, Serializable {

    public static final String NONE = SettingTable.NONE;
    public static final String DEFAULT_TYPE = SettingTable.DEFAULT_TYPE;
    @Serial
    private static final long serialVersionUID = 2421049451978985245L;
    @JsonProperty
    @DomainField
    private final Long id;
    @JsonProperty
    @DomainField(nullable = false)
    private final String variable;
    @JsonProperty
    @DomainField
    private final String value;
    @JsonProperty
    @DomainField
    private final String valueType;
    @JsonProperty
    @DomainField
    private final Long stanzaId;
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

    public Setting() {
        this(null, NONE, null, "Object", 0L, true, 0);
    }

    public Setting(
            Long id,
            @Nonnull String variable,
            String value,
            @Nonnull String valueType,
            @Nonnull Long stanzaId,
            boolean visible,
            int flags) {
        this.id = id;
        this.variable = variable;
        this.value = value;
        this.valueType = valueType;
        this.stanzaId = stanzaId;
        this.visible = visible;
        this.flags = flags;
    }

    public static Builder builder() {
        return new Setting.Builder();
    }

    public Builder toBuilder() {
        return builder()
                .id(this.id)
                .variable(this.variable)
                .value(this.value)
                .valueType(this.valueType)
                .stanzaId(this.stanzaId)
                .visible(this.visible)
                .flags(this.flags);
    }

    public Long id() {
        return id;
    }

    public String variable() {
        return variable;
    }

    public String value() {
        return value;
    }

    public String valueType() {
        return valueType;
    }

    public Long stanzaId() {
        return stanzaId;
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
        var that = (Setting) o;
        return visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(variable, that.variable)
                && Objects.equals(value, that.value)
                && Objects.equals(valueType, that.valueType)
                && Objects.equals(stanzaId, that.stanzaId);
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
        return Objects.hash(id, variable, value, valueType, stanzaId, visible, flags);
    }

    @Override
    public String toString() {
        return "Setting{" +
                "id=" + id +
                ", variable='" + variable + '\'' +
                ", value='" + value + '\'' +
                ", valueType='" + valueType + '\'' +
                ", stanzaId='" + stanzaId + '\'' +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private Long id;
        private @Nonnull String variable;
        private String value;
        private @Nonnull String valueType;
        private @Nonnull Long stanzaId;
        private boolean visible;
        private int flags;

        private Builder() {
            this.variable = NONE;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder variable(@Nonnull String variable) {
            this.variable = variable;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder valueType(@Nonnull String valueType) {
            this.valueType = valueType;
            return this;
        }

        public Builder stanzaId(long stanzaId) {
            this.stanzaId = stanzaId;
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

        public Setting build() {
            return new Setting(id, variable, value, valueType, stanzaId, visible, flags);
        }
    }
}
