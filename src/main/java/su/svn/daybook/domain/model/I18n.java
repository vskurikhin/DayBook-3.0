/*
 * This file was last modified at 2021.12.15 12:06 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * I18n.java
 * $Id$
 */

package su.svn.daybook.domain.model;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.RowSet;
import io.vertx.mutiny.sqlclient.Tuple;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public class I18n implements Serializable {

    private static final long serialVersionUID = -1065369508632115811L;

    private Long id;

    private Long languageId;

    private String message;

    private String translation;

    private String userName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean enabled;

    private Boolean visible;

    private Integer flags;

    public static final String SELECT_FROM_DICTIONARY_I18N_WHERE_ID_$1
            = "SELECT id, language_id, message, translation, user_name, create_time, update_time, enabled, visible, flags "
            + "  FROM dictionary.i18n "
            + " WHERE id = $1";

    public static final String SELECT_ALL_FROM_DICTIONARY_I18N_ORDER_BY_ID_ASC
            = "SELECT id, language_id, message, translation, user_name, create_time, update_time, enabled, visible, flags "
            + "  FROM dictionary.i18n "
            + " ORDER BY id ASC";

    public static final String INSERT_INTO_DICTIONARY_I18N
            = "INSERT INTO dictionary.i18n "
            + " (id, language_id, message, translation, user_name, create_time, update_time, enabled, visible, flags) "
            + " VALUES "
            + " ($1, $2, $3, $4, $5, $6, $7, $8, $9, $10) "
            + " RETURNING id";

    public static final String UPDATE_DICTIONARY_I18N_WHERE_ID_$1
            = "UPDATE dictionary.i18n "
            + " SET "
            + "  language_id = $2,"
            + "  message = $3,"
            + "  translation = $4,"
            + "  user_name = $5, "
            + "  create_time = $6, "
            + "  update_time = $7, "
            + "  enabled = $8, "
            + "  visible = $9, "
            + "  flags = $10 "
            + " WHERE id = $1 "
            + " RETURNING id";

    public static final String DELETE_FROM_DICTIONARY_I18N_WHERE_ID_$1
            = "DELETE FROM dictionary.i18n "
            + " WHERE id = $1 "
            + " RETURNING id";

    public static Uni<I18n> findById(PgPool client, Long id) {
        return client.preparedQuery(SELECT_FROM_DICTIONARY_I18N_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? I18n.from(iterator.next()) : null);
    }

    public static Multi<I18n> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_DICTIONARY_I18N_ORDER_BY_ID_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(I18n::from);

    }

    public Uni<Long> insert(PgPool client) {
        return client.preparedQuery(INSERT_INTO_DICTIONARY_I18N)
                .execute(Tuple.of(listOf()))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? iterator.next().getLong("id") : null);
    }

    public Uni<Long> update(PgPool client) {
        updateTime = LocalDateTime.now();
        return client.preparedQuery(UPDATE_DICTIONARY_I18N_WHERE_ID_$1)
                .execute(Tuple.of(listOf()))
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong("id"));
    }

    public Uni<Long> delete(PgPool client) {
        return client.preparedQuery(DELETE_FROM_DICTIONARY_I18N_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong("id"));
    }

    private List<?> listOf() {
        return List.of(id, languageId, message, translation, userName, createTime, updateTime, enabled, visible, flags);
    }

    public I18n() {}

    public I18n(
            Long id,
            Long languageId,
            String message,
            String translation,
            String userName,
            LocalDateTime createTime,
            LocalDateTime updateTime,
            Boolean enabled,
            Boolean visible,
            Integer flags) {
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

    public static I18n from(Row row) {
        return new I18n(
                row.getLong("id"),
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Long languageId) {
        this.languageId = languageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTranslation() {
        return translation;
    }

    public void setTranslation(String translation) {
        this.translation = translation;
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
        if (o == null || getClass() != o.getClass()) return false;

        I18n i18n = (I18n) o;

        if (id != null ? !id.equals(i18n.id) : i18n.id != null) return false;
        if (languageId != null ? !languageId.equals(i18n.languageId) : i18n.languageId != null) return false;
        if (message != null ? !message.equals(i18n.message) : i18n.message != null) return false;
        if (translation != null ? !translation.equals(i18n.translation) : i18n.translation != null) return false;
        if (userName != null ? !userName.equals(i18n.userName) : i18n.userName != null) return false;
        if (createTime != null ? !createTime.equals(i18n.createTime) : i18n.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(i18n.updateTime) : i18n.updateTime != null) return false;
        if (enabled != null ? !enabled.equals(i18n.enabled) : i18n.enabled != null) return false;
        if (visible != null ? !visible.equals(i18n.visible) : i18n.visible != null) return false;
        return flags != null ? flags.equals(i18n.flags) : i18n.flags == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (languageId != null ? languageId.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (translation != null ? translation.hashCode() : 0);
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
        return "I18n{" +
                "id=" + id +
                ", languageId=" + languageId +
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

    public static Builder builder() {
        return new I18n.Builder();
    }

    public static final class Builder {
        private Long id;
        private Long languageId;
        private String message;
        private String translation;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private Boolean enabled;
        private Boolean visible;
        private Integer flags;

        private Builder() {
        }

        public static Builder anI18n() {
            return new Builder();
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withLanguageId(Long languageId) {
            this.languageId = languageId;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder withTranslation(String translation) {
            this.translation = translation;
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

        public Builder but() {
            return anI18n().withId(id)
                    .withLanguageId(languageId)
                    .withMessage(message)
                    .withTranslation(translation)
                    .withUserName(userName)
                    .withCreateTime(createTime)
                    .withUpdateTime(updateTime)
                    .withEnabled(enabled)
                    .withVisible(visible)
                    .withFlags(flags);
        }

        public I18n build() {
            I18n i18n = new I18n();
            i18n.setId(id);
            i18n.setLanguageId(languageId);
            i18n.setMessage(message);
            i18n.setTranslation(translation);
            i18n.setUserName(userName);
            i18n.setCreateTime(createTime);
            i18n.setUpdateTime(updateTime);
            i18n.setEnabled(enabled);
            i18n.setVisible(visible);
            i18n.setFlags(flags);
            return i18n;
        }
    }
}
