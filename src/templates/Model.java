/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * @Name@.java
 * $Id$
 */

package su.svn.daybook.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class @Name@ implements LongIdentification, Marked, Owned, TimeUpdated, Serializable {

    @Serial
    private static final long serialVersionUID = @serialVersionUID@L;

    public static final String NONE = "_/NONE\\_";

    private final Long id;

    private final String @key@;

    private final String @value@;

    private final String userName;

    private final LocalDateTime createTime;

    private final LocalDateTime updateTime;

    private final boolean enabled;

    private final boolean visible;

    private final int flags;

    public static final String SELECT_FROM_@SCHEMA@_@TABLE@_WHERE_ID_$1 = """
            SELECT id, @key@, @value@, user_name, create_time, update_time, enabled, visible, flags
              FROM @schema@.@table@
             WHERE id = $1
            """;

    public static final String SELECT_ALL_FROM_@SCHEMA@_@TABLE@_ORDER_BY_ID_ASC = """
            SELECT id, @key@, @value@, user_name, create_time, update_time, enabled, visible, flags
              FROM @schema@.@table@
             ORDER BY id ASC
            """;

    public static final String INSERT_INTO_@SCHEMA@_@TABLE@ = """
            INSERT INTO @schema@.@table@
             (id, @key@, @value@, user_name, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5, $6, $7)
             RETURNING id
            """;

    public static final String UPDATE_@SCHEMA@_@TABLE@_WHERE_ID_$1 = """
            UPDATE @schema@.@table@ SET
              @key@ = $2,
              user_name = $3,
              enabled = $4,
              visible = $5,
              flags = $6,
              @value@ = $7
             WHERE id = $1
             RETURNING id
            """;

    public static final String DELETE_FROM_@SCHEMA@_@TABLE@_WHERE_ID_$1 = """
            DELETE FROM @schema@.@table@
             WHERE id = $1
             RETURNING id
            """;

    public static @Name@ from(Row row) {
        return new @Name@(
                row.getLong("id"),
                row.getString("@key@"),
                row.getString("@value@"),
                row.getString("user_name"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public static Uni<@Name@> findById(PgPool client, Long id) {
        return client.preparedQuery(SELECT_FROM_@SCHEMA@_@TABLE@_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? @Name@.from(iterator.next()) : null);
    }

    public static Multi<@Name@> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_@SCHEMA@_@TABLE@_ORDER_BY_ID_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(@Name@::from);

    }

    public Uni<Long> insert(PgPool client) {
        return client.preparedQuery(INSERT_INTO_@SCHEMA@_@TABLE@)
                .execute(Tuple.of(@key@, @value@, userName, enabled, visible, flags))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? iterator.next().getLong("id") : null);
    }

    public Uni<Long> update(PgPool client) {
        return client.preparedQuery(UPDATE_@SCHEMA@_@TABLE@_WHERE_ID_$1)
                .execute(Tuple.of(listOf()))
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong("id"));
    }

    public static Uni<Long> delete(PgPool client, Long id) {
        return client.preparedQuery(DELETE_FROM_@SCHEMA@_@TABLE@_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong("id"));
    }

    private List<?> listOf() {
        return Arrays.asList(id, @key@, userName, enabled, visible, flags, @value@);
    }

    public @Name@() {
        this.id = null;
        this.@key@ = NONE;
        this.@value@ = null;
        this.userName = null;
        this.createTime = null;
        this.updateTime = null;
        this.enabled = false;
        this.visible = true;
        this.flags = 0;
    }

    public @Name@(
            Long id,
            @Nonnull String @key@,
            String @value@,
            String userName,
            LocalDateTime createTime,
            LocalDateTime updateTime,
            boolean enabled,
            boolean visible,
            int flags) {
        this.id = id;
        this.@key@ = @key@;
        this.@value@ = @value@;
        this.userName = userName;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.enabled = enabled;
        this.visible = visible;
        this.flags = flags;
    }

    public Long getId() {
        return id;
    }

    public String get@Key@() {
        return @key@;
    }

    public String get@Value@() {
        return @value@;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public boolean getVisible() {
        return visible;
    }

    public int getFlags() {
        return flags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        @Name@ that = (@Name@) o;
        return enabled == that.enabled
                && visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(@key@, that.@key@)
                && Objects.equals(@value@, that.@value@)
                && Objects.equals(userName, that.userName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, @key@, @value@, userName, enabled, visible, flags);
    }

    @Override
    public String toString() {
        return "@Name@{" +
                "id=" + id +
                ", @key@='" + @key@ + '\'' +
                ", @value@='" + @value@ + '\'' +
                ", userName='" + userName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", enabled=" + enabled +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static @Name@.Builder builder() {
        return new @Name@.Builder();
    }

    public static final class Builder {
        private Long id;
        private String @key@;
        private String @value@;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private boolean visible;
        private int flags;

        private Builder() {
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder with@Key@(@Nonnull String @key@) {
            this.@key@ = @key@;
            return this;
        }

        public Builder with@Value@(String @value@) {
            this.@value@ = @value@;
            return this;
        }

        public Builder withUserName(String userName) {
            this.userName = userName;
            return this;
        }

        public Builder withCreateTime(LocalDateTime createTime) {
            this.createTime = createTime;
            return this;
        }

        public Builder withUpdateTime(LocalDateTime updateTime) {
            this.updateTime = updateTime;
            return this;
        }

        public Builder withEnabled(boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder withVisible(boolean visible) {
            this.visible = visible;
            return this;
        }

        public Builder withFlags(int flags) {
            this.flags = flags;
            return this;
        }

        public @Name@ build() {
            return new @Name@(
                    id, @key@, @value@, userName, createTime, updateTime, enabled, visible, flags
            );
        }
    }
}
