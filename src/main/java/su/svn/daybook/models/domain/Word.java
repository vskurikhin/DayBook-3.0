/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Word.java
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
public final class Word implements StringIdentification, Serializable {

    public static final String NONE = "9e9574c8-990d-490a-be46-748e3160dbe1";
    @Serial
    private static final long serialVersionUID = 165354442157702336L;
    public static final String ID = "word";
    @DomainField(nullable = false)
    private final String word;
    @DomainField
    private final boolean visible;
    @DomainField
    private final int flags;

    @JsonIgnore
    private transient volatile int hash;

    @JsonIgnore
    private transient volatile boolean hashIsZero;

    public Word() {
        this.word = NONE;
        this.visible = true;
        this.flags = 0;
    }

    public Word(
            @Nonnull String word,
            boolean visible,
            int flags) {
        this.word = word;
        this.visible = visible;
        this.flags = flags;
    }

    public static Word.Builder builder() {
        return new Word.Builder();
    }

    @JsonIgnore
    public String id() {
        return word;
    }

    public String getWord() {
        return word;
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
        var that = (Word) o;
        return visible == that.visible
                && flags == that.flags
                && Objects.equals(word, that.word);
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
        return Objects.hash(word, visible, flags);
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private String word;
        private boolean visible;
        private int flags;

        private Builder() {
        }

        public Builder id(@Nonnull String id) {
            this.word = id;
            return this;
        }

        public Builder word(@Nonnull String word) {
            this.word = word;
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

        public Word build() {
            return new Word(word, visible, flags);
        }
    }
}
