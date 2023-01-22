/*
 * This file was last modified at 2023.01.22 18:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Vocabulary.java
 * $Id$
 */

package su.svn.daybook.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import su.svn.daybook.annotations.DomainField;
import su.svn.daybook.domain.model.VocabularyTable;
import su.svn.daybook.models.LongIdentification;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public final class Vocabulary implements LongIdentification, Serializable {

    public static final String NONE = VocabularyTable.NONE;
    @Serial
    private static final long serialVersionUID = 7805728152466327730L;
    @JsonProperty
    @DomainField
    private final Long id;
    @JsonProperty
    @DomainField(nullable = false)
    private final String word;
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

    public Vocabulary() {
        this(null, NONE, null, true, 0);
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

    public static Builder builder() {
        return new Vocabulary.Builder();
    }

    public Builder toBuilder() {
        return builder()
                .id(this.id)
                .word(this.word)
                .value(this.value)
                .visible(this.visible)
                .flags(this.flags);
    }

    public Long id() {
        return id;
    }

    public String word() {
        return word;
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
        var that = (Vocabulary) o;
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
        private @Nonnull String word;
        private String value;
        private boolean visible;
        private int flags;

        private Builder() {
            this.word = NONE;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder word(@Nonnull String word) {
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
