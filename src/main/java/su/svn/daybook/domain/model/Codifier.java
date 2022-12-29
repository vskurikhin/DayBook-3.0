/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Codifier.java
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

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Codifier implements StringIdentification, Marked, Owned, TimeUpdated, Serializable {

    public static final String NONE = "b19bba7c-d53a-4174-9475-6ae9d7b9bbee";
    public static final String SELECT_FROM_DICTIONARY_CODIFIER_WHERE_CODE_$1 = """
            SELECT code, value, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.codifier
             WHERE code = $1
            """;
    public static final String SELECT_ALL_FROM_DICTIONARY_CODIFIER_ORDER_BY_CODE_ASC = """
            SELECT code, value, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.codifier
             ORDER BY code ASC
            """;
    public static final String INSERT_INTO_DICTIONARY_CODIFIER = """
            INSERT INTO dictionary.codifier
             (code, value, user_name, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6)
             RETURNING code
            """;
    public static final String UPDATE_DICTIONARY_CODIFIER_WHERE_CODE_$1 = """
            UPDATE dictionary.codifier SET
              user_name = $2,
              enabled = $3,
              visible = $4,
              flags = $5,
              value = $6
             WHERE code = $1
             RETURNING code
            """;
    public static final String DELETE_FROM_DICTIONARY_CODIFIER_WHERE_CODE_$1 = """
            DELETE FROM dictionary.codifier
             WHERE code = $1
             RETURNING code
            """;
    public static final String COUNT_DICTIONARY_CODIFIER = "SELECT count(*) FROM dictionary.codifier";
    @Serial
    private static final long serialVersionUID = 260226838732667047L;
    public static final String ID = "code";
    private final String code;
    private final String value;
    private final String userName;
    private final LocalDateTime createTime;
    private final LocalDateTime updateTime;
    private final boolean enabled;
    private final boolean visible;
    private final int flags;

    @JsonIgnore
    private transient volatile int hash;

    @JsonIgnore
    private transient volatile boolean hashIsZero;

    public Codifier() {
        this.code = NONE;
        this.value = null;
        this.userName = null;
        this.createTime = null;
        this.updateTime = null;
        this.enabled = false;
        this.visible = true;
        this.flags = 0;
    }

    public Codifier(
            @Nonnull String code,
            String value,
            String userName,
            LocalDateTime createTime,
            LocalDateTime updateTime,
            boolean enabled,
            boolean visible,
            int flags) {
        this.code = code;
        this.value = value;
        this.userName = userName;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.enabled = enabled;
        this.visible = visible;
        this.flags = flags;
    }

    public static Codifier from(Row row) {
        return new Codifier(
                row.getString(ID),
                row.getString("value"),
                row.getString("user_name"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public static Uni<Codifier> findById(PgPool client, String id) {
        return client
                .preparedQuery(SELECT_FROM_DICTIONARY_CODIFIER_WHERE_CODE_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? Codifier.from(iterator.next()) : null);
    }

    public static Multi<Codifier> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_DICTIONARY_CODIFIER_ORDER_BY_CODE_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(Codifier::from);

    }

    public static Uni<String> delete(PgPool client, String id) {
        return client.withTransaction(
                connection -> connection.preparedQuery(DELETE_FROM_DICTIONARY_CODIFIER_WHERE_CODE_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getString(ID)));
    }

    public static Uni<Long> count(PgPool client) {
        return client
                .preparedQuery(COUNT_DICTIONARY_CODIFIER)
                .execute()
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong("count"));
    }

    public static Codifier.Builder builder() {
        return new Codifier.Builder();
    }

    public Uni<String> insert(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(INSERT_INTO_DICTIONARY_CODIFIER)
                        .execute(Tuple.of(code, value, userName, enabled, visible, flags))
                        .onItem()
                        .transform(RowSet::iterator)
                        .onItem()
                        .transform(iterator -> iterator.hasNext() ? iterator.next().getString(ID) : null));
    }

    public Uni<String> update(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(UPDATE_DICTIONARY_CODIFIER_WHERE_CODE_$1)
                        .execute(Tuple.tuple(listOf()))
                        .onItem()
                        .transform(pgRowSet -> pgRowSet.iterator().next().getString(ID)));
    }

    private List<Object> listOf() {
        return Arrays.asList(code, userName, enabled, visible, flags, value);
    }

    public String getId() {
        return code;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
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
        var that = (Codifier) o;
        return enabled == that.enabled
                && visible == that.visible
                && flags == that.flags
                && Objects.equals(code, that.code)
                && Objects.equals(value, that.value)
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
        return Objects.hash(code, value, userName, enabled, visible, flags);
    }

    @Override
    public String toString() {
        return "Codifier{" +
                "code='" + code + '\'' +
                ", value='" + value + '\'' +
                ", userName='" + userName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", enabled=" + enabled +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private String code;
        private String value;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private boolean visible;
        private int flags;

        private Builder() {
        }

        public Builder id(String id) {
            this.code = id;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
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

        public Codifier build() {
            return new Codifier(code, value, userName, createTime, updateTime, enabled, visible, flags);
        }
    }
}
