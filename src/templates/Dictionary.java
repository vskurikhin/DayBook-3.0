/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * @Name@.java
 * $Id$
 */

package su.svn.daybook.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import su.svn.daybook.annotations.DomainField;
import su.svn.daybook.models.@IdType@Identification;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class @Name@ implements @IdType@Identification, Serializable {

    public static final String NONE = "@uuid@";
    public static final String ID = "id";
    @Serial
    private static final long serialVersionUID = @serialVersionUID@L;
    @DomainField
    private final @IdType@ id;
    @DomainField(nullable = false)
    private final @KType@ @key@;
    @DomainField
    private final @VType@ @value@;
    @DomainField
    private final boolean visible;
    @DomainField
    private final int flags;

    @JsonIgnore
    private transient int hash;

    @JsonIgnore
    private transient boolean hashIsZero;

    public @Name@() {
        this.id = null;
        this.@key@ = @KType@.ZERO;
        this.@value@ = null;
        this.visible = true;
        this.flags = 0;
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

    public static @Name@.Builder builder() {
        return new @Name@.Builder();
    }

    public @IdType@ getId() {
        return id;
    }

    public @KType@ get@Key@() {
        return @key@;
    }

    public @VType@ get@Value@() {
        return @value@;
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
        private @KType@ @key@;
        private @VType@ @value@;
        private boolean visible;
        private int flags;

        private Builder() {
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
