/*
 * This file was last modified at 2024-05-14 21:35 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabel.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import su.svn.daybook3.api.gateway.annotations.DomainField;
import su.svn.daybook3.api.gateway.domain.model.TagLabelTable;
import su.svn.daybook3.api.gateway.models.StringIdentification;

import jakarta.annotation.Nonnull;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public final class TagLabel implements StringIdentification, Serializable {

    public static final String NONE = TagLabelTable.NONE;
    @Serial
    private static final long serialVersionUID = 4452005651372854906L;
    @JsonProperty
    @DomainField
    private final String id;
    @JsonProperty
    @DomainField(nullable = false)
    private final String label;
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

    public TagLabel() {
        this(null, NONE, true, 0);
    }

    public TagLabel(
            String id,
            @Nonnull String label,
            boolean visible,
            int flags) {
        this.id = id;
        this.label = label;
        this.visible = visible;
        this.flags = flags;
    }

    public static Builder builder() {
        return new TagLabel.Builder();
    }

    public Builder toBuilder() {
        return builder()
                .id(this.id)
                .label(this.label)
                .visible(this.visible)
                .flags(this.flags);
    }

    public String id() {
        return id;
    }

    public String label() {
        return label;
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
        var that = (TagLabel) o;
        return visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(label, that.label);
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
        return Objects.hash(id, label, visible, flags);
    }

    @Override
    public String toString() {
        return "TagLabel{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private String id;
        private @Nonnull String label;
        private boolean visible;
        private int flags;

        private Builder() {
            this.label = NONE;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder label(@Nonnull String label) {
            this.label = label;
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

        public TagLabel build() {
            return new TagLabel(id, label, visible, flags);
        }
    }
}
