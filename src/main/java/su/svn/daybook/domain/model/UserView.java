/*
 * This file was last modified at 2021.12.06 19:31 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserNameTable.java
 * $Id$
 */

package su.svn.daybook.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import su.svn.daybook.annotations.ModelField;
import su.svn.daybook.models.Marked;
import su.svn.daybook.models.Owned;
import su.svn.daybook.models.TimeUpdated;
import su.svn.daybook.models.UUIDIdentification;
import su.svn.daybook.models.domain.Role;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class UserView implements UUIDIdentification, Marked, Owned, TimeUpdated, Serializable {

    public static final String SELECT_FROM_SECURITY_USER_VIEW_WHERE_ID_$1 = """
            SELECT id, user_name, password, create_time, update_time, enabled, visible, flags, roles
              FROM security.user_view
             WHERE id = $1 AND enabled
            """;
    public static final String SELECT_FROM_SECURITY_USER_VIEW_WHERE_USER_NAME_$1 = """
            SELECT id, user_name, password, create_time, update_time, enabled, visible, flags, roles
              FROM security.user_view
             WHERE user_name = $1 AND enabled
            """;
    public static final String SELECT_ALL_FROM_SECURITY_USER_VIEW_ORDER_BY_USER_NAME_ASC = """
            SELECT id, user_name, password, create_time, update_time, enabled, visible, flags, roles
              FROM security.user_view
             WHERE enabled
             ORDER BY user_name ASC
            """;
    public static final String SELECT_ALL_FROM_SECURITY_USER_VIEW_ORDER_BY_USER_NAME_ASC_OFFSET_LIMIT = """
            SELECT id, user_name, password, create_time, update_time, enabled, visible, flags, roles
              FROM security.user_view
             WHERE enabled
             ORDER BY user_name ASC OFFSET $1 LIMIT $2
            """;
    public static final String COUNT_SECURITY_USER_VIEW = "SELECT count(*) FROM security.user_view";
    @Serial
    private static final long serialVersionUID = -7382171118916377234L;
    public static final String ID = "id";
    public static final String COUNT = "count";
    @ModelField
    private final UUID id;
    @ModelField(nullable = false)
    private final String userName;
    private final String password;
    @ModelField(nullable = false)
    private final Set<String> roles;
    private final LocalDateTime createTime;
    private final LocalDateTime updateTime;
    private final boolean enabled;
    @ModelField
    private final boolean visible;
    @ModelField
    private final int flags;

    @JsonIgnore
    private transient volatile int hash;

    @JsonIgnore
    private transient volatile boolean hashIsZero;

    public UserView() {
        this.id = UUID.randomUUID();
        this.userName = "guest";
        this.password = "password";
        this.roles = Collections.emptySet();
        this.createTime = LocalDateTime.now();
        this.updateTime = null;
        this.enabled = true;
        this.visible = true;
        this.flags = 0;
    }

    public UserView(
            @Nonnull UUID id,
            @Nonnull String userName,
            @Nonnull String password,
            @Nonnull Set<String> roles,
            LocalDateTime createTime,
            LocalDateTime updateTime,
            boolean enabled,
            boolean visible,
            int flags) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.roles = roles;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.enabled = enabled;
        this.visible = visible;
        this.flags = flags;
    }

    public static UserView from(Row row) {
        return new UserView(
                row.getUUID(ID),
                row.getString("user_name"),
                row.getString("password"),
                Set.of(row.getArrayOfStrings("roles")),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public static Multi<UserView> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_SECURITY_USER_VIEW_ORDER_BY_USER_NAME_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(UserView::from);

    }

    public static Uni<UserView> findById(PgPool client, UUID id) {
        return client
                .preparedQuery(SELECT_FROM_SECURITY_USER_VIEW_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? UserView.from(iterator.next()) : null);
    }

    public static Uni<UserView> findByUserName(PgPool client, String userName) {
        return client
                .preparedQuery(SELECT_FROM_SECURITY_USER_VIEW_WHERE_USER_NAME_$1)
                .execute(Tuple.of(userName))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? UserView.from(iterator.next()) : null);
    }

    public static Multi<UserView> findRange(PgPool client, long offset, long limit) {
        return client
                .preparedQuery(SELECT_ALL_FROM_SECURITY_USER_VIEW_ORDER_BY_USER_NAME_ASC_OFFSET_LIMIT)
                .execute(Tuple.of(offset, limit))
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(UserView::from);
    }

    public static Uni<Long> count(PgPool client) {
        return client
                .preparedQuery(COUNT_SECURITY_USER_VIEW)
                .execute()
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong(COUNT));
    }

    public static Builder builder() {
        return new Builder();
    }

    public UUID getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public boolean isEnabled() {
        return enabled;
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
        var that = (UserView) o;
        return enabled == that.enabled
                && visible == that.visible
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
        return Objects.hash(id, userName, password, roles, enabled, visible, flags);
    }

    @Override
    public String toString() {
        return "UserView{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", roles='" + roles + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", enabled=" + enabled +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private UUID id;
        private String userName;
        private String password;
        private Set<String> roles;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private boolean visible;
        private int flags;

        private Builder() {
            this.enabled = true;
        }

        public Builder id(UUID id) {
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

        public Builder roles(@Nonnull Set<String> roles) {
            this.roles = roles;
            return this;
        }

        public Builder createTime(LocalDateTime createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder updateTime(LocalDateTime updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public Builder enabled(boolean enabled) {
            this.enabled = enabled;
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

        public UserView build() {
            return new UserView(id, userName, password, roles, createTime, updateTime, enabled, visible, flags);
        }
    }
}
