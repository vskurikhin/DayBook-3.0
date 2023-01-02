/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Setting.java
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
public final class SettingTable implements LongIdentification, Marked, Owned, TimeUpdated, Serializable {

    public static final String NONE = "f98f1078-373e-479e-8f81-874839306720";
    public static final String SELECT_FROM_DICTIONARY_SETTING_WHERE_ID_$1 = """
            SELECT id, key, value, value_type_id, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.setting
             WHERE id = $1 AND enabled
            """;
    public static final String SELECT_ALL_FROM_DICTIONARY_SETTING_ORDER_BY_ID_ASC = """
            SELECT id, key, value, value_type_id, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.setting
             WHERE enabled
             ORDER BY id ASC
            """;
    public static final String SELECT_ALL_FROM_DICTIONARY_SETTING_ORDER_BY_ID_ASC_OFFSET_LIMIT = """
            SELECT id, key, value, value_type_id, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.setting
             WHERE enabled
             ORDER BY id ASC OFFSET $1 LIMIT $2
            """;
    public static final String INSERT_INTO_DICTIONARY_SETTING = """
            INSERT INTO dictionary.setting
             (id, key, value, value_type_id, user_name, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6, $7, $8)
             RETURNING id
            """;
    public static final String INSERT_INTO_DICTIONARY_SETTING_DEFAULT_ID = """
            INSERT INTO dictionary.setting
             (id, key, value, value_type_id, user_name, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5, $6, $7)
             RETURNING id
            """;
    public static final String UPDATE_DICTIONARY_SETTING_WHERE_ID_$1 = """
            UPDATE dictionary.setting SET
              key = $2,
              value = $3,
              value_type_id = $4,
              user_name = $5,
              enabled = $6,
              visible = $7,
              flags = $8
             WHERE id = $1
             RETURNING id
            """;
    public static final String DELETE_FROM_DICTIONARY_SETTING_WHERE_ID_$1 = """
            DELETE FROM dictionary.setting
             WHERE id = $1
             RETURNING id
            """;
    public static final String COUNT_DICTIONARY_SETTING = "SELECT count(*) FROM dictionary.setting";
    @Serial
    private static final long serialVersionUID = -2058935813612788249L;
    public static final String ID = "id";
    private final Long id;
    private final String key;
    private final String value;
    private final Long valueTypeId;
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

    public SettingTable() {
        this.id = null;
        this.key = NONE;
        this.value = null;
        this.valueTypeId = 0L;
        this.userName = null;
        this.createTime = null;
        this.updateTime = null;
        this.enabled = true;
        this.visible = true;
        this.flags = 0;
    }

    public SettingTable(
            Long id,
            @Nonnull String key,
            String value,
            long valueTypeId,
            String userName,
            LocalDateTime createTime,
            LocalDateTime updateTime,
            boolean enabled,
            boolean visible,
            int flags) {
        this.id = id;
        this.key = key;
        this.value = value;
        this.valueTypeId = valueTypeId;
        this.userName = userName;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.enabled = enabled;
        this.visible = visible;
        this.flags = flags;
    }

    public static SettingTable from(Row row) {
        return new SettingTable(
                row.getLong(ID),
                row.getString("key"),
                row.getString("value"),
                row.getLong("value_type_id"),
                row.getString("user_name"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public static Multi<SettingTable> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_DICTIONARY_SETTING_ORDER_BY_ID_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(SettingTable::from);

    }

    public static Uni<SettingTable> findById(PgPool client, Long id) {
        return client
                .preparedQuery(SELECT_FROM_DICTIONARY_SETTING_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? SettingTable.from(iterator.next()) : null);
    }

    public static Multi<SettingTable> findRange(PgPool client, long offset, long limit) {
        return client
                .preparedQuery(SELECT_ALL_FROM_DICTIONARY_SETTING_ORDER_BY_ID_ASC_OFFSET_LIMIT)
                .execute(Tuple.of(offset, limit))
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(SettingTable::from);
    }

    public static Uni<Long> delete(PgPool client, Long id) {
        return client.withTransaction(
                connection -> connection.preparedQuery(DELETE_FROM_DICTIONARY_SETTING_WHERE_ID_$1)
                        .execute(Tuple.of(id))
                        .onItem()
                        .transform(pgRowSet -> pgRowSet.iterator().next().getLong(ID)));
    }

    public static Uni<Long> count(PgPool client) {
        return client
                .preparedQuery(COUNT_DICTIONARY_SETTING)
                .execute()
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong("count"));
    }

    public static SettingTable.Builder builder() {
        return new SettingTable.Builder();
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
                connection -> connection.preparedQuery(UPDATE_DICTIONARY_SETTING_WHERE_ID_$1)
                        .execute(Tuple.tuple(listOf()))
                        .onItem()
                        .transform(pgRowSet -> pgRowSet.iterator().next().getLong(ID)));
    }

    private String caseInsertSql() {
        return id != null ? INSERT_INTO_DICTIONARY_SETTING : INSERT_INTO_DICTIONARY_SETTING_DEFAULT_ID;
    }

    private Tuple caseInsertTuple() {
        return id != null ? Tuple.tuple(listOf()) : Tuple.tuple(listInsertWithDefaultOf());
    }

    private List<Object> listInsertWithDefaultOf() {
        return Arrays.asList(key, value, valueTypeId, userName, enabled, visible, flags);
    }

    private List<Object> listOf() {
        return Arrays.asList(id, key, value, valueTypeId, userName, enabled, visible, flags);
    }

    public Long getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public Long getValueTypeId() {
        return valueTypeId;
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
        var that = (SettingTable) o;
        return enabled == that.enabled
                && visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(key, that.key)
                && Objects.equals(value, that.value)
                && Objects.equals(valueTypeId, that.valueTypeId)
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
        return Objects.hash(id, key, value, valueTypeId, userName, enabled, visible, flags);
    }

    @Override
    public String toString() {
        return "Setting{" +
                "id=" + id +
                ", key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", valueTypeId='" + valueTypeId + '\'' +
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
        private String key;
        private String value;
        private long valueTypeId;
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

        public Builder key(@Nonnull String key) {
            this.key = key;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder valueTypeId(long valueTypeId) {
            this.valueTypeId = valueTypeId;
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

        public SettingTable build() {
            return new SettingTable(id, key, value, valueTypeId, userName, createTime, updateTime, enabled, visible, flags);
        }
    }
}
