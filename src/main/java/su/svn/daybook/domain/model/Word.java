/*
 * This file was last modified at 2021.12.06 19:31 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * Word.java
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
public final class Word implements StringIdentification, Marked, Owned, TimeUpdated, Serializable {

    @Serial
    private static final long serialVersionUID = 1026290511346629702L;

    public static final String NONE = "__NONE__";

    private final String word;

    private final String userName;

    private final LocalDateTime createTime;

    private final LocalDateTime updateTime;

    private final boolean enabled;

    private final boolean visible;

    private final int flags;

    public static final String SELECT_FROM_DICTIONARY_WORD_WHERE_WORD_$1 = """
            SELECT word, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.word
             WHERE word = $1
            """;

    public static final String SELECT_ALL_FROM_DICTIONARY_WORD_ORDER_BY_WORD_ASC = """
            SELECT word, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.word
             ORDER BY word ASC
            """;

    public static final String INSERT_INTO_DICTIONARY_WORD = """
            INSERT INTO dictionary.word
             (word, user_name, create_time, update_time, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6, $7)
             RETURNING word
            """;

    public static final String UPDATE_DICTIONARY_WORD_WHERE_WORD_$1 = """
           UPDATE dictionary.word SET
             user_name = $2,
             create_time = $3,
             update_time = $4,
             enabled = $5,
             visible = $6,
             flags = $7
            WHERE word = $1
            RETURNING word
           """;

    public static final String DELETE_FROM_DICTIONARY_WORD_WHERE_WORD_$1 = """
            DELETE FROM dictionary.word
             WHERE word = $1
             RETURNING word
            """;

    public static Word from(Row row) {
        return new Word(
                row.getString("word"),
                row.getString("user_name"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public static Uni<Word> findByWord(PgPool client, String word) {
        return client.preparedQuery(SELECT_FROM_DICTIONARY_WORD_WHERE_WORD_$1)
                .execute(Tuple.of(word))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? Word.from(iterator.next()) : null);
    }

    public static Multi<Word> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_DICTIONARY_WORD_ORDER_BY_WORD_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(Word::from);

    }

    public Uni<String> insert(PgPool client) {
        return client.preparedQuery(INSERT_INTO_DICTIONARY_WORD)
                .execute(Tuple.tuple(listOf()))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? iterator.next().getString("word") : null);
    }

    public Uni<String> update(PgPool client) {
        return client.preparedQuery(UPDATE_DICTIONARY_WORD_WHERE_WORD_$1)
                .execute(Tuple.tuple(listOf()))
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getString("word"));
    }

    public static Uni<String> delete(PgPool client, String word) {
        return client.preparedQuery(DELETE_FROM_DICTIONARY_WORD_WHERE_WORD_$1)
                .execute(Tuple.of(word))
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getString("word"));
    }

    private List<Object> listOf() {
        return Arrays.asList(word, userName, createTime, updateTime, enabled, visible, flags);
    }

    public Word() {
        this.word = NONE;
        this.userName = null;
        this.createTime = null;
        this.updateTime = null;
        this.enabled = false;
        this.visible = true;
        this.flags = 0;
    }

    public Word(
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
        Word word1 = (Word) o;
        return enabled == word1.enabled
                && visible == word1.visible
                && flags == word1.flags
                && Objects.equals(word, word1.word)
                && Objects.equals(userName, word1.userName);
    }

    @Override
    public int hashCode() {
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

    public static Word.Builder builder() {
        return new Word.Builder();
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
        }

        public Builder withWord(@Nonnull String word) {
            this.word = word;
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

        public Word build() {
            return new Word(word, userName, createTime, updateTime, enabled, visible, flags);
        }
    }
}
