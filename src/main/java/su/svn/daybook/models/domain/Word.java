/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Word.java
 * $Id$
 */

package su.svn.daybook.models.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import su.svn.daybook.annotations.DomainField;
import su.svn.daybook.domain.model.WordTable;
import su.svn.daybook.models.StringIdentification;

import jakarta.annotation.Nonnull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class Word implements StringIdentification, Serializable {

    public static final String NONE = WordTable.NONE;
    @Serial
    private static final long serialVersionUID = 165354442157702336L;
    public static final String ID = "word";
    @Nonnull
    @DomainField(nullable = false)
    private final String word;
    @DomainField
    private final boolean visible;
    @DomainField
    private final int flags;

    @JsonIgnore
    private transient int hash;

    @JsonIgnore
    private transient boolean hashIsZero;

    public Word() {
        this(NONE, true, 0);
    }

    public Word(
            @Nonnull String word,
            boolean visible,
            int flags) {
        this.word = word;
        this.visible = visible;
        this.flags = flags;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return builder()
                .word(this.word)
                .visible(this.visible)
                .flags(this.flags);
    }

    @JsonIgnore
    public String id() {
        return word;
    }

    public String word() {
        return word;
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
        @Nonnull
        private String word;
        private boolean visible;
        private int flags;

        private Builder() {
            this.word = NONE;
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
