/*
 * This file was last modified at 2024-05-14 21:25 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SettingTable.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.annotation.Nonnull;
import org.intellij.lang.annotations.Language;
import su.svn.daybook3.api.gateway.annotations.ModelField;
import su.svn.daybook3.api.gateway.models.Marked;
import su.svn.daybook3.api.gateway.models.Owned;
import su.svn.daybook3.api.gateway.models.TimeUpdated;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SettingTable(
        @ModelField Long id,
        @ModelField(nullable = false) @Nonnull String variable,
        @ModelField String value,
        @ModelField(nullable = false) @Nonnull Long valueTypeId,
        @ModelField(nullable = false) @Nonnull Long stanzaId,
        String userName,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean enabled,
        @ModelField boolean visible,
        @ModelField int flags)
        implements CasesOfLong, Marked, Owned, TimeUpdated, Serializable {

    public static final String ID = "id";
    public static final String NONE = "cf002ba8-bc1e-41a1-a977-491474fa24a4";
    public static final String DEFAULT_TYPE = "Object";
    @Language("SQL")
    public static final String COUNT_DICTIONARY_SETTING = """
            SELECT count(*) FROM dictionary.setting WHERE enabled""";
    @Language("SQL")
    public static final String INSERT_INTO_DICTIONARY_SETTING_RETURNING_S = """
            INSERT INTO dictionary.setting
             (id, variable, value, value_type_id, stanza_id, user_name, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6, $7, $8, $9)
             RETURNING %s
            """;
    @Language("SQL")
    public static final String INSERT_INTO_DICTIONARY_SETTING_DEFAULT_ID_RETURNING_S = """
            INSERT INTO dictionary.setting
             (id, variable, value, value_type_id, stanza_id, user_name, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5, $6, $7, $8)
             RETURNING %s
            """;
    @Language("SQL")
    public static final String DELETE_FROM_DICTIONARY_SETTING_WHERE_ID_$1_RETURNING_S = """
            DELETE FROM dictionary.setting
             WHERE id = $1
             RETURNING %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_SETTING_ORDER_BY_S = """
            SELECT
              id, variable, value, value_type_id, stanza_id, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.setting
             WHERE enabled
             ORDER BY %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_SETTING_ORDER_BY_S_OFFSET_$1_LIMIT_$2 = """
            SELECT
              id, variable, value, value_type_id, stanza_id, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.setting
             WHERE enabled
             ORDER BY %s OFFSET $1 LIMIT $2
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_SETTING_WHERE_ID_$1 = """
            SELECT
              id, variable, value, value_type_id, stanza_id, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.setting
             WHERE id = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_SETTING_WHERE_KEY_$1 = """
            SELECT 
              id, variable, value, value_type_id, stanza_id, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.setting
             WHERE variable = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_SETTING_WHERE_VALUE_$1 = """
            SELECT
              id, variable, value, value_type_id, stanza_id, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.setting
             WHERE value = $1 AND enabled
            """;
    @Language("SQL")
    public static final String UPDATE_DICTIONARY_SETTING_WHERE_ID_$1_RETURNING_S = """
            UPDATE dictionary.setting SET
              variable = $2,
              value = $3,
              value_type_id = $4,
              stanza_id = $5,
              user_name = $6,
              enabled = $7,
              visible = $8,
              flags = $9
             WHERE id = $1
             RETURNING %s
            """;

    public static Builder builder() {
        return new Builder();
    }

    public Builder toBuilder() {
        return builder()
                .id(this.id)
                .variable(this.variable)
                .value(this.value)
                .valueTypeId(this.valueTypeId)
                .stanzaId(this.stanzaId)
                .userName(this.userName)
                .createTime(this.createTime)
                .updateTime(this.updateTime)
                .enabled(this.enabled)
                .visible(this.visible)
                .flags(this.flags);
    }

    public static SettingTable from(Row row) {
        return new SettingTable(
                row.getLong(ID),
                row.getString("variable"),
                row.getString("value"),
                row.getLong("value_type_id"),
                row.getLong("stanza_id"),
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
                ? INSERT_INTO_DICTIONARY_SETTING_RETURNING_S
                : INSERT_INTO_DICTIONARY_SETTING_DEFAULT_ID_RETURNING_S;
    }

    @Override
    public Tuple caseInsertTuple() {
        return id != null
                ? Tuple.tuple(listOf())
                : Tuple.tuple(listOfWithOutId());
    }

    @Override
    public String deleteSql() {
        return DELETE_FROM_DICTIONARY_SETTING_WHERE_ID_$1_RETURNING_S;
    }

    @Override
    public String updateSql() {
        return UPDATE_DICTIONARY_SETTING_WHERE_ID_$1_RETURNING_S;
    }

    @Override
    public Tuple updateTuple() {
        return Tuple.tuple(listOf());
    }

    private List<Object> listOf() {
        return Arrays.asList(id, variable, value, valueTypeId, stanzaId, userName, enabled, visible, flags);
    }

    private List<Object> listOfWithOutId() {
        return Arrays.asList(variable, value, valueTypeId, stanzaId, userName, enabled, visible, flags);
    }

    public static final class Builder {
        private Long id;
        private @Nonnull String variable;
        private String value;
        private @Nonnull Long valueTypeId;
        private @Nonnull Long stanzaId;
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

        public Builder valueTypeId(Long valueTypeId) {
            this.valueTypeId = valueTypeId;
            return this;
        }

        public Builder stanzaId(long stanzaId) {
            this.stanzaId = stanzaId;
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
            return new SettingTable(
                    id, variable, value, valueTypeId, stanzaId, userName, createTime, updateTime, enabled, visible, flags
            );
        }
    }
}