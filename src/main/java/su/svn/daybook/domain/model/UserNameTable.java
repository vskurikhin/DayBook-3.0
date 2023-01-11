/*
 * This file was last modified at 2023.01.11 12:31 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserNameTable.java
 * $Id$
 */

package su.svn.daybook.domain.model;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import org.intellij.lang.annotations.Language;
import su.svn.daybook.annotations.ModelField;
import su.svn.daybook.models.Marked;
import su.svn.daybook.models.Owned;
import su.svn.daybook.models.TimeUpdated;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public record UserNameTable(
        @ModelField UUID id,
        @ModelField(nullable = false) @Nonnull String userName,
        @ModelField(onlyBuildPart = true) @Nonnull String password,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean enabled,
        @ModelField boolean visible,
        @ModelField int flags)
        implements CasesOfUUID, Marked, Owned, TimeUpdated, Serializable {

    public static final String ID = "id";
    public static final String COUNT = "count";
    public static final String NONE = "4acd4523-e27d-43e7-88dc-f40637c98bf1";
    @Language("SQL")
    public static final String SELECT_FROM_SECURITY_USER_NAME_WHERE_ID_$1 = """
            SELECT id, user_name, password, create_time, update_time, enabled, visible, flags
              FROM security.user_name
             WHERE id = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_SECURITY_USER_NAME_ORDER_BY_ID_ASC = """
            SELECT id, user_name, password, create_time, update_time, enabled, visible, flags
              FROM security.user_name
             WHERE enabled
             ORDER BY id ASC
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_SECURITY_USER_NAME_ORDER_BY_ID_ASC_OFFSET_LIMIT = """
            SELECT id, user_name, password, create_time, update_time, enabled, visible, flags
              FROM security.user_name
             WHERE enabled
             ORDER BY id ASC OFFSET $1 LIMIT $2
            """;
    @Language("SQL")
    public static final String INSERT_INTO_SECURITY_USER_NAME = """
            INSERT INTO security.user_name
             (id, user_name, password, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6)
             RETURNING id
            """;
    @Language("SQL")
    public static final String INSERT_INTO_SECURITY_USER_NAME_DEFAULT_ID = """
            INSERT INTO security.user_name
             (id, user_name, password, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5)
             RETURNING id
            """;
    @Language("SQL")
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
    @Language("SQL")
    public static final String DELETE_FROM_SECURITY_USER_NAME_WHERE_ID_$1 = """
            DELETE FROM security.user_name
             WHERE id = $1
             RETURNING id
            """;
    @Language("SQL")
    public static final String COUNT_SECURITY_USER_NAME = "SELECT count(*) FROM security.user_name";


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

    public static UserNameTable.Builder builder() {
        return new UserNameTable.Builder();
    }

    public Uni<UUID> insert(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(caseInsertSql())
                        .execute(caseInsertTuple())
                        .onItem()
                        .transform(RowSet::iterator)
                        .onItem()
                        .transform(iterator -> iterator.hasNext() ? iterator.next().getUUID(ID) : null)
        );
    }

    public Uni<UUID> update(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(UPDATE_SECURITY_USER_NAME_WHERE_ID_$1)
                        .execute(Tuple.of(id, userName, password, enabled, visible, flags))
                        .onItem()
                        .transform(pgRowSet -> pgRowSet.iterator().next().getUUID(ID)));
    }

    private String caseInsertSql() {
        return id != null ? INSERT_INTO_SECURITY_USER_NAME : INSERT_INTO_SECURITY_USER_NAME_DEFAULT_ID;
    }

    public Tuple caseInsertTuple() {
        return id != null
                ? Tuple.of(id, userName, password, enabled, visible, flags)
                : Tuple.of(userName, password, enabled, visible, flags);
    }

    public Tuple updateTuple() {
        return Tuple.of(id, userName, password, enabled, visible, flags);
    }

    public static final class Builder {
        private @ModelField UUID id;
        private @ModelField(nullable = false)
        @Nonnull String userName;
        private @ModelField(onlyBuildPart = true)
        @Nonnull String password;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private @ModelField boolean visible;
        private @ModelField int flags;

        private Builder() {
            this.id = UUID.randomUUID();
            this.userName = NONE;
            this.password = "password";
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
