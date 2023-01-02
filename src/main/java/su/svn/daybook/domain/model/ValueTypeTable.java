/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ValueType.java
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

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ValueTypeTable implements LongIdentification, Marked, Owned, TimeUpdated, Serializable {

    public static final String NONE = "8af24446-2ca5-4ed3-8d80-f2681feb0ecc";
    public static final String SELECT_FROM_DICTIONARY_VALUE_TYPE_WHERE_ID_$1 = """
            SELECT id, value_type, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.value_type
             WHERE id = $1 AND enabled
            """;
    public static final String SELECT_ALL_FROM_DICTIONARY_VALUE_TYPE_ORDER_BY_ID_ASC = """
            SELECT id, value_type, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.value_type
             WHERE enabled
             ORDER BY id ASC
            """;
    public static final String SELECT_ALL_FROM_DICTIONARY_VALUE_TYPE_ORDER_BY_ID_ASC_OFFSET_LIMIT = """
            SELECT id, value_type, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.value_type
             WHERE enabled
             ORDER BY id ASC OFFSET $1 LIMIT $2
            """;
    public static final String INSERT_INTO_DICTIONARY_VALUE_TYPE = """
            INSERT INTO dictionary.value_type
             (id, value_type, user_name, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6)
             RETURNING id
            """;
    public static final String INSERT_INTO_DICTIONARY_VALUE_TYPE_DEFAULT_ID = """
            INSERT INTO dictionary.value_type
             (id, value_type, user_name, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5)
             RETURNING id
            """;
    public static final String UPDATE_DICTIONARY_VALUE_TYPE_WHERE_ID_$1 = """
            UPDATE dictionary.value_type SET
              value_type = $2,
              user_name = $3,
              enabled = $4,
              visible = $5,
              flags = $6
             WHERE id = $1
             RETURNING id
            """;
    public static final String DELETE_FROM_DICTIONARY_VALUE_TYPE_WHERE_ID_$1 = """
            DELETE FROM dictionary.value_type
             WHERE id = $1
             RETURNING id
            """;
    public static final String COUNT_DICTIONARY_VALUE_TYPE = "SELECT count(*) FROM dictionary.value_type";
    @Serial
    private static final long serialVersionUID = 1855327022471501329L;
    public static final String ID = "id";
    private final Long id;
    private final String valueType;
    private final String userName;
    private final LocalDateTime createTime;
    private final LocalDateTime updateTime;
    private final boolean enabled;
    private final boolean visible;
    private final int flags;

    @JsonIgnore
    private transient volatile int hash;

    @JsonIgnore
    private transient volatile boolean hashIsZero;

    public ValueTypeTable() {
        this.id = null;
        this.valueType = NONE;
        this.userName = null;
        this.createTime = null;
        this.updateTime = null;
        this.enabled = false;
        this.visible = true;
        this.flags = 0;
    }

    public ValueTypeTable(
            Long id,
            @Nonnull String valueType,
            String userName,
            LocalDateTime createTime,
            LocalDateTime updateTime,
            boolean enabled,
            boolean visible,
            int flags) {
        this.id = id;
        this.valueType = valueType;
        this.userName = userName;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.enabled = enabled;
        this.visible = visible;
        this.flags = flags;
    }

    public static ValueTypeTable from(Row row) {
        return new ValueTypeTable(
                row.getLong(ID),
                row.getString("value_type"),
                row.getString("user_name"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public static Multi<ValueTypeTable> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_DICTIONARY_VALUE_TYPE_ORDER_BY_ID_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(ValueTypeTable::from);

    }

    public static Uni<ValueTypeTable> findById(PgPool client, Long id) {
        return client
                .preparedQuery(SELECT_FROM_DICTIONARY_VALUE_TYPE_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? ValueTypeTable.from(iterator.next()) : null);
    }

    public static Multi<ValueTypeTable> findRange(PgPool client, long offset, long limit) {
        return client
                .preparedQuery(SELECT_ALL_FROM_DICTIONARY_VALUE_TYPE_ORDER_BY_ID_ASC_OFFSET_LIMIT)
                .execute(Tuple.of(offset, limit))
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(ValueTypeTable::from);
    }

    public static Uni<Long> delete(PgPool client, Long id) {
        return client.withTransaction(
                connection -> connection.preparedQuery(DELETE_FROM_DICTIONARY_VALUE_TYPE_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong(ID)));
    }

    public static Uni<Long> count(PgPool client) {
        return client
                .preparedQuery(COUNT_DICTIONARY_VALUE_TYPE)
                .execute()
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong("count"));
    }

    public static ValueTypeTable.Builder builder() {
        return new ValueTypeTable.Builder();
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
                connection -> connection.preparedQuery(UPDATE_DICTIONARY_VALUE_TYPE_WHERE_ID_$1)
                        .execute(Tuple.tuple(listOf()))
                        .onItem()
                        .transform(pgRowSet -> pgRowSet.iterator().next().getLong(ID)));
    }

    private String caseInsertSql() {
        return id != null ? INSERT_INTO_DICTIONARY_VALUE_TYPE : INSERT_INTO_DICTIONARY_VALUE_TYPE_DEFAULT_ID;
    }

    private Tuple caseInsertTuple() {
        return id != null ? Tuple.tuple(listOf()) : Tuple.of(valueType, userName, enabled, visible, flags);
    }

    private List<Object> listOf() {
        return Arrays.asList(id, valueType, userName, enabled, visible, flags);
    }

    public Long getId() {
        return id;
    }

    public String getValueType() {
        return valueType;
    }

    public String getUserName() {
        return userName;
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
        var that = (ValueTypeTable) o;
        return enabled == that.enabled
                && visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(valueType, that.valueType)
                && Objects.equals(userName, that.userName);
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
        return Objects.hash(id, valueType, userName, enabled, visible, flags);
    }

    @Override
    public String toString() {
        return "ValueType{" +
                "id=" + id +
                ", valueType='" + valueType + '\'' +
                ", userName='" + userName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", enabled=" + enabled +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private Long id;
        private String valueType;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private boolean visible;
        private int flags;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder valueType(@Nonnull String valueType) {
            this.valueType = valueType;
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

        public ValueTypeTable build() {
            return new ValueTypeTable(id, valueType, userName, createTime, updateTime, enabled, visible, flags);
        }
    }
}
