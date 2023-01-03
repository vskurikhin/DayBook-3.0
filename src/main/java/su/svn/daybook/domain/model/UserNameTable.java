/*
 * This file was last modified at 2021.12.06 19:31 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserNameTable.java
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
import su.svn.daybook.annotations.ModelField;
import su.svn.daybook.models.Marked;
import su.svn.daybook.models.Owned;
import su.svn.daybook.models.TimeUpdated;
import su.svn.daybook.models.UUIDIdentification;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class UserNameTable implements UUIDIdentification, Marked, Owned, TimeUpdated, Serializable {

    public static final String SELECT_FROM_SECURITY_USER_NAME_WHERE_ID_$1 = """
            SELECT id, user_name, password, create_time, update_time, enabled, visible, flags
              FROM security.user_name
             WHERE id = $1 AND enabled
            """;
    public static final String SELECT_ALL_FROM_SECURITY_USER_NAME_ORDER_BY_ID_ASC = """
            SELECT id, user_name, password, create_time, update_time, enabled, visible, flags
              FROM security.user_name
             WHERE enabled
             ORDER BY id ASC
            """;
    public static final String SELECT_ALL_FROM_SECURITY_USER_NAME_ORDER_BY_ID_ASC_OFFSET_LIMIT = """
            SELECT id, user_name, password, create_time, update_time, enabled, visible, flags
              FROM security.user_name
             WHERE enabled
             ORDER BY id ASC OFFSET $1 LIMIT $2
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
    public static final String ID = "id";
    public static final String COUNT = "count";
    @ModelField
    private final UUID id;
    @ModelField
    private final String userName;
    @ModelField
    private final String password;
    private final LocalDateTime createTime;
    private final LocalDateTime updateTime;
    private final boolean enabled;
    @ModelField
    private final boolean visible;
    @ModelField
    private final int flags;

    private transient volatile int hash;

    private transient volatile boolean hashIsZero;

    public UserNameTable() {
        this.id = UUID.randomUUID();
        this.userName = "guest";
        this.password = "password";
        this.createTime = LocalDateTime.now();
        this.updateTime = null;
        this.enabled = true;
        this.visible = true;
        this.flags = 0;
    }

    public UserNameTable(
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

    public static UserNameTable from(Row row) {
        return new UserNameTable(
                row.getUUID(ID),
                row.getString("user_name"),
                row.getString("password"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public static Multi<UserNameTable> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_SECURITY_USER_NAME_ORDER_BY_ID_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(UserNameTable::from);

    }

    public static Uni<UserNameTable> findById(PgPool client, UUID id) {
        return client
                .preparedQuery(SELECT_FROM_SECURITY_USER_NAME_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? UserNameTable.from(iterator.next()) : null);
    }

    public static Multi<UserNameTable> findRange(PgPool client, long offset, long limit) {
        return client
                .preparedQuery(SELECT_ALL_FROM_SECURITY_USER_NAME_ORDER_BY_ID_ASC_OFFSET_LIMIT)
                .execute(Tuple.of(offset, limit))
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(UserNameTable::from);
    }

    public static Uni<UUID> delete(PgPool client, UUID id) {
        return client.withTransaction(
                connection -> connection.preparedQuery(DELETE_FROM_SECURITY_USER_NAME_WHERE_ID_$1)
                        .execute(Tuple.of(id))
                        .onItem()
                        .transform(pgRowSet -> pgRowSet.iterator().next().getUUID(ID)));
    }

    public static Uni<Long> count(PgPool client) {
        return client
                .preparedQuery(COUNT_SECURITY_USER_NAME)
                .execute()
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong(COUNT));
    }

    public static Builder builder() {
        return new Builder();
    }

    public Uni<UUID> insert(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(INSERT_INTO_SECURITY_USER_NAME)
                        .execute(Tuple.of(id, userName, password, enabled, visible, flags))
                        .onItem()
                        .transform(RowSet::iterator)
                        .onItem()
                        .transform(iterator -> iterator.hasNext() ? iterator.next().getUUID(ID) : null));
    }

    public Uni<UUID> update(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(UPDATE_SECURITY_USER_NAME_WHERE_ID_$1)
                        .execute(Tuple.of(id, userName, password, enabled, visible, flags))
                        .onItem()
                        .transform(pgRowSet -> pgRowSet.iterator().next().getUUID(ID)));
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
        var that = (UserNameTable) o;
        return enabled == that.enabled
                && visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(userName, that.userName)
                && Objects.equals(password, that.password);
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
            this.enabled = true;
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

        public UserNameTable build() {
            return new UserNameTable(id, userName, password, createTime, updateTime, enabled, visible, flags);
        }
    }
}
