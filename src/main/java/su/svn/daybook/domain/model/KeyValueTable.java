/*
 * This file was last modified at 2023.02.19 17:08 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * KeyValueTable.java
 * $Id$
 */

package su.svn.daybook.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.vertx.core.json.JsonObject;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;
import org.intellij.lang.annotations.Language;
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
        @ModelField(nullable = false) @Nonnull BigInteger key,
        @ModelField JsonObject value,
        String userName,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean enabled,
        @ModelField boolean visible,
        @ModelField int flags)
        implements CasesOfUUID, Marked, Owned, TimeUpdated, Serializable {

    public static final String ID = "id";
    public static final String NONE = "d94d93d9-d44c-403c-97b1-d071b6974d80";
    @Language("SQL")
    public static final String COUNT_DICTIONARY_KEY_VALUE = "SELECT count(*) FROM dictionary.key_value WHERE enabled";
    @Language("SQL")
    public static final String INSERT_INTO_DICTIONARY_KEY_VALUE_RETURNING_S = """
            INSERT INTO dictionary.key_value
             (id, key, value, user_name, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6, $7)
             RETURNING %s
            """;
    @Language("SQL")
    public static final String INSERT_INTO_DICTIONARY_KEY_VALUE_DEFAULT_ID_RETURNING_S = """
            INSERT INTO dictionary.key_value
             (id, key, value, user_name, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5, $6)
             RETURNING %s
            """;
    @Language("SQL")
    public static final String DELETE_FROM_DICTIONARY_KEY_VALUE_WHERE_ID_$1_RETURNING_S = """
            DELETE FROM dictionary.key_value
             WHERE id = $1
             RETURNING %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_KEY_VALUE_ORDER_BY_S = """
            SELECT id, key, value, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.key_value
             WHERE enabled
             ORDER BY %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_KEY_VALUE_ORDER_BY_S_OFFSET_$1_LIMIT_$2 = """
            SELECT id, key, value, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.key_value
             WHERE enabled
             ORDER BY %s OFFSET $1 LIMIT $2
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_KEY_VALUE_WHERE_ID_$1 = """
            SELECT id, key, value, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.key_value
             WHERE id = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_KEY_VALUE_WHERE_KEY_$1 = """
            SELECT id, key, value, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.key_value
             WHERE key = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_KEY_VALUE_WHERE_VALUE_$1 = """
            SELECT id, key, value, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.key_value
             WHERE value = $1 AND enabled
            """;
    @Language("SQL")
    public static final String UPDATE_DICTIONARY_KEY_VALUE_WHERE_ID_$1_RETURNING_S = """
            UPDATE dictionary.key_value SET
              key = $2,
              value = $3,
              user_name = $4,
              enabled = $5,
              visible = $6,
              flags = $7
             WHERE id = $1
             RETURNING %s
            """;

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

    @Override
    public String caseInsertSql() {
        return id != null ? INSERT_INTO_DICTIONARY_KEY_VALUE_RETURNING_S : INSERT_INTO_DICTIONARY_KEY_VALUE_DEFAULT_ID_RETURNING_S;
    }

    @Override
    public Tuple caseInsertTuple() {
        return id != null ? Tuple.tuple(listOf()) : Tuple.of(key, value, userName, enabled, visible, flags);
    }

    @Override
    public String deleteSql() {
        return DELETE_FROM_DICTIONARY_KEY_VALUE_WHERE_ID_$1_RETURNING_S;
    }

    @Override
    public String updateSql() {
        return UPDATE_DICTIONARY_KEY_VALUE_WHERE_ID_$1_RETURNING_S;
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
            this.enabled = true;
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