/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Word.java
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
import su.svn.daybook.models.StringIdentification;
import su.svn.daybook.models.TimeUpdated;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class WordTable implements StringIdentification, Marked, Owned, TimeUpdated, Serializable {

    public static final String NONE = "9e9574c8-990d-490a-be46-748e3160dbe1";
    public static final String SELECT_FROM_DICTIONARY_WORD_WHERE_ID_$1 = """
            SELECT word, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.word
             WHERE word = $1 AND enabled
            """;
    public static final String SELECT_ALL_FROM_DICTIONARY_WORD_ORDER_BY_ID_ASC = """
            SELECT word, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.word
             WHERE enabled
             ORDER BY word ASC
            """;
    public static final String SELECT_ALL_FROM_DICTIONARY_WORD_ORDER_BY_WORD_ASC_OFFSET_LIMIT = """
            SELECT word, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.word
             WHERE enabled
             ORDER BY word ASC OFFSET $1 LIMIT $2 
            """;
    public static final String INSERT_INTO_DICTIONARY_WORD = """
            INSERT INTO dictionary.word
             (word, user_name, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5)
             RETURNING word
            """;
    public static final String UPDATE_DICTIONARY_WORD_WHERE_ID_$1 = """
            UPDATE dictionary.word SET
              user_name = $2,
              enabled = $3,
              visible = $4,
              flags = $5
             WHERE word = $1
             RETURNING word
            """;
    public static final String DELETE_FROM_DICTIONARY_WORD_WHERE_ID_$1 = """
            DELETE FROM dictionary.word
             WHERE word = $1
             RETURNING word
            """;
    public static final String COUNT_DICTIONARY_WORD = "SELECT count(*) FROM dictionary.word";
    @Serial
    private static final long serialVersionUID = 5605080331607472920L;
    public static final String ID = "word";
    @ModelField
    private final String word;
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

    public WordTable() {
        this.word = NONE;
        this.userName = null;
        this.createTime = null;
        this.updateTime = null;
        this.enabled = true;
        this.visible = true;
        this.flags = 0;
    }

    public WordTable(
            @Nonnull String word,
            String userName,
            LocalDateTime createTime,
            LocalDateTime updateTime,
            boolean enabled,
            boolean visible,
            int flags) {
        this.word = word;
        this.userName = userName;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.enabled = enabled;
        this.visible = visible;
        this.flags = flags;
    }

    public static WordTable from(Row row) {
        return new WordTable(
                row.getString(ID),
                row.getString("user_name"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public static Uni<WordTable> findById(PgPool client, String id) {
        return client
                .preparedQuery(SELECT_FROM_DICTIONARY_WORD_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? WordTable.from(iterator.next()) : null);
    }

    public static Multi<WordTable> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_DICTIONARY_WORD_ORDER_BY_ID_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(WordTable::from);

    }

    public static Multi<WordTable> findRange(PgPool client, long offset, long limit) {
        return client
                .preparedQuery(SELECT_ALL_FROM_DICTIONARY_WORD_ORDER_BY_WORD_ASC_OFFSET_LIMIT)
                .execute(Tuple.of(offset, limit))
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(WordTable::from);

    }

    public static Uni<String> delete(PgPool client, String id) {
        return client.withTransaction(
                connection -> connection.preparedQuery(DELETE_FROM_DICTIONARY_WORD_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getString(ID)));
    }

    public static Uni<Long> count(PgPool client) {
        return client
                .preparedQuery(COUNT_DICTIONARY_WORD)
                .execute()
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong("count"));
    }

    public static WordTable.Builder builder() {
        return new WordTable.Builder();
    }

    public Uni<String> insert(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(INSERT_INTO_DICTIONARY_WORD)
                        .execute(Tuple.tuple(listOf()))
                        .onItem()
                        .transform(RowSet::iterator)
                        .onItem()
                        .transform(iterator -> iterator.hasNext() ? iterator.next().getString(ID) : null));
    }

    public Uni<String> update(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(UPDATE_DICTIONARY_WORD_WHERE_ID_$1)
                        .execute(Tuple.tuple(listOf()))
                        .onItem()
                        .transform(pgRowSet -> pgRowSet.iterator().next().getString(ID)));
    }

    private List<Object> listOf() {
        return Arrays.asList(word, userName, enabled, visible, flags);
    }

    @JsonIgnore
    public String getId() {
        return word;
    }

    public String getWord() {
        return word;
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
        var that = (WordTable) o;
        return enabled == that.enabled
                && visible == that.visible
                && flags == that.flags
                && Objects.equals(word, that.word)
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
        return Objects.hash(word, userName, enabled, visible, flags);
    }

    @Override
    public String toString() {
        return "Word{" +
                "word='" + word + '\'' +
                ", userName='" + userName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", enabled=" + enabled +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private String word;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private boolean visible;
        private int flags;

        private Builder() {
            this.enabled = true;
        }

        public Builder id(@Nonnull String id) {
            this.word = id;
            return this;
        }

        public Builder word(@Nonnull String word) {
            this.word = word;
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

        public WordTable build() {
            return new WordTable(word, userName, createTime, updateTime, enabled, visible, flags);
        }
    }
}
