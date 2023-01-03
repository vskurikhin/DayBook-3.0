/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Vocabulary.java
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
import su.svn.daybook.annotations.ModelField;
import su.svn.daybook.models.LongIdentification;
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

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class VocabularyTable implements LongIdentification, Marked, Owned, TimeUpdated, Serializable {

    public static final String NONE = "5f8c1804-5c59-43b6-9099-c1820cffc001";
    public static final String SELECT_FROM_DICTIONARY_VOCABULARY_WHERE_ID_$1 = """
            SELECT id, word, value, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.vocabulary
             WHERE id = $1 AND enabled
            """;
    public static final String SELECT_ALL_FROM_DICTIONARY_VOCABULARY_ORDER_BY_ID_ASC = """
            SELECT id, word, value, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.vocabulary
             WHERE enabled
             ORDER BY id ASC
            """;
    public static final String SELECT_ALL_FROM_DICTIONARY_VOCABULARY_ORDER_BY_ID_ASC_OFFSET_LIMIT = """
            SELECT id, word, value, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.vocabulary
             WHERE enabled
             ORDER BY id ASC OFFSET $1 LIMIT $2
            """;
    public static final String INSERT_INTO_DICTIONARY_VOCABULARY = """
            INSERT INTO dictionary.vocabulary
             (id, word, value, user_name, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5, $6)
             RETURNING id
            """;
    public static final String UPDATE_DICTIONARY_VOCABULARY_WHERE_ID_$1 = """
            UPDATE dictionary.vocabulary SET
              word = $2,
              user_name = $3,
              enabled = $4,
              visible = $5,
              flags = $6,
              value = $7
             WHERE id = $1
             RETURNING id
            """;
    public static final String DELETE_FROM_DICTIONARY_VOCABULARY_WHERE_ID_$1 = """
            DELETE FROM dictionary.vocabulary
             WHERE id = $1
             RETURNING id
            """;
    public static final String COUNT_DICTIONARY_VOCABULARY = "SELECT count(*) FROM dictionary.vocabulary";
    @Serial
    private static final long serialVersionUID = -71735002217330331L;
    public static final String ID = "id";
    @ModelField
    private final Long id;
    @ModelField
    private final String word;
    @ModelField
    private final String value;
    private final String userName;
    private final LocalDateTime createTime;
    private final LocalDateTime updateTime;
    private final boolean enabled;
    @ModelField
    private final boolean visible;
    @ModelField
    private final int flags;

    private transient volatile int hash;

    private transient volatile boolean hashIsZero;

    public VocabularyTable() {
        this.id = null;
        this.word = NONE;
        this.value = null;
        this.userName = null;
        this.createTime = null;
        this.updateTime = null;
        this.enabled = true;
        this.visible = true;
        this.flags = 0;
    }

    public VocabularyTable(
            Long id,
            @Nonnull String word,
            String value,
            String userName,
            LocalDateTime createTime,
            LocalDateTime updateTime,
            boolean enabled,
            boolean visible,
            int flags) {
        this.id = id;
        this.word = word;
        this.value = value;
        this.userName = userName;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.enabled = enabled;
        this.visible = visible;
        this.flags = flags;
    }

    public static VocabularyTable from(Row row) {
        return new VocabularyTable(
                row.getLong(ID),
                row.getString("word"),
                row.getString("value"),
                row.getString("user_name"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public static Multi<VocabularyTable> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_DICTIONARY_VOCABULARY_ORDER_BY_ID_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(VocabularyTable::from);

    }

    public static Uni<VocabularyTable> findById(PgPool client, Long id) {
        return client
                .preparedQuery(SELECT_FROM_DICTIONARY_VOCABULARY_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? VocabularyTable.from(iterator.next()) : null);
    }

    public static Multi<VocabularyTable> findRange(PgPool client, long offset, long limit) {
        return client
                .preparedQuery(SELECT_ALL_FROM_DICTIONARY_VOCABULARY_ORDER_BY_ID_ASC_OFFSET_LIMIT)
                .execute(Tuple.of(offset, limit))
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(VocabularyTable::from);
    }

    public static Uni<Long> delete(PgPool client, Long id) {
        return client.withTransaction(
                connection -> connection.preparedQuery(DELETE_FROM_DICTIONARY_VOCABULARY_WHERE_ID_$1)
                        .execute(Tuple.of(id))
                        .onItem()
                        .transform(pgRowSet -> pgRowSet.iterator().next().getLong(ID)));
    }

    public static Uni<Long> count(PgPool client) {
        return client
                .preparedQuery(COUNT_DICTIONARY_VOCABULARY)
                .execute()
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong("count"));
    }

    public static VocabularyTable.Builder builder() {
        return new VocabularyTable.Builder();
    }

    public Uni<Long> insert(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(INSERT_INTO_DICTIONARY_VOCABULARY)
                        .execute(Tuple.of(word, value, userName, enabled, visible, flags))
                        .onItem()
                        .transform(RowSet::iterator)
                        .onItem()
                        .transform(iterator -> iterator.hasNext() ? iterator.next().getLong(ID) : null));
    }

    public Uni<Long> update(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(UPDATE_DICTIONARY_VOCABULARY_WHERE_ID_$1)
                        .execute(Tuple.tuple(listOf()))
                        .onItem()
                        .transform(pgRowSet -> pgRowSet.iterator().next().getLong(ID)));
    }

    private List<Object> listOf() {
        return Arrays.asList(id, word, userName, enabled, visible, flags, value);
    }

    public Long getId() {
        return id;
    }

    public String getWord() {
        return word;
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
        VocabularyTable that = (VocabularyTable) o;
        return enabled == that.enabled
                && visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(word, that.word)
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
        return Objects.hash(id, word, value, userName, enabled, visible, flags);
    }

    @Override
    public String toString() {
        return "Vocabulary{" +
                "id=" + id +
                ", word='" + word + '\'' +
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
        private Long id;
        private String word;
        private String value;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private boolean visible;
        private int flags;

        private Builder() {
            this.enabled = true;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder word(String word) {
            this.word = word;
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

        public VocabularyTable build() {
            return new VocabularyTable(id, word, value, userName, createTime, updateTime, enabled, visible, flags);
        }
    }
}
