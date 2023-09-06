/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Session.java
 * $Id$
 */

package su.svn.daybook.models.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import su.svn.daybook.annotations.DomainField;
import su.svn.daybook.domain.model.SessionTable;
import su.svn.daybook.models.UUIDIdentification;
import su.svn.daybook.utils.TimeUtil;

import jakarta.annotation.Nonnull;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public final class Session implements UUIDIdentification, Serializable {

    public static final String NONE = SessionTable.NONE;
    @Serial
    private static final long serialVersionUID = 633765212405270020L;
    @JsonProperty
    @DomainField
    private final UUID id;
    @JsonProperty
    @DomainField(nullable = false)
    private final String userName;
    @JsonProperty
    @DomainField(nullable = false)
    private final Set<String> roles;
    @JsonProperty
    @DomainField(nullable = false)
    private final LocalDateTime validTime;
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

    public Session() {
        this(null, NONE, Collections.emptySet(), TimeUtil.EPOCH_UTC, true, 0);
    }

    public Session(
            UUID id,
            @Nonnull String userName,
            @Nonnull Set<String> roles,
            @Nonnull LocalDateTime validTime,
            boolean visible,
            int flags) {
        this.id = id;
        this.userName = userName;
        this.roles = roles;
        this.validTime = validTime;
        this.visible = visible;
        this.flags = flags;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Session.Builder toBuilder() {
        return builder()
                .id(this.id)
                .userName(this.userName)
                .roles(this.roles)
                .validTime(this.validTime)
                .visible(this.visible)
                .flags(this.flags);
    }

    public UUID id() {
        return id;
    }

    public String userName() {
        return userName;
    }

    public Set<String> roles() {
        return roles;
    }

    public LocalDateTime validTime() {
        return validTime;
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
        var that = (Session) o;
        return visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(userName, that.userName)
                && Objects.equals(roles, that.roles)
                && Objects.equals(validTime, that.validTime);
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
        return Objects.hash(id, userName, roles, validTime, visible, flags);
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", roles='" + roles + '\'' +
                ", validTime='" + validTime + '\'' +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private UUID id;
        private String userName;
        private Set<String> roles;
        private LocalDateTime validTime;
        private boolean visible;
        private int flags;

        private Builder() {
            this.userName = Session.NONE;
            this.roles = Collections.emptySet();
            this.validTime = TimeUtil.EPOCH_UTC;
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder userName(@Nonnull String userName) {
            this.userName = userName;
            return this;
        }

        public Builder roles(@Nonnull Set<String> roles) {
            this.roles = roles;
            return this;
        }

        public Builder validTime(@Nonnull LocalDateTime validTime) {
            this.validTime = validTime;
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

        public Session build() {
            return new Session(id, userName, roles, validTime, visible, flags);
        }
    }
}
