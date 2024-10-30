/*
 * This file was last modified at 2024-10-29 23:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * StanzaTable.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.model;

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
public record StanzaTable(
        @ModelField Long id,
        @ModelField(nullable = false) @Nonnull String name,
        @ModelField String description,
        @ModelField(nullable = false) @Nonnull Long parentId,
        String userName,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean enabled,
        @ModelField boolean visible,
        @ModelField int flags)
        implements CasesOfLong, Marked, Owned, TimeUpdated, Serializable {

    public static final String ID = "id";
    public static final String NONE = "00000000-0000-0000-0000-000000000000";
    public static final StanzaTable ROOT = new StanzaTable(0L, NONE, null, 0L, null, LocalDateTime.MIN, null, true, true, 0);
    @Language("SQL")
    public static final String COUNT_DICTIONARY_STANZA = """
            SELECT count(*) FROM dictionary.stanza WHERE enabled""";
    @Language("SQL")
    public static final String INSERT_INTO_DICTIONARY_STANZA_RETURNING_S = """
            INSERT INTO dictionary.stanza
             (id, name, description, parent_id, user_name, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6, $7, $8)
             RETURNING %s
            """;
    @Language("SQL")
    public static final String INSERT_INTO_DICTIONARY_STANZA_DEFAULT_ID_RETURNING_S = """
            INSERT INTO dictionary.stanza
             (id, name, description, parent_id, user_name, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5, $6, $7)
             RETURNING %s
            """;
    @Language("SQL")
    public static final String DELETE_FROM_DICTIONARY_STANZA_WHERE_ID_$1_RETURNING_S = """
            DELETE FROM dictionary.stanza
             WHERE id = $1
             RETURNING %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_STANZA_ORDER_BY_S = """
            SELECT id, name, description, parent_id, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.stanza
             WHERE enabled
             ORDER BY %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_STANZA_ORDER_BY_S_OFFSET_$1_LIMIT_$2 = """
            SELECT id, name, description, parent_id, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.stanza
             WHERE enabled
             ORDER BY %s OFFSET $1 LIMIT $2
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_STANZA_WHERE_ID_$1 = """
            SELECT id, name, description, parent_id, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.stanza
             WHERE id = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_STANZA_WHERE_NAME_$1 = """
            SELECT id, name, description, parent_id, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.stanza
             WHERE name = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_STANZA_WHERE_PARENT_ID_$1 = """
            SELECT id, name, description, parent_id, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.stanza
             WHERE parent_id = $1 AND enabled
            """;
    @Language("SQL")
    public static final String UPDATE_DICTIONARY_STANZA_WHERE_ID_$1_RETURNING_S = """
            UPDATE dictionary.stanza SET
              name = $2,
              description = $3,
              parent_id = $4,
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
                .name(this.name)
                .description(this.description)
                .parentId(this.parentId)
                .userName(this.userName)
                .createTime(this.createTime)
                .updateTime(this.updateTime)
                .enabled(this.enabled)
                .visible(this.visible)
                .flags(this.flags);
    }

    public static StanzaTable from(Row row) {
        return new StanzaTable(
                row.getLong(ID),
                row.getString("name"),
                row.getString("description"),
                row.getLong("parent_id"),
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
                ? INSERT_INTO_DICTIONARY_STANZA_RETURNING_S
                : INSERT_INTO_DICTIONARY_STANZA_DEFAULT_ID_RETURNING_S;
    }

    @Override
    public Tuple caseInsertTuple() {
        return id != null
                ? Tuple.tuple(listOf())
                : Tuple.tuple(listOfWithOutId());
    }

    @Override
    public String deleteSql() {
        return DELETE_FROM_DICTIONARY_STANZA_WHERE_ID_$1_RETURNING_S;
    }

    @Override
    public String updateSql() {
        return UPDATE_DICTIONARY_STANZA_WHERE_ID_$1_RETURNING_S;
    }

    @Override
    public Tuple updateTuple() {
        return Tuple.tuple(listOf());
    }

    private List<Object> listOf() {
        return Arrays.asList(id, name, description, parentId, userName, enabled, visible, flags);
    }

    private List<Object> listOfWithOutId() {
        return Arrays.asList(name, description, parentId, userName, enabled, visible, flags);
    }

    public static final class Builder {
        private @ModelField Long id;
        private @ModelField
        @Nonnull String name;
        private @ModelField String description;
        private @ModelField Long parentId;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private @ModelField boolean visible;
        private @ModelField int flags;

        private Builder() {
            this.name = NONE;
            this.parentId = 0L;
            this.enabled = true;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder name(@Nonnull String name) {
            this.name = name;
            return this;
        }

        public Builder description(@Nonnull String description) {
            this.description = description;
            return this;
        }

        public Builder parentId(Long parentId) {
            this.parentId = parentId;
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

        public StanzaTable build() {
            return new StanzaTable(
                    id, name, description, parentId, userName, createTime, updateTime, enabled, visible, flags
            );
        }
    }
}