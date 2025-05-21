/*
 * This file was last modified at 2024-10-30 17:26 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * VocabularyTable.java
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
public record VocabularyTable(
        @ModelField Long id,
        @ModelField(nullable = false) @Nonnull String word,
        @ModelField String value,
        String userName,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean enabled,
        boolean localChange,
        @ModelField boolean visible,
        @ModelField int flags)
        implements CasesOfLong, Marked, Owned, TimeUpdated, Serializable {

    public static final String ID = "id";
    public static final String NONE = "49aedbd1-c7e5-4040-bde4-2fcc3d8c93d3";
    @Language("SQL")
    public static final String COUNT_DICTIONARY_VOCABULARY = """
            SELECT count(*) FROM dictionary.vocabulary WHERE enabled""";
    @Language("SQL")
    public static final String INSERT_INTO_DICTIONARY_VOCABULARY_RETURNING_S = """
            INSERT INTO dictionary.vocabulary
             (id, word, value, user_name, enabled, local_change, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, DEFAULT, $6, $7)
             RETURNING %s
            """;
    @Language("SQL")
    public static final String INSERT_INTO_DICTIONARY_VOCABULARY_DEFAULT_ID_RETURNING_S = """
            INSERT INTO dictionary.vocabulary
             (id, word, value, user_name, enabled, local_change, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, DEFAULT, $5, $6)
             RETURNING %s
            """;
    @Language("SQL")
    public static final String DELETE_FROM_DICTIONARY_VOCABULARY_WHERE_ID_$1_RETURNING_S = """
            DELETE FROM dictionary.vocabulary
             WHERE id = $1
             RETURNING %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_VOCABULARY_ORDER_BY_S = """
            SELECT id, word, value, user_name, create_time, update_time, enabled, local_change, visible, flags
              FROM dictionary.vocabulary
             WHERE enabled
             ORDER BY %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_VOCABULARY_ORDER_BY_S_OFFSET_$1_LIMIT_$2 = """
            SELECT id, word, value, user_name, create_time, update_time, enabled, local_change, visible, flags
              FROM dictionary.vocabulary
             WHERE enabled
             ORDER BY %s OFFSET $1 LIMIT $2
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_VOCABULARY_WHERE_ID_$1 = """
            SELECT id, word, value, user_name, create_time, update_time, enabled, local_change, visible, flags
              FROM dictionary.vocabulary
             WHERE id = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_VOCABULARY_WHERE_KEY_$1 = """
            SELECT id, word, value, user_name, create_time, update_time, enabled, local_change, visible, flags
              FROM dictionary.vocabulary
             WHERE word = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_VOCABULARY_WHERE_VALUE_$1 = """
            SELECT id, word, value, user_name, create_time, update_time, enabled, local_change, visible, flags
              FROM dictionary.vocabulary
             WHERE value = $1 AND enabled
            """;
    @Language("SQL")
    public static final String UPDATE_DICTIONARY_VOCABULARY_WHERE_ID_$1_RETURNING_S = """
            UPDATE dictionary.vocabulary SET
              word = $2,
              value = $3,
              user_name = $4,
              enabled = $5,
              local_change = DEFAULT,
              visible = $6,
              flags = $7
             WHERE id = $1
             RETURNING %s
            """;

    public static Builder builder() {
        return new Builder();
    }

    public static VocabularyTable from(Row row) {
        return new VocabularyTable(
                row.getLong(ID),
                row.getString("word"),
                row.getString("value"),
                row.getString("user_name"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("local_change"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    @Override
    public String caseInsertSql() {
        return id != null
                ? INSERT_INTO_DICTIONARY_VOCABULARY_RETURNING_S
                : INSERT_INTO_DICTIONARY_VOCABULARY_DEFAULT_ID_RETURNING_S;
    }

    @Override
    public Tuple caseInsertTuple() {
        return id != null
                ? Tuple.tuple(listOf())
                : Tuple.of(word, value, userName, enabled, visible, flags);
    }

    @Override
    public String deleteSql() {
        return DELETE_FROM_DICTIONARY_VOCABULARY_WHERE_ID_$1_RETURNING_S;
    }

    @Override
    public String updateSql() {
        return UPDATE_DICTIONARY_VOCABULARY_WHERE_ID_$1_RETURNING_S;
    }

    @Override
    public Tuple updateTuple() {
        return Tuple.tuple(listOf());
    }

    private List<Object> listOf() {
        return Arrays.asList(id, word, value, userName, enabled, visible, flags);
    }

    public static final class Builder {
        private @ModelField Long id;
        private @ModelField
        @Nonnull String word;
        private @ModelField String value;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private @ModelField boolean visible;
        private @ModelField int flags;

        private Builder() {
            this.word = NONE;
            this.enabled = true;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder word(@Nonnull String word) {
            this.word = word;
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

        public VocabularyTable build() {
            return new VocabularyTable(id, word, value, userName, createTime, updateTime, enabled, true, visible, flags);
        }
    }
}