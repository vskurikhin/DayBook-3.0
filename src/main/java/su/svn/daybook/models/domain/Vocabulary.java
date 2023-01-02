/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Vocabulary.java
 * $Id$
 */

package su.svn.daybook.models.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import su.svn.daybook.domain.model.LongIdentification;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Vocabulary implements LongIdentification, Serializable {

    public static final String NONE = "5f8c1804-5c59-43b6-9099-c1820cffc001";
    public static final String ID = "id";
    @Serial
    private static final long serialVersionUID = 1986849816842086078L;
    private final Long id;
    private final String word;
    private final String value;
    private final boolean visible;
    private final int flags;

    private transient volatile int hash;

    private transient volatile boolean hashIsZero;

    public Vocabulary() {
        this.id = null;
        this.word = NONE;
        this.value = null;
        this.visible = true;
        this.flags = 0;
    }

    public Vocabulary(
            Long id,
            @Nonnull String word,
            String value,
            boolean visible,
            int flags) {
        this.id = id;
        this.word = word;
        this.value = value;
        this.visible = visible;
        this.flags = flags;
    }

    public static Vocabulary.Builder builder() {
        return new Vocabulary.Builder();
    }

    public Long getId() {
        return id;
    }

    public String getWord() {
        return word;
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
        Vocabulary that = (Vocabulary) o;
        return visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(word, that.word)
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
        return Objects.hash(id, word, value, visible, flags);
    }

    @Override
    public String toString() {
        return "Vocabulary{" +
                "id=" + id +
                ", word='" + word + '\'' +
                ", value='" + value + '\'' +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private Long id;
        private String word;
        private String value;
        private boolean visible;
        private int flags;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder word(String word) {
            this.word = word;
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

        public Vocabulary build() {
            return new Vocabulary(id, word, value, visible, flags);
        }
    }
}
