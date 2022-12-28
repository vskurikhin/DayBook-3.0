/*
 * This file was last modified at 2021.12.06 19:31 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserName.java
 * $Id$
 */

package su.svn.daybook.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class UserName implements UUIDIdentification, Marked, Owned, TimeUpdated, Serializable {

    public static final String SELECT_FROM_SECURITY_USER_NAME_WHERE_ID_$1 = """
            SELECT id, user_name, password, create_time, update_time, enabled, visible, flags
              FROM security.user_name
             WHERE id = $1
            """;
    public static final String SELECT_ALL_FROM_SECURITY_USER_NAME_ORDER_BY_ID_ASC = """
            SELECT id, user_name, password, create_time, update_time, enabled, visible, flags
              FROM security.user_name
             ORDER BY id ASC
            """;
    public static final String INSERT_INTO_SECURITY_USER_NAME = """
            INSERT INTO security.user_name
             (id, user_name, password, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6)
             RETURNING id
            """;
    public static final String UPDATE_SECURITY_USER_NAME_WHERE_ID_$1 = """
            UPDATE security.user_name SET
              user_name = $2,
              password = $3,
              enabled = $4,
              visible = $5,
              flags = $6
             WHERE id = $1
             RETURNING id
            """;
    public static final String DELETE_FROM_SECURITY_USER_NAME_WHERE_ID_$1 = """
            DELETE FROM security.user_name
             WHERE id = $1
             RETURNING id
            """;
    public static final String COUNT_SECURITY_USER_NAME = "SELECT count(*) FROM security.user_name";
    @Serial
    private static final long serialVersionUID = 3526532892030791269L;
    private final UUID id;
    private final String userName;
    private final String password;
    private final LocalDateTime createTime;
    private final LocalDateTime updateTime;
    private final boolean enabled;
    private final boolean visible;
    private final int flags;

    public UserName() {
        this.id = UUID.randomUUID();
        this.userName = "root";
        this.password = "password";
        this.createTime = LocalDateTime.now();
        this.updateTime = null;
        this.enabled = false;
        this.visible = true;
        this.flags = 0;
    }

    public UserName(
            @Nonnull UUID id,
            @Nonnull String userName,
            @Nonnull String password,
            LocalDateTime createTime,
            LocalDateTime updateTime,
            boolean enabled,
            boolean visible,
            int flags) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.enabled = enabled;
        this.visible = visible;
        this.flags = flags;
    }

    public static UserName from(Row row) {
        return new UserName(
                row.getUUID("id"),
                row.getString("user_name"),
                row.getString("password"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public static Uni<UserName> findById(PgPool client, UUID id) {
        return client.preparedQuery(SELECT_FROM_SECURITY_USER_NAME_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? UserName.from(iterator.next()) : null);
    }

    public static Multi<UserName> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_SECURITY_USER_NAME_ORDER_BY_ID_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(UserName::from);

    }

    public static Uni<UUID> delete(PgPool client, UUID id) {
        return client.withTransaction(sqlConnection -> sqlConnection.preparedQuery(DELETE_FROM_SECURITY_USER_NAME_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getUUID("id")));
    }

    public static Uni<Long> count(PgPool client) {
        return client.preparedQuery(COUNT_SECURITY_USER_NAME)
                .execute()
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong("count"));
    }

    public static Builder builder() {
        return new Builder();
    }

    public Uni<UUID> insert(PgPool client) {
        return client.withTransaction(sqlConnection -> sqlConnection.preparedQuery(INSERT_INTO_SECURITY_USER_NAME)
                .execute(Tuple.of(id, userName, password, enabled, visible, flags))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? iterator.next().getUUID("id") : null));
    }

    public Uni<UUID> update(PgPool client) {
        return client.withTransaction(sqlConnection -> sqlConnection.preparedQuery(UPDATE_SECURITY_USER_NAME_WHERE_ID_$1)
                .execute(Tuple.of(id, userName, password, enabled, visible, flags))
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getUUID("id")));
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
        UserName userName1 = (UserName) o;
        return enabled == userName1.enabled
                && visible == userName1.visible
                && flags == userName1.flags
                && Objects.equals(id, userName1.id)
                && Objects.equals(userName, userName1.userName)
                && Objects.equals(password, userName1.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userName, password, enabled, visible, flags);
    }

    @Override
    public String toString() {
        return "UserName{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
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
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private boolean visible;
        private int flags;

        private Builder() {
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
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

        public UserName build() {
            return new UserName(id, userName, password, createTime, updateTime, enabled, visible, flags);
        }
    }
}
