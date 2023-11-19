/*
 * This file was last modified at 2023.11.19 16:20 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * RoleTable.java
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
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record RoleTable(
        @ModelField UUID id,
        @ModelField(nullable = false) @Nonnull String role,
        @ModelField String description,
        String userName,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean enabled,
        @ModelField boolean visible,
        @ModelField int flags)
        implements CasesOfUUID, Marked, Owned, TimeUpdated, Serializable {

    public static final String ID = "id";
    public static final String NONE = "adc2221c-03dd-496f-9243-17ed9270b95e";
    @Language("SQL")
    public static final String COUNT_SECURITY_ROLE = "SELECT count(*) FROM security.role WHERE enabled";
    @Language("SQL")
    public static final String DELETE_FROM_SECURITY_ROLE_WHERE_ID_$1 = """
            DELETE FROM security.role
             WHERE id = $1
             RETURNING id
            """;
    @Language("SQL")
    public static final String INSERT_INTO_SECURITY_ROLE = """
            INSERT INTO security.role
             (id, role, description, user_name, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6, $7)
             RETURNING id
            """;
    @Language("SQL")
    public static final String INSERT_INTO_SECURITY_ROLE_DEFAULT_ID = """
            INSERT INTO security.role
             (id, role, description, user_name, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5, $6)
             RETURNING id
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_SECURITY_ROLE_ORDER_BY_ID_ASC = """
            SELECT id, role, description, user_name, create_time, update_time, enabled, visible, flags
              FROM security.role
             WHERE enabled
             ORDER BY id ASC
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_SECURITY_ROLE_ORDER_BY_ID_ASC_OFFSET_LIMIT = """
            SELECT id, role, description, user_name, create_time, update_time, enabled, visible, flags
              FROM security.role
             WHERE enabled
             ORDER BY id ASC OFFSET $1 LIMIT $2
            """;
    @Language("SQL")
    public static final String SELECT_FROM_SECURITY_ROLE_WHERE_ID_$1 = """
            SELECT id, role, description, user_name, create_time, update_time, enabled, visible, flags
              FROM security.role
             WHERE id = $1 AND enabled
            """;
    @Language("SQL")
    public static final String UPDATE_SECURITY_ROLE_WHERE_ID_$1 = """
            UPDATE security.role SET
              role = $2,
              description = $3,
              user_name = $4,
              enabled = $5,
              visible = $6,
              flags = $7
             WHERE id = $1
             RETURNING id
            """;

    public static Builder builder() {
        return new Builder();
    }

    public static RoleTable from(Row row) {
        return new RoleTable(
                row.getUUID(ID),
                row.getString("role"),
                row.getString("description"),
                row.getString("user_name"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public String caseInsertSql() {
        return id != null
                ? INSERT_INTO_SECURITY_ROLE
                : INSERT_INTO_SECURITY_ROLE_DEFAULT_ID;
    }

    public Tuple caseInsertTuple() {
        return id != null
                ? Tuple.tuple(listOf())
                : Tuple.of(role, description, userName, enabled, visible, flags);
    }

    @Override
    public String deleteSql() {
        return DELETE_FROM_SECURITY_ROLE_WHERE_ID_$1;
    }

    @Override
    public String updateSql() {
        return UPDATE_SECURITY_ROLE_WHERE_ID_$1;
    }

    @Override
    public Tuple updateTuple() {
        return Tuple.tuple(listOf());
    }

    private List<Object> listOf() {
        return Arrays.asList(id, role, description, userName, enabled, visible, flags);
    }

    public static final class Builder {
        private @ModelField UUID id;
        private @Nonnull
        @ModelField String role;
        private @ModelField String description;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private @ModelField boolean visible;
        private @ModelField int flags;

        private Builder() {
            this.role = NONE;
            this.enabled = true;
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder role(@Nonnull String role) {
            this.role = role;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
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

        public RoleTable build() {
            return new RoleTable(id, role, description, userName, createTime, updateTime, enabled, visible, flags);
        }
    }
}
