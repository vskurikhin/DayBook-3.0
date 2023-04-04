/*
 * This file was last modified at 2023.02.19 17:08 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SessionTable.java
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
import su.svn.daybook.utils.TimeUtil;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record SessionTable(
        @ModelField UUID id,
        @ModelField(nullable = false) @Nonnull String userName,
        @ModelField(nullable = false) @Nonnull Set<String> roles,
        @ModelField(nullable = false) @Nonnull LocalDateTime validTime,
        LocalDateTime createTime, LocalDateTime updateTime,
        boolean enabled,
        @ModelField boolean visible,
        @ModelField int flags)
        implements CasesOfUUID, Marked, Owned, TimeUpdated, Serializable {

    public static final String ID = "id";
    public static final String NONE = "3de2845b-eb5f-49e5-a1b4-ed90abd92c52";
    @Language("SQL")
    public static final String COUNT_SECURITY_SESSION = "SELECT count(*) FROM security.session WHERE enabled";
    @Language("SQL")
    public static final String DELETE_FROM_SECURITY_SESSION_WHERE_ID_$1 = """
            DELETE FROM security.session
             WHERE id = $1
             RETURNING id
            """;
    @Language("SQL")
    public static final String INSERT_INTO_SECURITY_SESSION = """
            INSERT INTO security.session
             (id, user_name, roles, valid_time, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6, $7)
             RETURNING id
            """;
    @Language("SQL")
    public static final String INSERT_INTO_SECURITY_SESSION_DEFAULT_ID = """
            INSERT INTO security.session
             (id, user_name, roles, valid_time, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5, $6)
             RETURNING id
            """;
    @Language("SQL")
    public static final String SELECT_FROM_SECURITY_SESSION_WHERE_ID_$1 = """
            SELECT id, user_name, roles, valid_time, create_time, update_time, enabled, visible, flags
              FROM security.session
             WHERE id = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_SECURITY_SESSION_WHERE_USER_NAME_$1 = """
            SELECT id, user_name, roles, valid_time, create_time, update_time, enabled, visible, flags
              FROM security.session
             WHERE user_name = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_SECURITY_SESSION_ORDER_BY_ID_ASC = """
            SELECT id, user_name, roles, valid_time, create_time, update_time, enabled, visible, flags
              FROM security.session
             WHERE enabled
             ORDER BY id ASC
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_SECURITY_SESSION_ORDER_BY_ID_ASC_OFFSET_LIMIT = """
            SELECT id, user_name, roles, valid_time, create_time, update_time, enabled, visible, flags
              FROM security.session
             WHERE enabled
             ORDER BY id ASC OFFSET $1 LIMIT $2
            """;
    @Language("SQL")
    public static final String UPDATE_SECURITY_SESSION_WHERE_ID_$1 = """
            UPDATE security.session SET
              user_name = $2,
              roles = $3,
              valid_time = $4,
              enabled = $5,
              visible = $6,
              flags = $7
             WHERE id = $1
             RETURNING id
            """;

    public static Builder builder() {
        return new Builder();
    }

    public static SessionTable from(Row row) {
        return new SessionTable(
                row.getUUID(ID),
                row.getString("user_name"),
                Set.of(row.getArrayOfStrings("roles")),
                row.getLocalDateTime("valid_time"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    @Override
    public String caseInsertSql() {
        return id != null ? INSERT_INTO_SECURITY_SESSION : INSERT_INTO_SECURITY_SESSION_DEFAULT_ID;
    }

    @Override
    public Tuple caseInsertTuple() {
        return id != null
                ? Tuple.tuple(listOf())
                : Tuple.of(userName, roles.toArray(), validTime, enabled, visible, flags);
    }

    @Override
    public String deleteSql() {
        return DELETE_FROM_SECURITY_SESSION_WHERE_ID_$1;
    }

    @Override
    public String updateSql() {
        return UPDATE_SECURITY_SESSION_WHERE_ID_$1;
    }

    @Override
    public Tuple updateTuple() {
        return Tuple.tuple(listOf());
    }

    private List<Object> listOf() {
        return Arrays.asList(id, userName, roles.toArray(), validTime, enabled, visible, flags);
    }

    public static final class Builder {
        private @ModelField UUID id;
        private @Nonnull
        @ModelField(nullable = false) String userName;
        private @Nonnull
        @ModelField(nullable = false) Set<String> roles;
        private @Nonnull
        @ModelField(nullable = false) LocalDateTime validTime;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private @ModelField boolean visible;
        private @ModelField int flags;

        private Builder() {
            this.userName = SessionTable.NONE;
            this.roles = Collections.emptySet();
            this.validTime = TimeUtil.EPOCH_UTC;
            this.enabled = true;
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder userName(@Nonnull String userName) {
            this.userName = userName;
            return this;
        }

        public Builder roles(@Nonnull Set<String> roles) {
            this.roles = roles;
            return this;
        }

        public Builder validTime(@Nonnull LocalDateTime validTime) {
            this.validTime = validTime;
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

        public SessionTable build() {
            return new SessionTable(id, userName, roles, validTime, createTime, updateTime, enabled, visible, flags);
        }
    }
}
