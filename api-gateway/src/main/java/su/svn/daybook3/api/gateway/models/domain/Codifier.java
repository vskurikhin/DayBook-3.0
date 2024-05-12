/*
 * This file was last modified at 2024-05-14 21:35 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Codifier.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import su.svn.daybook3.api.gateway.annotations.DomainField;
import su.svn.daybook3.api.gateway.domain.model.CodifierTable;
import su.svn.daybook3.api.gateway.models.StringIdentification;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public final class Codifier implements StringIdentification, Serializable {

    public static final String NONE = CodifierTable.NONE;
    @Serial
    private static final long serialVersionUID = -5560375697484076940L;
    @JsonProperty
    @DomainField
    private final String code;
    @JsonProperty
    @DomainField
    private final String value;
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

    public Codifier() {
        this(Codifier.NONE, null, true, 0);
    }

    public Codifier(
            String code,
            String value,
            boolean visible,
            int flags) {
        this.code = code;
        this.value = value;
        this.visible = visible;
        this.flags = flags;
    }

    public static Builder builder() {
        return new Codifier.Builder();
    }

    public Builder toBuilder() {
        return builder()
                .code(this.code)
                .value(this.value)
                .visible(this.visible)
                .flags(this.flags);
    }

    public String id() {
        return code;
    }

    public String code() {
        return code;
    }

    public String value() {
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
        var that = (Codifier) o;
        return visible == that.visible
                && flags == that.flags
                && Objects.equals(code, that.code)
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
        return Objects.hash(code, value, visible, flags);
    }

    @Override
    public String toString() {
        return "Codifier{" +
                "code=" + code +
                ", value='" + value + '\'' +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private String code;
        private String value;
        private boolean visible;
        private int flags;

        private Builder() {
            this.code = Codifier.NONE;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder value(String value) {
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

        public Codifier build() {
            return new Codifier(code, value, visible, flags);
        }
    }
}
