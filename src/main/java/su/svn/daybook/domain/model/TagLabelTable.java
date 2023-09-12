/*
 * This file was last modified at 2023.11.19 16:20 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabelTable.java
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

import jakarta.annotation.Nonnull;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TagLabelTable(
        @ModelField String id,
        @ModelField(nullable = false) @Nonnull String label,
        String userName,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean enabled,
        @ModelField boolean visible,
        @ModelField int flags)
        implements CasesOfString, Marked, Owned, TimeUpdated, Serializable {

    public static final String ID = "id";
    public static final String NONE = "8840121a-1518-4044-9454-f4f92c294e11";
    @Language("SQL")
    public static final String COUNT_DICTIONARY_TAG_LABEL = "SELECT count(*) FROM dictionary.tag_label WHERE enabled";
    @Language("SQL")
    public static final String INSERT_INTO_DICTIONARY_TAG_LABEL_RETURNING_S = """
            INSERT INTO dictionary.tag_label
             (id, label, user_name, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6)
             RETURNING %s
            """;
    @Language("SQL")
    public static final String INSERT_INTO_DICTIONARY_TAG_LABEL_DEFAULT_ID_RETURNING_S = """
            INSERT INTO dictionary.tag_label
             (id, label, user_name, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5)
             RETURNING %s
            """;
    @Language("SQL")
    public static final String DELETE_FROM_DICTIONARY_TAG_LABEL_WHERE_ID_$1_RETURNING_S = """
            DELETE FROM dictionary.tag_label
             WHERE id = $1
             RETURNING %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_TAG_LABEL_ORDER_BY_S = """
            SELECT id, label, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.tag_label
             WHERE enabled
             ORDER BY %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_DICTIONARY_TAG_LABEL_ORDER_BY_S_OFFSET_$1_LIMIT_$2 = """
            SELECT id, label, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.tag_label
             WHERE enabled
             ORDER BY %s OFFSET $1 LIMIT $2
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_TAG_LABEL_WHERE_ID_$1 = """
            SELECT id, label, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.tag_label
             WHERE id = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_DICTIONARY_TAG_LABEL_WHERE_KEY_$1 = """
            SELECT id, label, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.tag_label
             WHERE label = $1 AND enabled
            """;
    @Language("SQL")
    public static final String UPDATE_DICTIONARY_TAG_LABEL_WHERE_ID_$1_RETURNING_S = """
            UPDATE dictionary.tag_label SET
              label = $2,
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

    public static TagLabelTable from(Row row) {
        return new TagLabelTable(
                row.getString(ID),
                row.getString("label"),
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
                ? INSERT_INTO_DICTIONARY_TAG_LABEL_RETURNING_S
                : INSERT_INTO_DICTIONARY_TAG_LABEL_DEFAULT_ID_RETURNING_S;
    }

    @Override
    public Tuple caseInsertTuple() {
        return id != null
                ? Tuple.tuple(listOf())
                : Tuple.of(label, userName, enabled, visible, flags);
    }

    @Override
    public String deleteSql() {
        return DELETE_FROM_DICTIONARY_TAG_LABEL_WHERE_ID_$1_RETURNING_S;
    }

    @Override
    public String updateSql() {
        return UPDATE_DICTIONARY_TAG_LABEL_WHERE_ID_$1_RETURNING_S;
    }

    @Override
    public Tuple updateTuple() {
        return Tuple.tuple(listOf());
    }

    private List<Object> listOf() {
        return Arrays.asList(id, label, userName, enabled, visible, flags);
    }

    public static final class Builder {
        private @ModelField
        String id;
        private @ModelField
        @Nonnull
        String label;
        private @ModelField
        String value;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private @ModelField
        boolean visible;
        private @ModelField
        int flags;

        private Builder() {
            this.label = NONE;
            this.enabled = true;
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder label(@Nonnull String label) {
            this.label = label;
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

        public TagLabelTable build() {
            return new TagLabelTable(id, label, userName, createTime, updateTime, enabled, visible, flags);
        }
    }
}