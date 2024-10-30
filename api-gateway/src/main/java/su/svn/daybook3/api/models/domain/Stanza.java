/*
 * This file was last modified at 2024-10-30 17:26 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Stanza.java
 * $Id$
 */

package su.svn.daybook3.api.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;
import su.svn.daybook3.annotations.DomainField;
import su.svn.daybook3.api.domain.model.StanzaTable;
import su.svn.daybook3.models.LongIdentification;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;

public final class Stanza implements LongIdentification, Serializable {

    public static final Stanza ROOT = new Stanza();
    public static final String NONE = StanzaTable.NONE;
    @Serial
    private static final long serialVersionUID = 3117563277488183968L;
    @JsonProperty
    @DomainField
    private final Long id;
    @JsonProperty
    @DomainField(nullable = false)
    private final String name;
    @JsonProperty
    private final @DomainField String description;
    @JsonIgnore
    // @DomainField(nullable = false)
    private final Stanza parent;
    @JsonProperty
    @DomainField(nullable = false)
    private final Collection<Setting> settings;
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

    public Stanza() {
        this.id = 0L;
        this.name = NONE;
        this.description = null;
        this.parent = this;
        this.settings = Collections.emptySet();
        this.visible = true;
        this.flags = 0;
    }

    public Stanza(
            Long id,
            @Nonnull String name,
            String description,
            @Nonnull Stanza parent,
            @Nonnull Collection<Setting> settings,
            boolean visible,
            int flags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.parent = parent;
        this.settings = settings;
        this.visible = visible;
        this.flags = flags;
    }

    public static Builder builder() {
        return new Stanza.Builder();
    }

    public Builder toBuilder() {
        return builder()
                .id(this.id)
                .name(this.name)
                .description(this.description)
                .parent(this.parent)
                .settings(this.settings)
                .visible(this.visible)
                .flags(this.flags);
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public Stanza parent() {
        return parent;
    }

    public Collection<Setting> settings() {
        //noinspection Java9CollectionFactory
        return Collections.unmodifiableSet(new HashSet<>(settings));
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
        var that = (Stanza) o;
        return visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(name, that.name)
                && Objects.equals(description, that.description)
                && Objects.equals(parent.id, that.parent.id)
                && Objects.equals(settings, that.settings);
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
        return Objects.hash(id, name, description, parent.id, settings, visible, flags);
    }

    @Override
    public String toString() {
        return "Stanza{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", parent.id='" + parent.id + '\'' +
                ", settings='" + settings + '\'' +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private Long id;
        private @Nonnull String name;
        private String description;
        private Stanza parent;
        private Collection<Setting> settings;
        private boolean visible;
        private int flags;

        private Builder() {
            this.name = NONE;
            this.parent = ROOT;
            this.settings = Collections.emptySet();
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(@Nonnull String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder parent(@Nonnull Stanza parent) {
            this.parent = parent;
            return this;
        }

        public Builder settings(@Nonnull Collection<Setting> settings) {
            this.settings = settings;
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

        public Stanza build() {
            return new Stanza(id, name, description, parent, settings, visible, flags);
        }
    }
}
