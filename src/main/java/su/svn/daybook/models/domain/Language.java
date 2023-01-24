/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Language.java
 * $Id$
 */

package su.svn.daybook.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import su.svn.daybook.annotations.DomainField;
import su.svn.daybook.domain.model.LanguageTable;
import su.svn.daybook.models.LongIdentification;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public final class Language implements LongIdentification, Serializable {

    public static final String NONE = LanguageTable.NONE;
    @Serial
    private static final long serialVersionUID = -8396210656811502324L;
    @JsonProperty
    @DomainField
    private final Long id;
    @JsonProperty
    @DomainField(nullable = false)
    private final String language;
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

    public Language() {
        this(null, NONE, true, 0);
    }

    public Language(
            Long id,
            @Nonnull String language,
            boolean visible,
            int flags) {
        this.id = id;
        this.language = language;
        this.visible = visible;
        this.flags = flags;
    }

    public static Builder builder() {
        return new Language.Builder();
    }

    public Builder toBuilder() {
        return builder()
                .id(this.id)
                .language(this.language)
                .visible(this.visible)
                .flags(this.flags);
    }

    public Long id() {
        return id;
    }

    public String language() {
        return language;
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
        var that = (Language) o;
        return visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(language, that.language);
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
        return Objects.hash(id, language, visible, flags);
    }

    @Override
    public String toString() {
        return "Language{" +
                "id=" + id +
                ", language='" + language + '\'' +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private Long id;
        private @Nonnull String language;
        private boolean visible;
        private int flags;

        private Builder() {
            this.language = NONE;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder language(@Nonnull String language) {
            this.language = language;
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

        public Language build() {
            return new Language(id, language, visible, flags);
        }
    }
}
