/*
 * This file was last modified at 2023.01.05 18:24 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * User.java
 * $Id$
 */

package su.svn.daybook.models.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import su.svn.daybook.annotations.DomainField;
import su.svn.daybook.domain.model.UserNameTable;
import su.svn.daybook.models.UUIDIdentification;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public final class User implements UUIDIdentification, Serializable {

    public static final String NONE = UserNameTable.NONE;
    @Serial
    private static final long serialVersionUID = -5637024055159017594L;
    @JsonProperty
    @DomainField
    private final UUID id;
    @JsonProperty
    @DomainField(nullable = false)
    private final String userName;
    @DomainField(getterOnly = true)
    private transient final String password;
    @JsonProperty
    @DomainField
    private final Set<String> roles;
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

    public User() {
        this(null, "guest", null, Collections.emptySet(), true, 0);
    }

    public User(
            UUID id,
            @Nonnull String userName,
            String password,
            @Nonnull Collection<String> roles,
            boolean visible,
            int flags) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.roles = new LinkedHashSet<>(roles);
        this.visible = visible;
        this.flags = flags;
    }

    public static Builder builder() {
        return new Builder();
    }

    public User.Builder toBuilder() {
        return builder()
                .id(this.id)
                .userName(this.userName)
                .password(this.password)
                .roles(this.roles)
                .visible(this.visible)
                .flags(this.flags);
    }

    public UUID id() {
        return id;
    }

    public String userName() {
        return userName;
    }

    public String password() {
        return password;
    }

    public Set<String> roles() {
        return roles;
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
        var that = (User) o;
        return visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(userName, that.userName)
                && Objects.equals(password, that.password)
                && Objects.equals(roles, that.roles);
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
        return Objects.hash(id, userName, password, roles, visible, flags);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", roles='" + roles + '\'' +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private UUID id;
        private @Nonnull String userName;
        private String password;
        private @Nonnull Collection<String> roles;
        private boolean visible;
        private int flags;

        private Builder() {
            this.userName = "guest";
            this.roles = Collections.emptySet();
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder userName(@Nonnull String userName) {
            this.userName = userName;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder roles(@Nonnull Collection<String> roles) {
            this.roles = roles;
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

        public User build() {
            return new User(id, userName, password, roles, visible, flags);
        }
    }
}
