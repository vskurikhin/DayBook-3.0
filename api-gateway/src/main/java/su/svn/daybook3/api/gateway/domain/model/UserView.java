/*
 * This file was last modified at 2024-05-14 21:36 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserView.java
 * $Id$
 */

package su.svn.daybook3.api.gateway.domain.model;

import io.vertx.mutiny.sqlclient.Row;
import org.intellij.lang.annotations.Language;
import su.svn.daybook3.api.gateway.annotations.ModelField;
import su.svn.daybook3.api.gateway.models.Marked;
import su.svn.daybook3.api.gateway.models.Owned;
import su.svn.daybook3.api.gateway.models.TimeUpdated;
import su.svn.daybook3.api.gateway.models.UUIDIdentification;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record UserView(
        @ModelField UUID id,
        @ModelField(nullable = false) String userName,
        @ModelField(onlyBuildPart = true) String password,
        @ModelField(nullable = false) Set<String> roles,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean enabled,
        @ModelField boolean visible,
        @ModelField int flags)
        implements UUIDIdentification, Marked, Owned, TimeUpdated, Serializable {

    public static final String ID = "id";
    public static final String NONE = UserNameTable.NONE;
    @Language("SQL")
    public static final String COUNT_SECURITY_USER_VIEW = "SELECT count(*) FROM security.user_view WHERE enabled";
    @Language("SQL")
    public static final String SELECT_ALL_FROM_SECURITY_USER_VIEW_ORDER_BY_USER_NAME_ASC = """
            SELECT id, user_name, password, create_time, update_time, enabled, visible, flags, roles
              FROM security.user_view
             WHERE enabled
             ORDER BY user_name ASC
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_SECURITY_USER_VIEW_ORDER_BY_USER_NAME_ASC_OFFSET_LIMIT = """
            SELECT id, user_name, password, create_time, update_time, enabled, visible, flags, roles
              FROM security.user_view
             WHERE enabled
             ORDER BY user_name ASC OFFSET $1 LIMIT $2
            """;
    @Language("SQL")
    public static final String SELECT_FROM_SECURITY_USER_VIEW_WHERE_ID_$1 = """
            SELECT id, user_name, password, create_time, update_time, enabled, visible, flags, roles
              FROM security.user_view
             WHERE id = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_SECURITY_USER_VIEW_WHERE_USER_NAME_$1 = """
            SELECT id, user_name, password, create_time, update_time, enabled, visible, flags, roles
              FROM security.user_view
             WHERE user_name = $1 AND enabled
            """;

    public static UserView from(Row row) {
        return new UserView(
                row.getUUID(ID),
                row.getString("user_name"),
                row.getString("password"),
                Set.of(row.getArrayOfStrings("roles")),
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

    public static final class Builder {
        private @ModelField UUID id;
        private @ModelField(nullable = false) String userName;
        private @ModelField(onlyBuildPart = true) String password;
        private @ModelField(nullable = false) Set<String> roles;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private @ModelField boolean visible;
        private @ModelField int flags;

        private Builder() {
        }

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder roles(Set<String> roles) {
            this.roles = roles;
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

        public UserView build() {
            return new UserView(id, userName, password, roles, createTime, updateTime, enabled, visible, flags);
        }
    }
}
