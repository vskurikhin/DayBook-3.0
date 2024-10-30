/*
 * This file was last modified at 2024-10-30 17:26 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18nTable.java
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
public record I18nTable(
        @ModelField Long id,
        @ModelField(nullable = false) @Nonnull Long languageId,
        @ModelField String message,
        @ModelField String translation,
        String userName,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean enabled,
        @ModelField boolean visible,
        @ModelField int flags)
        implements CasesOfLong, Marked, Owned, TimeUpdated, Serializable {

    public static final String ID = "id";
    public static final String NONE = "715cab6f-ade8-4859-92eb-debb33b1d679";
    @Language("SQL")
    public static final String COUNT_DICTIONARY_I18N = "SELECT count(*) FROM dictionary.i18n";
    @Language("SQL")
    public static final String INSERT_INTO_DICTIONARY_I18N_RETURNING_S = """
            INSERT INTO dictionary.i18n
             (id, language_id, message, translation, user_name, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6, $7, $8)
             RETURNING %s
            """;
    @Language("SQL")
    public static final String INSERT_INTO_DICTIONARY_I18N_DEFAULT_ID_RETURNING_S = """
            INSERT INTO dictionary.i18n
             (id, language_id, message, translation, user_name, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5, $6, $7)
             RETURNING %s
            """;
    @Language("SQL")
    public static final String DELETE_FROM_DICTIONARY_I18N_WHERE_ID_$1_RETURNING_S = """
            DELETE FROM dictionary.i18n
             WHERE id = $1
             RETURNING %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_I18N_ORDER_BY_S = """
            SELECT id, language_id, message, translation, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.i18n
             WHERE enabled
             ORDER BY %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_I18N_ORDER_BY_S_OFFSET_$1_LIMIT_$2 = """
            SELECT id, language_id, message, translation, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.i18n
             WHERE enabled
             ORDER BY %s OFFSET $1 LIMIT $2
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_I18N_WHERE_ID_$1 = """
            SELECT id, language_id, message, translation, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.i18n
             WHERE id = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_I18N_WHERE_KEY_$1_$2 = """
            SELECT id, language_id, message, translation, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.i18n
             WHERE language_id = $1 AND message = $2 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_I18N_WHERE_MESSAGE_$1 = """
            SELECT id, language_id, message, translation, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.i18n
             WHERE message = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_I18N_WHERE_LANGUAGE_ID_$1 = """
            SELECT id, language_id, message, translation, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.i18n
             WHERE language_id = $1 AND enabled
            """;
    @Language("SQL")
    public static final String UPDATE_DICTIONARY_I18N_WHERE_ID_$1_RETURNING_S = """
            UPDATE dictionary.i18n SET
              language_id = $2,
              message = $3,
              translation = $4,
              user_name = $5,
              enabled = $6,
              visible = $7,
              flags = $8
             WHERE id = $1
             RETURNING %s
            """;

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return builder()
                .id(this.id)
                .languageId(this.languageId)
                .message(this.message)
                .translation(this.translation)
                .userName(this.userName)
                .createTime(this.createTime)
                .updateTime(this.updateTime)
                .enabled(this.enabled)
                .visible(this.visible)
                .flags(this.flags);
    }

    public static I18nTable from(Row row) {
        return new I18nTable(
                row.getLong(ID),
                row.getLong("language_id"),
                row.getString("message"),
                row.getString("translation"),
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
                ? INSERT_INTO_DICTIONARY_I18N_RETURNING_S
                : INSERT_INTO_DICTIONARY_I18N_DEFAULT_ID_RETURNING_S;
    }

    @Override
    public Tuple caseInsertTuple() {
        return id != null
                ? Tuple.tuple(listOf())
                : Tuple.tuple(listOfWithOutId());
    }

    @Override
    public String deleteSql() {
        return DELETE_FROM_DICTIONARY_I18N_WHERE_ID_$1_RETURNING_S;
    }

    @Override
    public String updateSql() {
        return UPDATE_DICTIONARY_I18N_WHERE_ID_$1_RETURNING_S;
    }

    @Override
    public Tuple updateTuple() {
        return Tuple.tuple(listOf());
    }

    private List<Object> listOf() {
        return Arrays.asList(id, languageId, message, translation, userName, enabled, visible, flags);
    }

    private List<Object> listOfWithOutId() {
        return Arrays.asList(languageId, message, translation, userName, enabled, visible, flags);
    }

    public static final class Builder {
        private @ModelField Long id;
        private @ModelField
        @Nonnull Long languageId;
        private @ModelField String message;
        private @ModelField String translation;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private @ModelField boolean visible;
        private @ModelField int flags;

        private Builder() {
            this.languageId = 0L;
            this.enabled = true;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder languageId(@Nonnull Long languageId) {
            this.languageId = languageId;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder translation(String translation) {
            this.translation = translation;
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

        public I18nTable build() {
            return new I18nTable(
                    id, languageId, message, translation, userName, createTime, updateTime, enabled, visible, flags
            );
        }
    }
}