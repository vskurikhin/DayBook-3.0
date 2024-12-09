/*
 * This file was last modified at 2024-10-30 17:26 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * LanguageTable.java
 * $Id$
 */

package su.svn.daybook3.api.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.annotation.Nonnull;
import org.intellij.lang.annotations.Language;
import su.svn.daybook3.annotations.ModelField;
import su.svn.daybook3.domain.model.CasesOfLong;
import su.svn.daybook3.models.Marked;
import su.svn.daybook3.models.Owned;
import su.svn.daybook3.models.TimeUpdated;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record LanguageTable(
        @ModelField Long id,
        @ModelField(nullable = false) @Nonnull String language,
        String userName,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean enabled,
        @ModelField boolean visible,
        @ModelField int flags)
        implements CasesOfLong, Marked, Owned, TimeUpdated, Serializable {

    public static final String ID = "id";
    public static final String NONE = "f53cb72e-1e95-4294-8bd7-0952f2ed69dc";
    @Language("SQL")
    public static final String COUNT_DICTIONARY_LANGUAGE = "SELECT count(*) FROM dictionary.language WHERE enabled";
    @Language("SQL")
    public static final String INSERT_INTO_DICTIONARY_LANGUAGE_RETURNING_S = """
            INSERT INTO dictionary.language
             (id, language, user_name, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6)
             RETURNING %s
            """;
    @Language("SQL")
    public static final String INSERT_INTO_DICTIONARY_LANGUAGE_DEFAULT_ID_RETURNING_S = """
            INSERT INTO dictionary.language
             (id, language, user_name, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5)
             RETURNING %s
            """;
    @Language("SQL")
    public static final String DELETE_FROM_DICTIONARY_LANGUAGE_WHERE_ID_$1_RETURNING_S = """
            DELETE FROM dictionary.language
             WHERE id = $1
             RETURNING %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_LANGUAGE_ORDER_BY_S = """
            SELECT id, language, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.language
             WHERE enabled
             ORDER BY %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_LANGUAGE_ORDER_BY_S_OFFSET_$1_LIMIT_$2 = """
            SELECT id, language, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.language
             WHERE enabled
             ORDER BY %s OFFSET $1 LIMIT $2
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_LANGUAGE_WHERE_ID_$1 = """
            SELECT id, language, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.language
             WHERE id = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_LANGUAGE_WHERE_KEY_$1 = """
            SELECT id, language, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.language
             WHERE language = $1 AND enabled
            """;
    @Language("SQL")
    public static final String UPDATE_DICTIONARY_LANGUAGE_WHERE_ID_$1_RETURNING_S = """
            UPDATE dictionary.language SET
              language = $2,
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

    public static LanguageTable from(Row row) {
        return new LanguageTable(
                row.getLong(ID),
                row.getString("language"),
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
                ? INSERT_INTO_DICTIONARY_LANGUAGE_RETURNING_S
                : INSERT_INTO_DICTIONARY_LANGUAGE_DEFAULT_ID_RETURNING_S;
    }

    @Override
    public Tuple caseInsertTuple() {
        return id != null
                ? Tuple.tuple(listOf())
                : Tuple.of(language, userName, enabled, visible, flags);
    }

    @Override
    public String deleteSql() {
        return DELETE_FROM_DICTIONARY_LANGUAGE_WHERE_ID_$1_RETURNING_S;
    }

    @Override
    public String updateSql() {
        return UPDATE_DICTIONARY_LANGUAGE_WHERE_ID_$1_RETURNING_S;
    }

    @Override
    public Tuple updateTuple() {
        return Tuple.tuple(listOf());
    }

    private List<Object> listOf() {
        return Arrays.asList(id, language, userName, enabled, visible, flags);
    }

    public static final class Builder {
        private @ModelField Long id;
        private @ModelField
        @Nonnull String language;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private @ModelField boolean visible;
        private @ModelField int flags;

        private Builder() {
            this.language = NONE;
            this.enabled = true;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder language(@Nonnull String language) {
            this.language = language;
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

        public LanguageTable build() {
            return new LanguageTable(id, language, userName, createTime, updateTime, enabled, visible, flags);
        }
    }
}