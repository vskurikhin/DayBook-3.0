/*
 * This file was last modified at 2021.12.06 18:10 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabel.java
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

public class TagLabel implements Serializable {

    private static final long serialVersionUID = 7430969393917118489L;

    private String id;

    private String label;

    private String userName;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private Boolean enabled;

    private Boolean visible;

    private Integer flags;

    public TagLabel() {}

    public TagLabel(
            String id,
            String label,
            String userName,
            LocalDateTime createTime,
            LocalDateTime updateTime,
            Boolean enabled,
            Boolean visible,
            Integer flags) {
        this.id = id;
        this.label = label;
        this.userName = userName;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.enabled = enabled;
        this.visible = visible;
        this.flags = flags;
    }

    public static final String SELECT_FROM_DICTIONARY_TAG_LABEL_WHERE_ID_$1
            = "SELECT id, label, user_name, create_time, update_time, enabled, visible, flags "
            + "  FROM dictionary.tag_label "
            + " WHERE id = $1";

    public static final String SELECT_ALL_FROM_DICTIONARY_TAG_LABEL_ORDER_BY_ID_ASC
            = "SELECT id, label, user_name, create_time, update_time, enabled, visible, flags "
            + "  FROM dictionary.tag_label "
            + " ORDER BY id ASC";

    public static final String INSERT_INTO_DICTIONARY_TAG_LABEL
            = "INSERT INTO dictionary.tag_label "
            + " (id, label, user_name, create_time, update_time, enabled, visible, flags) "
            + " VALUES "
            + " ($1, $2, $3, now(), now(), $4, $5, $6) RETURNING id";

    public static final String UPDATE_DICTIONARY_TAG_LABEL_WHERE_ID_$1
            = "UPDATE dictionary.tag_label "
            + " SET "
            + "  label = $2,"
            + "  user_name = $3, "
            + "  create_time = $4, "
            + "  update_time = $5,"
            + "  enabled = $6, "
            + "  visible = $7, "
            + "  flags = $8 "
            + " WHERE id = $1 "
            + " RETURNING id";

    public static TagLabel from(Row row) {
        return new TagLabel(
                row.getString("id"),
                row.getString("label"),
                row.getString("user_name"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public static Uni<TagLabel> findById(PgPool client, String id) {
        return client.preparedQuery(SELECT_FROM_DICTIONARY_TAG_LABEL_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? TagLabel.from(iterator.next()) : null);
    }

    public static Multi<TagLabel> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_DICTIONARY_TAG_LABEL_ORDER_BY_ID_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(TagLabel::from);

    }

    public Uni<String> insert(PgPool client) {
        return client.preparedQuery(INSERT_INTO_DICTIONARY_TAG_LABEL)
                .execute(Tuple.of(id, label, userName, enabled, visible, flags))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? iterator.next().getString("id") : null);
    }

    public Uni<String> update(PgPool client) {
        updateTime = LocalDateTime.now();
        return client.preparedQuery(UPDATE_DICTIONARY_TAG_LABEL_WHERE_ID_$1)
                .execute(Tuple.of(List.of(id, label, userName, createTime, updateTime, enabled, visible, flags)))
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getString("id"));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
        if (!(o instanceof TagLabel)) return false;

        TagLabel tagLabel = (TagLabel) o;

        if (id != null ? !id.equals(tagLabel.id) : tagLabel.id != null) return false;
        if (label != null ? !label.equals(tagLabel.label) : tagLabel.label != null) return false;
        if (userName != null ? !userName.equals(tagLabel.userName) : tagLabel.userName != null) return false;
        if (createTime != null ? !createTime.equals(tagLabel.createTime) : tagLabel.createTime != null) return false;
        if (updateTime != null ? !updateTime.equals(tagLabel.updateTime) : tagLabel.updateTime != null) return false;
        if (enabled != null ? !enabled.equals(tagLabel.enabled) : tagLabel.enabled != null) return false;
        if (visible != null ? !visible.equals(tagLabel.visible) : tagLabel.visible != null) return false;
        return flags != null ? flags.equals(tagLabel.flags) : tagLabel.flags == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (label != null ? label.hashCode() : 0);
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
        return "TagLabel{" +
                "id='" + id + '\'' +
                ", label='" + label + '\'' +
                ", userName='" + userName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", enabled=" + enabled +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }
}
