/*
 * This file was last modified at 2021.12.06 19:31 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserName.java
 * $Id$
 */

package su.svn.daybook.models.domain;

import su.svn.daybook.annotations.DomainField;
import su.svn.daybook.models.UUIDIdentification;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public final class UserName implements UUIDIdentification, Serializable {

    public static final String ID = "id";
    @Serial
    private static final long serialVersionUID = -4271584323707349383L;
    @DomainField(nullable = false)
    private final UUID id;
    @DomainField(nullable = false)
    private final String userName;
    @DomainField(nullable = false)
    private final String password;
    @DomainField
    private final boolean visible;
    @DomainField
    private final int flags;

    private transient int hash;

    private transient boolean hashIsZero;

    public UserName() {
        this(UUID.randomUUID(), "guest", "password", true, 0);
    }

    public UserName(
            @Nonnull UUID id,
            @Nonnull String userName,
            @Nonnull String password,
            boolean visible,
            int flags) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.visible = visible;
        this.flags = flags;
    }


    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return builder()
                .id(this.id)
                .userName(this.userName)
                .password(this.password)
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
        UserName userName1 = (UserName) o;
        return visible == userName1.visible
                && flags == userName1.flags
                && Objects.equals(id, userName1.id)
                && Objects.equals(userName, userName1.userName)
                && Objects.equals(password, userName1.password);
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
        return Objects.hash(id, userName, password, visible, flags);
    }

    @Override
    public String toString() {
        return "UserName{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private UUID id;
        private String userName;
        private String password;
        private boolean visible;
        private int flags;

        private Builder() {
            this.id = UUID.randomUUID();
            this.userName = "guest";
            this.password = "password";
        }

        public Builder id(@Nonnull UUID id) {
            this.id = id;
            return this;
        }

        public Builder userName(@Nonnull String userName) {
            this.userName = userName;
            return this;
        }

        public Builder password(@Nonnull String password) {
            this.password = password;
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

        public UserName build() {
            return new UserName(id, userName, password, visible, flags);
        }
    }
}
