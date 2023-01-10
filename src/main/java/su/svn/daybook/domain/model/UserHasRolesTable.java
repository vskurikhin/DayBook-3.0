/*
 * This file was last modified at 2023.01.05 18:24 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserHasRolesTable.java
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
import su.svn.daybook.models.LongIdentification;
import su.svn.daybook.models.Marked;
import su.svn.daybook.models.Owned;
import su.svn.daybook.models.TimeUpdated;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class UserHasRolesTable implements LongIdentification, Marked, Owned, TimeUpdated, Serializable {

    public static final String NONE = "4acd4523-e27d-43e7-88dc-f40637c98bf1";
    public static final String SELECT_FROM_SECURITY_USER_HAS_ROLES_WHERE_ID_$1 = """
            SELECT id, user_name, role, create_time, update_time, enabled, visible, flags
              FROM security.user_has_roles
             WHERE id = $1 AND enabled
            """;
    public static final String SELECT_ALL_FROM_SECURITY_USER_HAS_ROLES_ORDER_BY_ID_ASC = """
            SELECT id, user_name, role, create_time, update_time, enabled, visible, flags
              FROM security.user_has_roles
             WHERE enabled
             ORDER BY id ASC
            """;
    public static final String SELECT_ALL_FROM_SECURITY_USER_HAS_ROLES_ORDER_BY_ID_ASC_OFFSET_LIMIT = """
            SELECT id, user_name, role, create_time, update_time, enabled, visible, flags
              FROM security.user_has_roles
             WHERE enabled
             ORDER BY id ASC OFFSET $1 LIMIT $2
            """;
    public static final String INSERT_INTO_SECURITY_USER_HAS_ROLES = """
            INSERT INTO security.user_has_roles
             (id, user_name, role, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6)
             RETURNING id
            """;
    public static final String INSERT_INTO_SECURITY_USER_HAS_ROLES_DEFAULT_ID = """
            INSERT INTO security.user_has_roles
             (id, user_name, role, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5)
             RETURNING id
            """;
    public static final String UPDATE_SECURITY_USER_HAS_ROLES_WHERE_ID_$1 = """
            UPDATE security.user_has_roles SET
              user_name = $2,
              role = $3,
              enabled = $4,
              visible = $5,
              flags = $6
             WHERE id = $1
             RETURNING id
            """;
    public static final String DELETE_FROM_SECURITY_USER_HAS_ROLES_WHERE_USER_NAME_$1 = """
            DELETE FROM security.user_has_roles
             WHERE user_name = $1
             RETURNING id
            """;
    public static final String DELETE_FROM_SECURITY_USER_HAS_ROLES_WHERE_ID_$1 = """
            DELETE FROM security.user_has_roles
             WHERE id = $1
             RETURNING id
            """;
    public static final String COUNT_SECURITY_USER_HAS_ROLES = "SELECT count(*) FROM security.user_has_roles";
    @Serial
    private static final long serialVersionUID = 1351016300272270368L;
    public static final String ID = "id";
    @ModelField
    private final Long id;
    @ModelField
    private final String userName;
    @ModelField
    private final String role;
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

    public UserHasRolesTable() {
        this.id = null;
        this.userName = NONE;
        this.role = NONE;
        this.createTime = null;
        this.updateTime = null;
        this.enabled = true;
        this.visible = true;
        this.flags = 0;
    }

    public UserHasRolesTable(
            Long id,
            @Nonnull String userName,
            @Nonnull String role,
            LocalDateTime createTime,
            LocalDateTime updateTime,
            boolean enabled,
            boolean visible,
            int flags) {
        this.id = id;
        this.userName = userName;
        this.role = role;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.enabled = enabled;
        this.visible = visible;
        this.flags = flags;
    }

    public static UserHasRolesTable from(Row row) {
        return new UserHasRolesTable(
                row.getLong(ID),
                row.getString("user_name"),
                row.getString("role"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public static Multi<UserHasRolesTable> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_SECURITY_USER_HAS_ROLES_ORDER_BY_ID_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .map(UserHasRolesTable::from);

    }

    public static Uni<UserHasRolesTable> findById(PgPool client, Long id) {
        return client
                .preparedQuery(SELECT_FROM_SECURITY_USER_HAS_ROLES_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .map(RowSet::iterator)
                .map(iterator -> iterator.hasNext() ? UserHasRolesTable.from(iterator.next()) : null);
    }

    public static Multi<UserHasRolesTable> findRange(PgPool client, long offset, long limit) {
        return client
                .preparedQuery(SELECT_ALL_FROM_SECURITY_USER_HAS_ROLES_ORDER_BY_ID_ASC_OFFSET_LIMIT)
                .execute(Tuple.of(offset, limit))
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .map(UserHasRolesTable::from);
    }

    public static Uni<Long> delete(PgPool client, Long id) {
        return client.withTransaction(
                connection -> connection.preparedQuery(DELETE_FROM_SECURITY_USER_HAS_ROLES_WHERE_ID_$1)
                        .execute(Tuple.of(id))
                        .onItem()
                        .transform(pgRowSet -> pgRowSet.iterator().next().getLong(ID)));
    }

    public static Uni<Long> count(PgPool client) {
        return client
                .preparedQuery(COUNT_SECURITY_USER_HAS_ROLES)
                .execute()
                .map(pgRowSet -> pgRowSet.iterator().next().getLong("count"));
    }

    public static UserHasRolesTable.Builder builder() {
        return new UserHasRolesTable.Builder();
    }

    public Uni<Long> insert(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(caseInsertSql())
                        .execute(caseInsertTuple())
                        .onItem()
                        .transform(RowSet::iterator)
                        .onItem()
                        .transform(iterator -> iterator.hasNext() ? iterator.next().getLong(ID) : null));
    }

    public Uni<Long> update(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(UPDATE_SECURITY_USER_HAS_ROLES_WHERE_ID_$1)
                        .execute(Tuple.tuple(listOf()))
                        .onItem()
                        .transform(pgRowSet -> pgRowSet.iterator().next().getLong(ID)));
    }

    private String caseInsertSql() {
        return id != null ? INSERT_INTO_SECURITY_USER_HAS_ROLES : INSERT_INTO_SECURITY_USER_HAS_ROLES_DEFAULT_ID;
    }

    private Tuple caseInsertTuple() {
        return id != null ? Tuple.tuple(listOf()) : Tuple.of(userName, role, enabled, visible, flags);
    }

    private List<Object> listOf() {
        return Arrays.asList(id, userName, role, enabled, visible, flags);
    }

    public Long id() {
        return id;
    }

    public String userName() {
        return userName;
    }

    public String getRole() {
        return role;
    }

    public LocalDateTime createTime() {
        return createTime;
    }

    public LocalDateTime updateTime() {
        return updateTime;
    }

    public boolean enabled() {
        return enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean visible() {
        return visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public int flags() {
        return flags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (UserHasRolesTable) o;
        return enabled == that.enabled
                && visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(userName, that.userName)
                && Objects.equals(role, that.role);
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
        return Objects.hash(id, userName, role, enabled, visible, flags);
    }

    @Override
    public String toString() {
        return "UserHasRolesTable{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", role='" + role + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", enabled=" + enabled +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private Long id;
        private String userName;
        private String role;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private boolean visible;
        private int flags;

        private Builder() {
            this.enabled = true;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder userName(@Nonnull String userName) {
            this.userName = userName;
            return this;
        }

        public Builder role(@Nonnull String role) {
            this.role = role;
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

        public UserHasRolesTable build() {
            return new UserHasRolesTable(id, userName, role, createTime, updateTime, enabled, visible, flags);
        }
    }
}
