/*
 * This file was last modified at 2023.01.10 19:49 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18nViewTable.java
 * $Id$
 */

package su.svn.daybook.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;
import org.intellij.lang.annotations.Language;
import su.svn.daybook.annotations.ModelField;
import su.svn.daybook.models.LongIdentification;
import su.svn.daybook.models.Marked;
import su.svn.daybook.models.Owned;
import su.svn.daybook.models.TimeUpdated;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record I18nView(
        @ModelField Long id,
        @ModelField(nullable = false) @Nonnull String language,
        @ModelField String message,
        @ModelField String translation,
        String userName,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean enabled,
        @ModelField boolean visible,
        @ModelField int flags)
        implements LongIdentification, Marked, Owned, TimeUpdated, Serializable {

    public static final String ID = "id";
    public static final String NONE = I18nTable.NONE;
    @Language("SQL")
    public static final String COUNT_DICTIONARY_I18N_VIEW = "SELECT count(*) FROM dictionary.i18n_view";
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_I18N_VIEW_ORDER_BY_S = """
            SELECT id, language, message, translation, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.i18n_view
             WHERE enabled
             ORDER BY %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_I18N_VIEW_ORDER_BY_S_OFFSET_$1_LIMIT_$2 = """
            SELECT id, language, message, translation, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.i18n_view
             WHERE enabled
             ORDER BY %s OFFSET $1 LIMIT $2
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_I18N_VIEW_WHERE_ID_$1 = """
            SELECT id, language, message, translation, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.i18n_view
             WHERE id = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_I18N_VIEW_WHERE_LANGUAGE_$1_MESSAGE_$2 = """
            SELECT id, language, message, translation, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.i18n_view
             WHERE language = $1 AND message = $2 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_I18N_VIEW_WHERE_VALUE_$1 = """
            SELECT id, language, message, translation, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.i18n_view
             WHERE message = $1 AND enabled
            """;
    @Language("SQL")
    public static final String UPDATE_DICTIONARY_I18N_VIEW_WHERE_ID_$1_RETURNING_S = """
            UPDATE dictionary.i18n_view SET
              language = $2,
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

    public static I18nView from(Row row) {
        return new I18nView(
                row.getLong(ID),
                row.getString("language"),
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

    public static final class Builder {
        private @ModelField Long id;
        private @ModelField
        @Nonnull String language;
        private @ModelField String message;
        private @ModelField String translation;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private @ModelField boolean visible;
        private @ModelField int flags;

        private Builder() {
            this.language = LanguageTable.NONE;
            this.message = I18nTable.NONE;
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

        public I18nView build() {
            return new I18nView(
                    id, language, message, translation, userName, createTime, updateTime, enabled, visible, flags
            );
        }
    }
}