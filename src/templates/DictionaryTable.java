/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * @Name@Table.java
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
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class @Name@Table implements CasesOf@IdType@, Marked, Owned, TimeUpdated, Serializable {

    public static final String NONE = "@uuid@";
    public static final String SELECT_FROM_@SCHEMA@_@TABLE@_WHERE_ID_$1 = """
            SELECT id, @key@, @value@, user_name, create_time, update_time, enabled, visible, flags
              FROM @schema@.@table@
             WHERE id = $1 AND enabled
            """;
    public static final String SELECT_ALL_FROM_@SCHEMA@_@TABLE@_ORDER_BY_ID_ASC = """
            SELECT id, @key@, @value@, user_name, create_time, update_time, enabled, visible, flags
              FROM @schema@.@table@
             WHERE enabled
             ORDER BY id ASC
            """;
    public static final String SELECT_ALL_FROM_@SCHEMA@_@TABLE@_ORDER_BY_ID_ASC_OFFSET_LIMIT = """
            SELECT id, @key@, @value@, user_name, create_time, update_time, enabled, visible, flags
              FROM @schema@.@table@
             WHERE enabled
             ORDER BY id ASC OFFSET $1 LIMIT $2
            """;
    public static final String INSERT_INTO_@SCHEMA@_@TABLE@ = """
            INSERT INTO @schema@.@table@
             (id, @key@, @value@, user_name, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6, $7)
             RETURNING id
            """;
    public static final String INSERT_INTO_@SCHEMA@_@TABLE@_DEFAULT_ID = """
            INSERT INTO @schema@.@table@
             (id, @key@, @value@, user_name, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5, $6)
             RETURNING id
            """;
    public static final String UPDATE_@SCHEMA@_@TABLE@_WHERE_ID_$1 = """
            UPDATE @schema@.@table@ SET
              @key@ = $2,
              @value@ = $3,
              user_name = $4,
              enabled = $5,
              visible = $6,
              flags = $7
             WHERE id = $1
             RETURNING id
            """;
    public static final String DELETE_FROM_@SCHEMA@_@TABLE@_WHERE_ID_$1 = """
            DELETE FROM @schema@.@table@
             WHERE id = $1
             RETURNING id
            """;
    public static final String COUNT_@SCHEMA@_@TABLE@ = "SELECT count(*) FROM @schema@.@table@";
    @Serial
    private static final long serialVersionUID = @serialVersionUID@L;
    public static final String ID = "id";
    public static final String COUNT = "count";
    @ModelField
    private final @IdType@ id;
    @ModelField
    private final @KType@ @key@;
    @ModelField
    private final @VType@ @value@;
    private final String userName;
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

    public @Name@Table() {
        this.id = null;
        this.@key@ = @KType@.ZERO;
        this.@value@ = null;
        this.userName = null;
        this.createTime = null;
        this.updateTime = null;
        this.enabled = true;
        this.visible = true;
        this.flags = 0;
    }

    public @Name@Table(
            @IdType@ id,
            @Nonnull @KType@ @key@,
            @VType@ @value@,
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

    public static @Name@Table from(Row row) {
        return new @Name@Table(
                row.get@IdType@(ID),
                row.get@KType@("@key@"),
                row.get@VType@("@value@"),
                row.getString("user_name"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public static Multi<@Name@Table> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_@SCHEMA@_@TABLE@_ORDER_BY_ID_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(@Name@Table::from);

    }

    public static Uni<@Name@Table> findById(PgPool client, @IdType@ id) {
        return client
                .preparedQuery(SELECT_FROM_@SCHEMA@_@TABLE@_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? @Name@Table.from(iterator.next()) : null);
    }

    public static Multi<@Name@Table> findRange(PgPool client, long offset, long limit) {
        return client
                .preparedQuery(SELECT_ALL_FROM_@SCHEMA@_@TABLE@_ORDER_BY_ID_ASC_OFFSET_LIMIT)
                .execute(Tuple.of(offset, limit))
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(@Name@Table::from);
    }

    public static Uni<@IdType@> delete(PgPool client, @IdType@ id) {
        return client.withTransaction(
                connection -> connection.preparedQuery(DELETE_FROM_@SCHEMA@_@TABLE@_WHERE_ID_$1)
                        .execute(Tuple.of(id))
                        .onItem()
                        .transform(pgRowSet -> pgRowSet.iterator().next().get@IdType@(ID)));
    }

    public static Uni<Long> count(PgPool client) {
        return client
                .preparedQuery(COUNT_@SCHEMA@_@TABLE@)
                .execute()
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong(COUNT));
    }

    public static @Name@Table.Builder builder() {
        return new @Name@Table.Builder();
    }

    public Uni<@IdType@> insert(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(caseInsertSql())
                        .execute(caseInsertTuple())
                        .onItem()
                        .transform(RowSet::iterator)
                        .onItem()
                        .transform(iterator -> iterator.hasNext() ? iterator.next().get@IdType@(ID) : null));
    }

    public Uni<@IdType@> update(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(UPDATE_@SCHEMA@_@TABLE@_WHERE_ID_$1)
                        .execute(updateTuple())
                        .onItem()
                        .transform(pgRowSet -> pgRowSet.iterator().next().get@IdType@(ID)));
    }

    private String caseInsertSql() {
        return id != null ? INSERT_INTO_@SCHEMA@_@TABLE@ : INSERT_INTO_@SCHEMA@_@TABLE@_DEFAULT_ID;
    }

    @Override
    public Tuple caseInsertTuple() {
        return id != null ? Tuple.tuple(listOf()) : Tuple.of(@key@, @value@, userName, enabled, visible, flags);
    }

    @Override
    public Tuple updateTuple() {
        return Tuple.tuple(listOf());
    }

    private List<Object> listOf() {
        return Arrays.asList(id, @key@, @value@, userName, enabled, visible, flags);
    }

    public @IdType@ getId() {
        return id;
    }

    public @KType@ get@Key@() {
        return @key@;
    }

    public @VType@ get@Value@() {
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

    public boolean isEnabled() {
        return enabled;
    }

    public boolean getVisible() {
        return visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public int getFlags() {
        return flags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (@Name@Table) o;
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
        return Objects.hash(id, @key@, @value@, userName, enabled, visible, flags);
    }

    @Override
    public String toString() {
        return "@Name@Table{" +
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

    public static final class Builder {
        private @IdType@ id;
        private @KType@ @key@;
        private @VType@ @value@;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private boolean visible;
        private int flags;

        private Builder() {
            this.enabled = true;
        }

        public Builder id(@IdType@ id) {
            this.id = id;
            return this;
        }

        public Builder @key@(@Nonnull @KType@ @key@) {
            this.@key@ = @key@;
            return this;
        }

        public Builder @value@(@VType@ @value@) {
            this.@value@ = @value@;
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

        public @Name@Table build() {
            return new @Name@Table(id, @key@, @value@, userName, createTime, updateTime, enabled, visible, flags);
        }
    }
}
