/*
 * This file was last modified at 2023.01.10 22:50 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * RoleTbl.java
 * $Id$
 */

package su.svn.daybook.domain.model;

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
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public record RoleTable(
        @ModelField UUID id,
        @Nonnull @ModelField String role,
        @ModelField String description,
        String userName,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean enabled,
        @ModelField boolean visible,
        @ModelField int flags)
        implements UUIDIdentification, Marked, Owned, TimeUpdated, Serializable {

    public static final String NONE = "adc2221c-03dd-496f-9243-17ed9270b95e";
    public static final String SELECT_FROM_SECURITY_ROLE_WHERE_ID_$1 = """
            SELECT id, role, description, user_name, create_time, update_time, enabled, visible, flags
              FROM security.role
             WHERE id = $1 AND enabled
            """;
    public static final String SELECT_ALL_FROM_SECURITY_ROLE_ORDER_BY_ID_ASC = """
            SELECT id, role, description, user_name, create_time, update_time, enabled, visible, flags
              FROM security.role
             WHERE enabled
             ORDER BY id ASC
            """;
    public static final String SELECT_ALL_FROM_SECURITY_ROLE_ORDER_BY_ID_ASC_OFFSET_LIMIT = """
            SELECT id, role, description, user_name, create_time, update_time, enabled, visible, flags
              FROM security.role
             WHERE enabled
             ORDER BY id ASC OFFSET $1 LIMIT $2
            """;
    public static final String INSERT_INTO_SECURITY_ROLE = """
            INSERT INTO security.role
             (id, role, description, user_name, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6, $7)
             RETURNING id
            """;
    public static final String INSERT_INTO_SECURITY_ROLE_DEFAULT_ID = """
            INSERT INTO security.role
             (id, role, description, user_name, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5, $6)
             RETURNING id
            """;
    public static final String UPDATE_SECURITY_ROLE_WHERE_ID_$1 = """
            UPDATE security.role SET
              role = $2,
              description = $3,
              user_name = $4,
              enabled = $5,
              visible = $6,
              flags = $7
             WHERE id = $1
             RETURNING id
            """;
    public static final String DELETE_FROM_SECURITY_ROLE_WHERE_ID_$1 = """
            DELETE FROM security.role
             WHERE id = $1
             RETURNING id
            """;
    public static final String COUNT_SECURITY_ROLE = "SELECT count(*) FROM security.role";
    public static final String ID = "id";

    public static RoleTable.Builder builder() {
        return new RoleTable.Builder();
    }

    public static RoleTable from(Row row) {
        return new RoleTable(
                row.getUUID(ID),
                row.getString("role"),
                row.getString("description"),
                row.getString("user_name"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public static Multi<RoleTable> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_SECURITY_ROLE_ORDER_BY_ID_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .map(RoleTable::from);
    }

    public static Uni<RoleTable> findById(PgPool client, UUID id) {
        return client
                .preparedQuery(SELECT_FROM_SECURITY_ROLE_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .map(RowSet::iterator)
                .map(iterator -> iterator.hasNext() ? RoleTable.from(iterator.next()) : null);
    }

    public static Multi<RoleTable> findRange(PgPool client, long offset, long limit) {
        return client
                .preparedQuery(SELECT_ALL_FROM_SECURITY_ROLE_ORDER_BY_ID_ASC_OFFSET_LIMIT)
                .execute(Tuple.of(offset, limit))
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .map(RoleTable::from);
    }

    public static Uni<UUID> delete(PgPool client, UUID id) {
        return client.withTransaction(
                connection -> connection.preparedQuery(DELETE_FROM_SECURITY_ROLE_WHERE_ID_$1)
                        .execute(Tuple.of(id))
                        .map(pgRowSet -> pgRowSet.iterator().next().getUUID(ID)));
    }

    public static Uni<Long> count(PgPool client) {
        return client
                .preparedQuery(COUNT_SECURITY_ROLE)
                .execute()
                .map(pgRowSet -> pgRowSet.iterator().next().getLong("count"));
    }

    public Uni<UUID> insert(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(caseInsertSql())
                        .execute(caseInsertTuple())
                        .map(RowSet::iterator)
                        .map(iterator -> iterator.hasNext() ? iterator.next().getUUID(ID) : null));
    }

    public Uni<UUID> update(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(UPDATE_SECURITY_ROLE_WHERE_ID_$1)
                        .execute(Tuple.tuple(listOf()))
                        .map(pgRowSet -> pgRowSet.iterator().next().getUUID(ID)));
    }

    private String caseInsertSql() {
        return id != null ? INSERT_INTO_SECURITY_ROLE : INSERT_INTO_SECURITY_ROLE_DEFAULT_ID;
    }

    private Tuple caseInsertTuple() {
        return id != null ? Tuple.tuple(listOf()) : Tuple.of(role, description, userName, enabled, visible, flags);
    }

    private List<Object> listOf() {
        return Arrays.asList(id, role, description, userName, enabled, visible, flags);
    }

    public static final class Builder {
        private @ModelField UUID id;
        private @Nonnull
        @ModelField String role;
        private @ModelField String description;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private @ModelField boolean visible;
        private @ModelField int flags;

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

        public Builder userName(String userName) {
            this.userName = userName;
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

        public RoleTable build() {
            return new RoleTable(id, role, description, userName, createTime, updateTime, enabled, visible, flags);
        }
    }
}
