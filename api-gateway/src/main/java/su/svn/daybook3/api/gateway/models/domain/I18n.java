/*
 * This file was last modified at 2024-05-14 21:35 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18n.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import su.svn.daybook3.api.gateway.annotations.DomainField;
import su.svn.daybook3.api.gateway.models.LongIdentification;

import jakarta.annotation.Nonnull;
import su.svn.daybook3.api.gateway.domain.model.I18nView;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public final class I18n implements LongIdentification, Serializable {

    public static final String NONE = I18nView.NONE;
    @Serial
    private static final long serialVersionUID = 846154579454581650L;
    @JsonProperty
    @DomainField
    private final Long id;
    @JsonProperty
    @DomainField(nullable = false)
    private final String language;
    @JsonProperty
    @DomainField(nullable = false)
    private final String message;
    @JsonProperty
    @DomainField
    private final String translation;
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

    public I18n() {
        this(null, Language.NONE, NONE, null, true, 0);
    }

    public I18n(
            Long id,
            @Nonnull String language,
            @Nonnull String message,
            String translation,
            boolean visible,
            int flags) {
        this.id = id;
        this.language = language;
        this.message = message;
        this.translation = translation;
        this.visible = visible;
        this.flags = flags;
    }

    public static Builder builder() {
        return new I18n.Builder();
    }

    public Builder toBuilder() {
        return builder()
                .id(this.id)
                .language(this.language)
                .message(this.message)
                .visible(this.visible)
                .flags(this.flags);
    }

    public Long id() {
        return id;
    }

    public String language() {
        return language;
    }

    public String message() {
        return message;
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
        var that = (I18n) o;
        return visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(language, that.language)
                && Objects.equals(message, that.message)
                && Objects.equals(translation, that.translation);
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
        return Objects.hash(id, language, message, translation, visible, flags);
    }

    @Override
    public String toString() {
        return "I18nView{" +
                "id=" + id +
                ", language='" + language + '\'' +
                ", message='" + message + '\'' +
                ", translation='" + translation + '\'' +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private Long id;
        private @Nonnull String language;
        private String message;
        private String translation;
        private boolean visible;
        private int flags;

        private Builder() {
            this.language = Language.NONE;
            this.message = NONE;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder language(@Nonnull String language) {
            this.language = language;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder translation(String translation) {
            this.translation = translation;
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

        public I18n build() {
            return new I18n(id, language, message, translation, visible, flags);
        }
    }
}
