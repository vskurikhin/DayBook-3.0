/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabel.java
 * $Id$
 */

package su.svn.daybook.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import su.svn.daybook.domain.model.StringIdentification;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class TagLabel implements StringIdentification, Serializable {

    public static final String NONE = "519797ec-dba2-452b-b600-eb9dde6b57b8";
    public static final String ID = "id";
    @Serial
    private static final long serialVersionUID = -8634135233772426917L;
    private final String id;
    private final String label;
    private final boolean visible;
    private final int flags;

    @JsonIgnore
    private transient volatile int hash;

    @JsonIgnore
    private transient volatile boolean hashIsZero;

    public TagLabel() {
        this.id = null;
        this.label = NONE;
        this.visible = true;
        this.flags = 0;
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

    public static TagLabel.Builder builder() {
        return new TagLabel.Builder();
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
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
        private String label;
        private boolean visible;
        private int flags;

        private Builder() {
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
