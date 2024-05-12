/*
 * This file was last modified at 2024-05-14 21:25 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * ValueTypeTable.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;
import org.intellij.lang.annotations.Language;
import su.svn.daybook3.api.gateway.annotations.ModelField;
import su.svn.daybook3.api.gateway.models.Marked;
import su.svn.daybook3.api.gateway.models.Owned;
import su.svn.daybook3.api.gateway.models.TimeUpdated;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ValueTypeTable(
        @ModelField Long id,
        @ModelField(nullable = false) @Nonnull String valueType,
        String userName,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean enabled,
        @ModelField boolean visible,
        @ModelField int flags)
        implements CasesOfLong, Marked, Owned, TimeUpdated, Serializable {

    public static final String ID = "id";
    public static final String NONE = "cbc87f41-ef9b-41f1-b850-e9330645814c";
    @Language("SQL")
    public static final String COUNT_DICTIONARY_VALUE_TYPE = """
            SELECT count(*) FROM dictionary.value_type WHERE enabled""";
    @Language("SQL")
    public static final String INSERT_INTO_DICTIONARY_VALUE_TYPE_RETURNING_S = """
            INSERT INTO dictionary.value_type
             (id, value_type, user_name, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6)
             RETURNING %s
            """;
    @Language("SQL")
    public static final String INSERT_INTO_DICTIONARY_VALUE_TYPE_DEFAULT_ID_RETURNING_S = """
            INSERT INTO dictionary.value_type
             (id, value_type, user_name, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5)
             RETURNING %s
            """;
    @Language("SQL")
    public static final String DELETE_FROM_DICTIONARY_VALUE_TYPE_WHERE_ID_$1_RETURNING_S = """
            DELETE FROM dictionary.value_type
             WHERE id = $1
             RETURNING %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_VALUE_TYPE_ORDER_BY_S = """
            SELECT id, value_type, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.value_type
             WHERE enabled
             ORDER BY %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_VALUE_TYPE_ORDER_BY_S_OFFSET_$1_LIMIT_$2 = """
            SELECT id, value_type, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.value_type
             WHERE enabled
             ORDER BY %s OFFSET $1 LIMIT $2
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_VALUE_TYPE_WHERE_ID_$1 = """
            SELECT id, value_type, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.value_type
             WHERE id = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_VALUE_TYPE_WHERE_KEY_$1 = """
            SELECT id, value_type, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.value_type
             WHERE value_type = $1 AND enabled
            """;
    @Language("SQL")
    public static final String UPDATE_DICTIONARY_VALUE_TYPE_WHERE_ID_$1_RETURNING_S = """
            UPDATE dictionary.value_type SET
              value_type = $2,
              user_name = $3,
              enabled = $4,
              visible = $5,
              flags = $6
             WHERE id = $1
             RETURNING %s
            """;

    public static Builder builder() {
        return new Builder();
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

    @Override
    public String caseInsertSql() {
        return id != null
                ? INSERT_INTO_DICTIONARY_VALUE_TYPE_RETURNING_S
                : INSERT_INTO_DICTIONARY_VALUE_TYPE_DEFAULT_ID_RETURNING_S;
    }

    @Override
    public Tuple caseInsertTuple() {
        return id != null
                ? Tuple.tuple(listOf())
                : Tuple.of(valueType, userName, enabled, visible, flags);
    }

    @Override
    public String deleteSql() {
        return DELETE_FROM_DICTIONARY_VALUE_TYPE_WHERE_ID_$1_RETURNING_S;
    }

    @Override
    public String updateSql() {
        return UPDATE_DICTIONARY_VALUE_TYPE_WHERE_ID_$1_RETURNING_S;
    }

    @Override
    public Tuple updateTuple() {
        return Tuple.tuple(listOf());
    }

    private List<Object> listOf() {
        return Arrays.asList(id, valueType, userName, enabled, visible, flags);
    }

    public static final class Builder {
        private @ModelField Long id;
        private @ModelField
        @Nonnull String valueType;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private @ModelField boolean visible;
        private @ModelField int flags;

        private Builder() {
            this.enabled = true;
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