/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Codifier.java
 * $Id$
 */

package su.svn.daybook.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import su.svn.daybook.annotations.DomainField;
import su.svn.daybook.models.StringIdentification;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Codifier implements StringIdentification, Serializable {

    public static final String NONE = "b19bba7c-d53a-4174-9475-6ae9d7b9bbee";
    public static final String ID = "code";
    @Serial
    private static final long serialVersionUID = 593472282147659464L;
    @DomainField(nullable = false)
    private final String code;
    @DomainField
    private final String value;
    @DomainField
    private final boolean visible;
    @DomainField
    private final int flags;

    @JsonIgnore
    private transient volatile int hash;

    @JsonIgnore
    private transient volatile boolean hashIsZero;

    public Codifier() {
        this.code = NONE;
        this.value = null;
        this.visible = true;
        this.flags = 0;
    }

    public Codifier(
            @Nonnull String code,
            String value,
            boolean visible,
            int flags) {
        this.code = code;
        this.value = value;
        this.visible = visible;
        this.flags = flags;
    }

    public static Codifier.Builder builder() {
        return new Codifier.Builder();
    }

    @JsonIgnore
    public String getId() {
        return code;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
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
                "code='" + code + '\'' +
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
        }

        public Builder id(String id) {
            this.code = id;
            return this;
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
