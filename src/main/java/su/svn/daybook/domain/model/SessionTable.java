/*
 * This file was last modified at 2023.01.11 10:07 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SessionTable.java
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
import su.svn.daybook.utils.TimeUtil;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SessionTable(
        @ModelField UUID id,
        @ModelField(nullable = false) @Nonnull String userName,
        @ModelField(nullable = false) @Nonnull Set<String> roles,
        @ModelField(nullable = false) @Nonnull LocalDateTime validTime,
        LocalDateTime createTime, LocalDateTime updateTime,
        boolean enabled,
        @ModelField boolean visible,
        @ModelField int flags)
        implements CasesOfUUID, Marked, Owned, TimeUpdated, Serializable {

    public static final String NONE = "3de2845b-eb5f-49e5-a1b4-ed90abd92c52";
    public static final String ID = "id";
    public static final String COUNT = "count";
    public static final String SELECT_FROM_SECURITY_SESSION_WHERE_ID_$1 = """
            SELECT id, user_name, roles, valid_time, create_time, update_time, enabled, visible, flags
              FROM security.session
             WHERE id = $1 AND enabled
            """;
    public static final String SELECT_ALL_FROM_SECURITY_SESSION_ORDER_BY_ID_ASC = """
            SELECT id, user_name, roles, valid_time, create_time, update_time, enabled, visible, flags
              FROM security.session
             WHERE enabled
             ORDER BY id ASC
            """;
    public static final String SELECT_ALL_FROM_SECURITY_SESSION_ORDER_BY_ID_ASC_OFFSET_LIMIT = """
            SELECT id, user_name, roles, valid_time, create_time, update_time, enabled, visible, flags
              FROM security.session
             WHERE enabled
             ORDER BY id ASC OFFSET $1 LIMIT $2
            """;
    public static final String INSERT_INTO_SECURITY_SESSION = """
            INSERT INTO security.session
             (id, user_name, roles, valid_time, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6, $7)
             RETURNING id
            """;
    public static final String INSERT_INTO_SECURITY_SESSION_DEFAULT_ID = """
            INSERT INTO security.session
             (id, user_name, roles, valid_time, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5, $6)
             RETURNING id
            """;
    public static final String UPDATE_SECURITY_SESSION_WHERE_ID_$1 = """
            UPDATE security.session SET
              user_name = $2,
              roles = $3,
              valid_time = $4,
              enabled = $5,
              visible = $6,
              flags = $7
             WHERE id = $1
             RETURNING id
            """;
    public static final String DELETE_FROM_SECURITY_SESSION_WHERE_ID_$1 = """
            DELETE FROM security.session
             WHERE id = $1
             RETURNING id
            """;
    public static final String COUNT_SECURITY_SESSION = "SELECT count(*) FROM security.session";

    public static SessionTable.Builder builder() {
        return new SessionTable.Builder();
    }

    public static SessionTable from(Row row) {
        return new SessionTable(
                row.getUUID(ID),
                row.getString("user_name"),
                Set.of(row.getArrayOfStrings("roles")),
                row.getLocalDateTime("valid_time"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public static Multi<SessionTable> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_SECURITY_SESSION_ORDER_BY_ID_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .map(SessionTable::from);
    }

    public static Uni<SessionTable> findById(PgPool client, UUID id) {
        return client
                .preparedQuery(SELECT_FROM_SECURITY_SESSION_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .map(RowSet::iterator)
                .map(iterator -> iterator.hasNext() ? SessionTable.from(iterator.next()) : null);
    }

    public static Multi<SessionTable> findRange(PgPool client, long offset, long limit) {
        return client
                .preparedQuery(SELECT_ALL_FROM_SECURITY_SESSION_ORDER_BY_ID_ASC_OFFSET_LIMIT)
                .execute(Tuple.of(offset, limit))
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .map(SessionTable::from);
    }

    public static Uni<UUID> delete(PgPool client, UUID id) {
        return client.withTransaction(
                connection -> connection.preparedQuery(DELETE_FROM_SECURITY_SESSION_WHERE_ID_$1)
                        .execute(Tuple.of(id))
                        .map(pgRowSet -> pgRowSet.iterator().next().getUUID(ID)));
    }

    public static Uni<Long> count(PgPool client) {
        return client
                .preparedQuery(COUNT_SECURITY_SESSION)
                .execute()
                .map(pgRowSet -> pgRowSet.iterator().next().getLong(COUNT));
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
                connection -> connection.preparedQuery(UPDATE_SECURITY_SESSION_WHERE_ID_$1)
                        .execute(updateTuple())
                        .map(pgRowSet -> pgRowSet.iterator().next().getUUID(ID)));
    }

    private String caseInsertSql() {
        return id != null ? INSERT_INTO_SECURITY_SESSION : INSERT_INTO_SECURITY_SESSION_DEFAULT_ID;
    }

    @Override
    public Tuple caseInsertTuple() {
        return id != null
                ? Tuple.tuple(listOf())
                : Tuple.of(userName, roles.toArray(), validTime, enabled, visible, flags);
    }

    @Override
    public Tuple updateTuple() {
        return Tuple.tuple(listOf());
    }

    private List<Object> listOf() {
        return Arrays.asList(id, userName, roles.toArray(), validTime, enabled, visible, flags);
    }

    public static final class Builder {
        private @ModelField UUID id;
        private @Nonnull
        @ModelField(nullable = false) String userName;
        private @Nonnull
        @ModelField(nullable = false) Set<String> roles;
        private @Nonnull
        @ModelField(nullable = false) LocalDateTime validTime;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private @ModelField boolean visible;
        private @ModelField int flags;

        private Builder() {
            this.userName = SessionTable.NONE;
            this.roles = Collections.emptySet();
            this.validTime = TimeUtil.EPOCH_UTC;
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

        public Builder roles(@Nonnull Set<String> roles) {
            this.roles = roles;
            return this;
        }

        public Builder validTime(@Nonnull LocalDateTime validTime) {
            this.validTime = validTime;
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

        public SessionTable build() {
            return new SessionTable(id, userName, roles, validTime, createTime, updateTime, enabled, visible, flags);
        }
    }
}
