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

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Vocabulary implements Serializable {

    private static final long serialVersionUID = -71735002217330331L;

    private Long id;

    private Long wordId;

    private String value;

    private String userName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean enabled;

    private Boolean visible;

    private Integer flags;

    public static final String SELECT_FROM_DICTIONARY_VOCABULARY_WHERE_ID_$1
            = "SELECT id, word_id, value, user_name, create_time, update_time, enabled, visible, flags "
            + "  FROM dictionary.vocabulary "
            + " WHERE id = $1";

    public static final String SELECT_ALL_FROM_DICTIONARY_VOCABULARY_ORDER_BY_ID_ASC
            = "SELECT id, word_id, value, user_name, create_time, update_time, enabled, visible, flags "
            + "  FROM dictionary.vocabulary "
            + " ORDER BY id ASC";

    public static final String INSERT_INTO_DICTIONARY_VOCABULARY
            = "INSERT INTO dictionary.vocabulary "
            + " (id, word_id, value, user_name, create_time, update_time, enabled, visible, flags) "
            + " VALUES "
            + " ($1, $2, $3, $4, $5, $6, $7, $8, $9) "
            + " RETURNING id";

    public static final String UPDATE_DICTIONARY_VOCABULARY_WHERE_ID_$1
            = "UPDATE dictionary.vocabulary "
            + " SET "
            + "  word_id = $2,"
            + "  value = $3,"
            + "  user_name = $4, "
            + "  create_time = $5, "
            + "  update_time = $6,"
            + "  enabled = $7, "
            + "  visible = $8, "
            + "  flags = $9 "
            + " WHERE id = $1 "
            + " RETURNING id";

    public static final String DELETE_FROM_DICTIONARY_VOCABULARY_WHERE_ID_$1
            = "DELETE FROM dictionary.vocabulary "
            + " WHERE id = $1 "
            + " RETURNING id";

    public static Vocabulary from(Row row) {
        return new Vocabulary(
                row.getLong("id"),
                row.getLong("word_id"),
                row.getString("value"),
                row.getString("user_name"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public static Uni<Vocabulary> findById(PgPool client, Long id) {
        return client.preparedQuery(SELECT_FROM_DICTIONARY_VOCABULARY_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? Vocabulary.from(iterator.next()) : null);
    }

    public static Multi<Vocabulary> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_DICTIONARY_VOCABULARY_ORDER_BY_ID_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(Vocabulary::from);

    }

    public Uni<Long> insert(PgPool client) {
        return client.preparedQuery(INSERT_INTO_DICTIONARY_VOCABULARY)
                .execute(Tuple.of(listOf()))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? iterator.next().getLong("id") : null);
    }

    public Uni<Long> update(PgPool client) {
        updateTime = LocalDateTime.now();
        return client.preparedQuery(UPDATE_DICTIONARY_VOCABULARY_WHERE_ID_$1)
                .execute(Tuple.of(listOf()))
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong("id"));
    }

    public static Uni<Long> delete(PgPool client, Long id) {
        return client.preparedQuery(DELETE_FROM_DICTIONARY_VOCABULARY_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong("id"));
    }

    private List<?> listOf() {
        return List.of(id, wordId, value, userName, createTime, updateTime, enabled, visible, flags);
    }

    public Vocabulary() {}

    public Vocabulary(
            Long id,
            Long wordId,
            String value,
            String userName,
            LocalDateTime createTime,
            LocalDateTime updateTime,
            Boolean enabled,
            Boolean visible,
            Integer flags) {
        this.id = id;
        this.wordId = wordId;
        this.value = value;
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

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWordId() {
        return wordId;
    }

    public void setWordId(Long wordId) {
        this.wordId = wordId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
        if (!(o instanceof Vocabulary)) return false;

        Vocabulary that = (Vocabulary) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (wordId != null ? !wordId.equals(that.wordId) : that.wordId != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;
        if (userName != null ? !userName.equals(that.userName) : that.userName != null) return false;
        if (createTime != null ? !createTime.equals(that.createTime) : that.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(that.updateTime) : that.updateTime != null) return false;
        if (enabled != null ? !enabled.equals(that.enabled) : that.enabled != null) return false;
        if (visible != null ? !visible.equals(that.visible) : that.visible != null) return false;
        return flags != null ? flags.equals(that.flags) : that.flags == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (wordId != null ? wordId.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
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
        return "Vocabulary{" +
                "id=" + id +
                ", wordId='" + wordId + '\'' +
                ", value='" + value + '\'' +
                ", userName='" + userName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", enabled=" + enabled +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static Vocabulary.Builder builder() {
        return new Vocabulary.Builder();
    }

    public static final class Builder {
        private Long id;
        private Long wordId;
        private String value;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private Boolean enabled;
        private Boolean visible;
        private Integer flags;

        private Builder() {
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withWordId(Long wordId) {
            this.wordId = wordId;
            return this;
        }

        public Builder withValue(String value) {
            this.value = value;
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

        public Vocabulary build() {
            Vocabulary vocabulary = new Vocabulary();
            vocabulary.setId(id);
            vocabulary.setWordId(wordId);
            vocabulary.setValue(value);
            vocabulary.setUserName(userName);
            vocabulary.setCreateTime(createTime);
            vocabulary.setUpdateTime(updateTime);
            vocabulary.setEnabled(enabled);
            vocabulary.setVisible(visible);
            vocabulary.setFlags(flags);
            return vocabulary;
        }
    }
}
