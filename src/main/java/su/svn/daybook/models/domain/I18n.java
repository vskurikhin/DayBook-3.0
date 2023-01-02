/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18n.java
 * $Id$
 */

package su.svn.daybook.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import su.svn.daybook.annotations.DomainField;
import su.svn.daybook.models.LongIdentification;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class I18n implements LongIdentification, Serializable {

    public static final String ID = "id";
    @Serial
    private static final long serialVersionUID = -976693915341713662L;
    @DomainField
    private final Long id;
    @DomainField(nullable = false)
    private final Long languageId;
    @DomainField
    private final String message;
    @DomainField
    private final String translation;
    @DomainField
    private final boolean visible;
    @DomainField
    private final int flags;

    @JsonIgnore
    private transient volatile int hash;

    @JsonIgnore
    private transient volatile boolean hashIsZero;

    public I18n() {
        this.id = null;
        this.languageId = 0L;
        this.message = null;
        this.translation = null;
        this.visible = true;
        this.flags = 0;
    }

    public I18n(
            Long id,
            @Nonnull Long languageId,
            String message,
            String translation,
            boolean visible,
            int flags) {
        this.id = id;
        this.languageId = languageId;
        this.message = message;
        this.translation = translation;
        this.visible = visible;
        this.flags = flags;
    }

    public static I18n.Builder builder() {
        return new I18n.Builder();
    }

    public Long getId() {
        return id;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public String getMessage() {
        return message;
    }

    public String getTranslation() {
        return translation;
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
        var that = (I18n) o;
        return visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(languageId, that.languageId)
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
        return Objects.hash(id, languageId, message, translation, visible, flags);
    }

    @Override
    public String toString() {
        return "I18n{" +
                "id=" + id +
                ", languageId='" + languageId + '\'' +
                ", message='" + message + '\'' +
                ", translation='" + translation + '\'' +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private Long id;
        private Long languageId;
        private String message;
        private String translation;
        private boolean visible;
        private int flags;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder languageId(long languageId) {
            this.languageId = languageId;
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
            return new I18n(
                    id, languageId, message, translation, visible, flags
            );
        }
    }
}
