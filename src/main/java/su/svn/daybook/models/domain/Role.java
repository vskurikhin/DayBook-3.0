/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Role.java
 * $Id$
 */

package su.svn.daybook.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import su.svn.daybook.annotations.DomainField;
import su.svn.daybook.domain.model.RoleTable;
import su.svn.daybook.models.UUIDIdentification;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public final class Role implements UUIDIdentification, Serializable {

    public static final String NONE = RoleTable.NONE;
    @Serial
    private static final long serialVersionUID = 5979984741157131705L;
    @JsonProperty
    @DomainField
    private final UUID id;
    @JsonProperty
    @DomainField(nullable = false)
    private final String role;
    @JsonProperty
    @DomainField
    private final String description;
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

    public Role() {
        this(null, NONE, null, true, 0);
    }

    public Role(
            UUID id,
            @Nonnull String role,
            String description,
            boolean visible,
            int flags) {
        this.id = id;
        this.role = role;
        this.description = description;
        this.visible = visible;
        this.flags = flags;
    }

    public static Role.Builder builder() {
        return new Role.Builder();
    }

    public UUID id() {
        return id;
    }

    public String role() {
        return role;
    }

    public String description() {
        return description;
    }

    public boolean visible() {
        return visible;
    }

    public int flags() {
        return flags;
    }

    public Role.Builder toBuilder() {
        return Role
                .builder()
                .id(this.id)
                .role(this.role)
                .description(this.description)
                .visible(this.visible)
                .flags(this.flags);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (Role) o;
        return visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(role, that.role)
                && Objects.equals(description, that.description);
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
        return Objects.hash(id, role, description, visible, flags);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", description='" + description + '\'' +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private UUID id;
        private String role;
        private String description;
        private boolean visible;
        private int flags;

        private Builder() {
            this.role = NONE;
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder role(@Nonnull String role) {
            this.role = role;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
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

        public Role build() {
            return new Role(id, role, description, visible, flags);
        }
    }
}
