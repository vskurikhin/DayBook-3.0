/*
 * This file was last modified at 2023.01.11 10:42 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserHasRolesTable.java
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
import su.svn.daybook.models.LongIdentification;
import su.svn.daybook.models.Marked;
import su.svn.daybook.models.Owned;
import su.svn.daybook.models.TimeUpdated;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserHasRolesTable(
        @ModelField Long id,
        @Nonnull @ModelField String userName,
        @Nonnull @ModelField String role,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean enabled,
        @ModelField boolean visible,
        @ModelField int flags)
        implements LongIdentification, Marked, Owned, TimeUpdated, Serializable {

    public static final String ID = "id";
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

    public static final class Builder {
        private @ModelField Long id;
        private @Nonnull
        @ModelField String userName;
        private @Nonnull
        @ModelField String role;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private @ModelField boolean visible;
        private @ModelField int flags;

        private Builder() {
            this.userName = UserNameTable.NONE;
            this.role = RoleTable.NONE;
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
