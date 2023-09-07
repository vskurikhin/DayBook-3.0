/*
 * This file was last modified at 2023.09.07 16:35 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * @Name@Table.java
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
public record @Name@Table(
        @ModelField @IdType@ id,
        @ModelField(nullable = false) @Nonnull @KType@ @key@,
        @ModelField @VType@ @value@,
        String userName,
        LocalDateTime createTime,
        LocalDateTime updateTime,
        boolean enabled,
        @ModelField boolean visible,
        @ModelField int flags)
        implements CasesOf@IdType@, Marked, Owned, TimeUpdated, Serializable {

    public static final String ID = "id";
    public static final String NONE = "@uuid@";
    @Language("SQL")
    public static final String COUNT_@SCHEMA@_@TABLE@ = """
            SELECT count(*) FROM @schema@.@table@ WHERE enabled""";
    @Language("SQL")
    public static final String INSERT_INTO_@SCHEMA@_@TABLE@_RETURNING_S = """
            INSERT INTO @schema@.@table@
             (id, @key@, @value@, user_name, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6, $7)
             RETURNING %s
            """;
    @Language("SQL")
    public static final String INSERT_INTO_@SCHEMA@_@TABLE@_DEFAULT_ID_RETURNING_S = """
            INSERT INTO @schema@.@table@
             (id, @key@, @value@, user_name, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5, $6)
             RETURNING %s
            """;
    @Language("SQL")
    public static final String DELETE_FROM_@SCHEMA@_@TABLE@_WHERE_ID_$1_RETURNING_S = """
            DELETE FROM @schema@.@table@
             WHERE id = $1
             RETURNING %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_@SCHEMA@_@TABLE@_ORDER_BY_S = """
            SELECT id, @key@, @value@, user_name, create_time, update_time, enabled, visible, flags
              FROM @schema@.@table@
             WHERE enabled
             ORDER BY %s
            """;
    @Language("SQL")
    public static final String SELECT_ALL_FROM_@SCHEMA@_@TABLE@_ORDER_BY_S_OFFSET_$1_LIMIT_$2 = """
            SELECT id, @key@, @value@, user_name, create_time, update_time, enabled, visible, flags
              FROM @schema@.@table@
             WHERE enabled
             ORDER BY %s OFFSET $1 LIMIT $2
            """;
    @Language("SQL")
    public static final String SELECT_FROM_@SCHEMA@_@TABLE@_WHERE_ID_$1 = """
            SELECT id, @key@, @value@, user_name, create_time, update_time, enabled, visible, flags
              FROM @schema@.@table@
             WHERE id = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_@SCHEMA@_@TABLE@_WHERE_KEY_$1 = """
            SELECT id, @key@, @value@, user_name, create_time, update_time, enabled, visible, flags
              FROM @schema@.@table@
             WHERE @key@ = $1 AND enabled
            """;
    @Language("SQL")
    public static final String SELECT_FROM_@SCHEMA@_@TABLE@_WHERE_VALUE_$1 = """
            SELECT id, @key@, @value@, user_name, create_time, update_time, enabled, visible, flags
              FROM @schema@.@table@
             WHERE @value@ = $1 AND enabled
            """;
    @Language("SQL")
    public static final String UPDATE_@SCHEMA@_@TABLE@_WHERE_ID_$1_RETURNING_S = """
            UPDATE @schema@.@table@ SET
              @key@ = $2,
              @value@ = $3,
              user_name = $4,
              enabled = $5,
              visible = $6,
              flags = $7
             WHERE id = $1
             RETURNING %s
            """;

    public static Builder builder() {
        return new Builder();
    }

    public static @Name@Table from(Row row) {
        return new @Name@Table(
                row.get@IdType@(ID),
                row.getKey("@key@").to@KType@(),
                row.get@VType@("@value@"),
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
        return id != null ? INSERT_INTO_@SCHEMA@_@TABLE@_RETURNING_S : INSERT_INTO_@SCHEMA@_@TABLE@_DEFAULT_ID_RETURNING_S;
    }

    @Override
    public Tuple caseInsertTuple() {
        return id != null ? Tuple.tuple(listOf()) : Tuple.of(@key@, @value@, userName, enabled, visible, flags);
    }

    @Override
    public String deleteSql() {
        return DELETE_FROM_@SCHEMA@_@TABLE@_WHERE_ID_$1_RETURNING_S;
    }

    @Override
    public String updateSql() {
        return UPDATE_@SCHEMA@_@TABLE@_WHERE_ID_$1_RETURNING_S;
    }

    @Override
    public Tuple updateTuple() {
        return Tuple.tuple(listOf());
    }

    private List<Object> listOf() {
        return Arrays.asList(id, @key@, @value@, userName, enabled, visible, flags);
    }

    public static final class Builder {
        private @ModelField @IdType@ id;
        private @ModelField
        @Nonnull @KType@ @key@;
        private @ModelField @VType@ @value@;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private @ModelField boolean visible;
        private @ModelField int flags;

        private Builder() {
            this.@key@ = @KType@.ZERO;
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