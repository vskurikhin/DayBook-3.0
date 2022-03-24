/*
 * This file was last modified at 2022.03.24 13:26 by Victor N. Skurikhin.
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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Word implements Serializable {

    private static final long serialVersionUID = 1026290511346629702L;

    private String word;

    private String userName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean enabled;

    private Boolean visible;

    private Integer flags;

    public static final String SELECT_FROM_DICTIONARY_WORD_WHERE_WORD_$1
            = "SELECT word, user_name, create_time, update_time, enabled, visible, flags "
            + "  FROM dictionary.word "
            + " WHERE word = $1";

    public static final String SELECT_ALL_FROM_DICTIONARY_WORD_ORDER_BY_WORD_ASC
            = "SELECT word, user_name, create_time, update_time, enabled, visible, flags "
            + "  FROM dictionary.word "
            + " ORDER BY word ASC";

    public static final String INSERT_INTO_DICTIONARY_WORD
            = "INSERT INTO dictionary.word "
            + " (word, user_name, create_time, update_time, enabled, visible, flags) "
            + " VALUES "
            + " ($1, $2, $3, $4, $5, $6, $7) "
            + " RETURNING word";

    public static final String UPDATE_DICTIONARY_WORD_WHERE_WORD_$1
            = "UPDATE dictionary.word "
            + " SET "
            + "  user_name = $2, "
            + "  create_time = $3, "
            + "  update_time = $4,"
            + "  enabled = $5, "
            + "  visible = $6, "
            + "  flags = $7 "
            + " WHERE word = $1 "
            + " RETURNING word";

    public static final String DELETE_FROM_DICTIONARY_WORD_WHERE_WORD_$1
            = "DELETE FROM dictionary.word "
            + " WHERE word = $1 "
            + " RETURNING word";

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
                .execute(Tuple.of(listOf()))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? iterator.next().getString("word") : null);
    }

    public Uni<String> update(PgPool client) {
        updateTime = LocalDateTime.now();
        return client.preparedQuery(UPDATE_DICTIONARY_WORD_WHERE_WORD_$1)
                .execute(Tuple.of(listOf()))
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getString("word"));
    }

    public static Uni<String> delete(PgPool client, String word) {
        return client.preparedQuery(DELETE_FROM_DICTIONARY_WORD_WHERE_WORD_$1)
                .execute(Tuple.of(word))
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getString("word"));
    }

    private List<?> listOf() {
        return List.of(word, userName, createTime, updateTime, enabled, visible, flags);
    }

    public Word() {}

    public Word(
            String word,
            String userName,
            LocalDateTime createTime,
            LocalDateTime updateTime,
            Boolean enabled,
            Boolean visible,
            Integer flags) {
        this.word = word;
        this.userName = userName;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.enabled = enabled;
        this.visible = visible;
        this.flags = flags;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Integer getFlags() {
        return flags;
    }

    public void setFlags(Integer flags) {
        this.flags = flags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Word)) return false;

        Word codifier = (Word) o;

        if (word != null ? !word.equals(codifier.word) : codifier.word != null) return false;
        if (userName != null ? !userName.equals(codifier.userName) : codifier.userName != null) return false;
        if (createTime != null ? !createTime.equals(codifier.createTime) : codifier.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(codifier.updateTime) : codifier.updateTime != null) return false;
        if (enabled != null ? !enabled.equals(codifier.enabled) : codifier.enabled != null) return false;
        if (visible != null ? !visible.equals(codifier.visible) : codifier.visible != null) return false;
        return flags != null ? flags.equals(codifier.flags) : codifier.flags == null;
    }

    @Override
    public int hashCode() {
        int result = word != null ? word.hashCode() : 0;
        result = 31 * result + (userName != null ? userName.hashCode() : 0);
        result = 31 * result + (createTime != null ? createTime.hashCode() : 0);
        result = 31 * result + (updateTime != null ? updateTime.hashCode() : 0);
        result = 31 * result + (enabled != null ? enabled.hashCode() : 0);
        result = 31 * result + (visible != null ? visible.hashCode() : 0);
        result = 31 * result + (flags != null ? flags.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Codifier{" +
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
        private Boolean enabled;
        private Boolean visible;
        private Integer flags;

        private Builder() {
        }

        public Builder withWord(String word) {
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

        public Builder withEnabled(Boolean enabled) {
            this.enabled = enabled;
            return this;
        }

        public Builder withVisible(Boolean visible) {
            this.visible = visible;
            return this;
        }

        public Builder withFlags(Integer flags) {
            this.flags = flags;
            return this;
        }

        public Word build() {
            Word codifier = new Word();
            codifier.setWord(word);
            codifier.setUserName(userName);
            codifier.setCreateTime(createTime);
            codifier.setUpdateTime(updateTime);
            codifier.setEnabled(enabled);
            codifier.setVisible(visible);
            codifier.setFlags(flags);
            return codifier;
        }
    }
}
