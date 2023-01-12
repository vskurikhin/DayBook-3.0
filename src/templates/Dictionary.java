/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * @Name@.java
 * $Id$
 */

package su.svn.daybook.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import su.svn.daybook.annotations.DomainField;
import su.svn.daybook.domain.model.@Name@Table;
import su.svn.daybook.models.@IdType@Identification;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public final class @Name@ implements @IdType@Identification, Serializable {

    public static final String NONE = @Name@Table.NONE;
    @Serial
    private static final long serialVersionUID = @serialVersionUID@L;
    @JsonProperty
    @DomainField
    private final @IdType@ id;
    @JsonProperty
    @DomainField(nullable = false)
    private final @KType@ @key@;
    @JsonProperty
    @DomainField
    private final @VType@ @value@;
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

    public @Name@() {
        this(null, @KType@.ZERO, null, true, 0);
    }

    public @Name@(
            @IdType@ id,
            @Nonnull @KType@ @key@,
            @VType@ @value@,
            boolean visible,
            int flags) {
        this.id = id;
        this.@key@ = @key@;
        this.@value@ = @value@;
        this.visible = visible;
        this.flags = flags;
    }

    public static Builder builder() {
        return new @Name@.Builder();
    }

    public Builder toBuilder() {
        return builder()
                .id(this.id)
                .@key@(this.@key@)
                .@value@(this.@value@)
                .visible(this.visible)
                .flags(this.flags);
    }

    public @IdType@ id() {
        return id;
    }

    public @KType@ @key@() {
        return @key@;
    }

    public @VType@ @value@() {
        return @value@;
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
        var that = (@Name@) o;
        return visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(@key@, that.@key@)
                && Objects.equals(@value@, that.@value@);
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
        return Objects.hash(id, @key@, @value@, visible, flags);
    }

    @Override
    public String toString() {
        return "@Name@{" +
                "id=" + id +
                ", @key@='" + @key@ + '\'' +
                ", @value@='" + @value@ + '\'' +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private @IdType@ id;
        private @Nonnull @KType@ @key@;
        private @VType@ @value@;
        private boolean visible;
        private int flags;

        private Builder() {
            this.@key@ = @KType@.ZERO;
        }

        public Builder id(@IdType@ id) {
            this.id = id;
            return this;
        }

        public Builder @key@(@Nonnull @KType@ @key@) {
            this.@key@ = @key@;
            return this;
        }

        public Builder @value@(@VType@ @value@) {
            this.@value@ = @value@;
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

        public @Name@ build() {
            return new @Name@(id, @key@, @value@, visible, flags);
        }
    }
}
