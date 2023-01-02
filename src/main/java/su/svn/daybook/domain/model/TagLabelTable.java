/*
 * This file was last modified at 2022.01.12 22:58 by Victor N. Skurikhin.
 * This is free and unencumbered software released into the public domain.
 * For more information, please refer to <http://unlicense.org>
 * TagLabel.java
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
import su.svn.daybook.utils.StringUtil;

import javax.annotation.Nonnull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class TagLabelTable implements StringIdentification, Marked, Owned, TimeUpdated, Serializable {

    public static final String NONE = "519797ec-dba2-452b-b600-eb9dde6b57b8";
    public static final String SELECT_FROM_DICTIONARY_TAG_LABEL_WHERE_ID_$1 = """
            SELECT id, label, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.tag_label
             WHERE id = $1 AND enabled
            """;
    public static final String SELECT_ALL_FROM_DICTIONARY_TAG_LABEL_ORDER_BY_ID_ASC = """
            SELECT id, label, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.tag_label
             WHERE enabled
             ORDER BY id ASC
            """;
    public static final String SELECT_ALL_FROM_DICTIONARY_TAG_LABEL_ORDER_BY_ID_ASC_OFFSET_LIMIT = """
            SELECT id, label, user_name, create_time, update_time, enabled, visible, flags
              FROM dictionary.tag_label
             WHERE enabled
             ORDER BY id ASC OFFSET $1 LIMIT $2
            """;
    public static final String INSERT_INTO_DICTIONARY_TAG_LABEL = """
            INSERT INTO dictionary.tag_label
             (id, label, user_name, enabled, visible, flags)
             VALUES
             ($1, $2, $3, $4, $5, $6)
             RETURNING id
            """;
    public static final String INSERT_INTO_DICTIONARY_TAG_LABEL_DEFAULT_ID = """
            INSERT INTO dictionary.tag_label
             (id, label, user_name, enabled, visible, flags)
             VALUES
             (DEFAULT, $1, $2, $3, $4, $5)
             RETURNING id
            """;
    public static final String UPDATE_DICTIONARY_TAG_LABEL_WHERE_ID_$1 = """
            UPDATE dictionary.tag_label SET
              label = $2,
              user_name = $3,
              enabled = $4,
              visible = $5,
              flags = $6
             WHERE id = $1
             RETURNING id
            """;
    public static final String DELETE_FROM_DICTIONARY_TAG_LABEL_WHERE_ID_$1 = """
            DELETE FROM dictionary.tag_label
             WHERE id = $1
             RETURNING id
            """;
    public static final String COUNT_DICTIONARY_TAG_LABEL = "SELECT count(*) FROM dictionary.tag_label";
    @Serial
    private static final long serialVersionUID = 2947209495026660348L;
    public static final String ID = "id";
    @ModelField
    private final String id;
    @ModelField
    private final String label;
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

    public TagLabelTable() {
        this.id = null;
        this.label = NONE;
        this.userName = null;
        this.createTime = null;
        this.updateTime = null;
        this.enabled = true;
        this.visible = true;
        this.flags = 0;
    }

    public TagLabelTable(
            String id,
            @Nonnull String label,
            String userName,
            LocalDateTime createTime,
            LocalDateTime updateTime,
            boolean enabled,
            boolean visible,
            int flags) {
        this.id = id;
        this.label = label;
        this.userName = userName;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.enabled = enabled;
        this.visible = visible;
        this.flags = flags;
    }

    public static TagLabelTable from(Row row) {
        return new TagLabelTable(
                row.getString(ID),
                row.getString("label"),
                row.getString("user_name"),
                row.getLocalDateTime("create_time"),
                row.getLocalDateTime("update_time"),
                row.getBoolean("enabled"),
                row.getBoolean("visible"),
                row.getInteger("flags")
        );
    }

    public static Multi<TagLabelTable> findAll(PgPool client) {
        return client
                .query(SELECT_ALL_FROM_DICTIONARY_TAG_LABEL_ORDER_BY_ID_ASC)
                .execute()
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(TagLabelTable::from);

    }

    public static Uni<TagLabelTable> findById(PgPool client, String id) {
        return client
                .preparedQuery(SELECT_FROM_DICTIONARY_TAG_LABEL_WHERE_ID_$1)
                .execute(Tuple.of(id))
                .onItem()
                .transform(RowSet::iterator)
                .onItem()
                .transform(iterator -> iterator.hasNext() ? TagLabelTable.from(iterator.next()) : null);
    }

    public static Multi<TagLabelTable> findRange(PgPool client, long offset, long limit) {
        return client
                .preparedQuery(SELECT_ALL_FROM_DICTIONARY_TAG_LABEL_ORDER_BY_ID_ASC_OFFSET_LIMIT)
                .execute(Tuple.of(offset, limit))
                .onItem()
                .transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem()
                .transform(TagLabelTable::from);
    }

    public static Uni<String> delete(PgPool client, String id) {
        return client.withTransaction(
                connection -> connection.preparedQuery(DELETE_FROM_DICTIONARY_TAG_LABEL_WHERE_ID_$1)
                        .execute(Tuple.of(id))
                        .onItem()
                        .transform(pgRowSet -> pgRowSet.iterator().next().getString(ID)));
    }

    public static Uni<Long> count(PgPool client) {
        return client
                .preparedQuery(COUNT_DICTIONARY_TAG_LABEL)
                .execute()
                .onItem()
                .transform(pgRowSet -> pgRowSet.iterator().next().getLong("count"));
    }

    public static TagLabelTable.Builder builder() {
        return new TagLabelTable.Builder();
    }

    public Uni<String> insert(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(caseInsertSql())
                        .execute(caseInsertTuple())
                        .onItem()
                        .transform(RowSet::iterator)
                        .onItem()
                        .transform(iterator -> iterator.hasNext() ? iterator.next().getString(ID) : null));
    }

    public Uni<String> update(PgPool client) {
        return client.withTransaction(
                connection -> connection.preparedQuery(UPDATE_DICTIONARY_TAG_LABEL_WHERE_ID_$1)
                        .execute(Tuple.tuple(listOf()))
                        .onItem()
                        .transform(pgRowSet -> pgRowSet.iterator().next().getString(ID)));
    }

    private String caseInsertSql() {
        return id != null ? INSERT_INTO_DICTIONARY_TAG_LABEL : INSERT_INTO_DICTIONARY_TAG_LABEL_DEFAULT_ID;
    }

    private Tuple caseInsertTuple() {
        return id != null
                ? Tuple.of(StringUtil.generateTagId(id), label, userName, enabled, visible, flags)
                : Tuple.of(label, userName, enabled, visible, flags);
    }

    private List<Object> listOf() {
        return Arrays.asList(id, label, userName, enabled, visible, flags);
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
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
        var that = (TagLabelTable) o;
        return enabled == that.enabled
                && visible == that.visible
                && flags == that.flags
                && Objects.equals(id, that.id)
                && Objects.equals(label, that.label)
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
        return Objects.hash(id, label, userName, enabled, visible, flags);
    }

    @Override
    public String toString() {
        return "TagLabel{" +
                "id=" + id +
                ", label='" + label + '\'' +
                ", userName='" + userName + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", enabled=" + enabled +
                ", visible=" + visible +
                ", flags=" + flags +
                '}';
    }

    public static final class Builder {
        private String id;
        private String label;
        private String userName;
        private LocalDateTime createTime;
        private LocalDateTime updateTime;
        private boolean enabled;
        private boolean visible;
        private int flags;

        private Builder() {
        }

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder label(@Nonnull String label) {
            this.label = label;
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

        public TagLabelTable build() {
            return new TagLabelTable(id, label, userName, createTime, updateTime, enabled, visible, flags);
        }
    }
}
