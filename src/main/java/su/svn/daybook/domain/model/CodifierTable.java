/*
 * This file was last modified at 2023.01.10 19:49 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * CodifierTable.java
 * $Id$
 */

package su.svn.daybook.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;
import org.intellij.lang.annotations.Language;
import su.svn.daybook.annotations.ModelField;
import su.svn.daybook.models.Marked;
import su.svn.daybook.models.Owned;
import su.svn.daybook.models.TimeUpdated;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record CodifierTable(
        @ModelField String code,
        @ModelField String value,
        String userName,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean enabled,
        @ModelField boolean visible,
        @ModelField int flags)
        implements CasesOfString, Marked, Owned, TimeUpdated, Serializable {

    public static final String ID = "code";
    public static final String NONE = "1d63fe5b-8b11-4dcd-8158-f82aeea6a4b4";
    @Language("SQL")
    public static final String COUNT_DICTIONARY_CODIFIER = "SELECT count(*) FROM dictionary.codifier";
    @Language("SQL")
    public static final String INSERT_INTO_DICTIONARY_CODIFIER_RETURNING_S = """
            INSERT INTO dictionary.codifier
             (code, value, user_name, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6)
             RETURNING %s
            """;
    @Language("SQL")
    public static final String INSERT_INTO_DICTIONARY_CODIFIER_DEFAULT_ID_RETURNING_S = """
            INSERT INTO dictionary.codifier
             (code, value, user_name, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5)
             RETURNING %s
            """;
    @Language("SQL")
    public static final String DELETE_FROM_DICTIONARY_CODIFIER_WHERE_ID_$1_RETURNING_S = """
            DELETE FROM dictionary.codifier
             WHERE code = $1
             RETURNING %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_CODIFIER_ORDER_BY_S = """
            SELECT code, value, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.codifier
             WHERE enabled
             ORDER BY %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_CODIFIER_ORDER_BY_S_OFFSET_$1_LIMIT_$2 = """
            SELECT code, value, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.codifier
             WHERE enabled
             ORDER BY %s OFFSET $1 LIMIT $2
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_CODIFIER_WHERE_ID_$1 = """
            SELECT code, value, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.codifier
             WHERE code = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_CODIFIER_WHERE_KEY_$1 = """
            SELECT code, value, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.codifier
             WHERE code = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_CODIFIER_WHERE_VALUE_$1 = """
            SELECT code, value, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.codifier
             WHERE value = $1 AND enabled
            """;
    @Language("SQL")
    public static final String UPDATE_DICTIONARY_CODIFIER_WHERE_ID_$1_RETURNING_S = """
            UPDATE dictionary.codifier SET
              value = $2,
              user_name = $3,
              enabled = $4,
              visible = $5,
              flags = $6
             WHERE code = $1
             RETURNING %s
            """;

    public static Builder builder() {
        return new Builder();
    }

    public static CodifierTable from(Row row) {
        return new CodifierTable(
                row.getString(ID),
                row.getString("value"),
                row.getString("user_name"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    @Override
    public String id() {
        return this.code;
    }

    @Override
    public String caseInsertSql() {
        return INSERT_INTO_DICTIONARY_CODIFIER_RETURNING_S;
    }

    @Override
    public Tuple caseInsertTuple() {
        return Tuple.tuple(listOf());
    }

    @Override
    public Tuple updateTuple() {
        return Tuple.tuple(listOf());
    }

    private List<Object> listOf() {
        return Arrays.asList(code, value, userName, enabled, visible, flags);
    }

    public static final class Builder {
        private @ModelField String code;
        private @ModelField String value;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private @ModelField boolean visible;
        private @ModelField int flags;

        private Builder() {
            this.code = NONE;
            this.enabled = true;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder value(String value) {
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

        public CodifierTable build() {
            return new CodifierTable(code, value, userName, createTime, updateTime, enabled, visible, flags);
        }
    }
}