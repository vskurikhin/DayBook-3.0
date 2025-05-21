/*
 * This file was last modified at 2024-10-30 14:17 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserHasRolesTable.java
 * $Id$
 */

package su.svn.daybook3.auth.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;
import jakarta.annotation.Nonnull;
import org.intellij.lang.annotations.Language;
import su.svn.daybook3.annotations.ModelField;
import su.svn.daybook3.models.LongIdentification;
import su.svn.daybook3.models.Marked;
import su.svn.daybook3.models.Owned;
import su.svn.daybook3.models.TimeUpdated;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserHasRolesTable(
        @ModelField Long id,
        @Nonnull @ModelField String userName,
        @Nonnull @ModelField String role,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean enabled,
        boolean localChange,
        @ModelField boolean visible,
        @ModelField int flags)
        implements LongIdentification, Marked, Owned, TimeUpdated, Serializable {

    public static final String ID = "id";
    @Language("SQL")
    public static final String COUNT_SECURITY_USER_HAS_ROLES = """
            SELECT count(*) FROM security.user_has_roles WHERE enabled""";
    @Language("SQL")
    public static final String DELETE_FROM_SECURITY_USER_HAS_ROLES_WHERE_ID_$1 = """
            DELETE FROM security.user_has_roles
             WHERE id = $1
             RETURNING id
            """;
    @Language("SQL")
    public static final String DELETE_FROM_SECURITY_USER_HAS_ROLES_WHERE_USER_NAME_$1 = """
            DELETE FROM security.user_has_roles
             WHERE user_name = $1
             RETURNING id
            """;
    @Language("SQL")
    public static final String INSERT_INTO_SECURITY_USER_HAS_ROLES = """
            INSERT INTO security.user_has_roles
             (id, user_name, role, enabled, local_change, visible, flags)
             VALUES
             ($1, $2, $3, $4, DEFAULT, $5, $6)
             RETURNING id
            """;
    @Language("SQL")
    public static final String INSERT_INTO_SECURITY_USER_HAS_ROLES_DEFAULT_ID = """
            INSERT INTO security.user_has_roles
             (id, user_name, role, enabled, local_change, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, DEFAULT, $4, $5)
             RETURNING id
            """;
    @Language("SQL")
    public static final String SELECT_FROM_SECURITY_USER_HAS_ROLES_WHERE_ID_$1 = """
            SELECT id, user_name, role, create_time, update_time, enabled, local_change, visible, flags
              FROM security.user_has_roles
             WHERE id = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_SECURITY_USER_HAS_ROLES_ORDER_BY_ID_ASC = """
            SELECT id, user_name, role, create_time, update_time, enabled, local_change, visible, flags
              FROM security.user_has_roles
             WHERE enabled
             ORDER BY id ASC
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_SECURITY_USER_HAS_ROLES_ORDER_BY_ID_ASC_OFFSET_LIMIT = """
            SELECT id, user_name, role, create_time, update_time, enabled, local_change, visible, flags
              FROM security.user_has_roles
             WHERE enabled
             ORDER BY id ASC OFFSET $1 LIMIT $2
            """;
    @Language("SQL")
    public static final String UPDATE_SECURITY_USER_HAS_ROLES_WHERE_ID_$1 = """
            UPDATE security.user_has_roles SET
              user_name = $2,
              role = $3,
              enabled = $4,
              local_change = DEFAULT,
              visible = $5,
              flags = $6
             WHERE id = $1
             RETURNING id
            """;


    public static UserHasRolesTable from(Row row) {
        return new UserHasRolesTable(
                row.getLong(ID),
                row.getString("user_name"),
                row.getString("role"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("local_change"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public static Builder builder() {
        return new Builder();
    }

    private String caseInsertSql() {
        return id != null
                ? INSERT_INTO_SECURITY_USER_HAS_ROLES
                : INSERT_INTO_SECURITY_USER_HAS_ROLES_DEFAULT_ID;
    }

    private Tuple caseInsertTuple() {
        return id != null
                ? Tuple.tuple(listOf())
                : Tuple.of(userName, role, enabled, visible, flags);
    }

    private List<Object> listOf() {
        return Arrays.asList(id, userName, role, enabled, visible, flags);
    }

    public static final class Builder {
        private @ModelField Long id;
        private @Nonnull
        @ModelField String userName;
        private @Nonnull
        @ModelField String role;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private @ModelField boolean visible;
        private @ModelField int flags;

        private Builder() {
            this.userName = UserNameTable.NONE;
            this.role = RoleTable.NONE;
            this.enabled = true;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder userName(@Nonnull String userName) {
            this.userName = userName;
            return this;
        }

        public Builder role(@Nonnull String role) {
            this.role = role;
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

        public UserHasRolesTable build() {
            return new UserHasRolesTable(id, userName, role, createTime, updateTime, enabled, true, visible, flags);
        }
    }
}
