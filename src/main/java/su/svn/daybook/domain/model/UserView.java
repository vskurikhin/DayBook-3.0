/*
 * This file was last modified at 2023.01.11 12:53 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * UserView.java
 * $Id$
 */

package su.svn.daybook.domain.model;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import org.intellij.lang.annotations.Language;
import su.svn.daybook.annotations.ModelField;
import su.svn.daybook.models.Marked;
import su.svn.daybook.models.Owned;
import su.svn.daybook.models.TimeUpdated;
import su.svn.daybook.models.UUIDIdentification;

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

    public static final String NONE = UserNameTable.NONE;
    public static final String ID = "id";
    public static final String COUNT = "count";
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
    public static final String COUNT_SECURITY_USER_VIEW = "SELECT count(*) FROM security.user_view";

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

    public static Multi<UserView> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_SECURITY_USER_VIEW_ORDER_BY_USER_NAME_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(UserView::from);

    }

    public static Uni<UserView> findById(PgPool client, UUID id) {
        return client
                .preparedQuery(SELECT_FROM_SECURITY_USER_VIEW_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? UserView.from(iterator.next()) : null);
    }

    public static Uni<UserView> findByUserName(PgPool client, String userName) {
        return client
                .preparedQuery(SELECT_FROM_SECURITY_USER_VIEW_WHERE_USER_NAME_$1)
                .execute(Tuple.of(userName))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? UserView.from(iterator.next()) : null);
    }

    public static Multi<UserView> findRange(PgPool client, long offset, long limit) {
        return client
                .preparedQuery(SELECT_ALL_FROM_SECURITY_USER_VIEW_ORDER_BY_USER_NAME_ASC_OFFSET_LIMIT)
                .execute(Tuple.of(offset, limit))
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(UserView::from);
    }

    public static Uni<Long> count(PgPool client) {
        return client
                .preparedQuery(COUNT_SECURITY_USER_VIEW)
                .execute()
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong(COUNT));
    }

    public static UserView.Builder builder() {
        return new UserView.Builder();
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
