/*
 * This file was last modified at 2024-05-14 21:25 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * WordTable.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.model;

import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;
import org.intellij.lang.annotations.Language;
import su.svn.daybook3.api.gateway.annotations.ModelField;
import su.svn.daybook3.api.gateway.models.Marked;
import su.svn.daybook3.api.gateway.models.Owned;
import su.svn.daybook3.api.gateway.models.TimeUpdated;

import jakarta.annotation.Nonnull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public record WordTable(
        @ModelField(nullable = false) @Nonnull String word,
        String userName,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean enabled,
        @ModelField boolean visible,
        @ModelField int flags)
        implements CasesOfString, Marked, Owned, TimeUpdated, Serializable {

    public static final String NONE = "9e9574c8-990d-490a-be46-748e3160dbe1";
    public static final String ID = "word";
    @Language("SQL")
    public static final String COUNT_DICTIONARY_WORD = "SELECT count(*) FROM dictionary.word WHERE enabled";
    @Language("SQL")
    public static final String DELETE_FROM_DICTIONARY_WORD_WHERE_ID_$1 = """
            DELETE FROM dictionary.word
             WHERE word = $1
             RETURNING word
            """;
    @Language("SQL")
    public static final String INSERT_INTO_DICTIONARY_WORD_RETURNING_S = """
            INSERT INTO dictionary.word
             (word, user_name, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5)
             RETURNING %s
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_WORD_WHERE_ID_$1 = """
            SELECT word, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.word
             WHERE word = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_WORD_ORDER_BY_ID_ASC = """
            SELECT word, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.word
             WHERE enabled
             ORDER BY word ASC
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_WORD_ORDER_BY_WORD_ASC_OFFSET_LIMIT = """
            SELECT word, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.word
             WHERE enabled
             ORDER BY word ASC OFFSET $1 LIMIT $2 
            """;
    @Language("SQL")
    public static final String UPDATE_DICTIONARY_WORD_WHERE_ID_$1 = """
            UPDATE dictionary.word SET
              user_name = $2,
              enabled = $3,
              visible = $4,
              flags = $5
             WHERE word = $1
             RETURNING word
            """;
    @Language("SQL")
    public static final String UPDATE_DICTIONARY_WORD_WHERE_ID_$1_RETURNING_S = """
            UPDATE dictionary.word SET
              user_name = $2,
              enabled = $3,
              visible = $4,
              flags = $5
             WHERE word = $1
             RETURNING %s
            """;

    public static WordTable from(Row row) {
        return new WordTable(
                row.getString(ID),
                row.getString("user_name"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    public String id() {
        return word;
    }

    @Override
    public String caseInsertSql() {
        return INSERT_INTO_DICTIONARY_WORD_RETURNING_S;
    }

    @Override
    public Tuple caseInsertTuple() {
        return Tuple.tuple(listOf());
    }

    @Override
    public String deleteSql() {
        return DELETE_FROM_DICTIONARY_WORD_WHERE_ID_$1;
    }

    @Override
    public String updateSql() {
        return UPDATE_DICTIONARY_WORD_WHERE_ID_$1_RETURNING_S;
    }

    @Override
    public Tuple updateTuple() {
        return Tuple.tuple(listOf());
    }

    private List<Object> listOf() {
        return Arrays.asList(word, userName, enabled, visible, flags);
    }

    public static final class Builder {
        private @ModelField(nullable = false)
        @Nonnull String word;
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

        public Builder id(@Nonnull String id) {
            this.word = id;
            return this;
        }

        public Builder word(@Nonnull String word) {
            this.word = word;
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

        public WordTable build() {
            return new WordTable(word, userName, createTime, updateTime, enabled, visible, flags);
        }
    }
}
