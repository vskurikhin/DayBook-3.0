/*
 * This file was last modified at 2023.01.10 19:49 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * KeyValueTbl.java
 * $Id$
 */

package su.svn.daybook.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import su.svn.daybook.annotations.ModelField;
import su.svn.daybook.models.Marked;
import su.svn.daybook.models.Owned;
import su.svn.daybook.models.TimeUpdated;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record KeyValueTable(
        @ModelField UUID id,
        @ModelField @Nonnull BigInteger key,
        @ModelField JsonObject value,
        String userName,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean enabled,
        @ModelField boolean visible,
        @ModelField int flags)
        implements CasesOfUUID, Marked, Owned, TimeUpdated, Serializable {

    public static final String COUNT = "count";
    public static final String ID = "id";
    public static final String NONE = "d94d93d9-d44c-403c-97b1-d071b6974d80";
    public static final String SELECT_FROM_DICTIONARY_KEY_VALUE_WHERE_ID_$1 = """
            SELECT id, key, value, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.key_value
             WHERE id = $1 AND enabled
            """;
    public static final String SELECT_ALL_FROM_DICTIONARY_KEY_VALUE_ORDER_BY_ID_ASC = """
            SELECT id, key, value, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.key_value
             WHERE enabled
             ORDER BY id ASC
            """;
    public static final String SELECT_ALL_FROM_DICTIONARY_KEY_VALUE_ORDER_BY_ID_ASC_OFFSET_LIMIT = """
            SELECT id, key, value, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.key_value
             WHERE enabled
             ORDER BY id ASC OFFSET $1 LIMIT $2
            """;
    public static final String INSERT_INTO_DICTIONARY_KEY_VALUE = """
            INSERT INTO dictionary.key_value
             (id, key, value, user_name, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6, $7)
             RETURNING id
            """;
    public static final String INSERT_INTO_DICTIONARY_KEY_VALUE_DEFAULT_ID = """
            INSERT INTO dictionary.key_value
             (id, key, value, user_name, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5, $6)
             RETURNING id
            """;
    public static final String UPDATE_DICTIONARY_KEY_VALUE_WHERE_ID_$1 = """
            UPDATE dictionary.key_value SET
              key = $2,
              value = $3,
              user_name = $4,
              enabled = $5,
              visible = $6,
              flags = $7
             WHERE id = $1
             RETURNING id
            """;
    public static final String DELETE_FROM_DICTIONARY_KEY_VALUE_WHERE_ID_$1 = """
            DELETE FROM dictionary.key_value
             WHERE id = $1
             RETURNING id
            """;
    public static final String COUNT_DICTIONARY_KEY_VALUE = "SELECT count(*) FROM dictionary.key_value";

    public static Builder builder() {
        return new Builder();
    }

    public static KeyValueTable from(Row row) {
        return new KeyValueTable(
                row.getUUID(ID),
                row.getBigDecimal("key").toBigInteger(),
                row.getJsonObject("value"),
                row.getString("user_name"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public static Multi<KeyValueTable> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_DICTIONARY_KEY_VALUE_ORDER_BY_ID_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .map(KeyValueTable::from);
    }

    public static Uni<KeyValueTable> findById(PgPool client, UUID id) {
        return client
                .preparedQuery(SELECT_FROM_DICTIONARY_KEY_VALUE_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .map(RowSet::iterator)
                .map(iterator -> iterator.hasNext() ? KeyValueTable.from(iterator.next()) : null);
    }

    public static Multi<KeyValueTable> findRange(PgPool client, long offset, long limit) {
        return client
                .preparedQuery(SELECT_ALL_FROM_DICTIONARY_KEY_VALUE_ORDER_BY_ID_ASC_OFFSET_LIMIT)
                .execute(Tuple.of(offset, limit))
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .map(KeyValueTable::from);
    }

    public static Uni<UUID> delete(PgPool client, UUID id) {
        return client.withTransaction(
                connection -> connection.preparedQuery(DELETE_FROM_DICTIONARY_KEY_VALUE_WHERE_ID_$1)
                        .execute(Tuple.of(id))
                        .map(pgRowSet -> pgRowSet.iterator().next().getUUID(ID)));
    }

    public static Uni<Long> count(PgPool client) {
        return client
                .preparedQuery(COUNT_DICTIONARY_KEY_VALUE)
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
                connection -> connection.preparedQuery(UPDATE_DICTIONARY_KEY_VALUE_WHERE_ID_$1)
                        .execute(Tuple.tuple(listOf()))
                        .map(pgRowSet -> pgRowSet.iterator().next().getUUID(ID)));
    }

    private String caseInsertSql() {
        return id != null ? INSERT_INTO_DICTIONARY_KEY_VALUE : INSERT_INTO_DICTIONARY_KEY_VALUE_DEFAULT_ID;
    }

    @Override
    public Tuple caseInsertTuple() {
        return id != null ? Tuple.tuple(listOf()) : Tuple.of(key, value, userName, enabled, visible, flags);
    }

    @Override
    public Tuple updateTuple() {
        return Tuple.tuple(listOf());
    }

    private List<Object> listOf() {
        return Arrays.asList(id, key, value, userName, enabled, visible, flags);
    }

    public static final class Builder {
        private @ModelField UUID id;
        private @ModelField
        @Nonnull BigInteger key;
        private @ModelField JsonObject value;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private @ModelField boolean visible;
        private @ModelField int flags;

        private Builder() {
            this.key = BigInteger.ZERO;
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder key(@Nonnull BigInteger key) {
            this.key = key;
            return this;
        }

        public Builder value(JsonObject value) {
            this.value = value;
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

        public KeyValueTable build() {
            return new KeyValueTable(id, key, value, userName, createTime, updateTime, enabled, visible, flags);
        }
    }
}