/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18n.java
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
public final class I18nTable implements LongIdentification, Marked, Owned, TimeUpdated, Serializable {

    public static final String SELECT_FROM_DICTIONARY_I18N_WHERE_ID_$1 = """
            SELECT id, language_id, message, translation, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.i18n
             WHERE id = $1 AND enabled
            """;
    public static final String SELECT_ALL_FROM_DICTIONARY_I18N_ORDER_BY_ID_ASC = """
            SELECT id, language_id, message, translation, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.i18n
             WHERE enabled
             ORDER BY id ASC
            """;
    public static final String SELECT_ALL_FROM_DICTIONARY_KEY_VALUE_ORDER_BY_ID_ASC_OFFSET_LIMIT = """
            SELECT id, language_id, message, translation, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.i18n
             WHERE enabled
             ORDER BY id ASC OFFSET $1 LIMIT $2
            """;
    public static final String INSERT_INTO_DICTIONARY_I18N = """
            INSERT INTO dictionary.i18n
             (id, language_id, message, translation, user_name, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6, $7, $8)
             RETURNING id
            """;
    public static final String INSERT_INTO_DICTIONARY_I18N_DEFAULT_ID = """
            INSERT INTO dictionary.i18n
             (id, language_id, message, translation, user_name, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5, $6, $7)
             RETURNING id
            """;
    public static final String UPDATE_DICTIONARY_I18N_WHERE_ID_$1 = """
            UPDATE dictionary.i18n SET
              language_id = $2,
              message = $3,
              translation = $4,
              user_name = $5,
              enabled = $6,
              visible = $7,
              flags = $8
             WHERE id = $1
             RETURNING id
            """;
    public static final String DELETE_FROM_DICTIONARY_I18N_WHERE_ID_$1 = """
            DELETE FROM dictionary.i18n
             WHERE id = $1
             RETURNING id
            """;
    public static final String COUNT_DICTIONARY_I18N = "SELECT count(*) FROM dictionary.i18n";
    @Serial
    private static final long serialVersionUID = -3886622244418636664L;
    public static final String ID = "id";
    private final Long id;
    private final Long languageId;
    private final String message;
    private final String translation;
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

    public I18nTable() {
        this.id = null;
        this.languageId = 0L;
        this.message = null;
        this.translation = null;
        this.userName = null;
        this.createTime = null;
        this.updateTime = null;
        this.enabled = true;
        this.visible = true;
        this.flags = 0;
    }

    public I18nTable(
            Long id,
            @Nonnull Long languageId,
            String message,
            String translation,
            String userName,
            LocalDateTime createTime,
            LocalDateTime updateTime,
            boolean enabled,
            boolean visible,
            int flags) {
        this.id = id;
        this.languageId = languageId;
        this.message = message;
        this.translation = translation;
        this.userName = userName;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.enabled = enabled;
        this.visible = visible;
        this.flags = flags;
    }

    public static I18nTable from(Row row) {
        return new I18nTable(
                row.getLong(ID),
                row.getLong("language_id"),
                row.getString("message"),
                row.getString("translation"),
                row.getString("user_name"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public static Multi<I18nTable> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_DICTIONARY_I18N_ORDER_BY_ID_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(I18nTable::from);

    }

    public static Uni<I18nTable> findById(PgPool client, Long id) {
        return client
                .preparedQuery(SELECT_FROM_DICTIONARY_I18N_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? I18nTable.from(iterator.next()) : null);
    }

    public static Multi<I18nTable> findRange(PgPool client, long offset, long limit) {
        return client
                .preparedQuery(SELECT_ALL_FROM_DICTIONARY_KEY_VALUE_ORDER_BY_ID_ASC_OFFSET_LIMIT)
                .execute(Tuple.of(offset, limit))
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(I18nTable::from);
    }

    public static Uni<Long> delete(PgPool client, Long id) {
        return client.withTransaction(
                connection -> connection.preparedQuery(DELETE_FROM_DICTIONARY_I18N_WHERE_ID_$1)
                        .execute(Tuple.of(id))
                        .onItem()
                        .transform(pgRowSet -> pgRowSet.iterator().next().getLong(ID)));
    }

    public static Uni<Long> count(PgPool client) {
        return client
                .preparedQuery(COUNT_DICTIONARY_I18N)
                .execute()
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong("count"));
    }

    public static I18nTable.Builder builder() {
        return new I18nTable.Builder();
    }

    public Uni<Long> insert(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(caseInsertSql())
                        .execute(caseInsertTuple())
                        .onItem()
                        .transform(RowSet::iterator)
                        .onItem()
                        .transform(iterator -> iterator.hasNext() ? iterator.next().getLong(ID) : null));
    }

    public Uni<Long> update(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(UPDATE_DICTIONARY_I18N_WHERE_ID_$1)
                        .execute(Tuple.tuple(listOf()))
                        .onItem()
                        .transform(pgRowSet -> pgRowSet.iterator().next().getLong(ID)));
    }

    private String caseInsertSql() {
        return id != null ? INSERT_INTO_DICTIONARY_I18N : INSERT_INTO_DICTIONARY_I18N_DEFAULT_ID;
    }

    private Tuple caseInsertTuple() {
        return id != null ?
                Tuple.tuple(listOf())
                : Tuple.tuple(Arrays.asList(languageId, message, translation, userName, enabled, visible, flags));
    }

    private List<Object> listOf() {
        return Arrays.asList(id, languageId, message, translation, userName, enabled, visible, flags);
    }

    public Long getId() {
        return id;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public String getMessage() {
        return message;
    }

    public String getTranslation() {
        return translation;
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
        var that = (I18nTable) o;
        return enabled == that.enabled
                && visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(languageId, that.languageId)
                && Objects.equals(message, that.message)
                && Objects.equals(translation, that.translation)
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
        return Objects.hash(id, languageId, message, translation, userName, enabled, visible, flags);
    }

    @Override
    public String toString() {
        return "I18n{" +
                "id=" + id +
                ", languageId='" + languageId + '\'' +
                ", message='" + message + '\'' +
                ", translation='" + translation + '\'' +
                ", userName='" + userName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", enabled=" + enabled +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private Long id;
        private Long languageId;
        private String message;
        private String translation;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private boolean visible;
        private int flags;

        private Builder() {
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder languageId(long languageId) {
            this.languageId = languageId;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder translation(String translation) {
            this.translation = translation;
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

        public I18nTable build() {
            return new I18nTable(
                    id, languageId, message, translation, userName, createTime, updateTime, enabled, visible, flags
            );
        }
    }
}
