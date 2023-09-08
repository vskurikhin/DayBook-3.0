/*
 * This file was last modified at 2023.09.06 17:04 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SettingView.java
 * $Id$
 */

package su.svn.daybook.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.vertx.mutiny.sqlclient.Row;
import jakarta.annotation.Nonnull;
import org.intellij.lang.annotations.Language;
import su.svn.daybook.annotations.ModelField;
import su.svn.daybook.models.LongIdentification;
import su.svn.daybook.models.Marked;
import su.svn.daybook.models.Owned;
import su.svn.daybook.models.TimeUpdated;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SettingView(
        @ModelField Long id,
        @ModelField(nullable = false) @Nonnull String variable,
        @ModelField String value,
        @ModelField(nullable = false) @Nonnull String valueType,
        String userName,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean enabled,
        @ModelField boolean visible,
        @ModelField int flags)
        implements LongIdentification, Marked, Owned, TimeUpdated, Serializable {

    public static final String ID = "id";
    public static final String NONE = SettingTable.NONE;
    public static final String DEFAULT_TYPE = SettingTable.DEFAULT_TYPE;
    @Language("SQL")
    public static final String COUNT_DICTIONARY_SETTING_VIEW = """
            SELECT count(*) FROM dictionary.setting_view WHERE enabled""";
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_SETTING_VIEW_ORDER_BY_S = """
            SELECT id, variable, value, value_type, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.setting_view
             WHERE enabled
             ORDER BY %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_SETTING_VIEW_ORDER_BY_S_OFFSET_$1_LIMIT_$2 = """
            SELECT id, variable, value, value_type, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.setting_view
             WHERE enabled
             ORDER BY %s OFFSET $1 LIMIT $2
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_SETTING_VIEW_WHERE_ID_$1 = """
            SELECT id, variable, value, value_type, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.setting_view
             WHERE id = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_SETTING_VIEW_WHERE_VARIABLE_$1 = """
            SELECT id, variable, value, value_type, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.setting_view
             WHERE variable = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_SETTING_VIEW_WHERE_VALUE_$1 = """
            SELECT id, variable, value, value_type, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.setting_view
             WHERE value = $1 AND enabled
            """;
    @Language("SQL")
    public static final String UPDATE_DICTIONARY_SETTING_VIEW_WHERE_ID_$1_RETURNING_S = """
            UPDATE dictionary.setting_view SET
              variable = $2,
              value = $3,
              value_type = $4,
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

    public static SettingView from(Row row) {
        return new SettingView(
                row.getLong(ID),
                row.getString("variable"),
                row.getString("value"),
                row.getString("value_type"),
                row.getString("user_name"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    private List<Object> listOf() {
        return Arrays.asList(id, variable, value, valueType, userName, enabled, visible, flags);
    }

    public static final class Builder {
        private @ModelField Long id;
        private @ModelField
        @Nonnull String variable;
        private @ModelField String value;
        private @ModelField String valueType;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private @ModelField boolean visible;
        private @ModelField int flags;

        private Builder() {
            this.variable = NONE;
            this.enabled = true;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder variable(@Nonnull String variable) {
            this.variable = variable;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder valueType(String valueType) {
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

        public SettingView build() {
            return new SettingView(
                    id, variable, value, valueType, userName, createTime, updateTime, enabled, visible, flags
            );
        }
    }
}