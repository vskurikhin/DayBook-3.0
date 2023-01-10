/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * SessionTable.java
 * $Id$
 */

package su.svn.daybook.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;
import su.svn.daybook.annotations.ModelField;
import su.svn.daybook.models.Marked;
import su.svn.daybook.models.Owned;
import su.svn.daybook.models.TimeUpdated;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class SessionTable implements CasesOfUUID, Marked, Owned, TimeUpdated, Serializable {

    public static final String NONE = "3de2845b-eb5f-49e5-a1b4-ed90abd92c52";
    public static final String SELECT_FROM_SECURITY_SESSION_WHERE_ID_$1 = """
            SELECT id, user_name, roles, valid_time, create_time, update_time, enabled, visible, flags
              FROM security.session
             WHERE id = $1 AND enabled
            """;
    public static final String SELECT_ALL_FROM_SECURITY_SESSION_ORDER_BY_ID_ASC = """
            SELECT id, user_name, roles, valid_time, create_time, update_time, enabled, visible, flags
              FROM security.session
             WHERE enabled
             ORDER BY id ASC
            """;
    public static final String SELECT_ALL_FROM_SECURITY_SESSION_ORDER_BY_ID_ASC_OFFSET_LIMIT = """
            SELECT id, user_name, roles, valid_time, create_time, update_time, enabled, visible, flags
              FROM security.session
             WHERE enabled
             ORDER BY id ASC OFFSET $1 LIMIT $2
            """;
    public static final String INSERT_INTO_SECURITY_SESSION = """
            INSERT INTO security.session
             (id, user_name, roles, valid_time, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6, $7)
             RETURNING id
            """;
    public static final String INSERT_INTO_SECURITY_SESSION_DEFAULT_ID = """
            INSERT INTO security.session
             (id, user_name, roles, valid_time, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5, $6)
             RETURNING id
            """;
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
    public static final String DELETE_FROM_SECURITY_SESSION_WHERE_ID_$1 = """
            DELETE FROM security.session
             WHERE id = $1
             RETURNING id
            """;
    public static final String COUNT_SECURITY_SESSION = "SELECT count(*) FROM security.session";
    @Serial
    private static final long serialVersionUID = -3840183181082947551L;
    public static final String ID = "id";
    public static final String COUNT = "count";
    @ModelField
    private final UUID id;
    @ModelField(nullable = false)
    private final String userName;
    @ModelField(nullable = false)
    private final Set<String> roles;
    @ModelField(nullable = false)
    private final LocalDateTime validTime;
    private final LocalDateTime createTime;
    private final LocalDateTime updateTime;
    private final boolean enabled;
    @ModelField
    private final boolean visible;
    @ModelField
    private final int flags;

    @JsonIgnore
    private transient volatile int hash;

    @JsonIgnore
    private transient volatile boolean hashIsZero;

    public SessionTable() {
        this.id = null;
        this.userName = SessionTable.NONE;
        this.roles = Collections.emptySet();
        this.validTime = LocalDateTime.ofEpochSecond(0, 0, ZoneOffset.UTC);
        this.createTime = null;
        this.updateTime = null;
        this.enabled = true;
        this.visible = true;
        this.flags = 0;
    }

    public SessionTable(
            UUID id,
            @Nonnull String userName,
            @Nonnull Set<String> roles,
            @Nonnull LocalDateTime validTime,
            LocalDateTime createTime,
            LocalDateTime updateTime,
            boolean enabled,
            boolean visible,
            int flags) {
        this.id = id;
        this.userName = userName;
        this.roles = roles;
        this.validTime = validTime;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.enabled = enabled;
        this.visible = visible;
        this.flags = flags;
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

    public static Multi<SessionTable> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_SECURITY_SESSION_ORDER_BY_ID_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(SessionTable::from);

    }

    public static Uni<SessionTable> findById(PgPool client, UUID id) {
        return client
                .preparedQuery(SELECT_FROM_SECURITY_SESSION_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? SessionTable.from(iterator.next()) : null);
    }

    public static Multi<SessionTable> findRange(PgPool client, long offset, long limit) {
        return client
                .preparedQuery(SELECT_ALL_FROM_SECURITY_SESSION_ORDER_BY_ID_ASC_OFFSET_LIMIT)
                .execute(Tuple.of(offset, limit))
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(SessionTable::from);
    }

    public static Uni<UUID> delete(PgPool client, UUID id) {
        return client.withTransaction(
                connection -> connection.preparedQuery(DELETE_FROM_SECURITY_SESSION_WHERE_ID_$1)
                        .execute(Tuple.of(id))
                        .onItem()
                        .transform(pgRowSet -> pgRowSet.iterator().next().getUUID(ID)));
    }

    public static Uni<Long> count(PgPool client) {
        return client
                .preparedQuery(COUNT_SECURITY_SESSION)
                .execute()
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong(COUNT));
    }

    public static Builder builder() {
        return new Builder();
    }

    public Uni<UUID> insert(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(caseInsertSql())
                        .execute(caseInsertTuple())
                        .onItem()
                        .transform(RowSet::iterator)
                        .onItem()
                        .transform(iterator -> iterator.hasNext() ? iterator.next().getUUID(ID) : null));
    }

    public Uni<UUID> update(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(UPDATE_SECURITY_SESSION_WHERE_ID_$1)
                        .execute(updateTuple())
                        .onItem()
                        .transform(pgRowSet -> pgRowSet.iterator().next().getUUID(ID)));
    }

    private String caseInsertSql() {
        return id != null ? INSERT_INTO_SECURITY_SESSION : INSERT_INTO_SECURITY_SESSION_DEFAULT_ID;
    }

    @Override
    public Tuple caseInsertTuple() {
        return id != null
                ? Tuple.tuple(listOf())
                : Tuple.of(userName, roles.toArray(), validTime, enabled, visible, flags);
    }

    @Override
    public Tuple updateTuple() {
        return Tuple.tuple(listOf());
    }

    private List<Object> listOf() {
        return Arrays.asList(id, userName, roles.toArray(), validTime, enabled, visible, flags);
    }

    public UUID id() {
        return id;
    }

    public String userName() {
        return userName;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public LocalDateTime getValidTime() {
        return validTime;
    }

    public LocalDateTime createTime() {
        return createTime;
    }

    public LocalDateTime updateTime() {
        return updateTime;
    }

    public boolean enabled() {
        return enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public boolean visible() {
        return visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public int flags() {
        return flags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (SessionTable) o;
        return enabled == that.enabled
                && visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(userName, that.userName)
                && Objects.equals(roles, that.roles)
                && Objects.equals(validTime, that.validTime);
    }

    @Override
    public int hashCode() {
        int h = hash;
        if (h == 0 && !hashIsZero) {
            h = calculateHashCode();
            if (h == 0) {
                hashIsZero = true;
            } else {
                hash = h;
            }
        }
        return h;
    }

    private int calculateHashCode() {
        return Objects.hash(id, userName, roles, validTime, enabled, visible, flags);
    }

    @Override
    public String toString() {
        return "SessionTable{" +
                "id=" + id +
                ", userName='" + userName + '\'' +
                ", roles='" + roles + '\'' +
                ", validTime='" + validTime + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", enabled=" + enabled +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private UUID id;
        private String userName;
        private Set<String> roles;
        private LocalDateTime validTime;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private boolean visible;
        private int flags;

        private Builder() {
            this.roles = Collections.emptySet();
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
